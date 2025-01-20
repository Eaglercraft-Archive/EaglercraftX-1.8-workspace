package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
public abstract class EntityAgeable extends EntityCreature {
	protected int growingAge;
	protected int field_175502_b;
	protected int field_175503_c;
	private float ageWidth = -1.0F;
	private float ageHeight;

	public EntityAgeable(World worldIn) {
		super(worldIn);
	}

	public abstract EntityAgeable createChild(EntityAgeable var1);

	/**+
	 * Called when a player interacts with a mob. e.g. gets milk
	 * from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer player) {
		ItemStack itemstack = player.inventory.getCurrentItem();
		if (itemstack != null && itemstack.getItem() == Items.spawn_egg) {
			if (!this.worldObj.isRemote) {
				Class oclass = EntityList.getClassFromID(itemstack.getMetadata());
				if (oclass != null && this.getClass() == oclass) {
					EntityAgeable entityageable = this.createChild(this);
					if (entityageable != null) {
						entityageable.setGrowingAge(-24000);
						entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
						this.worldObj.spawnEntityInWorld(entityageable);
						if (itemstack.hasDisplayName()) {
							entityageable.setCustomNameTag(itemstack.getDisplayName());
						}

						if (!player.capabilities.isCreativeMode) {
							--itemstack.stackSize;
							if (itemstack.stackSize <= 0) {
								player.inventory.setInventorySlotContents(player.inventory.currentItem,
										(ItemStack) null);
							}
						}
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(12, Byte.valueOf((byte) 0));
	}

	/**+
	 * The age value may be negative or positive or zero. If it's
	 * negative, it get's incremented on each tick, if it's
	 * positive, it get's decremented each tick. Don't confuse this
	 * with EntityLiving.getAge. With a negative value the Entity is
	 * considered a child.
	 */
	public int getGrowingAge() {
		return this.worldObj.isRemote ? this.dataWatcher.getWatchableObjectByte(12) : this.growingAge;
	}

	public void func_175501_a(int parInt1, boolean parFlag) {
		int i = this.getGrowingAge();
		int j = i;
		i = i + parInt1 * 20;
		if (i > 0) {
			i = 0;
			if (j < 0) {
				this.onGrowingAdult();
			}
		}

		int k = i - j;
		this.setGrowingAge(i);
		if (parFlag) {
			this.field_175502_b += k;
			if (this.field_175503_c == 0) {
				this.field_175503_c = 40;
			}
		}

		if (this.getGrowingAge() == 0) {
			this.setGrowingAge(this.field_175502_b);
		}

	}

	/**+
	 * "Adds the value of the parameter times 20 to the age of this
	 * entity. If the entity is an adult (if the entity's age is
	 * greater than 0), it will have no effect."
	 */
	public void addGrowth(int growth) {
		this.func_175501_a(growth, false);
	}

	/**+
	 * The age value may be negative or positive or zero. If it's
	 * negative, it get's incremented on each tick, if it's
	 * positive, it get's decremented each tick. With a negative
	 * value the Entity is considered a child.
	 */
	public void setGrowingAge(int age) {
		this.dataWatcher.updateObject(12, Byte.valueOf((byte) MathHelper.clamp_int(age, -1, 1)));
		this.growingAge = age;
		this.setScaleForAge(this.isChild());
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("Age", this.getGrowingAge());
		nbttagcompound.setInteger("ForcedAge", this.field_175502_b);
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setGrowingAge(nbttagcompound.getInteger("Age"));
		this.field_175502_b = nbttagcompound.getInteger("ForcedAge");
	}

	/**+
	 * Called frequently so the entity can update its state every
	 * tick as required. For example, zombies and skeletons use this
	 * to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.worldObj.isRemote) {
			if (this.field_175503_c > 0) {
				if (this.field_175503_c % 4 == 0) {
					this.worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
							this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
							this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height),
							this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
							0.0D, 0.0D, 0.0D, new int[0]);
				}

				--this.field_175503_c;
			}

			this.setScaleForAge(this.isChild());
		} else {
			int i = this.getGrowingAge();
			if (i < 0) {
				++i;
				this.setGrowingAge(i);
				if (i == 0) {
					this.onGrowingAdult();
				}
			} else if (i > 0) {
				--i;
				this.setGrowingAge(i);
			}
		}

	}

	/**+
	 * This is called when Entity's growing age timer reaches 0
	 * (negative values are considered as a child, positive as an
	 * adult)
	 */
	protected void onGrowingAdult() {
	}

	/**+
	 * If Animal, checks if the age timer is negative
	 */
	public boolean isChild() {
		return this.getGrowingAge() < 0;
	}

	/**+
	 * "Sets the scale for an ageable entity according to the
	 * boolean parameter, which says if it's a child."
	 */
	public void setScaleForAge(boolean parFlag) {
		this.setScale(parFlag ? 0.5F : 1.0F);
	}

	/**+
	 * Sets the width and height of the entity. Args: width, height
	 */
	protected final void setSize(float width, float height) {
		boolean flag = this.ageWidth > 0.0F;
		this.ageWidth = width;
		this.ageHeight = height;
		if (!flag) {
			this.setScale(1.0F);
		}

	}

	protected final void setScale(float scale) {
		super.setSize(this.ageWidth * scale, this.ageHeight * scale);
	}
}