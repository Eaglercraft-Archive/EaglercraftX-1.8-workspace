package net.minecraft.item.crafting;

import java.util.Map;
import java.util.Map.Entry;

import com.carrotsearch.hppc.ObjectFloatHashMap;
import com.carrotsearch.hppc.ObjectFloatMap;
import com.carrotsearch.hppc.cursors.ObjectFloatCursor;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
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
public class FurnaceRecipes {
	private static FurnaceRecipes smeltingBase;
	private Map<ItemStack, ItemStack> smeltingList = Maps.newHashMap();
	/**+
	 * A list which contains how many experience points each recipe
	 * output will give.
	 */
	private ObjectFloatMap<ItemStack> experienceList = new ObjectFloatHashMap<>();

	/**+
	 * Returns an instance of FurnaceRecipes.
	 */
	public static FurnaceRecipes instance() {
		if (smeltingBase == null) {
			smeltingBase = new FurnaceRecipes();
		}
		return smeltingBase;
	}

	private FurnaceRecipes() {
		this.addSmeltingRecipeForBlock(Blocks.iron_ore, new ItemStack(Items.iron_ingot), 0.7F);
		this.addSmeltingRecipeForBlock(Blocks.gold_ore, new ItemStack(Items.gold_ingot), 1.0F);
		this.addSmeltingRecipeForBlock(Blocks.diamond_ore, new ItemStack(Items.diamond), 1.0F);
		this.addSmeltingRecipeForBlock(Blocks.sand, new ItemStack(Blocks.glass), 0.1F);
		this.addSmelting(Items.porkchop, new ItemStack(Items.cooked_porkchop), 0.35F);
		this.addSmelting(Items.beef, new ItemStack(Items.cooked_beef), 0.35F);
		this.addSmelting(Items.chicken, new ItemStack(Items.cooked_chicken), 0.35F);
		this.addSmelting(Items.rabbit, new ItemStack(Items.cooked_rabbit), 0.35F);
		this.addSmelting(Items.mutton, new ItemStack(Items.cooked_mutton), 0.35F);
		this.addSmeltingRecipeForBlock(Blocks.cobblestone, new ItemStack(Blocks.stone), 0.1F);
		this.addSmeltingRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.DEFAULT_META),
				new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.CRACKED_META), 0.1F);
		this.addSmelting(Items.clay_ball, new ItemStack(Items.brick), 0.3F);
		this.addSmeltingRecipeForBlock(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.35F);
		this.addSmeltingRecipeForBlock(Blocks.cactus, new ItemStack(Items.dye, 1, EnumDyeColor.GREEN.getDyeDamage()),
				0.2F);
		this.addSmeltingRecipeForBlock(Blocks.log, new ItemStack(Items.coal, 1, 1), 0.15F);
		this.addSmeltingRecipeForBlock(Blocks.log2, new ItemStack(Items.coal, 1, 1), 0.15F);
		this.addSmeltingRecipeForBlock(Blocks.emerald_ore, new ItemStack(Items.emerald), 1.0F);
		this.addSmelting(Items.potato, new ItemStack(Items.baked_potato), 0.35F);
		this.addSmeltingRecipeForBlock(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.1F);
		this.addSmeltingRecipe(new ItemStack(Blocks.sponge, 1, 1), new ItemStack(Blocks.sponge, 1, 0), 0.15F);

		ItemFishFood.FishType[] types = ItemFishFood.FishType.values();
		for (int i = 0; i < types.length; ++i) {
			ItemFishFood.FishType itemfishfood$fishtype = types[i];
			if (itemfishfood$fishtype.canCook()) {
				this.addSmeltingRecipe(new ItemStack(Items.fish, 1, itemfishfood$fishtype.getMetadata()),
						new ItemStack(Items.cooked_fish, 1, itemfishfood$fishtype.getMetadata()), 0.35F);
			}
		}

		this.addSmeltingRecipeForBlock(Blocks.coal_ore, new ItemStack(Items.coal), 0.1F);
		this.addSmeltingRecipeForBlock(Blocks.redstone_ore, new ItemStack(Items.redstone), 0.7F);
		this.addSmeltingRecipeForBlock(Blocks.lapis_ore, new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()),
				0.2F);
		this.addSmeltingRecipeForBlock(Blocks.quartz_ore, new ItemStack(Items.quartz), 0.2F);
	}

	/**+
	 * Adds a smelting recipe, where the input item is an instance
	 * of Block.
	 */
	public void addSmeltingRecipeForBlock(Block input, ItemStack stack, float experience) {
		this.addSmelting(Item.getItemFromBlock(input), stack, experience);
	}

	/**+
	 * Adds a smelting recipe using an Item as the input item.
	 */
	public void addSmelting(Item input, ItemStack stack, float experience) {
		this.addSmeltingRecipe(new ItemStack(input, 1, 32767), stack, experience);
	}

	/**+
	 * Adds a smelting recipe using an ItemStack as the input for
	 * the recipe.
	 */
	public void addSmeltingRecipe(ItemStack input, ItemStack stack, float experience) {
		this.smeltingList.put(input, stack);
		this.experienceList.put(stack, experience);
	}

	/**+
	 * Returns the smelting result of an item.
	 */
	public ItemStack getSmeltingResult(ItemStack stack) {
		for (Entry entry : this.smeltingList.entrySet()) {
			if (this.compareItemStacks(stack, (ItemStack) entry.getKey())) {
				return (ItemStack) entry.getValue();
			}
		}

		return null;
	}

	/**+
	 * Compares two itemstacks to ensure that they are the same.
	 * This checks both the item and the metadata of the item.
	 */
	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem()
				&& (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}

	public Map<ItemStack, ItemStack> getSmeltingList() {
		return this.smeltingList;
	}

	public float getSmeltingExperience(ItemStack stack) {
		for (ObjectFloatCursor<ItemStack> entry : this.experienceList) {
			if (this.compareItemStacks(stack, entry.key)) {
				return entry.value;
			}
		}

		return 0.0F;
	}
}