package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.BlockDispenser;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.BlockSourceImpl;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.dispenser.IBehaviorDispenseItem;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.inventory.IInventory;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemStack;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.tileentity.TileEntity;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.tileentity.TileEntityDispenser;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.tileentity.TileEntityDropper;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.tileentity.TileEntityHopper;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.BlockPos;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.EnumFacing;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.World;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class BlockDropper extends BlockDispenser {

	static {
		__checkIntegratedContextValid("net/minecraft/block/BlockDropper");
	}

	private final IBehaviorDispenseItem dropBehavior = new BehaviorDefaultDispenseItem();

	protected IBehaviorDispenseItem getBehavior(ItemStack var1) {
		return this.dropBehavior;
	}

	/**+
	 * Returns a new instance of a block's tile entity class. Called
	 * on placing the block.
	 */
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityDropper();
	}

	protected void dispense(World world, BlockPos blockpos) {
		BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, blockpos);
		TileEntityDispenser tileentitydispenser = (TileEntityDispenser) blocksourceimpl.getBlockTileEntity();
		if (tileentitydispenser != null) {
			int i = tileentitydispenser.getDispenseSlot();
			if (i < 0) {
				world.playAuxSFX(1001, blockpos, 0);
			} else {
				ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
				if (itemstack != null) {
					EnumFacing enumfacing = (EnumFacing) world.getBlockState(blockpos).getValue(FACING);
					BlockPos blockpos1 = blockpos.offset(enumfacing);
					IInventory iinventory = TileEntityHopper.getInventoryAtPosition(world, (double) blockpos1.getX(),
							(double) blockpos1.getY(), (double) blockpos1.getZ());
					ItemStack itemstack1;
					if (iinventory == null) {
						itemstack1 = this.dropBehavior.dispense(blocksourceimpl, itemstack);
						if (itemstack1 != null && itemstack1.stackSize <= 0) {
							itemstack1 = null;
						}
					} else {
						itemstack1 = TileEntityHopper.putStackInInventoryAllSlots(iinventory,
								itemstack.copy().splitStack(1), enumfacing.getOpposite());
						if (itemstack1 == null) {
							itemstack1 = itemstack.copy();
							if (--itemstack1.stackSize <= 0) {
								itemstack1 = null;
							}
						} else {
							itemstack1 = itemstack.copy();
						}
					}

					tileentitydispenser.setInventorySlotContents(i, itemstack1);
				}
			}
		}
	}
}