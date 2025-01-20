package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EnumPlayerModelParts;

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
public class GuiCustomizeSkin extends GuiScreen {
	private final GuiScreen parentScreen;
	private String title;

	private GuiButton enableFNAWSkinsButton;

	public GuiCustomizeSkin(GuiScreen parentScreenIn) {
		this.parentScreen = parentScreenIn;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		int i = 0;
		this.title = I18n.format("options.skinCustomisation.title", new Object[0]);

		EnumPlayerModelParts[] parts = EnumPlayerModelParts._VALUES;
		for (int k = 0; k < parts.length; ++k) {
			EnumPlayerModelParts enumplayermodelparts = parts[k];
			this.buttonList.add(new GuiCustomizeSkin.ButtonPart(enumplayermodelparts.getPartId(),
					this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20,
					enumplayermodelparts));
			++i;
		}

		if (i % 2 == 1) {
			++i;
		}

		this.buttonList.add(enableFNAWSkinsButton = new GuiButton(201, this.width / 2 - 100,
				this.height / 6 + 10 + 24 * (i >> 1), I18n.format("options.skinCustomisation.enableFNAWSkins") + ": "
						+ I18n.format(mc.gameSettings.enableFNAWSkins ? "options.on" : "options.off")));
		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 40 + 24 * (i >> 1),
				I18n.format("gui.done", new Object[0])));
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.enabled) {
			if (parGuiButton.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentScreen);
			} else if (parGuiButton.id == 201) {
				mc.gameSettings.enableFNAWSkins = !mc.gameSettings.enableFNAWSkins;
				mc.getRenderManager().setEnableFNAWSkins(mc.getEnableFNAWSkins());
				enableFNAWSkinsButton.displayString = I18n.format("options.skinCustomisation.enableFNAWSkins") + ": "
						+ I18n.format(mc.gameSettings.enableFNAWSkins ? "options.on" : "options.off");
			} else if (parGuiButton instanceof GuiCustomizeSkin.ButtonPart) {
				EnumPlayerModelParts enumplayermodelparts = ((GuiCustomizeSkin.ButtonPart) parGuiButton).playerModelParts;
				this.mc.gameSettings.switchModelPartEnabled(enumplayermodelparts);
				parGuiButton.displayString = this.func_175358_a(enumplayermodelparts);
			}

		}
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 20, 16777215);
		super.drawScreen(i, j, f);
	}

	private String func_175358_a(EnumPlayerModelParts playerModelParts) {
		String s;
		if (this.mc.gameSettings.getModelParts().contains(playerModelParts)) {
			s = I18n.format("options.on", new Object[0]);
		} else {
			s = I18n.format("options.off", new Object[0]);
		}

		/*
		 * TODO: I changed this to getUnformattedText() from getFormattedText() because
		 * the latter was returning a pink formatting code at the end for no reason
		 */
		return playerModelParts.func_179326_d().getUnformattedText() + ": " + s;
	}

	class ButtonPart extends GuiButton {
		private final EnumPlayerModelParts playerModelParts;

		private ButtonPart(int parInt1, int parInt2, int parInt3, int parInt4, int parInt5,
				EnumPlayerModelParts playerModelParts) {
			super(parInt1, parInt2, parInt3, parInt4, parInt5, GuiCustomizeSkin.this.func_175358_a(playerModelParts));
			this.playerModelParts = playerModelParts;
		}
	}
}