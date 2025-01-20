package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

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
public class MovingSoundMinecartRiding extends MovingSound {
	private final EntityPlayer player;
	private final EntityMinecart minecart;

	public MovingSoundMinecartRiding(EntityPlayer playerRiding, EntityMinecart minecart) {
		super(new ResourceLocation("minecraft:minecart.inside"));
		this.player = playerRiding;
		this.minecart = minecart;
		this.attenuationType = ISound.AttenuationType.NONE;
		this.repeat = true;
	}

	/**+
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		if (!this.minecart.isDead && this.player.isRiding() && this.player.ridingEntity == this.minecart) {
			float f = MathHelper.sqrt_double(
					this.minecart.motionX * this.minecart.motionX + this.minecart.motionZ * this.minecart.motionZ);
			if ((double) f >= 0.01D) {
				this.volume = 0.0F + MathHelper.clamp_float(f, 0.0F, 1.0F) * 0.75F;
			} else {
				this.volume = 0.0F;
			}

		} else {
			this.donePlaying = true;
		}
	}
}