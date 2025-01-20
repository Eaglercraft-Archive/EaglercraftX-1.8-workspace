package net.minecraft.world;

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
public class WorldType {
	/**+
	 * List of world types.
	 */
	public static final WorldType[] worldTypes = new WorldType[16];
	/**+
	 * Default world type.
	 */
	public static final WorldType DEFAULT = (new WorldType(0, "default", 1)).setVersioned();
	/**+
	 * Flat world type.
	 */
	public static final WorldType FLAT = new WorldType(1, "flat");
	/**+
	 * Large Biome world Type.
	 */
	public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");
	/**+
	 * amplified world type
	 */
	public static final WorldType AMPLIFIED = (new WorldType(3, "amplified")).setNotificationData();
	public static final WorldType CUSTOMIZED = new WorldType(4, "customized");
	public static final WorldType DEBUG_WORLD = new WorldType(5, "debug_all_block_states");
	/**+
	 * Default (1.1) world type.
	 */
	public static final WorldType DEFAULT_1_1 = (new WorldType(8, "default_1_1", 0)).setCanBeCreated(false);
	private final int worldTypeId;
	private final String worldType;
	private final int generatorVersion;
	private boolean canBeCreated;
	private boolean isWorldTypeVersioned;
	private boolean hasNotificationData;

	private WorldType(int id, String name) {
		this(id, name, 0);
	}

	private WorldType(int id, String name, int version) {
		this.worldType = name;
		this.generatorVersion = version;
		this.canBeCreated = true;
		this.worldTypeId = id;
		worldTypes[id] = this;
	}

	public String getWorldTypeName() {
		return this.worldType;
	}

	/**+
	 * Gets the translation key for the name of this world type.
	 */
	public String getTranslateName() {
		return "generator." + this.worldType;
	}

	public String func_151359_c() {
		return this.getTranslateName() + ".info";
	}

	/**+
	 * Returns generatorVersion.
	 */
	public int getGeneratorVersion() {
		return this.generatorVersion;
	}

	public WorldType getWorldTypeForGeneratorVersion(int version) {
		return this == DEFAULT && version == 0 ? DEFAULT_1_1 : this;
	}

	/**+
	 * Sets canBeCreated to the provided value, and returns this.
	 */
	private WorldType setCanBeCreated(boolean enable) {
		this.canBeCreated = enable;
		return this;
	}

	/**+
	 * Gets whether this WorldType can be used to generate a new
	 * world.
	 */
	public boolean getCanBeCreated() {
		return this.canBeCreated;
	}

	/**+
	 * Flags this world type as having an associated version.
	 */
	private WorldType setVersioned() {
		this.isWorldTypeVersioned = true;
		return this;
	}

	/**+
	 * Returns true if this world Type has a version associated with
	 * it.
	 */
	public boolean isVersioned() {
		return this.isWorldTypeVersioned;
	}

	public static WorldType parseWorldType(String type) {
		for (int i = 0; i < worldTypes.length; ++i) {
			if (worldTypes[i] != null && worldTypes[i].worldType.equalsIgnoreCase(type)) {
				return worldTypes[i];
			}
		}

		return null;
	}

	public int getWorldTypeID() {
		return this.worldTypeId;
	}

	/**+
	 * returns true if selecting this worldtype from the customize
	 * menu should display the generator.[worldtype].info message
	 */
	public boolean showWorldInfoNotice() {
		return this.hasNotificationData;
	}

	/**+
	 * enables the display of generator.[worldtype].info message on
	 * the customize world menu
	 */
	private WorldType setNotificationData() {
		this.hasNotificationData = true;
		return this;
	}
}