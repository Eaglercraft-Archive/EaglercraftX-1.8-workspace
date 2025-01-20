package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
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
public class GuiListButton extends GuiButton {
	private boolean field_175216_o;
	private String localizationStr;
	private final GuiPageButtonList.GuiResponder guiResponder;

	public GuiListButton(GuiPageButtonList.GuiResponder responder, int parInt1, int parInt2, int parInt3,
			String parString1, boolean parFlag) {
		super(parInt1, parInt2, parInt3, 150, 20, "");
		this.localizationStr = parString1;
		this.field_175216_o = parFlag;
		this.displayString = this.buildDisplayString();
		this.guiResponder = responder;
	}

	/**+
	 * Builds the localized display string for this GuiListButton
	 */
	private String buildDisplayString() {
		return I18n.format(this.localizationStr, new Object[0]) + ": "
				+ (this.field_175216_o ? I18n.format("gui.yes", new Object[0]) : I18n.format("gui.no", new Object[0]));
	}

	public void func_175212_b(boolean parFlag) {
		this.field_175216_o = parFlag;
		this.displayString = this.buildDisplayString();
		this.guiResponder.func_175321_a(this.id, parFlag);
	}

	/**+
	 * Returns true if the mouse has been pressed on this control.
	 * Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	public boolean mousePressed(Minecraft minecraft, int i, int j) {
		if (super.mousePressed(minecraft, i, j)) {
			this.field_175216_o = !this.field_175216_o;
			this.displayString = this.buildDisplayString();
			this.guiResponder.func_175321_a(this.id, this.field_175216_o);
			return true;
		} else {
			return false;
		}
	}
}