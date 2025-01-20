package net.minecraft.entity.player;

import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;

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
public class InventoryPlayer implements IInventory {
	/**+
	 * An array of 36 item stacks indicating the main player
	 * inventory (including the visible bar).
	 */
	public ItemStack[] mainInventory = new ItemStack[36];
	/**+
	 * An array of 4 item stacks containing the currently worn armor
	 * pieces.
	 */
	public ItemStack[] armorInventory = new ItemStack[4];
	public int currentItem;
	public EntityPlayer player;
	private ItemStack itemStack;
	public boolean inventoryChanged;

	public InventoryPlayer(EntityPlayer playerIn) {
		this.player = playerIn;
	}

	/**+
	 * Returns the item stack currently held by the player.
	 */
	public ItemStack getCurrentItem() {
		return this.currentItem < 9 && this.currentItem >= 0 ? this.mainInventory[this.currentItem] : null;
	}

	/**+
	 * Get the size of the player hotbar inventory
	 */
	public static int getHotbarSize() {
		return 9;
	}

	private int getInventorySlotContainItem(Item itemIn) {
		for (int i = 0; i < this.mainInventory.length; ++i) {
			if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == itemIn) {
				return i;
			}
		}

		return -1;
	}

	private int getInventorySlotContainItemAndDamage(Item itemIn, int parInt1) {
		for (int i = 0; i < this.mainInventory.length; ++i) {
			if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == itemIn
					&& this.mainInventory[i].getMetadata() == parInt1) {
				return i;
			}
		}

		return -1;
	}

	/**+
	 * stores an itemstack in the users inventory
	 */
	private int storeItemStack(ItemStack itemStackIn) {
		for (int i = 0; i < this.mainInventory.length; ++i) {
			if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == itemStackIn.getItem()
					&& this.mainInventory[i].isStackable()
					&& this.mainInventory[i].stackSize < this.mainInventory[i].getMaxStackSize()
					&& this.mainInventory[i].stackSize < this.getInventoryStackLimit()
					&& (!this.mainInventory[i].getHasSubtypes()
							|| this.mainInventory[i].getMetadata() == itemStackIn.getMetadata())
					&& ItemStack.areItemStackTagsEqual(this.mainInventory[i], itemStackIn)) {
				return i;
			}
		}

		return -1;
	}

	/**+
	 * Returns the first item stack that is empty.
	 */
	public int getFirstEmptyStack() {
		for (int i = 0; i < this.mainInventory.length; ++i) {
			if (this.mainInventory[i] == null) {
				return i;
			}
		}

		return -1;
	}

	public void setCurrentItem(Item itemIn, int parInt1, boolean parFlag, boolean parFlag2) {
		ItemStack itemstack = this.getCurrentItem();
		int i = parFlag ? this.getInventorySlotContainItemAndDamage(itemIn, parInt1)
				: this.getInventorySlotContainItem(itemIn);
		if (i >= 0 && i < 9) {
			this.currentItem = i;
		} else if (parFlag2 && itemIn != null) {
			int j = this.getFirstEmptyStack();
			if (j >= 0 && j < 9) {
				this.currentItem = j;
			}

			if (itemstack == null || !itemstack.isItemEnchantable()
					|| this.getInventorySlotContainItemAndDamage(itemstack.getItem(),
							itemstack.getItemDamage()) != this.currentItem) {
				int k = this.getInventorySlotContainItemAndDamage(itemIn, parInt1);
				int l;
				if (k >= 0) {
					l = this.mainInventory[k].stackSize;
					this.mainInventory[k] = this.mainInventory[this.currentItem];
				} else {
					l = 1;
				}

				this.mainInventory[this.currentItem] = new ItemStack(itemIn, l, parInt1);
			}

		}
	}

	/**+
	 * Switch the current item to the next one or the previous one
	 */
	public void changeCurrentItem(int parInt1) {
		if (parInt1 > 0) {
			parInt1 = 1;
		}

		if (parInt1 < 0) {
			parInt1 = -1;
		}

		for (this.currentItem -= parInt1; this.currentItem < 0; this.currentItem += 9) {
			;
		}

		while (this.currentItem >= 9) {
			this.currentItem -= 9;
		}

	}

	/**+
	 * Removes matching items from the inventory.\n@param itemIn The
	 * item to match, null ignores.\n@param metadataIn The metadata
	 * to match, -1 ignores.\n@param removeCount The number of items
	 * to remove. If less than 1, removes all matching
	 * items.\n@param itemNBT The NBT data to match, null
	 * ignores.\n@return The number of items removed from the
	 * inventory.
	 */
	public int clearMatchingItems(Item itemIn, int metadataIn, int removeCount, NBTTagCompound itemNBT) {
		int i = 0;

		for (int j = 0; j < this.mainInventory.length; ++j) {
			ItemStack itemstack = this.mainInventory[j];
			if (itemstack != null && (itemIn == null || itemstack.getItem() == itemIn)
					&& (metadataIn <= -1 || itemstack.getMetadata() == metadataIn)
					&& (itemNBT == null || NBTUtil.func_181123_a(itemNBT, itemstack.getTagCompound(), true))) {
				int k = removeCount <= 0 ? itemstack.stackSize : Math.min(removeCount - i, itemstack.stackSize);
				i += k;
				if (removeCount != 0) {
					this.mainInventory[j].stackSize -= k;
					if (this.mainInventory[j].stackSize == 0) {
						this.mainInventory[j] = null;
					}

					if (removeCount > 0 && i >= removeCount) {
						return i;
					}
				}
			}
		}

		for (int l = 0; l < this.armorInventory.length; ++l) {
			ItemStack itemstack1 = this.armorInventory[l];
			if (itemstack1 != null && (itemIn == null || itemstack1.getItem() == itemIn)
					&& (metadataIn <= -1 || itemstack1.getMetadata() == metadataIn)
					&& (itemNBT == null || NBTUtil.func_181123_a(itemNBT, itemstack1.getTagCompound(), false))) {
				int j1 = removeCount <= 0 ? itemstack1.stackSize : Math.min(removeCount - i, itemstack1.stackSize);
				i += j1;
				if (removeCount != 0) {
					this.armorInventory[l].stackSize -= j1;
					if (this.armorInventory[l].stackSize == 0) {
						this.armorInventory[l] = null;
					}

					if (removeCount > 0 && i >= removeCount) {
						return i;
					}
				}
			}
		}

		if (this.itemStack != null) {
			if (itemIn != null && this.itemStack.getItem() != itemIn) {
				return i;
			}

			if (metadataIn > -1 && this.itemStack.getMetadata() != metadataIn) {
				return i;
			}

			if (itemNBT != null && !NBTUtil.func_181123_a(itemNBT, this.itemStack.getTagCompound(), false)) {
				return i;
			}

			int i1 = removeCount <= 0 ? this.itemStack.stackSize : Math.min(removeCount - i, this.itemStack.stackSize);
			i += i1;
			if (removeCount != 0) {
				this.itemStack.stackSize -= i1;
				if (this.itemStack.stackSize == 0) {
					this.itemStack = null;
				}

				if (removeCount > 0 && i >= removeCount) {
					return i;
				}
			}
		}

		return i;
	}

	/**+
	 * This function stores as many items of an ItemStack as
	 * possible in a matching slot and returns the quantity of left
	 * over items.
	 */
	private int storePartialItemStack(ItemStack itemStackIn) {
		Item item = itemStackIn.getItem();
		int i = itemStackIn.stackSize;
		int j = this.storeItemStack(itemStackIn);
		if (j < 0) {
			j = this.getFirstEmptyStack();
		}

		if (j < 0) {
			return i;
		} else {
			if (this.mainInventory[j] == null) {
				this.mainInventory[j] = new ItemStack(item, 0, itemStackIn.getMetadata());
				if (itemStackIn.hasTagCompound()) {
					this.mainInventory[j].setTagCompound((NBTTagCompound) itemStackIn.getTagCompound().copy());
				}
			}

			int k = i;
			if (i > this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize) {
				k = this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize;
			}

			if (k > this.getInventoryStackLimit() - this.mainInventory[j].stackSize) {
				k = this.getInventoryStackLimit() - this.mainInventory[j].stackSize;
			}

			if (k == 0) {
				return i;
			} else {
				i = i - k;
				this.mainInventory[j].stackSize += k;
				this.mainInventory[j].animationsToGo = 5;
				return i;
			}
		}
	}

	/**+
	 * Decrement the number of animations remaining. Only called on
	 * client side. This is used to handle the animation of
	 * receiving a block.
	 */
	public void decrementAnimations() {
		for (int i = 0; i < this.mainInventory.length; ++i) {
			if (this.mainInventory[i] != null) {
				this.mainInventory[i].updateAnimation(this.player.worldObj, this.player, i, this.currentItem == i);
			}
		}

	}

	/**+
	 * removed one item of specified Item from inventory (if it is
	 * in a stack, the stack size will reduce with 1)
	 */
	public boolean consumeInventoryItem(Item itemIn) {
		int i = this.getInventorySlotContainItem(itemIn);
		if (i < 0) {
			return false;
		} else {
			if (--this.mainInventory[i].stackSize <= 0) {
				this.mainInventory[i] = null;
			}

			return true;
		}
	}

	/**+
	 * Checks if a specified Item is inside the inventory
	 */
	public boolean hasItem(Item itemIn) {
		int i = this.getInventorySlotContainItem(itemIn);
		return i >= 0;
	}

	/**+
	 * Adds the item stack to the inventory, returns false if it is
	 * impossible.
	 */
	public boolean addItemStackToInventory(final ItemStack itemStackIn) {
		if (itemStackIn != null && itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
			try {
				if (itemStackIn.isItemDamaged()) {
					int j = this.getFirstEmptyStack();
					if (j >= 0) {
						this.mainInventory[j] = ItemStack.copyItemStack(itemStackIn);
						this.mainInventory[j].animationsToGo = 5;
						itemStackIn.stackSize = 0;
						return true;
					} else if (this.player.capabilities.isCreativeMode) {
						itemStackIn.stackSize = 0;
						return true;
					} else {
						return false;
					}
				} else {
					int i;
					while (true) {
						i = itemStackIn.stackSize;
						itemStackIn.stackSize = this.storePartialItemStack(itemStackIn);
						if (itemStackIn.stackSize <= 0 || itemStackIn.stackSize >= i) {
							break;
						}
					}

					if (itemStackIn.stackSize == i && this.player.capabilities.isCreativeMode) {
						itemStackIn.stackSize = 0;
						return true;
					} else {
						return itemStackIn.stackSize < i;
					}
				}
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
				crashreportcategory.addCrashSection("Item ID",
						Integer.valueOf(Item.getIdFromItem(itemStackIn.getItem())));
				crashreportcategory.addCrashSection("Item data", Integer.valueOf(itemStackIn.getMetadata()));
				crashreportcategory.addCrashSectionCallable("Item name", new Callable<String>() {
					public String call() throws Exception {
						return itemStackIn.getDisplayName();
					}
				});
				throw new ReportedException(crashreport);
			}
		} else {
			return false;
		}
	}

	/**+
	 * Removes up to a specified number of items from an inventory
	 * slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int i, int j) {
		ItemStack[] aitemstack = this.mainInventory;
		if (i >= this.mainInventory.length) {
			aitemstack = this.armorInventory;
			i -= this.mainInventory.length;
		}

		if (aitemstack[i] != null) {
			if (aitemstack[i].stackSize <= j) {
				ItemStack itemstack1 = aitemstack[i];
				aitemstack[i] = null;
				return itemstack1;
			} else {
				ItemStack itemstack = aitemstack[i].splitStack(j);
				if (aitemstack[i].stackSize == 0) {
					aitemstack[i] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	/**+
	 * Removes a stack from the given slot and returns it.
	 */
	public ItemStack removeStackFromSlot(int i) {
		ItemStack[] aitemstack = this.mainInventory;
		if (i >= this.mainInventory.length) {
			aitemstack = this.armorInventory;
			i -= this.mainInventory.length;
		}

		if (aitemstack[i] != null) {
			ItemStack itemstack = aitemstack[i];
			aitemstack[i] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	/**+
	 * Sets the given item stack to the specified slot in the
	 * inventory (can be crafting or armor sections).
	 */
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		ItemStack[] aitemstack = this.mainInventory;
		if (i >= aitemstack.length) {
			i -= aitemstack.length;
			aitemstack = this.armorInventory;
		}

		aitemstack[i] = itemstack;
	}

	public float getStrVsBlock(Block blockIn) {
		float f = 1.0F;
		if (this.mainInventory[this.currentItem] != null) {
			f *= this.mainInventory[this.currentItem].getStrVsBlock(blockIn);
		}

		return f;
	}

	/**+
	 * Writes the inventory out as a list of compound tags. This is
	 * where the slot indices are used (+100 for armor, +80 for
	 * crafting).
	 */
	public NBTTagList writeToNBT(NBTTagList parNBTTagList) {
		for (int i = 0; i < this.mainInventory.length; ++i) {
			if (this.mainInventory[i] != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				this.mainInventory[i].writeToNBT(nbttagcompound);
				parNBTTagList.appendTag(nbttagcompound);
			}
		}

		for (int j = 0; j < this.armorInventory.length; ++j) {
			if (this.armorInventory[j] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) (j + 100));
				this.armorInventory[j].writeToNBT(nbttagcompound1);
				parNBTTagList.appendTag(nbttagcompound1);
			}
		}

		return parNBTTagList;
	}

	/**+
	 * Reads from the given tag list and fills the slots in the
	 * inventory with the correct items.
	 */
	public void readFromNBT(NBTTagList parNBTTagList) {
		this.mainInventory = new ItemStack[36];
		this.armorInventory = new ItemStack[4];

		for (int i = 0; i < parNBTTagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = parNBTTagList.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
			if (itemstack != null) {
				if (j >= 0 && j < this.mainInventory.length) {
					this.mainInventory[j] = itemstack;
				}

				if (j >= 100 && j < this.armorInventory.length + 100) {
					this.armorInventory[j - 100] = itemstack;
				}
			}
		}

	}

	/**+
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return this.mainInventory.length + 4;
	}

	/**+
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int i) {
		ItemStack[] aitemstack = this.mainInventory;
		if (i >= aitemstack.length) {
			i -= aitemstack.length;
			aitemstack = this.armorInventory;
		}

		return aitemstack[i];
	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return "container.inventory";
	}

	/**+
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName() {
		return false;
	}

	/**+
	 * Get the formatted ChatComponent that will be used for the
	 * sender's username in chat
	 */
	public IChatComponent getDisplayName() {
		return (IChatComponent) (this.hasCustomName() ? new ChatComponentText(this.getName())
				: new ChatComponentTranslation(this.getName(), new Object[0]));
	}

	/**+
	 * Returns the maximum stack size for a inventory slot. Seems to
	 * always be 64, possibly will be extended.
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean canHeldItemHarvest(Block blockIn) {
		if (blockIn.getMaterial().isToolNotRequired()) {
			return true;
		} else {
			ItemStack itemstack = this.getStackInSlot(this.currentItem);
			return itemstack != null ? itemstack.canHarvestBlock(blockIn) : false;
		}
	}

	/**+
	 * returns a player armor item (as itemstack) contained in
	 * specified armor slot.
	 */
	public ItemStack armorItemInSlot(int parInt1) {
		return this.armorInventory[parInt1];
	}

	/**+
	 * Based on the damage values and maximum damage values of each
	 * armor item, returns the current armor value.
	 */
	public int getTotalArmorValue() {
		int i = 0;

		for (int j = 0; j < this.armorInventory.length; ++j) {
			if (this.armorInventory[j] != null && this.armorInventory[j].getItem() instanceof ItemArmor) {
				int k = ((ItemArmor) this.armorInventory[j].getItem()).damageReduceAmount;
				i += k;
			}
		}

		return i;
	}

	/**+
	 * Damages armor in each slot by the specified amount.
	 */
	public void damageArmor(float damage) {
		damage = damage / 4.0F;
		if (damage < 1.0F) {
			damage = 1.0F;
		}

		for (int i = 0; i < this.armorInventory.length; ++i) {
			if (this.armorInventory[i] != null && this.armorInventory[i].getItem() instanceof ItemArmor) {
				this.armorInventory[i].damageItem((int) damage, this.player);
				if (this.armorInventory[i].stackSize == 0) {
					this.armorInventory[i] = null;
				}
			}
		}

	}

	/**+
	 * Drop all armor and main inventory items.
	 */
	public void dropAllItems() {
		for (int i = 0; i < this.mainInventory.length; ++i) {
			if (this.mainInventory[i] != null) {
				this.player.dropItem(this.mainInventory[i], true, false);
				this.mainInventory[i] = null;
			}
		}

		for (int j = 0; j < this.armorInventory.length; ++j) {
			if (this.armorInventory[j] != null) {
				this.player.dropItem(this.armorInventory[j], true, false);
				this.armorInventory[j] = null;
			}
		}

	}

	/**+
	 * For tile entities, ensures the chunk containing the tile
	 * entity is saved to disk later - the game won't think it
	 * hasn't changed and skip it.
	 */
	public void markDirty() {
		this.inventoryChanged = true;
	}

	/**+
	 * Set the stack helds by mouse, used in GUI/Container
	 */
	public void setItemStack(ItemStack itemStackIn) {
		this.itemStack = itemStackIn;
	}

	/**+
	 * Stack helds by mouse, used in GUI and Containers
	 */
	public ItemStack getItemStack() {
		return this.itemStack;
	}

	/**+
	 * Do not make give this method the name canInteractWith because
	 * it clashes with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return this.player.isDead ? false : entityplayer.getDistanceSqToEntity(this.player) <= 64.0D;
	}

	/**+
	 * Returns true if the specified ItemStack exists in the
	 * inventory.
	 */
	public boolean hasItemStack(ItemStack itemStackIn) {
		for (int i = 0; i < this.armorInventory.length; ++i) {
			if (this.armorInventory[i] != null && this.armorInventory[i].isItemEqual(itemStackIn)) {
				return true;
			}
		}

		for (int j = 0; j < this.mainInventory.length; ++j) {
			if (this.mainInventory[j] != null && this.mainInventory[j].isItemEqual(itemStackIn)) {
				return true;
			}
		}

		return false;
	}

	public void openInventory(EntityPlayer var1) {
	}

	public void closeInventory(EntityPlayer var1) {
	}

	/**+
	 * Returns true if automation is allowed to insert the given
	 * stack (ignoring stack size) into the given slot.
	 */
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return true;
	}

	/**+
	 * Copy the ItemStack contents from another InventoryPlayer
	 * instance
	 */
	public void copyInventory(InventoryPlayer playerInventory) {
		for (int i = 0; i < this.mainInventory.length; ++i) {
			this.mainInventory[i] = ItemStack.copyItemStack(playerInventory.mainInventory[i]);
		}

		for (int j = 0; j < this.armorInventory.length; ++j) {
			this.armorInventory[j] = ItemStack.copyItemStack(playerInventory.armorInventory[j]);
		}

		this.currentItem = playerInventory.currentItem;
	}

	public int getField(int var1) {
		return 0;
	}

	public void setField(int var1, int var2) {
	}

	public int getFieldCount() {
		return 0;
	}

	public void clear() {
		for (int i = 0; i < this.mainInventory.length; ++i) {
			this.mainInventory[i] = null;
		}

		for (int j = 0; j < this.armorInventory.length; ++j) {
			this.armorInventory[j] = null;
		}

	}
}