package net.minecraft.pathfinding;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

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
public abstract class PathNavigate {
	protected EntityLiving theEntity;
	protected World worldObj;
	protected PathEntity currentPath;
	protected double speed;
	private final IAttributeInstance pathSearchRange;
	private int totalTicks;
	private int ticksAtLastPos;
	private int lastFailure = 0;
	private int pathfindFailures = 0;
	/**+
	 * Coordinates of the entity's position last time a check was
	 * done (part of monitoring getting 'stuck')
	 */
	private Vec3 lastPosCheck = new Vec3(0.0D, 0.0D, 0.0D);
	private float heightRequirement = 1.0F;
	private final PathFinder pathFinder;

	public PathNavigate(EntityLiving entitylivingIn, World worldIn) {
		this.theEntity = entitylivingIn;
		this.worldObj = worldIn;
		this.pathSearchRange = entitylivingIn.getEntityAttribute(SharedMonsterAttributes.followRange);
		this.pathFinder = this.getPathFinder();
	}

	protected abstract PathFinder getPathFinder();

	/**+
	 * Sets the speed
	 */
	public void setSpeed(double speedIn) {
		this.speed = speedIn;
	}

	/**+
	 * Gets the maximum distance that the path finding will search
	 * in.
	 */
	public float getPathSearchRange() {
		return (float) this.pathSearchRange.getAttributeValue();
	}

	/**+
	 * Returns the path to the given coordinates. Args : x, y, z
	 */
	public final PathEntity getPathToXYZ(double x, double y, double z) {
		return this.getPathToPos(new BlockPos(MathHelper.floor_double(x), (int) y, MathHelper.floor_double(z)));
	}

	/**+
	 * Returns path to given BlockPos
	 */
	public PathEntity getPathToPos(BlockPos pos) {
		if (!this.canNavigate()) {
			return null;
		} else {
			float f = this.getPathSearchRange();
			BlockPos blockpos = new BlockPos(this.theEntity);
			int i = (int) (f + 8.0F);
			ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
			PathEntity pathentity = this.pathFinder.createEntityPathTo(chunkcache, this.theEntity, (BlockPos) pos, f);
			return pathentity;
		}
	}

	/**+
	 * Try to find and set a path to XYZ. Returns true if
	 * successful. Args : x, y, z, speed
	 */
	public boolean tryMoveToXYZ(double x, double y, double z, double speedIn) {
		PathEntity pathentity = this.getPathToXYZ((double) MathHelper.floor_double(x), (double) ((int) y),
				(double) MathHelper.floor_double(z));
		return this.setPath(pathentity, speedIn);
	}

	/**+
	 * Sets vertical space requirement for path
	 */
	public void setHeightRequirement(float jumpHeight) {
		this.heightRequirement = jumpHeight;
	}

	/**+
	 * Returns the path to the given EntityLiving. Args : entity
	 */
	public PathEntity getPathToEntityLiving(Entity entityIn) {
		if (!this.canNavigate()) {
			return null;
		} else {
			float f = this.getPathSearchRange();
			BlockPos blockpos = (new BlockPos(this.theEntity)).up();
			int i = (int) (f + 16.0F);
			ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
			PathEntity pathentity = this.pathFinder.createEntityPathTo(chunkcache, this.theEntity, (Entity) entityIn,
					f);
			return pathentity;
		}
	}

	/**+
	 * Try to find and set a path to EntityLiving. Returns true if
	 * successful. Args : entity, speed
	 */
	public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
		int i = -1;
		if (this.pathfindFailures > 10 && this.currentPath == null
				&& (i = (int) (MinecraftServer.getCurrentTimeMillis() / 50l)) < this.lastFailure + 40) {
			return false;
		}

		PathEntity pathentity = this.getPathToEntityLiving(entityIn);

		if (pathentity != null && this.setPath(pathentity, speedIn)) {
			this.lastFailure = 0;
			this.pathfindFailures = 0;
			return true;
		} else {
			this.pathfindFailures++;
			this.lastFailure = i == -1 ? (int) (MinecraftServer.getCurrentTimeMillis() / 50l) : i;
			return false;
		}
	}

	/**+
	 * Sets a new path. If it's diferent from the old path. Checks
	 * to adjust path for sun avoiding, and stores start coords.
	 * Args : path, speed
	 */
	public boolean setPath(PathEntity pathentityIn, double speedIn) {
		if (pathentityIn == null) {
			this.currentPath = null;
			return false;
		} else {
			if (!pathentityIn.isSamePath(this.currentPath)) {
				this.currentPath = pathentityIn;
			}

			this.removeSunnyPath();
			if (this.currentPath.getCurrentPathLength() == 0) {
				return false;
			} else {
				this.speed = speedIn;
				Vec3 vec3 = this.getEntityPosition();
				this.ticksAtLastPos = this.totalTicks;
				this.lastPosCheck = vec3;
				return true;
			}
		}
	}

	/**+
	 * gets the actively used PathEntity
	 */
	public PathEntity getPath() {
		return this.currentPath;
	}

	public void onUpdateNavigation() {
		++this.totalTicks;
		if (!this.noPath()) {
			if (this.canNavigate()) {
				this.pathFollow();
			} else if (this.currentPath != null
					&& this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
				Vec3 vec3 = this.getEntityPosition();
				Vec3 vec31 = this.currentPath.getVectorFromIndex(this.theEntity,
						this.currentPath.getCurrentPathIndex());
				if (vec3.yCoord > vec31.yCoord && !this.theEntity.onGround
						&& MathHelper.floor_double(vec3.xCoord) == MathHelper.floor_double(vec31.xCoord)
						&& MathHelper.floor_double(vec3.zCoord) == MathHelper.floor_double(vec31.zCoord)) {
					this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
				}
			}

			if (!this.noPath()) {
				Vec3 vec32 = this.currentPath.getPosition(this.theEntity);
				if (vec32 != null) {
					AxisAlignedBB axisalignedbb1 = (new AxisAlignedBB(vec32.xCoord, vec32.yCoord, vec32.zCoord,
							vec32.xCoord, vec32.yCoord, vec32.zCoord)).expand(0.5D, 0.5D, 0.5D);
					List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this.theEntity,
							axisalignedbb1.addCoord(0.0D, -1.0D, 0.0D));
					double d0 = -1.0D;
					axisalignedbb1 = axisalignedbb1.offset(0.0D, 1.0D, 0.0D);

					for (int i = 0, l = list.size(); i < l; ++i) {
						AxisAlignedBB axisalignedbb = list.get(i);
						d0 = axisalignedbb.calculateYOffset(axisalignedbb1, d0);
					}

					this.theEntity.getMoveHelper().setMoveTo(vec32.xCoord, vec32.yCoord + d0, vec32.zCoord, this.speed);
				}
			}
		}
	}

	protected void pathFollow() {
		Vec3 vec3 = this.getEntityPosition();
		int i = this.currentPath.getCurrentPathLength();

		for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); ++j) {
			if (this.currentPath.getPathPointFromIndex(j).yCoord != (int) vec3.yCoord) {
				i = j;
				break;
			}
		}

		float f = this.theEntity.width * this.theEntity.width * this.heightRequirement;

		for (int k = this.currentPath.getCurrentPathIndex(); k < i; ++k) {
			Vec3 vec31 = this.currentPath.getVectorFromIndex(this.theEntity, k);
			if (vec3.squareDistanceTo(vec31) < (double) f) {
				this.currentPath.setCurrentPathIndex(k + 1);
			}
		}

		int j1 = MathHelper.ceiling_float_int(this.theEntity.width);
		int k1 = (int) this.theEntity.height + 1;
		int l = j1;

		for (int i1 = i - 1; i1 >= this.currentPath.getCurrentPathIndex(); --i1) {
			if (this.isDirectPathBetweenPoints(vec3, this.currentPath.getVectorFromIndex(this.theEntity, i1), j1, k1,
					l)) {
				this.currentPath.setCurrentPathIndex(i1);
				break;
			}
		}

		this.checkForStuck(vec3);
	}

	/**+
	 * Checks if entity haven't been moved when last checked and if
	 * so, clears current {@link
	 * net.minecraft.pathfinding.PathEntity}
	 */
	protected void checkForStuck(Vec3 positionVec3) {
		if (this.totalTicks - this.ticksAtLastPos > 100) {
			if (positionVec3.squareDistanceTo(this.lastPosCheck) < 2.25D) {
				this.clearPathEntity();
			}

			this.ticksAtLastPos = this.totalTicks;
			this.lastPosCheck = positionVec3;
		}

	}

	/**+
	 * If null path or reached the end
	 */
	public boolean noPath() {
		return this.currentPath == null || this.currentPath.isFinished();
	}

	/**+
	 * sets active PathEntity to null
	 */
	public void clearPathEntity() {
		this.pathfindFailures = 0;
		this.lastFailure = 0;
		this.currentPath = null;
	}

	protected abstract Vec3 getEntityPosition();

	protected abstract boolean canNavigate();

	/**+
	 * Returns true if the entity is in water or lava, false
	 * otherwise
	 */
	protected boolean isInLiquid() {
		return this.theEntity.isInWater() || this.theEntity.isInLava();
	}

	/**+
	 * Trims path data from the end to the first sun covered block
	 */
	protected void removeSunnyPath() {
	}

	protected abstract boolean isDirectPathBetweenPoints(Vec3 var1, Vec3 var2, int var3, int var4, int var5);
}