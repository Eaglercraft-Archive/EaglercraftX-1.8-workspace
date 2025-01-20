package net.minecraft.client.particle;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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
public class EntityLargeExplodeFX extends EntityFX {
	private static final ResourceLocation EXPLOSION_TEXTURE = new ResourceLocation("textures/entity/explosion.png");
	private int field_70581_a;
	private int field_70584_aq;
	private TextureManager theRenderEngine;
	private float field_70582_as;

	protected EntityLargeExplodeFX(TextureManager renderEngine, World worldIn, double xCoordIn, double yCoordIn,
			double zCoordIn, double parDouble1, double parDouble2, double parDouble3) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
		this.theRenderEngine = renderEngine;
		this.field_70584_aq = 6 + this.rand.nextInt(4);
		this.particleRed = this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.6F + 0.4F;
		this.field_70582_as = 1.0F - (float) parDouble1 * 0.5F;
	}

	/**+
	 * Renders the particle
	 */
	public void renderParticle(WorldRenderer worldrenderer, Entity var2, float f, float f1, float f2, float f3,
			float f4, float f5) {
		int i = (int) (((float) this.field_70581_a + f) * 15.0F / (float) this.field_70584_aq);
		if (i <= 15) {
			this.theRenderEngine.bindTexture(EXPLOSION_TEXTURE);
			float f6 = (float) (i % 4) / 4.0F;
			float f7 = f6 + 0.24975F;
			float f8 = (float) (i / 4) / 4.0F;
			float f9 = f8 + 0.24975F;
			float f10 = 2.0F * this.field_70582_as;
			float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) f - interpPosX);
			float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) f - interpPosY);
			float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) f - interpPosZ);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			RenderHelper.disableStandardItemLighting();
			int l = DeferredStateManager.isInDeferredPass() ? ((var2.getBrightnessForRender(f) >> 16) & 0xFF) : 0;
			worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
			worldrenderer
					.pos((double) (f11 - f1 * f10 - f4 * f10), (double) (f12 - f2 * f10),
							(double) (f13 - f3 * f10 - f5 * f10))
					.tex((double) f7, (double) f9).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
					.lightmap(l, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			worldrenderer
					.pos((double) (f11 - f1 * f10 + f4 * f10), (double) (f12 + f2 * f10),
							(double) (f13 - f3 * f10 + f5 * f10))
					.tex((double) f7, (double) f8).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
					.lightmap(l, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			worldrenderer
					.pos((double) (f11 + f1 * f10 + f4 * f10), (double) (f12 + f2 * f10),
							(double) (f13 + f3 * f10 + f5 * f10))
					.tex((double) f6, (double) f8).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
					.lightmap(l, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			worldrenderer
					.pos((double) (f11 + f1 * f10 - f4 * f10), (double) (f12 - f2 * f10),
							(double) (f13 + f3 * f10 - f5 * f10))
					.tex((double) f6, (double) f9).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
					.lightmap(l, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			Tessellator.getInstance().draw();
			GlStateManager.enableLighting();
		}
	}

	public int getBrightnessForRender(float var1) {
		return '\uf0f0';
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		++this.field_70581_a;
		if (this.field_70581_a == this.field_70584_aq) {
			this.setDead();
		}

	}

	public int getFXLayer() {
		return 3;
	}

	public static class Factory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double d3, double d4,
				double d5, int... var15) {
			return new EntityLargeExplodeFX(Minecraft.getMinecraft().getTextureManager(), world, d0, d1, d2, d3, d4,
					d5);
		}
	}
}