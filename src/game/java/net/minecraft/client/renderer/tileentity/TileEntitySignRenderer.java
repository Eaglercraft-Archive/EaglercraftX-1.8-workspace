package net.minecraft.client.renderer.tileentity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.model.ModelSign;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;
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
public class TileEntitySignRenderer extends TileEntitySpecialRenderer<TileEntitySign> {
	private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
	/**+
	 * The ModelSign instance for use in this renderer
	 */
	private final ModelSign model = new ModelSign();

	public static boolean disableProfanityFilter = false;

	public void renderTileEntityAt(TileEntitySign tileentitysign, double d0, double d1, double d2, float var8, int i) {
		Block block = tileentitysign.getBlockType();
		GlStateManager.pushMatrix();
		float f = 0.6666667F;
		if (block == Blocks.standing_sign) {
			GlStateManager.translate((float) d0 + 0.5F, (float) d1 + 0.75F * f, (float) d2 + 0.5F);
			float f1 = (float) (tileentitysign.getBlockMetadata() * 360) / 16.0F;
			GlStateManager.rotate(-f1, 0.0F, 1.0F, 0.0F);
			this.model.signStick.showModel = true;
		} else {
			int k = tileentitysign.getBlockMetadata();
			float f2 = 0.0F;
			if (k == 2) {
				f2 = 180.0F;
			}

			if (k == 4) {
				f2 = 90.0F;
			}

			if (k == 5) {
				f2 = -90.0F;
			}

			GlStateManager.translate((float) d0 + 0.5F, (float) d1 + 0.75F * f, (float) d2 + 0.5F);
			GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
			this.model.signStick.showModel = false;
		}

		if (i >= 0) {
			this.bindTexture(DESTROY_STAGES[i]);
			GlStateManager.matrixMode(GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 2.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(GL_MODELVIEW);
		} else {
			this.bindTexture(SIGN_TEXTURE);
		}

		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		GlStateManager.scale(f, -f, -f);
		this.model.renderSign();
		GlStateManager.popMatrix();
		FontRenderer fontrenderer = this.getFontRenderer();
		float f3 = 0.015625F * f;
		GlStateManager.translate(0.0F, 0.5F * f, 0.07F * f);
		GlStateManager.scale(f3, -f3, f3);
		EaglercraftGPU.glNormal3f(0.0F, 0.0F, -1.0F * f3);
		GlStateManager.depthMask(false);
		byte b0 = 0;
		if (i < 0) {
			if (DeferredStateManager.isInDeferredPass()) {
				_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);
				GlStateManager.colorMask(true, true, true, false);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			}
			IChatComponent[] signText = disableProfanityFilter ? tileentitysign.signText
					: tileentitysign.getSignTextProfanityFilter();
			for (int j = 0; j < signText.length; ++j) {
				if (signText[j] != null) {
					IChatComponent ichatcomponent = signText[j];
					List list = GuiUtilRenderComponents.func_178908_a(ichatcomponent, 90, fontrenderer, false, true);
					String s = list != null && list.size() > 0 ? ((IChatComponent) list.get(0)).getFormattedText() : "";
					if (j == tileentitysign.lineBeingEdited) {
						s = "> " + s + " <";
						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - signText.length * 5,
								b0);
					} else {
						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - signText.length * 5,
								b0);
					}
				}
			}
			if (DeferredStateManager.isInDeferredPass()) {
				_wglDrawBuffers(EaglerDeferredPipeline.instance.gBufferDrawBuffers);
				GlStateManager.colorMask(true, true, true, true);
			}
		}

		GlStateManager.depthMask(true);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
		if (i >= 0) {
			GlStateManager.matrixMode(GL_TEXTURE);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL_MODELVIEW);
		}

	}
}