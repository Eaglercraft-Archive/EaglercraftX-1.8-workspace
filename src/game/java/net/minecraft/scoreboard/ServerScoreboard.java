package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.server.MinecraftServer;

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
public class ServerScoreboard extends Scoreboard {

	private final MinecraftServer scoreboardMCServer;
	private final Set<ScoreObjective> field_96553_b = Sets.newHashSet();
	private ScoreboardSaveData scoreboardSaveData;

	public ServerScoreboard(MinecraftServer mcServer) {
		this.scoreboardMCServer = mcServer;
	}

	public void func_96536_a(Score score) {
		super.func_96536_a(score);
		if (this.field_96553_b.contains(score.getObjective())) {
			this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3CPacketUpdateScore(score));
		}

		this.func_96551_b();
	}

	public void func_96516_a(String s) {
		super.func_96516_a(s);
		this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3CPacketUpdateScore(s));
		this.func_96551_b();
	}

	public void func_178820_a(String s, ScoreObjective scoreobjective) {
		super.func_178820_a(s, scoreobjective);
		this.scoreboardMCServer.getConfigurationManager()
				.sendPacketToAllPlayers(new S3CPacketUpdateScore(s, scoreobjective));
		this.func_96551_b();
	}

	/**+
	 * 0 is tab menu, 1 is sidebar, 2 is below name
	 */
	public void setObjectiveInDisplaySlot(int i, ScoreObjective scoreobjective) {
		ScoreObjective scoreobjective1 = this.getObjectiveInDisplaySlot(i);
		super.setObjectiveInDisplaySlot(i, scoreobjective);
		if (scoreobjective1 != scoreobjective && scoreobjective1 != null) {
			if (this.func_96552_h(scoreobjective1) > 0) {
				this.scoreboardMCServer.getConfigurationManager()
						.sendPacketToAllPlayers(new S3DPacketDisplayScoreboard(i, scoreobjective));
			} else {
				this.getPlayerIterator(scoreobjective1);
			}
		}

		if (scoreobjective != null) {
			if (this.field_96553_b.contains(scoreobjective)) {
				this.scoreboardMCServer.getConfigurationManager()
						.sendPacketToAllPlayers(new S3DPacketDisplayScoreboard(i, scoreobjective));
			} else {
				this.func_96549_e(scoreobjective);
			}
		}

		this.func_96551_b();
	}

	/**+
	 * Adds a player to the given team
	 */
	public boolean addPlayerToTeam(String s, String s1) {
		if (super.addPlayerToTeam(s, s1)) {
			ScorePlayerTeam scoreplayerteam = this.getTeam(s1);
			this.scoreboardMCServer.getConfigurationManager()
					.sendPacketToAllPlayers(new S3EPacketTeams(scoreplayerteam, Arrays.asList(new String[] { s }), 3));
			this.func_96551_b();
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
	public void removePlayerFromTeam(String s, ScorePlayerTeam scoreplayerteam) {
		super.removePlayerFromTeam(s, scoreplayerteam);
		this.scoreboardMCServer.getConfigurationManager()
				.sendPacketToAllPlayers(new S3EPacketTeams(scoreplayerteam, Arrays.asList(new String[] { s }), 4));
		this.func_96551_b();
	}

	/**+
	 * Called when a score objective is added
	 */
	public void onScoreObjectiveAdded(ScoreObjective scoreobjective) {
		super.onScoreObjectiveAdded(scoreobjective);
		this.func_96551_b();
	}

	public void func_96532_b(ScoreObjective scoreobjective) {
		super.func_96532_b(scoreobjective);
		if (this.field_96553_b.contains(scoreobjective)) {
			this.scoreboardMCServer.getConfigurationManager()
					.sendPacketToAllPlayers(new S3BPacketScoreboardObjective(scoreobjective, 2));
		}

		this.func_96551_b();
	}

	public void func_96533_c(ScoreObjective scoreobjective) {
		super.func_96533_c(scoreobjective);
		if (this.field_96553_b.contains(scoreobjective)) {
			this.getPlayerIterator(scoreobjective);
		}

		this.func_96551_b();
	}

	/**+
	 * This packet will notify the players that this team is
	 * created, and that will register it on the client
	 */
	public void broadcastTeamCreated(ScorePlayerTeam scoreplayerteam) {
		super.broadcastTeamCreated(scoreplayerteam);
		this.scoreboardMCServer.getConfigurationManager()
				.sendPacketToAllPlayers(new S3EPacketTeams(scoreplayerteam, 0));
		this.func_96551_b();
	}

	/**+
	 * This packet will notify the players that this team is updated
	 */
	public void sendTeamUpdate(ScorePlayerTeam scoreplayerteam) {
		super.sendTeamUpdate(scoreplayerteam);
		this.scoreboardMCServer.getConfigurationManager()
				.sendPacketToAllPlayers(new S3EPacketTeams(scoreplayerteam, 2));
		this.func_96551_b();
	}

	public void func_96513_c(ScorePlayerTeam scoreplayerteam) {
		super.func_96513_c(scoreplayerteam);
		this.scoreboardMCServer.getConfigurationManager()
				.sendPacketToAllPlayers(new S3EPacketTeams(scoreplayerteam, 1));
		this.func_96551_b();
	}

	public void func_96547_a(ScoreboardSaveData parScoreboardSaveData) {
		this.scoreboardSaveData = parScoreboardSaveData;
	}

	protected void func_96551_b() {
		if (this.scoreboardSaveData != null) {
			this.scoreboardSaveData.markDirty();
		}

	}

	public List<Packet> func_96550_d(ScoreObjective parScoreObjective) {
		ArrayList arraylist = Lists.newArrayList();
		arraylist.add(new S3BPacketScoreboardObjective(parScoreObjective, 0));

		for (int i = 0; i < 19; ++i) {
			if (this.getObjectiveInDisplaySlot(i) == parScoreObjective) {
				arraylist.add(new S3DPacketDisplayScoreboard(i, parScoreObjective));
			}
		}

		for (Score score : this.getSortedScores(parScoreObjective)) {
			arraylist.add(new S3CPacketUpdateScore(score));
		}

		return arraylist;
	}

	public void func_96549_e(ScoreObjective parScoreObjective) {
		List<Packet> list = this.func_96550_d(parScoreObjective);

		List<EntityPlayerMP> players = this.scoreboardMCServer.getConfigurationManager().func_181057_v();
		for (int i = 0, l = players.size(); i < l; ++i) {
			EntityPlayerMP entityplayermp = players.get(i);
			for (int j = 0, m = list.size(); j < m; ++j) {
				entityplayermp.playerNetServerHandler.sendPacket(list.get(j));
			}
		}

		this.field_96553_b.add(parScoreObjective);
	}

	public List<Packet> func_96548_f(ScoreObjective parScoreObjective) {
		ArrayList arraylist = Lists.newArrayList();
		arraylist.add(new S3BPacketScoreboardObjective(parScoreObjective, 1));

		for (int i = 0; i < 19; ++i) {
			if (this.getObjectiveInDisplaySlot(i) == parScoreObjective) {
				arraylist.add(new S3DPacketDisplayScoreboard(i, parScoreObjective));
			}
		}

		return arraylist;
	}

	public void getPlayerIterator(ScoreObjective parScoreObjective) {
		List<Packet> list = this.func_96548_f(parScoreObjective);

		List<EntityPlayerMP> players = this.scoreboardMCServer.getConfigurationManager().func_181057_v();
		for (int i = 0, l = players.size(); i < l; ++i) {
			EntityPlayerMP entityplayermp = players.get(i);
			for (int j = 0, m = list.size(); j < m; ++j) {
				entityplayermp.playerNetServerHandler.sendPacket(list.get(j));
			}
		}

		this.field_96553_b.remove(parScoreObjective);
	}

	public int func_96552_h(ScoreObjective parScoreObjective) {
		int i = 0;

		for (int j = 0; j < 19; ++j) {
			if (this.getObjectiveInDisplaySlot(j) == parScoreObjective) {
				++i;
			}
		}

		return i;
	}
}