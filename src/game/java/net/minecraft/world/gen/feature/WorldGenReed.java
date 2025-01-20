package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.material.Material;
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
public class WorldGenReed extends WorldGenerator {
	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		for (int i = 0; i < 20; ++i) {
			BlockPos blockpos1 = blockpos.add(random.nextInt(4) - random.nextInt(4), 0,
					random.nextInt(4) - random.nextInt(4));
			if (world.isAirBlock(blockpos1)) {
				BlockPos blockpos2 = blockpos1.down();
				if (world.getBlockState(blockpos2.west()).getBlock().getMaterial() == Material.water
						|| world.getBlockState(blockpos2.east()).getBlock().getMaterial() == Material.water
						|| world.getBlockState(blockpos2.north()).getBlock().getMaterial() == Material.water
						|| world.getBlockState(blockpos2.south()).getBlock().getMaterial() == Material.water) {
					int j = 2 + random.nextInt(random.nextInt(3) + 1);

					for (int k = 0; k < j; ++k) {
						if (Blocks.reeds.canBlockStay(world, blockpos1)) {
							world.setBlockState(blockpos1.up(k), Blocks.reeds.getDefaultState(), 2);
						}
					}
				}
			}
		}

		return true;
	}
}