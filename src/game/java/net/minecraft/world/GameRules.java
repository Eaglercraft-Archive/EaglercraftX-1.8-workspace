package net.minecraft.world;

import java.util.Set;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;

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
public class GameRules {
	private TreeMap<String, GameRules.Value> theGameRules = new TreeMap();

	public GameRules() {
		this.addGameRule("doFireTick", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("mobGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("keepInventory", "false", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("doMobSpawning", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("doMobLoot", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("doTileDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("doEntityDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("commandBlockOutput", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("naturalRegeneration", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("doDaylightCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("logAdminCommands", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("showDeathMessages", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("randomTickSpeed", "3", GameRules.ValueType.NUMERICAL_VALUE);
		this.addGameRule("sendCommandFeedback", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("reducedDebugInfo", "false", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("loadSpawnChunks", "false", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("bedSpawnPoint", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("clickToRide", "false", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("clickToSit", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("colorCodes", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("doSignEditing", "true", GameRules.ValueType.BOOLEAN_VALUE);
		this.addGameRule("doWeatherCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
	}

	public void addGameRule(String key, String value, GameRules.ValueType type) {
		this.theGameRules.put(key, new GameRules.Value(value, type));
	}

	public void setOrCreateGameRule(String key, String ruleValue) {
		GameRules.Value gamerules$value = (GameRules.Value) this.theGameRules.get(key);
		if (gamerules$value != null) {
			gamerules$value.setValue(ruleValue);
		} else {
			this.addGameRule(key, ruleValue, GameRules.ValueType.ANY_VALUE);
		}

	}

	/**+
	 * Gets the string Game Rule value.
	 */
	public String getString(String name) {
		GameRules.Value gamerules$value = (GameRules.Value) this.theGameRules.get(name);
		return gamerules$value != null ? gamerules$value.getString() : "";
	}

	/**+
	 * Gets the boolean Game Rule value.
	 */
	public boolean getBoolean(String name) {
		GameRules.Value gamerules$value = (GameRules.Value) this.theGameRules.get(name);
		return gamerules$value != null ? gamerules$value.getBoolean() : false;
	}

	public int getInt(String name) {
		GameRules.Value gamerules$value = (GameRules.Value) this.theGameRules.get(name);
		return gamerules$value != null ? gamerules$value.getInt() : 0;
	}

	/**+
	 * Return the defined game rules as NBT.
	 */
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();

		for (String s : this.theGameRules.keySet()) {
			GameRules.Value gamerules$value = (GameRules.Value) this.theGameRules.get(s);
			nbttagcompound.setString(s, gamerules$value.getString());
		}

		return nbttagcompound;
	}

	/**+
	 * Set defined game rules from NBT.
	 */
	public void readFromNBT(NBTTagCompound nbt) {
		for (String s : nbt.getKeySet()) {
			String s1 = nbt.getString(s);
			this.setOrCreateGameRule(s, s1);
		}

	}

	/**+
	 * Return the defined game rules.
	 */
	public String[] getRules() {
		Set set = this.theGameRules.keySet();
		return (String[]) set.toArray(new String[set.size()]);
	}

	/**+
	 * Return whether the specified game rule is defined.
	 */
	public boolean hasRule(String name) {
		return this.theGameRules.containsKey(name);
	}

	public boolean areSameType(String key, GameRules.ValueType otherValue) {
		GameRules.Value gamerules$value = (GameRules.Value) this.theGameRules.get(key);
		return gamerules$value != null
				&& (gamerules$value.getType() == otherValue || otherValue == GameRules.ValueType.ANY_VALUE);
	}

	static class Value {
		private String valueString;
		private boolean valueBoolean;
		private int valueInteger;
		private double valueDouble;
		private final GameRules.ValueType type;

		public Value(String value, GameRules.ValueType type) {
			this.type = type;
			this.setValue(value);
		}

		public void setValue(String value) {
			this.valueString = value;
			this.valueBoolean = Boolean.parseBoolean(value);
			this.valueInteger = this.valueBoolean ? 1 : 0;

			try {
				this.valueInteger = Integer.parseInt(value);
			} catch (NumberFormatException var4) {
				;
			}

			try {
				this.valueDouble = Double.parseDouble(value);
			} catch (NumberFormatException var3) {
				;
			}

		}

		/**+
		 * Gets the string Game Rule value.
		 */
		public String getString() {
			return this.valueString;
		}

		/**+
		 * Gets the boolean Game Rule value.
		 */
		public boolean getBoolean() {
			return this.valueBoolean;
		}

		public int getInt() {
			return this.valueInteger;
		}

		public GameRules.ValueType getType() {
			return this.type;
		}
	}

	public static enum ValueType {
		ANY_VALUE, BOOLEAN_VALUE, NUMERICAL_VALUE;
	}
}