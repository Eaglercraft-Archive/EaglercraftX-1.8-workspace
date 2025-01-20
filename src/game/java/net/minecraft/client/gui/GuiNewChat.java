package net.minecraft.client.gui;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
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
public class GuiNewChat extends Gui {
	private static final Logger logger = LogManager.getLogger();
	private final Minecraft mc;
	/**+
	 * A list of messages previously sent through the chat GUI
	 */
	private final List<String> sentMessages = Lists.newArrayList();
	/**+
	 * Chat lines to be displayed in the chat box
	 */
	private final List<ChatLine> chatLines = Lists.newArrayList();
	private final List<ChatLine> field_146253_i = Lists.newArrayList();
	private int scrollPos;
	private boolean isScrolled;

	public GuiNewChat(Minecraft mcIn) {
		this.mc = mcIn;
	}

	public void drawChat(int parInt1) {
		if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
			int i = this.getLineCount();
			boolean flag = false;
			int j = 0;
			int k = this.field_146253_i.size();
			float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
			if (k > 0) {
				if (this.getChatOpen()) {
					flag = true;
				}

				float f1 = this.getChatScale();
				int l = MathHelper.ceiling_float_int((float) this.getChatWidth() / f1);
				GlStateManager.pushMatrix();
				GlStateManager.translate(2.0F, 20.0F, 0.0F);
				GlStateManager.scale(f1, f1, 1.0F);

				for (int i1 = 0; i1 + this.scrollPos < this.field_146253_i.size() && i1 < i; ++i1) {
					ChatLine chatline = (ChatLine) this.field_146253_i.get(i1 + this.scrollPos);
					if (chatline != null) {
						int j1 = parInt1 - chatline.getUpdatedCounter();
						if (j1 < 200 || flag) {
							double d0 = (double) j1 / 200.0D;
							d0 = 1.0D - d0;
							d0 = d0 * 10.0D;
							d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
							d0 = d0 * d0;
							int l1 = (int) (255.0D * d0);
							if (flag) {
								l1 = 255;
							}

							l1 = (int) ((float) l1 * f);
							++j;
							if (l1 > 3) {
								byte b0 = 0;
								int i2 = -i1 * 9;
								drawRect(b0, i2 - 9, b0 + l + 4, i2, l1 / 2 << 24);
								String s = chatline.getChatComponent().getFormattedText();
								GlStateManager.enableBlend();
								this.mc.fontRendererObj.drawStringWithShadow(s, (float) b0, (float) (i2 - 8),
										16777215 + (l1 << 24));
								GlStateManager.disableAlpha();
								GlStateManager.disableBlend();
							}
						}
					}
				}

				if (flag) {
					int j2 = this.mc.fontRendererObj.FONT_HEIGHT;
					GlStateManager.translate(-3.0F, 0.0F, 0.0F);
					int k2 = k * j2 + k;
					int l2 = j * j2 + j;
					int i3 = this.scrollPos * l2 / k;
					int k1 = l2 * l2 / k2;
					if (k2 != l2) {
						int j3 = i3 > 0 ? 170 : 96;
						int k3 = this.isScrolled ? 13382451 : 3355562;
						drawRect(0, -i3, 2, -i3 - k1, k3 + (j3 << 24));
						drawRect(2, -i3, 1, -i3 - k1, 13421772 + (j3 << 24));
					}
				}

				GlStateManager.popMatrix();
			}
		}
	}

	/**+
	 * Clears the chat.
	 */
	public void clearChatMessages() {
		this.field_146253_i.clear();
		this.chatLines.clear();
		this.sentMessages.clear();
	}

	public void printChatMessage(IChatComponent parIChatComponent) {
		this.printChatMessageWithOptionalDeletion(parIChatComponent, 0);
	}

	/**+
	 * prints the ChatComponent to Chat. If the ID is not 0, deletes
	 * an existing Chat Line of that ID from the GUI
	 */
	public void printChatMessageWithOptionalDeletion(IChatComponent parIChatComponent, int parInt1) {
		this.setChatLine(parIChatComponent, parInt1, this.mc.ingameGUI.getUpdateCounter(), false);
		logger.info("[CHAT] " + parIChatComponent.getUnformattedText());
	}

	private void setChatLine(IChatComponent parIChatComponent, int parInt1, int parInt2, boolean parFlag) {
		if (parInt1 != 0) {
			this.deleteChatLine(parInt1);
		}

		int i = MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale());
		List list = GuiUtilRenderComponents.func_178908_a(parIChatComponent, i, this.mc.fontRendererObj, false, false);
		boolean flag = this.getChatOpen();

		for (int j = 0, l = list.size(); j < l; ++j) {
			if (flag && this.scrollPos > 0) {
				this.isScrolled = true;
				this.scroll(1);
			}

			this.field_146253_i.add(0, new ChatLine(parInt2, (IChatComponent) list.get(j), parInt1));
		}

		while (this.field_146253_i.size() > 100) {
			this.field_146253_i.remove(this.field_146253_i.size() - 1);
		}

		if (!parFlag) {
			this.chatLines.add(0, new ChatLine(parInt2, parIChatComponent, parInt1));

			while (this.chatLines.size() > 100) {
				this.chatLines.remove(this.chatLines.size() - 1);
			}
		}

	}

	public void refreshChat() {
		this.field_146253_i.clear();
		this.resetScroll();

		for (int i = this.chatLines.size() - 1; i >= 0; --i) {
			ChatLine chatline = (ChatLine) this.chatLines.get(i);
			this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
		}

	}

	/**+
	 * Gets the list of messages previously sent through the chat
	 * GUI
	 */
	public List<String> getSentMessages() {
		return this.sentMessages;
	}

	/**+
	 * Adds this string to the list of sent messages, for recall
	 * using the up/down arrow keys
	 */
	public void addToSentMessages(String parString1) {
		if (this.sentMessages.isEmpty()
				|| !((String) this.sentMessages.get(this.sentMessages.size() - 1)).equals(parString1)) {
			this.sentMessages.add(parString1);
		}

	}

	/**+
	 * Resets the chat scroll (executed when the GUI is closed,
	 * among others)
	 */
	public void resetScroll() {
		this.scrollPos = 0;
		this.isScrolled = false;
	}

	/**+
	 * Scrolls the chat by the given number of lines.
	 */
	public void scroll(int parInt1) {
		this.scrollPos += parInt1;
		int i = this.field_146253_i.size();
		if (this.scrollPos > i - this.getLineCount()) {
			this.scrollPos = i - this.getLineCount();
		}

		if (this.scrollPos <= 0) {
			this.scrollPos = 0;
			this.isScrolled = false;
		}

	}

	/**+
	 * Gets the chat component under the mouse
	 */
	public IChatComponent getChatComponent(int parInt1, int parInt2) {
		if (!this.getChatOpen()) {
			return null;
		} else {
			ScaledResolution scaledresolution = mc.scaledResolution;
			int i = scaledresolution.getScaleFactor();
			float f = this.getChatScale();
			int j = parInt1 / i - 3;
			int k = parInt2 / i - 27;
			j = MathHelper.floor_float((float) j / f);
			k = MathHelper.floor_float((float) k / f);
			if (j >= 0 && k >= 0) {
				int l = Math.min(this.getLineCount(), this.field_146253_i.size());
				if (j <= MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale())
						&& k < this.mc.fontRendererObj.FONT_HEIGHT * l + l) {
					int i1 = k / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
					if (i1 >= 0 && i1 < this.field_146253_i.size()) {
						ChatLine chatline = (ChatLine) this.field_146253_i.get(i1);
						int j1 = 0;

						for (IChatComponent ichatcomponent : chatline.getChatComponent()) {
							if (ichatcomponent instanceof ChatComponentText) {
								j1 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(
										((ChatComponentText) ichatcomponent).getChatComponentText_TextValue(), false));
								if (j1 > j) {
									return ichatcomponent;
								}
							}
						}
					}

					return null;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	/**+
	 * Returns true if the chat GUI is open
	 */
	public boolean getChatOpen() {
		return this.mc.currentScreen instanceof GuiChat;
	}

	/**+
	 * finds and deletes a Chat line by ID
	 */
	public void deleteChatLine(int parInt1) {
		Iterator iterator = this.field_146253_i.iterator();

		while (iterator.hasNext()) {
			ChatLine chatline = (ChatLine) iterator.next();
			if (chatline.getChatLineID() == parInt1) {
				iterator.remove();
			}
		}

		iterator = this.chatLines.iterator();

		while (iterator.hasNext()) {
			ChatLine chatline1 = (ChatLine) iterator.next();
			if (chatline1.getChatLineID() == parInt1) {
				iterator.remove();
				break;
			}
		}

	}

	public int getChatWidth() {
		return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
	}

	public int getChatHeight() {
		return calculateChatboxHeight(
				this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
	}

	/**+
	 * Returns the chatscale from mc.gameSettings.chatScale
	 */
	public float getChatScale() {
		return this.mc.gameSettings.chatScale;
	}

	public static int calculateChatboxWidth(float parFloat1) {
		short short1 = 320;
		byte b0 = 40;
		return MathHelper.floor_float(parFloat1 * (float) (short1 - b0) + (float) b0);
	}

	public static int calculateChatboxHeight(float parFloat1) {
		short short1 = 180;
		byte b0 = 20;
		return MathHelper.floor_float(parFloat1 * (float) (short1 - b0) + (float) b0);
	}

	public int getLineCount() {
		return this.getChatHeight() / 9;
	}
}