package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.material.Material;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.init.Blocks;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.BlockPos;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.EnumFacing;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.World;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.gen.feature.WorldGenerator;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class WorldGenGlowStone1 extends WorldGenerator {

	static {
		__checkIntegratedContextValid("net/minecraft/world/gen/feature/WorldGenGlowStone1");
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		if (!world.isAirBlock(blockpos)) {
			return false;
		} else if (world.getBlockState(blockpos.up()).getBlock() != Blocks.netherrack) {
			return false;
		} else {
			world.setBlockState(blockpos, Blocks.glowstone.getDefaultState(), 2);

			for (int i = 0; i < 1500; ++i) {
				BlockPos blockpos1 = blockpos.add(random.nextInt(8) - random.nextInt(8), -random.nextInt(12),
						random.nextInt(8) - random.nextInt(8));
				if (world.getBlockState(blockpos1).getBlock().getMaterial() == Material.air) {
					int j = 0;

					for (EnumFacing enumfacing : EnumFacing.values()) {
						if (world.getBlockState(blockpos1.offset(enumfacing)).getBlock() == Blocks.glowstone) {
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