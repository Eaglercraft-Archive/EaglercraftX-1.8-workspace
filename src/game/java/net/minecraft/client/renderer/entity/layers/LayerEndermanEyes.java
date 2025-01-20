package net.minecraft.client.renderer.entity.layers;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.entity.monster.EntityEnderman;
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
public class LayerEndermanEyes implements LayerRenderer<EntityEnderman> {
	private static final ResourceLocation field_177203_a = new ResourceLocation(
			"textures/entity/enderman/enderman_eyes.png");
	private final RenderEnderman endermanRenderer;

	public LayerEndermanEyes(RenderEnderman endermanRendererIn) {
		this.endermanRenderer = endermanRendererIn;
	}

	public void doRenderLayer(EntityEnderman entityenderman, float f, float f1, float f2, float f3, float f4, float f5,
			float f6) {
		if (DeferredStateManager.isInDeferredPass()) {
			if (entityenderman.isInvisible()) {
				if (!DeferredStateManager.isEnableShadowRender()
						&& DeferredStateManager.forwardCallbackHandler != null) {
					final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
					DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(entityenderman) {
						@Override
						public void draw(PassType pass) {
							if (pass == PassType.MAIN) {
								DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
							}
							LayerEndermanEyes.this.endermanRenderer.bindTexture(field_177203_a);
							DeferredStateManager.setDefaultMaterialConstants();
							DeferredStateManager.setRoughnessConstant(0.3f);
							DeferredStateManager.setMetalnessConstant(0.1f);
							DeferredStateManager.setEmissionConstant(0.9f);
							EntityRenderer.disableLightmapStatic();
							GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, GL_ZERO, GL_ZERO);
							GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
							GlStateManager.depthMask(false);
							GlStateManager.pushMatrix();
							GlStateManager.loadMatrix(mat);
							GlStateManager.disableCull();
							ModelEnderman eee = (ModelEnderman) LayerEndermanEyes.this.endermanRenderer.getMainModel();
							eee.isAttacking = entityenderman.isScreaming();
							eee.setLivingAnimations(entityenderman, f, f1, f1);
							eee.render(entityenderman, f, f1, f3, f4, f5, f6);
							GlStateManager.popMatrix();
							GlStateManager.depthMask(true);
							GlStateManager.enableCull();
							DeferredStateManager.setDefaultMaterialConstants();
							DeferredStateManager.setHDRTranslucentPassBlendFunc();
							GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
						}
					});
				}
				return;
			}
			this.endermanRenderer.bindTexture(field_177203_a);
			EntityRenderer.disableLightmapStatic();
			DeferredStateManager.setEmissionConstant(0.9f);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enablePolygonOffset();
			GlStateManager.doPolygonOffset(-0.025f, 1.0f);
			this.endermanRenderer.getMainModel().render(entityenderman, f, f1, f3, f4, f5, f6);
			GlStateManager.disablePolygonOffset();
			DeferredStateManager.setEmissionConstant(0.0f);
			EntityRenderer.enableLightmapStatic();
			return;
		}
		this.endermanRenderer.bindTexture(field_177203_a);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(GL_ONE, GL_ONE);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(!entityenderman.isInvisible());
		char c0 = '\uf0f0';
		int i = c0 % 65536;
		int j = c0 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) i / 1.0F, (float) j / 1.0F);
		GlStateManager.enableLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.endermanRenderer.getMainModel().render(entityenderman, f, f1, f3, f4, f5, f6);
		this.endermanRenderer.func_177105_a(entityenderman, f2);
		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}