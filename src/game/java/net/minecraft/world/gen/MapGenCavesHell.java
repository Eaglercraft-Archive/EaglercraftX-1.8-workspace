package net.minecraft.world.gen;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
public class MapGenCavesHell extends MapGenBase {

	public MapGenCavesHell(boolean scramble) {
		super(scramble);
	}

	protected void func_180705_a(long parLong1, int parInt1, int parInt2, ChunkPrimer parChunkPrimer, double parDouble1,
			double parDouble2, double parDouble3) {
		this.func_180704_a(parLong1, parInt1, parInt2, parChunkPrimer, parDouble1, parDouble2, parDouble3,
				1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
	}

	protected void func_180704_a(long parLong1, int parInt1, int parInt2, ChunkPrimer parChunkPrimer, double parDouble1,
			double parDouble2, double parDouble3, float parFloat1, float parFloat2, float parFloat3, int parInt3,
			int parInt4, double parDouble4) {
		double d0 = (double) (parInt1 * 16 + 8);
		double d1 = (double) (parInt2 * 16 + 8);
		float f = 0.0F;
		float f1 = 0.0F;
		EaglercraftRandom random = new EaglercraftRandom(parLong1, this.rand.isScramble());
		if (parInt4 <= 0) {
			int i = this.range * 16 - 16;
			parInt4 = i - random.nextInt(i / 4);
		}

		boolean flag1 = false;
		if (parInt3 == -1) {
			parInt3 = parInt4 / 2;
			flag1 = true;
		}

		int j = random.nextInt(parInt4 / 2) + parInt4 / 4;

		for (boolean flag = random.nextInt(6) == 0; parInt3 < parInt4; ++parInt3) {
			double d2 = 1.5D
					+ (double) (MathHelper.sin((float) parInt3 * 3.1415927F / (float) parInt4) * parFloat1 * 1.0F);
			double d3 = d2 * parDouble4;
			float f2 = MathHelper.cos(parFloat3);
			float f3 = MathHelper.sin(parFloat3);
			parDouble1 += (double) (MathHelper.cos(parFloat2) * f2);
			parDouble2 += (double) f3;
			parDouble3 += (double) (MathHelper.sin(parFloat2) * f2);
			if (flag) {
				parFloat3 = parFloat3 * 0.92F;
			} else {
				parFloat3 = parFloat3 * 0.7F;
			}

			parFloat3 = parFloat3 + f1 * 0.1F;
			parFloat2 += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (!flag1 && parInt3 == j && parFloat1 > 1.0F) {
				this.func_180704_a(random.nextLong(), parInt1, parInt2, parChunkPrimer, parDouble1, parDouble2,
						parDouble3, random.nextFloat() * 0.5F + 0.5F, parFloat2 - 1.5707964F, parFloat3 / 3.0F, parInt3,
						parInt4, 1.0D);
				this.func_180704_a(random.nextLong(), parInt1, parInt2, parChunkPrimer, parDouble1, parDouble2,
						parDouble3, random.nextFloat() * 0.5F + 0.5F, parFloat2 + 1.5707964F, parFloat3 / 3.0F, parInt3,
						parInt4, 1.0D);
				return;
			}

			if (flag1 || random.nextInt(4) != 0) {
				double d4 = parDouble1 - d0;
				double d5 = parDouble3 - d1;
				double d6 = (double) (parInt4 - parInt3);
				double d7 = (double) (parFloat1 + 2.0F + 16.0F);
				if (d4 * d4 + d5 * d5 - d6 * d6 > d7 * d7) {
					return;
				}

				if (parDouble1 >= d0 - 16.0D - d2 * 2.0D && parDouble3 >= d1 - 16.0D - d2 * 2.0D
						&& parDouble1 <= d0 + 16.0D + d2 * 2.0D && parDouble3 <= d1 + 16.0D + d2 * 2.0D) {
					int j2 = MathHelper.floor_double(parDouble1 - d2) - parInt1 * 16 - 1;
					int k = MathHelper.floor_double(parDouble1 + d2) - parInt1 * 16 + 1;
					int k2 = MathHelper.floor_double(parDouble2 - d3) - 1;
					int l = MathHelper.floor_double(parDouble2 + d3) + 1;
					int l2 = MathHelper.floor_double(parDouble3 - d2) - parInt2 * 16 - 1;
					int i1 = MathHelper.floor_double(parDouble3 + d2) - parInt2 * 16 + 1;
					if (j2 < 0) {
						j2 = 0;
					}

					if (k > 16) {
						k = 16;
					}

					if (k2 < 1) {
						k2 = 1;
					}

					if (l > 120) {
						l = 120;
					}

					if (l2 < 0) {
						l2 = 0;
					}

					if (i1 > 16) {
						i1 = 16;
					}

					boolean flag2 = false;

					for (int j1 = j2; !flag2 && j1 < k; ++j1) {
						for (int k1 = l2; !flag2 && k1 < i1; ++k1) {
							for (int l1 = l + 1; !flag2 && l1 >= k2 - 1; --l1) {
								if (l1 >= 0 && l1 < 128) {
									IBlockState iblockstate = parChunkPrimer.getBlockState(j1, l1, k1);
									if (iblockstate.getBlock() == Blocks.flowing_lava
											|| iblockstate.getBlock() == Blocks.lava) {
										flag2 = true;
									}

									if (l1 != k2 - 1 && j1 != j2 && j1 != k - 1 && k1 != l2 && k1 != i1 - 1) {
										l1 = k2;
									}
								}
							}
						}
					}

					if (!flag2) {
						for (int i3 = j2; i3 < k; ++i3) {
							double d10 = ((double) (i3 + parInt1 * 16) + 0.5D - parDouble1) / d2;

							for (int j3 = l2; j3 < i1; ++j3) {
								double d8 = ((double) (j3 + parInt2 * 16) + 0.5D - parDouble3) / d2;

								for (int i2 = l; i2 > k2; --i2) {
									double d9 = ((double) (i2 - 1) + 0.5D - parDouble2) / d3;
									if (d9 > -0.7D && d10 * d10 + d9 * d9 + d8 * d8 < 1.0D) {
										IBlockState iblockstate1 = parChunkPrimer.getBlockState(i3, i2, j3);
										if (iblockstate1.getBlock() == Blocks.netherrack
												|| iblockstate1.getBlock() == Blocks.dirt
												|| iblockstate1.getBlock() == Blocks.grass) {
											parChunkPrimer.setBlockState(i3, i2, j3, Blocks.air.getDefaultState());
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
	protected void recursiveGenerate(World var1, int i, int j, int k, int l, ChunkPrimer chunkprimer) {
		int i1 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);
		if (this.rand.nextInt(5) != 0) {
			i1 = 0;
		}

		for (int j1 = 0; j1 < i1; ++j1) {
			double d0 = (double) (i * 16 + this.rand.nextInt(16));
			double d1 = (double) this.rand.nextInt(128);
			double d2 = (double) (j * 16 + this.rand.nextInt(16));
			int k1 = 1;
			if (this.rand.nextInt(4) == 0) {
				this.func_180705_a(this.rand.nextLong(), k, l, chunkprimer, d0, d1, d2);
				k1 += this.rand.nextInt(4);
			}

			for (int l1 = 0; l1 < k1; ++l1) {
				float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
				float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
				this.func_180704_a(this.rand.nextLong(), k, l, chunkprimer, d0, d1, d2, f2 * 2.0F, f, f1, 0, 0, 0.5D);
			}
		}

	}
}