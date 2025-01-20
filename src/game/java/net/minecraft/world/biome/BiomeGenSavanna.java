package net.minecraft.world.biome;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

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
public class BiomeGenSavanna extends BiomeGenBase {

	private static final WorldGenSavannaTree field_150627_aC = new WorldGenSavannaTree(false);

	protected BiomeGenSavanna(int parInt1) {
		super(parInt1);
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 1, 2, 6));
		this.theBiomeDecorator.treesPerChunk = 1;
		this.theBiomeDecorator.flowersPerChunk = 4;
		this.theBiomeDecorator.grassPerChunk = 20;
	}

	public WorldGenAbstractTree genBigTreeChance(EaglercraftRandom random) {
		return (WorldGenAbstractTree) (random.nextInt(5) > 0 ? field_150627_aC : this.worldGeneratorTrees);
	}

	protected BiomeGenBase createMutatedBiome(int i) {
		BiomeGenSavanna.Mutated biomegensavanna$mutated = new BiomeGenSavanna.Mutated(i, this);
		biomegensavanna$mutated.temperature = (this.temperature + 1.0F) * 0.5F;
		biomegensavanna$mutated.minHeight = this.minHeight * 0.5F + 0.3F;
		biomegensavanna$mutated.maxHeight = this.maxHeight * 0.5F + 1.2F;
		return biomegensavanna$mutated;
	}

	public void decorate(World world, EaglercraftRandom random, BlockPos blockpos) {
		DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);

		for (int i = 0; i < 7; ++i) {
			int j = random.nextInt(16) + 8;
			int k = random.nextInt(16) + 8;
			int l = random.nextInt(world.getHeight(blockpos.add(j, 0, k)).getY() + 32);
			DOUBLE_PLANT_GENERATOR.generate(world, random, blockpos.add(j, l, k));
		}

		super.decorate(world, random, blockpos);
	}

	public static class Mutated extends BiomeGenMutated {
		public Mutated(int parInt1, BiomeGenBase parBiomeGenBase) {
			super(parInt1, parBiomeGenBase);
			this.theBiomeDecorator.treesPerChunk = 2;
			this.theBiomeDecorator.flowersPerChunk = 2;
			this.theBiomeDecorator.grassPerChunk = 5;
		}

		public void genTerrainBlocks(World world, EaglercraftRandom random, ChunkPrimer chunkprimer, int i, int j,
				double d0) {
			this.topBlock = Blocks.grass.getDefaultState();
			this.fillerBlock = Blocks.dirt.getDefaultState();
			if (d0 > 1.75D) {
				this.topBlock = Blocks.stone.getDefaultState();
				this.fillerBlock = Blocks.stone.getDefaultState();
			} else if (d0 > -0.5D) {
				this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT,
						BlockDirt.DirtType.COARSE_DIRT);
			}

			this.generateBiomeTerrain(world, random, chunkprimer, i, j, d0);
		}

		public void decorate(World world, EaglercraftRandom random, BlockPos blockpos) {
			this.theBiomeDecorator.decorate(world, random, this, blockpos);
		}
	}
}