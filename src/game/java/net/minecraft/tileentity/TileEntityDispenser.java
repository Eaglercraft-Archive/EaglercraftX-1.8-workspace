package net.minecraft.tileentity;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
public class TileEntityDispenser extends TileEntityLockable implements IInventory {
	private static final EaglercraftRandom RNG = new EaglercraftRandom();
	private ItemStack[] stacks = new ItemStack[9];
	protected String customName;

	/**+
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return 9;
	}

	/**+
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int i) {
		return this.stacks[i];
	}

	/**+
	 * Removes up to a specified number of items from an inventory
	 * slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int i, int j) {
		if (this.stacks[i] != null) {
			if (this.stacks[i].stackSize <= j) {
				ItemStack itemstack1 = this.stacks[i];
				this.stacks[i] = null;
				this.markDirty();
				return itemstack1;
			} else {
				ItemStack itemstack = this.stacks[i].splitStack(j);
				if (this.stacks[i].stackSize == 0) {
					this.stacks[i] = null;
				}

				this.markDirty();
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
		if (this.stacks[i] != null) {
			ItemStack itemstack = this.stacks[i];
			this.stacks[i] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	public int getDispenseSlot() {
		int i = -1;
		int j = 1;

		for (int k = 0; k < this.stacks.length; ++k) {
			if (this.stacks[k] != null && RNG.nextInt(j++) == 0) {
				i = k;
			}
		}

		return i;
	}

	/**+
	 * Sets the given item stack to the specified slot in the
	 * inventory (can be crafting or armor sections).
	 */
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.stacks[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	/**+
	 * Add the given ItemStack to this Dispenser. Return the Slot
	 * the Item was placed in or -1 if no free slot is available.
	 */
	public int addItemStack(ItemStack stack) {
		for (int i = 0; i < this.stacks.length; ++i) {
			if (this.stacks[i] == null || this.stacks[i].getItem() == null) {
				this.setInventorySlotContents(i, stack);
				return i;
			}
		}

		return -1;
	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.dispenser";
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	/**+
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName() {
		return this.customName != null;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
		this.stacks = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			if (j >= 0 && j < this.stacks.length) {
				this.stacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		if (nbttagcompound.hasKey("CustomName", 8)) {
			this.customName = nbttagcompound.getString("CustomName");
		}

	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.stacks.length; ++i) {
			if (this.stacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.stacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
		if (this.hasCustomName()) {
			nbttagcompound.setString("CustomName", this.customName);
		}

	}

	/**+
	 * Returns the maximum stack size for a inventory slot. Seems to
	 * always be 64, possibly will be extended.
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	/**+
	 * Do not make give this method the name canInteractWith because
	 * it clashes with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return this.worldObj.getTileEntity(this.pos) != this ? false
				: entityplayer.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
						(double) this.pos.getZ() + 0.5D) <= 64.0D;
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

	public String getGuiID() {
		return "minecraft:dispenser";
	}

	public Container createContainer(InventoryPlayer inventoryplayer, EntityPlayer var2) {
		return new ContainerDispenser(inventoryplayer, this);
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
		for (int i = 0; i < this.stacks.length; ++i) {
			this.stacks[i] = null;
		}

	}
}