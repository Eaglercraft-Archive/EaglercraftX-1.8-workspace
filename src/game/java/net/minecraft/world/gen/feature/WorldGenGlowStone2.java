package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
public class WorldGenGlowStone2 extends WorldGenerator {
	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		BlockPos tmp;
		if (!world.isAirBlock(blockpos)) {
			return false;
		} else if (world.getBlockState(blockpos.offsetEvenFaster(EnumFacing.UP, tmp = new BlockPos(0, 0, 0)))
				.getBlock() != Blocks.netherrack) {
			return false;
		} else {
			world.setBlockState(blockpos, Blocks.glowstone.getDefaultState(), 2);

			for (int i = 0; i < 1500; ++i) {
				BlockPos blockpos1 = blockpos.add(random.nextInt(8) - random.nextInt(8), -random.nextInt(12),
						random.nextInt(8) - random.nextInt(8));
				if (world.getBlockState(blockpos1).getBlock().getMaterial() == Material.air) {
					int j = 0;

					EnumFacing[] facings = EnumFacing._VALUES;
					for (int k = 0; k < facings.length; ++k) {
						if (world.getBlockState(blockpos1.offsetEvenFaster(facings[k], tmp))
								.getBlock() == Blocks.glowstone) {
							++j;
						}

						if (j > 1) {
							break;
						}
					}

					if (j == 1) {
						world.setBlockState(blockpos1, Blocks.glowstone.getDefaultState(), 2);
					}
				}
			}

			return true;
		}
	}
}