package net.minecraft.world.biome;

import java.util.Arrays;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

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
public class BiomeGenMesa extends BiomeGenBase {
	private IBlockState[] field_150621_aC;
	private long field_150622_aD;
	private NoiseGeneratorPerlin field_150623_aE;
	private NoiseGeneratorPerlin field_150624_aF;
	private NoiseGeneratorPerlin field_150625_aG;
	private boolean field_150626_aH;
	private boolean field_150620_aI;

	public BiomeGenMesa(int parInt1, boolean parFlag, boolean parFlag2) {
		super(parInt1);
		this.field_150626_aH = parFlag;
		this.field_150620_aI = parFlag2;
		this.setDisableRain();
		this.setTemperatureRainfall(2.0F, 0.0F);
		this.spawnableCreatureList.clear();
		this.topBlock = Blocks.sand.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
		this.fillerBlock = Blocks.stained_hardened_clay.getDefaultState();
		this.theBiomeDecorator.treesPerChunk = -999;
		this.theBiomeDecorator.deadBushPerChunk = 20;
		this.theBiomeDecorator.reedsPerChunk = 3;
		this.theBiomeDecorator.cactiPerChunk = 5;
		this.theBiomeDecorator.flowersPerChunk = 0;
		this.spawnableCreatureList.clear();
		if (parFlag2) {
			this.theBiomeDecorator.treesPerChunk = 5;
		}

	}

	public WorldGenAbstractTree genBigTreeChance(EaglercraftRandom var1) {
		return this.worldGeneratorTrees;
	}

	public int getFoliageColorAtPos(BlockPos var1) {
		return 10387789;
	}

	public int getGrassColorAtPos(BlockPos var1) {
		return 9470285;
	}

	public void decorate(World world, EaglercraftRandom random, BlockPos blockpos) {
		super.decorate(world, random, blockpos);
	}

	public void genTerrainBlocks(World world, EaglercraftRandom random, ChunkPrimer chunkprimer, int i, int j,
			double d0) {
		if (this.field_150621_aC == null || this.field_150622_aD != world.getSeed()) {
			this.func_150619_a(world.getSeed(), !world.getWorldInfo().isOldEaglercraftRandom());
		}

		if (this.field_150623_aE == null || this.field_150624_aF == null || this.field_150622_aD != world.getSeed()) {
			EaglercraftRandom random1 = new EaglercraftRandom(this.field_150622_aD,
					!world.getWorldInfo().isOldEaglercraftRandom());
			this.field_150623_aE = new NoiseGeneratorPerlin(random1, 4);
			this.field_150624_aF = new NoiseGeneratorPerlin(random1, 1);
		}

		this.field_150622_aD = world.getSeed();
		double d5 = 0.0D;
		if (this.field_150626_aH) {
			int k = (i & -16) + (j & 15);
			int l = (j & -16) + (i & 15);
			double d1 = Math.min(Math.abs(d0),
					this.field_150623_aE.func_151601_a((double) k * 0.25D, (double) l * 0.25D));
			if (d1 > 0.0D) {
				double d2 = 0.001953125D;
				double d3 = Math.abs(this.field_150624_aF.func_151601_a((double) k * d2, (double) l * d2));
				d5 = d1 * d1 * 2.5D;
				double d4 = Math.ceil(d3 * 50.0D) + 14.0D;
				if (d5 > d4) {
					d5 = d4;
				}

				d5 = d5 + 64.0D;
			}
		}

		int l1 = i & 15;
		int i2 = j & 15;
		int j2 = world.func_181545_F();
		IBlockState iblockstate = Blocks.stained_hardened_clay.getDefaultState();
		IBlockState iblockstate3 = this.fillerBlock;
		int i1 = (int) (d0 / 3.0D + 3.0D + random.nextDouble() * 0.25D);
		boolean flag = Math.cos(d0 / 3.0D * 3.141592653589793D) > 0.0D;
		int j1 = -1;
		boolean flag1 = false;

		for (int k1 = 255; k1 >= 0; --k1) {
			if (chunkprimer.getBlockState(i2, k1, l1).getBlock().getMaterial() == Material.air && k1 < (int) d5) {
				chunkprimer.setBlockState(i2, k1, l1, Blocks.stone.getDefaultState());
			}

			if (k1 <= random.nextInt(5)) {
				chunkprimer.setBlockState(i2, k1, l1, Blocks.bedrock.getDefaultState());
			} else {
				IBlockState iblockstate1 = chunkprimer.getBlockState(i2, k1, l1);
				if (iblockstate1.getBlock().getMaterial() == Material.air) {
					j1 = -1;
				} else if (iblockstate1.getBlock() == Blocks.stone) {
					if (j1 == -1) {
						flag1 = false;
						if (i1 <= 0) {
							iblockstate = null;
							iblockstate3 = Blocks.stone.getDefaultState();
						} else if (k1 >= j2 - 4 && k1 <= j2 + 1) {
							iblockstate = Blocks.stained_hardened_clay.getDefaultState();
							iblockstate3 = this.fillerBlock;
						}

						if (k1 < j2 && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air)) {
							iblockstate = Blocks.water.getDefaultState();
						}

						j1 = i1 + Math.max(0, k1 - j2);
						if (k1 < j2 - 1) {
							chunkprimer.setBlockState(i2, k1, l1, iblockstate3);
							if (iblockstate3.getBlock() == Blocks.stained_hardened_clay) {
								chunkprimer.setBlockState(i2, k1, l1, iblockstate3.getBlock().getDefaultState()
										.withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
							}
						} else if (this.field_150620_aI && k1 > 86 + i1 * 2) {
							if (flag) {
								chunkprimer.setBlockState(i2, k1, l1, Blocks.dirt.getDefaultState()
										.withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
							} else {
								chunkprimer.setBlockState(i2, k1, l1, Blocks.grass.getDefaultState());
							}
						} else if (k1 <= j2 + 3 + i1) {
							chunkprimer.setBlockState(i2, k1, l1, this.topBlock);
							flag1 = true;
						} else {
							IBlockState iblockstate4;
							if (k1 >= 64 && k1 <= 127) {
								if (flag) {
									iblockstate4 = Blocks.hardened_clay.getDefaultState();
								} else {
									iblockstate4 = this.func_180629_a(i, k1, j);
								}
							} else {
								iblockstate4 = Blocks.stained_hardened_clay.getDefaultState()
										.withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
							}

							chunkprimer.setBlockState(i2, k1, l1, iblockstate4);
						}
					} else if (j1 > 0) {
						--j1;
						if (flag1) {
							chunkprimer.setBlockState(i2, k1, l1, Blocks.stained_hardened_clay.getDefaultState()
									.withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
						} else {
							IBlockState iblockstate2 = this.func_180629_a(i, k1, j);
							chunkprimer.setBlockState(i2, k1, l1, iblockstate2);
						}
					}
				}
			}
		}

	}

	private void func_150619_a(long parLong1, boolean scrambleRNG) {
		this.field_150621_aC = new IBlockState[64];
		Arrays.fill(this.field_150621_aC, Blocks.hardened_clay.getDefaultState());
		EaglercraftRandom random = new EaglercraftRandom(parLong1, scrambleRNG);
		this.field_150625_aG = new NoiseGeneratorPerlin(random, 1);

		for (int l1 = 0; l1 < 64; ++l1) {
			l1 += random.nextInt(5) + 1;
			if (l1 < 64) {
				this.field_150621_aC[l1] = Blocks.stained_hardened_clay.getDefaultState()
						.withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
			}
		}

		int i2 = random.nextInt(4) + 2;

		for (int i = 0; i < i2; ++i) {
			int j = random.nextInt(3) + 1;
			int k = random.nextInt(64);

			for (int l = 0; k + l < 64 && l < j; ++l) {
				this.field_150621_aC[k + l] = Blocks.stained_hardened_clay.getDefaultState()
						.withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
			}
		}

		int j2 = random.nextInt(4) + 2;

		for (int k2 = 0; k2 < j2; ++k2) {
			int i3 = random.nextInt(3) + 2;
			int l3 = random.nextInt(64);

			for (int i1 = 0; l3 + i1 < 64 && i1 < i3; ++i1) {
				this.field_150621_aC[l3 + i1] = Blocks.stained_hardened_clay.getDefaultState()
						.withProperty(BlockColored.COLOR, EnumDyeColor.BROWN);
			}
		}

		int l2 = random.nextInt(4) + 2;

		for (int j3 = 0; j3 < l2; ++j3) {
			int i4 = random.nextInt(3) + 1;
			int k4 = random.nextInt(64);

			for (int j1 = 0; k4 + j1 < 64 && j1 < i4; ++j1) {
				this.field_150621_aC[k4 + j1] = Blocks.stained_hardened_clay.getDefaultState()
						.withProperty(BlockColored.COLOR, EnumDyeColor.RED);
			}
		}

		int k3 = random.nextInt(3) + 3;
		int j4 = 0;

		for (int l4 = 0; l4 < k3; ++l4) {
			byte b0 = 1;
			j4 += random.nextInt(16) + 4;

			for (int k1 = 0; j4 + k1 < 64 && k1 < b0; ++k1) {
				this.field_150621_aC[j4 + k1] = Blocks.stained_hardened_clay.getDefaultState()
						.withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
				if (j4 + k1 > 1 && random.nextBoolean()) {
					this.field_150621_aC[j4 + k1 - 1] = Blocks.stained_hardened_clay.getDefaultState()
							.withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
				}

				if (j4 + k1 < 63 && random.nextBoolean()) {
					this.field_150621_aC[j4 + k1 + 1] = Blocks.stained_hardened_clay.getDefaultState()
							.withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
				}
			}
		}

	}

	private IBlockState func_180629_a(int parInt1, int parInt2, int parInt3) {
		int i = (int) Math.round(
				this.field_150625_aG.func_151601_a((double) parInt1 * 1.0D / 512.0D, (double) parInt1 * 1.0D / 512.0D)
						* 2.0D);
		return this.field_150621_aC[(parInt2 + i + 64) % 64];
	}

	protected BiomeGenBase createMutatedBiome(int i) {
		boolean flag = this.biomeID == BiomeGenBase.mesa.biomeID;
		BiomeGenMesa biomegenmesa = new BiomeGenMesa(i, flag, this.field_150620_aI);
		if (!flag) {
			biomegenmesa.setHeight(height_LowHills);
			biomegenmesa.setBiomeName(this.biomeName + " M");
		} else {
			biomegenmesa.setBiomeName(this.biomeName + " (Bryce)");
		}

		biomegenmesa.func_150557_a(this.color, true);
		return biomegenmesa;
	}
}