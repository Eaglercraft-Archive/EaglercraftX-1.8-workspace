package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
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
public class EntityAIArrowAttack extends EntityAIBase {
	private final EntityLiving entityHost;
	private final IRangedAttackMob rangedAttackEntityHost;
	private EntityLivingBase attackTarget;
	private int rangedAttackTime;
	private double entityMoveSpeed;
	private int field_75318_f;
	private int field_96561_g;
	private int maxRangedAttackTime;
	private float field_96562_i;
	private float maxAttackDistance;

	public EntityAIArrowAttack(IRangedAttackMob attacker, double movespeed, int parInt1, float parFloat1) {
		this(attacker, movespeed, parInt1, parInt1, parFloat1);
	}

	public EntityAIArrowAttack(IRangedAttackMob attacker, double movespeed, int parInt1, int maxAttackTime,
			float maxAttackDistanceIn) {
		this.rangedAttackTime = -1;
		if (!(attacker instanceof EntityLivingBase)) {
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		} else {
			this.rangedAttackEntityHost = attacker;
			this.entityHost = (EntityLiving) attacker;
			this.entityMoveSpeed = movespeed;
			this.field_96561_g = parInt1;
			this.maxRangedAttackTime = maxAttackTime;
			this.field_96562_i = maxAttackDistanceIn;
			this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
			this.setMutexBits(3);
		}
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
		if (entitylivingbase == null) {
			return false;
		} else {
			this.attackTarget = entitylivingbase;
			return true;
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.attackTarget = null;
		this.field_75318_f = 0;
		this.rangedAttackTime = -1;
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY,
				this.attackTarget.posZ);
		boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);
		if (flag) {
			++this.field_75318_f;
		} else {
			this.field_75318_f = 0;
		}

		if (d0 <= (double) this.maxAttackDistance && this.field_75318_f >= 20) {
			this.entityHost.getNavigator().clearPathEntity();
		} else {
			this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
		}

		this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
		if (--this.rangedAttackTime == 0) {
			if (d0 > (double) this.maxAttackDistance || !flag) {
				return;
			}

			float f = MathHelper.sqrt_double(d0) / this.field_96562_i;
			float f1 = MathHelper.clamp_float(f, 0.1F, 1.0F);
			this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, f1);
			this.rangedAttackTime = MathHelper.floor_float(
					f * (float) (this.maxRangedAttackTime - this.field_96561_g) + (float) this.field_96561_g);
		} else if (this.rangedAttackTime < 0) {
			float f2 = MathHelper.sqrt_double(d0) / this.field_96562_i;
			this.rangedAttackTime = MathHelper.floor_float(
					f2 * (float) (this.maxRangedAttackTime - this.field_96561_g) + (float) this.field_96561_g);
		}

	}
}