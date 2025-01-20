package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
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
public class WorldGenLiquids extends WorldGenerator {
	private Block block;

	public WorldGenLiquids(Block parBlock) {
		this.block = parBlock;
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		if (world.getBlockState(blockpos.up()).getBlock() != Blocks.stone) {
			return false;
		} else if (world.getBlockState(blockpos.down()).getBlock() != Blocks.stone) {
			return false;
		} else if (world.getBlockState(blockpos).getBlock().getMaterial() != Material.air
				&& world.getBlockState(blockpos).getBlock() != Blocks.stone) {
			return false;
		} else {
			int i = 0;
			if (world.getBlockState(blockpos.west()).getBlock() == Blocks.stone) {
				++i;
			}

			if (world.getBlockState(blockpos.east()).getBlock() == Blocks.stone) {
				++i;
			}

			if (world.getBlockState(blockpos.north()).getBlock() == Blocks.stone) {
				++i;
			}

			if (world.getBlockState(blockpos.south()).getBlock() == Blocks.stone) {
				++i;
			}

			int j = 0;
			if (world.isAirBlock(blockpos.west())) {
				++j;
			}

			if (world.isAirBlock(blockpos.east())) {
				++j;
			}

			if (world.isAirBlock(blockpos.north())) {
				++j;
			}

			if (world.isAirBlock(blockpos.south())) {
				++j;
			}

			if (i == 3 && j == 1) {
				world.setBlockState(blockpos, this.block.getDefaultState(), 2);
				world.forceBlockUpdateTick(this.block, blockpos, random);
			}

			return true;
		}
	}
}