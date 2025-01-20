package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
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
public class BlockCocoa extends BlockDirectional implements IGrowable {
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);

	public BlockCocoa() {
		super(Material.plants);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(AGE,
				Integer.valueOf(0)));
		this.setTickRandomly(true);
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {
		if (!this.canBlockStay(world, blockpos, iblockstate)) {
			this.dropBlock(world, blockpos, iblockstate);
		} else if (world.rand.nextInt(5) == 0) {
			int i = ((Integer) iblockstate.getValue(AGE)).intValue();
			if (i < 2) {
				world.setBlockState(blockpos, iblockstate.withProperty(AGE, Integer.valueOf(i + 1)), 2);
			}
		}

	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		pos = pos.offset((EnumFacing) state.getValue(FACING));
		IBlockState iblockstate = worldIn.getBlockState(pos);
		return iblockstate.getBlock() == Blocks.log
				&& iblockstate.getValue(BlockPlanks.VARIANT) == BlockPlanks.EnumType.JUNGLE;
	}

	public boolean isFullCube() {
		return false;
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.setBlockBoundsBasedOnState(world, blockpos);
		return super.getCollisionBoundingBox(world, blockpos, iblockstate);
	}

	public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockpos) {
		this.setBlockBoundsBasedOnState(world, blockpos);
		return super.getSelectedBoundingBox(world, blockpos);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		IBlockState iblockstate = iblockaccess.getBlockState(blockpos);
		EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
		int i = ((Integer) iblockstate.getValue(AGE)).intValue();
		int j = 4 + i * 2;
		int k = 5 + i * 2;
		float f = (float) j / 2.0F;
		switch (enumfacing) {
		case SOUTH:
			this.setBlockBounds((8.0F - f) / 16.0F, (12.0F - (float) k) / 16.0F, (15.0F - (float) j) / 16.0F,
					(8.0F + f) / 16.0F, 0.75F, 0.9375F);
			break;
		case NORTH:
			this.setBlockBounds((8.0F - f) / 16.0F, (12.0F - (float) k) / 16.0F, 0.0625F, (8.0F + f) / 16.0F, 0.75F,
					(1.0F + (float) j) / 16.0F);
			break;
		case WEST:
			this.setBlockBounds(0.0625F, (12.0F - (float) k) / 16.0F, (8.0F - f) / 16.0F, (1.0F + (float) j) / 16.0F,
					0.75F, (8.0F + f) / 16.0F);
			break;
		case EAST:
			this.setBlockBounds((15.0F - (float) j) / 16.0F, (12.0F - (float) k) / 16.0F, (8.0F - f) / 16.0F, 0.9375F,
					0.75F, (8.0F + f) / 16.0F);
		}

	}

	/**+
	 * Called by ItemBlocks after a block is set in the world, to
	 * allow post-place logic
	 */
	public void onBlockPlacedBy(World world, BlockPos blockpos, IBlockState iblockstate,
			EntityLivingBase entitylivingbase, ItemStack var5) {
		EnumFacing enumfacing = EnumFacing.fromAngle((double) entitylivingbase.rotationYaw);
		world.setBlockState(blockpos, iblockstate.withProperty(FACING, enumfacing), 2);
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing enumfacing, float var4, float var5,
			float var6, int var7, EntityLivingBase var8) {
		if (!enumfacing.getAxis().isHorizontal()) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing.getOpposite()).withProperty(AGE,
				Integer.valueOf(0));
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		if (!this.canBlockStay(world, blockpos, iblockstate)) {
			this.dropBlock(world, blockpos, iblockstate);
		}

	}

	private void dropBlock(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
		this.dropBlockAsItem(worldIn, pos, state, 0);
	}

	/**+
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World world, BlockPos blockpos, IBlockState iblockstate, float var4,
			int var5) {
		int i = ((Integer) iblockstate.getValue(AGE)).intValue();
		byte b0 = 1;
		if (i >= 2) {
			b0 = 3;
		}

		for (int j = 0; j < b0; ++j) {
			spawnAsEntity(world, blockpos, new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeDamage()));
		}

	}

	public Item getItem(World var1, BlockPos var2) {
		return Items.dye;
	}

	public int getDamageValue(World var1, BlockPos var2) {
		return EnumDyeColor.BROWN.getDyeDamage();
	}

	/**+
	 * Whether this IGrowable can grow
	 */
	public boolean canGrow(World var1, BlockPos var2, IBlockState iblockstate, boolean var4) {
		return ((Integer) iblockstate.getValue(AGE)).intValue() < 2;
	}

	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {
		return true;
	}

	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState iblockstate) {
		world.setBlockState(blockpos,
				iblockstate.withProperty(AGE, Integer.valueOf(((Integer) iblockstate.getValue(AGE)).intValue() + 1)),
				2);
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(i)).withProperty(AGE,
				Integer.valueOf((i & 15) >> 2));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((EnumFacing) iblockstate.getValue(FACING)).getHorizontalIndex();
		i = i | ((Integer) iblockstate.getValue(AGE)).intValue() << 2;
		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, AGE });
	}
}