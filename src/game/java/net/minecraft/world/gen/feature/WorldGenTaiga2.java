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
public class WorldGenTaiga2 extends WorldGenAbstractTree {
	private static final IBlockState field_181645_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT,
			BlockPlanks.EnumType.SPRUCE);
	private static final IBlockState field_181646_b = Blocks.leaves.getDefaultState()
			.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)
			.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

	public WorldGenTaiga2(boolean parFlag) {
		super(parFlag);
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		int i = random.nextInt(4) + 6;
		int j = 1 + random.nextInt(2);
		int k = i - j;
		int l = 2 + random.nextInt(2);
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
							Block block = world.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, i1, l1))
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
				if ((block1 == Blocks.grass || block1 == Blocks.dirt || block1 == Blocks.farmland)
						&& blockpos.getY() < 256 - i - 1) {
					this.func_175921_a(world, blockpos.down());
					int i3 = random.nextInt(2);
					int j3 = 1;
					byte b0 = 0;

					for (int k3 = 0; k3 <= k; ++k3) {
						int i4 = blockpos.getY() + i - k3;

						for (int i2 = blockpos.getX() - i3; i2 <= blockpos.getX() + i3; ++i2) {
							int j2 = i2 - blockpos.getX();

							for (int k2 = blockpos.getZ() - i3; k2 <= blockpos.getZ() + i3; ++k2) {
								int l2 = k2 - blockpos.getZ();
								if (Math.abs(j2) != i3 || Math.abs(l2) != i3 || i3 <= 0) {
									BlockPos blockpos1 = new BlockPos(i2, i4, k2);
									if (!world.getBlockState(blockpos1).getBlock().isFullBlock()) {
										this.setBlockAndNotifyAdequately(world, blockpos1, field_181646_b);
									}
								}
							}
						}

						if (i3 >= j3) {
							i3 = b0;
							b0 = 1;
							++j3;
							if (j3 > l) {
								j3 = l;
							}
						} else {
							++i3;
						}
					}

					int l3 = random.nextInt(3);

					for (int j4 = 0; j4 < i - l3; ++j4) {
						Block block2 = world.getBlockState(blockpos.up(j4)).getBlock();
						if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves) {
							this.setBlockAndNotifyAdequately(world, blockpos.up(j4), field_181645_a);
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