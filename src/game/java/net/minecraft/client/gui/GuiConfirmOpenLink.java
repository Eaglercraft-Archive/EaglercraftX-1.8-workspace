package net.minecraft.client.gui;

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
public class GuiConfirmOpenLink extends GuiYesNo {
	private final String openLinkWarning;
	private final String copyLinkButtonText;
	private final String linkText;
	private boolean showSecurityWarning = true;

	public GuiConfirmOpenLink(GuiYesNoCallback parGuiYesNoCallback, String linkTextIn, int parInt1, boolean parFlag) {
		super(parGuiYesNoCallback,
				I18n.format(parFlag ? "chat.link.confirmTrusted" : "chat.link.confirm", new Object[0]), linkTextIn,
				parInt1);
		this.confirmButtonText = I18n.format(parFlag ? "chat.link.open" : "gui.yes", new Object[0]);
		this.cancelButtonText = I18n.format(parFlag ? "gui.cancel" : "gui.no", new Object[0]);
		this.copyLinkButtonText = I18n.format("chat.copy", new Object[0]);
		this.openLinkWarning = I18n.format("chat.link.warning", new Object[0]);
		this.linkText = linkTextIn;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(
				new GuiButton(0, this.width / 2 - 50 - 105, this.height / 6 + 96, 100, 20, this.confirmButtonText));
		this.buttonList
				.add(new GuiButton(2, this.width / 2 - 50, this.height / 6 + 96, 100, 20, this.copyLinkButtonText));
		this.buttonList
				.add(new GuiButton(1, this.width / 2 - 50 + 105, this.height / 6 + 96, 100, 20, this.cancelButtonText));
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.id == 2) {
			this.copyLinkToClipboard();
		}

		this.parentScreen.confirmClicked(parGuiButton.id == 0, this.parentButtonClickedId);
	}

	/**+
	 * Copies the link to the system clipboard.
	 */
	public void copyLinkToClipboard() {
		setClipboardString(this.linkText);
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		if (this.showSecurityWarning) {
			this.drawCenteredString(this.fontRendererObj, this.openLinkWarning, this.width / 2, 110, 16764108);
		}

	}

	public void disableSecurityWarning() {
		this.showSecurityWarning = false;
	}
}