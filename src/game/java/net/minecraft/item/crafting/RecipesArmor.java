package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
public class RecipesArmor {
	private String[][] recipePatterns = new String[][] { { "XXX", "X X" }, { "X X", "XXX", "XXX" },
			{ "XXX", "X X", "X X" }, { "X X", "X X" } };
	private Item[][] recipeItems = new Item[][] { { Items.leather, Items.iron_ingot, Items.diamond, Items.gold_ingot },
			{ Items.leather_helmet, Items.iron_helmet, Items.diamond_helmet, Items.golden_helmet },
			{ Items.leather_chestplate, Items.iron_chestplate, Items.diamond_chestplate, Items.golden_chestplate },
			{ Items.leather_leggings, Items.iron_leggings, Items.diamond_leggings, Items.golden_leggings },
			{ Items.leather_boots, Items.iron_boots, Items.diamond_boots, Items.golden_boots } };

	/**+
	 * Adds the armor recipes to the CraftingManager.
	 */
	public void addRecipes(CraftingManager craftManager) {
		for (int i = 0; i < this.recipeItems[0].length; ++i) {
			Item item = this.recipeItems[0][i];

			for (int j = 0; j < this.recipeItems.length - 1; ++j) {
				Item item1 = this.recipeItems[j + 1][i];
				craftManager.addRecipe(new ItemStack(item1),
						new Object[] { this.recipePatterns[j], Character.valueOf('X'), item });
			}
		}

	}
}