package net.minecraft.client.gui;

import java.io.IOException;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.cookie.ServerCookieDataStore;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiNetworkSettingsButton;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenConnectOption;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANConnecting;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerList;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayServer;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
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
public class GuiMultiplayer extends GuiScreen implements GuiYesNoCallback {
	private static final Logger logger = LogManager.getLogger();
	private GuiScreen parentScreen;
	private ServerSelectionList serverListSelector;
	private ServerList savedServerList;
	private GuiButton btnEditServer;
	private GuiButton btnSelectServer;
	private GuiButton btnDeleteServer;
	private boolean deletingServer;
	private boolean addingServer;
	private boolean editingServer;
	private boolean directConnect;
	private String hoveringText;

	public ServerData getSelectedServer() {
		return selectedServer;
	}

	private ServerData selectedServer;
	private boolean initialized;
	private static long lastRefreshCommit = 0l;

	private static LANServerList lanServerList = null;

	public int ticksOpened;

	private final GuiNetworkSettingsButton relaysButton;

	public GuiMultiplayer(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
		this.relaysButton = new GuiNetworkSettingsButton(this);
		if (lanServerList != null) {
			lanServerList.forceRefresh();
		}
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		if (!this.initialized) {
			this.initialized = true;
			this.savedServerList = ServerList.getServerList();
			this.savedServerList.loadServerList();
			this.serverListSelector = new ServerSelectionList(this, this.mc, this.width, this.height, 32,
					this.height - 64, 36);
			this.serverListSelector.func_148195_a(this.savedServerList);
			if (lanServerList == null) {
				lanServerList = new LANServerList();
			}
			lanServerList.forceRefresh();
		} else {
			this.serverListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
		}

		this.createButtons();
	}

	/**+
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.serverListSelector.handleMouseInput();
	}

	public void handleTouchInput() throws IOException {
		super.handleTouchInput();
		this.serverListSelector.handleTouchInput();
	}

	public void createButtons() {
		this.buttonList.add(this.btnEditServer = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20,
				I18n.format("selectServer.edit", new Object[0])));
		this.buttonList.add(this.btnDeleteServer = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20,
				I18n.format("selectServer.delete", new Object[0])));
		this.buttonList.add(this.btnSelectServer = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20,
				I18n.format("selectServer.select", new Object[0])));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20,
				I18n.format("selectServer.direct", new Object[0])));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20,
				I18n.format("selectServer.add", new Object[0])));
		this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20,
				I18n.format("selectServer.refresh", new Object[0])));
		this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20,
				I18n.format("gui.cancel", new Object[0])));
		this.selectServer(this.serverListSelector.func_148193_k());
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();
		this.savedServerList.updateServerPing();
		if (lanServerList.update()) {
			this.selectServer(-1);
		}
		++ticksOpened;
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
			GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0
					? null
					: this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
			if (parGuiButton.id == 2 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
				String s4 = ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData().serverName;
				if (s4 != null) {
					this.deletingServer = true;
					String s = I18n.format("selectServer.deleteQuestion", new Object[0]);
					String s1 = "\'" + s4 + "\' " + I18n.format("selectServer.deleteWarning", new Object[0]);
					String s2 = I18n.format("selectServer.deleteButton", new Object[0]);
					String s3 = I18n.format("gui.cancel", new Object[0]);
					GuiYesNo guiyesno = new GuiYesNo(this, s, s1, s2, s3, this.serverListSelector.func_148193_k());
					this.mc.displayGuiScreen(guiyesno);
				}
			} else if (parGuiButton.id == 1) {
				this.connectToSelected();
			} else if (parGuiButton.id == 4) {
				this.directConnect = true;
				this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
				this.mc.displayGuiScreen(new GuiScreenConnectOption(this));
			} else if (parGuiButton.id == 3) {
				this.addingServer = true;
				this.mc.displayGuiScreen(new GuiScreenAddServer(this,
						this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "",
								false)));
			} else if (parGuiButton.id == 7 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
				this.editingServer = true;
				ServerData serverdata = ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData();
				if (serverdata != null) {
					this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
					this.selectedServer.copyFrom(serverdata);
					this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
				}
			} else if (parGuiButton.id == 0) {
				this.mc.displayGuiScreen(this.parentScreen);
			} else if (parGuiButton.id == 8) {
				long millis = EagRuntime.steadyTimeMillis();
				if (millis - lastRefreshCommit > 700l) {
					lastRefreshCommit = millis;
					this.refreshServerList();
				}
			}

		}
	}

	public void refreshServerList() {
		this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
	}

	public void confirmClicked(boolean flag, int var2) {
		GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null
				: this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
		if (this.deletingServer) {
			this.deletingServer = false;
			if (flag && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
				this.savedServerList.removeServerData(this.serverListSelector.func_148193_k());
				this.savedServerList.saveServerList();
				this.serverListSelector.setSelectedSlotIndex(-1);
				this.serverListSelector.func_148195_a(this.savedServerList);
			}
			long millis = EagRuntime.steadyTimeMillis();
			if (millis - lastRefreshCommit > 700l) {
				lastRefreshCommit = millis;
				this.refreshServerList();
			}
		} else if (this.directConnect) {
			this.directConnect = false;
			if (flag) {
				this.connectToServer(this.selectedServer);
			} else {
				this.mc.displayGuiScreen(this);
			}
		} else if (this.addingServer) {
			this.addingServer = false;
			if (flag) {
				if (!this.selectedServer.enableCookies) {
					ServerCookieDataStore.clearCookie(this.selectedServer.serverIP);
				}
				this.savedServerList.addServerData(this.selectedServer);
				this.savedServerList.saveServerList();
				this.serverListSelector.setSelectedSlotIndex(-1);
				this.serverListSelector.func_148195_a(this.savedServerList);
			}
			long millis = EagRuntime.steadyTimeMillis();
			if (millis - lastRefreshCommit > 700l) {
				lastRefreshCommit = millis;
				this.refreshServerList();
			}
		} else if (this.editingServer) {
			this.editingServer = false;
			if (flag && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
				ServerData serverdata = ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData();
				serverdata.serverName = this.selectedServer.serverName;
				serverdata.serverIP = this.selectedServer.serverIP;
				if (serverdata.enableCookies && !this.selectedServer.enableCookies) {
					ServerCookieDataStore.clearCookie(this.selectedServer.serverIP);
				}
				serverdata.copyFrom(this.selectedServer);
				this.savedServerList.saveServerList();
				this.serverListSelector.func_148195_a(this.savedServerList);
			}
			long millis = EagRuntime.steadyTimeMillis();
			if (millis - lastRefreshCommit > 700l) {
				lastRefreshCommit = millis;
				this.refreshServerList();
			}
		}
	}

	public void cancelDirectConnect() {
		this.directConnect = false;
	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
		int i = this.serverListSelector.func_148193_k();
		GuiListExtended.IGuiListEntry guilistextended$iguilistentry = i < 0 ? null
				: this.serverListSelector.getListEntry(i);
		if (parInt1 == 63) {
			this.refreshServerList();
		} else {
			if (i >= 0) {
				if (parInt1 == 200) {
					if (isShiftKeyDown()) {
						if (i > 0 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
							this.savedServerList.swapServers(i, i - 1);
							this.selectServer(this.serverListSelector.func_148193_k() - 1);
							this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
							this.serverListSelector.func_148195_a(this.savedServerList);
						}
					} else if (i > 0) {
						this.selectServer(this.serverListSelector.func_148193_k() - 1);
						this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
					} else {
						this.selectServer(-1);
					}
				} else if (parInt1 == 208) {
					if (isShiftKeyDown()) {
						if (i < this.savedServerList.countServers() - 1) {
							this.savedServerList.swapServers(i, i + 1);
							this.selectServer(i + 1);
							this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
							this.serverListSelector.func_148195_a(this.savedServerList);
						}
					} else if (i < this.serverListSelector.getSize() - 1) {
						this.selectServer(this.serverListSelector.func_148193_k() + 1);
						this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
					} else {
						this.selectServer(-1);
					}
				} else if (parInt1 != 28 && parInt1 != 156) {
					super.keyTyped(parChar1, parInt1);
				} else {
					this.actionPerformed((GuiButton) this.buttonList.get(2));
				}
			} else {
				super.keyTyped(parChar1, parInt1);
			}

		}
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.hoveringText = null;
		this.drawDefaultBackground();
		this.serverListSelector.drawScreen(i, j, f);
		this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.title", new Object[0]), this.width / 2,
				20, 16777215);
		super.drawScreen(i, j, f);
		relaysButton.drawScreen(i, j);
		drawPluginDownloadLink(i, j);
		if (this.hoveringText != null) {
			this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), i, j);
			GlStateManager.disableLighting();
		}
	}

	private void drawPluginDownloadLink(int xx, int yy) {
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.75f, 0.75f, 0.75f);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		String text = "Download EaglerXServer";
		int w = mc.fontRendererObj.getStringWidth(text);
		boolean hover = xx > width - 5 - (w + 5) * 3 / 4 && yy > 1 && xx < width - 2 && yy < 12;
		if (hover) {
			Mouse.showCursor(EnumCursorType.HAND);
		}

		drawString(mc.fontRendererObj, EnumChatFormatting.UNDERLINE + text, (width - 1) * 4 / 3 - w - 5, 5,
				hover ? 0xFFEEEE22 : 0xFFCCCCCC);

		GlStateManager.popMatrix();
	}

	public void connectToSelected() {
		if (this.serverListSelector.func_148193_k() < this.serverListSelector.getOrigSize()) {
			GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0
					? null
					: this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
			if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
				this.connectToServer(((ServerListEntryNormal) guilistextended$iguilistentry).getServerData());
			}
		} else {
			int par1 = this.serverListSelector.func_148193_k() - this.serverListSelector.getOrigSize();

			if (par1 < lanServerList.countServers()) {
				LANServerList.LanServer var2 = lanServerList.getServer(par1);
				connectToLAN("Connecting to '" + var2.getLanServerMotd() + "'...", var2.getLanServerCode(),
						var2.getLanServerRelay());
			}
		}
	}

	private void connectToServer(ServerData server) {
		this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, server));
	}

	private void connectToLAN(String text, String code, RelayServer uri) {
		this.mc.loadingScreen.resetProgressAndMessage(text);
		this.mc.displayGuiScreen(new GuiScreenLANConnecting(this, code, uri));
	}

	public void selectServer(int index) {
		this.serverListSelector.setSelectedSlotIndex(index);
		GuiListExtended.IGuiListEntry guilistextended$iguilistentry = index < 0 ? null
				: this.serverListSelector.getListEntry(index);
		this.btnSelectServer.enabled = false;
		this.btnEditServer.enabled = false;
		this.btnDeleteServer.enabled = false;
		if (guilistextended$iguilistentry != null) {
			this.btnSelectServer.enabled = true;
			if (guilistextended$iguilistentry instanceof ServerListEntryNormal
					&& ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData() != null) {
				this.btnEditServer.enabled = true;
				this.btnDeleteServer.enabled = true;
			}
		}
	}

	public void setHoveringText(String parString1) {
		this.hoveringText = parString1;
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		relaysButton.mouseClicked(parInt1, parInt2, parInt3);
		super.mouseClicked(parInt1, parInt2, parInt3);
		this.serverListSelector.mouseClicked(parInt1, parInt2, parInt3);
		String text = "Download EaglerXServer";
		int w = mc.fontRendererObj.getStringWidth(text);
		if (parInt1 > width - 5 - (w + 5) * 3 / 4 && parInt2 > 1 && parInt1 < width - 2 && parInt2 < 12) {
			this.mc.getSoundHandler()
					.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			EagRuntime.openLink("https://lax1dude.net/eaglerxserver");
		}
	}

	/**+
	 * Called when a mouse button is released. Args : mouseX,
	 * mouseY, releaseButton
	 */
	protected void mouseReleased(int i, int j, int k) {
		super.mouseReleased(i, j, k);
		this.serverListSelector.mouseReleased(i, j, k);
	}

	public ServerList getServerList() {
		return this.savedServerList;
	}

	static LANServerList getLanServerList() {
		return lanServerList;
	}

	public boolean func_175392_a(ServerListEntryNormal parServerListEntryNormal, int parInt1) {
		return parInt1 > 0;
	}

	public boolean func_175394_b(ServerListEntryNormal parServerListEntryNormal, int parInt1) {
		return parInt1 < this.savedServerList.countServers() - 1;
	}

	public void func_175391_a(ServerListEntryNormal parServerListEntryNormal, int parInt1, boolean parFlag) {
		int i = parFlag ? 0 : parInt1 - 1;
		this.savedServerList.swapServers(parInt1, i);
		if (this.serverListSelector.func_148193_k() == parInt1) {
			this.selectServer(i);
		}

		this.serverListSelector.func_148195_a(this.savedServerList);
	}

	public void func_175393_b(ServerListEntryNormal parServerListEntryNormal, int parInt1, boolean parFlag) {
		int i = parFlag ? this.savedServerList.countServers() - 1 : parInt1 + 1;
		this.savedServerList.swapServers(parInt1, i);
		if (this.serverListSelector.func_148193_k() == parInt1) {
			this.selectServer(i);
		}

		this.serverListSelector.func_148195_a(this.savedServerList);
	}
}