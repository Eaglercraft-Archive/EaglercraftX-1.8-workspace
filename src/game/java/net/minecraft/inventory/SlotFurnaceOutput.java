package net.minecraft.inventory;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.AchievementList;
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
public class SlotFurnaceOutput extends Slot {
	private EntityPlayer thePlayer;
	private int field_75228_b;

	public SlotFurnaceOutput(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.thePlayer = player;
	}

	/**+
	 * Check if the stack is a valid item for this slot. Always true
	 * beside for the armor slots.
	 */
	public boolean isItemValid(ItemStack var1) {
		return false;
	}

	/**+
	 * Decrease the size of the stack in slot (first int arg) by the
	 * amount of the second int arg. Returns the new stack.
	 */
	public ItemStack decrStackSize(int i) {
		if (this.getHasStack()) {
			this.field_75228_b += Math.min(i, this.getStack().stackSize);
		}

		return super.decrStackSize(i);
	}

	public void onPickupFromSlot(EntityPlayer entityplayer, ItemStack itemstack) {
		this.onCrafting(itemstack);
		super.onPickupFromSlot(entityplayer, itemstack);
	}

	/**+
	 * the itemStack passed in is the output - ie, iron ingots, and
	 * pickaxes, not ore and wood. Typically increases an internal
	 * count then calls onCrafting(item).
	 */
	protected void onCrafting(ItemStack stack, int amount) {
		this.field_75228_b += amount;
		this.onCrafting(stack);
	}

	/**+
	 * the itemStack passed in is the output - ie, iron ingots, and
	 * pickaxes, not ore and wood. Typically increases an internal
	 * count then calls onCrafting(item).
	 */
	protected void onCrafting(ItemStack stack) {
		stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);
		if (!this.thePlayer.worldObj.isRemote) {
			int i = this.field_75228_b;
			float f = FurnaceRecipes.instance().getSmeltingExperience(stack);
			if (f == 0.0F) {
				i = 0;
			} else if (f < 1.0F) {
				int j = MathHelper.floor_float((float) i * f);
				if (j < MathHelper.ceiling_float_int((float) i * f)
						&& Math.random() < (double) ((float) i * f - (float) j)) {
					++j;
				}

				i = j;
			}

			while (i > 0) {
				int k = EntityXPOrb.getXPSplit(i);
				i -= k;
				this.thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX,
						this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, k));
			}
		}

		this.field_75228_b = 0;
		if (stack.getItem() == Items.iron_ingot) {
			this.thePlayer.triggerAchievement(AchievementList.acquireIron);
		}

		if (stack.getItem() == Items.cooked_fish) {
			this.thePlayer.triggerAchievement(AchievementList.cookFish);
		}

	}
}