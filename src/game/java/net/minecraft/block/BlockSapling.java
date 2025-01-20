package net.minecraft.block;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

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
public class BlockSapling extends BlockBush implements IGrowable {
	public static PropertyEnum<BlockPlanks.EnumType> TYPE;
	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

	protected BlockSapling() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockPlanks.EnumType.OAK)
				.withProperty(STAGE, Integer.valueOf(0)));
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	public static void bootstrapStates() {
		TYPE = PropertyEnum.<BlockPlanks.EnumType>create("type", BlockPlanks.EnumType.class);
	}

	/**+
	 * Gets the localized name of this block. Used for the
	 * statistics page.
	 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal(
				this.getUnlocalizedName() + "." + BlockPlanks.EnumType.OAK.getUnlocalizedName() + ".name");
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		if (!world.isRemote) {
			super.updateTick(world, blockpos, iblockstate, random);
			if (world.getLightFromNeighbors(blockpos.up()) >= 9 && random.nextInt(7) == 0) {
				this.grow(world, blockpos, iblockstate, random);
			}
		}
	}

	public void grow(World worldIn, BlockPos pos, IBlockState state, EaglercraftRandom rand) {
		if (((Integer) state.getValue(STAGE)).intValue() == 0) {
			worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
		} else {
			this.generateTree(worldIn, pos, state, rand);
		}

	}

	public void generateTree(World worldIn, BlockPos pos, IBlockState state, EaglercraftRandom rand) {
		Object object = rand.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
		int i = 0;
		int j = 0;
		boolean flag = false;
		switch ((BlockPlanks.EnumType) state.getValue(TYPE)) {
		case SPRUCE:
			label114: for (i = 0; i >= -1; --i) {
				for (j = 0; j >= -1; --j) {
					if (this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE)) {
						object = new WorldGenMegaPineTree(false, rand.nextBoolean());
						flag = true;
						break label114;
					}
				}
			}

			if (!flag) {
				j = 0;
				i = 0;
				object = new WorldGenTaiga2(true);
			}
			break;
		case BIRCH:
			object = new WorldGenForest(true, false);
			break;
		case JUNGLE:
			IBlockState iblockstate = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT,
					BlockPlanks.EnumType.JUNGLE);
			IBlockState iblockstate1 = Blocks.leaves.getDefaultState()
					.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE)
					.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

			label269: for (i = 0; i >= -1; --i) {
				for (j = 0; j >= -1; --j) {
					if (this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE)) {
						object = new WorldGenMegaJungle(true, 10, 20, iblockstate, iblockstate1);
						flag = true;
						break label269;
					}
				}
			}

			if (!flag) {
				j = 0;
				i = 0;
				object = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockstate, iblockstate1, false);
			}
			break;
		case ACACIA:
			object = new WorldGenSavannaTree(true);
			break;
		case DARK_OAK:
			label390: for (i = 0; i >= -1; --i) {
				for (j = 0; j >= -1; --j) {
					if (this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK)) {
						object = new WorldGenCanopyTree(true);
						flag = true;
						break label390;
					}
				}
			}

			if (!flag) {
				return;
			}
		case OAK:
		}

		IBlockState iblockstate2 = Blocks.air.getDefaultState();
		if (flag) {
			worldIn.setBlockState(pos.add(i, 0, j), iblockstate2, 4);
			worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate2, 4);
			worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate2, 4);
			worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate2, 4);
		} else {
			worldIn.setBlockState(pos, iblockstate2, 4);
		}

		if (!((WorldGenerator) object).generate(worldIn, rand, pos.add(i, 0, j))) {
			if (flag) {
				worldIn.setBlockState(pos.add(i, 0, j), state, 4);
				worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
				worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
				worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
			} else {
				worldIn.setBlockState(pos, state, 4);
			}
		}

	}

	private boolean func_181624_a(World parWorld, BlockPos parBlockPos, int parInt1, int parInt2,
			BlockPlanks.EnumType parEnumType) {
		return this.isTypeAt(parWorld, parBlockPos.add(parInt1, 0, parInt2), parEnumType)
				&& this.isTypeAt(parWorld, parBlockPos.add(parInt1 + 1, 0, parInt2), parEnumType)
				&& this.isTypeAt(parWorld, parBlockPos.add(parInt1, 0, parInt2 + 1), parEnumType)
				&& this.isTypeAt(parWorld, parBlockPos.add(parInt1 + 1, 0, parInt2 + 1), parEnumType);
	}

	/**+
	 * Check whether the given BlockPos has a Sapling of the given
	 * type
	 */
	public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		return iblockstate.getBlock() == this && iblockstate.getValue(TYPE) == type;
	}

	/**+
	 * Gets the metadata of the item this Block can drop. This
	 * method is called when the block gets destroyed. It returns
	 * the metadata of the dropped item based on the old metadata of
	 * the block.
	 */
	public int damageDropped(IBlockState iblockstate) {
		return ((BlockPlanks.EnumType) iblockstate.getValue(TYPE)).getMetadata();
	}

	/**+
	 * returns a list of blocks with the same ID, but different meta
	 * (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(Item item, CreativeTabs var2, List<ItemStack> list) {
		BlockPlanks.EnumType[] types = BlockPlanks.EnumType.META_LOOKUP;
		for (int i = 0; i < types.length; ++i) {
			list.add(new ItemStack(item, 1, types[i].getMetadata()));
		}

	}

	/**+
	 * Whether this IGrowable can grow
	 */
	public boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4) {
		return true;
	}

	public boolean canUseBonemeal(World world, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {
		return (double) world.rand.nextFloat() < 0.45D;
	}

	public void grow(World world, EaglercraftRandom random, BlockPos blockpos, IBlockState iblockstate) {
		this.grow(world, blockpos, iblockstate, random);
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(TYPE, BlockPlanks.EnumType.byMetadata(i & 7)).withProperty(STAGE,
				Integer.valueOf((i & 8) >> 3));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((BlockPlanks.EnumType) iblockstate.getValue(TYPE)).getMetadata();
		i = i | ((Integer) iblockstate.getValue(STAGE)).intValue() << 3;
		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { TYPE, STAGE });
	}
}