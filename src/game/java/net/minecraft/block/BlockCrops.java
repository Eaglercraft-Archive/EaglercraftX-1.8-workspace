package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
public class BlockCrops extends BlockBush implements IGrowable {
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

	protected BlockCrops() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		this.setTickRandomly(true);
		float f = 0.5F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
		this.setCreativeTab((CreativeTabs) null);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		this.disableStats();
	}

	/**+
	 * is the block grass, dirt or farmland
	 */
	protected boolean canPlaceBlockOn(Block block) {
		return block == Blocks.farmland;
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		super.updateTick(world, blockpos, iblockstate, random);
		if (world.getLightFromNeighbors(blockpos.up()) >= 9) {
			int i = ((Integer) iblockstate.getValue(AGE)).intValue();
			if (i < 7) {
				float f = getGrowthChance(this, world, blockpos);
				if (random.nextInt((int) (25.0F / f) + 1) == 0) {
					world.setBlockState(blockpos, iblockstate.withProperty(AGE, Integer.valueOf(i + 1)), 2);
				}
			}
		}

	}

	public void grow(World worldIn, BlockPos pos, IBlockState state) {
		int i = ((Integer) state.getValue(AGE)).intValue() + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
		if (i > 7) {
			i = 7;
		}

		worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i)), 2);
	}

	protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {
		float f = 1.0F;
		BlockPos blockpos = pos.down();

		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				float f1 = 0.0F;
				IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));
				if (iblockstate.getBlock() == Blocks.farmland) {
					f1 = 1.0F;
					if (((Integer) iblockstate.getValue(BlockFarmland.MOISTURE)).intValue() > 0) {
						f1 = 3.0F;
					}
				}

				if (i != 0 || j != 0) {
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		BlockPos blockpos1 = pos.north();
		BlockPos blockpos2 = pos.south();
		BlockPos blockpos3 = pos.west();
		BlockPos blockpos4 = pos.east();
		boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock()
				|| blockIn == worldIn.getBlockState(blockpos4).getBlock();
		boolean flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock()
				|| blockIn == worldIn.getBlockState(blockpos2).getBlock();
		if (flag && flag1) {
			f /= 2.0F;
		} else {
			boolean flag2 = blockIn == worldIn.getBlockState(blockpos3.north()).getBlock()
					|| blockIn == worldIn.getBlockState(blockpos4.north()).getBlock()
					|| blockIn == worldIn.getBlockState(blockpos4.south()).getBlock()
					|| blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();
			if (flag2) {
				f /= 2.0F;
			}
		}

		return f;
	}

	public boolean canBlockStay(World world, BlockPos blockpos, IBlockState var3) {
		return (world.getLight(blockpos) >= 8 || world.canSeeSky(blockpos))
				&& this.canPlaceBlockOn(world.getBlockState(blockpos.down()).getBlock());
	}

	protected Item getSeed() {
		return Items.wheat_seeds;
	}

	protected Item getCrop() {
		return Items.wheat;
	}

	/**+
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World world, BlockPos blockpos, IBlockState iblockstate, float f, int i) {
		super.dropBlockAsItemWithChance(world, blockpos, iblockstate, f, 0);
		if (!world.isRemote) {
			int j = ((Integer) iblockstate.getValue(AGE)).intValue();
			if (j >= 7) {
				int k = 3 + i;

				for (int l = 0; l < k; ++l) {
					if (world.rand.nextInt(15) <= j) {
						spawnAsEntity(world, blockpos, new ItemStack(this.getSeed(), 1, 0));
					}
				}
			}
		}
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {
		return ((Integer) iblockstate.getValue(AGE)).intValue() == 7 ? this.getCrop() : this.getSeed();
	}

	public Item getItem(World var1, BlockPos var2) {
		return this.getSeed();
	}

	/**+
	 * Whether this IGrowable can grow
	 */
	public boolean canGrow(World var1, BlockPos var2, IBlockState iblockstate, boolean var4) {
		return ((Integer) iblockstate.getValue(AGE)).intValue() < 7;
	}

	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {
		return true;
	}

	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState iblockstate) {
		this.grow(world, blockpos, iblockstate);
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(AGE)).intValue();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { AGE });
	}
}