package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
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
public class BlockHugeMushroom extends Block {
	public static PropertyEnum<BlockHugeMushroom.EnumType> VARIANT;
	private final Block smallBlock;

	public BlockHugeMushroom(Material parMaterial, MapColor parMapColor, Block parBlock) {
		super(parMaterial, parMapColor);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE));
		this.smallBlock = parBlock;
	}

	public static void bootstrapStates() {
		VARIANT = PropertyEnum.<BlockHugeMushroom.EnumType>create("variant", BlockHugeMushroom.EnumType.class);
	}

	/**+
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom random) {
		return Math.max(0, random.nextInt(10) - 7);
	}

	/**+
	 * Get the MapColor for this Block and the given BlockState
	 */
	public MapColor getMapColor(IBlockState iblockstate) {
		switch ((BlockHugeMushroom.EnumType) iblockstate.getValue(VARIANT)) {
		case ALL_STEM:
			return MapColor.clothColor;
		case ALL_INSIDE:
			return MapColor.sandColor;
		case STEM:
			return MapColor.sandColor;
		default:
			return super.getMapColor(iblockstate);
		}
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Item.getItemFromBlock(this.smallBlock);
	}

	public Item getItem(World var1, BlockPos var2) {
		return Item.getItemFromBlock(this.smallBlock);
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6,
			int var7, EntityLivingBase var8) {
		return this.getDefaultState();
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(VARIANT, BlockHugeMushroom.EnumType.byMetadata(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((BlockHugeMushroom.EnumType) iblockstate.getValue(VARIANT)).getMetadata();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { VARIANT });
	}

	public static enum EnumType implements IStringSerializable {
		NORTH_WEST(1, "north_west"), NORTH(2, "north"), NORTH_EAST(3, "north_east"), WEST(4, "west"),
		CENTER(5, "center"), EAST(6, "east"), SOUTH_WEST(7, "south_west"), SOUTH(8, "south"),
		SOUTH_EAST(9, "south_east"), STEM(10, "stem"), ALL_INSIDE(0, "all_inside"), ALL_OUTSIDE(14, "all_outside"),
		ALL_STEM(15, "all_stem");

		private static final BlockHugeMushroom.EnumType[] META_LOOKUP = new BlockHugeMushroom.EnumType[16];
		private final int meta;
		private final String name;

		private EnumType(int meta, String name) {
			this.meta = meta;
			this.name = name;
		}

		public int getMetadata() {
			return this.meta;
		}

		public String toString() {
			return this.name;
		}

		public static BlockHugeMushroom.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			BlockHugeMushroom.EnumType blockhugemushroom$enumtype = META_LOOKUP[meta];
			return blockhugemushroom$enumtype == null ? META_LOOKUP[0] : blockhugemushroom$enumtype;
		}

		public String getName() {
			return this.name;
		}

		static {
			BlockHugeMushroom.EnumType[] types = values();
			for (int i = 0; i < types.length; ++i) {
				META_LOOKUP[types[i].getMetadata()] = types[i];
			}

		}
	}
}