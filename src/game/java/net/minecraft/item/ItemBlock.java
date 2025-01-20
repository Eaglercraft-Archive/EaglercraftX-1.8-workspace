package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

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
public class ItemBlock extends Item {
	protected final Block block;

	public ItemBlock(Block block) {
		this.block = block;
	}

	/**+
	 * Sets the unlocalized name of this item to the string passed
	 * as the parameter, prefixed by "item."
	 */
	public ItemBlock setUnlocalizedName(String unlocalizedName) {
		super.setUnlocalizedName(unlocalizedName);
		return this;
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float f, float f1, float f2) {
		IBlockState iblockstate = world.getBlockState(blockpos);
		Block blockx = iblockstate.getBlock();
		if (!blockx.isReplaceable(world, blockpos)) {
			blockpos = blockpos.offset(enumfacing);
		}

		if (itemstack.stackSize == 0) {
			return false;
		} else if (!entityplayer.canPlayerEdit(blockpos, enumfacing, itemstack)) {
			return false;
		} else if (world.canBlockBePlaced(this.block, blockpos, false, enumfacing, (Entity) null, itemstack)) {
			int i = this.getMetadata(itemstack.getMetadata());
			IBlockState iblockstate1 = this.block.onBlockPlaced(world, blockpos, enumfacing, f, f1, f2, i,
					entityplayer);
			if (world.setBlockState(blockpos, iblockstate1, 3)) {
				iblockstate1 = world.getBlockState(blockpos);
				if (iblockstate1.getBlock() == this.block) {
					setTileEntityNBT(world, entityplayer, blockpos, itemstack);
					this.block.onBlockPlacedBy(world, blockpos, iblockstate1, entityplayer, itemstack);
				}

				world.playSoundEffect((double) ((float) blockpos.getX() + 0.5F),
						(double) ((float) blockpos.getY() + 0.5F), (double) ((float) blockpos.getZ() + 0.5F),
						this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F,
						this.block.stepSound.getFrequency() * 0.8F);
				--itemstack.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}

	public static boolean setTileEntityNBT(World worldIn, EntityPlayer pos, BlockPos stack, ItemStack parItemStack) {
		MinecraftServer minecraftserver = MinecraftServer.getServer();
		if (minecraftserver == null) {
			return false;
		} else {
			if (parItemStack.hasTagCompound() && parItemStack.getTagCompound().hasKey("BlockEntityTag", 10)) {
				TileEntity tileentity = worldIn.getTileEntity(stack);
				if (tileentity != null) {
					if (!worldIn.isRemote && tileentity.func_183000_F()
							&& !minecraftserver.getConfigurationManager().canSendCommands(pos.getGameProfile())) {
						return false;
					}

					NBTTagCompound nbttagcompound = new NBTTagCompound();
					NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttagcompound.copy();
					tileentity.writeToNBT(nbttagcompound);
					NBTTagCompound nbttagcompound2 = (NBTTagCompound) parItemStack.getTagCompound()
							.getTag("BlockEntityTag");
					nbttagcompound.merge(nbttagcompound2);
					nbttagcompound.setInteger("x", stack.getX());
					nbttagcompound.setInteger("y", stack.getY());
					nbttagcompound.setInteger("z", stack.getZ());
					if (!nbttagcompound.equals(nbttagcompound1)) {
						tileentity.readFromNBT(nbttagcompound);
						tileentity.markDirty();
						return true;
					}
				}
			}

			return false;
		}
	}

	public boolean canPlaceBlockOnSide(World world, BlockPos blockpos, EnumFacing enumfacing, EntityPlayer var4,
			ItemStack itemstack) {
		Block blockx = world.getBlockState(blockpos).getBlock();
		if (blockx == Blocks.snow_layer) {
			enumfacing = EnumFacing.UP;
		} else if (!blockx.isReplaceable(world, blockpos)) {
			blockpos = blockpos.offset(enumfacing);
		}

		return world.canBlockBePlaced(this.block, blockpos, false, enumfacing, (Entity) null, itemstack);
	}

	/**+
	 * Returns the unlocalized name of this item.
	 */
	public String getUnlocalizedName(ItemStack var1) {
		return this.block.getUnlocalizedName();
	}

	/**+
	 * Returns the unlocalized name of this item.
	 */
	public String getUnlocalizedName() {
		return this.block.getUnlocalizedName();
	}

	/**+
	 * gets the CreativeTab this item is displayed on
	 */
	public CreativeTabs getCreativeTab() {
		return this.block.getCreativeTabToDisplayOn();
	}

	/**+
	 * returns a list of items with the same ID, but different meta
	 * (eg: dye returns 16 items)
	 */
	public void getSubItems(Item item, CreativeTabs creativetabs, List<ItemStack> list) {
		this.block.getSubBlocks(item, creativetabs, list);
	}

	public Block getBlock() {
		return this.block;
	}

	public float getHeldItemBrightnessEagler(ItemStack itemStack) {
		return this.block.getLightValue() * 0.06667f;
	}
}