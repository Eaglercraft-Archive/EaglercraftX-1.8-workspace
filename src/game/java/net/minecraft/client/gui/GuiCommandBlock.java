package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.IChatComponent;

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
public class GuiCommandBlock extends GuiScreen {
	private static final Logger field_146488_a = LogManager.getLogger();
	private GuiTextField commandTextField;
	private GuiTextField previousOutputTextField;
	private final CommandBlockLogic localCommandBlock;
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	private GuiButton field_175390_s;
	private boolean field_175389_t;

	public GuiCommandBlock(CommandBlockLogic parCommandBlockLogic) {
		this.localCommandBlock = parCommandBlockLogic;
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		this.commandTextField.updateCursorCounter();
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150,
				20, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20,
				I18n.format("gui.cancel", new Object[0])));
		this.buttonList.add(this.field_175390_s = new GuiButton(4, this.width / 2 + 150 - 20, 150, 20, 20, "O"));
		this.commandTextField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
		this.commandTextField.setMaxStringLength(32767);
		this.commandTextField.setFocused(true);
		this.commandTextField.setText(this.localCommandBlock.getCommand());
		this.previousOutputTextField = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 150, 150, 276, 20);
		this.previousOutputTextField.setMaxStringLength(32767);
		this.previousOutputTextField.setEnabled(false);
		this.previousOutputTextField.setText("-");
		this.field_175389_t = this.localCommandBlock.shouldTrackOutput();
		this.func_175388_a();
		this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
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
				this.localCommandBlock.setTrackOutput(this.field_175389_t);
				this.mc.displayGuiScreen((GuiScreen) null);
			} else if (parGuiButton.id == 0) {
				PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
				packetbuffer.writeByte(this.localCommandBlock.func_145751_f());
				this.localCommandBlock.func_145757_a(packetbuffer);
				packetbuffer.writeString(this.commandTextField.getText());
				packetbuffer.writeBoolean(this.localCommandBlock.shouldTrackOutput());
				this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|AdvCdm", packetbuffer));
				if (!this.localCommandBlock.shouldTrackOutput()) {
					this.localCommandBlock.setLastOutput((IChatComponent) null);
				}

				this.mc.displayGuiScreen((GuiScreen) null);
			} else if (parGuiButton.id == 4) {
				this.localCommandBlock.setTrackOutput(!this.localCommandBlock.shouldTrackOutput());
				this.func_175388_a();
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
		this.commandTextField.textboxKeyTyped(parChar1, parInt1);
		this.previousOutputTextField.textboxKeyTyped(parChar1, parInt1);
		this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
		if (parInt1 != 28 && parInt1 != 156) {
			if (parInt1 == 1) {
				this.actionPerformed(this.cancelBtn);
			}
		} else {
			this.actionPerformed(this.doneBtn);
		}

	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		super.mouseClicked(parInt1, parInt2, parInt3);
		this.commandTextField.mouseClicked(parInt1, parInt2, parInt3);
		this.previousOutputTextField.mouseClicked(parInt1, parInt2, parInt3);
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), this.width / 2,
				20, 16777215);
		this.drawString(this.fontRendererObj, I18n.format("advMode.command", new Object[0]), this.width / 2 - 150, 37,
				10526880);
		this.commandTextField.drawTextBox();
		int k = 75;
		int l = 0;
		this.drawString(this.fontRendererObj, I18n.format("advMode.nearestPlayer", new Object[0]), this.width / 2 - 150,
				k + l++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
		this.drawString(this.fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), this.width / 2 - 150,
				k + l++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
		this.drawString(this.fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), this.width / 2 - 150,
				k + l++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
		this.drawString(this.fontRendererObj, I18n.format("advMode.allEntities", new Object[0]), this.width / 2 - 150,
				k + l++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
		this.drawString(this.fontRendererObj, "", this.width / 2 - 150, k + l++ * this.fontRendererObj.FONT_HEIGHT,
				10526880);
		if (this.previousOutputTextField.getText().length() > 0) {
			k = k + l * this.fontRendererObj.FONT_HEIGHT + 16;
			this.drawString(this.fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]),
					this.width / 2 - 150, k, 10526880);
			this.previousOutputTextField.drawTextBox();
		}

		super.drawScreen(i, j, f);
	}

	private void func_175388_a() {
		if (this.localCommandBlock.shouldTrackOutput()) {
			this.field_175390_s.displayString = "O";
			if (this.localCommandBlock.getLastOutput() != null) {
				this.previousOutputTextField.setText(this.localCommandBlock.getLastOutput().getUnformattedText());
			}
		} else {
			this.field_175390_s.displayString = "X";
			this.previousOutputTextField.setText("-");
		}

	}

	public boolean blockPTTKey() {
		return commandTextField.isFocused() || previousOutputTextField.isFocused();
	}

	@Override
	public boolean showCopyPasteButtons() {
		return commandTextField.isFocused() || previousOutputTextField.isFocused();
	}

	@Override
	public void fireInputEvent(EnumInputEvent event, String param) {
		commandTextField.fireInputEvent(event, param);
		previousOutputTextField.fireInputEvent(event, param);
	}

}