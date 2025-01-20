package net.minecraft.world.storage;

import net.minecraft.world.WorldSavedData;

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
public class SaveDataMemoryStorage extends MapStorage {
	public SaveDataMemoryStorage() {
		super((ISaveHandler) null);
	}

	/**+
	 * Loads an existing MapDataBase corresponding to the given
	 * String id from disk, instantiating the given Class, or
	 * returns null if none such file exists. args: Class to
	 * instantiate, String dataid
	 */
	public WorldSavedData loadData(Class<? extends WorldSavedData> clazz, String dataIdentifier) {
		return (WorldSavedData) this.loadedDataMap.get(dataIdentifier);
	}

	/**+
	 * Assigns the given String id to the given MapDataBase,
	 * removing any existing ones of the same id.
	 */
	public void setData(String dataIdentifier, WorldSavedData data) {
		this.loadedDataMap.put(dataIdentifier, data);
	}

	/**+
	 * Saves all dirty loaded MapDataBases to disk.
	 */
	public void saveAllData() {
	}

	/**+
	 * Returns an unique new data id for the given prefix and saves
	 * the idCounts map to the 'idcounts' file.
	 */
	public int getUniqueDataId(String key) {
		return 0;
	}
}