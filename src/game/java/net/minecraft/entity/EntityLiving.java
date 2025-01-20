package net.minecraft.entity;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
public abstract class EntityLiving extends EntityLivingBase {
	public int livingSoundTime;
	protected int experienceValue;
	private EntityLookHelper lookHelper;
	protected EntityMoveHelper moveHelper;
	protected EntityJumpHelper jumpHelper;
	private EntityBodyHelper bodyHelper;
	protected PathNavigate navigator;
	protected final EntityAITasks tasks;
	protected final EntityAITasks targetTasks;
	private EntityLivingBase attackTarget;
	private EntitySenses senses;
	/**+
	 * Equipment (armor and held item) for this entity.
	 */
	private ItemStack[] equipment = new ItemStack[5];
	/**+
	 * Chances for each equipment piece from dropping when this
	 * entity dies.
	 */
	protected float[] equipmentDropChances = new float[5];
	private boolean canPickUpLoot;
	private boolean persistenceRequired;
	private boolean isLeashed;
	private Entity leashedToEntity;
	private NBTTagCompound leashNBTTag;

	public EntityLiving(World worldIn) {
		super(worldIn);
		this.tasks = new EntityAITasks();
		this.targetTasks = new EntityAITasks();
		this.lookHelper = new EntityLookHelper(this);
		this.moveHelper = new EntityMoveHelper(this);
		this.jumpHelper = new EntityJumpHelper(this);
		this.bodyHelper = new EntityBodyHelper(this);
		this.navigator = this.getNewNavigator(worldIn);
		this.senses = new EntitySenses(this);

		for (int i = 0; i < this.equipmentDropChances.length; ++i) {
			this.equipmentDropChances[i] = 0.085F;
		}

	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
	}

	/**+
	 * Returns new PathNavigateGround instance
	 */
	protected PathNavigate getNewNavigator(World worldIn) {
		return new PathNavigateGround(this, worldIn);
	}

	public EntityLookHelper getLookHelper() {
		return this.lookHelper;
	}

	public EntityMoveHelper getMoveHelper() {
		return this.moveHelper;
	}

	public EntityJumpHelper getJumpHelper() {
		return this.jumpHelper;
	}

	public PathNavigate getNavigator() {
		return this.navigator;
	}

	/**+
	 * returns the EntitySenses Object for the EntityLiving
	 */
	public EntitySenses getEntitySenses() {
		return this.senses;
	}

	/**+
	 * Gets the active target the Task system uses for tracking
	 */
	public EntityLivingBase getAttackTarget() {
		return this.attackTarget;
	}

	/**+
	 * Sets the active target the Task system uses for tracking
	 */
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
		this.attackTarget = entitylivingbaseIn;
	}

	/**+
	 * Returns true if this entity can attack entities of the
	 * specified class.
	 */
	public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
		return cls != EntityGhast.class;
	}

	/**+
	 * This function applies the benefits of growing back wool and
	 * faster growing up to the acting entity. (This function is
	 * used in the AIEatGrass)
	 */
	public void eatGrassBonus() {
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(15, Byte.valueOf((byte) 0));
	}

	/**+
	 * Get number of ticks, at least during which the living entity
	 * will be silent.
	 */
	public int getTalkInterval() {
		return 80;
	}

	/**+
	 * Plays living's sound at its position
	 */
	public void playLivingSound() {
		String s = this.getLivingSound();
		if (s != null) {
			this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
		}

	}

	/**+
	 * Gets called every tick from main Entity class
	 */
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (this.isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
			this.livingSoundTime = -this.getTalkInterval();
			this.playLivingSound();
		}
	}

	/**+
	 * Get the experience points the entity currently has.
	 */
	protected int getExperiencePoints(EntityPlayer var1) {
		if (this.experienceValue > 0) {
			int i = this.experienceValue;
			ItemStack[] aitemstack = this.getInventory();

			for (int j = 0; j < aitemstack.length; ++j) {
				if (aitemstack[j] != null && this.equipmentDropChances[j] <= 1.0F) {
					i += 1 + this.rand.nextInt(3);
				}
			}

			return i;
		} else {
			return this.experienceValue;
		}
	}

	/**+
	 * Spawns an explosion particle around the Entity's location
	 */
	public void spawnExplosionParticle() {
		if (this.worldObj.isRemote) {
			for (int i = 0; i < 20; ++i) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				double d3 = 10.0D;
				this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
						this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width
								- d0 * d3,
						this.posY + (double) (this.rand.nextFloat() * this.height) - d1 * d3, this.posZ
								+ (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width - d2 * d3,
						d0, d1, d2, new int[0]);
			}
		} else {
			this.worldObj.setEntityState(this, (byte) 20);
		}

	}

	public void handleStatusUpdate(byte b0) {
		if (b0 == 20) {
			this.spawnExplosionParticle();
		} else {
			super.handleStatusUpdate(b0);
		}

	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		if (!this.worldObj.isRemote) {
			this.updateLeashedState();
		}

	}

	protected float func_110146_f(float var1, float f) {
		this.bodyHelper.updateRenderAngles();
		return f;
	}

	/**+
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return null;
	}

	protected Item getDropItem() {
		return null;
	}

	/**+
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean var1, int i) {
		Item item = this.getDropItem();
		if (item != null) {
			int j = this.rand.nextInt(3);
			if (i > 0) {
				j += this.rand.nextInt(i + 1);
			}

			for (int k = 0; k < j; ++k) {
				this.dropItem(item, 1);
			}
		}

	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("CanPickUpLoot", this.canPickUpLoot());
		nbttagcompound.setBoolean("PersistenceRequired", this.persistenceRequired);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.equipment.length; ++i) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			if (this.equipment[i] != null) {
				this.equipment[i].writeToNBT(nbttagcompound1);
			}

			nbttaglist.appendTag(nbttagcompound1);
		}

		nbttagcompound.setTag("Equipment", nbttaglist);
		NBTTagList nbttaglist1 = new NBTTagList();

		for (int j = 0; j < this.equipmentDropChances.length; ++j) {
			nbttaglist1.appendTag(new NBTTagFloat(this.equipmentDropChances[j]));
		}

		nbttagcompound.setTag("DropChances", nbttaglist1);
		nbttagcompound.setBoolean("Leashed", this.isLeashed);
		if (this.leashedToEntity != null) {
			NBTTagCompound nbttagcompound2 = new NBTTagCompound();
			if (this.leashedToEntity instanceof EntityLivingBase) {
				nbttagcompound2.setLong("UUIDMost", this.leashedToEntity.getUniqueID().getMostSignificantBits());
				nbttagcompound2.setLong("UUIDLeast", this.leashedToEntity.getUniqueID().getLeastSignificantBits());
			} else if (this.leashedToEntity instanceof EntityHanging) {
				BlockPos blockpos = ((EntityHanging) this.leashedToEntity).getHangingPosition();
				nbttagcompound2.setInteger("X", blockpos.getX());
				nbttagcompound2.setInteger("Y", blockpos.getY());
				nbttagcompound2.setInteger("Z", blockpos.getZ());
			}

			nbttagcompound.setTag("Leash", nbttagcompound2);
		}

		if (this.isAIDisabled()) {
			nbttagcompound.setBoolean("NoAI", this.isAIDisabled());
		}

	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("CanPickUpLoot", 1)) {
			this.setCanPickUpLoot(nbttagcompound.getBoolean("CanPickUpLoot"));
		}

		this.persistenceRequired = nbttagcompound.getBoolean("PersistenceRequired");
		if (nbttagcompound.hasKey("Equipment", 9)) {
			NBTTagList nbttaglist = nbttagcompound.getTagList("Equipment", 10);

			for (int i = 0; i < this.equipment.length; ++i) {
				this.equipment[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
			}
		}

		if (nbttagcompound.hasKey("DropChances", 9)) {
			NBTTagList nbttaglist1 = nbttagcompound.getTagList("DropChances", 5);

			for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
				this.equipmentDropChances[j] = nbttaglist1.getFloatAt(j);
			}
		}

		this.isLeashed = nbttagcompound.getBoolean("Leashed");
		if (this.isLeashed && nbttagcompound.hasKey("Leash", 10)) {
			this.leashNBTTag = nbttagcompound.getCompoundTag("Leash");
		}

		this.setNoAI(nbttagcompound.getBoolean("NoAI"));
	}

	public void setMoveForward(float parFloat1) {
		this.moveForward = parFloat1;
	}

	/**+
	 * set the movespeed used for the new AI system
	 */
	public void setAIMoveSpeed(float f) {
		super.setAIMoveSpeed(f);
		this.setMoveForward(f);
	}

	/**+
	 * Called frequently so the entity can update its state every
	 * tick as required. For example, zombies and skeletons use this
	 * to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!this.worldObj.isRemote && this.canPickUpLoot() && !this.dead
				&& this.worldObj.getGameRules().getBoolean("mobGriefing")) {
			List<EntityItem> lst = this.worldObj.getEntitiesWithinAABB(EntityItem.class,
					this.getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D));
			for (int i = 0, l = lst.size(); i < l; ++i) {
				EntityItem entityitem = lst.get(i);
				if (!entityitem.isDead && entityitem.getEntityItem() != null && !entityitem.cannotPickup()) {
					this.updateEquipmentIfNeeded(entityitem);
				}
			}
		}
	}

	/**+
	 * Tests if this entity should pickup a weapon or an armor.
	 * Entity drops current weapon or armor if the new one is
	 * better.
	 */
	protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
		ItemStack itemstack = itemEntity.getEntityItem();
		int i = getArmorPosition(itemstack);
		if (i > -1) {
			boolean flag = true;
			ItemStack itemstack1 = this.getEquipmentInSlot(i);
			if (itemstack1 != null) {
				if (i == 0) {
					if (itemstack.getItem() instanceof ItemSword && !(itemstack1.getItem() instanceof ItemSword)) {
						flag = true;
					} else if (itemstack.getItem() instanceof ItemSword && itemstack1.getItem() instanceof ItemSword) {
						ItemSword itemsword = (ItemSword) itemstack.getItem();
						ItemSword itemsword1 = (ItemSword) itemstack1.getItem();
						if (itemsword.getDamageVsEntity() != itemsword1.getDamageVsEntity()) {
							flag = itemsword.getDamageVsEntity() > itemsword1.getDamageVsEntity();
						} else {
							flag = itemstack.getMetadata() > itemstack1.getMetadata()
									|| itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
						}
					} else if (itemstack.getItem() instanceof ItemBow && itemstack1.getItem() instanceof ItemBow) {
						flag = itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
					} else {
						flag = false;
					}
				} else if (itemstack.getItem() instanceof ItemArmor && !(itemstack1.getItem() instanceof ItemArmor)) {
					flag = true;
				} else if (itemstack.getItem() instanceof ItemArmor && itemstack1.getItem() instanceof ItemArmor) {
					ItemArmor itemarmor = (ItemArmor) itemstack.getItem();
					ItemArmor itemarmor1 = (ItemArmor) itemstack1.getItem();
					if (itemarmor.damageReduceAmount != itemarmor1.damageReduceAmount) {
						flag = itemarmor.damageReduceAmount > itemarmor1.damageReduceAmount;
					} else {
						flag = itemstack.getMetadata() > itemstack1.getMetadata()
								|| itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
					}
				} else {
					flag = false;
				}
			}

			if (flag && this.func_175448_a(itemstack)) {
				if (itemstack1 != null && this.rand.nextFloat() - 0.1F < this.equipmentDropChances[i]) {
					this.entityDropItem(itemstack1, 0.0F);
				}

				if (itemstack.getItem() == Items.diamond && itemEntity.getThrower() != null) {
					EntityPlayer entityplayer = this.worldObj.getPlayerEntityByName(itemEntity.getThrower());
					if (entityplayer != null) {
						entityplayer.triggerAchievement(AchievementList.diamondsToYou);
					}
				}

				this.setCurrentItemOrArmor(i, itemstack);
				this.equipmentDropChances[i] = 2.0F;
				this.persistenceRequired = true;
				this.onItemPickup(itemEntity, 1);
				itemEntity.setDead();
			}
		}

	}

	protected boolean func_175448_a(ItemStack stack) {
		return true;
	}

	/**+
	 * Determines if an entity can be despawned, used on idle far
	 * away entities
	 */
	protected boolean canDespawn() {
		return true;
	}

	/**+
	 * Makes the entity despawn if requirements are reached
	 */
	protected void despawnEntity() {
		if (this.persistenceRequired) {
			this.entityAge = 0;
		} else {
			EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
			if (entityplayer != null) {
				double d0 = entityplayer.posX - this.posX;
				double d1 = entityplayer.posY - this.posY;
				double d2 = entityplayer.posZ - this.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				if (this.canDespawn() && d3 > 16384.0D) {
					this.setDead();
				}

				if (this.entityAge > 600 && this.rand.nextInt(800) == 0 && d3 > 1024.0D && this.canDespawn()) {
					this.setDead();
				} else if (d3 < 1024.0D) {
					this.entityAge = 0;
				}
			}

		}
	}

	protected final void updateEntityActionState() {
		++this.entityAge;
		this.despawnEntity();
		this.senses.clearSensingCache();
		this.targetTasks.onUpdateTasks();
		this.tasks.onUpdateTasks();
		this.navigator.onUpdateNavigation();
		this.updateAITasks();
		this.moveHelper.onUpdateMoveHelper();
		this.lookHelper.onUpdateLook();
		this.jumpHelper.doJump();
	}

	protected void updateAITasks() {
	}

	/**+
	 * The speed it takes to move the entityliving's rotationPitch
	 * through the faceEntity method. This is only currently use in
	 * wolves.
	 */
	public int getVerticalFaceSpeed() {
		return 40;
	}

	/**+
	 * Changes pitch and yaw so that the entity calling the function
	 * is facing the entity provided as an argument.
	 */
	public void faceEntity(Entity entityIn, float parFloat1, float parFloat2) {
		double d0 = entityIn.posX - this.posX;
		double d2 = entityIn.posZ - this.posZ;
		double d1;
		if (entityIn instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) entityIn;
			d1 = entitylivingbase.posY + (double) entitylivingbase.getEyeHeight()
					- (this.posY + (double) this.getEyeHeight());
		} else {
			d1 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D
					- (this.posY + (double) this.getEyeHeight());
		}

		double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		float f = (float) (MathHelper.func_181159_b(d2, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
		float f1 = (float) (-(MathHelper.func_181159_b(d1, d3) * 180.0D / 3.1415927410125732D));
		this.rotationPitch = this.updateRotation(this.rotationPitch, f1, parFloat2);
		this.rotationYaw = this.updateRotation(this.rotationYaw, f, parFloat1);
	}

	/**+
	 * Arguments: current rotation, intended rotation, max
	 * increment.
	 */
	private float updateRotation(float parFloat1, float parFloat2, float parFloat3) {
		float f = MathHelper.wrapAngleTo180_float(parFloat2 - parFloat1);
		if (f > parFloat3) {
			f = parFloat3;
		}

		if (f < -parFloat3) {
			f = -parFloat3;
		}

		return parFloat1 + f;
	}

	/**+
	 * Checks if the entity's current position is a valid location
	 * to spawn this entity.
	 */
	public boolean getCanSpawnHere() {
		return true;
	}

	/**+
	 * Checks that the entity is not colliding with any blocks /
	 * liquids
	 */
	public boolean isNotColliding() {
		return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this)
				&& this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty()
				&& !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
	}

	/**+
	 * Returns render size modifier
	 */
	public float getRenderSizeModifier() {
		return 1.0F;
	}

	/**+
	 * Will return how many at most can spawn in a chunk at once.
	 */
	public int getMaxSpawnedInChunk() {
		return 4;
	}

	/**+
	 * The maximum height from where the entity is alowed to jump
	 * (used in pathfinder)
	 */
	public int getMaxFallHeight() {
		if (this.getAttackTarget() == null) {
			return 3;
		} else {
			int i = (int) (this.getHealth() - this.getMaxHealth() * 0.33F);
			i = i - (3 - this.worldObj.getDifficulty().getDifficultyId()) * 4;
			if (i < 0) {
				i = 0;
			}

			return i + 3;
		}
	}

	/**+
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	public ItemStack getHeldItem() {
		return this.equipment[0];
	}

	/**+
	 * 0: Tool in Hand; 1-4: Armor
	 */
	public ItemStack getEquipmentInSlot(int i) {
		return this.equipment[i];
	}

	public ItemStack getCurrentArmor(int i) {
		return this.equipment[i + 1];
	}

	/**+
	 * Sets the held item, or an armor slot. Slot 0 is held item.
	 * Slot 1-4 is armor. Params: Item, slot
	 */
	public void setCurrentItemOrArmor(int i, ItemStack itemstack) {
		this.equipment[i] = itemstack;
	}

	/**+
	 * returns the inventory of this entity (only used in
	 * EntityPlayerMP it seems)
	 */
	public ItemStack[] getInventory() {
		return this.equipment;
	}

	/**+
	 * Drop the equipment for this entity.
	 */
	protected void dropEquipment(boolean flag, int i) {
		for (int j = 0; j < this.getInventory().length; ++j) {
			ItemStack itemstack = this.getEquipmentInSlot(j);
			boolean flag1 = this.equipmentDropChances[j] > 1.0F;
			if (itemstack != null && (flag || flag1)
					&& this.rand.nextFloat() - (float) i * 0.01F < this.equipmentDropChances[j]) {
				if (!flag1 && itemstack.isItemStackDamageable()) {
					int k = Math.max(itemstack.getMaxDamage() - 25, 1);
					int l = itemstack.getMaxDamage() - this.rand.nextInt(this.rand.nextInt(k) + 1);
					if (l > k) {
						l = k;
					}

					if (l < 1) {
						l = 1;
					}

					itemstack.setItemDamage(l);
				}

				this.entityDropItem(itemstack, 0.0F);
			}
		}

	}

	/**+
	 * Gives armor or weapon for entity based on given
	 * DifficultyInstance
	 */
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		if (this.rand.nextFloat() < 0.15F * difficulty.getClampedAdditionalDifficulty()) {
			int i = this.rand.nextInt(2);
			float f = this.worldObj.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.25F;
			if (this.rand.nextFloat() < 0.095F) {
				++i;
			}

			if (this.rand.nextFloat() < 0.095F) {
				++i;
			}

			if (this.rand.nextFloat() < 0.095F) {
				++i;
			}

			for (int j = 3; j >= 0; --j) {
				ItemStack itemstack = this.getCurrentArmor(j);
				if (j < 3 && this.rand.nextFloat() < f) {
					break;
				}

				if (itemstack == null) {
					Item item = getArmorItemForSlot(j + 1, i);
					if (item != null) {
						this.setCurrentItemOrArmor(j + 1, new ItemStack(item));
					}
				}
			}
		}

	}

	public static int getArmorPosition(ItemStack stack) {
		if (stack.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && stack.getItem() != Items.skull) {
			if (stack.getItem() instanceof ItemArmor) {
				switch (((ItemArmor) stack.getItem()).armorType) {
				case 0:
					return 4;
				case 1:
					return 3;
				case 2:
					return 2;
				case 3:
					return 1;
				}
			}

			return 0;
		} else {
			return 4;
		}
	}

	/**+
	 * Gets the vanilla armor Item that can go in the slot specified
	 * for the given tier.
	 */
	public static Item getArmorItemForSlot(int armorSlot, int itemTier) {
		switch (armorSlot) {
		case 4:
			if (itemTier == 0) {
				return Items.leather_helmet;
			} else if (itemTier == 1) {
				return Items.golden_helmet;
			} else if (itemTier == 2) {
				return Items.chainmail_helmet;
			} else if (itemTier == 3) {
				return Items.iron_helmet;
			} else if (itemTier == 4) {
				return Items.diamond_helmet;
			}
		case 3:
			if (itemTier == 0) {
				return Items.leather_chestplate;
			} else if (itemTier == 1) {
				return Items.golden_chestplate;
			} else if (itemTier == 2) {
				return Items.chainmail_chestplate;
			} else if (itemTier == 3) {
				return Items.iron_chestplate;
			} else if (itemTier == 4) {
				return Items.diamond_chestplate;
			}
		case 2:
			if (itemTier == 0) {
				return Items.leather_leggings;
			} else if (itemTier == 1) {
				return Items.golden_leggings;
			} else if (itemTier == 2) {
				return Items.chainmail_leggings;
			} else if (itemTier == 3) {
				return Items.iron_leggings;
			} else if (itemTier == 4) {
				return Items.diamond_leggings;
			}
		case 1:
			if (itemTier == 0) {
				return Items.leather_boots;
			} else if (itemTier == 1) {
				return Items.golden_boots;
			} else if (itemTier == 2) {
				return Items.chainmail_boots;
			} else if (itemTier == 3) {
				return Items.iron_boots;
			} else if (itemTier == 4) {
				return Items.diamond_boots;
			}
		default:
			return null;
		}
	}

	/**+
	 * Enchants Entity's current equipments based on given
	 * DifficultyInstance
	 */
	protected void setEnchantmentBasedOnDifficulty(DifficultyInstance difficulty) {
		float f = difficulty.getClampedAdditionalDifficulty();
		if (this.getHeldItem() != null && this.rand.nextFloat() < 0.25F * f) {
			EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItem(),
					(int) (5.0F + f * (float) this.rand.nextInt(18)));
		}

		for (int i = 0; i < 4; ++i) {
			ItemStack itemstack = this.getCurrentArmor(i);
			if (itemstack != null && this.rand.nextFloat() < 0.5F * f) {
				EnchantmentHelper.addRandomEnchantment(this.rand, itemstack,
						(int) (5.0F + f * (float) this.rand.nextInt(18)));
			}
		}

	}

	/**+
	 * Called only once on an entity when first time spawned, via
	 * egg, mob spawner, natural spawning etc, but not called when
	 * entity is reloaded from nbt. Mainly used for initializing
	 * attributes and inventory
	 */
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.getEntityAttribute(SharedMonsterAttributes.followRange)
				.applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
		return livingdata;
	}

	/**+
	 * returns true if all the conditions for steering the entity
	 * are met. For pigs, this is true if it is being ridden by a
	 * player and the player is holding a carrot-on-a-stick
	 */
	public boolean canBeSteered() {
		return false;
	}

	/**+
	 * Enable the Entity persistence
	 */
	public void enablePersistence() {
		this.persistenceRequired = true;
	}

	public void setEquipmentDropChance(int slotIn, float chance) {
		this.equipmentDropChances[slotIn] = chance;
	}

	public boolean canPickUpLoot() {
		return this.canPickUpLoot;
	}

	public void setCanPickUpLoot(boolean canPickup) {
		this.canPickUpLoot = canPickup;
	}

	public boolean isNoDespawnRequired() {
		return this.persistenceRequired;
	}

	/**+
	 * First layer of player interaction
	 */
	public final boolean interactFirst(EntityPlayer entityplayer) {
		if (this.getLeashed() && this.getLeashedToEntity() == entityplayer) {
			this.clearLeashed(true, !entityplayer.capabilities.isCreativeMode);
			return true;
		} else {
			ItemStack itemstack = entityplayer.inventory.getCurrentItem();
			if (itemstack != null && itemstack.getItem() == Items.lead && this.allowLeashing()) {
				if (!(this instanceof EntityTameable) || !((EntityTameable) this).isTamed()) {
					this.setLeashedToEntity(entityplayer, true);
					--itemstack.stackSize;
					return true;
				}

				if (((EntityTameable) this).isOwner(entityplayer)) {
					this.setLeashedToEntity(entityplayer, true);
					--itemstack.stackSize;
					return true;
				}
			}

			if (this.interact(entityplayer)) {
				return true;
			} else {
				return super.interactFirst(entityplayer);
			}
		}
	}

	/**+
	 * Called when a player interacts with a mob. e.g. gets milk
	 * from a cow, gets into the saddle on a pig.
	 */
	protected boolean interact(EntityPlayer var1) {
		return false;
	}

	/**+
	 * Applies logic related to leashes, for example dragging the
	 * entity or breaking the leash.
	 */
	protected void updateLeashedState() {
		if (this.leashNBTTag != null) {
			this.recreateLeash();
		}

		if (this.isLeashed) {
			if (!this.isEntityAlive()) {
				this.clearLeashed(true, true);
			}

			if (this.leashedToEntity == null || this.leashedToEntity.isDead) {
				this.clearLeashed(true, true);
			}
		}
	}

	/**+
	 * Removes the leash from this entity
	 */
	public void clearLeashed(boolean sendPacket, boolean dropLead) {
		if (this.isLeashed) {
			this.isLeashed = false;
			this.leashedToEntity = null;
			if (!this.worldObj.isRemote && dropLead) {
				this.dropItem(Items.lead, 1);
			}

			if (!this.worldObj.isRemote && sendPacket && this.worldObj instanceof WorldServer) {
				((WorldServer) this.worldObj).getEntityTracker().sendToAllTrackingEntity(this,
						new S1BPacketEntityAttach(1, this, (Entity) null));
			}
		}

	}

	public boolean allowLeashing() {
		return !this.getLeashed() && !(this instanceof IMob);
	}

	public boolean getLeashed() {
		return this.isLeashed;
	}

	public Entity getLeashedToEntity() {
		return this.leashedToEntity;
	}

	/**+
	 * Sets the entity to be leashed to.
	 */
	public void setLeashedToEntity(Entity entityIn, boolean sendAttachNotification) {
		this.isLeashed = true;
		this.leashedToEntity = entityIn;
		if (!this.worldObj.isRemote && sendAttachNotification && this.worldObj instanceof WorldServer) {
			((WorldServer) this.worldObj).getEntityTracker().sendToAllTrackingEntity(this,
					new S1BPacketEntityAttach(1, this, this.leashedToEntity));
		}

	}

	private void recreateLeash() {
		if (this.isLeashed && this.leashNBTTag != null) {
			if (this.leashNBTTag.hasKey("UUIDMost", 4) && this.leashNBTTag.hasKey("UUIDLeast", 4)) {
				EaglercraftUUID uuid = new EaglercraftUUID(this.leashNBTTag.getLong("UUIDMost"),
						this.leashNBTTag.getLong("UUIDLeast"));

				List<EntityLivingBase> entities = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
						this.getEntityBoundingBox().expand(10.0D, 10.0D, 10.0D));
				for (int i = 0, l = entities.size(); i < l; ++i) {
					EntityLivingBase entitylivingbase = entities.get(i);
					if (entitylivingbase.getUniqueID().equals(uuid)) {
						this.leashedToEntity = entitylivingbase;
						break;
					}
				}
			} else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99)
					&& this.leashNBTTag.hasKey("Z", 99)) {
				BlockPos blockpos = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"),
						this.leashNBTTag.getInteger("Z"));
				EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(this.worldObj, blockpos);
				if (entityleashknot == null) {
					entityleashknot = EntityLeashKnot.createKnot(this.worldObj, blockpos);
				}

				this.leashedToEntity = entityleashknot;
			} else {
				this.clearLeashed(false, true);
			}
		}

		this.leashNBTTag = null;
	}

	public boolean replaceItemInInventory(int i, ItemStack itemstack) {
		int j;
		if (i == 99) {
			j = 0;
		} else {
			j = i - 100 + 1;
			if (j < 0 || j >= this.equipment.length) {
				return false;
			}
		}

		if (itemstack != null && getArmorPosition(itemstack) != j
				&& (j != 4 || !(itemstack.getItem() instanceof ItemBlock))) {
			return false;
		} else {
			this.setCurrentItemOrArmor(j, itemstack);
			return true;
		}
	}

	/**+
	 * Returns whether the entity is in a server world
	 */
	public boolean isServerWorld() {
		return super.isServerWorld() && !this.isAIDisabled();
	}

	/**+
	 * Set whether this Entity's AI is disabled
	 */
	public void setNoAI(boolean disable) {
		this.dataWatcher.updateObject(15, Byte.valueOf((byte) (disable ? 1 : 0)));
	}

	/**+
	 * Get whether this Entity's AI is disabled
	 */
	public boolean isAIDisabled() {
		return this.dataWatcher.getWatchableObjectByte(15) != 0;
	}

	public static enum SpawnPlacementType {
		ON_GROUND, IN_AIR, IN_WATER;
	}
}