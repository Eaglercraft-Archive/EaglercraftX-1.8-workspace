package net.minecraft.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglerXBungeeVersion;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

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
public abstract class GuiScreen extends Gui implements GuiYesNoCallback {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Set<String> PROTOCOLS = Sets.newHashSet(new String[] { "http", "https" });
	private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
	protected Minecraft mc;
	protected RenderItem itemRender;
	public int width;
	public int height;
	/**+
	 * A list of all the buttons in this container.
	 */
	protected List<GuiButton> buttonList = Lists.newArrayList();
	/**+
	 * A list of all the labels in this container.
	 */
	protected List<GuiLabel> labelList = Lists.newArrayList();
	public boolean allowUserInput;
	protected FontRenderer fontRendererObj;
	private GuiButton selectedButton;
	private int eventButton;
	private long lastMouseEvent;
	private int touchValue;
	private String clickedLinkURI;
	protected long showingCloseKey = 0;

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float var3) {
		for (int k = 0, l = this.buttonList.size(); k < l; ++k) {
			((GuiButton) this.buttonList.get(k)).drawButton(this.mc, i, j);
		}

		for (int l = 0, m = this.labelList.size(); l < m; ++l) {
			((GuiLabel) this.labelList.get(l)).drawLabel(this.mc, i, j);
		}

		long millis = System.currentTimeMillis();
		long closeKeyTimeout = millis - showingCloseKey;
		if (closeKeyTimeout < 3000l) {
			int alpha1 = 0xC0000000;
			int alpha2 = 0xFF000000;
			if (closeKeyTimeout > 2500l) {
				float f = (float) (3000l - closeKeyTimeout) * 0.002f;
				if (f < 0.03f)
					f = 0.03f;
				alpha1 = (int) (f * 192.0f) << 24;
				alpha2 = (int) (f * 255.0f) << 24;
			}
			String str;
			int k = getCloseKey();
			if (k == KeyboardConstants.KEY_GRAVE) {
				str = I18n.format("gui.exitKeyRetarded");
			} else {
				str = I18n.format("gui.exitKey", Keyboard.getKeyName(k));
			}
			int w = fontRendererObj.getStringWidth(str);
			int x = (width - w - 4) / 2;
			int y = 10;
			drawRect(x, y, x + w + 4, y + 12, alpha1);
			if (closeKeyTimeout > 2500l)
				GlStateManager.enableBlend();
			fontRendererObj.drawStringWithShadow(str, x + 2, y + 2, 0xFFAAAA | alpha2);
			if (closeKeyTimeout > 2500l)
				GlStateManager.disableBlend();
		}

	}

	protected int getCloseKey() {
		if (this instanceof GuiContainer) {
			return this.mc.gameSettings.keyBindInventory.getKeyCode();
		} else {
			return this.mc.gameSettings.keyBindClose.getKeyCode();
		}
	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
		if (((this.mc.theWorld == null || this.mc.thePlayer.getHealth() <= 0.0F) && parInt1 == 1)
				|| parInt1 == this.mc.gameSettings.keyBindClose.getKeyCode()
				|| (parInt1 == 1 && (this.mc.gameSettings.keyBindClose.getKeyCode() == 0 || this.mc.areKeysLocked()))) {
			this.mc.displayGuiScreen((GuiScreen) null);
			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
		} else if (parInt1 == 1) {
			showingCloseKey = System.currentTimeMillis();
		}
	}

	/**+
	 * Returns a string stored in the system clipboard.
	 */
	public static String getClipboardString() {
		return EagRuntime.getClipboard();
	}

	/**+
	 * Stores the given string in the system clipboard
	 */
	public static void setClipboardString(String copyText) {
		if (!StringUtils.isEmpty(copyText)) {
			EagRuntime.setClipboard(copyText);
		}
	}

	protected void renderToolTip(ItemStack itemstack, int i, int j) {
		List list = itemstack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

		for (int k = 0, l = list.size(); k < l; ++k) {
			if (k == 0) {
				list.set(k, itemstack.getRarity().rarityColor + (String) list.get(k));
			} else {
				list.set(k, EnumChatFormatting.GRAY + (String) list.get(k));
			}
		}

		this.drawHoveringText(list, i, j);
	}

	/**+
	 * Draws the text when mouse is over creative inventory tab.
	 * Params: current creative tab to be checked, current mouse x
	 * position, current mouse y position.
	 */
	protected void drawCreativeTabHoveringText(String s, int i, int j) {
		this.drawHoveringText(Arrays.asList(new String[] { s }), i, j);
	}

	/**+
	 * Draws a List of strings as a tooltip. Every entry is drawn on
	 * a seperate line.
	 */
	protected void drawHoveringText(List<String> list, int i, int j) {
		if (!list.isEmpty()) {
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int k = 0;

			for (int m = 0, n = list.size(); m < n; ++m) {
				int l = this.fontRendererObj.getStringWidth(list.get(m));
				if (l > k) {
					k = l;
				}
			}

			int j2 = i + 12;
			int k2 = j - 12;
			int i1 = 8;
			if (list.size() > 1) {
				i1 += 2 + (list.size() - 1) * 10;
			}

			if (j2 + k > this.width) {
				j2 -= 28 + k;
			}

			if (k2 + i1 + 6 > this.height) {
				k2 = this.height - i1 - 6;
			}

			this.zLevel = 300.0F;
			this.itemRender.zLevel = 300.0F;
			int j1 = -267386864;
			this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
			int k1 = 1347420415;
			int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
			this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

			for (int i2 = 0; i2 < list.size(); ++i2) {
				String s1 = (String) list.get(i2);
				if (s1.length() > 0) {
					this.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1);
				}
				if (i2 == 0) {
					k2 += 2;
				}

				k2 += 10;
			}

			this.zLevel = 0.0F;
			this.itemRender.zLevel = 0.0F;
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();
		}
	}

	/**+
	 * Draws the hover event specified by the given chat component
	 */
	protected void handleComponentHover(IChatComponent parIChatComponent, int parInt1, int parInt2) {
		if (parIChatComponent != null && parIChatComponent.getChatStyle().getChatHoverEvent() != null) {
			HoverEvent hoverevent = parIChatComponent.getChatStyle().getChatHoverEvent();
			if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
				ItemStack itemstack = null;

				try {
					NBTTagCompound nbttagcompound = JsonToNBT
							.getTagFromJson(hoverevent.getValue().getUnformattedText());
					if (nbttagcompound instanceof NBTTagCompound) {
						itemstack = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbttagcompound);
					}
				} catch (NBTException var11) {
					;
				}

				if (itemstack != null) {
					this.renderToolTip(itemstack, parInt1, parInt2);
				} else {
					this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", parInt1, parInt2);
				}
			} else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
				if (this.mc.gameSettings.advancedItemTooltips) {
					try {
						NBTTagCompound nbttagcompound2 = JsonToNBT
								.getTagFromJson(hoverevent.getValue().getUnformattedText());
						if (nbttagcompound2 instanceof NBTTagCompound) {
							ArrayList arraylist1 = Lists.newArrayList();
							NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttagcompound2;
							arraylist1.add(nbttagcompound1.getString("name"));
							if (nbttagcompound1.hasKey("type", 8)) {
								String s = nbttagcompound1.getString("type");
								arraylist1.add("Type: " + s + " (" + EntityList.getIDFromString(s) + ")");
							}

							arraylist1.add(nbttagcompound1.getString("id"));
							this.drawHoveringText(arraylist1, parInt1, parInt2);
						} else {
							this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", parInt1,
									parInt2);
						}
					} catch (NBTException var10) {
						this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", parInt1, parInt2);
					}
				}
			} else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
				this.drawHoveringText(NEWLINE_SPLITTER.splitToList(hoverevent.getValue().getFormattedText()), parInt1,
						parInt2);
			} else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
				StatBase statbase = StatList.getOneShotStat(hoverevent.getValue().getUnformattedText());
				if (statbase != null) {
					IChatComponent ichatcomponent = statbase.getStatName();
					ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
							"stats.tooltip.type." + (statbase.isAchievement() ? "achievement" : "statistic"),
							new Object[0]);
					chatcomponenttranslation.getChatStyle().setItalic(Boolean.valueOf(true));
					String s1 = statbase instanceof Achievement ? ((Achievement) statbase).getDescription() : null;
					ArrayList arraylist = Lists.newArrayList(new String[] { ichatcomponent.getFormattedText(),
							chatcomponenttranslation.getFormattedText() });
					if (s1 != null) {
						arraylist.addAll(this.fontRendererObj.listFormattedStringToWidth(s1, 150));
					}

					this.drawHoveringText(arraylist, parInt1, parInt2);
				} else {
					this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", parInt1,
							parInt2);
				}
			}

			GlStateManager.disableLighting();
		}
	}

	/**+
	 * Sets the text of the chat
	 */
	protected void setText(String var1, boolean var2) {
	}

	/**+
	 * Executes the click event specified by the given chat
	 * component
	 */
	protected boolean handleComponentClick(IChatComponent parIChatComponent) {
		if (parIChatComponent == null) {
			return false;
		} else {
			ClickEvent clickevent = parIChatComponent.getChatStyle().getChatClickEvent();
			if (isShiftKeyDown()) {
				if (parIChatComponent.getChatStyle().getInsertion() != null) {
					this.setText(parIChatComponent.getChatStyle().getInsertion(), false);
				}
			} else if (clickevent != null) {
				if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
					if (!this.mc.gameSettings.chatLinks) {
						return false;
					}
					String uri = clickevent.getValue();

					if (this.mc.gameSettings.chatLinksPrompt) {
						this.clickedLinkURI = uri;
						this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, clickevent.getValue(), 31102009, false));
					} else {
						this.openWebLink(uri);
					}
				} else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
					// rip
				} else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
					this.setText(clickevent.getValue(), true);
				} else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
					this.sendChatMessage(clickevent.getValue(), false);
				} else if (clickevent.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
					/*
					 * ChatUserInfo chatuserinfo =
					 * this.mc.getTwitchStream().func_152926_a(clickevent.getValue()); if
					 * (chatuserinfo != null) { this.mc.displayGuiScreen(new
					 * GuiTwitchUserMode(this.mc.getTwitchStream(), chatuserinfo)); } else { }
					 */
					LOGGER.error("Tried to handle twitch user but couldn\'t find them!");
				} else if (clickevent.getAction() == ClickEvent.Action.EAGLER_PLUGIN_DOWNLOAD) {
					if (EaglerXBungeeVersion.pluginFileEPK.equals(clickevent.getValue())) {
						EaglerXBungeeVersion.startPluginDownload();
					} else {
						LOGGER.error("Invalid plugin download from EPK was blocked: {}",
								EaglerXBungeeVersion.pluginFileEPK);
					}
				} else {
					LOGGER.error("Don\'t know how to handle " + clickevent);
				}

				return true;
			}

			return false;
		}
	}

	public void sendChatMessage(String msg) {
		this.sendChatMessage(msg, true);
	}

	public void sendChatMessage(String msg, boolean addToChat) {
		if (addToChat) {
			this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
		}

		this.mc.thePlayer.sendChatMessage(msg);
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		if (parInt3 == 0) {
			for (int i = 0; i < this.buttonList.size(); ++i) {
				GuiButton guibutton = (GuiButton) this.buttonList.get(i);
				if (guibutton.mousePressed(this.mc, parInt1, parInt2)) {
					this.selectedButton = guibutton;
					guibutton.playPressSound(this.mc.getSoundHandler());
					this.actionPerformed(guibutton);
				}
			}
		}

	}

	/**+
	 * Called when a mouse button is released. Args : mouseX,
	 * mouseY, releaseButton
	 */
	protected void mouseReleased(int i, int j, int k) {
		if (this.selectedButton != null && k == 0) {
			this.selectedButton.mouseReleased(i, j);
			this.selectedButton = null;
		}

	}

	/**+
	 * Called when a mouse button is pressed and the mouse is moved
	 * around. Parameters are : mouseX, mouseY, lastButtonClicked &
	 * timeSinceMouseClick.
	 */
	protected void mouseClickMove(int var1, int var2, int var3, long var4) {
	}

	/**+
	 * Called by the controls from the buttonList when activated.
	 * (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton parGuiButton) {
	}

	/**+
	 * Causes the screen to lay out its subcomponents again. This is
	 * the equivalent of the Java call Container.validate()
	 */
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		this.mc = mc;
		this.itemRender = mc.getRenderItem();
		this.fontRendererObj = mc.fontRendererObj;
		this.width = width;
		this.height = height;
		this.buttonList.clear();
		this.initGui();
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
	}

	/**+
	 * Delegates mouse and keyboard input.
	 */
	public void handleInput() throws IOException {
		if (Mouse.isCreated()) {
			while (Mouse.next()) {
				this.handleMouseInput();
			}
		}

		if (Keyboard.isCreated()) {
			while (Keyboard.next()) {
				this.handleKeyboardInput();
			}
		}

	}

	/**+
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		int k = Mouse.getEventButton();
		if (Mouse.getEventButtonState()) {
			if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
				return;
			}

			this.eventButton = k;
			this.lastMouseEvent = Minecraft.getSystemTime();
			this.mouseClicked(i, j, this.eventButton);
		} else if (k != -1) {
			if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
				return;
			}

			this.eventButton = -1;
			this.mouseReleased(i, j, k);
		} else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
			long l = Minecraft.getSystemTime() - this.lastMouseEvent;
			this.mouseClickMove(i, j, this.eventButton, l);
		}

	}

	/**+
	 * Handles keyboard input.
	 */
	public void handleKeyboardInput() throws IOException {
		if (Keyboard.getEventKeyState()) {
			this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

		this.mc.dispatchKeypresses();
	}

	/**+
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
	}

	/**+
	 * Called when the screen is unloaded. Used to disable keyboard
	 * repeat events
	 */
	public void onGuiClosed() {
	}

	/**+
	 * Draws either a gradient over the background screen (when it
	 * exists) or a flat gradient over background.png
	 */
	public void drawDefaultBackground() {
		this.drawWorldBackground(0);
	}

	public void drawWorldBackground(int i) {
		if (this.mc.theWorld != null) {
			this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
		} else {
			this.drawBackground(i);
		}

	}

	/**+
	 * Draws the background (i is always 0 as of 1.2.2)
	 */
	public void drawBackground(int tint) {
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		this.mc.getTextureManager().bindTexture(optionsBackground);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, (double) this.height, 0.0D)
				.tex(0.0D, (double) ((float) this.height / 32.0F + (float) tint)).color(64, 64, 64, 255).endVertex();
		worldrenderer.pos((double) this.width, (double) this.height, 0.0D)
				.tex((double) ((float) this.width / 32.0F), (double) ((float) this.height / 32.0F + (float) tint))
				.color(64, 64, 64, 255).endVertex();
		worldrenderer.pos((double) this.width, 0.0D, 0.0D).tex((double) ((float) this.width / 32.0F), (double) tint)
				.color(64, 64, 64, 255).endVertex();
		worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double) tint).color(64, 64, 64, 255).endVertex();
		tessellator.draw();
	}

	/**+
	 * Returns true if this GUI should pause the game when it is
	 * displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return true;
	}

	public void confirmClicked(boolean flag, int i) {
		if (i == 31102009) {
			if (flag) {
				this.openWebLink(this.clickedLinkURI);
			}

			this.clickedLinkURI = null;
			this.mc.displayGuiScreen(this);
		}

	}

	private void openWebLink(String parURI) {
		EagRuntime.openLink(parURI);
	}

	/**+
	 * Returns true if either windows ctrl key is down or if either
	 * mac meta key is down
	 */
	public static boolean isCtrlKeyDown() {
		return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220)
				: Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
	}

	/**+
	 * Returns true if either shift key is down
	 */
	public static boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
	}

	/**+
	 * Returns true if either alt key is down
	 */
	public static boolean isAltKeyDown() {
		return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
	}

	public static boolean isKeyComboCtrlX(int parInt1) {
		return parInt1 == 45 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
	}

	public static boolean isKeyComboCtrlV(int parInt1) {
		return parInt1 == 47 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
	}

	public static boolean isKeyComboCtrlC(int parInt1) {
		return parInt1 == 46 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
	}

	public static boolean isKeyComboCtrlA(int parInt1) {
		return parInt1 == 30 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
	}

	/**+
	 * Called when the GUI is resized in order to update the world
	 * and the resolution
	 */
	public void onResize(Minecraft mcIn, int parInt1, int parInt2) {
		this.setWorldAndResolution(mcIn, parInt1, parInt2);
	}

	public boolean shouldHangupIntegratedServer() {
		return true;
	}
}