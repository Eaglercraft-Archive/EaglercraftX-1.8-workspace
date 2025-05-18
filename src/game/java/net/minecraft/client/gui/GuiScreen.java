package net.minecraft.client.gui;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.internal.EnumTouchEvent;
import org.apache.commons.lang3.StringUtils;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.PauseMenuCustomizeState;
import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.lax1dude.eaglercraft.v1_8.Touch;
import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.touch_gui.TouchControls;
import net.lax1dude.eaglercraft.v1_8.webview.GuiScreenServerInfo;
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
	protected GuiButton selectedButton;
	private int eventButton;
	private long lastMouseEvent;
	private int touchValue;
	private String clickedLinkURI;
	protected long showingCloseKey = 0;

	protected int touchModeCursorPosX = -1;
	protected int touchModeCursorPosY = -1;
	private long lastTouchEvent;

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

		long millis = EagRuntime.steadyTimeMillis();
		long closeKeyTimeout = millis - showingCloseKey;
		if (closeKeyTimeout < 3000l && showingCloseKey != 0l) {
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
		if (!canCloseGui())
			return;
		if (((this.mc.theWorld == null || this.mc.thePlayer.getHealth() <= 0.0F) && parInt1 == 1)
				|| parInt1 == this.mc.gameSettings.keyBindClose.getKeyCode() || (parInt1 == 1
						&& (this.mc.gameSettings.keyBindClose.getKeyCode() == 0 || Keyboard.areKeysLocked()))) {
			this.mc.displayGuiScreen((GuiScreen) null);
			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
		} else if (parInt1 == 1) {
			showingCloseKey = EagRuntime.steadyTimeMillis();
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
		renderToolTip0(itemstack, i, j, false);
	}

	protected void renderToolTip0(ItemStack itemstack, int i, int j, boolean eagler) {
		List list = itemstack.getTooltipProfanityFilter(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

		for (int k = 0, l = list.size(); k < l; ++k) {
			if (k == 0) {
				list.set(k, itemstack.getRarity().rarityColor + (String) list.get(k));
			} else {
				list.set(k, EnumChatFormatting.GRAY + (String) list.get(k));
			}
		}

		this.drawHoveringText0(list, i, j, eagler);
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
		drawHoveringText0(list, i, j, false);
	}

	protected void drawHoveringText0(List<String> list, int i, int j, boolean eagler) {
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

			int j2 = i;
			int k2 = j;
			int i1 = 8;
			if (list.size() > 1) {
				i1 += 2 + (list.size() - 1) * 10;
			}

			if (!eagler) {
				j2 += 12;
				k2 -= 12;

				if (j2 + k > this.width) {
					j2 -= 28 + k;
				}

				if (k2 + i1 + 6 > this.height) {
					k2 = this.height - i1 - 6;
				}
			} else {
				j2 -= (k + 3) >> 1;
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
	public void handleComponentHover(IChatComponent parIChatComponent, int parInt1, int parInt2) {
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
	public boolean handleComponentClick(IChatComponent parIChatComponent) {
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

	protected void touchStarted(int parInt1, int parInt2, int parInt3) {
		if (shouldTouchGenerateMouseEvents()) {
			this.mouseClicked(parInt1, parInt2, 12345);
		}
	}

	protected void touchTapped(int parInt1, int parInt2, int parInt3) {
		if (shouldTouchGenerateMouseEvents()) {
			this.mouseClicked(parInt1, parInt2, 0);
			this.mouseReleased(parInt1, parInt2, 0);
		}
	}

	protected void touchMoved(int parInt1, int parInt2, int parInt3) {
	}

	protected void touchEndMove(int parInt1, int parInt2, int parInt3) {
		if (shouldTouchGenerateMouseEvents()) {
			this.mouseReleased(parInt1, parInt2, 12345);
		}
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		boolean touchMode = PointerInputAbstraction.isTouchMode();
		if (parInt3 == 0 || parInt3 == 12345) {
			for (int i = 0; i < this.buttonList.size(); ++i) {
				GuiButton guibutton = (GuiButton) this.buttonList.get(i);
				if (touchMode && (parInt3 == 12345) != guibutton.isSliderTouchEvents())
					continue;
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
		if (this.selectedButton != null && (k == 0 || k == 12345)
				&& (!PointerInputAbstraction.isTouchMode() || (k == 12345) == selectedButton.isSliderTouchEvents())) {
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
		boolean noTouch = true;
		while (Touch.next()) {
			noTouch = false;
			this.handleTouchInput();
			TouchControls.handleInput();
		}

		if (Mouse.isCreated()) {
			while (Mouse.next()) {
				if (noTouch) {
					this.handleMouseInput();
				}
			}
		}

		if (Keyboard.isCreated()) {
			while (Keyboard.next()) {
				this.handleKeyboardInput();
			}
		}

	}

	public final IntObjectMap<int[]> touchStarts = new IntObjectHashMap<>();

	/**
	 * Handles touch input.
	 */
	public void handleTouchInput() throws IOException {
		EnumTouchEvent et = Touch.getEventType();
		if (et == EnumTouchEvent.TOUCHSTART) {
			PointerInputAbstraction.enterTouchModeHook();
		}
		float scaleFac = getEaglerScale();
		for (int t = 0, c = Touch.getEventTouchPointCount(); t < c; ++t) {
			int u = Touch.getEventTouchPointUID(t);
			int i = Touch.getEventTouchX(t);
			int j = Touch.getEventTouchY(t);
			if (et == EnumTouchEvent.TOUCHSTART) {
				if (TouchControls.handleTouchBegin(u, i, j)) {
					continue;
				}
			} else if (et == EnumTouchEvent.TOUCHEND) {
				if (TouchControls.handleTouchEnd(u, i, j)) {
					continue;
				}
			}
			i = applyEaglerScale(scaleFac, i * this.width / this.mc.displayWidth, this.width);
			j = applyEaglerScale(scaleFac, this.height - j * this.height / this.mc.displayHeight - 1, this.height);
			float rad = Touch.getEventTouchRadiusMixed(t);
			float si = rad * this.width / this.mc.displayWidth / scaleFac;
			if (si < 1.0f)
				si = 1.0f;
			float sj = rad * this.height / this.mc.displayHeight / scaleFac;
			if (sj < 1.0f)
				sj = 1.0f;
			int[] ck = touchStarts.remove(u);
			switch (et) {
			case TOUCHSTART:
				if (t == 0) {
					touchModeCursorPosX = i;
					touchModeCursorPosY = j;
				}
				lastTouchEvent = EagRuntime.steadyTimeMillis();
				touchStarts.put(u, new int[] { i, j, 0 });
				this.touchStarted(i, j, u);
				break;
			case TOUCHMOVE:
				if (t == 0) {
					touchModeCursorPosX = i;
					touchModeCursorPosY = j;
				}
				if (ck != null && Math.abs(ck[0] - i) < si && Math.abs(ck[1] - j) < sj) {
					touchStarts.put(u, ck);
					break;
				}
				touchStarts.put(u, new int[] { i, j, (ck != null && isTouchDraggingStateLocked(u)) ? ck[2] : 1 });
				this.touchMoved(i, j, u);
				if (t == 0 && shouldTouchGenerateMouseEvents()) {
					this.mouseClickMove(i, j, 0, EagRuntime.steadyTimeMillis() - lastTouchEvent);
				}
				break;
			case TOUCHEND:
				if (ck == null)
					break;
				if (t == 0) {
					touchModeCursorPosX = -1;
					touchModeCursorPosY = -1;
				}
				if (ck != null && ck[2] == 1) {
					this.touchEndMove(i, j, u);
				} else {
					if (ck != null) {
						i = ck[0];
						j = ck[1];
					}
					this.touchTapped(i, j, u);
				}
				break;
			}
		}
	}

	public boolean isTouchPointDragging(int uid) {
		int[] ret = touchStarts.get(uid);
		return ret != null && ret[2] == 1;
	}

	/**+
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		float f = getEaglerScale();
		int i = applyEaglerScale(f, Mouse.getEventX() * this.width / this.mc.displayWidth, this.width);
		int j = applyEaglerScale(f, this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1,
				this.height);
		int k = Mouse.getEventButton();
		if (Mouse.getEventButtonState()) {
			PointerInputAbstraction.enterMouseModeHook();
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

	protected boolean isPartOfPauseMenu() {
		return false;
	}

	public void drawWorldBackground(int i) {
		if (this.mc.theWorld != null) {
			boolean ingame = isPartOfPauseMenu();
			ResourceLocation loc = (ingame && PauseMenuCustomizeState.icon_background_pause != null)
					? PauseMenuCustomizeState.icon_background_pause
					: PauseMenuCustomizeState.icon_background_all;
			float aspect = (ingame && PauseMenuCustomizeState.icon_background_pause != null)
					? 1.0f / PauseMenuCustomizeState.icon_background_pause_aspect
					: 1.0f / PauseMenuCustomizeState.icon_background_all_aspect;
			if (loc != null) {
				GlStateManager.disableLighting();
				GlStateManager.disableFog();
				GlStateManager.enableBlend();
				GlStateManager.disableAlpha();
				GlStateManager.enableTexture2D();
				GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				Tessellator tessellator = Tessellator.getInstance();
				WorldRenderer worldrenderer = tessellator.getWorldRenderer();
				this.mc.getTextureManager().bindTexture(loc);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				float f = 64.0F;
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				worldrenderer.pos(0.0D, (double) this.height, 0.0D).tex(0.0D, (double) ((float) this.height / f))
						.color(64, 64, 64, 192).endVertex();
				worldrenderer.pos((double) this.width, (double) this.height, 0.0D)
						.tex((double) ((float) this.width / f * aspect), (double) ((float) this.height / f))
						.color(64, 64, 64, 192).endVertex();
				worldrenderer.pos((double) this.width, 0.0D, 0.0D)
						.tex((double) ((float) this.width / f * aspect), (double) 0).color(64, 64, 64, 192).endVertex();
				worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double) 0).color(64, 64, 64, 192).endVertex();
				tessellator.draw();
				GlStateManager.enableAlpha();
			} else {
				this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
			}
			if (!(this instanceof GuiScreenServerInfo)) {
				loc = (ingame && PauseMenuCustomizeState.icon_watermark_pause != null)
						? PauseMenuCustomizeState.icon_watermark_pause
						: PauseMenuCustomizeState.icon_watermark_all;
				aspect = (ingame && PauseMenuCustomizeState.icon_watermark_pause != null)
						? PauseMenuCustomizeState.icon_watermark_pause_aspect
						: PauseMenuCustomizeState.icon_watermark_all_aspect;
				if (loc != null) {
					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
					mc.getTextureManager().bindTexture(loc);
					GlStateManager.pushMatrix();
					GlStateManager.translate(8, height - 72, 0.0f);
					float f2 = 64.0f / 256.0f;
					GlStateManager.scale(f2 * aspect, f2, f2);
					this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
					GlStateManager.popMatrix();
				}
			}
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

	public boolean blockPTTKey() {
		return false;
	}

	public void fireInputEvent(EnumInputEvent event, String param) {

	}

	public boolean showCopyPasteButtons() {
		return false;
	}

	public static int applyEaglerScale(float scaleFac, int coord, int screenDim) {
		return (int) ((coord - (1.0f - scaleFac) * screenDim * 0.5f) / scaleFac);
	}

	public float getEaglerScale() {
		return PointerInputAbstraction.isTouchMode() ? getTouchModeScale() : 1.0f;
	}

	protected float getTouchModeScale() {
		return 1.0f;
	}

	public boolean canCloseGui() {
		return true;
	}

	protected boolean isTouchDraggingStateLocked(int uid) {
		return false;
	}

	protected boolean shouldTouchGenerateMouseEvents() {
		return true;
	}

}