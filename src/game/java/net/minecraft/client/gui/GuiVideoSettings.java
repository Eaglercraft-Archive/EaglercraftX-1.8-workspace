package net.minecraft.client.gui;

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.Display;
import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenVideoSettingsWarning;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.DynamicLightsStateManager;
import net.lax1dude.eaglercraft.v1_8.recording.ScreenRecordingController;
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
public class GuiVideoSettings extends GuiScreen {
	private GuiScreen parentGuiScreen;
	protected String screenTitle = "Video Settings";
	private GameSettings guiGameSettings;
	private GuiListExtended optionsRowList;
	private boolean vsyncLock = false;
	/**
	 * + An array of all of GameSettings.Options's video options.
	 */
	/**+
	 * An array of all of GameSettings.Options's video options.
	 */
	private static final GameSettings.Options[] videoOptions = new GameSettings.Options[] {
			GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION,
			GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.EAGLER_VSYNC, GameSettings.Options.ANAGLYPH,
			GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.GAMMA,
			GameSettings.Options.RENDER_CLOUDS, GameSettings.Options.PARTICLES, GameSettings.Options.FXAA,
			GameSettings.Options.MIPMAP_LEVELS, GameSettings.Options.BLOCK_ALTERNATIVES,
			GameSettings.Options.ENTITY_SHADOWS, GameSettings.Options.FOG, GameSettings.Options.EAGLER_DYNAMIC_LIGHTS,
			GameSettings.Options.FULLSCREEN, GameSettings.Options.OF_CONNECTED_TEXTURES,
			GameSettings.Options.OF_BETTER_GRASS, GameSettings.Options.OF_CUSTOM_SKIES,
			GameSettings.Options.OF_SMART_LEAVES, GameSettings.Options.OF_CUSTOM_ITEMS, GameSettings.Options.FNAW_SKINS,
			GameSettings.Options.HUD_FPS, GameSettings.Options.HUD_COORDS, GameSettings.Options.HUD_PLAYER,
			GameSettings.Options.HUD_STATS, GameSettings.Options.HUD_WORLD, GameSettings.Options.HUD_24H,
			GameSettings.Options.CHUNK_FIX };

	public GuiVideoSettings(GuiScreen parentScreenIn, GameSettings gameSettingsIn) {
		this.parentGuiScreen = parentScreenIn;
		this.guiGameSettings = gameSettingsIn;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		this.screenTitle = I18n.format("options.videoTitle", new Object[0]);
		this.buttonList.clear();
		this.buttonList.add(
				new GuiButton(200, this.width / 2 - 100, this.height - 27, I18n.format("gui.done", new Object[0])));
		this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25,
				videoOptions);
		if (!DynamicLightsStateManager.isSupported()) {
			GuiOptionButton btn = ((GuiOptionsRowList) optionsRowList)
					.getButtonFor(GameSettings.Options.EAGLER_DYNAMIC_LIGHTS);
			if (btn != null) {
				btn.enabled = false;
			}
		}
		if (EaglercraftGPU.checkOpenGLESVersion() < 300) {
			GuiOptionSlider btn = ((GuiOptionsRowList) optionsRowList).getSliderFor(GameSettings.Options.MIPMAP_LEVELS);
			if (btn != null) {
				btn.displayString = I18n.format(GameSettings.Options.MIPMAP_LEVELS.getEnumString()) + ": N/A";
				btn.sliderValue = 0.0f;
				btn.enabled = false;
			}
		}
		if (!Display.supportsFullscreen()) {
			GuiOptionButton btn = ((GuiOptionsRowList) optionsRowList).getButtonFor(GameSettings.Options.FULLSCREEN);
			if (btn != null) {
				btn.displayString = I18n.format(GameSettings.Options.FULLSCREEN.getEnumString()) + ": "
						+ I18n.format("options.off");
				btn.enabled = false;
			}
		}
	}

	/**+
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.optionsRowList.handleMouseInput();
	}

	public void handleTouchInput() throws IOException {
		super.handleTouchInput();
		this.optionsRowList.handleTouchInput();
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.enabled) {
			if (parGuiButton.id == 200) {
				this.mc.gameSettings.saveOptions();
				GuiScreen contScreen = parentGuiScreen;
				int vidIssues = mc.gameSettings.checkBadVideoSettings();
				if (vidIssues != 0) {
					contScreen = new GuiScreenVideoSettingsWarning(contScreen, vidIssues);
				}
				this.mc.displayGuiScreen(contScreen);
			}
		}
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		int i = this.guiGameSettings.guiScale;
		super.mouseClicked(parInt1, parInt2, parInt3);
		this.optionsRowList.mouseClicked(parInt1, parInt2, parInt3);
		if (this.guiGameSettings.guiScale != i) {
			ScaledResolution scaledresolution = mc.scaledResolution = new ScaledResolution(mc);
			int j = scaledresolution.getScaledWidth();
			int k = scaledresolution.getScaledHeight();
			this.setWorldAndResolution(this.mc, j, k);
			this.mc.voiceOverlay.setResolution(j, k);
			this.mc.notifRenderer.setResolution(this.mc, j, k, scaledresolution.getScaleFactor());
		}

	}

	/**+
	 * Called when a mouse button is released. Args : mouseX,
	 * mouseY, releaseButton
	 */
	protected void mouseReleased(int i, int j, int k) {
		int l = this.guiGameSettings.guiScale;
		super.mouseReleased(i, j, k);
		this.optionsRowList.mouseReleased(i, j, k);
		if (this.guiGameSettings.guiScale != l) {
			ScaledResolution scaledresolution = mc.scaledResolution = new ScaledResolution(mc);
			int i1 = scaledresolution.getScaledWidth();
			int j1 = scaledresolution.getScaledHeight();
			this.setWorldAndResolution(this.mc, i1, j1);
		}

	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.optionsRowList.drawScreen(i, j, f);
		this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 5, 16777215);
		super.drawScreen(i, j, f);
	}

	@Override
	public void updateScreen() {
		boolean vsyncLockEn = ScreenRecordingController.isVSyncLocked();
		if (vsyncLockEn != vsyncLock) {
			vsyncLock = vsyncLockEn;
			GuiOptionButton btn = ((GuiOptionsRowList) optionsRowList).getButtonFor(GameSettings.Options.EAGLER_VSYNC);
			if (btn != null) {
				btn.enabled = !vsyncLockEn;
			}
		}
	}

}