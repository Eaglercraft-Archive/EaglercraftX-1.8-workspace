package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

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
public class PathEntity {
	private final PathPoint[] points;
	private int currentPathIndex;
	private int pathLength;

	public PathEntity(PathPoint[] pathpoints) {
		this.points = pathpoints;
		this.pathLength = pathpoints.length;
	}

	/**+
	 * Directs this path to the next point in its array
	 */
	public void incrementPathIndex() {
		++this.currentPathIndex;
	}

	/**+
	 * Returns true if this path has reached the end
	 */
	public boolean isFinished() {
		return this.currentPathIndex >= this.pathLength;
	}

	/**+
	 * returns the last PathPoint of the Array
	 */
	public PathPoint getFinalPathPoint() {
		return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
	}

	/**+
	 * return the PathPoint located at the specified PathIndex,
	 * usually the current one
	 */
	public PathPoint getPathPointFromIndex(int index) {
		return this.points[index];
	}

	public int getCurrentPathLength() {
		return this.pathLength;
	}

	public void setCurrentPathLength(int length) {
		this.pathLength = length;
	}

	public int getCurrentPathIndex() {
		return this.currentPathIndex;
	}

	public void setCurrentPathIndex(int currentPathIndexIn) {
		this.currentPathIndex = currentPathIndexIn;
	}

	/**+
	 * Gets the vector of the PathPoint associated with the given
	 * index.
	 */
	public Vec3 getVectorFromIndex(Entity entityIn, int index) {
		double d0 = (double) this.points[index].xCoord + (double) ((int) (entityIn.width + 1.0F)) * 0.5D;
		double d1 = (double) this.points[index].yCoord;
		double d2 = (double) this.points[index].zCoord + (double) ((int) (entityIn.width + 1.0F)) * 0.5D;
		return new Vec3(d0, d1, d2);
	}

	/**+
	 * returns the current PathEntity target node as Vec3D
	 */
	public Vec3 getPosition(Entity entityIn) {
		return this.getVectorFromIndex(entityIn, this.currentPathIndex);
	}

	/**+
	 * Returns true if the EntityPath are the same. Non instance
	 * related equals.
	 */
	public boolean isSamePath(PathEntity pathentityIn) {
		if (pathentityIn == null) {
			return false;
		} else if (pathentityIn.points.length != this.points.length) {
			return false;
		} else {
			for (int i = 0; i < this.points.length; ++i) {
				if (this.points[i].xCoord != pathentityIn.points[i].xCoord
						|| this.points[i].yCoord != pathentityIn.points[i].yCoord
						|| this.points[i].zCoord != pathentityIn.points[i].zCoord) {
					return false;
				}
			}

			return true;
		}
	}

	/**+
	 * Returns true if the final PathPoint in the PathEntity is
	 * equal to Vec3D coords.
	 */
	public boolean isDestinationSame(Vec3 vec) {
		PathPoint pathpoint = this.getFinalPathPoint();
		return pathpoint == null ? false : pathpoint.xCoord == (int) vec.xCoord && pathpoint.zCoord == (int) vec.zCoord;
	}
}