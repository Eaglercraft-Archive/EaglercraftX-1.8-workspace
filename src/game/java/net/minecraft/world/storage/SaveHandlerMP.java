package net.minecraft.world.storage;

import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

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
public class SaveHandlerMP implements ISaveHandler {
	/**+
	 * Loads and returns the world info
	 */
	public WorldInfo loadWorldInfo() {
		return null;
	}

	/**+
	 * Saves the given World Info with the given NBTTagCompound as
	 * the Player.
	 */
	public void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2) {
	}

	/**+
	 * used to update level.dat from old format to MCRegion format
	 */
	public void saveWorldInfo(WorldInfo var1) {
	}

	public IPlayerFileData getPlayerNBTManager() {
		return null;
	}

	/**+
	 * Called to flush all changes to disk, waiting for them to
	 * complete.
	 */
	public void flush() {
	}

	/**+
	 * Returns the name of the directory where world information is
	 * saved.
	 */
	public String getWorldDirectoryName() {
		return "none";
	}

	@Override
	/**+
	 * initializes and returns the chunk loader for the specified
	 * world provider
	 */
	public IChunkLoader getChunkLoader(WorldProvider var1) {
		return null;
	}

	@Override
	/**+
	 * Gets the File object corresponding to the base directory of
	 * this world.
	 */
	public VFile2 getWorldDirectory() {
		return null;
	}

	@Override
	/**+
	 * Gets the file location of the given map
	 */
	public VFile2 getMapFileFromName(String var1) {
		return null;
	}

}