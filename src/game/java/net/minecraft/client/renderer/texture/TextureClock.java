package net.minecraft.client.renderer.texture;

import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

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
public class TextureClock extends EaglerTextureAtlasSprite {
	private double field_94239_h;
	private double field_94240_i;

	public TextureClock(String iconName) {
		super(iconName);
	}

	public void updateAnimation() {
		if (!this.framesTextureData.isEmpty()) {
			Minecraft minecraft = Minecraft.getMinecraft();
			double d0 = 0.0D;
			if (minecraft.theWorld != null && minecraft.thePlayer != null) {
				d0 = (double) minecraft.theWorld.getCelestialAngle(1.0F);
				if (!minecraft.theWorld.provider.isSurfaceWorld()) {
					d0 = Math.random();
				}
			}

			double d1;
			for (d1 = d0 - this.field_94239_h; d1 < -0.5D; ++d1) {
				;
			}

			while (d1 >= 0.5D) {
				--d1;
			}

			d1 = MathHelper.clamp_double(d1, -1.0D, 1.0D);
			this.field_94240_i += d1 * 0.1D;
			this.field_94240_i *= 0.8D;
			this.field_94239_h += this.field_94240_i;

			int i;
			for (i = (int) ((this.field_94239_h + 1.0D) * (double) this.framesTextureData.size())
					% this.framesTextureData
							.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size()) {
				;
			}

			if (i != this.frameCounter) {
				this.frameCounter = i;
				currentAnimUpdater = (mapWidth, mapHeight, mapLevel) -> {
					animationCache.copyFrameToTex2D(this.frameCounter, mapLevel, this.originX >> mapLevel,
							this.originY >> mapLevel, this.width >> mapLevel, this.height >> mapLevel, mapWidth,
							mapHeight);
				};
			} else {
				currentAnimUpdater = null;
			}

		} else {
			currentAnimUpdater = null;
		}
	}

}