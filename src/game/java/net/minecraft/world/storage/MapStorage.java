package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagShort;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.gen.structure.MapGenStructureData;

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
public class MapStorage {
	private ISaveHandler saveHandler;
	protected Map<String, WorldSavedData> loadedDataMap = Maps.newHashMap();
	/**+
	 * List of loaded MapDataBases.
	 */
	private List<WorldSavedData> loadedDataList = Lists.newArrayList();
	private Map<String, Short> idCounts = Maps.newHashMap();

	public static interface MapStorageProvider {
		WorldSavedData createInstance(String mapFileName);
	}

	public static final Map<Class<? extends WorldSavedData>, MapStorageProvider> storageProviders = new HashMap<>();

	static {
		storageProviders.put(MapData.class, MapData::new);
		storageProviders.put(MapGenStructureData.class, MapGenStructureData::new);
		storageProviders.put(ScoreboardSaveData.class, ScoreboardSaveData::new);
		storageProviders.put(VillageCollection.class, VillageCollection::new);
	}

	public MapStorage(ISaveHandler saveHandlerIn) {
		this.saveHandler = saveHandlerIn;
		this.loadIdCounts();
	}

	/**+
	 * Loads an existing MapDataBase corresponding to the given
	 * String id from disk, instantiating the given Class, or
	 * returns null if none such file exists. args: Class to
	 * instantiate, String dataid
	 */
	public WorldSavedData loadData(Class<? extends WorldSavedData> oclass, String s) {
		WorldSavedData worldsaveddata = (WorldSavedData) this.loadedDataMap.get(s);
		if (worldsaveddata != null) {
			return worldsaveddata;
		} else {
			if (this.saveHandler != null) {
				try {
					VFile2 file1 = this.saveHandler.getMapFileFromName(s);
					if (file1 != null && file1.exists()) {
						try {
							worldsaveddata = (WorldSavedData) storageProviders.get(oclass).createInstance(s);
						} catch (Exception exception) {
							throw new RuntimeException("Failed to instantiate " + oclass.toString(), exception);
						}
						try (InputStream is = file1.getInputStream()) {
							NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(is);
							worldsaveddata.readFromNBT(nbttagcompound.getCompoundTag("data"));
						}
					}
				} catch (Exception exception1) {
					EagRuntime.debugPrintStackTrace(exception1);
				}
			}

			if (worldsaveddata != null) {
				this.loadedDataMap.put(s, worldsaveddata);
				this.loadedDataList.add(worldsaveddata);
			}

			return worldsaveddata;
		}
	}

	/**+
	 * Assigns the given String id to the given MapDataBase,
	 * removing any existing ones of the same id.
	 */
	public void setData(String s, WorldSavedData worldsaveddata) {
		if (this.loadedDataMap.containsKey(s)) {
			this.loadedDataList.remove(this.loadedDataMap.remove(s));
		}

		this.loadedDataMap.put(s, worldsaveddata);
		this.loadedDataList.add(worldsaveddata);
	}

	/**+
	 * Saves all dirty loaded MapDataBases to disk.
	 */
	public void saveAllData() {
		for (int i = 0; i < this.loadedDataList.size(); ++i) {
			WorldSavedData worldsaveddata = (WorldSavedData) this.loadedDataList.get(i);
			if (worldsaveddata.isDirty()) {
				this.saveData(worldsaveddata);
				worldsaveddata.setDirty(false);
			}
		}

	}

	/**+
	 * Saves the given MapDataBase to disk.
	 */
	private void saveData(WorldSavedData parWorldSavedData) {
		if (this.saveHandler != null) {
			try {
				VFile2 file1 = this.saveHandler.getMapFileFromName(parWorldSavedData.mapName);
				if (file1 != null) {
					NBTTagCompound nbttagcompound = new NBTTagCompound();
					parWorldSavedData.writeToNBT(nbttagcompound);
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setTag("data", nbttagcompound);

					try (OutputStream fileoutputstream = file1.getOutputStream()) {
						CompressedStreamTools.writeCompressed(nbttagcompound1, fileoutputstream);
					}
				}
			} catch (Exception exception) {
				EagRuntime.debugPrintStackTrace(exception);
			}

		}
	}

	/**+
	 * Loads the idCounts Map from the 'idcounts' file.
	 */
	private void loadIdCounts() {
		try {
			this.idCounts.clear();
			if (this.saveHandler == null) {
				return;
			}

			VFile2 file1 = this.saveHandler.getMapFileFromName("idcounts");
			if (file1 != null && file1.exists()) {
				NBTTagCompound nbttagcompound;
				try (DataInputStream datainputstream = new DataInputStream(file1.getInputStream())) {
					nbttagcompound = CompressedStreamTools.read(datainputstream);
				}

				for (String s : nbttagcompound.getKeySet()) {
					NBTBase nbtbase = nbttagcompound.getTag(s);
					if (nbtbase instanceof NBTTagShort) {
						NBTTagShort nbttagshort = (NBTTagShort) nbtbase;
						short short1 = nbttagshort.getShort();
						this.idCounts.put(s, Short.valueOf(short1));
					}
				}
			}
		} catch (Exception exception) {
			EagRuntime.debugPrintStackTrace(exception);
		}

	}

	/**+
	 * Returns an unique new data id for the given prefix and saves
	 * the idCounts map to the 'idcounts' file.
	 */
	public int getUniqueDataId(String s) {
		Short oshort = (Short) this.idCounts.get(s);
		if (oshort == null) {
			oshort = Short.valueOf((short) 0);
		} else {
			oshort = Short.valueOf((short) (oshort.shortValue() + 1));
		}

		this.idCounts.put(s, oshort);
		if (this.saveHandler == null) {
			return oshort.shortValue();
		} else {
			try {
				VFile2 file1 = this.saveHandler.getMapFileFromName("idcounts");
				if (file1 != null) {
					NBTTagCompound nbttagcompound = new NBTTagCompound();

					for (String s1 : this.idCounts.keySet()) {
						short short1 = ((Short) this.idCounts.get(s1)).shortValue();
						nbttagcompound.setShort(s1, short1);
					}

					try (DataOutputStream dataoutputstream = new DataOutputStream(file1.getOutputStream())) {
						CompressedStreamTools.write(nbttagcompound, (DataOutput) dataoutputstream);
					}
				}
			} catch (Exception exception) {
				EagRuntime.debugPrintStackTrace(exception);
			}

			return oshort.shortValue();
		}
	}
}