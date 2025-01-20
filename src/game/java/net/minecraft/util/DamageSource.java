package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.world.Explosion;

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
public class DamageSource {
	public static DamageSource inFire = (new DamageSource("inFire")).setFireDamage();
	public static DamageSource lightningBolt = new DamageSource("lightningBolt");
	public static DamageSource onFire = (new DamageSource("onFire")).setDamageBypassesArmor().setFireDamage();
	public static DamageSource lava = (new DamageSource("lava")).setFireDamage();
	public static DamageSource inWall = (new DamageSource("inWall")).setDamageBypassesArmor();
	public static DamageSource drown = (new DamageSource("drown")).setDamageBypassesArmor();
	public static DamageSource starve = (new DamageSource("starve")).setDamageBypassesArmor().setDamageIsAbsolute();
	public static DamageSource cactus = new DamageSource("cactus");
	public static DamageSource fall = (new DamageSource("fall")).setDamageBypassesArmor();
	public static DamageSource outOfWorld = (new DamageSource("outOfWorld")).setDamageBypassesArmor()
			.setDamageAllowedInCreativeMode();
	public static DamageSource generic = (new DamageSource("generic")).setDamageBypassesArmor();
	public static DamageSource magic = (new DamageSource("magic")).setDamageBypassesArmor().setMagicDamage();
	public static DamageSource wither = (new DamageSource("wither")).setDamageBypassesArmor();
	public static DamageSource anvil = new DamageSource("anvil");
	public static DamageSource fallingBlock = new DamageSource("fallingBlock");
	private boolean isUnblockable;
	private boolean isDamageAllowedInCreativeMode;
	private boolean damageIsAbsolute;
	private float hungerDamage = 0.3F;
	private boolean fireDamage;
	private boolean projectile;
	private boolean difficultyScaled;
	private boolean magicDamage;
	private boolean explosion;
	public String damageType;

	public static DamageSource causeMobDamage(EntityLivingBase mob) {
		return new EntityDamageSource("mob", mob);
	}

	/**+
	 * returns an EntityDamageSource of type player
	 */
	public static DamageSource causePlayerDamage(EntityPlayer player) {
		return new EntityDamageSource("player", player);
	}

	/**+
	 * returns EntityDamageSourceIndirect of an arrow
	 */
	public static DamageSource causeArrowDamage(EntityArrow arrow, Entity parEntity) {
		return (new EntityDamageSourceIndirect("arrow", arrow, parEntity)).setProjectile();
	}

	/**+
	 * returns EntityDamageSourceIndirect of a fireball
	 */
	public static DamageSource causeFireballDamage(EntityFireball fireball, Entity parEntity) {
		return parEntity == null
				? (new EntityDamageSourceIndirect("onFire", fireball, fireball)).setFireDamage().setProjectile()
				: (new EntityDamageSourceIndirect("fireball", fireball, parEntity)).setFireDamage().setProjectile();
	}

	public static DamageSource causeThrownDamage(Entity parEntity, Entity parEntity2) {
		return (new EntityDamageSourceIndirect("thrown", parEntity, parEntity2)).setProjectile();
	}

	public static DamageSource causeIndirectMagicDamage(Entity parEntity, Entity parEntity2) {
		return (new EntityDamageSourceIndirect("indirectMagic", parEntity, parEntity2)).setDamageBypassesArmor()
				.setMagicDamage();
	}

	/**+
	 * Returns the EntityDamageSource of the Thorns enchantment
	 */
	public static DamageSource causeThornsDamage(Entity parEntity) {
		return (new EntityDamageSource("thorns", parEntity)).setIsThornsDamage().setMagicDamage();
	}

	public static DamageSource setExplosionSource(Explosion explosionIn) {
		return explosionIn != null && explosionIn.getExplosivePlacedBy() != null
				? (new EntityDamageSource("explosion.player", explosionIn.getExplosivePlacedBy())).setDifficultyScaled()
						.setExplosion()
				: (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
	}

	/**+
	 * Returns true if the damage is projectile based.
	 */
	public boolean isProjectile() {
		return this.projectile;
	}

	/**+
	 * Define the damage type as projectile based.
	 */
	public DamageSource setProjectile() {
		this.projectile = true;
		return this;
	}

	public boolean isExplosion() {
		return this.explosion;
	}

	public DamageSource setExplosion() {
		this.explosion = true;
		return this;
	}

	public boolean isUnblockable() {
		return this.isUnblockable;
	}

	/**+
	 * How much satiate(food) is consumed by this DamageSource
	 */
	public float getHungerDamage() {
		return this.hungerDamage;
	}

	public boolean canHarmInCreative() {
		return this.isDamageAllowedInCreativeMode;
	}

	/**+
	 * Whether or not the damage ignores modification by potion
	 * effects or enchantments.
	 */
	public boolean isDamageAbsolute() {
		return this.damageIsAbsolute;
	}

	protected DamageSource(String damageTypeIn) {
		this.damageType = damageTypeIn;
	}

	public Entity getSourceOfDamage() {
		return this.getEntity();
	}

	public Entity getEntity() {
		return null;
	}

	protected DamageSource setDamageBypassesArmor() {
		this.isUnblockable = true;
		this.hungerDamage = 0.0F;
		return this;
	}

	protected DamageSource setDamageAllowedInCreativeMode() {
		this.isDamageAllowedInCreativeMode = true;
		return this;
	}

	/**+
	 * Sets a value indicating whether the damage is absolute
	 * (ignores modification by potion effects or enchantments), and
	 * also clears out hunger damage.
	 */
	protected DamageSource setDamageIsAbsolute() {
		this.damageIsAbsolute = true;
		this.hungerDamage = 0.0F;
		return this;
	}

	/**+
	 * Define the damage type as fire based.
	 */
	protected DamageSource setFireDamage() {
		this.fireDamage = true;
		return this;
	}

	/**+
	 * Gets the death message that is displayed when the player dies
	 */
	public IChatComponent getDeathMessage(EntityLivingBase parEntityLivingBase) {
		EntityLivingBase entitylivingbase = parEntityLivingBase.func_94060_bK();
		String s = "death.attack." + this.damageType;
		String s1 = s + ".player";
		return entitylivingbase != null && StatCollector.canTranslate(s1)
				? new ChatComponentTranslation(s1,
						new Object[] { parEntityLivingBase.getDisplayName(), entitylivingbase.getDisplayName() })
				: new ChatComponentTranslation(s, new Object[] { parEntityLivingBase.getDisplayName() });
	}

	/**+
	 * Returns true if the damage is fire based.
	 */
	public boolean isFireDamage() {
		return this.fireDamage;
	}

	/**+
	 * Return the name of damage type.
	 */
	public String getDamageType() {
		return this.damageType;
	}

	/**+
	 * Set whether this damage source will have its damage amount
	 * scaled based on the current difficulty.
	 */
	public DamageSource setDifficultyScaled() {
		this.difficultyScaled = true;
		return this;
	}

	/**+
	 * Return whether this damage source will have its damage amount
	 * scaled based on the current difficulty.
	 */
	public boolean isDifficultyScaled() {
		return this.difficultyScaled;
	}

	/**+
	 * Returns true if the damage is magic based.
	 */
	public boolean isMagicDamage() {
		return this.magicDamage;
	}

	/**+
	 * Define the damage type as magic based.
	 */
	public DamageSource setMagicDamage() {
		this.magicDamage = true;
		return this;
	}

	public boolean isCreativePlayer() {
		Entity entity = this.getEntity();
		return entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode;
	}
}