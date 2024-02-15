package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.DifficultyInstance;
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
public class EntityRabbit extends EntityAnimal {
	private int field_175540_bm = 0;
	private int field_175535_bn = 0;
	private boolean field_175536_bo = false;
	private boolean field_175537_bp = false;
	private int currentMoveTypeDuration = 0;
	private EntityRabbit.EnumMoveType moveType = EntityRabbit.EnumMoveType.HOP;
	private int carrotTicks = 0;
	private EntityPlayer field_175543_bt = null;

	public EntityRabbit(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 0.7F);
	}

	public void setMoveType(EntityRabbit.EnumMoveType type) {
		this.moveType = type;
	}

	public float func_175521_o(float parFloat1) {
		return this.field_175535_bn == 0 ? 0.0F
				: ((float) this.field_175540_bm + parFloat1) / (float) this.field_175535_bn;
	}

	public void setJumping(boolean jump, EntityRabbit.EnumMoveType moveTypeIn) {
		super.setJumping(jump);
		if (!jump) {
			if (this.moveType == EntityRabbit.EnumMoveType.ATTACK) {
				this.moveType = EntityRabbit.EnumMoveType.HOP;
			}
		} else {
			this.playSound(this.getJumpingSound(), this.getSoundVolume(),
					((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
		}

		this.field_175536_bo = jump;
	}

	public void doMovementAction(EntityRabbit.EnumMoveType movetype) {
		this.setJumping(true, movetype);
		this.field_175535_bn = movetype.func_180073_d();
		this.field_175540_bm = 0;
	}

	public boolean func_175523_cj() {
		return this.field_175536_bo;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(18, Byte.valueOf((byte) 0));
	}

	/**+
	 * Attempts to create sprinting particles if the entity is
	 * sprinting and not in water.
	 */
	public void spawnRunningParticles() {
	}

	/**+
	 * Called frequently so the entity can update its state every
	 * tick as required. For example, zombies and skeletons use this
	 * to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.field_175540_bm != this.field_175535_bn) {
			++this.field_175540_bm;
		} else if (this.field_175535_bn != 0) {
			this.field_175540_bm = 0;
			this.field_175535_bn = 0;
		}

	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("RabbitType", this.getRabbitType());
		nbttagcompound.setInteger("MoreCarrotTicks", this.carrotTicks);
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setRabbitType(nbttagcompound.getInteger("RabbitType"));
		this.carrotTicks = nbttagcompound.getInteger("MoreCarrotTicks");
	}

	protected String getJumpingSound() {
		return "mob.rabbit.hop";
	}

	/**+
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.rabbit.idle";
	}

	/**+
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.rabbit.hurt";
	}

	/**+
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.rabbit.death";
	}

	public boolean attackEntityAsMob(Entity entity) {
		if (this.getRabbitType() == 99) {
			this.playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0F);
		} else {
			return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
		}
	}

	/**+
	 * Returns the current armor value as determined by a call to
	 * InventoryPlayer.getTotalArmorValue
	 */
	public int getTotalArmorValue() {
		return this.getRabbitType() == 99 ? 8 : super.getTotalArmorValue();
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		return this.isEntityInvulnerable(damagesource) ? false : super.attackEntityFrom(damagesource, f);
	}

	/**+
	 * Causes this Entity to drop a random item.
	 */
	protected void addRandomDrop() {
		this.entityDropItem(new ItemStack(Items.rabbit_foot, 1), 0.0F);
	}

	/**+
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean var1, int i) {
		int j = this.rand.nextInt(2) + this.rand.nextInt(1 + i);

		for (int k = 0; k < j; ++k) {
			this.dropItem(Items.rabbit_hide, 1);
		}

		j = this.rand.nextInt(2);

		for (int l = 0; l < j; ++l) {
			if (this.isBurning()) {
				this.dropItem(Items.cooked_rabbit, 1);
			} else {
				this.dropItem(Items.rabbit, 1);
			}
		}

	}

	private boolean isRabbitBreedingItem(Item itemIn) {
		return itemIn == Items.carrot || itemIn == Items.golden_carrot
				|| itemIn == Item.getItemFromBlock(Blocks.yellow_flower);
	}

	public EntityRabbit createChild(EntityAgeable entityageable) {
		EntityRabbit entityrabbit = new EntityRabbit(this.worldObj);
		if (entityageable instanceof EntityRabbit) {
			entityrabbit.setRabbitType(
					this.rand.nextBoolean() ? this.getRabbitType() : ((EntityRabbit) entityageable).getRabbitType());
		}

		return entityrabbit;
	}

	/**+
	 * Checks if the parameter is an item which this animal can be
	 * fed to breed it (wheat, carrots or seeds depending on the
	 * animal type)
	 */
	public boolean isBreedingItem(ItemStack itemstack) {
		return itemstack != null && this.isRabbitBreedingItem(itemstack.getItem());
	}

	public int getRabbitType() {
		return this.dataWatcher.getWatchableObjectByte(18);
	}

	public void setRabbitType(int rabbitTypeId) {
		this.dataWatcher.updateObject(18, Byte.valueOf((byte) rabbitTypeId));
	}

	/**+
	 * Called only once on an entity when first time spawned, via
	 * egg, mob spawner, natural spawning etc, but not called when
	 * entity is reloaded from nbt. Mainly used for initializing
	 * attributes and inventory
	 */
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyinstance,
			IEntityLivingData ientitylivingdata) {
		ientitylivingdata = super.onInitialSpawn(difficultyinstance, ientitylivingdata);
		int i = this.rand.nextInt(6);
		boolean flag = false;
		if (ientitylivingdata instanceof EntityRabbit.RabbitTypeData) {
			i = ((EntityRabbit.RabbitTypeData) ientitylivingdata).typeData;
			flag = true;
		} else {
			ientitylivingdata = new EntityRabbit.RabbitTypeData(i);
		}

		this.setRabbitType(i);
		if (flag) {
			this.setGrowingAge(-24000);
		}

		return ientitylivingdata;
	}

	/**+
	 * Returns duration of the current {@link
	 * net.minecraft.entity.passive.EntityRabbit.EnumMoveType move
	 * type}
	 */
	protected int getMoveTypeDuration() {
		return this.moveType.getDuration();
	}

	protected void createEatingParticles() {
		this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST,
				this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
				this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height),
				this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0.0D, 0.0D,
				0.0D, new int[] { Block.getStateId(Blocks.carrots.getStateFromMeta(7)) });
		this.carrotTicks = 100;
	}

	public void handleStatusUpdate(byte b0) {
		if (b0 == 1) {
			this.createRunningParticles();
			this.field_175535_bn = 10;
			this.field_175540_bm = 0;
		} else {
			super.handleStatusUpdate(b0);
		}

	}

	static enum EnumMoveType {
		NONE(0.0F, 0.0F, 30, 1), HOP(0.8F, 0.2F, 20, 10), STEP(1.0F, 0.45F, 14, 14), SPRINT(1.75F, 0.4F, 1, 8),
		ATTACK(2.0F, 0.7F, 7, 8);

		private final float speed;
		private final float field_180077_g;
		private final int duration;
		private final int field_180085_i;

		private EnumMoveType(float typeSpeed, float parFloat1, int typeDuration, int parInt1) {
			this.speed = typeSpeed;
			this.field_180077_g = parFloat1;
			this.duration = typeDuration;
			this.field_180085_i = parInt1;
		}

		public float getSpeed() {
			return this.speed;
		}

		public float func_180074_b() {
			return this.field_180077_g;
		}

		public int getDuration() {
			return this.duration;
		}

		public int func_180073_d() {
			return this.field_180085_i;
		}
	}

	public static class RabbitTypeData implements IEntityLivingData {
		public int typeData;

		public RabbitTypeData(int type) {
			this.typeData = type;
		}
	}
}