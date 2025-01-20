package net.minecraft.server.management;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2025 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class ItemInWorldManager {
	public World theWorld;
	public EntityPlayerMP thisPlayerMP;
	private WorldSettings.GameType gameType = WorldSettings.GameType.NOT_SET;
	private boolean isDestroyingBlock;
	private int initialDamage;
	private BlockPos field_180240_f = BlockPos.ORIGIN;
	private int curblockDamage;
	private boolean receivedFinishDiggingPacket;
	private BlockPos field_180241_i = BlockPos.ORIGIN;
	private int initialBlockDamage;
	private int durabilityRemainingOnBlock = -1;

	public ItemInWorldManager(World worldIn) {
		this.theWorld = worldIn;
	}

	public void setGameType(WorldSettings.GameType type) {
		this.gameType = type;
		type.configurePlayerCapabilities(this.thisPlayerMP.capabilities);
		this.thisPlayerMP.sendPlayerAbilities();
		this.thisPlayerMP.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S38PacketPlayerListItem(
				S38PacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] { this.thisPlayerMP }));
	}

	public WorldSettings.GameType getGameType() {
		return this.gameType;
	}

	public boolean survivalOrAdventure() {
		return this.gameType.isSurvivalOrAdventure();
	}

	/**+
	 * Get if we are in creative game mode.
	 */
	public boolean isCreative() {
		return this.gameType.isCreative();
	}

	/**+
	 * if the gameType is currently NOT_SET then change it to par1
	 */
	public void initializeGameType(WorldSettings.GameType type) {
		if (this.gameType == WorldSettings.GameType.NOT_SET) {
			this.gameType = type;
		}

		this.setGameType(this.gameType);
	}

	public void updateBlockRemoving() {
		++this.curblockDamage;
		if (this.receivedFinishDiggingPacket) {
			int i = this.curblockDamage - this.initialBlockDamage;
			Block block = this.theWorld.getBlockState(this.field_180241_i).getBlock();
			if (block.getMaterial() == Material.air) {
				this.receivedFinishDiggingPacket = false;
			} else {
				float f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj,
						this.field_180241_i) * (float) (i + 1);
				int j = (int) (f * 10.0F);
				if (j != this.durabilityRemainingOnBlock) {
					this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180241_i, j);
					this.durabilityRemainingOnBlock = j;
				}

				if (f >= 1.0F) {
					this.receivedFinishDiggingPacket = false;
					this.tryHarvestBlock(this.field_180241_i);
				}
			}
		} else if (this.isDestroyingBlock) {
			Block block1 = this.theWorld.getBlockState(this.field_180240_f).getBlock();
			if (block1.getMaterial() == Material.air) {
				this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
				this.durabilityRemainingOnBlock = -1;
				this.isDestroyingBlock = false;
			} else {
				int k = this.curblockDamage - this.initialDamage;
				float f1 = block1.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj,
						this.field_180241_i) * (float) (k + 1);
				int l = (int) (f1 * 10.0F);
				if (l != this.durabilityRemainingOnBlock) {
					this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, l);
					this.durabilityRemainingOnBlock = l;
				}
			}
		}

	}

	/**+
	 * If not creative, it calls sendBlockBreakProgress until the
	 * block is broken first. tryHarvestBlock can also be the result
	 * of this call.
	 */
	public void onBlockClicked(BlockPos blockpos, EnumFacing enumfacing) {
		if (this.isCreative()) {
			if (!this.theWorld.extinguishFire((EntityPlayer) null, blockpos, enumfacing)) {
				this.tryHarvestBlock(blockpos);
			}

		} else {
			Block block = this.theWorld.getBlockState(blockpos).getBlock();
			if (this.gameType.isAdventure()) {
				if (this.gameType == WorldSettings.GameType.SPECTATOR) {
					return;
				}

				if (!this.thisPlayerMP.isAllowEdit()) {
					ItemStack itemstack = this.thisPlayerMP.getCurrentEquippedItem();
					if (itemstack == null) {
						return;
					}

					if (!itemstack.canDestroy(block)) {
						return;
					}
				}
			}

			this.theWorld.extinguishFire((EntityPlayer) null, blockpos, enumfacing);
			this.initialDamage = this.curblockDamage;
			float f = 1.0F;
			if (block.getMaterial() != Material.air) {
				block.onBlockClicked(this.theWorld, blockpos, this.thisPlayerMP);
				f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, blockpos);
			}

			if (block.getMaterial() != Material.air && f >= 1.0F) {
				this.tryHarvestBlock(blockpos);
			} else {
				this.isDestroyingBlock = true;
				this.field_180240_f = blockpos;
				int i = (int) (f * 10.0F);
				this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), blockpos, i);
				this.durabilityRemainingOnBlock = i;
			}

		}
	}

	public void blockRemoving(BlockPos blockpos) {
		if (blockpos.equals(this.field_180240_f)) {
			int i = this.curblockDamage - this.initialDamage;
			Block block = this.theWorld.getBlockState(blockpos).getBlock();
			if (block.getMaterial() != Material.air) {
				float f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, blockpos)
						* (float) (i + 1);
				if (f >= 0.7F) {
					this.isDestroyingBlock = false;
					this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), blockpos, -1);
					this.tryHarvestBlock(blockpos);
				} else if (!this.receivedFinishDiggingPacket) {
					this.isDestroyingBlock = false;
					this.receivedFinishDiggingPacket = true;
					this.field_180241_i = blockpos;
					this.initialBlockDamage = this.initialDamage;
				}
			}
		}

	}

	/**+
	 * Stops the block breaking process
	 */
	public void cancelDestroyingBlock() {
		this.isDestroyingBlock = false;
		this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
	}

	/**+
	 * Removes a block and triggers the appropriate events
	 */
	private boolean removeBlock(BlockPos pos) {
		IBlockState iblockstate = this.theWorld.getBlockState(pos);
		iblockstate.getBlock().onBlockHarvested(this.theWorld, pos, iblockstate, this.thisPlayerMP);
		boolean flag = this.theWorld.setBlockToAir(pos);
		if (flag) {
			iblockstate.getBlock().onBlockDestroyedByPlayer(this.theWorld, pos, iblockstate);
		}

		return flag;
	}

	/**+
	 * Attempts to harvest a block
	 */
	public boolean tryHarvestBlock(BlockPos blockpos) {
		if (this.gameType.isCreative() && this.thisPlayerMP.getHeldItem() != null
				&& this.thisPlayerMP.getHeldItem().getItem() instanceof ItemSword) {
			return false;
		} else {
			IBlockState iblockstate = this.theWorld.getBlockState(blockpos);
			TileEntity tileentity = this.theWorld.getTileEntity(blockpos);
			if (this.gameType.isAdventure()) {
				if (this.gameType == WorldSettings.GameType.SPECTATOR) {
					return false;
				}

				if (!this.thisPlayerMP.isAllowEdit()) {
					ItemStack itemstack = this.thisPlayerMP.getCurrentEquippedItem();
					if (itemstack == null) {
						return false;
					}

					if (!itemstack.canDestroy(iblockstate.getBlock())) {
						return false;
					}
				}
			}

			this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, blockpos, Block.getStateId(iblockstate));
			boolean flag1 = this.removeBlock(blockpos);
			if (this.isCreative()) {
				this.thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(this.theWorld, blockpos));
			} else {
				ItemStack itemstack1 = this.thisPlayerMP.getCurrentEquippedItem();
				boolean flag = this.thisPlayerMP.canHarvestBlock(iblockstate.getBlock());
				if (itemstack1 != null) {
					itemstack1.onBlockDestroyed(this.theWorld, iblockstate.getBlock(), blockpos, this.thisPlayerMP);
					if (itemstack1.stackSize == 0) {
						this.thisPlayerMP.destroyCurrentEquippedItem();
					}
				}

				if (flag1 && flag) {
					iblockstate.getBlock().harvestBlock(this.theWorld, this.thisPlayerMP, blockpos, iblockstate,
							tileentity);
				}
			}

			return flag1;
		}
	}

	/**+
	 * Attempts to right-click use an item by the given EntityPlayer
	 * in the given World
	 */
	public boolean tryUseItem(EntityPlayer entityplayer, World world, ItemStack itemstack) {
		if (this.gameType == WorldSettings.GameType.SPECTATOR) {
			return false;
		} else {
			int i = itemstack.stackSize;
			int j = itemstack.getMetadata();
			ItemStack itemstack1 = itemstack.useItemRightClick(world, entityplayer);
			if (itemstack1 != itemstack || itemstack1 != null && (itemstack1.stackSize != i
					|| itemstack1.getMaxItemUseDuration() > 0 || itemstack1.getMetadata() != j)) {
				entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = itemstack1;
				if (this.isCreative()) {
					itemstack1.stackSize = i;
					if (itemstack1.isItemStackDamageable()) {
						itemstack1.setItemDamage(j);
					}
				}

				if (itemstack1.stackSize == 0) {
					entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
				}

				if (!entityplayer.isUsingItem()) {
					((EntityPlayerMP) entityplayer).sendContainerToPlayer(entityplayer.inventoryContainer);
				}

				return true;
			} else {
				return false;
			}
		}
	}

	/**+
	 * Activate the clicked on block, otherwise use the held item.
	 */
	public boolean activateBlockOrUseItem(EntityPlayer entityplayer, World world, ItemStack itemstack,
			BlockPos blockpos, EnumFacing enumfacing, float f, float f1, float f2) {
		if (this.gameType == WorldSettings.GameType.SPECTATOR) {
			TileEntity tileentity = world.getTileEntity(blockpos);
			if (tileentity instanceof ILockableContainer) {
				Block block = world.getBlockState(blockpos).getBlock();
				ILockableContainer ilockablecontainer = (ILockableContainer) tileentity;
				if (ilockablecontainer instanceof TileEntityChest && block instanceof BlockChest) {
					ilockablecontainer = ((BlockChest) block).getLockableContainer(world, blockpos);
				}

				if (ilockablecontainer != null) {
					entityplayer.displayGUIChest(ilockablecontainer);
					return true;
				}
			} else if (tileentity instanceof IInventory) {
				entityplayer.displayGUIChest((IInventory) tileentity);
				return true;
			}

			return false;
		} else {
			if (!entityplayer.isSneaking() || entityplayer.getHeldItem() == null) {
				IBlockState iblockstate = world.getBlockState(blockpos);
				if (iblockstate.getBlock().onBlockActivated(world, blockpos, iblockstate, entityplayer, enumfacing, f,
						f1, f2)) {
					return true;
				}
			}

			if (itemstack == null) {
				return false;
			} else if (this.isCreative()) {
				int j = itemstack.getMetadata();
				int i = itemstack.stackSize;
				boolean flag = itemstack.onItemUse(entityplayer, world, blockpos, enumfacing, f, f1, f2);
				itemstack.setItemDamage(j);
				itemstack.stackSize = i;
				return flag;
			} else {
				return itemstack.onItemUse(entityplayer, world, blockpos, enumfacing, f, f1, f2);
			}
		}
	}

	/**+
	 * Sets the world instance.
	 */
	public void setWorld(WorldServer serverWorld) {
		this.theWorld = serverWorld;
	}
}