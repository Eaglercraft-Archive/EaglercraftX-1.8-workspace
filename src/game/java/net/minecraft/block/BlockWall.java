package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
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
public class BlockWall extends Block {
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static PropertyEnum<BlockWall.EnumType> VARIANT;

	public BlockWall(Block modelBlock) {
		super(modelBlock.blockMaterial);
		this.setDefaultState(this.blockState.getBaseState().withProperty(UP, Boolean.valueOf(false))
				.withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false))
				.withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false))
				.withProperty(VARIANT, BlockWall.EnumType.NORMAL));
		this.setHardness(modelBlock.blockHardness);
		this.setResistance(modelBlock.blockResistance / 3.0F);
		this.setStepSound(modelBlock.stepSound);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public static void bootstrapStates() {
		VARIANT = PropertyEnum.<BlockWall.EnumType>create("variant", BlockWall.EnumType.class);
	}

	/**+
	 * Gets the localized name of this block. Used for the
	 * statistics page.
	 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal(
				this.getUnlocalizedName() + "." + BlockWall.EnumType.NORMAL.getUnlocalizedName() + ".name");
	}

	public boolean isFullCube() {
		return false;
	}

	public boolean isPassable(IBlockAccess var1, BlockPos var2) {
		return false;
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		boolean flag = this.canConnectTo(iblockaccess, blockpos.north());
		boolean flag1 = this.canConnectTo(iblockaccess, blockpos.south());
		boolean flag2 = this.canConnectTo(iblockaccess, blockpos.west());
		boolean flag3 = this.canConnectTo(iblockaccess, blockpos.east());
		float f = 0.25F;
		float f1 = 0.75F;
		float f2 = 0.25F;
		float f3 = 0.75F;
		float f4 = 1.0F;
		if (flag) {
			f2 = 0.0F;
		}

		if (flag1) {
			f3 = 1.0F;
		}

		if (flag2) {
			f = 0.0F;
		}

		if (flag3) {
			f1 = 1.0F;
		}

		if (flag && flag1 && !flag2 && !flag3) {
			f4 = 0.8125F;
			f = 0.3125F;
			f1 = 0.6875F;
		} else if (!flag && !flag1 && flag2 && flag3) {
			f4 = 0.8125F;
			f2 = 0.3125F;
			f3 = 0.6875F;
		}

		this.setBlockBounds(f, 0.0F, f2, f1, f4, f3);
	}

	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.setBlockBoundsBasedOnState(world, blockpos);
		this.maxY = 1.5D;
		return super.getCollisionBoundingBox(world, blockpos, iblockstate);
	}

	public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos).getBlock();
		return block == Blocks.barrier ? false
				: (block != this && !(block instanceof BlockFenceGate)
						? (block.blockMaterial.isOpaque() && block.isFullCube() ? block.blockMaterial != Material.gourd
								: false)
						: true);
	}

	/**+
	 * returns a list of blocks with the same ID, but different meta
	 * (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(Item item, CreativeTabs var2, List<ItemStack> list) {
		BlockWall.EnumType[] types = BlockWall.EnumType.META_LOOKUP;
		for (int i = 0; i < types.length; ++i) {
			list.add(new ItemStack(item, 1, types[i].getMetadata()));
		}

	}

	/**+
	 * Gets the metadata of the item this Block can drop. This
	 * method is called when the block gets destroyed. It returns
	 * the metadata of the dropped item based on the old metadata of
	 * the block.
	 */
	public int damageDropped(IBlockState iblockstate) {
		return ((BlockWall.EnumType) iblockstate.getValue(VARIANT)).getMetadata();
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, BlockPos blockpos, EnumFacing enumfacing) {
		return enumfacing == EnumFacing.DOWN ? super.shouldSideBeRendered(iblockaccess, blockpos, enumfacing) : true;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(VARIANT, BlockWall.EnumType.byMetadata(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((BlockWall.EnumType) iblockstate.getValue(VARIANT)).getMetadata();
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		return iblockstate.withProperty(UP, Boolean.valueOf(!iblockaccess.isAirBlock(blockpos.up())))
				.withProperty(NORTH, Boolean.valueOf(this.canConnectTo(iblockaccess, blockpos.north())))
				.withProperty(EAST, Boolean.valueOf(this.canConnectTo(iblockaccess, blockpos.east())))
				.withProperty(SOUTH, Boolean.valueOf(this.canConnectTo(iblockaccess, blockpos.south())))
				.withProperty(WEST, Boolean.valueOf(this.canConnectTo(iblockaccess, blockpos.west())));
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { UP, NORTH, EAST, WEST, SOUTH, VARIANT });
	}

	public static enum EnumType implements IStringSerializable {
		NORMAL(0, "cobblestone", "normal"), MOSSY(1, "mossy_cobblestone", "mossy");

		public static final BlockWall.EnumType[] META_LOOKUP = new BlockWall.EnumType[2];
		private final int meta;
		private final String name;
		private String unlocalizedName;

		private EnumType(int meta, String name, String unlocalizedName) {
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		public int getMetadata() {
			return this.meta;
		}

		public String toString() {
			return this.name;
		}

		public static BlockWall.EnumType byMetadata(int meta) {
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
			BlockWall.EnumType[] types = values();
			for (int i = 0; i < types.length; ++i) {
				META_LOOKUP[types[i].getMetadata()] = types[i];
			}

		}
	}
}