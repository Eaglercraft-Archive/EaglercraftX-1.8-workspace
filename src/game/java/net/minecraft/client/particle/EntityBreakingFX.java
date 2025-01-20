package net.minecraft.client.particle;

import net.lax1dude.eaglercraft.v1_8.minecraft.IAcceleratedParticleEngine;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
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
public class EntityBreakingFX extends EntityFX {
	protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, Item parItem) {
		this(worldIn, posXIn, posYIn, posZIn, parItem, 0);
	}

	protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn, Item parItem, int parInt1) {
		this(worldIn, posXIn, posYIn, posZIn, parItem, parInt1);
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		this.motionX += xSpeedIn;
		this.motionY += ySpeedIn;
		this.motionZ += zSpeedIn;
	}

	protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, Item parItem, int parInt1) {
		super(worldIn, posXIn, posYIn, posZIn, 0.0D, 0.0D, 0.0D);
		this.setParticleIcon(
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(parItem, parInt1));
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleGravity = Blocks.snow.blockParticleGravity;
		this.particleScale /= 2.0F;
	}

	public int getFXLayer() {
		return 1;
	}

	/**+
	 * Renders the particle
	 */
	public void renderParticle(WorldRenderer worldrenderer, Entity var2, float f, float f1, float f2, float f3,
			float f4, float f5) {
		float f6 = ((float) this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
		float f7 = f6 + 0.015609375F;
		float f8 = ((float) this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
		float f9 = f8 + 0.015609375F;
		float f10 = 0.1F * this.particleScale;
		if (this.particleIcon != null) {
			f6 = this.particleIcon.getInterpolatedU((double) (this.particleTextureJitterX / 4.0F * 16.0F));
			f7 = this.particleIcon.getInterpolatedU((double) ((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
			f8 = this.particleIcon.getInterpolatedV((double) (this.particleTextureJitterY / 4.0F * 16.0F));
			f9 = this.particleIcon.getInterpolatedV((double) ((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
		}

		float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) f - interpPosX);
		float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) f - interpPosY);
		float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) f - interpPosZ);
		int i = this.getBrightnessForRender(f);
		int j = i >> 16 & '\uffff';
		int k = i & '\uffff';
		worldrenderer
				.pos((double) (f11 - f1 * f10 - f4 * f10), (double) (f12 - f2 * f10),
						(double) (f13 - f3 * f10 - f5 * f10))
				.tex((double) f6, (double) f9).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
				.lightmap(j, k).endVertex();
		worldrenderer
				.pos((double) (f11 - f1 * f10 + f4 * f10), (double) (f12 + f2 * f10),
						(double) (f13 - f3 * f10 + f5 * f10))
				.tex((double) f6, (double) f8).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
				.lightmap(j, k).endVertex();
		worldrenderer
				.pos((double) (f11 + f1 * f10 + f4 * f10), (double) (f12 + f2 * f10),
						(double) (f13 + f3 * f10 + f5 * f10))
				.tex((double) f7, (double) f8).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
				.lightmap(j, k).endVertex();
		worldrenderer
				.pos((double) (f11 + f1 * f10 - f4 * f10), (double) (f12 - f2 * f10),
						(double) (f13 + f3 * f10 - f5 * f10))
				.tex((double) f7, (double) f9).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
				.lightmap(j, k).endVertex();
	}

	public boolean renderAccelerated(IAcceleratedParticleEngine accelerator, Entity var2, float f, float f1, float f2,
			float f3, float f4, float f5) {
		int w = this.particleIcon.getIconWidth();
		int h = this.particleIcon.getIconHeight();
		int xOffset = MathHelper.floor_float(w * this.particleTextureJitterX * 4.0f * 0.0625f);
		int yOffset = MathHelper.floor_float(h * this.particleTextureJitterY * 4.0f * 0.0625f);
		int texSize = Math.min(w, h) / 4;
		accelerator.drawParticle(this, this.particleIcon.getOriginX() + xOffset,
				this.particleIcon.getOriginY() + yOffset, getBrightnessForRender(f), texSize, particleScale * 0.1f,
				this.particleRed, this.particleGreen, this.particleBlue, 1.0f);
		return true;
	}

	public static class Factory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double d3, double d4,
				double d5, int... aint) {
			int i = aint.length > 1 ? aint[1] : 0;
			return new EntityBreakingFX(world, d0, d1, d2, d3, d4, d5, Item.getItemById(aint[0]), i);
		}
	}

	public static class SlimeFactory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double var9, double var11,
				double var13, int... var15) {
			return new EntityBreakingFX(world, d0, d1, d2, Items.slime_ball);
		}
	}

	public static class SnowballFactory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double var9, double var11,
				double var13, int... var15) {
			return new EntityBreakingFX(world, d0, d1, d2, Items.snowball);
		}
	}
}