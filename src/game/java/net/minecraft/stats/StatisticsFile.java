package net.minecraft.stats;

import com.carrotsearch.hppc.ObjectIntHashMap;
import com.carrotsearch.hppc.ObjectIntMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class StatisticsFile extends StatFileWriter {
	private static final Logger logger = LogManager.getLogger();
	private final MinecraftServer mcServer;
	private final VFile2 statsFile;
	private final Set<StatBase> field_150888_e = Sets.newHashSet();
	private int field_150885_f = -300;
	private boolean field_150886_g = false;

	public StatisticsFile(MinecraftServer serverIn, VFile2 statsFileIn) {
		this.mcServer = serverIn;
		this.statsFile = statsFileIn;
	}

	public void readStatFile() {
		if (this.statsFile.exists()) {
			try {
				this.statsData.clear();
				this.statsData.putAll(this.parseJson(this.statsFile.getAllChars()));
			} catch (JSONException jsonparseexception) {
				logger.error("Couldn\'t parse statistics file " + this.statsFile, jsonparseexception);
			}
		}

	}

	public void saveStatFile() {
		this.statsFile.setAllChars(dumpJson(this.statsData));
	}

	/**+
	 * Triggers the logging of an achievement and attempts to
	 * announce to server
	 */
	public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int parInt1) {
		int i = statIn.isAchievement() ? this.readStat(statIn) : 0;
		super.unlockAchievement(playerIn, statIn, parInt1);
		this.field_150888_e.add(statIn);
		if (statIn.isAchievement() && i == 0 && parInt1 > 0) {
			this.field_150886_g = true;
			if (this.mcServer.isAnnouncingPlayerAchievements()) {
				this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentTranslation(
						"chat.type.achievement", new Object[] { playerIn.getDisplayName(), statIn.func_150955_j() }));
			}
		}

		if (statIn.isAchievement() && i > 0 && parInt1 == 0) {
			this.field_150886_g = true;
			if (this.mcServer.isAnnouncingPlayerAchievements()) {
				this.mcServer.getConfigurationManager()
						.sendChatMsg(new ChatComponentTranslation("chat.type.achievement.taken",
								new Object[] { playerIn.getDisplayName(), statIn.func_150955_j() }));
			}
		}

	}

	public Set<StatBase> func_150878_c() {
		HashSet hashset = Sets.newHashSet(this.field_150888_e);
		this.field_150888_e.clear();
		this.field_150886_g = false;
		return hashset;
	}

	public Map<StatBase, TupleIntJsonSerializable> parseJson(String parString1) {
		JSONObject jsonobject = null;
		try {
			jsonobject = new JSONObject(parString1);
		} catch (JSONException ex) {
		}
		if (jsonobject == null) {
			return Maps.newHashMap();
		} else {
			HashMap hashmap = Maps.newHashMap();

			for (Entry<String, Object> entry : jsonobject.toMap().entrySet()) {
				StatBase statbase = StatList.getOneShotStat((String) entry.getKey());
				if (statbase != null) {
					TupleIntJsonSerializable tupleintjsonserializable = new TupleIntJsonSerializable();
					if (entry.getValue() instanceof Integer) {
						tupleintjsonserializable.setIntegerValue((Integer) entry.getValue());
					} else if (entry.getValue() instanceof JSONObject) {
						JSONObject jsonobject1 = (JSONObject) entry.getValue();
						Object value = jsonobject1.opt("value");
						if (value != null && (value instanceof Integer)) {
							tupleintjsonserializable.setIntegerValue(jsonobject1.getInt("value"));
						}

						if (jsonobject1.has("progress") && statbase.func_150954_l() != null) {
							try {
								IJsonSerializable ijsonserializable = (IJsonSerializable) statbase.func_150954_l_ctor()
										.get();
								ijsonserializable.fromJson(jsonobject1.get("progress"));
								tupleintjsonserializable.setJsonSerializableValue(ijsonserializable);
							} catch (Throwable throwable) {
								logger.warn("Invalid statistic progress in " + this.statsFile, throwable);
							}
						}
					}

					hashmap.put(statbase, tupleintjsonserializable);
				} else {
					logger.warn("Invalid statistic in " + this.statsFile + ": Don\'t know what "
							+ (String) entry.getKey() + " is");
				}
			}

			return hashmap;
		}
	}

	public static String dumpJson(Map<StatBase, TupleIntJsonSerializable> parMap) {
		JSONObject jsonobject = new JSONObject();

		for (Entry entry : parMap.entrySet()) {
			if (((TupleIntJsonSerializable) entry.getValue()).getJsonSerializableValue() != null) {
				JSONObject jsonobject1 = new JSONObject();
				jsonobject1.put("value",
						Integer.valueOf(((TupleIntJsonSerializable) entry.getValue()).getIntegerValue()));

				try {
					jsonobject1.put("progress", ((TupleIntJsonSerializable) entry.getValue()).getJsonSerializableValue()
							.getSerializableElement());
				} catch (Throwable throwable) {
					logger.warn("Couldn\'t save statistic " + ((StatBase) entry.getKey()).getStatName()
							+ ": error serializing progress", throwable);
				}

				jsonobject.put(((StatBase) entry.getKey()).statId, jsonobject1);
			} else {
				jsonobject.put(((StatBase) entry.getKey()).statId,
						Integer.valueOf(((TupleIntJsonSerializable) entry.getValue()).getIntegerValue()));
			}
		}

		return jsonobject.toString();
	}

	public void func_150877_d() {
		for (StatBase statbase : this.statsData.keySet()) {
			this.field_150888_e.add(statbase);
		}

	}

	public void func_150876_a(EntityPlayerMP parEntityPlayerMP) {
		int i = this.mcServer.getTickCounter();
		ObjectIntMap<StatBase> hashmap = new ObjectIntHashMap<>();
		if (this.field_150886_g || i - this.field_150885_f > 300) {
			this.field_150885_f = i;

			for (StatBase statbase : this.func_150878_c()) {
				hashmap.put(statbase, this.readStat(statbase));
			}
		}

		parEntityPlayerMP.playerNetServerHandler.sendPacket(new S37PacketStatistics(hashmap));
	}

	public void sendAchievements(EntityPlayerMP player) {
		ObjectIntMap<StatBase> hashmap = new ObjectIntHashMap<>();

		for (int i = 0, l = AchievementList.achievementList.size(); i < l; ++i) {
			Achievement achievement = AchievementList.achievementList.get(i);
			if (this.hasAchievementUnlocked(achievement)) {
				hashmap.put(achievement, this.readStat(achievement));
				this.field_150888_e.remove(achievement);
			}
		}

		player.playerNetServerHandler.sendPacket(new S37PacketStatistics(hashmap));
	}

	public boolean func_150879_e() {
		return this.field_150886_g;
	}
}