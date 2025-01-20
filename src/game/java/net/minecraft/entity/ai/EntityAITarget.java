package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import org.apache.commons.lang3.StringUtils;

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
public abstract class EntityAITarget extends EntityAIBase {
	protected final EntityCreature taskOwner;
	protected boolean shouldCheckSight;
	private boolean nearbyOnly;
	private int targetSearchStatus;
	private int targetSearchDelay;
	private int targetUnseenTicks;

	public EntityAITarget(EntityCreature creature, boolean checkSight) {
		this(creature, checkSight, false);
	}

	public EntityAITarget(EntityCreature creature, boolean checkSight, boolean onlyNearby) {
		this.taskOwner = creature;
		this.shouldCheckSight = checkSight;
		this.nearbyOnly = onlyNearby;
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		EntityLivingBase entitylivingbase = this.taskOwner.getAttackTarget();
		if (entitylivingbase == null) {
			return false;
		} else if (!entitylivingbase.isEntityAlive()) {
			return false;
		} else {
			Team team = this.taskOwner.getTeam();
			Team team1 = entitylivingbase.getTeam();
			if (team != null && team1 == team) {
				return false;
			} else {
				double d0 = this.getTargetDistance();
				if (this.taskOwner.getDistanceSqToEntity(entitylivingbase) > d0 * d0) {
					return false;
				} else {
					if (this.shouldCheckSight) {
						if (this.taskOwner.getEntitySenses().canSee(entitylivingbase)) {
							this.targetUnseenTicks = 0;
						} else if (++this.targetUnseenTicks > 60) {
							return false;
						}
					}

					return !(entitylivingbase instanceof EntityPlayer)
							|| !((EntityPlayer) entitylivingbase).capabilities.disableDamage;
				}
			}
		}
	}

	protected double getTargetDistance() {
		IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.followRange);
		return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.targetSearchStatus = 0;
		this.targetSearchDelay = 0;
		this.targetUnseenTicks = 0;
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.taskOwner.setAttackTarget((EntityLivingBase) null);
	}

	/**+
	 * A method used to see if an entity is a suitable target
	 * through a number of checks. Args : entity,
	 * canTargetInvinciblePlayer
	 */
	public static boolean isSuitableTarget(EntityLiving attacker, EntityLivingBase target, boolean includeInvincibles,
			boolean checkSight) {
		if (target == null) {
			return false;
		} else if (target == attacker) {
			return false;
		} else if (!target.isEntityAlive()) {
			return false;
		} else if (!attacker.canAttackClass(target.getClass())) {
			return false;
		} else {
			Team team = attacker.getTeam();
			Team team1 = target.getTeam();
			if (team != null && team1 == team) {
				return false;
			} else {
				if (attacker instanceof IEntityOwnable
						&& StringUtils.isNotEmpty(((IEntityOwnable) attacker).getOwnerId())) {
					if (target instanceof IEntityOwnable && ((IEntityOwnable) attacker).getOwnerId()
							.equals(((IEntityOwnable) target).getOwnerId())) {
						return false;
					}

					if (target == ((IEntityOwnable) attacker).getOwner()) {
						return false;
					}
				} else if (target instanceof EntityPlayer && !includeInvincibles
						&& ((EntityPlayer) target).capabilities.disableDamage) {
					return false;
				}

				return !checkSight || attacker.getEntitySenses().canSee(target);
			}
		}
	}

	/**+
	 * A method used to see if an entity is a suitable target
	 * through a number of checks. Args : entity,
	 * canTargetInvinciblePlayer
	 */
	protected boolean isSuitableTarget(EntityLivingBase target, boolean includeInvincibles) {
		if (!isSuitableTarget(this.taskOwner, target, includeInvincibles, this.shouldCheckSight)) {
			return false;
		} else if (!this.taskOwner.isWithinHomeDistanceFromPosition(new BlockPos(target))) {
			return false;
		} else {
			if (this.nearbyOnly) {
				if (--this.targetSearchDelay <= 0) {
					this.targetSearchStatus = 0;
				}

				if (this.targetSearchStatus == 0) {
					this.targetSearchStatus = this.canEasilyReach(target) ? 1 : 2;
				}

				if (this.targetSearchStatus == 2) {
					return false;
				}
			}

			return true;
		}
	}

	/**+
	 * Checks to see if this entity can find a short path to the
	 * given target.
	 */
	private boolean canEasilyReach(EntityLivingBase parEntityLivingBase) {
		this.targetSearchDelay = 10 + this.taskOwner.getRNG().nextInt(5);
		PathEntity pathentity = this.taskOwner.getNavigator().getPathToEntityLiving(parEntityLivingBase);
		if (pathentity == null) {
			return false;
		} else {
			PathPoint pathpoint = pathentity.getFinalPathPoint();
			if (pathpoint == null) {
				return false;
			} else {
				int i = pathpoint.xCoord - MathHelper.floor_double(parEntityLivingBase.posX);
				int j = pathpoint.zCoord - MathHelper.floor_double(parEntityLivingBase.posZ);
				return (double) (i * i + j * j) <= 2.25D;
			}
		}
	}
}