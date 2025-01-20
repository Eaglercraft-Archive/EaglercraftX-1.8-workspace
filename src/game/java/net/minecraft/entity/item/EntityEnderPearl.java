package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
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
public class EntityEnderPearl extends EntityThrowable {
	private EntityLivingBase field_181555_c;

	public EntityEnderPearl(World parWorld) {
		super(parWorld);
	}

	public EntityEnderPearl(World worldIn, EntityLivingBase parEntityLivingBase) {
		super(worldIn, parEntityLivingBase);
		this.field_181555_c = parEntityLivingBase;
	}

	public EntityEnderPearl(World worldIn, double parDouble1, double parDouble2, double parDouble3) {
		super(worldIn, parDouble1, parDouble2, parDouble3);
	}

	/**+
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		EntityLivingBase entitylivingbase = this.getThrower();
		if (movingobjectposition.entityHit != null) {
			if (movingobjectposition.entityHit == this.field_181555_c) {
				return;
			}

			movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase),
					0.0F);
		}

		for (int i = 0; i < 32; ++i) {
			this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D,
					this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);
		}

		if (!this.worldObj.isRemote) {
			if (entitylivingbase instanceof EntityPlayerMP) {
				EntityPlayerMP entityplayermp = (EntityPlayerMP) entitylivingbase;
				if (entityplayermp.playerNetServerHandler.getNetworkManager().isChannelOpen()
						&& entityplayermp.worldObj == this.worldObj && !entityplayermp.isPlayerSleeping()) {
					if (this.rand.nextFloat() < 0.05F && this.worldObj.getGameRules().getBoolean("doMobSpawning")) {
						EntityEndermite entityendermite = new EntityEndermite(this.worldObj);
						entityendermite.setSpawnedByPlayer(true);
						entityendermite.setLocationAndAngles(entitylivingbase.posX, entitylivingbase.posY,
								entitylivingbase.posZ, entitylivingbase.rotationYaw, entitylivingbase.rotationPitch);
						this.worldObj.spawnEntityInWorld(entityendermite);
					}

					if (entitylivingbase.isRiding()) {
						entitylivingbase.mountEntity((Entity) null);
					}

					entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
					entitylivingbase.fallDistance = 0.0F;
					entitylivingbase.attackEntityFrom(DamageSource.fall, 5.0F);
				}
			} else if (entitylivingbase != null) {
				entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
				entitylivingbase.fallDistance = 0.0F;
			}

			this.setDead();
		}

	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		EntityLivingBase entitylivingbase = this.getThrower();
		if (entitylivingbase != null && entitylivingbase instanceof EntityPlayer && !entitylivingbase.isEntityAlive()) {
			this.setDead();
		} else {
			super.onUpdate();
		}

	}
}