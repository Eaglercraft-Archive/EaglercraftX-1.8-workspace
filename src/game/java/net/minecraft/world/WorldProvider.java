package net.minecraft.world;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderDebug;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.FlatGeneratorInfo;

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
public abstract class WorldProvider {
	public static final float[] moonPhaseFactors = new float[] { 1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F };
	protected World worldObj;
	private WorldType terrainType;
	private String generatorSettings;
	protected WorldChunkManager worldChunkMgr;
	protected boolean isHellWorld;
	protected boolean hasNoSky;
	/**+
	 * Light to brightness conversion table
	 */
	protected final float[] lightBrightnessTable = new float[16];
	protected int dimensionId;
	/**+
	 * Array for sunrise/sunset colors (RGBA)
	 */
	private final float[] colorsSunriseSunset = new float[4];

	/**+
	 * associate an existing world with a World provider, and setup
	 * its lightbrightness table
	 */
	public final void registerWorld(World worldIn) {
		this.worldObj = worldIn;
		this.terrainType = worldIn.getWorldInfo().getTerrainType();
		this.generatorSettings = worldIn.getWorldInfo().getGeneratorOptions();
		this.registerWorldChunkManager();
		this.generateLightBrightnessTable();
	}

	/**+
	 * Creates the light to brightness table
	 */
	protected void generateLightBrightnessTable() {
		float f = 0.0F;

		for (int i = 0; i <= 15; ++i) {
			float f1 = 1.0F - (float) i / 15.0F;
			this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
		}

	}

	/**+
	 * creates a new world chunk manager for WorldProvider
	 */
	protected void registerWorldChunkManager() {
		WorldType worldtype = this.worldObj.getWorldInfo().getTerrainType();
		if (worldtype == WorldType.FLAT) {
			FlatGeneratorInfo flatgeneratorinfo = FlatGeneratorInfo
					.createFlatGeneratorFromString(this.worldObj.getWorldInfo().getGeneratorOptions());
			this.worldChunkMgr = new WorldChunkManagerHell(
					BiomeGenBase.getBiomeFromBiomeList(flatgeneratorinfo.getBiome(), BiomeGenBase.field_180279_ad),
					0.5F);
		} else if (worldtype == WorldType.DEBUG_WORLD) {
			this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.0F);
		} else {
			this.worldChunkMgr = new WorldChunkManager(this.worldObj);
		}

	}

	/**+
	 * Returns a new chunk provider which generates chunks for this
	 * world
	 */
	public IChunkProvider createChunkGenerator() {
		return (IChunkProvider) (this.terrainType == WorldType.FLAT
				? new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(),
						this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings)
				: (this.terrainType == WorldType.DEBUG_WORLD ? new ChunkProviderDebug(this.worldObj)
						: (this.terrainType == WorldType.CUSTOMIZED
								? new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(),
										this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings)
								: new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(),
										this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings))));
	}

	/**+
	 * Will check if the x, z position specified is alright to be
	 * set as the map spawn point
	 */
	public boolean canCoordinateBeSpawn(int x, int z) {
		return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)) == Blocks.grass;
	}

	/**+
	 * Calculates the angle of sun and moon in the sky relative to a
	 * specified time (usually worldTime)
	 */
	public float calculateCelestialAngle(long parLong1, float parFloat1) {
		int i = (int) (parLong1 % 24000L);
		float f = ((float) i + parFloat1) / 24000.0F - 0.25F;
		if (f < 0.0F) {
			++f;
		}

		if (f > 1.0F) {
			--f;
		}

		float f2 = f;
		f = 1.0F - (float) ((Math.cos((double) f * 3.141592653589793D) + 1.0D) / 2.0D);
		f = f2 + (f - f2) / 3.0F;
		return f;
	}

	public int getMoonPhase(long parLong1) {
		return (int) (parLong1 / 24000L % 8L + 8L) % 8;
	}

	/**+
	 * Returns 'true' if in the "main surface world", but 'false' if
	 * in the Nether or End dimensions.
	 */
	public boolean isSurfaceWorld() {
		return true;
	}

	/**+
	 * Returns array with sunrise/sunset colors
	 */
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
		float f = 0.4F;
		float f1 = MathHelper.cos(celestialAngle * 3.1415927F * 2.0F) - 0.0F;
		float f2 = 0.0F;
		if (f1 >= f2 - f && f1 <= f2 + f) {
			float f3 = (f1 - f2) / f * 0.5F + 0.5F;
			float f4 = 1.0F - (1.0F - MathHelper.sin(f3 * 3.1415927F)) * 0.99F;
			f4 = f4 * f4;
			this.colorsSunriseSunset[0] = f3 * 0.3F + 0.7F;
			this.colorsSunriseSunset[1] = f3 * f3 * 0.7F + 0.2F;
			this.colorsSunriseSunset[2] = f3 * f3 * 0.0F + 0.2F;
			this.colorsSunriseSunset[3] = f4;
			return this.colorsSunriseSunset;
		} else {
			return null;
		}
	}

	/**+
	 * Return Vec3D with biome specific fog color
	 */
	public Vec3 getFogColor(float parFloat1, float parFloat2) {
		float f = MathHelper.cos(parFloat1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
		f = MathHelper.clamp_float(f, 0.0F, 1.0F);
		float f1 = 0.7529412F;
		float f2 = 0.84705883F;
		float f3 = 1.0F;
		f1 = f1 * (f * 0.94F + 0.06F);
		f2 = f2 * (f * 0.94F + 0.06F);
		f3 = f3 * (f * 0.91F + 0.09F);
		return new Vec3((double) f1, (double) f2, (double) f3);
	}

	/**+
	 * True if the player can respawn in this dimension (true =
	 * overworld, false = nether).
	 */
	public boolean canRespawnHere() {
		return true;
	}

	public static WorldProvider getProviderForDimension(int dimension) {
		return (WorldProvider) (dimension == -1 ? new WorldProviderHell()
				: (dimension == 0 ? new WorldProviderSurface() : (dimension == 1 ? new WorldProviderEnd() : null)));
	}

	/**+
	 * the y level at which clouds are rendered.
	 */
	public float getCloudHeight() {
		return 128.0F;
	}

	public boolean isSkyColored() {
		return true;
	}

	public BlockPos getSpawnCoordinate() {
		return null;
	}

	public int getAverageGroundLevel() {
		return this.terrainType == WorldType.FLAT ? 4 : this.worldObj.func_181545_F() + 1;
	}

	/**+
	 * Returns a double value representing the Y value relative to
	 * the top of the map at which void fog is at its maximum. The
	 * default factor of 0.03125 relative to 256, for example, means
	 * the void fog will be at its maximum at (256*0.03125), or 8.
	 */
	public double getVoidFogYFactor() {
		return this.terrainType == WorldType.FLAT ? 1.0D : 0.03125D;
	}

	/**+
	 * Returns true if the given X,Z coordinate should show
	 * environmental fog.
	 */
	public boolean doesXZShowFog(int x, int z) {
		return false;
	}

	public abstract String getDimensionName();

	public abstract String getInternalNameSuffix();

	public WorldChunkManager getWorldChunkManager() {
		return this.worldChunkMgr;
	}

	public boolean doesWaterVaporize() {
		return this.isHellWorld;
	}

	public boolean getHasNoSky() {
		return this.hasNoSky;
	}

	public float[] getLightBrightnessTable() {
		return this.lightBrightnessTable;
	}

	/**+
	 * Gets the dimension of the provider
	 */
	public int getDimensionId() {
		return this.dimensionId;
	}

	public WorldBorder getWorldBorder() {
		return new WorldBorder();
	}
}