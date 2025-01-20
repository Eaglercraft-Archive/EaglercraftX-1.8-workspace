package net.minecraft.client.renderer.entity;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.MathHelper;
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
public class RenderFish extends Render<EntityFishHook> {
	private static final ResourceLocation FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");

	public RenderFish(RenderManager renderManagerIn) {
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
	public void doRender(EntityFishHook entityfishhook, double d0, double d1, double d2, float f, float f1) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) d0, (float) d1, (float) d2);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		this.bindEntityTexture(entityfishhook);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		boolean flag = true;
		boolean flag1 = true;
		float f2 = 0.0625F;
		float f3 = 0.125F;
		float f4 = 0.125F;
		float f5 = 0.1875F;
		float f6 = 1.0F;
		float f7 = 0.5F;
		float f8 = 0.5F;
		GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		worldrenderer.pos(-0.5D, -0.5D, 0.0D).tex(0.0625D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos(0.5D, -0.5D, 0.0D).tex(0.125D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos(0.5D, 0.5D, 0.0D).tex(0.125D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos(-0.5D, 0.5D, 0.0D).tex(0.0625D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
		tessellator.draw();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		if (entityfishhook.angler != null) {
			float f9 = entityfishhook.angler.getSwingProgress(f1);
			float f10 = MathHelper.sin(MathHelper.sqrt_float(f9) * 3.1415927F);
			Vec3 vec3 = new Vec3(-0.36D, 0.03D, 0.35D);
			vec3 = vec3.rotatePitch(-(entityfishhook.angler.prevRotationPitch
					+ (entityfishhook.angler.rotationPitch - entityfishhook.angler.prevRotationPitch) * f1) * 3.1415927F
					/ 180.0F);
			vec3 = vec3.rotateYaw(-(entityfishhook.angler.prevRotationYaw
					+ (entityfishhook.angler.rotationYaw - entityfishhook.angler.prevRotationYaw) * f1) * 3.1415927F
					/ 180.0F);
			vec3 = vec3.rotateYaw(f10 * 0.5F);
			vec3 = vec3.rotatePitch(-f10 * 0.7F);
			double d3 = entityfishhook.angler.prevPosX
					+ (entityfishhook.angler.posX - entityfishhook.angler.prevPosX) * (double) f1 + vec3.xCoord;
			double d4 = entityfishhook.angler.prevPosY
					+ (entityfishhook.angler.posY - entityfishhook.angler.prevPosY) * (double) f1 + vec3.yCoord;
			double d5 = entityfishhook.angler.prevPosZ
					+ (entityfishhook.angler.posZ - entityfishhook.angler.prevPosZ) * (double) f1 + vec3.zCoord;
			double d6 = (double) entityfishhook.angler.getEyeHeight();
			if (this.renderManager.options != null && this.renderManager.options.thirdPersonView > 0
					|| entityfishhook.angler != Minecraft.getMinecraft().thePlayer) {
				float f11 = (entityfishhook.angler.prevRenderYawOffset
						+ (entityfishhook.angler.renderYawOffset - entityfishhook.angler.prevRenderYawOffset) * f1)
						* 3.1415927F / 180.0F;
				double d7 = (double) MathHelper.sin(f11);
				double d9 = (double) MathHelper.cos(f11);
				double d11 = 0.35D;
				double d13 = 0.8D;
				d3 = entityfishhook.angler.prevPosX
						+ (entityfishhook.angler.posX - entityfishhook.angler.prevPosX) * (double) f1 - d9 * 0.35D
						- d7 * 0.8D;
				d4 = entityfishhook.angler.prevPosY + d6
						+ (entityfishhook.angler.posY - entityfishhook.angler.prevPosY) * (double) f1 - 0.45D;
				d5 = entityfishhook.angler.prevPosZ
						+ (entityfishhook.angler.posZ - entityfishhook.angler.prevPosZ) * (double) f1 - d7 * 0.35D
						+ d9 * 0.8D;
				d6 = entityfishhook.angler.isSneaking() ? -0.1875D : 0.0D;
			}

			double d16 = entityfishhook.prevPosX + (entityfishhook.posX - entityfishhook.prevPosX) * (double) f1;
			double d8 = entityfishhook.prevPosY + (entityfishhook.posY - entityfishhook.prevPosY) * (double) f1 + 0.25D;
			double d10 = entityfishhook.prevPosZ + (entityfishhook.posZ - entityfishhook.prevPosZ) * (double) f1;
			double d12 = (double) ((float) (d3 - d16));
			double d14 = (double) ((float) (d4 - d8)) + d6;
			double d15 = (double) ((float) (d5 - d10));
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
			boolean flag2 = true;

			for (int i = 0; i <= 16; ++i) {
				float f12 = (float) i / 16.0F;
				worldrenderer.pos(d0 + d12 * (double) f12, d1 + d14 * (double) (f12 * f12 + f12) * 0.5D + 0.25D,
						d2 + d15 * (double) f12).color(0, 0, 0, 255).endVertex();
			}

			tessellator.draw();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			super.doRender(entityfishhook, d0, d1, d2, f, f1);
		}

	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityFishHook var1) {
		return FISH_PARTICLES;
	}
}