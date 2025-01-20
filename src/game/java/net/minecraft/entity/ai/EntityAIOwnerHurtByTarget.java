package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

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
public class EntityAIOwnerHurtByTarget extends EntityAITarget {
	EntityTameable theDefendingTameable;
	EntityLivingBase theOwnerAttacker;
	private int field_142051_e;

	public EntityAIOwnerHurtByTarget(EntityTameable theDefendingTameableIn) {
		super(theDefendingTameableIn, false);
		this.theDefendingTameable = theDefendingTameableIn;
		this.setMutexBits(1);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (!this.theDefendingTameable.isTamed()) {
			return false;
		} else {
			EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwner();
			if (entitylivingbase == null) {
				return false;
			} else {
				this.theOwnerAttacker = entitylivingbase.getAITarget();
				int i = entitylivingbase.getRevengeTimer();
				return i != this.field_142051_e && this.isSuitableTarget(this.theOwnerAttacker, false)
						&& this.theDefendingTameable.shouldAttackEntity(this.theOwnerAttacker, entitylivingbase);
			}
		}
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.theOwnerAttacker);
		EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwner();
		if (entitylivingbase != null) {
			this.field_142051_e = entitylivingbase.getRevengeTimer();
		}

		super.startExecuting();
	}
}