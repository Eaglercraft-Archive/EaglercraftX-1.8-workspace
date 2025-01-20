package net.minecraft.item.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockWall;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
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
public class CraftingManager {
	private static CraftingManager instance;
	/**+
	 * A list of all the recipes added
	 */
	private final List<IRecipe> recipes = Lists.newArrayList();

	/**+
	 * Returns the static instance of this class
	 */
	public static CraftingManager getInstance() {
		if (instance == null) {
			instance = new CraftingManager();
		}
		return instance;
	}

	private CraftingManager() {
		(new RecipesTools()).addRecipes(this);
		(new RecipesWeapons()).addRecipes(this);
		(new RecipesIngots()).addRecipes(this);
		(new RecipesFood()).addRecipes(this);
		(new RecipesCrafting()).addRecipes(this);
		(new RecipesArmor()).addRecipes(this);
		(new RecipesDyes()).addRecipes(this);
		this.recipes.add(new RecipesArmorDyes());
		this.recipes.add(new RecipeBookCloning());
		this.recipes.add(new RecipesMapCloning());
		this.recipes.add(new RecipesMapExtending());
		this.recipes.add(new RecipeFireworks());
		this.recipes.add(new RecipeRepairItem());
		(new RecipesBanners()).addRecipes(this);
		this.addRecipe(new ItemStack(Items.paper, 3), new Object[] { "###", Character.valueOf('#'), Items.reeds });
		this.addShapelessRecipe(new ItemStack(Items.book, 1),
				new Object[] { Items.paper, Items.paper, Items.paper, Items.leather });
		this.addShapelessRecipe(new ItemStack(Items.writable_book, 1), new Object[] { Items.book,
				new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()), Items.feather });
		this.addRecipe(new ItemStack(Blocks.oak_fence, 3),
				new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.birch_fence, 3),
				new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.spruce_fence, 3),
				new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.jungle_fence, 3),
				new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.acacia_fence, 3),
				new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4) });
		this.addRecipe(new ItemStack(Blocks.dark_oak_fence, 3),
				new Object[] { "W#W", "W#W", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4) });
		this.addRecipe(new ItemStack(Blocks.cobblestone_wall, 6, BlockWall.EnumType.NORMAL.getMetadata()),
				new Object[] { "###", "###", Character.valueOf('#'), Blocks.cobblestone });
		this.addRecipe(new ItemStack(Blocks.cobblestone_wall, 6, BlockWall.EnumType.MOSSY.getMetadata()),
				new Object[] { "###", "###", Character.valueOf('#'), Blocks.mossy_cobblestone });
		this.addRecipe(new ItemStack(Blocks.nether_brick_fence, 6),
				new Object[] { "###", "###", Character.valueOf('#'), Blocks.nether_brick });
		this.addRecipe(new ItemStack(Blocks.oak_fence_gate, 1),
				new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.birch_fence_gate, 1),
				new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.spruce_fence_gate, 1),
				new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.jungle_fence_gate, 1),
				new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.acacia_fence_gate, 1),
				new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4) });
		this.addRecipe(new ItemStack(Blocks.dark_oak_fence_gate, 1),
				new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.stick, Character.valueOf('W'),
						new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4) });
		this.addRecipe(new ItemStack(Blocks.jukebox, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'),
				Blocks.planks, Character.valueOf('X'), Items.diamond });
		this.addRecipe(new ItemStack(Items.lead, 2), new Object[] { "~~ ", "~O ", "  ~", Character.valueOf('~'),
				Items.string, Character.valueOf('O'), Items.slime_ball });
		this.addRecipe(new ItemStack(Blocks.noteblock, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'),
				Blocks.planks, Character.valueOf('X'), Items.redstone });
		this.addRecipe(new ItemStack(Blocks.bookshelf, 1), new Object[] { "###", "XXX", "###", Character.valueOf('#'),
				Blocks.planks, Character.valueOf('X'), Items.book });
		this.addRecipe(new ItemStack(Blocks.snow, 1),
				new Object[] { "##", "##", Character.valueOf('#'), Items.snowball });
		this.addRecipe(new ItemStack(Blocks.snow_layer, 6),
				new Object[] { "###", Character.valueOf('#'), Blocks.snow });
		this.addRecipe(new ItemStack(Blocks.clay, 1),
				new Object[] { "##", "##", Character.valueOf('#'), Items.clay_ball });
		this.addRecipe(new ItemStack(Blocks.brick_block, 1),
				new Object[] { "##", "##", Character.valueOf('#'), Items.brick });
		this.addRecipe(new ItemStack(Blocks.glowstone, 1),
				new Object[] { "##", "##", Character.valueOf('#'), Items.glowstone_dust });
		this.addRecipe(new ItemStack(Blocks.quartz_block, 1),
				new Object[] { "##", "##", Character.valueOf('#'), Items.quartz });
		this.addRecipe(new ItemStack(Blocks.wool, 1),
				new Object[] { "##", "##", Character.valueOf('#'), Items.string });
		this.addRecipe(new ItemStack(Blocks.tnt, 1), new Object[] { "X#X", "#X#", "X#X", Character.valueOf('X'),
				Items.gunpowder, Character.valueOf('#'), Blocks.sand });
		this.addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.COBBLESTONE.getMetadata()),
				new Object[] { "###", Character.valueOf('#'), Blocks.cobblestone });
		this.addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.STONE.getMetadata()), new Object[] {
				"###", Character.valueOf('#'), new ItemStack(Blocks.stone, BlockStone.EnumType.STONE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.SAND.getMetadata()),
				new Object[] { "###", Character.valueOf('#'), Blocks.sandstone });
		this.addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.BRICK.getMetadata()),
				new Object[] { "###", Character.valueOf('#'), Blocks.brick_block });
		this.addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()),
				new Object[] { "###", Character.valueOf('#'), Blocks.stonebrick });
		this.addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.NETHERBRICK.getMetadata()),
				new Object[] { "###", Character.valueOf('#'), Blocks.nether_brick });
		this.addRecipe(new ItemStack(Blocks.stone_slab, 6, BlockStoneSlab.EnumType.QUARTZ.getMetadata()),
				new Object[] { "###", Character.valueOf('#'), Blocks.quartz_block });
		this.addRecipe(new ItemStack(Blocks.stone_slab2, 6, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata()),
				new Object[] { "###", Character.valueOf('#'), Blocks.red_sandstone });
		this.addRecipe(new ItemStack(Blocks.wooden_slab, 6, 0), new Object[] { "###", Character.valueOf('#'),
				new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.wooden_slab, 6, BlockPlanks.EnumType.BIRCH.getMetadata()),
				new Object[] { "###", Character.valueOf('#'),
						new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.wooden_slab, 6, BlockPlanks.EnumType.SPRUCE.getMetadata()),
				new Object[] { "###", Character.valueOf('#'),
						new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.wooden_slab, 6, BlockPlanks.EnumType.JUNGLE.getMetadata()),
				new Object[] { "###", Character.valueOf('#'),
						new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.wooden_slab, 6, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4),
				new Object[] { "###", Character.valueOf('#'),
						new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4) });
		this.addRecipe(new ItemStack(Blocks.wooden_slab, 6, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4),
				new Object[] { "###", Character.valueOf('#'),
						new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4) });
		this.addRecipe(new ItemStack(Blocks.ladder, 3),
				new Object[] { "# #", "###", "# #", Character.valueOf('#'), Items.stick });
		this.addRecipe(new ItemStack(Items.oak_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'),
				new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.getMetadata()) });
		this.addRecipe(new ItemStack(Items.spruce_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'),
				new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()) });
		this.addRecipe(new ItemStack(Items.birch_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'),
				new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.getMetadata()) });
		this.addRecipe(new ItemStack(Items.jungle_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'),
				new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()) });
		this.addRecipe(new ItemStack(Items.acacia_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'),
				new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.ACACIA.getMetadata()) });
		this.addRecipe(new ItemStack(Items.dark_oak_door, 3), new Object[] { "##", "##", "##", Character.valueOf('#'),
				new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.trapdoor, 2),
				new Object[] { "###", "###", Character.valueOf('#'), Blocks.planks });
		this.addRecipe(new ItemStack(Items.iron_door, 3),
				new Object[] { "##", "##", "##", Character.valueOf('#'), Items.iron_ingot });
		this.addRecipe(new ItemStack(Blocks.iron_trapdoor, 1),
				new Object[] { "##", "##", Character.valueOf('#'), Items.iron_ingot });
		this.addRecipe(new ItemStack(Items.sign, 3), new Object[] { "###", "###", " X ", Character.valueOf('#'),
				Blocks.planks, Character.valueOf('X'), Items.stick });
		this.addRecipe(new ItemStack(Items.cake, 1),
				new Object[] { "AAA", "BEB", "CCC", Character.valueOf('A'), Items.milk_bucket, Character.valueOf('B'),
						Items.sugar, Character.valueOf('C'), Items.wheat, Character.valueOf('E'), Items.egg });
		this.addRecipe(new ItemStack(Items.sugar, 1), new Object[] { "#", Character.valueOf('#'), Items.reeds });
		this.addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.OAK.getMetadata()), new Object[] { "#",
				Character.valueOf('#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.OAK.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.SPRUCE.getMetadata()), new Object[] { "#",
				Character.valueOf('#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.BIRCH.getMetadata()), new Object[] { "#",
				Character.valueOf('#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.BIRCH.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.planks, 4, BlockPlanks.EnumType.JUNGLE.getMetadata()), new Object[] { "#",
				Character.valueOf('#'), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.planks, 4, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4),
				new Object[] { "#", Character.valueOf('#'),
						new ItemStack(Blocks.log2, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4) });
		this.addRecipe(new ItemStack(Blocks.planks, 4, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4),
				new Object[] { "#", Character.valueOf('#'),
						new ItemStack(Blocks.log2, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4) });
		this.addRecipe(new ItemStack(Items.stick, 4), new Object[] { "#", "#", Character.valueOf('#'), Blocks.planks });
		this.addRecipe(new ItemStack(Blocks.torch, 4),
				new Object[] { "X", "#", Character.valueOf('X'), Items.coal, Character.valueOf('#'), Items.stick });
		this.addRecipe(new ItemStack(Blocks.torch, 4), new Object[] { "X", "#", Character.valueOf('X'),
				new ItemStack(Items.coal, 1, 1), Character.valueOf('#'), Items.stick });
		this.addRecipe(new ItemStack(Items.bowl, 4),
				new Object[] { "# #", " # ", Character.valueOf('#'), Blocks.planks });
		this.addRecipe(new ItemStack(Items.glass_bottle, 3),
				new Object[] { "# #", " # ", Character.valueOf('#'), Blocks.glass });
		this.addRecipe(new ItemStack(Blocks.rail, 16), new Object[] { "X X", "X#X", "X X", Character.valueOf('X'),
				Items.iron_ingot, Character.valueOf('#'), Items.stick });
		this.addRecipe(new ItemStack(Blocks.golden_rail, 6), new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'),
				Items.gold_ingot, Character.valueOf('R'), Items.redstone, Character.valueOf('#'), Items.stick });
		this.addRecipe(new ItemStack(Blocks.activator_rail, 6),
				new Object[] { "XSX", "X#X", "XSX", Character.valueOf('X'), Items.iron_ingot, Character.valueOf('#'),
						Blocks.redstone_torch, Character.valueOf('S'), Items.stick });
		this.addRecipe(new ItemStack(Blocks.detector_rail, 6),
				new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'), Items.iron_ingot, Character.valueOf('R'),
						Items.redstone, Character.valueOf('#'), Blocks.stone_pressure_plate });
		this.addRecipe(new ItemStack(Items.minecart, 1),
				new Object[] { "# #", "###", Character.valueOf('#'), Items.iron_ingot });
		this.addRecipe(new ItemStack(Items.cauldron, 1),
				new Object[] { "# #", "# #", "###", Character.valueOf('#'), Items.iron_ingot });
		this.addRecipe(new ItemStack(Items.brewing_stand, 1), new Object[] { " B ", "###", Character.valueOf('#'),
				Blocks.cobblestone, Character.valueOf('B'), Items.blaze_rod });
		this.addRecipe(new ItemStack(Blocks.lit_pumpkin, 1), new Object[] { "A", "B", Character.valueOf('A'),
				Blocks.pumpkin, Character.valueOf('B'), Blocks.torch });
		this.addRecipe(new ItemStack(Items.chest_minecart, 1), new Object[] { "A", "B", Character.valueOf('A'),
				Blocks.chest, Character.valueOf('B'), Items.minecart });
		this.addRecipe(new ItemStack(Items.furnace_minecart, 1), new Object[] { "A", "B", Character.valueOf('A'),
				Blocks.furnace, Character.valueOf('B'), Items.minecart });
		this.addRecipe(new ItemStack(Items.tnt_minecart, 1),
				new Object[] { "A", "B", Character.valueOf('A'), Blocks.tnt, Character.valueOf('B'), Items.minecart });
		this.addRecipe(new ItemStack(Items.hopper_minecart, 1), new Object[] { "A", "B", Character.valueOf('A'),
				Blocks.hopper, Character.valueOf('B'), Items.minecart });
		this.addRecipe(new ItemStack(Items.boat, 1),
				new Object[] { "# #", "###", Character.valueOf('#'), Blocks.planks });
		this.addRecipe(new ItemStack(Items.bucket, 1),
				new Object[] { "# #", " # ", Character.valueOf('#'), Items.iron_ingot });
		this.addRecipe(new ItemStack(Items.flower_pot, 1),
				new Object[] { "# #", " # ", Character.valueOf('#'), Items.brick });
		this.addShapelessRecipe(new ItemStack(Items.flint_and_steel, 1),
				new Object[] { new ItemStack(Items.iron_ingot, 1), new ItemStack(Items.flint, 1) });
		this.addRecipe(new ItemStack(Items.bread, 1), new Object[] { "###", Character.valueOf('#'), Items.wheat });
		this.addRecipe(new ItemStack(Blocks.oak_stairs, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'),
				new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.OAK.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.birch_stairs, 4), new Object[] { "#  ", "## ", "###",
				Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.BIRCH.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.spruce_stairs, 4), new Object[] { "#  ", "## ", "###",
				Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.jungle_stairs, 4), new Object[] { "#  ", "## ", "###",
				Character.valueOf('#'), new ItemStack(Blocks.planks, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.acacia_stairs, 4),
				new Object[] { "#  ", "## ", "###", Character.valueOf('#'),
						new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.ACACIA.getMetadata() - 4) });
		this.addRecipe(new ItemStack(Blocks.dark_oak_stairs, 4),
				new Object[] { "#  ", "## ", "###", Character.valueOf('#'),
						new ItemStack(Blocks.planks, 1, 4 + BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4) });
		this.addRecipe(new ItemStack(Items.fishing_rod, 1), new Object[] { "  #", " #X", "# X", Character.valueOf('#'),
				Items.stick, Character.valueOf('X'), Items.string });
		this.addRecipe(new ItemStack(Items.carrot_on_a_stick, 1), new Object[] { "# ", " X", Character.valueOf('#'),
				Items.fishing_rod, Character.valueOf('X'), Items.carrot });
		this.addRecipe(new ItemStack(Blocks.stone_stairs, 4),
				new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.cobblestone });
		this.addRecipe(new ItemStack(Blocks.brick_stairs, 4),
				new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.brick_block });
		this.addRecipe(new ItemStack(Blocks.stone_brick_stairs, 4),
				new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.stonebrick });
		this.addRecipe(new ItemStack(Blocks.nether_brick_stairs, 4),
				new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.nether_brick });
		this.addRecipe(new ItemStack(Blocks.sandstone_stairs, 4),
				new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.sandstone });
		this.addRecipe(new ItemStack(Blocks.red_sandstone_stairs, 4),
				new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.red_sandstone });
		this.addRecipe(new ItemStack(Blocks.quartz_stairs, 4),
				new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.quartz_block });
		this.addRecipe(new ItemStack(Items.painting, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'),
				Items.stick, Character.valueOf('X'), Blocks.wool });
		this.addRecipe(new ItemStack(Items.item_frame, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'),
				Items.stick, Character.valueOf('X'), Items.leather });
		this.addRecipe(new ItemStack(Items.golden_apple, 1, 0), new Object[] { "###", "#X#", "###",
				Character.valueOf('#'), Items.gold_ingot, Character.valueOf('X'), Items.apple });
		this.addRecipe(new ItemStack(Items.golden_apple, 1, 1), new Object[] { "###", "#X#", "###",
				Character.valueOf('#'), Blocks.gold_block, Character.valueOf('X'), Items.apple });
		this.addRecipe(new ItemStack(Items.golden_carrot, 1, 0), new Object[] { "###", "#X#", "###",
				Character.valueOf('#'), Items.gold_nugget, Character.valueOf('X'), Items.carrot });
		this.addRecipe(new ItemStack(Items.speckled_melon, 1), new Object[] { "###", "#X#", "###",
				Character.valueOf('#'), Items.gold_nugget, Character.valueOf('X'), Items.melon });
		this.addRecipe(new ItemStack(Blocks.lever, 1), new Object[] { "X", "#", Character.valueOf('#'),
				Blocks.cobblestone, Character.valueOf('X'), Items.stick });
		this.addRecipe(new ItemStack(Blocks.tripwire_hook, 2), new Object[] { "I", "S", "#", Character.valueOf('#'),
				Blocks.planks, Character.valueOf('S'), Items.stick, Character.valueOf('I'), Items.iron_ingot });
		this.addRecipe(new ItemStack(Blocks.redstone_torch, 1),
				new Object[] { "X", "#", Character.valueOf('#'), Items.stick, Character.valueOf('X'), Items.redstone });
		this.addRecipe(new ItemStack(Items.repeater, 1),
				new Object[] { "#X#", "III", Character.valueOf('#'), Blocks.redstone_torch, Character.valueOf('X'),
						Items.redstone, Character.valueOf('I'),
						new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata()) });
		this.addRecipe(new ItemStack(Items.comparator, 1),
				new Object[] { " # ", "#X#", "III", Character.valueOf('#'), Blocks.redstone_torch,
						Character.valueOf('X'), Items.quartz, Character.valueOf('I'),
						new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata()) });
		this.addRecipe(new ItemStack(Items.clock, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'),
				Items.gold_ingot, Character.valueOf('X'), Items.redstone });
		this.addRecipe(new ItemStack(Items.compass, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'),
				Items.iron_ingot, Character.valueOf('X'), Items.redstone });
		this.addRecipe(new ItemStack(Items.map, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'),
				Items.paper, Character.valueOf('X'), Items.compass });
		this.addRecipe(new ItemStack(Blocks.stone_button, 1), new Object[] { "#", Character.valueOf('#'),
				new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.wooden_button, 1),
				new Object[] { "#", Character.valueOf('#'), Blocks.planks });
		this.addRecipe(new ItemStack(Blocks.stone_pressure_plate, 1), new Object[] { "##", Character.valueOf('#'),
				new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata()) });
		this.addRecipe(new ItemStack(Blocks.wooden_pressure_plate, 1),
				new Object[] { "##", Character.valueOf('#'), Blocks.planks });
		this.addRecipe(new ItemStack(Blocks.heavy_weighted_pressure_plate, 1),
				new Object[] { "##", Character.valueOf('#'), Items.iron_ingot });
		this.addRecipe(new ItemStack(Blocks.light_weighted_pressure_plate, 1),
				new Object[] { "##", Character.valueOf('#'), Items.gold_ingot });
		this.addRecipe(new ItemStack(Blocks.dispenser, 1), new Object[] { "###", "#X#", "#R#", Character.valueOf('#'),
				Blocks.cobblestone, Character.valueOf('X'), Items.bow, Character.valueOf('R'), Items.redstone });
		this.addRecipe(new ItemStack(Blocks.dropper, 1), new Object[] { "###", "# #", "#R#", Character.valueOf('#'),
				Blocks.cobblestone, Character.valueOf('R'), Items.redstone });
		this.addRecipe(new ItemStack(Blocks.piston, 1),
				new Object[] { "TTT", "#X#", "#R#", Character.valueOf('#'), Blocks.cobblestone, Character.valueOf('X'),
						Items.iron_ingot, Character.valueOf('R'), Items.redstone, Character.valueOf('T'),
						Blocks.planks });
		this.addRecipe(new ItemStack(Blocks.sticky_piston, 1), new Object[] { "S", "P", Character.valueOf('S'),
				Items.slime_ball, Character.valueOf('P'), Blocks.piston });
		this.addRecipe(new ItemStack(Items.bed, 1), new Object[] { "###", "XXX", Character.valueOf('#'), Blocks.wool,
				Character.valueOf('X'), Blocks.planks });
		this.addRecipe(new ItemStack(Blocks.enchanting_table, 1),
				new Object[] { " B ", "D#D", "###", Character.valueOf('#'), Blocks.obsidian, Character.valueOf('B'),
						Items.book, Character.valueOf('D'), Items.diamond });
		this.addRecipe(new ItemStack(Blocks.anvil, 1), new Object[] { "III", " i ", "iii", Character.valueOf('I'),
				Blocks.iron_block, Character.valueOf('i'), Items.iron_ingot });
		this.addRecipe(new ItemStack(Items.leather),
				new Object[] { "##", "##", Character.valueOf('#'), Items.rabbit_hide });
		this.addShapelessRecipe(new ItemStack(Items.ender_eye, 1),
				new Object[] { Items.ender_pearl, Items.blaze_powder });
		this.addShapelessRecipe(new ItemStack(Items.fire_charge, 3),
				new Object[] { Items.gunpowder, Items.blaze_powder, Items.coal });
		this.addShapelessRecipe(new ItemStack(Items.fire_charge, 3),
				new Object[] { Items.gunpowder, Items.blaze_powder, new ItemStack(Items.coal, 1, 1) });
		this.addRecipe(new ItemStack(Blocks.daylight_detector),
				new Object[] { "GGG", "QQQ", "WWW", Character.valueOf('G'), Blocks.glass, Character.valueOf('Q'),
						Items.quartz, Character.valueOf('W'), Blocks.wooden_slab });
		this.addRecipe(new ItemStack(Blocks.hopper), new Object[] { "I I", "ICI", " I ", Character.valueOf('I'),
				Items.iron_ingot, Character.valueOf('C'), Blocks.chest });
		this.addRecipe(new ItemStack(Items.armor_stand, 1),
				new Object[] { "///", " / ", "/_/", Character.valueOf('/'), Items.stick, Character.valueOf('_'),
						new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.STONE.getMetadata()) });
		Collections.sort(this.recipes, new Comparator<IRecipe>() {
			public int compare(IRecipe parIRecipe, IRecipe parIRecipe2) {
				return parIRecipe instanceof ShapelessRecipes && parIRecipe2 instanceof ShapedRecipes ? 1
						: (parIRecipe2 instanceof ShapelessRecipes && parIRecipe instanceof ShapedRecipes ? -1
								: (parIRecipe2.getRecipeSize() < parIRecipe.getRecipeSize() ? -1
										: (parIRecipe2.getRecipeSize() > parIRecipe.getRecipeSize() ? 1 : 0)));
			}
		});
	}

	/**+
	 * Adds an IRecipe to the list of crafting recipes.
	 */
	public ShapedRecipes addRecipe(ItemStack stack, Object... recipeComponents) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		if (recipeComponents[i] instanceof String[]) {
			String[] astring = (String[]) ((String[]) recipeComponents[i++]);

			for (int l = 0; l < astring.length; ++l) {
				String s2 = astring[l];
				++k;
				j = s2.length();
				s = s + s2;
			}
		} else {
			while (recipeComponents[i] instanceof String) {
				String s1 = (String) recipeComponents[i++];
				++k;
				j = s1.length();
				s = s + s1;
			}
		}

		HashMap hashmap;
		for (hashmap = Maps.newHashMap(); i < recipeComponents.length; i += 2) {
			Character character = (Character) recipeComponents[i];
			ItemStack itemstack = null;
			if (recipeComponents[i + 1] instanceof Item) {
				itemstack = new ItemStack((Item) recipeComponents[i + 1]);
			} else if (recipeComponents[i + 1] instanceof Block) {
				itemstack = new ItemStack((Block) recipeComponents[i + 1], 1, 32767);
			} else if (recipeComponents[i + 1] instanceof ItemStack) {
				itemstack = (ItemStack) recipeComponents[i + 1];
			}

			hashmap.put(character, itemstack);
		}

		ItemStack[] aitemstack = new ItemStack[j * k];

		for (int i1 = 0; i1 < j * k; ++i1) {
			char c0 = s.charAt(i1);
			if (hashmap.containsKey(Character.valueOf(c0))) {
				aitemstack[i1] = ((ItemStack) hashmap.get(Character.valueOf(c0))).copy();
			} else {
				aitemstack[i1] = null;
			}
		}

		ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, stack);
		this.recipes.add(shapedrecipes);
		return shapedrecipes;
	}

	/**+
	 * Adds a shapeless crafting recipe to the the game.
	 */
	public void addShapelessRecipe(ItemStack stack, Object... recipeComponents) {
		ArrayList arraylist = Lists.newArrayList();

		for (int i = 0; i < recipeComponents.length; ++i) {
			Object object = recipeComponents[i];
			if (object instanceof ItemStack) {
				arraylist.add(((ItemStack) object).copy());
			} else if (object instanceof Item) {
				arraylist.add(new ItemStack((Item) object));
			} else {
				if (!(object instanceof Block)) {
					throw new IllegalArgumentException(
							"Invalid shapeless recipe: unknown type " + object.getClass().getName() + "!");
				}

				arraylist.add(new ItemStack((Block) object));
			}
		}

		this.recipes.add(new ShapelessRecipes(stack, arraylist));
	}

	/**+
	 * Adds an IRecipe to the list of crafting recipes.
	 */
	public void addRecipe(IRecipe recipe) {
		this.recipes.add(recipe);
	}

	/**+
	 * Retrieves an ItemStack that has multiple recipes for it.
	 */
	public ItemStack findMatchingRecipe(InventoryCrafting worldIn, World parWorld) {
		for (int i = 0, l = this.recipes.size(); i < l; ++i) {
			IRecipe irecipe = this.recipes.get(i);
			if (irecipe.matches(worldIn, parWorld)) {
				return irecipe.getCraftingResult(worldIn);
			}
		}

		return null;
	}

	public ItemStack[] func_180303_b(InventoryCrafting worldIn, World parWorld) {
		for (int i = 0, l = this.recipes.size(); i < l; ++i) {
			IRecipe irecipe = this.recipes.get(i);
			if (irecipe.matches(worldIn, parWorld)) {
				return irecipe.getRemainingItems(worldIn);
			}
		}

		ItemStack[] aitemstack = new ItemStack[worldIn.getSizeInventory()];

		for (int i = 0; i < aitemstack.length; ++i) {
			aitemstack[i] = worldIn.getStackInSlot(i);
		}

		return aitemstack;
	}

	/**+
	 * returns the List<> of all recipes
	 */
	public List<IRecipe> getRecipeList() {
		return this.recipes;
	}
}