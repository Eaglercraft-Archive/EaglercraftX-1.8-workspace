package net.minecraft.block;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
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
public abstract class BlockRailBase extends Block {
	protected final boolean isPowered;

	public static boolean isRailBlock(World worldIn, BlockPos pos) {
		return isRailBlock(worldIn.getBlockState(pos));
	}

	public static boolean isRailBlock(IBlockState state) {
		Block block = state.getBlock();
		return block == Blocks.rail || block == Blocks.golden_rail || block == Blocks.detector_rail
				|| block == Blocks.activator_rail;
	}

	protected BlockRailBase(boolean isPowered) {
		super(Material.circuits);
		this.isPowered = isPowered;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setCreativeTab(CreativeTabs.tabTransport);
	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
		return null;
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	/**+
	 * Ray traces through the blocks collision from start vector to
	 * end vector returning a ray trace hit.
	 */
	public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
		this.setBlockBoundsBasedOnState(worldIn, pos);
		return super.collisionRayTrace(worldIn, pos, start, end);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		IBlockState iblockstate = iblockaccess.getBlockState(blockpos);
		BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() == this
				? (BlockRailBase.EnumRailDirection) iblockstate.getValue(this.getShapeProperty())
				: null;
		if (blockrailbase$enumraildirection != null && blockrailbase$enumraildirection.isAscending()) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		}

	}

	public boolean isFullCube() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		return World.doesBlockHaveSolidTopSurface(world, blockpos.down());
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			state = this.func_176564_a(worldIn, pos, state, true);
			if (this.isPowered) {
				this.onNeighborBlockChange(worldIn, pos, state, this);
			}
		}
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block block) {
		if (!world.isRemote) {
			BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection) iblockstate
					.getValue(this.getShapeProperty());
			boolean flag = false;
			if (!World.doesBlockHaveSolidTopSurface(world, blockpos.down())) {
				flag = true;
			}

			if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_EAST
					&& !World.doesBlockHaveSolidTopSurface(world, blockpos.east())) {
				flag = true;
			} else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_WEST
					&& !World.doesBlockHaveSolidTopSurface(world, blockpos.west())) {
				flag = true;
			} else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_NORTH
					&& !World.doesBlockHaveSolidTopSurface(world, blockpos.north())) {
				flag = true;
			} else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH
					&& !World.doesBlockHaveSolidTopSurface(world, blockpos.south())) {
				flag = true;
			}

			if (flag) {
				this.dropBlockAsItem(world, blockpos, iblockstate, 0);
				world.setBlockToAir(blockpos);
			} else {
				this.onNeighborChangedInternal(world, blockpos, iblockstate, block);
			}
		}
	}

	protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
	}

	protected IBlockState func_176564_a(World worldIn, BlockPos parBlockPos, IBlockState parIBlockState,
			boolean parFlag) {
		return worldIn.isRemote ? parIBlockState
				: (new BlockRailBase.Rail(worldIn, parBlockPos, parIBlockState))
						.func_180364_a(worldIn.isBlockPowered(parBlockPos), parFlag).getBlockState();
	}

	public int getMobilityFlag() {
		return 0;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		super.breakBlock(world, blockpos, iblockstate);
		if (((BlockRailBase.EnumRailDirection) iblockstate.getValue(this.getShapeProperty())).isAscending()) {
			world.notifyNeighborsOfStateChange(blockpos.up(), this);
		}

		if (this.isPowered) {
			world.notifyNeighborsOfStateChange(blockpos, this);
			world.notifyNeighborsOfStateChange(blockpos.down(), this);
		}

	}

	public abstract IProperty<BlockRailBase.EnumRailDirection> getShapeProperty();

	public static enum EnumRailDirection implements IStringSerializable {
		NORTH_SOUTH(0, "north_south"), EAST_WEST(1, "east_west"), ASCENDING_EAST(2, "ascending_east"),
		ASCENDING_WEST(3, "ascending_west"), ASCENDING_NORTH(4, "ascending_north"),
		ASCENDING_SOUTH(5, "ascending_south"), SOUTH_EAST(6, "south_east"), SOUTH_WEST(7, "south_west"),
		NORTH_WEST(8, "north_west"), NORTH_EAST(9, "north_east");

		private static final BlockRailBase.EnumRailDirection[] META_LOOKUP = new BlockRailBase.EnumRailDirection[10];
		private final int meta;
		private final String name;

		private EnumRailDirection(int meta, String name) {
			this.meta = meta;
			this.name = name;
		}

		public int getMetadata() {
			return this.meta;
		}

		public String toString() {
			return this.name;
		}

		public boolean isAscending() {
			return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH
					|| this == ASCENDING_WEST;
		}

		public static BlockRailBase.EnumRailDirection byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName() {
			return this.name;
		}

		static {
			BlockRailBase.EnumRailDirection[] directions = values();
			for (int i = 0; i < directions.length; ++i) {
				META_LOOKUP[directions[i].getMetadata()] = directions[i];
			}

		}
	}

	public class Rail {
		private final World world;
		private final BlockPos pos;
		private final BlockRailBase block;
		private IBlockState state;
		private final boolean isPowered;
		private final List<BlockPos> field_150657_g = Lists.newArrayList();

		public Rail(World worldIn, BlockPos pos, IBlockState state) {
			this.world = worldIn;
			this.pos = pos;
			this.state = state;
			this.block = (BlockRailBase) state.getBlock();
			BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection) state
					.getValue(BlockRailBase.this.getShapeProperty());
			this.isPowered = this.block.isPowered;
			this.func_180360_a(blockrailbase$enumraildirection);
		}

		private void func_180360_a(BlockRailBase.EnumRailDirection parEnumRailDirection) {
			this.field_150657_g.clear();
			switch (parEnumRailDirection) {
			case NORTH_SOUTH:
				this.field_150657_g.add(this.pos.north());
				this.field_150657_g.add(this.pos.south());
				break;
			case EAST_WEST:
				this.field_150657_g.add(this.pos.west());
				this.field_150657_g.add(this.pos.east());
				break;
			case ASCENDING_EAST:
				this.field_150657_g.add(this.pos.west());
				this.field_150657_g.add(this.pos.east().up());
				break;
			case ASCENDING_WEST:
				this.field_150657_g.add(this.pos.west().up());
				this.field_150657_g.add(this.pos.east());
				break;
			case ASCENDING_NORTH:
				this.field_150657_g.add(this.pos.north().up());
				this.field_150657_g.add(this.pos.south());
				break;
			case ASCENDING_SOUTH:
				this.field_150657_g.add(this.pos.north());
				this.field_150657_g.add(this.pos.south().up());
				break;
			case SOUTH_EAST:
				this.field_150657_g.add(this.pos.east());
				this.field_150657_g.add(this.pos.south());
				break;
			case SOUTH_WEST:
				this.field_150657_g.add(this.pos.west());
				this.field_150657_g.add(this.pos.south());
				break;
			case NORTH_WEST:
				this.field_150657_g.add(this.pos.west());
				this.field_150657_g.add(this.pos.north());
				break;
			case NORTH_EAST:
				this.field_150657_g.add(this.pos.east());
				this.field_150657_g.add(this.pos.north());
			}

		}

		private void func_150651_b() {
			for (int i = 0; i < this.field_150657_g.size(); ++i) {
				BlockRailBase.Rail blockrailbase$rail = this.findRailAt((BlockPos) this.field_150657_g.get(i));
				if (blockrailbase$rail != null && blockrailbase$rail.func_150653_a(this)) {
					this.field_150657_g.set(i, blockrailbase$rail.pos);
				} else {
					this.field_150657_g.remove(i--);
				}
			}

		}

		private boolean hasRailAt(BlockPos pos) {
			return BlockRailBase.isRailBlock(this.world, pos) || BlockRailBase.isRailBlock(this.world, pos.up())
					|| BlockRailBase.isRailBlock(this.world, pos.down());
		}

		private BlockRailBase.Rail findRailAt(BlockPos pos) {
			IBlockState iblockstate = this.world.getBlockState(pos);
			if (BlockRailBase.isRailBlock(iblockstate)) {
				return BlockRailBase.this.new Rail(this.world, pos, iblockstate);
			} else {
				BlockPos blockpos = pos.up();
				iblockstate = this.world.getBlockState(blockpos);
				if (BlockRailBase.isRailBlock(iblockstate)) {
					return BlockRailBase.this.new Rail(this.world, blockpos, iblockstate);
				} else {
					blockpos = pos.down();
					iblockstate = this.world.getBlockState(blockpos);
					return BlockRailBase.isRailBlock(iblockstate)
							? BlockRailBase.this.new Rail(this.world, blockpos, iblockstate)
							: null;
				}
			}
		}

		private boolean func_150653_a(BlockRailBase.Rail parRail) {
			return this.func_180363_c(parRail.pos);
		}

		private boolean func_180363_c(BlockPos parBlockPos) {
			for (int i = 0; i < this.field_150657_g.size(); ++i) {
				BlockPos blockpos = (BlockPos) this.field_150657_g.get(i);
				if (blockpos.getX() == parBlockPos.getX() && blockpos.getZ() == parBlockPos.getZ()) {
					return true;
				}
			}

			return false;
		}

		protected int countAdjacentRails() {
			int i = 0;

			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
			BlockPos tmp = new BlockPos(0, 0, 0);
			for (int j = 0; j < facings.length; ++j) {
				if (this.hasRailAt(this.pos.offsetEvenFaster(facings[j], tmp))) {
					++i;
				}
			}

			return i;
		}

		private boolean func_150649_b(BlockRailBase.Rail rail) {
			return this.func_150653_a(rail) || this.field_150657_g.size() != 2;
		}

		private void func_150645_c(BlockRailBase.Rail parRail) {
			this.field_150657_g.add(parRail.pos);
			BlockPos blockpos = this.pos.north();
			BlockPos blockpos1 = this.pos.south();
			BlockPos blockpos2 = this.pos.west();
			BlockPos blockpos3 = this.pos.east();
			boolean flag = this.func_180363_c(blockpos);
			boolean flag1 = this.func_180363_c(blockpos1);
			boolean flag2 = this.func_180363_c(blockpos2);
			boolean flag3 = this.func_180363_c(blockpos3);
			BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;
			if (flag || flag1) {
				blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
			}

			if (flag2 || flag3) {
				blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
			}

			if (!this.isPowered) {
				if (flag1 && flag3 && !flag && !flag2) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
				}

				if (flag1 && flag2 && !flag && !flag3) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
				}

				if (flag && flag2 && !flag1 && !flag3) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
				}

				if (flag && flag3 && !flag1 && !flag2) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
				}
			}

			if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
				if (BlockRailBase.isRailBlock(this.world, blockpos.up())) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
				}

				if (BlockRailBase.isRailBlock(this.world, blockpos1.up())) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
				}
			}

			if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST) {
				if (BlockRailBase.isRailBlock(this.world, blockpos3.up())) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
				}

				if (BlockRailBase.isRailBlock(this.world, blockpos2.up())) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
				}
			}

			if (blockrailbase$enumraildirection == null) {
				blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
			}

			this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
			this.world.setBlockState(this.pos, this.state, 3);
		}

		private boolean func_180361_d(BlockPos parBlockPos) {
			BlockRailBase.Rail blockrailbase$rail = this.findRailAt(parBlockPos);
			if (blockrailbase$rail == null) {
				return false;
			} else {
				blockrailbase$rail.func_150651_b();
				return blockrailbase$rail.func_150649_b(this);
			}
		}

		public BlockRailBase.Rail func_180364_a(boolean parFlag, boolean parFlag2) {
			BlockPos blockpos = this.pos.north();
			BlockPos blockpos1 = this.pos.south();
			BlockPos blockpos2 = this.pos.west();
			BlockPos blockpos3 = this.pos.east();
			boolean flag = this.func_180361_d(blockpos);
			boolean flag1 = this.func_180361_d(blockpos1);
			boolean flag2 = this.func_180361_d(blockpos2);
			boolean flag3 = this.func_180361_d(blockpos3);
			BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;
			if ((flag || flag1) && !flag2 && !flag3) {
				blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
			}

			if ((flag2 || flag3) && !flag && !flag1) {
				blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
			}

			if (!this.isPowered) {
				if (flag1 && flag3 && !flag && !flag2) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
				}

				if (flag1 && flag2 && !flag && !flag3) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
				}

				if (flag && flag2 && !flag1 && !flag3) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
				}

				if (flag && flag3 && !flag1 && !flag2) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
				}
			}

			if (blockrailbase$enumraildirection == null) {
				if (flag || flag1) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
				}

				if (flag2 || flag3) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
				}

				if (!this.isPowered) {
					if (parFlag) {
						if (flag1 && flag3) {
							blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
						}

						if (flag2 && flag1) {
							blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
						}

						if (flag3 && flag) {
							blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
						}

						if (flag && flag2) {
							blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
						}
					} else {
						if (flag && flag2) {
							blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
						}

						if (flag3 && flag) {
							blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
						}

						if (flag2 && flag1) {
							blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
						}

						if (flag1 && flag3) {
							blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
						}
					}
				}
			}

			if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
				if (BlockRailBase.isRailBlock(this.world, blockpos.up())) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
				}

				if (BlockRailBase.isRailBlock(this.world, blockpos1.up())) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
				}
			}

			if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST) {
				if (BlockRailBase.isRailBlock(this.world, blockpos3.up())) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
				}

				if (BlockRailBase.isRailBlock(this.world, blockpos2.up())) {
					blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
				}
			}

			if (blockrailbase$enumraildirection == null) {
				blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
			}

			this.func_180360_a(blockrailbase$enumraildirection);
			this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
			if (parFlag2 || this.world.getBlockState(this.pos) != this.state) {
				this.world.setBlockState(this.pos, this.state, 3);

				for (int i = 0; i < this.field_150657_g.size(); ++i) {
					BlockRailBase.Rail blockrailbase$rail = this.findRailAt((BlockPos) this.field_150657_g.get(i));
					if (blockrailbase$rail != null) {
						blockrailbase$rail.func_150651_b();
						if (blockrailbase$rail.func_150649_b(this)) {
							blockrailbase$rail.func_150645_c(this);
						}
					}
				}
			}

			return this;
		}

		public IBlockState getBlockState() {
			return this.state;
		}
	}
}