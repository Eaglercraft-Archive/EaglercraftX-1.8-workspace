package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.storage.WorldInfo;

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
public final class WorldSettings {
	private final long seed;
	private final WorldSettings.GameType theGameType;
	private final boolean mapFeaturesEnabled;
	private final boolean hardcoreEnabled;
	private final WorldType terrainType;
	private boolean commandsAllowed;
	private boolean bonusChestEnabled;
	private String worldName;

	public WorldSettings(long seedIn, WorldSettings.GameType gameType, boolean enableMapFeatures, boolean hardcoreMode,
			WorldType worldTypeIn) {
		this.worldName = "";
		this.seed = seedIn;
		this.theGameType = gameType;
		this.mapFeaturesEnabled = enableMapFeatures;
		this.hardcoreEnabled = hardcoreMode;
		this.terrainType = worldTypeIn;
	}

	public WorldSettings(WorldInfo info) {
		this(info.getSeed(), info.getGameType(), info.isMapFeaturesEnabled(), info.isHardcoreModeEnabled(),
				info.getTerrainType());
	}

	/**+
	 * Enables the bonus chest.
	 */
	public WorldSettings enableBonusChest() {
		this.bonusChestEnabled = true;
		return this;
	}

	/**+
	 * Enables Commands (cheats).
	 */
	public WorldSettings enableCommands() {
		this.commandsAllowed = true;
		return this;
	}

	public WorldSettings setWorldName(String name) {
		this.worldName = name;
		return this;
	}

	/**+
	 * Returns true if the Bonus Chest is enabled.
	 */
	public boolean isBonusChestEnabled() {
		return this.bonusChestEnabled;
	}

	/**+
	 * Returns the seed for the world.
	 */
	public long getSeed() {
		return this.seed;
	}

	/**+
	 * Gets the game type.
	 */
	public WorldSettings.GameType getGameType() {
		return this.theGameType;
	}

	/**+
	 * Returns true if hardcore mode is enabled, otherwise false
	 */
	public boolean getHardcoreEnabled() {
		return this.hardcoreEnabled;
	}

	/**+
	 * Get whether the map features (e.g. strongholds) generation is
	 * enabled or disabled.
	 */
	public boolean isMapFeaturesEnabled() {
		return this.mapFeaturesEnabled;
	}

	public WorldType getTerrainType() {
		return this.terrainType;
	}

	/**+
	 * Returns true if Commands (cheats) are allowed.
	 */
	public boolean areCommandsAllowed() {
		return this.commandsAllowed;
	}

	/**+
	 * Gets the GameType by ID
	 */
	public static WorldSettings.GameType getGameTypeById(int id) {
		return WorldSettings.GameType.getByID(id);
	}

	public String getWorldName() {
		return this.worldName;
	}

	public static enum GameType {
		NOT_SET(-1, ""), SURVIVAL(0, "survival"), CREATIVE(1, "creative"), ADVENTURE(2, "adventure"),
		SPECTATOR(3, "spectator");

		public static final GameType[] _VALUES = values();

		int id;
		String name;

		private GameType(int typeId, String nameIn) {
			this.id = typeId;
			this.name = nameIn;
		}

		public int getID() {
			return this.id;
		}

		public String getName() {
			return this.name;
		}

		public void configurePlayerCapabilities(PlayerCapabilities capabilities) {
			if (this == CREATIVE) {
				capabilities.allowFlying = true;
				capabilities.isCreativeMode = true;
				capabilities.disableDamage = true;
			} else if (this == SPECTATOR) {
				capabilities.allowFlying = true;
				capabilities.isCreativeMode = false;
				capabilities.disableDamage = true;
				capabilities.isFlying = true;
			} else {
				capabilities.allowFlying = false;
				capabilities.isCreativeMode = false;
				capabilities.disableDamage = false;
				capabilities.isFlying = false;
			}

			capabilities.allowEdit = !this.isAdventure();
		}

		public boolean isAdventure() {
			return this == ADVENTURE || this == SPECTATOR;
		}

		public boolean isCreative() {
			return this == CREATIVE;
		}

		public boolean isSurvivalOrAdventure() {
			return this == SURVIVAL || this == ADVENTURE;
		}

		public static WorldSettings.GameType getByID(int idIn) {
			WorldSettings.GameType[] types = _VALUES;
			for (int i = 0; i < types.length; ++i) {
				WorldSettings.GameType worldsettings$gametype = types[i];
				if (worldsettings$gametype.id == idIn) {
					return worldsettings$gametype;
				}
			}

			return SURVIVAL;
		}

		public static WorldSettings.GameType getByName(String parString1) {
			WorldSettings.GameType[] types = _VALUES;
			for (int i = 0; i < types.length; ++i) {
				WorldSettings.GameType worldsettings$gametype = types[i];
				if (worldsettings$gametype.name.equals(parString1)) {
					return worldsettings$gametype;
				}
			}

			return SURVIVAL;
		}
	}
}