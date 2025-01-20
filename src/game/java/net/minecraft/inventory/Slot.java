package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

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
public class Slot {
	private final int slotIndex;
	public final IInventory inventory;
	public int slotNumber;
	public int xDisplayPosition;
	public int yDisplayPosition;

	public Slot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		this.inventory = inventoryIn;
		this.slotIndex = index;
		this.xDisplayPosition = xPosition;
		this.yDisplayPosition = yPosition;
	}

	/**+
	 * if par2 has more items than par1,
	 * onCrafting(item,countIncrease) is called
	 */
	public void onSlotChange(ItemStack parItemStack, ItemStack parItemStack2) {
		if (parItemStack != null && parItemStack2 != null) {
			if (parItemStack.getItem() == parItemStack2.getItem()) {
				int i = parItemStack2.stackSize - parItemStack.stackSize;
				if (i > 0) {
					this.onCrafting(parItemStack, i);
				}

			}
		}
	}

	/**+
	 * the itemStack passed in is the output - ie, iron ingots, and
	 * pickaxes, not ore and wood. Typically increases an internal
	 * count then calls onCrafting(item).
	 */
	protected void onCrafting(ItemStack var1, int var2) {
	}

	/**+
	 * the itemStack passed in is the output - ie, iron ingots, and
	 * pickaxes, not ore and wood. Typically increases an internal
	 * count then calls onCrafting(item).
	 */
	protected void onCrafting(ItemStack var1) {
	}

	public void onPickupFromSlot(EntityPlayer var1, ItemStack var2) {
		this.onSlotChanged();
	}

	/**+
	 * Check if the stack is a valid item for this slot. Always true
	 * beside for the armor slots.
	 */
	public boolean isItemValid(ItemStack var1) {
		return true;
	}

	/**+
	 * Helper fnct to get the stack in the slot.
	 */
	public ItemStack getStack() {
		return this.inventory.getStackInSlot(this.slotIndex);
	}

	/**+
	 * Returns if this slot contains a stack.
	 */
	public boolean getHasStack() {
		return this.getStack() != null;
	}

	/**+
	 * Helper method to put a stack in the slot.
	 */
	public void putStack(ItemStack itemstack) {
		this.inventory.setInventorySlotContents(this.slotIndex, itemstack);
		this.onSlotChanged();
	}

	/**+
	 * Called when the stack in a Slot changes
	 */
	public void onSlotChanged() {
		this.inventory.markDirty();
	}

	/**+
	 * Returns the maximum stack size for a given slot (usually the
	 * same as getInventoryStackLimit(), but 1 in the case of armor
	 * slots)
	 */
	public int getSlotStackLimit() {
		return this.inventory.getInventoryStackLimit();
	}

	public int getItemStackLimit(ItemStack var1) {
		return this.getSlotStackLimit();
	}

	public String getSlotTexture() {
		return null;
	}

	/**+
	 * Decrease the size of the stack in slot (first int arg) by the
	 * amount of the second int arg. Returns the new stack.
	 */
	public ItemStack decrStackSize(int i) {
		return this.inventory.decrStackSize(this.slotIndex, i);
	}

	/**+
	 * returns true if the slot exists in the given inventory and
	 * location
	 */
	public boolean isHere(IInventory iinventory, int i) {
		return iinventory == this.inventory && i == this.slotIndex;
	}

	/**+
	 * Return whether this slot's stack can be taken from this slot.
	 */
	public boolean canTakeStack(EntityPlayer var1) {
		return true;
	}

	/**+
	 * Actualy only call when we want to render the white square
	 * effect over the slots. Return always True, except for the
	 * armor slot of the Donkey/Mule (we can't interact with the
	 * Undead and Skeleton horses)
	 */
	public boolean canBeHovered() {
		return true;
	}
}