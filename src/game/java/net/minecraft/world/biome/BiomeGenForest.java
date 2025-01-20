package net.minecraft.world.biome;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;

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
public class BiomeGenForest extends BiomeGenBase {
	private int field_150632_aF;
	protected static final WorldGenForest field_150629_aC = new WorldGenForest(false, true);
	protected static final WorldGenForest field_150630_aD = new WorldGenForest(false, false);
	protected static final WorldGenCanopyTree field_150631_aE = new WorldGenCanopyTree(false);

	public BiomeGenForest(int parInt1, int parInt2) {
		super(parInt1);
		this.field_150632_aF = parInt2;
		this.theBiomeDecorator.treesPerChunk = 10;
		this.theBiomeDecorator.grassPerChunk = 2;
		if (this.field_150632_aF == 1) {
			this.theBiomeDecorator.treesPerChunk = 6;
			this.theBiomeDecorator.flowersPerChunk = 100;
			this.theBiomeDecorator.grassPerChunk = 1;
		}

		this.setFillerBlockMetadata(5159473);
		this.setTemperatureRainfall(0.7F, 0.8F);
		if (this.field_150632_aF == 2) {
			this.field_150609_ah = 353825;
			this.color = 3175492;
			this.setTemperatureRainfall(0.6F, 0.6F);
		}

		if (this.field_150632_aF == 0) {
			this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 5, 4, 4));
		}

		if (this.field_150632_aF == 3) {
			this.theBiomeDecorator.treesPerChunk = -999;
		}

	}

	protected BiomeGenBase func_150557_a(int i, boolean flag) {
		if (this.field_150632_aF == 2) {
			this.field_150609_ah = 353825;
			this.color = i;
			if (flag) {
				this.field_150609_ah = (this.field_150609_ah & 16711422) >> 1;
			}

			return this;
		} else {
			return super.func_150557_a(i, flag);
		}
	}

	public WorldGenAbstractTree genBigTreeChance(EaglercraftRandom random) {
		return (WorldGenAbstractTree) (this.field_150632_aF == 3 && random.nextInt(3) > 0 ? field_150631_aE
				: (this.field_150632_aF != 2 && random.nextInt(5) != 0 ? this.worldGeneratorTrees : field_150630_aD));
	}

	public BlockFlower.EnumFlowerType pickRandomFlower(EaglercraftRandom random, BlockPos blockpos) {
		if (this.field_150632_aF == 1) {
			double d0 = MathHelper.clamp_double((1.0D + GRASS_COLOR_NOISE
					.func_151601_a((double) blockpos.getX() / 48.0D, (double) blockpos.getZ() / 48.0D)) / 2.0D, 0.0D,
					0.9999D);
			BlockFlower.EnumFlowerType blockflower$enumflowertype = BlockFlower.EnumFlowerType._VALUES[(int) (d0
					* (double) BlockFlower.EnumFlowerType._VALUES.length)];
			return blockflower$enumflowertype == BlockFlower.EnumFlowerType.BLUE_ORCHID
					? BlockFlower.EnumFlowerType.POPPY
					: blockflower$enumflowertype;
		} else {
			return super.pickRandomFlower(random, blockpos);
		}
	}

	public void decorate(World world, EaglercraftRandom random, BlockPos blockpos) {
		if (this.field_150632_aF == 3) {
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					int k = i * 4 + 1 + 8 + random.nextInt(3);
					int l = j * 4 + 1 + 8 + random.nextInt(3);
					BlockPos blockpos1 = world.getHeight(blockpos.add(k, 0, l));
					if (random.nextInt(20) == 0) {
						WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
						worldgenbigmushroom.generate(world, random, blockpos1);
					} else {
						WorldGenAbstractTree worldgenabstracttree = this.genBigTreeChance(random);
						worldgenabstracttree.func_175904_e();
						if (worldgenabstracttree.generate(world, random, blockpos1)) {
							worldgenabstracttree.func_180711_a(world, random, blockpos1);
						}
					}
				}
			}
		}

		int j1 = random.nextInt(5) - 3;
		if (this.field_150632_aF == 1) {
			j1 += 2;
		}

		for (int k1 = 0; k1 < j1; ++k1) {
			int l1 = random.nextInt(3);
			if (l1 == 0) {
				DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SYRINGA);
			} else if (l1 == 1) {
				DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.ROSE);
			} else if (l1 == 2) {
				DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.PAEONIA);
			}

			for (int i2 = 0; i2 < 5; ++i2) {
				int j2 = random.nextInt(16) + 8;
				int k2 = random.nextInt(16) + 8;
				int i1 = random.nextInt(world.getHeight(blockpos.add(j2, 0, k2)).getY() + 32);
				if (DOUBLE_PLANT_GENERATOR.generate(world, random,
						new BlockPos(blockpos.getX() + j2, i1, blockpos.getZ() + k2))) {
					break;
				}
			}
		}

		super.decorate(world, random, blockpos);
	}

	public int getGrassColorAtPos(BlockPos blockpos) {
		int i = super.getGrassColorAtPos(blockpos);
		return this.field_150632_aF == 3 ? (i & 16711422) + 2634762 >> 1 : i;
	}

	protected BiomeGenBase createMutatedBiome(final int i) {
		if (this.biomeID == BiomeGenBase.forest.biomeID) {
			BiomeGenForest biomegenforest = new BiomeGenForest(i, 1);
			biomegenforest.setHeight(new BiomeGenBase.Height(this.minHeight, this.maxHeight + 0.2F));
			biomegenforest.setBiomeName("Flower Forest");
			biomegenforest.func_150557_a(6976549, true);
			biomegenforest.setFillerBlockMetadata(8233509);
			return biomegenforest;
		} else {
			return this.biomeID != BiomeGenBase.birchForest.biomeID
					&& this.biomeID != BiomeGenBase.birchForestHills.biomeID ? new BiomeGenMutated(i, this) {
						public void decorate(World world, EaglercraftRandom random, BlockPos blockpos) {
							this.baseBiome.decorate(world, random, blockpos);
						}
					} : new BiomeGenMutated(i, this) {
						public WorldGenAbstractTree genBigTreeChance(EaglercraftRandom random) {
							return random.nextBoolean() ? BiomeGenForest.field_150629_aC
									: BiomeGenForest.field_150630_aD;
						}
					};
		}
	}
}