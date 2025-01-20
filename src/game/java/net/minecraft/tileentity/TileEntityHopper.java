package net.minecraft.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
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
public class TileEntityHopper extends TileEntityLockable implements IHopper, ITickable {
	private ItemStack[] inventory = new ItemStack[5];
	private String customName;
	private int transferCooldown = -1;

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
		this.inventory = new ItemStack[this.getSizeInventory()];
		if (nbttagcompound.hasKey("CustomName", 8)) {
			this.customName = nbttagcompound.getString("CustomName");
		}

		this.transferCooldown = nbttagcompound.getInteger("TransferCooldown");

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");
			if (b0 >= 0 && b0 < this.inventory.length) {
				this.inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.inventory.length; ++i) {
			if (this.inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
		nbttagcompound.setInteger("TransferCooldown", this.transferCooldown);
		if (this.hasCustomName()) {
			nbttagcompound.setString("CustomName", this.customName);
		}

	}

	/**+
	 * For tile entities, ensures the chunk containing the tile
	 * entity is saved to disk later - the game won't think it
	 * hasn't changed and skip it.
	 */
	public void markDirty() {
		super.markDirty();
	}

	/**+
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return this.inventory.length;
	}

	/**+
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int i) {
		return this.inventory[i];
	}

	/**+
	 * Removes up to a specified number of items from an inventory
	 * slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int i, int j) {
		if (this.inventory[i] != null) {
			if (this.inventory[i].stackSize <= j) {
				ItemStack itemstack1 = this.inventory[i];
				this.inventory[i] = null;
				return itemstack1;
			} else {
				ItemStack itemstack = this.inventory[i].splitStack(j);
				if (this.inventory[i].stackSize == 0) {
					this.inventory[i] = null;
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
		if (this.inventory[i] != null) {
			ItemStack itemstack = this.inventory[i];
			this.inventory[i] = null;
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
		this.inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.hopper";
	}

	/**+
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName() {
		return this.customName != null && this.customName.length() > 0;
	}

	public void setCustomName(String customNameIn) {
		this.customName = customNameIn;
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

	/**+
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		if (this.worldObj != null && !this.worldObj.isRemote) {
			--this.transferCooldown;
			if (!this.isOnTransferCooldown()) {
				this.setTransferCooldown(0);
				this.updateHopper();
			}

		}
	}

	public boolean updateHopper() {
		if (this.worldObj != null && !this.worldObj.isRemote) {
			if (!this.isOnTransferCooldown() && BlockHopper.isEnabled(this.getBlockMetadata())) {
				boolean flag = false;
				if (!this.isEmpty()) {
					flag = this.transferItemsOut();
				}

				if (!this.isFull()) {
					flag = captureDroppedItems(this) || flag;
				}

				if (flag) {
					this.setTransferCooldown(8);
					this.markDirty();
					return true;
				}
			}

			return false;
		} else {
			return false;
		}
	}

	private boolean isEmpty() {
		for (int i = 0; i < this.inventory.length; ++i) {
			if (this.inventory[i] != null) {
				return false;
			}
		}

		return true;
	}

	private boolean isFull() {
		for (int i = 0; i < this.inventory.length; ++i) {
			ItemStack itemstack = this.inventory[i];
			if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize()) {
				return false;
			}
		}

		return true;
	}

	private boolean transferItemsOut() {
		IInventory iinventory = this.getInventoryForHopperTransfer();
		if (iinventory == null) {
			return false;
		} else {
			EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();
			if (this.isInventoryFull(iinventory, enumfacing)) {
				return false;
			} else {
				for (int i = 0; i < this.getSizeInventory(); ++i) {
					if (this.getStackInSlot(i) != null) {
						ItemStack itemstack = this.getStackInSlot(i).copy();
						ItemStack itemstack1 = putStackInInventoryAllSlots(iinventory, this.decrStackSize(i, 1),
								enumfacing);
						if (itemstack1 == null || itemstack1.stackSize == 0) {
							iinventory.markDirty();
							return true;
						}

						this.setInventorySlotContents(i, itemstack);
					}
				}

				return false;
			}
		}
	}

	/**+
	 * Returns false if the inventory has any room to place items in
	 */
	private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side) {
		if (inventoryIn instanceof ISidedInventory) {
			ISidedInventory isidedinventory = (ISidedInventory) inventoryIn;
			int[] aint = isidedinventory.getSlotsForFace(side);

			for (int k = 0; k < aint.length; ++k) {
				ItemStack itemstack1 = isidedinventory.getStackInSlot(aint[k]);
				if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize()) {
					return false;
				}
			}
		} else {
			int i = inventoryIn.getSizeInventory();

			for (int j = 0; j < i; ++j) {
				ItemStack itemstack = inventoryIn.getStackInSlot(j);
				if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize()) {
					return false;
				}
			}
		}

		return true;
	}

	/**+
	 * Returns false if the specified IInventory contains any items
	 */
	private static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side) {
		if (inventoryIn instanceof ISidedInventory) {
			ISidedInventory isidedinventory = (ISidedInventory) inventoryIn;
			int[] aint = isidedinventory.getSlotsForFace(side);

			for (int i = 0; i < aint.length; ++i) {
				if (isidedinventory.getStackInSlot(aint[i]) != null) {
					return false;
				}
			}
		} else {
			int j = inventoryIn.getSizeInventory();

			for (int k = 0; k < j; ++k) {
				if (inventoryIn.getStackInSlot(k) != null) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean captureDroppedItems(IHopper parIHopper) {
		IInventory iinventory = getHopperInventory(parIHopper);
		if (iinventory != null) {
			EnumFacing enumfacing = EnumFacing.DOWN;
			if (isInventoryEmpty(iinventory, enumfacing)) {
				return false;
			}

			if (iinventory instanceof ISidedInventory) {
				ISidedInventory isidedinventory = (ISidedInventory) iinventory;
				int[] aint = isidedinventory.getSlotsForFace(enumfacing);

				for (int i = 0; i < aint.length; ++i) {
					if (pullItemFromSlot(parIHopper, iinventory, aint[i], enumfacing)) {
						return true;
					}
				}
			} else {
				int j = iinventory.getSizeInventory();

				for (int k = 0; k < j; ++k) {
					if (pullItemFromSlot(parIHopper, iinventory, k, enumfacing)) {
						return true;
					}
				}
			}
		} else {
			List<EntityItem> list = func_181556_a(parIHopper.getWorld(), parIHopper.getXPos(),
					parIHopper.getYPos() + 1.0D, parIHopper.getZPos());
			for (int i = 0, l = list.size(); i < l; ++i) {
				if (putDropInInventoryAllSlots(parIHopper, list.get(i))) {
					return true;
				}
			}
		}

		return false;
	}

	/**+
	 * Pulls from the specified slot in the inventory and places in
	 * any available slot in the hopper. Returns true if the entire
	 * stack was moved
	 */
	private static boolean pullItemFromSlot(IHopper hopper, IInventory inventoryIn, int index, EnumFacing direction) {
		ItemStack itemstack = inventoryIn.getStackInSlot(index);
		if (itemstack != null && canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) {
			ItemStack itemstack1 = itemstack.copy();
			ItemStack itemstack2 = putStackInInventoryAllSlots(hopper, inventoryIn.decrStackSize(index, 1),
					(EnumFacing) null);
			if (itemstack2 == null || itemstack2.stackSize == 0) {
				inventoryIn.markDirty();
				return true;
			}

			inventoryIn.setInventorySlotContents(index, itemstack1);
		}

		return false;
	}

	/**+
	 * Attempts to place the passed EntityItem's stack into the
	 * inventory using as many slots as possible. Returns false if
	 * the stackSize of the drop was not depleted.
	 */
	public static boolean putDropInInventoryAllSlots(IInventory itemIn, EntityItem parEntityItem) {
		boolean flag = false;
		if (parEntityItem == null) {
			return false;
		} else {
			ItemStack itemstack = parEntityItem.getEntityItem().copy();
			ItemStack itemstack1 = putStackInInventoryAllSlots(itemIn, itemstack, (EnumFacing) null);
			if (itemstack1 != null && itemstack1.stackSize != 0) {
				parEntityItem.setEntityItemStack(itemstack1);
			} else {
				flag = true;
				parEntityItem.setDead();
			}

			return flag;
		}
	}

	/**+
	 * Attempts to place the passed stack in the inventory, using as
	 * many slots as required. Returns leftover items
	 */
	public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, ItemStack stack, EnumFacing side) {
		if (inventoryIn instanceof ISidedInventory && side != null) {
			ISidedInventory isidedinventory = (ISidedInventory) inventoryIn;
			int[] aint = isidedinventory.getSlotsForFace(side);

			for (int k = 0; k < aint.length && stack != null && stack.stackSize > 0; ++k) {
				stack = insertStack(inventoryIn, stack, aint[k], side);
			}
		} else {
			int i = inventoryIn.getSizeInventory();

			for (int j = 0; j < i && stack != null && stack.stackSize > 0; ++j) {
				stack = insertStack(inventoryIn, stack, j, side);
			}
		}

		if (stack != null && stack.stackSize == 0) {
			stack = null;
		}

		return stack;
	}

	/**+
	 * Can this hopper insert the specified item from the specified
	 * slot on the specified side?
	 */
	private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
		return !inventoryIn.isItemValidForSlot(index, stack) ? false
				: !(inventoryIn instanceof ISidedInventory)
						|| ((ISidedInventory) inventoryIn).canInsertItem(index, stack, side);
	}

	/**+
	 * Can this hopper extract the specified item from the specified
	 * slot on the specified side?
	 */
	private static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
		return !(inventoryIn instanceof ISidedInventory)
				|| ((ISidedInventory) inventoryIn).canExtractItem(index, stack, side);
	}

	/**+
	 * Insert the specified stack to the specified inventory and
	 * return any leftover items
	 */
	private static ItemStack insertStack(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
		ItemStack itemstack = inventoryIn.getStackInSlot(index);
		if (canInsertItemInSlot(inventoryIn, stack, index, side)) {
			boolean flag = false;
			if (itemstack == null) {
				inventoryIn.setInventorySlotContents(index, stack);
				stack = null;
				flag = true;
			} else if (canCombine(itemstack, stack)) {
				int i = stack.getMaxStackSize() - itemstack.stackSize;
				int j = Math.min(stack.stackSize, i);
				stack.stackSize -= j;
				itemstack.stackSize += j;
				flag = j > 0;
			}

			if (flag) {
				if (inventoryIn instanceof TileEntityHopper) {
					TileEntityHopper tileentityhopper = (TileEntityHopper) inventoryIn;
					if (tileentityhopper.mayTransfer()) {
						tileentityhopper.setTransferCooldown(8);
					}

					inventoryIn.markDirty();
				}

				inventoryIn.markDirty();
			}
		}

		return stack;
	}

	/**+
	 * Returns the IInventory that this hopper is pointing into
	 */
	private IInventory getInventoryForHopperTransfer() {
		EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata());
		/**+
		 * Returns the IInventory (if applicable) of the TileEntity at
		 * the specified position
		 */
		return getInventoryAtPosition(this.getWorld(), (double) (this.pos.getX() + enumfacing.getFrontOffsetX()),
				(double) (this.pos.getY() + enumfacing.getFrontOffsetY()),
				(double) (this.pos.getZ() + enumfacing.getFrontOffsetZ()));
	}

	/**+
	 * Returns the IInventory for the specified hopper
	 */
	public static IInventory getHopperInventory(IHopper hopper) {
		/**+
		 * Returns the IInventory (if applicable) of the TileEntity at
		 * the specified position
		 */
		return getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0D, hopper.getZPos());
	}

	public static List<EntityItem> func_181556_a(World parWorld, double parDouble1, double parDouble2,
			double parDouble3) {
		return parWorld
				.<EntityItem>getEntitiesWithinAABB(
						EntityItem.class, new AxisAlignedBB(parDouble1 - 0.5D, parDouble2 - 0.5D, parDouble3 - 0.5D,
								parDouble1 + 0.5D, parDouble2 + 0.5D, parDouble3 + 0.5D),
						EntitySelectors.selectAnything);
	}

	/**+
	 * Returns the IInventory (if applicable) of the TileEntity at
	 * the specified position
	 */
	public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z) {
		Object object = null;
		int i = MathHelper.floor_double(x);
		int j = MathHelper.floor_double(y);
		int k = MathHelper.floor_double(z);
		BlockPos blockpos = new BlockPos(i, j, k);
		Block block = worldIn.getBlockState(blockpos).getBlock();
		if (block.hasTileEntity()) {
			TileEntity tileentity = worldIn.getTileEntity(blockpos);
			if (tileentity instanceof IInventory) {
				object = (IInventory) tileentity;
				if (object instanceof TileEntityChest && block instanceof BlockChest) {
					object = ((BlockChest) block).getLockableContainer(worldIn, blockpos);
				}
			}
		}

		if (object == null) {
			List list = worldIn.getEntitiesInAABBexcluding((Entity) null,
					new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D),
					EntitySelectors.selectInventories);
			if (list.size() > 0) {
				object = (IInventory) list.get(worldIn.rand.nextInt(list.size()));
			}
		}

		return (IInventory) object;
	}

	private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem() != stack2.getItem() ? false
				: (stack1.getMetadata() != stack2.getMetadata() ? false
						: (stack1.stackSize > stack1.getMaxStackSize() ? false
								: ItemStack.areItemStackTagsEqual(stack1, stack2)));
	}

	/**+
	 * Gets the world X position for this hopper entity.
	 */
	public double getXPos() {
		return (double) this.pos.getX() + 0.5D;
	}

	/**+
	 * Gets the world Y position for this hopper entity.
	 */
	public double getYPos() {
		return (double) this.pos.getY() + 0.5D;
	}

	/**+
	 * Gets the world Z position for this hopper entity.
	 */
	public double getZPos() {
		return (double) this.pos.getZ() + 0.5D;
	}

	public void setTransferCooldown(int ticks) {
		this.transferCooldown = ticks;
	}

	public boolean isOnTransferCooldown() {
		return this.transferCooldown > 0;
	}

	public boolean mayTransfer() {
		return this.transferCooldown <= 1;
	}

	public String getGuiID() {
		return "minecraft:hopper";
	}

	public Container createContainer(InventoryPlayer inventoryplayer, EntityPlayer entityplayer) {
		return new ContainerHopper(inventoryplayer, this, entityplayer);
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
		for (int i = 0; i < this.inventory.length; ++i) {
			this.inventory[i] = null;
		}

	}
}