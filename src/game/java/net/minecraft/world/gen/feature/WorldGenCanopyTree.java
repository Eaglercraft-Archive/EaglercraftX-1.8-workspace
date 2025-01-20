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
public class WorldGenCanopyTree extends WorldGenAbstractTree {
	private static final IBlockState field_181640_a = Blocks.log2.getDefaultState().withProperty(BlockNewLog.VARIANT,
			BlockPlanks.EnumType.DARK_OAK);
	private static final IBlockState field_181641_b = Blocks.leaves2.getDefaultState()
			.withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK)
			.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

	public WorldGenCanopyTree(boolean parFlag) {
		super(parFlag);
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		int i = random.nextInt(3) + random.nextInt(2) + 6;
		int j = blockpos.getX();
		int k = blockpos.getY();
		int l = blockpos.getZ();
		if (k >= 1 && k + i + 1 < 256) {
			BlockPos blockpos1 = blockpos.down();
			Block block = world.getBlockState(blockpos1).getBlock();
			if (block != Blocks.grass && block != Blocks.dirt) {
				return false;
			} else if (!this.func_181638_a(world, blockpos, i)) {
				return false;
			} else {
				this.func_175921_a(world, blockpos1);
				this.func_175921_a(world, blockpos1.east());
				this.func_175921_a(world, blockpos1.south());
				this.func_175921_a(world, blockpos1.south().east());
				EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(random);
				int i1 = i - random.nextInt(4);
				int j1 = 2 - random.nextInt(3);
				int k1 = j;
				int l1 = l;
				int i2 = k + i - 1;

				for (int j2 = 0; j2 < i; ++j2) {
					if (j2 >= i1 && j1 > 0) {
						k1 += enumfacing.getFrontOffsetX();
						l1 += enumfacing.getFrontOffsetZ();
						--j1;
					}

					int k2 = k + j2;
					BlockPos blockpos2 = new BlockPos(k1, k2, l1);
					Material material = world.getBlockState(blockpos2).getBlock().getMaterial();
					if (material == Material.air || material == Material.leaves) {
						this.func_181639_b(world, blockpos2);
						this.func_181639_b(world, blockpos2.east());
						this.func_181639_b(world, blockpos2.south());
						this.func_181639_b(world, blockpos2.east().south());
					}
				}

				for (int i3 = -2; i3 <= 0; ++i3) {
					for (int l3 = -2; l3 <= 0; ++l3) {
						byte b0 = -1;
						this.func_150526_a(world, k1 + i3, i2 + b0, l1 + l3);
						this.func_150526_a(world, 1 + k1 - i3, i2 + b0, l1 + l3);
						this.func_150526_a(world, k1 + i3, i2 + b0, 1 + l1 - l3);
						this.func_150526_a(world, 1 + k1 - i3, i2 + b0, 1 + l1 - l3);
						if ((i3 > -2 || l3 > -1) && (i3 != -1 || l3 != -2)) {
							b0 = 1;
							this.func_150526_a(world, k1 + i3, i2 + b0, l1 + l3);
							this.func_150526_a(world, 1 + k1 - i3, i2 + b0, l1 + l3);
							this.func_150526_a(world, k1 + i3, i2 + b0, 1 + l1 - l3);
							this.func_150526_a(world, 1 + k1 - i3, i2 + b0, 1 + l1 - l3);
						}
					}
				}

				if (random.nextBoolean()) {
					this.func_150526_a(world, k1, i2 + 2, l1);
					this.func_150526_a(world, k1 + 1, i2 + 2, l1);
					this.func_150526_a(world, k1 + 1, i2 + 2, l1 + 1);
					this.func_150526_a(world, k1, i2 + 2, l1 + 1);
				}

				for (int j3 = -3; j3 <= 4; ++j3) {
					for (int i4 = -3; i4 <= 4; ++i4) {
						if ((j3 != -3 || i4 != -3) && (j3 != -3 || i4 != 4) && (j3 != 4 || i4 != -3)
								&& (j3 != 4 || i4 != 4) && (Math.abs(j3) < 3 || Math.abs(i4) < 3)) {
							this.func_150526_a(world, k1 + j3, i2, l1 + i4);
						}
					}
				}

				for (int k3 = -1; k3 <= 2; ++k3) {
					for (int j4 = -1; j4 <= 2; ++j4) {
						if ((k3 < 0 || k3 > 1 || j4 < 0 || j4 > 1) && random.nextInt(3) <= 0) {
							int k4 = random.nextInt(3) + 2;

							for (int l4 = 0; l4 < k4; ++l4) {
								this.func_181639_b(world, new BlockPos(j + k3, i2 - l4 - 1, l + j4));
							}

							for (int i5 = -1; i5 <= 1; ++i5) {
								for (int l2 = -1; l2 <= 1; ++l2) {
									this.func_150526_a(world, k1 + k3 + i5, i2, l1 + j4 + l2);
								}
							}

							for (int j5 = -2; j5 <= 2; ++j5) {
								for (int k5 = -2; k5 <= 2; ++k5) {
									if (Math.abs(j5) != 2 || Math.abs(k5) != 2) {
										this.func_150526_a(world, k1 + k3 + j5, i2 - 1, l1 + j4 + k5);
									}
								}
							}
						}
					}
				}

				return true;
			}
		} else {
			return false;
		}
	}

	private boolean func_181638_a(World parWorld, BlockPos parBlockPos, int parInt1) {
		int i = parBlockPos.getX();
		int j = parBlockPos.getY();
		int k = parBlockPos.getZ();
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int l = 0; l <= parInt1 + 1; ++l) {
			byte b0 = 1;
			if (l == 0) {
				b0 = 0;
			}

			if (l >= parInt1 - 1) {
				b0 = 2;
			}

			for (int i1 = -b0; i1 <= b0; ++i1) {
				for (int j1 = -b0; j1 <= b0; ++j1) {
					if (!this.func_150523_a(parWorld
							.getBlockState(blockpos$mutableblockpos.func_181079_c(i + i1, j + l, k + j1)).getBlock())) {
						return false;
					}
				}
			}
		}

		return true;
	}

	private void func_181639_b(World parWorld, BlockPos parBlockPos) {
		if (this.func_150523_a(parWorld.getBlockState(parBlockPos).getBlock())) {
			this.setBlockAndNotifyAdequately(parWorld, parBlockPos, field_181640_a);
		}

	}

	private void func_150526_a(World worldIn, int parInt1, int parInt2, int parInt3) {
		BlockPos blockpos = new BlockPos(parInt1, parInt2, parInt3);
		Block block = worldIn.getBlockState(blockpos).getBlock();
		if (block.getMaterial() == Material.air) {
			this.setBlockAndNotifyAdequately(worldIn, blockpos, field_181641_b);
		}

	}
}