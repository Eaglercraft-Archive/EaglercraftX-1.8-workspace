package net.minecraft.util;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.enchantment.EnchantmentHelper;
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
public class WeightedRandomFishable extends WeightedRandom.Item {
	private final ItemStack returnStack;
	private float maxDamagePercent;
	private boolean enchantable;

	public WeightedRandomFishable(ItemStack returnStackIn, int itemWeightIn) {
		super(itemWeightIn);
		this.returnStack = returnStackIn;
	}

	public ItemStack getItemStack(EaglercraftRandom random) {
		ItemStack itemstack = this.returnStack.copy();
		if (this.maxDamagePercent > 0.0F) {
			int i = (int) (this.maxDamagePercent * (float) this.returnStack.getMaxDamage());
			int j = itemstack.getMaxDamage() - random.nextInt(random.nextInt(i) + 1);
			if (j > i) {
				j = i;
			}

			if (j < 1) {
				j = 1;
			}

			itemstack.setItemDamage(j);
		}

		if (this.enchantable) {
			EnchantmentHelper.addRandomEnchantment(random, itemstack, 30);
		}

		return itemstack;
	}

	public WeightedRandomFishable setMaxDamagePercent(float maxDamagePercentIn) {
		this.maxDamagePercent = maxDamagePercentIn;
		return this;
	}

	public WeightedRandomFishable setEnchantable() {
		this.enchantable = true;
		return this;
	}
}