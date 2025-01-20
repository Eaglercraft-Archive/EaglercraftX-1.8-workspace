package net.minecraft.enchantment;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

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
public class EnchantmentThorns extends Enchantment {
	public EnchantmentThorns(int parInt1, ResourceLocation parResourceLocation, int parInt2) {
		super(parInt1, parResourceLocation, parInt2, EnumEnchantmentType.ARMOR_TORSO);
		this.setName("thorns");
	}

	/**+
	 * Returns the minimal value of enchantability needed on the
	 * enchantment level passed.
	 */
	public int getMinEnchantability(int i) {
		return 10 + 20 * (i - 1);
	}

	/**+
	 * Returns the maximum value of enchantability nedded on the
	 * enchantment level passed.
	 */
	public int getMaxEnchantability(int i) {
		return super.getMinEnchantability(i) + 50;
	}

	/**+
	 * Returns the maximum level that the enchantment can have.
	 */
	public int getMaxLevel() {
		return 3;
	}

	/**+
	 * Determines if this enchantment can be applied to a specific
	 * ItemStack.
	 */
	public boolean canApply(ItemStack itemstack) {
		return itemstack.getItem() instanceof ItemArmor ? true : super.canApply(itemstack);
	}

	/**+
	 * Whenever an entity that has this enchantment on one of its
	 * associated items is damaged this method will be called.
	 */
	public void onUserHurt(EntityLivingBase entitylivingbase, Entity entity, int i) {
		EaglercraftRandom random = entitylivingbase.getRNG();
		ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantment.thorns, entitylivingbase);
		if (func_92094_a(i, random)) {
			if (entity != null) {
				entity.attackEntityFrom(DamageSource.causeThornsDamage(entitylivingbase),
						(float) func_92095_b(i, random));
				entity.playSound("damage.thorns", 0.5F, 1.0F);
			}

			if (itemstack != null) {
				itemstack.damageItem(3, entitylivingbase);
			}
		} else if (itemstack != null) {
			itemstack.damageItem(1, entitylivingbase);
		}

	}

	public static boolean func_92094_a(int parInt1, EaglercraftRandom parRandom) {
		return parInt1 <= 0 ? false : parRandom.nextFloat() < 0.15F * (float) parInt1;
	}

	public static int func_92095_b(int parInt1, EaglercraftRandom parRandom) {
		return parInt1 > 10 ? parInt1 - 10 : 1 + parRandom.nextInt(4);
	}
}