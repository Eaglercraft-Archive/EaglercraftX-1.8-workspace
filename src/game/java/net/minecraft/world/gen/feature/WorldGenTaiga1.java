package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
public class WorldGenTaiga1 extends WorldGenAbstractTree {
	private static final IBlockState field_181636_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT,
			BlockPlanks.EnumType.SPRUCE);
	private static final IBlockState field_181637_b = Blocks.leaves.getDefaultState()
			.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)
			.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

	public WorldGenTaiga1() {
		super(false);
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		int i = random.nextInt(5) + 7;
		int j = i - random.nextInt(2) - 3;
		int k = i - j;
		int l = 1 + random.nextInt(k + 1);
		boolean flag = true;
		if (blockpos.getY() >= 1 && blockpos.getY() + i + 1 <= 256) {
			for (int i1 = blockpos.getY(); i1 <= blockpos.getY() + 1 + i && flag; ++i1) {
				int j1 = 1;
				if (i1 - blockpos.getY() < j) {
					j1 = 0;
				} else {
					j1 = l;
				}

				BlockPos blockpos$mutableblockpos = new BlockPos();

				for (int k1 = blockpos.getX() - j1; k1 <= blockpos.getX() + j1 && flag; ++k1) {
					for (int l1 = blockpos.getZ() - j1; l1 <= blockpos.getZ() + j1 && flag; ++l1) {
						if (i1 >= 0 && i1 < 256) {
							if (!this.func_150523_a(world
									.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, i1, l1)).getBlock())) {
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
				Block block = world.getBlockState(blockpos.down()).getBlock();
				if ((block == Blocks.grass || block == Blocks.dirt) && blockpos.getY() < 256 - i - 1) {
					this.func_175921_a(world, blockpos.down());
					int k2 = 0;

					for (int l2 = blockpos.getY() + i; l2 >= blockpos.getY() + j; --l2) {
						for (int j3 = blockpos.getX() - k2; j3 <= blockpos.getX() + k2; ++j3) {
							int k3 = j3 - blockpos.getX();

							for (int i2 = blockpos.getZ() - k2; i2 <= blockpos.getZ() + k2; ++i2) {
								int j2 = i2 - blockpos.getZ();
								if (Math.abs(k3) != k2 || Math.abs(j2) != k2 || k2 <= 0) {
									BlockPos blockpos1 = new BlockPos(j3, l2, i2);
									if (!world.getBlockState(blockpos1).getBlock().isFullBlock()) {
										this.setBlockAndNotifyAdequately(world, blockpos1, field_181637_b);
									}
								}
							}
						}

						if (k2 >= 1 && l2 == blockpos.getY() + j + 1) {
							--k2;
						} else if (k2 < l) {
							++k2;
						}
					}

					for (int i3 = 0; i3 < i - 1; ++i3) {
						Block block1 = world.getBlockState(blockpos.up(i3)).getBlock();
						if (block1.getMaterial() == Material.air || block1.getMaterial() == Material.leaves) {
							this.setBlockAndNotifyAdequately(world, blockpos.up(i3), field_181636_a);
						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
}