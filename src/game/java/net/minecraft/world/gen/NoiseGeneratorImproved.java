package net.minecraft.world.gen;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

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
public class NoiseGeneratorImproved extends NoiseGenerator {
	private int[] permutations;
	public double xCoord;
	public double yCoord;
	public double zCoord;
	private static final double[] field_152381_e = new double[] { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D,
			0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
	private static final double[] field_152382_f = new double[] { 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D,
			1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D };
	private static final double[] field_152383_g = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D,
			1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };
	private static final double[] field_152384_h = new double[] { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D,
			0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
	private static final double[] field_152385_i = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D,
			1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };

	public NoiseGeneratorImproved() {
		this(new EaglercraftRandom());
	}

	public NoiseGeneratorImproved(EaglercraftRandom parRandom) {
		this.permutations = new int[512];
		this.xCoord = parRandom.nextDouble() * 256.0D;
		this.yCoord = parRandom.nextDouble() * 256.0D;
		this.zCoord = parRandom.nextDouble() * 256.0D;

		for (int i = 0; i < 256; this.permutations[i] = i++) {
			;
		}

		for (int l = 0; l < 256; ++l) {
			int j = parRandom.nextInt(256 - l) + l;
			int k = this.permutations[l];
			this.permutations[l] = this.permutations[j];
			this.permutations[j] = k;
			this.permutations[l + 256] = this.permutations[l];
		}

	}

	public final double lerp(double parDouble1, double parDouble2, double parDouble3) {
		return parDouble2 + parDouble1 * (parDouble3 - parDouble2);
	}

	public final double func_76309_a(int parInt1, double parDouble1, double parDouble2) {
		int i = parInt1 & 15;
		return field_152384_h[i] * parDouble1 + field_152385_i[i] * parDouble2;
	}

	public final double grad(int parInt1, double parDouble1, double parDouble2, double parDouble3) {
		int i = parInt1 & 15;
		return field_152381_e[i] * parDouble1 + field_152382_f[i] * parDouble2 + field_152383_g[i] * parDouble3;
	}

	/**+
	 * pars: noiseArray , xOffset , yOffset , zOffset , xSize ,
	 * ySize , zSize , xScale, yScale , zScale , noiseScale.
	 * noiseArray should be xSize*ySize*zSize in size
	 */
	public void populateNoiseArray(double[] parArrayOfDouble, double parDouble1, double parDouble2, double parDouble3,
			int parInt1, int parInt2, int parInt3, double parDouble4, double parDouble5, double parDouble6,
			double parDouble7) {
		if (parInt2 == 1) {
			int i5 = 0;
			int j5 = 0;
			int j = 0;
			int k5 = 0;
			double d14 = 0.0D;
			double d15 = 0.0D;
			int l5 = 0;
			double d16 = 1.0D / parDouble7;

			for (int j2 = 0; j2 < parInt1; ++j2) {
				double d17 = parDouble1 + (double) j2 * parDouble4 + this.xCoord;
				int i6 = (int) d17;
				if (d17 < (double) i6) {
					--i6;
				}

				int k2 = i6 & 255;
				d17 = d17 - (double) i6;
				double d18 = d17 * d17 * d17 * (d17 * (d17 * 6.0D - 15.0D) + 10.0D);

				for (int j6 = 0; j6 < parInt3; ++j6) {
					double d19 = parDouble3 + (double) j6 * parDouble6 + this.zCoord;
					int k6 = (int) d19;
					if (d19 < (double) k6) {
						--k6;
					}

					int l6 = k6 & 255;
					d19 = d19 - (double) k6;
					double d20 = d19 * d19 * d19 * (d19 * (d19 * 6.0D - 15.0D) + 10.0D);
					i5 = this.permutations[k2] + 0;
					j5 = this.permutations[i5] + l6;
					j = this.permutations[k2 + 1] + 0;
					k5 = this.permutations[j] + l6;
					d14 = this.lerp(d18, this.func_76309_a(this.permutations[j5], d17, d19),
							this.grad(this.permutations[k5], d17 - 1.0D, 0.0D, d19));
					d15 = this.lerp(d18, this.grad(this.permutations[j5 + 1], d17, 0.0D, d19 - 1.0D),
							this.grad(this.permutations[k5 + 1], d17 - 1.0D, 0.0D, d19 - 1.0D));
					double d21 = this.lerp(d20, d14, d15);
					int i7 = l5++;
					parArrayOfDouble[i7] += d21 * d16;
				}
			}

		} else {
			int i = 0;
			double d0 = 1.0D / parDouble7;
			int k = -1;
			int l = 0;
			int i1 = 0;
			int j1 = 0;
			int k1 = 0;
			int l1 = 0;
			int i2 = 0;
			double d1 = 0.0D;
			double d2 = 0.0D;
			double d3 = 0.0D;
			double d4 = 0.0D;

			for (int l2 = 0; l2 < parInt1; ++l2) {
				double d5 = parDouble1 + (double) l2 * parDouble4 + this.xCoord;
				int i3 = (int) d5;
				if (d5 < (double) i3) {
					--i3;
				}

				int j3 = i3 & 255;
				d5 = d5 - (double) i3;
				double d6 = d5 * d5 * d5 * (d5 * (d5 * 6.0D - 15.0D) + 10.0D);

				for (int k3 = 0; k3 < parInt3; ++k3) {
					double d7 = parDouble3 + (double) k3 * parDouble6 + this.zCoord;
					int l3 = (int) d7;
					if (d7 < (double) l3) {
						--l3;
					}

					int i4 = l3 & 255;
					d7 = d7 - (double) l3;
					double d8 = d7 * d7 * d7 * (d7 * (d7 * 6.0D - 15.0D) + 10.0D);

					for (int j4 = 0; j4 < parInt2; ++j4) {
						double d9 = parDouble2 + (double) j4 * parDouble5 + this.yCoord;
						int k4 = (int) d9;
						if (d9 < (double) k4) {
							--k4;
						}

						int l4 = k4 & 255;
						d9 = d9 - (double) k4;
						double d10 = d9 * d9 * d9 * (d9 * (d9 * 6.0D - 15.0D) + 10.0D);
						if (j4 == 0 || l4 != k) {
							k = l4;
							l = this.permutations[j3] + l4;
							i1 = this.permutations[l] + i4;
							j1 = this.permutations[l + 1] + i4;
							k1 = this.permutations[j3 + 1] + l4;
							l1 = this.permutations[k1] + i4;
							i2 = this.permutations[k1 + 1] + i4;
							d1 = this.lerp(d6, this.grad(this.permutations[i1], d5, d9, d7),
									this.grad(this.permutations[l1], d5 - 1.0D, d9, d7));
							d2 = this.lerp(d6, this.grad(this.permutations[j1], d5, d9 - 1.0D, d7),
									this.grad(this.permutations[i2], d5 - 1.0D, d9 - 1.0D, d7));
							d3 = this.lerp(d6, this.grad(this.permutations[i1 + 1], d5, d9, d7 - 1.0D),
									this.grad(this.permutations[l1 + 1], d5 - 1.0D, d9, d7 - 1.0D));
							d4 = this.lerp(d6, this.grad(this.permutations[j1 + 1], d5, d9 - 1.0D, d7 - 1.0D),
									this.grad(this.permutations[i2 + 1], d5 - 1.0D, d9 - 1.0D, d7 - 1.0D));
						}

						double d11 = this.lerp(d10, d1, d2);
						double d12 = this.lerp(d10, d3, d4);
						double d13 = this.lerp(d8, d11, d12);
						int j7 = i++;
						parArrayOfDouble[j7] += d13 * d0;
					}
				}
			}

		}
	}
}