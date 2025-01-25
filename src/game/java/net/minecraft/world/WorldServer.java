package net.minecraft.world;

import com.carrotsearch.hppc.cursors.LongCursor;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import java.util.Set;
import java.util.TreeSet;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.INpc;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
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
public class WorldServer extends World implements IThreadListener {
	private static final Logger logger = LogManager.getLogger();
	private final MinecraftServer mcServer;
	private final EntityTracker theEntityTracker;
	private final PlayerManager thePlayerManager;
	private final Set<NextTickListEntry> pendingTickListEntriesHashSet = Sets.newHashSet();
	/**+
	 * All work to do in future ticks.
	 */
	private final TreeSet<NextTickListEntry> pendingTickListEntriesTreeSet = new TreeSet();
	private final Map<EaglercraftUUID, Entity> entitiesByUuid = Maps.newHashMap();
	public ChunkProviderServer theChunkProviderServer;
	public boolean disableLevelSaving;
	private boolean allPlayersSleeping;
	private int updateEntityTick;
	private final Teleporter worldTeleporter;
	private final SpawnerAnimals mobSpawner = new SpawnerAnimals();
	protected final VillageSiege villageSiege = new VillageSiege(this);
	private WorldServer.ServerBlockEventList[] field_147490_S = new WorldServer.ServerBlockEventList[] {
			new WorldServer.ServerBlockEventList(), new WorldServer.ServerBlockEventList() };
	private int blockEventCacheIndex;
	private static final List<WeightedRandomChestContent> bonusChestContent = Lists
			.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.stick, 0, 1, 3, 10),
					new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.planks), 0, 1, 3, 10),
					new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log), 0, 1, 3, 10),
					new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 3),
					new WeightedRandomChestContent(Items.wooden_axe, 0, 1, 1, 5),
					new WeightedRandomChestContent(Items.stone_pickaxe, 0, 1, 1, 3),
					new WeightedRandomChestContent(Items.wooden_pickaxe, 0, 1, 1, 5),
					new WeightedRandomChestContent(Items.apple, 0, 2, 3, 5),
					new WeightedRandomChestContent(Items.bread, 0, 2, 3, 3),
					new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log2), 0, 1, 3, 10) });
	private List<NextTickListEntry> pendingTickListEntriesThisTick = Lists.newArrayList();

	public WorldServer(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId) {
		super(saveHandlerIn, info, WorldProvider.getProviderForDimension(dimensionId), false);
		this.mcServer = server;
		this.theEntityTracker = new EntityTracker(this);
		this.thePlayerManager = new PlayerManager(this);
		this.provider.registerWorld(this);
		this.chunkProvider = this.createChunkProvider();
		this.worldTeleporter = new Teleporter(this);
		this.calculateInitialSkylight();
		this.calculateInitialWeather();
		this.getWorldBorder().setSize(server.getMaxWorldSize());
	}

	public World init() {
		this.mapStorage = new MapStorage(this.saveHandler);
		String s = VillageCollection.fileNameForProvider(this.provider);
		VillageCollection villagecollection = (VillageCollection) this.mapStorage.loadData(VillageCollection.class, s);
		if (villagecollection == null) {
			this.villageCollectionObj = new VillageCollection(this);
			this.mapStorage.setData(s, this.villageCollectionObj);
		} else {
			this.villageCollectionObj = villagecollection;
			this.villageCollectionObj.setWorldsForAll(this);
		}

		this.worldScoreboard = new ServerScoreboard(this.mcServer);
		ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData) this.mapStorage.loadData(ScoreboardSaveData.class,
				"scoreboard");
		if (scoreboardsavedata == null) {
			scoreboardsavedata = new ScoreboardSaveData();
			this.mapStorage.setData("scoreboard", scoreboardsavedata);
		}

		scoreboardsavedata.setScoreboard(this.worldScoreboard);
		((ServerScoreboard) this.worldScoreboard).func_96547_a(scoreboardsavedata);
		this.getWorldBorder().setCenter(this.worldInfo.getBorderCenterX(), this.worldInfo.getBorderCenterZ());
		this.getWorldBorder().setDamageAmount(this.worldInfo.getBorderDamagePerBlock());
		this.getWorldBorder().setDamageBuffer(this.worldInfo.getBorderSafeZone());
		this.getWorldBorder().setWarningDistance(this.worldInfo.getBorderWarningDistance());
		this.getWorldBorder().setWarningTime(this.worldInfo.getBorderWarningTime());
		if (this.worldInfo.getBorderLerpTime() > 0L) {
			this.getWorldBorder().setTransition(this.worldInfo.getBorderSize(), this.worldInfo.getBorderLerpTarget(),
					this.worldInfo.getBorderLerpTime());
		} else {
			this.getWorldBorder().setTransition(this.worldInfo.getBorderSize());
		}

		return this;
	}

	/**+
	 * Runs a single tick for the world
	 */
	public void tick() {
		super.tick();
		if (this.getWorldInfo().isHardcoreModeEnabled() && this.getDifficulty() != EnumDifficulty.HARD) {
			this.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
		}

		this.provider.getWorldChunkManager().cleanupCache();
		if (this.areAllPlayersAsleep()) {
			if (this.getGameRules().getBoolean("doDaylightCycle")) {
				long i = this.worldInfo.getWorldTime() + 24000L;
				this.worldInfo.setWorldTime(i - i % 24000L);
			}

			this.wakeAllPlayers();
		}

		if (this.getGameRules().getBoolean("doMobSpawning")
				&& this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD) {
			this.mobSpawner.findChunksForSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs,
					this.worldInfo.getWorldTotalTime() % 400L == 0L);
		}

		this.chunkProvider.unloadQueuedChunks();
		alfheim$getLightingEngine().processLightUpdates();
		int j = this.calculateSkylightSubtracted(1.0F);
		if (j != this.getSkylightSubtracted()) {
			this.setSkylightSubtracted(j);
		}

		this.worldInfo.setWorldTotalTime(this.worldInfo.getWorldTotalTime() + 1L);
		if (this.getGameRules().getBoolean("doDaylightCycle")) {
			this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
		}

		this.tickUpdates(false);
		this.updateBlocks();
		this.thePlayerManager.updatePlayerInstances();
		this.villageCollectionObj.tick();
		this.villageSiege.tick();
		this.worldTeleporter.removeStalePortalLocations(this.getTotalWorldTime());
		this.sendQueuedBlockEvents();
	}

	public BiomeGenBase.SpawnListEntry getSpawnListEntryForTypeAt(EnumCreatureType creatureType, BlockPos pos) {
		List list = this.getChunkProvider().getPossibleCreatures(creatureType, pos);
		return list != null && !list.isEmpty()
				? (BiomeGenBase.SpawnListEntry) WeightedRandom.getRandomItem(this.rand, list)
				: null;
	}

	public boolean canCreatureTypeSpawnHere(EnumCreatureType creatureType, BiomeGenBase.SpawnListEntry spawnListEntry,
			BlockPos pos) {
		List list = this.getChunkProvider().getPossibleCreatures(creatureType, pos);
		return list != null && !list.isEmpty() ? list.contains(spawnListEntry) : false;
	}

	/**+
	 * Updates the flag that indicates whether or not all players in
	 * the world are sleeping.
	 */
	public void updateAllPlayersSleepingFlag() {
		this.allPlayersSleeping = false;
		if (!this.playerEntities.isEmpty()) {
			int i = 0;
			int j = 0;

			for (int k = 0, l = this.playerEntities.size(); k < l; ++k) {
				EntityPlayer entityplayer = this.playerEntities.get(k);
				if (entityplayer.isSpectator()) {
					++i;
				} else if (entityplayer.isPlayerSleeping()) {
					++j;
				}
			}

			this.allPlayersSleeping = j > 0 && j >= this.playerEntities.size() - i;
		}

	}

	protected void wakeAllPlayers() {
		this.allPlayersSleeping = false;

		for (int k = 0, l = this.playerEntities.size(); k < l; ++k) {
			EntityPlayer entityplayer = this.playerEntities.get(k);
			if (entityplayer.isPlayerSleeping()) {
				entityplayer.wakeUpPlayer(false, false, true);
			}
		}

		this.resetRainAndThunder();
	}

	private void resetRainAndThunder() {
		this.worldInfo.setRainTime(0);
		this.worldInfo.setRaining(false);
		this.worldInfo.setThunderTime(0);
		this.worldInfo.setThundering(false);
	}

	public boolean areAllPlayersAsleep() {
		if (this.allPlayersSleeping) {
			for (int k = 0, l = this.playerEntities.size(); k < l; ++k) {
				EntityPlayer entityplayer = this.playerEntities.get(k);
				if (entityplayer.isSpectator() || !entityplayer.isPlayerFullyAsleep()) {
					return false;
				}
			}

			return true;
		} else {
			return false;
		}
	}

	/**+
	 * Sets a new spawn location by finding an uncovered block at a
	 * random (x,z) location in the chunk.
	 */
	public void setInitialSpawnLocation() {
		if (this.worldInfo.getSpawnY() <= 0) {
			this.worldInfo.setSpawnY(this.func_181545_F() + 1);
		}

		int i = this.worldInfo.getSpawnX();
		int j = this.worldInfo.getSpawnZ();
		int k = 0;

		while (this.getGroundAboveSeaLevel(new BlockPos(i, 0, j)).getMaterial() == Material.air) {
			i += this.rand.nextInt(8) - this.rand.nextInt(8);
			j += this.rand.nextInt(8) - this.rand.nextInt(8);
			++k;
			if (k == 10000) {
				break;
			}
		}

		this.worldInfo.setSpawnX(i);
		this.worldInfo.setSpawnZ(j);
	}

	protected void updateBlocks() {
		super.updateBlocks();
		if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
			for (LongCursor chunkcoordintpair1 : this.activeChunkSet) {
				long l = chunkcoordintpair1.value;
				this.getChunkFromChunkCoords((int) (l & 4294967295L), (int) (l >>> 32)).func_150804_b(false);
			}

		} else {
			int i = 0;
			int j = 0;

			for (LongCursor chunkcoordintpair : this.activeChunkSet) {
				long ll = chunkcoordintpair.value;
				int chunkXPos = (int) (ll & 4294967295L);
				int chunkZPos = (int) (ll >>> 32);
				int k = chunkXPos * 16;
				int l = chunkZPos * 16;
				Chunk chunk = this.getChunkFromChunkCoords(chunkXPos, chunkZPos);
				this.playMoodSoundAndCheckLight(k, l, chunk);
				chunk.func_150804_b(false);
				if (this.rand.nextInt(100000) == 0 && this.isRaining() && this.isThundering()) {
					this.updateLCG = this.updateLCG * 3 + 1013904223;
					int i1 = this.updateLCG >> 2;
					BlockPos blockpos = this
							.adjustPosToNearbyEntity(new BlockPos(k + (i1 & 15), 0, l + (i1 >> 8 & 15)));
					if (this.canLightningStrike(blockpos)) {
						this.addWeatherEffect(new EntityLightningBolt(this, (double) blockpos.getX(),
								(double) blockpos.getY(), (double) blockpos.getZ()));
					}
				}

				if (this.rand.nextInt(16) == 0) {
					this.updateLCG = this.updateLCG * 3 + 1013904223;
					int k2 = this.updateLCG >> 2;
					BlockPos blockpos2 = this
							.getPrecipitationHeight(new BlockPos(k + (k2 & 15), 0, l + (k2 >> 8 & 15)));
					BlockPos blockpos1 = blockpos2.down();
					if (this.canBlockFreezeNoWater(blockpos1)) {
						this.setBlockState(blockpos1, Blocks.ice.getDefaultState());
					}

					if (this.isRaining() && this.canSnowAt(blockpos2, true)) {
						this.setBlockState(blockpos2, Blocks.snow_layer.getDefaultState());
					}

					if (this.isRaining() && this.getBiomeGenForCoords(blockpos1).canSpawnLightningBolt()) {
						this.getBlockState(blockpos1).getBlock().fillWithRain(this, blockpos1);
					}
				}

				int l2 = this.getGameRules().getInt("randomTickSpeed");
				if (l2 > 0) {
					ExtendedBlockStorage[] vigg = chunk.getBlockStorageArray();
					for (int m = 0; m < vigg.length; ++m) {
						ExtendedBlockStorage extendedblockstorage = vigg[m];
						if (extendedblockstorage != null && extendedblockstorage.getNeedsRandomTick()) {
							for (int j1 = 0; j1 < l2; ++j1) {
								this.updateLCG = this.updateLCG * 3 + 1013904223;
								int k1 = this.updateLCG >> 2;
								int l1 = k1 & 15;
								int i2 = k1 >> 8 & 15;
								int j2 = k1 >> 16 & 15;
								++j;
								IBlockState iblockstate = extendedblockstorage.get(l1, j2, i2);
								Block block = iblockstate.getBlock();
								if (block.getTickRandomly()) {
									++i;
									block.randomTick(this,
											new BlockPos(l1 + k, j2 + extendedblockstorage.getYLocation(), i2 + l),
											iblockstate, this.rand);
									++EaglerMinecraftServer.counterTileUpdate;
								}
							}
						}
					}
				}
			}
		}
	}

	protected BlockPos adjustPosToNearbyEntity(BlockPos pos) {
		BlockPos blockpos = this.getPrecipitationHeight(pos);
		AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockpos,
				new BlockPos(blockpos.getX(), this.getHeight(), blockpos.getZ()))).expand(3.0D, 3.0D, 3.0D);
		List list = this.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb,
				new Predicate<EntityLivingBase>() {
					public boolean apply(EntityLivingBase entitylivingbase) {
						return entitylivingbase != null && entitylivingbase.isEntityAlive()
								&& WorldServer.this.canSeeSky(entitylivingbase.getPosition());
					}
				});
		return !list.isEmpty() ? ((EntityLivingBase) list.get(this.rand.nextInt(list.size()))).getPosition() : blockpos;
	}

	public boolean isBlockTickPending(BlockPos blockpos, Block block) {
		NextTickListEntry nextticklistentry = new NextTickListEntry(blockpos, block);
		return this.pendingTickListEntriesThisTick.contains(nextticklistentry);
	}

	public void scheduleUpdate(BlockPos blockpos, Block block, int i) {
		this.updateBlockTick(blockpos, block, i, 0);
	}

	public void updateBlockTick(BlockPos blockpos, Block block, int i, int j) {
		NextTickListEntry nextticklistentry = new NextTickListEntry(blockpos, block);
		byte b0 = 0;
		if (this.scheduledUpdatesAreImmediate && block.getMaterial() != Material.air) {
			if (block.requiresUpdates()) {
				b0 = 8;
				if (this.isAreaLoaded(nextticklistentry.position.add(-b0, -b0, -b0),
						nextticklistentry.position.add(b0, b0, b0))) {
					IBlockState iblockstate = this.getBlockState(nextticklistentry.position);
					if (iblockstate.getBlock().getMaterial() != Material.air
							&& iblockstate.getBlock() == nextticklistentry.getBlock()) {
						iblockstate.getBlock().updateTick(this, nextticklistentry.position, iblockstate, this.rand);
						++EaglerMinecraftServer.counterTileUpdate;
					}
				}

				return;
			}

			i = 1;
		}

		if (this.isAreaLoaded(blockpos.add(-b0, -b0, -b0), blockpos.add(b0, b0, b0))) {
			if (block.getMaterial() != Material.air) {
				nextticklistentry.setScheduledTime((long) i + this.worldInfo.getWorldTotalTime());
				nextticklistentry.setPriority(j);
			}

			if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry)) {
				this.pendingTickListEntriesHashSet.add(nextticklistentry);
				this.pendingTickListEntriesTreeSet.add(nextticklistentry);
			}
		}

	}

	public void scheduleBlockUpdate(BlockPos blockpos, Block block, int i, int j) {
		NextTickListEntry nextticklistentry = new NextTickListEntry(blockpos, block);
		nextticklistentry.setPriority(j);
		if (block.getMaterial() != Material.air) {
			nextticklistentry.setScheduledTime((long) i + this.worldInfo.getWorldTotalTime());
		}

		if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry)) {
			this.pendingTickListEntriesHashSet.add(nextticklistentry);
			this.pendingTickListEntriesTreeSet.add(nextticklistentry);
		}

	}

	/**+
	 * Updates (and cleans up) entities and tile entities
	 */
	public void updateEntities() {
		if (this.playerEntities.isEmpty()) {
			if (this.updateEntityTick++ >= 1200) {
				return;
			}
		} else {
			this.resetUpdateEntityTick();
		}

		super.updateEntities();
	}

	/**+
	 * Resets the updateEntityTick field to 0
	 */
	public void resetUpdateEntityTick() {
		this.updateEntityTick = 0;
	}

	/**+
	 * Runs through the list of updates to run and ticks them
	 */
	public boolean tickUpdates(boolean flag) {
		if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
			return false;
		} else {
			int i = this.pendingTickListEntriesTreeSet.size();
			if (i != this.pendingTickListEntriesHashSet.size()) {
				throw new IllegalStateException("TickNextTick list out of synch");
			} else {
				if (i > 1000) {
					i = 1000;
				}

				for (int j = 0; j < i; ++j) {
					NextTickListEntry nextticklistentry = (NextTickListEntry) this.pendingTickListEntriesTreeSet
							.first();
					if (!flag && nextticklistentry.scheduledTime > this.worldInfo.getWorldTotalTime()) {
						break;
					}

					this.pendingTickListEntriesTreeSet.remove(nextticklistentry);
					this.pendingTickListEntriesHashSet.remove(nextticklistentry);
					this.pendingTickListEntriesThisTick.add(nextticklistentry);
				}

				Iterator iterator = this.pendingTickListEntriesThisTick.iterator();

				while (iterator.hasNext()) {
					NextTickListEntry nextticklistentry1 = (NextTickListEntry) iterator.next();
					iterator.remove();
					byte b0 = 0;
					if (this.isAreaLoaded(nextticklistentry1.position.add(-b0, -b0, -b0),
							nextticklistentry1.position.add(b0, b0, b0))) {
						IBlockState iblockstate = this.getBlockState(nextticklistentry1.position);
						if (iblockstate.getBlock().getMaterial() != Material.air
								&& Block.isEqualTo(iblockstate.getBlock(), nextticklistentry1.getBlock())) {
							try {
								iblockstate.getBlock().updateTick(this, nextticklistentry1.position, iblockstate,
										this.rand);
								++EaglerMinecraftServer.counterTileUpdate;
							} catch (Throwable throwable) {
								CrashReport crashreport = CrashReport.makeCrashReport(throwable,
										"Exception while ticking a block");
								CrashReportCategory crashreportcategory = crashreport
										.makeCategory("Block being ticked");
								CrashReportCategory.addBlockInfo(crashreportcategory, nextticklistentry1.position,
										iblockstate);
								throw new ReportedException(crashreport);
							}
						}
					} else {
						this.scheduleUpdate(nextticklistentry1.position, nextticklistentry1.getBlock(), 0);
					}
				}

				this.pendingTickListEntriesThisTick.clear();
				return !this.pendingTickListEntriesTreeSet.isEmpty();
			}
		}
	}

	public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunk, boolean flag) {
		ChunkCoordIntPair chunkcoordintpair = chunk.getChunkCoordIntPair();
		int i = (chunkcoordintpair.chunkXPos << 4) - 2;
		int j = i + 16 + 2;
		int k = (chunkcoordintpair.chunkZPos << 4) - 2;
		int l = k + 16 + 2;
		return this.func_175712_a(new StructureBoundingBox(i, 0, k, j, 256, l), flag);
	}

	public List<NextTickListEntry> func_175712_a(StructureBoundingBox structureboundingbox, boolean flag) {
		ArrayList arraylist = null;

		for (int i = 0; i < 2; ++i) {
			Iterator iterator;
			if (i == 0) {
				iterator = this.pendingTickListEntriesTreeSet.iterator();
			} else {
				iterator = this.pendingTickListEntriesThisTick.iterator();
			}

			while (iterator.hasNext()) {
				NextTickListEntry nextticklistentry = (NextTickListEntry) iterator.next();
				BlockPos blockpos = nextticklistentry.position;
				if (blockpos.getX() >= structureboundingbox.minX && blockpos.getX() < structureboundingbox.maxX
						&& blockpos.getZ() >= structureboundingbox.minZ
						&& blockpos.getZ() < structureboundingbox.maxZ) {
					if (flag) {
						this.pendingTickListEntriesHashSet.remove(nextticklistentry);
						iterator.remove();
					}

					if (arraylist == null) {
						arraylist = Lists.newArrayList();
					}

					arraylist.add(nextticklistentry);
				}
			}
		}

		return arraylist;
	}

	/**+
	 * Will update the entity in the world if the chunk the entity
	 * is in is currently loaded or its forced to update. Args:
	 * entity, forceUpdate
	 */
	public void updateEntityWithOptionalForce(Entity entity, boolean flag) {
		if (!this.canSpawnAnimals() && (entity instanceof EntityAnimal || entity instanceof EntityWaterMob)) {
			entity.setDead();
		}

		if (!this.canSpawnNPCs() && entity instanceof INpc) {
			entity.setDead();
		}

		super.updateEntityWithOptionalForce(entity, flag);
	}

	private boolean canSpawnNPCs() {
		return this.mcServer.getCanSpawnNPCs();
	}

	private boolean canSpawnAnimals() {
		return this.mcServer.getCanSpawnAnimals();
	}

	/**+
	 * Creates the chunk provider for this world. Called in the
	 * constructor. Retrieves provider from worldProvider?
	 */
	protected IChunkProvider createChunkProvider() {
		IChunkLoader ichunkloader = this.saveHandler.getChunkLoader(this.provider);
		this.theChunkProviderServer = new ChunkProviderServer(this, ichunkloader, this.provider.createChunkGenerator());
		return this.theChunkProviderServer;
	}

	/**+
	 * Returns all the tile entities located between the given
	 * coordinates
	 */
	public List<TileEntity> getTileEntitiesIn(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		ArrayList<TileEntity> arraylist = Lists.newArrayList();

		for (int chunkX = (minX >> 4); chunkX <= ((maxX - 1) >> 4); chunkX++) {
			for (int chunkZ = (minZ >> 4); chunkZ <= ((maxZ - 1) >> 4); chunkZ++) {
				Chunk chunk = getChunkFromChunkCoords(chunkX, chunkZ);
				if (chunk == null) {
					continue;
				}

				for (TileEntity tileentity : chunk.getTileEntityMap().values()) {
					BlockPos pos = tileentity.getPos();
					if ((pos.x >= minX) && (pos.y >= minY) && (pos.z >= minZ) && (pos.x < maxX) && (pos.y < maxY)
							&& (pos.z < maxZ)) {
						arraylist.add(tileentity);
					}
				}
			}
		}

		return arraylist;
	}

	public boolean isBlockModifiable(EntityPlayer entityplayer, BlockPos blockpos) {
		return !this.mcServer.isBlockProtected(this, blockpos, entityplayer)
				&& this.getWorldBorder().contains(blockpos);
	}

	public void initialize(WorldSettings worldsettings) {
		if (!this.worldInfo.isInitialized()) {
			try {
				this.createSpawnPosition(worldsettings);
				if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
					this.setDebugWorldSettings();
				}

				super.initialize(worldsettings);
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception initializing level");

				try {
					this.addWorldInfoToCrashReport(crashreport);
				} catch (Throwable var5) {
					;
				}

				throw new ReportedException(crashreport);
			}

			this.worldInfo.setServerInitialized(true);
		}

	}

	private void setDebugWorldSettings() {
		this.worldInfo.setMapFeaturesEnabled(false);
		this.worldInfo.setAllowCommands(true);
		this.worldInfo.setRaining(false);
		this.worldInfo.setThundering(false);
		this.worldInfo.setCleanWeatherTime(1000000000);
		this.worldInfo.setWorldTime(6000L);
		this.worldInfo.setGameType(WorldSettings.GameType.SPECTATOR);
		this.worldInfo.setHardcore(false);
		this.worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
		this.worldInfo.setDifficultyLocked(true);
		this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
	}

	/**+
	 * creates a spawn position at random within 256 blocks of 0,0
	 */
	private void createSpawnPosition(WorldSettings parWorldSettings) {
		if (!this.provider.canRespawnHere()) {
			this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
		} else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
			this.worldInfo.setSpawn(BlockPos.ORIGIN.up());
		} else {
			this.findingSpawnPoint = true;
			WorldChunkManager worldchunkmanager = this.provider.getWorldChunkManager();
			List list = worldchunkmanager.getBiomesToSpawnIn();
			EaglercraftRandom random = new EaglercraftRandom(this.getSeed());
			BlockPos blockpos = worldchunkmanager.findBiomePosition(0, 0, 256, list, random);
			int i = 0;
			int j = this.provider.getAverageGroundLevel();
			int k = 0;
			if (blockpos != null) {
				i = blockpos.getX();
				k = blockpos.getZ();
			} else {
				logger.warn("Unable to find spawn biome");
			}

			int l = 0;

			while (!this.provider.canCoordinateBeSpawn(i, k)) {
				i += random.nextInt(64) - random.nextInt(64);
				k += random.nextInt(64) - random.nextInt(64);
				++l;
				if (l == 1000) {
					break;
				}
			}

			this.worldInfo.setSpawn(new BlockPos(i, j, k));
			this.findingSpawnPoint = false;
			if (parWorldSettings.isBonusChestEnabled()) {
				this.createBonusChest();
			}

		}
	}

	/**+
	 * Creates the bonus chest in the world.
	 */
	protected void createBonusChest() {
		WorldGeneratorBonusChest worldgeneratorbonuschest = new WorldGeneratorBonusChest(bonusChestContent, 10);

		for (int i = 0; i < 10; ++i) {
			int j = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
			int k = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
			BlockPos blockpos = this.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k)).up();
			if (worldgeneratorbonuschest.generate(this, this.rand, blockpos)) {
				break;
			}
		}

	}

	/**+
	 * Returns null for anything other than the End
	 */
	public BlockPos getSpawnCoordinate() {
		return this.provider.getSpawnCoordinate();
	}

	/**+
	 * Saves all chunks to disk while updating progress bar.
	 */
	public void saveAllChunks(boolean progressCallback, IProgressUpdate parIProgressUpdate) {
		if (this.chunkProvider.canSave()) {
			if (parIProgressUpdate != null) {
				parIProgressUpdate.displaySavingString("Saving level");
			}

			this.saveLevel();
			if (parIProgressUpdate != null) {
				parIProgressUpdate.displayLoadingString("Saving chunks");
			}

			this.chunkProvider.saveChunks(progressCallback, parIProgressUpdate);

			List<Chunk> lst = Lists.newArrayList(this.theChunkProviderServer.func_152380_a());
			for (int i = 0, l = lst.size(); i < l; ++i) {
				Chunk chunk = lst.get(i);
				if (chunk != null && !this.thePlayerManager.hasPlayerInstance(chunk.xPosition, chunk.zPosition)) {
					this.theChunkProviderServer.dropChunk(chunk.xPosition, chunk.zPosition);
				}
			}

		}
	}

	/**+
	 * saves chunk data - currently only called during execution of
	 * the Save All command
	 */
	public void saveChunkData() {
		if (this.chunkProvider.canSave()) {
			this.chunkProvider.saveExtraData();
		}
	}

	/**+
	 * Saves the chunks to disk.
	 */
	protected void saveLevel() {
		this.worldInfo.setBorderSize(this.getWorldBorder().getDiameter());
		this.worldInfo.getBorderCenterX(this.getWorldBorder().getCenterX());
		this.worldInfo.getBorderCenterZ(this.getWorldBorder().getCenterZ());
		this.worldInfo.setBorderSafeZone(this.getWorldBorder().getDamageBuffer());
		this.worldInfo.setBorderDamagePerBlock(this.getWorldBorder().getDamageAmount());
		this.worldInfo.setBorderWarningDistance(this.getWorldBorder().getWarningDistance());
		this.worldInfo.setBorderWarningTime(this.getWorldBorder().getWarningTime());
		this.worldInfo.setBorderLerpTarget(this.getWorldBorder().getTargetSize());
		this.worldInfo.setBorderLerpTime(this.getWorldBorder().getTimeUntilTarget());
		this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo,
				this.mcServer.getConfigurationManager().getHostPlayerData());
		this.mapStorage.saveAllData();
	}

	protected void onEntityAdded(Entity entity) {
		super.onEntityAdded(entity);
		this.entitiesById.put(entity.getEntityId(), entity);
		this.entitiesByUuid.put(entity.getUniqueID(), entity);
		Entity[] aentity = entity.getParts();
		if (aentity != null) {
			for (int i = 0; i < aentity.length; ++i) {
				this.entitiesById.put(aentity[i].getEntityId(), aentity[i]);
			}
		}

	}

	protected void onEntityRemoved(Entity entity) {
		super.onEntityRemoved(entity);
		this.entitiesById.remove(entity.getEntityId());
		this.entitiesByUuid.remove(entity.getUniqueID());
		Entity[] aentity = entity.getParts();
		if (aentity != null) {
			for (int i = 0; i < aentity.length; ++i) {
				this.entitiesById.remove(aentity[i].getEntityId());
			}
		}

	}

	/**+
	 * adds a lightning bolt to the list of lightning bolts in this
	 * world.
	 */
	public boolean addWeatherEffect(Entity entity) {
		if (super.addWeatherEffect(entity)) {
			this.mcServer.getConfigurationManager().sendToAllNear(entity.posX, entity.posY, entity.posZ, 512.0D,
					this.provider.getDimensionId(), new S2CPacketSpawnGlobalEntity(entity));
			return true;
		} else {
			return false;
		}
	}

	/**+
	 * sends a Packet 38 (Entity Status) to all tracked players of
	 * that entity
	 */
	public void setEntityState(Entity entity, byte b0) {
		this.getEntityTracker().func_151248_b(entity, new S19PacketEntityStatus(entity, b0));
	}

	/**+
	 * returns a new explosion. Does initiation (at time of writing
	 * Explosion is not finished)
	 */
	public Explosion newExplosion(Entity entity, double d0, double d1, double d2, float f, boolean flag,
			boolean flag1) {
		Explosion explosion = new Explosion(this, entity, d0, d1, d2, f, flag, flag1);
		explosion.doExplosionA();
		explosion.doExplosionB(false);
		if (!flag1) {
			explosion.func_180342_d();
		}

		List<EntityPlayer> lst = this.playerEntities;
		for (int i = 0, l = lst.size(); i < l; ++i) {
			EntityPlayer entityplayer = lst.get(i);
			if (entityplayer.getDistanceSq(d0, d1, d2) < 4096.0D) {
				((EntityPlayerMP) entityplayer).playerNetServerHandler
						.sendPacket(new S27PacketExplosion(d0, d1, d2, f, explosion.getAffectedBlockPositions(),
								(Vec3) explosion.getPlayerKnockbackMap().get(entityplayer)));
			}
		}

		return explosion;
	}

	public void addBlockEvent(BlockPos blockpos, Block block, int i, int j) {
		BlockEventData blockeventdata = new BlockEventData(blockpos, block, i, j);

		ServerBlockEventList lst = this.field_147490_S[this.blockEventCacheIndex];
		for (int k = 0, l = lst.size(); k < l; ++k) {
			if (lst.get(k).equals(blockeventdata)) {
				return;
			}
		}

		this.field_147490_S[this.blockEventCacheIndex].add(blockeventdata);
	}

	private void sendQueuedBlockEvents() {
		while (!this.field_147490_S[this.blockEventCacheIndex].isEmpty()) {
			int i = this.blockEventCacheIndex;
			this.blockEventCacheIndex ^= 1;

			ServerBlockEventList lst = this.field_147490_S[i];
			for (int k = 0, l = lst.size(); k < l; ++k) {
				BlockEventData blockeventdata = lst.get(k);
				if (this.fireBlockEvent(blockeventdata)) {
					this.mcServer.getConfigurationManager().sendToAllNear((double) blockeventdata.getPosition().getX(),
							(double) blockeventdata.getPosition().getY(), (double) blockeventdata.getPosition().getZ(),
							64.0D, this.provider.getDimensionId(),
							new S24PacketBlockAction(blockeventdata.getPosition(), blockeventdata.getBlock(),
									blockeventdata.getEventID(), blockeventdata.getEventParameter()));
				}
			}

			this.field_147490_S[i].clear();
		}

	}

	private boolean fireBlockEvent(BlockEventData event) {
		IBlockState iblockstate = this.getBlockState(event.getPosition());
		return iblockstate.getBlock() == event.getBlock() ? iblockstate.getBlock().onBlockEventReceived(this,
				event.getPosition(), iblockstate, event.getEventID(), event.getEventParameter()) : false;
	}

	/**+
	 * Syncs all changes to disk and wait for completion.
	 */
	public void flush() {
		this.saveHandler.flush();
	}

	/**+
	 * Updates all weather states.
	 */
	protected void updateWeather() {
		boolean flag = this.isRaining();
		super.updateWeather();
		if (this.prevRainingStrength != this.rainingStrength) {
			this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(
					new S2BPacketChangeGameState(7, this.rainingStrength), this.provider.getDimensionId());
		}

		if (this.prevThunderingStrength != this.thunderingStrength) {
			this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(
					new S2BPacketChangeGameState(8, this.thunderingStrength), this.provider.getDimensionId());
		}

		if (flag != this.isRaining()) {
			if (flag) {
				this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0F));
			} else {
				this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(1, 0.0F));
			}

			this.mcServer.getConfigurationManager()
					.sendPacketToAllPlayers(new S2BPacketChangeGameState(7, this.rainingStrength));
			this.mcServer.getConfigurationManager()
					.sendPacketToAllPlayers(new S2BPacketChangeGameState(8, this.thunderingStrength));
		}

	}

	protected int getRenderDistanceChunks() {
		return this.mcServer.getConfigurationManager().getViewDistance();
	}

	public MinecraftServer getMinecraftServer() {
		return this.mcServer;
	}

	/**+
	 * Gets the EntityTracker
	 */
	public EntityTracker getEntityTracker() {
		return this.theEntityTracker;
	}

	public PlayerManager getPlayerManager() {
		return this.thePlayerManager;
	}

	public Teleporter getDefaultTeleporter() {
		return this.worldTeleporter;
	}

	/**+
	 * Spawns the desired particle and sends the necessary packets
	 * to the relevant connected players.
	 */
	public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord,
			int numberOfParticles, double parDouble4, double parDouble5, double parDouble6, double parDouble7,
			int... parArrayOfInt) {
		this.spawnParticle(particleType, false, xCoord, yCoord, zCoord, numberOfParticles, parDouble4, parDouble5,
				parDouble6, parDouble7, parArrayOfInt);
	}

	/**+
	 * Spawns the desired particle and sends the necessary packets
	 * to the relevant connected players.
	 */
	public void spawnParticle(EnumParticleTypes particleType, boolean longDistance, double xCoord, double yCoord,
			double zCoord, int numberOfParticles, double xOffset, double yOffset, double zOffset, double particleSpeed,
			int... parArrayOfInt) {
		S2APacketParticles s2apacketparticles = new S2APacketParticles(particleType, longDistance, (float) xCoord,
				(float) yCoord, (float) zCoord, (float) xOffset, (float) yOffset, (float) zOffset,
				(float) particleSpeed, numberOfParticles, parArrayOfInt);

		for (int i = 0; i < this.playerEntities.size(); ++i) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) this.playerEntities.get(i);
			BlockPos blockpos = entityplayermp.getPosition();
			double d0 = blockpos.distanceSq(xCoord, yCoord, zCoord);
			if (d0 <= 256.0D || longDistance && d0 <= 65536.0D) {
				entityplayermp.playerNetServerHandler.sendPacket(s2apacketparticles);
			}
		}

	}

	public Entity getEntityFromUuid(EaglercraftUUID uuid) {
		return (Entity) this.entitiesByUuid.get(uuid);
	}

	public void addScheduledTask(Runnable runnable) {
		this.mcServer.addScheduledTask(runnable);
	}

	static class ServerBlockEventList extends ArrayList<BlockEventData> {
		private ServerBlockEventList() {
		}
	}
}