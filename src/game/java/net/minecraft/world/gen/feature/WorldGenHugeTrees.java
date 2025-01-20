package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
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
public abstract class WorldGenHugeTrees extends WorldGenAbstractTree {
	protected final int baseHeight;
	protected final IBlockState woodMetadata;
	protected final IBlockState leavesMetadata;
	protected int extraRandomHeight;

	public WorldGenHugeTrees(boolean parFlag, int parInt1, int parInt2, IBlockState parIBlockState,
			IBlockState parIBlockState2) {
		super(parFlag);
		this.baseHeight = parInt1;
		this.extraRandomHeight = parInt2;
		this.woodMetadata = parIBlockState;
		this.leavesMetadata = parIBlockState2;
	}

	protected int func_150533_a(EaglercraftRandom parRandom) {
		int i = parRandom.nextInt(3) + this.baseHeight;
		if (this.extraRandomHeight > 1) {
			i += parRandom.nextInt(this.extraRandomHeight);
		}

		return i;
	}

	private boolean func_175926_c(World worldIn, BlockPos parBlockPos, int parInt1) {
		boolean flag = true;
		if (parBlockPos.getY() >= 1 && parBlockPos.getY() + parInt1 + 1 <= 256) {
			for (int i = 0; i <= 1 + parInt1; ++i) {
				byte b0 = 2;
				if (i == 0) {
					b0 = 1;
				} else if (i >= 1 + parInt1 - 2) {
					b0 = 2;
				}

				for (int j = -b0; j <= b0 && flag; ++j) {
					for (int k = -b0; k <= b0 && flag; ++k) {
						if (parBlockPos.getY() + i < 0 || parBlockPos.getY() + i >= 256
								|| !this.func_150523_a(worldIn.getBlockState(parBlockPos.add(j, i, k)).getBlock())) {
							flag = false;
						}
					}
				}
			}

			return flag;
		} else {
			return false;
		}
	}

	private boolean func_175927_a(BlockPos worldIn, World parWorld) {
		BlockPos blockpos = worldIn.down();
		Block block = parWorld.getBlockState(blockpos).getBlock();
		if ((block == Blocks.grass || block == Blocks.dirt) && worldIn.getY() >= 2) {
			this.func_175921_a(parWorld, blockpos);
			this.func_175921_a(parWorld, blockpos.east());
			this.func_175921_a(parWorld, blockpos.south());
			this.func_175921_a(parWorld, blockpos.south().east());
			return true;
		} else {
			return false;
		}
	}

	protected boolean func_175929_a(World worldIn, EaglercraftRandom parRandom, BlockPos parBlockPos, int parInt1) {
		return this.func_175926_c(worldIn, parBlockPos, parInt1) && this.func_175927_a(parBlockPos, worldIn);
	}

	protected void func_175925_a(World worldIn, BlockPos parBlockPos, int parInt1) {
		int i = parInt1 * parInt1;

		for (int j = -parInt1; j <= parInt1 + 1; ++j) {
			for (int k = -parInt1; k <= parInt1 + 1; ++k) {
				int l = j - 1;
				int i1 = k - 1;
				if (j * j + k * k <= i || l * l + i1 * i1 <= i || j * j + i1 * i1 <= i || l * l + k * k <= i) {
					BlockPos blockpos = parBlockPos.add(j, 0, k);
					Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
					if (material == Material.air || material == Material.leaves) {
						this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
					}
				}
			}
		}

	}

	protected void func_175928_b(World worldIn, BlockPos parBlockPos, int parInt1) {
		int i = parInt1 * parInt1;

		for (int j = -parInt1; j <= parInt1; ++j) {
			for (int k = -parInt1; k <= parInt1; ++k) {
				if (j * j + k * k <= i) {
					BlockPos blockpos = parBlockPos.add(j, 0, k);
					Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
					if (material == Material.air || material == Material.leaves) {
						this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
					}
				}
			}
		}

	}
}