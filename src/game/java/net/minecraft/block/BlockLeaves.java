package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

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
public abstract class BlockLeaves extends BlockLeavesBase {
	public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
	public static final PropertyBool CHECK_DECAY = PropertyBool.create("check_decay");
	int[] surroundings;
	protected int iconIndex;
	protected boolean isTransparent;

	public BlockLeaves() {
		super(Material.leaves, false);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setHardness(0.2F);
		this.setLightOpacity(1);
		this.setStepSound(soundTypeGrass);
	}

	public int getBlockColor() {
		return ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
	}

	public int getRenderColor(IBlockState var1) {
		return ColorizerFoliage.getFoliageColorBasic();
	}

	public int colorMultiplier(IBlockAccess iblockaccess, BlockPos blockpos, int var3) {
		return BiomeColorHelper.getFoliageColorAtPos(iblockaccess, blockpos);
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState var3) {
		byte b0 = 1;
		int i = b0 + 1;
		int j = blockpos.getX();
		int k = blockpos.getY();
		int l = blockpos.getZ();
		if (world.isAreaLoaded(new BlockPos(j - i, k - i, l - i), new BlockPos(j + i, k + i, l + i))) {
			for (int i1 = -b0; i1 <= b0; ++i1) {
				for (int j1 = -b0; j1 <= b0; ++j1) {
					for (int k1 = -b0; k1 <= b0; ++k1) {
						BlockPos blockpos1 = blockpos.add(i1, j1, k1);
						IBlockState iblockstate = world.getBlockState(blockpos1);
						if (iblockstate.getBlock().getMaterial() == Material.leaves
								&& !((Boolean) iblockstate.getValue(CHECK_DECAY)).booleanValue()) {
							world.setBlockState(blockpos1, iblockstate.withProperty(CHECK_DECAY, Boolean.valueOf(true)),
									4);
						}
					}
				}
			}
		}

	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {
		if (!world.isRemote) {
			if (((Boolean) iblockstate.getValue(CHECK_DECAY)).booleanValue()
					&& ((Boolean) iblockstate.getValue(DECAYABLE)).booleanValue()) {
				byte b0 = 4;
				int i = b0 + 1;
				int j = blockpos.getX();
				int k = blockpos.getY();
				int l = blockpos.getZ();
				byte b1 = 32;
				int i1 = b1 * b1;
				int j1 = b1 / 2;
				if (this.surroundings == null) {
					this.surroundings = new int[b1 * b1 * b1];
				}

				if (world.isAreaLoaded(new BlockPos(j - i, k - i, l - i), new BlockPos(j + i, k + i, l + i))) {
					BlockPos blockpos$mutableblockpos = new BlockPos();

					for (int k1 = -b0; k1 <= b0; ++k1) {
						for (int l1 = -b0; l1 <= b0; ++l1) {
							for (int i2 = -b0; i2 <= b0; ++i2) {
								Block block = world
										.getBlockState(blockpos$mutableblockpos.func_181079_c(j + k1, k + l1, l + i2))
										.getBlock();
								if (block != Blocks.log && block != Blocks.log2) {
									if (block.getMaterial() == Material.leaves) {
										this.surroundings[(k1 + j1) * i1 + (l1 + j1) * b1 + i2 + j1] = -2;
									} else {
										this.surroundings[(k1 + j1) * i1 + (l1 + j1) * b1 + i2 + j1] = -1;
									}
								} else {
									this.surroundings[(k1 + j1) * i1 + (l1 + j1) * b1 + i2 + j1] = 0;
								}
							}
						}
					}

					for (int k2 = 1; k2 <= 4; ++k2) {
						for (int l2 = -b0; l2 <= b0; ++l2) {
							for (int i3 = -b0; i3 <= b0; ++i3) {
								for (int j3 = -b0; j3 <= b0; ++j3) {
									if (this.surroundings[(l2 + j1) * i1 + (i3 + j1) * b1 + j3 + j1] == k2 - 1) {
										if (this.surroundings[(l2 + j1 - 1) * i1 + (i3 + j1) * b1 + j3 + j1] == -2) {
											this.surroundings[(l2 + j1 - 1) * i1 + (i3 + j1) * b1 + j3 + j1] = k2;
										}

										if (this.surroundings[(l2 + j1 + 1) * i1 + (i3 + j1) * b1 + j3 + j1] == -2) {
											this.surroundings[(l2 + j1 + 1) * i1 + (i3 + j1) * b1 + j3 + j1] = k2;
										}

										if (this.surroundings[(l2 + j1) * i1 + (i3 + j1 - 1) * b1 + j3 + j1] == -2) {
											this.surroundings[(l2 + j1) * i1 + (i3 + j1 - 1) * b1 + j3 + j1] = k2;
										}

										if (this.surroundings[(l2 + j1) * i1 + (i3 + j1 + 1) * b1 + j3 + j1] == -2) {
											this.surroundings[(l2 + j1) * i1 + (i3 + j1 + 1) * b1 + j3 + j1] = k2;
										}

										if (this.surroundings[(l2 + j1) * i1 + (i3 + j1) * b1 + (j3 + j1 - 1)] == -2) {
											this.surroundings[(l2 + j1) * i1 + (i3 + j1) * b1 + (j3 + j1 - 1)] = k2;
										}

										if (this.surroundings[(l2 + j1) * i1 + (i3 + j1) * b1 + j3 + j1 + 1] == -2) {
											this.surroundings[(l2 + j1) * i1 + (i3 + j1) * b1 + j3 + j1 + 1] = k2;
										}
									}
								}
							}
						}
					}
				}

				int j2 = this.surroundings[j1 * i1 + j1 * b1 + j1];
				if (j2 >= 0) {
					world.setBlockState(blockpos, iblockstate.withProperty(CHECK_DECAY, Boolean.valueOf(false)), 4);
				} else {
					this.destroy(world, blockpos);
				}
			}
		}
	}

	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom random) {
		if (world.canLightningStrike(blockpos.up()) && !World.doesBlockHaveSolidTopSurface(world, blockpos.down())
				&& random.nextInt(15) == 1) {
			double d0 = (double) ((float) blockpos.getX() + random.nextFloat());
			double d1 = (double) blockpos.getY() - 0.05D;
			double d2 = (double) ((float) blockpos.getZ() + random.nextFloat());
			world.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
		}

	}

	private void destroy(World worldIn, BlockPos pos) {
		this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
		worldIn.setBlockToAir(pos);
	}

	/**+
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom random) {
		return random.nextInt(20) == 0 ? 1 : 0;
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Item.getItemFromBlock(Blocks.sapling);
	}

	/**+
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World world, BlockPos blockpos, IBlockState iblockstate, float var4, int i) {
		if (!world.isRemote) {
			int j = this.getSaplingDropChance(iblockstate);
			if (i > 0) {
				j -= 2 << i;
				if (j < 10) {
					j = 10;
				}
			}

			if (world.rand.nextInt(j) == 0) {
				Item item = this.getItemDropped(iblockstate, world.rand, i);
				spawnAsEntity(world, blockpos, new ItemStack(item, 1, this.damageDropped(iblockstate)));
			}

			j = 200;
			if (i > 0) {
				j -= 10 << i;
				if (j < 40) {
					j = 40;
				}
			}

			this.dropApple(world, blockpos, iblockstate, j);
		}
	}

	protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
	}

	protected int getSaplingDropChance(IBlockState state) {
		return 20;
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return !this.fancyGraphics;
	}

	/**+
	 * Pass true to draw this block using fancy graphics, or false
	 * for fast graphics.
	 */
	public void setGraphicsLevel(boolean fancy) {
		this.isTransparent = fancy;
		this.fancyGraphics = fancy;
		this.iconIndex = fancy ? 0 : 1;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return this.isTransparent ? EnumWorldBlockLayer.CUTOUT_MIPPED : EnumWorldBlockLayer.SOLID;
	}

	public boolean isVisuallyOpaque() {
		return false;
	}

	public abstract BlockPlanks.EnumType getWoodType(int var1);
}