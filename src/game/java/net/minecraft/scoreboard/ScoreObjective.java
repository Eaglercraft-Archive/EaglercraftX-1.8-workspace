package net.minecraft.scoreboard;

import net.lax1dude.eaglercraft.v1_8.profanity_filter.ProfanityFilter;
import net.minecraft.client.Minecraft;

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
public class ScoreObjective {
	private final Scoreboard theScoreboard;
	private final String name;
	private final IScoreObjectiveCriteria objectiveCriteria;
	private IScoreObjectiveCriteria.EnumRenderType renderType;
	private String displayName;
	private String displayNameProfanityFilter;

	public ScoreObjective(Scoreboard theScoreboardIn, String nameIn, IScoreObjectiveCriteria objectiveCriteriaIn) {
		this.theScoreboard = theScoreboardIn;
		this.name = nameIn;
		this.objectiveCriteria = objectiveCriteriaIn;
		this.displayName = nameIn;
		this.renderType = objectiveCriteriaIn.getRenderType();
	}

	public Scoreboard getScoreboard() {
		return this.theScoreboard;
	}

	public String getName() {
		return this.name;
	}

	public IScoreObjectiveCriteria getCriteria() {
		return this.objectiveCriteria;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getDisplayNameProfanityFilter() {
		if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
			if (displayNameProfanityFilter == null) {
				displayNameProfanityFilter = ProfanityFilter.getInstance().profanityFilterString(displayName);
			}
			return displayNameProfanityFilter;
		} else {
			return this.displayName;
		}
	}

	public void setDisplayName(String nameIn) {
		this.displayName = nameIn;
		this.theScoreboard.func_96532_b(this);
	}

	public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
		return this.renderType;
	}

	public void setRenderType(IScoreObjectiveCriteria.EnumRenderType type) {
		this.renderType = type;
		this.theScoreboard.func_96532_b(this);
	}
}