package net.minecraft.world.storage;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.server.WorldsDB;

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
public class SaveHandler implements ISaveHandler, IPlayerFileData {
	private static final Logger logger = LogManager.getLogger();
	private final VFile2 worldDirectory;
	private final VFile2 playersDirectory;
	private final VFile2 mapDataDir;
	/**+
	 * The time in milliseconds when this field was initialized.
	 * Stored in the session lock file.
	 */
	private final long initializationTime = MinecraftServer.getCurrentTimeMillis();
	private final String saveDirectoryName;

	public SaveHandler(VFile2 savesDirectory, String directoryName) {
		this.worldDirectory = WorldsDB.newVFile(savesDirectory, directoryName);
		this.playersDirectory = WorldsDB.newVFile(this.worldDirectory, "player");
		this.mapDataDir = WorldsDB.newVFile(this.worldDirectory, "data");
		this.saveDirectoryName = directoryName;

	}

	/**+
	 * Gets the File object corresponding to the base directory of
	 * this world.
	 */
	public VFile2 getWorldDirectory() {
		return this.worldDirectory;
	}

	/**+
	 * initializes and returns the chunk loader for the specified
	 * world provider
	 */
	public IChunkLoader getChunkLoader(WorldProvider var1) {
		throw new RuntimeException("eagler");
	}

	/**+
	 * Loads and returns the world info
	 */
	public WorldInfo loadWorldInfo() {
		VFile2 file1 = WorldsDB.newVFile(this.worldDirectory, "level.dat");
		if (file1.exists()) {
			try (InputStream is = file1.getInputStream()) {
				NBTTagCompound nbttagcompound2 = CompressedStreamTools.readCompressed(is);
				NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompoundTag("Data");
				return new WorldInfo(nbttagcompound3);
			} catch (Exception exception1) {
				logger.error("Failed to load level.dat!");
				logger.error(exception1);
			}
		}

		file1 = WorldsDB.newVFile(this.worldDirectory, "level.dat_old");
		if (file1.exists()) {
			try (InputStream is = file1.getInputStream()) {
				NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(is);
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
				return new WorldInfo(nbttagcompound1);
			} catch (Exception exception) {
				logger.error("Failed to load level.dat_old!");
				logger.error(exception);
			}
		}

		return null;
	}

	/**+
	 * Saves the given World Info with the given NBTTagCompound as
	 * the Player.
	 */
	public void saveWorldInfoWithPlayer(WorldInfo worldinfo, NBTTagCompound nbttagcompound) {
		NBTTagCompound nbttagcompound1 = worldinfo.cloneNBTCompound(nbttagcompound);
		NBTTagCompound nbttagcompound2 = new NBTTagCompound();
		nbttagcompound2.setTag("Data", nbttagcompound1);

		try {
			VFile2 file1 = WorldsDB.newVFile(this.worldDirectory, "level.dat_new");
			VFile2 file2 = WorldsDB.newVFile(this.worldDirectory, "level.dat_old");
			VFile2 file3 = WorldsDB.newVFile(this.worldDirectory, "level.dat");
			try (OutputStream os = file1.getOutputStream()) {
				CompressedStreamTools.writeCompressed(nbttagcompound2, os);
			}
			if (file2.exists()) {
				file2.delete();
			}

			file3.renameTo(file2);
			if (file3.exists()) {
				file3.delete();
			}

			file1.renameTo(file3);
			if (file1.exists()) {
				file1.delete();
			}
		} catch (Exception exception) {
			logger.error("Failed to write level.dat!");
			logger.error(exception);
		}

	}

	/**+
	 * used to update level.dat from old format to MCRegion format
	 */
	public void saveWorldInfo(WorldInfo worldInformation) {
		NBTTagCompound nbttagcompound = worldInformation.getNBTTagCompound();
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setTag("Data", nbttagcompound);

		try {
			VFile2 file1 = WorldsDB.newVFile(this.worldDirectory, "level.dat_new");
			VFile2 file2 = WorldsDB.newVFile(this.worldDirectory, "level.dat_old");
			VFile2 file3 = WorldsDB.newVFile(this.worldDirectory, "level.dat");
			try (OutputStream os = file1.getOutputStream()) {
				CompressedStreamTools.writeCompressed(nbttagcompound1, os);
			}
			if (file2.exists()) {
				file2.delete();
			}

			file3.renameTo(file2);
			if (file3.exists()) {
				file3.delete();
			}

			file1.renameTo(file3);
			if (file1.exists()) {
				file1.delete();
			}
		} catch (Exception exception) {
			logger.error("Failed to write level.dat!");
			logger.error(exception);
		}

	}

	/**+
	 * Writes the player data to disk from the specified
	 * PlayerEntityMP.
	 */
	public void writePlayerData(EntityPlayer player) {
		try {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			player.writeToNBT(nbttagcompound);
			String s = player.getName().toLowerCase();
			VFile2 file1 = WorldsDB.newVFile(this.playersDirectory, s + ".dat.tmp");
			VFile2 file2 = WorldsDB.newVFile(this.playersDirectory, s + ".dat");
			try (OutputStream os = file1.getOutputStream()) {
				CompressedStreamTools.writeCompressed(nbttagcompound, os);
			}
			if (file2.exists()) {
				file2.delete();
			}

			file1.renameTo(file2);
		} catch (Exception var5) {
			logger.error("Failed to save player data for {}", player.getName());
			logger.error(var5);
		}

	}

	/**+
	 * Reads the player data from disk into the specified
	 * PlayerEntityMP.
	 */
	public NBTTagCompound readPlayerData(EntityPlayer player) {
		NBTTagCompound nbttagcompound = null;

		try {
			VFile2 file1 = WorldsDB.newVFile(this.playersDirectory, player.getName().toLowerCase() + ".dat");
			if (file1.exists()) {
				try (InputStream is = file1.getInputStream()) {
					nbttagcompound = CompressedStreamTools.readCompressed(is);
				}
			}
		} catch (Exception var4) {
			logger.error("Failed to load player data for {}", player.getName());
			logger.error(var4);
		}

		if (nbttagcompound != null) {
			player.readFromNBT(nbttagcompound);
		}

		return nbttagcompound;
	}

	public IPlayerFileData getPlayerNBTManager() {
		return this;
	}

	/**+
	 * Returns an array of usernames for which player.dat exists
	 * for.
	 */
	public String[] getAvailablePlayerDat() {
		List<String> astring = this.playersDirectory.listFilenames(false);

		for (int i = 0, l = astring.size(); i < l; ++i) {
			String str = astring.get(i);
			if (str.endsWith(".dat")) {
				astring.set(i, str.substring(0, str.length() - 4));
			}
		}

		return astring.toArray(new String[astring.size()]);
	}

	/**+
	 * Called to flush all changes to disk, waiting for them to
	 * complete.
	 */
	public void flush() {
	}

	/**+
	 * Gets the file location of the given map
	 */
	public VFile2 getMapFileFromName(String mapName) {
		return WorldsDB.newVFile(this.mapDataDir, mapName + ".dat");
	}

	/**+
	 * Returns the name of the directory where world information is
	 * saved.
	 */
	public String getWorldDirectoryName() {
		return this.saveDirectoryName;
	}
}