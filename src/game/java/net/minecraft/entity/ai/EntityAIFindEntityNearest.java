package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
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
public class EntityAIFindEntityNearest extends EntityAIBase {
	private static final Logger field_179444_a = LogManager.getLogger();
	private EntityLiving field_179442_b;
	private final Predicate<EntityLivingBase> field_179443_c;
	private final EntityAINearestAttackableTarget.Sorter field_179440_d;
	private EntityLivingBase field_179441_e;
	private Class<? extends EntityLivingBase> field_179439_f;

	public EntityAIFindEntityNearest(EntityLiving parEntityLiving, Class<? extends EntityLivingBase> parClass1) {
		this.field_179442_b = parEntityLiving;
		this.field_179439_f = parClass1;
		if (parEntityLiving instanceof EntityCreature) {
			field_179444_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
		}

		this.field_179443_c = new Predicate<EntityLivingBase>() {
			public boolean apply(EntityLivingBase entitylivingbase) {
				double d0 = EntityAIFindEntityNearest.this.func_179438_f();
				if (entitylivingbase.isSneaking()) {
					d0 *= 0.800000011920929D;
				}

				return entitylivingbase.isInvisible() ? false
						: ((double) entitylivingbase
								.getDistanceToEntity(EntityAIFindEntityNearest.this.field_179442_b) > d0 ? false
										: EntityAITarget.isSuitableTarget(EntityAIFindEntityNearest.this.field_179442_b,
												entitylivingbase, false, true));
			}
		};
		this.field_179440_d = new EntityAINearestAttackableTarget.Sorter(parEntityLiving);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		double d0 = this.func_179438_f();
		List list = this.field_179442_b.worldObj.getEntitiesWithinAABB(this.field_179439_f,
				this.field_179442_b.getEntityBoundingBox().expand(d0, 4.0D, d0), this.field_179443_c);
		Collections.sort(list, this.field_179440_d);
		if (list.isEmpty()) {
			return false;
		} else {
			this.field_179441_e = (EntityLivingBase) list.get(0);
			return true;
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		EntityLivingBase entitylivingbase = this.field_179442_b.getAttackTarget();
		if (entitylivingbase == null) {
			return false;
		} else if (!entitylivingbase.isEntityAlive()) {
			return false;
		} else {
			double d0 = this.func_179438_f();
			return this.field_179442_b.getDistanceSqToEntity(entitylivingbase) > d0 * d0 ? false
					: !(entitylivingbase instanceof EntityPlayerMP)
							|| !((EntityPlayerMP) entitylivingbase).theItemInWorldManager.isCreative();
		}
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.field_179442_b.setAttackTarget(this.field_179441_e);
		super.startExecuting();
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.field_179442_b.setAttackTarget((EntityLivingBase) null);
		super.startExecuting();
	}

	protected double func_179438_f() {
		IAttributeInstance iattributeinstance = this.field_179442_b
				.getEntityAttribute(SharedMonsterAttributes.followRange);
		return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
	}
}