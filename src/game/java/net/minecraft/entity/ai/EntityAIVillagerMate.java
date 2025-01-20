package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.BlockPos;
import net.minecraft.village.Village;
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
public class EntityAIVillagerMate extends EntityAIBase {
	private EntityVillager villagerObj;
	private EntityVillager mate;
	private World worldObj;
	private int matingTimeout;
	Village villageObj;

	public EntityAIVillagerMate(EntityVillager villagerIn) {
		this.villagerObj = villagerIn;
		this.worldObj = villagerIn.worldObj;
		this.setMutexBits(3);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.villagerObj.getGrowingAge() != 0) {
			return false;
		} else if (this.villagerObj.getRNG().nextInt(500) != 0) {
			return false;
		} else {
			this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos(this.villagerObj), 0);
			if (this.villageObj == null) {
				return false;
			} else if (this.checkSufficientDoorsPresentForNewVillager() && this.villagerObj.getIsWillingToMate(true)) {
				Entity entity = this.worldObj.findNearestEntityWithinAABB(EntityVillager.class,
						this.villagerObj.getEntityBoundingBox().expand(8.0D, 3.0D, 8.0D), this.villagerObj);
				if (entity == null) {
					return false;
				} else {
					this.mate = (EntityVillager) entity;
					return this.mate.getGrowingAge() == 0 && this.mate.getIsWillingToMate(true);
				}
			} else {
				return false;
			}
		}
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.matingTimeout = 300;
		this.villagerObj.setMating(true);
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.villageObj = null;
		this.mate = null;
		this.villagerObj.setMating(false);
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return this.matingTimeout >= 0 && this.checkSufficientDoorsPresentForNewVillager()
				&& this.villagerObj.getGrowingAge() == 0 && this.villagerObj.getIsWillingToMate(false);
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		--this.matingTimeout;
		this.villagerObj.getLookHelper().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
		if (this.villagerObj.getDistanceSqToEntity(this.mate) > 2.25D) {
			this.villagerObj.getNavigator().tryMoveToEntityLiving(this.mate, 0.25D);
		} else if (this.matingTimeout == 0 && this.mate.isMating()) {
			this.giveBirth();
		}

		if (this.villagerObj.getRNG().nextInt(35) == 0) {
			this.worldObj.setEntityState(this.villagerObj, (byte) 12);
		}

	}

	private boolean checkSufficientDoorsPresentForNewVillager() {
		if (!this.villageObj.isMatingSeason()) {
			return false;
		} else {
			int i = (int) ((double) ((float) this.villageObj.getNumVillageDoors()) * 0.35D);
			return this.villageObj.getNumVillagers() < i;
		}
	}

	private void giveBirth() {
		EntityVillager entityvillager = this.villagerObj.createChild(this.mate);
		this.mate.setGrowingAge(6000);
		this.villagerObj.setGrowingAge(6000);
		this.mate.setIsWillingToMate(false);
		this.villagerObj.setIsWillingToMate(false);
		entityvillager.setGrowingAge(-24000);
		entityvillager.setLocationAndAngles(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0F,
				0.0F);
		this.worldObj.spawnEntityInWorld(entityvillager);
		this.worldObj.setEntityState(entityvillager, (byte) 12);
	}
}