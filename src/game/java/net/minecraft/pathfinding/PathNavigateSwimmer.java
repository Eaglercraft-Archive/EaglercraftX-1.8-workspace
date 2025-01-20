package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.SwimNodeProcessor;

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
public class PathNavigateSwimmer extends PathNavigate {
	public PathNavigateSwimmer(EntityLiving entitylivingIn, World worldIn) {
		super(entitylivingIn, worldIn);
	}

	protected PathFinder getPathFinder() {
		return new PathFinder(new SwimNodeProcessor());
	}

	/**+
	 * If on ground or swimming and can swim
	 */
	protected boolean canNavigate() {
		return this.isInLiquid();
	}

	protected Vec3 getEntityPosition() {
		return new Vec3(this.theEntity.posX, this.theEntity.posY + (double) this.theEntity.height * 0.5D,
				this.theEntity.posZ);
	}

	protected void pathFollow() {
		Vec3 vec3 = this.getEntityPosition();
		float f = this.theEntity.width * this.theEntity.width;
		byte b0 = 6;
		if (vec3.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity,
				this.currentPath.getCurrentPathIndex())) < (double) f) {
			this.currentPath.incrementPathIndex();
		}

		for (int i = Math.min(this.currentPath.getCurrentPathIndex() + b0,
				this.currentPath.getCurrentPathLength() - 1); i > this.currentPath.getCurrentPathIndex(); --i) {
			Vec3 vec31 = this.currentPath.getVectorFromIndex(this.theEntity, i);
			if (vec31.squareDistanceTo(vec3) <= 36.0D && this.isDirectPathBetweenPoints(vec3, vec31, 0, 0, 0)) {
				this.currentPath.setCurrentPathIndex(i);
				break;
			}
		}

		this.checkForStuck(vec3);
	}

	/**+
	 * Trims path data from the end to the first sun covered block
	 */
	protected void removeSunnyPath() {
		super.removeSunnyPath();
	}

	/**+
	 * Returns true when an entity of specified size could safely
	 * walk in a straight line between the two points. Args: pos1,
	 * pos2, entityXSize, entityYSize, entityZSize
	 */
	protected boolean isDirectPathBetweenPoints(Vec3 vec3, Vec3 vec31, int var3, int var4, int var5) {
		MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3,
				new Vec3(vec31.xCoord, vec31.yCoord + (double) this.theEntity.height * 0.5D, vec31.zCoord), false, true,
				false);
		return movingobjectposition == null
				|| movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.MISS;
	}
}