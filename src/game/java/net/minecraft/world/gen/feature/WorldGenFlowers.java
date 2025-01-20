package net.minecraft.world.gen.feature;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
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
public class WorldGenFlowers extends WorldGenerator {
	private BlockFlower flower;
	private IBlockState field_175915_b;

	public WorldGenFlowers(BlockFlower parBlockFlower, BlockFlower.EnumFlowerType parEnumFlowerType) {
		this.setGeneratedBlock(parBlockFlower, parEnumFlowerType);
	}

	public void setGeneratedBlock(BlockFlower parBlockFlower, BlockFlower.EnumFlowerType parEnumFlowerType) {
		this.flower = parBlockFlower;
		this.field_175915_b = parBlockFlower.getDefaultState().withProperty(parBlockFlower.getTypeProperty(),
				parEnumFlowerType);
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		for (int i = 0; i < 64; ++i) {
			BlockPos blockpos1 = blockpos.add(random.nextInt(8) - random.nextInt(8),
					random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
			if (world.isAirBlock(blockpos1) && (!world.provider.getHasNoSky() || blockpos1.getY() < 255)
					&& this.flower.canBlockStay(world, blockpos1, this.field_175915_b)) {
				world.setBlockState(blockpos1, this.field_175915_b, 2);
			}
		}

		return true;
	}
}