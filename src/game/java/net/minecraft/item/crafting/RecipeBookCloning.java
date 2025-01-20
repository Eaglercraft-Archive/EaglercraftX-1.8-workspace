package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
public class RecipeBookCloning implements IRecipe {
	/**+
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(InventoryCrafting inventorycrafting, World var2) {
		int i = 0;
		ItemStack itemstack = null;

		for (int j = 0; j < inventorycrafting.getSizeInventory(); ++j) {
			ItemStack itemstack1 = inventorycrafting.getStackInSlot(j);
			if (itemstack1 != null) {
				if (itemstack1.getItem() == Items.written_book) {
					if (itemstack != null) {
						return false;
					}

					itemstack = itemstack1;
				} else {
					if (itemstack1.getItem() != Items.writable_book) {
						return false;
					}

					++i;
				}
			}
		}

		return itemstack != null && i > 0;
	}

	/**+
	 * Returns an Item that is the result of this recipe
	 */
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		int i = 0;
		ItemStack itemstack = null;

		for (int j = 0; j < inventorycrafting.getSizeInventory(); ++j) {
			ItemStack itemstack1 = inventorycrafting.getStackInSlot(j);
			if (itemstack1 != null) {
				if (itemstack1.getItem() == Items.written_book) {
					if (itemstack != null) {
						return null;
					}

					itemstack = itemstack1;
				} else {
					if (itemstack1.getItem() != Items.writable_book) {
						return null;
					}

					++i;
				}
			}
		}

		if (itemstack != null && i >= 1 && ItemEditableBook.getGeneration(itemstack) < 2) {
			ItemStack itemstack2 = new ItemStack(Items.written_book, i);
			itemstack2.setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
			itemstack2.getTagCompound().setInteger("generation", ItemEditableBook.getGeneration(itemstack) + 1);
			if (itemstack.hasDisplayName()) {
				itemstack2.setStackDisplayName(itemstack.getDisplayName());
			}

			return itemstack2;
		} else {
			return null;
		}
	}

	/**+
	 * Returns the size of the recipe area
	 */
	public int getRecipeSize() {
		return 9;
	}

	public ItemStack getRecipeOutput() {
		return null;
	}

	public ItemStack[] getRemainingItems(InventoryCrafting inventorycrafting) {
		ItemStack[] aitemstack = new ItemStack[inventorycrafting.getSizeInventory()];

		for (int i = 0; i < aitemstack.length; ++i) {
			ItemStack itemstack = inventorycrafting.getStackInSlot(i);
			if (itemstack != null && itemstack.getItem() instanceof ItemEditableBook) {
				aitemstack[i] = itemstack;
				break;
			}
		}

		return aitemstack;
	}
}