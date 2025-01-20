package net.minecraft.client.particle;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.entity.Entity;
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
public class EntitySpellParticleFX extends EntityFX {
	private static final EaglercraftRandom RANDOM = new EaglercraftRandom();
	/**+
	 * Base spell texture index
	 */
	private int baseSpellTextureIndex = 128;

	protected EntitySpellParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double parDouble1,
			double parDouble2, double parDouble3) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.5D - RANDOM.nextDouble(), parDouble2,
				0.5D - RANDOM.nextDouble());
		this.motionY *= 0.20000000298023224D;
		if (parDouble1 == 0.0D && parDouble3 == 0.0D) {
			this.motionX *= 0.10000000149011612D;
			this.motionZ *= 0.10000000149011612D;
		}

		this.particleScale *= 0.75F;
		this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		this.noClip = false;
	}

	/**+
	 * Renders the particle
	 */
	public void renderParticle(WorldRenderer worldrenderer, Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		float f6 = ((float) this.particleAge + f) / (float) this.particleMaxAge * 32.0F;
		f6 = MathHelper.clamp_float(f6, 0.0F, 1.0F);
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

		this.setParticleTextureIndex(this.baseSpellTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
		this.motionY += 0.004D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		if (this.posY == this.prevPosY) {
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;
		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}

	}

	/**+
	 * Sets the base spell texture index
	 */
	public void setBaseSpellTextureIndex(int baseSpellTextureIndexIn) {
		this.baseSpellTextureIndex = baseSpellTextureIndexIn;
	}

	public static class AmbientMobFactory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double d3, double d4,
				double d5, int... var15) {
			EntitySpellParticleFX entityspellparticlefx = new EntitySpellParticleFX(world, d0, d1, d2, d3, d4, d5);
			entityspellparticlefx.setAlphaF(0.15F);
			entityspellparticlefx.setRBGColorF((float) d3, (float) d4, (float) d5);
			return entityspellparticlefx;
		}
	}

	public static class Factory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double d3, double d4,
				double d5, int... var15) {
			return new EntitySpellParticleFX(world, d0, d1, d2, d3, d4, d5);
		}
	}

	public static class InstantFactory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double d3, double d4,
				double d5, int... var15) {
			EntitySpellParticleFX entityspellparticlefx = new EntitySpellParticleFX(world, d0, d1, d2, d3, d4, d5);
			((EntitySpellParticleFX) entityspellparticlefx).setBaseSpellTextureIndex(144);
			return entityspellparticlefx;
		}
	}

	public static class MobFactory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double d3, double d4,
				double d5, int... var15) {
			EntitySpellParticleFX entityspellparticlefx = new EntitySpellParticleFX(world, d0, d1, d2, d3, d4, d5);
			entityspellparticlefx.setRBGColorF((float) d3, (float) d4, (float) d5);
			return entityspellparticlefx;
		}
	}

	public static class WitchFactory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double d3, double d4,
				double d5, int... var15) {
			EntitySpellParticleFX entityspellparticlefx = new EntitySpellParticleFX(world, d0, d1, d2, d3, d4, d5);
			((EntitySpellParticleFX) entityspellparticlefx).setBaseSpellTextureIndex(144);
			float f = world.rand.nextFloat() * 0.5F + 0.35F;
			entityspellparticlefx.setRBGColorF(1.0F * f, 0.0F * f, 1.0F * f);
			return entityspellparticlefx;
		}
	}
}