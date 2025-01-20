package net.minecraft.enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.carrotsearch.hppc.IntIntHashMap;
import com.carrotsearch.hppc.IntIntMap;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.WeightedRandom;

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
public class EnchantmentHelper {
	/**+
	 * Is the random seed of enchantment effects.
	 */
	private static final EaglercraftRandom enchantmentRand = new EaglercraftRandom();
	/**+
	 * Used to calculate the extra armor of enchantments on armors
	 * equipped on player.
	 */
	private static final EnchantmentHelper.ModifierDamage enchantmentModifierDamage = new EnchantmentHelper.ModifierDamage();
	/**+
	 * Used to calculate the (magic) extra damage done by
	 * enchantments on current equipped item of player.
	 */
	private static final EnchantmentHelper.ModifierLiving enchantmentModifierLiving = new EnchantmentHelper.ModifierLiving();
	private static final EnchantmentHelper.HurtIterator ENCHANTMENT_ITERATOR_HURT = new EnchantmentHelper.HurtIterator();
	private static final EnchantmentHelper.DamageIterator ENCHANTMENT_ITERATOR_DAMAGE = new EnchantmentHelper.DamageIterator();

	/**+
	 * Returns the level of enchantment on the ItemStack passed.
	 */
	public static int getEnchantmentLevel(int enchID, ItemStack stack) {
		if (stack == null) {
			return 0;
		} else {
			NBTTagList nbttaglist = stack.getEnchantmentTagList();
			if (nbttaglist == null) {
				return 0;
			} else {
				for (int i = 0; i < nbttaglist.tagCount(); ++i) {
					short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
					short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");
					if (short1 == enchID) {
						return short2;
					}
				}

				return 0;
			}
		}
	}

	/**+
	 * Return the enchantments for the specified stack.
	 */
	public static IntIntMap getEnchantments(ItemStack stack) {
		IntIntHashMap linkedhashmap = new IntIntHashMap();
		NBTTagList nbttaglist = stack.getItem() == Items.enchanted_book ? Items.enchanted_book.getEnchantments(stack)
				: stack.getEnchantmentTagList();
		if (nbttaglist != null) {
			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
				short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");
				linkedhashmap.put(short1, short2);
			}
		}

		return linkedhashmap;
	}

	/**+
	 * Set the enchantments for the specified stack.
	 */
	public static void setEnchantments(IntIntMap enchMap, ItemStack stack) {
		NBTTagList nbttaglist = new NBTTagList();

		for (IntCursor cur : enchMap.keys()) {
			int i = cur.value;
			Enchantment enchantment = Enchantment.getEnchantmentById(i);
			if (enchantment != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setShort("id", (short) i);
				nbttagcompound.setShort("lvl", (short) ((Integer) enchMap.get(Integer.valueOf(i))).intValue());
				nbttaglist.appendTag(nbttagcompound);
				if (stack.getItem() == Items.enchanted_book) {
					Items.enchanted_book.addEnchantment(stack,
							new EnchantmentData(enchantment, ((Integer) enchMap.get(Integer.valueOf(i))).intValue()));
				}
			}
		}

		if (nbttaglist.tagCount() > 0) {
			if (stack.getItem() != Items.enchanted_book) {
				stack.setTagInfo("ench", nbttaglist);
			}
		} else if (stack.hasTagCompound()) {
			stack.getTagCompound().removeTag("ench");
		}

	}

	/**+
	 * Returns the biggest level of the enchantment on the array of
	 * ItemStack passed.
	 */
	public static int getMaxEnchantmentLevel(int enchID, ItemStack[] stacks) {
		if (stacks == null) {
			return 0;
		} else {
			int i = 0;

			for (int k = 0; k < stacks.length; ++k) {
				int j = getEnchantmentLevel(enchID, stacks[k]);
				if (j > i) {
					i = j;
				}
			}

			return i;
		}
	}

	/**+
	 * Executes the enchantment modifier on the ItemStack passed.
	 */
	private static void applyEnchantmentModifier(EnchantmentHelper.IModifier modifier, ItemStack stack) {
		if (stack != null) {
			NBTTagList nbttaglist = stack.getEnchantmentTagList();
			if (nbttaglist != null) {
				for (int i = 0; i < nbttaglist.tagCount(); ++i) {
					short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
					short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");
					if (Enchantment.getEnchantmentById(short1) != null) {
						modifier.calculateModifier(Enchantment.getEnchantmentById(short1), short2);
					}
				}

			}
		}
	}

	/**+
	 * Executes the enchantment modifier on the array of ItemStack
	 * passed.
	 */
	private static void applyEnchantmentModifierArray(EnchantmentHelper.IModifier modifier, ItemStack[] stacks) {
		for (int k = 0; k < stacks.length; ++k) {
			applyEnchantmentModifier(modifier, stacks[k]);
		}

	}

	/**+
	 * Returns the modifier of protection enchantments on armors
	 * equipped on player.
	 */
	public static int getEnchantmentModifierDamage(ItemStack[] stacks, DamageSource source) {
		enchantmentModifierDamage.damageModifier = 0;
		enchantmentModifierDamage.source = source;
		applyEnchantmentModifierArray(enchantmentModifierDamage, stacks);
		if (enchantmentModifierDamage.damageModifier > 25) {
			enchantmentModifierDamage.damageModifier = 25;
		} else if (enchantmentModifierDamage.damageModifier < 0) {
			enchantmentModifierDamage.damageModifier = 0;
		}

		return (enchantmentModifierDamage.damageModifier + 1 >> 1)
				+ enchantmentRand.nextInt((enchantmentModifierDamage.damageModifier >> 1) + 1);
	}

	public static float func_152377_a(ItemStack parItemStack, EnumCreatureAttribute parEnumCreatureAttribute) {
		enchantmentModifierLiving.livingModifier = 0.0F;
		enchantmentModifierLiving.entityLiving = parEnumCreatureAttribute;
		applyEnchantmentModifier(enchantmentModifierLiving, parItemStack);
		return enchantmentModifierLiving.livingModifier;
	}

	public static void applyThornEnchantments(EntityLivingBase parEntityLivingBase, Entity parEntity) {
		ENCHANTMENT_ITERATOR_HURT.attacker = parEntity;
		ENCHANTMENT_ITERATOR_HURT.user = parEntityLivingBase;
		if (parEntityLivingBase != null) {
			applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_HURT, parEntityLivingBase.getInventory());
		}

		if (parEntity instanceof EntityPlayer) {
			applyEnchantmentModifier(ENCHANTMENT_ITERATOR_HURT, parEntityLivingBase.getHeldItem());
		}

	}

	public static void applyArthropodEnchantments(EntityLivingBase parEntityLivingBase, Entity parEntity) {
		ENCHANTMENT_ITERATOR_DAMAGE.user = parEntityLivingBase;
		ENCHANTMENT_ITERATOR_DAMAGE.target = parEntity;
		if (parEntityLivingBase != null) {
			applyEnchantmentModifierArray(ENCHANTMENT_ITERATOR_DAMAGE, parEntityLivingBase.getInventory());
		}

		if (parEntityLivingBase instanceof EntityPlayer) {
			applyEnchantmentModifier(ENCHANTMENT_ITERATOR_DAMAGE, parEntityLivingBase.getHeldItem());
		}

	}

	/**+
	 * Returns the Knockback modifier of the enchantment on the
	 * players held item.
	 */
	public static int getKnockbackModifier(EntityLivingBase player) {
		/**+
		 * Returns the level of enchantment on the ItemStack passed.
		 */
		return getEnchantmentLevel(Enchantment.knockback.effectId, player.getHeldItem());
	}

	/**+
	 * Returns the fire aspect modifier of the players held item.
	 */
	public static int getFireAspectModifier(EntityLivingBase player) {
		/**+
		 * Returns the level of enchantment on the ItemStack passed.
		 */
		return getEnchantmentLevel(Enchantment.fireAspect.effectId, player.getHeldItem());
	}

	/**+
	 * Returns the 'Water Breathing' modifier of enchantments on
	 * player equipped armors.
	 */
	public static int getRespiration(Entity player) {
		/**+
		 * Returns the biggest level of the enchantment on the array of
		 * ItemStack passed.
		 */
		return getMaxEnchantmentLevel(Enchantment.respiration.effectId, player.getInventory());
	}

	/**+
	 * Returns the level of the Depth Strider enchantment.
	 */
	public static int getDepthStriderModifier(Entity player) {
		/**+
		 * Returns the biggest level of the enchantment on the array of
		 * ItemStack passed.
		 */
		return getMaxEnchantmentLevel(Enchantment.depthStrider.effectId, player.getInventory());
	}

	/**+
	 * Return the extra efficiency of tools based on enchantments on
	 * equipped player item.
	 */
	public static int getEfficiencyModifier(EntityLivingBase player) {
		/**+
		 * Returns the level of enchantment on the ItemStack passed.
		 */
		return getEnchantmentLevel(Enchantment.efficiency.effectId, player.getHeldItem());
	}

	/**+
	 * Returns the silk touch status of enchantments on current
	 * equipped item of player.
	 */
	public static boolean getSilkTouchModifier(EntityLivingBase player) {
		/**+
		 * Returns the level of enchantment on the ItemStack passed.
		 */
		return getEnchantmentLevel(Enchantment.silkTouch.effectId, player.getHeldItem()) > 0;
	}

	/**+
	 * Returns the fortune enchantment modifier of the current
	 * equipped item of player.
	 */
	public static int getFortuneModifier(EntityLivingBase player) {
		/**+
		 * Returns the level of enchantment on the ItemStack passed.
		 */
		return getEnchantmentLevel(Enchantment.fortune.effectId, player.getHeldItem());
	}

	/**+
	 * Returns the level of the 'Luck Of The Sea' enchantment.
	 */
	public static int getLuckOfSeaModifier(EntityLivingBase player) {
		/**+
		 * Returns the level of enchantment on the ItemStack passed.
		 */
		return getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, player.getHeldItem());
	}

	/**+
	 * Returns the level of the 'Lure' enchantment on the players
	 * held item.
	 */
	public static int getLureModifier(EntityLivingBase player) {
		/**+
		 * Returns the level of enchantment on the ItemStack passed.
		 */
		return getEnchantmentLevel(Enchantment.lure.effectId, player.getHeldItem());
	}

	/**+
	 * Returns the looting enchantment modifier of the current
	 * equipped item of player.
	 */
	public static int getLootingModifier(EntityLivingBase player) {
		/**+
		 * Returns the level of enchantment on the ItemStack passed.
		 */
		return getEnchantmentLevel(Enchantment.looting.effectId, player.getHeldItem());
	}

	/**+
	 * Returns the aqua affinity status of enchantments on current
	 * equipped item of player.
	 */
	public static boolean getAquaAffinityModifier(EntityLivingBase player) {
		/**+
		 * Returns the biggest level of the enchantment on the array of
		 * ItemStack passed.
		 */
		return getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, player.getInventory()) > 0;
	}

	public static ItemStack getEnchantedItem(Enchantment parEnchantment, EntityLivingBase parEntityLivingBase) {
		ItemStack[] stacks = parEntityLivingBase.getInventory();
		for (int k = 0; k < stacks.length; ++k) {
			ItemStack itemstack = stacks[k];
			if (itemstack != null && getEnchantmentLevel(parEnchantment.effectId, itemstack) > 0) {
				return itemstack;
			}
		}

		return null;
	}

	/**+
	 * Returns the enchantability of itemstack, it's uses a singular
	 * formula for each index (2nd parameter: 0, 1 and 2), cutting
	 * to the max enchantability power of the table (3rd parameter)
	 */
	public static int calcItemStackEnchantability(EaglercraftRandom parRandom, int parInt1, int parInt2,
			ItemStack parItemStack) {
		Item item = parItemStack.getItem();
		int i = item.getItemEnchantability();
		if (i <= 0) {
			return 0;
		} else {
			if (parInt2 > 15) {
				parInt2 = 15;
			}

			int j = parRandom.nextInt(8) + 1 + (parInt2 >> 1) + parRandom.nextInt(parInt2 + 1);
			return parInt1 == 0 ? Math.max(j / 3, 1) : (parInt1 == 1 ? j * 2 / 3 + 1 : Math.max(j, parInt2 * 2));
		}
	}

	/**+
	 * Adds a random enchantment to the specified item. Args:
	 * random, itemStack, enchantabilityLevel
	 */
	public static ItemStack addRandomEnchantment(EaglercraftRandom parRandom, ItemStack parItemStack, int parInt1) {
		List<EnchantmentData> list = buildEnchantmentList(parRandom, parItemStack, parInt1);
		boolean flag = parItemStack.getItem() == Items.book;
		if (flag) {
			parItemStack.setItem(Items.enchanted_book);
		}

		if (list != null) {
			for (int i = 0, l = list.size(); i < l; ++i) {
				EnchantmentData enchantmentdata = list.get(i);
				if (flag) {
					Items.enchanted_book.addEnchantment(parItemStack, enchantmentdata);
				} else {
					parItemStack.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
				}
			}
		}

		return parItemStack;
	}

	/**+
	 * Create a list of random EnchantmentData (enchantments) that
	 * can be added together to the ItemStack, the 3rd parameter is
	 * the total enchantability level.
	 */
	public static List<EnchantmentData> buildEnchantmentList(EaglercraftRandom randomIn, ItemStack itemStackIn,
			int parInt1) {
		Item item = itemStackIn.getItem();
		int i = item.getItemEnchantability();
		if (i <= 0) {
			return null;
		} else {
			i = i / 2;
			i = 1 + randomIn.nextInt((i >> 1) + 1) + randomIn.nextInt((i >> 1) + 1);
			int j = i + parInt1;
			float f = (randomIn.nextFloat() + randomIn.nextFloat() - 1.0F) * 0.15F;
			int k = (int) ((float) j * (1.0F + f) + 0.5F);
			if (k < 1) {
				k = 1;
			}

			ArrayList<EnchantmentData> arraylist = null;
			Map map = mapEnchantmentData(k, itemStackIn);
			if (map != null && !map.isEmpty()) {
				EnchantmentData enchantmentdata = (EnchantmentData) WeightedRandom.getRandomItem(randomIn,
						map.values());
				if (enchantmentdata != null) {
					arraylist = Lists.newArrayList();
					arraylist.add(enchantmentdata);

					for (int l = k; randomIn.nextInt(50) <= l; l >>= 1) {
						Iterator iterator = map.keySet().iterator();

						while (iterator.hasNext()) {
							Integer integer = (Integer) iterator.next();
							boolean flag = true;

							for (int m = 0, n = arraylist.size(); m < n; ++m) {
								EnchantmentData enchantmentdata1 = arraylist.get(m);
								if (!enchantmentdata1.enchantmentobj
										.canApplyTogether(Enchantment.getEnchantmentById(integer.intValue()))) {
									flag = false;
									break;
								}
							}

							if (!flag) {
								iterator.remove();
							}
						}

						if (!map.isEmpty()) {
							EnchantmentData enchantmentdata2 = (EnchantmentData) WeightedRandom.getRandomItem(randomIn,
									map.values());
							arraylist.add(enchantmentdata2);
						}
					}
				}
			}

			return arraylist;
		}
	}

	public static Map<Integer, EnchantmentData> mapEnchantmentData(int parInt1, ItemStack parItemStack) {
		Item item = parItemStack.getItem();
		HashMap hashmap = null;
		boolean flag = parItemStack.getItem() == Items.book;

		for (int j = 0; j < Enchantment.enchantmentsBookList.length; ++j) {
			Enchantment enchantment = Enchantment.enchantmentsBookList[j];
			if (enchantment != null && (enchantment.type.canEnchantItem(item) || flag)) {
				for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
					if (parInt1 >= enchantment.getMinEnchantability(i)
							&& parInt1 <= enchantment.getMaxEnchantability(i)) {
						if (hashmap == null) {
							hashmap = Maps.newHashMap();
						}

						hashmap.put(Integer.valueOf(enchantment.effectId), new EnchantmentData(enchantment, i));
					}
				}
			}
		}

		return hashmap;
	}

	static final class DamageIterator implements EnchantmentHelper.IModifier {
		public EntityLivingBase user;
		public Entity target;

		private DamageIterator() {
		}

		public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
			enchantmentIn.onEntityDamaged(this.user, this.target, enchantmentLevel);
		}
	}

	static final class HurtIterator implements EnchantmentHelper.IModifier {
		public EntityLivingBase user;
		public Entity attacker;

		private HurtIterator() {
		}

		public void calculateModifier(Enchantment enchantment, int i) {
			enchantment.onUserHurt(this.user, this.attacker, i);
		}
	}

	interface IModifier {
		void calculateModifier(Enchantment var1, int var2);
	}

	static final class ModifierDamage implements EnchantmentHelper.IModifier {
		public int damageModifier;
		public DamageSource source;

		private ModifierDamage() {
		}

		public void calculateModifier(Enchantment enchantment, int i) {
			this.damageModifier += enchantment.calcModifierDamage(i, this.source);
		}
	}

	static final class ModifierLiving implements EnchantmentHelper.IModifier {
		public float livingModifier;
		public EnumCreatureAttribute entityLiving;

		private ModifierLiving() {
		}

		public void calculateModifier(Enchantment enchantment, int i) {
			this.livingModifier += enchantment.calcDamageByCreature(i, this.entityLiving);
		}
	}
}