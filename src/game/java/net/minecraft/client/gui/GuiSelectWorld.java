package net.minecraft.client.gui;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANConnect;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANNotSupported;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacket1CIssueDetected;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.SaveFormatComparator;
import org.apache.commons.lang3.StringUtils;

import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenBackupWorldSelection;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenCreateWorldSelection;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenIntegratedServerBusy;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANInfo;

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
public class GuiSelectWorld extends GuiScreen implements GuiYesNoCallback {
	private static final Logger logger = LogManager.getLogger();
	private final DateFormat field_146633_h = new SimpleDateFormat();
	protected GuiScreen parentScreen;
	protected String field_146628_f = "Select world";
	private boolean field_146634_i;
	private int field_146640_r;
	private java.util.List<SaveFormatComparator> field_146639_s;
	private GuiSelectWorld.List field_146638_t;
	private String field_146637_u;
	private String field_146636_v;
	private String[] field_146635_w = new String[4];
	private boolean field_146643_x;
	private GuiButton deleteButton;
	private GuiButton selectButton;
	private GuiButton renameButton;
	private GuiButton recreateButton;
	private boolean hasRequestedWorlds = false;
	private boolean waitingForWorlds = false;
	private boolean ramdiskMode = false;

	public GuiSelectWorld(GuiScreen parentScreenIn) {
		this.parentScreen = parentScreenIn;
		this.field_146639_s = new ArrayList();
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		this.ramdiskMode = SingleplayerServerController.isIssueDetected(IPCPacket1CIssueDetected.ISSUE_RAMDISK_MODE);
		this.field_146628_f = I18n.format("selectWorld.title", new Object[0]);
		this.field_146637_u = I18n.format("selectWorld.world", new Object[0]);
		this.field_146636_v = I18n.format("selectWorld.conversion", new Object[0]);
		this.field_146635_w[WorldSettings.GameType.SURVIVAL.getID()] = I18n.format("gameMode.survival", new Object[0]);
		this.field_146635_w[WorldSettings.GameType.CREATIVE.getID()] = I18n.format("gameMode.creative", new Object[0]);
		this.field_146635_w[WorldSettings.GameType.ADVENTURE.getID()] = I18n.format("gameMode.adventure",
				new Object[0]);
		this.field_146635_w[WorldSettings.GameType.SPECTATOR.getID()] = I18n.format("gameMode.spectator",
				new Object[0]);
		this.field_146638_t = new GuiSelectWorld.List(this.mc, ramdiskMode ? -10 : 0);
		this.field_146638_t.registerScrollButtons(4, 5);
		this.func_146618_g();
	}

	public void updateScreen() {
		if (!hasRequestedWorlds && SingleplayerServerController.isReady()) {
			hasRequestedWorlds = true;
			waitingForWorlds = true;
			this.mc.getSaveLoader().flushCache();
			this.mc.displayGuiScreen(new GuiScreenIntegratedServerBusy(this, "singleplayer.busy.listingworlds",
					"singleplayer.failed.listingworlds", SingleplayerServerController::isReady, (t, u) -> {
						GuiScreenIntegratedServerBusy tt = (GuiScreenIntegratedServerBusy) t;
						Minecraft.getMinecraft().displayGuiScreen(
								GuiScreenIntegratedServerBusy.createException(parentScreen, tt.failMessage, u));
					}));
		} else if (waitingForWorlds && SingleplayerServerController.isReady()) {
			waitingForWorlds = false;
			this.func_146627_h();
		}
	}

	/**+
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.field_146638_t.handleMouseInput();
	}

	public void handleTouchInput() throws IOException {
		super.handleTouchInput();
		this.field_146638_t.handleTouchInput();
	}

	private void func_146627_h() {
		ISaveFormat isaveformat = this.mc.getSaveLoader();
		this.field_146639_s = isaveformat.getSaveList();
		Collections.sort(this.field_146639_s);
		this.field_146640_r = -1;
	}

	protected String func_146621_a(int parInt1) {
		return ((SaveFormatComparator) this.field_146639_s.get(parInt1)).getFileName();
	}

	protected String func_146614_d(int parInt1) {
		String s = ((SaveFormatComparator) this.field_146639_s.get(parInt1)).getDisplayName();
		if (StringUtils.isEmpty(s)) {
			s = I18n.format("selectWorld.world", new Object[0]) + " " + (parInt1 + 1);
		}

		return s;
	}

	public void func_146618_g() {
		this.buttonList.add(this.selectButton = new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20,
				I18n.format("selectWorld.select", new Object[0])));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20,
				I18n.format("selectWorld.create", new Object[0])));
		this.buttonList.add(this.renameButton = new GuiButton(6, this.width / 2 - 154, this.height - 28, 72, 20,
				I18n.format("selectWorld.rename", new Object[0])));
		this.buttonList.add(this.deleteButton = new GuiButton(2, this.width / 2 - 76, this.height - 28, 72, 20,
				I18n.format("selectWorld.delete", new Object[0])));
		this.buttonList.add(this.recreateButton = new GuiButton(7, this.width / 2 + 4, this.height - 28, 72, 20,
				I18n.format("selectWorld.backup", new Object[0])));
		this.buttonList.add(new GuiButton(0, this.width / 2 + 82, this.height - 28, 72, 20,
				I18n.format("gui.cancel", new Object[0])));
		this.selectButton.enabled = false;
		this.deleteButton.enabled = false;
		this.renameButton.enabled = false;
		this.recreateButton.enabled = false;
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		if (parGuiButton.enabled) {
			if (parGuiButton.id == 2) {
				String s = this.func_146614_d(this.field_146640_r);
				if (s != null) {
					this.field_146643_x = true;
					GuiYesNo guiyesno = func_152129_a(this, s, this.field_146640_r);
					this.mc.displayGuiScreen(guiyesno);
				}
			} else if (parGuiButton.id == 1) {
				this.func_146615_e(this.field_146640_r);
			} else if (parGuiButton.id == 3) {
				hasRequestedWorlds = false; // force refresh
				this.mc.displayGuiScreen(new GuiScreenCreateWorldSelection(this));
			} else if (parGuiButton.id == 6) {
				hasRequestedWorlds = false; // force refresh
				this.mc.displayGuiScreen(new GuiRenameWorld(this, this.func_146621_a(this.field_146640_r)));
			} else if (parGuiButton.id == 0) {
				this.mc.displayGuiScreen(this.parentScreen);
			} else if (parGuiButton.id == 7) {
				hasRequestedWorlds = false; // force refresh
				this.mc.displayGuiScreen(
						new GuiScreenBackupWorldSelection(this, this.func_146621_a(this.field_146640_r),
								((SaveFormatComparator) field_146639_s.get(this.field_146640_r)).levelDat));
			} else {
				this.field_146638_t.actionPerformed(parGuiButton);
			}

		}
	}

	public void func_146615_e(int parInt1) {
		this.mc.displayGuiScreen((GuiScreen) null);
		if (!this.field_146634_i) {
			this.field_146634_i = true;
			String s = this.func_146621_a(parInt1);
			if (s == null) {
				s = "World" + parInt1;
			}

			String s1 = this.func_146614_d(parInt1);
			if (s1 == null) {
				s1 = "World" + parInt1;
			}

			if (this.mc.getSaveLoader().canLoadWorld(s)) {
				this.mc.launchIntegratedServer(s, s1, (WorldSettings) null);
			}

		}
	}

	public void confirmClicked(boolean flag, int i) {
		if (this.field_146643_x) {
			this.field_146643_x = false;
			if (flag) {
				hasRequestedWorlds = false; // force refresh
				ISaveFormat isaveformat = this.mc.getSaveLoader();
				isaveformat.deleteWorldDirectory(this.func_146621_a(i));
				this.mc.displayGuiScreen(new GuiScreenIntegratedServerBusy(this, "singleplayer.busy.deleting",
						"singleplayer.failed.deleting", SingleplayerServerController::isReady));
			} else {
				this.mc.displayGuiScreen(this);
			}
		}

	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.field_146638_t.drawScreen(i, j, f);
		this.drawCenteredString(this.fontRendererObj, this.field_146628_f, this.width / 2, 20, 16777215);

		if (ramdiskMode) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.ramdiskWarning"), this.width / 2,
					height - 68, 11184810);
		}

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.75f, 0.75f, 0.75f);

		String text = I18n.format("directConnect.lanWorld");
		int w = mc.fontRendererObj.getStringWidth(text);
		boolean hover = i > 1 && j > 1 && i < (w * 3 / 4) + 7 && j < 12;
		if (hover) {
			Mouse.showCursor(EnumCursorType.HAND);
		}

		drawString(mc.fontRendererObj, EnumChatFormatting.UNDERLINE + text, 5, 5, hover ? 0xFFEEEE22 : 0xFFCCCCCC);

		GlStateManager.popMatrix();

		super.drawScreen(i, j, f);
	}

	@Override
	public void mouseClicked(int xx, int yy, int btn) {
		String text = I18n.format("directConnect.lanWorld");
		int w = mc.fontRendererObj.getStringWidth(text);
		if (xx > 2 && yy > 2 && xx < (w * 3 / 4) + 5 && yy < 12) {
			if (LANServerController.supported()) {
				mc.displayGuiScreen(GuiScreenLANInfo.showLANInfoScreen(new GuiScreenLANConnect(this)));
			} else {
				mc.displayGuiScreen(new GuiScreenLANNotSupported(this));
			}
			mc.getSoundHandler()
					.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
		}
		super.mouseClicked(xx, yy, btn);
	}

	public static GuiYesNo func_152129_a(GuiYesNoCallback parGuiYesNoCallback, String parString1, int parInt1) {
		String s = I18n.format("selectWorld.deleteQuestion", new Object[0]);
		String s1 = "\'" + parString1 + "\' " + I18n.format("selectWorld.deleteWarning", new Object[0]);
		String s2 = I18n.format("selectWorld.deleteButton", new Object[0]);
		String s3 = I18n.format("gui.cancel", new Object[0]);
		GuiYesNo guiyesno = new GuiYesNo(parGuiYesNoCallback, s, s1, s2, s3, parInt1);
		return guiyesno;
	}

	class List extends GuiSlot {
		public List(Minecraft mcIn, int i) {
			super(mcIn, GuiSelectWorld.this.width, GuiSelectWorld.this.height, 32, GuiSelectWorld.this.height - 64 + i,
					36);
		}

		protected int getSize() {
			return GuiSelectWorld.this.field_146639_s.size();
		}

		protected void elementClicked(int i, boolean flag, int var3, int var4) {
			GuiSelectWorld.this.field_146640_r = i;
			boolean flag1 = GuiSelectWorld.this.field_146640_r >= 0
					&& GuiSelectWorld.this.field_146640_r < this.getSize();
			GuiSelectWorld.this.selectButton.enabled = flag1;
			GuiSelectWorld.this.deleteButton.enabled = flag1;
			GuiSelectWorld.this.renameButton.enabled = flag1;
			GuiSelectWorld.this.recreateButton.enabled = flag1;
			if (flag && flag1) {
				GuiSelectWorld.this.func_146615_e(i);
			}

		}

		protected boolean isSelected(int i) {
			return i == GuiSelectWorld.this.field_146640_r;
		}

		protected int getContentHeight() {
			return GuiSelectWorld.this.field_146639_s.size() * 36;
		}

		protected void drawBackground() {
			GuiSelectWorld.this.drawDefaultBackground();
		}

		protected void drawSlot(int i, int j, int k, int var4, int var5, int var6) {
			SaveFormatComparator saveformatcomparator = (SaveFormatComparator) GuiSelectWorld.this.field_146639_s
					.get(i);
			String s = saveformatcomparator.getDisplayName();
			if (StringUtils.isEmpty(s)) {
				s = GuiSelectWorld.this.field_146637_u + " " + (i + 1);
			}

			String s1 = saveformatcomparator.getFileName();
			s1 = s1 + " ("
					+ GuiSelectWorld.this.field_146633_h.format(new Date(saveformatcomparator.getLastTimePlayed()));
			s1 = s1 + ")";
			String s2 = "";
			if (saveformatcomparator.requiresConversion()) {
				s2 = GuiSelectWorld.this.field_146636_v + " " + s2;
			} else {
				s2 = GuiSelectWorld.this.field_146635_w[saveformatcomparator.getEnumGameType().getID()];
				if (saveformatcomparator.isHardcoreModeEnabled()) {
					s2 = EnumChatFormatting.DARK_RED + I18n.format("gameMode.hardcore", new Object[0])
							+ EnumChatFormatting.RESET;
				}

				if (saveformatcomparator.getCheatsEnabled()) {
					s2 = s2 + ", " + I18n.format("selectWorld.cheats", new Object[0]);
				}
			}

			GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s, j + 2, k + 1, 16777215);
			GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s1, j + 2, k + 12, 8421504);
			GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s2, j + 2, k + 12 + 10, 8421504);
		}
	}
}