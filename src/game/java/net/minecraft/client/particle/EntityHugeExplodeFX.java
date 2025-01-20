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
public class EntityHugeExplodeFX extends EntityFX {
	private int timeSinceStart;
	/**+
	 * the maximum time for the explosion
	 */
	private int maximumTime = 8;

	protected EntityHugeExplodeFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double parDouble1,
			double parDouble2, double parDouble3) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
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
		for (int i = 0; i < 6; ++i) {
			double d0 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
			double d1 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
			double d2 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2,
					(double) ((float) this.timeSinceStart / (float) this.maximumTime), 0.0D, 0.0D, new int[0]);
		}

		++this.timeSinceStart;
		if (this.timeSinceStart == this.maximumTime) {
			this.setDead();
		}

	}

	public int getFXLayer() {
		return 1;
	}

	public static class Factory implements IParticleFactory {
		public EntityFX getEntityFX(int var1, World world, double d0, double d1, double d2, double d3, double d4,
				double d5, int... var15) {
			return new EntityHugeExplodeFX(world, d0, d1, d2, d3, d4, d5);
		}
	}
}