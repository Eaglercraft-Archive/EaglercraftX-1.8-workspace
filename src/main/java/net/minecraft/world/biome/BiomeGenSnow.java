package net.minecraft.world.biome;

import net.minecraft.init.Blocks;

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
public class BiomeGenSnow extends BiomeGenBase {
	private boolean field_150615_aC;

	public BiomeGenSnow(int parInt1, boolean parFlag) {
		super(parInt1);
		this.field_150615_aC = parFlag;
		if (parFlag) {
			this.topBlock = Blocks.snow.getDefaultState();
		}

		this.spawnableCreatureList.clear();
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