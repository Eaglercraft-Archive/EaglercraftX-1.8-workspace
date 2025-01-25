package net.minecraft.client.multiplayer;

import java.util.List;

import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.LongObjectMap;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;

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
public class ChunkProviderClient implements IChunkProvider {
	private static final Logger logger = LogManager.getLogger();
	private Chunk blankChunk;
	/**+
	 * The mapping between ChunkCoordinates and Chunks that
	 * ChunkProviderClient maintains.
	 */
	private LongObjectMap<Chunk> chunkMapping = new LongObjectHashMap<>();
	/**+
	 * This may have been intended to be an iterable version of all
	 * currently loaded chunks (MultiplayerChunkCache), with
	 * identical contents to chunkMapping's values. However it is
	 * never actually added to.
	 */
	private List<Chunk> chunkListing = Lists.newArrayList();
	private World worldObj;

	public ChunkProviderClient(World worldIn) {
		this.blankChunk = new EmptyChunk(worldIn, 0, 0);
		this.worldObj = worldIn;
	}

	/**+
	 * Checks to see if a chunk exists at x, z
	 */
	public boolean chunkExists(int var1, int var2) {
		return true;
	}

	/**+
	 * Unload chunk from ChunkProviderClient's hashmap. Called in
	 * response to a Packet50PreChunk with its mode field set to
	 * false
	 */
	public void unloadChunk(int parInt1, int parInt2) {
		Chunk chunk = this.provideChunk(parInt1, parInt2);
		if (!chunk.isEmpty()) {
			chunk.onChunkUnload();
		}

		this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(parInt1, parInt2));
		this.chunkListing.remove(chunk);
	}

	/**+
	 * loads or generates the chunk at the chunk location specified
	 */
	public Chunk loadChunk(int parInt1, int parInt2) {
		Chunk chunk = new Chunk(this.worldObj, parInt1, parInt2);
		this.chunkMapping.put(ChunkCoordIntPair.chunkXZ2Int(parInt1, parInt2), chunk);
		this.chunkListing.add(chunk);
		chunk.setChunkLoaded(true);
		return chunk;
	}

	/**+
	 * Will return back a chunk, if it doesn't exist and its not a
	 * MP client it will generates all the blocks for the specified
	 * chunk from the map seed and chunk seed
	 */
	public Chunk provideChunk(int i, int j) {
		Chunk chunk = this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(i, j));
		return chunk == null ? this.blankChunk : chunk;
	}

	/**+
	 * Two modes of operation: if passed true, save all Chunks in
	 * one go. If passed false, save up to two chunks. Return true
	 * if all chunks have been saved.
	 */
	public boolean saveChunks(boolean var1, IProgressUpdate var2) {
		return true;
	}

	/**+
	 * Save extra data not associated with any Chunk. Not saved
	 * during autosave, only during world unload. Currently
	 * unimplemented.
	 */
	public void saveExtraData() {
	}

	/**+
	 * Unloads chunks that are marked to be unloaded. This is not
	 * guaranteed to unload every such chunk.
	 */
	public boolean unloadQueuedChunks() {
		long i = EagRuntime.steadyTimeMillis();

		for (int j = 0, k = this.chunkListing.size(); j < k; ++j) {
			this.chunkListing.get(j).func_150804_b(EagRuntime.steadyTimeMillis() - i > 5L);
		}

		if (EagRuntime.steadyTimeMillis() - i > 100L) {
			logger.info("Warning: Clientside chunk ticking took {} ms",
					new Object[] { Long.valueOf(EagRuntime.steadyTimeMillis() - i) });
		}

		return false;
	}

	/**+
	 * Returns if the IChunkProvider supports saving.
	 */
	public boolean canSave() {
		return false;
	}

	/**+
	 * Populates chunk with ores etc etc
	 */
	public void populate(IChunkProvider var1, int var2, int var3) {
	}

	public boolean func_177460_a(IChunkProvider var1, Chunk var2, int var3, int var4) {
		return false;
	}

	/**+
	 * Converts the instance data to a readable string.
	 */
	public String makeString() {
		return "MultiplayerChunkCache: " + this.chunkMapping.size() + ", " + this.chunkListing.size();
	}

	public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType var1, BlockPos var2) {
		return null;
	}

	public BlockPos getStrongholdGen(World var1, String var2, BlockPos var3) {
		return null;
	}

	public int getLoadedChunkCount() {
		return this.chunkListing.size();
	}

	public void recreateStructures(Chunk var1, int var2, int var3) {
	}

	/**+
	 * Will return back a chunk, if it doesn't exist and its not a
	 * MP client it will generates all the blocks for the specified
	 * chunk from the map seed and chunk seed
	 */
	public Chunk provideChunk(BlockPos blockpos) {
		return this.provideChunk(blockpos.getX() >> 4, blockpos.getZ() >> 4);
	}

	public Chunk getLoadedChunk(int var1, int var2) {
		return this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
	}
}