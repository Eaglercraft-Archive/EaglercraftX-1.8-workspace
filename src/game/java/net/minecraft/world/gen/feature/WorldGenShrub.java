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
public class WorldGenShrub extends WorldGenTrees {
	private final IBlockState leavesMetadata;
	private final IBlockState woodMetadata;

	public WorldGenShrub(IBlockState parIBlockState, IBlockState parIBlockState2) {
		super(false);
		this.woodMetadata = parIBlockState;
		this.leavesMetadata = parIBlockState2;
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		Block block;
		while (((block = world.getBlockState(blockpos).getBlock()).getMaterial() == Material.air
				|| block.getMaterial() == Material.leaves) && blockpos.getY() > 0) {
			blockpos = blockpos.down();
		}

		Block block1 = world.getBlockState(blockpos).getBlock();
		if (block1 == Blocks.dirt || block1 == Blocks.grass) {
			blockpos = blockpos.up();
			this.setBlockAndNotifyAdequately(world, blockpos, this.woodMetadata);

			for (int i = blockpos.getY(); i <= blockpos.getY() + 2; ++i) {
				int j = i - blockpos.getY();
				int k = 2 - j;

				for (int l = blockpos.getX() - k; l <= blockpos.getX() + k; ++l) {
					int i1 = l - blockpos.getX();

					for (int j1 = blockpos.getZ() - k; j1 <= blockpos.getZ() + k; ++j1) {
						int k1 = j1 - blockpos.getZ();
						if (Math.abs(i1) != k || Math.abs(k1) != k || random.nextInt(2) != 0) {
							BlockPos blockpos1 = new BlockPos(l, i, j1);
							if (!world.getBlockState(blockpos1).getBlock().isFullBlock()) {
								this.setBlockAndNotifyAdequately(world, blockpos1, this.leavesMetadata);
							}
						}
					}
				}
			}
		}

		return true;
	}
}