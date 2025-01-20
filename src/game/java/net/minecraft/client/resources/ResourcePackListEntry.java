package net.minecraft.client.resources;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

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
public abstract class ResourcePackListEntry implements GuiListExtended.IGuiListEntry {
	private static final ResourceLocation RESOURCE_PACKS_TEXTURE = new ResourceLocation(
			"textures/gui/resource_packs.png");
	private static final IChatComponent field_183020_d = new ChatComponentTranslation("resourcePack.incompatible",
			new Object[0]);
	private static final IChatComponent field_183021_e = new ChatComponentTranslation("resourcePack.incompatible.old",
			new Object[0]);
	private static final IChatComponent field_183022_f = new ChatComponentTranslation("resourcePack.incompatible.new",
			new Object[0]);
	protected final Minecraft mc;
	protected final GuiScreenResourcePacks resourcePacksGUI;

	public ResourcePackListEntry(GuiScreenResourcePacks resourcePacksGUIIn) {
		this.resourcePacksGUI = resourcePacksGUIIn;
		this.mc = Minecraft.getMinecraft();
	}

	public void drawEntry(int var1, int i, int j, int k, int l, int i1, int j1, boolean flag) {
		int k1 = this.func_183019_a();
		if (k1 != 1) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			Gui.drawRect(i - 1, j - 1, i + k - 9, j + l + 1, -8978432);
		}

		this.func_148313_c();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Gui.drawModalRectWithCustomSizedTexture(i, j, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
		String s = this.func_148312_b();
		String s1 = this.func_148311_a();
		if ((this.mc.gameSettings.touchscreen || flag) && this.func_148310_d()) {
			this.mc.getTextureManager().bindTexture(RESOURCE_PACKS_TEXTURE);
			Gui.drawRect(i, j, i + 32, j + 32, -1601138544);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			int l1 = i1 - i;
			int i2 = j1 - j;
			if (k1 < 1) {
				s = field_183020_d.getFormattedText();
				s1 = field_183021_e.getFormattedText();
			} else if (k1 > 1) {
				s = field_183020_d.getFormattedText();
				s1 = field_183022_f.getFormattedText();
			}

			if (this.func_148309_e()) {
				if (l1 < 32) {
					Gui.drawModalRectWithCustomSizedTexture(i, j, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
				} else {
					Gui.drawModalRectWithCustomSizedTexture(i, j, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
				}
			} else {
				if (this.func_148308_f()) {
					if (l1 < 16) {
						Gui.drawModalRectWithCustomSizedTexture(i, j, 32.0F, 32.0F, 32, 32, 256.0F, 256.0F);
					} else {
						Gui.drawModalRectWithCustomSizedTexture(i, j, 32.0F, 0.0F, 32, 32, 256.0F, 256.0F);
					}
				}

				if (this.func_148314_g()) {
					if (l1 < 32 && l1 > 16 && i2 < 16) {
						Gui.drawModalRectWithCustomSizedTexture(i, j, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
					} else {
						Gui.drawModalRectWithCustomSizedTexture(i, j, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
					}
				}

				if (this.func_148307_h()) {
					if (l1 < 32 && l1 > 16 && i2 > 16) {
						Gui.drawModalRectWithCustomSizedTexture(i, j, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
					} else {
						Gui.drawModalRectWithCustomSizedTexture(i, j, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
					}
				}
			}
		}

		int k2 = this.mc.fontRendererObj.getStringWidth(s);
		if (k2 > 157) {
			s = this.mc.fontRendererObj.trimStringToWidth(s, 157 - this.mc.fontRendererObj.getStringWidth("..."))
					+ "...";
		}

		this.mc.fontRendererObj.drawStringWithShadow(s, (float) (i + 32 + 2), (float) (j + 1), 16777215);
		List list = this.mc.fontRendererObj.listFormattedStringToWidth(s1, 157);

		for (int j2 = 0; j2 < 2 && j2 < list.size(); ++j2) {
			this.mc.fontRendererObj.drawStringWithShadow((String) list.get(j2), (float) (i + 32 + 2),
					(float) (j + 12 + 10 * j2), 8421504);
		}

	}

	protected abstract int func_183019_a();

	protected abstract String func_148311_a();

	protected abstract String func_148312_b();

	protected abstract void func_148313_c();

	protected abstract String getEaglerFolderName();

	protected boolean func_148310_d() {
		return true;
	}

	protected boolean func_148309_e() {
		return !this.resourcePacksGUI.hasResourcePackEntry(this);
	}

	protected boolean func_148308_f() {
		return this.resourcePacksGUI.hasResourcePackEntry(this);
	}

	protected boolean func_148314_g() {
		List list = this.resourcePacksGUI.getListContaining(this);
		int i = list.indexOf(this);
		return i > 0 && ((ResourcePackListEntry) list.get(i - 1)).func_148310_d();
	}

	protected boolean func_148307_h() {
		List list = this.resourcePacksGUI.getListContaining(this);
		int i = list.indexOf(this);
		return i >= 0 && i < list.size() - 1 && ((ResourcePackListEntry) list.get(i + 1)).func_148310_d();
	}

	private void proceedWithBs(int l, boolean deleteInstead) {
		if (!deleteInstead && l != 1) {
			String s1 = I18n.format("resourcePack.incompatible.confirm.title", new Object[0]);
			String s = I18n.format("resourcePack.incompatible.confirm." + (l > 1 ? "new" : "old"), new Object[0]);
			this.mc.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
				public void confirmClicked(boolean flag, int var2) {
					List list2 = ResourcePackListEntry.this.resourcePacksGUI
							.getListContaining(ResourcePackListEntry.this);
					ResourcePackListEntry.this.mc.displayGuiScreen(ResourcePackListEntry.this.resourcePacksGUI);
					if (flag) {
						list2.remove(ResourcePackListEntry.this);
						ResourcePackListEntry.this.resourcePacksGUI.getSelectedResourcePacks().add(0,
								ResourcePackListEntry.this);
					}

				}
			}, s1, s, 0).withOpaqueBackground());
		} else {
			this.mc.displayGuiScreen(this.resourcePacksGUI);
			this.resourcePacksGUI.getListContaining(this).remove(this);
			if (deleteInstead) {
				this.mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.deleting"), this.func_148312_b());
				EaglerFolderResourcePack.deleteResourcePack(EaglerFolderResourcePack.RESOURCE_PACKS,
						this.getEaglerFolderName());
			} else {
				this.resourcePacksGUI.getSelectedResourcePacks().add(0, this);
			}
		}
	}

	/**+
	 * Returns true if the mouse has been pressed on this control.
	 */
	public boolean mousePressed(int var1, int var2, int var3, int var4, int i, int j) {
		if (this.func_148310_d() && i <= 32) {
			if (this.func_148309_e()) {
				this.resourcePacksGUI.markChanged();
				int l = this.func_183019_a();
				if (Keyboard.isKeyDown(KeyboardConstants.KEY_LSHIFT)
						|| Keyboard.isKeyDown(KeyboardConstants.KEY_RSHIFT)) {
					proceedWithBs(l, false);
				} else {
					this.mc.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
						public void confirmClicked(boolean flag, int var2) {
							proceedWithBs(l, flag);

						}
					}, I18n.format("resourcePack.prompt.title", this.func_148312_b()),
							I18n.format("resourcePack.prompt.text", new Object[0]),
							I18n.format("resourcePack.prompt.delete", new Object[0]),
							I18n.format("resourcePack.prompt.add", new Object[0]), 0).withOpaqueBackground());
				}
				return true;
			}

			if (i < 16 && this.func_148308_f()) {
				this.resourcePacksGUI.getListContaining(this).remove(this);
				this.resourcePacksGUI.getAvailableResourcePacks().add(0, this);
				this.resourcePacksGUI.markChanged();
				return true;
			}

			if (i > 16 && j < 16 && this.func_148314_g()) {
				List list1 = this.resourcePacksGUI.getListContaining(this);
				int i1 = list1.indexOf(this);
				list1.remove(this);
				list1.add(i1 - 1, this);
				this.resourcePacksGUI.markChanged();
				return true;
			}

			if (i > 16 && j > 16 && this.func_148307_h()) {
				List list = this.resourcePacksGUI.getListContaining(this);
				int k = list.indexOf(this);
				list.remove(this);
				list.add(k + 1, this);
				this.resourcePacksGUI.markChanged();
				return true;
			}
		}

		return false;
	}

	public void setSelected(int var1, int var2, int var3) {
	}

	/**+
	 * Fired when the mouse button is released. Arguments: index, x,
	 * y, mouseEvent, relativeX, relativeY
	 */
	public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
	}
}