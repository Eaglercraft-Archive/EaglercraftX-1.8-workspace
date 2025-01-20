package net.minecraft.block;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

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
public abstract class BlockFlower extends BlockBush {
	protected PropertyEnum<BlockFlower.EnumFlowerType> type;

	protected BlockFlower() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(this.getTypeProperty(),
				this.getBlockType() == BlockFlower.EnumFlowerColor.RED ? BlockFlower.EnumFlowerType.POPPY
						: BlockFlower.EnumFlowerType.DANDELION));
	}

	/**+
	 * Gets the metadata of the item this Block can drop. This
	 * method is called when the block gets destroyed. It returns
	 * the metadata of the dropped item based on the old metadata of
	 * the block.
	 */
	public int damageDropped(IBlockState iblockstate) {
		return ((BlockFlower.EnumFlowerType) iblockstate.getValue(this.getTypeProperty())).getMeta();
	}

	/**+
	 * returns a list of blocks with the same ID, but different meta
	 * (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(Item item, CreativeTabs var2, List<ItemStack> list) {
		BlockFlower.EnumFlowerType[] flowerTypes = BlockFlower.EnumFlowerType.getTypes(this.getBlockType());
		for (int i = 0; i < flowerTypes.length; ++i) {
			list.add(new ItemStack(item, 1, flowerTypes[i].getMeta()));
		}

	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(this.getTypeProperty(),
				BlockFlower.EnumFlowerType.getType(this.getBlockType(), i));
	}

	public abstract BlockFlower.EnumFlowerColor getBlockType();

	public IProperty<BlockFlower.EnumFlowerType> getTypeProperty() {
		if (this.type == null) {
			this.type = PropertyEnum.create("type", BlockFlower.EnumFlowerType.class,
					new Predicate<BlockFlower.EnumFlowerType>() {
						public boolean apply(BlockFlower.EnumFlowerType blockflower$enumflowertype) {
							return blockflower$enumflowertype.getBlockType() == BlockFlower.this.getBlockType();
						}
					});
		}

		return this.type;
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((BlockFlower.EnumFlowerType) iblockstate.getValue(this.getTypeProperty())).getMeta();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { this.getTypeProperty() });
	}

	/**+
	 * Get the OffsetType for this Block. Determines if the model is
	 * rendered slightly offset.
	 */
	public Block.EnumOffsetType getOffsetType() {
		return Block.EnumOffsetType.XZ;
	}

	public static enum EnumFlowerColor {
		YELLOW, RED;

		public BlockFlower getBlock() {
			return this == YELLOW ? Blocks.yellow_flower : Blocks.red_flower;
		}
	}

	public static enum EnumFlowerType implements IStringSerializable {
		DANDELION(BlockFlower.EnumFlowerColor.YELLOW, 0, "dandelion"),
		POPPY(BlockFlower.EnumFlowerColor.RED, 0, "poppy"),
		BLUE_ORCHID(BlockFlower.EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"),
		ALLIUM(BlockFlower.EnumFlowerColor.RED, 2, "allium"),
		HOUSTONIA(BlockFlower.EnumFlowerColor.RED, 3, "houstonia"),
		RED_TULIP(BlockFlower.EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"),
		ORANGE_TULIP(BlockFlower.EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"),
		WHITE_TULIP(BlockFlower.EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"),
		PINK_TULIP(BlockFlower.EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"),
		OXEYE_DAISY(BlockFlower.EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");

		public static final BlockFlower.EnumFlowerType[] _VALUES = EnumFlowerType.values();

		private static final BlockFlower.EnumFlowerType[][] TYPES_FOR_BLOCK = new BlockFlower.EnumFlowerType[_VALUES.length][];
		private final BlockFlower.EnumFlowerColor blockType;
		private final int meta;
		private final String name;
		private final String unlocalizedName;

		private EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name) {
			this(blockType, meta, name, name);
		}

		private EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name, String unlocalizedName) {
			this.blockType = blockType;
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		/**+
		 * Get the Type of this flower (Yellow/Red)
		 */
		public BlockFlower.EnumFlowerColor getBlockType() {
			return this.blockType;
		}

		public int getMeta() {
			return this.meta;
		}

		public static BlockFlower.EnumFlowerType getType(BlockFlower.EnumFlowerColor blockType, int meta) {
			BlockFlower.EnumFlowerType[] ablockflower$enumflowertype = TYPES_FOR_BLOCK[blockType.ordinal()];
			if (meta < 0 || meta >= ablockflower$enumflowertype.length) {
				meta = 0;
			}

			return ablockflower$enumflowertype[meta];
		}

		public static BlockFlower.EnumFlowerType[] getTypes(BlockFlower.EnumFlowerColor flowerColor) {
			return TYPES_FOR_BLOCK[flowerColor.ordinal()];
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}

		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		static {
			BlockFlower.EnumFlowerColor[] colors = BlockFlower.EnumFlowerColor.values();
			for (int i = 0; i < colors.length; ++i) {
				final BlockFlower.EnumFlowerColor blockflower$enumflowercolor = colors[i];
				Collection collection = Collections2.filter(Lists.newArrayList(values()),
						new Predicate<BlockFlower.EnumFlowerType>() {
							public boolean apply(BlockFlower.EnumFlowerType blockflower$enumflowertype) {
								return blockflower$enumflowertype.getBlockType() == blockflower$enumflowercolor;
							}
						});
				TYPES_FOR_BLOCK[blockflower$enumflowercolor.ordinal()] = (BlockFlower.EnumFlowerType[]) collection
						.toArray(new BlockFlower.EnumFlowerType[collection.size()]);
			}

		}
	}
}