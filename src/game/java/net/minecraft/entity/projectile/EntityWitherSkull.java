package net.minecraft.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

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
public class EntityWitherSkull extends EntityFireball {
	public EntityWitherSkull(World worldIn) {
		super(worldIn);
		this.setSize(0.3125F, 0.3125F);
	}

	public EntityWitherSkull(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ);
		this.setSize(0.3125F, 0.3125F);
	}

	/**+
	 * Return the motion factor for this projectile. The factor is
	 * multiplied by the original motion.
	 */
	protected float getMotionFactor() {
		return this.isInvulnerable() ? 0.73F : super.getMotionFactor();
	}

	public EntityWitherSkull(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
		super(worldIn, x, y, z, accelX, accelY, accelZ);
		this.setSize(0.3125F, 0.3125F);
	}

	/**+
	 * Returns true if the entity is on fire. Used by render to add
	 * the fire effect on rendering.
	 */
	public boolean isBurning() {
		return false;
	}

	/**+
	 * Explosion resistance of a block relative to this entity
	 */
	public float getExplosionResistance(Explosion explosion, World world, BlockPos blockpos, IBlockState iblockstate) {
		float f = super.getExplosionResistance(explosion, world, blockpos, iblockstate);
		Block block = iblockstate.getBlock();
		if (this.isInvulnerable() && EntityWither.func_181033_a(block)) {
			f = Math.min(0.8F, f);
		}

		return f;
	}

	/**+
	 * Called when this EntityFireball hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		if (!this.worldObj.isRemote) {
			if (movingobjectposition.entityHit != null) {
				if (this.shootingEntity != null) {
					if (movingobjectposition.entityHit
							.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0F)) {
						if (!movingobjectposition.entityHit.isEntityAlive()) {
							this.shootingEntity.heal(5.0F);
						} else {
							this.applyEnchantments(this.shootingEntity, movingobjectposition.entityHit);
						}
					}
				} else {
					movingobjectposition.entityHit.attackEntityFrom(DamageSource.magic, 5.0F);
				}

				if (movingobjectposition.entityHit instanceof EntityLivingBase) {
					byte b0 = 0;
					if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
						b0 = 10;
					} else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
						b0 = 40;
					}

					if (b0 > 0) {
						((EntityLivingBase) movingobjectposition.entityHit)
								.addPotionEffect(new PotionEffect(Potion.wither.id, 20 * b0, 1));
					}
				}
			}

			this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false,
					this.worldObj.getGameRules().getBoolean("mobGriefing"));
			this.setDead();
		}

	}

	/**+
	 * Returns true if other Entities should be prevented from
	 * moving through this Entity.
	 */
	public boolean canBeCollidedWith() {
		return false;
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource var1, float var2) {
		return false;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(10, Byte.valueOf((byte) 0));
	}

	/**+
	 * Return whether this skull comes from an invulnerable (aura)
	 * wither boss.
	 */
	public boolean isInvulnerable() {
		return this.dataWatcher.getWatchableObjectByte(10) == 1;
	}

	/**+
	 * Set whether this skull comes from an invulnerable (aura)
	 * wither boss.
	 */
	public void setInvulnerable(boolean invulnerable) {
		this.dataWatcher.updateObject(10, Byte.valueOf((byte) (invulnerable ? 1 : 0)));
	}
}