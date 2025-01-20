package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.minecraft.client.resources.I18n;
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
public class ScreenChatOptions extends GuiScreen {
	private static final GameSettings.Options[] field_146399_a = new GameSettings.Options[] {
			GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS,
			GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE,
			GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED,
			GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO,
			GameSettings.Options.EAGLER_PROFANITY_FILTER };
	private static final GameSettings.Options[] no_profanity_filter = new GameSettings.Options[] {
			GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS,
			GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE,
			GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED,
			GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO };
	private final GuiScreen parentScreen;
	private final GameSettings game_settings;
	private String field_146401_i;

	public ScreenChatOptions(GuiScreen parentScreenIn, GameSettings gameSettingsIn) {
		this.parentScreen = parentScreenIn;
		this.game_settings = gameSettingsIn;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		int i = 0;
		this.field_146401_i = I18n.format("options.chat.title", new Object[0]);

		boolean profanityFilterForce = EagRuntime.getConfiguration().isForceProfanityFilter();
		GameSettings.Options[] opts = profanityFilterForce ? no_profanity_filter : field_146399_a;
		for (int j = 0; j < opts.length; ++j) {
			GameSettings.Options gamesettings$options = opts[j];
			if (gamesettings$options.getEnumFloat()) {
				this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(),
						this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), gamesettings$options));
			} else {
				this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(),
						this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), gamesettings$options,
						this.game_settings.getKeyBinding(gamesettings$options)));
			}

			++i;
		}

		this.buttonList.add(new GuiButton(200, this.width / 2 - 100,
				this.height / 6 + (profanityFilterForce ? 130 : 154), I18n.format("gui.done", new Object[0])));
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.enabled) {
			if (parGuiButton.id < 100 && parGuiButton instanceof GuiOptionButton) {
				this.game_settings.setOptionValue(((GuiOptionButton) parGuiButton).returnEnumOptions(), 1);
				parGuiButton.displayString = this.game_settings
						.getKeyBinding(GameSettings.Options.getEnumOptions(parGuiButton.id));
			}

			if (parGuiButton.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentScreen);
			}

		}
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, this.field_146401_i, this.width / 2, 20, 16777215);
		super.drawScreen(i, j, f);
	}
}