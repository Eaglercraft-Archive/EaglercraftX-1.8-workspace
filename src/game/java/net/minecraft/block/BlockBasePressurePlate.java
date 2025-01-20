package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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
public abstract class BlockBasePressurePlate extends Block {
	protected BlockBasePressurePlate(Material materialIn) {
		this(materialIn, materialIn.getMaterialMapColor());
	}

	protected BlockBasePressurePlate(Material parMaterial, MapColor parMapColor) {
		super(parMaterial, parMapColor);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setTickRandomly(true);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		this.setBlockBoundsBasedOnState0(iblockaccess.getBlockState(blockpos));
	}

	protected void setBlockBoundsBasedOnState0(IBlockState state) {
		boolean flag = this.getRedstoneStrength(state) > 0;
		float f = 0.0625F;
		if (flag) {
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.03125F, 0.9375F);
		} else {
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.0625F, 0.9375F);
		}

	}

	/**+
	 * How many world ticks before ticking
	 */
	public int tickRate(World worldIn) {
		return 20;
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

	public boolean isPassable(IBlockAccess var1, BlockPos var2) {
		return true;
	}

	public boolean func_181623_g() {
		return true;
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		return this.canBePlacedOn(world, blockpos.down());
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		if (!this.canBePlacedOn(world, blockpos.down())) {
			this.dropBlockAsItem(world, blockpos, iblockstate, 0);
			world.setBlockToAir(blockpos);
		}

	}

	private boolean canBePlacedOn(World worldIn, BlockPos pos) {
		return World.doesBlockHaveSolidTopSurface(worldIn, pos)
				|| worldIn.getBlockState(pos).getBlock() instanceof BlockFence;
	}

	/**+
	 * Called randomly when setTickRandomly is set to true (used by
	 * e.g. crops to grow, etc.)
	 */
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, EaglercraftRandom random) {
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, EaglercraftRandom rand) {
		if (!worldIn.isRemote) {
			int i = this.getRedstoneStrength(state);
			if (i > 0) {
				this.updateState(worldIn, pos, state, i);
			}
		}
	}

	/**+
	 * Called When an Entity Collided with the Block
	 */
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote) {
			int i = this.getRedstoneStrength(state);
			if (i == 0) {
				this.updateState(worldIn, pos, state, i);
			}
		}
	}

	/**+
	 * Updates the pressure plate when stepped on
	 */
	protected void updateState(World worldIn, BlockPos pos, IBlockState state, int oldRedstoneStrength) {
		int i = this.computeRedstoneStrength(worldIn, pos);
		boolean flag = oldRedstoneStrength > 0;
		boolean flag1 = i > 0;
		if (oldRedstoneStrength != i) {
			state = this.setRedstoneStrength(state, i);
			worldIn.setBlockState(pos, state, 2);
			this.updateNeighbors(worldIn, pos);
			worldIn.markBlockRangeForRenderUpdate(pos, pos);
		}

		if (!flag1 && flag) {
			worldIn.playSoundEffect((double) pos.getX() + 0.5D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D,
					"random.click", 0.3F, 0.5F);
		} else if (flag1 && !flag) {
			worldIn.playSoundEffect((double) pos.getX() + 0.5D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D,
					"random.click", 0.3F, 0.6F);
		}

		if (flag1) {
			worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
		}

	}

	/**+
	 * Returns the cubic AABB inset by 1/8 on all sides
	 */
	protected AxisAlignedBB getSensitiveAABB(BlockPos pos) {
		float f = 0.125F;
		return new AxisAlignedBB((double) ((float) pos.getX() + 0.125F), (double) pos.getY(),
				(double) ((float) pos.getZ() + 0.125F), (double) ((float) (pos.getX() + 1) - 0.125F),
				(double) pos.getY() + 0.25D, (double) ((float) (pos.getZ() + 1) - 0.125F));
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		if (this.getRedstoneStrength(iblockstate) > 0) {
			this.updateNeighbors(world, blockpos);
		}

		super.breakBlock(world, blockpos, iblockstate);
	}

	/**+
	 * Notify block and block below of changes
	 */
	protected void updateNeighbors(World worldIn, BlockPos pos) {
		worldIn.notifyNeighborsOfStateChange(pos, this);
		worldIn.notifyNeighborsOfStateChange(pos.down(), this);
	}

	public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
		return this.getRedstoneStrength(state);
	}

	public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
		return side == EnumFacing.UP ? this.getRedstoneStrength(state) : 0;
	}

	/**+
	 * Can this block provide power. Only wire currently seems to
	 * have this change based on its state.
	 */
	public boolean canProvidePower() {
		return true;
	}

	/**+
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		float f = 0.5F;
		float f1 = 0.125F;
		float f2 = 0.5F;
		this.setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
	}

	public int getMobilityFlag() {
		return 1;
	}

	protected abstract int computeRedstoneStrength(World var1, BlockPos var2);

	protected abstract int getRedstoneStrength(IBlockState var1);

	protected abstract IBlockState setRedstoneStrength(IBlockState var1, int var2);
}