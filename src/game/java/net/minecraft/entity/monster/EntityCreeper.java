package net.minecraft.entity.monster;

import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
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
public class EntityCreeper extends EntityMob {
	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 30;
	/**+
	 * Explosion radius for this creeper.
	 */
	private int explosionRadius = 3;
	private int field_175494_bm = 0;

	public EntityCreeper(World worldIn) {
		super(worldIn);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAICreeperSwell(this));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	/**+
	 * The maximum height from where the entity is alowed to jump
	 * (used in pathfinder)
	 */
	public int getMaxFallHeight() {
		return this.getAttackTarget() == null ? 3 : 3 + (int) (this.getHealth() - 1.0F);
	}

	public void fall(float f, float f1) {
		super.fall(f, f1);
		this.timeSinceIgnited = (int) ((float) this.timeSinceIgnited + f * 1.5F);
		if (this.timeSinceIgnited > this.fuseTime - 5) {
			this.timeSinceIgnited = this.fuseTime - 5;
		}

	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte) -1));
		this.dataWatcher.addObject(17, Byte.valueOf((byte) 0));
		this.dataWatcher.addObject(18, Byte.valueOf((byte) 0));
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		if (this.dataWatcher.getWatchableObjectByte(17) == 1) {
			nbttagcompound.setBoolean("powered", true);
		}

		nbttagcompound.setShort("Fuse", (short) this.fuseTime);
		nbttagcompound.setByte("ExplosionRadius", (byte) this.explosionRadius);
		nbttagcompound.setBoolean("ignited", this.hasIgnited());
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.dataWatcher.updateObject(17, Byte.valueOf((byte) (nbttagcompound.getBoolean("powered") ? 1 : 0)));
		if (nbttagcompound.hasKey("Fuse", 99)) {
			this.fuseTime = nbttagcompound.getShort("Fuse");
		}

		if (nbttagcompound.hasKey("ExplosionRadius", 99)) {
			this.explosionRadius = nbttagcompound.getByte("ExplosionRadius");
		}

		if (nbttagcompound.getBoolean("ignited")) {
			this.ignite();
		}

	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		if (this.isEntityAlive()) {
			this.lastActiveTime = this.timeSinceIgnited;
			if (this.hasIgnited()) {
				this.setCreeperState(1);
			}

			int i = this.getCreeperState();
			if (i > 0 && this.timeSinceIgnited == 0) {
				this.playSound("creeper.primed", 1.0F, 0.5F);
			}

			this.timeSinceIgnited += i;
			if (this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}

			if (this.timeSinceIgnited >= this.fuseTime) {
				this.timeSinceIgnited = this.fuseTime;
				this.explode();
			}
		}

		super.onUpdate();
	}

	/**+
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.creeper.say";
	}

	/**+
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.creeper.death";
	}

	/**+
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		if (damagesource.getEntity() instanceof EntitySkeleton) {
			int i = Item.getIdFromItem(Items.record_13);
			int j = Item.getIdFromItem(Items.record_wait);
			int k = i + this.rand.nextInt(j - i + 1);
			this.dropItem(Item.getItemById(k), 1);
		} else if (damagesource.getEntity() instanceof EntityCreeper && damagesource.getEntity() != this
				&& ((EntityCreeper) damagesource.getEntity()).getPowered()
				&& ((EntityCreeper) damagesource.getEntity()).isAIEnabled()) {
			((EntityCreeper) damagesource.getEntity()).func_175493_co();
			this.entityDropItem(new ItemStack(Items.skull, 1, 4), 0.0F);
		}

	}

	public boolean attackEntityAsMob(Entity var1) {
		return true;
	}

	/**+
	 * Returns true if the creeper is powered by a lightning bolt.
	 */
	public boolean getPowered() {
		return this.dataWatcher.getWatchableObjectByte(17) == 1;
	}

	/**+
	 * Params: (Float)Render tick. Returns the intensity of the
	 * creeper's flash when it is ignited.
	 */
	public float getCreeperFlashIntensity(float parFloat1) {
		return ((float) this.lastActiveTime + (float) (this.timeSinceIgnited - this.lastActiveTime) * parFloat1)
				/ (float) (this.fuseTime - 2);
	}

	protected Item getDropItem() {
		return Items.gunpowder;
	}

	/**+
	 * Returns the current state of creeper, -1 is idle, 1 is 'in
	 * fuse'
	 */
	public int getCreeperState() {
		return this.dataWatcher.getWatchableObjectByte(16);
	}

	/**+
	 * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
	 */
	public void setCreeperState(int state) {
		this.dataWatcher.updateObject(16, Byte.valueOf((byte) state));
	}

	/**+
	 * Called when a lightning bolt hits the entity.
	 */
	public void onStruckByLightning(EntityLightningBolt entitylightningbolt) {
		super.onStruckByLightning(entitylightningbolt);
		this.dataWatcher.updateObject(17, Byte.valueOf((byte) 1));
	}

	/**+
	 * Called when a player interacts with a mob. e.g. gets milk
	 * from a cow, gets into the saddle on a pig.
	 */
	protected boolean interact(EntityPlayer entityplayer) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack != null && itemstack.getItem() == Items.flint_and_steel) {
			this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.ignite", 1.0F,
					this.rand.nextFloat() * 0.4F + 0.8F);
			entityplayer.swingItem();
			if (!this.worldObj.isRemote) {
				this.ignite();
				itemstack.damageItem(1, entityplayer);
				return true;
			}
		}

		return super.interact(entityplayer);
	}

	/**+
	 * Creates an explosion as determined by this creeper's power
	 * and explosion radius.
	 */
	private void explode() {
		if (!this.worldObj.isRemote) {
			boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
			float f = this.getPowered() ? 2.0F : 1.0F;
			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float) this.explosionRadius * f,
					flag);
			this.setDead();
		}

	}

	public boolean hasIgnited() {
		return this.dataWatcher.getWatchableObjectByte(18) != 0;
	}

	public void ignite() {
		this.dataWatcher.updateObject(18, Byte.valueOf((byte) 1));
	}

	/**+
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return this.field_175494_bm < 1 && this.worldObj.getGameRules().getBoolean("doMobLoot");
	}

	public void func_175493_co() {
		++this.field_175494_bm;
	}

	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
		super.renderDynamicLightsEaglerAt(entityX, entityY, entityZ, renderX, renderY, renderZ, partialTicks,
				isInFrustum);
		float ff = getCreeperFlashIntensity(partialTicks);
		if ((int) (ff * 10.0F) % 2 != 0) {
			float dynamicLightMag = 7.0f;
			DynamicLightManager.renderDynamicLight("entity_" + getEntityId() + "_creeper_flash", entityX, entityY + 1.0,
					entityZ, dynamicLightMag, dynamicLightMag * 0.7792f, dynamicLightMag * 0.618f, false);
			DeferredStateManager.setEmissionConstant(1.0f);
		}
	}

	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
		float f = super.getEaglerDynamicLightsValueSimple(partialTicks);
		float ff = getCreeperFlashIntensity(partialTicks);
		if ((int) (ff * 10.0F) % 2 != 0) {
			f = Math.min(f + 0.5f, 1.15f);
		}
		return f;
	}
}