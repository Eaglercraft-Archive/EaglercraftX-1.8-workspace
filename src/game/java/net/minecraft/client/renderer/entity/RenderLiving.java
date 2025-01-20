package net.minecraft.client.renderer.entity;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;

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
public abstract class RenderLiving<T extends EntityLiving> extends RendererLivingEntity<T> {
	public RenderLiving(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
	}

	protected boolean canRenderName(T entityliving) {
		return super.canRenderName(entityliving) && (entityliving.getAlwaysRenderNameTagForRender()
				|| entityliving.hasCustomName() && entityliving == this.renderManager.pointedEntity);
	}

	public boolean shouldRender(T entityliving, ICamera icamera, double d0, double d1, double d2) {
		if (super.shouldRender(entityliving, icamera, d0, d1, d2)) {
			return true;
		} else if (entityliving.getLeashed() && entityliving.getLeashedToEntity() != null) {
			Entity entity = entityliving.getLeashedToEntity();
			return icamera.isBoundingBoxInFrustum(entity.getEntityBoundingBox());
		} else {
			return false;
		}
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
	public void doRender(T entityliving, double d0, double d1, double d2, float f, float f1) {
		super.doRender(entityliving, d0, d1, d2, f, f1);
		this.renderLeash(entityliving, d0, d1, d2, f, f1);
	}

	public void func_177105_a(T entityLivingIn, float partialTicks) {
		int i = entityLivingIn.getBrightnessForRender(partialTicks);
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F, (float) k / 1.0F);
	}

	/**+
	 * Gets the value between start and end according to pct
	 */
	private double interpolateValue(double start, double end, double pct) {
		return start + (end - start) * pct;
	}

	protected void renderLeash(T entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks) {
		Entity entity = entityLivingIn.getLeashedToEntity();
		if (entity != null) {
			y = y - (1.6D - (double) entityLivingIn.height) * 0.5D;
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			double d0 = this.interpolateValue((double) entity.prevRotationYaw, (double) entity.rotationYaw,
					(double) (partialTicks * 0.5F)) * 0.01745329238474369D;
			double d1 = this.interpolateValue((double) entity.prevRotationPitch, (double) entity.rotationPitch,
					(double) (partialTicks * 0.5F)) * 0.01745329238474369D;
			double d2 = Math.cos(d0);
			double d3 = Math.sin(d0);
			double d4 = Math.sin(d1);
			if (entity instanceof EntityHanging) {
				d2 = 0.0D;
				d3 = 0.0D;
				d4 = -1.0D;
			}

			double d5 = Math.cos(d1);
			double d6 = this.interpolateValue(entity.prevPosX, entity.posX, (double) partialTicks) - d2 * 0.7D
					- d3 * 0.5D * d5;
			double d7 = this.interpolateValue(entity.prevPosY + (double) entity.getEyeHeight() * 0.7D,
					entity.posY + (double) entity.getEyeHeight() * 0.7D, (double) partialTicks) - d4 * 0.5D - 0.25D;
			double d8 = this.interpolateValue(entity.prevPosZ, entity.posZ, (double) partialTicks) - d3 * 0.7D
					+ d2 * 0.5D * d5;
			double d9 = this.interpolateValue((double) entityLivingIn.prevRenderYawOffset,
					(double) entityLivingIn.renderYawOffset, (double) partialTicks) * 0.01745329238474369D
					+ 1.5707963267948966D;
			d2 = Math.cos(d9) * (double) entityLivingIn.width * 0.4D;
			d3 = Math.sin(d9) * (double) entityLivingIn.width * 0.4D;
			double d10 = this.interpolateValue(entityLivingIn.prevPosX, entityLivingIn.posX, (double) partialTicks)
					+ d2;
			double d11 = this.interpolateValue(entityLivingIn.prevPosY, entityLivingIn.posY, (double) partialTicks);
			double d12 = this.interpolateValue(entityLivingIn.prevPosZ, entityLivingIn.posZ, (double) partialTicks)
					+ d3;
			x = x + d2;
			z = z + d3;
			double d13 = (double) ((float) (d6 - d10));
			double d14 = (double) ((float) (d7 - d11));
			double d15 = (double) ((float) (d8 - d12));
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			boolean flag = true;
			double d16 = 0.025D;
			worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);

			for (int i = 0; i <= 24; ++i) {
				float f = 0.5F;
				float f1 = 0.4F;
				float f2 = 0.3F;
				if (i % 2 == 0) {
					f *= 0.7F;
					f1 *= 0.7F;
					f2 *= 0.7F;
				}

				float f3 = (float) i / 24.0F;
				worldrenderer
						.pos(x + d13 * (double) f3 + 0.0D,
								y + d14 * (double) (f3 * f3 + f3) * 0.5D
										+ (double) ((24.0F - (float) i) / 18.0F + 0.125F),
								z + d15 * (double) f3)
						.color(f, f1, f2, 1.0F).endVertex();
				worldrenderer
						.pos(x + d13 * (double) f3 + 0.025D,
								y + d14 * (double) (f3 * f3 + f3) * 0.5D
										+ (double) ((24.0F - (float) i) / 18.0F + 0.125F) + 0.025D,
								z + d15 * (double) f3)
						.color(f, f1, f2, 1.0F).endVertex();
			}

			tessellator.draw();
			worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);

			for (int j = 0; j <= 24; ++j) {
				float f4 = 0.5F;
				float f5 = 0.4F;
				float f6 = 0.3F;
				if (j % 2 == 0) {
					f4 *= 0.7F;
					f5 *= 0.7F;
					f6 *= 0.7F;
				}

				float f7 = (float) j / 24.0F;
				worldrenderer
						.pos(x + d13 * (double) f7 + 0.0D,
								y + d14 * (double) (f7 * f7 + f7) * 0.5D
										+ (double) ((24.0F - (float) j) / 18.0F + 0.125F) + 0.025D,
								z + d15 * (double) f7)
						.color(f4, f5, f6, 1.0F).endVertex();
				worldrenderer.pos(x + d13 * (double) f7 + 0.025D,
						y + d14 * (double) (f7 * f7 + f7) * 0.5D + (double) ((24.0F - (float) j) / 18.0F + 0.125F),
						z + d15 * (double) f7 + 0.025D).color(f4, f5, f6, 1.0F).endVertex();
			}

			tessellator.draw();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			GlStateManager.enableCull();
		}

	}
}