package net.minecraft.client.renderer.entity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.effect.EntityLightningBolt;
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
public class RenderLightningBolt extends Render<EntityLightningBolt> {
	public RenderLightningBolt(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	/**+
	 * Actually renders the given argument. This is a synthetic
	 * bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual
	 * work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity>) and this method has signature
	 * public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doe
	 */
	public void doRender(EntityLightningBolt entitylightningbolt, double d0, double d1, double d2, float var8,
			float var9) {
		if (DeferredStateManager.isInDeferredPass()) {
			GlStateManager.disableExtensionPipeline();
			EntityRenderer.disableLightmapStatic();
			GlStateManager.tryBlendFuncSeparate(GL_SRC_COLOR, GL_ONE, GL_ZERO, GL_ZERO);
			GlStateManager.disableCull();
			float bright = 0.04f;
			GlStateManager.color(6.0f * bright, 6.25f * bright, 7.0f * bright, 1.0f);
			doRender0(entitylightningbolt, d0, d1, d2, var8, var9);
			GlStateManager.enableCull();
			DeferredStateManager.setHDRTranslucentPassBlendFunc();
			GlStateManager.enableExtensionPipeline();
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			return;
		}
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE);
		doRender0(entitylightningbolt, d0, d1, d2, var8, var9);
		GlStateManager.disableBlend();
	}

	private void doRender0(EntityLightningBolt entitylightningbolt, double d0, double d1, double d2, float var8,
			float var9) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		double[] adouble = new double[8];
		double[] adouble1 = new double[8];
		double d3 = 0.0D;
		double d4 = 0.0D;
		EaglercraftRandom random = new EaglercraftRandom(entitylightningbolt.boltVertex);

		for (int i = 7; i >= 0; --i) {
			adouble[i] = d3;
			adouble1[i] = d4;
			d3 += (double) (random.nextInt(11) - 5);
			d4 += (double) (random.nextInt(11) - 5);
		}

		for (int k1 = 0; k1 < 4; ++k1) {
			EaglercraftRandom random1 = new EaglercraftRandom(entitylightningbolt.boltVertex);

			for (int j = 0; j < 3; ++j) {
				int k = 7;
				int l = 0;
				if (j > 0) {
					k = 7 - j;
				}

				if (j > 0) {
					l = k - 2;
				}

				double d5 = adouble[k] - d3;
				double d6 = adouble1[k] - d4;

				for (int i1 = k; i1 >= l; --i1) {
					double d7 = d5;
					double d8 = d6;
					if (j == 0) {
						d5 += (double) (random1.nextInt(11) - 5);
						d6 += (double) (random1.nextInt(11) - 5);
					} else {
						d5 += (double) (random1.nextInt(31) - 15);
						d6 += (double) (random1.nextInt(31) - 15);
					}

					worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
					float f = 0.5F;
					float f1 = 0.45F;
					float f2 = 0.45F;
					float f3 = 0.5F;
					double d9 = 0.1D + (double) k1 * 0.2D;
					if (j == 0) {
						d9 *= (double) i1 * 0.1D + 1.0D;
					}

					double d10 = 0.1D + (double) k1 * 0.2D;
					if (j == 0) {
						d10 *= (double) (i1 - 1) * 0.1D + 1.0D;
					}

					for (int j1 = 0; j1 < 5; ++j1) {
						double d11 = d0 + 0.5D - d9;
						double d12 = d2 + 0.5D - d9;
						if (j1 == 1 || j1 == 2) {
							d11 += d9 * 2.0D;
						}

						if (j1 == 2 || j1 == 3) {
							d12 += d9 * 2.0D;
						}

						double d13 = d0 + 0.5D - d10;
						double d14 = d2 + 0.5D - d10;
						if (j1 == 1 || j1 == 2) {
							d13 += d10 * 2.0D;
						}

						if (j1 == 2 || j1 == 3) {
							d14 += d10 * 2.0D;
						}

						worldrenderer.pos(d13 + d5, d1 + (double) (i1 * 16), d14 + d6).color(0.45F, 0.45F, 0.5F, 0.3F)
								.endVertex();
						worldrenderer.pos(d11 + d7, d1 + (double) ((i1 + 1) * 16), d12 + d8)
								.color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
					}

					tessellator.draw();
				}
			}
		}

		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityLightningBolt var1) {
		return null;
	}
}