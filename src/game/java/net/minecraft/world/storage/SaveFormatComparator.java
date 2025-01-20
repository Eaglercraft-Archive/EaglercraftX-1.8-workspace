package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSettings;

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
public class SaveFormatComparator implements Comparable<SaveFormatComparator> {
	private final String fileName;
	private final String displayName;
	private final long lastTimePlayed;
	private final long sizeOnDisk;
	private final boolean requiresConversion;
	private final WorldSettings.GameType theEnumGameType;
	private final boolean hardcore;
	private final boolean cheatsEnabled;
	public final NBTTagCompound levelDat;

	public SaveFormatComparator(String fileNameIn, String displayNameIn, long lastTimePlayedIn, long sizeOnDiskIn,
			WorldSettings.GameType theEnumGameTypeIn, boolean requiresConversionIn, boolean hardcoreIn,
			boolean cheatsEnabledIn, NBTTagCompound levelDat) {
		this.fileName = fileNameIn;
		this.displayName = displayNameIn;
		this.lastTimePlayed = lastTimePlayedIn;
		this.sizeOnDisk = sizeOnDiskIn;
		this.theEnumGameType = theEnumGameTypeIn;
		this.requiresConversion = requiresConversionIn;
		this.hardcore = hardcoreIn;
		this.cheatsEnabled = cheatsEnabledIn;
		this.levelDat = levelDat;
	}

	/**+
	 * return the file name
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**+
	 * return the display name of the save
	 */
	public String getDisplayName() {
		return this.displayName;
	}

	public long getSizeOnDisk() {
		return this.sizeOnDisk;
	}

	public boolean requiresConversion() {
		return this.requiresConversion;
	}

	public long getLastTimePlayed() {
		return this.lastTimePlayed;
	}

	public int compareTo(SaveFormatComparator saveformatcomparator) {
		return this.lastTimePlayed < saveformatcomparator.lastTimePlayed ? 1
				: (this.lastTimePlayed > saveformatcomparator.lastTimePlayed ? -1
						: this.fileName.compareTo(saveformatcomparator.fileName));
	}

	/**+
	 * Gets the EnumGameType.
	 */
	public WorldSettings.GameType getEnumGameType() {
		return this.theEnumGameType;
	}

	public boolean isHardcoreModeEnabled() {
		return this.hardcore;
	}

	/**+
	 * @return {@code true} if cheats are enabled for this world
	 */
	public boolean getCheatsEnabled() {
		return this.cheatsEnabled;
	}
}