package net.minecraft.client.renderer.entity.layers;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.entity.boss.EntityDragon;
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
public class LayerEnderDragonEyes implements LayerRenderer<EntityDragon> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
	private final RenderDragon dragonRenderer;

	public LayerEnderDragonEyes(RenderDragon dragonRendererIn) {
		this.dragonRenderer = dragonRendererIn;
	}

	public void doRenderLayer(EntityDragon entitydragon, float f, float f1, float f2, float f3, float f4, float f5,
			float f6) {
		this.dragonRenderer.bindTexture(TEXTURE);
		if (DeferredStateManager.isInDeferredPass()) {
			DeferredStateManager.setEmissionConstant(0.5f);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enablePolygonOffset();
			GlStateManager.doPolygonOffset(-0.025f, 1.0f);
			this.dragonRenderer.getMainModel().render(entitydragon, f, f1, f3, f4, f5, f6);
			this.dragonRenderer.func_177105_a(entitydragon, f2);
			GlStateManager.disablePolygonOffset();
			DeferredStateManager.setEmissionConstant(0.0f);
			return;
		}
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(GL_ONE, GL_ONE);
		GlStateManager.disableLighting();
		GlStateManager.depthFunc(GL_EQUAL);
		char c0 = '\uf0f0';
		int i = c0 % 65536;
		int j = c0 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) i / 1.0F, (float) j / 1.0F);
		GlStateManager.enableLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.dragonRenderer.getMainModel().render(entitydragon, f, f1, f3, f4, f5, f6);
		this.dragonRenderer.func_177105_a(entitydragon, f2);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.depthFunc(GL_LEQUAL);
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}