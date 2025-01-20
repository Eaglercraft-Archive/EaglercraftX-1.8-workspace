package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

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
public class InventoryLargeChest implements ILockableContainer {
	private String name;
	private ILockableContainer upperChest;
	private ILockableContainer lowerChest;

	public InventoryLargeChest(String nameIn, ILockableContainer upperChestIn, ILockableContainer lowerChestIn) {
		this.name = nameIn;
		if (upperChestIn == null) {
			upperChestIn = lowerChestIn;
		}

		if (lowerChestIn == null) {
			lowerChestIn = upperChestIn;
		}

		this.upperChest = upperChestIn;
		this.lowerChest = lowerChestIn;
		if (upperChestIn.isLocked()) {
			lowerChestIn.setLockCode(upperChestIn.getLockCode());
		} else if (lowerChestIn.isLocked()) {
			upperChestIn.setLockCode(lowerChestIn.getLockCode());
		}

	}

	/**+
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
	}

	/**+
	 * Return whether the given inventory is part of this large
	 * chest.
	 */
	public boolean isPartOfLargeChest(IInventory inventoryIn) {
		return this.upperChest == inventoryIn || this.lowerChest == inventoryIn;
	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return this.upperChest.hasCustomName() ? this.upperChest.getName()
				: (this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name);
	}

	/**+
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName() {
		return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
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
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int i) {
		return i >= this.upperChest.getSizeInventory()
				? this.lowerChest.getStackInSlot(i - this.upperChest.getSizeInventory())
				: this.upperChest.getStackInSlot(i);
	}

	/**+
	 * Removes up to a specified number of items from an inventory
	 * slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int i, int j) {
		return i >= this.upperChest.getSizeInventory()
				? this.lowerChest.decrStackSize(i - this.upperChest.getSizeInventory(), j)
				: this.upperChest.decrStackSize(i, j);
	}

	/**+
	 * Removes a stack from the given slot and returns it.
	 */
	public ItemStack removeStackFromSlot(int i) {
		return i >= this.upperChest.getSizeInventory()
				? this.lowerChest.removeStackFromSlot(i - this.upperChest.getSizeInventory())
				: this.upperChest.removeStackFromSlot(i);
	}

	/**+
	 * Sets the given item stack to the specified slot in the
	 * inventory (can be crafting or armor sections).
	 */
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (i >= this.upperChest.getSizeInventory()) {
			this.lowerChest.setInventorySlotContents(i - this.upperChest.getSizeInventory(), itemstack);
		} else {
			this.upperChest.setInventorySlotContents(i, itemstack);
		}

	}

	/**+
	 * Returns the maximum stack size for a inventory slot. Seems to
	 * always be 64, possibly will be extended.
	 */
	public int getInventoryStackLimit() {
		return this.upperChest.getInventoryStackLimit();
	}

	/**+
	 * For tile entities, ensures the chunk containing the tile
	 * entity is saved to disk later - the game won't think it
	 * hasn't changed and skip it.
	 */
	public void markDirty() {
		this.upperChest.markDirty();
		this.lowerChest.markDirty();
	}

	/**+
	 * Do not make give this method the name canInteractWith because
	 * it clashes with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return this.upperChest.isUseableByPlayer(entityplayer) && this.lowerChest.isUseableByPlayer(entityplayer);
	}

	public void openInventory(EntityPlayer entityplayer) {
		this.upperChest.openInventory(entityplayer);
		this.lowerChest.openInventory(entityplayer);
	}

	public void closeInventory(EntityPlayer entityplayer) {
		this.upperChest.closeInventory(entityplayer);
		this.lowerChest.closeInventory(entityplayer);
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

	public boolean isLocked() {
		return this.upperChest.isLocked() || this.lowerChest.isLocked();
	}

	public void setLockCode(LockCode lockcode) {
		this.upperChest.setLockCode(lockcode);
		this.lowerChest.setLockCode(lockcode);
	}

	public LockCode getLockCode() {
		return this.upperChest.getLockCode();
	}

	public String getGuiID() {
		return this.upperChest.getGuiID();
	}

	public Container createContainer(InventoryPlayer inventoryplayer, EntityPlayer entityplayer) {
		return new ContainerChest(inventoryplayer, this, entityplayer);
	}

	public void clear() {
		this.upperChest.clear();
		this.lowerChest.clear();
	}
}