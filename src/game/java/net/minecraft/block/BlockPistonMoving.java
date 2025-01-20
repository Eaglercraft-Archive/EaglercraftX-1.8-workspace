package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
public class BlockPistonMoving extends BlockContainer {
	public static final PropertyDirection FACING = BlockPistonExtension.FACING;
	public static PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;

	public BlockPistonMoving() {
		super(Material.piston);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE,
				BlockPistonExtension.EnumPistonType.DEFAULT));
		this.setHardness(-1.0F);
	}

	public static void bootstrapStates() {
		TYPE = BlockPistonExtension.TYPE;
	}

	/**+
	 * Returns a new instance of a block's tile entity class. Called
	 * on placing the block.
	 */
	public TileEntity createNewTileEntity(World var1, int var2) {
		return null;
	}

	public static TileEntity newTileEntity(IBlockState state, EnumFacing facing, boolean extending,
			boolean renderHead) {
		return new TileEntityPiston(state, facing, extending, renderHead);
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		TileEntity tileentity = world.getTileEntity(blockpos);
		if (tileentity instanceof TileEntityPiston) {
			((TileEntityPiston) tileentity).clearPistonTileEntity();
		} else {
			super.breakBlock(world, blockpos, iblockstate);
		}

	}

	public boolean canPlaceBlockAt(World var1, BlockPos var2) {
		return false;
	}

	/**+
	 * Check whether this Block can be placed on the given side
	 */
	public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
		return false;
	}

	/**+
	 * Called when a player destroys this Block
	 */
	public void onBlockDestroyedByPlayer(World world, BlockPos blockpos, IBlockState iblockstate) {
		BlockPos blockpos1 = blockpos.offset(((EnumFacing) iblockstate.getValue(FACING)).getOpposite());
		IBlockState iblockstate1 = world.getBlockState(blockpos1);
		if (iblockstate1.getBlock() instanceof BlockPistonBase
				&& ((Boolean) iblockstate1.getValue(BlockPistonBase.EXTENDED)).booleanValue()) {
			world.setBlockToAir(blockpos1);
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

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState var3, EntityPlayer var4,
			EnumFacing var5, float var6, float var7, float var8) {
		if (!world.isRemote && world.getTileEntity(blockpos) == null) {
			world.setBlockToAir(blockpos);
			return true;
		} else {
			return false;
		}
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return null;
	}

	/**+
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World world, BlockPos blockpos, IBlockState var3, float var4, int var5) {
		if (!world.isRemote) {
			TileEntityPiston tileentitypiston = this.getTileEntity(world, blockpos);
			if (tileentitypiston != null) {
				IBlockState iblockstate = tileentitypiston.getPistonState();
				iblockstate.getBlock().dropBlockAsItem(world, blockpos, iblockstate, 0);
			}
		}
	}

	/**+
	 * Ray traces through the blocks collision from start vector to
	 * end vector returning a ray trace hit.
	 */
	public MovingObjectPosition collisionRayTrace(World var1, BlockPos var2, Vec3 var3, Vec3 var4) {
		return null;
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState var3, Block var4) {
		if (!world.isRemote) {
			world.getTileEntity(blockpos);
		}
	}

	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockpos, IBlockState var3) {
		TileEntityPiston tileentitypiston = this.getTileEntity(world, blockpos);
		if (tileentitypiston == null) {
			return null;
		} else {
			float f = tileentitypiston.getProgress(0.0F);
			if (tileentitypiston.isExtending()) {
				f = 1.0F - f;
			}

			return this.getBoundingBox(world, blockpos, tileentitypiston.getPistonState(), f,
					tileentitypiston.getFacing());
		}
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		TileEntityPiston tileentitypiston = this.getTileEntity(iblockaccess, blockpos);
		if (tileentitypiston != null) {
			IBlockState iblockstate = tileentitypiston.getPistonState();
			Block block = iblockstate.getBlock();
			if (block == this || block.getMaterial() == Material.air) {
				return;
			}

			float f = tileentitypiston.getProgress(0.0F);
			if (tileentitypiston.isExtending()) {
				f = 1.0F - f;
			}

			block.setBlockBoundsBasedOnState(iblockaccess, blockpos);
			if (block == Blocks.piston || block == Blocks.sticky_piston) {
				f = 0.0F;
			}

			EnumFacing enumfacing = tileentitypiston.getFacing();
			this.minX = block.getBlockBoundsMinX() - (double) ((float) enumfacing.getFrontOffsetX() * f);
			this.minY = block.getBlockBoundsMinY() - (double) ((float) enumfacing.getFrontOffsetY() * f);
			this.minZ = block.getBlockBoundsMinZ() - (double) ((float) enumfacing.getFrontOffsetZ() * f);
			this.maxX = block.getBlockBoundsMaxX() - (double) ((float) enumfacing.getFrontOffsetX() * f);
			this.maxY = block.getBlockBoundsMaxY() - (double) ((float) enumfacing.getFrontOffsetY() * f);
			this.maxZ = block.getBlockBoundsMaxZ() - (double) ((float) enumfacing.getFrontOffsetZ() * f);
		}

	}

	public AxisAlignedBB getBoundingBox(World worldIn, BlockPos pos, IBlockState extendingBlock, float progress,
			EnumFacing direction) {
		if (extendingBlock.getBlock() != this && extendingBlock.getBlock().getMaterial() != Material.air) {
			AxisAlignedBB axisalignedbb = extendingBlock.getBlock().getCollisionBoundingBox(worldIn, pos,
					extendingBlock);
			if (axisalignedbb == null) {
				return null;
			} else {
				double d0 = axisalignedbb.minX;
				double d1 = axisalignedbb.minY;
				double d2 = axisalignedbb.minZ;
				double d3 = axisalignedbb.maxX;
				double d4 = axisalignedbb.maxY;
				double d5 = axisalignedbb.maxZ;
				if (direction.getFrontOffsetX() < 0) {
					d0 -= (double) ((float) direction.getFrontOffsetX() * progress);
				} else {
					d3 -= (double) ((float) direction.getFrontOffsetX() * progress);
				}

				if (direction.getFrontOffsetY() < 0) {
					d1 -= (double) ((float) direction.getFrontOffsetY() * progress);
				} else {
					d4 -= (double) ((float) direction.getFrontOffsetY() * progress);
				}

				if (direction.getFrontOffsetZ() < 0) {
					d2 -= (double) ((float) direction.getFrontOffsetZ() * progress);
				} else {
					d5 -= (double) ((float) direction.getFrontOffsetZ() * progress);
				}

				return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
			}
		} else {
			return null;
		}
	}

	private TileEntityPiston getTileEntity(IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity instanceof TileEntityPiston ? (TileEntityPiston) tileentity : null;
	}

	public Item getItem(World var1, BlockPos var2) {
		return null;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, BlockPistonExtension.getFacing(i)).withProperty(TYPE,
				(i & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((EnumFacing) iblockstate.getValue(FACING)).getIndex();
		if (iblockstate.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, TYPE });
	}
}