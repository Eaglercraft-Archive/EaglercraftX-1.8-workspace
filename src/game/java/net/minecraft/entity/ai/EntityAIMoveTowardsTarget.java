package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
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
public class EntityAIMoveTowardsTarget extends EntityAIBase {
	private EntityCreature theEntity;
	private EntityLivingBase targetEntity;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private double speed;
	private float maxTargetDistance;

	public EntityAIMoveTowardsTarget(EntityCreature creature, double speedIn, float targetMaxDistance) {
		this.theEntity = creature;
		this.speed = speedIn;
		this.maxTargetDistance = targetMaxDistance;
		this.setMutexBits(1);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		this.targetEntity = this.theEntity.getAttackTarget();
		if (this.targetEntity == null) {
			return false;
		} else if (this.targetEntity
				.getDistanceSqToEntity(this.theEntity) > (double) (this.maxTargetDistance * this.maxTargetDistance)) {
			return false;
		} else {
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7,
					new Vec3(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ));
			if (vec3 == null) {
				return false;
			} else {
				this.movePosX = vec3.xCoord;
				this.movePosY = vec3.yCoord;
				this.movePosZ = vec3.zCoord;
				return true;
			}
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return !this.theEntity.getNavigator().noPath() && this.targetEntity.isEntityAlive() && this.targetEntity
				.getDistanceSqToEntity(this.theEntity) < (double) (this.maxTargetDistance * this.maxTargetDistance);
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.targetEntity = null;
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
	}
}