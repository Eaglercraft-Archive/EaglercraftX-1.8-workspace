package net.minecraft.entity.item;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
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
public class EntityItem extends Entity {
	private static final Logger logger = LogManager.getLogger();
	private int age;
	private int delayBeforeCanPickup;
	private int health;
	private String thrower;
	private String owner;
	public float hoverStart;

	public EntityItem(World worldIn, double x, double y, double z) {
		super(worldIn);
		this.health = 5;
		this.hoverStart = (float) (Math.random() * 3.141592653589793D * 2.0D);
		this.setSize(0.25F, 0.25F);
		this.setPosition(x, y, z);
		this.rotationYaw = (float) (Math.random() * 360.0D);
		this.motionX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
		this.motionY = 0.20000000298023224D;
		this.motionZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
	}

	public EntityItem(World worldIn, double x, double y, double z, ItemStack stack) {
		this(worldIn, x, y, z);
		this.setEntityItemStack(stack);
	}

	/**+
	 * returns if this entity triggers Block.onEntityWalking on the
	 * blocks they walk on. used for spiders and wolves to prevent
	 * them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	public EntityItem(World worldIn) {
		super(worldIn);
		this.health = 5;
		this.hoverStart = (float) (Math.random() * 3.141592653589793D * 2.0D);
		this.setSize(0.25F, 0.25F);
		this.setEntityItemStack(new ItemStack(Blocks.air, 0));
	}

	protected void entityInit() {
		this.getDataWatcher().addObjectByDataType(10, 5);
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		if (this.getEntityItem() == null) {
			this.setDead();
		} else {
			super.onUpdate();
			if (this.delayBeforeCanPickup > 0 && this.delayBeforeCanPickup != 32767) {
				--this.delayBeforeCanPickup;
			}

			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			this.motionY -= 0.03999999910593033D;
			this.noClip = this.pushOutOfBlocks(this.posX,
					(this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			boolean flag = (int) this.prevPosX != (int) this.posX || (int) this.prevPosY != (int) this.posY
					|| (int) this.prevPosZ != (int) this.posZ;
			if (flag || this.ticksExisted % 25 == 0) {
				if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava) {
					this.motionY = 0.20000000298023224D;
					this.motionX = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
					this.motionZ = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
					this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
				}

				if (!this.worldObj.isRemote) {
					this.searchForOtherItemsNearby();
				}
			}

			float f = 0.98F;
			if (this.onGround) {
				f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX),
						MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1,
						MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98F;
			}

			this.motionX *= (double) f;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= (double) f;
			if (this.onGround) {
				this.motionY *= -0.5D;
			}

			if (this.age != -32768) {
				++this.age;
			}

			this.handleWaterMovement();
			if (!this.worldObj.isRemote && this.age >= 6000) {
				this.setDead();
			}

		}
	}

	/**+
	 * Looks for other itemstacks nearby and tries to stack them
	 * together
	 */
	private void searchForOtherItemsNearby() {
		List<EntityItem> lst = this.worldObj.getEntitiesWithinAABB(EntityItem.class,
				this.getEntityBoundingBox().expand(0.5D, 0.0D, 0.5D));
		for (int i = 0, l = lst.size(); i < l; ++i) {
			this.combineItems(lst.get(i));
		}

	}

	/**+
	 * Tries to merge this item with the item passed as the
	 * parameter. Returns true if successful. Either this item or
	 * the other item will be removed from the world.
	 */
	private boolean combineItems(EntityItem other) {
		if (other == this) {
			return false;
		} else if (other.isEntityAlive() && this.isEntityAlive()) {
			ItemStack itemstack = this.getEntityItem();
			ItemStack itemstack1 = other.getEntityItem();
			if (this.delayBeforeCanPickup != 32767 && other.delayBeforeCanPickup != 32767) {
				if (this.age != -32768 && other.age != -32768) {
					if (itemstack1.getItem() != itemstack.getItem()) {
						return false;
					} else if (itemstack1.hasTagCompound() ^ itemstack.hasTagCompound()) {
						return false;
					} else if (itemstack1.hasTagCompound()
							&& !itemstack1.getTagCompound().equals(itemstack.getTagCompound())) {
						return false;
					} else if (itemstack1.getItem() == null) {
						return false;
					} else if (itemstack1.getItem().getHasSubtypes()
							&& itemstack1.getMetadata() != itemstack.getMetadata()) {
						return false;
					} else if (itemstack1.stackSize < itemstack.stackSize) {
						return other.combineItems(this);
					} else if (itemstack1.stackSize + itemstack.stackSize > itemstack1.getMaxStackSize()) {
						return false;
					} else {
						itemstack1.stackSize += itemstack.stackSize;
						other.delayBeforeCanPickup = Math.max(other.delayBeforeCanPickup, this.delayBeforeCanPickup);
						other.age = Math.min(other.age, this.age);
						other.setEntityItemStack(itemstack1);
						this.setDead();
						return true;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**+
	 * sets the age of the item so that it'll despawn one minute
	 * after it has been dropped (instead of five). Used when items
	 * are dropped from players in creative mode
	 */
	public void setAgeToCreativeDespawnTime() {
		this.age = 4800;
	}

	/**+
	 * Returns if this entity is in water and will end up adding the
	 * waters velocity to the entity
	 */
	public boolean handleWaterMovement() {
		if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.water, this)) {
			if (!this.inWater && !this.firstUpdate) {
				this.resetHeight();
			}

			this.inWater = true;
		} else {
			this.inWater = false;
		}

		return this.inWater;
	}

	/**+
	 * Will deal the specified amount of damage to the entity if the
	 * entity isn't immune to fire damage. Args: amountDamage
	 */
	protected void dealFireDamage(int i) {
		this.attackEntityFrom(DamageSource.inFire, (float) i);
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		if (this.isEntityInvulnerable(damagesource)) {
			return false;
		} else if (this.getEntityItem() != null && this.getEntityItem().getItem() == Items.nether_star
				&& damagesource.isExplosion()) {
			return false;
		} else {
			this.setBeenAttacked();
			this.health = (int) ((float) this.health - f);
			if (this.health <= 0) {
				this.setDead();
			}

			return false;
		}
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("Health", (short) ((byte) this.health));
		nbttagcompound.setShort("Age", (short) this.age);
		nbttagcompound.setShort("PickupDelay", (short) this.delayBeforeCanPickup);
		if (this.getThrower() != null) {
			nbttagcompound.setString("Thrower", this.thrower);
		}

		if (this.getOwner() != null) {
			nbttagcompound.setString("Owner", this.owner);
		}

		if (this.getEntityItem() != null) {
			nbttagcompound.setTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
		}

	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.health = nbttagcompound.getShort("Health") & 255;
		this.age = nbttagcompound.getShort("Age");
		if (nbttagcompound.hasKey("PickupDelay")) {
			this.delayBeforeCanPickup = nbttagcompound.getShort("PickupDelay");
		}

		if (nbttagcompound.hasKey("Owner")) {
			this.owner = nbttagcompound.getString("Owner");
		}

		if (nbttagcompound.hasKey("Thrower")) {
			this.thrower = nbttagcompound.getString("Thrower");
		}

		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Item");
		this.setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound1));
		if (this.getEntityItem() == null) {
			this.setDead();
		}

	}

	/**+
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer entityplayer) {
		if (!this.worldObj.isRemote) {
			ItemStack itemstack = this.getEntityItem();
			int i = itemstack.stackSize;
			if (this.delayBeforeCanPickup == 0
					&& (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(entityplayer.getName()))
					&& entityplayer.inventory.addItemStackToInventory(itemstack)) {
				if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log)) {
					entityplayer.triggerAchievement(AchievementList.mineWood);
				}

				if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log2)) {
					entityplayer.triggerAchievement(AchievementList.mineWood);
				}

				if (itemstack.getItem() == Items.leather) {
					entityplayer.triggerAchievement(AchievementList.killCow);
				}

				if (itemstack.getItem() == Items.diamond) {
					entityplayer.triggerAchievement(AchievementList.diamonds);
				}

				if (itemstack.getItem() == Items.blaze_rod) {
					entityplayer.triggerAchievement(AchievementList.blazeRod);
				}

				if (itemstack.getItem() == Items.diamond && this.getThrower() != null) {
					EntityPlayer entityplayer1 = this.worldObj.getPlayerEntityByName(this.getThrower());
					if (entityplayer1 != null && entityplayer1 != entityplayer) {
						entityplayer1.triggerAchievement(AchievementList.diamondsToYou);
					}
				}

				if (!this.isSilent()) {
					this.worldObj.playSoundAtEntity(entityplayer, "random.pop", 0.2F,
							((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				}

				entityplayer.onItemPickup(this, i);
				if (itemstack.stackSize <= 0) {
					this.setDead();
				}
			}

		}
	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return this.hasCustomName() ? this.getCustomNameTag()
				: StatCollector.translateToLocal("item." + this.getEntityItem().getUnlocalizedName());
	}

	/**+
	 * If returns false, the item will not inflict any damage
	 * against entities.
	 */
	public boolean canAttackWithItem() {
		return false;
	}

	/**+
	 * Teleports the entity to another dimension. Params: Dimension
	 * number to teleport to
	 */
	public void travelToDimension(int i) {
		super.travelToDimension(i);
		if (!this.worldObj.isRemote) {
			this.searchForOtherItemsNearby();
		}

	}

	/**+
	 * Returns the ItemStack corresponding to the Entity (Note: if
	 * no item exists, will log an error but still return an
	 * ItemStack containing Block.stone)
	 */
	public ItemStack getEntityItem() {
		ItemStack itemstack = this.getDataWatcher().getWatchableObjectItemStack(10);
		if (itemstack == null) {
			if (this.worldObj != null) {
				logger.error("Item entity " + this.getEntityId() + " has no item?!");
			}

			return new ItemStack(Blocks.stone);
		} else {
			return itemstack;
		}
	}

	/**+
	 * Sets the ItemStack for this entity
	 */
	public void setEntityItemStack(ItemStack stack) {
		this.getDataWatcher().updateObject(10, stack);
		this.getDataWatcher().setObjectWatched(10);
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getThrower() {
		return this.thrower;
	}

	public void setThrower(String thrower) {
		this.thrower = thrower;
	}

	public int getAge() {
		return this.age;
	}

	public void setDefaultPickupDelay() {
		this.delayBeforeCanPickup = 10;
	}

	public void setNoPickupDelay() {
		this.delayBeforeCanPickup = 0;
	}

	public void setInfinitePickupDelay() {
		this.delayBeforeCanPickup = 32767;
	}

	public void setPickupDelay(int ticks) {
		this.delayBeforeCanPickup = ticks;
	}

	public boolean cannotPickup() {
		return this.delayBeforeCanPickup > 0;
	}

	public void setNoDespawn() {
		this.age = -6000;
	}

	public void func_174870_v() {
		this.setInfinitePickupDelay();
		this.age = 5999;
	}

	public boolean eaglerEmissiveFlag = false;

	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
		super.renderDynamicLightsEaglerAt(entityX, entityY, entityZ, renderX, renderY, renderZ, partialTicks,
				isInFrustum);
		eaglerEmissiveFlag = Minecraft.getMinecraft().entityRenderer.renderItemEntityLight(this, 0.1f);
	}

	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
		float f = super.getEaglerDynamicLightsValueSimple(partialTicks);
		ItemStack itm = this.getEntityItem();
		if (itm != null && itm.stackSize > 0) {
			Item item = itm.getItem();
			if (item != null) {
				float f2 = item.getHeldItemBrightnessEagler(itm) * 0.75f;
				f = Math.min(f + f2 * 0.5f, 1.0f) + f2 * 0.5f;
			}
		}
		return f;
	}
}