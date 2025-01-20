package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
public class EntityAIOcelotAttack extends EntityAIBase {
	World theWorld;
	EntityLiving theEntity;
	EntityLivingBase theVictim;
	int attackCountdown;

	public EntityAIOcelotAttack(EntityLiving theEntityIn) {
		this.theEntity = theEntityIn;
		this.theWorld = theEntityIn.worldObj;
		this.setMutexBits(3);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
		if (entitylivingbase == null) {
			return false;
		} else {
			this.theVictim = entitylivingbase;
			return true;
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return !this.theVictim.isEntityAlive() ? false
				: (this.theEntity.getDistanceSqToEntity(this.theVictim) > 225.0D ? false
						: !this.theEntity.getNavigator().noPath() || this.shouldExecute());
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.theVictim = null;
		this.theEntity.getNavigator().clearPathEntity();
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		this.theEntity.getLookHelper().setLookPositionWithEntity(this.theVictim, 30.0F, 30.0F);
		double d0 = (double) (this.theEntity.width * 2.0F * this.theEntity.width * 2.0F);
		double d1 = this.theEntity.getDistanceSq(this.theVictim.posX, this.theVictim.getEntityBoundingBox().minY,
				this.theVictim.posZ);
		double d2 = 0.8D;
		if (d1 > d0 && d1 < 16.0D) {
			d2 = 1.33D;
		} else if (d1 < 225.0D) {
			d2 = 0.6D;
		}

		this.theEntity.getNavigator().tryMoveToEntityLiving(this.theVictim, d2);
		this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
		if (d1 <= d0) {
			if (this.attackCountdown <= 0) {
				this.attackCountdown = 20;
				this.theEntity.attackEntityAsMob(this.theVictim);
			}
		}
	}
}