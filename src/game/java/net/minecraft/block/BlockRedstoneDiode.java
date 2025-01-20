package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
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
public abstract class BlockRedstoneDiode extends BlockDirectional {
	protected final boolean isRepeaterPowered;

	protected BlockRedstoneDiode(boolean powered) {
		super(Material.circuits);
		this.isRepeaterPowered = powered;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	}

	public boolean isFullCube() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		return World.doesBlockHaveSolidTopSurface(world, blockpos.down()) ? super.canPlaceBlockAt(world, blockpos)
				: false;
	}

	public boolean canBlockStay(World worldIn, BlockPos pos) {
		return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
	}

	/**+
	 * Called randomly when setTickRandomly is set to true (used by
	 * e.g. crops to grow, etc.)
	 */
	public void randomTick(World var1, BlockPos var2, IBlockState var3, EaglercraftRandom var4) {
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {
		if (!this.isLocked(world, blockpos, iblockstate)) {
			boolean flag = this.shouldBePowered(world, blockpos, iblockstate);
			if (this.isRepeaterPowered && !flag) {
				world.setBlockState(blockpos, this.getUnpoweredState(iblockstate), 2);
			} else if (!this.isRepeaterPowered) {
				world.setBlockState(blockpos, this.getPoweredState(iblockstate), 2);
				if (!flag) {
					world.updateBlockTick(blockpos, this.getPoweredState(iblockstate).getBlock(),
							this.getTickDelay(iblockstate), -1);
				}
			}

		}
	}

	public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing enumfacing) {
		return enumfacing.getAxis() != EnumFacing.Axis.Y;
	}

	protected boolean isPowered(IBlockState var1) {
		return this.isRepeaterPowered;
	}

	public int getStrongPower(IBlockAccess iblockaccess, BlockPos blockpos, IBlockState iblockstate,
			EnumFacing enumfacing) {
		return this.getWeakPower(iblockaccess, blockpos, iblockstate, enumfacing);
	}

	public int getWeakPower(IBlockAccess iblockaccess, BlockPos blockpos, IBlockState iblockstate,
			EnumFacing enumfacing) {
		return !this.isPowered(iblockstate) ? 0
				: (iblockstate.getValue(FACING) == enumfacing
						? this.getActiveSignal(iblockaccess, blockpos, iblockstate)
						: 0);
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		if (this.canBlockStay(world, blockpos)) {
			this.updateState(world, blockpos, iblockstate);
		} else {
			this.dropBlockAsItem(world, blockpos, iblockstate, 0);
			world.setBlockToAir(blockpos);

			EnumFacing[] facings = EnumFacing._VALUES;
			BlockPos tmp = new BlockPos(0, 0, 0);
			for (int i = 0; i < facings.length; ++i) {
				world.notifyNeighborsOfStateChange(blockpos.offsetEvenFaster(facings[i], tmp), this);
			}

		}
	}

	protected void updateState(World world, BlockPos blockpos, IBlockState iblockstate) {
		if (!this.isLocked(world, blockpos, iblockstate)) {
			boolean flag = this.shouldBePowered(world, blockpos, iblockstate);
			if ((this.isRepeaterPowered && !flag || !this.isRepeaterPowered && flag)
					&& !world.isBlockTickPending(blockpos, this)) {
				byte b0 = -1;
				if (this.isFacingTowardsRepeater(world, blockpos, iblockstate)) {
					b0 = -3;
				} else if (this.isRepeaterPowered) {
					b0 = -2;
				}

				world.updateBlockTick(blockpos, this, this.getDelay(iblockstate), b0);
			}

		}
	}

	public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		return false;
	}

	protected boolean shouldBePowered(World world, BlockPos blockpos, IBlockState iblockstate) {
		return this.calculateInputStrength(world, blockpos, iblockstate) > 0;
	}

	protected int calculateInputStrength(World world, BlockPos blockpos, IBlockState iblockstate) {
		EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
		BlockPos blockpos1 = blockpos.offset(enumfacing);
		int i = world.getRedstonePower(blockpos1, enumfacing);
		if (i >= 15) {
			return i;
		} else {
			IBlockState iblockstate1 = world.getBlockState(blockpos1);
			return Math.max(i,
					iblockstate1.getBlock() == Blocks.redstone_wire
							? ((Integer) iblockstate1.getValue(BlockRedstoneWire.POWER)).intValue()
							: 0);
		}
	}

	protected int getPowerOnSides(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
		EnumFacing enumfacing1 = enumfacing.rotateY();
		EnumFacing enumfacing2 = enumfacing.rotateYCCW();
		return Math.max(this.getPowerOnSide(worldIn, pos.offset(enumfacing1), enumfacing1),
				this.getPowerOnSide(worldIn, pos.offset(enumfacing2), enumfacing2));
	}

	protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();
		return this.canPowerSide(block)
				? (block == Blocks.redstone_wire ? ((Integer) iblockstate.getValue(BlockRedstoneWire.POWER)).intValue()
						: worldIn.getStrongPower(pos, side))
				: 0;
	}

	/**+
	 * Can this block provide power. Only wire currently seems to
	 * have this change based on its state.
	 */
	public boolean canProvidePower() {
		return true;
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6,
			int var7, EntityLivingBase entitylivingbase) {
		return this.getDefaultState().withProperty(FACING, entitylivingbase.getHorizontalFacing().getOpposite());
	}

	/**+
	 * Called by ItemBlocks after a block is set in the world, to
	 * allow post-place logic
	 */
	public void onBlockPlacedBy(World world, BlockPos blockpos, IBlockState iblockstate, EntityLivingBase var4,
			ItemStack var5) {
		if (this.shouldBePowered(world, blockpos, iblockstate)) {
			world.scheduleUpdate(blockpos, this, 1);
		}

	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.notifyNeighbors(world, blockpos, iblockstate);
	}

	protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
		BlockPos blockpos = pos.offset(enumfacing.getOpposite());
		worldIn.notifyBlockOfStateChange(blockpos, this);
		worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
	}

	/**+
	 * Called when a player destroys this Block
	 */
	public void onBlockDestroyedByPlayer(World world, BlockPos blockpos, IBlockState iblockstate) {
		if (this.isRepeaterPowered) {
			EnumFacing[] facings = EnumFacing._VALUES;
			BlockPos tmp = new BlockPos(0, 0, 0);
			for (int i = 0; i < facings.length; ++i) {
				world.notifyNeighborsOfStateChange(blockpos.offsetEvenFaster(facings[i], tmp), this);
			}
		}

		super.onBlockDestroyedByPlayer(world, blockpos, iblockstate);
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	protected boolean canPowerSide(Block blockIn) {
		return blockIn.canProvidePower();
	}

	protected int getActiveSignal(IBlockAccess var1, BlockPos var2, IBlockState var3) {
		return 15;
	}

	public static boolean isRedstoneRepeaterBlockID(Block blockIn) {
		return Blocks.unpowered_repeater.isAssociated(blockIn) || Blocks.unpowered_comparator.isAssociated(blockIn);
	}

	public boolean isAssociated(Block other) {
		return other == this.getPoweredState(this.getDefaultState()).getBlock()
				|| other == this.getUnpoweredState(this.getDefaultState()).getBlock();
	}

	public boolean isFacingTowardsRepeater(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing enumfacing = ((EnumFacing) state.getValue(FACING)).getOpposite();
		BlockPos blockpos = pos.offset(enumfacing);
		return isRedstoneRepeaterBlockID(worldIn.getBlockState(blockpos).getBlock())
				? worldIn.getBlockState(blockpos).getValue(FACING) != enumfacing
				: false;
	}

	protected int getTickDelay(IBlockState state) {
		return this.getDelay(state);
	}

	protected abstract int getDelay(IBlockState var1);

	protected abstract IBlockState getPoweredState(IBlockState var1);

	protected abstract IBlockState getUnpoweredState(IBlockState var1);

	public boolean isAssociatedBlock(Block block) {
		return this.isAssociated(block);
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}
}