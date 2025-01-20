package net.minecraft.world;

import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderHell;

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
public class WorldProviderHell extends WorldProvider {
	/**+
	 * creates a new world chunk manager for WorldProvider
	 */
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 0.0F);
		this.isHellWorld = true;
		this.hasNoSky = true;
		this.dimensionId = -1;
	}

	/**+
	 * Return Vec3D with biome specific fog color
	 */
	public Vec3 getFogColor(float var1, float var2) {
		return new Vec3(0.20000000298023224D, 0.029999999329447746D, 0.029999999329447746D);
	}

	/**+
	 * Creates the light to brightness table
	 */
	protected void generateLightBrightnessTable() {
		float f = 0.1F;

		for (int i = 0; i <= 15; ++i) {
			float f1 = 1.0F - (float) i / 15.0F;
			this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
		}

	}

	/**+
	 * Returns a new chunk provider which generates chunks for this
	 * world
	 */
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderHell(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(),
				this.worldObj.getSeed());
	}

	/**+
	 * Returns 'true' if in the "main surface world", but 'false' if
	 * in the Nether or End dimensions.
	 */
	public boolean isSurfaceWorld() {
		return false;
	}

	/**+
	 * Will check if the x, z position specified is alright to be
	 * set as the map spawn point
	 */
	public boolean canCoordinateBeSpawn(int var1, int var2) {
		return false;
	}

	/**+
	 * Calculates the angle of sun and moon in the sky relative to a
	 * specified time (usually worldTime)
	 */
	public float calculateCelestialAngle(long var1, float var3) {
		return 0.5F;
	}

	/**+
	 * True if the player can respawn in this dimension (true =
	 * overworld, false = nether).
	 */
	public boolean canRespawnHere() {
		return false;
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
		return "Nether";
	}

	public String getInternalNameSuffix() {
		return "_nether";
	}

	public WorldBorder getWorldBorder() {
		return new WorldBorder() {
			public double getCenterX() {
				return super.getCenterX() / 8.0D;
			}

			public double getCenterZ() {
				return super.getCenterZ() / 8.0D;
			}
		};
	}
}