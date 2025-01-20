package net.minecraft.util;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.HString;

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
public class ChatComponentScore extends ChatComponentStyle {
	private final String name;
	private final String objective;
	/**+
	 * The value displayed instead of the real score (may be null)
	 */
	private String value = "";

	public ChatComponentScore(String nameIn, String objectiveIn) {
		this.name = nameIn;
		this.objective = objectiveIn;
	}

	public String getName() {
		return this.name;
	}

	public String getObjective() {
		return this.objective;
	}

	/**+
	 * Sets the value displayed instead of the real score.
	 */
	public void setValue(String valueIn) {
		this.value = valueIn;
	}

	/**+
	 * Gets the text of this component, without any special
	 * formatting codes added, for chat. TODO: why is this two
	 * different methods?
	 */
	public String getUnformattedTextForChat() {
		MinecraftServer minecraftserver = MinecraftServer.getServer();
		if (minecraftserver != null && StringUtils.isNullOrEmpty(this.value)) {
			Scoreboard scoreboard = minecraftserver.worldServerForDimension(0).getScoreboard();
			ScoreObjective scoreobjective = scoreboard.getObjective(this.objective);
			if (scoreboard.entityHasObjective(this.name, scoreobjective)) {
				Score score = scoreboard.getValueFromObjective(this.name, scoreobjective);
				this.setValue(HString.format("%d", new Object[] { Integer.valueOf(score.getScorePoints()) }));
			} else {
				this.value = "";
			}
		}

		return this.value;
	}

	/**+
	 * Creates a copy of this component. Almost a deep copy, except
	 * the style is shallow-copied.
	 */
	public ChatComponentScore createCopy() {
		ChatComponentScore chatcomponentscore = new ChatComponentScore(this.name, this.objective);
		chatcomponentscore.setValue(this.value);
		chatcomponentscore.setChatStyle(this.getChatStyle().createShallowCopy());

		List<IChatComponent> lst = this.getSiblings();
		for (int i = 0, l = lst.size(); i < l; ++i) {
			chatcomponentscore.appendSibling(lst.get(i).createCopy());
		}

		return chatcomponentscore;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof ChatComponentScore)) {
			return false;
		} else {
			ChatComponentScore chatcomponentscore = (ChatComponentScore) object;
			return this.name.equals(chatcomponentscore.name) && this.objective.equals(chatcomponentscore.objective)
					&& super.equals(object);
		}
	}

	public String toString() {
		return "ScoreComponent{name=\'" + this.name + '\'' + "objective=\'" + this.objective + '\'' + ", siblings="
				+ this.siblings + ", style=" + this.getChatStyle() + '}';
	}
}