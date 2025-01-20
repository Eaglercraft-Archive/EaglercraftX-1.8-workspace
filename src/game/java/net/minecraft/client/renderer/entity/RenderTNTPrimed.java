package net.minecraft.client.renderer.entity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
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
public class RenderTNTPrimed extends Render<EntityTNTPrimed> {
	public RenderTNTPrimed(RenderManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0.5F;
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
	public void doRender(EntityTNTPrimed entitytntprimed, double d0, double d1, double d2, float f, float f1) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) d0, (float) d1 + 0.5F, (float) d2);
		if ((float) entitytntprimed.fuse - f1 + 1.0F < 10.0F) {
			float f2 = 1.0F - ((float) entitytntprimed.fuse - f1 + 1.0F) / 10.0F;
			f2 = MathHelper.clamp_float(f2, 0.0F, 1.0F);
			f2 = f2 * f2;
			f2 = f2 * f2;
			float f3 = 1.0F + f2 * 0.3F;
			GlStateManager.scale(f3, f3, f3);
		}

		float f4 = (1.0F - ((float) entitytntprimed.fuse - f1 + 1.0F) / 100.0F) * 0.8F;
		this.bindEntityTexture(entitytntprimed);
		GlStateManager.translate(-0.5F, -0.5F, 0.5F);
		boolean light = entitytntprimed.fuse / 5 % 2 == 0;
		boolean deferred = DeferredStateManager.isInDeferredPass();
		if (light && deferred) {
			DeferredStateManager.setEmissionConstant(1.0f);
			DeferredStateManager.disableMaterialTexture();
			GlStateManager.enableShaderBlendAdd();
			GlStateManager.setShaderBlendSrc(0.0f, 0.0f, 0.0f, 0.0f);
			GlStateManager.setShaderBlendAdd(1.0f, 1.0f, 1.0f, 1.0f);
		}
		blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), entitytntprimed.getBrightness(f1));
		GlStateManager.translate(0.0F, 0.0F, 1.0F);
		if (light && !deferred) {
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_DST_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, f4);
			GlStateManager.doPolygonOffset(-3.0F, -3.0F);
			GlStateManager.enablePolygonOffset();
			blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0F);
			GlStateManager.doPolygonOffset(0.0F, 0.0F);
			GlStateManager.disablePolygonOffset();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
		}

		GlStateManager.popMatrix();
		super.doRender(entitytntprimed, d0, d1, d2, f, f1);
		if (light && deferred) {
			DeferredStateManager.setEmissionConstant(0.0f);
			DeferredStateManager.enableMaterialTexture();
			GlStateManager.disableShaderBlendAdd();
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityTNTPrimed var1) {
		return TextureMap.locationBlocksTexture;
	}
}