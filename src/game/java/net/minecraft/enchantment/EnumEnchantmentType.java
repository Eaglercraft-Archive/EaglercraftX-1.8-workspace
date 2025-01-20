package net.minecraft.enchantment;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

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
public enum EnumEnchantmentType {
	ALL, ARMOR, ARMOR_FEET, ARMOR_LEGS, ARMOR_TORSO, ARMOR_HEAD, WEAPON, DIGGER, FISHING_ROD, BREAKABLE, BOW;

	/**+
	 * Return true if the item passed can be enchanted by a
	 * enchantment of this type.
	 */
	public boolean canEnchantItem(Item parItem) {
		if (this == ALL) {
			return true;
		} else if (this == BREAKABLE && parItem.isDamageable()) {
			return true;
		} else if (parItem instanceof ItemArmor) {
			if (this == ARMOR) {
				return true;
			} else {
				ItemArmor itemarmor = (ItemArmor) parItem;
				return itemarmor.armorType == 0 ? this == ARMOR_HEAD
						: (itemarmor.armorType == 2 ? this == ARMOR_LEGS
								: (itemarmor.armorType == 1 ? this == ARMOR_TORSO
										: (itemarmor.armorType == 3 ? this == ARMOR_FEET : false)));
			}
		} else {
			return parItem instanceof ItemSword ? this == WEAPON
					: (parItem instanceof ItemTool ? this == DIGGER
							: (parItem instanceof ItemBow ? this == BOW
									: (parItem instanceof ItemFishingRod ? this == FISHING_ROD : false)));
		}
	}
}