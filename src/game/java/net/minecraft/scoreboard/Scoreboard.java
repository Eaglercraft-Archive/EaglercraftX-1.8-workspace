package net.minecraft.scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
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
public class Scoreboard {
	private final Map<String, ScoreObjective> scoreObjectives = Maps.newHashMap();
	private final Map<IScoreObjectiveCriteria, List<ScoreObjective>> scoreObjectiveCriterias = Maps.newHashMap();
	private final Map<String, Map<ScoreObjective, Score>> entitiesScoreObjectives = Maps.newHashMap();
	/**+
	 * Index 0 is tab menu, 1 is sidebar, and 2 is below name
	 */
	private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];
	private final Map<String, ScorePlayerTeam> teams = Maps.newHashMap();
	private final Map<String, ScorePlayerTeam> teamMemberships = Maps.newHashMap();
	private static String[] field_178823_g = null;

	/**+
	 * Returns a ScoreObjective for the objective name
	 */
	public ScoreObjective getObjective(String name) {
		return (ScoreObjective) this.scoreObjectives.get(name);
	}

	/**+
	 * Create and returns the score objective for the given name and
	 * ScoreCriteria
	 */
	public ScoreObjective addScoreObjective(String name, IScoreObjectiveCriteria criteria) {
		if (name.length() > 16) {
			throw new IllegalArgumentException("The objective name \'" + name + "\' is too long!");
		} else {
			ScoreObjective scoreobjective = this.getObjective(name);
			if (scoreobjective != null) {
				throw new IllegalArgumentException("An objective with the name \'" + name + "\' already exists!");
			} else {
				scoreobjective = new ScoreObjective(this, name, criteria);
				Object object = (List) this.scoreObjectiveCriterias.get(criteria);
				if (object == null) {
					object = Lists.newArrayList();
					this.scoreObjectiveCriterias.put(criteria, (List<ScoreObjective>) object);
				}

				((List) object).add(scoreobjective);
				this.scoreObjectives.put(name, scoreobjective);
				this.onScoreObjectiveAdded(scoreobjective);
				return scoreobjective;
			}
		}
	}

	/**+
	 * Returns all the objectives for the given criteria
	 */
	public Collection<ScoreObjective> getObjectivesFromCriteria(IScoreObjectiveCriteria criteria) {
		Collection collection = (Collection) this.scoreObjectiveCriterias.get(criteria);
		return collection == null ? Lists.newArrayList() : Lists.newArrayList(collection);
	}

	/**+
	 * Returns if the entity has the given ScoreObjective
	 */
	public boolean entityHasObjective(String name, ScoreObjective parScoreObjective) {
		Map map = (Map) this.entitiesScoreObjectives.get(name);
		if (map == null) {
			return false;
		} else {
			Score score = (Score) map.get(parScoreObjective);
			return score != null;
		}
	}

	/**+
	 * Returns the value of the given objective for the given entity
	 * name
	 */
	public Score getValueFromObjective(String name, ScoreObjective objective) {
		if (name.length() > 40) {
			throw new IllegalArgumentException("The player name \'" + name + "\' is too long!");
		} else {
			Object object = (Map) this.entitiesScoreObjectives.get(name);
			if (object == null) {
				object = Maps.newHashMap();
				this.entitiesScoreObjectives.put(name, (Map<ScoreObjective, Score>) object);
			}

			Score score = (Score) ((Map) object).get(objective);
			if (score == null) {
				score = new Score(this, objective, name);
				((Map) object).put(objective, score);
			}

			return score;
		}
	}

	/**+
	 * Returns an array of Score objects, sorting by
	 * Score.getScorePoints()
	 */
	public Collection<Score> getSortedScores(ScoreObjective objective) {
		ArrayList arraylist = Lists.newArrayList();

		for (Map map : this.entitiesScoreObjectives.values()) {
			Score score = (Score) map.get(objective);
			if (score != null) {
				arraylist.add(score);
			}
		}

		Collections.sort(arraylist, Score.scoreComparator);
		return arraylist;
	}

	public Collection<ScoreObjective> getScoreObjectives() {
		return this.scoreObjectives.values();
	}

	public Collection<String> getObjectiveNames() {
		return this.entitiesScoreObjectives.keySet();
	}

	/**+
	 * Remove the given ScoreObjective for the given Entity name.
	 */
	public void removeObjectiveFromEntity(String name, ScoreObjective objective) {
		if (objective == null) {
			Map map = (Map) this.entitiesScoreObjectives.remove(name);
			if (map != null) {
				this.func_96516_a(name);
			}
		} else {
			Map map2 = (Map) this.entitiesScoreObjectives.get(name);
			if (map2 != null) {
				Score score = (Score) map2.remove(objective);
				if (map2.size() < 1) {
					Map map1 = (Map) this.entitiesScoreObjectives.remove(name);
					if (map1 != null) {
						this.func_96516_a(name);
					}
				} else if (score != null) {
					this.func_178820_a(name, objective);
				}
			}
		}

	}

	public Collection<Score> getScores() {
		Collection collection = this.entitiesScoreObjectives.values();
		ArrayList arraylist = Lists.newArrayList();

		for (Map map : (Collection<Map>) collection) {
			arraylist.addAll(map.values());
		}

		return arraylist;
	}

	public Map<ScoreObjective, Score> getObjectivesForEntity(String name) {
		Object object = (Map) this.entitiesScoreObjectives.get(name);
		if (object == null) {
			object = Maps.newHashMap();
		}

		return (Map<ScoreObjective, Score>) object;
	}

	public void removeObjective(ScoreObjective parScoreObjective) {
		this.scoreObjectives.remove(parScoreObjective.getName());

		for (int i = 0; i < 19; ++i) {
			if (this.getObjectiveInDisplaySlot(i) == parScoreObjective) {
				this.setObjectiveInDisplaySlot(i, (ScoreObjective) null);
			}
		}

		List list = (List) this.scoreObjectiveCriterias.get(parScoreObjective.getCriteria());
		if (list != null) {
			list.remove(parScoreObjective);
		}

		for (Map map : this.entitiesScoreObjectives.values()) {
			map.remove(parScoreObjective);
		}

		this.func_96533_c(parScoreObjective);
	}

	/**+
	 * 0 is tab menu, 1 is sidebar, 2 is below name
	 */
	public void setObjectiveInDisplaySlot(int parInt1, ScoreObjective parScoreObjective) {
		this.objectiveDisplaySlots[parInt1] = parScoreObjective;
	}

	/**+
	 * 0 is tab menu, 1 is sidebar, 2 is below name
	 */
	public ScoreObjective getObjectiveInDisplaySlot(int parInt1) {
		return this.objectiveDisplaySlots[parInt1];
	}

	/**+
	 * Retrieve the ScorePlayerTeam instance identified by the
	 * passed team name
	 */
	public ScorePlayerTeam getTeam(String parString1) {
		return (ScorePlayerTeam) this.teams.get(parString1);
	}

	public ScorePlayerTeam createTeam(String parString1) {
		if (parString1.length() > 16) {
			throw new IllegalArgumentException("The team name \'" + parString1 + "\' is too long!");
		} else {
			ScorePlayerTeam scoreplayerteam = this.getTeam(parString1);
			if (scoreplayerteam != null) {
				throw new IllegalArgumentException("A team with the name \'" + parString1 + "\' already exists!");
			} else {
				scoreplayerteam = new ScorePlayerTeam(this, parString1);
				this.teams.put(parString1, scoreplayerteam);
				this.broadcastTeamCreated(scoreplayerteam);
				return scoreplayerteam;
			}
		}
	}

	/**+
	 * Removes the team from the scoreboard, updates all player
	 * memberships and broadcasts the deletion to all players
	 */
	public void removeTeam(ScorePlayerTeam parScorePlayerTeam) {
		this.teams.remove(parScorePlayerTeam.getRegisteredName());

		for (String s : parScorePlayerTeam.getMembershipCollection()) {
			this.teamMemberships.remove(s);
		}

		this.func_96513_c(parScorePlayerTeam);
	}

	/**+
	 * Adds a player to the given team
	 */
	public boolean addPlayerToTeam(String player, String newTeam) {
		if (player.length() > 40) {
			throw new IllegalArgumentException("The player name \'" + player + "\' is too long!");
		} else if (!this.teams.containsKey(newTeam)) {
			return false;
		} else {
			ScorePlayerTeam scoreplayerteam = this.getTeam(newTeam);
			if (this.getPlayersTeam(player) != null) {
				this.removePlayerFromTeams(player);
			}

			this.teamMemberships.put(player, scoreplayerteam);
			scoreplayerteam.getMembershipCollection().add(player);
			return true;
		}
	}

	public boolean removePlayerFromTeams(String parString1) {
		ScorePlayerTeam scoreplayerteam = this.getPlayersTeam(parString1);
		if (scoreplayerteam != null) {
			this.removePlayerFromTeam(parString1, scoreplayerteam);
			return true;
		} else {
			return false;
		}
	}

	/**+
	 * Removes the given username from the given ScorePlayerTeam. If
	 * the player is not on the team then an IllegalStateException
	 * is thrown.
	 */
	public void removePlayerFromTeam(String parString1, ScorePlayerTeam parScorePlayerTeam) {
		if (this.getPlayersTeam(parString1) != parScorePlayerTeam) {
			throw new IllegalStateException(
					"Player is either on another team or not on any team. Cannot remove from team \'"
							+ parScorePlayerTeam.getRegisteredName() + "\'.");
		} else {
			this.teamMemberships.remove(parString1);
			parScorePlayerTeam.getMembershipCollection().remove(parString1);
		}
	}

	/**+
	 * Retrieve all registered ScorePlayerTeam names
	 */
	public Collection<String> getTeamNames() {
		return this.teams.keySet();
	}

	/**+
	 * Retrieve all registered ScorePlayerTeam instances
	 */
	public Collection<ScorePlayerTeam> getTeams() {
		return this.teams.values();
	}

	/**+
	 * Gets the ScorePlayerTeam object for the given username.
	 */
	public ScorePlayerTeam getPlayersTeam(String parString1) {
		return (ScorePlayerTeam) this.teamMemberships.get(parString1);
	}

	/**+
	 * Called when a score objective is added
	 */
	public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn) {
	}

	public void func_96532_b(ScoreObjective parScoreObjective) {
	}

	public void func_96533_c(ScoreObjective parScoreObjective) {
	}

	public void func_96536_a(Score parScore) {
	}

	public void func_96516_a(String parString1) {
	}

	public void func_178820_a(String parString1, ScoreObjective parScoreObjective) {
	}

	/**+
	 * This packet will notify the players that this team is
	 * created, and that will register it on the client
	 */
	public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {
	}

	/**+
	 * This packet will notify the players that this team is updated
	 */
	public void sendTeamUpdate(ScorePlayerTeam playerTeam) {
	}

	public void func_96513_c(ScorePlayerTeam playerTeam) {
	}

	/**+
	 * Returns 'list' for 0, 'sidebar' for 1, 'belowName for 2,
	 * otherwise null.
	 */
	public static String getObjectiveDisplaySlot(int parInt1) {
		switch (parInt1) {
		case 0:
			return "list";
		case 1:
			return "sidebar";
		case 2:
			return "belowName";
		default:
			if (parInt1 >= 3 && parInt1 <= 18) {
				EnumChatFormatting enumchatformatting = EnumChatFormatting.func_175744_a(parInt1 - 3);
				if (enumchatformatting != null && enumchatformatting != EnumChatFormatting.RESET) {
					return "sidebar.team." + enumchatformatting.getFriendlyName();
				}
			}

			return null;
		}
	}

	/**+
	 * Returns 0 for (case-insensitive) 'list', 1 for 'sidebar', 2
	 * for 'belowName', otherwise -1.
	 */
	public static int getObjectiveDisplaySlotNumber(String parString1) {
		if (parString1.equalsIgnoreCase("list")) {
			return 0;
		} else if (parString1.equalsIgnoreCase("sidebar")) {
			return 1;
		} else if (parString1.equalsIgnoreCase("belowName")) {
			return 2;
		} else {
			if (parString1.startsWith("sidebar.team.")) {
				String s = parString1.substring("sidebar.team.".length());
				EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s);
				if (enumchatformatting != null && enumchatformatting.getColorIndex() >= 0) {
					return enumchatformatting.getColorIndex() + 3;
				}
			}

			return -1;
		}
	}

	public static String[] getDisplaySlotStrings() {
		if (field_178823_g == null) {
			field_178823_g = new String[19];

			for (int i = 0; i < 19; ++i) {
				field_178823_g[i] = getObjectiveDisplaySlot(i);
			}
		}

		return field_178823_g;
	}

	public void func_181140_a(Entity parEntity) {
		if (parEntity != null && !(parEntity instanceof EntityPlayer) && !parEntity.isEntityAlive()) {
			String s = parEntity.getUniqueID().toString();
			this.removeObjectiveFromEntity(s, (ScoreObjective) null);
			this.removePlayerFromTeams(s);
		}
	}
}