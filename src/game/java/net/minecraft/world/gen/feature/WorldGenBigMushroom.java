package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.material.Material;
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
public class WorldGenBigMushroom extends WorldGenerator {
	private Block mushroomType;

	public WorldGenBigMushroom(Block parBlock) {
		super(true);
		this.mushroomType = parBlock;
	}

	public WorldGenBigMushroom() {
		super(false);
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		if (this.mushroomType == null) {
			this.mushroomType = random.nextBoolean() ? Blocks.brown_mushroom_block : Blocks.red_mushroom_block;
		}

		int i = random.nextInt(3) + 4;
		boolean flag = true;
		if (blockpos.getY() >= 1 && blockpos.getY() + i + 1 < 256) {
			for (int j = blockpos.getY(); j <= blockpos.getY() + 1 + i; ++j) {
				byte b0 = 3;
				if (j <= blockpos.getY() + 3) {
					b0 = 0;
				}

				BlockPos blockpos$mutableblockpos = new BlockPos();

				for (int k = blockpos.getX() - b0; k <= blockpos.getX() + b0 && flag; ++k) {
					for (int l = blockpos.getZ() - b0; l <= blockpos.getZ() + b0 && flag; ++l) {
						if (j >= 0 && j < 256) {
							Block block = world.getBlockState(blockpos$mutableblockpos.func_181079_c(k, j, l))
									.getBlock();
							if (block.getMaterial() != Material.air && block.getMaterial() != Material.leaves) {
								flag = false;
							}
						} else {
							flag = false;
						}
					}
				}
			}

			if (!flag) {
				return false;
			} else {
				Block block1 = world.getBlockState(blockpos.down()).getBlock();
				if (block1 != Blocks.dirt && block1 != Blocks.grass && block1 != Blocks.mycelium) {
					return false;
				} else {
					int j2 = blockpos.getY() + i;
					if (this.mushroomType == Blocks.red_mushroom_block) {
						j2 = blockpos.getY() + i - 3;
					}

					for (int k2 = j2; k2 <= blockpos.getY() + i; ++k2) {
						int i3 = 1;
						if (k2 < blockpos.getY() + i) {
							++i3;
						}

						if (this.mushroomType == Blocks.brown_mushroom_block) {
							i3 = 3;
						}

						int j3 = blockpos.getX() - i3;
						int k3 = blockpos.getX() + i3;
						int i1 = blockpos.getZ() - i3;
						int j1 = blockpos.getZ() + i3;

						for (int k1 = j3; k1 <= k3; ++k1) {
							for (int l1 = i1; l1 <= j1; ++l1) {
								int i2 = 5;
								if (k1 == j3) {
									--i2;
								} else if (k1 == k3) {
									++i2;
								}

								if (l1 == i1) {
									i2 -= 3;
								} else if (l1 == j1) {
									i2 += 3;
								}

								BlockHugeMushroom.EnumType blockhugemushroom$enumtype = BlockHugeMushroom.EnumType
										.byMetadata(i2);
								if (this.mushroomType == Blocks.brown_mushroom_block || k2 < blockpos.getY() + i) {
									if ((k1 == j3 || k1 == k3) && (l1 == i1 || l1 == j1)) {
										continue;
									}

									if (k1 == blockpos.getX() - (i3 - 1) && l1 == i1) {
										blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
									}

									if (k1 == j3 && l1 == blockpos.getZ() - (i3 - 1)) {
										blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
									}

									if (k1 == blockpos.getX() + (i3 - 1) && l1 == i1) {
										blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
									}

									if (k1 == k3 && l1 == blockpos.getZ() - (i3 - 1)) {
										blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
									}

									if (k1 == blockpos.getX() - (i3 - 1) && l1 == j1) {
										blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
									}

									if (k1 == j3 && l1 == blockpos.getZ() + (i3 - 1)) {
										blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
									}

									if (k1 == blockpos.getX() + (i3 - 1) && l1 == j1) {
										blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
									}

									if (k1 == k3 && l1 == blockpos.getZ() + (i3 - 1)) {
										blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
									}
								}

								if (blockhugemushroom$enumtype == BlockHugeMushroom.EnumType.CENTER
										&& k2 < blockpos.getY() + i) {
									blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.ALL_INSIDE;
								}

								if (blockpos.getY() >= blockpos.getY() + i - 1
										|| blockhugemushroom$enumtype != BlockHugeMushroom.EnumType.ALL_INSIDE) {
									BlockPos blockpos1 = new BlockPos(k1, k2, l1);
									if (!world.getBlockState(blockpos1).getBlock().isFullBlock()) {
										this.setBlockAndNotifyAdequately(world, blockpos1,
												this.mushroomType.getDefaultState().withProperty(
														BlockHugeMushroom.VARIANT, blockhugemushroom$enumtype));
									}
								}
							}
						}
					}

					for (int l2 = 0; l2 < i; ++l2) {
						Block block2 = world.getBlockState(blockpos.up(l2)).getBlock();
						if (!block2.isFullBlock()) {
							this.setBlockAndNotifyAdequately(world, blockpos.up(l2), this.mushroomType.getDefaultState()
									.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
						}
					}

					return true;
				}
			}
		} else {
			return false;
		}
	}
}