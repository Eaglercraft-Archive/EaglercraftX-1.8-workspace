package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
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
public class BlockTrapDoor extends Block {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool OPEN = PropertyBool.create("open");
	public static PropertyEnum<BlockTrapDoor.DoorHalf> HALF;

	protected BlockTrapDoor(Material materialIn) {
		super(materialIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(OPEN, Boolean.valueOf(false)).withProperty(HALF, BlockTrapDoor.DoorHalf.BOTTOM));
		float f = 0.5F;
		float f1 = 1.0F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	public static void bootstrapStates() {
		HALF = PropertyEnum.<BlockTrapDoor.DoorHalf>create("half", BlockTrapDoor.DoorHalf.class);
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

	public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockpos) {
		return !((Boolean) iblockaccess.getBlockState(blockpos).getValue(OPEN)).booleanValue();
	}

	public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockpos) {
		this.setBlockBoundsBasedOnState(world, blockpos);
		return super.getSelectedBoundingBox(world, blockpos);
	}

	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.setBlockBoundsBasedOnState(world, blockpos);
		return super.getCollisionBoundingBox(world, blockpos, iblockstate);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		this.setBounds(iblockaccess.getBlockState(blockpos));
	}

	/**+
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		float f = 0.1875F;
		this.setBlockBounds(0.0F, 0.40625F, 0.0F, 1.0F, 0.59375F, 1.0F);
	}

	public void setBounds(IBlockState state) {
		if (state.getBlock() == this) {
			boolean flag = state.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP;
			Boolean obool = (Boolean) state.getValue(OPEN);
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
			float f = 0.1875F;
			if (flag) {
				this.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else {
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
			}

			if (obool.booleanValue()) {
				if (enumfacing == EnumFacing.NORTH) {
					this.setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
				}

				if (enumfacing == EnumFacing.SOUTH) {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
				}

				if (enumfacing == EnumFacing.WEST) {
					this.setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				}

				if (enumfacing == EnumFacing.EAST) {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
				}
			}

		}
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		if (this.blockMaterial == Material.iron) {
			return true;
		} else {
			iblockstate = iblockstate.cycleProperty(OPEN);
			world.setBlockState(blockpos, iblockstate, 2);
			world.playAuxSFXAtEntity(entityplayer, ((Boolean) iblockstate.getValue(OPEN)).booleanValue() ? 1003 : 1006,
					blockpos, 0);
			return true;
		}
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block block) {
		if (!world.isRemote) {
			BlockPos blockpos1 = blockpos.offset(((EnumFacing) iblockstate.getValue(FACING)).getOpposite());
			if (!isValidSupportBlock(world.getBlockState(blockpos1).getBlock())) {
				world.setBlockToAir(blockpos);
				this.dropBlockAsItem(world, blockpos, iblockstate, 0);
			} else {
				boolean flag = world.isBlockPowered(blockpos);
				if (flag || block.canProvidePower()) {
					boolean flag1 = ((Boolean) iblockstate.getValue(OPEN)).booleanValue();
					if (flag1 != flag) {
						world.setBlockState(blockpos, iblockstate.withProperty(OPEN, Boolean.valueOf(flag)), 2);
						world.playAuxSFXAtEntity((EntityPlayer) null, flag ? 1003 : 1006, blockpos, 0);
					}
				}
			}
		}
	}

	/**+
	 * Ray traces through the blocks collision from start vector to
	 * end vector returning a ray trace hit.
	 */
	public MovingObjectPosition collisionRayTrace(World world, BlockPos blockpos, Vec3 vec3, Vec3 vec31) {
		this.setBlockBoundsBasedOnState(world, blockpos);
		return super.collisionRayTrace(world, blockpos, vec3, vec31);
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing enumfacing, float var4, float f, float var6,
			int var7, EntityLivingBase var8) {
		IBlockState iblockstate = this.getDefaultState();
		if (enumfacing.getAxis().isHorizontal()) {
			iblockstate = iblockstate.withProperty(FACING, enumfacing).withProperty(OPEN, Boolean.valueOf(false));
			iblockstate = iblockstate.withProperty(HALF,
					f > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
		}

		return iblockstate;
	}

	/**+
	 * Check whether this Block can be placed on the given side
	 */
	public boolean canPlaceBlockOnSide(World world, BlockPos blockpos, EnumFacing enumfacing) {
		return !enumfacing.getAxis().isVertical()
				&& isValidSupportBlock(world.getBlockState(blockpos.offset(enumfacing.getOpposite())).getBlock());
	}

	protected static EnumFacing getFacing(int meta) {
		switch (meta & 3) {
		case 0:
			return EnumFacing.NORTH;
		case 1:
			return EnumFacing.SOUTH;
		case 2:
			return EnumFacing.WEST;
		case 3:
		default:
			return EnumFacing.EAST;
		}
	}

	protected static int getMetaForFacing(EnumFacing facing) {
		switch (facing) {
		case NORTH:
			return 0;
		case SOUTH:
			return 1;
		case WEST:
			return 2;
		case EAST:
		default:
			return 3;
		}
	}

	private static boolean isValidSupportBlock(Block blockIn) {
		return blockIn.blockMaterial.isOpaque() && blockIn.isFullCube() || blockIn == Blocks.glowstone
				|| blockIn instanceof BlockSlab || blockIn instanceof BlockStairs;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, getFacing(i))
				.withProperty(OPEN, Boolean.valueOf((i & 4) != 0))
				.withProperty(HALF, (i & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | getMetaForFacing((EnumFacing) iblockstate.getValue(FACING));
		if (((Boolean) iblockstate.getValue(OPEN)).booleanValue()) {
			i |= 4;
		}

		if (iblockstate.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, OPEN, HALF });
	}

	public static enum DoorHalf implements IStringSerializable {
		TOP("top"), BOTTOM("bottom");

		private final String name;

		private DoorHalf(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}
}