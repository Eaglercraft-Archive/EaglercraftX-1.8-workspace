package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

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
public class GuiLanguage extends GuiScreen {
	protected GuiScreen parentScreen;
	private GuiLanguage.List list;
	private final GameSettings game_settings_3;
	private final LanguageManager languageManager;
	private GuiOptionButton forceUnicodeFontBtn;
	private GuiOptionButton confirmSettingsBtn;

	public GuiLanguage(GuiScreen screen, GameSettings gameSettingsObj, LanguageManager manager) {
		this.parentScreen = screen;
		this.game_settings_3 = gameSettingsObj;
		this.languageManager = manager;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		this.buttonList.add(this.forceUnicodeFontBtn = new GuiOptionButton(100, this.width / 2 - 155, this.height - 38,
				GameSettings.Options.FORCE_UNICODE_FONT,
				this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
		this.buttonList.add(this.confirmSettingsBtn = new GuiOptionButton(6, this.width / 2 - 155 + 160,
				this.height - 38, I18n.format("gui.done", new Object[0])));
		this.list = new GuiLanguage.List(this.mc);
		this.list.registerScrollButtons(7, 8);
	}

	/**+
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.list.handleMouseInput();
	}

	public void handleTouchInput() throws IOException {
		super.handleTouchInput();
		this.list.handleTouchInput();
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.enabled) {
			switch (parGuiButton.id) {
			case 5:
				break;
			case 6:
				this.mc.displayGuiScreen(this.parentScreen);
				break;
			case 100:
				if (parGuiButton instanceof GuiOptionButton) {
					this.game_settings_3.setOptionValue(((GuiOptionButton) parGuiButton).returnEnumOptions(), 1);
					parGuiButton.displayString = this.game_settings_3
							.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
					ScaledResolution scaledresolution = this.mc.scaledResolution;
					int i = scaledresolution.getScaledWidth();
					int j = scaledresolution.getScaledHeight();
					this.setWorldAndResolution(this.mc, i, j);
				}
				break;
			default:
				this.list.actionPerformed(parGuiButton);
			}

		}
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.list.drawScreen(i, j, f);
		this.drawCenteredString(this.fontRendererObj, I18n.format("options.language", new Object[0]), this.width / 2,
				16, 16777215);
		this.drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning", new Object[0]) + ")",
				this.width / 2, this.height - 56, 8421504);
		super.drawScreen(i, j, f);
	}

	class List extends GuiSlot {
		private final java.util.List<String> langCodeList = Lists.newArrayList();
		private final Map<String, Language> languageMap = Maps.newHashMap();

		public List(Minecraft mcIn) {
			super(mcIn, GuiLanguage.this.width, GuiLanguage.this.height, 32, GuiLanguage.this.height - 65 + 4, 18);

			for (Language language : GuiLanguage.this.languageManager.getLanguages()) {
				this.languageMap.put(language.getLanguageCode(), language);
				this.langCodeList.add(language.getLanguageCode());
			}

		}

		protected int getSize() {
			return this.langCodeList.size();
		}

		protected void elementClicked(int i, boolean var2, int var3, int var4) {
			Language language = (Language) this.languageMap.get(this.langCodeList.get(i));
			GuiLanguage.this.languageManager.setCurrentLanguage(language);
			GuiLanguage.this.game_settings_3.language = language.getLanguageCode();
			this.mc.loadingScreen.eaglerShowRefreshResources();
			this.mc.refreshResources();
			GuiLanguage.this.fontRendererObj.setUnicodeFlag(GuiLanguage.this.languageManager.isCurrentLocaleUnicode()
					|| GuiLanguage.this.game_settings_3.forceUnicodeFont);
			GuiLanguage.this.fontRendererObj
					.setBidiFlag(GuiLanguage.this.languageManager.isCurrentLanguageBidirectional());
			GuiLanguage.this.confirmSettingsBtn.displayString = I18n.format("gui.done", new Object[0]);
			GuiLanguage.this.forceUnicodeFontBtn.displayString = GuiLanguage.this.game_settings_3
					.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
			GuiLanguage.this.game_settings_3.saveOptions();
			GuiLanguage.this.mc.displayGuiScreen(GuiLanguage.this);
		}

		protected boolean isSelected(int i) {
			return ((String) this.langCodeList.get(i))
					.equals(GuiLanguage.this.languageManager.getCurrentLanguage().getLanguageCode());
		}

		protected int getContentHeight() {
			return this.getSize() * 18;
		}

		protected void drawBackground() {
			GuiLanguage.this.drawDefaultBackground();
		}

		protected void drawSlot(int i, int var2, int j, int var4, int var5, int var6) {
			GuiLanguage.this.fontRendererObj.setBidiFlag(true);
			GuiLanguage.this.drawCenteredString(GuiLanguage.this.fontRendererObj,
					((Language) this.languageMap.get(this.langCodeList.get(i))).toString(), this.width / 2, j + 1,
					16777215);
			GuiLanguage.this.fontRendererObj
					.setBidiFlag(GuiLanguage.this.languageManager.getCurrentLanguage().isBidirectional());
		}
	}
}