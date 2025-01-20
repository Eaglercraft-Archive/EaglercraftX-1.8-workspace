package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.RegistryDefaulted;
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
public class BlockDispenser extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
	public static final RegistryDefaulted<Item, IBehaviorDispenseItem> dispenseBehaviorRegistry = new RegistryDefaulted(
			new BehaviorDefaultDispenseItem());
	protected EaglercraftRandom rand = new EaglercraftRandom();

	protected BlockDispenser() {
		super(Material.rock);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(TRIGGERED, Boolean.valueOf(false)));
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	/**+
	 * How many world ticks before ticking
	 */
	public int tickRate(World var1) {
		return 4;
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		super.onBlockAdded(world, blockpos, iblockstate);
		this.setDefaultDirection(world, blockpos, iblockstate);
	}

	private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
			boolean flag = worldIn.getBlockState(pos.north()).getBlock().isFullBlock();
			boolean flag1 = worldIn.getBlockState(pos.south()).getBlock().isFullBlock();
			if (enumfacing == EnumFacing.NORTH && flag && !flag1) {
				enumfacing = EnumFacing.SOUTH;
			} else if (enumfacing == EnumFacing.SOUTH && flag1 && !flag) {
				enumfacing = EnumFacing.NORTH;
			} else {
				boolean flag2 = worldIn.getBlockState(pos.west()).getBlock().isFullBlock();
				boolean flag3 = worldIn.getBlockState(pos.east()).getBlock().isFullBlock();
				if (enumfacing == EnumFacing.WEST && flag2 && !flag3) {
					enumfacing = EnumFacing.EAST;
				} else if (enumfacing == EnumFacing.EAST && flag3 && !flag2) {
					enumfacing = EnumFacing.WEST;
				}
			}

			worldIn.setBlockState(pos,
					state.withProperty(FACING, enumfacing).withProperty(TRIGGERED, Boolean.valueOf(false)), 2);
		}
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState var3, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		if (!world.isRemote) {
			TileEntity tileentity = world.getTileEntity(blockpos);
			if (tileentity instanceof TileEntityDispenser) {
				entityplayer.displayGUIChest((TileEntityDispenser) tileentity);
				if (tileentity instanceof TileEntityDropper) {
					entityplayer.triggerAchievement(StatList.field_181731_O);
				} else {
					entityplayer.triggerAchievement(StatList.field_181733_Q);
				}
			}
		}
		return true;
	}

	protected void dispense(World worldIn, BlockPos pos) {
		BlockSourceImpl blocksourceimpl = new BlockSourceImpl(worldIn, pos);
		TileEntityDispenser tileentitydispenser = (TileEntityDispenser) blocksourceimpl.getBlockTileEntity();
		if (tileentitydispenser != null) {
			int i = tileentitydispenser.getDispenseSlot();
			if (i < 0) {
				worldIn.playAuxSFX(1001, pos, 0);
			} else {
				ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
				IBehaviorDispenseItem ibehaviordispenseitem = this.getBehavior(itemstack);
				if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider) {
					ItemStack itemstack1 = ibehaviordispenseitem.dispense(blocksourceimpl, itemstack);
					tileentitydispenser.setInventorySlotContents(i, itemstack1.stackSize <= 0 ? null : itemstack1);
				}

			}
		}
	}

	protected IBehaviorDispenseItem getBehavior(ItemStack stack) {
		return (IBehaviorDispenseItem) dispenseBehaviorRegistry.getObject(stack == null ? null : stack.getItem());
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		boolean flag = world.isBlockPowered(blockpos) || world.isBlockPowered(blockpos.up());
		boolean flag1 = ((Boolean) iblockstate.getValue(TRIGGERED)).booleanValue();
		if (flag && !flag1) {
			world.scheduleUpdate(blockpos, this, this.tickRate(world));
			world.setBlockState(blockpos, iblockstate.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
		} else if (!flag && flag1) {
			world.setBlockState(blockpos, iblockstate.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);
		}

	}

	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {
		if (!world.isRemote) {
			this.dispense(world, blockpos);
		}
	}

	/**+
	 * Returns a new instance of a block's tile entity class. Called
	 * on placing the block.
	 */
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityDispenser();
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World world, BlockPos blockpos, EnumFacing var3, float var4, float var5,
			float var6, int var7, EntityLivingBase entitylivingbase) {
		return this.getDefaultState()
				.withProperty(FACING, BlockPistonBase.getFacingFromEntity(world, blockpos, entitylivingbase))
				.withProperty(TRIGGERED, Boolean.valueOf(false));
	}

	/**+
	 * Called by ItemBlocks after a block is set in the world, to
	 * allow post-place logic
	 */
	public void onBlockPlacedBy(World world, BlockPos blockpos, IBlockState iblockstate,
			EntityLivingBase entitylivingbase, ItemStack itemstack) {
		world.setBlockState(blockpos, iblockstate.withProperty(FACING,
				BlockPistonBase.getFacingFromEntity(world, blockpos, entitylivingbase)), 2);
		if (itemstack.hasDisplayName()) {
			TileEntity tileentity = world.getTileEntity(blockpos);
			if (tileentity instanceof TileEntityDispenser) {
				((TileEntityDispenser) tileentity).setCustomName(itemstack.getDisplayName());
			}
		}

	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		TileEntity tileentity = world.getTileEntity(blockpos);
		if (tileentity instanceof TileEntityDispenser) {
			InventoryHelper.dropInventoryItems(world, blockpos, (TileEntityDispenser) tileentity);
			world.updateComparatorOutputLevel(blockpos, this);
		}

		super.breakBlock(world, blockpos, iblockstate);
	}

	/**+
	 * Get the position where the dispenser at the given Coordinates
	 * should dispense to.
	 */
	public static IPosition getDispensePosition(IBlockSource coords) {
		EnumFacing enumfacing = getFacing(coords.getBlockMetadata());
		double d0 = coords.getX() + 0.7D * (double) enumfacing.getFrontOffsetX();
		double d1 = coords.getY() + 0.7D * (double) enumfacing.getFrontOffsetY();
		double d2 = coords.getZ() + 0.7D * (double) enumfacing.getFrontOffsetZ();
		return new PositionImpl(d0, d1, d2);
	}

	/**+
	 * Get the facing of a dispenser with the given metadata
	 */
	public static EnumFacing getFacing(int meta) {
		return EnumFacing.getFront(meta & 7);
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

	public int getComparatorInputOverride(World world, BlockPos blockpos) {
		return Container.calcRedstone(world.getTileEntity(blockpos));
	}

	/**+
	 * The type of render function called. 3 for standard block
	 * models, 2 for TESR's, 1 for liquids, -1 is no render
	 */
	public int getRenderType() {
		return 3;
	}

	/**+
	 * Possibly modify the given BlockState before rendering it on
	 * an Entity (Minecarts, Endermen, ...)
	 */
	public IBlockState getStateForEntityRender(IBlockState var1) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, getFacing(i)).withProperty(TRIGGERED,
				Boolean.valueOf((i & 8) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((EnumFacing) iblockstate.getValue(FACING)).getIndex();
		if (((Boolean) iblockstate.getValue(TRIGGERED)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, TRIGGERED });
	}
}