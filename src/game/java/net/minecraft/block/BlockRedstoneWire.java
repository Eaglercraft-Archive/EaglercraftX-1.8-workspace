package net.minecraft.block;

import java.util.ArrayList;
import java.util.EnumSet;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
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
public class BlockRedstoneWire extends Block {
	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> NORTH;
	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> EAST;
	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> SOUTH;
	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> WEST;
	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	private boolean canProvidePower = true;
	/**+
	 * List of blocks to update with redstone.
	 */
	private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();

	public BlockRedstoneWire() {
		super(Material.circuits);
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(NORTH, BlockRedstoneWire.EnumAttachPosition.NONE)
				.withProperty(EAST, BlockRedstoneWire.EnumAttachPosition.NONE)
				.withProperty(SOUTH, BlockRedstoneWire.EnumAttachPosition.NONE)
				.withProperty(WEST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(POWER, Integer.valueOf(0)));
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}

	public static void bootstrapStates() {
		NORTH = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("north",
				BlockRedstoneWire.EnumAttachPosition.class);
		EAST = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("east",
				BlockRedstoneWire.EnumAttachPosition.class);
		SOUTH = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("south",
				BlockRedstoneWire.EnumAttachPosition.class);
		WEST = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("west",
				BlockRedstoneWire.EnumAttachPosition.class);
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		iblockstate = iblockstate.withProperty(WEST, this.getAttachPosition(iblockaccess, blockpos, EnumFacing.WEST));
		iblockstate = iblockstate.withProperty(EAST, this.getAttachPosition(iblockaccess, blockpos, EnumFacing.EAST));
		iblockstate = iblockstate.withProperty(NORTH, this.getAttachPosition(iblockaccess, blockpos, EnumFacing.NORTH));
		iblockstate = iblockstate.withProperty(SOUTH, this.getAttachPosition(iblockaccess, blockpos, EnumFacing.SOUTH));
		return iblockstate;
	}

	private BlockRedstoneWire.EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos,
			EnumFacing direction) {
		BlockPos blockpos = pos.offset(direction);
		BlockPos posTmp = new BlockPos(0, 0, 0);
		Block block = worldIn.getBlockState(blockpos).getBlock();
		if (!canConnectTo(worldIn.getBlockState(blockpos), direction) && (block.isBlockNormalCube()
				|| !canConnectUpwardsTo(worldIn.getBlockState(blockpos.offsetEvenFaster(EnumFacing.DOWN, posTmp))))) {
			Block block1 = worldIn.getBlockState(pos.up()).getBlock();
			return !block1.isBlockNormalCube() && block.isBlockNormalCube()
					&& canConnectUpwardsTo(worldIn.getBlockState(blockpos.offsetEvenFaster(EnumFacing.UP, posTmp)))
							? BlockRedstoneWire.EnumAttachPosition.UP
							: BlockRedstoneWire.EnumAttachPosition.NONE;
		} else {
			return BlockRedstoneWire.EnumAttachPosition.SIDE;
		}
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

	public int colorMultiplier(IBlockAccess iblockaccess, BlockPos blockpos, int i) {
		IBlockState iblockstate = iblockaccess.getBlockState(blockpos);
		return iblockstate.getBlock() != this ? super.colorMultiplier(iblockaccess, blockpos, i)
				: this.colorMultiplier(((Integer) iblockstate.getValue(POWER)).intValue());
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		BlockPos fuckOff = blockpos.down();
		return World.doesBlockHaveSolidTopSurface(world, fuckOff)
				|| world.getBlockState(fuckOff).getBlock() == Blocks.glowstone;
	}

	private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state) {
		state = this.calculateCurrentChanges(worldIn, pos, pos, state);
		ArrayList<BlockPos> arraylist = Lists.newArrayList(this.blocksNeedingUpdate);
		this.blocksNeedingUpdate.clear();

		for (int i = 0, l = arraylist.size(); i < l; ++i) {
			worldIn.notifyNeighborsOfStateChange(arraylist.get(i), this);
		}

		return state;
	}

	private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state) {
		IBlockState iblockstate = state;
		int i = ((Integer) state.getValue(POWER)).intValue();
		int j = 0;
		j = this.getMaxCurrentStrength(worldIn, pos2, j);
		this.canProvidePower = false;
		int k = worldIn.isBlockIndirectlyGettingPowered(pos1);
		this.canProvidePower = true;
		if (k > 0 && k > j - 1) {
			j = k;
		}

		int l = 0;

		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
		BlockPos tmp = new BlockPos(0, 0, 0);
		for (int m = 0; m < facings.length; ++m) {
			EnumFacing enumfacing = facings[m];
			BlockPos blockpos = pos1.offsetEvenFaster(enumfacing, tmp);
			boolean flag = blockpos.x != pos2.x || blockpos.z != pos2.z;
			if (flag) {
				l = this.getMaxCurrentStrength(worldIn, blockpos, l);
			}

			if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()
					&& !worldIn.getBlockState(pos1.up()).getBlock().isNormalCube()) {
				if (flag && pos1.getY() >= pos2.getY()) {
					++blockpos.y;
					l = this.getMaxCurrentStrength(worldIn, blockpos, l);
				}
			} else if (!worldIn.getBlockState(blockpos).getBlock().isNormalCube() && flag
					&& pos1.getY() <= pos2.getY()) {
				--blockpos.y;
				l = this.getMaxCurrentStrength(worldIn, blockpos, l);
			}
		}

		if (l > j) {
			j = l - 1;
		} else if (j > 0) {
			--j;
		} else {
			j = 0;
		}

		if (k > j - 1) {
			j = k;
		}

		if (i != j) {
			state = state.withProperty(POWER, Integer.valueOf(j));
			if (worldIn.getBlockState(pos1) == iblockstate) {
				worldIn.setBlockState(pos1, state, 2);
			}

			this.blocksNeedingUpdate.add(pos1);

			facings = EnumFacing._VALUES;
			for (int m = 0; m < facings.length; ++m) {
				this.blocksNeedingUpdate.add(pos1.offset(facings[m]));
			}
		}

		return state;
	}

	/**+
	 * Calls World.notifyNeighborsOfStateChange() for all
	 * neighboring blocks, but only if the given block is a redstone
	 * wire.
	 */
	private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos) {
		if (worldIn.getBlockState(pos).getBlock() == this) {
			worldIn.notifyNeighborsOfStateChange(pos, this);

			EnumFacing[] facings = EnumFacing._VALUES;
			BlockPos tmp = new BlockPos(0, 0, 0);
			for (int i = 0; i < facings.length; ++i) {
				worldIn.notifyNeighborsOfStateChange(pos.offsetEvenFaster(facings[i], tmp), this);
			}

		}
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		if (!world.isRemote) {
			this.updateSurroundingRedstone(world, blockpos, iblockstate);

			BlockPos tmp = new BlockPos(0, 0, 0);
			EnumFacing[] facings = EnumFacing.Plane.VERTICAL.facingsArray;
			for (int i = 0; i < facings.length; ++i) {
				world.notifyNeighborsOfStateChange(blockpos.offsetEvenFaster(facings[i], tmp), this);
			}

			facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
			for (int i = 0; i < facings.length; ++i) {
				this.notifyWireNeighborsOfStateChange(world, blockpos.offsetEvenFaster(facings[i], tmp));
			}

			for (int i = 0; i < facings.length; ++i) {
				BlockPos blockpos1 = blockpos.offsetEvenFaster(facings[i], tmp);
				if (world.getBlockState(blockpos1).getBlock().isNormalCube()) {
					++blockpos1.y;
					this.notifyWireNeighborsOfStateChange(world, blockpos1);
				} else {
					--blockpos1.y;
					this.notifyWireNeighborsOfStateChange(world, blockpos1);
				}
			}
		}
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		super.breakBlock(world, blockpos, iblockstate);
		if (!world.isRemote) {
			BlockPos tmp = new BlockPos(0, 0, 0);
			EnumFacing[] facings = EnumFacing._VALUES;
			for (int i = 0; i < facings.length; ++i) {
				world.notifyNeighborsOfStateChange(blockpos.offsetEvenFaster(facings[i], tmp), this);
			}

			this.updateSurroundingRedstone(world, blockpos, iblockstate);

			facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
			for (int i = 0; i < facings.length; ++i) {
				this.notifyWireNeighborsOfStateChange(world, blockpos.offsetEvenFaster(facings[i], tmp));
			}

			facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
			for (int i = 0; i < facings.length; ++i) {
				BlockPos blockpos1 = blockpos.offsetEvenFaster(facings[i], tmp);
				if (world.getBlockState(blockpos1).getBlock().isNormalCube()) {
					++blockpos1.y;
					this.notifyWireNeighborsOfStateChange(world, blockpos1);
				} else {
					--blockpos1.y;
					this.notifyWireNeighborsOfStateChange(world, blockpos1);
				}
			}
		}
	}

	private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength) {
		if (worldIn.getBlockState(pos).getBlock() != this) {
			return strength;
		} else {
			int i = ((Integer) worldIn.getBlockState(pos).getValue(POWER)).intValue();
			return i > strength ? i : strength;
		}
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		if (!world.isRemote) {
			if (this.canPlaceBlockAt(world, blockpos)) {
				this.updateSurroundingRedstone(world, blockpos, iblockstate);
			} else {
				this.dropBlockAsItem(world, blockpos, iblockstate, 0);
				world.setBlockToAir(blockpos);
			}
		}
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Items.redstone;
	}

	public int getStrongPower(IBlockAccess iblockaccess, BlockPos blockpos, IBlockState iblockstate,
			EnumFacing enumfacing) {
		return !this.canProvidePower ? 0 : this.getWeakPower(iblockaccess, blockpos, iblockstate, enumfacing);
	}

	public int getWeakPower(IBlockAccess iblockaccess, BlockPos blockpos, IBlockState iblockstate,
			EnumFacing enumfacing) {
		if (!this.canProvidePower) {
			return 0;
		} else {
			int i = ((Integer) iblockstate.getValue(POWER)).intValue();
			if (i == 0) {
				return 0;
			} else if (enumfacing == EnumFacing.UP) {
				return i;
			} else {
				EnumSet enumset = EnumSet.noneOf(EnumFacing.class);

				EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
				for (int j = 0; j < facings.length; ++j) {
					EnumFacing enumfacing1 = facings[j];
					if (this.func_176339_d(iblockaccess, blockpos, enumfacing1)) {
						enumset.add(enumfacing1);
					}
				}

				if (enumfacing.getAxis().isHorizontal() && enumset.isEmpty()) {
					return i;
				} else if (enumset.contains(enumfacing) && !enumset.contains(enumfacing.rotateYCCW())
						&& !enumset.contains(enumfacing.rotateY())) {
					return i;
				} else {
					return 0;
				}
			}
		}
	}

	private boolean func_176339_d(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		BlockPos blockpos = pos.offset(side);
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		Block block = iblockstate.getBlock();
		boolean flag = block.isNormalCube();
		boolean flag1 = worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
		return !flag1 && flag && canConnectUpwardsTo(worldIn, blockpos.up()) ? true
				: (canConnectTo(iblockstate, side) ? true
						: (block == Blocks.powered_repeater && iblockstate.getValue(BlockRedstoneDiode.FACING) == side
								? true
								: !flag && canConnectUpwardsTo(worldIn, blockpos.down())));
	}

	protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos) {
		return canConnectUpwardsTo(worldIn.getBlockState(pos));
	}

	protected static boolean canConnectUpwardsTo(IBlockState state) {
		return canConnectTo(state, (EnumFacing) null);
	}

	protected static boolean canConnectTo(IBlockState blockState, EnumFacing side) {
		Block block = blockState.getBlock();
		if (block == Blocks.redstone_wire) {
			return true;
		} else if (Blocks.unpowered_repeater.isAssociated(block)) {
			EnumFacing enumfacing = (EnumFacing) blockState.getValue(BlockRedstoneRepeater.FACING);
			return enumfacing == side || enumfacing.getOpposite() == side;
		} else {
			return block.canProvidePower() && side != null;
		}
	}

	/**+
	 * Can this block provide power. Only wire currently seems to
	 * have this change based on its state.
	 */
	public boolean canProvidePower() {
		return this.canProvidePower;
	}

	private int colorMultiplier(int powerLevel) {
		float f = (float) powerLevel / 15.0F;
		float f1 = f * 0.6F + 0.4F;
		if (powerLevel == 0) {
			f1 = 0.3F;
		}

		float f2 = f * f * 0.7F - 0.5F;
		float f3 = f * f * 0.6F - 0.7F;
		if (f2 < 0.0F) {
			f2 = 0.0F;
		}

		if (f3 < 0.0F) {
			f3 = 0.0F;
		}

		int i = MathHelper.clamp_int((int) (f1 * 255.0F), 0, 255);
		int j = MathHelper.clamp_int((int) (f2 * 255.0F), 0, 255);
		int k = MathHelper.clamp_int((int) (f3 * 255.0F), 0, 255);
		return -16777216 | i << 16 | j << 8 | k;
	}

	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		int i = ((Integer) iblockstate.getValue(POWER)).intValue();
		if (i != 0) {
			double d0 = (double) blockpos.getX() + 0.5D + ((double) random.nextFloat() - 0.5D) * 0.2D;
			double d1 = (double) ((float) blockpos.getY() + 0.0625F);
			double d2 = (double) blockpos.getZ() + 0.5D + ((double) random.nextFloat() - 0.5D) * 0.2D;
			float f = (float) i / 15.0F;
			float f1 = f * 0.6F + 0.4F;
			float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
			float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
			world.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, (double) f1, (double) f2, (double) f3,
					new int[0]);
		}
	}

	public Item getItem(World var1, BlockPos var2) {
		return Items.redstone;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(POWER, Integer.valueOf(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(POWER)).intValue();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { NORTH, EAST, SOUTH, WEST, POWER });
	}

	static enum EnumAttachPosition implements IStringSerializable {
		UP("up"), SIDE("side"), NONE("none");

		private final String name;

		private EnumAttachPosition(String name) {
			this.name = name;
		}

		public String toString() {
			return this.getName();
		}

		public String getName() {
			return this.name;
		}
	}
}