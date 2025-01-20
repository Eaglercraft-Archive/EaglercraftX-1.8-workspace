package net.minecraft.entity.passive;

import org.apache.commons.lang3.StringUtils;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
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
public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable {

	protected EntityAISit aiSit = new EntityAISit(this);

	public EntityTameable(World worldIn) {
		super(worldIn);
		this.setupTamedAI();
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
		this.dataWatcher.addObject(17, "");
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		if (worldObj.isRemote && !SingleplayerServerController.isClientInEaglerSingleplayerOrLAN()) {
			if (this.getOwnerId() == null) {
				nbttagcompound.setString("OwnerUUID", "");
			} else {
				nbttagcompound.setString("OwnerUUID", this.getOwnerId());
			}
		} else {
			if (this.getOwnerId() == null) {
				nbttagcompound.setString("Owner", "");
			} else {
				nbttagcompound.setString("Owner", this.getOwnerId());
			}
		}

		nbttagcompound.setBoolean("Sitting", this.isSitting());
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		String s = "";
		if (worldObj.isRemote && !SingleplayerServerController.isClientInEaglerSingleplayerOrLAN()) {
			if (nbttagcompound.hasKey("OwnerUUID", 8)) {
				s = nbttagcompound.getString("OwnerUUID");
			}
		} else {
			if (nbttagcompound.hasKey("Owner", 8)) {
				s = nbttagcompound.getString("Owner");
			}
		}

		if (s.length() > 0) {
			this.setOwnerId(s);
			this.setTamed(true);
		}

		this.aiSit.setSitting(nbttagcompound.getBoolean("Sitting"));
		this.setSitting(nbttagcompound.getBoolean("Sitting"));
	}

	/**+
	 * Play the taming effect, will either be hearts or smoke
	 * depending on status
	 */
	protected void playTameEffect(boolean play) {
		EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;
		if (!play) {
			enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;
		}

		for (int i = 0; i < 7; ++i) {
			double d0 = this.rand.nextGaussian() * 0.02D;
			double d1 = this.rand.nextGaussian() * 0.02D;
			double d2 = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle(enumparticletypes,
					this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
					this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height),
					this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2,
					new int[0]);
		}

	}

	public void handleStatusUpdate(byte b0) {
		if (b0 == 7) {
			this.playTameEffect(true);
		} else if (b0 == 6) {
			this.playTameEffect(false);
		} else {
			super.handleStatusUpdate(b0);
		}

	}

	public boolean isTamed() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 4) != 0;
	}

	public void setTamed(boolean tamed) {
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);
		if (tamed) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 4)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -5)));
		}

		this.setupTamedAI();
	}

	protected void setupTamedAI() {
	}

	public boolean isSitting() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setSitting(boolean sitting) {
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);
		if (sitting) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
		}

	}

	public String getOwnerId() {
		return this.dataWatcher.getWatchableObjectString(17);
	}

	public void setOwnerId(String ownerUuid) {
		this.dataWatcher.updateObject(17, ownerUuid);
	}

	public EntityLivingBase getOwner() {
		String ownerName = this.getOwnerId();
		if (StringUtils.isEmpty(ownerName)) {
			return null;
		}
		try {
			EaglercraftUUID uuid = EaglercraftUUID.fromString(ownerName);
			return uuid == null ? null : this.worldObj.getPlayerEntityByUUID(uuid);
		} catch (IllegalArgumentException var2) {
			return this.worldObj.getPlayerEntityByName(ownerName);
		}
	}

	public boolean isOwner(EntityLivingBase entityIn) {
		return entityIn == this.getOwner();
	}

	/**+
	 * Returns the AITask responsible of the sit logic
	 */
	public EntityAISit getAISit() {
		return this.aiSit;
	}

	public boolean shouldAttackEntity(EntityLivingBase parEntityLivingBase, EntityLivingBase parEntityLivingBase2) {
		return true;
	}

	public Team getTeam() {
		if (this.isTamed()) {
			EntityLivingBase entitylivingbase = this.getOwner();
			if (entitylivingbase != null) {
				return entitylivingbase.getTeam();
			}
		}

		return super.getTeam();
	}

	public boolean isOnSameTeam(EntityLivingBase entitylivingbase) {
		if (this.isTamed()) {
			EntityLivingBase entitylivingbase1 = this.getOwner();
			if (entitylivingbase == entitylivingbase1) {
				return true;
			}

			if (entitylivingbase1 != null) {
				return entitylivingbase1.isOnSameTeam(entitylivingbase);
			}
		}

		return super.isOnSameTeam(entitylivingbase);
	}

	/**+
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource damagesource) {
		if (!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("showDeathMessages")
				&& this.hasCustomName() && this.getOwner() instanceof EntityPlayerMP) {
			((EntityPlayerMP) this.getOwner()).addChatMessage(this.getCombatTracker().getDeathMessage());
		}

		super.onDeath(damagesource);
	}
}