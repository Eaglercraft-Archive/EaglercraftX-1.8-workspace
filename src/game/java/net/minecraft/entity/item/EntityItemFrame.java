package net.minecraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

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
public class EntityItemFrame extends EntityHanging {
	/**+
	 * Chance for this item frame's item to drop from the frame.
	 */
	private float itemDropChance = 1.0F;

	public EntityItemFrame(World worldIn) {
		super(worldIn);
	}

	public EntityItemFrame(World worldIn, BlockPos parBlockPos, EnumFacing parEnumFacing) {
		super(worldIn, parBlockPos);
		this.updateFacingWithBoundingBox(parEnumFacing);
	}

	protected void entityInit() {
		this.getDataWatcher().addObjectByDataType(8, 5);
		this.getDataWatcher().addObject(9, Byte.valueOf((byte) 0));
	}

	public float getCollisionBorderSize() {
		return 0.0F;
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		if (this.isEntityInvulnerable(damagesource)) {
			return false;
		} else if (!damagesource.isExplosion() && this.getDisplayedItem() != null) {
			if (!this.worldObj.isRemote) {
				this.dropItemOrSelf(damagesource.getEntity(), false);
				this.setDisplayedItem((ItemStack) null);
			}

			return true;
		} else {
			return super.attackEntityFrom(damagesource, f);
		}
	}

	public int getWidthPixels() {
		return 12;
	}

	public int getHeightPixels() {
		return 12;
	}

	/**+
	 * Checks if the entity is in range to render by using the past
	 * in distance and comparing it to its average edge length * 64
	 * * renderDistanceWeight Args: distance
	 */
	public boolean isInRangeToRenderDist(double d0) {
		double d1 = 16.0D;
		d1 = d1 * 64.0D * this.renderDistanceWeight;
		return d0 < d1 * d1;
	}

	/**+
	 * Called when this entity is broken. Entity parameter may be
	 * null.
	 */
	public void onBroken(Entity entity) {
		this.dropItemOrSelf(entity, true);
	}

	public void dropItemOrSelf(Entity parEntity, boolean parFlag) {
		if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
			ItemStack itemstack = this.getDisplayedItem();
			if (parEntity instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) parEntity;
				if (entityplayer.capabilities.isCreativeMode) {
					this.removeFrameFromMap(itemstack);
					return;
				}
			}

			if (parFlag) {
				this.entityDropItem(new ItemStack(Items.item_frame), 0.0F);
			}

			if (itemstack != null && this.rand.nextFloat() < this.itemDropChance) {
				itemstack = itemstack.copy();
				this.removeFrameFromMap(itemstack);
				this.entityDropItem(itemstack, 0.0F);
			}

		}
	}

	/**+
	 * Removes the dot representing this frame's position from the
	 * map when the item frame is broken.
	 */
	private void removeFrameFromMap(ItemStack parItemStack) {
		if (parItemStack != null) {
			if (parItemStack.getItem() == Items.filled_map) {
				MapData mapdata = ((ItemMap) parItemStack.getItem()).getMapData(parItemStack, this.worldObj);
				mapdata.mapDecorations.remove("frame-" + this.getEntityId());
			}

			parItemStack.setItemFrame((EntityItemFrame) null);
		}
	}

	public ItemStack getDisplayedItem() {
		return this.getDataWatcher().getWatchableObjectItemStack(8);
	}

	public void setDisplayedItem(ItemStack parItemStack) {
		this.setDisplayedItemWithUpdate(parItemStack, true);
	}

	private void setDisplayedItemWithUpdate(ItemStack parItemStack, boolean parFlag) {
		if (parItemStack != null) {
			parItemStack = parItemStack.copy();
			parItemStack.stackSize = 1;
			parItemStack.setItemFrame(this);
		}

		this.getDataWatcher().updateObject(8, parItemStack);
		this.getDataWatcher().setObjectWatched(8);
		if (parFlag && this.hangingPosition != null) {
			this.worldObj.updateComparatorOutputLevel(this.hangingPosition, Blocks.air);
		}

	}

	/**+
	 * Return the rotation of the item currently on this frame.
	 */
	public int getRotation() {
		return this.getDataWatcher().getWatchableObjectByte(9);
	}

	public void setItemRotation(int parInt1) {
		this.func_174865_a(parInt1, true);
	}

	private void func_174865_a(int parInt1, boolean parFlag) {
		this.getDataWatcher().updateObject(9, Byte.valueOf((byte) (parInt1 % 8)));
		if (parFlag && this.hangingPosition != null) {
			this.worldObj.updateComparatorOutputLevel(this.hangingPosition, Blocks.air);
		}

	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		if (this.getDisplayedItem() != null) {
			nbttagcompound.setTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
			nbttagcompound.setByte("ItemRotation", (byte) this.getRotation());
			nbttagcompound.setFloat("ItemDropChance", this.itemDropChance);
		}

		super.writeEntityToNBT(nbttagcompound);
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Item");
		if (nbttagcompound1 != null && !nbttagcompound1.hasNoTags()) {
			this.setDisplayedItemWithUpdate(ItemStack.loadItemStackFromNBT(nbttagcompound1), false);
			this.func_174865_a(nbttagcompound.getByte("ItemRotation"), false);
			if (nbttagcompound.hasKey("ItemDropChance", 99)) {
				this.itemDropChance = nbttagcompound.getFloat("ItemDropChance");
			}

			if (nbttagcompound.hasKey("Direction")) {
				this.func_174865_a(this.getRotation() * 2, false);
			}
		}

		super.readEntityFromNBT(nbttagcompound);
	}

	/**+
	 * First layer of player interaction
	 */
	public boolean interactFirst(EntityPlayer entityplayer) {
		if (this.getDisplayedItem() == null) {
			ItemStack itemstack = entityplayer.getHeldItem();
			if (itemstack != null && !this.worldObj.isRemote) {
				this.setDisplayedItem(itemstack);
				if (!entityplayer.capabilities.isCreativeMode && --itemstack.stackSize <= 0) {
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem,
							(ItemStack) null);
				}
			}
		} else if (!this.worldObj.isRemote) {
			this.setItemRotation(this.getRotation() + 1);
		}

		return true;
	}

	public int func_174866_q() {
		return this.getDisplayedItem() == null ? 0 : this.getRotation() % 8 + 1;
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
		ItemStack itm = this.getDisplayedItem();
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