package net.minecraft.item.crafting;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
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
public class RecipeRepairItem implements IRecipe {
	/**+
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(InventoryCrafting inventorycrafting, World var2) {
		ArrayList arraylist = Lists.newArrayList();

		for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
			ItemStack itemstack = inventorycrafting.getStackInSlot(i);
			if (itemstack != null) {
				arraylist.add(itemstack);
				if (arraylist.size() > 1) {
					ItemStack itemstack1 = (ItemStack) arraylist.get(0);
					if (itemstack.getItem() != itemstack1.getItem() || itemstack1.stackSize != 1
							|| itemstack.stackSize != 1 || !itemstack1.getItem().isDamageable()) {
						return false;
					}
				}
			}
		}

		return arraylist.size() == 2;
	}

	/**+
	 * Returns an Item that is the result of this recipe
	 */
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		ArrayList arraylist = Lists.newArrayList();

		for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
			ItemStack itemstack = inventorycrafting.getStackInSlot(i);
			if (itemstack != null) {
				arraylist.add(itemstack);
				if (arraylist.size() > 1) {
					ItemStack itemstack1 = (ItemStack) arraylist.get(0);
					if (itemstack.getItem() != itemstack1.getItem() || itemstack1.stackSize != 1
							|| itemstack.stackSize != 1 || !itemstack1.getItem().isDamageable()) {
						return null;
					}
				}
			}
		}

		if (arraylist.size() == 2) {
			ItemStack itemstack2 = (ItemStack) arraylist.get(0);
			ItemStack itemstack3 = (ItemStack) arraylist.get(1);
			if (itemstack2.getItem() == itemstack3.getItem() && itemstack2.stackSize == 1 && itemstack3.stackSize == 1
					&& itemstack2.getItem().isDamageable()) {
				Item item = itemstack2.getItem();
				int j = item.getMaxDamage() - itemstack2.getItemDamage();
				int k = item.getMaxDamage() - itemstack3.getItemDamage();
				int l = j + k + item.getMaxDamage() * 5 / 100;
				int i1 = item.getMaxDamage() - l;
				if (i1 < 0) {
					i1 = 0;
				}

				return new ItemStack(itemstack2.getItem(), 1, i1);
			}
		}

		return null;
	}

	/**+
	 * Returns the size of the recipe area
	 */
	public int getRecipeSize() {
		return 4;
	}

	public ItemStack getRecipeOutput() {
		return null;
	}

	public ItemStack[] getRemainingItems(InventoryCrafting inventorycrafting) {
		ItemStack[] aitemstack = new ItemStack[inventorycrafting.getSizeInventory()];

		for (int i = 0; i < aitemstack.length; ++i) {
			ItemStack itemstack = inventorycrafting.getStackInSlot(i);
			if (itemstack != null && itemstack.getItem().hasContainerItem()) {
				aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
			}
		}

		return aitemstack;
	}
}