package net.minecraft.client.renderer.tileentity;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityWitherSkull;
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
public class RenderWitherSkull extends Render<EntityWitherSkull> {
	private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation(
			"textures/entity/wither/wither_invulnerable.png");
	private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
	/**+
	 * The Skeleton's head model.
	 */
	private final ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();

	public RenderWitherSkull(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	private float func_82400_a(float parFloat1, float parFloat2, float parFloat3) {
		float f;
		for (f = parFloat2 - parFloat1; f < -180.0F; f += 360.0F) {
			;
		}

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return parFloat1 + parFloat3 * f;
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
	public void doRender(EntityWitherSkull entitywitherskull, double d0, double d1, double d2, float f, float f1) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		float f2 = this.func_82400_a(entitywitherskull.prevRotationYaw, entitywitherskull.rotationYaw, f1);
		float f3 = entitywitherskull.prevRotationPitch
				+ (entitywitherskull.rotationPitch - entitywitherskull.prevRotationPitch) * f1;
		GlStateManager.translate((float) d0, (float) d1, (float) d2);
		float f4 = 0.0625F;
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		GlStateManager.enableAlpha();
		this.bindEntityTexture(entitywitherskull);
		this.skeletonHeadModel.render(entitywitherskull, 0.0F, 0.0F, 0.0F, f2, f3, f4);
		GlStateManager.popMatrix();
		super.doRender(entitywitherskull, d0, d1, d2, f, f1);
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityWitherSkull entitywitherskull) {
		return entitywitherskull.isInvulnerable() ? invulnerableWitherTextures : witherTextures;
	}
}