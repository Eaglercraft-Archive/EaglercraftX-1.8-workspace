package net.minecraft.pathfinding;

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
public class Path {
	/**+
	 * Contains the points in this path
	 */
	private PathPoint[] pathPoints = new PathPoint[1024];
	private int count;

	/**+
	 * Adds a point to the path
	 */
	public PathPoint addPoint(PathPoint point) {
		if (point.index >= 0) {
			throw new IllegalStateException("OW KNOWS!");
		} else {
			if (this.count == this.pathPoints.length) {
				PathPoint[] apathpoint = new PathPoint[this.count << 1];
				System.arraycopy(this.pathPoints, 0, apathpoint, 0, this.count);
				this.pathPoints = apathpoint;
			}

			this.pathPoints[this.count] = point;
			point.index = this.count;
			this.sortBack(this.count++);
			return point;
		}
	}

	/**+
	 * Clears the path
	 */
	public void clearPath() {
		this.count = 0;
	}

	/**+
	 * Returns and removes the first point in the path
	 */
	public PathPoint dequeue() {
		PathPoint pathpoint = this.pathPoints[0];
		this.pathPoints[0] = this.pathPoints[--this.count];
		this.pathPoints[this.count] = null;
		if (this.count > 0) {
			this.sortForward(0);
		}

		pathpoint.index = -1;
		return pathpoint;
	}

	/**+
	 * Changes the provided point's distance to target
	 */
	public void changeDistance(PathPoint parPathPoint, float parFloat1) {
		float f = parPathPoint.distanceToTarget;
		parPathPoint.distanceToTarget = parFloat1;
		if (parFloat1 < f) {
			this.sortBack(parPathPoint.index);
		} else {
			this.sortForward(parPathPoint.index);
		}

	}

	/**+
	 * Sorts a point to the left
	 */
	private void sortBack(int parInt1) {
		PathPoint pathpoint = this.pathPoints[parInt1];

		int i;
		for (float f = pathpoint.distanceToTarget; parInt1 > 0; parInt1 = i) {
			i = parInt1 - 1 >> 1;
			PathPoint pathpoint1 = this.pathPoints[i];
			if (f >= pathpoint1.distanceToTarget) {
				break;
			}

			this.pathPoints[parInt1] = pathpoint1;
			pathpoint1.index = parInt1;
		}

		this.pathPoints[parInt1] = pathpoint;
		pathpoint.index = parInt1;
	}

	/**+
	 * Sorts a point to the right
	 */
	private void sortForward(int parInt1) {
		PathPoint pathpoint = this.pathPoints[parInt1];
		float f = pathpoint.distanceToTarget;

		while (true) {
			int i = 1 + (parInt1 << 1);
			int j = i + 1;
			if (i >= this.count) {
				break;
			}

			PathPoint pathpoint1 = this.pathPoints[i];
			float f1 = pathpoint1.distanceToTarget;
			PathPoint pathpoint2;
			float f2;
			if (j >= this.count) {
				pathpoint2 = null;
				f2 = Float.POSITIVE_INFINITY;
			} else {
				pathpoint2 = this.pathPoints[j];
				f2 = pathpoint2.distanceToTarget;
			}

			if (f1 < f2) {
				if (f1 >= f) {
					break;
				}

				this.pathPoints[parInt1] = pathpoint1;
				pathpoint1.index = parInt1;
				parInt1 = i;
			} else {
				if (f2 >= f) {
					break;
				}

				this.pathPoints[parInt1] = pathpoint2;
				pathpoint2.index = parInt1;
				parInt1 = j;
			}
		}

		this.pathPoints[parInt1] = pathpoint;
		pathpoint.index = parInt1;
	}

	/**+
	 * Returns true if this path contains no points
	 */
	public boolean isPathEmpty() {
		return this.count == 0;
	}
}