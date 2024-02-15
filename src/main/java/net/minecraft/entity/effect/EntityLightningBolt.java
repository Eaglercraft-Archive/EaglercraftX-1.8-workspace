package net.minecraft.entity.effect;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class EntityLightningBolt extends EntityWeatherEffect {
	private int lightningState;
	public long boltVertex;
	private int boltLivingTime;

	public EntityLightningBolt(World worldIn, double posX, double posY, double posZ) {
		super(worldIn);
		this.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
		this.lightningState = 2;
		this.boltVertex = this.rand.nextLong();
		this.boltLivingTime = this.rand.nextInt(3) + 1;
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		if (this.lightningState == 2) {
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F,
					0.8F + this.rand.nextFloat() * 0.2F);
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F,
					0.5F + this.rand.nextFloat() * 0.2F);
		}

		--this.lightningState;
		if (this.lightningState < 0) {
			if (this.boltLivingTime == 0) {
				this.setDead();
			} else if (this.lightningState < -this.rand.nextInt(10)) {
				--this.boltLivingTime;
				this.lightningState = 1;
				this.boltVertex = this.rand.nextLong();
			}
		}

		if (this.lightningState >= 0) {
			this.worldObj.setLastLightningBolt(2);
		}

	}

	protected void entityInit() {
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound var1) {
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound var1) {
	}
}