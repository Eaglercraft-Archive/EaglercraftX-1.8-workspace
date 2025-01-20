package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
public class BlockFenceGate extends BlockDirectional {
	public static final PropertyBool OPEN = PropertyBool.create("open");
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");

	public BlockFenceGate(BlockPlanks.EnumType parEnumType) {
		super(Material.wood, parEnumType.func_181070_c());
		this.setDefaultState(this.blockState.getBaseState().withProperty(OPEN, Boolean.valueOf(false))
				.withProperty(POWERED, Boolean.valueOf(false)).withProperty(IN_WALL, Boolean.valueOf(false)));
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		EnumFacing.Axis enumfacing$axis = ((EnumFacing) iblockstate.getValue(FACING)).getAxis();
		if (enumfacing$axis == EnumFacing.Axis.Z
				&& (iblockaccess.getBlockState(blockpos.west()).getBlock() == Blocks.cobblestone_wall
						|| iblockaccess.getBlockState(blockpos.east()).getBlock() == Blocks.cobblestone_wall)
				|| enumfacing$axis == EnumFacing.Axis.X && (iblockaccess.getBlockState(blockpos.north())
						.getBlock() == Blocks.cobblestone_wall
						|| iblockaccess.getBlockState(blockpos.south()).getBlock() == Blocks.cobblestone_wall)) {
			iblockstate = iblockstate.withProperty(IN_WALL, Boolean.valueOf(true));
		}

		return iblockstate;
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		return world.getBlockState(blockpos.down()).getBlock().getMaterial().isSolid()
				? super.canPlaceBlockAt(world, blockpos)
				: false;
	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos blockpos, IBlockState iblockstate) {
		if (((Boolean) iblockstate.getValue(OPEN)).booleanValue()) {
			return null;
		} else {
			EnumFacing.Axis enumfacing$axis = ((EnumFacing) iblockstate.getValue(FACING)).getAxis();
			return enumfacing$axis == EnumFacing.Axis.Z
					? new AxisAlignedBB((double) blockpos.getX(), (double) blockpos.getY(),
							(double) ((float) blockpos.getZ() + 0.375F), (double) (blockpos.getX() + 1),
							(double) ((float) blockpos.getY() + 1.5F), (double) ((float) blockpos.getZ() + 0.625F))
					: new AxisAlignedBB((double) ((float) blockpos.getX() + 0.375F), (double) blockpos.getY(),
							(double) blockpos.getZ(), (double) ((float) blockpos.getX() + 0.625F),
							(double) ((float) blockpos.getY() + 1.5F), (double) (blockpos.getZ() + 1));
		}
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		EnumFacing.Axis enumfacing$axis = ((EnumFacing) iblockaccess.getBlockState(blockpos).getValue(FACING))
				.getAxis();
		if (enumfacing$axis == EnumFacing.Axis.Z) {
			this.setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
		} else {
			this.setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
		}

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
		return ((Boolean) iblockaccess.getBlockState(blockpos).getValue(OPEN)).booleanValue();
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6,
			int var7, EntityLivingBase entitylivingbase) {
		return this.getDefaultState().withProperty(FACING, entitylivingbase.getHorizontalFacing())
				.withProperty(OPEN, Boolean.valueOf(false)).withProperty(POWERED, Boolean.valueOf(false))
				.withProperty(IN_WALL, Boolean.valueOf(false));
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		if (((Boolean) iblockstate.getValue(OPEN)).booleanValue()) {
			iblockstate = iblockstate.withProperty(OPEN, Boolean.valueOf(false));
			world.setBlockState(blockpos, iblockstate, 2);
		} else {
			EnumFacing enumfacing = EnumFacing.fromAngle((double) entityplayer.rotationYaw);
			if (iblockstate.getValue(FACING) == enumfacing.getOpposite()) {
				iblockstate = iblockstate.withProperty(FACING, enumfacing);
			}

			iblockstate = iblockstate.withProperty(OPEN, Boolean.valueOf(true));
			world.setBlockState(blockpos, iblockstate, 2);
		}

		world.playAuxSFXAtEntity(entityplayer, ((Boolean) iblockstate.getValue(OPEN)).booleanValue() ? 1003 : 1006,
				blockpos, 0);
		return true;
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block block) {
		if (!world.isRemote) {
			boolean flag = world.isBlockPowered(blockpos);
			if (flag || block.canProvidePower()) {
				if (flag && !((Boolean) iblockstate.getValue(OPEN)).booleanValue()
						&& !((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
					world.setBlockState(blockpos, iblockstate.withProperty(OPEN, Boolean.valueOf(true))
							.withProperty(POWERED, Boolean.valueOf(true)), 2);
					world.playAuxSFXAtEntity((EntityPlayer) null, 1003, blockpos, 0);
				} else if (!flag && ((Boolean) iblockstate.getValue(OPEN)).booleanValue()
						&& ((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
					world.setBlockState(blockpos, iblockstate.withProperty(OPEN, Boolean.valueOf(false))
							.withProperty(POWERED, Boolean.valueOf(false)), 2);
					world.playAuxSFXAtEntity((EntityPlayer) null, 1006, blockpos, 0);
				} else if (flag != ((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
					world.setBlockState(blockpos, iblockstate.withProperty(POWERED, Boolean.valueOf(flag)), 2);
				}
			}
		}
	}

	public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
		return true;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(i))
				.withProperty(OPEN, Boolean.valueOf((i & 4) != 0)).withProperty(POWERED, Boolean.valueOf((i & 8) != 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((EnumFacing) iblockstate.getValue(FACING)).getHorizontalIndex();
		if (((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
			i |= 8;
		}

		if (((Boolean) iblockstate.getValue(OPEN)).booleanValue()) {
			i |= 4;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, OPEN, POWERED, IN_WALL });
	}
}