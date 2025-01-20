package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
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
public class EntityAIPlay extends EntityAIBase {
	private EntityVillager villagerObj;
	private EntityLivingBase targetVillager;
	private double speed;
	private int playTime;

	public EntityAIPlay(EntityVillager villagerObjIn, double speedIn) {
		this.villagerObj = villagerObjIn;
		this.speed = speedIn;
		this.setMutexBits(1);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.villagerObj.getGrowingAge() >= 0) {
			return false;
		} else if (this.villagerObj.getRNG().nextInt(400) != 0) {
			return false;
		} else {
			List<EntityVillager> list = this.villagerObj.worldObj.getEntitiesWithinAABB(EntityVillager.class,
					this.villagerObj.getEntityBoundingBox().expand(6.0D, 3.0D, 6.0D));
			double d0 = Double.MAX_VALUE;

			for (int i = 0, l = list.size(); i < l; ++i) {
				EntityVillager entityvillager = list.get(i);
				if (entityvillager != this.villagerObj && !entityvillager.isPlaying()
						&& entityvillager.getGrowingAge() < 0) {
					double d1 = entityvillager.getDistanceSqToEntity(this.villagerObj);
					if (d1 <= d0) {
						d0 = d1;
						this.targetVillager = entityvillager;
					}
				}
			}

			if (this.targetVillager == null) {
				Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
				if (vec3 == null) {
					return false;
				}
			}

			return true;
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return this.playTime > 0;
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		if (this.targetVillager != null) {
			this.villagerObj.setPlaying(true);
		}

		this.playTime = 1000;
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.villagerObj.setPlaying(false);
		this.targetVillager = null;
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		--this.playTime;
		if (this.targetVillager != null) {
			if (this.villagerObj.getDistanceSqToEntity(this.targetVillager) > 4.0D) {
				this.villagerObj.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.speed);
			}
		} else if (this.villagerObj.getNavigator().noPath()) {
			Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
			if (vec3 == null) {
				return;
			}

			this.villagerObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
		}

	}
}