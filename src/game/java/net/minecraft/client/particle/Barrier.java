package net.minecraft.client.particle;

import net.lax1dude.eaglercraft.v1_8.minecraft.IAcceleratedParticleEngine;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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
public class Barrier extends EntityFX {
	protected Barrier(World worldIn, double parDouble1, double parDouble2, double parDouble3, Item parItem) {
		super(worldIn, parDouble1, parDouble2, parDouble3, 0.0D, 0.0D, 0.0D);
		this.setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(parItem));
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.motionX = this.motionY = this.motionZ = 0.0D;
		this.particleGravity = 0.0F;
		this.particleMaxAge = 80;
	}

	public int getFXLayer() {
		return 1;
	}

	/**+
	 * Renders the particle
	 */
	public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float parFloat2,
			float parFloat3, float parFloat4, float parFloat5, float parFloat6) {
		float f = this.particleIcon.getMinU();
		float f1 = this.particleIcon.getMaxU();
		float f2 = this.particleIcon.getMinV();
		float f3 = this.particleIcon.getMaxV();
		float f4 = 0.5F;
		float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
		float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
		float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
		int i = this.getBrightnessForRender(partialTicks);
		int j = i >> 16 & '\uffff';
		int k = i & '\uffff';
		worldRendererIn
				.pos((double) (f5 - parFloat2 * 0.5F - parFloat5 * 0.5F), (double) (f6 - parFloat3 * 0.5F),
						(double) (f7 - parFloat4 * 0.5F - parFloat6 * 0.5F))
				.tex((double) f1, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
				.lightmap(j, k).endVertex();
		worldRendererIn
				.pos((double) (f5 - parFloat2 * 0.5F + parFloat5 * 0.5F), (double) (f6 + parFloat3 * 0.5F),
						(double) (f7 - parFloat4 * 0.5F + parFloat6 * 0.5F))
				.tex((double) f1, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
				.lightmap(j, k).endVertex();
		worldRendererIn
				.pos((double) (f5 + parFloat2 * 0.5F + parFloat5 * 0.5F), (double) (f6 + parFloat3 * 0.5F),
						(double) (f7 + parFloat4 * 0.5F + parFloat6 * 0.5F))
				.tex((double) f, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
				.lightmap(j, k).endVertex();
		worldRendererIn
				.pos((double) (f5 + parFloat2 * 0.5F - parFloat5 * 0.5F), (double) (f6 - parFloat3 * 0.5F),
						(double) (f7 + parFloat4 * 0.5F - parFloat6 * 0.5F))
				.tex((double) f, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
				.lightmap(j, k).endVertex();
	}

	public boolean renderAccelerated(IAcceleratedParticleEngine accelerator, Entity var2, float f, float f1, float f2,
			float f3, float f4, float f5) {
		accelerator.drawParticle(this, particleIcon.getOriginX(), particleIcon.getOriginY(), getBrightnessForRender(f),
				Math.min(particleIcon.getIconWidth(), particleIcon.getIconHeight()), 0.5f, this.particleRed,
				this.particleGreen, this.particleBlue, this.particleAlpha);
		return true;
	}

	public static class Factory implements IParticleFactory {
		public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
				double xSpeedIn, double ySpeedIn, double zSpeedIn, int... parArrayOfInt) {
			return new Barrier(worldIn, xCoordIn, yCoordIn, zCoordIn, Item.getItemFromBlock(Blocks.barrier));
		}
	}
}