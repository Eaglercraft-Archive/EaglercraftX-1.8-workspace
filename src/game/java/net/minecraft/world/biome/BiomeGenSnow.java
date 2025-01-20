package net.minecraft.world.biome;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

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
public class BiomeGenSnow extends BiomeGenBase {
	private boolean field_150615_aC;
	private WorldGenIceSpike field_150616_aD = new WorldGenIceSpike();
	private WorldGenIcePath field_150617_aE = new WorldGenIcePath(4);

	public BiomeGenSnow(int parInt1, boolean parFlag) {
		super(parInt1);
		this.field_150615_aC = parFlag;
		if (parFlag) {
			this.topBlock = Blocks.snow.getDefaultState();
		}

		this.spawnableCreatureList.clear();
	}

	public void decorate(World world, EaglercraftRandom random, BlockPos blockpos) {
		if (this.field_150615_aC) {
			for (int i = 0; i < 3; ++i) {
				int j = random.nextInt(16) + 8;
				int k = random.nextInt(16) + 8;
				this.field_150616_aD.generate(world, random, world.getHeight(blockpos.add(j, 0, k)));
			}

			for (int l = 0; l < 2; ++l) {
				int i1 = random.nextInt(16) + 8;
				int j1 = random.nextInt(16) + 8;
				this.field_150617_aE.generate(world, random, world.getHeight(blockpos.add(i1, 0, j1)));
			}
		}

		super.decorate(world, random, blockpos);
	}

	public WorldGenAbstractTree genBigTreeChance(EaglercraftRandom var1) {
		return new WorldGenTaiga2(false);
	}

	protected BiomeGenBase createMutatedBiome(int i) {
		BiomeGenBase biomegenbase = (new BiomeGenSnow(i, true)).func_150557_a(13828095, true)
				.setBiomeName(this.biomeName + " Spikes").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F)
				.setHeight(new BiomeGenBase.Height(this.minHeight + 0.1F, this.maxHeight + 0.1F));
		biomegenbase.minHeight = this.minHeight + 0.3F;
		biomegenbase.maxHeight = this.maxHeight + 0.4F;
		return biomegenbase;
	}
}