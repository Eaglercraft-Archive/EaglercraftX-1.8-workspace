package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotations;
import net.minecraft.util.Vec3;
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
public class EntityArmorStand extends EntityLivingBase {
	private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
	private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
	private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
	private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
	private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
	private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
	private final ItemStack[] contents;
	private boolean canInteract;
	private long punchCooldown;
	private int disabledSlots;
	private boolean field_181028_bj;
	private Rotations headRotation;
	private Rotations bodyRotation;
	private Rotations leftArmRotation;
	private Rotations rightArmRotation;
	private Rotations leftLegRotation;
	private Rotations rightLegRotation;

	public EntityArmorStand(World worldIn) {
		super(worldIn);
		this.contents = new ItemStack[5];
		this.headRotation = DEFAULT_HEAD_ROTATION;
		this.bodyRotation = DEFAULT_BODY_ROTATION;
		this.leftArmRotation = DEFAULT_LEFTARM_ROTATION;
		this.rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
		this.leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
		this.rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
		this.setSilent(true);
		this.noClip = this.hasNoGravity();
		this.setSize(0.5F, 1.975F);
	}

	public EntityArmorStand(World worldIn, double posX, double posY, double posZ) {
		this(worldIn);
		this.setPosition(posX, posY, posZ);
	}

	/**+
	 * Returns whether the entity is in a server world
	 */
	public boolean isServerWorld() {
		return super.isServerWorld() && !this.hasNoGravity();
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(10, Byte.valueOf((byte) 0));
		this.dataWatcher.addObject(11, DEFAULT_HEAD_ROTATION);
		this.dataWatcher.addObject(12, DEFAULT_BODY_ROTATION);
		this.dataWatcher.addObject(13, DEFAULT_LEFTARM_ROTATION);
		this.dataWatcher.addObject(14, DEFAULT_RIGHTARM_ROTATION);
		this.dataWatcher.addObject(15, DEFAULT_LEFTLEG_ROTATION);
		this.dataWatcher.addObject(16, DEFAULT_RIGHTLEG_ROTATION);
	}

	/**+
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	public ItemStack getHeldItem() {
		return this.contents[0];
	}

	/**+
	 * 0: Tool in Hand; 1-4: Armor
	 */
	public ItemStack getEquipmentInSlot(int i) {
		return this.contents[i];
	}

	public ItemStack getCurrentArmor(int i) {
		return this.contents[i + 1];
	}

	/**+
	 * Sets the held item, or an armor slot. Slot 0 is held item.
	 * Slot 1-4 is armor. Params: Item, slot
	 */
	public void setCurrentItemOrArmor(int i, ItemStack itemstack) {
		this.contents[i] = itemstack;
	}

	/**+
	 * returns the inventory of this entity (only used in
	 * EntityPlayerMP it seems)
	 */
	public ItemStack[] getInventory() {
		return this.contents;
	}

	public boolean replaceItemInInventory(int i, ItemStack itemstack) {
		int j;
		if (i == 99) {
			j = 0;
		} else {
			j = i - 100 + 1;
			if (j < 0 || j >= this.contents.length) {
				return false;
			}
		}

		if (itemstack != null && EntityLiving.getArmorPosition(itemstack) != j
				&& (j != 4 || !(itemstack.getItem() instanceof ItemBlock))) {
			return false;
		} else {
			this.setCurrentItemOrArmor(j, itemstack);
			return true;
		}
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.contents.length; ++i) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			if (this.contents[i] != null) {
				this.contents[i].writeToNBT(nbttagcompound1);
			}

			nbttaglist.appendTag(nbttagcompound1);
		}

		nbttagcompound.setTag("Equipment", nbttaglist);
		if (this.getAlwaysRenderNameTag()
				&& (this.getCustomNameTag() == null || this.getCustomNameTag().length() == 0)) {
			nbttagcompound.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
		}

		nbttagcompound.setBoolean("Invisible", this.isInvisible());
		nbttagcompound.setBoolean("Small", this.isSmall());
		nbttagcompound.setBoolean("ShowArms", this.getShowArms());
		nbttagcompound.setInteger("DisabledSlots", this.disabledSlots);
		nbttagcompound.setBoolean("NoGravity", this.hasNoGravity());
		nbttagcompound.setBoolean("NoBasePlate", this.hasNoBasePlate());
		if (this.func_181026_s()) {
			nbttagcompound.setBoolean("Marker", this.func_181026_s());
		}

		nbttagcompound.setTag("Pose", this.readPoseFromNBT());
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("Equipment", 9)) {
			NBTTagList nbttaglist = nbttagcompound.getTagList("Equipment", 10);

			for (int i = 0; i < this.contents.length; ++i) {
				this.contents[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
			}
		}

		this.setInvisible(nbttagcompound.getBoolean("Invisible"));
		this.setSmall(nbttagcompound.getBoolean("Small"));
		this.setShowArms(nbttagcompound.getBoolean("ShowArms"));
		this.disabledSlots = nbttagcompound.getInteger("DisabledSlots");
		this.setNoGravity(nbttagcompound.getBoolean("NoGravity"));
		this.setNoBasePlate(nbttagcompound.getBoolean("NoBasePlate"));
		this.func_181027_m(nbttagcompound.getBoolean("Marker"));
		this.field_181028_bj = !this.func_181026_s();
		this.noClip = this.hasNoGravity();
		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Pose");
		this.writePoseToNBT(nbttagcompound1);
	}

	/**+
	 * Saves the pose to an NBTTagCompound.
	 */
	private void writePoseToNBT(NBTTagCompound tagCompound) {
		NBTTagList nbttaglist = tagCompound.getTagList("Head", 5);
		if (nbttaglist.tagCount() > 0) {
			this.setHeadRotation(new Rotations(nbttaglist));
		} else {
			this.setHeadRotation(DEFAULT_HEAD_ROTATION);
		}

		NBTTagList nbttaglist1 = tagCompound.getTagList("Body", 5);
		if (nbttaglist1.tagCount() > 0) {
			this.setBodyRotation(new Rotations(nbttaglist1));
		} else {
			this.setBodyRotation(DEFAULT_BODY_ROTATION);
		}

		NBTTagList nbttaglist2 = tagCompound.getTagList("LeftArm", 5);
		if (nbttaglist2.tagCount() > 0) {
			this.setLeftArmRotation(new Rotations(nbttaglist2));
		} else {
			this.setLeftArmRotation(DEFAULT_LEFTARM_ROTATION);
		}

		NBTTagList nbttaglist3 = tagCompound.getTagList("RightArm", 5);
		if (nbttaglist3.tagCount() > 0) {
			this.setRightArmRotation(new Rotations(nbttaglist3));
		} else {
			this.setRightArmRotation(DEFAULT_RIGHTARM_ROTATION);
		}

		NBTTagList nbttaglist4 = tagCompound.getTagList("LeftLeg", 5);
		if (nbttaglist4.tagCount() > 0) {
			this.setLeftLegRotation(new Rotations(nbttaglist4));
		} else {
			this.setLeftLegRotation(DEFAULT_LEFTLEG_ROTATION);
		}

		NBTTagList nbttaglist5 = tagCompound.getTagList("RightLeg", 5);
		if (nbttaglist5.tagCount() > 0) {
			this.setRightLegRotation(new Rotations(nbttaglist5));
		} else {
			this.setRightLegRotation(DEFAULT_RIGHTLEG_ROTATION);
		}

	}

	private NBTTagCompound readPoseFromNBT() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation)) {
			nbttagcompound.setTag("Head", this.headRotation.writeToNBT());
		}

		if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation)) {
			nbttagcompound.setTag("Body", this.bodyRotation.writeToNBT());
		}

		if (!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation)) {
			nbttagcompound.setTag("LeftArm", this.leftArmRotation.writeToNBT());
		}

		if (!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation)) {
			nbttagcompound.setTag("RightArm", this.rightArmRotation.writeToNBT());
		}

		if (!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation)) {
			nbttagcompound.setTag("LeftLeg", this.leftLegRotation.writeToNBT());
		}

		if (!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation)) {
			nbttagcompound.setTag("RightLeg", this.rightLegRotation.writeToNBT());
		}

		return nbttagcompound;
	}

	/**+
	 * Returns true if this entity should push and be pushed by
	 * other entities when colliding.
	 */
	public boolean canBePushed() {
		return false;
	}

	protected void collideWithEntity(Entity var1) {
	}

	protected void collideWithNearbyEntities() {
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); ++i) {
				Entity entity = (Entity) list.get(i);
				if (entity instanceof EntityMinecart
						&& ((EntityMinecart) entity).getMinecartType() == EntityMinecart.EnumMinecartType.RIDEABLE
						&& this.getDistanceSqToEntity(entity) <= 0.2D) {
					entity.applyEntityCollision(this);
				}
			}
		}

	}

	/**+
	 * New version of interactWith that includes vector information
	 * on where precisely the player targeted.
	 */
	public boolean interactAt(EntityPlayer entityplayer, Vec3 vec3) {
		if (this.func_181026_s()) {
			return false;
		} else if (!this.worldObj.isRemote && !entityplayer.isSpectator()) {
			byte b0 = 0;
			ItemStack itemstack = entityplayer.getCurrentEquippedItem();
			boolean flag = itemstack != null;
			if (flag && itemstack.getItem() instanceof ItemArmor) {
				ItemArmor itemarmor = (ItemArmor) itemstack.getItem();
				if (itemarmor.armorType == 3) {
					b0 = 1;
				} else if (itemarmor.armorType == 2) {
					b0 = 2;
				} else if (itemarmor.armorType == 1) {
					b0 = 3;
				} else if (itemarmor.armorType == 0) {
					b0 = 4;
				}
			}

			if (flag && (itemstack.getItem() == Items.skull
					|| itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin))) {
				b0 = 4;
			}

			double d4 = 0.1D;
			double d0 = 0.9D;
			double d1 = 0.4D;
			double d2 = 1.6D;
			byte b1 = 0;
			boolean flag1 = this.isSmall();
			double d3 = flag1 ? vec3.yCoord * 2.0D : vec3.yCoord;
			if (d3 >= 0.1D && d3 < 0.1D + (flag1 ? 0.8D : 0.45D) && this.contents[1] != null) {
				b1 = 1;
			} else if (d3 >= 0.9D + (flag1 ? 0.3D : 0.0D) && d3 < 0.9D + (flag1 ? 1.0D : 0.7D)
					&& this.contents[3] != null) {
				b1 = 3;
			} else if (d3 >= 0.4D && d3 < 0.4D + (flag1 ? 1.0D : 0.8D) && this.contents[2] != null) {
				b1 = 2;
			} else if (d3 >= 1.6D && this.contents[4] != null) {
				b1 = 4;
			}

			boolean flag2 = this.contents[b1] != null;
			if ((this.disabledSlots & 1 << b1) != 0 || (this.disabledSlots & 1 << b0) != 0) {
				b1 = b0;
				if ((this.disabledSlots & 1 << b0) != 0) {
					if ((this.disabledSlots & 1) != 0) {
						return true;
					}

					b1 = 0;
				}
			}

			if (flag && b0 == 0 && !this.getShowArms()) {
				return true;
			} else {
				if (flag) {
					this.func_175422_a(entityplayer, b0);
				} else if (flag2) {
					this.func_175422_a(entityplayer, b1);
				}

				return true;
			}
		} else {
			return true;
		}
	}

	private void func_175422_a(EntityPlayer parEntityPlayer, int parInt1) {
		ItemStack itemstack = this.contents[parInt1];
		if (itemstack == null || (this.disabledSlots & 1 << parInt1 + 8) == 0) {
			if (itemstack != null || (this.disabledSlots & 1 << parInt1 + 16) == 0) {
				int i = parEntityPlayer.inventory.currentItem;
				ItemStack itemstack1 = parEntityPlayer.inventory.getStackInSlot(i);
				if (parEntityPlayer.capabilities.isCreativeMode
						&& (itemstack == null || itemstack.getItem() == Item.getItemFromBlock(Blocks.air))
						&& itemstack1 != null) {
					ItemStack itemstack3 = itemstack1.copy();
					itemstack3.stackSize = 1;
					this.setCurrentItemOrArmor(parInt1, itemstack3);
				} else if (itemstack1 != null && itemstack1.stackSize > 1) {
					if (itemstack == null) {
						ItemStack itemstack2 = itemstack1.copy();
						itemstack2.stackSize = 1;
						this.setCurrentItemOrArmor(parInt1, itemstack2);
						--itemstack1.stackSize;
					}
				} else {
					this.setCurrentItemOrArmor(parInt1, itemstack1);
					parEntityPlayer.inventory.setInventorySlotContents(i, itemstack);
				}
			}
		}
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float var2) {
		if (this.worldObj.isRemote) {
			return false;
		} else if (DamageSource.outOfWorld.equals(damagesource)) {
			this.setDead();
			return false;
		} else if (!this.isEntityInvulnerable(damagesource) && !this.canInteract && !this.func_181026_s()) {
			if (damagesource.isExplosion()) {
				this.dropContents();
				this.setDead();
				return false;
			} else if (DamageSource.inFire.equals(damagesource)) {
				if (!this.isBurning()) {
					this.setFire(5);
				} else {
					this.damageArmorStand(0.15F);
				}

				return false;
			} else if (DamageSource.onFire.equals(damagesource) && this.getHealth() > 0.5F) {
				this.damageArmorStand(4.0F);
				return false;
			} else {
				boolean flag = "arrow".equals(damagesource.getDamageType());
				boolean flag1 = "player".equals(damagesource.getDamageType());
				if (!flag1 && !flag) {
					return false;
				} else {
					if (damagesource.getSourceOfDamage() instanceof EntityArrow) {
						damagesource.getSourceOfDamage().setDead();
					}

					if (damagesource.getEntity() instanceof EntityPlayer
							&& !((EntityPlayer) damagesource.getEntity()).capabilities.allowEdit) {
						return false;
					} else if (damagesource.isCreativePlayer()) {
						this.playParticles();
						this.setDead();
						return false;
					} else {
						long i = this.worldObj.getTotalWorldTime();
						if (i - this.punchCooldown > 5L && !flag) {
							this.punchCooldown = i;
						} else {
							this.dropBlock();
							this.playParticles();
							this.setDead();
						}

						return false;
					}
				}
			}
		} else {
			return false;
		}
	}

	/**+
	 * Checks if the entity is in range to render by using the past
	 * in distance and comparing it to its average edge length * 64
	 * * renderDistanceWeight Args: distance
	 */
	public boolean isInRangeToRenderDist(double d0) {
		double d1 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
		if (Double.isNaN(d1) || d1 == 0.0D) {
			d1 = 4.0D;
		}

		d1 = d1 * 64.0D;
		return d0 < d1 * d1;
	}

	private void playParticles() {
		if (this.worldObj instanceof WorldServer) {
			((WorldServer) this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX,
					this.posY + (double) this.height / 1.5D, this.posZ, 10, (double) (this.width / 4.0F),
					(double) (this.height / 4.0F), (double) (this.width / 4.0F), 0.05D,
					new int[] { Block.getStateId(Blocks.planks.getDefaultState()) });
		}

	}

	private void damageArmorStand(float parFloat1) {
		float f = this.getHealth();
		f = f - parFloat1;
		if (f <= 0.5F) {
			this.dropContents();
			this.setDead();
		} else {
			this.setHealth(f);
		}

	}

	private void dropBlock() {
		Block.spawnAsEntity(this.worldObj, new BlockPos(this), new ItemStack(Items.armor_stand));
		this.dropContents();
	}

	private void dropContents() {
		for (int i = 0; i < this.contents.length; ++i) {
			if (this.contents[i] != null && this.contents[i].stackSize > 0) {
				if (this.contents[i] != null) {
					Block.spawnAsEntity(this.worldObj, (new BlockPos(this)).up(), this.contents[i]);
				}

				this.contents[i] = null;
			}
		}

	}

	protected float func_110146_f(float var1, float var2) {
		this.prevRenderYawOffset = this.prevRotationYaw;
		this.renderYawOffset = this.rotationYaw;
		return 0.0F;
	}

	public float getEyeHeight() {
		return this.isChild() ? this.height * 0.5F : this.height * 0.9F;
	}

	/**+
	 * Moves the entity based on the specified heading. Args:
	 * strafe, forward
	 */
	public void moveEntityWithHeading(float f, float f1) {
		if (!this.hasNoGravity()) {
			super.moveEntityWithHeading(f, f1);
		}
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		Rotations rotations = this.dataWatcher.getWatchableObjectRotations(11);
		if (!this.headRotation.equals(rotations)) {
			this.setHeadRotation(rotations);
		}

		Rotations rotations1 = this.dataWatcher.getWatchableObjectRotations(12);
		if (!this.bodyRotation.equals(rotations1)) {
			this.setBodyRotation(rotations1);
		}

		Rotations rotations2 = this.dataWatcher.getWatchableObjectRotations(13);
		if (!this.leftArmRotation.equals(rotations2)) {
			this.setLeftArmRotation(rotations2);
		}

		Rotations rotations3 = this.dataWatcher.getWatchableObjectRotations(14);
		if (!this.rightArmRotation.equals(rotations3)) {
			this.setRightArmRotation(rotations3);
		}

		Rotations rotations4 = this.dataWatcher.getWatchableObjectRotations(15);
		if (!this.leftLegRotation.equals(rotations4)) {
			this.setLeftLegRotation(rotations4);
		}

		Rotations rotations5 = this.dataWatcher.getWatchableObjectRotations(16);
		if (!this.rightLegRotation.equals(rotations5)) {
			this.setRightLegRotation(rotations5);
		}

		boolean flag = this.func_181026_s();
		if (!this.field_181028_bj && flag) {
			this.func_181550_a(false);
		} else {
			if (!this.field_181028_bj || flag) {
				return;
			}

			this.func_181550_a(true);
		}

		this.field_181028_bj = flag;
	}

	private void func_181550_a(boolean parFlag) {
		double d0 = this.posX;
		double d1 = this.posY;
		double d2 = this.posZ;
		if (parFlag) {
			this.setSize(0.5F, 1.975F);
		} else {
			this.setSize(0.0F, 0.0F);
		}

		this.setPosition(d0, d1, d2);
	}

	/**+
	 * Clears potion metadata values if the entity has no potion
	 * effects. Otherwise, updates potion effect color, ambience,
	 * and invisibility metadata values
	 */
	protected void updatePotionMetadata() {
		this.setInvisible(this.canInteract);
	}

	public void setInvisible(boolean flag) {
		this.canInteract = flag;
		super.setInvisible(flag);
	}

	/**+
	 * If Animal, checks if the age timer is negative
	 */
	public boolean isChild() {
		return this.isSmall();
	}

	/**+
	 * Called by the /kill command.
	 */
	public void onKillCommand() {
		this.setDead();
	}

	public boolean isImmuneToExplosions() {
		return this.isInvisible();
	}

	private void setSmall(boolean parFlag) {
		byte b0 = this.dataWatcher.getWatchableObjectByte(10);
		if (parFlag) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 = (byte) (b0 & -2);
		}

		this.dataWatcher.updateObject(10, Byte.valueOf(b0));
	}

	public boolean isSmall() {
		return (this.dataWatcher.getWatchableObjectByte(10) & 1) != 0;
	}

	private void setNoGravity(boolean parFlag) {
		byte b0 = this.dataWatcher.getWatchableObjectByte(10);
		if (parFlag) {
			b0 = (byte) (b0 | 2);
		} else {
			b0 = (byte) (b0 & -3);
		}

		this.dataWatcher.updateObject(10, Byte.valueOf(b0));
	}

	public boolean hasNoGravity() {
		return (this.dataWatcher.getWatchableObjectByte(10) & 2) != 0;
	}

	private void setShowArms(boolean parFlag) {
		byte b0 = this.dataWatcher.getWatchableObjectByte(10);
		if (parFlag) {
			b0 = (byte) (b0 | 4);
		} else {
			b0 = (byte) (b0 & -5);
		}

		this.dataWatcher.updateObject(10, Byte.valueOf(b0));
	}

	public boolean getShowArms() {
		return (this.dataWatcher.getWatchableObjectByte(10) & 4) != 0;
	}

	private void setNoBasePlate(boolean parFlag) {
		byte b0 = this.dataWatcher.getWatchableObjectByte(10);
		if (parFlag) {
			b0 = (byte) (b0 | 8);
		} else {
			b0 = (byte) (b0 & -9);
		}

		this.dataWatcher.updateObject(10, Byte.valueOf(b0));
	}

	public boolean hasNoBasePlate() {
		return (this.dataWatcher.getWatchableObjectByte(10) & 8) != 0;
	}

	private void func_181027_m(boolean parFlag) {
		byte b0 = this.dataWatcher.getWatchableObjectByte(10);
		if (parFlag) {
			b0 = (byte) (b0 | 16);
		} else {
			b0 = (byte) (b0 & -17);
		}

		this.dataWatcher.updateObject(10, Byte.valueOf(b0));
	}

	public boolean func_181026_s() {
		return (this.dataWatcher.getWatchableObjectByte(10) & 16) != 0;
	}

	public void setHeadRotation(Rotations parRotations) {
		this.headRotation = parRotations;
		this.dataWatcher.updateObject(11, parRotations);
	}

	public void setBodyRotation(Rotations parRotations) {
		this.bodyRotation = parRotations;
		this.dataWatcher.updateObject(12, parRotations);
	}

	public void setLeftArmRotation(Rotations parRotations) {
		this.leftArmRotation = parRotations;
		this.dataWatcher.updateObject(13, parRotations);
	}

	public void setRightArmRotation(Rotations parRotations) {
		this.rightArmRotation = parRotations;
		this.dataWatcher.updateObject(14, parRotations);
	}

	public void setLeftLegRotation(Rotations parRotations) {
		this.leftLegRotation = parRotations;
		this.dataWatcher.updateObject(15, parRotations);
	}

	public void setRightLegRotation(Rotations parRotations) {
		this.rightLegRotation = parRotations;
		this.dataWatcher.updateObject(16, parRotations);
	}

	public Rotations getHeadRotation() {
		return this.headRotation;
	}

	public Rotations getBodyRotation() {
		return this.bodyRotation;
	}

	public Rotations getLeftArmRotation() {
		return this.leftArmRotation;
	}

	public Rotations getRightArmRotation() {
		return this.rightArmRotation;
	}

	public Rotations getLeftLegRotation() {
		return this.leftLegRotation;
	}

	public Rotations getRightLegRotation() {
		return this.rightLegRotation;
	}

	/**+
	 * Returns true if other Entities should be prevented from
	 * moving through this Entity.
	 */
	public boolean canBeCollidedWith() {
		return super.canBeCollidedWith() && !this.func_181026_s();
	}
}