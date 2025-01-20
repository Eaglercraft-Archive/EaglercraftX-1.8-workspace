package net.minecraft.world.gen.layer;

import java.util.concurrent.Callable;

import net.lax1dude.eaglercraft.v1_8.sp.server.GenLayerEaglerRivers;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;

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
public abstract class GenLayer {
	protected long worldGenSeed;
	protected GenLayer parent;
	private long chunkSeed;
	protected long baseSeed;

	public static GenLayer[] initializeAllBiomeGenerators(long seed, WorldType parWorldType, String parString1) {
		GenLayerIsland genlayerisland = new GenLayerIsland(1L);
		GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
		GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayerfuzzyzoom);
		GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
		GenLayerAddIsland genlayeraddisland1 = new GenLayerAddIsland(2L, genlayerzoom);
		genlayeraddisland1 = new GenLayerAddIsland(50L, genlayeraddisland1);
		genlayeraddisland1 = new GenLayerAddIsland(70L, genlayeraddisland1);
		GenLayerRemoveTooMuchOcean genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland1);
		GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
		GenLayerAddIsland genlayeraddisland2 = new GenLayerAddIsland(3L, genlayeraddsnow);
		GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland2, GenLayerEdge.Mode.COOL_WARM);
		genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
		genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
		GenLayerZoom genlayerzoom1 = new GenLayerZoom(2002L, genlayeredge);
		genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);
		GenLayerAddIsland genlayeraddisland3 = new GenLayerAddIsland(4L, genlayerzoom1);
		GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland3);
		GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
		GenLayer genlayer2 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
		ChunkProviderSettings chunkprovidersettings = null;
		int i = 4;
		int j = i;
		if (parWorldType == WorldType.CUSTOMIZED && parString1.length() > 0) {
			chunkprovidersettings = ChunkProviderSettings.Factory.jsonToFactory(parString1).func_177864_b();
			i = chunkprovidersettings.biomeSize;
			j = chunkprovidersettings.riverSize;
		}

		if (parWorldType == WorldType.LARGE_BIOMES) {
			i = 6;
		}

		GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer2, 0);
		GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
		GenLayerBiome genlayerbiome = new GenLayerBiome(200L, genlayer2, parWorldType, parString1);
		GenLayer genlayer4 = GenLayerZoom.magnify(1000L, genlayerbiome, 2);
		GenLayerBiomeEdge genlayerbiomeedge = new GenLayerBiomeEdge(1000L, genlayer4);
		GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
		GenLayer genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, genlayer1);
		GenLayer genlayer3 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
		genlayer3 = GenLayerZoom.magnify(1000L, genlayer3, j);
		GenLayer genlayerriver = new GenLayerRiver(1L, genlayer3);
		genlayerriver = new GenLayerSmooth(1000L, genlayerriver);
		genlayerriver = new GenLayerEaglerRivers(69L, genlayerriver);
		genlayerhills = new GenLayerRareBiome(1001L, genlayerhills);

		for (int k = 0; k < i; ++k) {
			genlayerhills = new GenLayerZoom((long) (1000 + k), genlayerhills);
			if (k == 0) {
				genlayerhills = new GenLayerAddIsland(3L, genlayerhills);
			}

			if (k == 1 || i == 1) {
				genlayerhills = new GenLayerShore(1000L, genlayerhills);
			}
		}

		GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, genlayerhills);
		GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayerriver);
		GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
		genlayerrivermix.initWorldGenSeed(seed);
		genlayervoronoizoom.initWorldGenSeed(seed);
		return new GenLayer[] { genlayerrivermix, genlayervoronoizoom, genlayerrivermix };
	}

	public GenLayer(long parLong1) {
		this.baseSeed = parLong1;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += parLong1;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += parLong1;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += parLong1;
	}

	/**+
	 * Initialize layer's local worldGenSeed based on its own
	 * baseSeed and the world's global seed (passed in as an
	 * argument).
	 */
	public void initWorldGenSeed(long seed) {
		this.worldGenSeed = seed;
		if (this.parent != null) {
			this.parent.initWorldGenSeed(seed);
		}

		this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldGenSeed += this.baseSeed;
		this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldGenSeed += this.baseSeed;
		this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldGenSeed += this.baseSeed;
	}

	/**+
	 * Initialize layer's current chunkSeed based on the local
	 * worldGenSeed and the (x,z) chunk coordinates.
	 */
	public void initChunkSeed(long parLong1, long parLong2) {
		this.chunkSeed = this.worldGenSeed;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += parLong1;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += parLong2;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += parLong1;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += parLong2;
	}

	/**+
	 * returns a LCG pseudo random number from [0, x). Args: int x
	 */
	protected int nextInt(int parInt1) {
		int i = (int) ((this.chunkSeed >> 24) % (long) parInt1);
		if (i < 0) {
			i += parInt1;
		}

		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += this.worldGenSeed;
		return i;
	}

	public abstract int[] getInts(int var1, int var2, int var3, int var4);

	protected static boolean biomesEqualOrMesaPlateau(int biomeIDA, int biomeIDB) {
		if (biomeIDA == biomeIDB) {
			return true;
		} else if (biomeIDA != BiomeGenBase.mesaPlateau_F.biomeID && biomeIDA != BiomeGenBase.mesaPlateau.biomeID) {
			final BiomeGenBase biomegenbase = BiomeGenBase.getBiome(biomeIDA);
			final BiomeGenBase biomegenbase1 = BiomeGenBase.getBiome(biomeIDB);

			try {
				return biomegenbase != null && biomegenbase1 != null ? biomegenbase.isEqualTo(biomegenbase1) : false;
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Comparing biomes");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Biomes being compared");
				crashreportcategory.addCrashSection("Biome A ID", Integer.valueOf(biomeIDA));
				crashreportcategory.addCrashSection("Biome B ID", Integer.valueOf(biomeIDB));
				crashreportcategory.addCrashSectionCallable("Biome A", new Callable<String>() {
					public String call() throws Exception {
						return String.valueOf(biomegenbase);
					}
				});
				crashreportcategory.addCrashSectionCallable("Biome B", new Callable<String>() {
					public String call() throws Exception {
						return String.valueOf(biomegenbase1);
					}
				});
				throw new ReportedException(crashreport);
			}
		} else {
			return biomeIDB == BiomeGenBase.mesaPlateau_F.biomeID || biomeIDB == BiomeGenBase.mesaPlateau.biomeID;
		}
	}

	/**+
	 * returns true if the biomeId is one of the various ocean
	 * biomes.
	 */
	protected static boolean isBiomeOceanic(int parInt1) {
		return parInt1 == BiomeGenBase.ocean.biomeID || parInt1 == BiomeGenBase.deepOcean.biomeID
				|| parInt1 == BiomeGenBase.frozenOcean.biomeID;
	}

	/**+
	 * selects a random integer from a set of provided integers
	 */
	protected int selectRandom(int... parArrayOfInt) {
		return parArrayOfInt[this.nextInt(parArrayOfInt.length)];
	}

	/**+
	 * returns the most frequently occurring number of the set, or a
	 * random number from those provided
	 */
	protected int selectModeOrRandom(int i, int j, int k, int l) {
		return j == k && k == l ? j
				: (i == j && i == k ? i
						: (i == j && i == l ? i
								: (i == k && i == l ? i
										: (i == j && k != l ? i
												: (i == k && j != l ? i
														: (i == l && j != k ? i
																: (j == k && i != l ? j
																		: (j == l && i != k ? j
																				: (k == l && i != j ? k
																						: this.selectRandom(new int[] {
																								i, j, k, l }))))))))));
	}
}