package net.minecraft.world.chunk.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.NibbleArray;

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
public class ChunkLoader {
	public static ChunkLoader.AnvilConverterData load(NBTTagCompound nbt) {
		int i = nbt.getInteger("xPos");
		int j = nbt.getInteger("zPos");
		ChunkLoader.AnvilConverterData chunkloader$anvilconverterdata = new ChunkLoader.AnvilConverterData(i, j);
		chunkloader$anvilconverterdata.blocks = nbt.getByteArray("Blocks");
		chunkloader$anvilconverterdata.data = new NibbleArrayReader(nbt.getByteArray("Data"), 7);
		chunkloader$anvilconverterdata.skyLight = new NibbleArrayReader(nbt.getByteArray("SkyLight"), 7);
		chunkloader$anvilconverterdata.blockLight = new NibbleArrayReader(nbt.getByteArray("BlockLight"), 7);
		chunkloader$anvilconverterdata.heightmap = nbt.getByteArray("HeightMap");
		chunkloader$anvilconverterdata.terrainPopulated = nbt.getBoolean("TerrainPopulated");
		chunkloader$anvilconverterdata.entities = nbt.getTagList("Entities", 10);
		chunkloader$anvilconverterdata.tileEntities = nbt.getTagList("TileEntities", 10);
		chunkloader$anvilconverterdata.tileTicks = nbt.getTagList("TileTicks", 10);

		try {
			chunkloader$anvilconverterdata.lastUpdated = nbt.getLong("LastUpdate");
		} catch (ClassCastException var5) {
			chunkloader$anvilconverterdata.lastUpdated = (long) nbt.getInteger("LastUpdate");
		}

		return chunkloader$anvilconverterdata;
	}

	public static void convertToAnvilFormat(ChunkLoader.AnvilConverterData parAnvilConverterData,
			NBTTagCompound parNBTTagCompound, WorldChunkManager parWorldChunkManager) {
		parNBTTagCompound.setInteger("xPos", parAnvilConverterData.x);
		parNBTTagCompound.setInteger("zPos", parAnvilConverterData.z);
		parNBTTagCompound.setLong("LastUpdate", parAnvilConverterData.lastUpdated);
		int[] aint = new int[parAnvilConverterData.heightmap.length];

		for (int i = 0; i < parAnvilConverterData.heightmap.length; ++i) {
			aint[i] = parAnvilConverterData.heightmap[i];
		}

		parNBTTagCompound.setIntArray("HeightMap", aint);
		parNBTTagCompound.setBoolean("TerrainPopulated", parAnvilConverterData.terrainPopulated);
		NBTTagList nbttaglist = new NBTTagList();

		for (int j = 0; j < 8; ++j) {
			boolean flag = true;

			for (int k = 0; k < 16 && flag; ++k) {
				for (int l = 0; l < 16 && flag; ++l) {
					for (int i1 = 0; i1 < 16; ++i1) {
						int j1 = k << 11 | i1 << 7 | l + (j << 4);
						byte b0 = parAnvilConverterData.blocks[j1];
						if (b0 != 0) {
							flag = false;
							break;
						}
					}
				}
			}

			if (!flag) {
				byte[] abyte1 = new byte[4096];
				NibbleArray nibblearray = new NibbleArray();
				NibbleArray nibblearray1 = new NibbleArray();
				NibbleArray nibblearray2 = new NibbleArray();

				for (int l2 = 0; l2 < 16; ++l2) {
					for (int k1 = 0; k1 < 16; ++k1) {
						for (int l1 = 0; l1 < 16; ++l1) {
							int i2 = l2 << 11 | l1 << 7 | k1 + (j << 4);
							byte b1 = parAnvilConverterData.blocks[i2];
							abyte1[k1 << 8 | l1 << 4 | l2] = (byte) (b1 & 255);
							nibblearray.set(l2, k1, l1, parAnvilConverterData.data.get(l2, k1 + (j << 4), l1));
							nibblearray1.set(l2, k1, l1, parAnvilConverterData.skyLight.get(l2, k1 + (j << 4), l1));
							nibblearray2.set(l2, k1, l1, parAnvilConverterData.blockLight.get(l2, k1 + (j << 4), l1));
						}
					}
				}

				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Y", (byte) (j & 255));
				nbttagcompound.setByteArray("Blocks", abyte1);
				nbttagcompound.setByteArray("Data", nibblearray.getData());
				nbttagcompound.setByteArray("SkyLight", nibblearray1.getData());
				nbttagcompound.setByteArray("BlockLight", nibblearray2.getData());
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		parNBTTagCompound.setTag("Sections", nbttaglist);
		byte[] abyte = new byte[256];
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int j2 = 0; j2 < 16; ++j2) {
			for (int k2 = 0; k2 < 16; ++k2) {
				blockpos$mutableblockpos.func_181079_c(parAnvilConverterData.x << 4 | j2, 0,
						parAnvilConverterData.z << 4 | k2);
				abyte[k2 << 4 | j2] = (byte) (parWorldChunkManager.getBiomeGenerator(blockpos$mutableblockpos,
						BiomeGenBase.field_180279_ad).biomeID & 255);
			}
		}

		parNBTTagCompound.setByteArray("Biomes", abyte);
		parNBTTagCompound.setTag("Entities", parAnvilConverterData.entities);
		parNBTTagCompound.setTag("TileEntities", parAnvilConverterData.tileEntities);
		if (parAnvilConverterData.tileTicks != null) {
			parNBTTagCompound.setTag("TileTicks", parAnvilConverterData.tileTicks);
		}

	}

	public static class AnvilConverterData {
		public long lastUpdated;
		public boolean terrainPopulated;
		public byte[] heightmap;
		public NibbleArrayReader blockLight;
		public NibbleArrayReader skyLight;
		public NibbleArrayReader data;
		public byte[] blocks;
		public NBTTagList entities;
		public NBTTagList tileEntities;
		public NBTTagList tileTicks;
		public final int x;
		public final int z;

		public AnvilConverterData(int parInt1, int parInt2) {
			this.x = parInt1;
			this.z = parInt2;
		}
	}
}