package net.minecraft.block;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.google.common.base.Predicate;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
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
public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider {
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static PropertyEnum<BlockRedstoneComparator.Mode> MODE;

	public BlockRedstoneComparator(boolean powered) {
		super(powered);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(POWERED, Boolean.valueOf(false))
				.withProperty(MODE, BlockRedstoneComparator.Mode.COMPARE));
		this.isBlockContainer = true;
	}

	public static void bootstrapStates() {
		MODE = PropertyEnum.<BlockRedstoneComparator.Mode>create("mode", BlockRedstoneComparator.Mode.class);
	}

	/**+
	 * Gets the localized name of this block. Used for the
	 * statistics page.
	 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal("item.comparator.name");
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Items.comparator;
	}

	public Item getItem(World var1, BlockPos var2) {
		return Items.comparator;
	}

	protected int getDelay(IBlockState state) {
		return 2;
	}

	protected IBlockState getPoweredState(IBlockState unpoweredState) {
		Boolean obool = (Boolean) unpoweredState.getValue(POWERED);
		BlockRedstoneComparator.Mode blockredstonecomparator$mode = (BlockRedstoneComparator.Mode) unpoweredState
				.getValue(MODE);
		EnumFacing enumfacing = (EnumFacing) unpoweredState.getValue(FACING);
		return Blocks.powered_comparator.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, obool)
				.withProperty(MODE, blockredstonecomparator$mode);
	}

	protected IBlockState getUnpoweredState(IBlockState poweredState) {
		Boolean obool = (Boolean) poweredState.getValue(POWERED);
		BlockRedstoneComparator.Mode blockredstonecomparator$mode = (BlockRedstoneComparator.Mode) poweredState
				.getValue(MODE);
		EnumFacing enumfacing = (EnumFacing) poweredState.getValue(FACING);
		return Blocks.unpowered_comparator.getDefaultState().withProperty(FACING, enumfacing)
				.withProperty(POWERED, obool).withProperty(MODE, blockredstonecomparator$mode);
	}

	protected boolean isPowered(IBlockState state) {
		return this.isRepeaterPowered || ((Boolean) state.getValue(POWERED)).booleanValue();
	}

	protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity instanceof TileEntityComparator ? ((TileEntityComparator) tileentity).getOutputSignal() : 0;
	}

	private int calculateOutput(World worldIn, BlockPos pos, IBlockState state) {
		return state.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT
				? Math.max(this.calculateInputStrength(worldIn, pos, state) - this.getPowerOnSides(worldIn, pos, state),
						0)
				: this.calculateInputStrength(worldIn, pos, state);
	}

	protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state) {
		int i = this.calculateInputStrength(worldIn, pos, state);
		if (i >= 15) {
			return true;
		} else if (i == 0) {
			return false;
		} else {
			int j = this.getPowerOnSides(worldIn, pos, state);
			return j == 0 ? true : i >= j;
		}
	}

	protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
		int i = super.calculateInputStrength(worldIn, pos, state);
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
		BlockPos blockpos = pos.offset(enumfacing);
		Block block = worldIn.getBlockState(blockpos).getBlock();
		if (block.hasComparatorInputOverride()) {
			i = block.getComparatorInputOverride(worldIn, blockpos);
		} else if (i < 15 && block.isNormalCube()) {
			blockpos = blockpos.offset(enumfacing);
			block = worldIn.getBlockState(blockpos).getBlock();
			if (block.hasComparatorInputOverride()) {
				i = block.getComparatorInputOverride(worldIn, blockpos);
			} else if (block.getMaterial() == Material.air) {
				EntityItemFrame entityitemframe = this.findItemFrame(worldIn, enumfacing, blockpos);
				if (entityitemframe != null) {
					i = entityitemframe.func_174866_q();
				}
			}
		}

		return i;
	}

	private EntityItemFrame findItemFrame(World worldIn, final EnumFacing facing, BlockPos pos) {
		List list = worldIn.getEntitiesWithinAABB(EntityItemFrame.class,
				new AxisAlignedBB((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
						(double) (pos.getX() + 1), (double) (pos.getY() + 1), (double) (pos.getZ() + 1)),
				new Predicate<Entity>() {
					public boolean apply(Entity entity) {
						return entity != null && entity.getHorizontalFacing() == facing;
					}
				});
		return list.size() == 1 ? (EntityItemFrame) list.get(0) : null;
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		if (!entityplayer.capabilities.allowEdit) {
			return false;
		} else {
			iblockstate = iblockstate.cycleProperty(MODE);
			world.playSoundEffect((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D,
					(double) blockpos.getZ() + 0.5D, "random.click", 0.3F,
					iblockstate.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT ? 0.55F : 0.5F);
			world.setBlockState(blockpos, iblockstate, 2);
			this.onStateChange(world, blockpos, iblockstate);
			return true;
		}
	}

	protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isBlockTickPending(pos, this)) {
			int i = this.calculateOutput(worldIn, pos, state);
			TileEntity tileentity = worldIn.getTileEntity(pos);
			int j = tileentity instanceof TileEntityComparator ? ((TileEntityComparator) tileentity).getOutputSignal()
					: 0;
			if (i != j || this.isPowered(state) != this.shouldBePowered(worldIn, pos, state)) {
				if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
					worldIn.updateBlockTick(pos, this, 2, -1);
				} else {
					worldIn.updateBlockTick(pos, this, 2, 0);
				}
			}

		}
	}

	private void onStateChange(World worldIn, BlockPos pos, IBlockState state) {
		int i = this.calculateOutput(worldIn, pos, state);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		int j = 0;
		if (tileentity instanceof TileEntityComparator) {
			TileEntityComparator tileentitycomparator = (TileEntityComparator) tileentity;
			j = tileentitycomparator.getOutputSignal();
			tileentitycomparator.setOutputSignal(i);
		}

		if (j != i || state.getValue(MODE) == BlockRedstoneComparator.Mode.COMPARE) {
			boolean flag1 = this.shouldBePowered(worldIn, pos, state);
			boolean flag = this.isPowered(state);
			if (flag && !flag1) {
				worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)), 2);
			} else if (!flag && flag1) {
				worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 2);
			}

			this.notifyNeighbors(worldIn, pos, state);
		}

	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {
		if (this.isRepeaterPowered) {
			world.setBlockState(blockpos,
					this.getUnpoweredState(iblockstate).withProperty(POWERED, Boolean.valueOf(true)), 4);
		}

		this.onStateChange(world, blockpos, iblockstate);
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		super.onBlockAdded(world, blockpos, iblockstate);
		world.setTileEntity(blockpos, this.createNewTileEntity(world, 0));
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		super.breakBlock(world, blockpos, iblockstate);
		world.removeTileEntity(blockpos);
		this.notifyNeighbors(world, blockpos, iblockstate);
	}

	/**+
	 * Called on both Client and Server when World#addBlockEvent is
	 * called
	 */
	public boolean onBlockEventReceived(World world, BlockPos blockpos, IBlockState iblockstate, int i, int j) {
		super.onBlockEventReceived(world, blockpos, iblockstate, i, j);
		TileEntity tileentity = world.getTileEntity(blockpos);
		return tileentity == null ? false : tileentity.receiveClientEvent(i, j);
	}

	/**+
	 * Returns a new instance of a block's tile entity class. Called
	 * on placing the block.
	 */
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityComparator();
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(i))
				.withProperty(POWERED, Boolean.valueOf((i & 8) > 0)).withProperty(MODE,
						(i & 4) > 0 ? BlockRedstoneComparator.Mode.SUBTRACT : BlockRedstoneComparator.Mode.COMPARE);
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

		if (iblockstate.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT) {
			i |= 4;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, MODE, POWERED });
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6,
			int var7, EntityLivingBase entitylivingbase) {
		return this.getDefaultState().withProperty(FACING, entitylivingbase.getHorizontalFacing().getOpposite())
				.withProperty(POWERED, Boolean.valueOf(false)).withProperty(MODE, BlockRedstoneComparator.Mode.COMPARE);
	}

	public static enum Mode implements IStringSerializable {
		COMPARE("compare"), SUBTRACT("subtract");

		private final String name;

		private Mode(String name) {
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