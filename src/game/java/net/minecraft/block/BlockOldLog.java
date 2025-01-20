package net.minecraft.block;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
public class BlockOldLog extends BlockLog {
	public static PropertyEnum<BlockPlanks.EnumType> VARIANT;

	public BlockOldLog() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK)
				.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
	}

	public static void bootstrapStates() {
		VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
			public boolean apply(BlockPlanks.EnumType blockplanks$enumtype) {
				return blockplanks$enumtype.getMetadata() < 4;
			}
		});
	}

	/**+
	 * Get the MapColor for this Block and the given BlockState
	 */
	public MapColor getMapColor(IBlockState iblockstate) {
		BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType) iblockstate.getValue(VARIANT);
		switch ((BlockLog.EnumAxis) iblockstate.getValue(LOG_AXIS)) {
		case X:
		case Z:
		case NONE:
		default:
			switch (blockplanks$enumtype) {
			case OAK:
			default:
				return BlockPlanks.EnumType.SPRUCE.func_181070_c();
			case SPRUCE:
				return BlockPlanks.EnumType.DARK_OAK.func_181070_c();
			case BIRCH:
				return MapColor.quartzColor;
			case JUNGLE:
				return BlockPlanks.EnumType.SPRUCE.func_181070_c();
			}
		case Y:
			return blockplanks$enumtype.func_181070_c();
		}
	}

	/**+
	 * returns a list of blocks with the same ID, but different meta
	 * (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(Item item, CreativeTabs var2, List<ItemStack> list) {
		list.add(new ItemStack(item, 1, BlockPlanks.EnumType.OAK.getMetadata()));
		list.add(new ItemStack(item, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
		list.add(new ItemStack(item, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
		list.add(new ItemStack(item, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT,
				BlockPlanks.EnumType.byMetadata((i & 3) % 4));
		switch (i & 12) {
		case 0:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
			break;
		case 4:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
			break;
		case 8:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
			break;
		default:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
		}

		return iblockstate;
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((BlockPlanks.EnumType) iblockstate.getValue(VARIANT)).getMetadata();
		switch ((BlockLog.EnumAxis) iblockstate.getValue(LOG_AXIS)) {
		case X:
			i |= 4;
			break;
		case Z:
			i |= 8;
			break;
		case NONE:
			i |= 12;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { VARIANT, LOG_AXIS });
	}

	protected ItemStack createStackedBlock(IBlockState iblockstate) {
		return new ItemStack(Item.getItemFromBlock(this), 1,
				((BlockPlanks.EnumType) iblockstate.getValue(VARIANT)).getMetadata());
	}

	/**+
	 * Gets the metadata of the item this Block can drop. This
	 * method is called when the block gets destroyed. It returns
	 * the metadata of the dropped item based on the old metadata of
	 * the block.
	 */
	public int damageDropped(IBlockState iblockstate) {
		return ((BlockPlanks.EnumType) iblockstate.getValue(VARIANT)).getMetadata();
	}
}