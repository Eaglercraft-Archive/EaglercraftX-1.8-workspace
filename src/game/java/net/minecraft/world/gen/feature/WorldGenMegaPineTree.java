package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
public class WorldGenMegaPineTree extends WorldGenHugeTrees {
	private static final IBlockState field_181633_e = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT,
			BlockPlanks.EnumType.SPRUCE);
	private static final IBlockState field_181634_f = Blocks.leaves.getDefaultState()
			.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)
			.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
	private static final IBlockState field_181635_g = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT,
			BlockDirt.DirtType.PODZOL);
	private boolean useBaseHeight;

	public WorldGenMegaPineTree(boolean parFlag, boolean parFlag2) {
		super(parFlag, 13, 15, field_181633_e, field_181634_f);
		this.useBaseHeight = parFlag2;
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		int i = this.func_150533_a(random);
		if (!this.func_175929_a(world, random, blockpos, i)) {
			return false;
		} else {
			this.func_150541_c(world, blockpos.getX(), blockpos.getZ(), blockpos.getY() + i, 0, random);

			for (int j = 0; j < i; ++j) {
				Block block = world.getBlockState(blockpos.up(j)).getBlock();
				if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
					this.setBlockAndNotifyAdequately(world, blockpos.up(j), this.woodMetadata);
				}

				if (j < i - 1) {
					block = world.getBlockState(blockpos.add(1, j, 0)).getBlock();
					if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
						this.setBlockAndNotifyAdequately(world, blockpos.add(1, j, 0), this.woodMetadata);
					}

					block = world.getBlockState(blockpos.add(1, j, 1)).getBlock();
					if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
						this.setBlockAndNotifyAdequately(world, blockpos.add(1, j, 1), this.woodMetadata);
					}

					block = world.getBlockState(blockpos.add(0, j, 1)).getBlock();
					if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves) {
						this.setBlockAndNotifyAdequately(world, blockpos.add(0, j, 1), this.woodMetadata);
					}
				}
			}

			return true;
		}
	}

	private void func_150541_c(World worldIn, int parInt1, int parInt2, int parInt3, int parInt4,
			EaglercraftRandom parRandom) {
		int i = parRandom.nextInt(5) + (this.useBaseHeight ? this.baseHeight : 3);
		int j = 0;

		for (int k = parInt3 - i; k <= parInt3; ++k) {
			int l = parInt3 - k;
			int i1 = parInt4 + MathHelper.floor_float((float) l / (float) i * 3.5F);
			this.func_175925_a(worldIn, new BlockPos(parInt1, k, parInt2),
					i1 + (l > 0 && i1 == j && (k & 1) == 0 ? 1 : 0));
			j = i1;
		}

	}

	public void func_180711_a(World world, EaglercraftRandom random, BlockPos blockpos) {
		this.func_175933_b(world, blockpos.west().north());
		this.func_175933_b(world, blockpos.east(2).north());
		this.func_175933_b(world, blockpos.west().south(2));
		this.func_175933_b(world, blockpos.east(2).south(2));

		for (int i = 0; i < 5; ++i) {
			int j = random.nextInt(64);
			int k = j % 8;
			int l = j / 8;
			if (k == 0 || k == 7 || l == 0 || l == 7) {
				this.func_175933_b(world, blockpos.add(-3 + k, 0, -3 + l));
			}
		}

	}

	private void func_175933_b(World worldIn, BlockPos parBlockPos) {
		for (int i = -2; i <= 2; ++i) {
			for (int j = -2; j <= 2; ++j) {
				if (Math.abs(i) != 2 || Math.abs(j) != 2) {
					this.func_175934_c(worldIn, parBlockPos.add(i, 0, j));
				}
			}
		}

	}

	private void func_175934_c(World worldIn, BlockPos parBlockPos) {
		for (int i = 2; i >= -3; --i) {
			BlockPos blockpos = parBlockPos.up(i);
			Block block = worldIn.getBlockState(blockpos).getBlock();
			if (block == Blocks.grass || block == Blocks.dirt) {
				this.setBlockAndNotifyAdequately(worldIn, blockpos, field_181635_g);
				break;
			}

			if (block.getMaterial() != Material.air && i < 0) {
				break;
			}
		}

	}
}