package net.minecraft.stats;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;

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
public class StatFileWriter {
	protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.newHashMap();

	/**+
	 * Returns true if the achievement has been unlocked.
	 */
	public boolean hasAchievementUnlocked(Achievement achievementIn) {
		return this.readStat(achievementIn) > 0;
	}

	/**+
	 * Returns true if the parent has been unlocked, or there is no
	 * parent
	 */
	public boolean canUnlockAchievement(Achievement achievementIn) {
		return achievementIn.parentAchievement == null || this.hasAchievementUnlocked(achievementIn.parentAchievement);
	}

	public int func_150874_c(Achievement parAchievement) {
		if (this.hasAchievementUnlocked(parAchievement)) {
			return 0;
		} else {
			int i = 0;

			for (Achievement achievement = parAchievement.parentAchievement; achievement != null
					&& !this.hasAchievementUnlocked(achievement); ++i) {
				achievement = achievement.parentAchievement;
			}

			return i;
		}
	}

	public void increaseStat(EntityPlayer player, StatBase stat, int amount) {
		if (!stat.isAchievement() || this.canUnlockAchievement((Achievement) stat)) {
			this.unlockAchievement(player, stat, this.readStat(stat) + amount);
		}
	}

	/**+
	 * Triggers the logging of an achievement and attempts to
	 * announce to server
	 */
	public void unlockAchievement(EntityPlayer var1, StatBase statbase, int i) {
		TupleIntJsonSerializable tupleintjsonserializable = (TupleIntJsonSerializable) this.statsData.get(statbase);
		if (tupleintjsonserializable == null) {
			tupleintjsonserializable = new TupleIntJsonSerializable();
			this.statsData.put(statbase, tupleintjsonserializable);
		}

		tupleintjsonserializable.setIntegerValue(i);
	}

	/**+
	 * Reads the given stat and returns its value as an int.
	 */
	public int readStat(StatBase stat) {
		TupleIntJsonSerializable tupleintjsonserializable = (TupleIntJsonSerializable) this.statsData.get(stat);
		return tupleintjsonserializable == null ? 0 : tupleintjsonserializable.getIntegerValue();
	}

	public <T extends IJsonSerializable> T func_150870_b(StatBase parStatBase) {
		TupleIntJsonSerializable tupleintjsonserializable = (TupleIntJsonSerializable) this.statsData.get(parStatBase);
		return (T) (tupleintjsonserializable != null ? tupleintjsonserializable.getJsonSerializableValue() : null);
	}

	public <T extends IJsonSerializable> T func_150872_a(StatBase parStatBase, T parIJsonSerializable) {
		TupleIntJsonSerializable tupleintjsonserializable = (TupleIntJsonSerializable) this.statsData.get(parStatBase);
		if (tupleintjsonserializable == null) {
			tupleintjsonserializable = new TupleIntJsonSerializable();
			this.statsData.put(parStatBase, tupleintjsonserializable);
		}

		tupleintjsonserializable.setJsonSerializableValue(parIJsonSerializable);
		return (T) parIJsonSerializable;
	}
}