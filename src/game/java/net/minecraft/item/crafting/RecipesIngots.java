package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

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
public class RecipesIngots {
	private Object[][] recipeItems;

	/**+
	 * Adds the ingot recipes to the CraftingManager.
	 */
	public void addRecipes(CraftingManager parCraftingManager) {
		recipeItems = new Object[][] { { Blocks.gold_block, new ItemStack(Items.gold_ingot, 9) },
				{ Blocks.iron_block, new ItemStack(Items.iron_ingot, 9) },
				{ Blocks.diamond_block, new ItemStack(Items.diamond, 9) },
				{ Blocks.emerald_block, new ItemStack(Items.emerald, 9) },
				{ Blocks.lapis_block, new ItemStack(Items.dye, 9, EnumDyeColor.BLUE.getDyeDamage()) },
				{ Blocks.redstone_block, new ItemStack(Items.redstone, 9) },
				{ Blocks.coal_block, new ItemStack(Items.coal, 9, 0) },
				{ Blocks.hay_block, new ItemStack(Items.wheat, 9) },
				{ Blocks.slime_block, new ItemStack(Items.slime_ball, 9) } };
		for (int i = 0; i < this.recipeItems.length; ++i) {
			Block block = (Block) this.recipeItems[i][0];
			ItemStack itemstack = (ItemStack) this.recipeItems[i][1];
			parCraftingManager.addRecipe(new ItemStack(block),
					new Object[] { "###", "###", "###", Character.valueOf('#'), itemstack });
			parCraftingManager.addRecipe(itemstack, new Object[] { "#", Character.valueOf('#'), block });
		}

		parCraftingManager.addRecipe(new ItemStack(Items.gold_ingot),
				new Object[] { "###", "###", "###", Character.valueOf('#'), Items.gold_nugget });
		parCraftingManager.addRecipe(new ItemStack(Items.gold_nugget, 9),
				new Object[] { "#", Character.valueOf('#'), Items.gold_ingot });
	}
}