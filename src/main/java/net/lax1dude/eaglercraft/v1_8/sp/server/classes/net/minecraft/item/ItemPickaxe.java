package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.Block;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.material.Material;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.init.Blocks;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.Item;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemStack;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemTool;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class ItemPickaxe extends ItemTool {

	static {
		__checkIntegratedContextValid("net/minecraft/item/ItemPickaxe");
	}

	private static Set<Block> EFFECTIVE_ON = null;

	public static void doBootstrap() {
		EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2,
				Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder });
	}

	protected ItemPickaxe(Item.ToolMaterial material) {
		super(2.0F, material, EFFECTIVE_ON);
	}

	/**+
	 * Check whether this Item can harvest the given Block
	 */
	public boolean canHarvestBlock(Block blockIn) {
		return blockIn == Blocks.obsidian ? this.toolMaterial.getHarvestLevel() == 3
				: (blockIn != Blocks.diamond_block && blockIn != Blocks.diamond_ore
						? (blockIn != Blocks.emerald_ore && blockIn != Blocks.emerald_block
								? (blockIn != Blocks.gold_block && blockIn != Blocks.gold_ore
										? (blockIn != Blocks.iron_block && blockIn != Blocks.iron_ore
												? (blockIn != Blocks.lapis_block && blockIn != Blocks.lapis_ore
														? (blockIn != Blocks.redstone_ore
																&& blockIn != Blocks.lit_redstone_ore
																		? (blockIn.getMaterial() == Material.rock ? true
																				: (blockIn
																						.getMaterial() == Material.iron
																								? true
																								: blockIn
																										.getMaterial() == Material.anvil))
																		: this.toolMaterial.getHarvestLevel() >= 2)
														: this.toolMaterial.getHarvestLevel() >= 1)
												: this.toolMaterial.getHarvestLevel() >= 1)
										: this.toolMaterial.getHarvestLevel() >= 2)
								: this.toolMaterial.getHarvestLevel() >= 2)
						: this.toolMaterial.getHarvestLevel() >= 2);
	}

	public float getStrVsBlock(ItemStack stack, Block block) {
		return block.getMaterial() != Material.iron && block.getMaterial() != Material.anvil
				&& block.getMaterial() != Material.rock ? super.getStrVsBlock(stack, block)
						: this.efficiencyOnProperMaterial;
	}
}