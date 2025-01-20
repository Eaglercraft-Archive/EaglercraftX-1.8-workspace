package net.minecraft.world.gen.feature;

import com.google.common.base.Predicates;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
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
public class WorldGenDesertWells extends WorldGenerator {
	private static final BlockStateHelper field_175913_a = BlockStateHelper.forBlock(Blocks.sand)
			.where(BlockSand.VARIANT, Predicates.equalTo(BlockSand.EnumType.SAND));
	private final IBlockState field_175911_b = Blocks.stone_slab.getDefaultState()
			.withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND)
			.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
	private final IBlockState field_175912_c = Blocks.sandstone.getDefaultState();
	private final IBlockState field_175910_d = Blocks.flowing_water.getDefaultState();

	public boolean generate(World world, EaglercraftRandom var2, BlockPos blockpos) {
		while (world.isAirBlock(blockpos) && blockpos.getY() > 2) {
			blockpos = blockpos.down();
		}

		if (!field_175913_a.apply(world.getBlockState(blockpos))) {
			return false;
		} else {
			for (int i = -2; i <= 2; ++i) {
				for (int j = -2; j <= 2; ++j) {
					if (world.isAirBlock(blockpos.add(i, -1, j)) && world.isAirBlock(blockpos.add(i, -2, j))) {
						return false;
					}
				}
			}

			for (int l = -1; l <= 0; ++l) {
				for (int l1 = -2; l1 <= 2; ++l1) {
					for (int k = -2; k <= 2; ++k) {
						world.setBlockState(blockpos.add(l1, l, k), this.field_175912_c, 2);
					}
				}
			}

			world.setBlockState(blockpos, this.field_175910_d, 2);

			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
			for (int i = 0; i < facings.length; ++i) {
				world.setBlockState(blockpos.offset(facings[i]), this.field_175910_d, 2);
			}

			for (int i1 = -2; i1 <= 2; ++i1) {
				for (int i2 = -2; i2 <= 2; ++i2) {
					if (i1 == -2 || i1 == 2 || i2 == -2 || i2 == 2) {
						world.setBlockState(blockpos.add(i1, 1, i2), this.field_175912_c, 2);
					}
				}
			}

			world.setBlockState(blockpos.add(2, 1, 0), this.field_175911_b, 2);
			world.setBlockState(blockpos.add(-2, 1, 0), this.field_175911_b, 2);
			world.setBlockState(blockpos.add(0, 1, 2), this.field_175911_b, 2);
			world.setBlockState(blockpos.add(0, 1, -2), this.field_175911_b, 2);

			for (int j1 = -1; j1 <= 1; ++j1) {
				for (int j2 = -1; j2 <= 1; ++j2) {
					if (j1 == 0 && j2 == 0) {
						world.setBlockState(blockpos.add(j1, 4, j2), this.field_175912_c, 2);
					} else {
						world.setBlockState(blockpos.add(j1, 4, j2), this.field_175911_b, 2);
					}
				}
			}

			for (int k1 = 1; k1 <= 3; ++k1) {
				world.setBlockState(blockpos.add(-1, k1, -1), this.field_175912_c, 2);
				world.setBlockState(blockpos.add(-1, k1, 1), this.field_175912_c, 2);
				world.setBlockState(blockpos.add(1, k1, -1), this.field_175912_c, 2);
				world.setBlockState(blockpos.add(1, k1, 1), this.field_175912_c, 2);
			}

			return true;
		}
	}
}