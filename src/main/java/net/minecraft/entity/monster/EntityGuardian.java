package net.minecraft.entity.monster;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class EntityGuardian extends EntityMob {
	private float field_175482_b;
	private float field_175484_c;
	private float field_175483_bk;
	private float field_175485_bl;
	private float field_175486_bm;
	private EntityLivingBase targetedEntity;
	private int field_175479_bo;
	private boolean field_175480_bp;

	public EntityGuardian(World worldIn) {
		super(worldIn);
		this.experienceValue = 10;
		this.setSize(0.85F, 0.85F);
		this.field_175484_c = this.field_175482_b = this.rand.nextFloat();
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setElder(nbttagcompound.getBoolean("Elder"));
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Elder", this.isElder());
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Integer.valueOf(0));
		this.dataWatcher.addObject(17, Integer.valueOf(0));
	}

	/**+
	 * Returns true if given flag is set
	 */
	private boolean isSyncedFlagSet(int flagId) {
		return (this.dataWatcher.getWatchableObjectInt(16) & flagId) != 0;
	}

	/**+
	 * Sets a flag state "on/off" on both sides (client/server) by
	 * using DataWatcher
	 */
	private void setSyncedFlag(int flagId, boolean state) {
		int i = this.dataWatcher.getWatchableObjectInt(16);
		if (state) {
			this.dataWatcher.updateObject(16, Integer.valueOf(i | flagId));
		} else {
			this.dataWatcher.updateObject(16, Integer.valueOf(i & ~flagId));
		}

	}

	public boolean func_175472_n() {
		return this.isSyncedFlagSet(2);
	}

	public int func_175464_ck() {
		return this.isElder() ? 60 : 80;
	}

	public boolean isElder() {
		return this.isSyncedFlagSet(4);
	}

	/**+
	 * Sets this Guardian to be an elder or not.
	 */
	public void setElder(boolean elder) {
		this.setSyncedFlag(4, elder);
		if (elder) {
			this.setSize(1.9975F, 1.9975F);
			this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
			this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D);
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0D);
			this.enablePersistence();
		}

	}

	/**+
	 * Sets this Guardian to be an elder or not.
	 */
	public void setElder() {
		this.setElder(true);
		this.field_175486_bm = this.field_175485_bl = 1.0F;
	}

	public boolean hasTargetedEntity() {
		return this.dataWatcher.getWatchableObjectInt(17) != 0;
	}

	public EntityLivingBase getTargetedEntity() {
		if (!this.hasTargetedEntity()) {
			return null;
		} else {
			if (this.targetedEntity != null) {
				return this.targetedEntity;
			} else {
				Entity entity = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(17));
				if (entity instanceof EntityLivingBase) {
					this.targetedEntity = (EntityLivingBase) entity;
					return this.targetedEntity;
				} else {
					return null;
				}
			}
		}
	}

	public void onDataWatcherUpdate(int i) {
		super.onDataWatcherUpdate(i);
		if (i == 16) {
			if (this.isElder() && this.width < 1.0F) {
				this.setSize(1.9975F, 1.9975F);
			}
		} else if (i == 17) {
			this.field_175479_bo = 0;
			this.targetedEntity = null;
		}

	}

	/**+
	 * Get number of ticks, at least during which the living entity
	 * will be silent.
	 */
	public int getTalkInterval() {
		return 160;
	}

	/**+
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return !this.isInWater() ? "mob.guardian.land.idle"
				: (this.isElder() ? "mob.guardian.elder.idle" : "mob.guardian.idle");
	}

	/**+
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return !this.isInWater() ? "mob.guardian.land.hit"
				: (this.isElder() ? "mob.guardian.elder.hit" : "mob.guardian.hit");
	}

	/**+
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return !this.isInWater() ? "mob.guardian.land.death"
				: (this.isElder() ? "mob.guardian.elder.death" : "mob.guardian.death");
	}

	/**+
	 * returns if this entity triggers Block.onEntityWalking on the
	 * blocks they walk on. used for spiders and wolves to prevent
	 * them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	public float getEyeHeight() {
		return this.height * 0.5F;
	}

	public float getBlockPathWeight(BlockPos blockpos) {
		return this.worldObj.getBlockState(blockpos).getBlock().getMaterial() == Material.water
				? 10.0F + this.worldObj.getLightBrightness(blockpos) - 0.5F
				: super.getBlockPathWeight(blockpos);
	}

	/**+
	 * Called frequently so the entity can update its state every
	 * tick as required. For example, zombies and skeletons use this
	 * to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		this.field_175484_c = this.field_175482_b;
		if (!this.isInWater()) {
			this.field_175483_bk = 2.0F;
			if (this.motionY > 0.0D && this.field_175480_bp && !this.isSilent()) {
				this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0F, 1.0F, false);
			}

			this.field_175480_bp = this.motionY < 0.0D
					&& this.worldObj.isBlockNormalCube((new BlockPos(this)).down(), false);
		} else if (this.func_175472_n()) {
			if (this.field_175483_bk < 0.5F) {
				this.field_175483_bk = 4.0F;
			} else {
				this.field_175483_bk += (0.5F - this.field_175483_bk) * 0.1F;
			}
		} else {
			this.field_175483_bk += (0.125F - this.field_175483_bk) * 0.2F;
		}

		this.field_175482_b += this.field_175483_bk;
		this.field_175486_bm = this.field_175485_bl;
		if (!this.isInWater()) {
			this.field_175485_bl = this.rand.nextFloat();
		} else if (this.func_175472_n()) {
			this.field_175485_bl += (0.0F - this.field_175485_bl) * 0.25F;
		} else {
			this.field_175485_bl += (1.0F - this.field_175485_bl) * 0.06F;
		}

		if (this.func_175472_n() && this.isInWater()) {
			Vec3 vec3 = this.getLook(0.0F);

			for (int i = 0; i < 2; ++i) {
				this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE,
						this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width - vec3.xCoord * 1.5D,
						this.posY + this.rand.nextDouble() * (double) this.height - vec3.yCoord * 1.5D,
						this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width - vec3.zCoord * 1.5D, 0.0D,
						0.0D, 0.0D, new int[0]);
			}
		}

		if (this.hasTargetedEntity()) {
			if (this.field_175479_bo < this.func_175464_ck()) {
				++this.field_175479_bo;
			}

			EntityLivingBase entitylivingbase = this.getTargetedEntity();
			if (entitylivingbase != null) {
				double d5 = (double) this.func_175477_p(0.0F);
				double d0 = entitylivingbase.posX - this.posX;
				double d1 = entitylivingbase.posY + (double) (entitylivingbase.height * 0.5F)
						- (this.posY + (double) this.getEyeHeight());
				double d2 = entitylivingbase.posZ - this.posZ;
				double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
				d0 = d0 / d3;
				d1 = d1 / d3;
				d2 = d2 / d3;
				double d4 = this.rand.nextDouble();

				while (d4 < d3) {
					d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
					this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d0 * d4,
							this.posY + d1 * d4 + (double) this.getEyeHeight(), this.posZ + d2 * d4, 0.0D, 0.0D, 0.0D,
							new int[0]);
				}
			}
		}

		if (this.inWater) {
			this.setAir(300);
		} else if (this.onGround) {
			this.motionY += 0.5D;
			this.motionX += (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F);
			this.motionZ += (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F);
			this.rotationYaw = this.rand.nextFloat() * 360.0F;
			this.onGround = false;
			this.isAirBorne = true;
		}

		if (this.hasTargetedEntity()) {
			this.rotationYaw = this.rotationYawHead;
		}

		super.onLivingUpdate();
	}

	public float func_175471_a(float parFloat1) {
		return this.field_175484_c + (this.field_175482_b - this.field_175484_c) * parFloat1;
	}

	public float func_175469_o(float parFloat1) {
		return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * parFloat1;
	}

	public float func_175477_p(float parFloat1) {
		return ((float) this.field_175479_bo + parFloat1) / (float) this.func_175464_ck();
	}

	/**+
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean flag, int i) {
		int j = this.rand.nextInt(3) + this.rand.nextInt(i + 1);
		if (j > 0) {
			this.entityDropItem(new ItemStack(Items.prismarine_shard, j, 0), 1.0F);
		}

		if (this.rand.nextInt(3 + i) > 1) {
			this.entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 1.0F);
		} else if (this.rand.nextInt(3 + i) > 1) {
			this.entityDropItem(new ItemStack(Items.prismarine_crystals, 1, 0), 1.0F);
		}

		if (flag && this.isElder()) {
			this.entityDropItem(new ItemStack(Blocks.sponge, 1, 1), 1.0F);
		}

	}

	/**+
	 * Causes this Entity to drop a random item.
	 */
	protected void addRandomDrop() {
		ItemStack itemstack = ((WeightedRandomFishable) WeightedRandom.getRandomItem(this.rand,
				EntityFishHook.func_174855_j())).getItemStack(this.rand);
		this.entityDropItem(itemstack, 1.0F);
	}

	/**+
	 * Checks to make sure the light is not too bright where the mob
	 * is spawning
	 */
	protected boolean isValidLightLevel() {
		return true;
	}

	/**+
	 * Checks that the entity is not colliding with any blocks /
	 * liquids
	 */
	public boolean isNotColliding() {
		return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this)
				&& this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty();
	}

	/**+
	 * Checks if the entity's current position is a valid location
	 * to spawn this entity.
	 */
	public boolean getCanSpawnHere() {
		return (this.rand.nextInt(20) == 0 || !this.worldObj.canBlockSeeSky(new BlockPos(this)))
				&& super.getCanSpawnHere();
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		if (!this.func_175472_n() && !damagesource.isMagicDamage()
				&& damagesource.getSourceOfDamage() instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) damagesource.getSourceOfDamage();
			if (!damagesource.isExplosion()) {
				entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0F);
				entitylivingbase.playSound("damage.thorns", 0.5F, 1.0F);
			}
		}

		return super.attackEntityFrom(damagesource, f);
	}

	/**+
	 * The speed it takes to move the entityliving's rotationPitch
	 * through the faceEntity method. This is only currently use in
	 * wolves.
	 */
	public int getVerticalFaceSpeed() {
		return 180;
	}

	/**+
	 * Moves the entity based on the specified heading. Args:
	 * strafe, forward
	 */
	public void moveEntityWithHeading(float f, float f1) {
		if (this.isServerWorld()) {
			if (this.isInWater()) {
				this.moveFlying(f, f1, 0.1F);
				this.moveEntity(this.motionX, this.motionY, this.motionZ);
				this.motionX *= 0.8999999761581421D;
				this.motionY *= 0.8999999761581421D;
				this.motionZ *= 0.8999999761581421D;
				if (!this.func_175472_n() && this.getAttackTarget() == null) {
					this.motionY -= 0.005D;
				}
			} else {
				super.moveEntityWithHeading(f, f1);
			}
		} else {
			super.moveEntityWithHeading(f, f1);
		}

	}

}