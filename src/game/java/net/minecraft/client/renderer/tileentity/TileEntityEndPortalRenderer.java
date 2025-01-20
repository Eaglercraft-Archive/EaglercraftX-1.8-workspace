package net.minecraft.client.renderer.tileentity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityEndPortal;
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
public class TileEntityEndPortalRenderer extends TileEntitySpecialRenderer<TileEntityEndPortal> {
	private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
	private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
	private static final EaglercraftRandom field_147527_e = new EaglercraftRandom(31100L);
	FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);

	public void renderTileEntityAt(TileEntityEndPortal var1, double d0, double d1, double d2, float var8, int var9) {
		if (DeferredStateManager.isInDeferredPass()) {
			if (!DeferredStateManager.isInParaboloidPass() && !DeferredStateManager.isEnableShadowRender()
					&& DeferredStateManager.forwardCallbackHandler != null) {
				DeferredStateManager.forwardCallbackHandler
						.push(new ShadersRenderPassFuture((float) d0, (float) d1, (float) d2, var8) {
							@Override
							public void draw(PassType pass) {
								if (pass == PassType.MAIN) {
									DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
								}
								DeferredStateManager.setDefaultMaterialConstants();
								DeferredStateManager.setRoughnessConstant(0.3f);
								DeferredStateManager.setMetalnessConstant(0.3f);
								DeferredStateManager.setEmissionConstant(0.9f);
								renderTileEntityAt0(var1, d0, d1, d2, var8, var9);
								DeferredStateManager.setDefaultMaterialConstants();
								DeferredStateManager.setHDRTranslucentPassBlendFunc();
							}
						});
			}
			return;
		}
		GlStateManager.enableBlend();
		renderTileEntityAt0(var1, d0, d1, d2, var8, var9);
		GlStateManager.disableBlend();
	}

	private void renderTileEntityAt0(TileEntityEndPortal var1, double d0, double d1, double d2, float var8, int var9) {
		float f = (float) this.rendererDispatcher.entityX;
		float f1 = (float) this.rendererDispatcher.entityY;
		float f2 = (float) this.rendererDispatcher.entityZ;
		GlStateManager.disableLighting();
		field_147527_e.setSeed(31100L);
		float f3 = 0.75F;

		for (int i = 0; i < 16; ++i) {
			GlStateManager.pushMatrix();
			float f4 = (float) (16 - i);
			float f5 = 0.0625F;
			float f6 = 1.0F / (f4 + 1.0F);
			if (i == 0) {
				this.bindTexture(END_SKY_TEXTURE);
				f6 = 0.1F;
				f4 = 65.0F;
				f5 = 0.125F;
				if (DeferredStateManager.isInDeferredPass()) {
					DeferredStateManager.setHDRTranslucentPassBlendFunc();
				} else {
					GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				}
			}

			if (i >= 1) {
				this.bindTexture(END_PORTAL_TEXTURE);
			}

			if (i == 1) {
				if (DeferredStateManager.isInDeferredPass()) {
					GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, GL_ZERO, GL_ZERO);
				} else {
					GlStateManager.blendFunc(GL_ONE, GL_ONE);
				}
				f5 = 0.5F;
			}

			float f7 = (float) (-(d1 + (double) f3 - 1.25));
			float f8 = f7 + (float) ActiveRenderInfo.getPosition().yCoord;
			float f9 = f7 + f4 + (float) ActiveRenderInfo.getPosition().yCoord;
			float f10 = f8 / f9;
			f10 = (float) (d1 + (double) f3) + f10;
			GlStateManager.translate(f, f10, f2);
			GlStateManager.texGen(GlStateManager.TexGen.S, GL_OBJECT_LINEAR);
			GlStateManager.texGen(GlStateManager.TexGen.T, GL_OBJECT_LINEAR);
			GlStateManager.texGen(GlStateManager.TexGen.R, GL_OBJECT_LINEAR);
			GlStateManager.texGen(GlStateManager.TexGen.Q, GL_EYE_LINEAR);
			GlStateManager.func_179105_a(GlStateManager.TexGen.S, GL_OBJECT_PLANE, this.func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
			GlStateManager.func_179105_a(GlStateManager.TexGen.T, GL_OBJECT_PLANE, this.func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
			GlStateManager.func_179105_a(GlStateManager.TexGen.R, GL_OBJECT_PLANE, this.func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
			GlStateManager.func_179105_a(GlStateManager.TexGen.Q, GL_EYE_PLANE, this.func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
			GlStateManager.enableTexGen();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			GlStateManager.translate(0.0F, (float) (Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
			GlStateManager.scale(f5, f5, f5);
			GlStateManager.translate(0.5F, 0.5F, 0.0F);
			GlStateManager.rotate((float) (i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate(-0.5F, -0.5F, 0.0F);
			GlStateManager.translate(-f, -f2, -f1);
			f8 = f7 + (float) ActiveRenderInfo.getPosition().yCoord;
			GlStateManager.translate((float) ActiveRenderInfo.getPosition().xCoord * f4 / f8,
					(float) ActiveRenderInfo.getPosition().zCoord * f4 / f8, -f1);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			float f11 = (field_147527_e.nextFloat() * 0.5F + 0.1F) * f6;
			float f12 = (field_147527_e.nextFloat() * 0.5F + 0.4F) * f6;
			float f13 = (field_147527_e.nextFloat() * 0.5F + 0.5F) * f6;
			if (i == 0) {
				f11 = f12 = f13 = 1.0F * f6;
			}

			worldrenderer.pos(d0, d1 + (double) f3, d2).color(f11, f12, f13, 1.0F).endVertex();
			worldrenderer.pos(d0, d1 + (double) f3, d2 + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
			worldrenderer.pos(d0 + 1.0D, d1 + (double) f3, d2 + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
			worldrenderer.pos(d0 + 1.0D, d1 + (double) f3, d2).color(f11, f12, f13, 1.0F).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL_MODELVIEW);
			this.bindTexture(END_SKY_TEXTURE);
		}

		GlStateManager.disableTexGen();
		GlStateManager.enableLighting();
	}

	private FloatBuffer func_147525_a(float parFloat1, float parFloat2, float parFloat3, float parFloat4) {
		this.field_147528_b.clear();
		this.field_147528_b.put(parFloat1).put(parFloat2).put(parFloat3).put(parFloat4);
		this.field_147528_b.flip();
		return this.field_147528_b;
	}
}