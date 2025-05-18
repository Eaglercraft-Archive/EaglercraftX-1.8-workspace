package net.minecraft.world;

import com.carrotsearch.hppc.LongHashSet;
import com.carrotsearch.hppc.LongSet;
import com.carrotsearch.hppc.cursors.LongCursor;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

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
public final class SpawnerAnimals {
	private static final int MOB_COUNT_DIV = (int) Math.pow(17.0D, 2.0D);
	/**+
	 * The 17x17 area around the player where mobs can spawn
	 */
	private final LongSet eligibleChunksForSpawning = new LongHashSet();

	/**+
	 * adds all chunks within the spawn radius of the players to
	 * eligibleChunksForSpawning. pars: the world, hostileCreatures,
	 * passiveCreatures. returns number of eligible chunks.
	 */
	public int findChunksForSpawning(WorldServer spawnHostileMobs, boolean spawnPeacefulMobs, boolean parFlag2,
			boolean parFlag3) {
		if (!spawnPeacefulMobs && !parFlag2) {
			return 0;
		} else {
			this.eligibleChunksForSpawning.clear();
			int i = 0;

			List<EntityPlayer> lst = spawnHostileMobs.playerEntities;
			for (int m = 0, n = lst.size(); m < n; ++m) {
				EntityPlayer entityplayer = lst.get(m);
				if (!entityplayer.isSpectator()) {
					int j = MathHelper.floor_double(entityplayer.posX / 16.0D);
					int k = MathHelper.floor_double(entityplayer.posZ / 16.0D);
					byte b0 = (byte) spawnHostileMobs.getMinecraftServer().getConfigurationManager().getViewDistance();

					for (int l = -b0; l <= b0; ++l) {
						for (int i1 = -b0; i1 <= b0; ++i1) {
							boolean flag = l == -b0 || l == b0 || i1 == -b0 || i1 == b0;
							int cx = l + j;
							int cz = i1 + k;
							long chunkcoordintpair = ChunkCoordIntPair.chunkXZ2Int(cx, cz);
							if (!this.eligibleChunksForSpawning.contains(chunkcoordintpair)) {
								++i;
								if (!flag && spawnHostileMobs.getWorldBorder().contains(chunkcoordintpair)) {
									this.eligibleChunksForSpawning.add(chunkcoordintpair);
								}
							}
						}
					}
				}
			}

			int k3 = 0;
			BlockPos blockpos2 = spawnHostileMobs.getSpawnPoint();

			EnumCreatureType[] types = EnumCreatureType._VALUES;
			for (int m = 0; m < types.length; ++m) {
				EnumCreatureType enumcreaturetype = types[m];
				if ((!enumcreaturetype.getPeacefulCreature() || parFlag2)
						&& (enumcreaturetype.getPeacefulCreature() || spawnPeacefulMobs)
						&& (!enumcreaturetype.getAnimal() || parFlag3)) {
					int l3 = spawnHostileMobs.countEntities(enumcreaturetype.getCreatureClass());
					int i4 = enumcreaturetype.getMaxNumberOfCreature() * i / MOB_COUNT_DIV;
					if (l3 <= i4) {
						label374: for (LongCursor chunkcoordintpair1 : this.eligibleChunksForSpawning) {
							long chunkcoordintpair1l = chunkcoordintpair1.value;
							int chunkXPos = (int) (chunkcoordintpair1l & 4294967295L);
							int chunkZPos = (int) (chunkcoordintpair1l >>> 32);
							BlockPos blockpos = getRandomChunkPosition(spawnHostileMobs, chunkXPos, chunkZPos);
							int j1 = blockpos.getX();
							int k1 = blockpos.getY();
							int l1 = blockpos.getZ();
							Block block = spawnHostileMobs.getBlockState(blockpos).getBlock();
							if (!block.isNormalCube()) {
								int i2 = 0;

								for (int j2 = 0; j2 < 3; ++j2) {
									int k2 = j1;
									int l2 = k1;
									int i3 = l1;
									byte b1 = 6;
									BiomeGenBase.SpawnListEntry biomegenbase$spawnlistentry = null;
									IEntityLivingData ientitylivingdata = null;

									for (int j3 = 0; j3 < 4; ++j3) {
										k2 += spawnHostileMobs.rand.nextInt(b1) - spawnHostileMobs.rand.nextInt(b1);
										l2 += spawnHostileMobs.rand.nextInt(1) - spawnHostileMobs.rand.nextInt(1);
										i3 += spawnHostileMobs.rand.nextInt(b1) - spawnHostileMobs.rand.nextInt(b1);
										BlockPos blockpos1 = new BlockPos(k2, l2, i3);
										float f = (float) k2 + 0.5F;
										float f1 = (float) i3 + 0.5F;
										if (!spawnHostileMobs.isAnyPlayerWithinRangeAt((double) f, (double) l2,
												(double) f1, 24.0D)
												&& blockpos2.distanceSq((double) f, (double) l2,
														(double) f1) >= 576.0D) {
											if (biomegenbase$spawnlistentry == null) {
												biomegenbase$spawnlistentry = spawnHostileMobs
														.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos1);
												if (biomegenbase$spawnlistentry == null) {
													break;
												}
											}

											if (spawnHostileMobs.canCreatureTypeSpawnHere(enumcreaturetype,
													biomegenbase$spawnlistentry, blockpos1)
													&& canCreatureTypeSpawnAtLocation(
															EntitySpawnPlacementRegistry.getPlacementForEntity(
																	biomegenbase$spawnlistentry.entityClass),
															spawnHostileMobs, blockpos1)) {
												EntityLiving entityliving;
												try {
													entityliving = (EntityLiving) EntityList.createEntityByClassUnsafe(
															biomegenbase$spawnlistentry.entityClass, spawnHostileMobs);
												} catch (Exception exception) {
													EagRuntime.debugPrintStackTrace(exception);
													return k3;
												}

												entityliving.setLocationAndAngles((double) f, (double) l2, (double) f1,
														spawnHostileMobs.rand.nextFloat() * 360.0F, 0.0F);
												if (entityliving.getCanSpawnHere() && entityliving.isNotColliding()) {
													ientitylivingdata = entityliving
															.onInitialSpawn(
																	spawnHostileMobs.getDifficultyForLocation(
																			new BlockPos(entityliving)),
																	ientitylivingdata);
													if (entityliving.isNotColliding()) {
														++i2;
														spawnHostileMobs.spawnEntityInWorld(entityliving);
													}

													if (i2 >= entityliving.getMaxSpawnedInChunk()) {
														continue label374;
													}
												}

												k3 += i2;
											}
										}
									}
								}
							}
						}
					}
				}
			}

			return k3;
		}
	}

	protected static BlockPos getRandomChunkPosition(World worldIn, int x, int z) {
		Chunk chunk = worldIn.getChunkFromChunkCoords(x, z);
		int i = x * 16 + worldIn.rand.nextInt(16);
		int j = z * 16 + worldIn.rand.nextInt(16);
		int k = MathHelper.func_154354_b(chunk.getHeight(new BlockPos(i, 0, j)) + 1, 16);
		int l = worldIn.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
		return new BlockPos(i, l, j);
	}

	public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType worldIn, World pos,
			BlockPos parBlockPos) {
		if (!pos.getWorldBorder().contains(parBlockPos)) {
			return false;
		} else {
			Block block = pos.getBlockState(parBlockPos).getBlock();
			if (worldIn == EntityLiving.SpawnPlacementType.IN_WATER) {
				return block.getMaterial().isLiquid()
						&& pos.getBlockState(parBlockPos.down()).getBlock().getMaterial().isLiquid()
						&& !pos.getBlockState(parBlockPos.up()).getBlock().isNormalCube();
			} else {
				BlockPos blockpos = parBlockPos.down();
				if (!World.doesBlockHaveSolidTopSurface(pos, blockpos)) {
					return false;
				} else {
					Block block1 = pos.getBlockState(blockpos).getBlock();
					boolean flag = block1 != Blocks.bedrock && block1 != Blocks.barrier;
					return flag && !block.isNormalCube() && !block.getMaterial().isLiquid()
							&& !pos.getBlockState(parBlockPos.up()).getBlock().isNormalCube();
				}
			}
		}
	}

	/**+
	 * Called during chunk generation to spawn initial creatures.
	 */
	public static void performWorldGenSpawning(World worldIn, BiomeGenBase parBiomeGenBase, int parInt1, int parInt2,
			int parInt3, int parInt4, EaglercraftRandom parRandom) {
		List list = parBiomeGenBase.getSpawnableList(EnumCreatureType.CREATURE);
		if (!list.isEmpty()) {
			while (parRandom.nextFloat() < parBiomeGenBase.getSpawningChance()) {
				BiomeGenBase.SpawnListEntry biomegenbase$spawnlistentry = (BiomeGenBase.SpawnListEntry) WeightedRandom
						.getRandomItem(worldIn.rand, list);
				int i = biomegenbase$spawnlistentry.minGroupCount + parRandom.nextInt(
						1 + biomegenbase$spawnlistentry.maxGroupCount - biomegenbase$spawnlistentry.minGroupCount);
				IEntityLivingData ientitylivingdata = null;
				int j = parInt1 + parRandom.nextInt(parInt3);
				int k = parInt2 + parRandom.nextInt(parInt4);
				int l = j;
				int i1 = k;

				for (int j1 = 0; j1 < i; ++j1) {
					boolean flag = false;

					for (int k1 = 0; !flag && k1 < 4; ++k1) {
						BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
						if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn,
								blockpos)) {
							EntityLiving entityliving;
							try {
								entityliving = (EntityLiving) EntityList
										.createEntityByClass(biomegenbase$spawnlistentry.entityClass, worldIn);
							} catch (Exception exception) {
								EagRuntime.debugPrintStackTrace(exception);
								continue;
							}

							entityliving.setLocationAndAngles((double) ((float) j + 0.5F), (double) blockpos.getY(),
									(double) ((float) k + 0.5F), parRandom.nextFloat() * 360.0F, 0.0F);
							worldIn.spawnEntityInWorld(entityliving);
							ientitylivingdata = entityliving.onInitialSpawn(
									worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
							flag = true;
						}

						j += parRandom.nextInt(5) - parRandom.nextInt(5);

						for (k += parRandom.nextInt(5) - parRandom.nextInt(5); j < parInt1 || j >= parInt1 + parInt3
								|| k < parInt2
								|| k >= parInt2 + parInt3; k = i1 + parRandom.nextInt(5) - parRandom.nextInt(5)) {
							j = l + parRandom.nextInt(5) - parRandom.nextInt(5);
						}
					}
				}
			}

		}
	}
}