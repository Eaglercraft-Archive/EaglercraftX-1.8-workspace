package net.minecraft.world.gen.feature;

import com.google.common.base.Predicate;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
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
public class WorldGenMinable extends WorldGenerator {
	private final IBlockState oreBlock;
	private final int numberOfBlocks;
	private final Predicate<IBlockState> predicate;

	public WorldGenMinable(IBlockState state, int blockCount) {
		this(state, blockCount, BlockHelper.forBlock(Blocks.stone));
	}

	public WorldGenMinable(IBlockState state, int blockCount, Predicate<IBlockState> parPredicate) {
		this.oreBlock = state;
		this.numberOfBlocks = blockCount;
		this.predicate = parPredicate;
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		float f = random.nextFloat() * 3.1415927F;
		double d0 = (double) ((float) (blockpos.getX() + 8) + MathHelper.sin(f) * (float) this.numberOfBlocks / 8.0F);
		double d1 = (double) ((float) (blockpos.getX() + 8) - MathHelper.sin(f) * (float) this.numberOfBlocks / 8.0F);
		double d2 = (double) ((float) (blockpos.getZ() + 8) + MathHelper.cos(f) * (float) this.numberOfBlocks / 8.0F);
		double d3 = (double) ((float) (blockpos.getZ() + 8) - MathHelper.cos(f) * (float) this.numberOfBlocks / 8.0F);
		double d4 = (double) (blockpos.getY() + random.nextInt(3) - 2);
		double d5 = (double) (blockpos.getY() + random.nextInt(3) - 2);

		for (int i = 0; i < this.numberOfBlocks; ++i) {
			float f1 = (float) i / (float) this.numberOfBlocks;
			double d6 = d0 + (d1 - d0) * (double) f1;
			double d7 = d4 + (d5 - d4) * (double) f1;
			double d8 = d2 + (d3 - d2) * (double) f1;
			double d9 = random.nextDouble() * (double) this.numberOfBlocks / 16.0D;
			double d10 = (double) (MathHelper.sin(3.1415927F * f1) + 1.0F) * d9 + 1.0D;
			double d11 = (double) (MathHelper.sin(3.1415927F * f1) + 1.0F) * d9 + 1.0D;
			int j = MathHelper.floor_double(d6 - d10 / 2.0D);
			int k = MathHelper.floor_double(d7 - d11 / 2.0D);
			int l = MathHelper.floor_double(d8 - d10 / 2.0D);
			int i1 = MathHelper.floor_double(d6 + d10 / 2.0D);
			int j1 = MathHelper.floor_double(d7 + d11 / 2.0D);
			int k1 = MathHelper.floor_double(d8 + d10 / 2.0D);

			for (int l1 = j; l1 <= i1; ++l1) {
				double d12 = ((double) l1 + 0.5D - d6) / (d10 / 2.0D);
				if (d12 * d12 < 1.0D) {
					for (int i2 = k; i2 <= j1; ++i2) {
						double d13 = ((double) i2 + 0.5D - d7) / (d11 / 2.0D);
						if (d12 * d12 + d13 * d13 < 1.0D) {
							for (int j2 = l; j2 <= k1; ++j2) {
								double d14 = ((double) j2 + 0.5D - d8) / (d10 / 2.0D);
								if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
									BlockPos blockpos1 = new BlockPos(l1, i2, j2);
									if (this.predicate.apply(world.getBlockState(blockpos1))) {
										world.setBlockState(blockpos1, this.oreBlock, 2);
									}
								}
							}
						}
					}
				}
			}
		}

		return true;
	}
}