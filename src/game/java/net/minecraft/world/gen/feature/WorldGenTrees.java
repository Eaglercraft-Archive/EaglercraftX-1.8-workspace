package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
public class WorldGenTrees extends WorldGenAbstractTree {
	private static final IBlockState field_181653_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT,
			BlockPlanks.EnumType.OAK);
	private static final IBlockState field_181654_b = Blocks.leaves.getDefaultState()
			.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
			.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
	private final int minTreeHeight;
	private final boolean vinesGrow;
	private final IBlockState metaWood;
	private final IBlockState metaLeaves;

	public WorldGenTrees(boolean parFlag) {
		this(parFlag, 4, field_181653_a, field_181654_b, false);
	}

	public WorldGenTrees(boolean parFlag, int parInt1, IBlockState parIBlockState, IBlockState parIBlockState2,
			boolean parFlag2) {
		super(parFlag);
		this.minTreeHeight = parInt1;
		this.metaWood = parIBlockState;
		this.metaLeaves = parIBlockState2;
		this.vinesGrow = parFlag2;
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		int i = random.nextInt(3) + this.minTreeHeight;
		boolean flag = true;
		if (blockpos.getY() >= 1 && blockpos.getY() + i + 1 <= 256) {
			for (int j = blockpos.getY(); j <= blockpos.getY() + 1 + i; ++j) {
				byte b0 = 1;
				if (j == blockpos.getY()) {
					b0 = 0;
				}

				if (j >= blockpos.getY() + 1 + i - 2) {
					b0 = 2;
				}

				BlockPos blockpos$mutableblockpos = new BlockPos();

				for (int k = blockpos.getX() - b0; k <= blockpos.getX() + b0 && flag; ++k) {
					for (int l = blockpos.getZ() - b0; l <= blockpos.getZ() + b0 && flag; ++l) {
						if (j >= 0 && j < 256) {
							if (!this.func_150523_a(
									world.getBlockState(blockpos$mutableblockpos.func_181079_c(k, j, l)).getBlock())) {
								flag = false;
							}
						} else {
							flag = false;
						}
					}
				}
			}

			if (!flag) {
				return false;
			} else {
				Block block1 = world.getBlockState(blockpos.down()).getBlock();
				if ((block1 == Blocks.grass || block1 == Blocks.dirt || block1 == Blocks.farmland)
						&& blockpos.getY() < 256 - i - 1) {
					this.func_175921_a(world, blockpos.down());
					byte b1 = 3;
					byte b2 = 0;

					for (int j2 = blockpos.getY() - b1 + i; j2 <= blockpos.getY() + i; ++j2) {
						int j3 = j2 - (blockpos.getY() + i);
						int i1 = b2 + 1 - j3 / 2;

						for (int j1 = blockpos.getX() - i1; j1 <= blockpos.getX() + i1; ++j1) {
							int k1 = j1 - blockpos.getX();

							for (int l1 = blockpos.getZ() - i1; l1 <= blockpos.getZ() + i1; ++l1) {
								int i2 = l1 - blockpos.getZ();
								if (Math.abs(k1) != i1 || Math.abs(i2) != i1 || random.nextInt(2) != 0 && j3 != 0) {
									BlockPos blockpos1 = new BlockPos(j1, j2, l1);
									Block block = world.getBlockState(blockpos1).getBlock();
									if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves
											|| block.getMaterial() == Material.vine) {
										this.setBlockAndNotifyAdequately(world, blockpos1, this.metaLeaves);
									}
								}
							}
						}
					}

					for (int k2 = 0; k2 < i; ++k2) {
						Block block2 = world.getBlockState(blockpos.up(k2)).getBlock();
						if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves
								|| block2.getMaterial() == Material.vine) {
							this.setBlockAndNotifyAdequately(world, blockpos.up(k2), this.metaWood);
							if (this.vinesGrow && k2 > 0) {
								if (random.nextInt(3) > 0 && world.isAirBlock(blockpos.add(-1, k2, 0))) {
									this.func_181651_a(world, blockpos.add(-1, k2, 0), BlockVine.EAST);
								}

								if (random.nextInt(3) > 0 && world.isAirBlock(blockpos.add(1, k2, 0))) {
									this.func_181651_a(world, blockpos.add(1, k2, 0), BlockVine.WEST);
								}

								if (random.nextInt(3) > 0 && world.isAirBlock(blockpos.add(0, k2, -1))) {
									this.func_181651_a(world, blockpos.add(0, k2, -1), BlockVine.SOUTH);
								}

								if (random.nextInt(3) > 0 && world.isAirBlock(blockpos.add(0, k2, 1))) {
									this.func_181651_a(world, blockpos.add(0, k2, 1), BlockVine.NORTH);
								}
							}
						}
					}

					if (this.vinesGrow) {
						for (int l2 = blockpos.getY() - 3 + i; l2 <= blockpos.getY() + i; ++l2) {
							int k3 = l2 - (blockpos.getY() + i);
							int l3 = 2 - k3 / 2;
							BlockPos blockpos$mutableblockpos1 = new BlockPos();

							for (int i4 = blockpos.getX() - l3; i4 <= blockpos.getX() + l3; ++i4) {
								for (int j4 = blockpos.getZ() - l3; j4 <= blockpos.getZ() + l3; ++j4) {
									blockpos$mutableblockpos1.func_181079_c(i4, l2, j4);
									if (world.getBlockState(blockpos$mutableblockpos1).getBlock()
											.getMaterial() == Material.leaves) {
										BlockPos blockpos3 = blockpos$mutableblockpos1.west();
										BlockPos blockpos4 = blockpos$mutableblockpos1.east();
										BlockPos blockpos5 = blockpos$mutableblockpos1.north();
										BlockPos blockpos2 = blockpos$mutableblockpos1.south();
										if (random.nextInt(4) == 0 && world.getBlockState(blockpos3).getBlock()
												.getMaterial() == Material.air) {
											this.func_181650_b(world, blockpos3, BlockVine.EAST);
										}

										if (random.nextInt(4) == 0 && world.getBlockState(blockpos4).getBlock()
												.getMaterial() == Material.air) {
											this.func_181650_b(world, blockpos4, BlockVine.WEST);
										}

										if (random.nextInt(4) == 0 && world.getBlockState(blockpos5).getBlock()
												.getMaterial() == Material.air) {
											this.func_181650_b(world, blockpos5, BlockVine.SOUTH);
										}

										if (random.nextInt(4) == 0 && world.getBlockState(blockpos2).getBlock()
												.getMaterial() == Material.air) {
											this.func_181650_b(world, blockpos2, BlockVine.NORTH);
										}
									}
								}
							}
						}

						if (random.nextInt(5) == 0 && i > 5) {
							for (int i3 = 0; i3 < 2; ++i3) {
								EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
								for (int m = 0; m < facings.length; ++m) {
									EnumFacing enumfacing = facings[m];
									if (random.nextInt(4 - i3) == 0) {
										EnumFacing enumfacing1 = enumfacing.getOpposite();
										this.func_181652_a(world, random.nextInt(3),
												blockpos.add(enumfacing1.getFrontOffsetX(), i - 5 + i3,
														enumfacing1.getFrontOffsetZ()),
												enumfacing);
									}
								}
							}
						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	private void func_181652_a(World parWorld, int parInt1, BlockPos parBlockPos, EnumFacing parEnumFacing) {
		this.setBlockAndNotifyAdequately(parWorld, parBlockPos, Blocks.cocoa.getDefaultState()
				.withProperty(BlockCocoa.AGE, Integer.valueOf(parInt1)).withProperty(BlockCocoa.FACING, parEnumFacing));
	}

	private void func_181651_a(World parWorld, BlockPos parBlockPos, PropertyBool parPropertyBool) {
		this.setBlockAndNotifyAdequately(parWorld, parBlockPos,
				Blocks.vine.getDefaultState().withProperty(parPropertyBool, Boolean.valueOf(true)));
	}

	private void func_181650_b(World parWorld, BlockPos parBlockPos, PropertyBool parPropertyBool) {
		this.func_181651_a(parWorld, parBlockPos, parPropertyBool);
		int i = 4;

		for (parBlockPos = parBlockPos
				.down(); parWorld.getBlockState(parBlockPos).getBlock().getMaterial() == Material.air && i > 0; --i) {
			this.func_181651_a(parWorld, parBlockPos, parPropertyBool);
			parBlockPos = parBlockPos.down();
		}

	}
}