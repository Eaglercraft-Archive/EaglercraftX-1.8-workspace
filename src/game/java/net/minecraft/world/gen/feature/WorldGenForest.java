package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
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
public class WorldGenForest extends WorldGenAbstractTree {
	private static final IBlockState field_181629_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT,
			BlockPlanks.EnumType.BIRCH);
	private static final IBlockState field_181630_b = Blocks.leaves.getDefaultState()
			.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH)
			.withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false));
	private boolean useExtraRandomHeight;

	public WorldGenForest(boolean parFlag, boolean parFlag2) {
		super(parFlag);
		this.useExtraRandomHeight = parFlag2;
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		int i = random.nextInt(3) + 5;
		if (this.useExtraRandomHeight) {
			i += random.nextInt(7);
		}

		boolean flag = true;
		if (blockpos.getY() >= 1 && blockpos.getY() + i + 1 <= 256) {
			for (int j = blockpos.getY(); j <= blockpos.getY() + 1 + i; ++j) {
				byte b0 = 1;
				if (j == blockpos.getY()) {
					b0 = 0;
				}

				if (j >= blockpos.getY() + 1 + i - 2) {
					b0 = 2;
				}

				BlockPos blockpos$mutableblockpos = new BlockPos();

				for (int k = blockpos.getX() - b0; k <= blockpos.getX() + b0 && flag; ++k) {
					for (int l = blockpos.getZ() - b0; l <= blockpos.getZ() + b0 && flag; ++l) {
						if (j >= 0 && j < 256) {
							if (!this.func_150523_a(
									world.getBlockState(blockpos$mutableblockpos.func_181079_c(k, j, l)).getBlock())) {
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
				if ((block1 == Blocks.grass || block1 == Blocks.dirt || block1 == Blocks.farmland)
						&& blockpos.getY() < 256 - i - 1) {
					this.func_175921_a(world, blockpos.down());

					for (int l1 = blockpos.getY() - 3 + i; l1 <= blockpos.getY() + i; ++l1) {
						int j2 = l1 - (blockpos.getY() + i);
						int k2 = 1 - j2 / 2;

						for (int l2 = blockpos.getX() - k2; l2 <= blockpos.getX() + k2; ++l2) {
							int i1 = l2 - blockpos.getX();

							for (int j1 = blockpos.getZ() - k2; j1 <= blockpos.getZ() + k2; ++j1) {
								int k1 = j1 - blockpos.getZ();
								if (Math.abs(i1) != k2 || Math.abs(k1) != k2 || random.nextInt(2) != 0 && j2 != 0) {
									BlockPos blockpos1 = new BlockPos(l2, l1, j1);
									Block block = world.getBlockState(blockpos1).getBlock();
									if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
										this.setBlockAndNotifyAdequately(world, blockpos1, field_181630_b);
									}
								}
							}
						}
					}

					for (int i2 = 0; i2 < i; ++i2) {
						Block block2 = world.getBlockState(blockpos.up(i2)).getBlock();
						if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves) {
							this.setBlockAndNotifyAdequately(world, blockpos.up(i2), field_181629_a);
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