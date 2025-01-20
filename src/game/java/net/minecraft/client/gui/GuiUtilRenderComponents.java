package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

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
public class GuiUtilRenderComponents {
	public static String func_178909_a(String parString1, boolean parFlag) {
		return !parFlag && !Minecraft.getMinecraft().gameSettings.chatColours
				? EnumChatFormatting.getTextWithoutFormattingCodes(parString1)
				: parString1;
	}

	/**
	 * This function is like the FontRenderer wrap function, except for chat
	 * components
	 */
	public static List<IChatComponent> func_178908_a(IChatComponent parIChatComponent, int parInt1,
			FontRenderer parFontRenderer, boolean parFlag, boolean parFlag2) {
		int i = 0;
		ChatComponentText chatcomponenttext = new ChatComponentText("");
		ArrayList arraylist = Lists.newArrayList();
		ArrayList arraylist1 = Lists.newArrayList(parIChatComponent);

		for (int j = 0; j < arraylist1.size(); ++j) {
			IChatComponent ichatcomponent = (IChatComponent) arraylist1.get(j);
			String s = ichatcomponent.getUnformattedTextForChat();
			boolean flag = false;
			if (s.contains("\n")) {
				int k = s.indexOf(10);
				String s1 = s.substring(k + 1);
				s = s.substring(0, k + 1);
				ChatComponentText chatcomponenttext1 = new ChatComponentText(s1);
				chatcomponenttext1.setChatStyle(ichatcomponent.getChatStyle().createShallowCopy());
				arraylist1.add(j + 1, chatcomponenttext1);
				flag = true;
			}

			String s4 = func_178909_a(ichatcomponent.getChatStyle().getFormattingCode() + s, parFlag2);
			String s5 = s4.endsWith("\n") ? s4.substring(0, s4.length() - 1) : s4;
			int i1 = parFontRenderer.getStringWidth(s5);
			ChatComponentText chatcomponenttext2 = new ChatComponentText(s5);
			chatcomponenttext2.setChatStyle(ichatcomponent.getChatStyle().createShallowCopy());
			if (i + i1 > parInt1) {
				String s2 = parFontRenderer.trimStringToWidth(s4, parInt1 - i, false);
				String s3 = s2.length() < s4.length() ? s4.substring(s2.length()) : null;
				if (s3 != null && s3.length() > 0) {
					int l = s2.lastIndexOf(" ");
					if (l >= 0 && parFontRenderer.getStringWidth(s4.substring(0, l)) > 0) {
						s2 = s4.substring(0, l);
						if (parFlag) {
							++l;
						}

						s3 = s4.substring(l);
					} else if (i > 0 && !s4.contains(" ")) {
						s2 = "";
						s3 = s4;
					}

					ChatComponentText chatcomponenttext3 = new ChatComponentText(s3);
					chatcomponenttext3.setChatStyle(ichatcomponent.getChatStyle().createShallowCopy());
					arraylist1.add(j + 1, chatcomponenttext3);
				}

				i1 = parFontRenderer.getStringWidth(s2);
				chatcomponenttext2 = new ChatComponentText(s2);
				chatcomponenttext2.setChatStyle(ichatcomponent.getChatStyle().createShallowCopy());
				flag = true;
			}

			if (i + i1 <= parInt1) {
				i += i1;
				chatcomponenttext.appendSibling(chatcomponenttext2);
			} else {
				flag = true;
			}

			if (flag) {
				arraylist.add(chatcomponenttext);
				i = 0;
				chatcomponenttext = new ChatComponentText("");
			}
		}

		arraylist.add(chatcomponenttext);
		return arraylist;
	}
}