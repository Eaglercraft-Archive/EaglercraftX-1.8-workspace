package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.util.BlockPos;

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
public class WorldChunkManagerHell extends WorldChunkManager {
	private BiomeGenBase biomeGenerator;
	private float rainfall;

	public WorldChunkManagerHell(BiomeGenBase parBiomeGenBase, float parFloat1) {
		this.biomeGenerator = parBiomeGenBase;
		this.rainfall = parFloat1;
	}

	/**+
	 * Returns the biome generator
	 */
	public BiomeGenBase getBiomeGenerator(BlockPos var1) {
		return this.biomeGenerator;
	}

	/**+
	 * Returns an array of biomes for the location input.
	 */
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] abiomegenbase, int var2, int var3, int i, int j) {
		if (abiomegenbase == null || abiomegenbase.length < i * j) {
			abiomegenbase = new BiomeGenBase[i * j];
		}

		Arrays.fill(abiomegenbase, 0, i * j, this.biomeGenerator);
		return abiomegenbase;
	}

	/**+
	 * Returns a list of rainfall values for the specified blocks.
	 * Args: listToReuse, x, z, width, length.
	 */
	public float[] getRainfall(float[] afloat, int var2, int var3, int i, int j) {
		if (afloat == null || afloat.length < i * j) {
			afloat = new float[i * j];
		}

		Arrays.fill(afloat, 0, i * j, this.rainfall);
		return afloat;
	}

	/**+
	 * Returns biomes to use for the blocks and loads the other data
	 * like temperature and humidity onto the WorldChunkManager
	 * Args: oldBiomeList, x, z, width, depth
	 */
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] abiomegenbase, int var2, int var3, int i, int j) {
		if (abiomegenbase == null || abiomegenbase.length < i * j) {
			abiomegenbase = new BiomeGenBase[i * j];
		}

		Arrays.fill(abiomegenbase, 0, i * j, this.biomeGenerator);
		return abiomegenbase;
	}

	/**+
	 * Return a list of biomes for the specified blocks. Args:
	 * listToReuse, x, y, width, length, cacheFlag (if false, don't
	 * check biomeCache to avoid infinite loop in BiomeCacheBlock)
	 */
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] abiomegenbase, int i, int j, int k, int l, boolean var6) {
		return this.loadBlockGeneratorData(abiomegenbase, i, j, k, l);
	}

	public BlockPos findBiomePosition(int i, int j, int k, List<BiomeGenBase> list, EaglercraftRandom random) {
		return list.contains(this.biomeGenerator)
				? new BlockPos(i - k + random.nextInt(k * 2 + 1), 0, j - k + random.nextInt(k * 2 + 1))
				: null;
	}

	/**+
	 * checks given Chunk's Biomes against List of allowed ones
	 */
	public boolean areBiomesViable(int var1, int var2, int var3, List<BiomeGenBase> list) {
		return list.contains(this.biomeGenerator);
	}
}