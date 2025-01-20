package net.minecraft.item;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Rotations;
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
public class ItemArmorStand extends Item {
	public ItemArmorStand() {
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float var6, float var7, float var8) {
		if (enumfacing == EnumFacing.DOWN) {
			return false;
		} else {
			boolean flag = world.getBlockState(blockpos).getBlock().isReplaceable(world, blockpos);
			BlockPos blockpos1 = flag ? blockpos : blockpos.offset(enumfacing);
			if (!entityplayer.canPlayerEdit(blockpos1, enumfacing, itemstack)) {
				return false;
			} else {
				BlockPos blockpos2 = blockpos1.up();
				boolean flag1 = !world.isAirBlock(blockpos1)
						&& !world.getBlockState(blockpos1).getBlock().isReplaceable(world, blockpos1);
				flag1 = flag1 | (!world.isAirBlock(blockpos2)
						&& !world.getBlockState(blockpos2).getBlock().isReplaceable(world, blockpos2));
				if (flag1) {
					return false;
				} else {
					double d0 = (double) blockpos1.getX();
					double d1 = (double) blockpos1.getY();
					double d2 = (double) blockpos1.getZ();
					List list = world.getEntitiesWithinAABBExcludingEntity((Entity) null,
							AxisAlignedBB.fromBounds(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));
					if (list.size() > 0) {
						return false;
					} else {
						if (!world.isRemote) {
							world.setBlockToAir(blockpos1);
							world.setBlockToAir(blockpos2);
							EntityArmorStand entityarmorstand = new EntityArmorStand(world, d0 + 0.5D, d1, d2 + 0.5D);
							float f = (float) MathHelper.floor_float(
									(MathHelper.wrapAngleTo180_float(entityplayer.rotationYaw - 180.0F) + 22.5F)
											/ 45.0F)
									* 45.0F;
							entityarmorstand.setLocationAndAngles(d0 + 0.5D, d1, d2 + 0.5D, f, 0.0F);
							this.applyRandomRotations(entityarmorstand, world.rand);
							NBTTagCompound nbttagcompound = itemstack.getTagCompound();
							if (nbttagcompound != null && nbttagcompound.hasKey("EntityTag", 10)) {
								NBTTagCompound nbttagcompound1 = new NBTTagCompound();
								entityarmorstand.writeToNBTOptional(nbttagcompound1);
								nbttagcompound1.merge(nbttagcompound.getCompoundTag("EntityTag"));
								entityarmorstand.readFromNBT(nbttagcompound1);
							}

							world.spawnEntityInWorld(entityarmorstand);
						}

						--itemstack.stackSize;
						return true;
					}
				}
			}
		}
	}

	private void applyRandomRotations(EntityArmorStand armorStand, EaglercraftRandom rand) {
		Rotations rotations = armorStand.getHeadRotation();
		float f = rand.nextFloat() * 5.0F;
		float f1 = rand.nextFloat() * 20.0F - 10.0F;
		Rotations rotations1 = new Rotations(rotations.getX() + f, rotations.getY() + f1, rotations.getZ());
		armorStand.setHeadRotation(rotations1);
		rotations = armorStand.getBodyRotation();
		f = rand.nextFloat() * 10.0F - 5.0F;
		rotations1 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
		armorStand.setBodyRotation(rotations1);
	}
}