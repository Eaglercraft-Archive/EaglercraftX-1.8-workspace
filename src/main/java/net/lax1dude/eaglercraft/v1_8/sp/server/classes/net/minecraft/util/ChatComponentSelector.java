package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.ChatComponentStyle;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.IChatComponent;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

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
public class ChatComponentSelector extends ChatComponentStyle {

	static {
		__checkIntegratedContextValid("net/minecraft/util/ChatComponentSelector");
	}

	private final String selector;

	public ChatComponentSelector(String selectorIn) {
		this.selector = selectorIn;
	}

	/**+
	 * Gets the selector of this component, in plain text.
	 */
	public String getSelector() {
		return this.selector;
	}

	/**+
	 * Gets the text of this component, without any special
	 * formatting codes added, for chat. TODO: why is this two
	 * different methods?
	 */
	public String getUnformattedTextForChat() {
		return this.selector;
	}

	/**+
	 * Creates a copy of this component. Almost a deep copy, except
	 * the style is shallow-copied.
	 */
	public ChatComponentSelector createCopy() {
		ChatComponentSelector chatcomponentselector = new ChatComponentSelector(this.selector);
		chatcomponentselector.setChatStyle(this.getChatStyle().createShallowCopy());

		for (IChatComponent ichatcomponent : this.getSiblings()) {
			chatcomponentselector.appendSibling(ichatcomponent.createCopy());
		}

		return chatcomponentselector;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof ChatComponentSelector)) {
			return false;
		} else {
			ChatComponentSelector chatcomponentselector = (ChatComponentSelector) object;
			return this.selector.equals(chatcomponentselector.selector) && super.equals(object);
		}
	}

	public String toString() {
		return "SelectorComponent{pattern=\'" + this.selector + '\'' + ", siblings=" + this.siblings + ", style="
				+ this.getChatStyle() + '}';
	}
}