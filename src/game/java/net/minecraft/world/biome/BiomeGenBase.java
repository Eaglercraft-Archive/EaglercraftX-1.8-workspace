package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import java.util.Set;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public abstract class BiomeGenBase {
	private static final Logger logger = LogManager.getLogger();
	protected static final BiomeGenBase.Height height_Default = new BiomeGenBase.Height(0.1F, 0.2F);
	protected static final BiomeGenBase.Height height_ShallowWaters = new BiomeGenBase.Height(-0.5F, 0.0F);
	protected static final BiomeGenBase.Height height_Oceans = new BiomeGenBase.Height(-1.0F, 0.1F);
	protected static final BiomeGenBase.Height height_DeepOceans = new BiomeGenBase.Height(-1.8F, 0.1F);
	protected static final BiomeGenBase.Height height_LowPlains = new BiomeGenBase.Height(0.125F, 0.05F);
	protected static final BiomeGenBase.Height height_MidPlains = new BiomeGenBase.Height(0.2F, 0.2F);
	protected static final BiomeGenBase.Height height_LowHills = new BiomeGenBase.Height(0.45F, 0.3F);
	protected static final BiomeGenBase.Height height_HighPlateaus = new BiomeGenBase.Height(1.5F, 0.025F);
	protected static final BiomeGenBase.Height height_MidHills = new BiomeGenBase.Height(1.0F, 0.5F);
	protected static final BiomeGenBase.Height height_Shores = new BiomeGenBase.Height(0.0F, 0.025F);
	protected static final BiomeGenBase.Height height_RockyWaters = new BiomeGenBase.Height(0.1F, 0.8F);
	protected static final BiomeGenBase.Height height_LowIslands = new BiomeGenBase.Height(0.2F, 0.3F);
	protected static final BiomeGenBase.Height height_PartiallySubmerged = new BiomeGenBase.Height(-0.2F, 0.1F);
	/**+
	 * An array of all the biomes, indexed by biome id.
	 */
	private static final BiomeGenBase[] biomeList = new BiomeGenBase[256];
	public static final Set<BiomeGenBase> explorationBiomesList = Sets.newHashSet();
	public static final Map<String, BiomeGenBase> BIOME_ID_MAP = Maps.newHashMap();
	public static BiomeGenBase ocean;
	public static BiomeGenBase plains;
	public static BiomeGenBase desert;
	public static BiomeGenBase extremeHills;
	public static BiomeGenBase forest;
	public static BiomeGenBase taiga;
	public static BiomeGenBase swampland;
	public static BiomeGenBase river;
	public static BiomeGenBase hell;
	public static BiomeGenBase sky;
	public static BiomeGenBase frozenOcean;
	public static BiomeGenBase frozenRiver;
	public static BiomeGenBase icePlains;
	public static BiomeGenBase iceMountains;
	public static BiomeGenBase mushroomIsland;
	public static BiomeGenBase mushroomIslandShore;
	public static BiomeGenBase beach;
	public static BiomeGenBase desertHills;
	public static BiomeGenBase forestHills;
	public static BiomeGenBase taigaHills;
	public static BiomeGenBase extremeHillsEdge;
	public static BiomeGenBase jungle;
	public static BiomeGenBase jungleHills;
	public static BiomeGenBase jungleEdge;
	public static BiomeGenBase deepOcean;
	public static BiomeGenBase stoneBeach;
	public static BiomeGenBase coldBeach;
	public static BiomeGenBase birchForest;
	public static BiomeGenBase birchForestHills;
	public static BiomeGenBase roofedForest;
	public static BiomeGenBase coldTaiga;
	public static BiomeGenBase coldTaigaHills;
	public static BiomeGenBase megaTaiga;
	public static BiomeGenBase megaTaigaHills;
	public static BiomeGenBase extremeHillsPlus;
	public static BiomeGenBase savanna;
	public static BiomeGenBase savannaPlateau;
	public static BiomeGenBase mesa;
	public static BiomeGenBase mesaPlateau_F;
	public static BiomeGenBase mesaPlateau;
	public static BiomeGenBase field_180279_ad;
	protected static NoiseGeneratorPerlin temperatureNoise;
	protected static NoiseGeneratorPerlin GRASS_COLOR_NOISE;
	protected static WorldGenDoublePlant DOUBLE_PLANT_GENERATOR;
	public String biomeName;
	public int color;
	public int field_150609_ah;
	/**+
	 * The block expected to be on the top of this biome
	 */
	public IBlockState topBlock = Blocks.grass.getDefaultState();
	/**+
	 * The block to fill spots in when not on the top
	 */
	public IBlockState fillerBlock = Blocks.dirt.getDefaultState();
	public int fillerBlockMetadata = 5169201;
	public float minHeight;
	public float maxHeight;
	public float temperature;
	public float rainfall;
	public int waterColorMultiplier;
	public BiomeDecorator theBiomeDecorator;
	protected List<BiomeGenBase.SpawnListEntry> spawnableMonsterList;
	protected List<BiomeGenBase.SpawnListEntry> spawnableCreatureList;
	protected List<BiomeGenBase.SpawnListEntry> spawnableWaterCreatureList;
	protected List<BiomeGenBase.SpawnListEntry> spawnableCaveCreatureList;
	protected boolean enableSnow;
	protected boolean enableRain;
	public final int biomeID;
	protected WorldGenTrees worldGeneratorTrees;
	protected WorldGenBigTree worldGeneratorBigTree;
	protected WorldGenSwamp worldGeneratorSwamp;

	protected BiomeGenBase(int id) {
		this.minHeight = height_Default.rootHeight;
		this.maxHeight = height_Default.variation;
		this.temperature = 0.5F;
		this.rainfall = 0.5F;
		this.waterColorMultiplier = 16777215;
		this.spawnableMonsterList = Lists.newArrayList();
		this.spawnableCreatureList = Lists.newArrayList();
		this.spawnableWaterCreatureList = Lists.newArrayList();
		this.spawnableCaveCreatureList = Lists.newArrayList();
		this.enableRain = true;
		this.worldGeneratorTrees = new WorldGenTrees(false);
		this.worldGeneratorBigTree = new WorldGenBigTree(false);
		this.worldGeneratorSwamp = new WorldGenSwamp();
		this.biomeID = id;
		biomeList[id] = this;
		this.theBiomeDecorator = this.createBiomeDecorator();
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySheep.class, 12, 4, 4));
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityRabbit.class, 10, 3, 3));
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityPig.class, 10, 4, 4));
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityChicken.class, 10, 4, 4));
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityCow.class, 8, 4, 4));
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySpider.class, 100, 4, 4));
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityZombie.class, 100, 4, 4));
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityCreeper.class, 100, 4, 4));
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 100, 4, 4));
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityEnderman.class, 10, 1, 4));
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 5, 1, 1));
		this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySquid.class, 10, 4, 4));
		this.spawnableCaveCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityBat.class, 10, 8, 8));
	}

	/**+
	 * Allocate a new BiomeDecorator for this BiomeGenBase
	 */
	protected BiomeDecorator createBiomeDecorator() {
		return new BiomeDecorator();
	}

	/**+
	 * Sets the temperature and rainfall of this biome.
	 */
	protected BiomeGenBase setTemperatureRainfall(float temperatureIn, float rainfallIn) {
		if (temperatureIn > 0.1F && temperatureIn < 0.2F) {
			throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
		} else {
			this.temperature = temperatureIn;
			this.rainfall = rainfallIn;
			return this;
		}
	}

	protected final BiomeGenBase setHeight(BiomeGenBase.Height heights) {
		this.minHeight = heights.rootHeight;
		this.maxHeight = heights.variation;
		return this;
	}

	/**+
	 * Disable the rain for the biome.
	 */
	protected BiomeGenBase setDisableRain() {
		this.enableRain = false;
		return this;
	}

	public WorldGenAbstractTree genBigTreeChance(EaglercraftRandom rand) {
		return (WorldGenAbstractTree) (rand.nextInt(10) == 0 ? this.worldGeneratorBigTree : this.worldGeneratorTrees);
	}

	/**+
	 * Gets a WorldGen appropriate for this biome.
	 */
	public WorldGenerator getRandomWorldGenForGrass(EaglercraftRandom rand) {
		return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
	}

	public BlockFlower.EnumFlowerType pickRandomFlower(EaglercraftRandom rand, BlockPos pos) {
		return rand.nextInt(3) > 0 ? BlockFlower.EnumFlowerType.DANDELION : BlockFlower.EnumFlowerType.POPPY;
	}

	/**+
	 * sets enableSnow to true during biome initialization. returns
	 * BiomeGenBase.
	 */
	protected BiomeGenBase setEnableSnow() {
		this.enableSnow = true;
		return this;
	}

	protected BiomeGenBase setBiomeName(String name) {
		this.biomeName = name;
		return this;
	}

	protected BiomeGenBase setFillerBlockMetadata(int meta) {
		this.fillerBlockMetadata = meta;
		return this;
	}

	protected BiomeGenBase setColor(int colorIn) {
		this.func_150557_a(colorIn, false);
		return this;
	}

	protected BiomeGenBase func_150563_c(int parInt1) {
		this.field_150609_ah = parInt1;
		return this;
	}

	protected BiomeGenBase func_150557_a(int parInt1, boolean parFlag) {
		this.color = parInt1;
		if (parFlag) {
			this.field_150609_ah = (parInt1 & 16711422) >> 1;
		} else {
			this.field_150609_ah = parInt1;
		}

		return this;
	}

	/**+
	 * takes temperature, returns color
	 */
	public int getSkyColorByTemp(float parFloat1) {
		parFloat1 = parFloat1 / 3.0F;
		parFloat1 = MathHelper.clamp_float(parFloat1, -1.0F, 1.0F);
		return MathHelper.func_181758_c(0.62222224F - parFloat1 * 0.05F, 0.5F + parFloat1 * 0.1F, 1.0F);
	}

	/**+
	 * Returns the correspondent list of the EnumCreatureType
	 * informed.
	 */
	public List<BiomeGenBase.SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
		switch (creatureType) {
		case MONSTER:
			return this.spawnableMonsterList;
		case CREATURE:
			return this.spawnableCreatureList;
		case WATER_CREATURE:
			return this.spawnableWaterCreatureList;
		case AMBIENT:
			return this.spawnableCaveCreatureList;
		default:
			return Collections.emptyList();
		}
	}

	/**+
	 * Returns true if the biome have snowfall instead a normal
	 * rain.
	 */
	public boolean getEnableSnow() {
		return this.isSnowyBiome();
	}

	/**+
	 * Return true if the biome supports lightning bolt spawn,
	 * either by have the bolts enabled and have rain enabled.
	 */
	public boolean canSpawnLightningBolt() {
		return this.isSnowyBiome() ? false : this.enableRain;
	}

	/**+
	 * Checks to see if the rainfall level of the biome is extremely
	 * high
	 */
	public boolean isHighHumidity() {
		return this.rainfall > 0.85F;
	}

	/**+
	 * returns the chance a creature has to spawn.
	 */
	public float getSpawningChance() {
		return 0.1F;
	}

	/**+
	 * Gets an integer representation of this biome's rainfall
	 */
	public final int getIntRainfall() {
		return (int) (this.rainfall * 65536.0F);
	}

	/**+
	 * Gets a floating point representation of this biome's rainfall
	 */
	public final float getFloatRainfall() {
		return this.rainfall;
	}

	/**+
	 * Gets a floating point representation of this biome's
	 * temperature
	 */
	public final float getFloatTemperature(BlockPos pos) {
		if (pos.getY() > 64) {
			float f = (float) (temperatureNoise.func_151601_a((double) pos.getX() * 1.0D / 8.0D,
					(double) pos.getZ() * 1.0D / 8.0D) * 4.0D);
			return this.temperature - (f + (float) pos.getY() - 64.0F) * 0.05F / 30.0F;
		} else {
			return this.temperature;
		}
	}

	public void decorate(World worldIn, EaglercraftRandom rand, BlockPos pos) {
		this.theBiomeDecorator.decorate(worldIn, rand, this, pos);
	}

	public int getGrassColorAtPos(BlockPos pos) {
		double d0 = (double) MathHelper.clamp_float(this.getFloatTemperature(pos), 0.0F, 1.0F);
		double d1 = (double) MathHelper.clamp_float(this.getFloatRainfall(), 0.0F, 1.0F);
		return ColorizerGrass.getGrassColor(d0, d1);
	}

	public int getFoliageColorAtPos(BlockPos pos) {
		double d0 = (double) MathHelper.clamp_float(this.getFloatTemperature(pos), 0.0F, 1.0F);
		double d1 = (double) MathHelper.clamp_float(this.getFloatRainfall(), 0.0F, 1.0F);
		return ColorizerFoliage.getFoliageColor(d0, d1);
	}

	public boolean isSnowyBiome() {
		return this.enableSnow;
	}

	public void genTerrainBlocks(World worldIn, EaglercraftRandom rand, ChunkPrimer chunkPrimerIn, int parInt1,
			int parInt2, double parDouble1) {
		this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, parInt1, parInt2, parDouble1);
	}

	public final void generateBiomeTerrain(World worldIn, EaglercraftRandom rand, ChunkPrimer chunkPrimerIn,
			int parInt1, int parInt2, double parDouble1) {
		int i = worldIn.func_181545_F();
		IBlockState iblockstate = this.topBlock;
		IBlockState iblockstate1 = this.fillerBlock;
		int j = -1;
		int k = (int) (parDouble1 / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		int l = parInt1 & 15;
		int i1 = parInt2 & 15;
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int j1 = 255; j1 >= 0; --j1) {
			if (j1 <= rand.nextInt(5)) {
				chunkPrimerIn.setBlockState(i1, j1, l, Blocks.bedrock.getDefaultState());
			} else {
				IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);
				if (iblockstate2.getBlock().getMaterial() == Material.air) {
					j = -1;
				} else if (iblockstate2.getBlock() == Blocks.stone) {
					if (j == -1) {
						if (k <= 0) {
							iblockstate = null;
							iblockstate1 = Blocks.stone.getDefaultState();
						} else if (j1 >= i - 4 && j1 <= i + 1) {
							iblockstate = this.topBlock;
							iblockstate1 = this.fillerBlock;
						}

						if (j1 < i && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air)) {
							if (this.getFloatTemperature(
									blockpos$mutableblockpos.func_181079_c(parInt1, j1, parInt2)) < 0.15F) {
								iblockstate = Blocks.ice.getDefaultState();
							} else {
								iblockstate = Blocks.water.getDefaultState();
							}
						}

						j = k;
						if (j1 >= i - 1) {
							chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
						} else if (j1 < i - 7 - k) {
							iblockstate = null;
							iblockstate1 = Blocks.stone.getDefaultState();
							chunkPrimerIn.setBlockState(i1, j1, l, Blocks.gravel.getDefaultState());
						} else {
							chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
						}
					} else if (j > 0) {
						--j;
						chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
						if (j == 0 && iblockstate1.getBlock() == Blocks.sand) {
							j = rand.nextInt(4) + Math.max(0, j1 - 63);
							iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND
									? Blocks.red_sandstone.getDefaultState()
									: Blocks.sandstone.getDefaultState();
						}
					}
				}
			}
		}

	}

	/**+
	 * Creates a mutated version of the biome and places it into the
	 * biomeList with an index equal to the original plus 128
	 */
	protected BiomeGenBase createMutation() {
		return this.createMutatedBiome(this.biomeID + 128);
	}

	protected BiomeGenBase createMutatedBiome(int parInt1) {
		return new BiomeGenMutated(parInt1, this);
	}

	public Class<? extends BiomeGenBase> getBiomeClass() {
		return this.getClass();
	}

	/**+
	 * returns true if the biome specified is equal to this biome
	 */
	public boolean isEqualTo(BiomeGenBase biome) {
		return biome == this ? true : (biome == null ? false : this.getBiomeClass() == biome.getBiomeClass());
	}

	public BiomeGenBase.TempCategory getTempCategory() {
		return (double) this.temperature < 0.2D ? BiomeGenBase.TempCategory.COLD
				: ((double) this.temperature < 1.0D ? BiomeGenBase.TempCategory.MEDIUM
						: BiomeGenBase.TempCategory.WARM);
	}

	public static BiomeGenBase[] getBiomeGenArray() {
		return biomeList;
	}

	/**+
	 * return the biome specified by biomeID, or 0 (ocean) if out of
	 * bounds
	 */
	public static BiomeGenBase getBiome(int id) {
		return getBiomeFromBiomeList(id, (BiomeGenBase) null);
	}

	public static BiomeGenBase getBiomeFromBiomeList(int biomeId, BiomeGenBase biome) {
		if (biomeId >= 0 && biomeId <= biomeList.length) {
			BiomeGenBase biomegenbase = biomeList[biomeId];
			return biomegenbase == null ? biome : biomegenbase;
		} else {
			logger.warn("Biome ID is out of bounds: " + biomeId + ", defaulting to 0 (Ocean)");
			return ocean;
		}
	}

	public static void doBootstrap() {
		ocean = (new BiomeGenOcean(0)).setColor(112).setBiomeName("Ocean").setHeight(height_Oceans);
		plains = (new BiomeGenPlains(1)).setColor(9286496).setBiomeName("Plains");
		desert = (new BiomeGenDesert(2)).setColor(16421912).setBiomeName("Desert").setDisableRain()
				.setTemperatureRainfall(2.0F, 0.0F).setHeight(height_LowPlains);
		extremeHills = (new BiomeGenHills(3, false)).setColor(6316128).setBiomeName("Extreme Hills")
				.setHeight(height_MidHills).setTemperatureRainfall(0.2F, 0.3F);
		forest = (new BiomeGenForest(4, 0)).setColor(353825).setBiomeName("Forest");
		taiga = (new BiomeGenTaiga(5, 0)).setColor(747097).setBiomeName("Taiga").setFillerBlockMetadata(5159473)
				.setTemperatureRainfall(0.25F, 0.8F).setHeight(height_MidPlains);
		swampland = (new BiomeGenSwamp(6)).setColor(522674).setBiomeName("Swampland").setFillerBlockMetadata(9154376)
				.setHeight(height_PartiallySubmerged).setTemperatureRainfall(0.8F, 0.9F);
		river = (new BiomeGenRiver(7)).setColor(255).setBiomeName("River").setHeight(height_ShallowWaters);
		hell = (new BiomeGenHell(8)).setColor(16711680).setBiomeName("Hell").setDisableRain()
				.setTemperatureRainfall(2.0F, 0.0F);
		sky = (new BiomeGenEnd(9)).setColor(8421631).setBiomeName("The End").setDisableRain();
		frozenOcean = (new BiomeGenOcean(10)).setColor(9474208).setBiomeName("FrozenOcean").setEnableSnow()
				.setHeight(height_Oceans).setTemperatureRainfall(0.0F, 0.5F);
		frozenRiver = (new BiomeGenRiver(11)).setColor(10526975).setBiomeName("FrozenRiver").setEnableSnow()
				.setHeight(height_ShallowWaters).setTemperatureRainfall(0.0F, 0.5F);
		icePlains = (new BiomeGenSnow(12, false)).setColor(16777215).setBiomeName("Ice Plains").setEnableSnow()
				.setTemperatureRainfall(0.0F, 0.5F).setHeight(height_LowPlains);
		iceMountains = (new BiomeGenSnow(13, false)).setColor(10526880).setBiomeName("Ice Mountains").setEnableSnow()
				.setHeight(height_LowHills).setTemperatureRainfall(0.0F, 0.5F);
		mushroomIsland = (new BiomeGenMushroomIsland(14)).setColor(16711935).setBiomeName("MushroomIsland")
				.setTemperatureRainfall(0.9F, 1.0F).setHeight(height_LowIslands);
		mushroomIslandShore = (new BiomeGenMushroomIsland(15)).setColor(10486015).setBiomeName("MushroomIslandShore")
				.setTemperatureRainfall(0.9F, 1.0F).setHeight(height_Shores);
		beach = (new BiomeGenBeach(16)).setColor(16440917).setBiomeName("Beach").setTemperatureRainfall(0.8F, 0.4F)
				.setHeight(height_Shores);
		desertHills = (new BiomeGenDesert(17)).setColor(13786898).setBiomeName("DesertHills").setDisableRain()
				.setTemperatureRainfall(2.0F, 0.0F).setHeight(height_LowHills);
		forestHills = (new BiomeGenForest(18, 0)).setColor(2250012).setBiomeName("ForestHills")
				.setHeight(height_LowHills);
		taigaHills = (new BiomeGenTaiga(19, 0)).setColor(1456435).setBiomeName("TaigaHills")
				.setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(height_LowHills);
		extremeHillsEdge = (new BiomeGenHills(20, true)).setColor(7501978).setBiomeName("Extreme Hills Edge")
				.setHeight(height_MidHills.attenuate()).setTemperatureRainfall(0.2F, 0.3F);
		jungle = (new BiomeGenJungle(21, false)).setColor(5470985).setBiomeName("Jungle")
				.setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.9F);
		jungleHills = (new BiomeGenJungle(22, false)).setColor(2900485).setBiomeName("JungleHills")
				.setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.9F).setHeight(height_LowHills);
		jungleEdge = (new BiomeGenJungle(23, true)).setColor(6458135).setBiomeName("JungleEdge")
				.setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.8F);
		deepOcean = (new BiomeGenOcean(24)).setColor(48).setBiomeName("Deep Ocean").setHeight(height_DeepOceans);
		stoneBeach = (new BiomeGenStoneBeach(25)).setColor(10658436).setBiomeName("Stone Beach")
				.setTemperatureRainfall(0.2F, 0.3F).setHeight(height_RockyWaters);
		coldBeach = (new BiomeGenBeach(26)).setColor(16445632).setBiomeName("Cold Beach")
				.setTemperatureRainfall(0.05F, 0.3F).setHeight(height_Shores).setEnableSnow();
		birchForest = (new BiomeGenForest(27, 2)).setBiomeName("Birch Forest").setColor(3175492);
		birchForestHills = (new BiomeGenForest(28, 2)).setBiomeName("Birch Forest Hills").setColor(2055986)
				.setHeight(height_LowHills);
		roofedForest = (new BiomeGenForest(29, 3)).setColor(4215066).setBiomeName("Roofed Forest");
		coldTaiga = (new BiomeGenTaiga(30, 0)).setColor(3233098).setBiomeName("Cold Taiga")
				.setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F)
				.setHeight(height_MidPlains).func_150563_c(16777215);
		coldTaigaHills = (new BiomeGenTaiga(31, 0)).setColor(2375478).setBiomeName("Cold Taiga Hills")
				.setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F)
				.setHeight(height_LowHills).func_150563_c(16777215);
		megaTaiga = (new BiomeGenTaiga(32, 1)).setColor(5858897).setBiomeName("Mega Taiga")
				.setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3F, 0.8F).setHeight(height_MidPlains);
		megaTaigaHills = (new BiomeGenTaiga(33, 1)).setColor(4542270).setBiomeName("Mega Taiga Hills")
				.setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3F, 0.8F).setHeight(height_LowHills);
		extremeHillsPlus = (new BiomeGenHills(34, true)).setColor(5271632).setBiomeName("Extreme Hills+")
				.setHeight(height_MidHills).setTemperatureRainfall(0.2F, 0.3F);
		savanna = (new BiomeGenSavanna(35)).setColor(12431967).setBiomeName("Savanna")
				.setTemperatureRainfall(1.2F, 0.0F).setDisableRain().setHeight(height_LowPlains);
		savannaPlateau = (new BiomeGenSavanna(36)).setColor(10984804).setBiomeName("Savanna Plateau")
				.setTemperatureRainfall(1.0F, 0.0F).setDisableRain().setHeight(height_HighPlateaus);
		mesa = (new BiomeGenMesa(37, false, false)).setColor(14238997).setBiomeName("Mesa");
		mesaPlateau_F = (new BiomeGenMesa(38, false, true)).setColor(11573093).setBiomeName("Mesa Plateau F")
				.setHeight(height_HighPlateaus);
		mesaPlateau = (new BiomeGenMesa(39, false, false)).setColor(13274213).setBiomeName("Mesa Plateau")
				.setHeight(height_HighPlateaus);
		field_180279_ad = ocean;

		plains.createMutation();
		desert.createMutation();
		forest.createMutation();
		taiga.createMutation();
		swampland.createMutation();
		icePlains.createMutation();
		jungle.createMutation();
		jungleEdge.createMutation();
		coldTaiga.createMutation();
		savanna.createMutation();
		savannaPlateau.createMutation();
		mesa.createMutation();
		mesaPlateau_F.createMutation();
		mesaPlateau.createMutation();
		birchForest.createMutation();
		birchForestHills.createMutation();
		roofedForest.createMutation();
		megaTaiga.createMutation();
		extremeHills.createMutation();
		extremeHillsPlus.createMutation();
		megaTaiga.createMutatedBiome(megaTaigaHills.biomeID + 128).setBiomeName("Redwood Taiga Hills M");

		explorationBiomesList.clear();

		for (int i = 0; i < biomeList.length; ++i) {
			BiomeGenBase biomegenbase = biomeList[i];
			if (biomegenbase != null) {
				if (BIOME_ID_MAP.containsKey(biomegenbase.biomeName)) {
					throw new Error("Biome \"" + biomegenbase.biomeName + "\" is defined as both ID "
							+ ((BiomeGenBase) BIOME_ID_MAP.get(biomegenbase.biomeName)).biomeID + " and "
							+ biomegenbase.biomeID);
				}

				BIOME_ID_MAP.put(biomegenbase.biomeName, biomegenbase);
				if (biomegenbase.biomeID < 128) {
					explorationBiomesList.add(biomegenbase);
				}
			}
		}

		explorationBiomesList.remove(hell);
		explorationBiomesList.remove(sky);
		explorationBiomesList.remove(frozenOcean);
		explorationBiomesList.remove(extremeHillsEdge);
		temperatureNoise = new NoiseGeneratorPerlin(new EaglercraftRandom(1234L), 1);
		GRASS_COLOR_NOISE = new NoiseGeneratorPerlin(new EaglercraftRandom(2345L), 1);
		DOUBLE_PLANT_GENERATOR = new WorldGenDoublePlant();
	}

	public static class Height {
		public float rootHeight;
		public float variation;

		public Height(float rootHeightIn, float variationIn) {
			this.rootHeight = rootHeightIn;
			this.variation = variationIn;
		}

		public BiomeGenBase.Height attenuate() {
			return new BiomeGenBase.Height(this.rootHeight * 0.8F, this.variation * 0.6F);
		}
	}

	public static class SpawnListEntry extends WeightedRandom.Item {
		public Class<? extends EntityLiving> entityClass;
		public int minGroupCount;
		public int maxGroupCount;

		public SpawnListEntry(Class<? extends EntityLiving> entityclassIn, int weight, int groupCountMin,
				int groupCountMax) {
			super(weight);
			this.entityClass = entityclassIn;
			this.minGroupCount = groupCountMin;
			this.maxGroupCount = groupCountMax;
		}

		public String toString() {
			return this.entityClass.getSimpleName() + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):"
					+ this.itemWeight;
		}
	}

	public static enum TempCategory {
		OCEAN, COLD, MEDIUM, WARM;
	}
}