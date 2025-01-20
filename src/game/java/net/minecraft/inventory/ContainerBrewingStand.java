package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

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
public class ContainerBrewingStand extends Container {
	private IInventory tileBrewingStand;
	private final Slot theSlot;
	private int brewTime;

	public ContainerBrewingStand(InventoryPlayer playerInventory, IInventory tileBrewingStandIn) {
		this.tileBrewingStand = tileBrewingStandIn;
		this.addSlotToContainer(
				new ContainerBrewingStand.Potion(playerInventory.player, tileBrewingStandIn, 0, 56, 46));
		this.addSlotToContainer(
				new ContainerBrewingStand.Potion(playerInventory.player, tileBrewingStandIn, 1, 79, 53));
		this.addSlotToContainer(
				new ContainerBrewingStand.Potion(playerInventory.player, tileBrewingStandIn, 2, 102, 46));
		this.theSlot = this.addSlotToContainer(new ContainerBrewingStand.Ingredient(tileBrewingStandIn, 3, 79, 17));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
		}

	}

	public void onCraftGuiOpened(ICrafting icrafting) {
		super.onCraftGuiOpened(icrafting);
		icrafting.func_175173_a(this, this.tileBrewingStand);
	}

	/**+
	 * Looks for changes made in the container, sends them to every
	 * listener.
	 */
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			if (this.brewTime != this.tileBrewingStand.getField(0)) {
				icrafting.sendProgressBarUpdate(this, 0, this.tileBrewingStand.getField(0));
			}
		}

		this.brewTime = this.tileBrewingStand.getField(0);
	}

	public void updateProgressBar(int i, int j) {
		this.tileBrewingStand.setField(i, j);
	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return this.tileBrewingStand.isUseableByPlayer(entityplayer);
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
			if ((i < 0 || i > 2) && i != 3) {
				if (!this.theSlot.getHasStack() && this.theSlot.isItemValid(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 3, 4, false)) {
						return null;
					}
				} else if (ContainerBrewingStand.Potion.canHoldPotion(itemstack)) {
					if (!this.mergeItemStack(itemstack1, 0, 3, false)) {
						return null;
					}
				} else if (i >= 4 && i < 31) {
					if (!this.mergeItemStack(itemstack1, 31, 40, false)) {
						return null;
					}
				} else if (i >= 31 && i < 40) {
					if (!this.mergeItemStack(itemstack1, 4, 31, false)) {
						return null;
					}
				} else if (!this.mergeItemStack(itemstack1, 4, 40, false)) {
					return null;
				}
			} else {
				if (!this.mergeItemStack(itemstack1, 4, 40, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
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

	class Ingredient extends Slot {
		public Ingredient(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		public boolean isItemValid(ItemStack itemstack) {
			return itemstack != null ? itemstack.getItem().isPotionIngredient(itemstack) : false;
		}

		public int getSlotStackLimit() {
			return 64;
		}
	}

	static class Potion extends Slot {
		private EntityPlayer player;

		public Potion(EntityPlayer playerIn, IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
			this.player = playerIn;
		}

		public boolean isItemValid(ItemStack itemstack) {
			return canHoldPotion(itemstack);
		}

		public int getSlotStackLimit() {
			return 1;
		}

		public void onPickupFromSlot(EntityPlayer entityplayer, ItemStack itemstack) {
			if (itemstack.getItem() == Items.potionitem && itemstack.getMetadata() > 0) {
				this.player.triggerAchievement(AchievementList.potion);
			}

			super.onPickupFromSlot(entityplayer, itemstack);
		}

		public static boolean canHoldPotion(ItemStack parItemStack) {
			return parItemStack != null
					&& (parItemStack.getItem() == Items.potionitem || parItemStack.getItem() == Items.glass_bottle);
		}
	}
}