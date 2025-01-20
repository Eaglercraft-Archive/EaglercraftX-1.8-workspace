package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
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
public class WorldGenSavannaTree extends WorldGenAbstractTree {
	private static final IBlockState field_181643_a = Blocks.log2.getDefaultState().withProperty(BlockNewLog.VARIANT,
			BlockPlanks.EnumType.ACACIA);
	private static final IBlockState field_181644_b = Blocks.leaves2.getDefaultState()
			.withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA)
			.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

	public WorldGenSavannaTree(boolean parFlag) {
		super(parFlag);
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		int i = random.nextInt(3) + random.nextInt(3) + 5;
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
				Block block = world.getBlockState(blockpos.down()).getBlock();
				if ((block == Blocks.grass || block == Blocks.dirt) && blockpos.getY() < 256 - i - 1) {
					this.func_175921_a(world, blockpos.down());
					EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(random);
					int j2 = i - random.nextInt(4) - 1;
					int k2 = 3 - random.nextInt(3);
					int l2 = blockpos.getX();
					int i1 = blockpos.getZ();
					int j1 = 0;

					for (int k1 = 0; k1 < i; ++k1) {
						int l1 = blockpos.getY() + k1;
						if (k1 >= j2 && k2 > 0) {
							l2 += enumfacing.getFrontOffsetX();
							i1 += enumfacing.getFrontOffsetZ();
							--k2;
						}

						BlockPos blockpos1 = new BlockPos(l2, l1, i1);
						Material material = world.getBlockState(blockpos1).getBlock().getMaterial();
						if (material == Material.air || material == Material.leaves) {
							this.func_181642_b(world, blockpos1);
							j1 = l1;
						}
					}

					BlockPos blockpos3 = new BlockPos(l2, j1, i1);

					for (int i3 = -3; i3 <= 3; ++i3) {
						for (int l3 = -3; l3 <= 3; ++l3) {
							if (Math.abs(i3) != 3 || Math.abs(l3) != 3) {
								this.func_175924_b(world, blockpos3.add(i3, 0, l3));
							}
						}
					}

					blockpos3 = blockpos3.up();

					for (int j3 = -1; j3 <= 1; ++j3) {
						for (int i4 = -1; i4 <= 1; ++i4) {
							this.func_175924_b(world, blockpos3.add(j3, 0, i4));
						}
					}

					this.func_175924_b(world, blockpos3.east(2));
					this.func_175924_b(world, blockpos3.west(2));
					this.func_175924_b(world, blockpos3.south(2));
					this.func_175924_b(world, blockpos3.north(2));
					l2 = blockpos.getX();
					i1 = blockpos.getZ();
					EnumFacing enumfacing1 = EnumFacing.Plane.HORIZONTAL.random(random);
					if (enumfacing1 != enumfacing) {
						int k3 = j2 - random.nextInt(2) - 1;
						int j4 = 1 + random.nextInt(3);
						j1 = 0;

						for (int k4 = k3; k4 < i && j4 > 0; --j4) {
							if (k4 >= 1) {
								int i2 = blockpos.getY() + k4;
								l2 += enumfacing1.getFrontOffsetX();
								i1 += enumfacing1.getFrontOffsetZ();
								BlockPos blockpos2 = new BlockPos(l2, i2, i1);
								Material material1 = world.getBlockState(blockpos2).getBlock().getMaterial();
								if (material1 == Material.air || material1 == Material.leaves) {
									this.func_181642_b(world, blockpos2);
									j1 = i2;
								}
							}

							++k4;
						}

						if (j1 > 0) {
							BlockPos blockpos4 = new BlockPos(l2, j1, i1);

							for (int l4 = -2; l4 <= 2; ++l4) {
								for (int j5 = -2; j5 <= 2; ++j5) {
									if (Math.abs(l4) != 2 || Math.abs(j5) != 2) {
										this.func_175924_b(world, blockpos4.add(l4, 0, j5));
									}
								}
							}

							blockpos4 = blockpos4.up();

							for (int i5 = -1; i5 <= 1; ++i5) {
								for (int k5 = -1; k5 <= 1; ++k5) {
									this.func_175924_b(world, blockpos4.add(i5, 0, k5));
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

	private void func_181642_b(World parWorld, BlockPos parBlockPos) {
		this.setBlockAndNotifyAdequately(parWorld, parBlockPos, field_181643_a);
	}

	private void func_175924_b(World worldIn, BlockPos parBlockPos) {
		Material material = worldIn.getBlockState(parBlockPos).getBlock().getMaterial();
		if (material == Material.air || material == Material.leaves) {
			this.setBlockAndNotifyAdequately(worldIn, parBlockPos, field_181644_b);
		}

	}
}