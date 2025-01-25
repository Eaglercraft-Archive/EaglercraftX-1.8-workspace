package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import dev.redstudio.alfheim.lighting.LightingEngine;
import dev.redstudio.alfheim.utils.EnumBoundaryFacing;
import dev.redstudio.alfheim.utils.WorldChunkSlice;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

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
public class Chunk {
	private static final Logger logger = LogManager.getLogger();
	private final ExtendedBlockStorage[] storageArrays;
	private final byte[] blockBiomeArray;
	private final int[] precipitationHeightMap;
	private final boolean[] updateSkylightColumns;
	private boolean isChunkLoaded;
	private final World worldObj;
	private final int[] heightMap;
	public final int xPosition;
	public final int zPosition;
	private boolean isGapLightingUpdated;
	private final Map<BlockPos, TileEntity> chunkTileEntityMap;
	private final ClassInheritanceMultiMap<Entity>[] entityLists;
	private boolean isTerrainPopulated;
	private boolean isLightPopulated;
	private boolean field_150815_m;
	private boolean isModified;
	private boolean hasEntities;
	private long lastSaveTime;
	private int heightMapMinimum;
	private long inhabitedTime;
	private int queuedLightChecks;
	private List<BlockPos> tileEntityPosQueue;
	private final ChunkCoordIntPair coordsCache;

	private LightingEngine alfheim$lightingEngine;
	private boolean alfheim$isLightInitialized;
	public short[] alfheim$neighborLightChecks;

	public Chunk(World worldIn, int x, int z) {
		this.storageArrays = new ExtendedBlockStorage[16];
		this.blockBiomeArray = new byte[256];
		this.precipitationHeightMap = new int[256];
		this.updateSkylightColumns = new boolean[256];
		this.chunkTileEntityMap = Maps.newHashMap();
		this.queuedLightChecks = 4096;
		this.tileEntityPosQueue = new LinkedList();
		this.entityLists = (ClassInheritanceMultiMap[]) (new ClassInheritanceMultiMap[16]);
		this.worldObj = worldIn;
		this.xPosition = x;
		this.zPosition = z;
		this.heightMap = new int[256];
		this.coordsCache = new ChunkCoordIntPair(x, z);

		for (int i = 0; i < this.entityLists.length; ++i) {
			this.entityLists[i] = new ClassInheritanceMultiMap(Entity.class);
		}

		Arrays.fill(this.precipitationHeightMap, -999);
		Arrays.fill(this.blockBiomeArray, (byte) -1);

		alfheim$lightingEngine = worldIn != null ? worldIn.alfheim$getLightingEngine() : null;
	}

	public Chunk(World worldIn, ChunkPrimer primer, int x, int z) {
		this(worldIn, x, z);
		short short1 = 256;
		boolean flag = !worldIn.provider.getHasNoSky();

		for (int i = 0; i < 16; ++i) {
			for (int j = 0; j < 16; ++j) {
				for (int k = 0; k < short1; ++k) {
					int l = i * short1 * 16 | j * short1 | k;
					IBlockState iblockstate = primer.getBlockState(l);
					if (iblockstate.getBlock().getMaterial() != Material.air) {
						int i1 = k >> 4;
						if (this.storageArrays[i1] == null) {
							this.storageArrays[i1] = new ExtendedBlockStorage(i1 << 4, flag);
						}

						this.storageArrays[i1].set(i, k & 15, j, iblockstate);
					}
				}
			}
		}

	}

	/**+
	 * Checks whether the chunk is at the X/Z location specified
	 */
	public boolean isAtLocation(int i, int j) {
		return i == this.xPosition && j == this.zPosition;
	}

	public int getHeight(BlockPos pos) {
		return this.getHeightValue(pos.x & 15, pos.z & 15);
	}

	/**+
	 * Returns the value in the height map at this x, z coordinate
	 * in the chunk
	 */
	public int getHeightValue(int i, int j) {
		return this.heightMap[j << 4 | i];
	}

	/**+
	 * Returns the topmost ExtendedBlockStorage instance for this
	 * Chunk that actually contains a block.
	 */
	public int getTopFilledSegment() {
		for (int i = this.storageArrays.length - 1; i >= 0; --i) {
			if (this.storageArrays[i] != null) {
				return this.storageArrays[i].getYLocation();
			}
		}

		return 0;
	}

	/**+
	 * Returns the ExtendedBlockStorage array for this Chunk.
	 */
	public ExtendedBlockStorage[] getBlockStorageArray() {
		return this.storageArrays;
	}

	/**+
	 * Generates the height map for a chunk from scratch
	 */
	protected void generateHeightMap() {
		int i = this.getTopFilledSegment();
		this.heightMapMinimum = Integer.MAX_VALUE;

		for (int j = 0; j < 16; ++j) {
			for (int k = 0; k < 16; ++k) {
				this.precipitationHeightMap[j + (k << 4)] = -999;

				for (int l = i + 16; l > 0; --l) {
					Block block = this.getBlock(j, l - 1, k);
					if (block.getLightOpacity() != 0) {
						this.heightMap[k << 4 | j] = l;
						if (l < this.heightMapMinimum) {
							this.heightMapMinimum = l;
						}
						break;
					}
				}
			}
		}

		this.isModified = true;
	}

	/**+
	 * Generates the initial skylight map for the chunk upon
	 * generation or load.
	 */
	public void generateSkylightMap() {
		int i = this.getTopFilledSegment();
		this.heightMapMinimum = Integer.MAX_VALUE;

		for (int j = 0; j < 16; ++j) {
			for (int k = 0; k < 16; ++k) {
				this.precipitationHeightMap[j + (k << 4)] = -999;

				for (int l = i + 16; l > 0; --l) {
					if (this.getBlockLightOpacity(j, l - 1, k) != 0) {
						this.heightMap[k << 4 | j] = l;
						if (l < this.heightMapMinimum) {
							this.heightMapMinimum = l;
						}
						break;
					}
				}

				if (!this.worldObj.provider.getHasNoSky()) {
					int k1 = 15;
					int i1 = i + 16 - 1;

					while (true) {
						int j1 = this.getBlockLightOpacity(j, i1, k);
						if (j1 == 0 && k1 != 15) {
							j1 = 1;
						}

						k1 -= j1;
						if (k1 > 0) {
							ExtendedBlockStorage extendedblockstorage = this.storageArrays[i1 >> 4];
							if (extendedblockstorage != null) {
								extendedblockstorage.setExtSkylightValue(j, i1 & 15, k, k1);
								this.worldObj.notifyLightSet(
										new BlockPos((this.xPosition << 4) + j, i1, (this.zPosition << 4) + k));
							}
						}

						--i1;
						if (i1 <= 0 || k1 <= 0) {
							break;
						}
					}
				}
			}
		}

		if (!this.worldObj.isRemote) {
			++EaglerMinecraftServer.counterLightUpdate;
		}
		this.isModified = true;
	}

	private void recheckGaps(boolean parFlag) {
		if (!worldObj.isAreaLoaded(new BlockPos((xPosition << 4) + 8, 0, (zPosition << 4) + 8), 16)) {
			return;
		}

		if (!this.worldObj.isRemote) {
			++EaglerMinecraftServer.counterLightUpdate;
		}

		final WorldChunkSlice slice = new WorldChunkSlice(worldObj.getChunkProvider(), xPosition, zPosition);

		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				if (!alfheim$recheckGapsForColumn(slice, x, z))
					continue;

				if (parFlag)
					return;
			}
		}

		isGapLightingUpdated = false;
	}

	/**+
	 * Checks the height of a block next to a sky-visible block and
	 * schedules a lighting update as necessary.
	 */
	private void checkSkylightNeighborHeight(int x, int z, int maxValue) {
		int i = this.worldObj.getHeight(new BlockPos(x, 0, z)).getY();
		if (i > maxValue) {
			this.updateSkylightNeighborHeight(x, z, maxValue, i + 1);
		} else if (i < maxValue) {
			this.updateSkylightNeighborHeight(x, z, i, maxValue + 1);
		}

	}

	private void updateSkylightNeighborHeight(int x, int z, int startY, int endY) {
		if (endY > startY && this.worldObj.isAreaLoaded(new BlockPos(x, 0, z), 16)) {
			for (int i = startY; i < endY; ++i) {
				this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, i, z));
			}

			this.isModified = true;
		}

	}

	/**+
	 * Initiates the recalculation of both the block-light and
	 * sky-light for a given block inside a chunk.
	 */
	private void relightBlock(int x, int y, int z) {
		int heightMapY = heightMap[z << 4 | x] & 255;
		int newHeightMapY = Math.max(y, heightMapY);

		while (newHeightMapY > 0 && getBlockLightOpacity(x, newHeightMapY - 1, z) == 0)
			--newHeightMapY;

		if (newHeightMapY == heightMapY)
			return;

		if (!this.worldObj.isRemote) {
			++EaglerMinecraftServer.counterLightUpdate;
		}

		heightMap[z << 4 | x] = newHeightMapY;

		if (!worldObj.provider.getHasNoSky())
			alfheim$relightSkylightColumn(x, z, heightMapY, newHeightMapY);

		final int heightMapY1 = heightMap[z << 4 | x];

		if (heightMapY1 < heightMapMinimum) {
			heightMapMinimum = heightMapY1;
		}
	}

	public int getBlockLightOpacity(BlockPos blockpos) {
		return this.getBlock(blockpos).getLightOpacity();
	}

	private int getBlockLightOpacity(int x, int y, int z) {
		return this.getBlock(x, y, z).getLightOpacity();
	}

	public Block getBlock(int x, int y, int z) {
		if (y >= 0 && y >> 4 < this.storageArrays.length) {
			ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];
			if (extendedblockstorage != null) {
				return extendedblockstorage.getBlockByExtId(x, y & 15, z);
			}
		}

		return Blocks.air;
	}

	public Block getBlock(final BlockPos pos) {
		if (pos.y >= 0 && pos.y >> 4 < this.storageArrays.length) {
			ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.y >> 4];
			if (extendedblockstorage != null) {
				int j = pos.x & 15;
				int k = pos.y & 15;
				int i = pos.z & 15;
				return extendedblockstorage.getBlockByExtId(j, k, i);
			}
		}

		return Blocks.air;
	}

	public IBlockState getBlockState(final BlockPos pos) {
		if (pos.y >= 0 && pos.y >> 4 < this.storageArrays.length) {
			ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.y >> 4];
			if (extendedblockstorage != null) {
				int j = pos.x & 15;
				int k = pos.y & 15;
				int i = pos.z & 15;
				return extendedblockstorage.get(j, k, i);
			}
		}

		return Blocks.air.getDefaultState();
	}

	/**+
	 * Return the metadata corresponding to the given coordinates
	 * inside a chunk.
	 */
	private int getBlockMetadata(int x, int y, int z) {
		if (y >> 4 >= this.storageArrays.length) {
			return 0;
		} else {
			ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];
			return extendedblockstorage != null ? extendedblockstorage.getExtBlockMetadata(x, y & 15, z) : 0;
		}
	}

	/**+
	 * Return the metadata corresponding to the given coordinates
	 * inside a chunk.
	 */
	public int getBlockMetadata(BlockPos blockpos) {
		return this.getBlockMetadata(blockpos.getX() & 15, blockpos.getY(), blockpos.getZ() & 15);
	}

	public IBlockState setBlockState(BlockPos pos, IBlockState state) {
		int i = pos.x & 15;
		int j = pos.y;
		int k = pos.z & 15;
		int l = k << 4 | i;
		if (j >= this.precipitationHeightMap[l] - 1) {
			this.precipitationHeightMap[l] = -999;
		}

		int i1 = this.heightMap[l];
		IBlockState iblockstate = this.getBlockState(pos);
		if (iblockstate == state) {
			return null;
		} else {
			Block block = state.getBlock();
			Block block1 = iblockstate.getBlock();
			ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
			boolean flag = false;
			if (extendedblockstorage == null) {
				if (block == Blocks.air) {
					return null;
				}

				extendedblockstorage = this.storageArrays[j >> 4] = new ExtendedBlockStorage(j >> 4 << 4,
						!this.worldObj.provider.getHasNoSky());
				alfheim$initSkylightForSection(extendedblockstorage);
				// flag = j >= i1;
			}

			extendedblockstorage.set(i, j & 15, k, state);
			if (block1 != block) {
				if (!this.worldObj.isRemote) {
					block1.breakBlock(this.worldObj, pos, iblockstate);
				} else if (block1 instanceof ITileEntityProvider) {
					this.worldObj.removeTileEntity(pos);
				}
			}

			if (extendedblockstorage.getBlockByExtId(i, j & 15, k) != block) {
				return null;
			} else {
				if (flag) {
					this.generateSkylightMap();
				} else {
					int j1 = block.getLightOpacity();
					// int k1 = block1.getLightOpacity();
					if (j1 > 0) {
						if (j >= i1) {
							this.relightBlock(i, j + 1, k);
						}
					} else if (j == i1 - 1) {
						this.relightBlock(i, j, k);
					}

//					if (j1 != k1 && (j1 < k1 || this.getLightFor(EnumSkyBlock.SKY, pos) > 0
//							|| this.getLightFor(EnumSkyBlock.BLOCK, pos) > 0)) {
//						this.propagateSkylightOcclusion(i, k);
//					}
				}

				if (block1 instanceof ITileEntityProvider) {
					TileEntity tileentity = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
					if (tileentity != null) {
						tileentity.updateContainingBlockInfo();
					}
				}

				if (!this.worldObj.isRemote && block1 != block) {
					block.onBlockAdded(this.worldObj, pos, state);
				}

				if (block instanceof ITileEntityProvider) {
					TileEntity tileentity1 = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
					if (tileentity1 == null) {
						tileentity1 = ((ITileEntityProvider) block).createNewTileEntity(this.worldObj,
								block.getMetaFromState(state));
						this.worldObj.setTileEntity(pos, tileentity1);
					}

					if (tileentity1 != null) {
						tileentity1.updateContainingBlockInfo();
					}
				}

				this.isModified = true;
				return iblockstate;
			}
		}
	}

	public int getLightFor(EnumSkyBlock enumskyblock, BlockPos blockpos) {
		alfheim$lightingEngine.processLightUpdatesForType(enumskyblock);
		return alfheim$getCachedLightFor(enumskyblock, blockpos);
	}

	public void setLightFor(EnumSkyBlock enumskyblock, BlockPos blockpos, int i) {
		int j = blockpos.x & 15;
		int k = blockpos.y;
		int l = blockpos.z & 15;
		ExtendedBlockStorage extendedblockstorage = this.storageArrays[k >> 4];
		if (extendedblockstorage == null) {
			extendedblockstorage = this.storageArrays[k >> 4] = new ExtendedBlockStorage(k >> 4 << 4,
					!this.worldObj.provider.getHasNoSky());
			alfheim$initSkylightForSection(storageArrays[k >> 4]);
		}

		this.isModified = true;
		if (enumskyblock == EnumSkyBlock.SKY) {
			if (!this.worldObj.provider.getHasNoSky()) {
				extendedblockstorage.setExtSkylightValue(j, k & 15, l, i);
			}
		} else if (enumskyblock == EnumSkyBlock.BLOCK) {
			extendedblockstorage.setExtBlocklightValue(j, k & 15, l, i);
		}

	}

	public int getLightSubtracted(BlockPos blockpos, int i) {
		alfheim$lightingEngine.processLightUpdates();
		int j = blockpos.x & 15;
		int k = blockpos.y;
		int l = blockpos.z & 15;
		ExtendedBlockStorage extendedblockstorage = this.storageArrays[k >> 4];
		if (extendedblockstorage == null) {
			return !this.worldObj.provider.getHasNoSky() && i < EnumSkyBlock.SKY.defaultLightValue
					? EnumSkyBlock.SKY.defaultLightValue - i
					: getNoSkyLightValue();
		} else {
			int i1 = this.worldObj.provider.getHasNoSky() ? getNoSkyLightValue()
					: extendedblockstorage.getExtSkylightValue(j, k & 15, l);
			i1 = i1 - i;
			int j1 = extendedblockstorage.getExtBlocklightValue(j, k & 15, l);
			if (j1 > i1) {
				i1 = j1;
			}

			return i1;
		}
	}

	public static int getNoSkyLightValue() {
		return DeferredStateManager.isDeferredRenderer() ? 5 : 0;
	}

	/**+
	 * Adds an entity to the chunk. Args: entity
	 */
	public void addEntity(Entity entity) {
		this.hasEntities = true;
		int i = MathHelper.floor_double(entity.posX / 16.0D);
		int j = MathHelper.floor_double(entity.posZ / 16.0D);
		if (i != this.xPosition || j != this.zPosition) {
			logger.warn("Wrong location! (" + i + ", " + j + ") should be (" + this.xPosition + ", " + this.zPosition
					+ "), " + entity, new Object[] { entity });
			entity.setDead();
		}

		int k = MathHelper.floor_double(entity.posY / 16.0D);
		if (k < 0) {
			k = 0;
		}

		if (k >= this.entityLists.length) {
			k = this.entityLists.length - 1;
		}

		entity.addedToChunk = true;
		entity.chunkCoordX = this.xPosition;
		entity.chunkCoordY = k;
		entity.chunkCoordZ = this.zPosition;
		this.entityLists[k].add(entity);
	}

	/**+
	 * removes entity using its y chunk coordinate as its index
	 */
	public void removeEntity(Entity entity) {
		this.removeEntityAtIndex(entity, entity.chunkCoordY);
	}

	/**+
	 * Removes entity at the specified index from the entity array.
	 */
	public void removeEntityAtIndex(Entity entity, int i) {
		if (i < 0) {
			i = 0;
		}

		if (i >= this.entityLists.length) {
			i = this.entityLists.length - 1;
		}

		this.entityLists[i].remove(entity);
	}

	public boolean canSeeSky(BlockPos blockpos) {
		int i = blockpos.x & 15;
		int j = blockpos.y;
		int k = blockpos.z & 15;
		return j >= this.heightMap[k << 4 | i];
	}

	private TileEntity createNewTileEntity(BlockPos pos) {
		Block block = this.getBlock(pos);
		return !block.hasTileEntity() ? null
				: ((ITileEntityProvider) block).createNewTileEntity(this.worldObj, this.getBlockMetadata(pos));
	}

	public TileEntity getTileEntity(BlockPos blockpos, Chunk.EnumCreateEntityType chunk$enumcreateentitytype) {
		TileEntity tileentity = (TileEntity) this.chunkTileEntityMap.get(blockpos);
		if (tileentity == null) {
			BlockPos pos2 = new BlockPos(blockpos);
			if (chunk$enumcreateentitytype == Chunk.EnumCreateEntityType.IMMEDIATE) {
				tileentity = this.createNewTileEntity(pos2);
				this.worldObj.setTileEntity(pos2, tileentity);
			} else if (chunk$enumcreateentitytype == Chunk.EnumCreateEntityType.QUEUED) {
				this.tileEntityPosQueue.add(pos2);
			}
		} else if (tileentity.isInvalid()) {
			this.chunkTileEntityMap.remove(blockpos);
			return null;
		}

		return tileentity;
	}

	public void addTileEntity(TileEntity tileentity) {
		this.addTileEntity(tileentity.getPos(), tileentity);
		if (this.isChunkLoaded) {
			this.worldObj.addTileEntity(tileentity);
		}

	}

	public void addTileEntity(BlockPos blockpos, TileEntity tileentity) {
		tileentity.setWorldObj(this.worldObj);
		blockpos = new BlockPos(blockpos);
		tileentity.setPos(blockpos);
		if (this.getBlock(blockpos) instanceof ITileEntityProvider) {
			if (this.chunkTileEntityMap.containsKey(blockpos)) {
				((TileEntity) this.chunkTileEntityMap.get(blockpos)).invalidate();
			}

			tileentity.validate();
			this.chunkTileEntityMap.put(blockpos, tileentity);
		}
	}

	public void removeTileEntity(BlockPos blockpos) {
		if (this.isChunkLoaded) {
			TileEntity tileentity = (TileEntity) this.chunkTileEntityMap.remove(blockpos);
			if (tileentity != null) {
				tileentity.invalidate();
			}
		}

	}

	/**+
	 * Called when this Chunk is loaded by the ChunkProvider
	 */
	public void onChunkLoad() {
		this.isChunkLoaded = true;
		this.worldObj.addTileEntities(this.chunkTileEntityMap.values());

		for (int i = 0; i < this.entityLists.length; ++i) {
			for (Entity entity : this.entityLists[i]) {
				entity.onChunkLoad();
			}

			this.worldObj.loadEntities(this.entityLists[i]);
		}

		for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
			final int xOffset = facing.getFrontOffsetX();
			final int zOffset = facing.getFrontOffsetZ();

			final Chunk nChunk = worldObj.getChunkProvider().getLoadedChunk(xPosition + xOffset, zPosition + zOffset);

			if (nChunk == null)
				continue;

			EnumSkyBlock[] lightTypes = EnumSkyBlock._VALUES;
			EnumFacing.AxisDirection[] axisDirections = EnumFacing.AxisDirection._VALUES;
			for (int ii = 0, ll = lightTypes.length; ii < ll; ++ii) {
				final EnumSkyBlock lightType = lightTypes[ii];
				for (int jj = 0, mm = axisDirections.length; jj < mm; ++jj) {
					final EnumFacing.AxisDirection axisDir = axisDirections[jj];
					// Merge flags upon loading of a chunk. This ensures that all flags are always
					// already on the IN boundary below
					alfheim$mergeFlags(lightType, this, nChunk, facing, axisDir);
					alfheim$mergeFlags(lightType, nChunk, this, facing.getOpposite(), axisDir);

					// Check everything that might have been canceled due to this chunk not being
					// loaded.
					// Also, pass in chunks if already known
					// The boundary to the neighbor chunk (both ways)
					alfheim$scheduleRelightChecksForBoundary(this, nChunk, null, lightType, xOffset, zOffset, axisDir);
					alfheim$scheduleRelightChecksForBoundary(nChunk, this, null, lightType, -xOffset, -zOffset,
							axisDir);
					// The boundary to the diagonal neighbor (since the checks in that chunk were
					// aborted if this chunk wasn't loaded, see
					// alfheim$scheduleRelightChecksForBoundary)
					alfheim$scheduleRelightChecksForBoundary(nChunk, null, this, lightType,
							(zOffset != 0 ? axisDir.getOffset() : 0), (xOffset != 0 ? axisDir.getOffset() : 0),
							facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE
									? EnumFacing.AxisDirection.NEGATIVE
									: EnumFacing.AxisDirection.POSITIVE);
				}
			}
		}
	}

	/**+
	 * Called when this Chunk is unloaded by the ChunkProvider
	 */
	public void onChunkUnload() {
		this.isChunkLoaded = false;

		for (TileEntity tileentity : this.chunkTileEntityMap.values()) {
			this.worldObj.markTileEntityForRemoval(tileentity);
		}

		for (int i = 0; i < this.entityLists.length; ++i) {
			this.worldObj.unloadEntities(this.entityLists[i]);
		}

	}

	/**+
	 * Sets the isModified flag for this Chunk
	 */
	public void setChunkModified() {
		this.isModified = true;
	}

	/**+
	 * Fills the given list of all entities that intersect within
	 * the given bounding box that aren't the passed entity.
	 */
	public void getEntitiesWithinAABBForEntity(Entity entity, AxisAlignedBB axisalignedbb, List<Entity> list,
			Predicate<? super Entity> predicate) {
		int i = MathHelper.floor_double((axisalignedbb.minY - 2.0D) / 16.0D);
		int j = MathHelper.floor_double((axisalignedbb.maxY + 2.0D) / 16.0D);
		i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
		j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);

		for (int k = i; k <= j; ++k) {
			if (!this.entityLists[k].isEmpty()) {
				for (Entity entity1 : this.entityLists[k]) {
					if (entity1.getEntityBoundingBox().intersectsWith(axisalignedbb) && entity1 != entity) {
						if (predicate == null || predicate.apply(entity1)) {
							list.add(entity1);
						}

						Entity[] aentity = entity1.getParts();
						if (aentity != null) {
							for (int l = 0; l < aentity.length; ++l) {
								entity1 = aentity[l];
								if (entity1 != entity && entity1.getEntityBoundingBox().intersectsWith(axisalignedbb)
										&& (predicate == null || predicate.apply(entity1))) {
									list.add(entity1);
								}
							}
						}
					}
				}
			}
		}

	}

	public <T extends Entity> void getEntitiesOfTypeWithinAAAB(Class<? extends T> oclass, AxisAlignedBB axisalignedbb,
			List<T> list, Predicate<? super T> predicate) {
		int i = MathHelper.floor_double((axisalignedbb.minY - 2.0D) / 16.0D);
		int j = MathHelper.floor_double((axisalignedbb.maxY + 2.0D) / 16.0D);
		i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
		j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);

		for (int k = i; k <= j; ++k) {
			for (Entity entity : this.entityLists[k].getByClass(oclass)) {
				if (entity.getEntityBoundingBox().intersectsWith(axisalignedbb)
						&& (predicate == null || predicate.apply((T) entity))) {
					list.add((T) entity);
				}
			}
		}

	}

	/**+
	 * Returns true if this Chunk needs to be saved
	 */
	public boolean needsSaving(boolean flag) {
		if (flag) {
			if (this.hasEntities && this.worldObj.getTotalWorldTime() != this.lastSaveTime || this.isModified) {
				return true;
			}
		} else if (this.hasEntities && this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L) {
			return true;
		}

		return this.isModified;
	}

	public EaglercraftRandom getRandomWithSeed(long i) {
		return new EaglercraftRandom(this.worldObj.getSeed() + (long) (this.xPosition * this.xPosition * 4987142)
				+ (long) (this.xPosition * 5947611) + (long) (this.zPosition * this.zPosition) * 4392871L
				+ (long) (this.zPosition * 389711) ^ i, !this.worldObj.getWorldInfo().isOldEaglercraftRandom());
	}

	public boolean isEmpty() {
		return false;
	}

	public void populateChunk(IChunkProvider parIChunkProvider, IChunkProvider parIChunkProvider2, int parInt1,
			int parInt2) {
		boolean flag = parIChunkProvider.chunkExists(parInt1, parInt2 - 1);
		boolean flag1 = parIChunkProvider.chunkExists(parInt1 + 1, parInt2);
		boolean flag2 = parIChunkProvider.chunkExists(parInt1, parInt2 + 1);
		boolean flag3 = parIChunkProvider.chunkExists(parInt1 - 1, parInt2);
		boolean flag4 = parIChunkProvider.chunkExists(parInt1 - 1, parInt2 - 1);
		boolean flag5 = parIChunkProvider.chunkExists(parInt1 + 1, parInt2 + 1);
		boolean flag6 = parIChunkProvider.chunkExists(parInt1 - 1, parInt2 + 1);
		boolean flag7 = parIChunkProvider.chunkExists(parInt1 + 1, parInt2 - 1);
		if (flag1 && flag2 && flag5) {
			if (!this.isTerrainPopulated) {
				parIChunkProvider.populate(parIChunkProvider2, parInt1, parInt2);
			} else {
				parIChunkProvider.func_177460_a(parIChunkProvider2, this, parInt1, parInt2);
			}
		}

		if (flag3 && flag2 && flag6) {
			Chunk chunk = parIChunkProvider.provideChunk(parInt1 - 1, parInt2);
			if (!chunk.isTerrainPopulated) {
				parIChunkProvider.populate(parIChunkProvider2, parInt1 - 1, parInt2);
			} else {
				parIChunkProvider.func_177460_a(parIChunkProvider2, chunk, parInt1 - 1, parInt2);
			}
		}

		if (flag && flag1 && flag7) {
			Chunk chunk1 = parIChunkProvider.provideChunk(parInt1, parInt2 - 1);
			if (!chunk1.isTerrainPopulated) {
				parIChunkProvider.populate(parIChunkProvider2, parInt1, parInt2 - 1);
			} else {
				parIChunkProvider.func_177460_a(parIChunkProvider2, chunk1, parInt1, parInt2 - 1);
			}
		}

		if (flag4 && flag && flag3) {
			Chunk chunk2 = parIChunkProvider.provideChunk(parInt1 - 1, parInt2 - 1);
			if (!chunk2.isTerrainPopulated) {
				parIChunkProvider.populate(parIChunkProvider2, parInt1 - 1, parInt2 - 1);
			} else {
				parIChunkProvider.func_177460_a(parIChunkProvider2, chunk2, parInt1 - 1, parInt2 - 1);
			}
		}

	}

	public BlockPos getPrecipitationHeight(BlockPos pos) {
		int i = pos.getX() & 15;
		int j = pos.getZ() & 15;
		int k = i | j << 4;
		BlockPos blockpos = new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
		if (blockpos.getY() == -999) {
			int l = this.getTopFilledSegment() + 15;
			blockpos = new BlockPos(pos.getX(), l, pos.getZ());
			int i1 = -1;

			while (blockpos.getY() > 0 && i1 == -1) {
				Block block = this.getBlock(blockpos);
				Material material = block.getMaterial();
				if (!material.blocksMovement() && !material.isLiquid()) {
					blockpos = blockpos.down();
				} else {
					i1 = blockpos.getY() + 1;
				}
			}

			this.precipitationHeightMap[k] = i1;
		}

		return new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
	}

	public void func_150804_b(boolean parFlag) {
		if (this.isGapLightingUpdated && !this.worldObj.provider.getHasNoSky() && !parFlag) {
			this.recheckGaps(this.worldObj.isRemote);
		}

		this.field_150815_m = true;
		if (!this.isLightPopulated && this.isTerrainPopulated) {
			this.func_150809_p();
		}

		while (!this.tileEntityPosQueue.isEmpty()) {
			BlockPos blockpos = (BlockPos) this.tileEntityPosQueue.remove(0);
			if (this.getTileEntity(blockpos, Chunk.EnumCreateEntityType.CHECK) == null
					&& this.getBlock(blockpos).hasTileEntity()) {
				TileEntity tileentity = this.createNewTileEntity(blockpos);
				this.worldObj.setTileEntity(blockpos, tileentity);
				this.worldObj.markBlockRangeForRenderUpdate(blockpos, blockpos);
			}
		}

	}

	public boolean isPopulated() {
		return this.field_150815_m && this.isTerrainPopulated && this.isLightPopulated;
	}

	/**+
	 * Gets a ChunkCoordIntPair representing the Chunk's position.
	 */
	public ChunkCoordIntPair getChunkCoordIntPair() {
		return coordsCache;
	}

	public long getChunkCoordLong() {
		return ChunkCoordIntPair.chunkXZ2Int(this.xPosition, this.zPosition);
	}

	/**+
	 * Returns whether the ExtendedBlockStorages containing levels
	 * (in blocks) from arg 1 to arg 2 are fully empty (true) or not
	 * (false).
	 */
	public boolean getAreLevelsEmpty(int i, int j) {
		if (i < 0) {
			i = 0;
		}

		if (j >= 256) {
			j = 255;
		}

		for (int k = i; k <= j; k += 16) {
			ExtendedBlockStorage extendedblockstorage = this.storageArrays[k >> 4];
			if (extendedblockstorage != null && !extendedblockstorage.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public void setStorageArrays(ExtendedBlockStorage[] newStorageArrays) {
		if (this.storageArrays.length != newStorageArrays.length) {
			logger.warn("Could not set level chunk sections, array length is " + newStorageArrays.length
					+ " instead of " + this.storageArrays.length);
		} else {
			for (int i = 0; i < this.storageArrays.length; ++i) {
				this.storageArrays[i] = newStorageArrays[i];
			}

		}
	}

	/**+
	 * Initialize this chunk with new binary data.
	 */
	public void fillChunk(byte[] parArrayOfByte, int parInt1, boolean parFlag) {
		int i = 0;
		boolean flag = !this.worldObj.provider.getHasNoSky();

		for (int j = 0; j < this.storageArrays.length; ++j) {
			if ((parInt1 & 1 << j) != 0) {
				if (this.storageArrays[j] == null) {
					this.storageArrays[j] = new ExtendedBlockStorage(j << 4, flag);
				}

				char[] achar = this.storageArrays[j].getData();

				for (int k = 0; k < achar.length; ++k) {
					achar[k] = (char) ((parArrayOfByte[i + 1] & 255) << 8 | parArrayOfByte[i] & 255);
					i += 2;
				}
			} else if (parFlag && this.storageArrays[j] != null) {
				this.storageArrays[j] = null;
			}
		}

		for (int l = 0; l < this.storageArrays.length; ++l) {
			if ((parInt1 & 1 << l) != 0 && this.storageArrays[l] != null) {
				NibbleArray nibblearray = this.storageArrays[l].getBlocklightArray();
				System.arraycopy(parArrayOfByte, i, nibblearray.getData(), 0, nibblearray.getData().length);
				i += nibblearray.getData().length;
			}
		}

		if (flag) {
			for (int i1 = 0; i1 < this.storageArrays.length; ++i1) {
				if ((parInt1 & 1 << i1) != 0 && this.storageArrays[i1] != null) {
					NibbleArray nibblearray1 = this.storageArrays[i1].getSkylightArray();
					System.arraycopy(parArrayOfByte, i, nibblearray1.getData(), 0, nibblearray1.getData().length);
					i += nibblearray1.getData().length;
				}
			}
		}

		if (parFlag) {
			System.arraycopy(parArrayOfByte, i, this.blockBiomeArray, 0, this.blockBiomeArray.length);
			int k1 = i + this.blockBiomeArray.length;
		}

		for (int j1 = 0; j1 < this.storageArrays.length; ++j1) {
			if (this.storageArrays[j1] != null && (parInt1 & 1 << j1) != 0) {
				this.storageArrays[j1].removeInvalidBlocks();
			}
		}

		this.isLightPopulated = true;
		this.isTerrainPopulated = true;
		this.generateHeightMap();

		for (TileEntity tileentity : this.chunkTileEntityMap.values()) {
			tileentity.updateContainingBlockInfo();
		}

	}

	public BiomeGenBase getBiome(BlockPos pos, WorldChunkManager chunkManager) {
		int i = pos.getX() & 15;
		int j = pos.getZ() & 15;
		int k = this.blockBiomeArray[j << 4 | i] & 255;
		if (chunkManager != null && k == 255) {
			BiomeGenBase biomegenbase = chunkManager.getBiomeGenerator(pos, BiomeGenBase.plains);
			k = biomegenbase.biomeID;
			this.blockBiomeArray[j << 4 | i] = (byte) (k & 255);
		}

		BiomeGenBase biomegenbase1 = BiomeGenBase.getBiome(k);
		return biomegenbase1 == null ? BiomeGenBase.plains : biomegenbase1;
	}

	/**+
	 * Returns an array containing a 16x16 mapping on the X/Z of
	 * block positions in this Chunk to biome IDs.
	 */
	public byte[] getBiomeArray() {
		return this.blockBiomeArray;
	}

	/**+
	 * Accepts a 256-entry array that contains a 16x16 mapping on
	 * the X/Z plane of block positions in this Chunk to biome IDs.
	 */
	public void setBiomeArray(byte[] biomeArray) {
		if (this.blockBiomeArray.length != biomeArray.length) {
			logger.warn("Could not set level chunk biomes, array length is " + biomeArray.length + " instead of "
					+ this.blockBiomeArray.length);
		} else {
			for (int i = 0; i < this.blockBiomeArray.length; ++i) {
				this.blockBiomeArray[i] = biomeArray[i];
			}

		}
	}

	/**+
	 * Resets the relight check index to 0 for this Chunk.
	 */
	public void resetRelightChecks() {
		this.queuedLightChecks = 0;
	}

	/**+
	 * Called once-per-chunk-per-tick, and advances the round-robin
	 * relight check index by up to 8 blocks at a time. In a
	 * worst-case scenario, can potentially take up to 25.6 seconds,
	 * calculated via (4096/8)/20, to re-check all blocks in a
	 * chunk, which may explain lagging light updates on initial
	 * world generation.
	 */
	public void enqueueRelightChecks() {
		BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);

		for (int i = 0; i < 8; ++i) {
			if (this.queuedLightChecks >= 4096) {
				return;
			}

			int j = this.queuedLightChecks % 16;
			int k = this.queuedLightChecks / 16 % 16;
			int l = this.queuedLightChecks / 256;
			++this.queuedLightChecks;

			EnumFacing[] facings = EnumFacing._VALUES;
			for (int i1 = 0; i1 < 16; ++i1) {
				BlockPos blockpos1 = blockpos.add(k, (j << 4) + i1, l);
				boolean flag = i1 == 0 || i1 == 15 || k == 0 || k == 15 || l == 0 || l == 15;
				if (this.storageArrays[j] == null && flag || this.storageArrays[j] != null
						&& this.storageArrays[j].getBlockByExtId(k, i1, l).getMaterial() == Material.air) {
					for (int m = 0; m < facings.length; ++m) {
						BlockPos blockpos2 = blockpos1.offset(facings[m]);
						if (this.worldObj.getBlockState(blockpos2).getBlock().getLightValue() > 0) {
							this.worldObj.checkLight(blockpos2);
						}
					}

					this.worldObj.checkLight(blockpos1);
				}
			}
		}

	}

	public void func_150809_p() {
		this.isTerrainPopulated = true;

		if (!alfheim$isLightInitialized)
			alfheim$initChunkLighting(this, worldObj);

		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x == 0 && z == 0)
					continue;

				final Chunk nChunk = worldObj.getChunkProvider().getLoadedChunk(xPosition + x, zPosition + z);

				if (nChunk == null || !nChunk.alfheim$isLightInitialized())
					return;
			}
		}

		setLightPopulated(true);
	}

	private void func_177441_y() {
		for (int i = 0; i < this.updateSkylightColumns.length; ++i) {
			this.updateSkylightColumns[i] = true;
		}

		this.recheckGaps(false);
	}

	public boolean isLoaded() {
		return this.isChunkLoaded;
	}

	public void setChunkLoaded(boolean loaded) {
		this.isChunkLoaded = loaded;
	}

	public World getWorld() {
		return this.worldObj;
	}

	public int[] getHeightMap() {
		return this.heightMap;
	}

	public void setHeightMap(int[] newHeightMap) {
		if (this.heightMap.length != newHeightMap.length) {
			logger.warn("Could not set level chunk heightmap, array length is " + newHeightMap.length + " instead of "
					+ this.heightMap.length);
		} else {
			for (int i = 0; i < this.heightMap.length; ++i) {
				this.heightMap[i] = newHeightMap[i];
			}

		}
	}

	public Map<BlockPos, TileEntity> getTileEntityMap() {
		return this.chunkTileEntityMap;
	}

	public ClassInheritanceMultiMap<Entity>[] getEntityLists() {
		return this.entityLists;
	}

	public boolean isTerrainPopulated() {
		return this.isTerrainPopulated;
	}

	public void setTerrainPopulated(boolean terrainPopulated) {
		this.isTerrainPopulated = terrainPopulated;
	}

	public boolean isLightPopulated() {
		return this.isLightPopulated;
	}

	public void setLightPopulated(boolean lightPopulated) {
		this.isLightPopulated = lightPopulated;
	}

	public void setModified(boolean modified) {
		this.isModified = modified;
	}

	public void setHasEntities(boolean hasEntitiesIn) {
		this.hasEntities = hasEntitiesIn;
	}

	public void setLastSaveTime(long saveTime) {
		this.lastSaveTime = saveTime;
	}

	public int getLowestHeight() {
		return this.heightMapMinimum;
	}

	public long getInhabitedTime() {
		return this.inhabitedTime;
	}

	public void setInhabitedTime(long newInhabitedTime) {
		this.inhabitedTime = newInhabitedTime;
	}

	public static enum EnumCreateEntityType {
		IMMEDIATE, QUEUED, CHECK;
	}

	private boolean alfheim$recheckGapsForColumn(final WorldChunkSlice slice, final int x, final int z) {
		final int i = x + (z << 4);

		if (updateSkylightColumns[i]) {
			updateSkylightColumns[i] = false;

			final int x1 = (this.xPosition << 4) + x;
			final int z1 = (this.zPosition << 4) + z;

			alfheim$recheckGapsSkylightNeighborHeight(slice, x1, z1, getHeightValue(x, z),
					alfheim$recheckGapsGetLowestHeight(slice, x1, z1));

			return true;
		}

		return false;
	}

	private int alfheim$recheckGapsGetLowestHeight(final WorldChunkSlice slice, final int x, final int z) {
		int max = Integer.MAX_VALUE;

		Chunk chunk = slice.getChunkFromWorldCoords(x + 1, z);

		if (chunk != null)
			max = Math.min(max, chunk.getLowestHeight());

		chunk = slice.getChunkFromWorldCoords(x, z + 1);

		if (chunk != null)
			max = Math.min(max, chunk.getLowestHeight());

		chunk = slice.getChunkFromWorldCoords(x - 1, z);

		if (chunk != null)
			max = Math.min(max, chunk.getLowestHeight());

		chunk = slice.getChunkFromWorldCoords(x, z - 1);

		if (chunk != null)
			max = Math.min(max, chunk.getLowestHeight());

		return max;
	}

	private void alfheim$recheckGapsSkylightNeighborHeight(final WorldChunkSlice slice, final int x, final int z,
			final int height, final int max) {
		alfheim$checkSkylightNeighborHeight(slice, x, z, max);
		alfheim$checkSkylightNeighborHeight(slice, x + 1, z, height);
		alfheim$checkSkylightNeighborHeight(slice, x, z + 1, height);
		alfheim$checkSkylightNeighborHeight(slice, x - 1, z, height);
		alfheim$checkSkylightNeighborHeight(slice, x, z - 1, height);
	}

	private void alfheim$checkSkylightNeighborHeight(final WorldChunkSlice slice, final int x, final int z,
			final int maxValue) {
		Chunk c = slice.getChunkFromWorldCoords(x, z);
		if (c == null)
			return;

		final int y = c.getHeightValue(x & 15, z & 15);

		if (y > maxValue)
			alfheim$updateSkylightNeighborHeight(slice, x, z, maxValue, y + 1);
		else if (y < maxValue)
			alfheim$updateSkylightNeighborHeight(slice, x, z, y, maxValue + 1);
	}

	private void alfheim$updateSkylightNeighborHeight(final WorldChunkSlice slice, final int x, final int z,
			final int startY, final int endY) {
		if (endY < startY)
			return;

		if (!slice.isLoaded(x, z, 16))
			return;

		for (int y = startY; y < endY; ++y)
			worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, y, z));

		isModified = true;
	}

	private static void alfheim$mergeFlags(final EnumSkyBlock lightType, final Chunk inChunk, final Chunk outChunk,
			final EnumFacing dir, final EnumFacing.AxisDirection axisDirection) {
		if (outChunk.alfheim$neighborLightChecks == null)
			return;

		inChunk.alfheim$initNeighborLightChecks();

		final int inIndex = alfheim$getFlagIndex(lightType, dir, axisDirection, EnumBoundaryFacing.IN);
		final int outIndex = alfheim$getFlagIndex(lightType, dir.getOpposite(), axisDirection, EnumBoundaryFacing.OUT);

		inChunk.alfheim$neighborLightChecks[inIndex] |= outChunk.alfheim$neighborLightChecks[outIndex];
		// No need to call Chunk.setModified() since checks are not deleted from
		// outChunk
	}

	private void alfheim$scheduleRelightChecksForBoundary(final Chunk chunk, Chunk nChunk, Chunk sChunk,
			final EnumSkyBlock lightType, final int xOffset, final int zOffset,
			final EnumFacing.AxisDirection axisDirection) {
		if (chunk.alfheim$neighborLightChecks == null)
			return;

		final int flagIndex = alfheim$getFlagIndex(lightType, xOffset, zOffset, axisDirection, EnumBoundaryFacing.IN); // OUT
																														// checks
																														// from
																														// neighbor
																														// are
																														// already
																														// merged

		final int flags = chunk.alfheim$neighborLightChecks[flagIndex];

		if (flags == 0)
			return;

		if (nChunk == null) {
			nChunk = worldObj.getChunkProvider().getLoadedChunk(chunk.xPosition + xOffset, chunk.zPosition + zOffset);

			if (nChunk == null)
				return;
		}

		if (sChunk == null) {
			sChunk = worldObj.getChunkProvider().getLoadedChunk(
					chunk.xPosition + (zOffset != 0 ? axisDirection.getOffset() : 0),
					chunk.zPosition + (xOffset != 0 ? axisDirection.getOffset() : 0));

			if (sChunk == null)
				return; // Cancel, since the checks in the corner columns require the corner column of
						// sChunk
		}

		final int reverseIndex = alfheim$getFlagIndex(lightType, -xOffset, -zOffset, axisDirection,
				EnumBoundaryFacing.OUT);

		chunk.alfheim$neighborLightChecks[flagIndex] = 0;

		if (alfheim$neighborLightChecks != null)
			nChunk.alfheim$neighborLightChecks[reverseIndex] = 0; // Clear only now that it's clear that the checks
																	// are processed

		chunk.setChunkModified();
		nChunk.setChunkModified();

		// Get the area to check
		// Start in the corner...
		int xMin = chunk.xPosition << 4;
		int zMin = chunk.zPosition << 4;

		// Move to other side of chunk if the direction is positive
		if ((xOffset | zOffset) > 0) {
			xMin += 15 * xOffset;
			zMin += 15 * zOffset;
		}

		// Shift to other half if necessary (shift perpendicular to dir)
		if (axisDirection == EnumFacing.AxisDirection.POSITIVE) {
			xMin += 8 * (zOffset & 1); // x & 1 is same as abs(x) for x=-1,0,1
			zMin += 8 * (xOffset & 1);
		}

		// Get maximal values (shift perpendicular to dir)
		final int xMax = xMin + 7 * (zOffset & 1);
		final int zMax = zMin + 7 * (xOffset & 1);

		for (int y = 0; y < 16; ++y)
			if ((flags & (1 << y)) != 0)
				for (int x = xMin; x <= xMax; ++x)
					for (int z = zMin; z <= zMax; ++z)
						alfheim$scheduleRelightChecksForColumn(lightType, x, z, y << 4, (y << 4) + 15);
	}

	private void alfheim$initSkylightForSection(final ExtendedBlockStorage extendedBlockStorage) {
		if (worldObj.provider.getHasNoSky())
			return;

		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				if (getHeightValue(x, z) > extendedBlockStorage.getYLocation())
					continue;

				for (int y = 0; y < 16; ++y)
					extendedBlockStorage.setExtSkylightValue(x, y, z, EnumSkyBlock.SKY.defaultLightValue);
			}
		}
	}

	private void alfheim$scheduleRelightChecksForColumn(final EnumSkyBlock lightType, final int x, final int z,
			final int yMin, final int yMax) {
		final BlockPos mutableBlockPos = new BlockPos();

		for (int y = yMin; y <= yMax; ++y)
			worldObj.checkLightFor(lightType, mutableBlockPos.func_181079_c(x, y, z));
	}

	private static int alfheim$getFlagIndex(final EnumSkyBlock lightType, final int xOffset, final int zOffset,
			final EnumFacing.AxisDirection axisDirection, final EnumBoundaryFacing boundaryFacing) {
		return (lightType == EnumSkyBlock.BLOCK ? 0 : 16) | ((xOffset + 1) << 2) | ((zOffset + 1) << 1)
				| (axisDirection.getOffset() + 1) | boundaryFacing.ordinal();
	}

	private static int alfheim$getFlagIndex(final EnumSkyBlock lightType, final EnumFacing facing,
			final EnumFacing.AxisDirection axisDirection, final EnumBoundaryFacing boundaryFacing) {
		return alfheim$getFlagIndex(lightType, facing.getFrontOffsetX(), facing.getFrontOffsetZ(), axisDirection,
				boundaryFacing);
	}

	private static void alfheim$initChunkLighting(final Chunk chunk, final World world) {
		final int xBase = chunk.xPosition << 4;
		final int zBase = chunk.zPosition << 4;

		final BlockPos mutableBlockPos = new BlockPos(xBase, 0, zBase);

		if (world.isAreaLoaded(mutableBlockPos.add(-16, 0, -16), mutableBlockPos.add(31, 255, 31), false)) {
			final ExtendedBlockStorage[] extendedBlockStorage = chunk.getBlockStorageArray();

			for (int i = 0; i < extendedBlockStorage.length; ++i) {
				final ExtendedBlockStorage storage = extendedBlockStorage[i];

				if (storage == null)
					continue;

				int yBase = i * 16;

				for (int y = 0; y < 16; y++) {
					for (int z = 0; z < 16; z++) {
						for (int x = 0; x < 16; x++) {
							if (storage.getBlockByExtId(x, y, z).getLightValue() > 0) {
								mutableBlockPos.func_181079_c(xBase + x, yBase + y, zBase + z);
								world.checkLightFor(EnumSkyBlock.BLOCK, mutableBlockPos);
							}
						}
					}
				}
			}

			if (!world.provider.getHasNoSky())
				chunk.alfheim$setSkylightUpdatedPublic();

			chunk.alfheim$setLightInitialized(true);
		}
	}

	private void alfheim$relightSkylightColumn(final int x, final int z, final int height1, final int height2) {
		final int yMin = Math.min(height1, height2);
		final int yMax = Math.max(height1, height2) - 1;

		final ExtendedBlockStorage[] sections = getBlockStorageArray();

		final int xBase = (xPosition << 4) + x;
		final int zBase = (zPosition << 4) + z;

		alfheim$scheduleRelightChecksForColumn(EnumSkyBlock.SKY, xBase, zBase, yMin, yMax);

		if (sections[yMin >> 4] == null && yMin > 0) {
			worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(xBase, yMin - 1, zBase));
		}

		short emptySections = 0;

		for (int sec = yMax >> 4; sec >= yMin >> 4; --sec) {
			if (sections[sec] == null) {
				emptySections |= (short) (1 << sec);
			}
		}

		if (emptySections != 0) {
			for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
				final int xOffset = facing.getFrontOffsetX();
				final int zOffset = facing.getFrontOffsetZ();

				final boolean neighborColumnExists = (((x + xOffset) | (z + zOffset)) & 16) == 0
						// Checks whether the position is at the specified border (the 16 bit is set for
						// both 15+1 and 0-1)
						|| worldObj.getChunkProvider().getLoadedChunk(xPosition + xOffset, zPosition + zOffset) != null;

				if (neighborColumnExists) {
					for (int sec = yMax >> 4; sec >= yMin >> 4; --sec) {
						if ((emptySections & (1 << sec)) != 0)
							alfheim$scheduleRelightChecksForColumn(EnumSkyBlock.SKY, xBase + xOffset, zBase + zOffset,
									sec << 4, (sec << 4) + 15);
					}
				} else {
					alfheim$initNeighborLightChecks();

					final EnumFacing.AxisDirection axisDirection = ((facing.getAxis() == EnumFacing.Axis.X ? z : x)
							& 15) < 8 ? EnumFacing.AxisDirection.NEGATIVE : EnumFacing.AxisDirection.POSITIVE;
					alfheim$neighborLightChecks[alfheim$getFlagIndex(EnumSkyBlock.SKY, facing, axisDirection,
							EnumBoundaryFacing.OUT)] |= emptySections;

					setChunkModified();
				}
			}
		}
	}

	public LightingEngine alfheim$getLightingEngine() {
		return alfheim$lightingEngine;
	}

	public boolean alfheim$isLightInitialized() {
		return alfheim$isLightInitialized;
	}

	public void alfheim$setLightInitialized(final boolean lightInitialized) {
		alfheim$isLightInitialized = lightInitialized;
	}

	public void alfheim$setSkylightUpdatedPublic() {
		func_177441_y();
	}

	public void alfheim$initNeighborLightChecks() {
		if (alfheim$neighborLightChecks == null) {
			alfheim$neighborLightChecks = new short[32];
		}
	}

	public byte alfheim$getCachedLightFor(final EnumSkyBlock lightType, final BlockPos blockPos) {
		final int x = blockPos.x & 15;
		final int y = blockPos.y;
		final int z = blockPos.z & 15;

		final ExtendedBlockStorage extendedblockstorage = storageArrays[y >> 4];

		if (extendedblockstorage == null)
			return canSeeSky(blockPos) ? (byte) lightType.defaultLightValue : 0;
		else if (lightType == EnumSkyBlock.SKY)
			return !worldObj.provider.getHasNoSky() ? (byte) extendedblockstorage.getExtSkylightValue(x, y & 15, z) : 0;
		else
			return lightType == EnumSkyBlock.BLOCK ? (byte) extendedblockstorage.getExtBlocklightValue(x, y & 15, z)
					: (byte) lightType.defaultLightValue;
	}
}