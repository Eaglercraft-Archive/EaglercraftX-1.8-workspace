package net.minecraft.client;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IProgressUpdate;

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
public class LoadingScreenRenderer implements IProgressUpdate {
	private String message = "";
	private Minecraft mc;
	/**+
	 * The text currently displayed (i.e. the argument to the last
	 * call to printText or func_73722_d)
	 */
	private String currentlyDisplayedText = "";
	/**+
	 * The system's time represented in milliseconds.
	 */
	private long systemTime = Minecraft.getSystemTime();
	private boolean field_73724_e;

	public LoadingScreenRenderer(Minecraft mcIn) {
		this.mc = mcIn;
	}

	/**+
	 * this string, followed by "working..." and then the "%
	 * complete" are the 3 lines shown. This resets progress to 0,
	 * and the WorkingString to "working...".
	 */
	public void resetProgressAndMessage(String message) {
		this.field_73724_e = false;
		this.displayString(message);
	}

	/**+
	 * Shows the 'Saving level' string.
	 */
	public void displaySavingString(String message) {
		this.field_73724_e = true;
		this.displayString(message);
	}

	private void displayString(String message) {
		this.currentlyDisplayedText = message;
		if (this.mc.running) {
			GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
			GlStateManager.matrixMode(GL_PROJECTION);
			GlStateManager.loadIdentity();
			GlStateManager.ortho(0.0D, mc.scaledResolution.getScaledWidth_double(),
					mc.scaledResolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
			GlStateManager.matrixMode(GL_MODELVIEW);
			GlStateManager.loadIdentity();
			GlStateManager.translate(0.0F, 0.0F, -200.0F);
		}
	}

	/**+
	 * Displays a string on the loading screen supposed to indicate
	 * what is being done currently.
	 */
	public void displayLoadingString(String message) {
		if (this.mc.running) {
			this.systemTime = 0L;
			this.message = message;
			this.setLoadingProgress(-1);
			this.systemTime = 0L;
		}
	}

	public void eaglerShow(String line1, String line2) {
		if (this.mc.running) {
			this.systemTime = 0L;
			this.currentlyDisplayedText = line1;
			this.message = line2;
			this.setLoadingProgress(-1);
			this.systemTime = 0L;
		}
	}

	public void eaglerShowRefreshResources() {
		eaglerShow(I18n.format("resourcePack.load.refreshing"), I18n.format("resourcePack.load.pleaseWait"));
	}

	/**+
	 * Updates the progress bar on the loading screen to the
	 * specified amount. Args: loadProgress
	 */
	public void setLoadingProgress(int progress) {
		if (this.mc.running) {
			long i = Minecraft.getSystemTime();
			if (i - this.systemTime >= 100L) {
				this.systemTime = i;
				ScaledResolution scaledresolution = mc.scaledResolution;
				int j = scaledresolution.getScaleFactor();
				int k = scaledresolution.getScaledWidth();
				int l = scaledresolution.getScaledHeight();
				GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
				GlStateManager.matrixMode(GL_PROJECTION);
				GlStateManager.loadIdentity();
				GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(),
						scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
				GlStateManager.matrixMode(GL_MODELVIEW);
				GlStateManager.loadIdentity();
				GlStateManager.translate(0.0F, 0.0F, -200.0F);
				GlStateManager.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

				Tessellator tessellator = Tessellator.getInstance();
				WorldRenderer worldrenderer = tessellator.getWorldRenderer();
				this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
				float f = 32.0F;
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				worldrenderer.pos(0.0D, (double) l, 0.0D).tex(0.0D, (double) ((float) l / f)).color(64, 64, 64, 255)
						.endVertex();
				worldrenderer.pos((double) k, (double) l, 0.0D).tex((double) ((float) k / f), (double) ((float) l / f))
						.color(64, 64, 64, 255).endVertex();
				worldrenderer.pos((double) k, 0.0D, 0.0D).tex((double) ((float) k / f), 0.0D).color(64, 64, 64, 255)
						.endVertex();
				worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 255).endVertex();
				tessellator.draw();
				if (progress >= 0) {
					byte b0 = 100;
					byte b1 = 2;
					int i1 = k / 2 - b0 / 2;
					int j1 = l / 2 + 16;
					GlStateManager.disableTexture2D();
					worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
					worldrenderer.pos((double) i1, (double) j1, 0.0D).color(128, 128, 128, 255).endVertex();
					worldrenderer.pos((double) i1, (double) (j1 + b1), 0.0D).color(128, 128, 128, 255).endVertex();
					worldrenderer.pos((double) (i1 + b0), (double) (j1 + b1), 0.0D).color(128, 128, 128, 255)
							.endVertex();
					worldrenderer.pos((double) (i1 + b0), (double) j1, 0.0D).color(128, 128, 128, 255).endVertex();
					worldrenderer.pos((double) i1, (double) j1, 0.0D).color(128, 255, 128, 255).endVertex();
					worldrenderer.pos((double) i1, (double) (j1 + b1), 0.0D).color(128, 255, 128, 255).endVertex();
					worldrenderer.pos((double) (i1 + progress), (double) (j1 + b1), 0.0D).color(128, 255, 128, 255)
							.endVertex();
					worldrenderer.pos((double) (i1 + progress), (double) j1, 0.0D).color(128, 255, 128, 255)
							.endVertex();
					tessellator.draw();
					GlStateManager.enableTexture2D();
				}

				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				this.mc.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText,
						(float) ((k - this.mc.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2),
						(float) (l / 2 - 4 - 16), 16777215);
				if (this.message != null) {
					this.mc.fontRendererObj.drawStringWithShadow(this.message,
							(float) ((k - this.mc.fontRendererObj.getStringWidth(this.message)) / 2),
							(float) (l / 2 - 4 + 8), 16777215);
				}
				this.mc.updateDisplay();
			}
		}
	}

	public void setDoneWorking() {
	}
}