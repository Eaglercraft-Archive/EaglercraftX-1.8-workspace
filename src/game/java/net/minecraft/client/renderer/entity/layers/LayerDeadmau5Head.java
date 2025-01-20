package net.minecraft.client.renderer.entity.layers;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;

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
public class LayerDeadmau5Head implements LayerRenderer<AbstractClientPlayer> {
	private final RenderPlayer playerRenderer;

	public LayerDeadmau5Head(RenderPlayer playerRendererIn) {
		this.playerRenderer = playerRendererIn;
	}

	public void doRenderLayer(AbstractClientPlayer abstractclientplayer, float var2, float var3, float f, float var5,
			float var6, float var7, float var8) {
		if (abstractclientplayer.getName().equals("deadmau5") && abstractclientplayer.hasSkin()
				&& !abstractclientplayer.isInvisible() && this.playerRenderer.getMainModel() instanceof ModelPlayer) {
			this.playerRenderer.bindTexture(abstractclientplayer.getLocationSkin());

			for (int i = 0; i < 2; ++i) {
				float f1 = abstractclientplayer.prevRotationYaw
						+ (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * f
						- (abstractclientplayer.prevRenderYawOffset
								+ (abstractclientplayer.renderYawOffset - abstractclientplayer.prevRenderYawOffset)
										* f);
				float f2 = abstractclientplayer.prevRotationPitch
						+ (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * f;
				GlStateManager.pushMatrix();
				GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(f2, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(0.375F * (float) (i * 2 - 1), 0.0F, 0.0F);
				GlStateManager.translate(0.0F, -0.375F, 0.0F);
				GlStateManager.rotate(-f2, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-f1, 0.0F, 1.0F, 0.0F);
				float f3 = 1.3333334F;
				GlStateManager.scale(f3, f3, f3);
				((ModelPlayer) this.playerRenderer.getMainModel()).renderDeadmau5Head(0.0625F);
				GlStateManager.popMatrix();
			}

		}
	}

	public boolean shouldCombineTextures() {
		return true;
	}
}