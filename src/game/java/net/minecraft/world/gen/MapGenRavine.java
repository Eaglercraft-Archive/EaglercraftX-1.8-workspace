package net.minecraft.world.gen;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

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
public class MapGenRavine extends MapGenBase {
	private float[] field_75046_d = new float[1024];

	public MapGenRavine(boolean scramble) {
		super(scramble);
	}

	protected void func_180707_a(long parLong1, int parInt1, int parInt2, ChunkPrimer parChunkPrimer, double parDouble1,
			double parDouble2, double parDouble3, float parFloat1, float parFloat2, float parFloat3, int parInt3,
			int parInt4, double parDouble4) {
		EaglercraftRandom random = new EaglercraftRandom(parLong1, this.rand.isScramble());
		double d0 = (double) (parInt1 * 16 + 8);
		double d1 = (double) (parInt2 * 16 + 8);
		float f = 0.0F;
		float f1 = 0.0F;
		if (parInt4 <= 0) {
			int i = this.range * 16 - 16;
			parInt4 = i - random.nextInt(i / 4);
		}

		boolean flag1 = false;
		if (parInt3 == -1) {
			parInt3 = parInt4 / 2;
			flag1 = true;
		}

		float f2 = 1.0F;

		for (int j = 0; j < 256; ++j) {
			if (j == 0 || random.nextInt(3) == 0) {
				f2 = 1.0F + random.nextFloat() * random.nextFloat() * 1.0F;
			}

			this.field_75046_d[j] = f2 * f2;
		}

		for (; parInt3 < parInt4; ++parInt3) {
			double d9 = 1.5D
					+ (double) (MathHelper.sin((float) parInt3 * 3.1415927F / (float) parInt4) * parFloat1 * 1.0F);
			double d2 = d9 * parDouble4;
			d9 = d9 * ((double) random.nextFloat() * 0.25D + 0.75D);
			d2 = d2 * ((double) random.nextFloat() * 0.25D + 0.75D);
			float f3 = MathHelper.cos(parFloat3);
			float f4 = MathHelper.sin(parFloat3);
			parDouble1 += (double) (MathHelper.cos(parFloat2) * f3);
			parDouble2 += (double) f4;
			parDouble3 += (double) (MathHelper.sin(parFloat2) * f3);
			parFloat3 = parFloat3 * 0.7F;
			parFloat3 = parFloat3 + f1 * 0.05F;
			parFloat2 += f * 0.05F;
			f1 = f1 * 0.8F;
			f = f * 0.5F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (flag1 || random.nextInt(4) != 0) {
				double d3 = parDouble1 - d0;
				double d4 = parDouble3 - d1;
				double d5 = (double) (parInt4 - parInt3);
				double d6 = (double) (parFloat1 + 2.0F + 16.0F);
				if (d3 * d3 + d4 * d4 - d5 * d5 > d6 * d6) {
					return;
				}

				if (parDouble1 >= d0 - 16.0D - d9 * 2.0D && parDouble3 >= d1 - 16.0D - d9 * 2.0D
						&& parDouble1 <= d0 + 16.0D + d9 * 2.0D && parDouble3 <= d1 + 16.0D + d9 * 2.0D) {
					int k2 = MathHelper.floor_double(parDouble1 - d9) - parInt1 * 16 - 1;
					int k = MathHelper.floor_double(parDouble1 + d9) - parInt1 * 16 + 1;
					int l2 = MathHelper.floor_double(parDouble2 - d2) - 1;
					int l = MathHelper.floor_double(parDouble2 + d2) + 1;
					int i3 = MathHelper.floor_double(parDouble3 - d9) - parInt2 * 16 - 1;
					int i1 = MathHelper.floor_double(parDouble3 + d9) - parInt2 * 16 + 1;
					if (k2 < 0) {
						k2 = 0;
					}

					if (k > 16) {
						k = 16;
					}

					if (l2 < 1) {
						l2 = 1;
					}

					if (l > 248) {
						l = 248;
					}

					if (i3 < 0) {
						i3 = 0;
					}

					if (i1 > 16) {
						i1 = 16;
					}

					boolean flag2 = false;

					for (int j1 = k2; !flag2 && j1 < k; ++j1) {
						for (int k1 = i3; !flag2 && k1 < i1; ++k1) {
							for (int l1 = l + 1; !flag2 && l1 >= l2 - 1; --l1) {
								if (l1 >= 0 && l1 < 256) {
									IBlockState iblockstate = parChunkPrimer.getBlockState(j1, l1, k1);
									if (iblockstate.getBlock() == Blocks.flowing_water
											|| iblockstate.getBlock() == Blocks.water) {
										flag2 = true;
									}

									if (l1 != l2 - 1 && j1 != k2 && j1 != k - 1 && k1 != i3 && k1 != i1 - 1) {
										l1 = l2;
									}
								}
							}
						}
					}

					if (!flag2) {
						BlockPos blockpos$mutableblockpos = new BlockPos();

						for (int j3 = k2; j3 < k; ++j3) {
							double d10 = ((double) (j3 + parInt1 * 16) + 0.5D - parDouble1) / d9;

							for (int i2 = i3; i2 < i1; ++i2) {
								double d7 = ((double) (i2 + parInt2 * 16) + 0.5D - parDouble3) / d9;
								boolean flag = false;
								if (d10 * d10 + d7 * d7 < 1.0D) {
									for (int j2 = l; j2 > l2; --j2) {
										double d8 = ((double) (j2 - 1) + 0.5D - parDouble2) / d2;
										if ((d10 * d10 + d7 * d7) * (double) this.field_75046_d[j2 - 1]
												+ d8 * d8 / 6.0D < 1.0D) {
											IBlockState iblockstate1 = parChunkPrimer.getBlockState(j3, j2, i2);
											if (iblockstate1.getBlock() == Blocks.grass) {
												flag = true;
											}

											if (iblockstate1.getBlock() == Blocks.stone
													|| iblockstate1.getBlock() == Blocks.dirt
													|| iblockstate1.getBlock() == Blocks.grass) {
												if (j2 - 1 < 10) {
													parChunkPrimer.setBlockState(j3, j2, i2,
															Blocks.flowing_lava.getDefaultState());
												} else {
													parChunkPrimer.setBlockState(j3, j2, i2,
															Blocks.air.getDefaultState());
													if (flag && parChunkPrimer.getBlockState(j3, j2 - 1, i2)
															.getBlock() == Blocks.dirt) {
														blockpos$mutableblockpos.func_181079_c(j3 + parInt1 * 16, 0,
																i2 + parInt2 * 16);
														parChunkPrimer.setBlockState(j3, j2 - 1, i2,
																this.worldObj.getBiomeGenForCoords(
																		blockpos$mutableblockpos).topBlock);
													}
												}
											}
										}
									}
								}
							}
						}

						if (flag1) {
							break;
						}
					}
				}
			}
		}

	}

	/**+
	 * Recursively called by generate()
	 */
	protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int chunkPrimerIn, int parInt4,
			ChunkPrimer parChunkPrimer) {
		if (this.rand.nextInt(50) == 0) {
			double d0 = (double) (chunkX * 16 + this.rand.nextInt(16));
			double d1 = (double) (this.rand.nextInt(this.rand.nextInt(40) + 8) + 20);
			double d2 = (double) (chunkZ * 16 + this.rand.nextInt(16));
			byte b0 = 1;

			for (int i = 0; i < b0; ++i) {
				float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
				float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float f2 = (this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F;
				this.func_180707_a(this.rand.nextLong(), chunkPrimerIn, parInt4, parChunkPrimer, d0, d1, d2, f2, f, f1,
						0, 0, 3.0D);
			}

		}
	}
}