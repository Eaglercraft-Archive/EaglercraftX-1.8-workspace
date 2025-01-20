package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
public class EntityAILeapAtTarget extends EntityAIBase {
	EntityLiving leaper;
	EntityLivingBase leapTarget;
	float leapMotionY;

	public EntityAILeapAtTarget(EntityLiving leapingEntity, float leapMotionYIn) {
		this.leaper = leapingEntity;
		this.leapMotionY = leapMotionYIn;
		this.setMutexBits(5);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		this.leapTarget = this.leaper.getAttackTarget();
		if (this.leapTarget == null) {
			return false;
		} else {
			double d0 = this.leaper.getDistanceSqToEntity(this.leapTarget);
			return d0 >= 4.0D && d0 <= 16.0D ? (!this.leaper.onGround ? false : this.leaper.getRNG().nextInt(5) == 0)
					: false;
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return !this.leaper.onGround;
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		double d0 = this.leapTarget.posX - this.leaper.posX;
		double d1 = this.leapTarget.posZ - this.leaper.posZ;
		float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
		this.leaper.motionX += d0 / (double) f * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
		this.leaper.motionZ += d1 / (double) f * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
		this.leaper.motionY = (double) this.leapMotionY;
	}
}