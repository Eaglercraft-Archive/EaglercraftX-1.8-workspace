package net.minecraft.world.gen;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

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
public class MapGenBase {
	/**+
	 * The number of Chunks to gen-check in any given direction.
	 */
	protected int range = 8;
	protected EaglercraftRandom rand;
	protected World worldObj;

	public MapGenBase() {
		this(true);
	}

	public MapGenBase(boolean scramble) {
		rand = new EaglercraftRandom(scramble);
	}

	public void generate(IChunkProvider chunkProviderIn, World worldIn, int x, int z, ChunkPrimer chunkPrimerIn) {
		int i = this.range;
		this.worldObj = worldIn;
		this.rand.setSeed(worldIn.getSeed());
		long j = this.rand.nextLong();
		long k = this.rand.nextLong();

		for (int l = x - i; l <= x + i; ++l) {
			for (int i1 = z - i; i1 <= z + i; ++i1) {
				long j1 = (long) l * j;
				long k1 = (long) i1 * k;
				this.rand.setSeed(j1 ^ k1 ^ worldIn.getSeed());
				this.recursiveGenerate(worldIn, l, i1, x, z, chunkPrimerIn);
			}
		}

	}

	/**+
	 * Recursively called by generate()
	 */
	protected void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, ChunkPrimer var6) {
	}
}