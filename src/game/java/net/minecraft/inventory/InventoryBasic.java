package net.minecraft.inventory;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

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
public class InventoryBasic implements IInventory {
	private String inventoryTitle;
	private int slotsCount;
	private ItemStack[] inventoryContents;
	private List<IInvBasic> field_70480_d;
	private boolean hasCustomName;

	public InventoryBasic(String title, boolean customName, int slotCount) {
		this.inventoryTitle = title;
		this.hasCustomName = customName;
		this.slotsCount = slotCount;
		this.inventoryContents = new ItemStack[slotCount];
	}

	public InventoryBasic(IChatComponent title, int slotCount) {
		this(title.getUnformattedText(), true, slotCount);
	}

	public void func_110134_a(IInvBasic parIInvBasic) {
		if (this.field_70480_d == null) {
			this.field_70480_d = Lists.newArrayList();
		}

		this.field_70480_d.add(parIInvBasic);
	}

	public void func_110132_b(IInvBasic parIInvBasic) {
		this.field_70480_d.remove(parIInvBasic);
	}

	/**+
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int i) {
		return i >= 0 && i < this.inventoryContents.length ? this.inventoryContents[i] : null;
	}

	/**+
	 * Removes up to a specified number of items from an inventory
	 * slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int i, int j) {
		if (this.inventoryContents[i] != null) {
			if (this.inventoryContents[i].stackSize <= j) {
				ItemStack itemstack1 = this.inventoryContents[i];
				this.inventoryContents[i] = null;
				this.markDirty();
				return itemstack1;
			} else {
				ItemStack itemstack = this.inventoryContents[i].splitStack(j);
				if (this.inventoryContents[i].stackSize == 0) {
					this.inventoryContents[i] = null;
				}

				this.markDirty();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	public ItemStack func_174894_a(ItemStack stack) {
		ItemStack itemstack = stack.copy();

		for (int i = 0; i < this.slotsCount; ++i) {
			ItemStack itemstack1 = this.getStackInSlot(i);
			if (itemstack1 == null) {
				this.setInventorySlotContents(i, itemstack);
				this.markDirty();
				return null;
			}

			if (ItemStack.areItemsEqual(itemstack1, itemstack)) {
				int j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize());
				int k = Math.min(itemstack.stackSize, j - itemstack1.stackSize);
				if (k > 0) {
					itemstack1.stackSize += k;
					itemstack.stackSize -= k;
					if (itemstack.stackSize <= 0) {
						this.markDirty();
						return null;
					}
				}
			}
		}

		if (itemstack.stackSize != stack.stackSize) {
			this.markDirty();
		}

		return itemstack;
	}

	/**+
	 * Removes a stack from the given slot and returns it.
	 */
	public ItemStack removeStackFromSlot(int i) {
		if (this.inventoryContents[i] != null) {
			ItemStack itemstack = this.inventoryContents[i];
			this.inventoryContents[i] = null;
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
		this.inventoryContents[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	/**+
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return this.slotsCount;
	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return this.inventoryTitle;
	}

	/**+
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName() {
		return this.hasCustomName;
	}

	/**+
	 * Sets the name of this inventory. This is displayed to the
	 * client on opening.
	 */
	public void setCustomName(String inventoryTitleIn) {
		this.hasCustomName = true;
		this.inventoryTitle = inventoryTitleIn;
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

	/**+
	 * For tile entities, ensures the chunk containing the tile
	 * entity is saved to disk later - the game won't think it
	 * hasn't changed and skip it.
	 */
	public void markDirty() {
		if (this.field_70480_d != null) {
			for (int i = 0; i < this.field_70480_d.size(); ++i) {
				((IInvBasic) this.field_70480_d.get(i)).onInventoryChanged(this);
			}
		}

	}

	/**+
	 * Do not make give this method the name canInteractWith because
	 * it clashes with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
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

	public int getField(int var1) {
		return 0;
	}

	public void setField(int var1, int var2) {
	}

	public int getFieldCount() {
		return 0;
	}

	public void clear() {
		for (int i = 0; i < this.inventoryContents.length; ++i) {
			this.inventoryContents[i] = null;
		}

	}
}