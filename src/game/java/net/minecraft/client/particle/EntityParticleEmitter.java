package net.minecraft.client.particle;

import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
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
public class EntityParticleEmitter extends EntityFX {
	private Entity attachedEntity;
	private int age;
	private int lifetime;
	private EnumParticleTypes particleTypes;

	public EntityParticleEmitter(World worldIn, Entity parEntity, EnumParticleTypes particleTypesIn) {
		super(worldIn, parEntity.posX, parEntity.getEntityBoundingBox().minY + (double) (parEntity.height / 2.0F),
				parEntity.posZ, parEntity.motionX, parEntity.motionY, parEntity.motionZ);
		this.attachedEntity = parEntity;
		this.lifetime = 3;
		this.particleTypes = particleTypesIn;
		this.onUpdate();
	}

	/**+
	 * Renders the particle
	 */
	public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6,
			float var7, float var8) {
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		for (int i = 0; i < 16; ++i) {
			double d0 = (double) (this.rand.nextFloat() * 2.0F - 1.0F);
			double d1 = (double) (this.rand.nextFloat() * 2.0F - 1.0F);
			double d2 = (double) (this.rand.nextFloat() * 2.0F - 1.0F);
			if (d0 * d0 + d1 * d1 + d2 * d2 <= 1.0D) {
				double d3 = this.attachedEntity.posX + d0 * (double) this.attachedEntity.width / 4.0D;
				double d4 = this.attachedEntity.getEntityBoundingBox().minY
						+ (double) (this.attachedEntity.height / 2.0F)
						+ d1 * (double) this.attachedEntity.height / 4.0D;
				double d5 = this.attachedEntity.posZ + d2 * (double) this.attachedEntity.width / 4.0D;
				this.worldObj.spawnParticle(this.particleTypes, false, d3, d4, d5, d0, d1 + 0.2D, d2, new int[0]);
			}
		}

		++this.age;
		if (this.age >= this.lifetime) {
			this.setDead();
		}

	}

	public int getFXLayer() {
		return 3;
	}
}