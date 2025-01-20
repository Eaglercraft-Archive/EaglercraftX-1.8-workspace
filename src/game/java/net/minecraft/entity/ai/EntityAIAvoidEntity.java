package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
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
public class EntityAIAvoidEntity<T extends Entity> extends EntityAIBase {
	private final Predicate<Entity> canBeSeenSelector;
	protected EntityCreature theEntity;
	private double farSpeed;
	private double nearSpeed;
	protected T closestLivingEntity;
	private float avoidDistance;
	private PathEntity entityPathEntity;
	private PathNavigate entityPathNavigate;
	private Class<T> field_181064_i;
	private Predicate<? super T> avoidTargetSelector;

	public EntityAIAvoidEntity(EntityCreature parEntityCreature, Class<T> parClass1, float parFloat1, double parDouble1,
			double parDouble2) {
		this(parEntityCreature, parClass1, Predicates.alwaysTrue(), parFloat1, parDouble1, parDouble2);
	}

	public EntityAIAvoidEntity(EntityCreature parEntityCreature, Class<T> parClass1, Predicate<? super T> parPredicate,
			float parFloat1, double parDouble1, double parDouble2) {
		this.canBeSeenSelector = new Predicate<Entity>() {
			public boolean apply(Entity entity) {
				return entity.isEntityAlive() && EntityAIAvoidEntity.this.theEntity.getEntitySenses().canSee(entity);
			}
		};
		this.theEntity = parEntityCreature;
		this.field_181064_i = parClass1;
		this.avoidTargetSelector = parPredicate;
		this.avoidDistance = parFloat1;
		this.farSpeed = parDouble1;
		this.nearSpeed = parDouble2;
		this.entityPathNavigate = parEntityCreature.getNavigator();
		this.setMutexBits(1);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		List list = this.theEntity.worldObj.getEntitiesWithinAABB(this.field_181064_i,
				this.theEntity.getEntityBoundingBox().expand((double) this.avoidDistance, 3.0D,
						(double) this.avoidDistance),
				Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING, this.canBeSeenSelector,
						this.avoidTargetSelector }));
		if (list.isEmpty()) {
			return false;
		} else {
			this.closestLivingEntity = (T) list.get(0);
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3(
					this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
			if (vec3 == null) {
				return false;
			} else if (this.closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord,
					vec3.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity)) {
				return false;
			} else {
				this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
				return this.entityPathEntity == null ? false : this.entityPathEntity.isDestinationSame(vec3);
			}
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return !this.entityPathNavigate.noPath();
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.closestLivingEntity = null;
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0D) {
			this.theEntity.getNavigator().setSpeed(this.nearSpeed);
		} else {
			this.theEntity.getNavigator().setSpeed(this.farSpeed);
		}

	}
}