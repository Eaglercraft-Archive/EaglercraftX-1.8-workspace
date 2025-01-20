package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
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
public class BlockLever extends Block {
	public static PropertyEnum<BlockLever.EnumOrientation> FACING;
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	protected BlockLever() {
		super(Material.circuits);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, BlockLever.EnumOrientation.NORTH)
				.withProperty(POWERED, Boolean.valueOf(false)));
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	public static void bootstrapStates() {
		FACING = PropertyEnum.<BlockLever.EnumOrientation>create("facing", BlockLever.EnumOrientation.class);
	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
		return null;
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	public boolean isFullCube() {
		return false;
	}

	/**+
	 * Check whether this Block can be placed on the given side
	 */
	public boolean canPlaceBlockOnSide(World world, BlockPos blockpos, EnumFacing enumfacing) {
		return func_181090_a(world, blockpos, enumfacing.getOpposite());
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		EnumFacing[] facings = EnumFacing._VALUES;
		for (int i = 0; i < facings.length; ++i) {
			EnumFacing enumfacing = facings[i];
			if (func_181090_a(world, blockpos, enumfacing)) {
				return true;
			}
		}

		return false;
	}

	protected static boolean func_181090_a(World parWorld, BlockPos parBlockPos, EnumFacing parEnumFacing) {
		return BlockButton.func_181088_a(parWorld, parBlockPos, parEnumFacing);
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World world, BlockPos blockpos, EnumFacing enumfacing, float var4, float var5,
			float var6, int var7, EntityLivingBase entitylivingbase) {
		IBlockState iblockstate = this.getDefaultState().withProperty(POWERED, Boolean.valueOf(false));
		if (func_181090_a(world, blockpos, enumfacing.getOpposite())) {
			return iblockstate.withProperty(FACING,
					BlockLever.EnumOrientation.forFacings(enumfacing, entitylivingbase.getHorizontalFacing()));
		} else {
			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
			for (int i = 0; i < facings.length; ++i) {
				EnumFacing enumfacing1 = facings[i];
				if (enumfacing1 != enumfacing && func_181090_a(world, blockpos, enumfacing1.getOpposite())) {
					return iblockstate.withProperty(FACING,
							BlockLever.EnumOrientation.forFacings(enumfacing1, entitylivingbase.getHorizontalFacing()));
				}
			}

			if (World.doesBlockHaveSolidTopSurface(world, blockpos.down())) {
				return iblockstate.withProperty(FACING,
						BlockLever.EnumOrientation.forFacings(EnumFacing.UP, entitylivingbase.getHorizontalFacing()));
			} else {
				return iblockstate;
			}
		}
	}

	public static int getMetadataForFacing(EnumFacing facing) {
		switch (facing) {
		case DOWN:
			return 0;
		case UP:
			return 5;
		case NORTH:
			return 4;
		case SOUTH:
			return 3;
		case WEST:
			return 2;
		case EAST:
			return 1;
		default:
			return -1;
		}
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		if (this.func_181091_e(world, blockpos, iblockstate) && !func_181090_a(world, blockpos,
				((BlockLever.EnumOrientation) iblockstate.getValue(FACING)).getFacing().getOpposite())) {
			this.dropBlockAsItem(world, blockpos, iblockstate, 0);
			world.setBlockToAir(blockpos);
		}

	}

	private boolean func_181091_e(World parWorld, BlockPos parBlockPos, IBlockState parIBlockState) {
		if (this.canPlaceBlockAt(parWorld, parBlockPos)) {
			return true;
		} else {
			this.dropBlockAsItem(parWorld, parBlockPos, parIBlockState, 0);
			parWorld.setBlockToAir(parBlockPos);
			return false;
		}
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		float f = 0.1875F;
		switch ((BlockLever.EnumOrientation) iblockaccess.getBlockState(blockpos).getValue(FACING)) {
		case EAST:
			this.setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
			break;
		case WEST:
			this.setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
			break;
		case SOUTH:
			this.setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
			break;
		case NORTH:
			this.setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
			break;
		case UP_Z:
		case UP_X:
			f = 0.25F;
			this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
			break;
		case DOWN_X:
		case DOWN_Z:
			f = 0.25F;
			this.setBlockBounds(0.5F - f, 0.4F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
		}

	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer var4,
			EnumFacing var5, float var6, float var7, float var8) {
		if (world.isRemote) {
			return true;
		} else {
			iblockstate = iblockstate.cycleProperty(POWERED);
			world.setBlockState(blockpos, iblockstate, 3);
			world.playSoundEffect((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D,
					(double) blockpos.getZ() + 0.5D, "random.click", 0.3F,
					((Boolean) iblockstate.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F);
			world.notifyNeighborsOfStateChange(blockpos, this);
			EnumFacing enumfacing = ((BlockLever.EnumOrientation) iblockstate.getValue(FACING)).getFacing();
			world.notifyNeighborsOfStateChange(blockpos.offset(enumfacing.getOpposite()), this);
			return true;
		}
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		if (((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
			world.notifyNeighborsOfStateChange(blockpos, this);
			EnumFacing enumfacing = ((BlockLever.EnumOrientation) iblockstate.getValue(FACING)).getFacing();
			world.notifyNeighborsOfStateChange(blockpos.offset(enumfacing.getOpposite()), this);
		}

		super.breakBlock(world, blockpos, iblockstate);
	}

	public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState iblockstate, EnumFacing var4) {
		return ((Boolean) iblockstate.getValue(POWERED)).booleanValue() ? 15 : 0;
	}

	public int getStrongPower(IBlockAccess var1, BlockPos var2, IBlockState iblockstate, EnumFacing enumfacing) {
		return !((Boolean) iblockstate.getValue(POWERED)).booleanValue() ? 0
				: (((BlockLever.EnumOrientation) iblockstate.getValue(FACING)).getFacing() == enumfacing ? 15 : 0);
	}

	/**+
	 * Can this block provide power. Only wire currently seems to
	 * have this change based on its state.
	 */
	public boolean canProvidePower() {
		return true;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, BlockLever.EnumOrientation.byMetadata(i & 7))
				.withProperty(POWERED, Boolean.valueOf((i & 8) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((BlockLever.EnumOrientation) iblockstate.getValue(FACING)).getMetadata();
		if (((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, POWERED });
	}

	public static enum EnumOrientation implements IStringSerializable {
		DOWN_X(0, "down_x", EnumFacing.DOWN), EAST(1, "east", EnumFacing.EAST), WEST(2, "west", EnumFacing.WEST),
		SOUTH(3, "south", EnumFacing.SOUTH), NORTH(4, "north", EnumFacing.NORTH), UP_Z(5, "up_z", EnumFacing.UP),
		UP_X(6, "up_x", EnumFacing.UP), DOWN_Z(7, "down_z", EnumFacing.DOWN);

		private static final BlockLever.EnumOrientation[] META_LOOKUP = new BlockLever.EnumOrientation[8];
		private final int meta;
		private final String name;
		private final EnumFacing facing;

		private EnumOrientation(int meta, String name, EnumFacing facing) {
			this.meta = meta;
			this.name = name;
			this.facing = facing;
		}

		public int getMetadata() {
			return this.meta;
		}

		public EnumFacing getFacing() {
			return this.facing;
		}

		public String toString() {
			return this.name;
		}

		public static BlockLever.EnumOrientation byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public static BlockLever.EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing) {
			switch (clickedSide) {
			case DOWN:
				switch (entityFacing.getAxis()) {
				case X:
					return DOWN_X;
				case Z:
					return DOWN_Z;
				default:
					throw new IllegalArgumentException(
							"Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
				}
			case UP:
				switch (entityFacing.getAxis()) {
				case X:
					return UP_X;
				case Z:
					return UP_Z;
				default:
					throw new IllegalArgumentException(
							"Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
				}
			case NORTH:
				return NORTH;
			case SOUTH:
				return SOUTH;
			case WEST:
				return WEST;
			case EAST:
				return EAST;
			default:
				throw new IllegalArgumentException("Invalid facing: " + clickedSide);
			}
		}

		public String getName() {
			return this.name;
		}

		static {
			BlockLever.EnumOrientation[] orientations = values();
			for (int i = 0; i < orientations.length; ++i) {
				META_LOOKUP[orientations[i].getMetadata()] = orientations[i];
			}

		}
	}
}