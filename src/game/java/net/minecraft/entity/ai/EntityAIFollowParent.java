package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.passive.EntityAnimal;

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
public class EntityAIFollowParent extends EntityAIBase {
	EntityAnimal childAnimal;
	EntityAnimal parentAnimal;
	double moveSpeed;
	private int delayCounter;

	public EntityAIFollowParent(EntityAnimal animal, double speed) {
		this.childAnimal = animal;
		this.moveSpeed = speed;
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.childAnimal.getGrowingAge() >= 0) {
			return false;
		} else {
			List<EntityAnimal> list = this.childAnimal.worldObj.getEntitiesWithinAABB(this.childAnimal.getClass(),
					this.childAnimal.getEntityBoundingBox().expand(8.0D, 4.0D, 8.0D));
			EntityAnimal entityanimal = null;
			double d0 = Double.MAX_VALUE;

			for (int i = 0, l = list.size(); i < l; ++i) {
				EntityAnimal entityanimal1 = list.get(i);
				if (entityanimal1.getGrowingAge() >= 0) {
					double d1 = this.childAnimal.getDistanceSqToEntity(entityanimal1);
					if (d1 <= d0) {
						d0 = d1;
						entityanimal = entityanimal1;
					}
				}
			}

			if (entityanimal == null) {
				return false;
			} else if (d0 < 9.0D) {
				return false;
			} else {
				this.parentAnimal = entityanimal;
				return true;
			}
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		if (this.childAnimal.getGrowingAge() >= 0) {
			return false;
		} else if (!this.parentAnimal.isEntityAlive()) {
			return false;
		} else {
			double d0 = this.childAnimal.getDistanceSqToEntity(this.parentAnimal);
			return d0 >= 9.0D && d0 <= 256.0D;
		}
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.delayCounter = 0;
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.parentAnimal = null;
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		if (--this.delayCounter <= 0) {
			this.delayCounter = 10;
			this.childAnimal.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
		}
	}
}