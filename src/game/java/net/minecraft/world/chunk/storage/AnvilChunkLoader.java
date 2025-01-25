package net.minecraft.world.chunk.storage;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
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
public abstract class AnvilChunkLoader implements IChunkLoader {
	private static final Logger logger = LogManager.getLogger("AnvilChunkLoader");
	private static final String NEIGHBOR_LIGHT_CHECKS_KEY = "NeighborLightChecks";

	/**+
	 * Wraps readChunkFromNBT. Checks the coordinates and several
	 * NBT tags.
	 */
	protected Chunk checkedReadChunkFromNBT(World worldIn, int x, int z, NBTTagCompound parNBTTagCompound) {
		if (!parNBTTagCompound.hasKey("Level", 10)) {
			logger.error("Chunk file at " + x + "," + z + " is missing level data, skipping");
			return null;
		} else {
			NBTTagCompound nbttagcompound = parNBTTagCompound.getCompoundTag("Level");
			if (!nbttagcompound.hasKey("Sections", 9)) {
				logger.error("Chunk file at " + x + "," + z + " is missing block data, skipping");
				return null;
			} else {
				Chunk chunk = this.readChunkFromNBT(worldIn, nbttagcompound);
				if (!chunk.isAtLocation(x, z)) {
					logger.error("Chunk file at " + x + "," + z + " is in the wrong location; relocating. (Expected "
							+ x + ", " + z + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
					nbttagcompound.setInteger("xPos", x);
					nbttagcompound.setInteger("zPos", z);
					chunk = this.readChunkFromNBT(worldIn, nbttagcompound);
				}

				return chunk;
			}
		}
	}

	/**+
	 * Writes the Chunk passed as an argument to the NBTTagCompound
	 * also passed, using the World argument to retrieve the Chunk's
	 * last update time.
	 */
	protected void writeChunkToNBT(Chunk chunkIn, World worldIn, NBTTagCompound parNBTTagCompound) {
		alfheim$writeNeighborLightChecksToNBT(chunkIn, parNBTTagCompound);
		parNBTTagCompound.setBoolean("LightPopulated", chunkIn.alfheim$isLightInitialized());
		parNBTTagCompound.setByte("V", (byte) 1);
		parNBTTagCompound.setInteger("xPos", chunkIn.xPosition);
		parNBTTagCompound.setInteger("zPos", chunkIn.zPosition);
		parNBTTagCompound.setLong("LastUpdate", worldIn.getTotalWorldTime());
		parNBTTagCompound.setIntArray("HeightMap", chunkIn.getHeightMap());
		parNBTTagCompound.setBoolean("TerrainPopulated", chunkIn.isTerrainPopulated());
		parNBTTagCompound.setBoolean("LightPopulated", chunkIn.isLightPopulated());
		parNBTTagCompound.setLong("InhabitedTime", chunkIn.getInhabitedTime());
		ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
		NBTTagList nbttaglist = new NBTTagList();
		boolean flag = !worldIn.provider.getHasNoSky();

		for (ExtendedBlockStorage extendedblockstorage : aextendedblockstorage) {
			if (extendedblockstorage != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Y", (byte) (extendedblockstorage.getYLocation() >> 4 & 255));
				byte[] abyte = new byte[extendedblockstorage.getData().length];
				NibbleArray nibblearray = new NibbleArray();
				NibbleArray nibblearray1 = null;

				for (int i = 0; i < extendedblockstorage.getData().length; ++i) {
					char c0 = extendedblockstorage.getData()[i];
					int j = i & 15;
					int k = i >> 8 & 15;
					int l = i >> 4 & 15;
					if (c0 >> 12 != 0) {
						if (nibblearray1 == null) {
							nibblearray1 = new NibbleArray();
						}

						nibblearray1.set(j, k, l, c0 >> 12);
					}

					abyte[i] = (byte) (c0 >> 4 & 255);
					nibblearray.set(j, k, l, c0 & 15);
				}

				nbttagcompound.setByteArray("Blocks", abyte);
				nbttagcompound.setByteArray("Data", nibblearray.getData());
				if (nibblearray1 != null) {
					nbttagcompound.setByteArray("Add", nibblearray1.getData());
				}

				nbttagcompound.setByteArray("BlockLight", extendedblockstorage.getBlocklightArray().getData());
				if (flag) {
					nbttagcompound.setByteArray("SkyLight", extendedblockstorage.getSkylightArray().getData());
				} else {
					nbttagcompound.setByteArray("SkyLight",
							new byte[extendedblockstorage.getBlocklightArray().getData().length]);
				}

				nbttaglist.appendTag(nbttagcompound);
			}
		}

		parNBTTagCompound.setTag("Sections", nbttaglist);
		parNBTTagCompound.setByteArray("Biomes", chunkIn.getBiomeArray());
		chunkIn.setHasEntities(false);
		NBTTagList nbttaglist1 = new NBTTagList();

		for (int i1 = 0; i1 < chunkIn.getEntityLists().length; ++i1) {
			for (Entity entity : chunkIn.getEntityLists()[i1]) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				if (entity.writeToNBTOptional(nbttagcompound1)) {
					chunkIn.setHasEntities(true);
					nbttaglist1.appendTag(nbttagcompound1);
				}
			}
		}

		parNBTTagCompound.setTag("Entities", nbttaglist1);
		NBTTagList nbttaglist2 = new NBTTagList();

		for (TileEntity tileentity : chunkIn.getTileEntityMap().values()) {
			NBTTagCompound nbttagcompound2 = new NBTTagCompound();
			tileentity.writeToNBT(nbttagcompound2);
			nbttaglist2.appendTag(nbttagcompound2);
		}

		parNBTTagCompound.setTag("TileEntities", nbttaglist2);
		List<NextTickListEntry> list = worldIn.getPendingBlockUpdates(chunkIn, false);
		if (list != null) {
			long j1 = worldIn.getTotalWorldTime();
			NBTTagList nbttaglist3 = new NBTTagList();

			for (int k = 0, l = list.size(); k < l; ++k) {
				NextTickListEntry nextticklistentry = list.get(k);
				NBTTagCompound nbttagcompound3 = new NBTTagCompound();
				ResourceLocation resourcelocation = (ResourceLocation) Block.blockRegistry
						.getNameForObject(nextticklistentry.getBlock());
				nbttagcompound3.setString("i", resourcelocation == null ? "" : resourcelocation.toString());
				nbttagcompound3.setInteger("x", nextticklistentry.position.getX());
				nbttagcompound3.setInteger("y", nextticklistentry.position.getY());
				nbttagcompound3.setInteger("z", nextticklistentry.position.getZ());
				nbttagcompound3.setInteger("t", (int) (nextticklistentry.scheduledTime - j1));
				nbttagcompound3.setInteger("p", nextticklistentry.priority);
				nbttaglist3.appendTag(nbttagcompound3);
			}

			parNBTTagCompound.setTag("TileTicks", nbttaglist3);
		}

	}

	/**+
	 * Reads the data stored in the passed NBTTagCompound and
	 * creates a Chunk with that data in the passed World. Returns
	 * the created Chunk.
	 */
	protected Chunk readChunkFromNBT(World worldIn, NBTTagCompound parNBTTagCompound) {
		int i = parNBTTagCompound.getInteger("xPos");
		int j = parNBTTagCompound.getInteger("zPos");
		Chunk chunk = new Chunk(worldIn, i, j);
		chunk.setHeightMap(parNBTTagCompound.getIntArray("HeightMap"));
		chunk.setTerrainPopulated(parNBTTagCompound.getBoolean("TerrainPopulated"));
		chunk.setLightPopulated(parNBTTagCompound.getBoolean("LightPopulated"));
		chunk.setInhabitedTime(parNBTTagCompound.getLong("InhabitedTime"));
		NBTTagList nbttaglist = parNBTTagCompound.getTagList("Sections", 10);
		byte b0 = 16;
		ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[b0];
		boolean flag = !worldIn.provider.getHasNoSky();

		for (int k = 0; k < nbttaglist.tagCount(); ++k) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(k);
			byte b1 = nbttagcompound.getByte("Y");
			ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(b1 << 4, flag);
			byte[] abyte = nbttagcompound.getByteArray("Blocks");
			NibbleArray nibblearray = new NibbleArray(nbttagcompound.getByteArray("Data"));
			NibbleArray nibblearray1 = nbttagcompound.hasKey("Add", 7)
					? new NibbleArray(nbttagcompound.getByteArray("Add"))
					: null;
			char[] achar = new char[abyte.length];

			for (int l = 0; l < achar.length; ++l) {
				int i1 = l & 15;
				int j1 = l >> 8 & 15;
				int k1 = l >> 4 & 15;
				int l1 = nibblearray1 != null ? nibblearray1.get(i1, j1, k1) : 0;
				achar[l] = (char) (l1 << 12 | (abyte[l] & 255) << 4 | nibblearray.get(i1, j1, k1));
			}

			extendedblockstorage.setData(achar);
			extendedblockstorage.setBlocklightArray(new NibbleArray(nbttagcompound.getByteArray("BlockLight")));
			if (flag) {
				extendedblockstorage.setSkylightArray(new NibbleArray(nbttagcompound.getByteArray("SkyLight")));
			}

			extendedblockstorage.removeInvalidBlocks();
			aextendedblockstorage[b1] = extendedblockstorage;
		}

		chunk.setStorageArrays(aextendedblockstorage);
		if (parNBTTagCompound.hasKey("Biomes", 7)) {
			chunk.setBiomeArray(parNBTTagCompound.getByteArray("Biomes"));
		}

		NBTTagList nbttaglist1 = parNBTTagCompound.getTagList("Entities", 10);
		if (nbttaglist1 != null) {
			for (int i2 = 0; i2 < nbttaglist1.tagCount(); ++i2) {
				NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(i2);
				Entity entity = EntityList.createEntityFromNBT(nbttagcompound1, worldIn);
				chunk.setHasEntities(true);
				if (entity != null) {
					chunk.addEntity(entity);
					Entity entity1 = entity;

					for (NBTTagCompound nbttagcompound4 = nbttagcompound1; nbttagcompound4.hasKey("Riding",
							10); nbttagcompound4 = nbttagcompound4.getCompoundTag("Riding")) {
						Entity entity2 = EntityList.createEntityFromNBT(nbttagcompound4.getCompoundTag("Riding"),
								worldIn);
						if (entity2 != null) {
							chunk.addEntity(entity2);
							entity1.mountEntity(entity2);
						}

						entity1 = entity2;
					}
				}
			}
		}

		NBTTagList nbttaglist2 = parNBTTagCompound.getTagList("TileEntities", 10);
		if (nbttaglist2 != null) {
			for (int j2 = 0; j2 < nbttaglist2.tagCount(); ++j2) {
				NBTTagCompound nbttagcompound2 = nbttaglist2.getCompoundTagAt(j2);
				TileEntity tileentity = TileEntity.createAndLoadEntity(nbttagcompound2);
				if (tileentity != null) {
					chunk.addTileEntity(tileentity);
				}
			}
		}

		if (parNBTTagCompound.hasKey("TileTicks", 9)) {
			NBTTagList nbttaglist3 = parNBTTagCompound.getTagList("TileTicks", 10);
			if (nbttaglist3 != null) {
				for (int k2 = 0; k2 < nbttaglist3.tagCount(); ++k2) {
					NBTTagCompound nbttagcompound3 = nbttaglist3.getCompoundTagAt(k2);
					Block block;
					if (nbttagcompound3.hasKey("i", 8)) {
						block = Block.getBlockFromName(nbttagcompound3.getString("i"));
					} else {
						block = Block.getBlockById(nbttagcompound3.getInteger("i"));
					}

					worldIn.scheduleBlockUpdate(
							new BlockPos(nbttagcompound3.getInteger("x"), nbttagcompound3.getInteger("y"),
									nbttagcompound3.getInteger("z")),
							block, nbttagcompound3.getInteger("t"), nbttagcompound3.getInteger("p"));
				}
			}
		}

		alfheim$readNeighborLightChecksFromNBT(chunk, parNBTTagCompound);
		chunk.alfheim$setLightInitialized(parNBTTagCompound.getBoolean("LightPopulated"));

		return chunk;
	}

	private static void alfheim$readNeighborLightChecksFromNBT(final Chunk chunk, final NBTTagCompound compound) {
		if (!compound.hasKey(NEIGHBOR_LIGHT_CHECKS_KEY, 9)) {
			return;
		}

		final NBTTagList tagList = compound.getTagList(NEIGHBOR_LIGHT_CHECKS_KEY, 2);

		if (tagList.tagCount() != 32) {
			return;
		}

		chunk.alfheim$initNeighborLightChecks();

		final short[] neighborLightChecks = chunk.alfheim$neighborLightChecks;

		for (int i = 0; i < 32; ++i) {
			neighborLightChecks[i] = ((NBTTagShort) tagList.get(i)).getShort();
		}
	}

	private static void alfheim$writeNeighborLightChecksToNBT(final Chunk chunk, final NBTTagCompound compound) {
		final short[] neighborLightChecks = chunk.alfheim$neighborLightChecks;

		if (neighborLightChecks == null) {
			return;
		}

		boolean empty = true;

		final NBTTagList list = new NBTTagList();

		for (final short flags : neighborLightChecks) {
			list.appendTag(new NBTTagShort(flags));

			if (flags != 0) {
				empty = false;
			}
		}

		if (!empty) {
			compound.setTag(NEIGHBOR_LIGHT_CHECKS_KEY, list);
		}
	}
}