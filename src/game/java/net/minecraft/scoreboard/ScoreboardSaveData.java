package net.minecraft.scoreboard;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSavedData;

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
public class ScoreboardSaveData extends WorldSavedData {
	private static final Logger logger = LogManager.getLogger();
	private Scoreboard theScoreboard;
	private NBTTagCompound delayedInitNbt;

	public ScoreboardSaveData() {
		this("scoreboard");
	}

	public ScoreboardSaveData(String name) {
		super(name);
	}

	public void setScoreboard(Scoreboard scoreboardIn) {
		this.theScoreboard = scoreboardIn;
		if (this.delayedInitNbt != null) {
			this.readFromNBT(this.delayedInitNbt);
		}

	}

	/**+
	 * reads in data from the NBTTagCompound into this MapDataBase
	 */
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		if (this.theScoreboard == null) {
			this.delayedInitNbt = nbttagcompound;
		} else {
			this.readObjectives(nbttagcompound.getTagList("Objectives", 10));
			this.readScores(nbttagcompound.getTagList("PlayerScores", 10));
			if (nbttagcompound.hasKey("DisplaySlots", 10)) {
				this.readDisplayConfig(nbttagcompound.getCompoundTag("DisplaySlots"));
			}

			if (nbttagcompound.hasKey("Teams", 9)) {
				this.readTeams(nbttagcompound.getTagList("Teams", 10));
			}

		}
	}

	protected void readTeams(NBTTagList parNBTTagList) {
		for (int i = 0; i < parNBTTagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = parNBTTagList.getCompoundTagAt(i);
			String s = nbttagcompound.getString("Name");
			if (s.length() > 16) {
				s = s.substring(0, 16);
			}

			ScorePlayerTeam scoreplayerteam = this.theScoreboard.createTeam(s);
			String s1 = nbttagcompound.getString("DisplayName");
			if (s1.length() > 32) {
				s1 = s1.substring(0, 32);
			}

			scoreplayerteam.setTeamName(s1);
			if (nbttagcompound.hasKey("TeamColor", 8)) {
				scoreplayerteam.setChatFormat(EnumChatFormatting.getValueByName(nbttagcompound.getString("TeamColor")));
			}

			scoreplayerteam.setNamePrefix(nbttagcompound.getString("Prefix"));
			scoreplayerteam.setNameSuffix(nbttagcompound.getString("Suffix"));
			if (nbttagcompound.hasKey("AllowFriendlyFire", 99)) {
				scoreplayerteam.setAllowFriendlyFire(nbttagcompound.getBoolean("AllowFriendlyFire"));
			}

			if (nbttagcompound.hasKey("SeeFriendlyInvisibles", 99)) {
				scoreplayerteam.setSeeFriendlyInvisiblesEnabled(nbttagcompound.getBoolean("SeeFriendlyInvisibles"));
			}

			if (nbttagcompound.hasKey("NameTagVisibility", 8)) {
				Team.EnumVisible team$enumvisible = Team.EnumVisible
						.func_178824_a(nbttagcompound.getString("NameTagVisibility"));
				if (team$enumvisible != null) {
					scoreplayerteam.setNameTagVisibility(team$enumvisible);
				}
			}

			if (nbttagcompound.hasKey("DeathMessageVisibility", 8)) {
				Team.EnumVisible team$enumvisible1 = Team.EnumVisible
						.func_178824_a(nbttagcompound.getString("DeathMessageVisibility"));
				if (team$enumvisible1 != null) {
					scoreplayerteam.setDeathMessageVisibility(team$enumvisible1);
				}
			}

			this.func_96502_a(scoreplayerteam, nbttagcompound.getTagList("Players", 8));
		}

	}

	protected void func_96502_a(ScorePlayerTeam parScorePlayerTeam, NBTTagList parNBTTagList) {
		for (int i = 0; i < parNBTTagList.tagCount(); ++i) {
			this.theScoreboard.addPlayerToTeam(parNBTTagList.getStringTagAt(i), parScorePlayerTeam.getRegisteredName());
		}

	}

	protected void readDisplayConfig(NBTTagCompound parNBTTagCompound) {
		for (int i = 0; i < 19; ++i) {
			if (parNBTTagCompound.hasKey("slot_" + i, 8)) {
				String s = parNBTTagCompound.getString("slot_" + i);
				ScoreObjective scoreobjective = this.theScoreboard.getObjective(s);
				this.theScoreboard.setObjectiveInDisplaySlot(i, scoreobjective);
			}
		}

	}

	protected void readObjectives(NBTTagList nbt) {
		for (int i = 0; i < nbt.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
			IScoreObjectiveCriteria iscoreobjectivecriteria = (IScoreObjectiveCriteria) IScoreObjectiveCriteria.INSTANCES
					.get(nbttagcompound.getString("CriteriaName"));
			if (iscoreobjectivecriteria != null) {
				String s = nbttagcompound.getString("Name");
				if (s.length() > 16) {
					s = s.substring(0, 16);
				}

				ScoreObjective scoreobjective = this.theScoreboard.addScoreObjective(s, iscoreobjectivecriteria);
				scoreobjective.setDisplayName(nbttagcompound.getString("DisplayName"));
				scoreobjective.setRenderType(
						IScoreObjectiveCriteria.EnumRenderType.func_178795_a(nbttagcompound.getString("RenderType")));
			}
		}

	}

	protected void readScores(NBTTagList nbt) {
		for (int i = 0; i < nbt.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
			ScoreObjective scoreobjective = this.theScoreboard.getObjective(nbttagcompound.getString("Objective"));
			String s = nbttagcompound.getString("Name");
			if (s.length() > 40) {
				s = s.substring(0, 40);
			}

			Score score = this.theScoreboard.getValueFromObjective(s, scoreobjective);
			score.setScorePoints(nbttagcompound.getInteger("Score"));
			if (nbttagcompound.hasKey("Locked")) {
				score.setLocked(nbttagcompound.getBoolean("Locked"));
			}
		}

	}

	/**+
	 * write data to NBTTagCompound from this MapDataBase, similar
	 * to Entities and TileEntities
	 */
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		if (this.theScoreboard == null) {
			logger.warn("Tried to save scoreboard without having a scoreboard...");
		} else {
			nbttagcompound.setTag("Objectives", this.objectivesToNbt());
			nbttagcompound.setTag("PlayerScores", this.scoresToNbt());
			nbttagcompound.setTag("Teams", this.func_96496_a());
			this.func_96497_d(nbttagcompound);
		}
	}

	protected NBTTagList func_96496_a() {
		NBTTagList nbttaglist = new NBTTagList();

		for (ScorePlayerTeam scoreplayerteam : this.theScoreboard.getTeams()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Name", scoreplayerteam.getRegisteredName());
			nbttagcompound.setString("DisplayName", scoreplayerteam.getTeamName());
			if (scoreplayerteam.getChatFormat().getColorIndex() >= 0) {
				nbttagcompound.setString("TeamColor", scoreplayerteam.getChatFormat().getFriendlyName());
			}

			nbttagcompound.setString("Prefix", scoreplayerteam.getColorPrefix());
			nbttagcompound.setString("Suffix", scoreplayerteam.getColorSuffix());
			nbttagcompound.setBoolean("AllowFriendlyFire", scoreplayerteam.getAllowFriendlyFire());
			nbttagcompound.setBoolean("SeeFriendlyInvisibles", scoreplayerteam.getSeeFriendlyInvisiblesEnabled());
			nbttagcompound.setString("NameTagVisibility", scoreplayerteam.getNameTagVisibility().field_178830_e);
			nbttagcompound.setString("DeathMessageVisibility",
					scoreplayerteam.getDeathMessageVisibility().field_178830_e);
			NBTTagList nbttaglist1 = new NBTTagList();

			for (String s : scoreplayerteam.getMembershipCollection()) {
				nbttaglist1.appendTag(new NBTTagString(s));
			}

			nbttagcompound.setTag("Players", nbttaglist1);
			nbttaglist.appendTag(nbttagcompound);
		}

		return nbttaglist;
	}

	protected void func_96497_d(NBTTagCompound parNBTTagCompound) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		boolean flag = false;

		for (int i = 0; i < 19; ++i) {
			ScoreObjective scoreobjective = this.theScoreboard.getObjectiveInDisplaySlot(i);
			if (scoreobjective != null) {
				nbttagcompound.setString("slot_" + i, scoreobjective.getName());
				flag = true;
			}
		}

		if (flag) {
			parNBTTagCompound.setTag("DisplaySlots", nbttagcompound);
		}

	}

	protected NBTTagList objectivesToNbt() {
		NBTTagList nbttaglist = new NBTTagList();

		for (ScoreObjective scoreobjective : this.theScoreboard.getScoreObjectives()) {
			if (scoreobjective.getCriteria() != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setString("Name", scoreobjective.getName());
				nbttagcompound.setString("CriteriaName", scoreobjective.getCriteria().getName());
				nbttagcompound.setString("DisplayName", scoreobjective.getDisplayName());
				nbttagcompound.setString("RenderType", scoreobjective.getRenderType().func_178796_a());
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		return nbttaglist;
	}

	protected NBTTagList scoresToNbt() {
		NBTTagList nbttaglist = new NBTTagList();

		for (Score score : this.theScoreboard.getScores()) {
			if (score.getObjective() != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setString("Name", score.getPlayerName());
				nbttagcompound.setString("Objective", score.getObjective().getName());
				nbttagcompound.setInteger("Score", score.getScorePoints());
				nbttagcompound.setBoolean("Locked", score.isLocked());
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		return nbttaglist;
	}
}