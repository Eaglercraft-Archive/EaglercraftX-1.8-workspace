package net.minecraft.event;

import java.util.Map;

import com.google.common.collect.Maps;

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
public class ClickEvent {
	private final ClickEvent.Action action;
	private final String value;

	public ClickEvent(ClickEvent.Action theAction, String theValue) {
		this.action = theAction;
		this.value = theValue;
	}

	/**+
	 * Gets the action to perform when this event is raised.
	 */
	public ClickEvent.Action getAction() {
		return this.action;
	}

	/**+
	 * Gets the value to perform the action on when this event is
	 * raised. For example, if the action is "open URL", this would
	 * be the URL to open.
	 */
	public String getValue() {
		return this.value;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object != null && this.getClass() == object.getClass()) {
			ClickEvent clickevent = (ClickEvent) object;
			if (this.action != clickevent.action) {
				return false;
			} else {
				if (this.value != null) {
					if (!this.value.equals(clickevent.value)) {
						return false;
					}
				} else if (clickevent.value != null) {
					return false;
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public String toString() {
		return "ClickEvent{action=" + this.action + ", value=\'" + this.value + '\'' + '}';
	}

	public int hashCode() {
		int i = this.action.hashCode();
		i = 31 * i + (this.value != null ? this.value.hashCode() : 0);
		return i;
	}

	public static enum Action {
		OPEN_URL("open_url", true), OPEN_FILE("open_file", false), RUN_COMMAND("run_command", true),
		TWITCH_USER_INFO("twitch_user_info", false), SUGGEST_COMMAND("suggest_command", true),
		CHANGE_PAGE("change_page", true);

		private static final Map<String, ClickEvent.Action> nameMapping = Maps.newHashMap();
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

		public static ClickEvent.Action getValueByCanonicalName(String canonicalNameIn) {
			return (ClickEvent.Action) nameMapping.get(canonicalNameIn);
		}

		static {
			ClickEvent.Action[] types = values();
			for (int i = 0; i < types.length; ++i) {
				nameMapping.put(types[i].getCanonicalName(), types[i]);
			}

		}
	}
}