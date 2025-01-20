package net.minecraft.client.renderer.entity;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
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
public class RenderCreeper extends RenderLiving<EntityCreeper> {
	private static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");

	public RenderCreeper(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelCreeper(), 0.5F);
		this.addLayer(new LayerCreeperCharge(this));
	}

	/**+
	 * Allows the render to do any OpenGL state modifications
	 * necessary before the model is rendered. Args: entityLiving,
	 * partialTickTime
	 */
	protected void preRenderCallback(EntityCreeper entitycreeper, float f) {
		float f1 = entitycreeper.getCreeperFlashIntensity(f);
		float f2 = 1.0F + MathHelper.sin(f1 * 100.0F) * f1 * 0.01F;
		f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
		f1 = f1 * f1;
		f1 = f1 * f1;
		float f3 = (1.0F + f1 * 0.4F) * f2;
		float f4 = (1.0F + f1 * 0.1F) / f2;
		GlStateManager.scale(f3, f4, f3);
	}

	/**+
	 * Returns an ARGB int color back. Args: entityLiving,
	 * lightBrightness, partialTickTime
	 */
	protected int getColorMultiplier(EntityCreeper entitycreeper, float var2, float f) {
		float f1 = entitycreeper.getCreeperFlashIntensity(f);
		if ((int) (f1 * 10.0F) % 2 == 0) {
			return 0;
		} else {
			int i = (int) (f1 * 0.2F * 255.0F);
			i = MathHelper.clamp_int(i, 0, 255);
			return i << 24 | 16777215;
		}
	}

	public void doRender(EntityCreeper entitycreeper, double d0, double d1, double d2, float f, float f1) {
		float ff = entitycreeper.getCreeperFlashIntensity(f);
		if ((int) (ff * 10.0F) % 2 != 0) {
			DeferredStateManager.setEmissionConstant(1.0f);
		}
		try {
			super.doRender(entitycreeper, d0, d1, d2, f, f1);
		} finally {
			DeferredStateManager.setEmissionConstant(0.0f);
		}
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityCreeper var1) {
		return creeperTextures;
	}
}