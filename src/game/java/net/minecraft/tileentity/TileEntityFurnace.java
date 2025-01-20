package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;

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
public class TileEntityFurnace extends TileEntityLockable implements ITickable, ISidedInventory {
	private static final int[] slotsTop = new int[] { 0 };
	private static final int[] slotsBottom = new int[] { 2, 1 };
	private static final int[] slotsSides = new int[] { 1 };
	/**+
	 * The ItemStacks that hold the items currently being used in
	 * the furnace
	 */
	private ItemStack[] furnaceItemStacks = new ItemStack[3];
	private int furnaceBurnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;
	private String furnaceCustomName;

	/**+
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}

	/**+
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int i) {
		return this.furnaceItemStacks[i];
	}

	/**+
	 * Removes up to a specified number of items from an inventory
	 * slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int i, int j) {
		if (this.furnaceItemStacks[i] != null) {
			if (this.furnaceItemStacks[i].stackSize <= j) {
				ItemStack itemstack1 = this.furnaceItemStacks[i];
				this.furnaceItemStacks[i] = null;
				return itemstack1;
			} else {
				ItemStack itemstack = this.furnaceItemStacks[i].splitStack(j);
				if (this.furnaceItemStacks[i].stackSize == 0) {
					this.furnaceItemStacks[i] = null;
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
		if (this.furnaceItemStacks[i] != null) {
			ItemStack itemstack = this.furnaceItemStacks[i];
			this.furnaceItemStacks[i] = null;
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
		boolean flag = itemstack != null && itemstack.isItemEqual(this.furnaceItemStacks[i])
				&& ItemStack.areItemStackTagsEqual(itemstack, this.furnaceItemStacks[i]);
		this.furnaceItemStacks[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

		if (i == 0 && !flag) {
			this.totalCookTime = this.getCookTime(itemstack);
			this.cookTime = 0;
			this.markDirty();
		}

	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return this.hasCustomName() ? this.furnaceCustomName : "container.furnace";
	}

	/**+
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName() {
		return this.furnaceCustomName != null && this.furnaceCustomName.length() > 0;
	}

	public void setCustomInventoryName(String parString1) {
		this.furnaceCustomName = parString1;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
		this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");
			if (b0 >= 0 && b0 < this.furnaceItemStacks.length) {
				this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		this.furnaceBurnTime = nbttagcompound.getShort("BurnTime");
		this.cookTime = nbttagcompound.getShort("CookTime");
		this.totalCookTime = nbttagcompound.getShort("CookTimeTotal");
		this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
		if (nbttagcompound.hasKey("CustomName", 8)) {
			this.furnaceCustomName = nbttagcompound.getString("CustomName");
		}

	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("BurnTime", (short) this.furnaceBurnTime);
		nbttagcompound.setShort("CookTime", (short) this.cookTime);
		nbttagcompound.setShort("CookTimeTotal", (short) this.totalCookTime);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
			if (this.furnaceItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
		if (this.hasCustomName()) {
			nbttagcompound.setString("CustomName", this.furnaceCustomName);
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
	 * Furnace isBurning
	 */
	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	/**+
	 * Furnace isBurning
	 */
	public static boolean isBurning(IInventory parIInventory) {
		return parIInventory.getField(0) > 0;
	}

	/**+
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		boolean flag = this.isBurning();
		boolean flag1 = false;
		if (this.isBurning()) {
			--this.furnaceBurnTime;
		}

		if (!this.worldObj.isRemote) {
			if (this.isBurning() || this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null) {
				if (!this.isBurning() && this.canSmelt()) {
					this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
					if (this.isBurning()) {
						flag1 = true;
						if (this.furnaceItemStacks[1] != null) {
							--this.furnaceItemStacks[1].stackSize;
							if (this.furnaceItemStacks[1].stackSize == 0) {
								Item item = this.furnaceItemStacks[1].getItem().getContainerItem();
								this.furnaceItemStacks[1] = item != null ? new ItemStack(item) : null;
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;
					if (this.cookTime == this.totalCookTime) {
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime(this.furnaceItemStacks[0]);
						this.smeltItem();
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
			}

			if (flag != this.isBurning()) {
				flag1 = true;
				BlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
			}
		}

		if (flag1) {
			this.markDirty();
		}

	}

	public int getCookTime(ItemStack stack) {
		return 200;
	}

	/**+
	 * Returns true if the furnace can smelt an item, i.e. has a
	 * source item, destination stack isn't full, etc.
	 */
	private boolean canSmelt() {
		if (this.furnaceItemStacks[0] == null) {
			return false;
		} else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
			return itemstack == null ? false
					: (this.furnaceItemStacks[2] == null ? true
							: (!this.furnaceItemStacks[2].isItemEqual(itemstack) ? false
									: (this.furnaceItemStacks[2].stackSize < this.getInventoryStackLimit()
											&& this.furnaceItemStacks[2].stackSize < this.furnaceItemStacks[2]
													.getMaxStackSize() ? true
															: this.furnaceItemStacks[2].stackSize < itemstack
																	.getMaxStackSize())));
		}
	}

	/**+
	 * Turn one item from the furnace source stack into the
	 * appropriate smelted item in the furnace result stack
	 */
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
			if (this.furnaceItemStacks[2] == null) {
				this.furnaceItemStacks[2] = itemstack.copy();
			} else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem()) {
				++this.furnaceItemStacks[2].stackSize;
			}

			if (this.furnaceItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.sponge)
					&& this.furnaceItemStacks[0].getMetadata() == 1 && this.furnaceItemStacks[1] != null
					&& this.furnaceItemStacks[1].getItem() == Items.bucket) {
				this.furnaceItemStacks[1] = new ItemStack(Items.water_bucket);
			}

			--this.furnaceItemStacks[0].stackSize;
			if (this.furnaceItemStacks[0].stackSize <= 0) {
				this.furnaceItemStacks[0] = null;
			}

		}
	}

	/**+
	 * Returns the number of ticks that the supplied fuel item will
	 * keep the furnace burning, or 0 if the item isn't fuel
	 */
	public static int getItemBurnTime(ItemStack parItemStack) {
		if (parItemStack == null) {
			return 0;
		} else {
			Item item = parItemStack.getItem();
			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
				Block block = Block.getBlockFromItem(item);
				if (block == Blocks.wooden_slab) {
					return 150;
				}

				if (block.getMaterial() == Material.wood) {
					return 300;
				}

				if (block == Blocks.coal_block) {
					return 16000;
				}
			}

			return item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD") ? 200
					: (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD") ? 200
							: (item instanceof ItemHoe && ((ItemHoe) item).getMaterialName().equals("WOOD") ? 200
									: (item == Items.stick ? 100
											: (item == Items.coal ? 1600
													: (item == Items.lava_bucket ? 20000
															: (item == Item.getItemFromBlock(Blocks.sapling) ? 100
																	: (item == Items.blaze_rod ? 2400 : 0)))))));
		}
	}

	public static boolean isItemFuel(ItemStack parItemStack) {
		/**+
		 * Returns the number of ticks that the supplied fuel item will
		 * keep the furnace burning, or 0 if the item isn't fuel
		 */
		return getItemBurnTime(parItemStack) > 0;
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
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return i == 2 ? false : (i != 1 ? true : isItemFuel(itemstack) || SlotFurnaceFuel.isBucket(itemstack));
	}

	public int[] getSlotsForFace(EnumFacing enumfacing) {
		return enumfacing == EnumFacing.DOWN ? slotsBottom : (enumfacing == EnumFacing.UP ? slotsTop : slotsSides);
	}

	/**+
	 * Returns true if automation can insert the given item in the
	 * given slot from the given side. Args: slot, item, side
	 */
	public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing var3) {
		return this.isItemValidForSlot(i, itemstack);
	}

	/**+
	 * Returns true if automation can extract the given item in the
	 * given slot from the given side. Args: slot, item, side
	 */
	public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing enumfacing) {
		if (enumfacing == EnumFacing.DOWN && i == 1) {
			Item item = itemstack.getItem();
			if (item != Items.water_bucket && item != Items.bucket) {
				return false;
			}
		}

		return true;
	}

	public String getGuiID() {
		return "minecraft:furnace";
	}

	public Container createContainer(InventoryPlayer inventoryplayer, EntityPlayer var2) {
		return new ContainerFurnace(inventoryplayer, this);
	}

	public int getField(int i) {
		switch (i) {
		case 0:
			return this.furnaceBurnTime;
		case 1:
			return this.currentItemBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		default:
			return 0;
		}
	}

	public void setField(int i, int j) {
		switch (i) {
		case 0:
			this.furnaceBurnTime = j;
			break;
		case 1:
			this.currentItemBurnTime = j;
			break;
		case 2:
			this.cookTime = j;
			break;
		case 3:
			this.totalCookTime = j;
		}

	}

	public int getFieldCount() {
		return 4;
	}

	public void clear() {
		for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
			this.furnaceItemStacks[i] = null;
		}

	}
}