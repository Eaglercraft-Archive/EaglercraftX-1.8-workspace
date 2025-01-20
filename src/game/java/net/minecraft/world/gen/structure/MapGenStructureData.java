package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
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
public class MapGenStructureData extends WorldSavedData {
	private NBTTagCompound tagCompound = new NBTTagCompound();

	public MapGenStructureData(String name) {
		super(name);
	}

	/**+
	 * reads in data from the NBTTagCompound into this MapDataBase
	 */
	public void readFromNBT(NBTTagCompound nbt) {
		this.tagCompound = nbt.getCompoundTag("Features");
	}

	/**+
	 * write data to NBTTagCompound from this MapDataBase, similar
	 * to Entities and TileEntities
	 */
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("Features", this.tagCompound);
	}

	/**+
	 * Writes the NBT tag of an instance of this structure type to
	 * the internal NBT tag, using the chunkcoordinates as the key
	 */
	public void writeInstance(NBTTagCompound tagCompoundIn, int chunkX, int chunkZ) {
		this.tagCompound.setTag(formatChunkCoords(chunkX, chunkZ), tagCompoundIn);
	}

	public static String formatChunkCoords(int chunkX, int chunkZ) {
		return "[" + chunkX + "," + chunkZ + "]";
	}

	public NBTTagCompound getTagCompound() {
		return this.tagCompound;
	}
}