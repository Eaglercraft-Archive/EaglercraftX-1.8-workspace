package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;

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
public class EntityAINearestAttackableTarget<T extends EntityLivingBase> extends EntityAITarget {
	protected final Class<T> targetClass;
	private final int targetChance;
	protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
	protected Predicate<? super T> targetEntitySelector;
	protected EntityLivingBase targetEntity;

	public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight) {
		this(creature, classTarget, checkSight, false);
	}

	public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight,
			boolean onlyNearby) {
		this(creature, classTarget, 10, checkSight, onlyNearby, (Predicate<? super T>) null);
	}

	public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, int chance,
			boolean checkSight, boolean onlyNearby, final Predicate<? super T> targetSelector) {
		super(creature, checkSight, onlyNearby);
		this.targetClass = classTarget;
		this.targetChance = chance;
		this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(creature);
		this.setMutexBits(1);
		this.targetEntitySelector = new Predicate<T>() {
			public boolean apply(T entitylivingbase) {
				if (targetSelector != null && !targetSelector.apply(entitylivingbase)) {
					return false;
				} else {
					if (entitylivingbase instanceof EntityPlayer) {
						double d0 = EntityAINearestAttackableTarget.this.getTargetDistance();
						if (entitylivingbase.isSneaking()) {
							d0 *= 0.800000011920929D;
						}

						if (entitylivingbase.isInvisible()) {
							float f = ((EntityPlayer) entitylivingbase).getArmorVisibility();
							if (f < 0.1F) {
								f = 0.1F;
							}

							d0 *= (double) (0.7F * f);
						}

						if ((double) entitylivingbase
								.getDistanceToEntity(EntityAINearestAttackableTarget.this.taskOwner) > d0) {
							return false;
						}
					}

					return EntityAINearestAttackableTarget.this.isSuitableTarget(entitylivingbase, false);
				}
			}
		};
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
			return false;
		} else {
			double d0 = this.getTargetDistance();
			List list = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass,
					this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0),
					Predicates.and(this.targetEntitySelector, EntitySelectors.NOT_SPECTATING));
			Collections.sort(list, this.theNearestAttackableTargetSorter);
			if (list.isEmpty()) {
				return false;
			} else {
				this.targetEntity = (EntityLivingBase) list.get(0);
				return true;
			}
		}
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}

	public static class Sorter implements Comparator<Entity> {
		private final Entity theEntity;

		public Sorter(Entity theEntityIn) {
			this.theEntity = theEntityIn;
		}

		public int compare(Entity entity, Entity entity1) {
			double d0 = this.theEntity.getDistanceSqToEntity(entity);
			double d1 = this.theEntity.getDistanceSqToEntity(entity1);
			return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
		}
	}
}