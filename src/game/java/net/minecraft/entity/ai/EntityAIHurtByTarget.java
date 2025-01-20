package net.minecraft.entity.ai;

import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;

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
public class EntityAIHurtByTarget extends EntityAITarget {
	private boolean entityCallsForHelp;
	private int revengeTimerOld;
	private final Class[] targetClasses;

	public EntityAIHurtByTarget(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class... targetClassesIn) {
		super(creatureIn, false);
		this.entityCallsForHelp = entityCallsForHelpIn;
		this.targetClasses = targetClassesIn;
		this.setMutexBits(1);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		int i = this.taskOwner.getRevengeTimer();
		return i != this.revengeTimerOld && this.isSuitableTarget(this.taskOwner.getAITarget(), false);
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
		this.revengeTimerOld = this.taskOwner.getRevengeTimer();
		if (this.entityCallsForHelp) {
			double d0 = this.getTargetDistance();

			List<EntityCreature> lst = this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(),
					(new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ,
							this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D))
									.expand(d0, 10.0D, d0));
			for (int i = 0, l = lst.size(); i < l; ++i) {
				EntityCreature entitycreature = lst.get(i);
				if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null
						&& !entitycreature.isOnSameTeam(this.taskOwner.getAITarget())) {
					boolean flag = false;

					for (int j = 0; j < this.targetClasses.length; ++j) {
						if (entitycreature.getClass() == this.targetClasses[j]) {
							flag = true;
							break;
						}
					}

					if (!flag) {
						this.setEntityAttackTarget(entitycreature, this.taskOwner.getAITarget());
					}
				}
			}
		}

		super.startExecuting();
	}

	protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
		creatureIn.setAttackTarget(entityLivingBaseIn);
	}
}