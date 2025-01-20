package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

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
public class WorldGenLakes extends WorldGenerator {
	private Block block;

	public WorldGenLakes(Block blockIn) {
		this.block = blockIn;
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		for (blockpos = blockpos.add(-8, 0, -8); blockpos.getY() > 5
				&& world.isAirBlock(blockpos); blockpos = blockpos.down()) {
			;
		}

		if (blockpos.getY() <= 4) {
			return false;
		} else {
			blockpos = blockpos.down(4);
			boolean[] aboolean = new boolean[2048];
			int i = random.nextInt(4) + 4;

			for (int j = 0; j < i; ++j) {
				double d0 = random.nextDouble() * 6.0D + 3.0D;
				double d1 = random.nextDouble() * 4.0D + 2.0D;
				double d2 = random.nextDouble() * 6.0D + 3.0D;
				double d3 = random.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
				double d4 = random.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
				double d5 = random.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

				for (int l = 1; l < 15; ++l) {
					for (int i1 = 1; i1 < 15; ++i1) {
						for (int j1 = 1; j1 < 7; ++j1) {
							double d6 = ((double) l - d3) / (d0 / 2.0D);
							double d7 = ((double) j1 - d4) / (d1 / 2.0D);
							double d8 = ((double) i1 - d5) / (d2 / 2.0D);
							double d9 = d6 * d6 + d7 * d7 + d8 * d8;
							if (d9 < 1.0D) {
								aboolean[(l * 16 + i1) * 8 + j1] = true;
							}
						}
					}
				}
			}

			for (int k1 = 0; k1 < 16; ++k1) {
				for (int l2 = 0; l2 < 16; ++l2) {
					for (int k = 0; k < 8; ++k) {
						boolean flag = !aboolean[(k1 * 16 + l2) * 8 + k]
								&& (k1 < 15 && aboolean[((k1 + 1) * 16 + l2) * 8 + k]
										|| k1 > 0 && aboolean[((k1 - 1) * 16 + l2) * 8 + k]
										|| l2 < 15 && aboolean[(k1 * 16 + l2 + 1) * 8 + k]
										|| l2 > 0 && aboolean[(k1 * 16 + (l2 - 1)) * 8 + k]
										|| k < 7 && aboolean[(k1 * 16 + l2) * 8 + k + 1]
										|| k > 0 && aboolean[(k1 * 16 + l2) * 8 + (k - 1)]);
						if (flag) {
							Material material = world.getBlockState(blockpos.add(k1, k, l2)).getBlock().getMaterial();
							if (k >= 4 && material.isLiquid()) {
								return false;
							}

							if (k < 4 && !material.isSolid()
									&& world.getBlockState(blockpos.add(k1, k, l2)).getBlock() != this.block) {
								return false;
							}
						}
					}
				}
			}

			for (int l1 = 0; l1 < 16; ++l1) {
				for (int i3 = 0; i3 < 16; ++i3) {
					for (int i4 = 0; i4 < 8; ++i4) {
						if (aboolean[(l1 * 16 + i3) * 8 + i4]) {
							world.setBlockState(blockpos.add(l1, i4, i3),
									i4 >= 4 ? Blocks.air.getDefaultState() : this.block.getDefaultState(), 2);
						}
					}
				}
			}

			for (int i2 = 0; i2 < 16; ++i2) {
				for (int j3 = 0; j3 < 16; ++j3) {
					for (int j4 = 4; j4 < 8; ++j4) {
						if (aboolean[(i2 * 16 + j3) * 8 + j4]) {
							BlockPos blockpos1 = blockpos.add(i2, j4 - 1, j3);
							if (world.getBlockState(blockpos1).getBlock() == Blocks.dirt
									&& world.getLightFor(EnumSkyBlock.SKY, blockpos.add(i2, j4, j3)) > 0) {
								BiomeGenBase biomegenbase = world.getBiomeGenForCoords(blockpos1);
								if (biomegenbase.topBlock.getBlock() == Blocks.mycelium) {
									world.setBlockState(blockpos1, Blocks.mycelium.getDefaultState(), 2);
								} else {
									world.setBlockState(blockpos1, Blocks.grass.getDefaultState(), 2);
								}
							}
						}
					}
				}
			}

			if (this.block.getMaterial() == Material.lava) {
				for (int j2 = 0; j2 < 16; ++j2) {
					for (int k3 = 0; k3 < 16; ++k3) {
						for (int k4 = 0; k4 < 8; ++k4) {
							boolean flag1 = !aboolean[(j2 * 16 + k3) * 8 + k4]
									&& (j2 < 15 && aboolean[((j2 + 1) * 16 + k3) * 8 + k4]
											|| j2 > 0 && aboolean[((j2 - 1) * 16 + k3) * 8 + k4]
											|| k3 < 15 && aboolean[(j2 * 16 + k3 + 1) * 8 + k4]
											|| k3 > 0 && aboolean[(j2 * 16 + (k3 - 1)) * 8 + k4]
											|| k4 < 7 && aboolean[(j2 * 16 + k3) * 8 + k4 + 1]
											|| k4 > 0 && aboolean[(j2 * 16 + k3) * 8 + (k4 - 1)]);
							if (flag1 && (k4 < 4 || random.nextInt(2) != 0) && world
									.getBlockState(blockpos.add(j2, k4, k3)).getBlock().getMaterial().isSolid()) {
								world.setBlockState(blockpos.add(j2, k4, k3), Blocks.stone.getDefaultState(), 2);
							}
						}
					}
				}
			}

			if (this.block.getMaterial() == Material.water) {
				for (int k2 = 0; k2 < 16; ++k2) {
					for (int l3 = 0; l3 < 16; ++l3) {
						byte b0 = 4;
						if (world.canBlockFreezeWater(blockpos.add(k2, b0, l3))) {
							world.setBlockState(blockpos.add(k2, b0, l3), Blocks.ice.getDefaultState(), 2);
						}
					}
				}
			}

			return true;
		}
	}
}