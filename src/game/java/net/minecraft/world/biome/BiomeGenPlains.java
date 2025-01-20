package net.minecraft.world.biome;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

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
public class BiomeGenPlains extends BiomeGenBase {
	protected boolean field_150628_aC;

	protected BiomeGenPlains(int parInt1) {
		super(parInt1);
		this.setTemperatureRainfall(0.8F, 0.4F);
		this.setHeight(height_LowPlains);
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 5, 2, 6));
		this.theBiomeDecorator.treesPerChunk = -999;
		this.theBiomeDecorator.flowersPerChunk = 4;
		this.theBiomeDecorator.grassPerChunk = 10;
	}

	public BlockFlower.EnumFlowerType pickRandomFlower(EaglercraftRandom random, BlockPos blockpos) {
		double d0 = GRASS_COLOR_NOISE.func_151601_a((double) blockpos.getX() / 200.0D,
				(double) blockpos.getZ() / 200.0D);
		if (d0 < -0.8D) {
			int j = random.nextInt(4);
			switch (j) {
			case 0:
				return BlockFlower.EnumFlowerType.ORANGE_TULIP;
			case 1:
				return BlockFlower.EnumFlowerType.RED_TULIP;
			case 2:
				return BlockFlower.EnumFlowerType.PINK_TULIP;
			case 3:
			default:
				return BlockFlower.EnumFlowerType.WHITE_TULIP;
			}
		} else if (random.nextInt(3) > 0) {
			int i = random.nextInt(3);
			return i == 0 ? BlockFlower.EnumFlowerType.POPPY
					: (i == 1 ? BlockFlower.EnumFlowerType.HOUSTONIA : BlockFlower.EnumFlowerType.OXEYE_DAISY);
		} else {
			return BlockFlower.EnumFlowerType.DANDELION;
		}
	}

	public void decorate(World world, EaglercraftRandom random, BlockPos blockpos) {
		double d0 = GRASS_COLOR_NOISE.func_151601_a((double) (blockpos.getX() + 8) / 200.0D,
				(double) (blockpos.getZ() + 8) / 200.0D);
		if (d0 < -0.8D) {
			this.theBiomeDecorator.flowersPerChunk = 15;
			this.theBiomeDecorator.grassPerChunk = 5;
		} else {
			this.theBiomeDecorator.flowersPerChunk = 4;
			this.theBiomeDecorator.grassPerChunk = 10;
			DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);

			for (int i = 0; i < 7; ++i) {
				int j = random.nextInt(16) + 8;
				int k = random.nextInt(16) + 8;
				int l = random.nextInt(world.getHeight(blockpos.add(j, 0, k)).getY() + 32);
				DOUBLE_PLANT_GENERATOR.generate(world, random, blockpos.add(j, l, k));
			}
		}

		if (this.field_150628_aC) {
			DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SUNFLOWER);

			for (int i1 = 0; i1 < 10; ++i1) {
				int j1 = random.nextInt(16) + 8;
				int k1 = random.nextInt(16) + 8;
				int l1 = random.nextInt(world.getHeight(blockpos.add(j1, 0, k1)).getY() + 32);
				DOUBLE_PLANT_GENERATOR.generate(world, random, blockpos.add(j1, l1, k1));
			}
		}

		super.decorate(world, random, blockpos);
	}

	protected BiomeGenBase createMutatedBiome(int i) {
		BiomeGenPlains biomegenplains = new BiomeGenPlains(i);
		biomegenplains.setBiomeName("Sunflower Plains");
		biomegenplains.field_150628_aC = true;
		biomegenplains.setColor(9286496);
		biomegenplains.field_150609_ah = 14273354;
		return biomegenplains;
	}
}