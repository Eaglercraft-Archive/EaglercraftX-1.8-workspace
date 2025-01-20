package net.minecraft.item.crafting;

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
public class RecipesFood {
	/**+
	 * Adds the food recipes to the CraftingManager.
	 */
	public void addRecipes(CraftingManager parCraftingManager) {
		parCraftingManager.addShapelessRecipe(new ItemStack(Items.mushroom_stew),
				new Object[] { Blocks.brown_mushroom, Blocks.red_mushroom, Items.bowl });
		parCraftingManager.addRecipe(new ItemStack(Items.cookie, 8), new Object[] { "#X#", Character.valueOf('X'),
				new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeDamage()), Character.valueOf('#'), Items.wheat });
		parCraftingManager.addRecipe(new ItemStack(Items.rabbit_stew),
				new Object[] { " R ", "CPM", " B ", Character.valueOf('R'), new ItemStack(Items.cooked_rabbit),
						Character.valueOf('C'), Items.carrot, Character.valueOf('P'), Items.baked_potato,
						Character.valueOf('M'), Blocks.brown_mushroom, Character.valueOf('B'), Items.bowl });
		parCraftingManager.addRecipe(new ItemStack(Items.rabbit_stew),
				new Object[] { " R ", "CPD", " B ", Character.valueOf('R'), new ItemStack(Items.cooked_rabbit),
						Character.valueOf('C'), Items.carrot, Character.valueOf('P'), Items.baked_potato,
						Character.valueOf('D'), Blocks.red_mushroom, Character.valueOf('B'), Items.bowl });
		parCraftingManager.addRecipe(new ItemStack(Blocks.melon_block),
				new Object[] { "MMM", "MMM", "MMM", Character.valueOf('M'), Items.melon });
		parCraftingManager.addRecipe(new ItemStack(Items.melon_seeds),
				new Object[] { "M", Character.valueOf('M'), Items.melon });
		parCraftingManager.addRecipe(new ItemStack(Items.pumpkin_seeds, 4),
				new Object[] { "M", Character.valueOf('M'), Blocks.pumpkin });
		parCraftingManager.addShapelessRecipe(new ItemStack(Items.pumpkin_pie),
				new Object[] { Blocks.pumpkin, Items.sugar, Items.egg });
		parCraftingManager.addShapelessRecipe(new ItemStack(Items.fermented_spider_eye),
				new Object[] { Items.spider_eye, Blocks.brown_mushroom, Items.sugar });
		parCraftingManager.addShapelessRecipe(new ItemStack(Items.blaze_powder, 2), new Object[] { Items.blaze_rod });
		parCraftingManager.addShapelessRecipe(new ItemStack(Items.magma_cream),
				new Object[] { Items.blaze_powder, Items.slime_ball });
	}
}