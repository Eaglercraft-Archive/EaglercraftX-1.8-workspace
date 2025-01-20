package net.minecraft.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

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
public class GuiYesNo extends GuiScreen {
	protected GuiYesNoCallback parentScreen;
	protected String messageLine1;
	private String messageLine2;
	private final List<String> field_175298_s = Lists.newArrayList();
	protected String confirmButtonText;
	protected String cancelButtonText;
	protected int parentButtonClickedId;
	private int ticksUntilEnable;
	private boolean opaqueBackground = false;

	public GuiYesNo(GuiYesNoCallback parGuiYesNoCallback, String parString1, String parString2, int parInt1) {
		this.parentScreen = parGuiYesNoCallback;
		this.messageLine1 = parString1;
		this.messageLine2 = parString2;
		this.parentButtonClickedId = parInt1;
		this.confirmButtonText = I18n.format("gui.yes", new Object[0]);
		this.cancelButtonText = I18n.format("gui.no", new Object[0]);
	}

	public GuiYesNo(GuiYesNoCallback parGuiYesNoCallback, String parString1, String parString2, String parString3,
			String parString4, int parInt1) {
		this.parentScreen = parGuiYesNoCallback;
		this.messageLine1 = parString1;
		this.messageLine2 = parString2;
		this.confirmButtonText = parString3;
		this.cancelButtonText = parString4;
		this.parentButtonClickedId = parInt1;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 6 + 96, this.confirmButtonText));
		this.buttonList
				.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.cancelButtonText));
		this.field_175298_s.clear();
		this.field_175298_s.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, this.width - 50));
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		this.parentScreen.confirmClicked(parGuiButton.id == 0, this.parentButtonClickedId);
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		if (opaqueBackground) {
			this.drawBackground(0);
		} else {
			this.drawDefaultBackground();
		}
		this.drawCenteredString(this.fontRendererObj, this.messageLine1, this.width / 2, 70, 16777215);
		int k = 90;

		for (int l = 0, m = this.field_175298_s.size(); l < m; ++l) {
			this.drawCenteredString(this.fontRendererObj, this.field_175298_s.get(l), this.width / 2, k, 16777215);
			k += this.fontRendererObj.FONT_HEIGHT;
		}

		super.drawScreen(i, j, f);
	}

	/**+
	 * Sets the number of ticks to wait before enabling the buttons.
	 */
	public void setButtonDelay(int parInt1) {
		this.ticksUntilEnable = parInt1;

		for (int l = 0, m = this.buttonList.size(); l < m; ++l) {
			this.buttonList.get(l).enabled = false;
		}

	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();
		if (--this.ticksUntilEnable == 0) {
			for (int l = 0, m = this.buttonList.size(); l < m; ++l) {
				this.buttonList.get(l).enabled = true;
			}
		}

	}

	public GuiYesNo withOpaqueBackground() {
		opaqueBackground = true;
		return this;
	}
}