package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class WorldGenDungeons extends WorldGenerator {
	private static final Logger field_175918_a = LogManager.getLogger();
	private static final String[] SPAWNERTYPES = new String[] { "Skeleton", "Zombie", "Zombie", "Spider" };
	private static final List<WeightedRandomChestContent> CHESTCONTENT = Lists
			.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10),
					new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 4, 10),
					new WeightedRandomChestContent(Items.bread, 0, 1, 1, 10),
					new WeightedRandomChestContent(Items.wheat, 0, 1, 4, 10),
					new WeightedRandomChestContent(Items.gunpowder, 0, 1, 4, 10),
					new WeightedRandomChestContent(Items.string, 0, 1, 4, 10),
					new WeightedRandomChestContent(Items.bucket, 0, 1, 1, 10),
					new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1),
					new WeightedRandomChestContent(Items.redstone, 0, 1, 4, 10),
					new WeightedRandomChestContent(Items.record_13, 0, 1, 1, 4),
					new WeightedRandomChestContent(Items.record_cat, 0, 1, 1, 4),
					new WeightedRandomChestContent(Items.name_tag, 0, 1, 1, 10),
					new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 2),
					new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5),
					new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });

	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
		boolean flag = true;
		int i = random.nextInt(2) + 2;
		int j = -i - 1;
		int k = i + 1;
		boolean flag1 = true;
		boolean flag2 = true;
		int l = random.nextInt(2) + 2;
		int i1 = -l - 1;
		int j1 = l + 1;
		int k1 = 0;

		for (int l1 = j; l1 <= k; ++l1) {
			for (int i2 = -1; i2 <= 4; ++i2) {
				for (int j2 = i1; j2 <= j1; ++j2) {
					BlockPos blockpos1 = blockpos.add(l1, i2, j2);
					Material material = world.getBlockState(blockpos1).getBlock().getMaterial();
					boolean flag3 = material.isSolid();
					if (i2 == -1 && !flag3) {
						return false;
					}

					if (i2 == 4 && !flag3) {
						return false;
					}

					if ((l1 == j || l1 == k || j2 == i1 || j2 == j1) && i2 == 0 && world.isAirBlock(blockpos1)
							&& world.isAirBlock(blockpos1.up())) {
						++k1;
					}
				}
			}
		}

		if (k1 >= 1 && k1 <= 5) {
			for (int l2 = j; l2 <= k; ++l2) {
				for (int j3 = 3; j3 >= -1; --j3) {
					for (int l3 = i1; l3 <= j1; ++l3) {
						BlockPos blockpos2 = blockpos.add(l2, j3, l3);
						if (l2 != j && j3 != -1 && l3 != i1 && l2 != k && j3 != 4 && l3 != j1) {
							if (world.getBlockState(blockpos2).getBlock() != Blocks.chest) {
								world.setBlockToAir(blockpos2);
							}
						} else if (blockpos2.getY() >= 0
								&& !world.getBlockState(blockpos2.down()).getBlock().getMaterial().isSolid()) {
							world.setBlockToAir(blockpos2);
						} else if (world.getBlockState(blockpos2).getBlock().getMaterial().isSolid()
								&& world.getBlockState(blockpos2).getBlock() != Blocks.chest) {
							if (j3 == -1 && random.nextInt(4) != 0) {
								world.setBlockState(blockpos2, Blocks.mossy_cobblestone.getDefaultState(), 2);
							} else {
								world.setBlockState(blockpos2, Blocks.cobblestone.getDefaultState(), 2);
							}
						}
					}
				}
			}

			for (int i3 = 0; i3 < 2; ++i3) {
				for (int k3 = 0; k3 < 3; ++k3) {
					int i4 = blockpos.getX() + random.nextInt(i * 2 + 1) - i;
					int j4 = blockpos.getY();
					int k4 = blockpos.getZ() + random.nextInt(l * 2 + 1) - l;
					BlockPos blockpos3 = new BlockPos(i4, j4, k4);
					if (world.isAirBlock(blockpos3)) {
						int k2 = 0;

						EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
						for (int m = 0; m < facings.length; ++m) {
							if (world.getBlockState(blockpos3.offset(facings[m])).getBlock().getMaterial().isSolid()) {
								++k2;
							}
						}

						if (k2 == 1) {
							world.setBlockState(blockpos3,
									Blocks.chest.correctFacing(world, blockpos3, Blocks.chest.getDefaultState()), 2);
							List list = WeightedRandomChestContent.func_177629_a(CHESTCONTENT,
									new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(random) });
							TileEntity tileentity1 = world.getTileEntity(blockpos3);
							if (tileentity1 instanceof TileEntityChest) {
								WeightedRandomChestContent.generateChestContents(random, list,
										(TileEntityChest) tileentity1, 8);
							}
							break;
						}
					}
				}
			}

			world.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
			TileEntity tileentity = world.getTileEntity(blockpos);
			if (tileentity instanceof TileEntityMobSpawner) {
				((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setEntityName(this.pickMobSpawner(random));
			} else {
				field_175918_a.error("Failed to fetch mob spawner entity at (" + blockpos.getX() + ", "
						+ blockpos.getY() + ", " + blockpos.getZ() + ")");
			}

			return true;
		} else {
			return false;
		}
	}

	/**+
	 * Randomly decides which spawner to use in a dungeon
	 */
	private String pickMobSpawner(EaglercraftRandom parRandom) {
		return SPAWNERTYPES[parRandom.nextInt(SPAWNERTYPES.length)];
	}
}