package net.minecraft.client.particle;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
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
public class MobAppearance extends EntityFX {
	private EntityLivingBase entity;

	protected MobAppearance(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.motionX = this.motionY = this.motionZ = 0.0D;
		this.particleGravity = 0.0F;
		this.particleMaxAge = 30;
	}

	public int getFXLayer() {
		return 3;
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		if (this.entity == null) {
			EntityGuardian entityguardian = new EntityGuardian(this.worldObj);
			entityguardian.setElder();
			this.entity = entityguardian;
		}

	}

	/**+
	 * Renders the particle
	 */
	public void renderParticle(WorldRenderer var1, Entity entityx, float f, float var4, float var5, float var6,
			float var7, float var8) {
		if (this.entity != null) {
			RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
			rendermanager.setRenderPosition(EntityFX.interpPosX, EntityFX.interpPosY, EntityFX.interpPosZ);
			float f1 = 0.42553192F;
			float f2 = ((float) this.particleAge + f) / (float) this.particleMaxAge;
			GlStateManager.depthMask(true);
			GlStateManager.enableBlend();
			GlStateManager.enableDepth();
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			float f3 = 240.0F;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f3, f3);
			GlStateManager.pushMatrix();
			float f4 = 0.05F + 0.5F * MathHelper.sin(f2 * 3.1415927F);
			GlStateManager.color(1.0F, 1.0F, 1.0F, f4);
			GlStateManager.translate(0.0F, 1.8F, 0.0F);
			GlStateManager.rotate(180.0F - entityx.rotationYaw, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(60.0F - 150.0F * f2 - entityx.rotationPitch, 1.0F, 0.0F, 0.0F);
			GlStateManager.translate(0.0F, -0.4F, -1.5F);
			GlStateManager.scale(f1, f1, f1);
			this.entity.rotationYaw = this.entity.prevRotationYaw = 0.0F;
			this.entity.rotationYawHead = this.entity.prevRotationYawHead = 0.0F;
			rendermanager.renderEntityWithPosYaw(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, f);
			GlStateManager.popMatrix();
			GlStateManager.enableDepth();
		}
	}

	public static class Factory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double var9, double var11,
				double var13, int... var15) {
			return new MobAppearance(world, d0, d1, d2);
		}
	}
}