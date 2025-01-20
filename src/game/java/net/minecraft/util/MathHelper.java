package net.minecraft.util;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

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
public class MathHelper {
	public static final float SQRT_2 = sqrt_float(2.0F);
	/**+
	 * A table of sin values computed from 0 (inclusive) to 2*pi
	 * (exclusive), with steps of 2*PI / 65536.
	 */
	private static final float[] SIN_TABLE = new float[65536];
	private static final int[] multiplyDeBruijnBitPosition;
	private static final double field_181163_d;
	private static final double[] field_181164_e;
	private static final double[] field_181165_f;

	/**+
	 * sin looked up in a table
	 */
	public static float sin(float parFloat1) {
		return SIN_TABLE[(int) (parFloat1 * 10430.378F) & '\uffff'];
	}

	/**+
	 * cos looked up in the sin table with the appropriate offset
	 */
	public static float cos(float value) {
		return SIN_TABLE[(int) (value * 10430.378F + 16384.0F) & '\uffff'];
	}

	public static float sqrt_float(float value) {
		return (float) Math.sqrt((double) value);
	}

	public static float sqrt_double(double value) {
		return (float) Math.sqrt(value);
	}

	/**+
	 * Returns the greatest integer less than or equal to the float
	 * argument
	 */
	public static int floor_float(float value) {
		int i = (int) value;
		return value < (float) i ? i - 1 : i;
	}

	/**+
	 * returns par0 cast as an int, and no greater than
	 * Integer.MAX_VALUE-1024
	 */
	public static int truncateDoubleToInt(double value) {
		return (int) (value + 1024.0D) - 1024;
	}

	/**+
	 * Returns the greatest integer less than or equal to the double
	 * argument
	 */
	public static int floor_double(double value) {
		int i = (int) value;
		return value < (double) i ? i - 1 : i;
	}

	/**+
	 * Long version of floor_double
	 */
	public static long floor_double_long(double value) {
		long i = (long) value;
		return value < (double) i ? i - 1L : i;
	}

	public static int func_154353_e(double value) {
		return (int) (value >= 0.0D ? value : -value + 1.0D);
	}

	public static float abs(float value) {
		return value >= 0.0F ? value : -value;
	}

	/**+
	 * Returns the unsigned value of an int.
	 */
	public static int abs_int(int value) {
		return value >= 0 ? value : -value;
	}

	public static int ceiling_float_int(float value) {
		int i = (int) value;
		return value > (float) i ? i + 1 : i;
	}

	public static int ceiling_double_int(double value) {
		int i = (int) value;
		return value > (double) i ? i + 1 : i;
	}

	/**+
	 * Returns the value of the first parameter, clamped to be
	 * within the lower and upper limits given by the second and
	 * third parameters.
	 */
	public static int clamp_int(int num, int min, int max) {
		return num < min ? min : (num > max ? max : num);
	}

	/**+
	 * Returns the value of the first parameter, clamped to be
	 * within the lower and upper limits given by the second and
	 * third parameters
	 */
	public static float clamp_float(float num, float min, float max) {
		return num < min ? min : (num > max ? max : num);
	}

	public static double clamp_double(double num, double min, double max) {
		return num < min ? min : (num > max ? max : num);
	}

	public static double denormalizeClamp(double parDouble1, double parDouble2, double parDouble3) {
		return parDouble3 < 0.0D ? parDouble1
				: (parDouble3 > 1.0D ? parDouble2 : parDouble1 + (parDouble2 - parDouble1) * parDouble3);
	}

	/**+
	 * Maximum of the absolute value of two numbers.
	 */
	public static double abs_max(double parDouble1, double parDouble2) {
		if (parDouble1 < 0.0D) {
			parDouble1 = -parDouble1;
		}

		if (parDouble2 < 0.0D) {
			parDouble2 = -parDouble2;
		}

		return parDouble1 > parDouble2 ? parDouble1 : parDouble2;
	}

	/**+
	 * Buckets an integer with specifed bucket sizes. Args: i,
	 * bucketSize
	 */
	public static int bucketInt(int parInt1, int parInt2) {
		return parInt1 < 0 ? -((-parInt1 - 1) / parInt2) - 1 : parInt1 / parInt2;
	}

	public static int getRandomIntegerInRange(EaglercraftRandom parRandom, int parInt1, int parInt2) {
		return parInt1 >= parInt2 ? parInt1 : parRandom.nextInt(parInt2 - parInt1 + 1) + parInt1;
	}

	public static float randomFloatClamp(EaglercraftRandom parRandom, float parFloat1, float parFloat2) {
		return parFloat1 >= parFloat2 ? parFloat1 : parRandom.nextFloat() * (parFloat2 - parFloat1) + parFloat1;
	}

	public static double getRandomDoubleInRange(EaglercraftRandom parRandom, double parDouble1, double parDouble2) {
		return parDouble1 >= parDouble2 ? parDouble1 : parRandom.nextDouble() * (parDouble2 - parDouble1) + parDouble1;
	}

	public static double average(long[] values) {
		long i = 0L;

		for (int j = 0; j < values.length; ++j) {
			i += values[j];
		}

		return (double) i / (double) values.length;
	}

	public static boolean epsilonEquals(float parFloat1, float parFloat2) {
		return abs(parFloat2 - parFloat1) < 1.0E-5F;
	}

	public static int normalizeAngle(int parInt1, int parInt2) {
		return (parInt1 % parInt2 + parInt2) % parInt2;
	}

	/**+
	 * the angle is reduced to an angle between -180 and +180 by
	 * mod, and a 360 check
	 */
	public static float wrapAngleTo180_float(float value) {
		value = value % 360.0F;
		if (value >= 180.0F) {
			value -= 360.0F;
		}

		if (value < -180.0F) {
			value += 360.0F;
		}

		return value;
	}

	/**+
	 * the angle is reduced to an angle between -180 and +180 by
	 * mod, and a 360 check
	 */
	public static double wrapAngleTo180_double(double value) {
		value = value % 360.0D;
		if (value >= 180.0D) {
			value -= 360.0D;
		}

		if (value < -180.0D) {
			value += 360.0D;
		}

		return value;
	}

	/**+
	 * parses the string as integer or returns the second parameter
	 * if it fails
	 */
	public static int parseIntWithDefault(String parString1, int parInt1) {
		try {
			return Integer.parseInt(parString1);
		} catch (Throwable var3) {
			return parInt1;
		}
	}

	/**+
	 * parses the string as integer or returns the second parameter
	 * if it fails. this value is capped to par2
	 */
	public static int parseIntWithDefaultAndMax(String parString1, int parInt1, int parInt2) {
		return Math.max(parInt2, parseIntWithDefault(parString1, parInt1));
	}

	/**+
	 * parses the string as double or returns the second parameter
	 * if it fails.
	 */
	public static double parseDoubleWithDefault(String parString1, double parDouble1) {
		try {
			return Double.parseDouble(parString1);
		} catch (Throwable var4) {
			return parDouble1;
		}
	}

	public static double parseDoubleWithDefaultAndMax(String parString1, double parDouble1, double parDouble2) {
		return Math.max(parDouble2, parseDoubleWithDefault(parString1, parDouble1));
	}

	/**+
	 * Returns the input value rounded up to the next highest power
	 * of two.
	 */
	public static int roundUpToPowerOfTwo(int value) {
		int i = value - 1;
		i = i | i >> 1;
		i = i | i >> 2;
		i = i | i >> 4;
		i = i | i >> 8;
		i = i | i >> 16;
		return i + 1;
	}

	/**+
	 * Is the given value a power of two? (1, 2, 4, 8, 16, ...)
	 */
	private static boolean isPowerOfTwo(int value) {
		return value != 0 && (value & value - 1) == 0;
	}

	/**+
	 * Uses a B(2, 5) De Bruijn sequence and a lookup table to
	 * efficiently calculate the log-base-two of the given value.
	 * Optimized for cases where the input value is a power-of-two.
	 * If the input value is not a power-of-two, then subtract 1
	 * from the return value.
	 */
	private static int calculateLogBaseTwoDeBruijn(int value) {
		value = isPowerOfTwo(value) ? value : roundUpToPowerOfTwo(value);
		return multiplyDeBruijnBitPosition[(int) ((long) value * 125613361L >> 27) & 31];
	}

	/**+
	 * Efficiently calculates the floor of the base-2 log of an
	 * integer value. This is effectively the index of the highest
	 * bit that is set. For example, if the number in binary is
	 * 0...100101, this will return 5.
	 */
	public static int calculateLogBaseTwo(int value) {
		/**+
		 * Uses a B(2, 5) De Bruijn sequence and a lookup table to
		 * efficiently calculate the log-base-two of the given value.
		 * Optimized for cases where the input value is a power-of-two.
		 * If the input value is not a power-of-two, then subtract 1
		 * from the return value.
		 */
		return calculateLogBaseTwoDeBruijn(value) - (isPowerOfTwo(value) ? 0 : 1);
	}

	public static int func_154354_b(int parInt1, int parInt2) {
		if (parInt2 == 0) {
			return 0;
		} else if (parInt1 == 0) {
			return parInt2;
		} else {
			if (parInt1 < 0) {
				parInt2 *= -1;
			}

			int i = parInt1 % parInt2;
			return i == 0 ? parInt1 : parInt1 + parInt2 - i;
		}
	}

	public static int func_180183_b(float parFloat1, float parFloat2, float parFloat3) {
		return func_180181_b(floor_float(parFloat1 * 255.0F), floor_float(parFloat2 * 255.0F),
				floor_float(parFloat3 * 255.0F));
	}

	public static int func_180181_b(int parInt1, int parInt2, int parInt3) {
		int i = (parInt1 << 8) + parInt2;
		i = (i << 8) + parInt3;
		return i;
	}

	public static int func_180188_d(int parInt1, int parInt2) {
		int i = (parInt1 & 16711680) >> 16;
		int j = (parInt2 & 16711680) >> 16;
		int k = (parInt1 & '\uff00') >> 8;
		int l = (parInt2 & '\uff00') >> 8;
		int i1 = (parInt1 & 255) >> 0;
		int j1 = (parInt2 & 255) >> 0;
		int k1 = (int) ((float) i * (float) j / 255.0F);
		int l1 = (int) ((float) k * (float) l / 255.0F);
		int i2 = (int) ((float) i1 * (float) j1 / 255.0F);
		return parInt1 & -16777216 | k1 << 16 | l1 << 8 | i2;
	}

	public static double func_181162_h(double parDouble1) {
		return parDouble1 - Math.floor(parDouble1);
	}

	public static long getPositionRandom(Vec3i pos) {
		return getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
	}

	public static long getCoordinateRandom(int x, int y, int z) {
		long i = (long) (x * 3129871) ^ (long) z * 116129781L ^ (long) y;
		i = i * i * 42317861L + i * 11L;
		return i;
	}

	public static EaglercraftUUID getRandomUuid(EaglercraftRandom rand) {
		long i = rand.nextLong() & -61441L | 16384L;
		long j = rand.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
		return new EaglercraftUUID(i, j);
	}

	public static double func_181160_c(double parDouble1, double parDouble2, double parDouble3) {
		return (parDouble1 - parDouble2) / (parDouble3 - parDouble2);
	}

	public static double func_181159_b(double parDouble1, double parDouble2) {
		double d0 = parDouble2 * parDouble2 + parDouble1 * parDouble1;
		if (Double.isNaN(d0)) {
			return Double.NaN;
		} else {
			boolean flag = parDouble1 < 0.0D;
			if (flag) {
				parDouble1 = -parDouble1;
			}

			boolean flag1 = parDouble2 < 0.0D;
			if (flag1) {
				parDouble2 = -parDouble2;
			}

			boolean flag2 = parDouble1 > parDouble2;
			if (flag2) {
				double d1 = parDouble2;
				parDouble2 = parDouble1;
				parDouble1 = d1;
			}

			double d9 = func_181161_i(d0);
			parDouble2 = parDouble2 * d9;
			parDouble1 = parDouble1 * d9;
			double d2 = field_181163_d + parDouble1;
			int i = (int) Double.doubleToRawLongBits(d2);
			double d3 = field_181164_e[i];
			double d4 = field_181165_f[i];
			double d5 = d2 - field_181163_d;
			double d6 = parDouble1 * d4 - parDouble2 * d5;
			double d7 = (6.0D + d6 * d6) * d6 * 0.16666666666666666D;
			double d8 = d3 + d7;
			if (flag2) {
				d8 = 1.5707963267948966D - d8;
			}

			if (flag1) {
				d8 = 3.141592653589793D - d8;
			}

			if (flag) {
				d8 = -d8;
			}

			return d8;
		}
	}

	public static double func_181161_i(double parDouble1) {
		double d0 = 0.5D * parDouble1;
		long i = Double.doubleToRawLongBits(parDouble1);
		i = 6910469410427058090L - (i >> 1);
		parDouble1 = Double.longBitsToDouble(i);
		parDouble1 = parDouble1 * (1.5D - d0 * parDouble1 * parDouble1);
		return parDouble1;
	}

	public static int func_181758_c(float parFloat1, float parFloat2, float parFloat3) {
		int i = (int) (parFloat1 * 6.0F) % 6;
		float f = parFloat1 * 6.0F - (float) i;
		float f1 = parFloat3 * (1.0F - parFloat2);
		float f2 = parFloat3 * (1.0F - f * parFloat2);
		float f3 = parFloat3 * (1.0F - (1.0F - f) * parFloat2);
		float f4;
		float f5;
		float f6;
		switch (i) {
		case 0:
			f4 = parFloat3;
			f5 = f3;
			f6 = f1;
			break;
		case 1:
			f4 = f2;
			f5 = parFloat3;
			f6 = f1;
			break;
		case 2:
			f4 = f1;
			f5 = parFloat3;
			f6 = f3;
			break;
		case 3:
			f4 = f1;
			f5 = f2;
			f6 = parFloat3;
			break;
		case 4:
			f4 = f3;
			f5 = f1;
			f6 = parFloat3;
			break;
		case 5:
			f4 = parFloat3;
			f5 = f1;
			f6 = f2;
			break;
		default:
			throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + parFloat1
					+ ", " + parFloat2 + ", " + parFloat3);
		}

		int j = clamp_int((int) (f4 * 255.0F), 0, 255);
		int k = clamp_int((int) (f5 * 255.0F), 0, 255);
		int l = clamp_int((int) (f6 * 255.0F), 0, 255);
		return j << 16 | k << 8 | l;
	}

	static {
		for (int i = 0; i < 65536; ++i) {
			SIN_TABLE[i] = (float) Math.sin((double) i * 3.141592653589793D * 2.0D / 65536.0D);
		}

		multiplyDeBruijnBitPosition = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13,
				23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
		field_181163_d = Double.longBitsToDouble(4805340802404319232L);
		field_181164_e = new double[257];
		field_181165_f = new double[257];

		for (int j = 0; j < 257; ++j) {
			double d0 = (double) j / 256.0D;
			double d1 = Math.asin(d0);
			field_181165_f[j] = Math.cos(d1);
			field_181164_e[j] = d1;
		}

	}
}