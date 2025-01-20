package net.minecraft.client.renderer.entity;

import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
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
public class RenderFireball extends Render<EntityFireball> {
	private float scale;

	public RenderFireball(RenderManager renderManagerIn, float scaleIn) {
		super(renderManagerIn);
		this.scale = scaleIn;
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
	public void doRender(EntityFireball entityfireball, double d0, double d1, double d2, float f, float f1) {
		GlStateManager.pushMatrix();
		this.bindEntityTexture(entityfireball);
		GlStateManager.translate((float) d0, (float) d1, (float) d2);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(this.scale, this.scale, this.scale);
		EaglerTextureAtlasSprite textureatlassprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.getParticleIcon(Items.fire_charge);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		float f2 = textureatlassprite.getMinU();
		float f3 = textureatlassprite.getMaxU();
		float f4 = textureatlassprite.getMinV();
		float f5 = textureatlassprite.getMaxV();
		float f6 = 1.0F;
		float f7 = 0.5F;
		float f8 = 0.25F;
		GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		worldrenderer.pos(-0.5D, -0.25D, 0.0D).tex((double) f2, (double) f5).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos(0.5D, -0.25D, 0.0D).tex((double) f3, (double) f5).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos(0.5D, 0.75D, 0.0D).tex((double) f3, (double) f4).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos(-0.5D, 0.75D, 0.0D).tex((double) f2, (double) f4).normal(0.0F, 1.0F, 0.0F).endVertex();
		tessellator.draw();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entityfireball, d0, d1, d2, f, f1);
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityFireball var1) {
		return TextureMap.locationBlocksTexture;
	}
}