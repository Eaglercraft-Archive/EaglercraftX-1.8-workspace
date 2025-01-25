package net.minecraft.world.gen;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
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
public class ChunkProviderGenerate implements IChunkProvider {
	private EaglercraftRandom rand;
	private NoiseGeneratorOctaves field_147431_j;
	private NoiseGeneratorOctaves field_147432_k;
	private NoiseGeneratorOctaves field_147429_l;
	private NoiseGeneratorPerlin field_147430_m;
	public NoiseGeneratorOctaves noiseGen5;
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves mobSpawnerNoise;
	private World worldObj;
	private final boolean mapFeaturesEnabled;
	private WorldType field_177475_o;
	private final double[] field_147434_q;
	private final float[] parabolicField;
	private ChunkProviderSettings settings;
	private Block field_177476_s = Blocks.water;
	private double[] stoneNoise = new double[256];
	private MapGenBase caveGenerator;
	private MapGenStronghold strongholdGenerator;
	private MapGenVillage villageGenerator;
	private MapGenMineshaft mineshaftGenerator;
	private MapGenScatteredFeature scatteredFeatureGenerator;
	private MapGenBase ravineGenerator;
	private StructureOceanMonument oceanMonumentGenerator;
	private BiomeGenBase[] biomesForGeneration;
	double[] field_147427_d;
	double[] field_147428_e;
	double[] field_147425_f;
	double[] field_147426_g;

	public ChunkProviderGenerate(World worldIn, long parLong1, boolean parFlag, String parString1) {
		this.worldObj = worldIn;
		this.mapFeaturesEnabled = parFlag;
		this.field_177475_o = worldIn.getWorldInfo().getTerrainType();
		boolean scramble = !worldIn.getWorldInfo().isOldEaglercraftRandom();
		this.rand = new EaglercraftRandom(parLong1, scramble);
		this.caveGenerator = new MapGenCaves(scramble);
		this.strongholdGenerator = new MapGenStronghold(scramble);
		this.villageGenerator = new MapGenVillage(scramble);
		this.mineshaftGenerator = new MapGenMineshaft(scramble);
		this.scatteredFeatureGenerator = new MapGenScatteredFeature(scramble);
		this.ravineGenerator = new MapGenRavine(scramble);
		this.oceanMonumentGenerator = new StructureOceanMonument(scramble);
		this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
		this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
		this.field_147429_l = new NoiseGeneratorOctaves(this.rand, 8);
		this.field_147430_m = new NoiseGeneratorPerlin(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.field_147434_q = new double[825];
		this.parabolicField = new float[25];

		for (int i = -2; i <= 2; ++i) {
			for (int j = -2; j <= 2; ++j) {
				float f = 10.0F / MathHelper.sqrt_float((float) (i * i + j * j) + 0.2F);
				this.parabolicField[i + 2 + (j + 2) * 5] = f;
			}
		}

		if (parString1 != null) {
			this.settings = ChunkProviderSettings.Factory.jsonToFactory(parString1).func_177864_b();
			this.field_177476_s = this.settings.useLavaOceans ? Blocks.lava : Blocks.water;
			worldIn.func_181544_b(this.settings.seaLevel);
		}

	}

	public void setBlocksInChunk(int parInt1, int parInt2, ChunkPrimer parChunkPrimer) {
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration,
				parInt1 * 4 - 2, parInt2 * 4 - 2, 10, 10);
		this.func_147423_a(parInt1 * 4, 0, parInt2 * 4);

		for (int i = 0; i < 4; ++i) {
			int j = i * 5;
			int k = (i + 1) * 5;

			for (int l = 0; l < 4; ++l) {
				int i1 = (j + l) * 33;
				int j1 = (j + l + 1) * 33;
				int k1 = (k + l) * 33;
				int l1 = (k + l + 1) * 33;

				for (int i2 = 0; i2 < 32; ++i2) {
					double d0 = 0.125D;
					double d1 = this.field_147434_q[i1 + i2];
					double d2 = this.field_147434_q[j1 + i2];
					double d3 = this.field_147434_q[k1 + i2];
					double d4 = this.field_147434_q[l1 + i2];
					double d5 = (this.field_147434_q[i1 + i2 + 1] - d1) * d0;
					double d6 = (this.field_147434_q[j1 + i2 + 1] - d2) * d0;
					double d7 = (this.field_147434_q[k1 + i2 + 1] - d3) * d0;
					double d8 = (this.field_147434_q[l1 + i2 + 1] - d4) * d0;

					for (int j2 = 0; j2 < 8; ++j2) {
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						for (int k2 = 0; k2 < 4; ++k2) {
							double d14 = 0.25D;
							double d16 = (d11 - d10) * d14;
							double d15 = d10 - d16;

							for (int l2 = 0; l2 < 4; ++l2) {
								if ((d15 += d16) > 0.0D) {
									parChunkPrimer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2,
											Blocks.stone.getDefaultState());
								} else if (i2 * 8 + j2 < this.settings.seaLevel) {
									parChunkPrimer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2,
											this.field_177476_s.getDefaultState());
								}
							}

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}

	}

	public void replaceBlocksForBiome(int parInt1, int parInt2, ChunkPrimer parChunkPrimer,
			BiomeGenBase[] parArrayOfBiomeGenBase) {
		double d0 = 0.03125D;
		this.stoneNoise = this.field_147430_m.func_151599_a(this.stoneNoise, (double) (parInt1 * 16),
				(double) (parInt2 * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);

		for (int i = 0; i < 16; ++i) {
			for (int j = 0; j < 16; ++j) {
				BiomeGenBase biomegenbase = parArrayOfBiomeGenBase[j + i * 16];
				biomegenbase.genTerrainBlocks(this.worldObj, this.rand, parChunkPrimer, parInt1 * 16 + i,
						parInt2 * 16 + j, this.stoneNoise[j + i * 16]);
			}
		}

	}

	/**+
	 * Will return back a chunk, if it doesn't exist and its not a
	 * MP client it will generates all the blocks for the specified
	 * chunk from the map seed and chunk seed
	 */
	public Chunk provideChunk(int i, int j) {
		this.rand.setSeed((long) i * 341873128712L + (long) j * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.setBlocksInChunk(i, j, chunkprimer);
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration,
				i * 16, j * 16, 16, 16);
		this.replaceBlocksForBiome(i, j, chunkprimer, this.biomesForGeneration);
		if (this.settings.useCaves) {
			this.caveGenerator.generate(this, this.worldObj, i, j, chunkprimer);
		}

		if (this.settings.useRavines) {
			this.ravineGenerator.generate(this, this.worldObj, i, j, chunkprimer);
		}

		if (this.settings.useMineShafts && this.mapFeaturesEnabled) {
			this.mineshaftGenerator.generate(this, this.worldObj, i, j, chunkprimer);
		}

		if (this.settings.useVillages && this.mapFeaturesEnabled) {
			this.villageGenerator.generate(this, this.worldObj, i, j, chunkprimer);
		}

		if (this.settings.useStrongholds && this.mapFeaturesEnabled) {
			this.strongholdGenerator.generate(this, this.worldObj, i, j, chunkprimer);
		}

		if (this.settings.useTemples && this.mapFeaturesEnabled) {
			this.scatteredFeatureGenerator.generate(this, this.worldObj, i, j, chunkprimer);
		}

		if (this.settings.useMonuments && this.mapFeaturesEnabled) {
			this.oceanMonumentGenerator.generate(this, this.worldObj, i, j, chunkprimer);
		}

		Chunk chunk = new Chunk(this.worldObj, chunkprimer, i, j);
		byte[] abyte = chunk.getBiomeArray();

		for (int k = 0; k < abyte.length; ++k) {
			abyte[k] = (byte) this.biomesForGeneration[k].biomeID;
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	private void func_147423_a(int parInt1, int parInt2, int parInt3) {
		this.field_147426_g = this.noiseGen6.generateNoiseOctaves(this.field_147426_g, parInt1, parInt3, 5, 5,
				(double) this.settings.depthNoiseScaleX, (double) this.settings.depthNoiseScaleZ,
				(double) this.settings.depthNoiseScaleExponent);
		float f = this.settings.coordinateScale;
		float f1 = this.settings.heightScale;
		this.field_147427_d = this.field_147429_l.generateNoiseOctaves(this.field_147427_d, parInt1, parInt2, parInt3,
				5, 33, 5, (double) (f / this.settings.mainNoiseScaleX), (double) (f1 / this.settings.mainNoiseScaleY),
				(double) (f / this.settings.mainNoiseScaleZ));
		this.field_147428_e = this.field_147431_j.generateNoiseOctaves(this.field_147428_e, parInt1, parInt2, parInt3,
				5, 33, 5, (double) f, (double) f1, (double) f);
		this.field_147425_f = this.field_147432_k.generateNoiseOctaves(this.field_147425_f, parInt1, parInt2, parInt3,
				5, 33, 5, (double) f, (double) f1, (double) f);
		parInt3 = 0;
		parInt1 = 0;
		int i = 0;
		int j = 0;

		for (int k = 0; k < 5; ++k) {
			for (int l = 0; l < 5; ++l) {
				float f2 = 0.0F;
				float f3 = 0.0F;
				float f4 = 0.0F;
				byte b0 = 2;
				BiomeGenBase biomegenbase = this.biomesForGeneration[k + 2 + (l + 2) * 10];

				for (int i1 = -b0; i1 <= b0; ++i1) {
					for (int j1 = -b0; j1 <= b0; ++j1) {
						BiomeGenBase biomegenbase1 = this.biomesForGeneration[k + i1 + 2 + (l + j1 + 2) * 10];
						float f5 = this.settings.biomeDepthOffSet
								+ biomegenbase1.minHeight * this.settings.biomeDepthWeight;
						float f6 = this.settings.biomeScaleOffset
								+ biomegenbase1.maxHeight * this.settings.biomeScaleWeight;
						if (this.field_177475_o == WorldType.AMPLIFIED && f5 > 0.0F) {
							f5 = 1.0F + f5 * 2.0F;
							f6 = 1.0F + f6 * 4.0F;
						}

						float f7 = this.parabolicField[i1 + 2 + (j1 + 2) * 5] / (f5 + 2.0F);
						if (biomegenbase1.minHeight > biomegenbase.minHeight) {
							f7 /= 2.0F;
						}

						f2 += f6 * f7;
						f3 += f5 * f7;
						f4 += f7;
					}
				}

				f2 = f2 / f4;
				f3 = f3 / f4;
				f2 = f2 * 0.9F + 0.1F;
				f3 = (f3 * 4.0F - 1.0F) / 8.0F;
				double d7 = this.field_147426_g[j] / 8000.0D;
				if (d7 < 0.0D) {
					d7 = -d7 * 0.3D;
				}

				d7 = d7 * 3.0D - 2.0D;
				if (d7 < 0.0D) {
					d7 = d7 / 2.0D;
					if (d7 < -1.0D) {
						d7 = -1.0D;
					}

					d7 = d7 / 1.4D;
					d7 = d7 / 2.0D;
				} else {
					if (d7 > 1.0D) {
						d7 = 1.0D;
					}

					d7 = d7 / 8.0D;
				}

				++j;
				double d8 = (double) f3;
				double d9 = (double) f2;
				d8 = d8 + d7 * 0.2D;
				d8 = d8 * (double) this.settings.baseSize / 8.0D;
				double d0 = (double) this.settings.baseSize + d8 * 4.0D;

				for (int k1 = 0; k1 < 33; ++k1) {
					double d1 = ((double) k1 - d0) * (double) this.settings.stretchY * 128.0D / 256.0D / d9;
					if (d1 < 0.0D) {
						d1 *= 4.0D;
					}

					double d2 = this.field_147428_e[i] / (double) this.settings.lowerLimitScale;
					double d3 = this.field_147425_f[i] / (double) this.settings.upperLimitScale;
					double d4 = (this.field_147427_d[i] / 10.0D + 1.0D) / 2.0D;
					double d5 = MathHelper.denormalizeClamp(d2, d3, d4) - d1;
					if (k1 > 29) {
						double d6 = (double) ((float) (k1 - 29) / 3.0F);
						d5 = d5 * (1.0D - d6) + -10.0D * d6;
					}

					this.field_147434_q[i] = d5;
					++i;
				}
			}
		}

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
		BlockFalling.fallInstantly = true;
		int k = i * 16;
		int l = j * 16;
		BlockPos blockpos = new BlockPos(k, 0, l);
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));
		this.rand.setSeed(this.worldObj.getSeed());
		long i1 = this.rand.nextLong() / 2L * 2L + 1L;
		long j1 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long) i * i1 + (long) j * j1 ^ this.worldObj.getSeed());
		boolean flag = false;
		ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);
		if (this.settings.useMineShafts && this.mapFeaturesEnabled) {
			this.mineshaftGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
		}

		if (this.settings.useVillages && this.mapFeaturesEnabled) {
			flag = this.villageGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
		}

		if (this.settings.useStrongholds && this.mapFeaturesEnabled) {
			this.strongholdGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
		}

		if (this.settings.useTemples && this.mapFeaturesEnabled) {
			this.scatteredFeatureGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
		}

		if (this.settings.useMonuments && this.mapFeaturesEnabled) {
			this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
		}

		if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills
				&& this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0) {
			int k1 = this.rand.nextInt(16) + 8;
			int l1 = this.rand.nextInt(256);
			int i2 = this.rand.nextInt(16) + 8;
			(new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.rand, blockpos.add(k1, l1, i2));
		}

		if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes) {
			int k2 = this.rand.nextInt(16) + 8;
			int j3 = this.rand.nextInt(this.rand.nextInt(248) + 8);
			int i4 = this.rand.nextInt(16) + 8;
			if (j3 < this.worldObj.func_181545_F() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0) {
				(new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.rand, blockpos.add(k2, j3, i4));
			}
		}

		if (this.settings.useDungeons) {
			for (int l2 = 0; l2 < this.settings.dungeonChance; ++l2) {
				int k3 = this.rand.nextInt(16) + 8;
				int j4 = this.rand.nextInt(256);
				int j2 = this.rand.nextInt(16) + 8;
				(new WorldGenDungeons()).generate(this.worldObj, this.rand, blockpos.add(k3, j4, j2));
			}
		}

		biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(k, 0, l));
		SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
		blockpos = blockpos.add(8, 0, 8);

		for (int i3 = 0; i3 < 16; ++i3) {
			for (int l3 = 0; l3 < 16; ++l3) {
				BlockPos blockpos1 = this.worldObj.getPrecipitationHeight(blockpos.add(i3, 0, l3));
				BlockPos blockpos2 = blockpos1.down();
				if (this.worldObj.canBlockFreezeWater(blockpos2)) {
					this.worldObj.setBlockState(blockpos2, Blocks.ice.getDefaultState(), 2);
				}

				if (this.worldObj.canSnowAt(blockpos1, true)) {
					this.worldObj.setBlockState(blockpos1, Blocks.snow_layer.getDefaultState(), 2);
				}
			}
		}

		BlockFalling.fallInstantly = false;
	}

	public boolean func_177460_a(IChunkProvider var1, Chunk chunk, int i, int j) {
		boolean flag = false;
		if (this.settings.useMonuments && this.mapFeaturesEnabled && chunk.getInhabitedTime() < 3600L) {
			flag |= this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand,
					new ChunkCoordIntPair(i, j));
		}

		return flag;
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
		return "RandomLevelSource";
	}

	public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype,
			BlockPos blockpos) {
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos);
		if (this.mapFeaturesEnabled) {
			if (enumcreaturetype == EnumCreatureType.MONSTER
					&& this.scatteredFeatureGenerator.func_175798_a(blockpos)) {
				return this.scatteredFeatureGenerator.getScatteredFeatureSpawnList();
			}

			if (enumcreaturetype == EnumCreatureType.MONSTER && this.settings.useMonuments
					&& this.oceanMonumentGenerator.func_175796_a(this.worldObj, blockpos)) {
				return this.oceanMonumentGenerator.func_175799_b();
			}
		}

		return biomegenbase.getSpawnableList(enumcreaturetype);
	}

	public BlockPos getStrongholdGen(World world, String s, BlockPos blockpos) {
		return "Stronghold".equals(s) && this.strongholdGenerator != null
				? this.strongholdGenerator.getClosestStrongholdPos(world, blockpos)
				: null;
	}

	public int getLoadedChunkCount() {
		return 0;
	}

	public void recreateStructures(Chunk var1, int i, int j) {
		if (this.settings.useMineShafts && this.mapFeaturesEnabled) {
			this.mineshaftGenerator.generate(this, this.worldObj, i, j, (ChunkPrimer) null);
		}

		if (this.settings.useVillages && this.mapFeaturesEnabled) {
			this.villageGenerator.generate(this, this.worldObj, i, j, (ChunkPrimer) null);
		}

		if (this.settings.useStrongholds && this.mapFeaturesEnabled) {
			this.strongholdGenerator.generate(this, this.worldObj, i, j, (ChunkPrimer) null);
		}

		if (this.settings.useTemples && this.mapFeaturesEnabled) {
			this.scatteredFeatureGenerator.generate(this, this.worldObj, i, j, (ChunkPrimer) null);
		}

		if (this.settings.useMonuments && this.mapFeaturesEnabled) {
			this.oceanMonumentGenerator.generate(this, this.worldObj, i, j, (ChunkPrimer) null);
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