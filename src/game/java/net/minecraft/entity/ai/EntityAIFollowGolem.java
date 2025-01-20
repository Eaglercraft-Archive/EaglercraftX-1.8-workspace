package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

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
public class EntityAIFollowGolem extends EntityAIBase {
	private EntityVillager theVillager;
	private EntityIronGolem theGolem;
	private int takeGolemRoseTick;
	private boolean tookGolemRose;

	public EntityAIFollowGolem(EntityVillager theVillagerIn) {
		this.theVillager = theVillagerIn;
		this.setMutexBits(3);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.theVillager.getGrowingAge() >= 0) {
			return false;
		} else if (!this.theVillager.worldObj.isDaytime()) {
			return false;
		} else {
			List<EntityIronGolem> list = this.theVillager.worldObj.getEntitiesWithinAABB(EntityIronGolem.class,
					this.theVillager.getEntityBoundingBox().expand(6.0D, 2.0D, 6.0D));
			if (list.isEmpty()) {
				return false;
			} else {
				for (int i = 0, l = list.size(); i < l; ++i) {
					EntityIronGolem entityirongolem = list.get(i);
					if (entityirongolem.getHoldRoseTick() > 0) {
						this.theGolem = entityirongolem;
						break;
					}
				}

				return this.theGolem != null;
			}
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return this.theGolem.getHoldRoseTick() > 0;
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.takeGolemRoseTick = this.theVillager.getRNG().nextInt(320);
		this.tookGolemRose = false;
		this.theGolem.getNavigator().clearPathEntity();
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.theGolem = null;
		this.theVillager.getNavigator().clearPathEntity();
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		this.theVillager.getLookHelper().setLookPositionWithEntity(this.theGolem, 30.0F, 30.0F);
		if (this.theGolem.getHoldRoseTick() == this.takeGolemRoseTick) {
			this.theVillager.getNavigator().tryMoveToEntityLiving(this.theGolem, 0.5D);
			this.tookGolemRose = true;
		}

		if (this.tookGolemRose && this.theVillager.getDistanceSqToEntity(this.theGolem) < 4.0D) {
			this.theGolem.setHoldingRose(false);
			this.theVillager.getNavigator().clearPathEntity();
		}

	}
}