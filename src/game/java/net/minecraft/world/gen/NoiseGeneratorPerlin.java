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
public class NoiseGeneratorPerlin extends NoiseGenerator {
	private NoiseGeneratorSimplex[] field_151603_a;
	private int field_151602_b;

	public NoiseGeneratorPerlin(EaglercraftRandom parRandom, int parInt1) {
		this.field_151602_b = parInt1;
		this.field_151603_a = new NoiseGeneratorSimplex[parInt1];

		for (int i = 0; i < parInt1; ++i) {
			this.field_151603_a[i] = new NoiseGeneratorSimplex(parRandom);
		}

	}

	public double func_151601_a(double parDouble1, double parDouble2) {
		double d0 = 0.0D;
		double d1 = 1.0D;

		for (int i = 0; i < this.field_151602_b; ++i) {
			d0 += this.field_151603_a[i].func_151605_a(parDouble1 * d1, parDouble2 * d1) / d1;
			d1 /= 2.0D;
		}

		return d0;
	}

	public double[] func_151599_a(double[] parArrayOfDouble, double parDouble1, double parDouble2, int parInt1,
			int parInt2, double parDouble3, double parDouble4, double parDouble5) {
		return this.func_151600_a(parArrayOfDouble, parDouble1, parDouble2, parInt1, parInt2, parDouble3, parDouble4,
				parDouble5, 0.5D);
	}

	public double[] func_151600_a(double[] parArrayOfDouble, double parDouble1, double parDouble2, int parInt1,
			int parInt2, double parDouble3, double parDouble4, double parDouble5, double parDouble6) {
		if (parArrayOfDouble != null && parArrayOfDouble.length >= parInt1 * parInt2) {
			for (int i = 0; i < parArrayOfDouble.length; ++i) {
				parArrayOfDouble[i] = 0.0D;
			}
		} else {
			parArrayOfDouble = new double[parInt1 * parInt2];
		}

		double d1 = 1.0D;
		double d0 = 1.0D;

		for (int j = 0; j < this.field_151602_b; ++j) {
			this.field_151603_a[j].func_151606_a(parArrayOfDouble, parDouble1, parDouble2, parInt1, parInt2,
					parDouble3 * d0 * d1, parDouble4 * d0 * d1, 0.55D / d1);
			d0 *= parDouble5;
			d1 *= parDouble6;
		}

		return parArrayOfDouble;
	}
}