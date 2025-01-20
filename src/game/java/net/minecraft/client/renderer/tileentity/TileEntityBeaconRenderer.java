package net.minecraft.client.renderer.tileentity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityBeacon;
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
public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer<TileEntityBeacon> {
	private static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");

	public void renderTileEntityAt(TileEntityBeacon tileentitybeacon, double d0, double d1, double d2, float f,
			int var9) {
		if (DeferredStateManager.isEnableShadowRender())
			return;
		float f1 = tileentitybeacon.shouldBeamRender();
		GlStateManager.alphaFunc(GL_GREATER, 0.1F);
		if (f1 > 0.0F) {
			boolean deferred = DeferredStateManager.isInDeferredPass();
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			GlStateManager.disableFog();
			List list = tileentitybeacon.getBeamSegments();
			int i = 0;

			for (int j = 0; j < list.size(); ++j) {
				TileEntityBeacon.BeamSegment tileentitybeacon$beamsegment = (TileEntityBeacon.BeamSegment) list.get(j);
				int k = i + tileentitybeacon$beamsegment.getHeight();
				this.bindTexture(beaconBeam);
				if (deferred) {
					DeferredStateManager.setDefaultMaterialConstants();
					DeferredStateManager.setRoughnessConstant(0.3f);
					DeferredStateManager.setMetalnessConstant(0.3f);
					DeferredStateManager.setEmissionConstant(0.9f);
				}
				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, 10497);
				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, 10497);
				GlStateManager.disableLighting();
				GlStateManager.disableCull();
				GlStateManager.disableBlend();
				GlStateManager.depthMask(true);
				GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, 1, 1, 0);
				double d3 = (double) tileentitybeacon.getWorld().getTotalWorldTime() + (double) f;
				double d4 = MathHelper.func_181162_h(-d3 * 0.2D - (double) MathHelper.floor_double(-d3 * 0.1D));
				float f2 = tileentitybeacon$beamsegment.getColors()[0];
				float f3 = tileentitybeacon$beamsegment.getColors()[1];
				float f4 = tileentitybeacon$beamsegment.getColors()[2];
				double d5 = d3 * 0.025D * -1.5D;
				double d6 = 0.2D;
				double d7 = 0.5D + Math.cos(d5 + 2.356194490192345D) * 0.2D;
				double d8 = 0.5D + Math.sin(d5 + 2.356194490192345D) * 0.2D;
				double d9 = 0.5D + Math.cos(d5 + 0.7853981633974483D) * 0.2D;
				double d10 = 0.5D + Math.sin(d5 + 0.7853981633974483D) * 0.2D;
				double d11 = 0.5D + Math.cos(d5 + 3.9269908169872414D) * 0.2D;
				double d12 = 0.5D + Math.sin(d5 + 3.9269908169872414D) * 0.2D;
				double d13 = 0.5D + Math.cos(d5 + 5.497787143782138D) * 0.2D;
				double d14 = 0.5D + Math.sin(d5 + 5.497787143782138D) * 0.2D;
				double d15 = 0.0D;
				double d16 = 1.0D;
				double d17 = -1.0D + d4;
				double d18 = (double) ((float) tileentitybeacon$beamsegment.getHeight() * f1) * 2.5D + d17;
				worldrenderer.begin(7, deferred ? DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL
						: DefaultVertexFormats.POSITION_TEX_COLOR);
				worldrenderer.pos(d0 + d7, d1 + (double) k, d2 + d8).tex(1.0D, d18).color(f2, f3, f4, 1.0F).endVertex();
				worldrenderer.pos(d0 + d7, d1 + (double) i, d2 + d8).tex(1.0D, d17).color(f2, f3, f4, 1.0F).endVertex();
				worldrenderer.pos(d0 + d9, d1 + (double) i, d2 + d10).tex(0.0D, d17).color(f2, f3, f4, 1.0F)
						.endVertex();
				worldrenderer.pos(d0 + d9, d1 + (double) k, d2 + d10).tex(0.0D, d18).color(f2, f3, f4, 1.0F)
						.endVertex();
				if (deferred)
					worldrenderer.genNormals(true, 0);
				worldrenderer.pos(d0 + d13, d1 + (double) k, d2 + d14).tex(1.0D, d18).color(f2, f3, f4, 1.0F)
						.endVertex();
				worldrenderer.pos(d0 + d13, d1 + (double) i, d2 + d14).tex(1.0D, d17).color(f2, f3, f4, 1.0F)
						.endVertex();
				worldrenderer.pos(d0 + d11, d1 + (double) i, d2 + d12).tex(0.0D, d17).color(f2, f3, f4, 1.0F)
						.endVertex();
				worldrenderer.pos(d0 + d11, d1 + (double) k, d2 + d12).tex(0.0D, d18).color(f2, f3, f4, 1.0F)
						.endVertex();
				if (deferred)
					worldrenderer.genNormals(true, 0);
				worldrenderer.pos(d0 + d9, d1 + (double) k, d2 + d10).tex(1.0D, d18).color(f2, f3, f4, 1.0F)
						.endVertex();
				worldrenderer.pos(d0 + d9, d1 + (double) i, d2 + d10).tex(1.0D, d17).color(f2, f3, f4, 1.0F)
						.endVertex();
				worldrenderer.pos(d0 + d13, d1 + (double) i, d2 + d14).tex(0.0D, d17).color(f2, f3, f4, 1.0F)
						.endVertex();
				worldrenderer.pos(d0 + d13, d1 + (double) k, d2 + d14).tex(0.0D, d18).color(f2, f3, f4, 1.0F)
						.endVertex();
				if (deferred)
					worldrenderer.genNormals(true, 0);
				worldrenderer.pos(d0 + d11, d1 + (double) k, d2 + d12).tex(1.0D, d18).color(f2, f3, f4, 1.0F)
						.endVertex();
				worldrenderer.pos(d0 + d11, d1 + (double) i, d2 + d12).tex(1.0D, d17).color(f2, f3, f4, 1.0F)
						.endVertex();
				worldrenderer.pos(d0 + d7, d1 + (double) i, d2 + d8).tex(0.0D, d17).color(f2, f3, f4, 1.0F).endVertex();
				worldrenderer.pos(d0 + d7, d1 + (double) k, d2 + d8).tex(0.0D, d18).color(f2, f3, f4, 1.0F).endVertex();
				if (deferred)
					worldrenderer.genNormals(true, 0);
				tessellator.draw();

				if (deferred) {
					DeferredStateManager.setDefaultMaterialConstants();
					GlStateManager.enableLighting();
					GlStateManager.depthMask(true);
					i = k;
					continue;
				}

				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				GlStateManager.depthMask(false);
				d5 = 0.2D;
				d6 = 0.2D;
				d7 = 0.8D;
				d8 = 0.2D;
				d9 = 0.2D;
				d10 = 0.8D;
				d11 = 0.8D;
				d12 = 0.8D;
				d13 = 0.0D;
				d14 = 1.0D;
				d15 = -1.0D + d4;
				d16 = (double) ((float) tileentitybeacon$beamsegment.getHeight() * f1) + d15;
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.2D).tex(1.0D, d16).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.2D).tex(1.0D, d15).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.2D).tex(0.0D, d15).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.2D).tex(0.0D, d16).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.8D).tex(1.0D, d16).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.8D).tex(1.0D, d15).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.8D).tex(0.0D, d15).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.8D).tex(0.0D, d16).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.2D).tex(1.0D, d16).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.2D).tex(1.0D, d15).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.8D).tex(0.0D, d15).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.8D).tex(0.0D, d16).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.8D).tex(1.0D, d16).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.8D).tex(1.0D, d15).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.2D).tex(0.0D, d15).color(f2, f3, f4, 0.125F)
						.endVertex();
				worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.2D).tex(0.0D, d16).color(f2, f3, f4, 0.125F)
						.endVertex();
				tessellator.draw();
				GlStateManager.enableLighting();
				GlStateManager.enableTexture2D();
				GlStateManager.depthMask(true);
				i = k;
			}

			if (deferred && list.size() > 0) {
				if (DeferredStateManager.forwardCallbackHandler != null) {
					final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
					final float lx = GlStateManager.getTexCoordX(1), ly = GlStateManager.getTexCoordY(1);
					DeferredStateManager.forwardCallbackHandler
							.push(new ShadersRenderPassFuture((float) d0, (float) d1, (float) d2, f) {
								@Override
								public void draw(PassType pass) {
									if (pass == PassType.MAIN) {
										DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
									}
									TileEntityBeaconRenderer.this.bindTexture(beaconBeam);
									EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, 10497);
									EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, 10497);
									DeferredStateManager.setDefaultMaterialConstants();
									DeferredStateManager.setRoughnessConstant(0.3f);
									DeferredStateManager.setMetalnessConstant(0.2f);
									DeferredStateManager.setEmissionConstant(0.6f);
									GlStateManager.depthMask(false);
									GlStateManager.pushMatrix();
									GlStateManager.loadMatrix(mat);
									GlStateManager.texCoords2DDirect(1, lx, ly);
									EntityRenderer.enableLightmapStatic();
									GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
									List list = tileentitybeacon.getBeamSegments();
									int i = 0;

									for (int j = 0; j < list.size(); ++j) {
										TileEntityBeacon.BeamSegment tileentitybeacon$beamsegment = (TileEntityBeacon.BeamSegment) list
												.get(j);
										int k = i + tileentitybeacon$beamsegment.getHeight();

										double d3 = (double) tileentitybeacon.getWorld().getTotalWorldTime()
												+ (double) f;
										double d4 = MathHelper.func_181162_h(
												-d3 * 0.2D - (double) MathHelper.floor_double(-d3 * 0.1D));
										float f2 = tileentitybeacon$beamsegment.getColors()[0];
										float f3 = tileentitybeacon$beamsegment.getColors()[1];
										float f4 = tileentitybeacon$beamsegment.getColors()[2];

										double d15 = 0.0D;
										double d16 = 1.0D;
										double d17 = -1.0D + d4;

										d15 = -1.0D + d4;
										d16 = (double) ((float) tileentitybeacon$beamsegment.getHeight() * f1) + d15;
										worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
										worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.2D).tex(1.0D, d16)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.2D).tex(1.0D, d15)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.2D).tex(0.0D, d15)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.2D).tex(0.0D, d16)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.genNormals(true, 0);
										worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.8D).tex(1.0D, d16)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.8D).tex(1.0D, d15)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.8D).tex(0.0D, d15)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.8D).tex(0.0D, d16)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.genNormals(true, 0);
										worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.2D).tex(1.0D, d16)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.2D).tex(1.0D, d15)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.8D).tex(0.0D, d15)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.8D).tex(0.0D, d16)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.genNormals(true, 0);
										worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.8D).tex(1.0D, d16)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.8D).tex(1.0D, d15)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.2D).tex(0.0D, d15)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.2D).tex(0.0D, d16)
												.color(f2, f3, f4, 0.125F).endVertex();
										worldrenderer.genNormals(true, 0);
										tessellator.draw();
										i = k;
									}
									GlStateManager.popMatrix();
									EntityRenderer.disableLightmapStatic();
									GlStateManager.depthMask(true);
								}
							});
				}
			}

			GlStateManager.enableFog();
		}

	}

	public boolean func_181055_a() {
		return true;
	}
}