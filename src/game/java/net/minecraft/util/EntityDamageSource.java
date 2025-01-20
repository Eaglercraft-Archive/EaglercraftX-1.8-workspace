package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
public class EntityDamageSource extends DamageSource {
	protected Entity damageSourceEntity;
	/**+
	 * Whether this EntityDamageSource is from an entity wearing
	 * Thorns-enchanted armor.
	 */
	private boolean isThornsDamage = false;

	public EntityDamageSource(String parString1, Entity damageSourceEntityIn) {
		super(parString1);
		this.damageSourceEntity = damageSourceEntityIn;
	}

	/**+
	 * Sets this EntityDamageSource as originating from Thorns armor
	 */
	public EntityDamageSource setIsThornsDamage() {
		this.isThornsDamage = true;
		return this;
	}

	public boolean getIsThornsDamage() {
		return this.isThornsDamage;
	}

	public Entity getEntity() {
		return this.damageSourceEntity;
	}

	/**+
	 * Gets the death message that is displayed when the player dies
	 */
	public IChatComponent getDeathMessage(EntityLivingBase entitylivingbase) {
		ItemStack itemstack = this.damageSourceEntity instanceof EntityLivingBase
				? ((EntityLivingBase) this.damageSourceEntity).getHeldItem()
				: null;
		String s = "death.attack." + this.damageType;
		String s1 = s + ".item";
		return itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1)
				? new ChatComponentTranslation(s1,
						new Object[] { entitylivingbase.getDisplayName(), this.damageSourceEntity.getDisplayName(),
								itemstack.getChatComponent() })
				: new ChatComponentTranslation(s,
						new Object[] { entitylivingbase.getDisplayName(), this.damageSourceEntity.getDisplayName() });
	}

	/**+
	 * Return whether this damage source will have its damage amount
	 * scaled based on the current difficulty.
	 */
	public boolean isDifficultyScaled() {
		return this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase
				&& !(this.damageSourceEntity instanceof EntityPlayer);
	}
}