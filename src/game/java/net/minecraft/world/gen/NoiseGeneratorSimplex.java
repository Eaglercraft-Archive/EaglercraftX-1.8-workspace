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
public class NoiseGeneratorSimplex {
	private static int[][] field_151611_e = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 },
			{ 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 },
			{ 0, -1, -1 } };
	public static final double field_151614_a = Math.sqrt(3.0D);
	private int[] field_151608_f;
	public double field_151612_b;
	public double field_151613_c;
	public double field_151610_d;
	private static final double field_151609_g = 0.5D * (field_151614_a - 1.0D);
	private static final double field_151615_h = (3.0D - field_151614_a) / 6.0D;

	public NoiseGeneratorSimplex() {
		this(new EaglercraftRandom());
	}

	public NoiseGeneratorSimplex(EaglercraftRandom parRandom) {
		this.field_151608_f = new int[512];
		this.field_151612_b = parRandom.nextDouble() * 256.0D;
		this.field_151613_c = parRandom.nextDouble() * 256.0D;
		this.field_151610_d = parRandom.nextDouble() * 256.0D;

		for (int i = 0; i < 256; this.field_151608_f[i] = i++) {
			;
		}

		for (int l = 0; l < 256; ++l) {
			int j = parRandom.nextInt(256 - l) + l;
			int k = this.field_151608_f[l];
			this.field_151608_f[l] = this.field_151608_f[j];
			this.field_151608_f[j] = k;
			this.field_151608_f[l + 256] = this.field_151608_f[l];
		}

	}

	private static int func_151607_a(double parDouble1) {
		return parDouble1 > 0.0D ? (int) parDouble1 : (int) parDouble1 - 1;
	}

	private static double func_151604_a(int[] parArrayOfInt, double parDouble1, double parDouble2) {
		return (double) parArrayOfInt[0] * parDouble1 + (double) parArrayOfInt[1] * parDouble2;
	}

	public double func_151605_a(double parDouble1, double parDouble2) {
		double d3 = 0.5D * (field_151614_a - 1.0D);
		double d4 = (parDouble1 + parDouble2) * d3;
		int i = func_151607_a(parDouble1 + d4);
		int j = func_151607_a(parDouble2 + d4);
		double d5 = (3.0D - field_151614_a) / 6.0D;
		double d6 = (double) (i + j) * d5;
		double d7 = (double) i - d6;
		double d8 = (double) j - d6;
		double d9 = parDouble1 - d7;
		double d10 = parDouble2 - d8;
		byte b0;
		byte b1;
		if (d9 > d10) {
			b0 = 1;
			b1 = 0;
		} else {
			b0 = 0;
			b1 = 1;
		}

		double d11 = d9 - (double) b0 + d5;
		double d12 = d10 - (double) b1 + d5;
		double d13 = d9 - 1.0D + 2.0D * d5;
		double d14 = d10 - 1.0D + 2.0D * d5;
		int k = i & 255;
		int l = j & 255;
		int i1 = this.field_151608_f[k + this.field_151608_f[l]] % 12;
		int j1 = this.field_151608_f[k + b0 + this.field_151608_f[l + b1]] % 12;
		int k1 = this.field_151608_f[k + 1 + this.field_151608_f[l + 1]] % 12;
		double d15 = 0.5D - d9 * d9 - d10 * d10;
		double d0;
		if (d15 < 0.0D) {
			d0 = 0.0D;
		} else {
			d15 = d15 * d15;
			d0 = d15 * d15 * func_151604_a(field_151611_e[i1], d9, d10);
		}

		double d16 = 0.5D - d11 * d11 - d12 * d12;
		double d1;
		if (d16 < 0.0D) {
			d1 = 0.0D;
		} else {
			d16 = d16 * d16;
			d1 = d16 * d16 * func_151604_a(field_151611_e[j1], d11, d12);
		}

		double d17 = 0.5D - d13 * d13 - d14 * d14;
		double d2;
		if (d17 < 0.0D) {
			d2 = 0.0D;
		} else {
			d17 = d17 * d17;
			d2 = d17 * d17 * func_151604_a(field_151611_e[k1], d13, d14);
		}

		return 70.0D * (d0 + d1 + d2);
	}

	public void func_151606_a(double[] parArrayOfDouble, double parDouble1, double parDouble2, int parInt1, int parInt2,
			double parDouble3, double parDouble4, double parDouble5) {
		int i = 0;

		for (int j = 0; j < parInt2; ++j) {
			double d0 = (parDouble2 + (double) j) * parDouble4 + this.field_151613_c;

			for (int k = 0; k < parInt1; ++k) {
				double d1 = (parDouble1 + (double) k) * parDouble3 + this.field_151612_b;
				double d5 = (d1 + d0) * field_151609_g;
				int l = func_151607_a(d1 + d5);
				int i1 = func_151607_a(d0 + d5);
				double d6 = (double) (l + i1) * field_151615_h;
				double d7 = (double) l - d6;
				double d8 = (double) i1 - d6;
				double d9 = d1 - d7;
				double d10 = d0 - d8;
				byte b0;
				byte b1;
				if (d9 > d10) {
					b0 = 1;
					b1 = 0;
				} else {
					b0 = 0;
					b1 = 1;
				}

				double d11 = d9 - (double) b0 + field_151615_h;
				double d12 = d10 - (double) b1 + field_151615_h;
				double d13 = d9 - 1.0D + 2.0D * field_151615_h;
				double d14 = d10 - 1.0D + 2.0D * field_151615_h;
				int j1 = l & 255;
				int k1 = i1 & 255;
				int l1 = this.field_151608_f[j1 + this.field_151608_f[k1]] % 12;
				int i2 = this.field_151608_f[j1 + b0 + this.field_151608_f[k1 + b1]] % 12;
				int j2 = this.field_151608_f[j1 + 1 + this.field_151608_f[k1 + 1]] % 12;
				double d15 = 0.5D - d9 * d9 - d10 * d10;
				double d2;
				if (d15 < 0.0D) {
					d2 = 0.0D;
				} else {
					d15 = d15 * d15;
					d2 = d15 * d15 * func_151604_a(field_151611_e[l1], d9, d10);
				}

				double d16 = 0.5D - d11 * d11 - d12 * d12;
				double d3;
				if (d16 < 0.0D) {
					d3 = 0.0D;
				} else {
					d16 = d16 * d16;
					d3 = d16 * d16 * func_151604_a(field_151611_e[i2], d11, d12);
				}

				double d17 = 0.5D - d13 * d13 - d14 * d14;
				double d4;
				if (d17 < 0.0D) {
					d4 = 0.0D;
				} else {
					d17 = d17 * d17;
					d4 = d17 * d17 * func_151604_a(field_151611_e[j2], d13, d14);
				}

				int k2 = i++;
				parArrayOfDouble[k2] += 70.0D * (d2 + d3 + d4) * parDouble5;
			}
		}

	}
}