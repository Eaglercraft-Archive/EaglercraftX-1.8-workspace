package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

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
public class RecipesMapExtending extends ShapedRecipes {
	public RecipesMapExtending() {
		super(3, 3,
				new ItemStack[] { new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper),
						new ItemStack(Items.paper), new ItemStack(Items.filled_map, 0, 32767),
						new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper),
						new ItemStack(Items.paper) },
				new ItemStack(Items.map, 0, 0));
	}

	/**+
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(InventoryCrafting inventorycrafting, World world) {
		if (!super.matches(inventorycrafting, world)) {
			return false;
		} else {
			ItemStack itemstack = null;

			for (int i = 0; i < inventorycrafting.getSizeInventory() && itemstack == null; ++i) {
				ItemStack itemstack1 = inventorycrafting.getStackInSlot(i);
				if (itemstack1 != null && itemstack1.getItem() == Items.filled_map) {
					itemstack = itemstack1;
				}
			}

			if (itemstack == null) {
				return false;
			} else {
				MapData mapdata = Items.filled_map.getMapData(itemstack, world);
				return mapdata == null ? false : mapdata.scale < 4;
			}
		}
	}

	/**+
	 * Returns an Item that is the result of this recipe
	 */
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		ItemStack itemstack = null;

		for (int i = 0; i < inventorycrafting.getSizeInventory() && itemstack == null; ++i) {
			ItemStack itemstack1 = inventorycrafting.getStackInSlot(i);
			if (itemstack1 != null && itemstack1.getItem() == Items.filled_map) {
				itemstack = itemstack1;
			}
		}

		itemstack = itemstack.copy();
		itemstack.stackSize = 1;
		if (itemstack.getTagCompound() == null) {
			itemstack.setTagCompound(new NBTTagCompound());
		}

		itemstack.getTagCompound().setBoolean("map_is_scaling", true);
		return itemstack;
	}
}