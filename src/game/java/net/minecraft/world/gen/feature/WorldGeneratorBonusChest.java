package net.minecraft.world.gen.feature;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
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
public class WorldGeneratorBonusChest extends WorldGenerator {
	private final List<WeightedRandomChestContent> chestItems;
	private final int itemsToGenerateInBonusChest;

	public WorldGeneratorBonusChest(List<WeightedRandomChestContent> parList, int parInt1) {
		this.chestItems = parList;
		this.itemsToGenerateInBonusChest = parInt1;
	}

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		Block block;
		while (((block = world.getBlockState(blockpos).getBlock()).getMaterial() == Material.air
				|| block.getMaterial() == Material.leaves) && blockpos.getY() > 1) {
			blockpos = blockpos.down();
		}

		if (blockpos.getY() < 1) {
			return false;
		} else {
			blockpos = blockpos.up();

			for (int i = 0; i < 4; ++i) {
				BlockPos blockpos1 = blockpos.add(random.nextInt(4) - random.nextInt(4),
						random.nextInt(3) - random.nextInt(3), random.nextInt(4) - random.nextInt(4));
				if (world.isAirBlock(blockpos1) && World.doesBlockHaveSolidTopSurface(world, blockpos1.down())) {
					world.setBlockState(blockpos1, Blocks.chest.getDefaultState(), 2);
					TileEntity tileentity = world.getTileEntity(blockpos1);
					if (tileentity instanceof TileEntityChest) {
						WeightedRandomChestContent.generateChestContents(random, this.chestItems,
								(TileEntityChest) tileentity, this.itemsToGenerateInBonusChest);
					}

					BlockPos blockpos2 = blockpos1.east();
					BlockPos blockpos3 = blockpos1.west();
					BlockPos blockpos4 = blockpos1.north();
					BlockPos blockpos5 = blockpos1.south();
					if (world.isAirBlock(blockpos3) && World.doesBlockHaveSolidTopSurface(world, blockpos3.down())) {
						world.setBlockState(blockpos3, Blocks.torch.getDefaultState(), 2);
					}

					if (world.isAirBlock(blockpos2) && World.doesBlockHaveSolidTopSurface(world, blockpos2.down())) {
						world.setBlockState(blockpos2, Blocks.torch.getDefaultState(), 2);
					}

					if (world.isAirBlock(blockpos4) && World.doesBlockHaveSolidTopSurface(world, blockpos4.down())) {
						world.setBlockState(blockpos4, Blocks.torch.getDefaultState(), 2);
					}

					if (world.isAirBlock(blockpos5) && World.doesBlockHaveSolidTopSurface(world, blockpos5.down())) {
						world.setBlockState(blockpos5, Blocks.torch.getDefaultState(), 2);
					}

					return true;
				}
			}

			return false;
		}
	}
}