package net.minecraft.client.multiplayer;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import java.util.Set;
import java.util.concurrent.Callable;

import com.carrotsearch.hppc.LongHashSet;
import com.carrotsearch.hppc.LongSet;
import com.carrotsearch.hppc.cursors.LongCursor;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EntityFirework;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
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
public class WorldClient extends World {
	private NetHandlerPlayClient sendQueue;
	private ChunkProviderClient clientChunkProvider;
	/**+
	 * Contains all entities for this client, both spawned and
	 * non-spawned.
	 */
	private final Set<Entity> entityList = Sets.newHashSet();
	/**+
	 * Contains all entities for this client that were not spawned
	 * due to a non-present chunk. The game will attempt to spawn up
	 * to 10 pending entities with each subsequent tick until the
	 * spawn queue is empty.
	 */
	private final Set<Entity> entitySpawnQueue = Sets.newHashSet();
	private final Minecraft mc = Minecraft.getMinecraft();
	private final LongSet previousActiveChunkSet = new LongHashSet();

	public WorldClient(NetHandlerPlayClient parNetHandlerPlayClient, WorldSettings parWorldSettings, int parInt1,
			EnumDifficulty parEnumDifficulty) {
		super(new SaveHandlerMP(), new WorldInfo(parWorldSettings, "MpServer"),
				WorldProvider.getProviderForDimension(parInt1), true);
		this.sendQueue = parNetHandlerPlayClient;
		this.getWorldInfo().setDifficulty(parEnumDifficulty);
		this.setSpawnPoint(new BlockPos(8, 64, 8));
		this.provider.registerWorld(this);
		this.chunkProvider = this.createChunkProvider();
		this.mapStorage = new SaveDataMemoryStorage();
		this.calculateInitialSkylight();
		this.calculateInitialWeather();
	}

	/**+
	 * Runs a single tick for the world
	 */
	public void tick() {
		super.tick();
		this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
		if (this.getGameRules().getBoolean("doDaylightCycle")) {
			this.setWorldTime(this.getWorldTime() + 1L);
		}

		for (int i = 0; i < 10 && !this.entitySpawnQueue.isEmpty(); ++i) {
			Entity entity = (Entity) this.entitySpawnQueue.iterator().next();
			this.entitySpawnQueue.remove(entity);
			if (!this.loadedEntityList.contains(entity)) {
				this.spawnEntityInWorld(entity);
			}
		}

		this.clientChunkProvider.unloadQueuedChunks();
		this.updateBlocks();
	}

	/**+
	 * Invalidates an AABB region of blocks from the receive queue,
	 * in the event that the block has been modified client-side in
	 * the intervening 80 receive ticks.
	 */
	public void invalidateBlockReceiveRegion(int parInt1, int parInt2, int parInt3, int parInt4, int parInt5,
			int parInt6) {
	}

	/**+
	 * Creates the chunk provider for this world. Called in the
	 * constructor. Retrieves provider from worldProvider?
	 */
	protected IChunkProvider createChunkProvider() {
		this.clientChunkProvider = new ChunkProviderClient(this);
		return this.clientChunkProvider;
	}

	protected void updateBlocks() {
		super.updateBlocks();
		this.previousActiveChunkSet.retainAll(this.activeChunkSet::contains);
		if (this.previousActiveChunkSet.size() == this.activeChunkSet.size()) {
			this.previousActiveChunkSet.clear();
		}

		int i = 0;

		for (LongCursor chunkcoordintpair : this.activeChunkSet) {
			long l = chunkcoordintpair.value;
			if (!this.previousActiveChunkSet.contains(l)) {
				int chunkXPos = (int) (l & 4294967295L);
				int chunkZPos = (int) (l >>> 32);
				int j = chunkXPos * 16;
				int k = chunkZPos * 16;
				Chunk chunk = this.getChunkFromChunkCoords(chunkXPos, chunkZPos);
				this.playMoodSoundAndCheckLight(j, k, chunk);
				this.previousActiveChunkSet.add(l);
				++i;
				if (i >= 10) {
					return;
				}
			}
		}

	}

	public void doPreChunk(int parInt1, int parInt2, boolean parFlag) {
		if (parFlag) {
			this.clientChunkProvider.loadChunk(parInt1, parInt2);
		} else {
			this.clientChunkProvider.unloadChunk(parInt1, parInt2);
		}

		if (!parFlag) {
			this.markBlockRangeForRenderUpdate(parInt1 * 16, 0, parInt2 * 16, parInt1 * 16 + 15, 256,
					parInt2 * 16 + 15);
		}

	}

	/**+
	 * Called when an entity is spawned in the world. This includes
	 * players.
	 */
	public boolean spawnEntityInWorld(Entity entity) {
		boolean flag = super.spawnEntityInWorld(entity);
		this.entityList.add(entity);
		if (!flag) {
			this.entitySpawnQueue.add(entity);
		} else if (entity instanceof EntityMinecart) {
			this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart) entity));
		}

		return flag;
	}

	/**+
	 * Schedule the entity for removal during the next tick. Marks
	 * the entity dead in anticipation.
	 */
	public void removeEntity(Entity entity) {
		super.removeEntity(entity);
		this.entityList.remove(entity);
	}

	protected void onEntityAdded(Entity entity) {
		super.onEntityAdded(entity);
		if (this.entitySpawnQueue.contains(entity)) {
			this.entitySpawnQueue.remove(entity);
		}

	}

	protected void onEntityRemoved(Entity entity) {
		super.onEntityRemoved(entity);
		boolean flag = false;
		if (this.entityList.contains(entity)) {
			if (entity.isEntityAlive()) {
				this.entitySpawnQueue.add(entity);
				flag = true;
			} else {
				this.entityList.remove(entity);
			}
		}

	}

	/**+
	 * Add an ID to Entity mapping to entityHashSet
	 */
	public void addEntityToWorld(int parInt1, Entity parEntity) {
		Entity entity = this.getEntityByID(parInt1);
		if (entity != null) {
			this.removeEntity(entity);
		}

		this.entityList.add(parEntity);
		parEntity.setEntityId(parInt1);
		if (!this.spawnEntityInWorld(parEntity)) {
			this.entitySpawnQueue.add(parEntity);
		}

		this.entitiesById.put(parInt1, parEntity);
	}

	/**+
	 * Returns the Entity with the given ID, or null if it doesn't
	 * exist in this World.
	 */
	public Entity getEntityByID(int i) {
		return i == this.mc.thePlayer.getEntityId() ? this.mc.thePlayer : super.getEntityByID(i);
	}

	public Entity removeEntityFromWorld(int parInt1) {
		Entity entity = this.entitiesById.remove(parInt1);
		if (entity != null) {
			this.entityList.remove(entity);
			this.removeEntity(entity);
		}

		return entity;
	}

	public boolean invalidateRegionAndSetBlock(BlockPos parBlockPos, IBlockState parIBlockState) {
		int i = parBlockPos.getX();
		int j = parBlockPos.getY();
		int k = parBlockPos.getZ();
		this.invalidateBlockReceiveRegion(i, j, k, i, j, k);
		return super.setBlockState(parBlockPos, parIBlockState, 3);
	}

	/**+
	 * If on MP, sends a quitting packet.
	 */
	public void sendQuittingDisconnectingPacket() {
		this.sendQueue.getNetworkManager().closeChannel(new ChatComponentText("Quitting"));
	}

	/**+
	 * Updates all weather states.
	 */
	protected void updateWeather() {
	}

	protected int getRenderDistanceChunks() {
		return this.mc.gameSettings.renderDistanceChunks;
	}

	public void doVoidFogParticles(int parInt1, int parInt2, int parInt3) {
		byte b0 = 16;
		EaglercraftRandom random = new EaglercraftRandom();
		ItemStack itemstack = this.mc.thePlayer.getHeldItem();
		boolean flag = this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE
				&& itemstack != null && Block.getBlockFromItem(itemstack.getItem()) == Blocks.barrier;
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int i = 0; i < 1000; ++i) {
			int j = parInt1 + this.rand.nextInt(b0) - this.rand.nextInt(b0);
			int k = parInt2 + this.rand.nextInt(b0) - this.rand.nextInt(b0);
			int l = parInt3 + this.rand.nextInt(b0) - this.rand.nextInt(b0);
			blockpos$mutableblockpos.func_181079_c(j, k, l);
			IBlockState iblockstate = this.getBlockState(blockpos$mutableblockpos);
			iblockstate.getBlock().randomDisplayTick(this, blockpos$mutableblockpos, iblockstate, random);
			if (flag && iblockstate.getBlock() == Blocks.barrier) {
				this.spawnParticle(EnumParticleTypes.BARRIER, (double) ((float) j + 0.5F), (double) ((float) k + 0.5F),
						(double) ((float) l + 0.5F), 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}

	}

	/**+
	 * also releases skins.
	 */
	public void removeAllEntities() {
		this.loadedEntityList.removeAll(this.unloadedEntityList);

		for (int i = 0; i < this.unloadedEntityList.size(); ++i) {
			Entity entity = (Entity) this.unloadedEntityList.get(i);
			int j = entity.chunkCoordX;
			int k = entity.chunkCoordZ;
			if (entity.addedToChunk && this.isChunkLoaded(j, k, true)) {
				this.getChunkFromChunkCoords(j, k).removeEntity(entity);
			}
		}

		for (int l = 0; l < this.unloadedEntityList.size(); ++l) {
			this.onEntityRemoved((Entity) this.unloadedEntityList.get(l));
		}

		this.unloadedEntityList.clear();

		for (int i1 = 0; i1 < this.loadedEntityList.size(); ++i1) {
			Entity entity1 = (Entity) this.loadedEntityList.get(i1);
			if (entity1.ridingEntity != null) {
				if (!entity1.ridingEntity.isDead && entity1.ridingEntity.riddenByEntity == entity1) {
					continue;
				}

				entity1.ridingEntity.riddenByEntity = null;
				entity1.ridingEntity = null;
			}

			if (entity1.isDead) {
				int j1 = entity1.chunkCoordX;
				int k1 = entity1.chunkCoordZ;
				if (entity1.addedToChunk && this.isChunkLoaded(j1, k1, true)) {
					this.getChunkFromChunkCoords(j1, k1).removeEntity(entity1);
				}

				this.loadedEntityList.remove(i1--);
				this.onEntityRemoved(entity1);
			}
		}

	}

	/**+
	 * Adds some basic stats of the world to the given crash report.
	 */
	public CrashReportCategory addWorldInfoToCrashReport(CrashReport crashreport) {
		CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(crashreport);
		crashreportcategory.addCrashSectionCallable("Forced entities", new Callable<String>() {
			public String call() {
				return WorldClient.this.entityList.size() + " total; " + WorldClient.this.entityList.toString();
			}
		});
		crashreportcategory.addCrashSectionCallable("Retry entities", new Callable<String>() {
			public String call() {
				return WorldClient.this.entitySpawnQueue.size() + " total; "
						+ WorldClient.this.entitySpawnQueue.toString();
			}
		});
		crashreportcategory.addCrashSectionCallable("Server brand", new Callable<String>() {
			public String call() throws Exception {
				return WorldClient.this.mc.thePlayer.getClientBrand();
			}
		});
		crashreportcategory.addCrashSectionCallable("Server type", new Callable<String>() {
			public String call() throws Exception {
				return "Non-integrated multiplayer server";
			}
		});
		return crashreportcategory;
	}

	/**+
	 * Plays a sound at the specified position.
	 */
	public void playSoundAtPos(BlockPos parBlockPos, String parString1, float parFloat1, float parFloat2,
			boolean parFlag) {
		this.playSound((double) parBlockPos.getX() + 0.5D, (double) parBlockPos.getY() + 0.5D,
				(double) parBlockPos.getZ() + 0.5D, parString1, parFloat1, parFloat2, parFlag);
	}

	/**+
	 * par8 is loudness, all pars passed to
	 * minecraftInstance.sndManager.playSound
	 */
	public void playSound(double d0, double d1, double d2, String s, float f, float f1, boolean flag) {
		double d3 = this.mc.getRenderViewEntity().getDistanceSq(d0, d1, d2);
		PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(new ResourceLocation(s), f, f1,
				(float) d0, (float) d1, (float) d2);
		if (flag && d3 > 100.0D) {
			double d4 = Math.sqrt(d3) / 40.0D;
			this.mc.getSoundHandler().playDelayedSound(positionedsoundrecord, (int) (d4 * 20.0D));
		} else {
			this.mc.getSoundHandler().playSound(positionedsoundrecord);
		}

	}

	public void makeFireworks(double d0, double d1, double d2, double d3, double d4, double d5,
			NBTTagCompound nbttagcompound) {
		this.mc.effectRenderer.addEffect(
				new EntityFirework.StarterFX(this, d0, d1, d2, d3, d4, d5, this.mc.effectRenderer, nbttagcompound));
	}

	public void setWorldScoreboard(Scoreboard parScoreboard) {
		this.worldScoreboard = parScoreboard;
	}

	/**+
	 * Sets the world time.
	 */
	public void setWorldTime(long i) {
		if (i < 0L) {
			i = -i;
			this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
		} else {
			this.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
		}

		super.setWorldTime(i);
	}
}