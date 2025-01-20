package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;

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
public class GuiScreenAddServer extends GuiScreen {
	private final GuiScreen parentScreen;
	private final ServerData serverData;
	private GuiTextField serverIPField;
	private GuiTextField serverNameField;
	private GuiButton serverResourcePacks;
	private GuiButton hideAddress;
	private GuiButton enableCookies;

	public GuiScreenAddServer(GuiScreen parGuiScreen, ServerData parServerData) {
		this.parentScreen = parGuiScreen;
		this.serverData = parServerData;
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		this.serverNameField.updateCursorCounter();
		this.serverIPField.updateCursorCounter();
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		int i = 80;
		this.buttonList.clear();
		GuiButton done;
		GuiButton cancel;
		this.buttonList.add(done = new GuiButton(0, this.width / 2 - 100, i + 96 + 12,
				I18n.format("addServer.add", new Object[0])));
		this.buttonList.add(cancel = new GuiButton(1, this.width / 2 - 100, i + 120 + 12,
				I18n.format("gui.cancel", new Object[0])));
		if (EagRuntime.requireSSL()) {
			done.yPosition = cancel.yPosition;
			done.width = (done.width / 2) - 2;
			cancel.width = (cancel.width / 2) - 2;
			done.xPosition += cancel.width + 4;
		}
		this.buttonList.add(this.serverResourcePacks = new GuiButton(2, this.width / 2 - 100, i + 54,
				I18n.format("addServer.resourcePack", new Object[0]) + ": "
						+ this.serverData.getResourceMode().getMotd().getFormattedText()));
		if (EagRuntime.getConfiguration().isEnableServerCookies()) {
			this.buttonList.add(this.enableCookies = new GuiButton(4, this.width / 2 - 100, i + 78, 99, 20,
					I18n.format("addServer.enableCookies") + ": "
							+ I18n.format(this.serverData.enableCookies ? "addServer.enableCookies.enabled"
									: "addServer.enableCookies.disabled")));
			this.buttonList.add(this.hideAddress = new GuiButton(3, this.width / 2 + 1, i + 78, 99, 20,
					I18n.format("addServer.hideAddr", new Object[0]) + ": "
							+ I18n.format(this.serverData.hideAddress ? "gui.yes" : "gui.no", new Object[0])));
		} else {
			this.buttonList.add(this.hideAddress = new GuiButton(3, this.width / 2 - 100, i + 78,
					I18n.format("addServer.hideAddress", new Object[0]) + ": "
							+ I18n.format(this.serverData.hideAddress ? "gui.yes" : "gui.no", new Object[0])));
		}
		this.serverNameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 66, 200, 20);
		this.serverNameField.setFocused(true);
		this.serverNameField.setText(this.serverData.serverName);
		this.serverIPField = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, 106, 200, 20);
		this.serverIPField.setMaxStringLength(128);
		this.serverIPField.setText(this.serverData.serverIP);
		((GuiButton) this.buttonList.get(0)).enabled = this.serverIPField.getText().trim().length() > 0;
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
			if (parGuiButton.id == 3) {
				this.serverData.hideAddress = !this.serverData.hideAddress;
				this.hideAddress.displayString = I18n
						.format(EagRuntime.getConfiguration().isEnableServerCookies() ? "addServer.hideAddr"
								: "addServer.hideAddress", new Object[0])
						+ ": " + I18n.format(this.serverData.hideAddress ? "gui.yes" : "gui.no", new Object[0]);
			} else if (parGuiButton.id == 4) {
				this.serverData.enableCookies = !this.serverData.enableCookies;
				this.enableCookies.displayString = I18n.format("addServer.enableCookies") + ": "
						+ I18n.format(this.serverData.enableCookies ? "addServer.enableCookies.enabled"
								: "addServer.enableCookies.disabled");
			} else if (parGuiButton.id == 2) {
				this.serverData.setResourceMode(
						ServerData.ServerResourceMode._VALUES[(this.serverData.getResourceMode().ordinal() + 1)
								% ServerData.ServerResourceMode._VALUES.length]);
				this.serverResourcePacks.displayString = I18n.format("addServer.resourcePack", new Object[0]) + ": "
						+ this.serverData.getResourceMode().getMotd().getFormattedText();
			} else if (parGuiButton.id == 1) {
				this.parentScreen.confirmClicked(false, 0);
			} else if (parGuiButton.id == 0) {
				this.serverData.serverName = this.serverNameField.getText().trim();
				this.serverData.serverIP = this.serverIPField.getText().trim();
				this.parentScreen.confirmClicked(true, 0);
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
		this.serverNameField.textboxKeyTyped(parChar1, parInt1);
		this.serverIPField.textboxKeyTyped(parChar1, parInt1);
		if (parInt1 == 15) {
			this.serverNameField.setFocused(!this.serverNameField.isFocused());
			this.serverIPField.setFocused(!this.serverIPField.isFocused());
		}

		if (parInt1 == 28 || parInt1 == 156) {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}

		((GuiButton) this.buttonList.get(0)).enabled = this.serverIPField.getText().trim().length() > 0;
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		super.mouseClicked(parInt1, parInt2, parInt3);
		this.serverIPField.mouseClicked(parInt1, parInt2, parInt3);
		this.serverNameField.mouseClicked(parInt1, parInt2, parInt3);
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.title", new Object[0]), this.width / 2, 17,
				16777215);
		this.drawString(this.fontRendererObj, I18n.format("addServer.enterName", new Object[0]), this.width / 2 - 100,
				53, 10526880);
		this.drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), this.width / 2 - 100, 94,
				10526880);
		if (EagRuntime.requireSSL()) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn1"), this.width / 2, 184,
					0xccccff);
			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn2"), this.width / 2, 196,
					0xccccff);
		}
		this.serverNameField.drawTextBox();
		this.serverIPField.drawTextBox();
		super.drawScreen(i, j, f);
	}

	@Override
	public boolean showCopyPasteButtons() {
		return serverNameField.isFocused() || serverIPField.isFocused();
	}

	@Override
	public void fireInputEvent(EnumInputEvent event, String param) {
		serverNameField.fireInputEvent(event, param);
		serverIPField.fireInputEvent(event, param);
	}

}