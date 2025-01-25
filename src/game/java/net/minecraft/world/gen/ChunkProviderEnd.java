package net.minecraft.world.gen;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

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
public class ChunkProviderEnd implements IChunkProvider {
	private EaglercraftRandom endRNG;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	public NoiseGeneratorOctaves noiseGen4;
	public NoiseGeneratorOctaves noiseGen5;
	private World endWorld;
	private double[] densities;
	private BiomeGenBase[] biomesForGeneration;
	double[] noiseData1;
	double[] noiseData2;
	double[] noiseData3;
	double[] noiseData4;
	double[] noiseData5;

	public ChunkProviderEnd(World worldIn, long parLong1) {
		this.endWorld = worldIn;
		this.endRNG = new EaglercraftRandom(parLong1, !worldIn.getWorldInfo().isOldEaglercraftRandom());
		this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
	}

	public void func_180520_a(int parInt1, int parInt2, ChunkPrimer parChunkPrimer) {
		byte b0 = 2;
		int i = b0 + 1;
		byte b1 = 33;
		int j = b0 + 1;
		this.densities = this.initializeNoiseField(this.densities, parInt1 * b0, 0, parInt2 * b0, i, b1, j);

		for (int k = 0; k < b0; ++k) {
			for (int l = 0; l < b0; ++l) {
				for (int i1 = 0; i1 < 32; ++i1) {
					double d0 = 0.25D;
					double d1 = this.densities[((k + 0) * j + l + 0) * b1 + i1 + 0];
					double d2 = this.densities[((k + 0) * j + l + 1) * b1 + i1 + 0];
					double d3 = this.densities[((k + 1) * j + l + 0) * b1 + i1 + 0];
					double d4 = this.densities[((k + 1) * j + l + 1) * b1 + i1 + 0];
					double d5 = (this.densities[((k + 0) * j + l + 0) * b1 + i1 + 1] - d1) * d0;
					double d6 = (this.densities[((k + 0) * j + l + 1) * b1 + i1 + 1] - d2) * d0;
					double d7 = (this.densities[((k + 1) * j + l + 0) * b1 + i1 + 1] - d3) * d0;
					double d8 = (this.densities[((k + 1) * j + l + 1) * b1 + i1 + 1] - d4) * d0;

					for (int j1 = 0; j1 < 4; ++j1) {
						double d9 = 0.125D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						for (int k1 = 0; k1 < 8; ++k1) {
							double d14 = 0.125D;
							double d15 = d10;
							double d16 = (d11 - d10) * d14;

							for (int l1 = 0; l1 < 8; ++l1) {
								IBlockState iblockstate = null;
								if (d15 > 0.0D) {
									iblockstate = Blocks.end_stone.getDefaultState();
								}

								int i2 = k1 + k * 8;
								int j2 = j1 + i1 * 4;
								int k2 = l1 + l * 8;
								parChunkPrimer.setBlockState(i2, j2, k2, iblockstate);
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

	public void func_180519_a(ChunkPrimer parChunkPrimer) {
		for (int i = 0; i < 16; ++i) {
			for (int j = 0; j < 16; ++j) {
				byte b0 = 1;
				int k = -1;
				IBlockState iblockstate = Blocks.end_stone.getDefaultState();
				IBlockState iblockstate1 = Blocks.end_stone.getDefaultState();

				for (int l = 127; l >= 0; --l) {
					IBlockState iblockstate2 = parChunkPrimer.getBlockState(i, l, j);
					if (iblockstate2.getBlock().getMaterial() == Material.air) {
						k = -1;
					} else if (iblockstate2.getBlock() == Blocks.stone) {
						if (k == -1) {
							if (b0 <= 0) {
								iblockstate = Blocks.air.getDefaultState();
								iblockstate1 = Blocks.end_stone.getDefaultState();
							}

							k = b0;
							if (l >= 0) {
								parChunkPrimer.setBlockState(i, l, j, iblockstate);
							} else {
								parChunkPrimer.setBlockState(i, l, j, iblockstate1);
							}
						} else if (k > 0) {
							--k;
							parChunkPrimer.setBlockState(i, l, j, iblockstate1);
						}
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
		this.endRNG.setSeed((long) i * 341873128712L + (long) j * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration,
				i * 16, j * 16, 16, 16);
		this.func_180520_a(i, j, chunkprimer);
		this.func_180519_a(chunkprimer);
		Chunk chunk = new Chunk(this.endWorld, chunkprimer, i, j);
		byte[] abyte = chunk.getBiomeArray();

		for (int k = 0; k < abyte.length; ++k) {
			abyte[k] = (byte) this.biomesForGeneration[k].biomeID;
		}

		chunk.generateSkylightMap();
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
		double d1 = 684.412D;
		this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, parInt1, parInt3, parInt4, parInt6,
				1.121D, 1.121D, 0.5D);
		this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, parInt1, parInt3, parInt4, parInt6,
				200.0D, 200.0D, 0.5D);
		d0 = d0 * 2.0D;
		this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, parInt1, parInt2, parInt3, parInt4,
				parInt5, parInt6, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
		this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, parInt1, parInt2, parInt3, parInt4,
				parInt5, parInt6, d0, d1, d0);
		this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, parInt1, parInt2, parInt3, parInt4,
				parInt5, parInt6, d0, d1, d0);
		int i = 0;

		for (int j = 0; j < parInt4; ++j) {
			for (int k = 0; k < parInt6; ++k) {
				float f = (float) (j + parInt1) / 1.0F;
				float f1 = (float) (k + parInt3) / 1.0F;
				float f2 = 100.0F - MathHelper.sqrt_float(f * f + f1 * f1) * 8.0F;
				if (f2 > 80.0F) {
					f2 = 80.0F;
				}

				if (f2 < -100.0F) {
					f2 = -100.0F;
				}

				for (int l = 0; l < parInt5; ++l) {
					double d2 = 0.0D;
					double d3 = this.noiseData2[i] / 512.0D;
					double d4 = this.noiseData3[i] / 512.0D;
					double d5 = (this.noiseData1[i] / 10.0D + 1.0D) / 2.0D;
					if (d5 < 0.0D) {
						d2 = d3;
					} else if (d5 > 1.0D) {
						d2 = d4;
					} else {
						d2 = d3 + (d4 - d3) * d5;
					}

					d2 = d2 - 8.0D;
					d2 = d2 + (double) f2;
					byte b0 = 2;
					if (l > parInt5 / 2 - b0) {
						double d6 = (double) ((float) (l - (parInt5 / 2 - b0)) / 64.0F);
						d6 = MathHelper.clamp_double(d6, 0.0D, 1.0D);
						d2 = d2 * (1.0D - d6) + -3000.0D * d6;
					}

					b0 = 8;
					if (l < b0) {
						double d7 = (double) ((float) (b0 - l) / ((float) b0 - 1.0F));
						d2 = d2 * (1.0D - d7) + -30.0D * d7;
					}

					parArrayOfDouble[i] = d2;
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
		this.endWorld.getBiomeGenForCoords(blockpos.add(16, 0, 16)).decorate(this.endWorld, this.endWorld.rand,
				blockpos);
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
		return "RandomLevelSource";
	}

	public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype,
			BlockPos blockpos) {
		return this.endWorld.getBiomeGenForCoords(blockpos).getSpawnableList(enumcreaturetype);
	}

	public BlockPos getStrongholdGen(World var1, String var2, BlockPos var3) {
		return null;
	}

	public int getLoadedChunkCount() {
		return 0;
	}

	public void recreateStructures(Chunk var1, int var2, int var3) {
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