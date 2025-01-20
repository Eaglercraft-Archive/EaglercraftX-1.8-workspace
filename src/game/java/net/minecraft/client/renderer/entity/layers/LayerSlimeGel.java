package net.minecraft.client.renderer.entity.layers;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;

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
public class LayerSlimeGel implements LayerRenderer<EntitySlime> {
	private final RenderSlime slimeRenderer;
	private final ModelBase slimeModel = new ModelSlime(0);

	public LayerSlimeGel(RenderSlime slimeRendererIn) {
		this.slimeRenderer = slimeRendererIn;
	}

	public void doRenderLayer(EntitySlime entityslime, float f, float f1, float var4, float f2, float f3, float f4,
			float f5) {
		if (DeferredStateManager.isInDeferredPass()) {
			if (DeferredStateManager.forwardCallbackHandler != null) {
				final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
				DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(entityslime) {
					@Override
					public void draw(PassType pass) {
						if (pass == PassType.MAIN) {
							DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
						}
						DeferredStateManager.setDefaultMaterialConstants();
						DeferredStateManager.setRoughnessConstant(0.3f);
						DeferredStateManager.setMetalnessConstant(0.1f);
						boolean flag = LayerSlimeGel.this.slimeRenderer.setBrightness(entityslime, partialTicks,
								LayerSlimeGel.this.shouldCombineTextures());
						EntityRenderer.enableLightmapStatic();
						GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
						GlStateManager.pushMatrix();
						GlStateManager.loadMatrix(mat);
						RenderManager.setupLightmapCoords(entityslime, partialTicks);
						LayerSlimeGel.this.slimeModel
								.setModelAttributes(LayerSlimeGel.this.slimeRenderer.getMainModel());
						LayerSlimeGel.this.slimeRenderer.bindTexture(RenderSlime.slimeTextures);
						LayerSlimeGel.this.slimeModel.render(entityslime, f, f1, f2, f3, f4, f5);
						GlStateManager.popMatrix();
						EntityRenderer.disableLightmapStatic();
						if (flag) {
							LayerSlimeGel.this.slimeRenderer.unsetBrightness();
						}
					}
				});
			}
			return;
		}
		if (!entityslime.isInvisible()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
			this.slimeModel.render(entityslime, f, f1, f2, f3, f4, f5);
			GlStateManager.disableBlend();
		}
	}

	public boolean shouldCombineTextures() {
		return true;
	}
}