package net.minecraft.client.particle;

import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.entity.Entity;
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
public class EntityPortalFX extends EntityFX {
	private float portalParticleScale;
	private double portalPosX;
	private double portalPosY;
	private double portalPosZ;

	protected EntityPortalFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.motionX = xSpeedIn;
		this.motionY = ySpeedIn;
		this.motionZ = zSpeedIn;
		this.portalPosX = this.posX = xCoordIn;
		this.portalPosY = this.posY = yCoordIn;
		this.portalPosZ = this.posZ = zCoordIn;
		float f = this.rand.nextFloat() * 0.6F + 0.4F;
		this.portalParticleScale = this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F * f;
		this.particleGreen *= 0.3F;
		this.particleRed *= 0.9F;
		this.particleMaxAge = (int) (Math.random() * 10.0D) + 40;
		this.noClip = true;
		this.setParticleTextureIndex((int) (Math.random() * 8.0D));
	}

	/**+
	 * Renders the particle
	 */
	public void renderParticle(WorldRenderer worldrenderer, Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		float f6 = ((float) this.particleAge + f) / (float) this.particleMaxAge;
		f6 = 1.0F - f6;
		f6 = f6 * f6;
		f6 = 1.0F - f6;
		this.particleScale = this.portalParticleScale * f6;
		super.renderParticle(worldrenderer, entity, f, f1, f2, f3, f4, f5);
	}

	public int getBrightnessForRender(float f) {
		int i = super.getBrightnessForRender(f);
		float f1 = (float) this.particleAge / (float) this.particleMaxAge;
		f1 = f1 * f1;
		f1 = f1 * f1;
		int j = i & 255;
		int k = i >> 16 & 255;
		k = k + (int) (f1 * 15.0F * 16.0F);
		if (k > 240) {
			k = 240;
		}

		return j | k << 16;
	}

	/**+
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float f) {
		float f1 = super.getBrightness(f);
		float f2 = (float) this.particleAge / (float) this.particleMaxAge;
		f2 = f2 * f2 * f2 * f2;
		return f1 * (1.0F - f2) + f2;
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		float f = (float) this.particleAge / (float) this.particleMaxAge;
		f = -f + f * f * 2.0F;
		f = 1.0F - f;
		this.posX = this.portalPosX + this.motionX * (double) f;
		this.posY = this.portalPosY + this.motionY * (double) f + (double) (1.0F - f);
		this.posZ = this.portalPosZ + this.motionZ * (double) f;
		if (this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}

	}

	public static class Factory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double d3, double d4,
				double d5, int... var15) {
			return new EntityPortalFX(world, d0, d1, d2, d3, d4, d5);
		}
	}
}