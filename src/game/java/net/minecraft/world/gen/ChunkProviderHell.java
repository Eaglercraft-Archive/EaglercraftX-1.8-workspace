package net.minecraft.world.gen;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenNetherBridge;

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
public class ChunkProviderHell implements IChunkProvider {
	private final World worldObj;
	private final boolean field_177466_i;
	private final EaglercraftRandom hellRNG;
	/**+
	 * Holds the noise used to determine whether slowsand can be
	 * generated at a location
	 */
	private double[] slowsandNoise = new double[256];
	private double[] gravelNoise = new double[256];
	/**+
	 * Holds the noise used to determine whether something other
	 * than netherrack can be generated at a location
	 */
	private double[] netherrackExclusivityNoise = new double[256];
	private double[] noiseField;
	private final NoiseGeneratorOctaves netherNoiseGen1;
	private final NoiseGeneratorOctaves netherNoiseGen2;
	private final NoiseGeneratorOctaves netherNoiseGen3;
	private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
	private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
	public final NoiseGeneratorOctaves netherNoiseGen6;
	public final NoiseGeneratorOctaves netherNoiseGen7;
	private final WorldGenFire field_177470_t = new WorldGenFire();
	private final WorldGenGlowStone1 field_177469_u = new WorldGenGlowStone1();
	private final WorldGenGlowStone2 field_177468_v = new WorldGenGlowStone2();
	private final WorldGenerator field_177467_w = new WorldGenMinable(Blocks.quartz_ore.getDefaultState(), 14,
			BlockHelper.forBlock(Blocks.netherrack));
	private final WorldGenHellLava field_177473_x = new WorldGenHellLava(Blocks.flowing_lava, true);
	private final WorldGenHellLava field_177472_y = new WorldGenHellLava(Blocks.flowing_lava, false);
	private final GeneratorBushFeature field_177471_z = new GeneratorBushFeature(Blocks.brown_mushroom);
	private final GeneratorBushFeature field_177465_A = new GeneratorBushFeature(Blocks.red_mushroom);
	private final MapGenNetherBridge genNetherBridge;
	private final MapGenBase netherCaveGenerator;
	double[] noiseData1;
	double[] noiseData2;
	double[] noiseData3;
	double[] noiseData4;
	double[] noiseData5;

	public ChunkProviderHell(World worldIn, boolean parFlag, long parLong1) {
		this.worldObj = worldIn;
		this.field_177466_i = parFlag;
		boolean scramble = !worldIn.getWorldInfo().isOldEaglercraftRandom();
		this.hellRNG = new EaglercraftRandom(parLong1, scramble);
		this.genNetherBridge = new MapGenNetherBridge(scramble);
		this.netherCaveGenerator = new MapGenCavesHell(scramble);
		this.netherNoiseGen1 = new NoiseGeneratorOctaves(this.hellRNG, 16);
		this.netherNoiseGen2 = new NoiseGeneratorOctaves(this.hellRNG, 16);
		this.netherNoiseGen3 = new NoiseGeneratorOctaves(this.hellRNG, 8);
		this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
		this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
		this.netherNoiseGen6 = new NoiseGeneratorOctaves(this.hellRNG, 10);
		this.netherNoiseGen7 = new NoiseGeneratorOctaves(this.hellRNG, 16);
		worldIn.func_181544_b(63);
	}

	public void func_180515_a(int parInt1, int parInt2, ChunkPrimer parChunkPrimer) {
		byte b0 = 4;
		int i = this.worldObj.func_181545_F() / 2 + 1;
		int j = b0 + 1;
		byte b1 = 17;
		int k = b0 + 1;
		this.noiseField = this.initializeNoiseField(this.noiseField, parInt1 * b0, 0, parInt2 * b0, j, b1, k);

		for (int l = 0; l < b0; ++l) {
			for (int i1 = 0; i1 < b0; ++i1) {
				for (int j1 = 0; j1 < 16; ++j1) {
					double d0 = 0.125D;
					double d1 = this.noiseField[((l + 0) * k + i1 + 0) * b1 + j1 + 0];
					double d2 = this.noiseField[((l + 0) * k + i1 + 1) * b1 + j1 + 0];
					double d3 = this.noiseField[((l + 1) * k + i1 + 0) * b1 + j1 + 0];
					double d4 = this.noiseField[((l + 1) * k + i1 + 1) * b1 + j1 + 0];
					double d5 = (this.noiseField[((l + 0) * k + i1 + 0) * b1 + j1 + 1] - d1) * d0;
					double d6 = (this.noiseField[((l + 0) * k + i1 + 1) * b1 + j1 + 1] - d2) * d0;
					double d7 = (this.noiseField[((l + 1) * k + i1 + 0) * b1 + j1 + 1] - d3) * d0;
					double d8 = (this.noiseField[((l + 1) * k + i1 + 1) * b1 + j1 + 1] - d4) * d0;

					for (int k1 = 0; k1 < 8; ++k1) {
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						for (int l1 = 0; l1 < 4; ++l1) {
							double d14 = 0.25D;
							double d15 = d10;
							double d16 = (d11 - d10) * d14;

							for (int i2 = 0; i2 < 4; ++i2) {
								IBlockState iblockstate = null;
								if (j1 * 8 + k1 < i) {
									iblockstate = Blocks.lava.getDefaultState();
								}

								if (d15 > 0.0D) {
									iblockstate = Blocks.netherrack.getDefaultState();
								}

								int j2 = l1 + l * 4;
								int k2 = k1 + j1 * 8;
								int l2 = i2 + i1 * 4;
								parChunkPrimer.setBlockState(j2, k2, l2, iblockstate);
								d15 += d16;
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

	public void func_180516_b(int parInt1, int parInt2, ChunkPrimer parChunkPrimer) {
		int i = this.worldObj.func_181545_F() + 1;
		double d0 = 0.03125D;
		this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, parInt1 * 16,
				parInt2 * 16, 0, 16, 16, 1, d0, d0, 1.0D);
		this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, parInt1 * 16, 109,
				parInt2 * 16, 16, 1, 16, d0, 1.0D, d0);
		this.netherrackExclusivityNoise = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(
				this.netherrackExclusivityNoise, parInt1 * 16, parInt2 * 16, 0, 16, 16, 1, d0 * 2.0D, d0 * 2.0D,
				d0 * 2.0D);

		for (int j = 0; j < 16; ++j) {
			for (int k = 0; k < 16; ++k) {
				boolean flag = this.slowsandNoise[j + k * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
				boolean flag1 = this.gravelNoise[j + k * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
				int l = (int) (this.netherrackExclusivityNoise[j + k * 16] / 3.0D + 3.0D
						+ this.hellRNG.nextDouble() * 0.25D);
				int i1 = -1;
				IBlockState iblockstate = Blocks.netherrack.getDefaultState();
				IBlockState iblockstate1 = Blocks.netherrack.getDefaultState();

				for (int j1 = 127; j1 >= 0; --j1) {
					if (j1 < 127 - this.hellRNG.nextInt(5) && j1 > this.hellRNG.nextInt(5)) {
						IBlockState iblockstate2 = parChunkPrimer.getBlockState(k, j1, j);
						if (iblockstate2.getBlock() != null && iblockstate2.getBlock().getMaterial() != Material.air) {
							if (iblockstate2.getBlock() == Blocks.netherrack) {
								if (i1 == -1) {
									if (l <= 0) {
										iblockstate = null;
										iblockstate1 = Blocks.netherrack.getDefaultState();
									} else if (j1 >= i - 4 && j1 <= i + 1) {
										iblockstate = Blocks.netherrack.getDefaultState();
										iblockstate1 = Blocks.netherrack.getDefaultState();
										if (flag1) {
											iblockstate = Blocks.gravel.getDefaultState();
											iblockstate1 = Blocks.netherrack.getDefaultState();
										}

										if (flag) {
											iblockstate = Blocks.soul_sand.getDefaultState();
											iblockstate1 = Blocks.soul_sand.getDefaultState();
										}
									}

									if (j1 < i && (iblockstate == null
											|| iblockstate.getBlock().getMaterial() == Material.air)) {
										iblockstate = Blocks.lava.getDefaultState();
									}

									i1 = l;
									if (j1 >= i - 1) {
										parChunkPrimer.setBlockState(k, j1, j, iblockstate);
									} else {
										parChunkPrimer.setBlockState(k, j1, j, iblockstate1);
									}
								} else if (i1 > 0) {
									--i1;
									parChunkPrimer.setBlockState(k, j1, j, iblockstate1);
								}
							}
						} else {
							i1 = -1;
						}
					} else {
						parChunkPrimer.setBlockState(k, j1, j, Blocks.bedrock.getDefaultState());
					}
				}
			}
		}

	}

	/**+
	 * Will return back a chunk, if it doesn't exist and its not a
	 * MP client it will generates all the blocks for the specified
	 * chunk from the map seed and chunk seed
	 */
	public Chunk provideChunk(int i, int j) {
		this.hellRNG.setSeed((long) i * 341873128712L + (long) j * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.func_180515_a(i, j, chunkprimer);
		this.func_180516_b(i, j, chunkprimer);
		this.netherCaveGenerator.generate(this, this.worldObj, i, j, chunkprimer);
		if (this.field_177466_i) {
			this.genNetherBridge.generate(this, this.worldObj, i, j, chunkprimer);
		}

		Chunk chunk = new Chunk(this.worldObj, chunkprimer, i, j);
		BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager()
				.loadBlockGeneratorData((BiomeGenBase[]) null, i * 16, j * 16, 16, 16);
		byte[] abyte = chunk.getBiomeArray();

		for (int k = 0; k < abyte.length; ++k) {
			abyte[k] = (byte) abiomegenbase[k].biomeID;
		}

		chunk.resetRelightChecks();
		return chunk;
	}

	/**+
	 * generates a subset of the level's terrain data. Takes 7
	 * arguments: the [empty] noise array, the position, and the
	 * size.
	 */
	private double[] initializeNoiseField(double[] parArrayOfDouble, int parInt1, int parInt2, int parInt3, int parInt4,
			int parInt5, int parInt6) {
		if (parArrayOfDouble == null) {
			parArrayOfDouble = new double[parInt4 * parInt5 * parInt6];
		}

		double d0 = 684.412D;
		double d1 = 2053.236D;
		this.noiseData4 = this.netherNoiseGen6.generateNoiseOctaves(this.noiseData4, parInt1, parInt2, parInt3, parInt4,
				1, parInt6, 1.0D, 0.0D, 1.0D);
		this.noiseData5 = this.netherNoiseGen7.generateNoiseOctaves(this.noiseData5, parInt1, parInt2, parInt3, parInt4,
				1, parInt6, 100.0D, 0.0D, 100.0D);
		this.noiseData1 = this.netherNoiseGen3.generateNoiseOctaves(this.noiseData1, parInt1, parInt2, parInt3, parInt4,
				parInt5, parInt6, d0 / 80.0D, d1 / 60.0D, d0 / 80.0D);
		this.noiseData2 = this.netherNoiseGen1.generateNoiseOctaves(this.noiseData2, parInt1, parInt2, parInt3, parInt4,
				parInt5, parInt6, d0, d1, d0);
		this.noiseData3 = this.netherNoiseGen2.generateNoiseOctaves(this.noiseData3, parInt1, parInt2, parInt3, parInt4,
				parInt5, parInt6, d0, d1, d0);
		int i = 0;
		double[] adouble = new double[parInt5];

		for (int j = 0; j < parInt5; ++j) {
			adouble[j] = Math.cos((double) j * 3.141592653589793D * 6.0D / (double) parInt5) * 2.0D;
			double d2 = (double) j;
			if (j > parInt5 / 2) {
				d2 = (double) (parInt5 - 1 - j);
			}

			if (d2 < 4.0D) {
				d2 = 4.0D - d2;
				adouble[j] -= d2 * d2 * d2 * 10.0D;
			}
		}

		for (int l = 0; l < parInt4; ++l) {
			for (int i1 = 0; i1 < parInt6; ++i1) {
				double d3 = 0.0D;

				for (int k = 0; k < parInt5; ++k) {
					double d4 = 0.0D;
					double d5 = adouble[k];
					double d6 = this.noiseData2[i] / 512.0D;
					double d7 = this.noiseData3[i] / 512.0D;
					double d8 = (this.noiseData1[i] / 10.0D + 1.0D) / 2.0D;
					if (d8 < 0.0D) {
						d4 = d6;
					} else if (d8 > 1.0D) {
						d4 = d7;
					} else {
						d4 = d6 + (d7 - d6) * d8;
					}

					d4 = d4 - d5;
					if (k > parInt5 - 4) {
						double d9 = (double) ((float) (k - (parInt5 - 4)) / 3.0F);
						d4 = d4 * (1.0D - d9) + -10.0D * d9;
					}

					if ((double) k < d3) {
						double d10 = (d3 - (double) k) / 4.0D;
						d10 = MathHelper.clamp_double(d10, 0.0D, 1.0D);
						d4 = d4 * (1.0D - d10) + -10.0D * d10;
					}

					parArrayOfDouble[i] = d4;
					++i;
				}
			}
		}

		return parArrayOfDouble;
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
		BlockPos blockpos = new BlockPos(i * 16, 0, j * 16);
		ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);
		this.genNetherBridge.generateStructure(this.worldObj, this.hellRNG, chunkcoordintpair);

		for (int k = 0; k < 8; ++k) {
			this.field_177472_y.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8,
					this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
		}

		for (int l = 0; l < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1) + 1; ++l) {
			this.field_177470_t.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8,
					this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
		}

		for (int i1 = 0; i1 < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1); ++i1) {
			this.field_177469_u.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8,
					this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
		}

		for (int j1 = 0; j1 < 10; ++j1) {
			this.field_177468_v.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8,
					this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
		}

		if (this.hellRNG.nextBoolean()) {
			this.field_177471_z.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8,
					this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
		}

		if (this.hellRNG.nextBoolean()) {
			this.field_177465_A.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8,
					this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
		}

		for (int k1 = 0; k1 < 16; ++k1) {
			this.field_177467_w.generate(this.worldObj, this.hellRNG,
					blockpos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
		}

		for (int l1 = 0; l1 < 16; ++l1) {
			this.field_177473_x.generate(this.worldObj, this.hellRNG,
					blockpos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
		}

		BlockFalling.fallInstantly = false;
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
		return "HellRandomLevelSource";
	}

	public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype,
			BlockPos blockpos) {
		if (enumcreaturetype == EnumCreatureType.MONSTER) {
			if (this.genNetherBridge.func_175795_b(blockpos)) {
				return this.genNetherBridge.getSpawnList();
			}

			if (this.genNetherBridge.func_175796_a(this.worldObj, blockpos)
					&& this.worldObj.getBlockState(blockpos.down()).getBlock() == Blocks.nether_brick) {
				return this.genNetherBridge.getSpawnList();
			}
		}

		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos);
		return biomegenbase.getSpawnableList(enumcreaturetype);
	}

	public BlockPos getStrongholdGen(World var1, String var2, BlockPos var3) {
		return null;
	}

	public int getLoadedChunkCount() {
		return 0;
	}

	public void recreateStructures(Chunk var1, int i, int j) {
		this.genNetherBridge.generate(this, this.worldObj, i, j, (ChunkPrimer) null);
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