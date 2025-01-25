package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;

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
public class ChunkProviderFlat implements IChunkProvider {
	private World worldObj;
	private EaglercraftRandom random;
	private final IBlockState[] cachedBlockIDs = new IBlockState[256];
	private final FlatGeneratorInfo flatWorldGenInfo;
	private final List<MapGenStructure> structureGenerators = Lists.newArrayList();
	private final boolean hasDecoration;
	private final boolean hasDungeons;
	private WorldGenLakes waterLakeGenerator;
	private WorldGenLakes lavaLakeGenerator;

	public ChunkProviderFlat(World worldIn, long seed, boolean generateStructures, String flatGeneratorSettings) {
		this.worldObj = worldIn;
		boolean scramble = !worldIn.getWorldInfo().isOldEaglercraftRandom();
		this.random = new EaglercraftRandom(seed, scramble);
		this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(flatGeneratorSettings);
		if (generateStructures) {
			Map map = this.flatWorldGenInfo.getWorldFeatures();
			if (map.containsKey("village")) {
				Map map1 = (Map) map.get("village");
				if (!map1.containsKey("size")) {
					map1.put("size", "1");
				}

				this.structureGenerators.add(new MapGenVillage(map1, scramble));
			}

			if (map.containsKey("biome_1")) {
				this.structureGenerators.add(new MapGenScatteredFeature((Map) map.get("biome_1"), scramble));
			}

			if (map.containsKey("mineshaft")) {
				this.structureGenerators.add(new MapGenMineshaft((Map) map.get("mineshaft"), scramble));
			}

			if (map.containsKey("stronghold")) {
				this.structureGenerators.add(new MapGenStronghold((Map) map.get("stronghold"), scramble));
			}

			if (map.containsKey("oceanmonument")) {
				this.structureGenerators.add(new StructureOceanMonument((Map) map.get("oceanmonument"), scramble));
			}
		}

		if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake")) {
			this.waterLakeGenerator = new WorldGenLakes(Blocks.water);
		}

		if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake")) {
			this.lavaLakeGenerator = new WorldGenLakes(Blocks.lava);
		}

		this.hasDungeons = this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
		int j = 0;
		int k = 0;
		boolean flag = true;

		for (FlatLayerInfo flatlayerinfo : this.flatWorldGenInfo.getFlatLayers()) {
			for (int i = flatlayerinfo.getMinY(); i < flatlayerinfo.getMinY() + flatlayerinfo.getLayerCount(); ++i) {
				IBlockState iblockstate = flatlayerinfo.func_175900_c();
				if (iblockstate.getBlock() != Blocks.air) {
					flag = false;
					this.cachedBlockIDs[i] = iblockstate;
				}
			}

			if (flatlayerinfo.func_175900_c().getBlock() == Blocks.air) {
				k += flatlayerinfo.getLayerCount();
			} else {
				j += flatlayerinfo.getLayerCount() + k;
				k = 0;
			}
		}

		worldIn.func_181544_b(j);
		this.hasDecoration = flag ? false : this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration");
	}

	/**+
	 * Will return back a chunk, if it doesn't exist and its not a
	 * MP client it will generates all the blocks for the specified
	 * chunk from the map seed and chunk seed
	 */
	public Chunk provideChunk(int i, int j) {
		ChunkPrimer chunkprimer = new ChunkPrimer();

		for (int k = 0; k < this.cachedBlockIDs.length; ++k) {
			IBlockState iblockstate = this.cachedBlockIDs[k];
			if (iblockstate != null) {
				for (int l = 0; l < 16; ++l) {
					for (int i1 = 0; i1 < 16; ++i1) {
						chunkprimer.setBlockState(l, k, i1, iblockstate);
					}
				}
			}
		}

		for (MapGenBase mapgenbase : this.structureGenerators) {
			mapgenbase.generate(this, this.worldObj, i, j, chunkprimer);
		}

		Chunk chunk = new Chunk(this.worldObj, chunkprimer, i, j);
		BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager()
				.loadBlockGeneratorData((BiomeGenBase[]) null, i * 16, j * 16, 16, 16);
		byte[] abyte = chunk.getBiomeArray();

		for (int j1 = 0; j1 < abyte.length; ++j1) {
			abyte[j1] = (byte) abiomegenbase[j1].biomeID;
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	/**+
	 * Checks to see if a chunk exists at x, z
	 */
	public boolean chunkExists(int var1, int var2) {
		return true;
	}

	/**+
	 * Populates chunk with ores etc etc
	 */
	public void populate(IChunkProvider var1, int i, int j) {
		int k = i * 16;
		int l = j * 16;
		BlockPos blockpos = new BlockPos(k, 0, l);
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(k + 16, 0, l + 16));
		boolean flag = false;
		this.random.setSeed(this.worldObj.getSeed());
		long i1 = this.random.nextLong() / 2L * 2L + 1L;
		long j1 = this.random.nextLong() / 2L * 2L + 1L;
		this.random.setSeed((long) i * i1 + (long) j * j1 ^ this.worldObj.getSeed());
		ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);

		for (int m = 0, n = this.structureGenerators.size(); m < n; ++m) {
			MapGenStructure mapgenstructure = this.structureGenerators.get(m);
			boolean flag1 = mapgenstructure.generateStructure(this.worldObj, this.random, chunkcoordintpair);
			if (mapgenstructure instanceof MapGenVillage) {
				flag |= flag1;
			}
		}

		if (this.waterLakeGenerator != null && !flag && this.random.nextInt(4) == 0) {
			this.waterLakeGenerator.generate(this.worldObj, this.random,
					blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
		}

		if (this.lavaLakeGenerator != null && !flag && this.random.nextInt(8) == 0) {
			BlockPos blockpos1 = blockpos.add(this.random.nextInt(16) + 8,
					this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8);
			if (blockpos1.getY() < this.worldObj.func_181545_F() || this.random.nextInt(10) == 0) {
				this.lavaLakeGenerator.generate(this.worldObj, this.random, blockpos1);
			}
		}

		if (this.hasDungeons) {
			for (int k1 = 0; k1 < 8; ++k1) {
				(new WorldGenDungeons()).generate(this.worldObj, this.random, blockpos.add(this.random.nextInt(16) + 8,
						this.random.nextInt(256), this.random.nextInt(16) + 8));
			}
		}

		if (this.hasDecoration) {
			biomegenbase.decorate(this.worldObj, this.random, blockpos);
		}

	}

	public boolean func_177460_a(IChunkProvider var1, Chunk var2, int var3, int var4) {
		return false;
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
		return false;
	}

	/**+
	 * Returns if the IChunkProvider supports saving.
	 */
	public boolean canSave() {
		return true;
	}

	/**+
	 * Converts the instance data to a readable string.
	 */
	public String makeString() {
		return "FlatLevelSource";
	}

	public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype,
			BlockPos blockpos) {
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos);
		return biomegenbase.getSpawnableList(enumcreaturetype);
	}

	public BlockPos getStrongholdGen(World world, String s, BlockPos blockpos) {
		if ("Stronghold".equals(s)) {
			for (int m = 0, n = this.structureGenerators.size(); m < n; ++m) {
				MapGenStructure mapgenstructure = this.structureGenerators.get(m);
				if (mapgenstructure instanceof MapGenStronghold) {
					return mapgenstructure.getClosestStrongholdPos(world, blockpos);
				}
			}
		}

		return null;
	}

	public int getLoadedChunkCount() {
		return 0;
	}

	public void recreateStructures(Chunk var1, int i, int j) {
		for (int m = 0, n = this.structureGenerators.size(); m < n; ++m) {
			this.structureGenerators.get(m).generate(this, this.worldObj, i, j, (ChunkPrimer) null);
		}

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
		/**+
		 * Will return back a chunk, if it doesn't exist and its not a
		 * MP client it will generates all the blocks for the specified
		 * chunk from the map seed and chunk seed
		 */
		return provideChunk(var1, var2);
	}
}