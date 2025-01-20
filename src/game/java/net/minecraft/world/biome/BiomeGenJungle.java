package net.minecraft.world.biome;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

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
public class BiomeGenJungle extends BiomeGenBase {
	private boolean field_150614_aC;
	private final IBlockState field_181620_aE;
	private final IBlockState field_181621_aF;
	private final IBlockState field_181622_aG;

	public BiomeGenJungle(int parInt1, boolean parFlag) {
		super(parInt1);
		field_181620_aE = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
		field_181621_aF = Blocks.leaves.getDefaultState()
				.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE)
				.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
		field_181622_aG = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
				.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
		this.field_150614_aC = parFlag;
		if (parFlag) {
			this.theBiomeDecorator.treesPerChunk = 2;
		} else {
			this.theBiomeDecorator.treesPerChunk = 50;
		}

		this.theBiomeDecorator.grassPerChunk = 25;
		this.theBiomeDecorator.flowersPerChunk = 4;
		if (!parFlag) {
			this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityOcelot.class, 2, 1, 1));
		}

		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityChicken.class, 10, 4, 4));
	}

	public WorldGenAbstractTree genBigTreeChance(EaglercraftRandom random) {
		return (WorldGenAbstractTree) (random.nextInt(10) == 0 ? this.worldGeneratorBigTree
				: (random.nextInt(2) == 0 ? new WorldGenShrub(field_181620_aE, field_181622_aG)
						: (!this.field_150614_aC && random.nextInt(3) == 0
								? new WorldGenMegaJungle(false, 10, 20, field_181620_aE, field_181621_aF)
								: new WorldGenTrees(false, 4 + random.nextInt(7), field_181620_aE, field_181621_aF,
										true))));
	}

	/**+
	 * Gets a WorldGen appropriate for this biome.
	 */
	public WorldGenerator getRandomWorldGenForGrass(EaglercraftRandom random) {
		return random.nextInt(4) == 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN)
				: new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
	}

	public void decorate(World world, EaglercraftRandom random, BlockPos blockpos) {
		super.decorate(world, random, blockpos);
		int i = random.nextInt(16) + 8;
		int j = random.nextInt(16) + 8;
		int k = random.nextInt(world.getHeight(blockpos.add(i, 0, j)).getY() * 2);
		(new WorldGenMelon()).generate(world, random, blockpos.add(i, k, j));
		WorldGenVines worldgenvines = new WorldGenVines();

		for (j = 0; j < 50; ++j) {
			k = random.nextInt(16) + 8;
			boolean flag = true;
			int l = random.nextInt(16) + 8;
			worldgenvines.generate(world, random, blockpos.add(k, 128, l));
		}

	}
}