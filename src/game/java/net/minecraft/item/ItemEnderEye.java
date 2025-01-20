package net.minecraft.item;

import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
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
public class ItemEnderEye extends Item {
	public ItemEnderEye() {
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float var6, float var7, float var8) {
		IBlockState iblockstate = world.getBlockState(blockpos);
		if (entityplayer.canPlayerEdit(blockpos.offset(enumfacing), enumfacing, itemstack)
				&& iblockstate.getBlock() == Blocks.end_portal_frame
				&& !((Boolean) iblockstate.getValue(BlockEndPortalFrame.EYE)).booleanValue()) {
			if (world.isRemote) {
				return true;
			} else {
				world.setBlockState(blockpos, iblockstate.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(true)),
						2);
				world.updateComparatorOutputLevel(blockpos, Blocks.end_portal_frame);
				--itemstack.stackSize;

				for (int i = 0; i < 16; ++i) {
					double d0 = (double) ((float) blockpos.getX() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
					double d1 = (double) ((float) blockpos.getY() + 0.8125F);
					double d2 = (double) ((float) blockpos.getZ() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
					double d3 = 0.0D;
					double d4 = 0.0D;
					double d5 = 0.0D;
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
				}

				EnumFacing enumfacing1 = (EnumFacing) iblockstate.getValue(BlockEndPortalFrame.FACING);
				int l = 0;
				int j = 0;
				boolean flag1 = false;
				boolean flag = true;
				EnumFacing enumfacing2 = enumfacing1.rotateY();

				for (int k = -2; k <= 2; ++k) {
					BlockPos blockpos2 = blockpos.offset(enumfacing2, k);
					IBlockState iblockstate1 = world.getBlockState(blockpos2);
					if (iblockstate1.getBlock() == Blocks.end_portal_frame) {
						if (!((Boolean) iblockstate1.getValue(BlockEndPortalFrame.EYE)).booleanValue()) {
							flag = false;
							break;
						}

						j = k;
						if (!flag1) {
							l = k;
							flag1 = true;
						}
					}
				}

				if (flag && j == l + 2) {
					BlockPos blockpos1 = blockpos.offset(enumfacing1, 4);

					for (int i1 = l; i1 <= j; ++i1) {
						BlockPos blockpos3 = blockpos1.offset(enumfacing2, i1);
						IBlockState iblockstate3 = world.getBlockState(blockpos3);
						if (iblockstate3.getBlock() != Blocks.end_portal_frame
								|| !((Boolean) iblockstate3.getValue(BlockEndPortalFrame.EYE)).booleanValue()) {
							flag = false;
							break;
						}
					}

					for (int j1 = l - 1; j1 <= j + 1; j1 += 4) {
						blockpos1 = blockpos.offset(enumfacing2, j1);

						for (int l1 = 1; l1 <= 3; ++l1) {
							BlockPos blockpos4 = blockpos1.offset(enumfacing1, l1);
							IBlockState iblockstate2 = world.getBlockState(blockpos4);
							if (iblockstate2.getBlock() != Blocks.end_portal_frame
									|| !((Boolean) iblockstate2.getValue(BlockEndPortalFrame.EYE)).booleanValue()) {
								flag = false;
								break;
							}
						}
					}

					if (flag) {
						for (int k1 = l; k1 <= j; ++k1) {
							blockpos1 = blockpos.offset(enumfacing2, k1);

							for (int i2 = 1; i2 <= 3; ++i2) {
								BlockPos blockpos5 = blockpos1.offset(enumfacing1, i2);
								world.setBlockState(blockpos5, Blocks.end_portal.getDefaultState(), 2);
							}
						}
					}
				}

				return true;
			}
		} else {
			return false;
		}
	}

	/**+
	 * Called whenever this item is equipped and the right mouse
	 * button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, entityplayer, false);
		if (movingobjectposition != null
				&& movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
				&& world.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.end_portal_frame) {
			return itemstack;
		} else {
			if (!world.isRemote) {
				BlockPos blockpos = world.getStrongholdPos("Stronghold", new BlockPos(entityplayer));
				if (blockpos != null) {
					EntityEnderEye entityendereye = new EntityEnderEye(world, entityplayer.posX, entityplayer.posY,
							entityplayer.posZ);
					entityendereye.moveTowards(blockpos);
					world.spawnEntityInWorld(entityendereye);
					world.playSoundAtEntity(entityplayer, "random.bow", 0.5F,
							0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
					world.playAuxSFXAtEntity((EntityPlayer) null, 1002, new BlockPos(entityplayer), 0);
					if (!entityplayer.capabilities.isCreativeMode) {
						--itemstack.stackSize;
					}

					entityplayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
				}
			}

			return itemstack;
		}
	}

	public boolean shouldUseOnTouchEagler(ItemStack itemStack) {
		return true;
	}
}