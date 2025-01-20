package net.minecraft.entity.monster;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
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
public class EntityWitch extends EntityMob implements IRangedAttackMob {
	private static final EaglercraftUUID MODIFIER_UUID = EaglercraftUUID
			.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
	private static final AttributeModifier MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty",
			-0.25D, 0)).setSaved(false);
	/**+
	 * List of items a witch should drop on death.
	 */
	private static final Item[] witchDrops = new Item[] { Items.glowstone_dust, Items.sugar, Items.redstone,
			Items.spider_eye, Items.glass_bottle, Items.gunpowder, Items.stick, Items.stick };
	private int witchAttackTimer;

	public EntityWitch(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	protected void entityInit() {
		super.entityInit();
		this.getDataWatcher().addObject(21, Byte.valueOf((byte) 0));
	}

	/**+
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return null;
	}

	/**+
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return null;
	}

	/**+
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return null;
	}

	/**+
	 * Set whether this witch is aggressive at an entity.
	 */
	public void setAggressive(boolean aggressive) {
		this.getDataWatcher().updateObject(21, Byte.valueOf((byte) (aggressive ? 1 : 0)));
	}

	/**+
	 * Return whether this witch is aggressive at an entity.
	 */
	public boolean getAggressive() {
		return this.getDataWatcher().getWatchableObjectByte(21) == 1;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	/**+
	 * Called frequently so the entity can update its state every
	 * tick as required. For example, zombies and skeletons use this
	 * to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		if (!this.worldObj.isRemote) {
			if (this.getAggressive()) {
				if (this.witchAttackTimer-- <= 0) {
					this.setAggressive(false);
					ItemStack itemstack = this.getHeldItem();
					this.setCurrentItemOrArmor(0, (ItemStack) null);
					if (itemstack != null && itemstack.getItem() == Items.potionitem) {
						List<PotionEffect> list = Items.potionitem.getEffects(itemstack);
						if (list != null) {
							for (int i = 0, l = list.size(); i < l; ++i) {
								this.addPotionEffect(new PotionEffect(list.get(i)));
							}
						}
					}

					this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(MODIFIER);
				}
			} else {
				short short1 = -1;
				if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.water)
						&& !this.isPotionActive(Potion.waterBreathing)) {
					short1 = 8237;
				} else if (this.rand.nextFloat() < 0.15F && this.isBurning()
						&& !this.isPotionActive(Potion.fireResistance)) {
					short1 = 16307;
				} else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
					short1 = 16341;
				} else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null
						&& !this.isPotionActive(Potion.moveSpeed)
						&& this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
					short1 = 16274;
				} else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null
						&& !this.isPotionActive(Potion.moveSpeed)
						&& this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
					short1 = 16274;
				}

				if (short1 > -1) {
					this.setCurrentItemOrArmor(0, new ItemStack(Items.potionitem, 1, short1));
					this.witchAttackTimer = this.getHeldItem().getMaxItemUseDuration();
					this.setAggressive(true);
					IAttributeInstance iattributeinstance = this
							.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
					iattributeinstance.removeModifier(MODIFIER);
					iattributeinstance.applyModifier(MODIFIER);
				}
			}

			if (this.rand.nextFloat() < 7.5E-4F) {
				this.worldObj.setEntityState(this, (byte) 15);
			}
		}

		super.onLivingUpdate();
	}

	public void handleStatusUpdate(byte b0) {
		if (b0 == 15) {
			for (int i = 0; i < this.rand.nextInt(35) + 10; ++i) {
				this.worldObj.spawnParticle(EnumParticleTypes.SPELL_WITCH,
						this.posX + this.rand.nextGaussian() * 0.12999999523162842D,
						this.getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D,
						this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		} else {
			super.handleStatusUpdate(b0);
		}

	}

	/**+
	 * Reduces damage, depending on potions
	 */
	protected float applyPotionDamageCalculations(DamageSource damagesource, float f) {
		f = super.applyPotionDamageCalculations(damagesource, f);
		if (damagesource.getEntity() == this) {
			f = 0.0F;
		}

		if (damagesource.isMagicDamage()) {
			f = (float) ((double) f * 0.15D);
		}

		return f;
	}

	/**+
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean var1, int i) {
		int j = this.rand.nextInt(3) + 1;

		for (int k = 0; k < j; ++k) {
			int l = this.rand.nextInt(3);
			Item item = witchDrops[this.rand.nextInt(witchDrops.length)];
			if (i > 0) {
				l += this.rand.nextInt(i + 1);
			}

			for (int i1 = 0; i1 < l; ++i1) {
				this.dropItem(item, 1);
			}
		}

	}

	/**+
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float var2) {
		if (!this.getAggressive()) {
			EntityPotion entitypotion = new EntityPotion(this.worldObj, this, 32732);
			double d0 = entitylivingbase.posY + (double) entitylivingbase.getEyeHeight() - 1.100000023841858D;
			entitypotion.rotationPitch -= -20.0F;
			double d1 = entitylivingbase.posX + entitylivingbase.motionX - this.posX;
			double d2 = d0 - this.posY;
			double d3 = entitylivingbase.posZ + entitylivingbase.motionZ - this.posZ;
			float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
			if (f >= 8.0F && !entitylivingbase.isPotionActive(Potion.moveSlowdown)) {
				entitypotion.setPotionDamage(32698);
			} else if (entitylivingbase.getHealth() >= 8.0F && !entitylivingbase.isPotionActive(Potion.poison)) {
				entitypotion.setPotionDamage(32660);
			} else if (f <= 3.0F && !entitylivingbase.isPotionActive(Potion.weakness)
					&& this.rand.nextFloat() < 0.25F) {
				entitypotion.setPotionDamage(32696);
			}

			entitypotion.setThrowableHeading(d1, d2 + (double) (f * 0.2F), d3, 0.75F, 8.0F);
			this.worldObj.spawnEntityInWorld(entitypotion);
		}
	}

	public float getEyeHeight() {
		return 1.62F;
	}
}