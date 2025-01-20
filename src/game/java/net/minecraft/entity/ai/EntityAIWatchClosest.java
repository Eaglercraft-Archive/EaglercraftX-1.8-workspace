package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

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
public class EntityAIWatchClosest extends EntityAIBase {
	protected EntityLiving theWatcher;
	protected Entity closestEntity;
	protected float maxDistanceForPlayer;
	private int lookTime;
	private float chance;
	protected Class<? extends Entity> watchedClass;

	public EntityAIWatchClosest(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass,
			float maxDistance) {
		this.theWatcher = entitylivingIn;
		this.watchedClass = watchTargetClass;
		this.maxDistanceForPlayer = maxDistance;
		this.chance = 0.02F;
		this.setMutexBits(2);
	}

	public EntityAIWatchClosest(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass,
			float maxDistance, float chanceIn) {
		this.theWatcher = entitylivingIn;
		this.watchedClass = watchTargetClass;
		this.maxDistanceForPlayer = maxDistance;
		this.chance = chanceIn;
		this.setMutexBits(2);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.theWatcher.getRNG().nextFloat() >= this.chance) {
			return false;
		} else {
			if (this.theWatcher.getAttackTarget() != null) {
				this.closestEntity = this.theWatcher.getAttackTarget();
			}

			if (this.watchedClass == EntityPlayer.class) {
				this.closestEntity = this.theWatcher.worldObj.getClosestPlayerToEntity(this.theWatcher,
						(double) this.maxDistanceForPlayer);
			} else {
				this.closestEntity = this.theWatcher.worldObj.findNearestEntityWithinAABB(
						this.watchedClass, this.theWatcher.getEntityBoundingBox()
								.expand((double) this.maxDistanceForPlayer, 3.0D, (double) this.maxDistanceForPlayer),
						this.theWatcher);
			}

			return this.closestEntity != null;
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return !this.closestEntity.isEntityAlive() ? false
				: (this.theWatcher.getDistanceSqToEntity(
						this.closestEntity) > (double) (this.maxDistanceForPlayer * this.maxDistanceForPlayer) ? false
								: this.lookTime > 0);
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.lookTime = 40 + this.theWatcher.getRNG().nextInt(40);
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.closestEntity = null;
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX,
				this.closestEntity.posY + (double) this.closestEntity.getEyeHeight(), this.closestEntity.posZ, 10.0F,
				(float) this.theWatcher.getVerticalFaceSpeed());
		--this.lookTime;
	}
}