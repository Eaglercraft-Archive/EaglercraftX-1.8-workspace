package net.minecraft.item;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.WeightedRandomChestContent;

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
public class ItemEnchantedBook extends Item {
	public boolean hasEffect(ItemStack var1) {
		return true;
	}

	/**+
	 * Checks isDamagable and if it cannot be stacked
	 */
	public boolean isItemTool(ItemStack var1) {
		return false;
	}

	/**+
	 * Return an item rarity from EnumRarity
	 */
	public EnumRarity getRarity(ItemStack itemstack) {
		return this.getEnchantments(itemstack).tagCount() > 0 ? EnumRarity.UNCOMMON : super.getRarity(itemstack);
	}

	public NBTTagList getEnchantments(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		return nbttagcompound != null && nbttagcompound.hasKey("StoredEnchantments", 9)
				? (NBTTagList) nbttagcompound.getTag("StoredEnchantments")
				: new NBTTagList();
	}

	/**+
	 * allows items to add custom lines of information to the
	 * mouseover description
	 */
	public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
		super.addInformation(itemstack, entityplayer, list, flag);
		NBTTagList nbttaglist = this.getEnchantments(itemstack);
		if (nbttaglist != null) {
			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
				short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");
				if (Enchantment.getEnchantmentById(short1) != null) {
					list.add(Enchantment.getEnchantmentById(short1).getTranslatedName(short2));
				}
			}
		}

	}

	/**+
	 * Adds an stored enchantment to an enchanted book ItemStack
	 */
	public void addEnchantment(ItemStack stack, EnchantmentData enchantment) {
		NBTTagList nbttaglist = this.getEnchantments(stack);
		boolean flag = true;

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			if (nbttagcompound.getShort("id") == enchantment.enchantmentobj.effectId) {
				if (nbttagcompound.getShort("lvl") < enchantment.enchantmentLevel) {
					nbttagcompound.setShort("lvl", (short) enchantment.enchantmentLevel);
				}

				flag = false;
				break;
			}
		}

		if (flag) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setShort("id", (short) enchantment.enchantmentobj.effectId);
			nbttagcompound1.setShort("lvl", (short) enchantment.enchantmentLevel);
			nbttaglist.appendTag(nbttagcompound1);
		}

		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		stack.getTagCompound().setTag("StoredEnchantments", nbttaglist);
	}

	/**+
	 * Returns the ItemStack of an enchanted version of this item.
	 */
	public ItemStack getEnchantedItemStack(EnchantmentData data) {
		ItemStack itemstack = new ItemStack(this);
		this.addEnchantment(itemstack, data);
		return itemstack;
	}

	public void getAll(Enchantment enchantment, List<ItemStack> list) {
		for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
			list.add(this.getEnchantedItemStack(new EnchantmentData(enchantment, i)));
		}

	}

	public WeightedRandomChestContent getRandom(EaglercraftRandom rand) {
		return this.getRandom(rand, 1, 1, 1);
	}

	public WeightedRandomChestContent getRandom(EaglercraftRandom rand, int minChance, int maxChance, int weight) {
		ItemStack itemstack = new ItemStack(Items.book, 1, 0);
		EnchantmentHelper.addRandomEnchantment(rand, itemstack, 30);
		return new WeightedRandomChestContent(itemstack, minChance, maxChance, weight);
	}
}