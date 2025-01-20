package net.minecraft.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.minecraft.client.Minecraft;
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
public class GuiOptionsRowList extends GuiListExtended {
	private final List<GuiOptionsRowList.Row> field_148184_k = Lists.newArrayList();

	public GuiOptionsRowList(Minecraft mcIn, int parInt1, int parInt2, int parInt3, int parInt4, int parInt5,
			GameSettings.Options... parArrayOfOptions) {
		super(mcIn, parInt1, parInt2, parInt3, parInt4, parInt5);
		this.field_148163_i = false;

		for (int i = 0; i < parArrayOfOptions.length; i += 2) {
			GameSettings.Options gamesettings$options = parArrayOfOptions[i];
			GameSettings.Options gamesettings$options1 = i < parArrayOfOptions.length - 1 ? parArrayOfOptions[i + 1]
					: null;
			GuiButton guibutton = this.func_148182_a(mcIn, parInt1 / 2 - 155, 0, gamesettings$options);
			GuiButton guibutton1 = this.func_148182_a(mcIn, parInt1 / 2 - 155 + 160, 0, gamesettings$options1);
			this.field_148184_k.add(new GuiOptionsRowList.Row(guibutton, guibutton1));
		}

	}

	private GuiButton func_148182_a(Minecraft mcIn, int parInt1, int parInt2, GameSettings.Options parOptions) {
		if (parOptions == null) {
			return null;
		} else {
			int i = parOptions.returnEnumOrdinal();
			return (GuiButton) (parOptions.getEnumFloat() ? new GuiOptionSlider(i, parInt1, parInt2, parOptions)
					: new GuiOptionButton(i, parInt1, parInt2, parOptions,
							mcIn.gameSettings.getKeyBinding(parOptions)));
		}
	}

	/**+
	 * Gets the IGuiListEntry object for the given index
	 */
	public GuiOptionsRowList.Row getListEntry(int i) {
		return (GuiOptionsRowList.Row) this.field_148184_k.get(i);
	}

	protected int getSize() {
		return this.field_148184_k.size();
	}

	/**+
	 * Gets the width of the list
	 */
	public int getListWidth() {
		return 400;
	}

	protected int getScrollBarX() {
		return super.getScrollBarX() + 32;
	}

	public static class Row implements GuiListExtended.IGuiListEntry {
		private final Minecraft field_148325_a = Minecraft.getMinecraft();
		private final GuiButton field_148323_b;
		private final GuiButton field_148324_c;

		public Row(GuiButton parGuiButton, GuiButton parGuiButton2) {
			this.field_148323_b = parGuiButton;
			this.field_148324_c = parGuiButton2;
		}

		public void drawEntry(int var1, int var2, int i, int var4, int var5, int j, int k, boolean var8) {
			if (this.field_148323_b != null) {
				this.field_148323_b.yPosition = i;
				this.field_148323_b.drawButton(this.field_148325_a, j, k);
			}

			if (this.field_148324_c != null) {
				this.field_148324_c.yPosition = i;
				this.field_148324_c.drawButton(this.field_148325_a, j, k);
			}

		}

		public boolean mousePressed(int var1, int i, int j, int var4, int var5, int var6) {
			if (var4 != 0 && var4 != 12345)
				return false;
			boolean touchMode = PointerInputAbstraction.isTouchMode();
			if ((!touchMode || (this.field_148323_b.isSliderTouchEvents() == (var4 == 12345)))
					&& this.field_148323_b.mousePressed(this.field_148325_a, i, j)) {
				if (this.field_148323_b instanceof GuiOptionButton) {
					this.field_148325_a.gameSettings
							.setOptionValue(((GuiOptionButton) this.field_148323_b).returnEnumOptions(), 1);
					this.field_148323_b.displayString = this.field_148325_a.gameSettings
							.getKeyBinding(GameSettings.Options.getEnumOptions(this.field_148323_b.id));
				}

				return true;
			} else if (this.field_148324_c != null
					&& (!touchMode || (this.field_148324_c.isSliderTouchEvents() == (var4 == 12345)))
					&& this.field_148324_c.mousePressed(this.field_148325_a, i, j)) {
				if (this.field_148324_c instanceof GuiOptionButton) {
					this.field_148325_a.gameSettings
							.setOptionValue(((GuiOptionButton) this.field_148324_c).returnEnumOptions(), 1);
					this.field_148324_c.displayString = this.field_148325_a.gameSettings
							.getKeyBinding(GameSettings.Options.getEnumOptions(this.field_148324_c.id));
				}

				return true;
			} else {
				return false;
			}
		}

		public void mouseReleased(int var1, int i, int j, int var4, int var5, int var6) {
			if (var4 != 0 && var4 != 12345)
				return;
			boolean touchMode = PointerInputAbstraction.isTouchMode();
			if (this.field_148323_b != null
					&& (!touchMode || (this.field_148323_b.isSliderTouchEvents() == (var4 == 12345)))) {
				this.field_148323_b.mouseReleased(i, j);
			}

			if (this.field_148324_c != null
					&& (!touchMode || (this.field_148324_c.isSliderTouchEvents() == (var4 == 12345)))) {
				this.field_148324_c.mouseReleased(i, j);
			}

		}

		public void setSelected(int var1, int var2, int var3) {
		}
	}

	public GuiOptionButton getButtonFor(GameSettings.Options enumOption) {
		for (Row r : field_148184_k) {
			if (r.field_148323_b != null) {
				if (r.field_148323_b instanceof GuiOptionButton) {
					GuiOptionButton btn = (GuiOptionButton) r.field_148323_b;
					if (btn.returnEnumOptions() == enumOption) {
						return btn;
					}
				}
			}
			if (r.field_148324_c != null) {
				if (r.field_148324_c instanceof GuiOptionButton) {
					GuiOptionButton btn = (GuiOptionButton) r.field_148324_c;
					if (btn.returnEnumOptions() == enumOption) {
						return btn;
					}
				}
			}
		}
		return null;
	}

	public GuiOptionSlider getSliderFor(GameSettings.Options enumOption) {
		for (Row r : field_148184_k) {
			if (r.field_148323_b != null) {
				if (r.field_148323_b instanceof GuiOptionSlider) {
					GuiOptionSlider btn = (GuiOptionSlider) r.field_148323_b;
					if (btn.getEnumOptions() == enumOption) {
						return btn;
					}
				}
			}
			if (r.field_148324_c != null) {
				if (r.field_148324_c instanceof GuiOptionSlider) {
					GuiOptionSlider btn = (GuiOptionSlider) r.field_148324_c;
					if (btn.getEnumOptions() == enumOption) {
						return btn;
					}
				}
			}
		}
		return null;
	}

}