package net.minecraft.world.storage;

import java.util.List;
import net.minecraft.util.IProgressUpdate;

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
public interface ISaveFormat {
	/**+
	 * Returns the name of the save format.
	 */
	String getName();

	/**+
	 * Returns back a loader for the specified save directory
	 */
	ISaveHandler getSaveLoader(String var1, boolean var2);

	List<SaveFormatComparator> getSaveList();

	void flushCache();

	/**+
	 * Returns the world's WorldInfo object
	 */
	WorldInfo getWorldInfo(String var1);

	boolean func_154335_d(String var1);

	/**+
	 * @args: Takes one argument - the name of the directory of the
	 * world to delete. @desc: Delete the world by deleting the
	 * associated directory recursively.
	 */
	boolean deleteWorldDirectory(String var1);

	/**+
	 * Renames the world by storing the new name in level.dat. It
	 * does *not* rename the directory containing the world data.
	 */
	boolean renameWorld(String var1, String var2);

	boolean func_154334_a(String var1);

	/**+
	 * gets if the map is old chunk saving (true) or McRegion
	 * (false)
	 */
	boolean isOldMapFormat(String var1);

	/**+
	 * converts the map to mcRegion
	 */
	boolean convertMapFormat(String var1, IProgressUpdate var2);

	/**+
	 * Return whether the given world can be loaded.
	 */
	boolean canLoadWorld(String var1);
}