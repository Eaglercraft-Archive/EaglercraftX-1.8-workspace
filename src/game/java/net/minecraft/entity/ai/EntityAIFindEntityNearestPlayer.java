package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class EntityAIFindEntityNearestPlayer extends EntityAIBase {
	private static final Logger field_179436_a = LogManager.getLogger();
	private EntityLiving field_179434_b;
	private final Predicate<Entity> field_179435_c;
	private final EntityAINearestAttackableTarget.Sorter field_179432_d;
	private EntityLivingBase field_179433_e;

	public EntityAIFindEntityNearestPlayer(EntityLiving parEntityLiving) {
		this.field_179434_b = parEntityLiving;
		if (parEntityLiving instanceof EntityCreature) {
			field_179436_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
		}

		this.field_179435_c = new Predicate<Entity>() {
			public boolean apply(Entity entity) {
				if (!(entity instanceof EntityPlayer)) {
					return false;
				} else if (((EntityPlayer) entity).capabilities.disableDamage) {
					return false;
				} else {
					double d0 = EntityAIFindEntityNearestPlayer.this.func_179431_f();
					if (entity.isSneaking()) {
						d0 *= 0.800000011920929D;
					}

					if (entity.isInvisible()) {
						float f = ((EntityPlayer) entity).getArmorVisibility();
						if (f < 0.1F) {
							f = 0.1F;
						}

						d0 *= (double) (0.7F * f);
					}

					return (double) entity.getDistanceToEntity(EntityAIFindEntityNearestPlayer.this.field_179434_b) > d0
							? false
							: EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPlayer.this.field_179434_b,
									(EntityLivingBase) entity, false, true);
				}
			}
		};
		this.field_179432_d = new EntityAINearestAttackableTarget.Sorter(parEntityLiving);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		double d0 = this.func_179431_f();
		List list = this.field_179434_b.worldObj.getEntitiesWithinAABB(EntityPlayer.class,
				this.field_179434_b.getEntityBoundingBox().expand(d0, 4.0D, d0), this.field_179435_c);
		Collections.sort(list, this.field_179432_d);
		if (list.isEmpty()) {
			return false;
		} else {
			this.field_179433_e = (EntityLivingBase) list.get(0);
			return true;
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		EntityLivingBase entitylivingbase = this.field_179434_b.getAttackTarget();
		if (entitylivingbase == null) {
			return false;
		} else if (!entitylivingbase.isEntityAlive()) {
			return false;
		} else if (entitylivingbase instanceof EntityPlayer
				&& ((EntityPlayer) entitylivingbase).capabilities.disableDamage) {
			return false;
		} else {
			Team team = this.field_179434_b.getTeam();
			Team team1 = entitylivingbase.getTeam();
			if (team != null && team1 == team) {
				return false;
			} else {
				double d0 = this.func_179431_f();
				return this.field_179434_b.getDistanceSqToEntity(entitylivingbase) > d0 * d0 ? false
						: !(entitylivingbase instanceof EntityPlayerMP)
								|| !((EntityPlayerMP) entitylivingbase).theItemInWorldManager.isCreative();
			}
		}
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.field_179434_b.setAttackTarget(this.field_179433_e);
		super.startExecuting();
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.field_179434_b.setAttackTarget((EntityLivingBase) null);
		super.startExecuting();
	}

	protected double func_179431_f() {
		IAttributeInstance iattributeinstance = this.field_179434_b
				.getEntityAttribute(SharedMonsterAttributes.followRange);
		return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
	}
}