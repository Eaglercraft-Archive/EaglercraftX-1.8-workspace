package net.minecraft.scoreboard;

import java.util.Comparator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

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
public class Score {
	/**+
	 * Used for sorting score by points
	 */
	public static final Comparator<Score> scoreComparator = new Comparator<Score>() {
		public int compare(Score score, Score score1) {
			return score.getScorePoints() > score1.getScorePoints() ? 1
					: (score.getScorePoints() < score1.getScorePoints() ? -1
							: score1.getPlayerName().compareToIgnoreCase(score.getPlayerName()));
		}
	};
	private final Scoreboard theScoreboard;
	private final ScoreObjective theScoreObjective;
	private final String scorePlayerName;
	private int scorePoints;
	private boolean locked;
	private boolean field_178818_g;

	public Score(Scoreboard theScoreboardIn, ScoreObjective theScoreObjectiveIn, String scorePlayerNameIn) {
		this.theScoreboard = theScoreboardIn;
		this.theScoreObjective = theScoreObjectiveIn;
		this.scorePlayerName = scorePlayerNameIn;
		this.field_178818_g = true;
	}

	public void increseScore(int amount) {
		if (this.theScoreObjective.getCriteria().isReadOnly()) {
			throw new IllegalStateException("Cannot modify read-only score");
		} else {
			this.setScorePoints(this.getScorePoints() + amount);
		}
	}

	public void decreaseScore(int amount) {
		if (this.theScoreObjective.getCriteria().isReadOnly()) {
			throw new IllegalStateException("Cannot modify read-only score");
		} else {
			this.setScorePoints(this.getScorePoints() - amount);
		}
	}

	public void func_96648_a() {
		if (this.theScoreObjective.getCriteria().isReadOnly()) {
			throw new IllegalStateException("Cannot modify read-only score");
		} else {
			this.increseScore(1);
		}
	}

	public int getScorePoints() {
		return this.scorePoints;
	}

	public void setScorePoints(int points) {
		int i = this.scorePoints;
		this.scorePoints = points;
		if (i != points || this.field_178818_g) {
			this.field_178818_g = false;
			this.getScoreScoreboard().func_96536_a(this);
		}

	}

	public ScoreObjective getObjective() {
		return this.theScoreObjective;
	}

	/**+
	 * Returns the name of the player this score belongs to
	 */
	public String getPlayerName() {
		return this.scorePlayerName;
	}

	public Scoreboard getScoreScoreboard() {
		return this.theScoreboard;
	}

	public boolean isLocked() {
		return this.locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void func_96651_a(List<EntityPlayer> parList) {
		this.setScorePoints(this.theScoreObjective.getCriteria().func_96635_a(parList));
	}
}