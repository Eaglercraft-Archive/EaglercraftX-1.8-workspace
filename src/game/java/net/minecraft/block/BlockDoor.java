package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
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
public class BlockDoor extends Block {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool OPEN = PropertyBool.create("open");
	public static PropertyEnum<BlockDoor.EnumHingePosition> HINGE;
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static PropertyEnum<BlockDoor.EnumDoorHalf> HALF;

	protected BlockDoor(Material materialIn) {
		super(materialIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(OPEN, Boolean.valueOf(false)).withProperty(HINGE, BlockDoor.EnumHingePosition.LEFT)
				.withProperty(POWERED, Boolean.valueOf(false)).withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER));
	}

	public static void bootstrapStates() {
		HINGE = PropertyEnum.<BlockDoor.EnumHingePosition>create("hinge", BlockDoor.EnumHingePosition.class);
		HALF = PropertyEnum.<BlockDoor.EnumDoorHalf>create("half", BlockDoor.EnumDoorHalf.class);
	}

	/**+
	 * Gets the localized name of this block. Used for the
	 * statistics page.
	 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal((this.getUnlocalizedName() + ".name").replaceAll("tile", "item"));
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockpos) {
		return isOpen(combineMetadata(iblockaccess, blockpos));
	}

	public boolean isFullCube() {
		return false;
	}

	public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockpos) {
		this.setBlockBoundsBasedOnState(world, blockpos);
		return super.getSelectedBoundingBox(world, blockpos);
	}

	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.setBlockBoundsBasedOnState(world, blockpos);
		return super.getCollisionBoundingBox(world, blockpos, iblockstate);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		this.setBoundBasedOnMeta(combineMetadata(iblockaccess, blockpos));
	}

	private void setBoundBasedOnMeta(int combinedMeta) {
		float f = 0.1875F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
		EnumFacing enumfacing = getFacing(combinedMeta);
		boolean flag = isOpen(combinedMeta);
		boolean flag1 = isHingeLeft(combinedMeta);
		if (flag) {
			if (enumfacing == EnumFacing.EAST) {
				if (!flag1) {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
				} else {
					this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
				}
			} else if (enumfacing == EnumFacing.SOUTH) {
				if (!flag1) {
					this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				} else {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
				}
			} else if (enumfacing == EnumFacing.WEST) {
				if (!flag1) {
					this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
				} else {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
				}
			} else if (enumfacing == EnumFacing.NORTH) {
				if (!flag1) {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
				} else {
					this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				}
			}
		} else if (enumfacing == EnumFacing.EAST) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		} else if (enumfacing == EnumFacing.SOUTH) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		} else if (enumfacing == EnumFacing.WEST) {
			this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else if (enumfacing == EnumFacing.NORTH) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		}

	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		if (this.blockMaterial == Material.iron) {
			return true;
		} else {
			BlockPos blockpos1 = iblockstate.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? blockpos
					: blockpos.down();
			IBlockState iblockstate1 = blockpos.equals(blockpos1) ? iblockstate : world.getBlockState(blockpos1);
			if (iblockstate1.getBlock() != this) {
				return false;
			} else {
				iblockstate = iblockstate1.cycleProperty(OPEN);
				world.setBlockState(blockpos1, iblockstate, 2);
				world.markBlockRangeForRenderUpdate(blockpos1, blockpos);
				world.playAuxSFXAtEntity(entityplayer,
						((Boolean) iblockstate.getValue(OPEN)).booleanValue() ? 1003 : 1006, blockpos, 0);
				return true;
			}
		}
	}

	public void toggleDoor(World worldIn, BlockPos pos, boolean open) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		if (iblockstate.getBlock() == this) {
			BlockPos blockpos = iblockstate.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
			IBlockState iblockstate1 = pos == blockpos ? iblockstate : worldIn.getBlockState(blockpos);
			if (iblockstate1.getBlock() == this && ((Boolean) iblockstate1.getValue(OPEN)).booleanValue() != open) {
				worldIn.setBlockState(blockpos, iblockstate1.withProperty(OPEN, Boolean.valueOf(open)), 2);
				worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
				worldIn.playAuxSFXAtEntity((EntityPlayer) null, open ? 1003 : 1006, pos, 0);
			}

		}
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block block) {
		if (iblockstate.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			BlockPos blockpos1 = blockpos.down();
			IBlockState iblockstate1 = world.getBlockState(blockpos1);
			if (iblockstate1.getBlock() != this) {
				world.setBlockToAir(blockpos);
			} else if (block != this) {
				this.onNeighborBlockChange(world, blockpos1, iblockstate1, block);
			}
		} else {
			boolean flag1 = false;
			BlockPos blockpos2 = blockpos.up();
			IBlockState iblockstate2 = world.getBlockState(blockpos2);
			if (iblockstate2.getBlock() != this) {
				world.setBlockToAir(blockpos);
				flag1 = true;
			}

			if (!World.doesBlockHaveSolidTopSurface(world, blockpos.down())) {
				world.setBlockToAir(blockpos);
				flag1 = true;
				if (iblockstate2.getBlock() == this) {
					world.setBlockToAir(blockpos2);
				}
			}

			if (flag1) {
				if (!world.isRemote) {
					this.dropBlockAsItem(world, blockpos, iblockstate, 0);
				}
			} else {
				boolean flag = world.isBlockPowered(blockpos) || world.isBlockPowered(blockpos2);
				if ((flag || block.canProvidePower()) && block != this
						&& flag != ((Boolean) iblockstate2.getValue(POWERED)).booleanValue()) {
					world.setBlockState(blockpos2, iblockstate2.withProperty(POWERED, Boolean.valueOf(flag)), 2);
					if (flag != ((Boolean) iblockstate.getValue(OPEN)).booleanValue()) {
						world.setBlockState(blockpos, iblockstate.withProperty(OPEN, Boolean.valueOf(flag)), 2);
						world.markBlockRangeForRenderUpdate(blockpos, blockpos);
						world.playAuxSFXAtEntity((EntityPlayer) null, flag ? 1003 : 1006, blockpos, 0);
					}
				}
			}
		}

	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {
		return iblockstate.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? null : this.getItem();
	}

	/**+
	 * Ray traces through the blocks collision from start vector to
	 * end vector returning a ray trace hit.
	 */
	public MovingObjectPosition collisionRayTrace(World world, BlockPos blockpos, Vec3 vec3, Vec3 vec31) {
		this.setBlockBoundsBasedOnState(world, blockpos);
		return super.collisionRayTrace(world, blockpos, vec3, vec31);
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		return blockpos.getY() >= 255 ? false
				: World.doesBlockHaveSolidTopSurface(world, blockpos.down()) && super.canPlaceBlockAt(world, blockpos)
						&& super.canPlaceBlockAt(world, blockpos.up());
	}

	public int getMobilityFlag() {
		return 1;
	}

	public static int combineMetadata(IBlockAccess worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		int i = iblockstate.getBlock().getMetaFromState(iblockstate);
		boolean flag = isTop(i);
		IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
		int j = iblockstate1.getBlock().getMetaFromState(iblockstate1);
		int k = flag ? j : i;
		IBlockState iblockstate2 = worldIn.getBlockState(pos.up());
		int l = iblockstate2.getBlock().getMetaFromState(iblockstate2);
		int i1 = flag ? i : l;
		boolean flag1 = (i1 & 1) != 0;
		boolean flag2 = (i1 & 2) != 0;
		return removeHalfBit(k) | (flag ? 8 : 0) | (flag1 ? 16 : 0) | (flag2 ? 32 : 0);
	}

	public Item getItem(World var1, BlockPos var2) {
		return this.getItem();
	}

	private Item getItem() {
		return this == Blocks.iron_door ? Items.iron_door
				: (this == Blocks.spruce_door ? Items.spruce_door
						: (this == Blocks.birch_door ? Items.birch_door
								: (this == Blocks.jungle_door ? Items.jungle_door
										: (this == Blocks.acacia_door ? Items.acacia_door
												: (this == Blocks.dark_oak_door ? Items.dark_oak_door
														: Items.oak_door)))));
	}

	public void onBlockHarvested(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer) {
		BlockPos blockpos1 = blockpos.down();
		if (entityplayer.capabilities.isCreativeMode && iblockstate.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER
				&& world.getBlockState(blockpos1).getBlock() == this) {
			world.setBlockToAir(blockpos1);
		}

	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		if (iblockstate.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER) {
			IBlockState iblockstate1 = iblockaccess.getBlockState(blockpos.up());
			if (iblockstate1.getBlock() == this) {
				iblockstate = iblockstate.withProperty(HINGE, iblockstate1.getValue(HINGE)).withProperty(POWERED,
						iblockstate1.getValue(POWERED));
			}
		} else {
			IBlockState iblockstate2 = iblockaccess.getBlockState(blockpos.down());
			if (iblockstate2.getBlock() == this) {
				iblockstate = iblockstate.withProperty(FACING, iblockstate2.getValue(FACING)).withProperty(OPEN,
						iblockstate2.getValue(OPEN));
			}
		}

		return iblockstate;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return (i & 8) > 0
				? this.getDefaultState().withProperty(HALF, BlockDoor.EnumDoorHalf.UPPER)
						.withProperty(HINGE,
								(i & 1) > 0 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT)
						.withProperty(POWERED, Boolean.valueOf((i & 2) > 0))
				: this.getDefaultState().withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER)
						.withProperty(FACING, EnumFacing.getHorizontal(i & 3).rotateYCCW())
						.withProperty(OPEN, Boolean.valueOf((i & 4) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		if (iblockstate.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			i = i | 8;
			if (iblockstate.getValue(HINGE) == BlockDoor.EnumHingePosition.RIGHT) {
				i |= 1;
			}

			if (((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
				i |= 2;
			}
		} else {
			i = i | ((EnumFacing) iblockstate.getValue(FACING)).rotateY().getHorizontalIndex();
			if (((Boolean) iblockstate.getValue(OPEN)).booleanValue()) {
				i |= 4;
			}
		}

		return i;
	}

	protected static int removeHalfBit(int meta) {
		return meta & 7;
	}

	public static boolean isOpen(IBlockAccess worldIn, BlockPos pos) {
		return isOpen(combineMetadata(worldIn, pos));
	}

	public static EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos) {
		return getFacing(combineMetadata(worldIn, pos));
	}

	public static EnumFacing getFacing(int combinedMeta) {
		return EnumFacing.getHorizontal(combinedMeta & 3).rotateYCCW();
	}

	protected static boolean isOpen(int combinedMeta) {
		return (combinedMeta & 4) != 0;
	}

	protected static boolean isTop(int meta) {
		return (meta & 8) != 0;
	}

	protected static boolean isHingeLeft(int combinedMeta) {
		return (combinedMeta & 16) != 0;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { HALF, FACING, OPEN, HINGE, POWERED });
	}

	public static enum EnumDoorHalf implements IStringSerializable {
		UPPER, LOWER;

		public String toString() {
			return this.getName();
		}

		public String getName() {
			return this == UPPER ? "upper" : "lower";
		}
	}

	public static enum EnumHingePosition implements IStringSerializable {
		LEFT, RIGHT;

		public String toString() {
			return this.getName();
		}

		public String getName() {
			return this == LEFT ? "left" : "right";
		}
	}
}