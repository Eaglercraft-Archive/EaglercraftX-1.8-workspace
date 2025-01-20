package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenIntegratedServerBusy;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

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
public class GuiRenameWorld extends GuiScreen {
	private GuiScreen parentScreen;
	private GuiTextField field_146583_f;
	private final String saveName;
	private final boolean duplicate;

	public GuiRenameWorld(GuiScreen parentScreenIn, String saveNameIn) {
		this.parentScreen = parentScreenIn;
		this.saveName = saveNameIn;
		this.duplicate = false;
	}

	public GuiRenameWorld(GuiScreen parentScreenIn, String saveNameIn, boolean duplicate) {
		this.parentScreen = parentScreenIn;
		this.saveName = saveNameIn;
		this.duplicate = duplicate;
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		this.field_146583_f.updateCursorCounter();
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12,
				I18n.format(duplicate ? "selectWorld.duplicateButton" : "selectWorld.renameButton", new Object[0])));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12,
				I18n.format("gui.cancel", new Object[0])));
		ISaveFormat isaveformat = this.mc.getSaveLoader();
		WorldInfo worldinfo = isaveformat.getWorldInfo(this.saveName);
		String s = worldinfo.getWorldName();
		if (duplicate) {
			s += " copy";
		}
		this.field_146583_f = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
		this.field_146583_f.setFocused(true);
		this.field_146583_f.setText(s);
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
				if (duplicate) {
					SingleplayerServerController.duplicateWorld(this.saveName, this.field_146583_f.getText().trim());
					this.mc.displayGuiScreen(
							new GuiScreenIntegratedServerBusy(this.parentScreen, "singleplayer.busy.duplicating",
									"singleplayer.failed.duplicating", SingleplayerServerController::isReady));
				} else {
					ISaveFormat isaveformat = this.mc.getSaveLoader();
					isaveformat.renameWorld(this.saveName, this.field_146583_f.getText().trim());
					this.mc.displayGuiScreen(
							new GuiScreenIntegratedServerBusy(this.parentScreen, "singleplayer.busy.renaming",
									"singleplayer.failed.renaming", SingleplayerServerController::isReady));
				}
			}
		}
	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
		this.field_146583_f.textboxKeyTyped(parChar1, parInt1);
		((GuiButton) this.buttonList.get(0)).enabled = this.field_146583_f.getText().trim().length() > 0;
		if (parInt1 == 28 || parInt1 == 156) {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}

	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		super.mouseClicked(parInt1, parInt2, parInt3);
		this.field_146583_f.mouseClicked(parInt1, parInt2, parInt3);
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj,
				I18n.format(duplicate ? "selectWorld.duplicate" : "selectWorld.renameTitle", new Object[0]),
				this.width / 2, 20, 16777215);
		this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterName", new Object[0]), this.width / 2 - 100,
				47, 10526880);
		this.field_146583_f.drawTextBox();
		super.drawScreen(i, j, f);
	}

	@Override
	public boolean showCopyPasteButtons() {
		return field_146583_f.isFocused();
	}

	@Override
	public void fireInputEvent(EnumInputEvent event, String param) {
		field_146583_f.fireInputEvent(event, param);
	}

}