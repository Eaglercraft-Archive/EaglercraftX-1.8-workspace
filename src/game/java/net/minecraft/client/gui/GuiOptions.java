package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.boot_menu.GuiScreenEnterBootMenu;
import net.lax1dude.eaglercraft.v1_8.cookie.GuiScreenRevokeSessionToken;
import net.lax1dude.eaglercraft.v1_8.cookie.ServerCookieDataStore;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenGenericErrorMessage;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.gui.GuiShaderConfig;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.gui.GuiShadersNotSupported;
import net.lax1dude.eaglercraft.v1_8.profile.GuiScreenImportExportProfile;
import net.lax1dude.eaglercraft.v1_8.recording.GuiScreenRecordingNote;
import net.lax1dude.eaglercraft.v1_8.recording.GuiScreenRecordingSettings;
import net.lax1dude.eaglercraft.v1_8.recording.ScreenRecordingController;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;

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
public class GuiOptions extends GuiScreen implements GuiYesNoCallback {
	private static final GameSettings.Options[] field_146440_f = new GameSettings.Options[] {
			GameSettings.Options.FOV };
	private final GuiScreen field_146441_g;
	private final GameSettings game_settings_1;
	private GuiButton field_175357_i;
	private GuiLockIconButton field_175356_r;
	protected String field_146442_a = "Options";
	private GuiButton broadcastSettings;

	public GuiOptions(GuiScreen parGuiScreen, GameSettings parGameSettings) {
		this.field_146441_g = parGuiScreen;
		this.game_settings_1 = parGameSettings;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		int i = 0;
		this.field_146442_a = I18n.format("options.title", new Object[0]);

		for (int j = 0; j < field_146440_f.length; ++j) {
			GameSettings.Options gamesettings$options = field_146440_f[j];
			if (gamesettings$options.getEnumFloat()) {
				this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(),
						this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1),
						gamesettings$options));
			} else {
				GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.returnEnumOrdinal(),
						this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), gamesettings$options,
						this.game_settings_1.getKeyBinding(gamesettings$options));
				this.buttonList.add(guioptionbutton);
			}

			++i;
		}

		if (this.mc.theWorld != null) {
			EnumDifficulty enumdifficulty = this.mc.theWorld.getDifficulty();
			this.field_175357_i = new GuiButton(108, this.width / 2 - 155 + i % 2 * 160,
					this.height / 6 - 12 + 24 * (i >> 1), 150, 20, this.func_175355_a(enumdifficulty));
			this.buttonList.add(this.field_175357_i);
			if (this.mc.isSingleplayer() && !this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
				this.field_175357_i.setWidth(this.field_175357_i.getButtonWidth() - 20);
				this.field_175356_r = new GuiLockIconButton(109,
						this.field_175357_i.xPosition + this.field_175357_i.getButtonWidth(),
						this.field_175357_i.yPosition);
				this.buttonList.add(this.field_175356_r);
				this.field_175356_r.func_175229_b(this.mc.theWorld.getWorldInfo().isDifficultyLocked());
				this.field_175356_r.enabled = !this.field_175356_r.func_175230_c();
				this.field_175357_i.enabled = !this.field_175356_r.func_175230_c();
			} else {
				this.field_175357_i.enabled = false;
			}
		}

		this.buttonList.add(new GuiButton(110, this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20,
				I18n.format("options.skinCustomisation", new Object[0])));
		this.buttonList.add(new GuiButton(8675309, this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20,
				I18n.format("shaders.gui.optionsButton")));
		this.buttonList.add(new GuiButton(106, this.width / 2 - 155, this.height / 6 + 72 - 6, 150, 20,
				I18n.format("options.sounds", new Object[0])));
		boolean support = ScreenRecordingController.isSupported();
		this.buttonList.add(broadcastSettings = new GuiButton(107, this.width / 2 + 5, this.height / 6 + 72 - 6, 150,
				20, I18n.format(support ? "options.screenRecording.button" : "options.screenRecording.unsupported")));
		broadcastSettings.enabled = support;
		this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height / 6 + 96 - 6, 150, 20,
				I18n.format("options.video", new Object[0])));
		this.buttonList.add(new GuiButton(100, this.width / 2 + 5, this.height / 6 + 96 - 6, 150, 20,
				I18n.format("options.controls", new Object[0])));
		this.buttonList.add(new GuiButton(102, this.width / 2 - 155, this.height / 6 + 120 - 6, 150, 20,
				I18n.format("options.language", new Object[0])));
		this.buttonList.add(new GuiButton(103, this.width / 2 + 5, this.height / 6 + 120 - 6, 150, 20,
				I18n.format("options.chat.title", new Object[0])));
		GuiButton btn;
		this.buttonList.add(btn = new GuiButton(105, this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20,
				I18n.format("options.resourcepack", new Object[0])));
		btn.enabled = EaglerFolderResourcePack.isSupported();
		this.buttonList.add(btn = new GuiButton(104, this.width / 2 + 5, this.height / 6 + 144 - 6, 150, 20,
				I18n.format("options.debugConsoleButton", new Object[0])));
		btn.enabled = EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP;
		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168,
				I18n.format("gui.done", new Object[0])));
	}

	public String func_175355_a(EnumDifficulty parEnumDifficulty) {
		ChatComponentText chatcomponenttext = new ChatComponentText("");
		chatcomponenttext.appendSibling(new ChatComponentTranslation("options.difficulty", new Object[0]));
		chatcomponenttext.appendText(": ");
		chatcomponenttext.appendSibling(
				new ChatComponentTranslation(parEnumDifficulty.getDifficultyResourceKey(), new Object[0]));
		return chatcomponenttext.getUnformattedText();
	}

	public void confirmClicked(boolean flag, int i) {
		this.mc.displayGuiScreen(this);
		if (i == 109 && flag && this.mc.theWorld != null) {
			this.mc.theWorld.getWorldInfo().setDifficultyLocked(true);
			SingleplayerServerController.setDifficulty(-1);
			this.field_175356_r.func_175229_b(true);
			this.field_175356_r.enabled = false;
			this.field_175357_i.enabled = false;
		}

	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.enabled) {
			if (parGuiButton.id < 100 && parGuiButton instanceof GuiOptionButton) {
				GameSettings.Options gamesettings$options = ((GuiOptionButton) parGuiButton).returnEnumOptions();
				this.game_settings_1.setOptionValue(gamesettings$options, 1);
				parGuiButton.displayString = this.game_settings_1
						.getKeyBinding(GameSettings.Options.getEnumOptions(parGuiButton.id));
			}

			if (parGuiButton.id == 108) {
				this.mc.theWorld.getWorldInfo().setDifficulty(
						EnumDifficulty.getDifficultyEnum(this.mc.theWorld.getDifficulty().getDifficultyId() + 1));
				SingleplayerServerController
						.setDifficulty(this.mc.theWorld.getWorldInfo().getDifficulty().getDifficultyId());
				this.field_175357_i.displayString = this.func_175355_a(this.mc.theWorld.getDifficulty());
			}

			if (parGuiButton.id == 109) {
				this.mc.displayGuiScreen(
						new GuiYesNo(this,
								(new ChatComponentTranslation("difficulty.lock.title", new Object[0]))
										.getFormattedText(),
								(new ChatComponentTranslation("difficulty.lock.question",
										new Object[] { new ChatComponentTranslation(this.mc.theWorld.getWorldInfo()
												.getDifficulty().getDifficultyResourceKey(), new Object[0]) }))
														.getFormattedText(),
								109));
			}

			if (parGuiButton.id == 110) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiCustomizeSkin(this));
			}

			if (parGuiButton.id == 8675309) {
				if (EaglerDeferredPipeline.isSupported()) {
					this.mc.displayGuiScreen(new GuiShaderConfig(this));
				} else {
					this.mc.displayGuiScreen(new GuiShadersNotSupported(this,
							I18n.format(EaglerDeferredPipeline.getReasonUnsupported())));
				}
			}

			if (parGuiButton.id == 101) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiVideoSettings(this, this.game_settings_1));
			}

			if (parGuiButton.id == 100) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiControls(this, this.game_settings_1));
			}

			if (parGuiButton.id == 102) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiLanguage(this, this.game_settings_1, this.mc.getLanguageManager()));
			}

			if (parGuiButton.id == 103) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new ScreenChatOptions(this, this.game_settings_1));
			}

			if (parGuiButton.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.field_146441_g);
			}

			if (parGuiButton.id == 105) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiScreenResourcePacks(this));
			}

			if (parGuiButton.id == 106) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiScreenOptionsSounds(this, this.game_settings_1));
			}

			if (parGuiButton.id == 107) {
				if (ScreenRecordingController.isSupported()) {
					GuiScreen screen = new GuiScreenRecordingSettings(this);
					if (!GuiScreenRecordingNote.hasShown) {
						screen = new GuiScreenRecordingNote(screen);
					}
					this.mc.displayGuiScreen(screen);
				}
			}

			if (parGuiButton.id == 104) {
				EagRuntime.showDebugConsole();
			}
		}
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, this.field_146442_a, this.width / 2, 15, 16777215);

		if (mc.theWorld == null && !EagRuntime.getConfiguration().isDemo()) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.75f, 0.75f, 0.75f);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			String text = I18n.format("editProfile.importExport");

			int w = mc.fontRendererObj.getStringWidth(text);
			boolean hover = i > 1 && j > 1 && i < (w * 3 / 4) + 7 && j < 12;
			if (hover) {
				Mouse.showCursor(EnumCursorType.HAND);
			}

			drawString(mc.fontRendererObj, EnumChatFormatting.UNDERLINE + text, 5, 5, hover ? 0xFFEEEE22 : 0xFFCCCCCC);

			GlStateManager.popMatrix();
		}

		if (mc.theWorld == null && EagRuntime.getConfiguration().isAllowBootMenu()) {
			drawCenteredString(mc.fontRendererObj, I18n.format("options.pressDeleteText"), width / 2, height / 6 + 22,
					11184810);
		}

		if (EagRuntime.getConfiguration().isEnableServerCookies() && mc.thePlayer == null) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.75f, 0.75f, 0.75f);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			String text = I18n.format("revokeSessionToken.button");

			int w = mc.fontRendererObj.getStringWidth(text);
			boolean hover = i > width - 5 - (w + 5) * 3 / 4 && j > 1 && i < width - 2 && j < 12;
			if (hover) {
				Mouse.showCursor(EnumCursorType.HAND);
			}

			drawString(mc.fontRendererObj, EnumChatFormatting.UNDERLINE + text, (width - 1) * 4 / 3 - w - 5, 5,
					hover ? 0xFFEEEE22 : 0xFFCCCCCC);

			GlStateManager.popMatrix();
		}

		super.drawScreen(i, j, f);
	}

	@Override
	protected void keyTyped(char parChar1, int parInt1) {
		super.keyTyped(parChar1, parInt1);
		if (parInt1 == KeyboardConstants.KEY_DELETE || parInt1 == KeyboardConstants.KEY_BACK) {
			if (mc.theWorld == null && EagRuntime.getConfiguration().isAllowBootMenu()) {
				mc.displayGuiScreen(new GuiScreenEnterBootMenu(this));
			}
		}
	}

	protected void mouseClicked(int mx, int my, int button) {
		super.mouseClicked(mx, my, button);
		if (mc.theWorld == null && !EagRuntime.getConfiguration().isDemo()) {
			int w = mc.fontRendererObj.getStringWidth(I18n.format("editProfile.importExport"));
			if (mx > 1 && my > 1 && mx < (w * 3 / 4) + 7 && my < 12) {
				mc.displayGuiScreen(new GuiScreenImportExportProfile(this));
				mc.getSoundHandler()
						.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			}
		}
		if (EagRuntime.getConfiguration().isEnableServerCookies() && mc.thePlayer == null) {
			int w = mc.fontRendererObj.getStringWidth(I18n.format("revokeSessionToken.button"));
			if (mx > width - 5 - (w + 5) * 3 / 4 && my > 1 && mx < width - 2 && my < 12) {
				ServerCookieDataStore.flush();
				mc.displayGuiScreen(ServerCookieDataStore.numRevokable() == 0
						? new GuiScreenGenericErrorMessage("errorNoSessions.title", "errorNoSessions.desc", this)
						: new GuiScreenRevokeSessionToken(this));
				mc.getSoundHandler()
						.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			}
		}
	}
}