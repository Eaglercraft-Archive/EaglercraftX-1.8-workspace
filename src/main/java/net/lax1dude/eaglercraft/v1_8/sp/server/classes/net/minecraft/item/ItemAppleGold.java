package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.creativetab.CreativeTabs;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.EnumRarity;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.Item;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemFood;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemStack;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.potion.Potion;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.potion.PotionEffect;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.World;

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
public class ItemAppleGold extends ItemFood {

	static {
		__checkIntegratedContextValid("net/minecraft/item/ItemAppleGold");
	}

	public ItemAppleGold(int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
		this.setHasSubtypes(true);
	}

	public boolean hasEffect(ItemStack itemstack) {
		return itemstack.getMetadata() > 0;
	}

	/**+
	 * Return an item rarity from EnumRarity
	 */
	public EnumRarity getRarity(ItemStack itemstack) {
		return itemstack.getMetadata() == 0 ? EnumRarity.RARE : EnumRarity.EPIC;
	}

	protected void onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		entityplayer.addPotionEffect(new PotionEffect(Potion.absorption.id, 2400, 0));

		if (itemstack.getMetadata() > 0) {
			{
				entityplayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 4));
				entityplayer.addPotionEffect(new PotionEffect(Potion.resistance.id, 6000, 0));
				entityplayer.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6000, 0));
			}
		} else {
			super.onFoodEaten(itemstack, world, entityplayer);
		}

	}

	/**+
	 * returns a list of items with the same ID, but different meta
	 * (eg: dye returns 16 items)
	 */
	public void getSubItems(Item item, CreativeTabs var2, List<ItemStack> list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
	}
}