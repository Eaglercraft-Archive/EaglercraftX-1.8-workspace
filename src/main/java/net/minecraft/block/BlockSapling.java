package net.minecraft.block;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

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
		for (BlockPlanks.EnumType blockplanks$enumtype : BlockPlanks.EnumType.values()) {
			list.add(new ItemStack(item, 1, blockplanks$enumtype.getMetadata()));
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