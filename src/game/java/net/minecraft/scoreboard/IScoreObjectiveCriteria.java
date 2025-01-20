package net.minecraft.scoreboard;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

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
public interface IScoreObjectiveCriteria {
	Map<String, IScoreObjectiveCriteria> INSTANCES = Maps.newHashMap();
	IScoreObjectiveCriteria DUMMY = new ScoreDummyCriteria("dummy");
	IScoreObjectiveCriteria TRIGGER = new ScoreDummyCriteria("trigger");
	IScoreObjectiveCriteria deathCount = new ScoreDummyCriteria("deathCount");
	IScoreObjectiveCriteria playerKillCount = new ScoreDummyCriteria("playerKillCount");
	IScoreObjectiveCriteria totalKillCount = new ScoreDummyCriteria("totalKillCount");
	IScoreObjectiveCriteria health = new ScoreHealthCriteria("health");
	IScoreObjectiveCriteria[] field_178792_h = new IScoreObjectiveCriteria[] {
			new GoalColor("teamkill.", EnumChatFormatting.BLACK),
			new GoalColor("teamkill.", EnumChatFormatting.DARK_BLUE),
			new GoalColor("teamkill.", EnumChatFormatting.DARK_GREEN),
			new GoalColor("teamkill.", EnumChatFormatting.DARK_AQUA),
			new GoalColor("teamkill.", EnumChatFormatting.DARK_RED),
			new GoalColor("teamkill.", EnumChatFormatting.DARK_PURPLE),
			new GoalColor("teamkill.", EnumChatFormatting.GOLD), new GoalColor("teamkill.", EnumChatFormatting.GRAY),
			new GoalColor("teamkill.", EnumChatFormatting.DARK_GRAY),
			new GoalColor("teamkill.", EnumChatFormatting.BLUE), new GoalColor("teamkill.", EnumChatFormatting.GREEN),
			new GoalColor("teamkill.", EnumChatFormatting.AQUA), new GoalColor("teamkill.", EnumChatFormatting.RED),
			new GoalColor("teamkill.", EnumChatFormatting.LIGHT_PURPLE),
			new GoalColor("teamkill.", EnumChatFormatting.YELLOW),
			new GoalColor("teamkill.", EnumChatFormatting.WHITE) };
	IScoreObjectiveCriteria[] field_178793_i = new IScoreObjectiveCriteria[] {
			new GoalColor("killedByTeam.", EnumChatFormatting.BLACK),
			new GoalColor("killedByTeam.", EnumChatFormatting.DARK_BLUE),
			new GoalColor("killedByTeam.", EnumChatFormatting.DARK_GREEN),
			new GoalColor("killedByTeam.", EnumChatFormatting.DARK_AQUA),
			new GoalColor("killedByTeam.", EnumChatFormatting.DARK_RED),
			new GoalColor("killedByTeam.", EnumChatFormatting.DARK_PURPLE),
			new GoalColor("killedByTeam.", EnumChatFormatting.GOLD),
			new GoalColor("killedByTeam.", EnumChatFormatting.GRAY),
			new GoalColor("killedByTeam.", EnumChatFormatting.DARK_GRAY),
			new GoalColor("killedByTeam.", EnumChatFormatting.BLUE),
			new GoalColor("killedByTeam.", EnumChatFormatting.GREEN),
			new GoalColor("killedByTeam.", EnumChatFormatting.AQUA),
			new GoalColor("killedByTeam.", EnumChatFormatting.RED),
			new GoalColor("killedByTeam.", EnumChatFormatting.LIGHT_PURPLE),
			new GoalColor("killedByTeam.", EnumChatFormatting.YELLOW),
			new GoalColor("killedByTeam.", EnumChatFormatting.WHITE) };

	String getName();

	int func_96635_a(List<EntityPlayer> var1);

	boolean isReadOnly();

	IScoreObjectiveCriteria.EnumRenderType getRenderType();

	public static enum EnumRenderType {
		INTEGER("integer"), HEARTS("hearts");

		private static final Map<String, IScoreObjectiveCriteria.EnumRenderType> field_178801_c = Maps.newHashMap();
		private final String field_178798_d;

		private EnumRenderType(String parString2) {
			this.field_178798_d = parString2;
		}

		public String func_178796_a() {
			return this.field_178798_d;
		}

		public static IScoreObjectiveCriteria.EnumRenderType func_178795_a(String parString1) {
			IScoreObjectiveCriteria.EnumRenderType iscoreobjectivecriteria$enumrendertype = (IScoreObjectiveCriteria.EnumRenderType) field_178801_c
					.get(parString1);
			return iscoreobjectivecriteria$enumrendertype == null ? INTEGER : iscoreobjectivecriteria$enumrendertype;
		}

		static {
			IScoreObjectiveCriteria.EnumRenderType[] types = values();
			for (int i = 0; i < types.length; ++i) {
				field_178801_c.put(types[i].func_178796_a(), types[i]);
			}

		}
	}
}