package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
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
public class EntityAIHarvestFarmland extends EntityAIMoveToBlock {
	private final EntityVillager theVillager;
	private boolean hasFarmItem;
	private boolean field_179503_e;
	private int field_179501_f;

	public EntityAIHarvestFarmland(EntityVillager theVillagerIn, double speedIn) {
		super(theVillagerIn, speedIn, 16);
		this.theVillager = theVillagerIn;
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.runDelay <= 0) {
			if (!this.theVillager.worldObj.getGameRules().getBoolean("mobGriefing")) {
				return false;
			}

			this.field_179501_f = -1;
			this.hasFarmItem = this.theVillager.isFarmItemInInventory();
			this.field_179503_e = this.theVillager.func_175557_cr();
		}

		return super.shouldExecute();
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return this.field_179501_f >= 0 && super.continueExecuting();
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		super.startExecuting();
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		super.resetTask();
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		super.updateTask();
		this.theVillager.getLookHelper().setLookPosition((double) this.destinationBlock.getX() + 0.5D,
				(double) (this.destinationBlock.getY() + 1), (double) this.destinationBlock.getZ() + 0.5D, 10.0F,
				(float) this.theVillager.getVerticalFaceSpeed());
		if (this.getIsAboveDestination()) {
			World world = this.theVillager.worldObj;
			BlockPos blockpos = this.destinationBlock.up();
			IBlockState iblockstate = world.getBlockState(blockpos);
			Block block = iblockstate.getBlock();
			if (this.field_179501_f == 0 && block instanceof BlockCrops
					&& ((Integer) iblockstate.getValue(BlockCrops.AGE)).intValue() == 7) {
				world.destroyBlock(blockpos, true);
			} else if (this.field_179501_f == 1 && block == Blocks.air) {
				InventoryBasic inventorybasic = this.theVillager.getVillagerInventory();

				for (int i = 0; i < inventorybasic.getSizeInventory(); ++i) {
					ItemStack itemstack = inventorybasic.getStackInSlot(i);
					boolean flag = false;
					if (itemstack != null) {
						if (itemstack.getItem() == Items.wheat_seeds) {
							world.setBlockState(blockpos, Blocks.wheat.getDefaultState(), 3);
							flag = true;
						} else if (itemstack.getItem() == Items.potato) {
							world.setBlockState(blockpos, Blocks.potatoes.getDefaultState(), 3);
							flag = true;
						} else if (itemstack.getItem() == Items.carrot) {
							world.setBlockState(blockpos, Blocks.carrots.getDefaultState(), 3);
							flag = true;
						}
					}

					if (flag) {
						--itemstack.stackSize;
						if (itemstack.stackSize <= 0) {
							inventorybasic.setInventorySlotContents(i, (ItemStack) null);
						}
						break;
					}
				}
			}

			this.field_179501_f = -1;
			this.runDelay = 10;
		}

	}

	/**+
	 * Return true to set given position as destination
	 */
	protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos).getBlock();
		if (block == Blocks.farmland) {
			pos = pos.up();
			IBlockState iblockstate = worldIn.getBlockState(pos);
			block = iblockstate.getBlock();
			if (block instanceof BlockCrops && ((Integer) iblockstate.getValue(BlockCrops.AGE)).intValue() == 7
					&& this.field_179503_e && (this.field_179501_f == 0 || this.field_179501_f < 0)) {
				this.field_179501_f = 0;
				return true;
			}

			if (block == Blocks.air && this.hasFarmItem && (this.field_179501_f == 1 || this.field_179501_f < 0)) {
				this.field_179501_f = 1;
				return true;
			}
		}

		return false;
	}
}