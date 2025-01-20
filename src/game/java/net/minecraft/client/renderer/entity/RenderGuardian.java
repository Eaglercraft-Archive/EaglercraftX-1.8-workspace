package net.minecraft.client.renderer.entity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

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
public class RenderGuardian extends RenderLiving<EntityGuardian> {
	private static final ResourceLocation GUARDIAN_TEXTURE = new ResourceLocation("textures/entity/guardian.png");
	private static final ResourceLocation GUARDIAN_ELDER_TEXTURE = new ResourceLocation(
			"textures/entity/guardian_elder.png");
	private static final ResourceLocation GUARDIAN_BEAM_TEXTURE = new ResourceLocation(
			"textures/entity/guardian_beam.png");
	int field_177115_a;

	public RenderGuardian(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelGuardian(), 0.5F);
		this.field_177115_a = ((ModelGuardian) this.mainModel).func_178706_a();
	}

	public boolean shouldRender(EntityGuardian entityguardian, ICamera icamera, double d0, double d1, double d2) {
		if (super.shouldRender(entityguardian, icamera, d0, d1, d2)) {
			return true;
		} else {
			if (entityguardian.hasTargetedEntity()) {
				EntityLivingBase entitylivingbase = entityguardian.getTargetedEntity();
				if (entitylivingbase != null) {
					Vec3 vec3 = this.func_177110_a(entitylivingbase, (double) entitylivingbase.height * 0.5D, 1.0F);
					Vec3 vec31 = this.func_177110_a(entityguardian, (double) entityguardian.getEyeHeight(), 1.0F);
					if (icamera.isBoundingBoxInFrustum(AxisAlignedBB.fromBounds(vec31.xCoord, vec31.yCoord,
							vec31.zCoord, vec3.xCoord, vec3.yCoord, vec3.zCoord))) {
						return true;
					}
				}
			}

			return false;
		}
	}

	private Vec3 func_177110_a(EntityLivingBase entityLivingBaseIn, double parDouble1, float parFloat1) {
		double d0 = entityLivingBaseIn.lastTickPosX
				+ (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * (double) parFloat1;
		double d1 = parDouble1 + entityLivingBaseIn.lastTickPosY
				+ (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * (double) parFloat1;
		double d2 = entityLivingBaseIn.lastTickPosZ
				+ (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * (double) parFloat1;
		return new Vec3(d0, d1, d2);
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
	public void doRender(EntityGuardian entityguardian, double d0, double d1, double d2, float f, float f1) {
		if (this.field_177115_a != ((ModelGuardian) this.mainModel).func_178706_a()) {
			this.mainModel = new ModelGuardian();
			this.field_177115_a = ((ModelGuardian) this.mainModel).func_178706_a();
		}

		super.doRender(entityguardian, d0, d1, d2, f, f1);
		EntityLivingBase entitylivingbase = entityguardian.getTargetedEntity();
		if (entitylivingbase != null) {
			float f2 = entityguardian.func_175477_p(f1);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			this.bindTexture(GUARDIAN_BEAM_TEXTURE);
			EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, 10497.0F);
			EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, 10497.0F);
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
			float f3 = 240.0F;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f3, f3);
			GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, 1, 1, 0);
			float f4 = (float) entityguardian.worldObj.getTotalWorldTime() + f1;
			float f5 = f4 * 0.5F % 1.0F;
			float f6 = entityguardian.getEyeHeight();
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) d0, (float) d1 + f6, (float) d2);
			Vec3 vec3 = this.func_177110_a(entitylivingbase, (double) entitylivingbase.height * 0.5D, f1);
			Vec3 vec31 = this.func_177110_a(entityguardian, (double) f6, f1);
			Vec3 vec32 = vec3.subtract(vec31);
			double d3 = vec32.lengthVector() + 1.0D;
			vec32 = vec32.normalize();
			float f7 = (float) Math.acos(vec32.yCoord);
			float f8 = (float) Math.atan2(vec32.zCoord, vec32.xCoord);
			GlStateManager.rotate((1.5707964F + -f8) * 57.295776F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(f7 * 57.295776F, 1.0F, 0.0F, 0.0F);
			byte b0 = 1;
			double d4 = (double) f4 * 0.05D * (1.0D - (double) (b0 & 1) * 2.5D);
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			float f9 = f2 * f2;
			int i = 64 + (int) (f9 * 240.0F);
			int j = 32 + (int) (f9 * 192.0F);
			int k = 128 - (int) (f9 * 64.0F);
			double d5 = (double) b0 * 0.2D;
			double d6 = d5 * 1.41D;
			double d7 = 0.0D + Math.cos(d4 + 2.356194490192345D) * d6;
			double d8 = 0.0D + Math.sin(d4 + 2.356194490192345D) * d6;
			double d9 = 0.0D + Math.cos(d4 + 0.7853981633974483D) * d6;
			double d10 = 0.0D + Math.sin(d4 + 0.7853981633974483D) * d6;
			double d11 = 0.0D + Math.cos(d4 + 3.9269908169872414D) * d6;
			double d12 = 0.0D + Math.sin(d4 + 3.9269908169872414D) * d6;
			double d13 = 0.0D + Math.cos(d4 + 5.497787143782138D) * d6;
			double d14 = 0.0D + Math.sin(d4 + 5.497787143782138D) * d6;
			double d15 = 0.0D + Math.cos(d4 + 3.141592653589793D) * d5;
			double d16 = 0.0D + Math.sin(d4 + 3.141592653589793D) * d5;
			double d17 = 0.0D + Math.cos(d4 + 0.0D) * d5;
			double d18 = 0.0D + Math.sin(d4 + 0.0D) * d5;
			double d19 = 0.0D + Math.cos(d4 + 1.5707963267948966D) * d5;
			double d20 = 0.0D + Math.sin(d4 + 1.5707963267948966D) * d5;
			double d21 = 0.0D + Math.cos(d4 + 4.71238898038469D) * d5;
			double d22 = 0.0D + Math.sin(d4 + 4.71238898038469D) * d5;
			double d23 = 0.0D;
			double d24 = 0.4999D;
			double d25 = (double) (-1.0F + f5);
			double d26 = d3 * (0.5D / d5) + d25;
			worldrenderer.pos(d15, d3, d16).tex(0.4999D, d26).color(i, j, k, 255).endVertex();
			worldrenderer.pos(d15, 0.0D, d16).tex(0.4999D, d25).color(i, j, k, 255).endVertex();
			worldrenderer.pos(d17, 0.0D, d18).tex(0.0D, d25).color(i, j, k, 255).endVertex();
			worldrenderer.pos(d17, d3, d18).tex(0.0D, d26).color(i, j, k, 255).endVertex();
			worldrenderer.pos(d19, d3, d20).tex(0.4999D, d26).color(i, j, k, 255).endVertex();
			worldrenderer.pos(d19, 0.0D, d20).tex(0.4999D, d25).color(i, j, k, 255).endVertex();
			worldrenderer.pos(d21, 0.0D, d22).tex(0.0D, d25).color(i, j, k, 255).endVertex();
			worldrenderer.pos(d21, d3, d22).tex(0.0D, d26).color(i, j, k, 255).endVertex();
			double d27 = 0.0D;
			if (entityguardian.ticksExisted % 2 == 0) {
				d27 = 0.5D;
			}

			worldrenderer.pos(d7, d3, d8).tex(0.5D, d27 + 0.5D).color(i, j, k, 255).endVertex();
			worldrenderer.pos(d9, d3, d10).tex(1.0D, d27 + 0.5D).color(i, j, k, 255).endVertex();
			worldrenderer.pos(d13, d3, d14).tex(1.0D, d27).color(i, j, k, 255).endVertex();
			worldrenderer.pos(d11, d3, d12).tex(0.5D, d27).color(i, j, k, 255).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}

	}

	/**+
	 * Allows the render to do any OpenGL state modifications
	 * necessary before the model is rendered. Args: entityLiving,
	 * partialTickTime
	 */
	protected void preRenderCallback(EntityGuardian entityguardian, float var2) {
		if (entityguardian.isElder()) {
			GlStateManager.scale(2.35F, 2.35F, 2.35F);
		}

	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityGuardian entityguardian) {
		return entityguardian.isElder() ? GUARDIAN_ELDER_TEXTURE : GUARDIAN_TEXTURE;
	}
}