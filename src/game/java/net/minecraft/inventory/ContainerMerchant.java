package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
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
public class ContainerMerchant extends Container {
	private IMerchant theMerchant;
	private InventoryMerchant merchantInventory;
	private final World theWorld;

	public ContainerMerchant(InventoryPlayer playerInventory, IMerchant merchant, World worldIn) {
		this.theMerchant = merchant;
		this.theWorld = worldIn;
		this.merchantInventory = new InventoryMerchant(playerInventory.player, merchant);
		this.addSlotToContainer(new Slot(this.merchantInventory, 0, 36, 53));
		this.addSlotToContainer(new Slot(this.merchantInventory, 1, 62, 53));
		this.addSlotToContainer(
				new SlotMerchantResult(playerInventory.player, merchant, this.merchantInventory, 2, 120, 53));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
		}

	}

	public InventoryMerchant getMerchantInventory() {
		return this.merchantInventory;
	}

	public void onCraftGuiOpened(ICrafting icrafting) {
		super.onCraftGuiOpened(icrafting);
	}

	/**+
	 * Looks for changes made in the container, sends them to every
	 * listener.
	 */
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}

	/**+
	 * Callback for when the crafting matrix is changed.
	 */
	public void onCraftMatrixChanged(IInventory iinventory) {
		this.merchantInventory.resetRecipeAndSlots();
		super.onCraftMatrixChanged(iinventory);
	}

	public void setCurrentRecipeIndex(int currentRecipeIndex) {
		this.merchantInventory.setCurrentRecipeIndex(currentRecipeIndex);
	}

	public void updateProgressBar(int var1, int var2) {
	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return this.theMerchant.getCustomer() == entityplayer;
	}

	/**+
	 * Take a stack from the specified inventory slot.
	 */
	public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i == 2) {
				if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (i != 0 && i != 1) {
				if (i >= 3 && i < 30) {
					if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
						return null;
					}
				} else if (i >= 30 && i < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(entityplayer, itemstack1);
		}

		return itemstack;
	}

	/**+
	 * Called when the container is closed.
	 */
	public void onContainerClosed(EntityPlayer entityplayer) {
		super.onContainerClosed(entityplayer);
		this.theMerchant.setCustomer((EntityPlayer) null);
		super.onContainerClosed(entityplayer);
		if (!this.theWorld.isRemote) {
			ItemStack itemstack = this.merchantInventory.removeStackFromSlot(0);
			if (itemstack != null) {
				entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);
			}

			itemstack = this.merchantInventory.removeStackFromSlot(1);
			if (itemstack != null) {
				entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);
			}
		}
	}
}