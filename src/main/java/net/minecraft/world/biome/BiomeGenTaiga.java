package net.minecraft.world.biome;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.BlockDirt;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class BiomeGenTaiga extends BiomeGenBase {
	private int field_150644_aH;

	public BiomeGenTaiga(int parInt1, int parInt2) {
		super(parInt1);
		this.field_150644_aH = parInt2;
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 8, 4, 4));
	}

	public void genTerrainBlocks(World world, EaglercraftRandom random, ChunkPrimer chunkprimer, int i, int j,
			double d0) {
		if (this.field_150644_aH == 1 || this.field_150644_aH == 2) {
			this.topBlock = Blocks.grass.getDefaultState();
			this.fillerBlock = Blocks.dirt.getDefaultState();
			if (d0 > 1.75D) {
				this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT,
						BlockDirt.DirtType.COARSE_DIRT);
			} else if (d0 > -0.95D) {
				this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT,
						BlockDirt.DirtType.PODZOL);
			}
		}

		this.generateBiomeTerrain(world, random, chunkprimer, i, j, d0);
	}

	protected BiomeGenBase createMutatedBiome(int i) {
		return this.biomeID == BiomeGenBase.megaTaiga.biomeID ? (new BiomeGenTaiga(i, 2)).func_150557_a(5858897, true)
				.setBiomeName("Mega Spruce Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25F, 0.8F)
				.setHeight(new BiomeGenBase.Height(this.minHeight, this.maxHeight)) : super.createMutatedBiome(i);
	}
}