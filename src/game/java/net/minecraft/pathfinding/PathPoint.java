package net.minecraft.pathfinding;

import net.minecraft.util.MathHelper;

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
public class PathPoint {
	public final int xCoord;
	public final int yCoord;
	public final int zCoord;
	private final int hash;
	int index = -1;
	float totalPathDistance;
	float distanceToNext;
	float distanceToTarget;
	PathPoint previous;
	public boolean visited;

	public PathPoint(int x, int y, int z) {
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		this.hash = makeHash(x, y, z);
	}

	public static int makeHash(int x, int y, int z) {
		return y & 255 | (x & 32767) << 8 | (z & 32767) << 24 | (x < 0 ? Integer.MIN_VALUE : 0)
				| (z < 0 ? '\u8000' : 0);
	}

	/**+
	 * Returns the linear distance to another path point
	 */
	public float distanceTo(PathPoint pathpointIn) {
		float f = (float) (pathpointIn.xCoord - this.xCoord);
		float f1 = (float) (pathpointIn.yCoord - this.yCoord);
		float f2 = (float) (pathpointIn.zCoord - this.zCoord);
		return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
	}

	/**+
	 * Returns the squared distance to another path point
	 */
	public float distanceToSquared(PathPoint pathpointIn) {
		float f = (float) (pathpointIn.xCoord - this.xCoord);
		float f1 = (float) (pathpointIn.yCoord - this.yCoord);
		float f2 = (float) (pathpointIn.zCoord - this.zCoord);
		return f * f + f1 * f1 + f2 * f2;
	}

	public boolean equals(Object object) {
		if (!(object instanceof PathPoint)) {
			return false;
		} else {
			PathPoint pathpoint = (PathPoint) object;
			return this.hash == pathpoint.hash && this.xCoord == pathpoint.xCoord && this.yCoord == pathpoint.yCoord
					&& this.zCoord == pathpoint.zCoord;
		}
	}

	public int hashCode() {
		return this.hash;
	}

	/**+
	 * Returns true if this point has already been assigned to a
	 * path
	 */
	public boolean isAssigned() {
		return this.index >= 0;
	}

	public String toString() {
		return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
	}
}