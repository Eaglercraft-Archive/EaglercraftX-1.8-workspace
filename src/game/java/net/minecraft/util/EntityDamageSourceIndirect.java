package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
public class EntityDamageSourceIndirect extends EntityDamageSource {
	private Entity indirectEntity;

	public EntityDamageSourceIndirect(String parString1, Entity parEntity, Entity indirectEntityIn) {
		super(parString1, parEntity);
		this.indirectEntity = indirectEntityIn;
	}

	public Entity getSourceOfDamage() {
		return this.damageSourceEntity;
	}

	public Entity getEntity() {
		return this.indirectEntity;
	}

	/**+
	 * Gets the death message that is displayed when the player dies
	 */
	public IChatComponent getDeathMessage(EntityLivingBase entitylivingbase) {
		IChatComponent ichatcomponent = this.indirectEntity == null ? this.damageSourceEntity.getDisplayName()
				: this.indirectEntity.getDisplayName();
		ItemStack itemstack = this.indirectEntity instanceof EntityLivingBase
				? ((EntityLivingBase) this.indirectEntity).getHeldItem()
				: null;
		String s = "death.attack." + this.damageType;
		String s1 = s + ".item";
		return itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1)
				? new ChatComponentTranslation(s1,
						new Object[] { entitylivingbase.getDisplayName(), ichatcomponent,
								itemstack.getChatComponent() })
				: new ChatComponentTranslation(s, new Object[] { entitylivingbase.getDisplayName(), ichatcomponent });
	}
}