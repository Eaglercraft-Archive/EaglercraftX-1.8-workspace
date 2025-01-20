package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
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
public interface ISaveHandler {
	/**+
	 * Loads and returns the world info
	 */
	WorldInfo loadWorldInfo();

	/**+
	 * initializes and returns the chunk loader for the specified
	 * world provider
	 */
	IChunkLoader getChunkLoader(WorldProvider var1);

	/**+
	 * Saves the given World Info with the given NBTTagCompound as
	 * the Player.
	 */
	void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2);

	/**+
	 * used to update level.dat from old format to MCRegion format
	 */
	void saveWorldInfo(WorldInfo var1);

	IPlayerFileData getPlayerNBTManager();

	/**+
	 * Called to flush all changes to disk, waiting for them to
	 * complete.
	 */
	void flush();

	/**+
	 * Gets the File object corresponding to the base directory of
	 * this world.
	 */
	VFile2 getWorldDirectory();

	/**+
	 * Gets the file location of the given map
	 */
	VFile2 getMapFileFromName(String var1);

	/**+
	 * Returns the name of the directory where world information is
	 * saved.
	 */
	String getWorldDirectoryName();
}