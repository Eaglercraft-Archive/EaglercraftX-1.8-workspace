package net.minecraft.world.storage;

import com.google.common.collect.Lists;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;
import net.lax1dude.eaglercraft.v1_8.sp.server.WorldsDB;
import net.minecraft.util.IProgressUpdate;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
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
public class SaveFormatOld implements ISaveFormat {
	private static final Logger logger = LogManager.getLogger();
	protected final VFile2 savesDirectory;

	public SaveFormatOld(VFile2 parFile) {
		this.savesDirectory = parFile;
	}

	/**+
	 * Returns the name of the save format.
	 */
	public String getName() {
		return "Old Format";
	}

	public List<SaveFormatComparator> getSaveList() {
		ArrayList arraylist = Lists.newArrayList();

		for (int i = 0; i < 5; ++i) {
			String s = "World" + (i + 1);
			WorldInfo worldinfo = this.getWorldInfo(s);
			if (worldinfo != null) {
				arraylist.add(new SaveFormatComparator(s, "", worldinfo.getLastTimePlayed(), worldinfo.getSizeOnDisk(),
						worldinfo.getGameType(), false, worldinfo.isHardcoreModeEnabled(),
						worldinfo.areCommandsAllowed(), null));
			}
		}

		return arraylist;
	}

	public void flushCache() {
	}

	/**+
	 * Returns the world's WorldInfo object
	 */
	public WorldInfo getWorldInfo(String saveName) {
		VFile2 file1 = WorldsDB.newVFile(this.savesDirectory, saveName);
		VFile2 file2 = WorldsDB.newVFile(file1, "level.dat");
		if (file2.exists()) {
			try {
				NBTTagCompound nbttagcompound2;
				try (InputStream is = file2.getInputStream()) {
					nbttagcompound2 = CompressedStreamTools.readCompressed(is);
				}
				NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompoundTag("Data");
				return new WorldInfo(nbttagcompound3);
			} catch (Exception exception1) {
				logger.error("Exception reading " + file2);
				logger.error(exception1);
			}
		}

		file2 = WorldsDB.newVFile(file1, "level.dat_old");
		if (file2.exists()) {
			try {
				NBTTagCompound nbttagcompound;
				try (InputStream is = file2.getInputStream()) {
					nbttagcompound = CompressedStreamTools.readCompressed(is);
				}
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
				return new WorldInfo(nbttagcompound1);
			} catch (Exception exception) {
				logger.error("Exception reading " + file2);
				logger.error(exception);
			}
		}

		return null;
	}

	/**+
	 * Renames the world by storing the new name in level.dat. It
	 * does *not* rename the directory containing the world data.
	 */
	public boolean renameWorld(String dirName, String newName) {
		VFile2 file1 = WorldsDB.newVFile(this.savesDirectory, dirName);
		VFile2 file2 = WorldsDB.newVFile(file1, "level.dat");
		{
			if (file2.exists()) {
				try {
					NBTTagCompound nbttagcompound;
					try (InputStream is = file2.getInputStream()) {
						nbttagcompound = CompressedStreamTools.readCompressed(is);
					}
					NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
					nbttagcompound1.setString("LevelName", newName);
					try (OutputStream os = file2.getOutputStream()) {
						CompressedStreamTools.writeCompressed(nbttagcompound, os);
					}
					return true;
				} catch (Throwable exception) {
					logger.error("Failed to rename world \"{}\"!", dirName);
					logger.error(exception);
				}
			}

		}
		return false;
	}

	public boolean func_154335_d(String parString1) {
		return !canLoadWorld(parString1);
	}

	/**+
	 * @args: Takes one argument - the name of the directory of the
	 * world to delete. @desc: Delete the world by deleting the
	 * associated directory recursively.
	 */
	public boolean deleteWorldDirectory(String parString1) {
		VFile2 file1 = WorldsDB.newVFile(this.savesDirectory, parString1);
		logger.info("Deleting level " + parString1);

		for (int i = 1; i <= 5; ++i) {
			logger.info("Attempt " + i + "...");
			if (deleteFiles(file1.listFiles(true), "singleplayer.busy.deleting")) {
				return true;
			}

			logger.warn("Unsuccessful in deleting contents.");
			if (i < 5) {
				EagUtils.sleep(500);
			}
		}

		return false;
	}

	/**+
	 * @args: Takes one argument - the list of files and directories
	 * to delete. @desc: Deletes the files and directory listed in
	 * the list recursively.
	 */
	protected static boolean deleteFiles(List<VFile2> files, String progressString) {
		long totalSize = 0l;
		long lastUpdate = 0;
		for (int i = 0, l = files.size(); i < l; ++i) {
			VFile2 file1 = files.get(i);
			if (progressString != null) {
				totalSize += file1.length();
				if (totalSize - lastUpdate > 10000) {
					lastUpdate = totalSize;
					EaglerIntegratedServerWorker.sendProgress(progressString, totalSize);
				}
			}
			if (!file1.delete()) {
				logger.warn("Couldn\'t delete file " + file1);
				return false;
			}
		}

		return true;
	}

	/**+
	 * Returns back a loader for the specified save directory
	 */
	public ISaveHandler getSaveLoader(String s, boolean flag) {
		return new SaveHandler(this.savesDirectory, s);
	}

	public boolean func_154334_a(String var1) {
		return false;
	}

	/**+
	 * gets if the map is old chunk saving (true) or McRegion
	 * (false)
	 */
	public boolean isOldMapFormat(String var1) {
		return false;
	}

	/**+
	 * converts the map to mcRegion
	 */
	public boolean convertMapFormat(String var1, IProgressUpdate var2) {
		return false;
	}

	/**+
	 * Return whether the given world can be loaded.
	 */
	public boolean canLoadWorld(String parString1) {
		return (WorldsDB.newVFile(this.savesDirectory, parString1, "level.dat")).exists()
				|| (WorldsDB.newVFile(this.savesDirectory, parString1, "level.dat_old")).exists();
	}
}