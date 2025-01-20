package net.minecraft.util;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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
public final class EntitySelectors {
	public static final Predicate<Entity> selectAnything = new Predicate<Entity>() {
		public boolean apply(Entity entity) {
			return entity.isEntityAlive();
		}
	};
	/**+
	 * Selects only entities which are neither ridden by anything
	 * nor ride on anything
	 */
	public static final Predicate<Entity> IS_STANDALONE = new Predicate<Entity>() {
		public boolean apply(Entity entity) {
			return entity.isEntityAlive() && entity.riddenByEntity == null && entity.ridingEntity == null;
		}
	};
	public static final Predicate<Entity> selectInventories = new Predicate<Entity>() {
		public boolean apply(Entity entity) {
			return entity instanceof IInventory && entity.isEntityAlive();
		}
	};
	/**+
	 * Selects entities which are either not players or players that
	 * are not spectating
	 */
	public static final Predicate<Entity> NOT_SPECTATING = new Predicate<Entity>() {
		public boolean apply(Entity entity) {
			return !(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator();
		}
	};

	public static class ArmoredMob implements Predicate<Entity> {
		private final ItemStack armor;

		public ArmoredMob(ItemStack armor) {
			this.armor = armor;
		}

		public boolean apply(Entity entity) {
			if (!entity.isEntityAlive()) {
				return false;
			} else if (!(entity instanceof EntityLivingBase)) {
				return false;
			} else {
				EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
				return entitylivingbase.getEquipmentInSlot(EntityLiving.getArmorPosition(this.armor)) != null ? false
						: (entitylivingbase instanceof EntityLiving ? ((EntityLiving) entitylivingbase).canPickUpLoot()
								: (entitylivingbase instanceof EntityArmorStand ? true
										: entitylivingbase instanceof EntityPlayer));
			}
		}
	}
}