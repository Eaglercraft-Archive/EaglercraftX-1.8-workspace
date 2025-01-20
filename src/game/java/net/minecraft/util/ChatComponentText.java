package net.minecraft.util;

import java.util.List;

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
public class ChatComponentText extends ChatComponentStyle {
	private final String text;

	public ChatComponentText(String msg) {
		this.text = msg;
	}

	/**+
	 * Gets the text value of this ChatComponentText. TODO: what are
	 * getUnformattedText and getUnformattedTextForChat missing that
	 * made someone decide to create a third equivalent method that
	 * only ChatComponentText can implement?
	 */
	public String getChatComponentText_TextValue() {
		return this.text;
	}

	/**+
	 * Gets the text of this component, without any special
	 * formatting codes added, for chat. TODO: why is this two
	 * different methods?
	 */
	public String getUnformattedTextForChat() {
		return this.text;
	}

	/**+
	 * Creates a copy of this component. Almost a deep copy, except
	 * the style is shallow-copied.
	 */
	public ChatComponentText createCopy() {
		ChatComponentText chatcomponenttext = new ChatComponentText(this.text);
		chatcomponenttext.setChatStyle(this.getChatStyle().createShallowCopy());

		List<IChatComponent> lst = this.getSiblings();
		for (int i = 0, l = lst.size(); i < l; ++i) {
			chatcomponenttext.appendSibling(lst.get(i).createCopy());
		}

		return chatcomponenttext;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof ChatComponentText)) {
			return false;
		} else {
			ChatComponentText chatcomponenttext = (ChatComponentText) object;
			return this.text.equals(chatcomponenttext.getChatComponentText_TextValue()) && super.equals(object);
		}
	}

	public String toString() {
		return "TextComponent{text=\'" + this.text + '\'' + ", siblings=" + this.siblings + ", style="
				+ this.getChatStyle() + '}';
	}
}