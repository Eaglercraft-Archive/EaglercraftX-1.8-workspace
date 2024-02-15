package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.enchantment;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.enchantment.Enchantment;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.enchantment.EnumEnchantmentType;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.ResourceLocation;

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
public class EnchantmentFishingSpeed extends Enchantment {

	static {
		__checkIntegratedContextValid("net/minecraft/enchantment/EnchantmentFishingSpeed");
	}

	protected EnchantmentFishingSpeed(int enchID, ResourceLocation enchName, int enchWeight,
			EnumEnchantmentType enchType) {
		super(enchID, enchName, enchWeight, enchType);
		this.setName("fishingSpeed");
	}

	/**+
	 * Returns the minimal value of enchantability needed on the
	 * enchantment level passed.
	 */
	public int getMinEnchantability(int i) {
		return 15 + (i - 1) * 9;
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
}