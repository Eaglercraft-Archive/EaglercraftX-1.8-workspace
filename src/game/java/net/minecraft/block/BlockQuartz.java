package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
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
public class BlockQuartz extends Block {
	public static PropertyEnum<BlockQuartz.EnumType> VARIANT;

	public BlockQuartz() {
		super(Material.rock);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockQuartz.EnumType.DEFAULT));
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public static void bootstrapStates() {
		VARIANT = PropertyEnum.<BlockQuartz.EnumType>create("variant", BlockQuartz.EnumType.class);
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing enumfacing, float var4, float var5,
			float var6, int i, EntityLivingBase var8) {
		if (i == BlockQuartz.EnumType.LINES_Y.getMetadata()) {
			switch (enumfacing.getAxis()) {
			case Z:
				return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_Z);
			case X:
				return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_X);
			case Y:
			default:
				return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_Y);
			}
		} else {
			return i == BlockQuartz.EnumType.CHISELED.getMetadata()
					? this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.CHISELED)
					: this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.DEFAULT);
		}
	}

	/**+
	 * Gets the metadata of the item this Block can drop. This
	 * method is called when the block gets destroyed. It returns
	 * the metadata of the dropped item based on the old metadata of
	 * the block.
	 */
	public int damageDropped(IBlockState iblockstate) {
		BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType) iblockstate.getValue(VARIANT);
		return blockquartz$enumtype != BlockQuartz.EnumType.LINES_X
				&& blockquartz$enumtype != BlockQuartz.EnumType.LINES_Z ? blockquartz$enumtype.getMetadata()
						: BlockQuartz.EnumType.LINES_Y.getMetadata();
	}

	protected ItemStack createStackedBlock(IBlockState iblockstate) {
		BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType) iblockstate.getValue(VARIANT);
		return blockquartz$enumtype != BlockQuartz.EnumType.LINES_X
				&& blockquartz$enumtype != BlockQuartz.EnumType.LINES_Z ? super.createStackedBlock(iblockstate)
						: new ItemStack(Item.getItemFromBlock(this), 1, BlockQuartz.EnumType.LINES_Y.getMetadata());
	}

	/**+
	 * returns a list of blocks with the same ID, but different meta
	 * (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(Item item, CreativeTabs var2, List<ItemStack> list) {
		list.add(new ItemStack(item, 1, BlockQuartz.EnumType.DEFAULT.getMetadata()));
		list.add(new ItemStack(item, 1, BlockQuartz.EnumType.CHISELED.getMetadata()));
		list.add(new ItemStack(item, 1, BlockQuartz.EnumType.LINES_Y.getMetadata()));
	}

	/**+
	 * Get the MapColor for this Block and the given BlockState
	 */
	public MapColor getMapColor(IBlockState var1) {
		return MapColor.quartzColor;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.byMetadata(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((BlockQuartz.EnumType) iblockstate.getValue(VARIANT)).getMetadata();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { VARIANT });
	}

	public static enum EnumType implements IStringSerializable {
		DEFAULT(0, "default", "default"), CHISELED(1, "chiseled", "chiseled"), LINES_Y(2, "lines_y", "lines"),
		LINES_X(3, "lines_x", "lines"), LINES_Z(4, "lines_z", "lines");

		private static final BlockQuartz.EnumType[] META_LOOKUP = new BlockQuartz.EnumType[5];
		private final int meta;
		private final String field_176805_h;
		private final String unlocalizedName;

		private EnumType(int meta, String name, String unlocalizedName) {
			this.meta = meta;
			this.field_176805_h = name;
			this.unlocalizedName = unlocalizedName;
		}

		public int getMetadata() {
			return this.meta;
		}

		public String toString() {
			return this.unlocalizedName;
		}

		public static BlockQuartz.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName() {
			return this.field_176805_h;
		}

		static {
			BlockQuartz.EnumType[] types = values();
			for (int i = 0; i < types.length; ++i) {
				META_LOOKUP[types[i].getMetadata()] = types[i];
			}

		}
	}
}