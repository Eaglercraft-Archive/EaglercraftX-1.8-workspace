package net.minecraft.client.gui;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

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
public class GuiTextField extends Gui {
	private final int id;
	private final FontRenderer fontRendererInstance;
	public int xPosition;
	public int yPosition;
	private final int width;
	private final int height;
	/**+
	 * Has the current text being edited on the textbox.
	 */
	protected String text = "";
	private int maxStringLength = 32;
	private int cursorCounter;
	private boolean enableBackgroundDrawing = true;
	/**+
	 * if true the textbox can lose focus by clicking elsewhere on
	 * the screen
	 */
	private boolean canLoseFocus = true;
	private boolean isFocused;
	/**+
	 * If this value is true along with isFocused, keyTyped will
	 * process the keys.
	 */
	private boolean isEnabled = true;
	private int lineScrollOffset;
	private int cursorPosition;
	private int selectionEnd;
	private int enabledColor = 14737632;
	private int disabledColor = 7368816;
	/**+
	 * True if this textbox is visible
	 */
	private boolean visible = true;
	private GuiPageButtonList.GuiResponder field_175210_x;
	private Predicate<String> field_175209_y = Predicates.alwaysTrue();

	public GuiTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
		this.id = componentId;
		this.fontRendererInstance = fontrendererObj;
		this.xPosition = x;
		this.yPosition = y;
		this.width = par5Width;
		this.height = par6Height;
	}

	public void func_175207_a(GuiPageButtonList.GuiResponder parGuiResponder) {
		this.field_175210_x = parGuiResponder;
	}

	/**+
	 * Increments the cursor counter
	 */
	public void updateCursorCounter() {
		++this.cursorCounter;
	}

	/**+
	 * Sets the text of the textbox
	 */
	public void setText(String parString1) {
		if (this.field_175209_y.apply(parString1)) {
			if (parString1.length() > this.maxStringLength) {
				this.text = parString1.substring(0, this.maxStringLength);
			} else {
				this.text = parString1;
			}

			this.setCursorPositionEnd();
		}
	}

	public void updateText(String parString1) {
		if (this.field_175209_y.apply(parString1)) {
			if (parString1.length() > this.maxStringLength) {
				this.text = parString1.substring(0, this.maxStringLength);
			} else {
				this.text = parString1;
			}

			this.setCursorPosition(cursorPosition);
		}
	}

	/**+
	 * Returns the contents of the textbox
	 */
	public String getText() {
		return this.text;
	}

	/**+
	 * returns the text between the cursor and selectionEnd
	 */
	public String getSelectedText() {
		int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
		int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
		return this.text.substring(i, j);
	}

	public void func_175205_a(Predicate<String> parPredicate) {
		this.field_175209_y = parPredicate;
	}

	/**+
	 * replaces selected text, or inserts text at the position on
	 * the cursor
	 */
	public void writeText(String parString1) {
		String s = "";
		String s1 = ChatAllowedCharacters.filterAllowedCharacters(parString1);
		int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
		int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
		int k = this.maxStringLength - this.text.length() - (i - j);
		int l = 0;
		if (this.text.length() > 0) {
			s = s + this.text.substring(0, i);
		}

		if (k < s1.length()) {
			s = s + s1.substring(0, k);
			l = k;
		} else {
			s = s + s1;
			l = s1.length();
		}

		if (this.text.length() > 0 && j < this.text.length()) {
			s = s + this.text.substring(j);
		}

		if (this.field_175209_y.apply(s)) {
			this.text = s;
			this.moveCursorBy(i - this.selectionEnd + l);
			if (this.field_175210_x != null) {
				this.field_175210_x.func_175319_a(this.id, this.text);
			}

		}
	}

	/**+
	 * Deletes the specified number of words starting at the cursor
	 * position. Negative numbers will delete words left of the
	 * cursor.
	 */
	public void deleteWords(int parInt1) {
		if (this.text.length() != 0) {
			if (this.selectionEnd != this.cursorPosition) {
				this.writeText("");
			} else {
				this.deleteFromCursor(this.getNthWordFromCursor(parInt1) - this.cursorPosition);
			}
		}
	}

	/**+
	 * delete the selected text, otherwsie deletes characters from
	 * either side of the cursor. params: delete num
	 */
	public void deleteFromCursor(int parInt1) {
		if (this.text.length() != 0) {
			if (this.selectionEnd != this.cursorPosition) {
				this.writeText("");
			} else {
				boolean flag = parInt1 < 0;
				int i = flag ? this.cursorPosition + parInt1 : this.cursorPosition;
				int j = flag ? this.cursorPosition : this.cursorPosition + parInt1;
				String s = "";
				if (i >= 0) {
					s = this.text.substring(0, i);
				}

				if (j < this.text.length()) {
					s = s + this.text.substring(j);
				}

				if (this.field_175209_y.apply(s)) {
					this.text = s;
					if (flag) {
						this.moveCursorBy(parInt1);
					}

					if (this.field_175210_x != null) {
						this.field_175210_x.func_175319_a(this.id, this.text);
					}

				}
			}
		}
	}

	public int getId() {
		return this.id;
	}

	/**+
	 * see @getNthNextWordFromPos() params: N, position
	 */
	public int getNthWordFromCursor(int parInt1) {
		return this.getNthWordFromPos(parInt1, this.getCursorPosition());
	}

	/**+
	 * gets the position of the nth word. N may be negative, then it
	 * looks backwards. params: N, position
	 */
	public int getNthWordFromPos(int parInt1, int parInt2) {
		return this.func_146197_a(parInt1, parInt2, true);
	}

	public int func_146197_a(int parInt1, int parInt2, boolean parFlag) {
		int i = parInt2;
		boolean flag = parInt1 < 0;
		int j = Math.abs(parInt1);

		for (int k = 0; k < j; ++k) {
			if (!flag) {
				int l = this.text.length();
				i = this.text.indexOf(32, i);
				if (i == -1) {
					i = l;
				} else {
					while (parFlag && i < l && this.text.charAt(i) == 32) {
						++i;
					}
				}
			} else {
				while (parFlag && i > 0 && this.text.charAt(i - 1) == 32) {
					--i;
				}

				while (i > 0 && this.text.charAt(i - 1) != 32) {
					--i;
				}
			}
		}

		return i;
	}

	/**+
	 * Moves the text cursor by a specified number of characters and
	 * clears the selection
	 */
	public void moveCursorBy(int parInt1) {
		this.setCursorPosition(this.selectionEnd + parInt1);
	}

	/**+
	 * sets the position of the cursor to the provided index
	 */
	public void setCursorPosition(int parInt1) {
		this.cursorPosition = parInt1;
		int i = this.text.length();
		this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, i);
		this.setSelectionPos(this.cursorPosition);
	}

	/**+
	 * sets the cursors position to the beginning
	 */
	public void setCursorPositionZero() {
		this.setCursorPosition(0);
	}

	/**+
	 * sets the cursors position to after the text
	 */
	public void setCursorPositionEnd() {
		this.setCursorPosition(this.text.length());
	}

	/**+
	 * Call this method from your GuiScreen to process the keys into
	 * the textbox
	 */
	public boolean textboxKeyTyped(char parChar1, int parInt1) {
		if (!this.isFocused) {
			return false;
		} else if (GuiScreen.isKeyComboCtrlA(parInt1)) {
			this.setCursorPositionEnd();
			this.setSelectionPos(0);
			return true;
		} else if (GuiScreen.isKeyComboCtrlC(parInt1)) {
			GuiScreen.setClipboardString(this.getSelectedText());
			return true;
		} else if (GuiScreen.isKeyComboCtrlV(parInt1)) {
			if (this.isEnabled) {
				this.writeText(GuiScreen.getClipboardString());
			}

			return true;
		} else if (GuiScreen.isKeyComboCtrlX(parInt1)) {
			GuiScreen.setClipboardString(this.getSelectedText());
			if (this.isEnabled) {
				this.writeText("");
			}

			return true;
		} else {
			switch (parInt1) {
			case 14:
				if (GuiScreen.isCtrlKeyDown()) {
					if (this.isEnabled) {
						this.deleteWords(-1);
					}
				} else if (this.isEnabled) {
					this.deleteFromCursor(-1);
				}

				return true;
			case 199:
				if (GuiScreen.isShiftKeyDown()) {
					this.setSelectionPos(0);
				} else {
					this.setCursorPositionZero();
				}

				return true;
			case 203:
				if (GuiScreen.isShiftKeyDown()) {
					if (GuiScreen.isCtrlKeyDown()) {
						this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
					} else {
						this.setSelectionPos(this.getSelectionEnd() - 1);
					}
				} else if (GuiScreen.isCtrlKeyDown()) {
					this.setCursorPosition(this.getNthWordFromCursor(-1));
				} else {
					this.moveCursorBy(-1);
				}

				return true;
			case 205:
				if (GuiScreen.isShiftKeyDown()) {
					if (GuiScreen.isCtrlKeyDown()) {
						this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
					} else {
						this.setSelectionPos(this.getSelectionEnd() + 1);
					}
				} else if (GuiScreen.isCtrlKeyDown()) {
					this.setCursorPosition(this.getNthWordFromCursor(1));
				} else {
					this.moveCursorBy(1);
				}

				return true;
			case 207:
				if (GuiScreen.isShiftKeyDown()) {
					this.setSelectionPos(this.text.length());
				} else {
					this.setCursorPositionEnd();
				}

				return true;
			case 211:
				if (GuiScreen.isCtrlKeyDown()) {
					if (this.isEnabled) {
						this.deleteWords(1);
					}
				} else if (this.isEnabled) {
					this.deleteFromCursor(1);
				}

				return true;
			default:
				if (ChatAllowedCharacters.isAllowedCharacter(parChar1)) {
					if (this.isEnabled) {
						this.writeText(Character.toString(parChar1));
					}

					return true;
				} else {
					return false;
				}
			}
		}
	}

	/**+
	 * Args: x, y, buttonClicked
	 */
	public void mouseClicked(int parInt1, int parInt2, int parInt3) {
		boolean flag = parInt1 >= this.xPosition && parInt1 < this.xPosition + this.width && parInt2 >= this.yPosition
				&& parInt2 < this.yPosition + this.height;
		if (this.canLoseFocus) {
			this.setFocused(flag);
		}

		if (this.isFocused && flag && parInt3 == 0) {
			int i = parInt1 - this.xPosition;
			if (this.enableBackgroundDrawing) {
				i -= 4;
			}

			String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset),
					this.getWidth());
			this.setCursorPosition(this.fontRendererInstance.trimStringToWidth(s, i).length() + this.lineScrollOffset);
		}

	}

	/**+
	 * Draws the textbox
	 */
	public void drawTextBox() {
		if (this.getVisible()) {
			if (this.getEnableBackgroundDrawing()) {
				drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1,
						this.yPosition + this.height + 1, -6250336);
				drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height,
						-16777216);
			}

			int i = this.isEnabled ? this.enabledColor : this.disabledColor;
			int j = this.cursorPosition - this.lineScrollOffset;
			int k = this.selectionEnd - this.lineScrollOffset;
			String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset),
					this.getWidth());
			boolean flag = j >= 0 && j <= s.length();
			boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
			int l = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
			int i1 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
			int j1 = l;
			if (k > s.length()) {
				k = s.length();
			}

			if (s.length() > 0) {
				String s1 = flag ? s.substring(0, j) : s;
				j1 = this.fontRendererInstance.drawStringWithShadow(s1, (float) l, (float) i1, i);
			}

			boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
			int k1 = j1;
			if (!flag) {
				k1 = j > 0 ? l + this.width : l;
			} else if (flag2) {
				k1 = j1 - 1;
				--j1;
			}

			if (s.length() > 0 && flag && j < s.length()) {
				j1 = this.fontRendererInstance.drawStringWithShadow(s.substring(j), (float) j1, (float) i1, i);
			}

			if (flag1) {
				if (flag2) {
					Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
				} else {
					this.fontRendererInstance.drawStringWithShadow("_", (float) k1, (float) i1, i);
				}
			}

			if (k != j) {
				int l1 = l + this.fontRendererInstance.getStringWidth(s.substring(0, k));
				this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT);
			}

		}
	}

	/**+
	 * draws the vertical line cursor in the textbox
	 */
	private void drawCursorVertical(int parInt1, int parInt2, int parInt3, int parInt4) {
		if (parInt1 < parInt3) {
			int i = parInt1;
			parInt1 = parInt3;
			parInt3 = i;
		}

		if (parInt2 < parInt4) {
			int j = parInt2;
			parInt2 = parInt4;
			parInt4 = j;
		}

		if (parInt3 > this.xPosition + this.width) {
			parInt3 = this.xPosition + this.width;
		}

		if (parInt1 > this.xPosition + this.width) {
			parInt1 = this.xPosition + this.width;
		}

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.color(0.2F, 0.2F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_ONE_MINUS_DST_COLOR, GL_SRC_ALPHA);
		GlStateManager.disableTexture2D();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos((double) parInt1, (double) parInt4, 0.0D).endVertex();
		worldrenderer.pos((double) parInt3, (double) parInt4, 0.0D).endVertex();
		worldrenderer.pos((double) parInt3, (double) parInt2, 0.0D).endVertex();
		worldrenderer.pos((double) parInt1, (double) parInt2, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
	}

	public void setMaxStringLength(int parInt1) {
		this.maxStringLength = parInt1;
		if (this.text.length() > parInt1) {
			this.text = this.text.substring(0, parInt1);
		}

	}

	/**+
	 * returns the maximum number of character that can be contained
	 * in this textbox
	 */
	public int getMaxStringLength() {
		return this.maxStringLength;
	}

	/**+
	 * returns the current position of the cursor
	 */
	public int getCursorPosition() {
		return this.cursorPosition;
	}

	/**+
	 * get enable drawing background and outline
	 */
	public boolean getEnableBackgroundDrawing() {
		return this.enableBackgroundDrawing;
	}

	/**+
	 * enable drawing background and outline
	 */
	public void setEnableBackgroundDrawing(boolean parFlag) {
		this.enableBackgroundDrawing = parFlag;
	}

	/**+
	 * Sets the text colour for this textbox (disabled text will not
	 * use this colour)
	 */
	public void setTextColor(int parInt1) {
		this.enabledColor = parInt1;
	}

	public void setDisabledTextColour(int parInt1) {
		this.disabledColor = parInt1;
	}

	/**+
	 * Sets focus to this gui element
	 */
	public void setFocused(boolean parFlag) {
		if (parFlag && !this.isFocused) {
			this.cursorCounter = 0;
		}

		this.isFocused = parFlag;
	}

	/**+
	 * Getter for the focused field
	 */
	public boolean isFocused() {
		return this.isFocused;
	}

	public void setEnabled(boolean parFlag) {
		this.isEnabled = parFlag;
	}

	/**+
	 * the side of the selection that is not the cursor, may be the
	 * same as the cursor
	 */
	public int getSelectionEnd() {
		return this.selectionEnd;
	}

	/**+
	 * returns the width of the textbox depending on if background
	 * drawing is enabled
	 */
	public int getWidth() {
		return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
	}

	/**+
	 * Sets the position of the selection anchor (i.e. position the
	 * selection was started at)
	 */
	public void setSelectionPos(int parInt1) {
		int i = this.text.length();
		if (parInt1 > i) {
			parInt1 = i;
		}

		if (parInt1 < 0) {
			parInt1 = 0;
		}

		this.selectionEnd = parInt1;
		if (this.fontRendererInstance != null) {
			if (this.lineScrollOffset > i) {
				this.lineScrollOffset = i;
			}

			int j = this.getWidth();
			String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
			int k = s.length() + this.lineScrollOffset;
			if (parInt1 == this.lineScrollOffset) {
				this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, j, true).length();
			}

			if (parInt1 > k) {
				this.lineScrollOffset += parInt1 - k;
			} else if (parInt1 <= this.lineScrollOffset) {
				this.lineScrollOffset -= this.lineScrollOffset - parInt1;
			}

			this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, i);
		}

	}

	/**+
	 * if true the textbox can lose focus by clicking elsewhere on
	 * the screen
	 */
	public void setCanLoseFocus(boolean parFlag) {
		this.canLoseFocus = parFlag;
	}

	/**+
	 * returns true if this textbox is visible
	 */
	public boolean getVisible() {
		return this.visible;
	}

	/**+
	 * Sets whether or not this textbox is visible
	 */
	public void setVisible(boolean parFlag) {
		this.visible = parFlag;
	}

	public void fireInputEvent(EnumInputEvent clipboardPaste, String param) {
		if (!isFocused)
			return;
		switch (clipboardPaste) {
		case CLIPBOARD_COPY:
			GuiScreen.setClipboardString(this.getSelectedText());
			break;
		case CLIPBOARD_PASTE:
			if (this.isEnabled) {
				this.writeText(param != null ? param : GuiScreen.getClipboardString());
			}
			break;
		default:
			break;
		}
	}

}