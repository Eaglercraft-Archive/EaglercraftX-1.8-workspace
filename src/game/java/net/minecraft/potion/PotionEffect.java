package net.minecraft.potion;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

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
public class PotionEffect {
	private static final Logger LOGGER = LogManager.getLogger();
	private int potionID;
	private int duration;
	private int amplifier;
	private boolean isSplashPotion;
	private boolean isAmbient;
	private boolean isPotionDurationMax;
	private boolean showParticles;

	public PotionEffect(int id, int effectDuration) {
		this(id, effectDuration, 0);
	}

	public PotionEffect(int id, int effectDuration, int effectAmplifier) {
		this(id, effectDuration, effectAmplifier, false, true);
	}

	public PotionEffect(int id, int effectDuration, int effectAmplifier, boolean ambient, boolean showParticles) {
		this.potionID = id;
		this.duration = effectDuration;
		this.amplifier = effectAmplifier;
		this.isAmbient = ambient;
		this.showParticles = showParticles;
	}

	public PotionEffect(PotionEffect other) {
		this.potionID = other.potionID;
		this.duration = other.duration;
		this.amplifier = other.amplifier;
		this.isAmbient = other.isAmbient;
		this.showParticles = other.showParticles;
	}

	/**+
	 * merges the input PotionEffect into this one if this.amplifier
	 * <= tomerge.amplifier. The duration in the supplied potion
	 * effect is assumed to be greater.
	 */
	public void combine(PotionEffect other) {
		if (this.potionID != other.potionID) {
			LOGGER.warn("This method should only be called for matching effects!");
		}

		if (other.amplifier > this.amplifier) {
			this.amplifier = other.amplifier;
			this.duration = other.duration;
		} else if (other.amplifier == this.amplifier && this.duration < other.duration) {
			this.duration = other.duration;
		} else if (!other.isAmbient && this.isAmbient) {
			this.isAmbient = other.isAmbient;
		}

		this.showParticles = other.showParticles;
	}

	/**+
	 * Retrieve the ID of the potion this effect matches.
	 */
	public int getPotionID() {
		return this.potionID;
	}

	public int getDuration() {
		return this.duration;
	}

	public int getAmplifier() {
		return this.amplifier;
	}

	/**+
	 * Set whether this potion is a splash potion.
	 */
	public void setSplashPotion(boolean splashPotion) {
		this.isSplashPotion = splashPotion;
	}

	/**+
	 * Gets whether this potion effect originated from a beacon
	 */
	public boolean getIsAmbient() {
		return this.isAmbient;
	}

	public boolean getIsShowParticles() {
		return this.showParticles;
	}

	public boolean onUpdate(EntityLivingBase entityIn) {
		if (this.duration > 0) {
			if (Potion.potionTypes[this.potionID].isReady(this.duration, this.amplifier)) {
				this.performEffect(entityIn);
			}

			this.deincrementDuration();
		}

		return this.duration > 0;
	}

	private int deincrementDuration() {
		return --this.duration;
	}

	public void performEffect(EntityLivingBase entityIn) {
		if (this.duration > 0) {
			Potion.potionTypes[this.potionID].performEffect(entityIn, this.amplifier);
		}

	}

	public String getEffectName() {
		return Potion.potionTypes[this.potionID].getName();
	}

	public int hashCode() {
		return this.potionID;
	}

	public String toString() {
		String s = "";
		if (this.getAmplifier() > 0) {
			s = this.getEffectName() + " x " + (this.getAmplifier() + 1) + ", Duration: " + this.getDuration();
		} else {
			s = this.getEffectName() + ", Duration: " + this.getDuration();
		}

		if (this.isSplashPotion) {
			s = s + ", Splash: true";
		}

		if (!this.showParticles) {
			s = s + ", Particles: false";
		}

		return Potion.potionTypes[this.potionID].isUsable() ? "(" + s + ")" : s;
	}

	public boolean equals(Object object) {
		if (!(object instanceof PotionEffect)) {
			return false;
		} else {
			PotionEffect potioneffect = (PotionEffect) object;
			return this.potionID == potioneffect.potionID && this.amplifier == potioneffect.amplifier
					&& this.duration == potioneffect.duration && this.isSplashPotion == potioneffect.isSplashPotion
					&& this.isAmbient == potioneffect.isAmbient;
		}
	}

	/**+
	 * Write a custom potion effect to a potion item's NBT data.
	 */
	public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nbt) {
		nbt.setByte("Id", (byte) this.getPotionID());
		nbt.setByte("Amplifier", (byte) this.getAmplifier());
		nbt.setInteger("Duration", this.getDuration());
		nbt.setBoolean("Ambient", this.getIsAmbient());
		nbt.setBoolean("ShowParticles", this.getIsShowParticles());
		return nbt;
	}

	/**+
	 * Read a custom potion effect from a potion item's NBT data.
	 */
	public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound nbt) {
		byte b0 = nbt.getByte("Id");
		if (b0 >= 0 && b0 < Potion.potionTypes.length && Potion.potionTypes[b0] != null) {
			byte b1 = nbt.getByte("Amplifier");
			int i = nbt.getInteger("Duration");
			boolean flag = nbt.getBoolean("Ambient");
			boolean flag1 = true;
			if (nbt.hasKey("ShowParticles", 1)) {
				flag1 = nbt.getBoolean("ShowParticles");
			}

			return new PotionEffect(b0, i, b1, flag, flag1);
		} else {
			return null;
		}
	}

	/**+
	 * Toggle the isPotionDurationMax field.
	 */
	public void setPotionDurationMax(boolean maxDuration) {
		this.isPotionDurationMax = maxDuration;
	}

	public boolean getIsPotionDurationMax() {
		return this.isPotionDurationMax;
	}
}