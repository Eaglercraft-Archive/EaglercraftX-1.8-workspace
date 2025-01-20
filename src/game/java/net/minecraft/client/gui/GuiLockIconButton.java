package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;

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
public class GuiLockIconButton extends GuiButton {
	private boolean field_175231_o = false;

	public GuiLockIconButton(int parInt1, int parInt2, int parInt3) {
		super(parInt1, parInt2, parInt3, 20, 20, "");
	}

	public boolean func_175230_c() {
		return this.field_175231_o;
	}

	public void func_175229_b(boolean parFlag) {
		this.field_175231_o = parFlag;
	}

	/**+
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft minecraft, int i, int j) {
		if (this.visible) {
			minecraft.getTextureManager().bindTexture(GuiButton.buttonTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			boolean flag = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width
					&& j < this.yPosition + this.height;
			GuiLockIconButton.Icon guilockiconbutton$icon;
			if (this.field_175231_o) {
				if (!this.enabled) {
					guilockiconbutton$icon = GuiLockIconButton.Icon.LOCKED_DISABLED;
				} else if (flag) {
					guilockiconbutton$icon = GuiLockIconButton.Icon.LOCKED_HOVER;
				} else {
					guilockiconbutton$icon = GuiLockIconButton.Icon.LOCKED;
				}
			} else if (!this.enabled) {
				guilockiconbutton$icon = GuiLockIconButton.Icon.UNLOCKED_DISABLED;
			} else if (flag) {
				guilockiconbutton$icon = GuiLockIconButton.Icon.UNLOCKED_HOVER;
			} else {
				guilockiconbutton$icon = GuiLockIconButton.Icon.UNLOCKED;
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, guilockiconbutton$icon.func_178910_a(),
					guilockiconbutton$icon.func_178912_b(), this.width, this.height);
		}
	}

	static enum Icon {
		LOCKED(0, 146), LOCKED_HOVER(0, 166), LOCKED_DISABLED(0, 186), UNLOCKED(20, 146), UNLOCKED_HOVER(20, 166),
		UNLOCKED_DISABLED(20, 186);

		private final int field_178914_g;
		private final int field_178920_h;

		private Icon(int parInt2, int parInt3) {
			this.field_178914_g = parInt2;
			this.field_178920_h = parInt3;
		}

		public int func_178910_a() {
			return this.field_178914_g;
		}

		public int func_178912_b() {
			return this.field_178920_h;
		}
	}
}