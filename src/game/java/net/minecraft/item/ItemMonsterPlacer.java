package net.minecraft.item;

import java.util.List;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.SortedIterationIntObjectHashMap;
import com.carrotsearch.hppc.cursors.ObjectCursor;

import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
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
public class ItemMonsterPlacer extends Item {
	public ItemMonsterPlacer() {
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	public String getItemStackDisplayName(ItemStack itemstack) {
		String s = ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
		String s1 = EntityList.getStringFromID(itemstack.getMetadata());
		if (s1 != null) {
			s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
		}

		return s;
	}

	public int getColorFromItemStack(ItemStack itemstack, int i) {
		EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo) EntityList.entityEggs
				.get(itemstack.getMetadata());
		return entitylist$entityegginfo != null
				? (i == 0 ? entitylist$entityegginfo.primaryColor : entitylist$entityegginfo.secondaryColor)
				: 16777215;
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float var6, float var7, float var8) {
		if (world.isRemote) {
			return true;
		} else if (!entityplayer.canPlayerEdit(blockpos.offset(enumfacing), enumfacing, itemstack)) {
			return false;
		} else {
			IBlockState iblockstate = world.getBlockState(blockpos);
			if (iblockstate.getBlock() == Blocks.mob_spawner) {
				TileEntity tileentity = world.getTileEntity(blockpos);
				if (tileentity instanceof TileEntityMobSpawner) {
					MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic();
					mobspawnerbaselogic.setEntityName(EntityList.getStringFromID(itemstack.getMetadata()));
					tileentity.markDirty();
					world.markBlockForUpdate(blockpos);
					if (!entityplayer.capabilities.isCreativeMode) {
						--itemstack.stackSize;
					}

					return true;
				}
			}

			blockpos = blockpos.offset(enumfacing);
			double d0 = 0.0D;
			if (enumfacing == EnumFacing.UP && iblockstate instanceof BlockFence) {
				d0 = 0.5D;
			}

			Entity entity = spawnCreature(world, itemstack.getMetadata(), (double) blockpos.getX() + 0.5D,
					(double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5D);
			if (entity != null) {
				if (entity instanceof EntityLivingBase && itemstack.hasDisplayName()) {
					entity.setCustomNameTag(itemstack.getDisplayName());
				}

				if (!entityplayer.capabilities.isCreativeMode) {
					--itemstack.stackSize;
				}
			}

			return true;
		}
	}

	/**+
	 * Called whenever this item is equipped and the right mouse
	 * button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (world.isRemote) {
			return itemstack;
		} else {
			MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, entityplayer,
					true);
			if (movingobjectposition == null) {
				return itemstack;
			} else {
				if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
					BlockPos blockpos = movingobjectposition.getBlockPos();
					if (!world.isBlockModifiable(entityplayer, blockpos)) {
						return itemstack;
					}

					if (!entityplayer.canPlayerEdit(blockpos, movingobjectposition.sideHit, itemstack)) {
						return itemstack;
					}

					if (world.getBlockState(blockpos).getBlock() instanceof BlockLiquid) {
						Entity entity = spawnCreature(world, itemstack.getMetadata(), (double) blockpos.getX() + 0.5D,
								(double) blockpos.getY() + 0.5D, (double) blockpos.getZ() + 0.5D);
						if (entity != null) {
							if (entity instanceof EntityLivingBase && itemstack.hasDisplayName()) {
								((EntityLiving) entity).setCustomNameTag(itemstack.getDisplayName());
							}

							if (!entityplayer.capabilities.isCreativeMode) {
								--itemstack.stackSize;
							}

							entityplayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
						}
					}
				}

				return itemstack;
			}
		}
	}

	/**+
	 * Spawns the creature specified by the egg's type in the
	 * location specified by the last three parameters. Parameters:
	 * world, entityID, x, y, z.
	 */
	public static Entity spawnCreature(World worldIn, int entityID, double x, double y, double z) {
		if (!EntityList.entityEggs.containsKey(entityID)) {
			return null;
		} else {
			Entity entity = null;

			for (int i = 0; i < 1; ++i) {
				entity = EntityList.createEntityByID(entityID, worldIn);
				if (entity instanceof EntityLivingBase) {
					EntityLiving entityliving = (EntityLiving) entity;
					entity.setLocationAndAngles(x, y, z,
							MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0F), 0.0F);
					entityliving.rotationYawHead = entityliving.rotationYaw;
					entityliving.renderYawOffset = entityliving.rotationYaw;
					entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)),
							(IEntityLivingData) null);
					worldIn.spawnEntityInWorld(entity);
					entityliving.playLivingSound();
				}
			}

			return entity;
		}
	}

	/**+
	 * returns a list of items with the same ID, but different meta
	 * (eg: dye returns 16 items)
	 */
	public void getSubItems(Item item, CreativeTabs var2, List<ItemStack> list) {
		for (ObjectCursor<EntityList.EntityEggInfo> entitylist$entityegginfo : new SortedIterationIntObjectHashMap<>(
				(IntObjectHashMap<EntityList.EntityEggInfo>) EntityList.entityEggs, (a, b) -> a - b).values()) {
			list.add(new ItemStack(item, 1, entitylist$entityegginfo.value.spawnedID));
		}

	}
}