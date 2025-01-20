package net.minecraft.client.gui;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenVisualViewport;
import net.lax1dude.eaglercraft.v1_8.notifications.GuiButtonNotifBell;
import net.lax1dude.eaglercraft.v1_8.notifications.GuiScreenNotifications;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

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
public class GuiChat extends GuiScreenVisualViewport {
	private static final Logger logger = LogManager.getLogger();
	private String historyBuffer = "";
	/**+
	 * keeps position of which chat message you will select when you
	 * press up, (does not increase for duplicated messages sent
	 * immediately after each other)
	 */
	private int sentHistoryCursor = -1;
	private boolean playerNamesFound;
	private boolean waitingOnAutocomplete;
	private int autocompleteIndex;
	private List<String> foundPlayerNames = Lists.newArrayList();
	protected GuiTextField inputField;
	/**+
	 * is the text that appears when you press the chat key and the
	 * input box appears pre-filled
	 */
	private String defaultInputFieldText = "";

	private GuiButton exitButton;
	private GuiButtonNotifBell notifBellButton;

	public GuiChat() {
	}

	public GuiChat(String defaultText) {
		this.defaultInputFieldText = defaultText;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		if (!(this instanceof GuiSleepMP)) {
			this.buttonList.add(exitButton = new GuiButton(69, this.width - 100, 3, 97, 20, I18n.format("chat.exit")));
			if (!this.mc.isIntegratedServerRunning() && this.mc.thePlayer != null
					&& this.mc.thePlayer.sendQueue.getEaglerMessageProtocol().ver >= 4) {
				this.buttonList.add(notifBellButton = new GuiButtonNotifBell(70, this.width - 122, 3));
				notifBellButton.setUnread(mc.thePlayer.sendQueue.getNotifManager().getUnread());
			}
		}
		this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
		this.inputField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
		this.inputField.setMaxStringLength(100);
		this.inputField.setEnableBackgroundDrawing(false);
		this.inputField.setFocused(true);
		this.inputField.setText(this.defaultInputFieldText);
		this.inputField.setCanLoseFocus(false);
	}

	/**+
	 * Called when the screen is unloaded. Used to disable keyboard
	 * repeat events
	 */
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		this.mc.ingameGUI.getChatGUI().resetScroll();
	}

	public void updateScreen0() {
		this.inputField.updateCursorCounter();
		if (notifBellButton != null && mc.thePlayer != null) {
			notifBellButton.setUnread(mc.thePlayer.sendQueue.getNotifManager().getUnread());
		}
	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
		if (parInt1 == 1 && (this.mc.gameSettings.keyBindClose.getKeyCode() == 0 || Keyboard.areKeysLocked())) {
			this.mc.displayGuiScreen((GuiScreen) null);
		} else {
			this.waitingOnAutocomplete = false;
			if (parInt1 == 15) {
				this.autocompletePlayerNames();
			} else {
				this.playerNamesFound = false;
			}

			if (parInt1 != 28 && parInt1 != 156) {
				if (parInt1 == 200) {
					this.getSentHistory(-1);
				} else if (parInt1 == 208) {
					this.getSentHistory(1);
				} else if (parInt1 == 201) {
					this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
				} else if (parInt1 == 209) {
					this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
				} else {
					this.inputField.textboxKeyTyped(parChar1, parInt1);
				}
			} else {
				String s = this.inputField.getText().trim();
				if (s.length() > 0) {
					this.sendChatMessage(s);
				}

				this.mc.displayGuiScreen((GuiScreen) null);
			}
		}

	}

	/**+
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		if (i != 0) {
			if (i > 1) {
				i = 1;
			}

			if (i < -1) {
				i = -1;
			}

			if (!isShiftKeyDown()) {
				i *= 7;
			}

			this.mc.ingameGUI.getChatGUI().scroll(i);
		}

	}

	protected void mouseClicked0(int parInt1, int parInt2, int parInt3) {
		if (parInt3 == 0) {
			IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI()
					.getChatComponent(PointerInputAbstraction.getVCursorX(), PointerInputAbstraction.getVCursorY());
			if (this.handleComponentClick(ichatcomponent)) {
				return;
			}
			if (mc.notifRenderer.handleClicked(this, parInt1, parInt2)) {
				return;
			}
		}

		this.inputField.mouseClicked(parInt1, parInt2, parInt3);
		super.mouseClicked0(parInt1, parInt2, parInt3);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 69) {
			this.mc.displayGuiScreen(null);
		} else if (par1GuiButton.id == 70) {
			this.mc.displayGuiScreen(new GuiScreenNotifications(this));
		}
	}

	/**+
	 * Sets the text of the chat
	 */
	protected void setText(String newChatText, boolean shouldOverwrite) {
		if (shouldOverwrite) {
			this.inputField.setText(newChatText);
		} else {
			this.inputField.writeText(newChatText);
		}

	}

	public void autocompletePlayerNames() {
		if (this.playerNamesFound) {
			this.inputField
					.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false)
							- this.inputField.getCursorPosition());
			if (this.autocompleteIndex >= this.foundPlayerNames.size()) {
				this.autocompleteIndex = 0;
			}
		} else {
			int i = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
			this.foundPlayerNames.clear();
			this.autocompleteIndex = 0;
			String s = this.inputField.getText().substring(i).toLowerCase();
			String s1 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
			this.sendAutocompleteRequest(s1, s);
			if (this.foundPlayerNames.isEmpty()) {
				return;
			}

			this.playerNamesFound = true;
			this.inputField.deleteFromCursor(i - this.inputField.getCursorPosition());
		}

		int l = this.foundPlayerNames.size();
		if (l > 1) {
			StringBuilder stringbuilder = new StringBuilder();

			for (int i = 0; i < l; ++i) {
				if (stringbuilder.length() > 0) {
					stringbuilder.append(", ");
				}

				stringbuilder.append(this.foundPlayerNames.get(i));
			}

			this.mc.ingameGUI.getChatGUI()
					.printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
		}

		this.inputField.writeText((String) this.foundPlayerNames.get(this.autocompleteIndex++));
	}

	private void sendAutocompleteRequest(String parString1, String parString2) {
		if (parString1.length() >= 1) {
			BlockPos blockpos = null;
			if (this.mc.objectMouseOver != null
					&& this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				blockpos = this.mc.objectMouseOver.getBlockPos();
			}

			this.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(parString1, blockpos));
			this.waitingOnAutocomplete = true;
		}
	}

	/**+
	 * input is relative and is applied directly to the
	 * sentHistoryCursor so -1 is the previous message, 1 is the
	 * next message from the current cursor position
	 */
	public void getSentHistory(int msgPos) {
		int i = this.sentHistoryCursor + msgPos;
		int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
		i = MathHelper.clamp_int(i, 0, j);
		if (i != this.sentHistoryCursor) {
			if (i == j) {
				this.sentHistoryCursor = j;
				this.inputField.setText(this.historyBuffer);
			} else {
				if (this.sentHistoryCursor == j) {
					this.historyBuffer = this.inputField.getText();
				}

				this.inputField.setText((String) this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
				this.sentHistoryCursor = i;
			}
		}
	}

	public void drawScreen0(int i, int j, float f) {
		drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		this.inputField.drawTextBox();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI()
				.getChatComponent(PointerInputAbstraction.getVCursorX(), PointerInputAbstraction.getVCursorY());
		if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null) {
			this.handleComponentHover(ichatcomponent, i, j);
		}

		if (exitButton != null) {
			exitButton.yPosition = 3 + mc.guiAchievement.getHeight();
		}

		super.drawScreen0(i, j, f);
	}

	public void onAutocompleteResponse(String[] parArrayOfString) {
		if (this.waitingOnAutocomplete) {
			this.playerNamesFound = false;
			this.foundPlayerNames.clear();

			for (int i = 0; i < parArrayOfString.length; ++i) {
				String s = parArrayOfString[i];
				if (s.length() > 0) {
					this.foundPlayerNames.add(s);
				}
			}

			String s1 = this.inputField.getText()
					.substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
			String s2 = StringUtils.getCommonPrefix(parArrayOfString);
			if (s2.length() > 0 && !s1.equalsIgnoreCase(s2)) {
				this.inputField
						.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false)
								- this.inputField.getCursorPosition());
				this.inputField.writeText(s2);
			} else if (this.foundPlayerNames.size() > 0) {
				this.playerNamesFound = true;
				this.autocompletePlayerNames();
			}
		}

	}

	/**+
	 * Returns true if this GUI should pause the game when it is
	 * displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	public boolean blockPTTKey() {
		return true;
	}

	public boolean showCopyPasteButtons() {
		return true;
	}

	public void fireInputEvent(EnumInputEvent event, String str) {
		inputField.fireInputEvent(event, str);
	}

}