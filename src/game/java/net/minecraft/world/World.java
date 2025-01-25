package net.minecraft.world;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.LongHashSet;
import com.carrotsearch.hppc.LongSet;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import dev.redstudio.alfheim.lighting.LightingEngine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.HString;

import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;

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
public abstract class World implements IBlockAccess {
	private int field_181546_a = 63;
	protected boolean scheduledUpdatesAreImmediate;
	/**+
	 * A list of all Entities in all currently-loaded chunks
	 */
	public final List<Entity> loadedEntityList = Lists.newArrayList();
	protected final List<Entity> unloadedEntityList = Lists.newArrayList();
	/**+
	 * A list of the loaded tile entities in the world
	 */
	public final Set<TileEntity> loadedTileEntityList = Sets.newIdentityHashSet();
	public final Set<TileEntity> tickableTileEntities = Sets.newIdentityHashSet();
	private final List<TileEntity> addedTileEntityList = Lists.newArrayList();
	private final List<TileEntity> tileEntitiesToBeRemoved = Lists.newArrayList();
	/**+
	 * Array list of players in the world.
	 */
	public final List<EntityPlayer> playerEntities = Lists.newArrayList();
	/**+
	 * a list of all the lightning entities
	 */
	public final List<Entity> weatherEffects = Lists.newArrayList();
	protected final IntObjectMap<Entity> entitiesById = new IntObjectHashMap<>();
	private long cloudColour = 16777215L;
	private int skylightSubtracted;
	/**+
	 * Contains the current Linear Congruential Generator seed for
	 * block updates. Used with an A value of 3 and a C value of
	 * 0x3c6ef35f, producing a highly planar series of values
	 * ill-suited for choosing random blocks in a 16x128x16 field.
	 */
	protected int updateLCG = (new EaglercraftRandom()).nextInt();
	/**+
	 * magic number used to generate fast random numbers for 3d
	 * distribution within a chunk
	 */
	protected final int DIST_HASH_MAGIC = 1013904223;
	protected float prevRainingStrength;
	protected float rainingStrength;
	protected float prevThunderingStrength;
	protected float thunderingStrength;
	private int lastLightningBolt;
	/**+
	 * RNG for World.
	 */
	public final EaglercraftRandom rand = new EaglercraftRandom();
	public final WorldProvider provider;
	protected List<IWorldAccess> worldAccesses = Lists.newArrayList();
	protected IChunkProvider chunkProvider;
	protected final ISaveHandler saveHandler;
	protected WorldInfo worldInfo;
	protected boolean findingSpawnPoint;
	protected MapStorage mapStorage;
	protected VillageCollection villageCollectionObj;
	private final Calendar theCalendar = Calendar.getInstance();
	protected Scoreboard worldScoreboard = new Scoreboard();
	/**+
	 * populated by chunks that are within 9 chunks of any player
	 */
	protected LongSet activeChunkSet = new LongHashSet();
	private int ambientTickCountdown;
	protected boolean spawnHostileMobs;
	protected boolean spawnPeacefulMobs;
	private boolean processingLoadedTiles;
	private final WorldBorder worldBorder;
	public final boolean isRemote;

	private LightingEngine alfheim$lightingEngine;

	protected World(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, boolean client) {
		this.ambientTickCountdown = this.rand.nextInt(12000);
		this.spawnHostileMobs = true;
		this.spawnPeacefulMobs = true;
		this.saveHandler = saveHandlerIn;
		this.worldInfo = info;
		this.provider = providerIn;
		this.worldBorder = providerIn.getWorldBorder();
		this.isRemote = client;
		this.alfheim$lightingEngine = new LightingEngine(this);
	}

	public World init() {
		return this;
	}

	public BiomeGenBase getBiomeGenForCoords(final BlockPos pos) {
		if (this.isBlockLoaded(pos)) {
			Chunk chunk = this.getChunkFromBlockCoords(pos);

			try {
				return chunk.getBiome(pos, this.provider.getWorldChunkManager());
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting biome");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Coordinates of biome request");
				crashreportcategory.addCrashSectionCallable("Location", new Callable<String>() {
					public String call() throws Exception {
						return CrashReportCategory
								.getCoordinateInfo(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ()));
					}
				});
				throw new ReportedException(crashreport);
			}
		} else {
			return this.provider.getWorldChunkManager().getBiomeGenerator(pos, BiomeGenBase.plains);
		}
	}

	public int getBiomeColorForCoords(BlockPos var1, int index) {
		return BiomeColorHelper.getBiomeColorForCoordsOld(this, var1, index);
	}

	public WorldChunkManager getWorldChunkManager() {
		return this.provider.getWorldChunkManager();
	}

	protected abstract IChunkProvider createChunkProvider();

	public void initialize(WorldSettings settings) {
		this.worldInfo.setServerInitialized(true);
	}

	/**+
	 * Sets a new spawn location by finding an uncovered block at a
	 * random (x,z) location in the chunk.
	 */
	public void setInitialSpawnLocation() {
		this.setSpawnPoint(new BlockPos(8, 64, 8));
	}

	public Block getGroundAboveSeaLevel(BlockPos pos) {
		BlockPos blockpos;
		for (blockpos = new BlockPos(pos.getX(), this.func_181545_F(), pos.getZ()); !this
				.isAirBlock(blockpos.up()); blockpos = blockpos.up()) {
			;
		}

		return this.getBlockState(blockpos).getBlock();
	}

	/**+
	 * Check if the given BlockPos has valid coordinates
	 */
	private boolean isValid(BlockPos pos) {
		return pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000
				&& pos.getY() >= 0 && pos.getY() < 256;
	}

	/**+
	 * Checks to see if an air block exists at the provided
	 * location. Note that this only checks to see if the blocks
	 * material is set to air, meaning it is possible for
	 * non-vanilla blocks to still pass this check.
	 */
	public boolean isAirBlock(BlockPos pos) {
		return this.getBlockState(pos).getBlock().getMaterial() == Material.air;
	}

	public boolean isBlockLoaded(BlockPos pos) {
		return !this.isValid(pos) ? false : this.isChunkLoaded(pos.x >> 4, pos.z >> 4, true);
	}

	public boolean isBlockLoaded(BlockPos pos, boolean allowEmpty) {
		return !this.isValid(pos) ? false : this.isChunkLoaded(pos.x >> 4, pos.z >> 4, allowEmpty);
	}

	public boolean isAreaLoaded(BlockPos center, int radius) {
		return this.isAreaLoaded(center, radius, true);
	}

	public boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty) {
		return this.isAreaLoaded(center.getX() - radius, center.getY() - radius, center.getZ() - radius,
				center.getX() + radius, center.getY() + radius, center.getZ() + radius, allowEmpty);
	}

	public boolean isAreaLoaded(BlockPos from, BlockPos to) {
		return this.isAreaLoaded(from, to, true);
	}

	public boolean isAreaLoaded(BlockPos from, BlockPos to, boolean allowEmpty) {
		return this.isAreaLoaded(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), allowEmpty);
	}

	public boolean isAreaLoaded(StructureBoundingBox box) {
		return this.isAreaLoaded(box, true);
	}

	public boolean isAreaLoaded(StructureBoundingBox box, boolean allowEmpty) {
		return this.isAreaLoaded(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, allowEmpty);
	}

	private boolean isAreaLoaded(int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd, boolean allowEmpty) {
		if (yEnd >= 0 && yStart < 256) {
			xStart = xStart >> 4;
			zStart = zStart >> 4;
			xEnd = xEnd >> 4;
			zEnd = zEnd >> 4;

			for (int i = xStart; i <= xEnd; ++i) {
				for (int j = zStart; j <= zEnd; ++j) {
					if (!this.isChunkLoaded(i, j, allowEmpty)) {
						return false;
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}

	protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
		return this.chunkProvider.chunkExists(x, z) && (allowEmpty || !this.chunkProvider.provideChunk(x, z).isEmpty());
	}

	public Chunk getChunkFromBlockCoords(BlockPos pos) {
		return this.chunkProvider.provideChunk(pos.x >> 4, pos.z >> 4);
	}

	public Chunk getChunkFromBlockCoordsIfLoaded(BlockPos pos) {
		int x = pos.x >> 4;
		int z = pos.z >> 4;
		return this.chunkProvider.chunkExists(x, z) ? this.chunkProvider.provideChunk(x, z) : null;
	}

	/**+
	 * Returns back a chunk looked up by chunk coordinates Args: x,
	 * y
	 */
	public Chunk getChunkFromChunkCoords(int chunkX, int chunkZ) {
		return this.chunkProvider.provideChunk(chunkX, chunkZ);
	}

	public Chunk getChunkFromChunkCoordsIfLoaded(int chunkX, int chunkZ) {
		return this.chunkProvider.chunkExists(chunkX, chunkZ) ? this.chunkProvider.provideChunk(chunkX, chunkZ) : null;
	}

	/**+
	 * Convenience method to update the block on both the client and
	 * server
	 */
	public boolean setBlockState(BlockPos pos, IBlockState newState, int flags) {
		if (!this.isValid(pos)) {
			return false;
		} else if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
			return false;
		} else {
			Chunk chunk = this.getChunkFromBlockCoords(pos);
			Block block = newState.getBlock();
			IBlockState iblockstate = chunk.setBlockState(pos, newState);
			if (iblockstate == null) {
				return false;
			} else {
				Block block1 = iblockstate.getBlock();
				if (block.getLightOpacity() != block1.getLightOpacity()
						|| block.getLightValue() != block1.getLightValue()) {
					this.checkLight(pos);
				}

				if ((flags & 2) != 0 && (!this.isRemote || (flags & 4) == 0) && chunk.isPopulated()) {
					this.markBlockForUpdate(pos);
				}

				if (!this.isRemote && (flags & 1) != 0) {
					this.notifyNeighborsRespectDebug(pos, iblockstate.getBlock());
					if (block.hasComparatorInputOverride()) {
						this.updateComparatorOutputLevel(pos, block);
					}
				}

				return true;
			}
		}
	}

	public boolean setBlockToAir(BlockPos pos) {
		return this.setBlockState(pos, Blocks.air.getDefaultState(), 3);
	}

	/**+
	 * Sets a block to air, but also plays the sound and particles
	 * and can spawn drops
	 */
	public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
		IBlockState iblockstate = this.getBlockState(pos);
		Block block = iblockstate.getBlock();
		if (block.getMaterial() == Material.air) {
			return false;
		} else {
			this.playAuxSFX(2001, pos, Block.getStateId(iblockstate));
			if (dropBlock) {
				block.dropBlockAsItem(this, pos, iblockstate, 0);
			}

			return this.setBlockState(pos, Blocks.air.getDefaultState(), 3);
		}
	}

	/**+
	 * Convenience method to update the block on both the client and
	 * server
	 */
	public boolean setBlockState(BlockPos pos, IBlockState state) {
		return this.setBlockState(pos, state, 3);
	}

	public void markBlockForUpdate(BlockPos pos) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			((IWorldAccess) this.worldAccesses.get(i)).markBlockForUpdate(pos);
		}

	}

	public void notifyNeighborsRespectDebug(BlockPos pos, Block blockType) {
		if (this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD) {
			this.notifyNeighborsOfStateChange(pos, blockType);
		}

	}

	/**+
	 * marks a vertical line of blocks as dirty
	 */
	public void markBlocksDirtyVertical(int x1, int z1, int x2, int z2) {
		if (x2 > z2) {
			int i = z2;
			z2 = x2;
			x2 = i;
		}

		if (!this.provider.getHasNoSky()) {
			for (int j = x2; j <= z2; ++j) {
				this.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x1, j, z1));
			}
		}

		this.markBlockRangeForRenderUpdate(x1, x2, z1, x1, z2, z1);
	}

	public void markBlockRangeForRenderUpdate(BlockPos rangeMin, BlockPos rangeMax) {
		this.markBlockRangeForRenderUpdate(rangeMin.getX(), rangeMin.getY(), rangeMin.getZ(), rangeMax.getX(),
				rangeMax.getY(), rangeMax.getZ());
	}

	public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			((IWorldAccess) this.worldAccesses.get(i)).markBlockRangeForRenderUpdate(x1, y1, z1, x2, y2, z2);
		}

	}

	public void notifyNeighborsOfStateChange(BlockPos pos, Block blockType) {
		this.notifyBlockOfStateChange(pos.west(), blockType);
		this.notifyBlockOfStateChange(pos.east(), blockType);
		this.notifyBlockOfStateChange(pos.down(), blockType);
		this.notifyBlockOfStateChange(pos.up(), blockType);
		this.notifyBlockOfStateChange(pos.north(), blockType);
		this.notifyBlockOfStateChange(pos.south(), blockType);
	}

	public void notifyNeighborsOfStateExcept(BlockPos pos, Block blockType, EnumFacing skipSide) {
		if (skipSide != EnumFacing.WEST) {
			this.notifyBlockOfStateChange(pos.west(), blockType);
		}

		if (skipSide != EnumFacing.EAST) {
			this.notifyBlockOfStateChange(pos.east(), blockType);
		}

		if (skipSide != EnumFacing.DOWN) {
			this.notifyBlockOfStateChange(pos.down(), blockType);
		}

		if (skipSide != EnumFacing.UP) {
			this.notifyBlockOfStateChange(pos.up(), blockType);
		}

		if (skipSide != EnumFacing.NORTH) {
			this.notifyBlockOfStateChange(pos.north(), blockType);
		}

		if (skipSide != EnumFacing.SOUTH) {
			this.notifyBlockOfStateChange(pos.south(), blockType);
		}

	}

	public void notifyBlockOfStateChange(BlockPos pos, final Block blockIn) {
		if (!this.isRemote) {
			IBlockState iblockstate = this.getBlockState(pos);

			try {
				iblockstate.getBlock().onNeighborBlockChange(this, pos, iblockstate, blockIn);
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
				crashreportcategory.addCrashSectionCallable("Source block type", new Callable<String>() {
					public String call() throws Exception {
						try {
							return HString.format("ID #%d (%s // %s)",
									new Object[] { Integer.valueOf(Block.getIdFromBlock(blockIn)),
											blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName() });
						} catch (Throwable var2) {
							return "ID #" + Block.getIdFromBlock(blockIn);
						}
					}
				});
				CrashReportCategory.addBlockInfo(crashreportcategory, pos, iblockstate);
				throw new ReportedException(crashreport);
			}
		}
	}

	public boolean isBlockTickPending(BlockPos pos, Block blockType) {
		return false;
	}

	public boolean canSeeSky(BlockPos pos) {
		return this.getChunkFromBlockCoords(pos).canSeeSky(pos);
	}

	public boolean canBlockSeeSky(BlockPos pos) {
		if (pos.getY() >= this.func_181545_F()) {
			return this.canSeeSky(pos);
		} else {
			BlockPos blockpos = new BlockPos(pos.getX(), this.func_181545_F(), pos.getZ());
			if (!this.canSeeSky(blockpos)) {
				return false;
			} else {
				for (blockpos = blockpos.down(); blockpos.getY() > pos.getY(); blockpos = blockpos.down()) {
					Block block = this.getBlockState(blockpos).getBlock();
					if (block.getLightOpacity() > 0 && !block.getMaterial().isLiquid()) {
						return false;
					}
				}

				return true;
			}
		}
	}

	public int getLight(BlockPos pos) {
		if (pos.getY() < 0) {
			return 0;
		} else {
			if (pos.getY() >= 256) {
				pos = new BlockPos(pos.getX(), 255, pos.getZ());
			}

			return this.getChunkFromBlockCoords(pos).getLightSubtracted(pos, 0);
		}
	}

	public int getLightFromNeighbors(BlockPos pos) {
		return this.getLight(pos, true);
	}

	public int getLight(BlockPos pos, boolean checkNeighbors) {
		if (!checkNeighbors)
			return getLight(pos);

		final IBlockState blockState = getBlockState(pos);

		return Math.max(blockState.getBlock().alfheim$getLightFor(blockState, this, EnumSkyBlock.BLOCK, pos),
				blockState.getBlock().alfheim$getLightFor(blockState, this, EnumSkyBlock.SKY, pos)
						- skylightSubtracted);
	}

	/**+
	 * Returns maximum world height.
	 */
	public BlockPos getHeight(BlockPos pos) {
		int i;
		if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000) {
			if (this.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, true)) {
				i = this.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4).getHeightValue(pos.getX() & 15,
						pos.getZ() & 15);
			} else {
				i = 0;
			}
		} else {
			i = this.func_181545_F() + 1;
		}

		return new BlockPos(pos.getX(), i, pos.getZ());
	}

	/**+
	 * Gets the lowest height of the chunk where sunlight directly
	 * reaches
	 */
	public int getChunksLowestHorizon(int x, int z) {
		if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000) {
			if (!this.isChunkLoaded(x >> 4, z >> 4, true)) {
				return 0;
			} else {
				Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
				return chunk.getLowestHeight();
			}
		} else {
			return this.func_181545_F() + 1;
		}
	}

	public int getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos) {
		IBlockState state = getBlockState(pos);
		return state.getBlock().alfheim$getLightFor(state, this, type, pos);
	}

	public int getLightFor(EnumSkyBlock type, BlockPos pos) {
		if (pos.getY() < 0) {
			pos = new BlockPos(pos.getX(), 0, pos.getZ());
		}

		if (!this.isValid(pos)) {
			return type.defaultLightValue;
		} else if (!this.isBlockLoaded(pos)) {
			return type.defaultLightValue;
		} else {
			Chunk chunk = this.getChunkFromBlockCoords(pos);
			return chunk.getLightFor(type, pos);
		}
	}

	public void setLightFor(EnumSkyBlock type, BlockPos pos, int lightValue) {
		if (this.isValid(pos)) {
			if (this.isBlockLoaded(pos)) {
				Chunk chunk = this.getChunkFromBlockCoords(pos);
				chunk.setLightFor(type, pos, lightValue);
				this.notifyLightSet(pos);
			}
		}
	}

	public void notifyLightSet(BlockPos pos) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			((IWorldAccess) this.worldAccesses.get(i)).notifyLightSet(pos);
		}

	}

	public int getCombinedLight(BlockPos pos, int lightValue) {
		int i = this.getLightFromNeighborsFor(EnumSkyBlock.SKY, pos);
		int j = this.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, pos);
		if (lightValue < 0) {
			j += -lightValue;
			if (j > 15) {
				j = 15;
			}
		} else if (j < lightValue) {
			j = lightValue;
		}

		return i << 20 | j << 4;
	}

	public float getLightBrightness(BlockPos pos) {
		return this.provider.getLightBrightnessTable()[this.getLightFromNeighbors(pos)];
	}

	public IBlockState getBlockState(BlockPos pos) {
		if (!this.isValid(pos)) {
			return Blocks.air.getDefaultState();
		} else {
			Chunk chunk = this.getChunkFromBlockCoords(pos);
			return chunk.getBlockState(pos);
		}
	}

	public IBlockState getBlockStateIfLoaded(BlockPos pos) {
		if (!this.isValid(pos)) {
			return Blocks.air.getDefaultState();
		} else {
			Chunk chunk = this.getChunkFromBlockCoordsIfLoaded(pos);
			return chunk != null ? chunk.getBlockState(pos) : null;
		}
	}

	/**+
	 * Checks whether its daytime by seeing if the light subtracted
	 * from the skylight is less than 4
	 */
	public boolean isDaytime() {
		return this.skylightSubtracted < 4;
	}

	/**+
	 * ray traces all blocks, including non-collideable ones
	 */
	public MovingObjectPosition rayTraceBlocks(Vec3 parVec3_1, Vec3 parVec3_2) {
		return this.rayTraceBlocks(parVec3_1, parVec3_2, false, false, false);
	}

	/**+
	 * ray traces all blocks, including non-collideable ones
	 */
	public MovingObjectPosition rayTraceBlocks(Vec3 start, Vec3 end, boolean stopOnLiquid) {
		return this.rayTraceBlocks(start, end, stopOnLiquid, false, false);
	}

	/**+
	 * ray traces all blocks, including non-collideable ones
	 */
	public MovingObjectPosition rayTraceBlocks(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid,
			boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
		if (!Double.isNaN(vec31.xCoord) && !Double.isNaN(vec31.yCoord) && !Double.isNaN(vec31.zCoord)) {
			if (!Double.isNaN(vec32.xCoord) && !Double.isNaN(vec32.yCoord) && !Double.isNaN(vec32.zCoord)) {
				int i = MathHelper.floor_double(vec32.xCoord);
				int j = MathHelper.floor_double(vec32.yCoord);
				int k = MathHelper.floor_double(vec32.zCoord);
				int l = MathHelper.floor_double(vec31.xCoord);
				int i1 = MathHelper.floor_double(vec31.yCoord);
				int j1 = MathHelper.floor_double(vec31.zCoord);
				BlockPos blockpos = new BlockPos(l, i1, j1);
				IBlockState iblockstate = this.getBlockStateIfLoaded(blockpos);
				if (iblockstate == null)
					return null;
				Block block = iblockstate.getBlock();
				if ((!ignoreBlockWithoutBoundingBox
						|| block.getCollisionBoundingBox(this, blockpos, iblockstate) != null)
						&& block.canCollideCheck(iblockstate, stopOnLiquid)) {
					MovingObjectPosition movingobjectposition = block.collisionRayTrace(this, blockpos, vec31, vec32);
					if (movingobjectposition != null) {
						return movingobjectposition;
					}
				}

				MovingObjectPosition movingobjectposition2 = null;
				int k1 = 200;

				while (k1-- >= 0) {
					if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord)) {
						return null;
					}

					if (l == i && i1 == j && j1 == k) {
						return returnLastUncollidableBlock ? movingobjectposition2 : null;
					}

					boolean flag2 = true;
					boolean flag = true;
					boolean flag1 = true;
					double d0 = 999.0D;
					double d1 = 999.0D;
					double d2 = 999.0D;
					if (i > l) {
						d0 = (double) l + 1.0D;
					} else if (i < l) {
						d0 = (double) l + 0.0D;
					} else {
						flag2 = false;
					}

					if (j > i1) {
						d1 = (double) i1 + 1.0D;
					} else if (j < i1) {
						d1 = (double) i1 + 0.0D;
					} else {
						flag = false;
					}

					if (k > j1) {
						d2 = (double) j1 + 1.0D;
					} else if (k < j1) {
						d2 = (double) j1 + 0.0D;
					} else {
						flag1 = false;
					}

					double d3 = 999.0D;
					double d4 = 999.0D;
					double d5 = 999.0D;
					double d6 = vec32.xCoord - vec31.xCoord;
					double d7 = vec32.yCoord - vec31.yCoord;
					double d8 = vec32.zCoord - vec31.zCoord;
					if (flag2) {
						d3 = (d0 - vec31.xCoord) / d6;
					}

					if (flag) {
						d4 = (d1 - vec31.yCoord) / d7;
					}

					if (flag1) {
						d5 = (d2 - vec31.zCoord) / d8;
					}

					if (d3 == -0.0D) {
						d3 = -1.0E-4D;
					}

					if (d4 == -0.0D) {
						d4 = -1.0E-4D;
					}

					if (d5 == -0.0D) {
						d5 = -1.0E-4D;
					}

					EnumFacing enumfacing;
					if (d3 < d4 && d3 < d5) {
						enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
						vec31 = new Vec3(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
					} else if (d4 < d5) {
						enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
						vec31 = new Vec3(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
					} else {
						enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
						vec31 = new Vec3(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
					}

					l = MathHelper.floor_double(vec31.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
					i1 = MathHelper.floor_double(vec31.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
					j1 = MathHelper.floor_double(vec31.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
					blockpos = new BlockPos(l, i1, j1);
					IBlockState iblockstate1 = this.getBlockStateIfLoaded(blockpos);
					if (iblockstate1 == null)
						return null;
					Block block1 = iblockstate1.getBlock();
					if (!ignoreBlockWithoutBoundingBox
							|| block1.getCollisionBoundingBox(this, blockpos, iblockstate1) != null) {
						if (block1.canCollideCheck(iblockstate1, stopOnLiquid)) {
							MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(this, blockpos, vec31,
									vec32);
							if (movingobjectposition1 != null) {
								return movingobjectposition1;
							}
						} else {
							movingobjectposition2 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS,
									vec31, enumfacing, blockpos);
						}
					}
				}

				return returnLastUncollidableBlock ? movingobjectposition2 : null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**+
	 * Plays a sound at the entity's position. Args: entity, sound,
	 * volume (relative to 1.0), and frequency (or pitch, also
	 * relative to 1.0).
	 */
	public void playSoundAtEntity(Entity entityIn, String name, float volume, float pitch) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			((IWorldAccess) this.worldAccesses.get(i)).playSound(name, entityIn.posX, entityIn.posY, entityIn.posZ,
					volume, pitch);
		}

	}

	/**+
	 * Plays sound to all near players except the player reference
	 * given
	 */
	public void playSoundToNearExcept(EntityPlayer player, String name, float volume, float pitch) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			((IWorldAccess) this.worldAccesses.get(i)).playSoundToNearExcept(player, name, player.posX, player.posY,
					player.posZ, volume, pitch);
		}

	}

	/**+
	 * Play a sound effect. Many many parameters for this function.
	 * Not sure what they do, but a classic call is : (double)i +
	 * 0.5D, (double)j + 0.5D, (double)k + 0.5D, 'random.door_open',
	 * 1.0F, world.rand.nextFloat() * 0.1F + 0.9F with i,j,k
	 * position of the block.
	 */
	public void playSoundEffect(double x, double y, double z, String soundName, float volume, float pitch) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			((IWorldAccess) this.worldAccesses.get(i)).playSound(soundName, x, y, z, volume, pitch);
		}

	}

	/**+
	 * par8 is loudness, all pars passed to
	 * minecraftInstance.sndManager.playSound
	 */
	public void playSound(double x, double y, double z, String soundName, float volume, float pitch,
			boolean distanceDelay) {
	}

	public void playRecord(BlockPos pos, String name) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			((IWorldAccess) this.worldAccesses.get(i)).playRecord(name, pos);
		}

	}

	public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord,
			double xOffset, double yOffset, double zOffset, int... parArrayOfInt) {
		this.spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange(), xCoord, yCoord, zCoord,
				xOffset, yOffset, zOffset, parArrayOfInt);
	}

	public void spawnParticle(EnumParticleTypes particleType, boolean xCoord, double yCoord, double zCoord,
			double xOffset, double yOffset, double zOffset, double parDouble6, int... parArrayOfInt) {
		this.spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange() | xCoord, yCoord, zCoord,
				xOffset, yOffset, zOffset, parDouble6, parArrayOfInt);
	}

	private void spawnParticle(int particleID, boolean xCood, double yCoord, double zCoord, double xOffset,
			double yOffset, double zOffset, double parDouble6, int... parArrayOfInt) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			((IWorldAccess) this.worldAccesses.get(i)).spawnParticle(particleID, xCood, yCoord, zCoord, xOffset,
					yOffset, zOffset, parDouble6, parArrayOfInt);
		}

	}

	/**+
	 * adds a lightning bolt to the list of lightning bolts in this
	 * world.
	 */
	public boolean addWeatherEffect(Entity entityIn) {
		this.weatherEffects.add(entityIn);
		return true;
	}

	/**+
	 * Called when an entity is spawned in the world. This includes
	 * players.
	 */
	public boolean spawnEntityInWorld(Entity entityIn) {
		int i = MathHelper.floor_double(entityIn.posX / 16.0D);
		int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
		boolean flag = entityIn.forceSpawn;
		if (entityIn instanceof EntityPlayer) {
			flag = true;
		}

		if (!flag && !this.isChunkLoaded(i, j, true)) {
			return false;
		} else {
			if (entityIn instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entityIn;
				this.playerEntities.add(entityplayer);
				this.updateAllPlayersSleepingFlag();
			}

			this.getChunkFromChunkCoords(i, j).addEntity(entityIn);
			this.loadedEntityList.add(entityIn);
			this.onEntityAdded(entityIn);
			return true;
		}
	}

	protected void onEntityAdded(Entity entityIn) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			((IWorldAccess) this.worldAccesses.get(i)).onEntityAdded(entityIn);
		}

	}

	protected void onEntityRemoved(Entity entityIn) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			((IWorldAccess) this.worldAccesses.get(i)).onEntityRemoved(entityIn);
		}

	}

	/**+
	 * Schedule the entity for removal during the next tick. Marks
	 * the entity dead in anticipation.
	 */
	public void removeEntity(Entity entityIn) {
		if (entityIn.riddenByEntity != null) {
			entityIn.riddenByEntity.mountEntity((Entity) null);
		}

		if (entityIn.ridingEntity != null) {
			entityIn.mountEntity((Entity) null);
		}

		entityIn.setDead();
		if (entityIn instanceof EntityPlayer) {
			this.playerEntities.remove(entityIn);
			this.updateAllPlayersSleepingFlag();
			this.onEntityRemoved(entityIn);
		}

	}

	/**+
	 * Do NOT use this method to remove normal entities- use normal
	 * removeEntity
	 */
	public void removePlayerEntityDangerously(Entity entityIn) {
		entityIn.setDead();
		if (entityIn instanceof EntityPlayer) {
			this.playerEntities.remove(entityIn);
			this.updateAllPlayersSleepingFlag();
		}

		int i = entityIn.chunkCoordX;
		int j = entityIn.chunkCoordZ;
		if (entityIn.addedToChunk && this.isChunkLoaded(i, j, true)) {
			this.getChunkFromChunkCoords(i, j).removeEntity(entityIn);
		}

		this.loadedEntityList.remove(entityIn);
		this.onEntityRemoved(entityIn);
	}

	/**+
	 * Adds a IWorldAccess to the list of worldAccesses
	 */
	public void addWorldAccess(IWorldAccess worldAccess) {
		this.worldAccesses.add(worldAccess);
	}

	/**+
	 * Removes a worldAccess from the worldAccesses object
	 */
	public void removeWorldAccess(IWorldAccess worldAccess) {
		this.worldAccesses.remove(worldAccess);
	}

	/**+
	 * Returns a list of bounding boxes that collide with aabb
	 * excluding the passed in entity's collision. Args: entity,
	 * aabb
	 */
	public List<AxisAlignedBB> getCollidingBoundingBoxes(Entity entityIn, AxisAlignedBB bb) {
		ArrayList arraylist = Lists.newArrayList();
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX + 1.0D);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY + 1.0D);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
		WorldBorder worldborder = this.getWorldBorder();
		boolean flag = entityIn.isOutsideBorder();
		boolean flag1 = this.isInsideBorder(worldborder, entityIn);
		IBlockState iblockstate = Blocks.stone.getDefaultState();
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = i1; l1 < j1; ++l1) {
				if (this.isBlockLoaded(blockpos$mutableblockpos.func_181079_c(k1, 64, l1))) {
					for (int i2 = k - 1; i2 < l; ++i2) {
						blockpos$mutableblockpos.func_181079_c(k1, i2, l1);
						if (flag && flag1) {
							entityIn.setOutsideBorder(false);
						} else if (!flag && !flag1) {
							entityIn.setOutsideBorder(true);
						}

						IBlockState iblockstate1 = iblockstate;
						if (worldborder.contains((BlockPos) blockpos$mutableblockpos) || !flag1) {
							iblockstate1 = this.getBlockState(blockpos$mutableblockpos);
						}

						iblockstate1.getBlock().addCollisionBoxesToList(this, blockpos$mutableblockpos, iblockstate1,
								bb, arraylist, entityIn);
					}
				}
			}
		}

		double d0 = 0.25D;
		List list = this.getEntitiesWithinAABBExcludingEntity(entityIn, bb.expand(d0, d0, d0));

		for (int j2 = 0; j2 < list.size(); ++j2) {
			if (entityIn.riddenByEntity != list && entityIn.ridingEntity != list) {
				AxisAlignedBB axisalignedbb = ((Entity) list.get(j2)).getCollisionBoundingBox();
				if (axisalignedbb != null && axisalignedbb.intersectsWith(bb)) {
					arraylist.add(axisalignedbb);
				}

				axisalignedbb = entityIn.getCollisionBox((Entity) list.get(j2));
				if (axisalignedbb != null && axisalignedbb.intersectsWith(bb)) {
					arraylist.add(axisalignedbb);
				}
			}
		}

		return arraylist;
	}

	public boolean isInsideBorder(WorldBorder worldBorderIn, Entity entityIn) {
		double d0 = worldBorderIn.minX();
		double d1 = worldBorderIn.minZ();
		double d2 = worldBorderIn.maxX();
		double d3 = worldBorderIn.maxZ();
		if (entityIn.isOutsideBorder()) {
			++d0;
			++d1;
			--d2;
			--d3;
		} else {
			--d0;
			--d1;
			++d2;
			++d3;
		}

		return entityIn.posX > d0 && entityIn.posX < d2 && entityIn.posZ > d1 && entityIn.posZ < d3;
	}

	public List<AxisAlignedBB> func_147461_a(AxisAlignedBB bb) {
		ArrayList arraylist = Lists.newArrayList();
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX + 1.0D);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY + 1.0D);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = i1; l1 < j1; ++l1) {
				if (this.isBlockLoaded(blockpos$mutableblockpos.func_181079_c(k1, 64, l1))) {
					for (int i2 = k - 1; i2 < l; ++i2) {
						blockpos$mutableblockpos.func_181079_c(k1, i2, l1);
						IBlockState iblockstate;
						if (k1 >= -30000000 && k1 < 30000000 && l1 >= -30000000 && l1 < 30000000) {
							iblockstate = this.getBlockState(blockpos$mutableblockpos);
						} else {
							iblockstate = Blocks.bedrock.getDefaultState();
						}

						iblockstate.getBlock().addCollisionBoxesToList(this, blockpos$mutableblockpos, iblockstate, bb,
								arraylist, (Entity) null);
					}
				}
			}
		}

		return arraylist;
	}

	/**+
	 * Returns the amount of skylight subtracted for the current
	 * time
	 */
	public int calculateSkylightSubtracted(float parFloat1) {
		float f = this.getCelestialAngle(parFloat1);
		float f1 = 1.0F - (MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F);
		f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
		f1 = 1.0F - f1;
		f1 = (float) ((double) f1 * (1.0D - (double) (this.getRainStrength(parFloat1) * 5.0F) / 16.0D));
		f1 = (float) ((double) f1 * (1.0D - (double) (this.getThunderStrength(parFloat1) * 5.0F) / 16.0D));
		f1 = 1.0F - f1;
		return (int) (f1 * 11.0F);
	}

	/**+
	 * Returns the sun brightness - checks time of day, rain and
	 * thunder
	 */
	public float getSunBrightness(float parFloat1) {
		float f = this.getCelestialAngle(parFloat1);
		float f1 = 1.0F - (MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.2F);
		f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
		f1 = 1.0F - f1;
		f1 = (float) ((double) f1 * (1.0D - (double) (this.getRainStrength(parFloat1) * 5.0F) / 16.0D));
		f1 = (float) ((double) f1 * (1.0D - (double) (this.getThunderStrength(parFloat1) * 5.0F) / 16.0D));
		return f1 * 0.8F + 0.2F;
	}

	/**+
	 * Calculates the color for the skybox
	 */
	public Vec3 getSkyColor(Entity entityIn, float partialTicks) {
		float f = this.getCelestialAngle(partialTicks);
		float f1 = MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F;
		f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
		int i = MathHelper.floor_double(entityIn.posX);
		int j = MathHelper.floor_double(entityIn.posY);
		int k = MathHelper.floor_double(entityIn.posZ);
		BlockPos blockpos = new BlockPos(i, j, k);
		BiomeGenBase biomegenbase = this.getBiomeGenForCoords(blockpos);
		float f2 = biomegenbase.getFloatTemperature(blockpos);
		int l = biomegenbase.getSkyColorByTemp(f2);
		float f3 = (float) (l >> 16 & 255) / 255.0F;
		float f4 = (float) (l >> 8 & 255) / 255.0F;
		float f5 = (float) (l & 255) / 255.0F;
		f3 = f3 * f1;
		f4 = f4 * f1;
		f5 = f5 * f1;
		float f6 = this.getRainStrength(partialTicks);
		if (f6 > 0.0F) {
			float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
			float f8 = 1.0F - f6 * 0.75F;
			f3 = f3 * f8 + f7 * (1.0F - f8);
			f4 = f4 * f8 + f7 * (1.0F - f8);
			f5 = f5 * f8 + f7 * (1.0F - f8);
		}

		float f10 = this.getThunderStrength(partialTicks);
		if (f10 > 0.0F) {
			float f11 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
			float f9 = 1.0F - f10 * 0.75F;
			f3 = f3 * f9 + f11 * (1.0F - f9);
			f4 = f4 * f9 + f11 * (1.0F - f9);
			f5 = f5 * f9 + f11 * (1.0F - f9);
		}

		if (this.lastLightningBolt > 0) {
			float f12 = (float) this.lastLightningBolt - partialTicks;
			if (f12 > 1.0F) {
				f12 = 1.0F;
			}

			f12 = f12 * 0.45F;
			f3 = f3 * (1.0F - f12) + 0.8F * f12;
			f4 = f4 * (1.0F - f12) + 0.8F * f12;
			f5 = f5 * (1.0F - f12) + 1.0F * f12;
		}

		return new Vec3((double) f3, (double) f4, (double) f5);
	}

	/**+
	 * calls calculateCelestialAngle
	 */
	public float getCelestialAngle(float partialTicks) {
		return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), partialTicks);
	}

	public int getMoonPhase() {
		return this.provider.getMoonPhase(this.worldInfo.getWorldTime());
	}

	/**+
	 * gets the current fullness of the moon expressed as a float
	 * between 1.0 and 0.0, in steps of .25
	 */
	public float getCurrentMoonPhaseFactor() {
		return WorldProvider.moonPhaseFactors[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
	}

	/**+
	 * Return getCelestialAngle()*2*PI
	 */
	public float getCelestialAngleRadians(float partialTicks) {
		float f = this.getCelestialAngle(partialTicks);
		return f * 3.1415927F * 2.0F;
	}

	public Vec3 getCloudColour(float partialTicks) {
		float f = this.getCelestialAngle(partialTicks);
		float f1 = MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F;
		f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
		float f2 = (float) (this.cloudColour >> 16 & 255L) / 255.0F;
		float f3 = (float) (this.cloudColour >> 8 & 255L) / 255.0F;
		float f4 = (float) (this.cloudColour & 255L) / 255.0F;
		float f5 = this.getRainStrength(partialTicks);
		if (f5 > 0.0F) {
			float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
			float f7 = 1.0F - f5 * 0.95F;
			f2 = f2 * f7 + f6 * (1.0F - f7);
			f3 = f3 * f7 + f6 * (1.0F - f7);
			f4 = f4 * f7 + f6 * (1.0F - f7);
		}

		f2 = f2 * (f1 * 0.9F + 0.1F);
		f3 = f3 * (f1 * 0.9F + 0.1F);
		f4 = f4 * (f1 * 0.85F + 0.15F);
		float f9 = this.getThunderStrength(partialTicks);
		if (f9 > 0.0F) {
			float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
			float f8 = 1.0F - f9 * 0.95F;
			f2 = f2 * f8 + f10 * (1.0F - f8);
			f3 = f3 * f8 + f10 * (1.0F - f8);
			f4 = f4 * f8 + f10 * (1.0F - f8);
		}

		return new Vec3((double) f2, (double) f3, (double) f4);
	}

	/**+
	 * Returns vector(ish) with R/G/B for fog
	 */
	public Vec3 getFogColor(float partialTicks) {
		float f = this.getCelestialAngle(partialTicks);
		return this.provider.getFogColor(f, partialTicks);
	}

	public BlockPos getPrecipitationHeight(BlockPos pos) {
		return this.getChunkFromBlockCoords(pos).getPrecipitationHeight(pos);
	}

	/**+
	 * Finds the highest block on the x and z coordinate that is
	 * solid or liquid, and returns its y coord.
	 */
	public BlockPos getTopSolidOrLiquidBlock(BlockPos pos) {
		Chunk chunk = this.getChunkFromBlockCoords(pos);

		BlockPos blockpos;
		BlockPos blockpos1;
		for (blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos
				.getY() >= 0; blockpos = blockpos1) {
			blockpos1 = blockpos.down();
			Material material = chunk.getBlock(blockpos1).getMaterial();
			if (material.blocksMovement() && material != Material.leaves) {
				break;
			}
		}

		return blockpos;
	}

	/**+
	 * How bright are stars in the sky
	 */
	public float getStarBrightness(float partialTicks) {
		float f = this.getCelestialAngle(partialTicks);
		float f1 = 1.0F - (MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.25F);
		f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
		return f1 * f1 * 0.5F;
	}

	public void scheduleUpdate(BlockPos pos, Block blockIn, int delay) {
	}

	public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority) {
	}

	public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority) {
	}

	/**+
	 * Updates (and cleans up) entities and tile entities
	 */
	public void updateEntities() {
		for (int i = 0; i < this.weatherEffects.size(); ++i) {
			Entity entity = (Entity) this.weatherEffects.get(i);

			try {
				++entity.ticksExisted;
				entity.onUpdate();
			} catch (Throwable throwable2) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");
				if (entity == null) {
					crashreportcategory.addCrashSection("Entity", "~~NULL~~");
				} else {
					entity.addEntityCrashInfo(crashreportcategory);
				}

				throw new ReportedException(crashreport);
			}

			if (entity.isDead) {
				this.weatherEffects.remove(i--);
			}
		}

		this.loadedEntityList.removeAll(this.unloadedEntityList);

		for (int k = 0; k < this.unloadedEntityList.size(); ++k) {
			Entity entity1 = (Entity) this.unloadedEntityList.get(k);
			int j = entity1.chunkCoordX;
			int l1 = entity1.chunkCoordZ;
			if (entity1.addedToChunk && this.isChunkLoaded(j, l1, true)) {
				this.getChunkFromChunkCoords(j, l1).removeEntity(entity1);
			}
		}

		for (int l = 0; l < this.unloadedEntityList.size(); ++l) {
			this.onEntityRemoved((Entity) this.unloadedEntityList.get(l));
		}

		this.unloadedEntityList.clear();

		for (int i1 = 0; i1 < this.loadedEntityList.size(); ++i1) {
			Entity entity2 = (Entity) this.loadedEntityList.get(i1);
			if (entity2.ridingEntity != null) {
				if (!entity2.ridingEntity.isDead && entity2.ridingEntity.riddenByEntity == entity2) {
					continue;
				}

				entity2.ridingEntity.riddenByEntity = null;
				entity2.ridingEntity = null;
			}

			if (!entity2.isDead) {
				try {
					this.updateEntity(entity2);
				} catch (Throwable throwable1) {
					CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Ticking entity");
					CrashReportCategory crashreportcategory2 = crashreport1.makeCategory("Entity being ticked");
					entity2.addEntityCrashInfo(crashreportcategory2);
					throw new ReportedException(crashreport1);
				}
			}

			if (entity2.isDead) {
				int k1 = entity2.chunkCoordX;
				int i2 = entity2.chunkCoordZ;
				if (entity2.addedToChunk && this.isChunkLoaded(k1, i2, true)) {
					this.getChunkFromChunkCoords(k1, i2).removeEntity(entity2);
				}

				this.loadedEntityList.remove(i1--);
				this.onEntityRemoved(entity2);
			}

		}

		this.processingLoadedTiles = true;
		Iterator iterator = this.tickableTileEntities.iterator();

		while (iterator.hasNext()) {
			TileEntity tileentity = (TileEntity) iterator.next();
			if (!tileentity.isInvalid() && tileentity.hasWorldObj()) {
				BlockPos blockpos = tileentity.getPos();
				if (this.isBlockLoaded(blockpos) && this.worldBorder.contains(blockpos)) {
					try {
						((ITickable) tileentity).update();
					} catch (Throwable throwable) {
						CrashReport crashreport2 = CrashReport.makeCrashReport(throwable, "Ticking block entity");
						CrashReportCategory crashreportcategory1 = crashreport2
								.makeCategory("Block entity being ticked");
						tileentity.addInfoToCrashReport(crashreportcategory1);
						throw new ReportedException(crashreport2);
					}
				}
			}

			if (tileentity.isInvalid()) {
				iterator.remove();
				this.loadedTileEntityList.remove(tileentity);
				if (this.isBlockLoaded(tileentity.getPos())) {
					this.getChunkFromBlockCoords(tileentity.getPos()).removeTileEntity(tileentity.getPos());
				}
			}
		}

		this.processingLoadedTiles = false;
		if (!this.tileEntitiesToBeRemoved.isEmpty()) {
			this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
			this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
			this.tileEntitiesToBeRemoved.clear();
		}

		if (!this.addedTileEntityList.isEmpty()) {
			for (int j1 = 0; j1 < this.addedTileEntityList.size(); ++j1) {
				TileEntity tileentity1 = (TileEntity) this.addedTileEntityList.get(j1);
				if (!tileentity1.isInvalid()) {
					if (!this.loadedTileEntityList.contains(tileentity1)) {
						this.addTileEntity(tileentity1);
					}

					if (this.isBlockLoaded(tileentity1.getPos())) {
						this.getChunkFromBlockCoords(tileentity1.getPos()).addTileEntity(tileentity1.getPos(),
								tileentity1);
					}

					this.markBlockForUpdate(tileentity1.getPos());
				}
			}

			this.addedTileEntityList.clear();
		}

	}

	public boolean addTileEntity(TileEntity tile) {
		boolean flag = this.loadedTileEntityList.add(tile);
		if (flag && tile instanceof ITickable) {
			this.tickableTileEntities.add(tile);
		}

		return flag;
	}

	public void addTileEntities(Collection<TileEntity> tileEntityCollection) {
		if (this.processingLoadedTiles) {
			this.addedTileEntityList.addAll(tileEntityCollection);
		} else {
			for (TileEntity tileentity : tileEntityCollection) {
				this.loadedTileEntityList.add(tileentity);
				if (tileentity instanceof ITickable) {
					this.tickableTileEntities.add(tileentity);
				}
			}
		}

	}

	/**+
	 * Will update the entity in the world if the chunk the entity
	 * is in is currently loaded. Args: entity
	 */
	public void updateEntity(Entity ent) {
		this.updateEntityWithOptionalForce(ent, true);
	}

	/**+
	 * Will update the entity in the world if the chunk the entity
	 * is in is currently loaded or its forced to update. Args:
	 * entity, forceUpdate
	 */
	public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate) {
		int i = MathHelper.floor_double(entityIn.posX);
		int j = MathHelper.floor_double(entityIn.posZ);
		byte b0 = 32;
		if (!forceUpdate || this.isAreaLoaded(i - b0, 0, j - b0, i + b0, 0, j + b0, true)) {
			entityIn.lastTickPosX = entityIn.posX;
			entityIn.lastTickPosY = entityIn.posY;
			entityIn.lastTickPosZ = entityIn.posZ;
			entityIn.prevRotationYaw = entityIn.rotationYaw;
			entityIn.prevRotationPitch = entityIn.rotationPitch;
			if (forceUpdate && entityIn.addedToChunk) {
				++entityIn.ticksExisted;
				if (entityIn.ridingEntity != null) {
					entityIn.updateRidden();
				} else {
					entityIn.onUpdate();
				}
			}

			if (Double.isNaN(entityIn.posX) || Double.isInfinite(entityIn.posX)) {
				entityIn.posX = entityIn.lastTickPosX;
			}

			if (Double.isNaN(entityIn.posY) || Double.isInfinite(entityIn.posY)) {
				entityIn.posY = entityIn.lastTickPosY;
			}

			if (Double.isNaN(entityIn.posZ) || Double.isInfinite(entityIn.posZ)) {
				entityIn.posZ = entityIn.lastTickPosZ;
			}

			if (Double.isNaN((double) entityIn.rotationPitch) || Double.isInfinite((double) entityIn.rotationPitch)) {
				entityIn.rotationPitch = entityIn.prevRotationPitch;
			}

			if (Double.isNaN((double) entityIn.rotationYaw) || Double.isInfinite((double) entityIn.rotationYaw)) {
				entityIn.rotationYaw = entityIn.prevRotationYaw;
			}

			int k = MathHelper.floor_double(entityIn.posX / 16.0D);
			int l = MathHelper.floor_double(entityIn.posY / 16.0D);
			int i1 = MathHelper.floor_double(entityIn.posZ / 16.0D);
			if (!entityIn.addedToChunk || entityIn.chunkCoordX != k || entityIn.chunkCoordY != l
					|| entityIn.chunkCoordZ != i1) {
				if (entityIn.addedToChunk && this.isChunkLoaded(entityIn.chunkCoordX, entityIn.chunkCoordZ, true)) {
					this.getChunkFromChunkCoords(entityIn.chunkCoordX, entityIn.chunkCoordZ)
							.removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
				}

				if (this.isChunkLoaded(k, i1, true)) {
					entityIn.addedToChunk = true;
					this.getChunkFromChunkCoords(k, i1).addEntity(entityIn);
				} else {
					entityIn.addedToChunk = false;
				}
			}

			if (forceUpdate && entityIn.addedToChunk && entityIn.riddenByEntity != null) {
				if (!entityIn.riddenByEntity.isDead && entityIn.riddenByEntity.ridingEntity == entityIn) {
					this.updateEntity(entityIn.riddenByEntity);
				} else {
					entityIn.riddenByEntity.ridingEntity = null;
					entityIn.riddenByEntity = null;
				}
			}

		}
	}

	/**+
	 * Returns true if there are no solid, live entities in the
	 * specified AxisAlignedBB, excluding the given entity
	 */
	public boolean checkNoEntityCollision(AxisAlignedBB bb) {
		return this.checkNoEntityCollision(bb, (Entity) null);
	}

	/**+
	 * Returns true if there are no solid, live entities in the
	 * specified AxisAlignedBB, excluding the given entity
	 */
	public boolean checkNoEntityCollision(AxisAlignedBB bb, Entity entityIn) {
		List list = this.getEntitiesWithinAABBExcludingEntity((Entity) null, bb);

		for (int i = 0; i < list.size(); ++i) {
			Entity entity = (Entity) list.get(i);
			if (!entity.isDead && entity.preventEntitySpawning && entity != entityIn
					&& (entityIn == null || entityIn.ridingEntity != entity && entityIn.riddenByEntity != entity)) {
				return false;
			}
		}

		return true;
	}

	/**+
	 * Returns true if there are any blocks in the region
	 * constrained by an AxisAlignedBB
	 */
	public boolean checkBlockCollision(AxisAlignedBB bb) {
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ);
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int k1 = i; k1 <= j; ++k1) {
			for (int l1 = k; l1 <= l; ++l1) {
				for (int i2 = i1; i2 <= j1; ++i2) {
					Block block = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock();
					if (block.getMaterial() != Material.air) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**+
	 * Returns if any of the blocks within the aabb are liquids.
	 * Args: aabb
	 */
	public boolean isAnyLiquid(AxisAlignedBB bb) {
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ);
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int k1 = i; k1 <= j; ++k1) {
			for (int l1 = k; l1 <= l; ++l1) {
				for (int i2 = i1; i2 <= j1; ++i2) {
					Block block = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock();
					if (block.getMaterial().isLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isFlammableWithin(AxisAlignedBB bb) {
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX + 1.0D);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY + 1.0D);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
		if (this.isAreaLoaded(i, k, i1, j, l, j1, true)) {
			BlockPos blockpos$mutableblockpos = new BlockPos();

			for (int k1 = i; k1 < j; ++k1) {
				for (int l1 = k; l1 < l; ++l1) {
					for (int i2 = i1; i2 < j1; ++i2) {
						Block block = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock();
						if (block == Blocks.fire || block == Blocks.flowing_lava || block == Blocks.lava) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**+
	 * handles the acceleration of an object whilst in water. Not
	 * sure if it is used elsewhere.
	 */
	public boolean handleMaterialAcceleration(AxisAlignedBB bb, Material materialIn, Entity entityIn) {
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX + 1.0D);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY + 1.0D);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
		if (!this.isAreaLoaded(i, k, i1, j, l, j1, true)) {
			return false;
		} else {
			boolean flag = false;
			Vec3 vec3 = new Vec3(0.0D, 0.0D, 0.0D);
			BlockPos blockpos$mutableblockpos = new BlockPos();

			for (int k1 = i; k1 < j; ++k1) {
				for (int l1 = k; l1 < l; ++l1) {
					for (int i2 = i1; i2 < j1; ++i2) {
						blockpos$mutableblockpos.func_181079_c(k1, l1, i2);
						IBlockState iblockstate = this.getBlockState(blockpos$mutableblockpos);
						Block block = iblockstate.getBlock();
						if (block.getMaterial() == materialIn) {
							double d0 = (double) ((float) (l1 + 1) - BlockLiquid.getLiquidHeightPercent(
									((Integer) iblockstate.getValue(BlockLiquid.LEVEL)).intValue()));
							if ((double) l >= d0) {
								flag = true;
								vec3 = block.modifyAcceleration(this, blockpos$mutableblockpos, entityIn, vec3);
							}
						}
					}
				}
			}

			if (vec3.lengthVector() > 0.0D && entityIn.isPushedByWater()) {
				vec3 = vec3.normalize();
				double d1 = 0.014D;
				entityIn.motionX += vec3.xCoord * d1;
				entityIn.motionY += vec3.yCoord * d1;
				entityIn.motionZ += vec3.zCoord * d1;
			}

			return flag;
		}
	}

	/**+
	 * Returns true if the given bounding box contains the given
	 * material
	 */
	public boolean isMaterialInBB(AxisAlignedBB bb, Material materialIn) {
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX + 1.0D);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY + 1.0D);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					if (this.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock()
							.getMaterial() == materialIn) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**+
	 * checks if the given AABB is in the material given. Used while
	 * swimming.
	 */
	public boolean isAABBInMaterial(AxisAlignedBB bb, Material materialIn) {
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX + 1.0D);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY + 1.0D);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					IBlockState iblockstate = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2));
					Block block = iblockstate.getBlock();
					if (block.getMaterial() == materialIn) {
						int j2 = ((Integer) iblockstate.getValue(BlockLiquid.LEVEL)).intValue();
						double d0 = (double) (l1 + 1);
						if (j2 < 8) {
							d0 = (double) (l1 + 1) - (double) j2 / 8.0D;
						}

						if (d0 >= bb.minY) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**+
	 * Creates an explosion. Args: entity, x, y, z, strength
	 */
	public Explosion createExplosion(Entity entityIn, double x, double y, double z, float strength, boolean isSmoking) {
		return this.newExplosion(entityIn, x, y, z, strength, false, isSmoking);
	}

	/**+
	 * returns a new explosion. Does initiation (at time of writing
	 * Explosion is not finished)
	 */
	public Explosion newExplosion(Entity entityIn, double x, double y, double z, float strength, boolean isFlaming,
			boolean isSmoking) {
		Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
		explosion.doExplosionA();
		explosion.doExplosionB(true);
		return explosion;
	}

	/**+
	 * Gets the percentage of real blocks within within a bounding
	 * box, along a specified vector.
	 */
	public float getBlockDensity(Vec3 vec, AxisAlignedBB bb) {
		double d0 = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
		double d1 = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
		double d2 = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
		double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
		double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
		if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D) {
			int i = 0;
			int j = 0;

			for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
				for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
					for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
						double d5 = bb.minX + (bb.maxX - bb.minX) * (double) f;
						double d6 = bb.minY + (bb.maxY - bb.minY) * (double) f1;
						double d7 = bb.minZ + (bb.maxZ - bb.minZ) * (double) f2;
						if (this.rayTraceBlocks(new Vec3(d5 + d3, d6, d7 + d4), vec) == null) {
							++i;
						}

						++j;
					}
				}
			}

			return (float) i / (float) j;
		} else {
			return 0.0F;
		}
	}

	/**+
	 * Attempts to extinguish a fire
	 */
	public boolean extinguishFire(EntityPlayer player, BlockPos pos, EnumFacing side) {
		pos = pos.offset(side);
		if (this.getBlockState(pos).getBlock() == Blocks.fire) {
			this.playAuxSFXAtEntity(player, 1004, pos, 0);
			this.setBlockToAir(pos);
			return true;
		} else {
			return false;
		}
	}

	/**+
	 * This string is 'All: (number of loaded entities)' Viewable by
	 * press ing F3
	 */
	public String getDebugLoadedEntities() {
		return "All: " + this.loadedEntityList.size();
	}

	/**+
	 * Returns the name of the current chunk provider, by calling
	 * chunkprovider.makeString()
	 */
	public String getProviderName() {
		return this.chunkProvider.makeString();
	}

	public TileEntity getTileEntity(BlockPos pos) {
		if (!this.isValid(pos)) {
			return null;
		} else {
			TileEntity tileentity = null;
			if (this.processingLoadedTiles) {
				for (int i = 0; i < this.addedTileEntityList.size(); ++i) {
					TileEntity tileentity1 = (TileEntity) this.addedTileEntityList.get(i);
					if (!tileentity1.isInvalid() && tileentity1.getPos().equals(pos)) {
						tileentity = tileentity1;
						break;
					}
				}
			}

			if (tileentity == null) {
				tileentity = this.getChunkFromBlockCoords(pos).getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
			}

			if (tileentity == null) {
				for (int j = 0; j < this.addedTileEntityList.size(); ++j) {
					TileEntity tileentity2 = (TileEntity) this.addedTileEntityList.get(j);
					if (!tileentity2.isInvalid() && tileentity2.getPos().equals(pos)) {
						tileentity = tileentity2;
						break;
					}
				}
			}

			return tileentity;
		}
	}

	public void setTileEntity(BlockPos pos, TileEntity tileEntityIn) {
		if (tileEntityIn != null && !tileEntityIn.isInvalid()) {
			if (this.processingLoadedTiles) {
				tileEntityIn.setPos(new BlockPos(pos));
				Iterator iterator = this.addedTileEntityList.iterator();

				while (iterator.hasNext()) {
					TileEntity tileentity = (TileEntity) iterator.next();
					if (tileentity.getPos().equals(pos)) {
						tileentity.invalidate();
						iterator.remove();
					}
				}

				this.addedTileEntityList.add(tileEntityIn);
			} else {
				this.addTileEntity(tileEntityIn);
				this.getChunkFromBlockCoords(pos).addTileEntity(pos, tileEntityIn);
			}
		}

	}

	public void removeTileEntity(BlockPos pos) {
		TileEntity tileentity = this.getTileEntity(pos);
		if (tileentity != null && this.processingLoadedTiles) {
			tileentity.invalidate();
			this.addedTileEntityList.remove(tileentity);
		} else {
			if (tileentity != null) {
				this.addedTileEntityList.remove(tileentity);
				this.loadedTileEntityList.remove(tileentity);
				this.tickableTileEntities.remove(tileentity);
			}

			this.getChunkFromBlockCoords(pos).removeTileEntity(pos);
		}

	}

	/**+
	 * Adds the specified TileEntity to the pending removal list.
	 */
	public void markTileEntityForRemoval(TileEntity tileEntityIn) {
		this.tileEntitiesToBeRemoved.add(tileEntityIn);
	}

	public boolean isBlockFullCube(BlockPos pos) {
		IBlockState iblockstate = this.getBlockState(pos);
		AxisAlignedBB axisalignedbb = iblockstate.getBlock().getCollisionBoundingBox(this, pos, iblockstate);
		return axisalignedbb != null && axisalignedbb.getAverageEdgeLength() >= 1.0D;
	}

	public static boolean doesBlockHaveSolidTopSurface(IBlockAccess blockAccess, BlockPos pos) {
		IBlockState iblockstate = blockAccess.getBlockState(pos);
		Block block = iblockstate.getBlock();
		return block.getMaterial().isOpaque() && block.isFullCube() ? true
				: (block instanceof BlockStairs ? iblockstate.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP
						: (block instanceof BlockSlab
								? iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP
								: (block instanceof BlockHopper ? true
										: (block instanceof BlockSnow
												? ((Integer) iblockstate.getValue(BlockSnow.LAYERS)).intValue() == 7
												: false))));
	}

	/**+
	 * Checks if a block's material is opaque, and that it takes up
	 * a full cube
	 */
	public boolean isBlockNormalCube(BlockPos pos, boolean _default) {
		if (!this.isValid(pos)) {
			return _default;
		} else {
			Chunk chunk = this.chunkProvider.provideChunk(pos);
			if (chunk.isEmpty()) {
				return _default;
			} else {
				Block block = this.getBlockState(pos).getBlock();
				return block.getMaterial().isOpaque() && block.isFullCube();
			}
		}
	}

	/**+
	 * Called on construction of the World class to setup the
	 * initial skylight values
	 */
	public void calculateInitialSkylight() {
		int i = this.calculateSkylightSubtracted(1.0F);
		if (i != this.skylightSubtracted) {
			this.skylightSubtracted = i;
		}

	}

	/**+
	 * first boolean for hostile mobs and second for peaceful mobs
	 */
	public void setAllowedSpawnTypes(boolean hostile, boolean peaceful) {
		this.spawnHostileMobs = hostile;
		this.spawnPeacefulMobs = peaceful;
	}

	/**+
	 * Runs a single tick for the world
	 */
	public void tick() {
		this.updateWeather();
	}

	/**+
	 * Called from World constructor to set rainingStrength and
	 * thunderingStrength
	 */
	protected void calculateInitialWeather() {
		if (this.worldInfo.isRaining()) {
			this.rainingStrength = 1.0F;
			if (this.worldInfo.isThundering()) {
				this.thunderingStrength = 1.0F;
			}
		}

	}

	/**+
	 * Updates all weather states.
	 */
	protected void updateWeather() {
		if (!this.provider.getHasNoSky() && this.getGameRules().getBoolean("doWeatherCycle")) {
			if (!this.isRemote) {
				int i = this.worldInfo.getCleanWeatherTime();
				if (i > 0) {
					--i;
					this.worldInfo.setCleanWeatherTime(i);
					this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
					this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
				}

				int j = this.worldInfo.getThunderTime();
				if (j <= 0) {
					if (this.worldInfo.isThundering()) {
						this.worldInfo.setThunderTime((this.rand.nextInt(12000) / 2) + 3600);
					} else {
						this.worldInfo.setThunderTime((this.rand.nextInt(168000) + 12000) * 2);
					}
				} else {
					--j;
					this.worldInfo.setThunderTime(j);
					if (j <= 0) {
						this.worldInfo.setThundering(!this.worldInfo.isThundering());
					}
				}

				this.prevThunderingStrength = this.thunderingStrength;
				if (this.worldInfo.isThundering()) {
					this.thunderingStrength = (float) ((double) this.thunderingStrength + 0.01D);
				} else {
					this.thunderingStrength = (float) ((double) this.thunderingStrength - 0.01D);
				}

				this.thunderingStrength = MathHelper.clamp_float(this.thunderingStrength, 0.0F, 1.0F);
				int k = this.worldInfo.getRainTime();
				if (k <= 0) {
					if (this.worldInfo.isRaining()) {
						this.worldInfo.setRainTime((this.rand.nextInt(12000) + 12000) / 2);
					} else {
						this.worldInfo.setRainTime((this.rand.nextInt(168000) + 12000) * 2);
					}
				} else {
					--k;
					this.worldInfo.setRainTime(k);
					if (k <= 0) {
						this.worldInfo.setRaining(!this.worldInfo.isRaining());
					}
				}

				this.prevRainingStrength = this.rainingStrength;
				if (this.worldInfo.isRaining()) {
					this.rainingStrength = (float) ((double) this.rainingStrength + 0.01D);
				} else {
					this.rainingStrength = (float) ((double) this.rainingStrength - 0.01D);
				}

				this.rainingStrength = MathHelper.clamp_float(this.rainingStrength, 0.0F, 1.0F);
			}
		}
	}

	protected void setActivePlayerChunksAndCheckLight() {
		this.activeChunkSet.clear();

		int l = this.getRenderDistanceChunks() - 1;
		if (l > 7)
			l = 7;
		else if (l < 1)
			l = 1;

		for (int i = 0; i < this.playerEntities.size(); ++i) {
			EntityPlayer entityplayer = (EntityPlayer) this.playerEntities.get(i);
			int j = MathHelper.floor_double(entityplayer.posX / 16.0D);
			int k = MathHelper.floor_double(entityplayer.posZ / 16.0D);

			for (int i1 = -l; i1 <= l; ++i1) {
				for (int j1 = -l; j1 <= l; ++j1) {
					this.activeChunkSet.add(ChunkCoordIntPair.chunkXZ2Int(i1 + j, j1 + k));
				}
			}
		}

		if (this.ambientTickCountdown > 0) {
			--this.ambientTickCountdown;
		}

		if (!this.playerEntities.isEmpty()) {
			int k1 = this.rand.nextInt(this.playerEntities.size());
			EntityPlayer entityplayer1 = (EntityPlayer) this.playerEntities.get(k1);
			int l1 = MathHelper.floor_double(entityplayer1.posX) + this.rand.nextInt(11) - 5;
			int i2 = MathHelper.floor_double(entityplayer1.posY) + this.rand.nextInt(11) - 5;
			int j2 = MathHelper.floor_double(entityplayer1.posZ) + this.rand.nextInt(11) - 5;
			this.checkLight(new BlockPos(l1, i2, j2));
		}

	}

	protected abstract int getRenderDistanceChunks();

	protected void playMoodSoundAndCheckLight(int chunkIn, int parInt2, Chunk parChunk) {
		if (this.ambientTickCountdown == 0 && !this.isRemote) {
			this.updateLCG = this.updateLCG * 3 + 1013904223;
			int i = this.updateLCG >> 2;
			int j = i & 15;
			int k = i >> 8 & 15;
			int l = i >> 16 & 255;
			BlockPos blockpos = new BlockPos(j, l, k);
			Block block = parChunk.getBlock(blockpos);
			j = j + chunkIn;
			k = k + parInt2;
			if (block.getMaterial() == Material.air && this.getLight(blockpos) <= this.rand.nextInt(8)
					&& this.getLightFor(EnumSkyBlock.SKY, blockpos) <= 0) {
				EntityPlayer entityplayer = this.getClosestPlayer((double) j + 0.5D, (double) l + 0.5D,
						(double) k + 0.5D, 8.0D);
				if (entityplayer != null
						&& entityplayer.getDistanceSq((double) j + 0.5D, (double) l + 0.5D, (double) k + 0.5D) > 4.0D) {
					this.playSoundEffect((double) j + 0.5D, (double) l + 0.5D, (double) k + 0.5D, "ambient.cave.cave",
							0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
					this.ambientTickCountdown = this.rand.nextInt(12000) + 6000;
				}
			}
		}

		parChunk.enqueueRelightChecks();
	}

	protected void updateBlocks() {
		this.setActivePlayerChunksAndCheckLight();
	}

	public void forceBlockUpdateTick(Block blockType, BlockPos pos, EaglercraftRandom random) {
		this.scheduledUpdatesAreImmediate = true;
		blockType.updateTick(this, pos, this.getBlockState(pos), random);
		this.scheduledUpdatesAreImmediate = false;
	}

	public boolean canBlockFreezeWater(BlockPos pos) {
		return this.canBlockFreeze(pos, false);
	}

	public boolean canBlockFreezeNoWater(BlockPos pos) {
		return this.canBlockFreeze(pos, true);
	}

	/**+
	 * Checks to see if a given block is both water and cold enough
	 * to freeze.
	 */
	public boolean canBlockFreeze(BlockPos pos, boolean noWaterAdj) {
		BiomeGenBase biomegenbase = this.getBiomeGenForCoords(pos);
		float f = biomegenbase.getFloatTemperature(pos);
		if (f > 0.15F) {
			return false;
		} else {
			if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
				IBlockState iblockstate = this.getBlockState(pos);
				Block block = iblockstate.getBlock();
				if ((block == Blocks.water || block == Blocks.flowing_water)
						&& ((Integer) iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
					if (!noWaterAdj) {
						return true;
					}

					BlockPos tmp = new BlockPos();
					boolean flag = this.isWater(pos.west(tmp)) && this.isWater(pos.east(tmp))
							&& this.isWater(pos.north(tmp)) && this.isWater(pos.south(tmp));
					if (!flag) {
						return true;
					}
				}
			}

			return false;
		}
	}

	private boolean isWater(BlockPos pos) {
		return this.getBlockState(pos).getBlock().getMaterial() == Material.water;
	}

	/**+
	 * Checks to see if a given block can accumulate snow from it
	 * snowing
	 */
	public boolean canSnowAt(BlockPos pos, boolean checkLight) {
		BiomeGenBase biomegenbase = this.getBiomeGenForCoords(pos);
		float f = biomegenbase.getFloatTemperature(pos);
		if (f > 0.15F) {
			return false;
		} else if (!checkLight) {
			return true;
		} else {
			if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
				Block block = this.getBlockState(pos).getBlock();
				if (block.getMaterial() == Material.air && Blocks.snow_layer.canPlaceBlockAt(this, pos)) {
					return true;
				}
			}

			return false;
		}
	}

	public boolean checkLight(BlockPos pos) {
		boolean flag = false;
		if (!this.provider.getHasNoSky()) {
			flag |= this.checkLightFor(EnumSkyBlock.SKY, pos);
		}

		flag = flag | this.checkLightFor(EnumSkyBlock.BLOCK, pos);
		return flag;
	}

	/**+
	 * gets the light level at the supplied position
	 */
	private int getRawLight(BlockPos pos, EnumSkyBlock lightType) {
		if (lightType == EnumSkyBlock.SKY && this.canSeeSky(pos)) {
			return 15;
		} else {
			Block block = this.getBlockState(pos).getBlock();
			int i = lightType == EnumSkyBlock.SKY ? 0 : block.getLightValue();
			int j = block.getLightOpacity();
			if (j >= 15 && block.getLightValue() > 0) {
				j = 1;
			}

			if (j < 1) {
				j = 1;
			}

			if (j >= 15) {
				return 0;
			} else if (i >= 14) {
				return i;
			} else {
				EnumFacing[] facings = EnumFacing._VALUES;
				for (int m = 0; m < facings.length; ++m) {
					EnumFacing enumfacing = facings[m];
					BlockPos blockpos = pos.offset(enumfacing);
					int k = this.getLightFor(lightType, blockpos) - j;
					if (k > i) {
						i = k;
					}

					if (i >= 14) {
						return i;
					}
				}

				return i;
			}
		}
	}

	public boolean checkLightFor(EnumSkyBlock lightType, BlockPos pos) {
		alfheim$lightingEngine.scheduleLightUpdate(lightType, pos);
		return true;
	}

	/**+
	 * Runs through the list of updates to run and ticks them
	 */
	public boolean tickUpdates(boolean parFlag) {
		return false;
	}

	public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunkIn, boolean parFlag) {
		return null;
	}

	public List<NextTickListEntry> func_175712_a(StructureBoundingBox structureBB, boolean parFlag) {
		return null;
	}

	/**+
	 * Will get all entities within the specified AABB excluding the
	 * one passed into it. Args: entityToExclude, aabb
	 */
	public List<Entity> getEntitiesWithinAABBExcludingEntity(Entity entityIn, AxisAlignedBB bb) {
		return this.getEntitiesInAABBexcluding(entityIn, bb, EntitySelectors.NOT_SPECTATING);
	}

	/**+
	 * Gets all entities within the specified AABB excluding the one
	 * passed into it. Args: entityToExclude, aabb, predicate
	 */
	public List<Entity> getEntitiesInAABBexcluding(Entity entityIn, AxisAlignedBB boundingBox,
			Predicate<? super Entity> predicate) {
		ArrayList arraylist = Lists.newArrayList();
		int i = MathHelper.floor_double((boundingBox.minX - 2.0D) / 16.0D);
		int j = MathHelper.floor_double((boundingBox.maxX + 2.0D) / 16.0D);
		int k = MathHelper.floor_double((boundingBox.minZ - 2.0D) / 16.0D);
		int l = MathHelper.floor_double((boundingBox.maxZ + 2.0D) / 16.0D);

		for (int i1 = i; i1 <= j; ++i1) {
			for (int j1 = k; j1 <= l; ++j1) {
				Chunk chunk = this.getChunkFromChunkCoordsIfLoaded(i1, j1);
				if (chunk != null) {
					chunk.getEntitiesWithinAABBForEntity(entityIn, boundingBox, arraylist, predicate);
				}
			}
		}

		return arraylist;
	}

	public <T extends Entity> List<T> getEntities(Class<? extends T> entityType, Predicate<? super T> filter) {
		ArrayList arraylist = Lists.newArrayList();

		for (int i = 0, l = this.loadedEntityList.size(); i < l; ++i) {
			Entity entity = this.loadedEntityList.get(i);
			if (entityType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {
				arraylist.add(entity);
			}
		}

		return arraylist;
	}

	public <T extends Entity> List<T> getPlayers(Class<? extends T> playerType, Predicate<? super T> filter) {
		ArrayList arraylist = Lists.newArrayList();

		for (int i = 0, l = this.playerEntities.size(); i < l; ++i) {
			Entity entity = this.playerEntities.get(i);
			if (playerType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {
				arraylist.add(entity);
			}
		}

		return arraylist;
	}

	public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> classEntity, AxisAlignedBB bb) {
		return this.<T>getEntitiesWithinAABB(classEntity, bb, EntitySelectors.NOT_SPECTATING);
	}

	public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB aabb,
			Predicate<? super T> filter) {
		int i = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
		int j = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
		int k = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
		int l = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);
		ArrayList arraylist = Lists.newArrayList();

		for (int i1 = i; i1 <= j; ++i1) {
			for (int j1 = k; j1 <= l; ++j1) {
				if (this.isChunkLoaded(i1, j1, true)) {
					this.getChunkFromChunkCoords(i1, j1).getEntitiesOfTypeWithinAAAB(clazz, aabb, arraylist, filter);
				}
			}
		}

		return arraylist;
	}

	public <T extends Entity> T findNearestEntityWithinAABB(Class<? extends T> entityType, AxisAlignedBB aabb,
			T closestTo) {
		List list = this.getEntitiesWithinAABB(entityType, aabb);
		Entity entity = null;
		double d0 = Double.MAX_VALUE;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = (Entity) list.get(i);
			if (entity1 != closestTo && EntitySelectors.NOT_SPECTATING.apply(entity1)) {
				double d1 = closestTo.getDistanceSqToEntity(entity1);
				if (d1 <= d0) {
					entity = entity1;
					d0 = d1;
				}
			}
		}

		return (T) entity;
	}

	/**+
	 * Returns the Entity with the given ID, or null if it doesn't
	 * exist in this World.
	 */
	public Entity getEntityByID(int id) {
		return this.entitiesById.get(id);
	}

	/**+
	 * Accessor for world Loaded Entity List
	 */
	public List<Entity> getLoadedEntityList() {
		return this.loadedEntityList;
	}

	public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity) {
		if (this.isBlockLoaded(pos)) {
			this.getChunkFromBlockCoords(pos).setChunkModified();
		}

	}

	/**+
	 * Counts how many entities of an entity class exist in the
	 * world. Args: entityClass
	 */
	public int countEntities(Class<?> entityType) {
		int i = 0;

		for (int j = 0, l = this.loadedEntityList.size(); j < l; ++j) {
			Entity entity = this.loadedEntityList.get(j);
			if ((!(entity instanceof EntityLiving) || !((EntityLiving) entity).isNoDespawnRequired())
					&& entityType.isAssignableFrom(entity.getClass())) {
				++i;
			}
		}

		return i;
	}

	public void loadEntities(Collection<Entity> entityCollection) {
		this.loadedEntityList.addAll(entityCollection);

		for (Entity entity : entityCollection) {
			this.onEntityAdded(entity);
		}

	}

	public void unloadEntities(Collection<Entity> entityCollection) {
		this.unloadedEntityList.addAll(entityCollection);
	}

	public boolean canBlockBePlaced(Block blockIn, BlockPos pos, boolean side, EnumFacing entityIn, Entity itemStackIn,
			ItemStack parItemStack) {
		Block block = this.getBlockState(pos).getBlock();
		AxisAlignedBB axisalignedbb = side ? null
				: blockIn.getCollisionBoundingBox(this, pos, blockIn.getDefaultState());
		return axisalignedbb != null && !this.checkNoEntityCollision(axisalignedbb, itemStackIn) ? false
				: (block.getMaterial() == Material.circuits && blockIn == Blocks.anvil ? true
						: block.getMaterial().isReplaceable() && blockIn.canReplace(this, pos, entityIn, parItemStack));
	}

	public int func_181545_F() {
		return this.field_181546_a;
	}

	public void func_181544_b(int parInt1) {
		this.field_181546_a = parInt1;
	}

	/**+
	 * Returns the single highest strong power out of all directions
	 * using getStrongPower(BlockPos, EnumFacing)
	 */
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		IBlockState iblockstate = this.getBlockState(pos);
		return iblockstate.getBlock().getStrongPower(this, pos, iblockstate, direction);
	}

	public WorldType getWorldType() {
		return this.worldInfo.getTerrainType();
	}

	/**+
	 * Returns the single highest strong power out of all directions
	 * using getStrongPower(BlockPos, EnumFacing)
	 */
	public int getStrongPower(BlockPos pos) {
		int i = 0;
		BlockPos tmp = new BlockPos();
		i = Math.max(i, this.getStrongPower(pos.down(tmp), EnumFacing.DOWN));
		if (i >= 15) {
			return i;
		} else {
			i = Math.max(i, this.getStrongPower(pos.up(tmp), EnumFacing.UP));
			if (i >= 15) {
				return i;
			} else {
				i = Math.max(i, this.getStrongPower(pos.north(tmp), EnumFacing.NORTH));
				if (i >= 15) {
					return i;
				} else {
					i = Math.max(i, this.getStrongPower(pos.south(tmp), EnumFacing.SOUTH));
					if (i >= 15) {
						return i;
					} else {
						i = Math.max(i, this.getStrongPower(pos.west(tmp), EnumFacing.WEST));
						if (i >= 15) {
							return i;
						} else {
							i = Math.max(i, this.getStrongPower(pos.east(tmp), EnumFacing.EAST));
							return i >= 15 ? i : i;
						}
					}
				}
			}
		}
	}

	public boolean isSidePowered(BlockPos pos, EnumFacing side) {
		return this.getRedstonePower(pos, side) > 0;
	}

	public int getRedstonePower(BlockPos pos, EnumFacing facing) {
		IBlockState iblockstate = this.getBlockState(pos);
		Block block = iblockstate.getBlock();
		return block.isNormalCube() ? this.getStrongPower(pos) : block.getWeakPower(this, pos, iblockstate, facing);
	}

	public boolean isBlockPowered(BlockPos pos) {
		BlockPos tmp = new BlockPos(0, 0, 0);
		return this.getRedstonePower(pos.down(tmp), EnumFacing.DOWN) > 0 ? true
				: (this.getRedstonePower(pos.up(tmp), EnumFacing.UP) > 0 ? true
						: (this.getRedstonePower(pos.north(tmp), EnumFacing.NORTH) > 0 ? true
								: (this.getRedstonePower(pos.south(tmp), EnumFacing.SOUTH) > 0 ? true
										: (this.getRedstonePower(pos.west(tmp), EnumFacing.WEST) > 0 ? true
												: this.getRedstonePower(pos.east(tmp), EnumFacing.EAST) > 0))));
	}

	/**+
	 * Checks if the specified block or its neighbors are powered by
	 * a neighboring block. Used by blocks like TNT and Doors.
	 */
	public int isBlockIndirectlyGettingPowered(BlockPos pos) {
		int i = 0;

		EnumFacing[] facings = EnumFacing._VALUES;
		BlockPos tmp = new BlockPos(0, 0, 0);
		for (int k = 0; k < facings.length; ++k) {
			EnumFacing enumfacing = facings[k];
			int j = this.getRedstonePower(pos.offsetEvenFaster(enumfacing, tmp), enumfacing);
			if (j >= 15) {
				return 15;
			}

			if (j > i) {
				i = j;
			}
		}

		return i;
	}

	/**+
	 * Gets the closest player to the entity within the specified
	 * distance (if distance is less than 0 then ignored). Args:
	 * entity, dist
	 */
	public EntityPlayer getClosestPlayerToEntity(Entity entityIn, double distance) {
		return this.getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance);
	}

	/**+
	 * Gets the closest player to the point within the specified
	 * distance (distance can be set to less than 0 to not limit the
	 * distance). Args: x, y, z, dist
	 */
	public EntityPlayer getClosestPlayer(double x, double y, double z, double distance) {
		double d0 = -1.0D;
		EntityPlayer entityplayer = null;

		for (int i = 0; i < this.playerEntities.size(); ++i) {
			EntityPlayer entityplayer1 = (EntityPlayer) this.playerEntities.get(i);
			if (EntitySelectors.NOT_SPECTATING.apply(entityplayer1)) {
				double d1 = entityplayer1.getDistanceSq(x, y, z);
				if ((distance < 0.0D || d1 < distance * distance) && (d0 == -1.0D || d1 < d0)) {
					d0 = d1;
					entityplayer = entityplayer1;
				}
			}
		}

		return entityplayer;
	}

	public boolean isAnyPlayerWithinRangeAt(double x, double y, double z, double range) {
		for (int i = 0; i < this.playerEntities.size(); ++i) {
			EntityPlayer entityplayer = (EntityPlayer) this.playerEntities.get(i);
			if (EntitySelectors.NOT_SPECTATING.apply(entityplayer)) {
				double d0 = entityplayer.getDistanceSq(x, y, z);
				if (range < 0.0D || d0 < range * range) {
					return true;
				}
			}
		}

		return false;
	}

	/**+
	 * Find a player by name in this world.
	 */
	public EntityPlayer getPlayerEntityByName(String name) {
		for (int i = 0; i < this.playerEntities.size(); ++i) {
			EntityPlayer entityplayer = (EntityPlayer) this.playerEntities.get(i);
			if (name.equals(entityplayer.getName())) {
				return entityplayer;
			}
		}

		return null;
	}

	public EntityPlayer getPlayerEntityByUUID(EaglercraftUUID uuid) {
		for (int i = 0; i < this.playerEntities.size(); ++i) {
			EntityPlayer entityplayer = (EntityPlayer) this.playerEntities.get(i);
			if (uuid.equals(entityplayer.getUniqueID())) {
				return entityplayer;
			}
		}

		return null;
	}

	/**+
	 * If on MP, sends a quitting packet.
	 */
	public void sendQuittingDisconnectingPacket() {
	}

	public void setTotalWorldTime(long worldTime) {
		this.worldInfo.setWorldTotalTime(worldTime);
	}

	/**+
	 * gets the random world seed
	 */
	public long getSeed() {
		return this.worldInfo.getSeed();
	}

	public long getTotalWorldTime() {
		return this.worldInfo.getWorldTotalTime();
	}

	public long getWorldTime() {
		return this.worldInfo.getWorldTime();
	}

	/**+
	 * Sets the world time.
	 */
	public void setWorldTime(long time) {
		this.worldInfo.setWorldTime(time);
	}

	/**+
	 * Gets the spawn point in the world
	 */
	public BlockPos getSpawnPoint() {
		BlockPos blockpos = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(),
				this.worldInfo.getSpawnZ());
		if (!this.getWorldBorder().contains(blockpos)) {
			blockpos = this.getHeight(
					new BlockPos(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
		}

		return blockpos;
	}

	public void setSpawnPoint(BlockPos pos) {
		this.worldInfo.setSpawn(pos);
	}

	/**+
	 * spwans an entity and loads surrounding chunks
	 */
	public void joinEntityInSurroundings(Entity entityIn) {
		int i = MathHelper.floor_double(entityIn.posX / 16.0D);
		int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
		byte b0 = 2;

		for (int k = i - b0; k <= i + b0; ++k) {
			for (int l = j - b0; l <= j + b0; ++l) {
				this.getChunkFromChunkCoords(k, l);
			}
		}

		if (!this.loadedEntityList.contains(entityIn)) {
			this.loadedEntityList.add(entityIn);
		}

	}

	public boolean isBlockModifiable(EntityPlayer player, BlockPos pos) {
		return true;
	}

	/**+
	 * sends a Packet 38 (Entity Status) to all tracked players of
	 * that entity
	 */
	public void setEntityState(Entity entityIn, byte state) {
	}

	/**+
	 * gets the world's chunk provider
	 */
	public IChunkProvider getChunkProvider() {
		return this.chunkProvider;
	}

	public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam) {
		blockIn.onBlockEventReceived(this, pos, this.getBlockState(pos), eventID, eventParam);
	}

	/**+
	 * Returns this world's current save handler
	 */
	public ISaveHandler getSaveHandler() {
		return this.saveHandler;
	}

	/**+
	 * Returns the world's WorldInfo object
	 */
	public WorldInfo getWorldInfo() {
		return this.worldInfo;
	}

	/**+
	 * Gets the GameRules instance.
	 */
	public GameRules getGameRules() {
		return this.worldInfo.getGameRulesInstance();
	}

	/**+
	 * Updates the flag that indicates whether or not all players in
	 * the world are sleeping.
	 */
	public void updateAllPlayersSleepingFlag() {
	}

	public float getThunderStrength(float delta) {
		return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * delta)
				* this.getRainStrength(delta);
	}

	/**+
	 * Sets the strength of the thunder.
	 */
	public void setThunderStrength(float strength) {
		this.prevThunderingStrength = strength;
		this.thunderingStrength = strength;
	}

	/**+
	 * Returns rain strength.
	 */
	public float getRainStrength(float delta) {
		return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * delta;
	}

	/**+
	 * Sets the strength of the rain.
	 */
	public void setRainStrength(float strength) {
		this.prevRainingStrength = strength;
		this.rainingStrength = strength;
	}

	/**+
	 * Returns true if the current thunder strength (weighted with
	 * the rain strength) is greater than 0.9
	 */
	public boolean isThundering() {
		return (double) this.getThunderStrength(1.0F) > 0.9D;
	}

	/**+
	 * Returns true if the current rain strength is greater than 0.2
	 */
	public boolean isRaining() {
		return (double) this.getRainStrength(1.0F) > 0.2D;
	}

	public boolean canLightningStrike(BlockPos strikePosition) {
		if (!this.isRaining()) {
			return false;
		} else if (!this.canSeeSky(strikePosition)) {
			return false;
		} else if (this.getPrecipitationHeight(strikePosition).getY() > strikePosition.getY()) {
			return false;
		} else {
			BiomeGenBase biomegenbase = this.getBiomeGenForCoords(strikePosition);
			return biomegenbase.getEnableSnow() ? false
					: (this.canSnowAt(strikePosition, false) ? false : biomegenbase.canSpawnLightningBolt());
		}
	}

	public boolean isBlockinHighHumidity(BlockPos pos) {
		BiomeGenBase biomegenbase = this.getBiomeGenForCoords(pos);
		return biomegenbase.isHighHumidity();
	}

	public MapStorage getMapStorage() {
		return this.mapStorage;
	}

	/**+
	 * Assigns the given String id to the given MapDataBase using
	 * the MapStorage, removing any existing ones of the same id.
	 */
	public void setItemData(String dataID, WorldSavedData worldSavedDataIn) {
		this.mapStorage.setData(dataID, worldSavedDataIn);
	}

	/**+
	 * Loads an existing MapDataBase corresponding to the given
	 * String id from disk using the MapStorage, instantiating the
	 * given Class, or returns null if none such file exists. args:
	 * Class to instantiate, String dataid
	 */
	public WorldSavedData loadItemData(Class<? extends WorldSavedData> clazz, String dataID) {
		return this.mapStorage.loadData(clazz, dataID);
	}

	/**+
	 * Returns an unique new data id from the MapStorage for the
	 * given prefix and saves the idCounts map to the 'idcounts'
	 * file.
	 */
	public int getUniqueDataId(String key) {
		return this.mapStorage.getUniqueDataId(key);
	}

	public void playBroadcastSound(int pos, BlockPos parBlockPos, int parInt2) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			((IWorldAccess) this.worldAccesses.get(i)).broadcastSound(pos, parBlockPos, parInt2);
		}

	}

	public void playAuxSFX(int pos, BlockPos parBlockPos, int parInt2) {
		this.playAuxSFXAtEntity((EntityPlayer) null, pos, parBlockPos, parInt2);
	}

	public void playAuxSFXAtEntity(EntityPlayer player, int sfxType, BlockPos pos, int parInt2) {
		try {
			for (int i = 0; i < this.worldAccesses.size(); ++i) {
				((IWorldAccess) this.worldAccesses.get(i)).playAuxSFX(player, sfxType, pos, parInt2);
			}

		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Playing level event");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Level event being played");
			crashreportcategory.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(pos));
			crashreportcategory.addCrashSection("Event source", player);
			crashreportcategory.addCrashSection("Event type", Integer.valueOf(sfxType));
			crashreportcategory.addCrashSection("Event data", Integer.valueOf(parInt2));
			throw new ReportedException(crashreport);
		}
	}

	/**+
	 * Returns maximum world height.
	 */
	public int getHeight() {
		return 256;
	}

	/**+
	 * Returns current world height.
	 */
	public int getActualHeight() {
		return this.provider.getHasNoSky() ? 128 : 256;
	}

	/**+
	 * puts the World Random seed to a specific state dependant on
	 * the inputs
	 */
	public EaglercraftRandom setRandomSeed(int parInt1, int parInt2, int parInt3) {
		long i = (long) parInt1 * 341873128712L + (long) parInt2 * 132897987541L + this.getWorldInfo().getSeed()
				+ (long) parInt3;
		this.rand.setSeed(i);
		return this.rand;
	}

	public BlockPos getStrongholdPos(String name, BlockPos pos) {
		return this.getChunkProvider().getStrongholdGen(this, name, pos);
	}

	/**+
	 * set by !chunk.getAreLevelsEmpty
	 */
	public boolean extendedLevelsInChunkCache() {
		return false;
	}

	/**+
	 * Returns horizon height for use in rendering the sky.
	 */
	public double getHorizon() {
		return this.worldInfo.getTerrainType() == WorldType.FLAT ? 0.0D : 63.0D;
	}

	/**+
	 * Adds some basic stats of the world to the given crash report.
	 */
	public CrashReportCategory addWorldInfoToCrashReport(CrashReport report) {
		CrashReportCategory crashreportcategory = report.makeCategoryDepth("Affected level", 1);
		crashreportcategory.addCrashSection("Level name",
				this.worldInfo == null ? "????" : this.worldInfo.getWorldName());
		crashreportcategory.addCrashSectionCallable("All players", new Callable<String>() {
			public String call() {
				return World.this.playerEntities.size() + " total; " + World.this.playerEntities.toString();
			}
		});
		crashreportcategory.addCrashSectionCallable("Chunk stats", new Callable<String>() {
			public String call() {
				return World.this.chunkProvider.makeString();
			}
		});

		try {
			this.worldInfo.addToCrashReport(crashreportcategory);
		} catch (Throwable throwable) {
			crashreportcategory.addCrashSectionThrowable("Level Data Unobtainable", throwable);
		}

		return crashreportcategory;
	}

	public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
		for (int i = 0; i < this.worldAccesses.size(); ++i) {
			IWorldAccess iworldaccess = (IWorldAccess) this.worldAccesses.get(i);
			iworldaccess.sendBlockBreakProgress(breakerId, pos, progress);
		}

	}

	/**+
	 * returns a calendar object containing the current date
	 */
	public Calendar getCurrentDate() {
		if (this.getTotalWorldTime() % 600L == 0L) {
			this.theCalendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
		}

		return this.theCalendar;
	}

	public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ,
			NBTTagCompound compund) {
	}

	public Scoreboard getScoreboard() {
		return this.worldScoreboard;
	}

	public void updateComparatorOutputLevel(BlockPos pos, Block blockIn) {
		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
		for (int i = 0; i < facings.length; ++i) {
			EnumFacing enumfacing = facings[i];
			BlockPos blockpos = pos.offset(enumfacing);
			if (this.isBlockLoaded(blockpos)) {
				IBlockState iblockstate = this.getBlockState(blockpos);
				if (Blocks.unpowered_comparator.isAssociated(iblockstate.getBlock())) {
					iblockstate.getBlock().onNeighborBlockChange(this, blockpos, iblockstate, blockIn);
				} else if (iblockstate.getBlock().isNormalCube()) {
					blockpos = blockpos.offset(enumfacing);
					iblockstate = this.getBlockState(blockpos);
					if (Blocks.unpowered_comparator.isAssociated(iblockstate.getBlock())) {
						iblockstate.getBlock().onNeighborBlockChange(this, blockpos, iblockstate, blockIn);
					}
				}
			}
		}

	}

	public DifficultyInstance getDifficultyForLocation(BlockPos pos) {
		long i = 0L;
		float f = 0.0F;
		if (this.isBlockLoaded(pos)) {
			f = this.getCurrentMoonPhaseFactor();
			i = this.getChunkFromBlockCoords(pos).getInhabitedTime();
		}

		return new DifficultyInstance(this.getDifficulty(), this.getWorldTime(), i, f);
	}

	public EnumDifficulty getDifficulty() {
		return this.getWorldInfo().getDifficulty();
	}

	public int getSkylightSubtracted() {
		return this.skylightSubtracted;
	}

	public void setSkylightSubtracted(int newSkylightSubtracted) {
		this.skylightSubtracted = newSkylightSubtracted;
	}

	public int getLastLightningBolt() {
		return this.lastLightningBolt;
	}

	public void setLastLightningBolt(int lastLightningBoltIn) {
		this.lastLightningBolt = lastLightningBoltIn;
	}

	public boolean isFindingSpawnPoint() {
		return this.findingSpawnPoint;
	}

	public VillageCollection getVillageCollection() {
		return this.villageCollectionObj;
	}

	public WorldBorder getWorldBorder() {
		return this.worldBorder;
	}

	/**+
	 * Returns true if the chunk is located near the spawn point
	 */
	public boolean isSpawnChunk(int x, int z) {
		if (!MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
				.getBoolean("loadSpawnChunks"))
			return false;
		BlockPos blockpos = this.getSpawnPoint();
		int i = x * 16 + 8 - blockpos.getX();
		int j = z * 16 + 8 - blockpos.getZ();
		short short1 = 128;
		return i >= -short1 && i <= short1 && j >= -short1 && j <= short1;
	}

	public LightingEngine alfheim$getLightingEngine() {
		return alfheim$lightingEngine;
	}
}