package net.minecraft.entity.player;

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
public class PlayerCapabilities {
	public boolean disableDamage;
	public boolean isFlying;
	public boolean allowFlying;
	public boolean isCreativeMode;
	/**+
	 * Indicates whether the player is allowed to modify the
	 * surroundings
	 */
	public boolean allowEdit = true;
	private float flySpeed = 0.05F;
	private float walkSpeed = 0.1F;

	public void writeCapabilitiesToNBT(NBTTagCompound tagCompound) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setBoolean("invulnerable", this.disableDamage);
		nbttagcompound.setBoolean("flying", this.isFlying);
		nbttagcompound.setBoolean("mayfly", this.allowFlying);
		nbttagcompound.setBoolean("instabuild", this.isCreativeMode);
		nbttagcompound.setBoolean("mayBuild", this.allowEdit);
		nbttagcompound.setFloat("flySpeed", this.flySpeed);
		nbttagcompound.setFloat("walkSpeed", this.walkSpeed);
		tagCompound.setTag("abilities", nbttagcompound);
	}

	public void readCapabilitiesFromNBT(NBTTagCompound tagCompound) {
		if (tagCompound.hasKey("abilities", 10)) {
			NBTTagCompound nbttagcompound = tagCompound.getCompoundTag("abilities");
			this.disableDamage = nbttagcompound.getBoolean("invulnerable");
			this.isFlying = nbttagcompound.getBoolean("flying");
			this.allowFlying = nbttagcompound.getBoolean("mayfly");
			this.isCreativeMode = nbttagcompound.getBoolean("instabuild");
			if (nbttagcompound.hasKey("flySpeed", 99)) {
				this.flySpeed = nbttagcompound.getFloat("flySpeed");
				this.walkSpeed = nbttagcompound.getFloat("walkSpeed");
			}

			if (nbttagcompound.hasKey("mayBuild", 1)) {
				this.allowEdit = nbttagcompound.getBoolean("mayBuild");
			}
		}

	}

	public float getFlySpeed() {
		return this.flySpeed;
	}

	public void setFlySpeed(float speed) {
		this.flySpeed = speed;
	}

	public float getWalkSpeed() {
		return this.walkSpeed;
	}

	public void setPlayerWalkSpeed(float speed) {
		this.walkSpeed = speed;
	}
}