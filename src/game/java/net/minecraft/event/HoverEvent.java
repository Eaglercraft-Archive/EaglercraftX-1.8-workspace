package net.minecraft.event;

import java.util.Map;

import com.google.common.collect.Maps;

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
public class HoverEvent {
	private final HoverEvent.Action action;
	private final IChatComponent value;

	public HoverEvent(HoverEvent.Action actionIn, IChatComponent valueIn) {
		this.action = actionIn;
		this.value = valueIn;
	}

	/**+
	 * Gets the action to perform when this event is raised.
	 */
	public HoverEvent.Action getAction() {
		return this.action;
	}

	/**+
	 * Gets the value to perform the action on when this event is
	 * raised. For example, if the action is "show item", this would
	 * be the item to show.
	 */
	public IChatComponent getValue() {
		return this.value;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object != null && this.getClass() == object.getClass()) {
			HoverEvent hoverevent = (HoverEvent) object;
			if (this.action != hoverevent.action) {
				return false;
			} else {
				if (this.value != null) {
					if (!this.value.equals(hoverevent.value)) {
						return false;
					}
				} else if (hoverevent.value != null) {
					return false;
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public String toString() {
		return "HoverEvent{action=" + this.action + ", value=\'" + this.value + '\'' + '}';
	}

	public int hashCode() {
		int i = this.action.hashCode();
		i = 31 * i + (this.value != null ? this.value.hashCode() : 0);
		return i;
	}

	public static enum Action {
		SHOW_TEXT("show_text", true), SHOW_ACHIEVEMENT("show_achievement", true), SHOW_ITEM("show_item", true),
		SHOW_ENTITY("show_entity", true);

		private static final Map<String, HoverEvent.Action> nameMapping = Maps.newHashMap();
		private final boolean allowedInChat;
		private final String canonicalName;

		private Action(String canonicalNameIn, boolean allowedInChatIn) {
			this.canonicalName = canonicalNameIn;
			this.allowedInChat = allowedInChatIn;
		}

		public boolean shouldAllowInChat() {
			return this.allowedInChat;
		}

		public String getCanonicalName() {
			return this.canonicalName;
		}

		public static HoverEvent.Action getValueByCanonicalName(String canonicalNameIn) {
			return (HoverEvent.Action) nameMapping.get(canonicalNameIn);
		}

		static {
			HoverEvent.Action[] types = values();
			for (int i = 0; i < types.length; ++i) {
				nameMapping.put(types[i].getCanonicalName(), types[i]);
			}

		}
	}
}