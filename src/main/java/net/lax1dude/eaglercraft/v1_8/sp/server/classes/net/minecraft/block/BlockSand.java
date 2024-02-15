package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.BlockFalling;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.material.MapColor;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.properties.IProperty;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.properties.PropertyEnum;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.state.BlockState;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.state.IBlockState;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.creativetab.CreativeTabs;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.Item;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemStack;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.IStringSerializable;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

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
public class BlockSand extends BlockFalling {

	static {
		__checkIntegratedContextValid("net/minecraft/block/BlockSand");
	}

	public static PropertyEnum<BlockSand.EnumType> VARIANT;

	public BlockSand() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockSand.EnumType.SAND));
	}

	public static void bootstrapStates() {
		VARIANT = PropertyEnum.<BlockSand.EnumType>create("variant", BlockSand.EnumType.class);
	}

	/**+
	 * Gets the metadata of the item this Block can drop. This
	 * method is called when the block gets destroyed. It returns
	 * the metadata of the dropped item based on the old metadata of
	 * the block.
	 */
	public int damageDropped(IBlockState iblockstate) {
		return ((BlockSand.EnumType) iblockstate.getValue(VARIANT)).getMetadata();
	}

	/**+
	 * returns a list of blocks with the same ID, but different meta
	 * (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(Item item, CreativeTabs var2, List<ItemStack> list) {
		for (BlockSand.EnumType blocksand$enumtype : BlockSand.EnumType.values()) {
			list.add(new ItemStack(item, 1, blocksand$enumtype.getMetadata()));
		}

	}

	/**+
	 * Get the MapColor for this Block and the given BlockState
	 */
	public MapColor getMapColor(IBlockState iblockstate) {
		return ((BlockSand.EnumType) iblockstate.getValue(VARIANT)).getMapColor();
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(VARIANT, BlockSand.EnumType.byMetadata(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((BlockSand.EnumType) iblockstate.getValue(VARIANT)).getMetadata();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { VARIANT });
	}

	public static enum EnumType implements IStringSerializable {
		SAND(0, "sand", "default", MapColor.sandColor), RED_SAND(1, "red_sand", "red", MapColor.adobeColor);

		private static final BlockSand.EnumType[] META_LOOKUP = new BlockSand.EnumType[values().length];
		private final int meta;
		private final String name;
		private final MapColor mapColor;
		private final String unlocalizedName;

		private EnumType(int meta, String name, String unlocalizedName, MapColor mapColor) {
			this.meta = meta;
			this.name = name;
			this.mapColor = mapColor;
			this.unlocalizedName = unlocalizedName;
		}

		public int getMetadata() {
			return this.meta;
		}

		public String toString() {
			return this.name;
		}

		/**+
		 * Get the MapColor for this Block and the given BlockState
		 */
		public MapColor getMapColor() {
			return this.mapColor;
		}

		public static BlockSand.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName() {
			return this.name;
		}

		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		static {
			for (BlockSand.EnumType blocksand$enumtype : values()) {
				META_LOOKUP[blocksand$enumtype.getMetadata()] = blocksand$enumtype;
			}

		}
	}
}