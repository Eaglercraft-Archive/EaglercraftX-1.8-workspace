package net.minecraft.util;

import net.minecraft.entity.EntityLivingBase;

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
public class CombatEntry {
	private final DamageSource damageSrc;
	private final int field_94567_b;
	private final float damage;
	private final float health;
	private final String field_94566_e;
	private final float fallDistance;

	public CombatEntry(DamageSource damageSrcIn, int parInt1, float healthAmount, float damageAmount, String parString1,
			float fallDistanceIn) {
		this.damageSrc = damageSrcIn;
		this.field_94567_b = parInt1;
		this.damage = damageAmount;
		this.health = healthAmount;
		this.field_94566_e = parString1;
		this.fallDistance = fallDistanceIn;
	}

	/**+
	 * Get the DamageSource of the CombatEntry instance.
	 */
	public DamageSource getDamageSrc() {
		return this.damageSrc;
	}

	public float func_94563_c() {
		return this.damage;
	}

	/**+
	 * Returns true if {@link
	 * net.minecraft.util.DamageSource#getEntity() damage source} is
	 * a living entity
	 */
	public boolean isLivingDamageSrc() {
		return this.damageSrc.getEntity() instanceof EntityLivingBase;
	}

	public String func_94562_g() {
		return this.field_94566_e;
	}

	public IChatComponent getDamageSrcDisplayName() {
		return this.getDamageSrc().getEntity() == null ? null : this.getDamageSrc().getEntity().getDisplayName();
	}

	public float getDamageAmount() {
		return this.damageSrc == DamageSource.outOfWorld ? Float.MAX_VALUE : this.fallDistance;
	}
}