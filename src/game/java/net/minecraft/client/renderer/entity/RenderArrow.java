package net.minecraft.client.renderer.entity;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityArrow;
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
public class RenderArrow extends Render<EntityArrow> {
	private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");

	public RenderArrow(RenderManager renderManagerIn) {
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
	public void doRender(EntityArrow entityarrow, double d0, double d1, double d2, float f, float f1) {
		this.bindEntityTexture(entityarrow);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) d0, (float) d1, (float) d2);
		GlStateManager.rotate(
				entityarrow.prevRotationYaw + (entityarrow.rotationYaw - entityarrow.prevRotationYaw) * f1 - 90.0F,
				0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(
				entityarrow.prevRotationPitch + (entityarrow.rotationPitch - entityarrow.prevRotationPitch) * f1, 0.0F,
				0.0F, 1.0F);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		byte b0 = 0;
		float f2 = 0.0F;
		float f3 = 0.5F;
		float f4 = (float) (0 + b0 * 10) / 32.0F;
		float f5 = (float) (5 + b0 * 10) / 32.0F;
		float f6 = 0.0F;
		float f7 = 0.15625F;
		float f8 = (float) (5 + b0 * 10) / 32.0F;
		float f9 = (float) (10 + b0 * 10) / 32.0F;
		float f10 = 0.05625F;
		GlStateManager.enableRescaleNormal();
		float f11 = (float) entityarrow.arrowShake - f1;
		if (f11 > 0.0F) {
			float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
			GlStateManager.rotate(f12, 0.0F, 0.0F, 1.0F);
		}

		GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(f10, f10, f10);
		GlStateManager.translate(-4.0F, 0.0F, 0.0F);
		EaglercraftGPU.glNormal3f(f10, 0.0F, 0.0F);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex((double) f6, (double) f8).endVertex();
		worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex((double) f7, (double) f8).endVertex();
		worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex((double) f7, (double) f9).endVertex();
		worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex((double) f6, (double) f9).endVertex();
		tessellator.draw();
		EaglercraftGPU.glNormal3f(-f10, 0.0F, 0.0F);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex((double) f6, (double) f8).endVertex();
		worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex((double) f7, (double) f8).endVertex();
		worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex((double) f7, (double) f9).endVertex();
		worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex((double) f6, (double) f9).endVertex();
		tessellator.draw();

		for (int i = 0; i < 4; ++i) {
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			EaglercraftGPU.glNormal3f(0.0F, 0.0F, f10);
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldrenderer.pos(-8.0D, -2.0D, 0.0D).tex((double) f2, (double) f4).endVertex();
			worldrenderer.pos(8.0D, -2.0D, 0.0D).tex((double) f3, (double) f4).endVertex();
			worldrenderer.pos(8.0D, 2.0D, 0.0D).tex((double) f3, (double) f5).endVertex();
			worldrenderer.pos(-8.0D, 2.0D, 0.0D).tex((double) f2, (double) f5).endVertex();
			tessellator.draw();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entityarrow, d0, d1, d2, f, f1);
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityArrow var1) {
		return arrowTextures;
	}
}