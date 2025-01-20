package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.carrotsearch.hppc.ObjectIntIdentityHashMap;
import com.carrotsearch.hppc.ObjectIntMap;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

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
public class BlockFire extends Block {
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
	public static final PropertyBool FLIP = PropertyBool.create("flip");
	public static final PropertyBool ALT = PropertyBool.create("alt");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyInteger UPPER = PropertyInteger.create("upper", 0, 2);
	private final ObjectIntMap<Block> encouragements = new ObjectIntIdentityHashMap<>();
	private final ObjectIntMap<Block> flammabilities = new ObjectIntIdentityHashMap<>();

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		int i = blockpos.getX();
		int j = blockpos.getY();
		int k = blockpos.getZ();
		if (!World.doesBlockHaveSolidTopSurface(iblockaccess, blockpos.down())
				&& !Blocks.fire.canCatchFire(iblockaccess, blockpos.down())) {
			boolean flag = (i + j + k & 1) == 1;
			boolean flag1 = (i / 2 + j / 2 + k / 2 & 1) == 1;
			int l = 0;
			if (this.canCatchFire(iblockaccess, blockpos.up())) {
				l = flag ? 1 : 2;
			}

			return iblockstate.withProperty(NORTH, Boolean.valueOf(this.canCatchFire(iblockaccess, blockpos.north())))
					.withProperty(EAST, Boolean.valueOf(this.canCatchFire(iblockaccess, blockpos.east())))
					.withProperty(SOUTH, Boolean.valueOf(this.canCatchFire(iblockaccess, blockpos.south())))
					.withProperty(WEST, Boolean.valueOf(this.canCatchFire(iblockaccess, blockpos.west())))
					.withProperty(UPPER, Integer.valueOf(l)).withProperty(FLIP, Boolean.valueOf(flag1))
					.withProperty(ALT, Boolean.valueOf(flag));
		} else {
			return this.getDefaultState();
		}
	}

	protected BlockFire() {
		super(Material.fire);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0))
				.withProperty(FLIP, Boolean.valueOf(false)).withProperty(ALT, Boolean.valueOf(false))
				.withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false))
				.withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false))
				.withProperty(UPPER, Integer.valueOf(0)));
		this.setTickRandomly(true);
	}

	public static void init() {
		Blocks.fire.setFireInfo(Blocks.planks, 5, 20);
		Blocks.fire.setFireInfo(Blocks.double_wooden_slab, 5, 20);
		Blocks.fire.setFireInfo(Blocks.wooden_slab, 5, 20);
		Blocks.fire.setFireInfo(Blocks.oak_fence_gate, 5, 20);
		Blocks.fire.setFireInfo(Blocks.spruce_fence_gate, 5, 20);
		Blocks.fire.setFireInfo(Blocks.birch_fence_gate, 5, 20);
		Blocks.fire.setFireInfo(Blocks.jungle_fence_gate, 5, 20);
		Blocks.fire.setFireInfo(Blocks.dark_oak_fence_gate, 5, 20);
		Blocks.fire.setFireInfo(Blocks.acacia_fence_gate, 5, 20);
		Blocks.fire.setFireInfo(Blocks.oak_fence, 5, 20);
		Blocks.fire.setFireInfo(Blocks.spruce_fence, 5, 20);
		Blocks.fire.setFireInfo(Blocks.birch_fence, 5, 20);
		Blocks.fire.setFireInfo(Blocks.jungle_fence, 5, 20);
		Blocks.fire.setFireInfo(Blocks.dark_oak_fence, 5, 20);
		Blocks.fire.setFireInfo(Blocks.acacia_fence, 5, 20);
		Blocks.fire.setFireInfo(Blocks.oak_stairs, 5, 20);
		Blocks.fire.setFireInfo(Blocks.birch_stairs, 5, 20);
		Blocks.fire.setFireInfo(Blocks.spruce_stairs, 5, 20);
		Blocks.fire.setFireInfo(Blocks.jungle_stairs, 5, 20);
		Blocks.fire.setFireInfo(Blocks.log, 5, 5);
		Blocks.fire.setFireInfo(Blocks.log2, 5, 5);
		Blocks.fire.setFireInfo(Blocks.leaves, 30, 60);
		Blocks.fire.setFireInfo(Blocks.leaves2, 30, 60);
		Blocks.fire.setFireInfo(Blocks.bookshelf, 30, 20);
		Blocks.fire.setFireInfo(Blocks.tnt, 15, 100);
		Blocks.fire.setFireInfo(Blocks.tallgrass, 60, 100);
		Blocks.fire.setFireInfo(Blocks.double_plant, 60, 100);
		Blocks.fire.setFireInfo(Blocks.yellow_flower, 60, 100);
		Blocks.fire.setFireInfo(Blocks.red_flower, 60, 100);
		Blocks.fire.setFireInfo(Blocks.deadbush, 60, 100);
		Blocks.fire.setFireInfo(Blocks.wool, 30, 60);
		Blocks.fire.setFireInfo(Blocks.vine, 15, 100);
		Blocks.fire.setFireInfo(Blocks.coal_block, 5, 5);
		Blocks.fire.setFireInfo(Blocks.hay_block, 60, 20);
		Blocks.fire.setFireInfo(Blocks.carpet, 60, 20);
	}

	public void setFireInfo(Block blockIn, int encouragement, int flammability) {
		this.encouragements.put(blockIn, encouragement);
		this.flammabilities.put(blockIn, flammability);
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

	public boolean isFullCube() {
		return false;
	}

	/**+
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	/**+
	 * How many world ticks before ticking
	 */
	public int tickRate(World var1) {
		return 30;
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		if (world.getGameRules().getBoolean("doFireTick")) {
			if (!this.canPlaceBlockAt(world, blockpos)) {
				world.setBlockToAir(blockpos);
			}

			Block block = world.getBlockState(blockpos.down()).getBlock();
			boolean flag = block == Blocks.netherrack;
			if (world.provider instanceof WorldProviderEnd && block == Blocks.bedrock) {
				flag = true;
			}

			if (!flag && world.isRaining() && this.canDie(world, blockpos)) {
				world.setBlockToAir(blockpos);
			} else {
				int i = ((Integer) iblockstate.getValue(AGE)).intValue();
				if (i < 15) {
					iblockstate = iblockstate.withProperty(AGE, Integer.valueOf(i + random.nextInt(3) / 2));
					world.setBlockState(blockpos, iblockstate, 4);
				}

				world.scheduleUpdate(blockpos, this, this.tickRate(world) + random.nextInt(10));
				if (!flag) {
					if (!this.canNeighborCatchFire(world, blockpos)) {
						if (!World.doesBlockHaveSolidTopSurface(world, blockpos.down()) || i > 3) {
							world.setBlockToAir(blockpos);
						}

						return;
					}

					if (!this.canCatchFire(world, blockpos.down()) && i == 15 && random.nextInt(4) == 0) {
						world.setBlockToAir(blockpos);
						return;
					}
				}

				boolean flag1 = world.isBlockinHighHumidity(blockpos);
				byte b0 = 0;
				if (flag1) {
					b0 = -50;
				}

				this.catchOnFire(world, blockpos.east(), 300 + b0, random, i);
				this.catchOnFire(world, blockpos.west(), 300 + b0, random, i);
				this.catchOnFire(world, blockpos.down(), 250 + b0, random, i);
				this.catchOnFire(world, blockpos.up(), 250 + b0, random, i);
				this.catchOnFire(world, blockpos.north(), 300 + b0, random, i);
				this.catchOnFire(world, blockpos.south(), 300 + b0, random, i);

				for (int j = -1; j <= 1; ++j) {
					for (int k = -1; k <= 1; ++k) {
						for (int l = -1; l <= 4; ++l) {
							if (j != 0 || l != 0 || k != 0) {
								int i1 = 100;
								if (l > 1) {
									i1 += (l - 1) * 100;
								}

								BlockPos blockpos1 = blockpos.add(j, l, k);
								if (!world.isBlockLoaded(blockpos1))
									continue;
								int j1 = this.getNeighborEncouragement(world, blockpos1);
								if (j1 > 0) {
									int k1 = (j1 + 40 + world.getDifficulty().getDifficultyId() * 7) / (i + 30);
									if (flag1) {
										k1 /= 2;
									}

									if (k1 > 0 && random.nextInt(i1) <= k1
											&& (!world.isRaining() || !this.canDie(world, blockpos1))) {
										int l1 = i + random.nextInt(5) / 4;
										if (l1 > 15) {
											l1 = 15;
										}

										world.setBlockState(blockpos1,
												iblockstate.withProperty(AGE, Integer.valueOf(l1)), 3);
									}
								}
							}
						}
					}
				}

			}
		}
	}

	protected boolean canDie(World worldIn, BlockPos pos) {
		return worldIn.canLightningStrike(pos) || worldIn.canLightningStrike(pos.west())
				|| worldIn.canLightningStrike(pos.east()) || worldIn.canLightningStrike(pos.north())
				|| worldIn.canLightningStrike(pos.south());
	}

	public boolean requiresUpdates() {
		return false;
	}

	private int getFlammability(Block blockIn) {
		return this.flammabilities.getOrDefault(blockIn, 0);
	}

	private int getEncouragement(Block blockIn) {
		return this.encouragements.getOrDefault(blockIn, 0);
	}

	private void catchOnFire(World worldIn, BlockPos pos, int chance, EaglercraftRandom random, int age) {
		IBlockState iblockstate = worldIn.getBlockStateIfLoaded(pos);
		if (iblockstate == null) {
			return;
		}
		int i = this.getFlammability(iblockstate.getBlock());
		if (random.nextInt(chance) < i) {
			if (random.nextInt(age + 10) < 5 && !worldIn.canLightningStrike(pos)) {
				int j = age + random.nextInt(5) / 4;
				if (j > 15) {
					j = 15;
				}

				worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, Integer.valueOf(j)), 3);
			} else {
				worldIn.setBlockToAir(pos);
			}

			if (iblockstate.getBlock() == Blocks.tnt) {
				Blocks.tnt.onBlockDestroyedByPlayer(worldIn, pos,
						iblockstate.withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)));
			}
		}

	}

	private boolean canNeighborCatchFire(World worldIn, BlockPos pos) {
		EnumFacing[] facings = EnumFacing._VALUES;
		for (int i = 0; i < facings.length; ++i) {
			EnumFacing enumfacing = facings[i];
			if (this.canCatchFire(worldIn, pos.offset(enumfacing))) {
				return true;
			}
		}

		return false;
	}

	private int getNeighborEncouragement(World worldIn, BlockPos pos) {
		if (!worldIn.isAirBlock(pos)) {
			return 0;
		} else {
			int i = 0;

			EnumFacing[] facings = EnumFacing._VALUES;
			BlockPos tmp = new BlockPos(0, 0, 0);
			for (int j = 0; j < facings.length; ++j) {
				IBlockState type = worldIn.getBlockStateIfLoaded(pos.offsetEvenFaster(facings[j], tmp));
				if (type != null) {
					i = Math.max(this.getEncouragement(type.getBlock()), i);
				}
			}

			return i;
		}
	}

	/**+
	 * Returns if this block is collidable (only used by Fire).
	 * Args: x, y, z
	 */
	public boolean isCollidable() {
		return false;
	}

	/**+
	 * Checks if the block can be caught on fire
	 */
	public boolean canCatchFire(IBlockAccess worldIn, BlockPos pos) {
		return this.getEncouragement(worldIn.getBlockState(pos).getBlock()) > 0;
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		return World.doesBlockHaveSolidTopSurface(world, blockpos.down()) || this.canNeighborCatchFire(world, blockpos);
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState var3, Block var4) {
		if (!World.doesBlockHaveSolidTopSurface(world, blockpos.down())
				&& !this.canNeighborCatchFire(world, blockpos)) {
			world.setBlockToAir(blockpos);
		}

	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState var3) {
		if (world.provider.getDimensionId() > 0 || !Blocks.portal.func_176548_d(world, blockpos)) {
			if (!World.doesBlockHaveSolidTopSurface(world, blockpos.down())
					&& !this.canNeighborCatchFire(world, blockpos)) {
				world.setBlockToAir(blockpos);
			} else {
				world.scheduleUpdate(blockpos, this, this.tickRate(world) + world.rand.nextInt(10));
			}
		}
	}

	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom random) {
		if (random.nextInt(24) == 0) {
			world.playSound((double) ((float) blockpos.getX() + 0.5F), (double) ((float) blockpos.getY() + 0.5F),
					(double) ((float) blockpos.getZ() + 0.5F), "fire.fire", 1.0F + random.nextFloat(),
					random.nextFloat() * 0.7F + 0.3F, false);
		}

		if (!World.doesBlockHaveSolidTopSurface(world, blockpos.down())
				&& !Blocks.fire.canCatchFire(world, blockpos.down())) {
			if (Blocks.fire.canCatchFire(world, blockpos.west())) {
				for (int j = 0; j < 2; ++j) {
					double d3 = (double) blockpos.getX() + random.nextDouble() * 0.10000000149011612D;
					double d8 = (double) blockpos.getY() + random.nextDouble();
					double d13 = (double) blockpos.getZ() + random.nextDouble();
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d8, d13, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}

			if (Blocks.fire.canCatchFire(world, blockpos.east())) {
				for (int k = 0; k < 2; ++k) {
					double d4 = (double) (blockpos.getX() + 1) - random.nextDouble() * 0.10000000149011612D;
					double d9 = (double) blockpos.getY() + random.nextDouble();
					double d14 = (double) blockpos.getZ() + random.nextDouble();
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d4, d9, d14, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}

			if (Blocks.fire.canCatchFire(world, blockpos.north())) {
				for (int l = 0; l < 2; ++l) {
					double d5 = (double) blockpos.getX() + random.nextDouble();
					double d10 = (double) blockpos.getY() + random.nextDouble();
					double d15 = (double) blockpos.getZ() + random.nextDouble() * 0.10000000149011612D;
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d5, d10, d15, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}

			if (Blocks.fire.canCatchFire(world, blockpos.south())) {
				for (int i1 = 0; i1 < 2; ++i1) {
					double d6 = (double) blockpos.getX() + random.nextDouble();
					double d11 = (double) blockpos.getY() + random.nextDouble();
					double d16 = (double) (blockpos.getZ() + 1) - random.nextDouble() * 0.10000000149011612D;
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d6, d11, d16, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}

			if (Blocks.fire.canCatchFire(world, blockpos.up())) {
				for (int j1 = 0; j1 < 2; ++j1) {
					double d7 = (double) blockpos.getX() + random.nextDouble();
					double d12 = (double) (blockpos.getY() + 1) - random.nextDouble() * 0.10000000149011612D;
					double d17 = (double) blockpos.getZ() + random.nextDouble();
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
		} else {
			for (int i = 0; i < 3; ++i) {
				double d0 = (double) blockpos.getX() + random.nextDouble();
				double d1 = (double) blockpos.getY() + random.nextDouble() * 0.5D + 0.5D;
				double d2 = (double) blockpos.getZ() + random.nextDouble();
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}

	}

	/**+
	 * Get the MapColor for this Block and the given BlockState
	 */
	public MapColor getMapColor(IBlockState var1) {
		return MapColor.tntColor;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(AGE)).intValue();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { AGE, NORTH, EAST, SOUTH, WEST, UPPER, FLIP, ALT });
	}
}