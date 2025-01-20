package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.cache.EaglerLoadingCache;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
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
public class BlockPortal extends BlockBreakable {
	public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class,
			new EnumFacing.Axis[] { EnumFacing.Axis.X, EnumFacing.Axis.Z });

	public BlockPortal() {
		super(Material.portal, false);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
		this.setTickRandomly(true);
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		super.updateTick(world, blockpos, iblockstate, random);
		if (world.provider.isSurfaceWorld() && world.getGameRules().getBoolean("doMobSpawning")
				&& random.nextInt(2000) < world.getDifficulty().getDifficultyId()) {
			int i = blockpos.getY();

			BlockPos blockpos1;
			for (blockpos1 = blockpos; !World.doesBlockHaveSolidTopSurface(world, blockpos1)
					&& blockpos1.getY() > 0; blockpos1 = blockpos1.down()) {
				;
			}

			if (i > 0 && !world.getBlockState(blockpos1.up()).getBlock().isNormalCube()) {
				Entity entity = ItemMonsterPlacer.spawnCreature(world, 57, (double) blockpos1.getX() + 0.5D,
						(double) blockpos1.getY() + 1.1D, (double) blockpos1.getZ() + 0.5D);
				if (entity != null) {
					entity.timeUntilPortal = entity.getPortalCooldown();
				}
			}
		}

	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
		return null;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis) iblockaccess.getBlockState(blockpos).getValue(AXIS);
		float f = 0.125F;
		float f1 = 0.125F;
		if (enumfacing$axis == EnumFacing.Axis.X) {
			f = 0.5F;
		}

		if (enumfacing$axis == EnumFacing.Axis.Z) {
			f1 = 0.5F;
		}

		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
	}

	public static int getMetaForAxis(EnumFacing.Axis axis) {
		return axis == EnumFacing.Axis.X ? 1 : (axis == EnumFacing.Axis.Z ? 2 : 0);
	}

	public boolean isFullCube() {
		return false;
	}

	public boolean func_176548_d(World worldIn, BlockPos parBlockPos) {
		BlockPortal.Size blockportal$size = new BlockPortal.Size(worldIn, parBlockPos, EnumFacing.Axis.X);
		if (blockportal$size.func_150860_b() && blockportal$size.field_150864_e == 0) {
			blockportal$size.func_150859_c();
			return true;
		} else {
			BlockPortal.Size blockportal$size1 = new BlockPortal.Size(worldIn, parBlockPos, EnumFacing.Axis.Z);
			if (blockportal$size1.func_150860_b() && blockportal$size1.field_150864_e == 0) {
				blockportal$size1.func_150859_c();
				return true;
			} else {
				return false;
			}
		}
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis) iblockstate.getValue(AXIS);
		if (enumfacing$axis == EnumFacing.Axis.X) {
			BlockPortal.Size blockportal$size = new BlockPortal.Size(world, blockpos, EnumFacing.Axis.X);
			if (!blockportal$size.func_150860_b() || blockportal$size.field_150864_e < blockportal$size.field_150868_h
					* blockportal$size.field_150862_g) {
				world.setBlockState(blockpos, Blocks.air.getDefaultState());
			}
		} else if (enumfacing$axis == EnumFacing.Axis.Z) {
			BlockPortal.Size blockportal$size1 = new BlockPortal.Size(world, blockpos, EnumFacing.Axis.Z);
			if (!blockportal$size1.func_150860_b()
					|| blockportal$size1.field_150864_e < blockportal$size1.field_150868_h
							* blockportal$size1.field_150862_g) {
				world.setBlockState(blockpos, Blocks.air.getDefaultState());
			}
		}

	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, BlockPos blockpos, EnumFacing enumfacing) {
		EnumFacing.Axis enumfacing$axis = null;
		IBlockState iblockstate = iblockaccess.getBlockState(blockpos);
		if (iblockaccess.getBlockState(blockpos).getBlock() == this) {
			enumfacing$axis = (EnumFacing.Axis) iblockstate.getValue(AXIS);
			if (enumfacing$axis == null) {
				return false;
			}

			if (enumfacing$axis == EnumFacing.Axis.Z && enumfacing != EnumFacing.EAST
					&& enumfacing != EnumFacing.WEST) {
				return false;
			}

			if (enumfacing$axis == EnumFacing.Axis.X && enumfacing != EnumFacing.SOUTH
					&& enumfacing != EnumFacing.NORTH) {
				return false;
			}
		}

		boolean flag = iblockaccess.getBlockState(blockpos.west()).getBlock() == this
				&& iblockaccess.getBlockState(blockpos.west(2)).getBlock() != this;
		boolean flag1 = iblockaccess.getBlockState(blockpos.east()).getBlock() == this
				&& iblockaccess.getBlockState(blockpos.east(2)).getBlock() != this;
		boolean flag2 = iblockaccess.getBlockState(blockpos.north()).getBlock() == this
				&& iblockaccess.getBlockState(blockpos.north(2)).getBlock() != this;
		boolean flag3 = iblockaccess.getBlockState(blockpos.south()).getBlock() == this
				&& iblockaccess.getBlockState(blockpos.south(2)).getBlock() != this;
		boolean flag4 = flag || flag1 || enumfacing$axis == EnumFacing.Axis.X;
		boolean flag5 = flag2 || flag3 || enumfacing$axis == EnumFacing.Axis.Z;
		return flag4 && enumfacing == EnumFacing.WEST ? true
				: (flag4 && enumfacing == EnumFacing.EAST ? true
						: (flag5 && enumfacing == EnumFacing.NORTH ? true : flag5 && enumfacing == EnumFacing.SOUTH));
	}

	/**+
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	/**+
	 * Called When an Entity Collided with the Block
	 */
	public void onEntityCollidedWithBlock(World var1, BlockPos blockpos, IBlockState var3, Entity entity) {
		if (entity.ridingEntity == null && entity.riddenByEntity == null) {
			entity.func_181015_d(blockpos);
		}

	}

	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom random) {
		if (random.nextInt(100) == 0) {
			world.playSound((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D,
					(double) blockpos.getZ() + 0.5D, "portal.portal", 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
		}

		for (int i = 0; i < 4; ++i) {
			double d0 = (double) ((float) blockpos.getX() + random.nextFloat());
			double d1 = (double) ((float) blockpos.getY() + random.nextFloat());
			double d2 = (double) ((float) blockpos.getZ() + random.nextFloat());
			double d3 = ((double) random.nextFloat() - 0.5D) * 0.5D;
			double d4 = ((double) random.nextFloat() - 0.5D) * 0.5D;
			double d5 = ((double) random.nextFloat() - 0.5D) * 0.5D;
			int j = random.nextInt(2) * 2 - 1;
			if (world.getBlockState(blockpos.west()).getBlock() != this
					&& world.getBlockState(blockpos.east()).getBlock() != this) {
				d0 = (double) blockpos.getX() + 0.5D + 0.25D * (double) j;
				d3 = (double) (random.nextFloat() * 2.0F * (float) j);
			} else {
				d2 = (double) blockpos.getZ() + 0.5D + 0.25D * (double) j;
				d5 = (double) (random.nextFloat() * 2.0F * (float) j);
			}

			world.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, new int[0]);
		}

	}

	public Item getItem(World var1, BlockPos var2) {
		return null;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(AXIS, (i & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return getMetaForAxis((EnumFacing.Axis) iblockstate.getValue(AXIS));
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { AXIS });
	}

	public BlockPattern.PatternHelper func_181089_f(World parWorld, BlockPos parBlockPos) {
		EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
		BlockPortal.Size blockportal$size = new BlockPortal.Size(parWorld, parBlockPos, EnumFacing.Axis.X);
		EaglerLoadingCache loadingcache = BlockPattern.func_181627_a(parWorld, true);
		if (!blockportal$size.func_150860_b()) {
			enumfacing$axis = EnumFacing.Axis.X;
			blockportal$size = new BlockPortal.Size(parWorld, parBlockPos, EnumFacing.Axis.Z);
		}

		if (!blockportal$size.func_150860_b()) {
			return new BlockPattern.PatternHelper(parBlockPos, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
		} else {
			EnumFacing.AxisDirection[] axis = EnumFacing.AxisDirection._VALUES;
			int[] aint = new int[axis.length];
			EnumFacing enumfacing = blockportal$size.field_150866_c.rotateYCCW();
			BlockPos blockpos = blockportal$size.field_150861_f.up(blockportal$size.func_181100_a() - 1);

			for (int k = 0; k < axis.length; ++k) {
				EnumFacing.AxisDirection enumfacing$axisdirection = axis[k];
				BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper(
						enumfacing.getAxisDirection() == enumfacing$axisdirection ? blockpos
								: blockpos.offset(blockportal$size.field_150866_c,
										blockportal$size.func_181101_b() - 1),
						EnumFacing.func_181076_a(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP,
						loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);

				for (int i = 0; i < blockportal$size.func_181101_b(); ++i) {
					for (int j = 0; j < blockportal$size.func_181100_a(); ++j) {
						BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);
						if (blockworldstate.getBlockState() != null
								&& blockworldstate.getBlockState().getBlock().getMaterial() != Material.air) {
							++aint[enumfacing$axisdirection.ordinal()];
						}
					}
				}
			}

			EnumFacing.AxisDirection enumfacing$axisdirection1 = EnumFacing.AxisDirection.POSITIVE;

			for (int k = 0; k < axis.length; ++k) {
				EnumFacing.AxisDirection enumfacing$axisdirection2 = axis[k];
				if (aint[enumfacing$axisdirection2.ordinal()] < aint[enumfacing$axisdirection1.ordinal()]) {
					enumfacing$axisdirection1 = enumfacing$axisdirection2;
				}
			}

			return new BlockPattern.PatternHelper(
					enumfacing.getAxisDirection() == enumfacing$axisdirection1 ? blockpos
							: blockpos.offset(blockportal$size.field_150866_c, blockportal$size.func_181101_b() - 1),
					EnumFacing.func_181076_a(enumfacing$axisdirection1, enumfacing$axis), EnumFacing.UP, loadingcache,
					blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
		}
	}

	public static class Size {
		private final World world;
		private final EnumFacing.Axis axis;
		private final EnumFacing field_150866_c;
		private final EnumFacing field_150863_d;
		private int field_150864_e = 0;
		private BlockPos field_150861_f;
		private int field_150862_g;
		private int field_150868_h;

		public Size(World worldIn, BlockPos parBlockPos, EnumFacing.Axis parAxis) {
			this.world = worldIn;
			this.axis = parAxis;
			if (parAxis == EnumFacing.Axis.X) {
				this.field_150863_d = EnumFacing.EAST;
				this.field_150866_c = EnumFacing.WEST;
			} else {
				this.field_150863_d = EnumFacing.NORTH;
				this.field_150866_c = EnumFacing.SOUTH;
			}

			for (BlockPos blockpos = parBlockPos; parBlockPos.getY() > blockpos.getY() - 21 && parBlockPos.getY() > 0
					&& this.func_150857_a(
							worldIn.getBlockState(parBlockPos.down()).getBlock()); parBlockPos = parBlockPos.down()) {
				;
			}

			int i = this.func_180120_a(parBlockPos, this.field_150863_d) - 1;
			if (i >= 0) {
				this.field_150861_f = parBlockPos.offset(this.field_150863_d, i);
				this.field_150868_h = this.func_180120_a(this.field_150861_f, this.field_150866_c);
				if (this.field_150868_h < 2 || this.field_150868_h > 21) {
					this.field_150861_f = null;
					this.field_150868_h = 0;
				}
			}

			if (this.field_150861_f != null) {
				this.field_150862_g = this.func_150858_a();
			}

		}

		protected int func_180120_a(BlockPos parBlockPos, EnumFacing parEnumFacing) {
			int i;
			for (i = 0; i < 22; ++i) {
				BlockPos blockpos = parBlockPos.offset(parEnumFacing, i);
				if (!this.func_150857_a(this.world.getBlockState(blockpos).getBlock())
						|| this.world.getBlockState(blockpos.down()).getBlock() != Blocks.obsidian) {
					break;
				}
			}

			Block block = this.world.getBlockState(parBlockPos.offset(parEnumFacing, i)).getBlock();
			return block == Blocks.obsidian ? i : 0;
		}

		public int func_181100_a() {
			return this.field_150862_g;
		}

		public int func_181101_b() {
			return this.field_150868_h;
		}

		protected int func_150858_a() {
			label24: for (this.field_150862_g = 0; this.field_150862_g < 21; ++this.field_150862_g) {
				for (int i = 0; i < this.field_150868_h; ++i) {
					BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i).up(this.field_150862_g);
					Block block = this.world.getBlockState(blockpos).getBlock();
					if (!this.func_150857_a(block)) {
						break label24;
					}

					if (block == Blocks.portal) {
						++this.field_150864_e;
					}

					if (i == 0) {
						block = this.world.getBlockState(blockpos.offset(this.field_150863_d)).getBlock();
						if (block != Blocks.obsidian) {
							break label24;
						}
					} else if (i == this.field_150868_h - 1) {
						block = this.world.getBlockState(blockpos.offset(this.field_150866_c)).getBlock();
						if (block != Blocks.obsidian) {
							break label24;
						}
					}
				}
			}

			for (int j = 0; j < this.field_150868_h; ++j) {
				if (this.world.getBlockState(this.field_150861_f.offset(this.field_150866_c, j).up(this.field_150862_g))
						.getBlock() != Blocks.obsidian) {
					this.field_150862_g = 0;
					break;
				}
			}

			if (this.field_150862_g <= 21 && this.field_150862_g >= 3) {
				return this.field_150862_g;
			} else {
				this.field_150861_f = null;
				this.field_150868_h = 0;
				this.field_150862_g = 0;
				return 0;
			}
		}

		protected boolean func_150857_a(Block parBlock) {
			return parBlock.blockMaterial == Material.air || parBlock == Blocks.fire || parBlock == Blocks.portal;
		}

		public boolean func_150860_b() {
			return this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21
					&& this.field_150862_g >= 3 && this.field_150862_g <= 21;
		}

		public void func_150859_c() {
			for (int i = 0; i < this.field_150868_h; ++i) {
				BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i);

				for (int j = 0; j < this.field_150862_g; ++j) {
					this.world.setBlockState(blockpos.up(j),
							Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, this.axis), 2);
				}
			}

		}
	}
}