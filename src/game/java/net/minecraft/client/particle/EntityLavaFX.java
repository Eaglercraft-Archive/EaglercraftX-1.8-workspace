package net.minecraft.client.particle;

import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
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
public class EntityLavaFX extends EntityFX {
	private float lavaParticleScale;

	protected EntityLavaFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
		this.motionX *= 0.800000011920929D;
		this.motionY *= 0.800000011920929D;
		this.motionZ *= 0.800000011920929D;
		this.motionY = (double) (this.rand.nextFloat() * 0.4F + 0.05F);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleScale *= this.rand.nextFloat() * 2.0F + 0.2F;
		this.lavaParticleScale = this.particleScale;
		this.particleMaxAge = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
		this.noClip = false;
		this.setParticleTextureIndex(49);
	}

	public int getBrightnessForRender(float f) {
		float f1 = ((float) this.particleAge + f) / (float) this.particleMaxAge;
		f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
		int i = super.getBrightnessForRender(f);
		short short1 = 240;
		int j = i >> 16 & 255;
		return short1 | j << 16;
	}

	/**+
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float var1) {
		return 1.0F;
	}

	/**+
	 * Renders the particle
	 */
	public void renderParticle(WorldRenderer worldrenderer, Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		float f6 = ((float) this.particleAge + f) / (float) this.particleMaxAge;
		this.particleScale = this.lavaParticleScale * (1.0F - f6 * f6);
		super.renderParticle(worldrenderer, entity, f, f1, f2, f3, f4, f5);
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}

		float f = (float) this.particleAge / (float) this.particleMaxAge;
		if (this.rand.nextFloat() > f) {
			this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, this.motionX,
					this.motionY, this.motionZ, new int[0]);
		}

		this.motionY -= 0.03D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9990000128746033D;
		this.motionY *= 0.9990000128746033D;
		this.motionZ *= 0.9990000128746033D;
		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}

	}

	public static class Factory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double var9, double var11,
				double var13, int... var15) {
			return new EntityLavaFX(world, d0, d1, d2);
		}
	}
}