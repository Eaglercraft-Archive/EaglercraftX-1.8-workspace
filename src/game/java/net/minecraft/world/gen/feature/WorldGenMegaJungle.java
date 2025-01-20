package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
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
public class WorldGenMegaJungle extends WorldGenHugeTrees {
	public WorldGenMegaJungle(boolean parFlag, int parInt1, int parInt2, IBlockState parIBlockState,
			IBlockState parIBlockState2) {
		super(parFlag, parInt1, parInt2, parIBlockState, parIBlockState2);
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		int i = this.func_150533_a(random);
		if (!this.func_175929_a(world, random, blockpos, i)) {
			return false;
		} else {
			this.func_175930_c(world, blockpos.up(i), 2);

			for (int j = blockpos.getY() + i - 2 - random.nextInt(4); j > blockpos.getY() + i / 2; j -= 2
					+ random.nextInt(4)) {
				float f = random.nextFloat() * 3.1415927F * 2.0F;
				int k = blockpos.getX() + (int) (0.5F + MathHelper.cos(f) * 4.0F);
				int l = blockpos.getZ() + (int) (0.5F + MathHelper.sin(f) * 4.0F);

				for (int i1 = 0; i1 < 5; ++i1) {
					k = blockpos.getX() + (int) (1.5F + MathHelper.cos(f) * (float) i1);
					l = blockpos.getZ() + (int) (1.5F + MathHelper.sin(f) * (float) i1);
					this.setBlockAndNotifyAdequately(world, new BlockPos(k, j - 3 + i1 / 2, l), this.woodMetadata);
				}

				int j2 = 1 + random.nextInt(2);
				int j1 = j;

				for (int k1 = j - j2; k1 <= j1; ++k1) {
					int l1 = k1 - j1;
					this.func_175928_b(world, new BlockPos(k, k1, l), 1 - l1);
				}
			}

			for (int i2 = 0; i2 < i; ++i2) {
				BlockPos blockpos1 = blockpos.up(i2);
				if (this.func_150523_a(world.getBlockState(blockpos1).getBlock())) {
					this.setBlockAndNotifyAdequately(world, blockpos1, this.woodMetadata);
					if (i2 > 0) {
						this.func_181632_a(world, random, blockpos1.west(), BlockVine.EAST);
						this.func_181632_a(world, random, blockpos1.north(), BlockVine.SOUTH);
					}
				}

				if (i2 < i - 1) {
					BlockPos blockpos2 = blockpos1.east();
					if (this.func_150523_a(world.getBlockState(blockpos2).getBlock())) {
						this.setBlockAndNotifyAdequately(world, blockpos2, this.woodMetadata);
						if (i2 > 0) {
							this.func_181632_a(world, random, blockpos2.east(), BlockVine.WEST);
							this.func_181632_a(world, random, blockpos2.north(), BlockVine.SOUTH);
						}
					}

					BlockPos blockpos3 = blockpos1.south().east();
					if (this.func_150523_a(world.getBlockState(blockpos3).getBlock())) {
						this.setBlockAndNotifyAdequately(world, blockpos3, this.woodMetadata);
						if (i2 > 0) {
							this.func_181632_a(world, random, blockpos3.east(), BlockVine.WEST);
							this.func_181632_a(world, random, blockpos3.south(), BlockVine.NORTH);
						}
					}

					BlockPos blockpos4 = blockpos1.south();
					if (this.func_150523_a(world.getBlockState(blockpos4).getBlock())) {
						this.setBlockAndNotifyAdequately(world, blockpos4, this.woodMetadata);
						if (i2 > 0) {
							this.func_181632_a(world, random, blockpos4.west(), BlockVine.EAST);
							this.func_181632_a(world, random, blockpos4.south(), BlockVine.NORTH);
						}
					}
				}
			}

			return true;
		}
	}

	private void func_181632_a(World parWorld, EaglercraftRandom parRandom, BlockPos parBlockPos,
			PropertyBool parPropertyBool) {
		if (parRandom.nextInt(3) > 0 && parWorld.isAirBlock(parBlockPos)) {
			this.setBlockAndNotifyAdequately(parWorld, parBlockPos,
					Blocks.vine.getDefaultState().withProperty(parPropertyBool, Boolean.valueOf(true)));
		}

	}

	private void func_175930_c(World worldIn, BlockPos parBlockPos, int parInt1) {
		byte b0 = 2;

		for (int i = -b0; i <= 0; ++i) {
			this.func_175925_a(worldIn, parBlockPos.up(i), parInt1 + 1 - i);
		}

	}
}