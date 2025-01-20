package net.minecraft.block;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
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
public class BlockCauldron extends Block {
	public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);

	public BlockCauldron() {
		super(Material.iron, MapColor.stoneColor);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
	}

	/**+
	 * Add all collision boxes of this Block to the list that
	 * intersect with the given mask.
	 */
	public void addCollisionBoxesToList(World world, BlockPos blockpos, IBlockState iblockstate,
			AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, Entity entity) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
		super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		float f = 0.125F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		this.setBlockBoundsForItemRender();
	}

	/**+
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	public boolean isFullCube() {
		return false;
	}

	/**+
	 * Called When an Entity Collided with the Block
	 */
	public void onEntityCollidedWithBlock(World world, BlockPos blockpos, IBlockState iblockstate, Entity entity) {
		int i = ((Integer) iblockstate.getValue(LEVEL)).intValue();
		float f = (float) blockpos.getY() + (6.0F + (float) (3 * i)) / 16.0F;
		if (!world.isRemote && entity.isBurning() && i > 0 && entity.getEntityBoundingBox().minY <= (double) f) {
			entity.extinguish();
			this.setWaterLevel(world, blockpos, iblockstate, i - 1);
		}

	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		if (!world.isRemote) {
			ItemStack itemstack = entityplayer.inventory.getCurrentItem();
			if (itemstack == null) {
				return true;
			} else {
				int i = ((Integer) iblockstate.getValue(LEVEL)).intValue();
				Item item = itemstack.getItem();
				if (item == Items.water_bucket) {
					if (i < 3) {
						if (!entityplayer.capabilities.isCreativeMode) {
							entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem,
									new ItemStack(Items.bucket));
						}

						entityplayer.triggerAchievement(StatList.field_181725_I);
						this.setWaterLevel(world, blockpos, iblockstate, 3);
					}

					return true;
				} else if (item == Items.glass_bottle) {
					if (i > 0) {
						if (!entityplayer.capabilities.isCreativeMode) {
							ItemStack itemstack2 = new ItemStack(Items.potionitem, 1, 0);
							if (!entityplayer.inventory.addItemStackToInventory(itemstack2)) {
								world.spawnEntityInWorld(new EntityItem(world, (double) blockpos.getX() + 0.5D,
										(double) blockpos.getY() + 1.5D, (double) blockpos.getZ() + 0.5D, itemstack2));
							} else if (entityplayer instanceof EntityPlayerMP) {
								((EntityPlayerMP) entityplayer).sendContainerToPlayer(entityplayer.inventoryContainer);
							}

							entityplayer.triggerAchievement(StatList.field_181726_J);
							--itemstack.stackSize;
							if (itemstack.stackSize <= 0) {
								entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem,
										(ItemStack) null);
							}
						}

						this.setWaterLevel(world, blockpos, iblockstate, i - 1);
					}

					return true;
				} else {
					if (i > 0 && item instanceof ItemArmor) {
						ItemArmor itemarmor = (ItemArmor) item;
						if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER
								&& itemarmor.hasColor(itemstack)) {
							itemarmor.removeColor(itemstack);
							this.setWaterLevel(world, blockpos, iblockstate, i - 1);
							entityplayer.triggerAchievement(StatList.field_181727_K);
							return true;
						}
					}

					if (i > 0 && item instanceof ItemBanner && TileEntityBanner.getPatterns(itemstack) > 0) {
						ItemStack itemstack1 = itemstack.copy();
						itemstack1.stackSize = 1;
						TileEntityBanner.removeBannerData(itemstack1);
						if (itemstack.stackSize <= 1 && !entityplayer.capabilities.isCreativeMode) {
							entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem,
									itemstack1);
						} else {
							if (!entityplayer.inventory.addItemStackToInventory(itemstack1)) {
								world.spawnEntityInWorld(new EntityItem(world, (double) blockpos.getX() + 0.5D,
										(double) blockpos.getY() + 1.5D, (double) blockpos.getZ() + 0.5D, itemstack1));
							} else if (entityplayer instanceof EntityPlayerMP) {
								((EntityPlayerMP) entityplayer).sendContainerToPlayer(entityplayer.inventoryContainer);
							}

							entityplayer.triggerAchievement(StatList.field_181728_L);
							if (!entityplayer.capabilities.isCreativeMode) {
								--itemstack.stackSize;
							}
						}

						if (!entityplayer.capabilities.isCreativeMode) {
							this.setWaterLevel(world, blockpos, iblockstate, i - 1);
						}

						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return true;
		}
	}

	public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level) {
		worldIn.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(MathHelper.clamp_int(level, 0, 3))), 2);
		worldIn.updateComparatorOutputLevel(pos, this);
	}

	/**+
	 * Called similar to random ticks, but only when it is raining.
	 */
	public void fillWithRain(World world, BlockPos blockpos) {
		if (world.rand.nextInt(20) == 1) {
			IBlockState iblockstate = world.getBlockState(blockpos);
			if (((Integer) iblockstate.getValue(LEVEL)).intValue() < 3) {
				world.setBlockState(blockpos, iblockstate.cycleProperty(LEVEL), 2);
			}

		}
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Items.cauldron;
	}

	public Item getItem(World var1, BlockPos var2) {
		return Items.cauldron;
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

	public int getComparatorInputOverride(World world, BlockPos blockpos) {
		return ((Integer) world.getBlockState(blockpos).getValue(LEVEL)).intValue();
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(LEVEL)).intValue();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { LEVEL });
	}
}