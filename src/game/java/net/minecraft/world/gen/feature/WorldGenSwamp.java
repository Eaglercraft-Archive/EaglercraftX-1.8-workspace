package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
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
public class WorldGenSwamp extends WorldGenAbstractTree {
	private static final IBlockState field_181648_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT,
			BlockPlanks.EnumType.OAK);
	private static final IBlockState field_181649_b = Blocks.leaves.getDefaultState()
			.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
			.withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false));

	public WorldGenSwamp() {
		super(false);
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		int i;
		for (i = random.nextInt(4) + 5; world.getBlockState(blockpos.down()).getBlock()
				.getMaterial() == Material.water; blockpos = blockpos.down()) {
			;
		}

		boolean flag = true;
		if (blockpos.getY() >= 1 && blockpos.getY() + i + 1 <= 256) {
			for (int j = blockpos.getY(); j <= blockpos.getY() + 1 + i; ++j) {
				byte b0 = 1;
				if (j == blockpos.getY()) {
					b0 = 0;
				}

				if (j >= blockpos.getY() + 1 + i - 2) {
					b0 = 3;
				}

				BlockPos blockpos$mutableblockpos = new BlockPos();

				for (int k = blockpos.getX() - b0; k <= blockpos.getX() + b0 && flag; ++k) {
					for (int l = blockpos.getZ() - b0; l <= blockpos.getZ() + b0 && flag; ++l) {
						if (j >= 0 && j < 256) {
							Block block = world.getBlockState(blockpos$mutableblockpos.func_181079_c(k, j, l))
									.getBlock();
							if (block.getMaterial() != Material.air && block.getMaterial() != Material.leaves) {
								if (block != Blocks.water && block != Blocks.flowing_water) {
									flag = false;
								} else if (j > blockpos.getY()) {
									flag = false;
								}
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
				if ((block1 == Blocks.grass || block1 == Blocks.dirt) && blockpos.getY() < 256 - i - 1) {
					this.func_175921_a(world, blockpos.down());

					for (int k1 = blockpos.getY() - 3 + i; k1 <= blockpos.getY() + i; ++k1) {
						int j2 = k1 - (blockpos.getY() + i);
						int l2 = 2 - j2 / 2;

						for (int j3 = blockpos.getX() - l2; j3 <= blockpos.getX() + l2; ++j3) {
							int k3 = j3 - blockpos.getX();

							for (int i1 = blockpos.getZ() - l2; i1 <= blockpos.getZ() + l2; ++i1) {
								int j1 = i1 - blockpos.getZ();
								if (Math.abs(k3) != l2 || Math.abs(j1) != l2 || random.nextInt(2) != 0 && j2 != 0) {
									BlockPos blockpos1 = new BlockPos(j3, k1, i1);
									if (!world.getBlockState(blockpos1).getBlock().isFullBlock()) {
										this.setBlockAndNotifyAdequately(world, blockpos1, field_181649_b);
									}
								}
							}
						}
					}

					for (int l1 = 0; l1 < i; ++l1) {
						Block block2 = world.getBlockState(blockpos.up(l1)).getBlock();
						if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves
								|| block2 == Blocks.flowing_water || block2 == Blocks.water) {
							this.setBlockAndNotifyAdequately(world, blockpos.up(l1), field_181648_a);
						}
					}

					for (int i2 = blockpos.getY() - 3 + i; i2 <= blockpos.getY() + i; ++i2) {
						int k2 = i2 - (blockpos.getY() + i);
						int i3 = 2 - k2 / 2;
						BlockPos blockpos$mutableblockpos1 = new BlockPos();

						for (int l3 = blockpos.getX() - i3; l3 <= blockpos.getX() + i3; ++l3) {
							for (int i4 = blockpos.getZ() - i3; i4 <= blockpos.getZ() + i3; ++i4) {
								blockpos$mutableblockpos1.func_181079_c(l3, i2, i4);
								if (world.getBlockState(blockpos$mutableblockpos1).getBlock()
										.getMaterial() == Material.leaves) {
									BlockPos blockpos4 = blockpos$mutableblockpos1.west();
									BlockPos blockpos5 = blockpos$mutableblockpos1.east();
									BlockPos blockpos2 = blockpos$mutableblockpos1.north();
									BlockPos blockpos3 = blockpos$mutableblockpos1.south();
									if (random.nextInt(4) == 0 && world.getBlockState(blockpos4).getBlock()
											.getMaterial() == Material.air) {
										this.func_181647_a(world, blockpos4, BlockVine.EAST);
									}

									if (random.nextInt(4) == 0 && world.getBlockState(blockpos5).getBlock()
											.getMaterial() == Material.air) {
										this.func_181647_a(world, blockpos5, BlockVine.WEST);
									}

									if (random.nextInt(4) == 0 && world.getBlockState(blockpos2).getBlock()
											.getMaterial() == Material.air) {
										this.func_181647_a(world, blockpos2, BlockVine.SOUTH);
									}

									if (random.nextInt(4) == 0 && world.getBlockState(blockpos3).getBlock()
											.getMaterial() == Material.air) {
										this.func_181647_a(world, blockpos3, BlockVine.NORTH);
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

	private void func_181647_a(World parWorld, BlockPos parBlockPos, PropertyBool parPropertyBool) {
		IBlockState iblockstate = Blocks.vine.getDefaultState().withProperty(parPropertyBool, Boolean.valueOf(true));
		this.setBlockAndNotifyAdequately(parWorld, parBlockPos, iblockstate);
		int i = 4;

		for (parBlockPos = parBlockPos
				.down(); parWorld.getBlockState(parBlockPos).getBlock().getMaterial() == Material.air && i > 0; --i) {
			this.setBlockAndNotifyAdequately(parWorld, parBlockPos, iblockstate);
			parBlockPos = parBlockPos.down();
		}

	}
}