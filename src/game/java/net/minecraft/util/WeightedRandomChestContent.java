package net.minecraft.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.google.common.collect.Lists;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;

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
public class WeightedRandomChestContent extends WeightedRandom.Item {
	private ItemStack theItemId;
	private int minStackSize;
	private int maxStackSize;

	public WeightedRandomChestContent(Item parItem, int parInt1, int minimumChance, int maximumChance,
			int itemWeightIn) {
		super(itemWeightIn);
		this.theItemId = new ItemStack(parItem, 1, parInt1);
		this.minStackSize = minimumChance;
		this.maxStackSize = maximumChance;
	}

	public WeightedRandomChestContent(ItemStack stack, int minimumChance, int maximumChance, int itemWeightIn) {
		super(itemWeightIn);
		this.theItemId = stack;
		this.minStackSize = minimumChance;
		this.maxStackSize = maximumChance;
	}

	public static void generateChestContents(EaglercraftRandom random, List<WeightedRandomChestContent> listIn,
			IInventory inv, int max) {
		for (int i = 0; i < max; ++i) {
			WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent) WeightedRandom
					.getRandomItem(random, listIn);
			int j = weightedrandomchestcontent.minStackSize + random
					.nextInt(weightedrandomchestcontent.maxStackSize - weightedrandomchestcontent.minStackSize + 1);
			if (weightedrandomchestcontent.theItemId.getMaxStackSize() >= j) {
				ItemStack itemstack1 = weightedrandomchestcontent.theItemId.copy();
				itemstack1.stackSize = j;
				inv.setInventorySlotContents(random.nextInt(inv.getSizeInventory()), itemstack1);
			} else {
				for (int k = 0; k < j; ++k) {
					ItemStack itemstack = weightedrandomchestcontent.theItemId.copy();
					itemstack.stackSize = 1;
					inv.setInventorySlotContents(random.nextInt(inv.getSizeInventory()), itemstack);
				}
			}
		}

	}

	public static void generateDispenserContents(EaglercraftRandom random, List<WeightedRandomChestContent> listIn,
			TileEntityDispenser dispenser, int max) {
		for (int i = 0; i < max; ++i) {
			WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent) WeightedRandom
					.getRandomItem(random, listIn);
			int j = weightedrandomchestcontent.minStackSize + random
					.nextInt(weightedrandomchestcontent.maxStackSize - weightedrandomchestcontent.minStackSize + 1);
			if (weightedrandomchestcontent.theItemId.getMaxStackSize() >= j) {
				ItemStack itemstack1 = weightedrandomchestcontent.theItemId.copy();
				itemstack1.stackSize = j;
				dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), itemstack1);
			} else {
				for (int k = 0; k < j; ++k) {
					ItemStack itemstack = weightedrandomchestcontent.theItemId.copy();
					itemstack.stackSize = 1;
					dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), itemstack);
				}
			}
		}

	}

	public static List<WeightedRandomChestContent> func_177629_a(List<WeightedRandomChestContent> parList,
			WeightedRandomChestContent... parArrayOfWeightedRandomChestContent) {
		ArrayList arraylist = Lists.newArrayList(parList);
		Collections.addAll(arraylist, parArrayOfWeightedRandomChestContent);
		return arraylist;
	}
}