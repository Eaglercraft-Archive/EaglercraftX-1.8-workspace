package net.minecraft.util;

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
public class Vec3 {
	public final double xCoord;
	public final double yCoord;
	public final double zCoord;

	public Vec3(double x, double y, double z) {
		if (x == -0.0D) {
			x = 0.0D;
		}

		if (y == -0.0D) {
			y = 0.0D;
		}

		if (z == -0.0D) {
			z = 0.0D;
		}

		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
	}

	public Vec3(Vec3i parVec3i) {
		this((double) parVec3i.getX(), (double) parVec3i.getY(), (double) parVec3i.getZ());
	}

	/**+
	 * Returns a new vector with the result of the specified vector
	 * minus this.
	 */
	public Vec3 subtractReverse(Vec3 vec) {
		return new Vec3(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
	}

	/**+
	 * Normalizes the vector to a length of 1 (except if it is the
	 * zero vector)
	 */
	public Vec3 normalize() {
		double d0 = (double) MathHelper
				.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
		return d0 < 1.0E-4D ? new Vec3(0.0D, 0.0D, 0.0D)
				: new Vec3(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
	}

	public double dotProduct(Vec3 vec) {
		return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
	}

	/**+
	 * Returns a new vector with the result of this vector x the
	 * specified vector.
	 */
	public Vec3 crossProduct(Vec3 vec) {
		return new Vec3(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord,
				this.zCoord * vec.xCoord - this.xCoord * vec.zCoord,
				this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
	}

	public Vec3 subtract(Vec3 vec) {
		return this.subtract(vec.xCoord, vec.yCoord, vec.zCoord);
	}

	public Vec3 subtract(double x, double y, double z) {
		return this.addVector(-x, -y, -z);
	}

	public Vec3 add(Vec3 vec) {
		return this.addVector(vec.xCoord, vec.yCoord, vec.zCoord);
	}

	/**+
	 * Adds the specified x,y,z vector components to this vector and
	 * returns the resulting vector. Does not change this vector.
	 */
	public Vec3 addVector(double x, double y, double z) {
		return new Vec3(this.xCoord + x, this.yCoord + y, this.zCoord + z);
	}

	/**+
	 * Euclidean distance between this and the specified vector,
	 * returned as double.
	 */
	public double distanceTo(Vec3 vec) {
		double d0 = vec.xCoord - this.xCoord;
		double d1 = vec.yCoord - this.yCoord;
		double d2 = vec.zCoord - this.zCoord;
		return (double) MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
	}

	/**+
	 * The square of the Euclidean distance between this and the
	 * specified vector.
	 */
	public double squareDistanceTo(Vec3 vec) {
		double d0 = vec.xCoord - this.xCoord;
		double d1 = vec.yCoord - this.yCoord;
		double d2 = vec.zCoord - this.zCoord;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	/**+
	 * Returns the length of the vector.
	 */
	public double lengthVector() {
		return (double) MathHelper
				.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
	}

	/**+
	 * Returns a new vector with x value equal to the second
	 * parameter, along the line between this vector and the passed
	 * in vector, or null if not possible.
	 */
	public Vec3 getIntermediateWithXValue(Vec3 vec, double x) {
		double d0 = vec.xCoord - this.xCoord;
		double d1 = vec.yCoord - this.yCoord;
		double d2 = vec.zCoord - this.zCoord;
		if (d0 * d0 < 1.0000000116860974E-7D) {
			return null;
		} else {
			double d3 = (x - this.xCoord) / d0;
			return d3 >= 0.0D && d3 <= 1.0D
					? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3)
					: null;
		}
	}

	/**+
	 * Returns a new vector with y value equal to the second
	 * parameter, along the line between this vector and the passed
	 * in vector, or null if not possible.
	 */
	public Vec3 getIntermediateWithYValue(Vec3 vec, double y) {
		double d0 = vec.xCoord - this.xCoord;
		double d1 = vec.yCoord - this.yCoord;
		double d2 = vec.zCoord - this.zCoord;
		if (d1 * d1 < 1.0000000116860974E-7D) {
			return null;
		} else {
			double d3 = (y - this.yCoord) / d1;
			return d3 >= 0.0D && d3 <= 1.0D
					? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3)
					: null;
		}
	}

	/**+
	 * Returns a new vector with z value equal to the second
	 * parameter, along the line between this vector and the passed
	 * in vector, or null if not possible.
	 */
	public Vec3 getIntermediateWithZValue(Vec3 vec, double z) {
		double d0 = vec.xCoord - this.xCoord;
		double d1 = vec.yCoord - this.yCoord;
		double d2 = vec.zCoord - this.zCoord;
		if (d2 * d2 < 1.0000000116860974E-7D) {
			return null;
		} else {
			double d3 = (z - this.zCoord) / d2;
			return d3 >= 0.0D && d3 <= 1.0D
					? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3)
					: null;
		}
	}

	public String toString() {
		return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
	}

	public Vec3 rotatePitch(float pitch) {
		float f = MathHelper.cos(pitch);
		float f1 = MathHelper.sin(pitch);
		double d0 = this.xCoord;
		double d1 = this.yCoord * (double) f + this.zCoord * (double) f1;
		double d2 = this.zCoord * (double) f - this.yCoord * (double) f1;
		return new Vec3(d0, d1, d2);
	}

	public Vec3 rotateYaw(float yaw) {
		float f = MathHelper.cos(yaw);
		float f1 = MathHelper.sin(yaw);
		double d0 = this.xCoord * (double) f + this.zCoord * (double) f1;
		double d1 = this.yCoord;
		double d2 = this.zCoord * (double) f - this.xCoord * (double) f1;
		return new Vec3(d0, d1, d2);
	}
}