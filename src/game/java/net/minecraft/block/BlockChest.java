package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
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
public class BlockChest extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public final int chestType;

	protected BlockChest(int type) {
		super(Material.wood);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.chestType = type;
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
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
	 * The type of render function called. 3 for standard block
	 * models, 2 for TESR's, 1 for liquids, -1 is no render
	 */
	public int getRenderType() {
		return 2;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		if (iblockaccess.getBlockState(blockpos.north()).getBlock() == this) {
			this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
		} else if (iblockaccess.getBlockState(blockpos.south()).getBlock() == this) {
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
		} else if (iblockaccess.getBlockState(blockpos.west()).getBlock() == this) {
			this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		} else if (iblockaccess.getBlockState(blockpos.east()).getBlock() == this) {
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
		} else {
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		}

	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.checkForSurroundingChests(world, blockpos, iblockstate);

		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facings();
		for (int i = 0; i < facings.length; ++i) {
			EnumFacing enumfacing = facings[i];
			BlockPos blockpos1 = blockpos.offset(enumfacing);
			IBlockState iblockstate1 = world.getBlockState(blockpos1);
			if (iblockstate1.getBlock() == this) {
				this.checkForSurroundingChests(world, blockpos1, iblockstate1);
			}
		}

	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6,
			int var7, EntityLivingBase entitylivingbase) {
		return this.getDefaultState().withProperty(FACING, entitylivingbase.getHorizontalFacing());
	}

	/**+
	 * Called by ItemBlocks after a block is set in the world, to
	 * allow post-place logic
	 */
	public void onBlockPlacedBy(World world, BlockPos blockpos, IBlockState iblockstate,
			EntityLivingBase entitylivingbase, ItemStack itemstack) {
		EnumFacing enumfacing = EnumFacing
				.getHorizontal(
						MathHelper.floor_double((double) (entitylivingbase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3)
				.getOpposite();
		iblockstate = iblockstate.withProperty(FACING, enumfacing);
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.south();
		BlockPos blockpos3 = blockpos.west();
		BlockPos blockpos4 = blockpos.east();
		boolean flag = this == world.getBlockState(blockpos1).getBlock();
		boolean flag1 = this == world.getBlockState(blockpos2).getBlock();
		boolean flag2 = this == world.getBlockState(blockpos3).getBlock();
		boolean flag3 = this == world.getBlockState(blockpos4).getBlock();
		if (!flag && !flag1 && !flag2 && !flag3) {
			world.setBlockState(blockpos, iblockstate, 3);
		} else if (enumfacing.getAxis() != EnumFacing.Axis.X || !flag && !flag1) {
			if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flag2 || flag3)) {
				if (flag2) {
					world.setBlockState(blockpos3, iblockstate, 3);
				} else {
					world.setBlockState(blockpos4, iblockstate, 3);
				}

				world.setBlockState(blockpos, iblockstate, 3);
			}
		} else {
			if (flag) {
				world.setBlockState(blockpos1, iblockstate, 3);
			} else {
				world.setBlockState(blockpos2, iblockstate, 3);
			}

			world.setBlockState(blockpos, iblockstate, 3);
		}

		if (itemstack.hasDisplayName()) {
			TileEntity tileentity = world.getTileEntity(blockpos);
			if (tileentity instanceof TileEntityChest) {
				((TileEntityChest) tileentity).setCustomName(itemstack.getDisplayName());
			}
		}

	}

	public IBlockState checkForSurroundingChests(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			IBlockState iblockstate = worldIn.getBlockState(pos.north());
			IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
			IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
			IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
			Block block = iblockstate.getBlock();
			Block block1 = iblockstate1.getBlock();
			Block block2 = iblockstate2.getBlock();
			Block block3 = iblockstate3.getBlock();
			if (block != this && block1 != this) {
				boolean flag = block.isFullBlock();
				boolean flag1 = block1.isFullBlock();
				if (block2 == this || block3 == this) {
					BlockPos blockpos1 = block2 == this ? pos.west() : pos.east();
					IBlockState iblockstate6 = worldIn.getBlockState(blockpos1.north());
					IBlockState iblockstate7 = worldIn.getBlockState(blockpos1.south());
					enumfacing = EnumFacing.SOUTH;
					EnumFacing enumfacing2;
					if (block2 == this) {
						enumfacing2 = (EnumFacing) iblockstate2.getValue(FACING);
					} else {
						enumfacing2 = (EnumFacing) iblockstate3.getValue(FACING);
					}

					if (enumfacing2 == EnumFacing.NORTH) {
						enumfacing = EnumFacing.NORTH;
					}

					Block block6 = iblockstate6.getBlock();
					Block block7 = iblockstate7.getBlock();
					if ((flag || block6.isFullBlock()) && !flag1 && !block7.isFullBlock()) {
						enumfacing = EnumFacing.SOUTH;
					}

					if ((flag1 || block7.isFullBlock()) && !flag && !block6.isFullBlock()) {
						enumfacing = EnumFacing.NORTH;
					}
				}
			} else {
				BlockPos blockpos = block == this ? pos.north() : pos.south();
				IBlockState iblockstate4 = worldIn.getBlockState(blockpos.west());
				IBlockState iblockstate5 = worldIn.getBlockState(blockpos.east());
				enumfacing = EnumFacing.EAST;
				EnumFacing enumfacing1;
				if (block == this) {
					enumfacing1 = (EnumFacing) iblockstate.getValue(FACING);
				} else {
					enumfacing1 = (EnumFacing) iblockstate1.getValue(FACING);
				}

				if (enumfacing1 == EnumFacing.WEST) {
					enumfacing = EnumFacing.WEST;
				}

				Block block4 = iblockstate4.getBlock();
				Block block5 = iblockstate5.getBlock();
				if ((block2.isFullBlock() || block4.isFullBlock()) && !block3.isFullBlock() && !block5.isFullBlock()) {
					enumfacing = EnumFacing.EAST;
				}

				if ((block3.isFullBlock() || block5.isFullBlock()) && !block2.isFullBlock() && !block4.isFullBlock()) {
					enumfacing = EnumFacing.WEST;
				}
			}

			state = state.withProperty(FACING, enumfacing);
			worldIn.setBlockState(pos, state, 3);
		}
		return state;
	}

	public IBlockState correctFacing(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing enumfacing = null;

		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
		for (int i = 0; i < facings.length; ++i) {
			EnumFacing enumfacing1 = facings[i];
			IBlockState iblockstate = worldIn.getBlockState(pos.offset(enumfacing1));
			if (iblockstate.getBlock() == this) {
				return state;
			}

			if (iblockstate.getBlock().isFullBlock()) {
				if (enumfacing != null) {
					enumfacing = null;
					break;
				}

				enumfacing = enumfacing1;
			}
		}

		if (enumfacing != null) {
			return state.withProperty(FACING, enumfacing.getOpposite());
		} else {
			EnumFacing enumfacing2 = (EnumFacing) state.getValue(FACING);
			if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock()) {
				enumfacing2 = enumfacing2.getOpposite();
			}

			if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock()) {
				enumfacing2 = enumfacing2.rotateY();
			}

			if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock()) {
				enumfacing2 = enumfacing2.getOpposite();
			}

			return state.withProperty(FACING, enumfacing2);
		}
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		int i = 0;
		BlockPos blockpos1 = blockpos.west();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.north();
		BlockPos blockpos4 = blockpos.south();
		if (world.getBlockState(blockpos1).getBlock() == this) {
			if (this.isDoubleChest(world, blockpos1)) {
				return false;
			}

			++i;
		}

		if (world.getBlockState(blockpos2).getBlock() == this) {
			if (this.isDoubleChest(world, blockpos2)) {
				return false;
			}

			++i;
		}

		if (world.getBlockState(blockpos3).getBlock() == this) {
			if (this.isDoubleChest(world, blockpos3)) {
				return false;
			}

			++i;
		}

		if (world.getBlockState(blockpos4).getBlock() == this) {
			if (this.isDoubleChest(world, blockpos4)) {
				return false;
			}

			++i;
		}

		return i <= 1;
	}

	private boolean isDoubleChest(World worldIn, BlockPos pos) {
		if (worldIn.getBlockState(pos).getBlock() != this) {
			return false;
		} else {
			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
			for (int i = 0; i < facings.length; ++i) {
				EnumFacing enumfacing = facings[i];
				if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this) {
					return true;
				}
			}

			return false;
		}
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block block) {
		super.onNeighborBlockChange(world, blockpos, iblockstate, block);
		TileEntity tileentity = world.getTileEntity(blockpos);
		if (tileentity instanceof TileEntityChest) {
			tileentity.updateContainingBlockInfo();
		}

	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		TileEntity tileentity = world.getTileEntity(blockpos);
		if (tileentity instanceof IInventory) {
			InventoryHelper.dropInventoryItems(world, blockpos, (IInventory) tileentity);
			world.updateComparatorOutputLevel(blockpos, this);
		}

		super.breakBlock(world, blockpos, iblockstate);
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState var3, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		{
			ILockableContainer ilockablecontainer = this.getLockableContainer(world, blockpos);
			if (ilockablecontainer != null) {
				entityplayer.displayGUIChest(ilockablecontainer);
				if (this.chestType == 0) {
					entityplayer.triggerAchievement(StatList.field_181723_aa);
				} else if (this.chestType == 1) {
					entityplayer.triggerAchievement(StatList.field_181737_U);
				}
			}

			return true;
		}
	}

	public ILockableContainer getLockableContainer(World worldIn, BlockPos pos) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (!(tileentity instanceof TileEntityChest)) {
			return null;
		} else {
			Object object = (TileEntityChest) tileentity;
			if (this.isBlocked(worldIn, pos)) {
				return null;
			} else {
				EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
				for (int i = 0; i < facings.length; ++i) {
					EnumFacing enumfacing = facings[i];
					BlockPos blockpos = pos.offset(enumfacing);
					Block block = worldIn.getBlockState(blockpos).getBlock();
					if (block == this) {
						if (this.isBlocked(worldIn, blockpos)) {
							return null;
						}

						TileEntity tileentity1 = worldIn.getTileEntity(blockpos);
						if (tileentity1 instanceof TileEntityChest) {
							if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH) {
								object = new InventoryLargeChest("container.chestDouble", (ILockableContainer) object,
										(TileEntityChest) tileentity1);
							} else {
								object = new InventoryLargeChest("container.chestDouble", (TileEntityChest) tileentity1,
										(ILockableContainer) object);
							}
						}
					}
				}

				return (ILockableContainer) object;
			}
		}
	}

	/**+
	 * Returns a new instance of a block's tile entity class. Called
	 * on placing the block.
	 */
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityChest();
	}

	/**+
	 * Can this block provide power. Only wire currently seems to
	 * have this change based on its state.
	 */
	public boolean canProvidePower() {
		return this.chestType == 1;
	}

	public int getWeakPower(IBlockAccess iblockaccess, BlockPos blockpos, IBlockState var3, EnumFacing var4) {
		if (!this.canProvidePower()) {
			return 0;
		} else {
			int i = 0;
			TileEntity tileentity = iblockaccess.getTileEntity(blockpos);
			if (tileentity instanceof TileEntityChest) {
				i = ((TileEntityChest) tileentity).numPlayersUsing;
			}

			return MathHelper.clamp_int(i, 0, 15);
		}
	}

	public int getStrongPower(IBlockAccess iblockaccess, BlockPos blockpos, IBlockState iblockstate,
			EnumFacing enumfacing) {
		return enumfacing == EnumFacing.UP ? this.getWeakPower(iblockaccess, blockpos, iblockstate, enumfacing) : 0;
	}

	private boolean isBlocked(World worldIn, BlockPos pos) {
		return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
	}

	private boolean isBelowSolidBlock(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
	}

	private boolean isOcelotSittingOnChest(World worldIn, BlockPos pos) {
		List<Entity> entityList = worldIn.getEntitiesWithinAABB(EntityOcelot.class,
				new AxisAlignedBB((double) pos.getX(), (double) (pos.getY() + 1), (double) pos.getZ(),
						(double) (pos.getX() + 1), (double) (pos.getY() + 2), (double) (pos.getZ() + 1)));
		for (int i = 0, l = entityList.size(); i < l; ++i) {
			Entity entity = entityList.get(i);
			EntityOcelot entityocelot = (EntityOcelot) entity;
			if (entityocelot.isSitting()) {
				return true;
			}
		}

		return false;
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

	public int getComparatorInputOverride(World world, BlockPos blockpos) {
		return Container.calcRedstoneFromInventory(this.getLockableContainer(world, blockpos));
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		EnumFacing enumfacing = EnumFacing.getFront(i);
		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((EnumFacing) iblockstate.getValue(FACING)).getIndex();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING });
	}
}