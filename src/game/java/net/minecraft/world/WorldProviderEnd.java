package net.minecraft.world;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;

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
public class WorldProviderEnd extends WorldProvider {
	/**+
	 * creates a new world chunk manager for WorldProvider
	 */
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F);
		this.dimensionId = 1;
		this.hasNoSky = true;
	}

	/**+
	 * Returns a new chunk provider which generates chunks for this
	 * world
	 */
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
	}

	/**+
	 * Calculates the angle of sun and moon in the sky relative to a
	 * specified time (usually worldTime)
	 */
	public float calculateCelestialAngle(long var1, float var3) {
		return 0.0F;
	}

	/**+
	 * Returns array with sunrise/sunset colors
	 */
	public float[] calcSunriseSunsetColors(float var1, float var2) {
		return null;
	}

	/**+
	 * Return Vec3D with biome specific fog color
	 */
	public Vec3 getFogColor(float f, float var2) {
		int i = 10518688;
		float f1 = MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F;
		f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
		float f2 = (float) (i >> 16 & 255) / 255.0F;
		float f3 = (float) (i >> 8 & 255) / 255.0F;
		float f4 = (float) (i & 255) / 255.0F;
		f2 = f2 * (f1 * 0.0F + 0.15F);
		f3 = f3 * (f1 * 0.0F + 0.15F);
		f4 = f4 * (f1 * 0.0F + 0.15F);
		return new Vec3((double) f2, (double) f3, (double) f4);
	}

	public boolean isSkyColored() {
		return false;
	}

	/**+
	 * True if the player can respawn in this dimension (true =
	 * overworld, false = nether).
	 */
	public boolean canRespawnHere() {
		return false;
	}

	/**+
	 * Returns 'true' if in the "main surface world", but 'false' if
	 * in the Nether or End dimensions.
	 */
	public boolean isSurfaceWorld() {
		return false;
	}

	/**+
	 * the y level at which clouds are rendered.
	 */
	public float getCloudHeight() {
		return 8.0F;
	}

	/**+
	 * Will check if the x, z position specified is alright to be
	 * set as the map spawn point
	 */
	public boolean canCoordinateBeSpawn(int i, int j) {
		return this.worldObj.getGroundAboveSeaLevel(new BlockPos(i, 0, j)).getMaterial().blocksMovement();
	}

	public BlockPos getSpawnCoordinate() {
		return new BlockPos(100, 50, 0);
	}

	public int getAverageGroundLevel() {
		return 50;
	}

	/**+
	 * Returns true if the given X,Z coordinate should show
	 * environmental fog.
	 */
	public boolean doesXZShowFog(int var1, int var2) {
		return true;
	}

	/**+
	 * Returns the dimension's name, e.g. "The End", "Nether", or
	 * "Overworld".
	 */
	public String getDimensionName() {
		return "The End";
	}

	public String getInternalNameSuffix() {
		return "_end";
	}
}