package net.minecraft.client.renderer.entity;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityXPOrb;
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
public class RenderXPOrb extends Render<EntityXPOrb> {
	private static final ResourceLocation experienceOrbTextures = new ResourceLocation(
			"textures/entity/experience_orb.png");

	public RenderXPOrb(RenderManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
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
	public void doRender(EntityXPOrb entityxporb, double d0, double d1, double d2, float f, float f1) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) d0, (float) d1, (float) d2);
		this.bindEntityTexture(entityxporb);
		int i = entityxporb.getTextureByXP();
		float f2 = (float) (i % 4 * 16 + 0) / 64.0F;
		float f3 = (float) (i % 4 * 16 + 16) / 64.0F;
		float f4 = (float) (i / 4 * 16 + 0) / 64.0F;
		float f5 = (float) (i / 4 * 16 + 16) / 64.0F;
		float f6 = 1.0F;
		float f7 = 0.5F;
		float f8 = 0.25F;
		int j = entityxporb.getBrightnessForRender(f1);
		int k = j % 65536;
		int l = j / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k / 1.0F, (float) l / 1.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float f10 = 255.0F;
		float f11 = ((float) entityxporb.xpColor + f1) / 2.0F;
		l = (int) ((MathHelper.sin(f11 + 0.0F) + 1.0F) * 0.5F * 255.0F);
		boolean flag = true;
		int i1 = (int) ((MathHelper.sin(f11 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
		GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		float f9 = 0.3F;
		GlStateManager.scale(0.3F, 0.3F, 0.3F);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
		worldrenderer.pos((double) (0.0F - f7), (double) (0.0F - f8), 0.0D).tex((double) f2, (double) f5)
				.color(l, 255, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos((double) (f6 - f7), (double) (0.0F - f8), 0.0D).tex((double) f3, (double) f5)
				.color(l, 255, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos((double) (f6 - f7), (double) (1.0F - f8), 0.0D).tex((double) f3, (double) f4)
				.color(l, 255, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos((double) (0.0F - f7), (double) (1.0F - f8), 0.0D).tex((double) f2, (double) f4)
				.color(l, 255, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entityxporb, d0, d1, d2, f, f1);
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityXPOrb var1) {
		return experienceOrbTextures;
	}
}