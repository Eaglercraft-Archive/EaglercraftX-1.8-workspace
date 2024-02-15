package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANInfo;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANNotSupported;
import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiShareToLan;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
import net.lax1dude.eaglercraft.v1_8.update.GuiUpdateCheckerOverlay;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class GuiIngameMenu extends GuiScreen {

	private GuiButton lanButton;

	boolean hasSentAutoSave = !SingleplayerServerController.isWorldRunning();

	private GuiUpdateCheckerOverlay updateCheckerOverlay;

	public GuiIngameMenu() {
		updateCheckerOverlay = new GuiUpdateCheckerOverlay(true, this);
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		this.buttonList.clear();
		this.updateCheckerOverlay.setResolution(mc, width, height);
		byte b0 = -16;
		boolean flag = true;
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + b0,
				I18n.format("menu.returnToMenu", new Object[0])));
		if (!this.mc.isIntegratedServerRunning()) {
			((GuiButton) this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]);
		}

		this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + b0,
				I18n.format("menu.returnToGame", new Object[0])));
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + b0, 98, 20,
				I18n.format("menu.options", new Object[0])));
		this.buttonList.add(lanButton = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + b0, 98, 20,
				I18n.format(LANServerController.isLANOpen() ? "menu.closeLan" : "menu.openToLan", new Object[0])));
		this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + b0, 98, 20,
				I18n.format("gui.achievements", new Object[0])));
		this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + b0, 98, 20,
				I18n.format("gui.stats", new Object[0])));
		lanButton.enabled = SingleplayerServerController.isWorldRunning();
		if (!hasSentAutoSave) {
			hasSentAutoSave = true;
			SingleplayerServerController.autoSave();
		}
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
		switch (parGuiButton.id) {
		case 0:
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
			break;
		case 1:
			boolean flag = this.mc.isIntegratedServerRunning() || this.mc.isDemo();
			parGuiButton.enabled = false;
			this.mc.theWorld.sendQuittingDisconnectingPacket();
			this.mc.loadWorld((WorldClient) null);
			if (flag) {
				this.mc.shutdownIntegratedServer(new GuiMainMenu());
			} else {
				this.mc.shutdownIntegratedServer(new GuiMultiplayer(new GuiMainMenu()));
			}
		case 2:
		case 3:
		default:
			break;
		case 4:
			this.mc.displayGuiScreen((GuiScreen) null);
			this.mc.setIngameFocus();
			break;
		case 5:
			this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
			break;
		case 6:
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
			break;
		case 7:
			if (!LANServerController.supported()) {
				mc.displayGuiScreen(new GuiScreenLANNotSupported(this));
			} else if (LANServerController.isLANOpen()) {
				if (LANServerController.hasPeers()) {
					mc.displayGuiScreen(new GuiYesNo(this, I18n.format("networkSettings.delete"),
							I18n.format("lanServer.wouldYouLikeToKick"), 0));
				} else {
					confirmClicked(false, 0);
				}
			} else {
				this.mc.displayGuiScreen(GuiScreenLANInfo.showLANInfoScreen(
						new GuiShareToLan(this, this.mc.playerController.getCurrentGameType().getName())));
			}
			break;
		}

	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();
		if (Mouse.isActuallyGrabbed()) {
			Mouse.setGrabbed(false);
		}
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), this.width / 2, 20,
				16777215);

		this.updateCheckerOverlay.drawScreen(i, j, f);

		if (LANServerController.isLANOpen()) {
			String str = I18n.format("lanServer.pauseMenu0");
			drawString(fontRendererObj, str, 6, 32, 0xFFFF55);

			if (mc.gameSettings.hideJoinCode) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(7.0f, 47.0f, 0.0f);
				GlStateManager.scale(0.75f, 0.75f, 0.75f);
				str = I18n.format("lanServer.showCode");
				int w = fontRendererObj.getStringWidth(str);
				boolean hover = i > 6 && i < 8 + w * 3 / 4 && j > 46 && j < 47 + 8;
				drawString(fontRendererObj, EnumChatFormatting.UNDERLINE + str, 0, 0, hover ? 0xEEEEAA : 0xCCCC55);
				GlStateManager.popMatrix();
			} else {
				int w = fontRendererObj.getStringWidth(str);
				GlStateManager.pushMatrix();
				GlStateManager.translate(6 + w + 3, 33, 0.0f);
				GlStateManager.scale(0.75f, 0.75f, 0.75f);
				str = I18n.format("lanServer.hideCode");
				int w2 = fontRendererObj.getStringWidth(str);
				boolean hover = i > 6 + w + 2 && i < 6 + w + 3 + w2 * 3 / 4 && j > 33 - 1 && j < 33 + 6;
				drawString(fontRendererObj, EnumChatFormatting.UNDERLINE + str, 0, 0, hover ? 0xEEEEAA : 0xCCCC55);
				GlStateManager.popMatrix();

				drawString(fontRendererObj, EnumChatFormatting.GRAY + I18n.format("lanServer.pauseMenu1") + " "
						+ EnumChatFormatting.RESET + LANServerController.getCurrentURI(), 6, 47, 0xFFFFFF);
				drawString(fontRendererObj, EnumChatFormatting.GRAY + I18n.format("lanServer.pauseMenu2") + " "
						+ EnumChatFormatting.RESET + LANServerController.getCurrentCode(), 6, 57, 0xFFFFFF);
			}
		}

		super.drawScreen(i, j, f);
	}

	public void confirmClicked(boolean par1, int par2) {
		mc.displayGuiScreen(this);
		LANServerController.closeLANNoKick();
		if (par1) {
			LANServerController.cleanupLAN();
			SingleplayerServerController.configureLAN(this.mc.theWorld.getWorldInfo().getGameType(), false);
		}
		this.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(I18n.format("lanServer.closed")));
		this.lanButton.displayString = I18n.format("menu.openToLan");
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 == 0) {
			if (mc.gameSettings.hideJoinCode) {
				String str = I18n.format("lanServer.showCode");
				int w = fontRendererObj.getStringWidth(str);
				if (par1 > 6 && par1 < 8 + w * 3 / 4 && par2 > 46 && par2 < 47 + 8) {
					mc.gameSettings.hideJoinCode = false;
					this.mc.getSoundHandler()
							.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
					mc.gameSettings.saveOptions();
				}
			} else {
				String str = I18n.format("lanServer.pauseMenu0");
				int w = fontRendererObj.getStringWidth(str);
				str = I18n.format("lanServer.hideCode");
				int w2 = fontRendererObj.getStringWidth(str);
				if (par1 > 6 + w + 2 && par1 < 6 + w + 3 + w2 * 3 / 4 && par2 > 33 - 1 && par2 < 33 + 6) {
					mc.gameSettings.hideJoinCode = true;
					this.mc.getSoundHandler()
							.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
					mc.gameSettings.saveOptions();
				}
			}

		}
		this.updateCheckerOverlay.mouseClicked(par1, par2, par3);
		super.mouseClicked(par1, par2, par3);
	}
}