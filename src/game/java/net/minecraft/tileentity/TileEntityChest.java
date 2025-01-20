package net.minecraft.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

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
public class TileEntityChest extends TileEntityLockable implements ITickable, IInventory {
	private ItemStack[] chestContents = new ItemStack[27];
	public boolean adjacentChestChecked;
	public TileEntityChest adjacentChestZNeg;
	public TileEntityChest adjacentChestXPos;
	public TileEntityChest adjacentChestXNeg;
	public TileEntityChest adjacentChestZPos;
	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;
	private int ticksSinceSync;
	private int cachedChestType;
	private String customName;

	public TileEntityChest() {
		this.cachedChestType = -1;
	}

	public TileEntityChest(int chestType) {
		this.cachedChestType = chestType;
	}

	/**+
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return 27;
	}

	/**+
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int i) {
		return this.chestContents[i];
	}

	/**+
	 * Removes up to a specified number of items from an inventory
	 * slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int i, int j) {
		if (this.chestContents[i] != null) {
			if (this.chestContents[i].stackSize <= j) {
				ItemStack itemstack1 = this.chestContents[i];
				this.chestContents[i] = null;
				this.markDirty();
				return itemstack1;
			} else {
				ItemStack itemstack = this.chestContents[i].splitStack(j);
				if (this.chestContents[i].stackSize == 0) {
					this.chestContents[i] = null;
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
		if (this.chestContents[i] != null) {
			ItemStack itemstack = this.chestContents[i];
			this.chestContents[i] = null;
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
		this.chestContents[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.chest";
	}

	/**+
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName() {
		return this.customName != null && this.customName.length() > 0;
	}

	public void setCustomName(String name) {
		this.customName = name;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
		this.chestContents = new ItemStack[this.getSizeInventory()];
		if (nbttagcompound.hasKey("CustomName", 8)) {
			this.customName = nbttagcompound.getString("CustomName");
		}

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			if (j >= 0 && j < this.chestContents.length) {
				this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.chestContents.length; ++i) {
			if (this.chestContents[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.chestContents[i].writeToNBT(nbttagcompound1);
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

	public void updateContainingBlockInfo() {
		super.updateContainingBlockInfo();
		this.adjacentChestChecked = false;
	}

	private void func_174910_a(TileEntityChest chestTe, EnumFacing side) {
		if (chestTe.isInvalid()) {
			this.adjacentChestChecked = false;
		} else if (this.adjacentChestChecked) {
			switch (side) {
			case NORTH:
				if (this.adjacentChestZNeg != chestTe) {
					this.adjacentChestChecked = false;
				}
				break;
			case SOUTH:
				if (this.adjacentChestZPos != chestTe) {
					this.adjacentChestChecked = false;
				}
				break;
			case EAST:
				if (this.adjacentChestXPos != chestTe) {
					this.adjacentChestChecked = false;
				}
				break;
			case WEST:
				if (this.adjacentChestXNeg != chestTe) {
					this.adjacentChestChecked = false;
				}
			}
		}

	}

	/**+
	 * Performs the check for adjacent chests to determine if this
	 * chest is double or not.
	 */
	public void checkForAdjacentChests() {
		if (!this.adjacentChestChecked) {
			this.adjacentChestChecked = true;
			this.adjacentChestXNeg = this.getAdjacentChest(EnumFacing.WEST);
			this.adjacentChestXPos = this.getAdjacentChest(EnumFacing.EAST);
			this.adjacentChestZNeg = this.getAdjacentChest(EnumFacing.NORTH);
			this.adjacentChestZPos = this.getAdjacentChest(EnumFacing.SOUTH);
		}
	}

	protected TileEntityChest getAdjacentChest(EnumFacing side) {
		BlockPos blockpos = this.pos.offset(side);
		if (this.isChestAt(blockpos)) {
			TileEntity tileentity = this.worldObj.getTileEntity(blockpos);
			if (tileentity instanceof TileEntityChest) {
				TileEntityChest tileentitychest = (TileEntityChest) tileentity;
				tileentitychest.func_174910_a(this, side.getOpposite());
				return tileentitychest;
			}
		}

		return null;
	}

	private boolean isChestAt(BlockPos posIn) {
		if (this.worldObj == null) {
			return false;
		} else {
			Block block = this.worldObj.getBlockState(posIn).getBlock();
			return block instanceof BlockChest && ((BlockChest) block).chestType == this.getChestType();
		}
	}

	/**+
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		this.checkForAdjacentChests();
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		++this.ticksSinceSync;
		if (!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
			this.numPlayersUsing = 0;
			float f = 5.0F;

			List<EntityPlayer> players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class,
					new AxisAlignedBB((double) ((float) i - f), (double) ((float) j - f), (double) ((float) k - f),
							(double) ((float) (i + 1) + f), (double) ((float) (j + 1) + f),
							(double) ((float) (k + 1) + f)));
			for (int m = 0, l = players.size(); m < l; ++m) {
				EntityPlayer entityplayer = players.get(m);
				if (entityplayer.openContainer instanceof ContainerChest) {
					IInventory iinventory = ((ContainerChest) entityplayer.openContainer).getLowerChestInventory();
					if (iinventory == this || iinventory instanceof InventoryLargeChest
							&& ((InventoryLargeChest) iinventory).isPartOfLargeChest(this)) {
						++this.numPlayersUsing;
					}
				}
			}
		}

		this.prevLidAngle = this.lidAngle;
		float f1 = 0.1F;
		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null
				&& this.adjacentChestXNeg == null) {
			double d1 = (double) i + 0.5D;
			double d2 = (double) k + 0.5D;
			if (this.adjacentChestZPos != null) {
				d2 += 0.5D;
			}

			if (this.adjacentChestXPos != null) {
				d1 += 0.5D;
			}

			this.worldObj.playSoundEffect(d1, (double) j + 0.5D, d2, "random.chestopen", 0.5F,
					this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
			float f2 = this.lidAngle;
			if (this.numPlayersUsing > 0) {
				this.lidAngle += f1;
			} else {
				this.lidAngle -= f1;
			}

			if (this.lidAngle > 1.0F) {
				this.lidAngle = 1.0F;
			}

			float f3 = 0.5F;
			if (this.lidAngle < f3 && f2 >= f3 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
				double d3 = (double) i + 0.5D;
				double d0 = (double) k + 0.5D;
				if (this.adjacentChestZPos != null) {
					d0 += 0.5D;
				}

				if (this.adjacentChestXPos != null) {
					d3 += 0.5D;
				}

				this.worldObj.playSoundEffect(d3, (double) j + 0.5D, d0, "random.chestclosed", 0.5F,
						this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}

	}

	public boolean receiveClientEvent(int i, int j) {
		if (i == 1) {
			this.numPlayersUsing = j;
			return true;
		} else {
			return super.receiveClientEvent(i, j);
		}
	}

	public void openInventory(EntityPlayer entityplayer) {
		if (!entityplayer.isSpectator()) {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}

			++this.numPlayersUsing;
			this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
			this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
		}

	}

	public void closeInventory(EntityPlayer entityplayer) {
		if (!entityplayer.isSpectator() && this.getBlockType() instanceof BlockChest) {
			--this.numPlayersUsing;
			this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
			this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
		}

	}

	/**+
	 * Returns true if automation is allowed to insert the given
	 * stack (ignoring stack size) into the given slot.
	 */
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return true;
	}

	/**+
	 * invalidates a tile entity
	 */
	public void invalidate() {
		super.invalidate();
		this.updateContainingBlockInfo();
		this.checkForAdjacentChests();
	}

	public int getChestType() {
		if (this.cachedChestType == -1) {
			if (this.worldObj == null || !(this.getBlockType() instanceof BlockChest)) {
				return 0;
			}

			this.cachedChestType = ((BlockChest) this.getBlockType()).chestType;
		}

		return this.cachedChestType;
	}

	public String getGuiID() {
		return "minecraft:chest";
	}

	public Container createContainer(InventoryPlayer inventoryplayer, EntityPlayer entityplayer) {
		return new ContainerChest(inventoryplayer, this, entityplayer);
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
		for (int i = 0; i < this.chestContents.length; ++i) {
			this.chestContents[i] = null;
		}

	}
}