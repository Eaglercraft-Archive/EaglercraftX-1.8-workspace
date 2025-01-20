package net.minecraft.block;

import java.util.List;

import com.google.common.base.Predicate;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
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
public class BlockRailDetector extends BlockRailBase {
	public static PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE;
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockRailDetector() {
		super(true);
		this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false))
				.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
		this.setTickRandomly(true);
	}

	public static void bootstrapStates() {
		SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class,
				new Predicate<BlockRailBase.EnumRailDirection>() {
					public boolean apply(BlockRailBase.EnumRailDirection blockrailbase$enumraildirection) {
						return blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_EAST
								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_WEST
								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.SOUTH_EAST
								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.SOUTH_WEST;
					}
				});
	}

	/**+
	 * How many world ticks before ticking
	 */
	public int tickRate(World var1) {
		return 20;
	}

	/**+
	 * Can this block provide power. Only wire currently seems to
	 * have this change based on its state.
	 */
	public boolean canProvidePower() {
		return true;
	}

	/**+
	 * Called When an Entity Collided with the Block
	 */
	public void onEntityCollidedWithBlock(World world, BlockPos blockpos, IBlockState iblockstate, Entity var4) {
		if (!world.isRemote) {
			if (!((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
				this.updatePoweredState(world, blockpos, iblockstate);
			}
		}
	}

	/**+
	 * Called randomly when setTickRandomly is set to true (used by
	 * e.g. crops to grow, etc.)
	 */
	public void randomTick(World var1, BlockPos var2, IBlockState var3, EaglercraftRandom var4) {
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {
		if (!world.isRemote && ((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
			this.updatePoweredState(world, blockpos, iblockstate);
		}
	}

	public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState iblockstate, EnumFacing var4) {
		return ((Boolean) iblockstate.getValue(POWERED)).booleanValue() ? 15 : 0;
	}

	public int getStrongPower(IBlockAccess var1, BlockPos var2, IBlockState iblockstate, EnumFacing enumfacing) {
		return !((Boolean) iblockstate.getValue(POWERED)).booleanValue() ? 0 : (enumfacing == EnumFacing.UP ? 15 : 0);
	}

	private void updatePoweredState(World worldIn, BlockPos pos, IBlockState state) {
		boolean flag = ((Boolean) state.getValue(POWERED)).booleanValue();
		boolean flag1 = false;
		List list = this.findMinecarts(worldIn, pos, EntityMinecart.class, new Predicate[0]);
		if (!list.isEmpty()) {
			flag1 = true;
		}

		if (flag1 && !flag) {
			worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 3);
			worldIn.notifyNeighborsOfStateChange(pos, this);
			worldIn.notifyNeighborsOfStateChange(pos.down(), this);
			worldIn.markBlockRangeForRenderUpdate(pos, pos);
		}

		if (!flag1 && flag) {
			worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)), 3);
			worldIn.notifyNeighborsOfStateChange(pos, this);
			worldIn.notifyNeighborsOfStateChange(pos.down(), this);
			worldIn.markBlockRangeForRenderUpdate(pos, pos);
		}

		if (flag1) {
			worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
		}

		worldIn.updateComparatorOutputLevel(pos, this);
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		super.onBlockAdded(world, blockpos, iblockstate);
		this.updatePoweredState(world, blockpos, iblockstate);
	}

	public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
		return SHAPE;
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

	public int getComparatorInputOverride(World world, BlockPos blockpos) {
		if (((Boolean) world.getBlockState(blockpos).getValue(POWERED)).booleanValue()) {
			List list = this.findMinecarts(world, blockpos, EntityMinecartCommandBlock.class, new Predicate[0]);
			if (!list.isEmpty()) {
				return ((EntityMinecartCommandBlock) list.get(0)).getCommandBlockLogic().getSuccessCount();
			}

			List list1 = this.findMinecarts(world, blockpos, EntityMinecart.class,
					new Predicate[] { EntitySelectors.selectInventories });
			if (!list1.isEmpty()) {
				return Container.calcRedstoneFromInventory((IInventory) list1.get(0));
			}
		}

		return 0;
	}

	protected <T extends EntityMinecart> List<T> findMinecarts(World worldIn, BlockPos pos, Class<T> clazz,
			Predicate<Entity>... filter) {
		AxisAlignedBB axisalignedbb = this.getDectectionBox(pos);
		return filter.length != 1 ? worldIn.getEntitiesWithinAABB(clazz, axisalignedbb)
				: worldIn.getEntitiesWithinAABB(clazz, axisalignedbb, filter[0]);
	}

	private AxisAlignedBB getDectectionBox(BlockPos pos) {
		float f = 0.2F;
		return new AxisAlignedBB((double) ((float) pos.getX() + 0.2F), (double) pos.getY(),
				(double) ((float) pos.getZ() + 0.2F), (double) ((float) (pos.getX() + 1) - 0.2F),
				(double) ((float) (pos.getY() + 1) - 0.2F), (double) ((float) (pos.getZ() + 1) - 0.2F));
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(i & 7))
				.withProperty(POWERED, Boolean.valueOf((i & 8) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((BlockRailBase.EnumRailDirection) iblockstate.getValue(SHAPE)).getMetadata();
		if (((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { SHAPE, POWERED });
	}
}