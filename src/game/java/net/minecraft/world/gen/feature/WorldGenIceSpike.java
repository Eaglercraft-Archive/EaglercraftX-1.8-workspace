package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
public class WorldGenIceSpike extends WorldGenerator {
	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		while (world.isAirBlock(blockpos) && blockpos.getY() > 2) {
			blockpos = blockpos.down();
		}

		if (world.getBlockState(blockpos).getBlock() != Blocks.snow) {
			return false;
		} else {
			blockpos = blockpos.up(random.nextInt(4));
			int i = random.nextInt(4) + 7;
			int j = i / 4 + random.nextInt(2);
			if (j > 1 && random.nextInt(60) == 0) {
				blockpos = blockpos.up(10 + random.nextInt(30));
			}

			for (int k = 0; k < i; ++k) {
				float f = (1.0F - (float) k / (float) i) * (float) j;
				int l = MathHelper.ceiling_float_int(f);

				for (int i1 = -l; i1 <= l; ++i1) {
					float f1 = (float) MathHelper.abs_int(i1) - 0.25F;

					for (int j1 = -l; j1 <= l; ++j1) {
						float f2 = (float) MathHelper.abs_int(j1) - 0.25F;
						if ((i1 == 0 && j1 == 0 || f1 * f1 + f2 * f2 <= f * f)
								&& (i1 != -l && i1 != l && j1 != -l && j1 != l || random.nextFloat() <= 0.75F)) {
							Block block = world.getBlockState(blockpos.add(i1, k, j1)).getBlock();
							if (block.getMaterial() == Material.air || block == Blocks.dirt || block == Blocks.snow
									|| block == Blocks.ice) {
								this.setBlockAndNotifyAdequately(world, blockpos.add(i1, k, j1),
										Blocks.packed_ice.getDefaultState());
							}

							if (k != 0 && l > 1) {
								block = world.getBlockState(blockpos.add(i1, -k, j1)).getBlock();
								if (block.getMaterial() == Material.air || block == Blocks.dirt || block == Blocks.snow
										|| block == Blocks.ice) {
									this.setBlockAndNotifyAdequately(world, blockpos.add(i1, -k, j1),
											Blocks.packed_ice.getDefaultState());
								}
							}
						}
					}
				}
			}

			int k1 = j - 1;
			if (k1 < 0) {
				k1 = 0;
			} else if (k1 > 1) {
				k1 = 1;
			}

			for (int l1 = -k1; l1 <= k1; ++l1) {
				for (int i2 = -k1; i2 <= k1; ++i2) {
					BlockPos blockpos1 = blockpos.add(l1, -1, i2);
					int j2 = 50;
					if (Math.abs(l1) == 1 && Math.abs(i2) == 1) {
						j2 = random.nextInt(5);
					}

					while (blockpos1.getY() > 50) {
						Block block1 = world.getBlockState(blockpos1).getBlock();
						if (block1.getMaterial() != Material.air && block1 != Blocks.dirt && block1 != Blocks.snow
								&& block1 != Blocks.ice && block1 != Blocks.packed_ice) {
							break;
						}

						this.setBlockAndNotifyAdequately(world, blockpos1, Blocks.packed_ice.getDefaultState());
						blockpos1 = blockpos1.down();
						--j2;
						if (j2 <= 0) {
							blockpos1 = blockpos1.down(random.nextInt(5) + 1);
							j2 = random.nextInt(5);
						}
					}
				}
			}

			return true;
		}
	}
}