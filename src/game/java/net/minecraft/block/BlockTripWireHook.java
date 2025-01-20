package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.google.common.base.Objects;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
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
public class BlockTripWireHook extends Block {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static final PropertyBool ATTACHED = PropertyBool.create("attached");
	public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");

	public BlockTripWireHook() {
		super(Material.circuits);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(POWERED, Boolean.valueOf(false)).withProperty(ATTACHED, Boolean.valueOf(false))
				.withProperty(SUSPENDED, Boolean.valueOf(false)));
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setTickRandomly(true);
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		return iblockstate.withProperty(SUSPENDED,
				Boolean.valueOf(!World.doesBlockHaveSolidTopSurface(iblockaccess, blockpos.down())));
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
		return enumfacing.getAxis().isHorizontal()
				&& world.getBlockState(blockpos.offset(enumfacing.getOpposite())).getBlock().isNormalCube();
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
		BlockPos tmp = new BlockPos(0, 0, 0);
		for (int i = 0; i < facings.length; ++i) {
			if (world.getBlockState(blockpos.offsetEvenFaster(facings[i], tmp)).getBlock().isNormalCube()) {
				return true;
			}
		}

		return false;
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing enumfacing, float var4, float var5,
			float var6, int var7, EntityLivingBase var8) {
		IBlockState iblockstate = this.getDefaultState().withProperty(POWERED, Boolean.valueOf(false))
				.withProperty(ATTACHED, Boolean.valueOf(false)).withProperty(SUSPENDED, Boolean.valueOf(false));
		if (enumfacing.getAxis().isHorizontal()) {
			iblockstate = iblockstate.withProperty(FACING, enumfacing);
		}

		return iblockstate;
	}

	/**+
	 * Called by ItemBlocks after a block is set in the world, to
	 * allow post-place logic
	 */
	public void onBlockPlacedBy(World world, BlockPos blockpos, IBlockState iblockstate, EntityLivingBase var4,
			ItemStack var5) {
		this.func_176260_a(world, blockpos, iblockstate, false, false, -1, (IBlockState) null);
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block block) {
		if (block != this) {
			if (this.checkForDrop(world, blockpos, iblockstate)) {
				EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
				if (!world.getBlockState(blockpos.offset(enumfacing.getOpposite())).getBlock().isNormalCube()) {
					this.dropBlockAsItem(world, blockpos, iblockstate, 0);
					world.setBlockToAir(blockpos);
				}
			}

		}
	}

	public void func_176260_a(World worldIn, BlockPos pos, IBlockState hookState, boolean parFlag, boolean parFlag2,
			int parInt1, IBlockState parIBlockState2) {
		EnumFacing enumfacing = (EnumFacing) hookState.getValue(FACING);
		boolean flag = ((Boolean) hookState.getValue(ATTACHED)).booleanValue();
		boolean flag1 = ((Boolean) hookState.getValue(POWERED)).booleanValue();
		boolean flag2 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
		boolean flag3 = !parFlag;
		boolean flag4 = false;
		int i = 0;
		IBlockState[] aiblockstate = new IBlockState[42];

		for (int j = 1; j < 42; ++j) {
			BlockPos blockpos = pos.offset(enumfacing, j);
			IBlockState iblockstate = worldIn.getBlockState(blockpos);
			if (iblockstate.getBlock() == Blocks.tripwire_hook) {
				if (iblockstate.getValue(FACING) == enumfacing.getOpposite()) {
					i = j;
				}
				break;
			}

			if (iblockstate.getBlock() != Blocks.tripwire && j != parInt1) {
				aiblockstate[j] = null;
				flag3 = false;
			} else {
				if (j == parInt1) {
					iblockstate = (IBlockState) Objects.firstNonNull(parIBlockState2, iblockstate);
				}

				boolean flag5 = !((Boolean) iblockstate.getValue(BlockTripWire.DISARMED)).booleanValue();
				boolean flag6 = ((Boolean) iblockstate.getValue(BlockTripWire.POWERED)).booleanValue();
				boolean flag7 = ((Boolean) iblockstate.getValue(BlockTripWire.SUSPENDED)).booleanValue();
				flag3 &= flag7 == flag2;
				flag4 |= flag5 && flag6;
				aiblockstate[j] = iblockstate;
				if (j == parInt1) {
					worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
					flag3 &= flag5;
				}
			}
		}

		flag3 = flag3 & i > 1;
		flag4 = flag4 & flag3;
		IBlockState iblockstate1 = this.getDefaultState().withProperty(ATTACHED, Boolean.valueOf(flag3))
				.withProperty(POWERED, Boolean.valueOf(flag4));
		if (i > 0) {
			BlockPos blockpos1 = pos.offset(enumfacing, i);
			EnumFacing enumfacing1 = enumfacing.getOpposite();
			worldIn.setBlockState(blockpos1, iblockstate1.withProperty(FACING, enumfacing1), 3);
			this.func_176262_b(worldIn, blockpos1, enumfacing1);
			this.func_180694_a(worldIn, blockpos1, flag3, flag4, flag, flag1);
		}

		this.func_180694_a(worldIn, pos, flag3, flag4, flag, flag1);
		if (!parFlag) {
			worldIn.setBlockState(pos, iblockstate1.withProperty(FACING, enumfacing), 3);
			if (parFlag2) {
				this.func_176262_b(worldIn, pos, enumfacing);
			}
		}

		if (flag != flag3) {
			for (int k = 1; k < i; ++k) {
				BlockPos blockpos2 = pos.offset(enumfacing, k);
				IBlockState iblockstate2 = aiblockstate[k];
				if (iblockstate2 != null && worldIn.getBlockState(blockpos2).getBlock() != Blocks.air) {
					worldIn.setBlockState(blockpos2, iblockstate2.withProperty(ATTACHED, Boolean.valueOf(flag3)), 3);
				}
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
		this.func_176260_a(world, blockpos, iblockstate, false, true, -1, (IBlockState) null);
	}

	private void func_180694_a(World worldIn, BlockPos pos, boolean parFlag, boolean parFlag2, boolean parFlag3,
			boolean parFlag4) {
		if (parFlag2 && !parFlag4) {
			worldIn.playSoundEffect((double) pos.getX() + 0.5D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D,
					"random.click", 0.4F, 0.6F);
		} else if (!parFlag2 && parFlag4) {
			worldIn.playSoundEffect((double) pos.getX() + 0.5D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D,
					"random.click", 0.4F, 0.5F);
		} else if (parFlag && !parFlag3) {
			worldIn.playSoundEffect((double) pos.getX() + 0.5D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D,
					"random.click", 0.4F, 0.7F);
		} else if (!parFlag && parFlag3) {
			worldIn.playSoundEffect((double) pos.getX() + 0.5D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D,
					"random.bowhit", 0.4F, 1.2F / (worldIn.rand.nextFloat() * 0.2F + 0.9F));
		}

	}

	private void func_176262_b(World worldIn, BlockPos parBlockPos, EnumFacing parEnumFacing) {
		worldIn.notifyNeighborsOfStateChange(parBlockPos, this);
		worldIn.notifyNeighborsOfStateChange(parBlockPos.offset(parEnumFacing.getOpposite()), this);
	}

	private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canPlaceBlockAt(worldIn, pos)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
			return false;
		} else {
			return true;
		}
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		float f = 0.1875F;
		switch ((EnumFacing) iblockaccess.getBlockState(blockpos).getValue(FACING)) {
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
		}

	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		boolean flag = ((Boolean) iblockstate.getValue(ATTACHED)).booleanValue();
		boolean flag1 = ((Boolean) iblockstate.getValue(POWERED)).booleanValue();
		if (flag || flag1) {
			this.func_176260_a(world, blockpos, iblockstate, true, false, -1, (IBlockState) null);
		}

		if (flag1) {
			world.notifyNeighborsOfStateChange(blockpos, this);
			world.notifyNeighborsOfStateChange(
					blockpos.offset(((EnumFacing) iblockstate.getValue(FACING)).getOpposite()), this);
		}

		super.breakBlock(world, blockpos, iblockstate);
	}

	public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState iblockstate, EnumFacing var4) {
		return ((Boolean) iblockstate.getValue(POWERED)).booleanValue() ? 15 : 0;
	}

	public int getStrongPower(IBlockAccess var1, BlockPos var2, IBlockState iblockstate, EnumFacing enumfacing) {
		return !((Boolean) iblockstate.getValue(POWERED)).booleanValue() ? 0
				: (iblockstate.getValue(FACING) == enumfacing ? 15 : 0);
	}

	/**+
	 * Can this block provide power. Only wire currently seems to
	 * have this change based on its state.
	 */
	public boolean canProvidePower() {
		return true;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(i & 3))
				.withProperty(POWERED, Boolean.valueOf((i & 8) > 0))
				.withProperty(ATTACHED, Boolean.valueOf((i & 4) > 0));
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

		if (((Boolean) iblockstate.getValue(ATTACHED)).booleanValue()) {
			i |= 4;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, POWERED, ATTACHED, SUSPENDED });
	}
}