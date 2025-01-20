package net.minecraft.world.biome;

import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.LongObjectMap;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.MinecraftServer;

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
public class BiomeCache {
	private final WorldChunkManager chunkManager;
	private long lastCleanupTime;
	/**+
	 * The map of keys to BiomeCacheBlocks. Keys are based on the
	 * chunk x, z coordinates as (x | z << 32).
	 */
	private LongObjectMap<BiomeCache.Block> cacheMap = new LongObjectHashMap<>();
	/**+
	 * The list of cached BiomeCacheBlocks
	 */
	private List<BiomeCache.Block> cache = Lists.newArrayList();

	public BiomeCache(WorldChunkManager chunkManagerIn) {
		this.chunkManager = chunkManagerIn;
	}

	/**+
	 * Returns a biome cache block at location specified.
	 */
	public BiomeCache.Block getBiomeCacheBlock(int x, int z) {
		x = x >> 4;
		z = z >> 4;
		long i = (long) x & 4294967295L | ((long) z & 4294967295L) << 32;
		BiomeCache.Block biomecache$block = this.cacheMap.get(i);
		if (biomecache$block == null) {
			biomecache$block = new BiomeCache.Block(x, z);
			this.cacheMap.put(i, biomecache$block);
			this.cache.add(biomecache$block);
		}

		biomecache$block.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
		return biomecache$block;
	}

	public BiomeGenBase func_180284_a(int x, int z, BiomeGenBase parBiomeGenBase) {
		BiomeGenBase biomegenbase = this.getBiomeCacheBlock(x, z).getBiomeGenAt(x, z);
		return biomegenbase == null ? parBiomeGenBase : biomegenbase;
	}

	/**+
	 * Removes BiomeCacheBlocks from this cache that haven't been
	 * accessed in at least 30 seconds.
	 */
	public void cleanupCache() {
		long i = MinecraftServer.getCurrentTimeMillis();
		long j = i - this.lastCleanupTime;
		if (j > 7500L || j < 0L) {
			this.lastCleanupTime = i;

			for (int k = 0; k < this.cache.size(); ++k) {
				BiomeCache.Block biomecache$block = this.cache.get(k);
				long l = i - biomecache$block.lastAccessTime;
				if (l > 30000L || l < 0L) {
					this.cache.remove(k--);
					long i1 = (long) biomecache$block.xPosition & 4294967295L
							| ((long) biomecache$block.zPosition & 4294967295L) << 32;
					this.cacheMap.remove(i1);
				}
			}
		}

	}

	/**+
	 * Returns the array of cached biome types in the
	 * BiomeCacheBlock at the given location.
	 */
	public BiomeGenBase[] getCachedBiomes(int x, int z) {
		return this.getBiomeCacheBlock(x, z).biomes;
	}

	public class Block {
		public float[] rainfallValues = new float[256];
		public BiomeGenBase[] biomes = new BiomeGenBase[256];
		public int xPosition;
		public int zPosition;
		public long lastAccessTime;

		public Block(int x, int z) {
			this.xPosition = x;
			this.zPosition = z;
			BiomeCache.this.chunkManager.getRainfall(this.rainfallValues, x << 4, z << 4, 16, 16);
			BiomeCache.this.chunkManager.getBiomeGenAt(this.biomes, x << 4, z << 4, 16, 16, false);
		}

		public BiomeGenBase getBiomeGenAt(int x, int z) {
			return this.biomes[x & 15 | (z & 15) << 4];
		}
	}
}