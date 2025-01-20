package net.minecraft.client.renderer.entity.layers;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
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
public class LayerCape implements LayerRenderer<AbstractClientPlayer> {
	private final RenderPlayer playerRenderer;

	public LayerCape(RenderPlayer playerRendererIn) {
		this.playerRenderer = playerRendererIn;
	}

	public void doRenderLayer(AbstractClientPlayer abstractclientplayer, float var2, float var3, float f, float var5,
			float var6, float var7, float var8) {
		if (abstractclientplayer.hasPlayerInfo() && !abstractclientplayer.isInvisible()
				&& abstractclientplayer.isWearing(EnumPlayerModelParts.CAPE)
				&& abstractclientplayer.getLocationCape() != null
				&& this.playerRenderer.getMainModel() instanceof ModelPlayer) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.playerRenderer.bindTexture(abstractclientplayer.getLocationCape());
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 0.0F, 0.125F);
			double d0 = abstractclientplayer.prevChasingPosX
					+ (abstractclientplayer.chasingPosX - abstractclientplayer.prevChasingPosX) * (double) f
					- (abstractclientplayer.prevPosX
							+ (abstractclientplayer.posX - abstractclientplayer.prevPosX) * (double) f);
			double d1 = abstractclientplayer.prevChasingPosY
					+ (abstractclientplayer.chasingPosY - abstractclientplayer.prevChasingPosY) * (double) f
					- (abstractclientplayer.prevPosY
							+ (abstractclientplayer.posY - abstractclientplayer.prevPosY) * (double) f);
			double d2 = abstractclientplayer.prevChasingPosZ
					+ (abstractclientplayer.chasingPosZ - abstractclientplayer.prevChasingPosZ) * (double) f
					- (abstractclientplayer.prevPosZ
							+ (abstractclientplayer.posZ - abstractclientplayer.prevPosZ) * (double) f);
			float f1 = abstractclientplayer.prevRenderYawOffset
					+ (abstractclientplayer.renderYawOffset - abstractclientplayer.prevRenderYawOffset) * f;
			double d3 = (double) MathHelper.sin(f1 * 3.1415927F / 180.0F);
			double d4 = (double) (-MathHelper.cos(f1 * 3.1415927F / 180.0F));
			float f2 = (float) d1 * 10.0F;
			f2 = MathHelper.clamp_float(f2, -6.0F, 32.0F);
			float f3 = (float) (d0 * d3 + d2 * d4) * 100.0F;
			float f4 = (float) (d0 * d4 - d2 * d3) * 100.0F;
			if (f3 < 0.0F) {
				f3 = 0.0F;
			}

			float f5 = abstractclientplayer.prevCameraYaw
					+ (abstractclientplayer.cameraYaw - abstractclientplayer.prevCameraYaw) * f;
			f2 = f2 + MathHelper.sin((abstractclientplayer.prevDistanceWalkedModified
					+ (abstractclientplayer.distanceWalkedModified - abstractclientplayer.prevDistanceWalkedModified)
							* f)
					* 6.0F) * 32.0F * f5;
			if (abstractclientplayer.isSneaking()) {
				f2 += 25.0F;
			}

			GlStateManager.rotate(6.0F + f3 / 2.0F + f2, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(f4 / 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(-f4 / 2.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			((ModelPlayer) this.playerRenderer.getMainModel()).renderCape(0.0625F);
			GlStateManager.popMatrix();
		}
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}