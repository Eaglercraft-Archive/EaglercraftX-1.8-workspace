package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityEnderCrystal;
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
public class WorldGenSpikes extends WorldGenerator {
	private Block baseBlockRequired;

	public WorldGenSpikes(Block parBlock) {
		this.baseBlockRequired = parBlock;
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		if (world.isAirBlock(blockpos) && world.getBlockState(blockpos.down()).getBlock() == this.baseBlockRequired) {
			int i = random.nextInt(32) + 6;
			int j = random.nextInt(4) + 1;
			BlockPos blockpos$mutableblockpos = new BlockPos();

			for (int k = blockpos.getX() - j; k <= blockpos.getX() + j; ++k) {
				for (int l = blockpos.getZ() - j; l <= blockpos.getZ() + j; ++l) {
					int i1 = k - blockpos.getX();
					int j1 = l - blockpos.getZ();
					if (i1 * i1 + j1 * j1 <= j * j + 1
							&& world.getBlockState(blockpos$mutableblockpos.func_181079_c(k, blockpos.getY() - 1, l))
									.getBlock() != this.baseBlockRequired) {
						return false;
					}
				}
			}

			for (int l1 = blockpos.getY(); l1 < blockpos.getY() + i && l1 < 256; ++l1) {
				for (int i2 = blockpos.getX() - j; i2 <= blockpos.getX() + j; ++i2) {
					for (int j2 = blockpos.getZ() - j; j2 <= blockpos.getZ() + j; ++j2) {
						int k2 = i2 - blockpos.getX();
						int k1 = j2 - blockpos.getZ();
						if (k2 * k2 + k1 * k1 <= j * j + 1) {
							world.setBlockState(new BlockPos(i2, l1, j2), Blocks.obsidian.getDefaultState(), 2);
						}
					}
				}
			}

			EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(world);
			entityendercrystal.setLocationAndAngles((double) ((float) blockpos.getX() + 0.5F),
					(double) (blockpos.getY() + i), (double) ((float) blockpos.getZ() + 0.5F),
					random.nextFloat() * 360.0F, 0.0F);
			world.spawnEntityInWorld(entityendercrystal);
			world.setBlockState(blockpos.up(i), Blocks.bedrock.getDefaultState(), 2);
			return true;
		} else {
			return false;
		}
	}
}