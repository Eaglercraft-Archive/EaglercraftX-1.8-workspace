package net.minecraft.client.renderer.texture;

import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
public class TextureCompass extends EaglerTextureAtlasSprite {
	public double currentAngle;
	public double angleDelta;
	public static String field_176608_l;

	public TextureCompass(String iconName) {
		super(iconName);
		field_176608_l = iconName;
	}

	public void updateAnimation() {
		Minecraft minecraft = Minecraft.getMinecraft();
		if (minecraft.theWorld != null && minecraft.thePlayer != null) {
			this.updateCompass(minecraft.theWorld, minecraft.thePlayer.posX, minecraft.thePlayer.posZ,
					(double) minecraft.thePlayer.rotationYaw, false, false);
		} else {
			this.updateCompass((World) null, 0.0D, 0.0D, 0.0D, true, false);
		}

	}

	/**+
	 * Updates the compass based on the given x,z coords and camera
	 * direction
	 */
	public void updateCompass(World worldIn, double parDouble1, double parDouble2, double parDouble3, boolean parFlag,
			boolean parFlag2) {
		if (!this.framesTextureData.isEmpty()) {
			double d0 = 0.0D;
			if (worldIn != null && !parFlag) {
				BlockPos blockpos = worldIn.getSpawnPoint();
				double d1 = (double) blockpos.getX() - parDouble1;
				double d2 = (double) blockpos.getZ() - parDouble2;
				parDouble3 = parDouble3 % 360.0D;
				d0 = -((parDouble3 - 90.0D) * 3.141592653589793D / 180.0D - Math.atan2(d2, d1));
				if (!worldIn.provider.isSurfaceWorld()) {
					d0 = Math.random() * 3.1415927410125732D * 2.0D;
				}
			}

			if (parFlag2) {
				this.currentAngle = d0;
			} else {
				double d3;
				for (d3 = d0 - this.currentAngle; d3 < -3.141592653589793D; d3 += 6.283185307179586D) {
					;
				}

				while (d3 >= 3.141592653589793D) {
					d3 -= 6.283185307179586D;
				}

				d3 = MathHelper.clamp_double(d3, -1.0D, 1.0D);
				this.angleDelta += d3 * 0.1D;
				this.angleDelta *= 0.8D;
				this.currentAngle += this.angleDelta;
			}

			int i;
			for (i = (int) ((this.currentAngle / 6.283185307179586D + 1.0D) * (double) this.framesTextureData.size())
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