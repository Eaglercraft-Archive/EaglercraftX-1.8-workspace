package net.minecraft.client.gui;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuRecipient;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
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
public class GuiSpectator extends Gui implements ISpectatorMenuRecipient {
	private static final ResourceLocation field_175267_f = new ResourceLocation("textures/gui/widgets.png");
	public static final ResourceLocation field_175269_a = new ResourceLocation("textures/gui/spectator_widgets.png");
	private final Minecraft field_175268_g;
	private long field_175270_h;
	private SpectatorMenu field_175271_i;

	public GuiSpectator(Minecraft mcIn) {
		this.field_175268_g = mcIn;
	}

	public void func_175260_a(int parInt1) {
		this.field_175270_h = Minecraft.getSystemTime();
		if (this.field_175271_i != null) {
			this.field_175271_i.func_178644_b(parInt1);
		} else {
			this.field_175271_i = new SpectatorMenu(this);
		}

	}

	private float func_175265_c() {
		long i = this.field_175270_h - Minecraft.getSystemTime() + 5000L;
		return MathHelper.clamp_float((float) i / 2000.0F, 0.0F, 1.0F);
	}

	public void renderTooltip(ScaledResolution parScaledResolution, float parFloat1) {
		if (this.field_175271_i != null) {
			float f = this.func_175265_c();
			if (f <= 0.0F) {
				this.field_175271_i.func_178641_d();
			} else {
				int i = parScaledResolution.getScaledWidth() / 2;
				float f1 = this.zLevel;
				this.zLevel = -90.0F;
				float f2 = (float) parScaledResolution.getScaledHeight() - 22.0F * f;
				SpectatorDetails spectatordetails = this.field_175271_i.func_178646_f();
				this.func_175258_a(parScaledResolution, f, i, f2, spectatordetails);
				this.zLevel = f1;
			}
		}
	}

	protected void func_175258_a(ScaledResolution parScaledResolution, float parFloat1, int parInt1, float parFloat2,
			SpectatorDetails parSpectatorDetails) {
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.color(1.0F, 1.0F, 1.0F, parFloat1);
		this.field_175268_g.getTextureManager().bindTexture(field_175267_f);
		this.drawTexturedModalRect((float) (parInt1 - 91), parFloat2, 0, 0, 182, 22);
		if (parSpectatorDetails.func_178681_b() >= 0) {
			this.drawTexturedModalRect((float) (parInt1 - 91 - 1 + parSpectatorDetails.func_178681_b() * 20),
					parFloat2 - 1.0F, 0, 22, 24, 22);
		}

		RenderHelper.enableGUIStandardItemLighting();

		for (int i = 0; i < 9; ++i) {
			this.func_175266_a(i, parScaledResolution.getScaledWidth() / 2 - 90 + i * 20 + 2, parFloat2 + 3.0F,
					parFloat1, parSpectatorDetails.func_178680_a(i));
		}

		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}

	private void func_175266_a(int parInt1, int parInt2, float parFloat1, float parFloat2,
			ISpectatorMenuObject parISpectatorMenuObject) {
		this.field_175268_g.getTextureManager().bindTexture(field_175269_a);
		if (parISpectatorMenuObject != SpectatorMenu.field_178657_a) {
			int i = (int) (parFloat2 * 255.0F);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) parInt2, parFloat1, 0.0F);
			float f = parISpectatorMenuObject.func_178662_A_() ? 1.0F : 0.25F;
			GlStateManager.color(f, f, f, parFloat2);
			parISpectatorMenuObject.func_178663_a(f, i);
			GlStateManager.popMatrix();
			String s = String.valueOf(GameSettings
					.getKeyDisplayString(this.field_175268_g.gameSettings.keyBindsHotbar[parInt1].getKeyCode()));
			if (i > 3 && parISpectatorMenuObject.func_178662_A_()) {
				this.field_175268_g.fontRendererObj.drawStringWithShadow(s,
						(float) (parInt2 + 19 - 2 - this.field_175268_g.fontRendererObj.getStringWidth(s)),
						parFloat1 + 6.0F + 3.0F, 16777215 + (i << 24));
			}
		}

	}

	public void func_175263_a(ScaledResolution parScaledResolution) {
		int i = (int) (this.func_175265_c() * 255.0F);
		if (i > 3 && this.field_175271_i != null) {
			ISpectatorMenuObject ispectatormenuobject = this.field_175271_i.func_178645_b();
			String s = ispectatormenuobject != SpectatorMenu.field_178657_a
					? ispectatormenuobject.getSpectatorName().getFormattedText()
					: this.field_175271_i.func_178650_c().func_178670_b().getFormattedText();
			if (s != null) {
				int j = (parScaledResolution.getScaledWidth() - this.field_175268_g.fontRendererObj.getStringWidth(s))
						/ 2;
				int k = parScaledResolution.getScaledHeight() - 35;
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				this.field_175268_g.fontRendererObj.drawStringWithShadow(s, (float) j, (float) k, 16777215 + (i << 24));
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}

	}

	public void func_175257_a(SpectatorMenu parSpectatorMenu) {
		this.field_175271_i = null;
		this.field_175270_h = 0L;
	}

	public boolean func_175262_a() {
		return this.field_175271_i != null;
	}

	public void func_175259_b(int parInt1) {
		int i;
		for (i = this.field_175271_i.func_178648_e() + parInt1; i >= 0 && i <= 8
				&& (this.field_175271_i.func_178643_a(i) == SpectatorMenu.field_178657_a
						|| !this.field_175271_i.func_178643_a(i).func_178662_A_()); i += parInt1) {
			;
		}

		if (i >= 0 && i <= 8) {
			this.field_175271_i.func_178644_b(i);
			this.field_175270_h = Minecraft.getSystemTime();
		}

	}

	public void func_175261_b() {
		this.field_175270_h = Minecraft.getSystemTime();
		if (this.func_175262_a()) {
			int i = this.field_175271_i.func_178648_e();
			if (i != -1) {
				this.field_175271_i.func_178644_b(i);
			}
		} else {
			this.field_175271_i = new SpectatorMenu(this);
		}

	}
}