package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

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
public class WorldGenBlockBlob extends WorldGenerator {
	private final Block field_150545_a;
	private final int field_150544_b;

	public WorldGenBlockBlob(Block parBlock, int parInt1) {
		super(false);
		this.field_150545_a = parBlock;
		this.field_150544_b = parInt1;
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		while (true) {
			label0: {
				if (blockpos.getY() > 3) {
					if (world.isAirBlock(blockpos.down())) {
						break label0;
					}

					Block block = world.getBlockState(blockpos.down()).getBlock();
					if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone) {
						break label0;
					}
				}

				if (blockpos.getY() <= 3) {
					return false;
				}

				int i1 = this.field_150544_b;

				for (int i = 0; i1 >= 0 && i < 3; ++i) {
					int j = i1 + random.nextInt(2);
					int k = i1 + random.nextInt(2);
					int l = i1 + random.nextInt(2);
					float f = (float) (j + k + l) * 0.333F + 0.5F;

					for (BlockPos blockpos1 : BlockPos.getAllInBoxMutable(blockpos.add(-j, -k, -l),
							blockpos.add(j, k, l))) {
						if (blockpos1.distanceSq(blockpos) <= (double) (f * f)) {
							world.setBlockState(blockpos1, this.field_150545_a.getDefaultState(), 4);
						}
					}

					blockpos = blockpos.add(-(i1 + 1) + random.nextInt(2 + i1 * 2), 0 - random.nextInt(2),
							-(i1 + 1) + random.nextInt(2 + i1 * 2));
				}

				return true;
			}

			blockpos = blockpos.down();
		}
	}
}