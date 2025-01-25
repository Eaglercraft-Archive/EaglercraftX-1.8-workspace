package net.minecraft.world.gen;

import com.carrotsearch.hppc.LongHashSet;
import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.LongObjectMap;
import com.carrotsearch.hppc.LongSet;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.lax1dude.eaglercraft.v1_8.HString;
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
public class ChunkProviderServer implements IChunkProvider {
	private static final Logger logger = LogManager.getLogger();
	private LongSet droppedChunksSet = new LongHashSet();
	private Chunk dummyChunk;
	private IChunkProvider serverChunkGenerator;
	private IChunkLoader chunkLoader;
	/**+
	 * if set, this flag forces a request to load a chunk to load
	 * the chunk rather than defaulting to the dummy if possible
	 */
	public boolean chunkLoadOverride = true;
	/**+
	 * map of chunk Id's to Chunk instances
	 */
	private LongObjectMap<Chunk> id2ChunkMap = new LongObjectHashMap<>();
	private List<Chunk> loadedChunks = Lists.newLinkedList();
	private WorldServer worldObj;

	public ChunkProviderServer(WorldServer parWorldServer, IChunkLoader parIChunkLoader,
			IChunkProvider parIChunkProvider) {
		this.dummyChunk = new EmptyChunk(parWorldServer, Integer.MIN_VALUE, Integer.MIN_VALUE);
		this.worldObj = parWorldServer;
		this.chunkLoader = parIChunkLoader;
		this.serverChunkGenerator = parIChunkProvider;
	}

	/**+
	 * Checks to see if a chunk exists at x, z
	 */
	public boolean chunkExists(int i, int j) {
		return this.id2ChunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(i, j));
	}

	public List<Chunk> func_152380_a() {
		return this.loadedChunks;
	}

	public void dropChunk(int parInt1, int parInt2) {
		if (this.worldObj.provider.canRespawnHere()) {
			if (!this.worldObj.isSpawnChunk(parInt1, parInt2)) {
				this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(parInt1, parInt2));
			}
		} else {
			this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(parInt1, parInt2));
		}

	}

	/**+
	 * marks all chunks for unload, ignoring those near the spawn
	 */
	public void unloadAllChunks() {
		for (Chunk chunk : this.loadedChunks) {
			this.dropChunk(chunk.xPosition, chunk.zPosition);
		}

	}

	/**+
	 * loads or generates the chunk at the chunk location specified
	 */
	public Chunk loadChunk(int i, int j) {
		long k = ChunkCoordIntPair.chunkXZ2Int(i, j);
		this.droppedChunksSet.removeAll(k);
		Chunk chunk = this.id2ChunkMap.get(k);
		if (chunk == null) {
			chunk = this.loadChunkFromFile(i, j);
			if (chunk == null) {
				if (this.serverChunkGenerator == null) {
					chunk = this.dummyChunk;
				} else {
					try {
						chunk = this.serverChunkGenerator.provideChunk(i, j);
						++EaglerMinecraftServer.counterChunkGenerate;
					} catch (Throwable throwable) {
						CrashReport crashreport = CrashReport.makeCrashReport(throwable,
								"Exception generating new chunk");
						CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
						crashreportcategory.addCrashSection("Location",
								HString.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));
						crashreportcategory.addCrashSection("Position hash", Long.valueOf(k));
						crashreportcategory.addCrashSection("Generator", this.serverChunkGenerator.makeString());
						throw new ReportedException(crashreport);
					}
				}
			} else {
				++EaglerMinecraftServer.counterChunkRead;
			}

			this.id2ChunkMap.put(k, chunk);
			this.loadedChunks.add(chunk);
			chunk.onChunkLoad();
			chunk.populateChunk(this, this, i, j);
		}

		return chunk;
	}

	/**+
	 * Will return back a chunk, if it doesn't exist and its not a
	 * MP client it will generates all the blocks for the specified
	 * chunk from the map seed and chunk seed
	 */
	public Chunk provideChunk(int i, int j) {
		Chunk chunk = this.id2ChunkMap.get(ChunkCoordIntPair.chunkXZ2Int(i, j));
		return chunk == null ? (!this.worldObj.isFindingSpawnPoint() && !this.chunkLoadOverride ? this.dummyChunk
				: this.loadChunk(i, j)) : chunk;
	}

	private Chunk loadChunkFromFile(int x, int z) {
		if (this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk chunk = this.chunkLoader.loadChunk(this.worldObj, x, z);
				if (chunk != null) {
					chunk.setLastSaveTime(this.worldObj.getTotalWorldTime());
					if (this.serverChunkGenerator != null) {
						this.serverChunkGenerator.recreateStructures(chunk, x, z);
					}
				}

				return chunk;
			} catch (Exception exception) {
				logger.error("Couldn\'t load chunk");
				logger.error(exception);
				return null;
			}
		}
	}

	private void saveChunkExtraData(Chunk parChunk) {
		if (this.chunkLoader != null) {
			try {
				this.chunkLoader.saveExtraChunkData(this.worldObj, parChunk);
			} catch (Exception exception) {
				logger.error("Couldn\'t save entities");
				logger.error(exception);
			}

		}
	}

	private void saveChunkData(Chunk parChunk) {
		if (this.chunkLoader != null) {
			try {
				parChunk.setLastSaveTime(this.worldObj.getTotalWorldTime());
				this.chunkLoader.saveChunk(this.worldObj, parChunk);
				++EaglerMinecraftServer.counterChunkWrite;
			} catch (IOException ioexception) {
				logger.error("Couldn\'t save chunk");
				logger.error(ioexception);
			}
		}
	}

	/**+
	 * Populates chunk with ores etc etc
	 */
	public void populate(IChunkProvider ichunkprovider, int i, int j) {
		Chunk chunk = this.provideChunk(i, j);
		if (!chunk.isTerrainPopulated()) {
			chunk.func_150809_p();
			if (this.serverChunkGenerator != null) {
				this.serverChunkGenerator.populate(ichunkprovider, i, j);
				chunk.setChunkModified();
			}
		}

	}

	public boolean func_177460_a(IChunkProvider ichunkprovider, Chunk chunk, int i, int j) {
		if (this.serverChunkGenerator != null && this.serverChunkGenerator.func_177460_a(ichunkprovider, chunk, i, j)) {
			Chunk chunk1 = this.provideChunk(i, j);
			chunk1.setChunkModified();
			return true;
		} else {
			return false;
		}
	}

	/**+
	 * Two modes of operation: if passed true, save all Chunks in
	 * one go. If passed false, save up to two chunks. Return true
	 * if all chunks have been saved.
	 */
	public boolean saveChunks(boolean flag, IProgressUpdate var2) {
		int i = 0;
		ArrayList arraylist = Lists.newArrayList(this.loadedChunks);

		for (int j = 0, l = arraylist.size(); j < l; ++j) {
			Chunk chunk = (Chunk) arraylist.get(j);
			if (flag) {
				this.saveChunkExtraData(chunk);
			}

			if (chunk.needsSaving(flag)) {
				this.saveChunkData(chunk);
				chunk.setModified(false);
				++i;
				if (i == 24 && !flag) {
					return false;
				}
			}
		}

		return true;
	}

	/**+
	 * Save extra data not associated with any Chunk. Not saved
	 * during autosave, only during world unload. Currently
	 * unimplemented.
	 */
	public void saveExtraData() {
		if (this.chunkLoader != null) {
			this.chunkLoader.saveExtraData();
		}

	}

	/**+
	 * Unloads chunks that are marked to be unloaded. This is not
	 * guaranteed to unload every such chunk.
	 */
	public boolean unloadQueuedChunks() {
		if (!this.worldObj.disableLevelSaving) {
			for (int i = 0; i < 100; ++i) {
				if (!this.droppedChunksSet.isEmpty()) {
					long olong = this.droppedChunksSet.iterator().next().value;
					Chunk chunk = this.id2ChunkMap.get(olong);
					if (chunk != null) {
						chunk.onChunkUnload();
						this.saveChunkData(chunk);
						this.saveChunkExtraData(chunk);
						this.id2ChunkMap.remove(olong);
						this.loadedChunks.remove(chunk);
					}

					this.droppedChunksSet.removeAll(olong);
				}
			}

			if (this.chunkLoader != null) {
				this.chunkLoader.chunkTick();
			}
		}

		return this.serverChunkGenerator.unloadQueuedChunks();
	}

	/**+
	 * Returns if the IChunkProvider supports saving.
	 */
	public boolean canSave() {
		return !this.worldObj.disableLevelSaving;
	}

	/**+
	 * Converts the instance data to a readable string.
	 */
	public String makeString() {
		return "ServerChunkCache: " + this.id2ChunkMap.size() + " Drop: " + this.droppedChunksSet.size();
	}

	public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype,
			BlockPos blockpos) {
		return this.serverChunkGenerator.getPossibleCreatures(enumcreaturetype, blockpos);
	}

	public BlockPos getStrongholdGen(World world, String s, BlockPos blockpos) {
		return this.serverChunkGenerator.getStrongholdGen(world, s, blockpos);
	}

	public int getLoadedChunkCount() {
		return this.id2ChunkMap.size();
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
		return this.id2ChunkMap.get(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
	}
}