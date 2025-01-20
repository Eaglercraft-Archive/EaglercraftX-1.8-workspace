package net.minecraft.entity.ai;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
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
public class EntityAIMate extends EntityAIBase {
	private EntityAnimal theAnimal;
	World theWorld;
	private EntityAnimal targetMate;
	int spawnBabyDelay;
	double moveSpeed;

	public EntityAIMate(EntityAnimal animal, double speedIn) {
		this.theAnimal = animal;
		this.theWorld = animal.worldObj;
		this.moveSpeed = speedIn;
		this.setMutexBits(3);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (!this.theAnimal.isInLove()) {
			return false;
		} else {
			this.targetMate = this.getNearbyMate();
			return this.targetMate != null;
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.targetMate = null;
		this.spawnBabyDelay = 0;
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F,
				(float) this.theAnimal.getVerticalFaceSpeed());
		this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
		++this.spawnBabyDelay;
		if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0D) {
			this.spawnBaby();
		}

	}

	/**+
	 * Loops through nearby animals and finds another animal of the
	 * same type that can be mated with. Returns the first valid
	 * mate found.
	 */
	private EntityAnimal getNearbyMate() {
		float f = 8.0F;
		List<EntityAnimal> list = this.theWorld.getEntitiesWithinAABB(this.theAnimal.getClass(),
				this.theAnimal.getEntityBoundingBox().expand((double) f, (double) f, (double) f));
		double d0 = Double.MAX_VALUE;
		EntityAnimal entityanimal = null;

		for (int i = 0, l = list.size(); i < l; ++i) {
			EntityAnimal entityanimal1 = list.get(i);
			if (this.theAnimal.canMateWith(entityanimal1) && this.theAnimal.getDistanceSqToEntity(entityanimal1) < d0) {
				entityanimal = entityanimal1;
				d0 = this.theAnimal.getDistanceSqToEntity(entityanimal1);
			}
		}

		return entityanimal;
	}

	/**+
	 * Spawns a baby animal of the same type.
	 */
	private void spawnBaby() {
		EntityAgeable entityageable = this.theAnimal.createChild(this.targetMate);
		if (entityageable != null) {
			EntityPlayer entityplayer = this.theAnimal.getPlayerInLove();
			if (entityplayer == null && this.targetMate.getPlayerInLove() != null) {
				entityplayer = this.targetMate.getPlayerInLove();
			}

			if (entityplayer != null) {
				entityplayer.triggerAchievement(StatList.animalsBredStat);
				if (this.theAnimal instanceof EntityCow) {
					entityplayer.triggerAchievement(AchievementList.breedCow);
				}
			}

			this.theAnimal.setGrowingAge(6000);
			this.targetMate.setGrowingAge(6000);
			this.theAnimal.resetInLove();
			this.targetMate.resetInLove();
			entityageable.setGrowingAge(-24000);
			entityageable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0F,
					0.0F);
			this.theWorld.spawnEntityInWorld(entityageable);
			EaglercraftRandom random = this.theAnimal.getRNG();

			for (int i = 0; i < 7; ++i) {
				double d0 = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				double d3 = random.nextDouble() * (double) this.theAnimal.width * 2.0D - (double) this.theAnimal.width;
				double d4 = 0.5D + random.nextDouble() * (double) this.theAnimal.height;
				double d5 = random.nextDouble() * (double) this.theAnimal.width * 2.0D - (double) this.theAnimal.width;
				this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + d3, this.theAnimal.posY + d4,
						this.theAnimal.posZ + d5, d0, d1, d2, new int[0]);
			}

			if (this.theWorld.getGameRules().getBoolean("doMobLoot")) {
				this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX,
						this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
			}

		}
	}
}