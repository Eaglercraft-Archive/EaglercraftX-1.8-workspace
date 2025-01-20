package net.minecraft.client.gui;

import java.util.Random;

import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.StringUtils;

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
public class GuiCreateWorld extends GuiScreen {
	private GuiScreen parentScreen;
	private GuiTextField field_146333_g;
	private GuiTextField field_146335_h;
	private String field_146336_i;
	private String gameMode = "survival";
	private String field_175300_s;
	private boolean field_146341_s = true;
	private boolean allowCheats;
	private boolean field_146339_u;
	private boolean field_146338_v;
	private boolean field_146337_w;
	private boolean field_146345_x;
	private boolean field_146344_y;
	private GuiButton btnGameMode;
	private GuiButton btnMoreOptions;
	private GuiButton btnMapFeatures;
	private GuiButton btnBonusItems;
	private GuiButton btnMapType;
	private GuiButton btnAllowCommands;
	private GuiButton btnCustomizeType;
	private String field_146323_G;
	private String field_146328_H;
	private String field_146329_I;
	private String field_146330_J;
	private int selectedIndex;
	public String chunkProviderSettingsJson = "";
	/**+
	 * These filenames are known to be restricted on one or more
	 * OS's.
	 */
	private static final String[] disallowedFilenames = new String[] { "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL",
			"COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4",
			"LPT5", "LPT6", "LPT7", "LPT8", "LPT9" };

	public GuiCreateWorld(GuiScreen parGuiScreen) {
		this.parentScreen = parGuiScreen;
		this.field_146329_I = "";
		this.field_146330_J = I18n.format("selectWorld.newWorld", new Object[0]);
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		this.field_146333_g.updateCursorCounter();
		this.field_146335_h.updateCursorCounter();
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20,
				I18n.format("selectWorld.create", new Object[0])));
		this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20,
				I18n.format("gui.cancel", new Object[0])));
		this.buttonList.add(this.btnGameMode = new GuiButton(2, this.width / 2 - 75, 115, 150, 20,
				I18n.format("selectWorld.gameMode", new Object[0])));
		this.buttonList.add(this.btnMoreOptions = new GuiButton(3, this.width / 2 - 75, 187, 150, 20,
				I18n.format("selectWorld.moreWorldOptions", new Object[0])));
		this.buttonList.add(this.btnMapFeatures = new GuiButton(4, this.width / 2 - 155, 100, 150, 20,
				I18n.format("selectWorld.mapFeatures", new Object[0])));
		this.btnMapFeatures.visible = false;
		this.buttonList.add(this.btnBonusItems = new GuiButton(7, this.width / 2 + 5, 151, 150, 20,
				I18n.format("selectWorld.bonusItems", new Object[0])));
		this.btnBonusItems.visible = false;
		this.buttonList.add(this.btnMapType = new GuiButton(5, this.width / 2 + 5, 100, 150, 20,
				I18n.format("selectWorld.mapType", new Object[0])));
		this.btnMapType.visible = false;
		this.buttonList.add(this.btnAllowCommands = new GuiButton(6, this.width / 2 - 155, 151, 150, 20,
				I18n.format("selectWorld.allowCommands", new Object[0])));
		this.btnAllowCommands.visible = false;
		this.buttonList.add(this.btnCustomizeType = new GuiButton(8, this.width / 2 + 5, 120, 150, 20,
				I18n.format("selectWorld.customizeType", new Object[0])));
		this.btnCustomizeType.visible = false;
		this.field_146333_g = new GuiTextField(9, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
		this.field_146333_g.setFocused(true);
		this.field_146333_g.setText(this.field_146330_J);
		this.field_146335_h = new GuiTextField(10, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
		this.field_146335_h.setText(this.field_146329_I);
		this.func_146316_a(this.field_146344_y);
		this.func_146314_g();
		this.func_146319_h();
	}

	private void func_146314_g() {
		this.field_146336_i = this.field_146333_g.getText().trim();

		for (int i = 0; i < ChatAllowedCharacters.allowedCharactersArray.length; ++i) {
			this.field_146336_i = this.field_146336_i.replace(ChatAllowedCharacters.allowedCharactersArray[i], '_');
		}

		if (StringUtils.isEmpty(this.field_146336_i)) {
			this.field_146336_i = "World";
		}

		this.field_146336_i = func_146317_a(this.mc.getSaveLoader(), this.field_146336_i);
	}

	private void func_146319_h() {
		this.btnGameMode.displayString = I18n.format("selectWorld.gameMode", new Object[0]) + ": "
				+ I18n.format("selectWorld.gameMode." + this.gameMode, new Object[0]);
		this.field_146323_G = I18n.format("selectWorld.gameMode." + this.gameMode + ".line1", new Object[0]);
		this.field_146328_H = I18n.format("selectWorld.gameMode." + this.gameMode + ".line2", new Object[0]);
		this.btnMapFeatures.displayString = I18n.format("selectWorld.mapFeatures", new Object[0]) + " ";
		if (this.field_146341_s) {
			this.btnMapFeatures.displayString = this.btnMapFeatures.displayString
					+ I18n.format("options.on", new Object[0]);
		} else {
			this.btnMapFeatures.displayString = this.btnMapFeatures.displayString
					+ I18n.format("options.off", new Object[0]);
		}

		this.btnBonusItems.displayString = I18n.format("selectWorld.bonusItems", new Object[0]) + " ";
		if (this.field_146338_v && !this.field_146337_w) {
			this.btnBonusItems.displayString = this.btnBonusItems.displayString
					+ I18n.format("options.on", new Object[0]);
		} else {
			this.btnBonusItems.displayString = this.btnBonusItems.displayString
					+ I18n.format("options.off", new Object[0]);
		}

		this.btnMapType.displayString = I18n.format("selectWorld.mapType", new Object[0]) + " "
				+ I18n.format(WorldType.worldTypes[this.selectedIndex].getTranslateName(), new Object[0]);
		this.btnAllowCommands.displayString = I18n.format("selectWorld.allowCommands", new Object[0]) + " ";
		if (this.allowCheats && !this.field_146337_w) {
			this.btnAllowCommands.displayString = this.btnAllowCommands.displayString
					+ I18n.format("options.on", new Object[0]);
		} else {
			this.btnAllowCommands.displayString = this.btnAllowCommands.displayString
					+ I18n.format("options.off", new Object[0]);
		}

	}

	public static String func_146317_a(ISaveFormat parISaveFormat, String parString1) {
		parString1 = parString1.replaceAll("[\\./\"]", "_");

		for (int i = 0; i < disallowedFilenames.length; ++i) {
			if (parString1.equalsIgnoreCase(disallowedFilenames[i])) {
				parString1 = "_" + parString1 + "_";
			}
		}

		while (parISaveFormat.getWorldInfo(parString1) != null) {
			parString1 = parString1 + "-";
		}

		return parString1;
	}

	/**+
	 * Called when the screen is unloaded. Used to disable keyboard
	 * repeat events
	 */
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.enabled) {
			if (parGuiButton.id == 1) {
				this.mc.displayGuiScreen(this.parentScreen);
			} else if (parGuiButton.id == 0) {
				this.mc.displayGuiScreen((GuiScreen) null);
				if (this.field_146345_x) {
					return;
				}

				this.field_146345_x = true;
				long i = (new Random()).nextLong();
				String s = this.field_146335_h.getText();
				if (!StringUtils.isEmpty(s)) {
					try {
						long j = Long.parseLong(s);
						if (j != 0L) {
							i = j;
						}
					} catch (NumberFormatException var7) {
						i = (long) s.hashCode();
					}
				}

				WorldSettings.GameType worldsettings$gametype = WorldSettings.GameType.getByName(this.gameMode);
				WorldSettings worldsettings = new WorldSettings(i, worldsettings$gametype, this.field_146341_s,
						this.field_146337_w, WorldType.worldTypes[this.selectedIndex]);
				worldsettings.setWorldName(this.chunkProviderSettingsJson);
				if (this.field_146338_v && !this.field_146337_w) {
					worldsettings.enableBonusChest();
				}

				if (this.allowCheats && !this.field_146337_w) {
					worldsettings.enableCommands();
				}

				this.mc.launchIntegratedServer(this.field_146336_i, this.field_146333_g.getText().trim(),
						worldsettings);
			} else if (parGuiButton.id == 3) {
				this.func_146315_i();
			} else if (parGuiButton.id == 2) {
				if (this.gameMode.equals("survival")) {
					if (!this.field_146339_u) {
						this.allowCheats = false;
					}

					this.field_146337_w = false;
					this.gameMode = "hardcore";
					this.field_146337_w = true;
					this.btnAllowCommands.enabled = false;
					this.btnBonusItems.enabled = false;
					this.func_146319_h();
				} else if (this.gameMode.equals("hardcore")) {
					if (!this.field_146339_u) {
						this.allowCheats = true;
					}

					this.field_146337_w = false;
					this.gameMode = "creative";
					this.func_146319_h();
					this.field_146337_w = false;
					this.btnAllowCommands.enabled = true;
					this.btnBonusItems.enabled = true;
				} else {
					if (!this.field_146339_u) {
						this.allowCheats = false;
					}

					this.gameMode = "survival";
					this.func_146319_h();
					this.btnAllowCommands.enabled = true;
					this.btnBonusItems.enabled = true;
					this.field_146337_w = false;
				}

				this.func_146319_h();
			} else if (parGuiButton.id == 4) {
				this.field_146341_s = !this.field_146341_s;
				this.func_146319_h();
			} else if (parGuiButton.id == 7) {
				this.field_146338_v = !this.field_146338_v;
				this.func_146319_h();
			} else if (parGuiButton.id == 5) {
				++this.selectedIndex;
				if (this.selectedIndex >= WorldType.worldTypes.length) {
					this.selectedIndex = 0;
				}

				while (!this.func_175299_g()) {
					++this.selectedIndex;
					if (this.selectedIndex >= WorldType.worldTypes.length) {
						this.selectedIndex = 0;
					}
				}

				this.chunkProviderSettingsJson = "";
				this.func_146319_h();
				this.func_146316_a(this.field_146344_y);
			} else if (parGuiButton.id == 6) {
				this.field_146339_u = true;
				this.allowCheats = !this.allowCheats;
				this.func_146319_h();
			} else if (parGuiButton.id == 8) {
				if (WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT) {
					this.mc.displayGuiScreen(new GuiCreateFlatWorld(this, this.chunkProviderSettingsJson));
				} else {
					this.mc.displayGuiScreen(new GuiCustomizeWorldScreen(this, this.chunkProviderSettingsJson));
				}
			}

		}
	}

	private boolean func_175299_g() {
		WorldType worldtype = WorldType.worldTypes[this.selectedIndex];
		return worldtype != null && worldtype.getCanBeCreated()
				? (worldtype == WorldType.DEBUG_WORLD ? isShiftKeyDown() : true)
				: false;
	}

	private void func_146315_i() {
		this.func_146316_a(!this.field_146344_y);
	}

	private void func_146316_a(boolean parFlag) {
		this.field_146344_y = parFlag;
		if (WorldType.worldTypes[this.selectedIndex] == WorldType.DEBUG_WORLD) {
			this.btnGameMode.visible = !this.field_146344_y;
			this.btnGameMode.enabled = false;
			if (this.field_175300_s == null) {
				this.field_175300_s = this.gameMode;
			}

			this.gameMode = "spectator";
			this.btnMapFeatures.visible = false;
			this.btnBonusItems.visible = false;
			this.btnMapType.visible = this.field_146344_y;
			this.btnAllowCommands.visible = false;
			this.btnCustomizeType.visible = false;
		} else {
			this.btnGameMode.visible = !this.field_146344_y;
			this.btnGameMode.enabled = true;
			if (this.field_175300_s != null) {
				this.gameMode = this.field_175300_s;
				this.field_175300_s = null;
			}

			this.btnMapFeatures.visible = this.field_146344_y
					&& WorldType.worldTypes[this.selectedIndex] != WorldType.CUSTOMIZED;
			this.btnBonusItems.visible = this.field_146344_y;
			this.btnMapType.visible = this.field_146344_y;
			this.btnAllowCommands.visible = this.field_146344_y;
			this.btnCustomizeType.visible = this.field_146344_y
					&& (WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT
							|| WorldType.worldTypes[this.selectedIndex] == WorldType.CUSTOMIZED);
		}

		this.func_146319_h();
		if (this.field_146344_y) {
			this.btnMoreOptions.displayString = I18n.format("gui.done", new Object[0]);
		} else {
			this.btnMoreOptions.displayString = I18n.format("selectWorld.moreWorldOptions", new Object[0]);
		}

	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
		if (this.field_146333_g.isFocused() && !this.field_146344_y) {
			this.field_146333_g.textboxKeyTyped(parChar1, parInt1);
			this.field_146330_J = this.field_146333_g.getText();
		} else if (this.field_146335_h.isFocused() && this.field_146344_y) {
			this.field_146335_h.textboxKeyTyped(parChar1, parInt1);
			this.field_146329_I = this.field_146335_h.getText();
		}

		if (parInt1 == 28 || parInt1 == 156) {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}

		((GuiButton) this.buttonList.get(0)).enabled = this.field_146333_g.getText().length() > 0;
		this.func_146314_g();
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		super.mouseClicked(parInt1, parInt2, parInt3);
		if (this.field_146344_y) {
			this.field_146335_h.mouseClicked(parInt1, parInt2, parInt3);
		} else {
			this.field_146333_g.mouseClicked(parInt1, parInt2, parInt3);
		}

	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.create", new Object[0]), this.width / 2,
				20, -1);
		if (this.field_146344_y) {
			this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterSeed", new Object[0]),
					this.width / 2 - 100, 47, -6250336);
			this.drawString(this.fontRendererObj, I18n.format(
					StringUtils.isNotEmpty(field_146335_h.text) ? "createWorld.seedNote" : "selectWorld.seedInfo",
					new Object[0]), this.width / 2 - 100, 85, -6250336);
			if (this.btnMapFeatures.visible) {
				this.drawString(this.fontRendererObj, I18n.format("selectWorld.mapFeatures.info", new Object[0]),
						this.width / 2 - 150, 122, -6250336);
			}

			if (this.btnAllowCommands.visible) {
				this.drawString(this.fontRendererObj, I18n.format("selectWorld.allowCommands.info", new Object[0]),
						this.width / 2 - 150, 172, -6250336);
			}

			this.field_146335_h.drawTextBox();
			if (WorldType.worldTypes[this.selectedIndex].showWorldInfoNotice()) {
				this.fontRendererObj.drawSplitString(
						I18n.format(WorldType.worldTypes[this.selectedIndex].func_151359_c(), new Object[0]),
						this.btnMapType.xPosition + 2, this.btnMapType.yPosition + 22, this.btnMapType.getButtonWidth(),
						10526880);
			}
		} else {
			this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterName", new Object[0]),
					this.width / 2 - 100, 47, -6250336);
			this.drawString(this.fontRendererObj,
					I18n.format("selectWorld.resultFolder", new Object[0]) + " " + this.field_146336_i,
					this.width / 2 - 100, 85, -6250336);
			this.field_146333_g.drawTextBox();
			this.drawString(this.fontRendererObj, this.field_146323_G, this.width / 2 - 100, 137, -6250336);
			this.drawString(this.fontRendererObj, this.field_146328_H, this.width / 2 - 100, 149, -6250336);
		}

		super.drawScreen(i, j, f);
	}

	public void func_146318_a(WorldInfo parWorldInfo) {
		this.field_146330_J = I18n.format("selectWorld.newWorld.copyOf", new Object[] { parWorldInfo.getWorldName() });
		this.field_146329_I = parWorldInfo.getSeed() + "";
		this.selectedIndex = parWorldInfo.getTerrainType().getWorldTypeID();
		this.chunkProviderSettingsJson = parWorldInfo.getGeneratorOptions();
		this.field_146341_s = parWorldInfo.isMapFeaturesEnabled();
		this.allowCheats = parWorldInfo.areCommandsAllowed();
		if (parWorldInfo.isHardcoreModeEnabled()) {
			this.gameMode = "hardcore";
		} else if (parWorldInfo.getGameType().isSurvivalOrAdventure()) {
			this.gameMode = "survival";
		} else if (parWorldInfo.getGameType().isCreative()) {
			this.gameMode = "creative";
		}

	}

	@Override
	public boolean showCopyPasteButtons() {
		return field_146333_g.isFocused() || field_146335_h.isFocused();
	}

	@Override
	public void fireInputEvent(EnumInputEvent event, String param) {
		field_146333_g.fireInputEvent(event, param);
		field_146335_h.fireInputEvent(event, param);
	}

}