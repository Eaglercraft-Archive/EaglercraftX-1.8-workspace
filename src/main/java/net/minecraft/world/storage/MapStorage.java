package net.minecraft.world.storage;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.world.WorldSavedData;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
		return (WorldSavedData) this.loadedDataMap.get(s);
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
			((WorldSavedData) this.loadedDataList.get(i)).setDirty(false);
		}

	}

	/**+
	 * Loads the idCounts Map from the 'idcounts' file.
	 */
	private void loadIdCounts() {
		this.idCounts.clear();
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
		return oshort.shortValue();
	}
}