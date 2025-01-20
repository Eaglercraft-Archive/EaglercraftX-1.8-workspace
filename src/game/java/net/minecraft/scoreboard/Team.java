package net.minecraft.scoreboard;

import java.util.Collection;
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
public abstract class Team {
	/**+
	 * Same as ==
	 */
	public boolean isSameTeam(Team other) {
		return other == null ? false : this == other;
	}

	public abstract String getRegisteredName();

	public abstract String formatString(String var1);

	public abstract boolean getSeeFriendlyInvisiblesEnabled();

	public abstract boolean getAllowFriendlyFire();

	public abstract Team.EnumVisible getNameTagVisibility();

	public abstract Collection<String> getMembershipCollection();

	public abstract Team.EnumVisible getDeathMessageVisibility();

	public static enum EnumVisible {
		ALWAYS("always", 0), NEVER("never", 1), HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
		HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);

		private static Map<String, Team.EnumVisible> field_178828_g = Maps.newHashMap();
		public final String field_178830_e;
		public final int field_178827_f;

		public static String[] func_178825_a() {
			return (String[]) field_178828_g.keySet().toArray(new String[field_178828_g.size()]);
		}

		public static Team.EnumVisible func_178824_a(String parString1) {
			return (Team.EnumVisible) field_178828_g.get(parString1);
		}

		private EnumVisible(String parString2, int parInt2) {
			this.field_178830_e = parString2;
			this.field_178827_f = parInt2;
		}

		static {
			Team.EnumVisible[] types = values();
			for (int i = 0; i < types.length; ++i) {
				field_178828_g.put(types[i].field_178830_e, types[i]);
			}

		}
	}
}