package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.google.common.base.Predicate;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class BlockSkull extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool NODROP = PropertyBool.create("nodrop");
	private static final Predicate<BlockWorldState> IS_WITHER_SKELETON = new Predicate<BlockWorldState>() {
		public boolean apply(BlockWorldState blockworldstate) {
			return blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getBlock() == Blocks.skull
					&& blockworldstate.getTileEntity() instanceof TileEntitySkull
					&& ((TileEntitySkull) blockworldstate.getTileEntity()).getSkullType() == 1;
		}
	};
	private BlockPattern witherBasePattern;
	private BlockPattern witherPattern;

	protected BlockSkull() {
		super(Material.circuits);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(NODROP,
				Boolean.valueOf(false)));
		this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
	}

	/**+
	 * Gets the localized name of this block. Used for the
	 * statistics page.
	 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal("tile.skull.skeleton.name");
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

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		switch ((EnumFacing) iblockaccess.getBlockState(blockpos).getValue(FACING)) {
		case UP:
		default:
			this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
			break;
		case NORTH:
			this.setBlockBounds(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
			break;
		case SOUTH:
			this.setBlockBounds(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
			break;
		case WEST:
			this.setBlockBounds(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
			break;
		case EAST:
			this.setBlockBounds(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
		}

	}

	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.setBlockBoundsBasedOnState(world, blockpos);
		return super.getCollisionBoundingBox(world, blockpos, iblockstate);
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6,
			int var7, EntityLivingBase entitylivingbase) {
		return this.getDefaultState().withProperty(FACING, entitylivingbase.getHorizontalFacing()).withProperty(NODROP,
				Boolean.valueOf(false));
	}

	/**+
	 * Returns a new instance of a block's tile entity class. Called
	 * on placing the block.
	 */
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntitySkull();
	}

	public Item getItem(World var1, BlockPos var2) {
		return Items.skull;
	}

	public int getDamageValue(World world, BlockPos blockpos) {
		TileEntity tileentity = world.getTileEntity(blockpos);
		return tileentity instanceof TileEntitySkull ? ((TileEntitySkull) tileentity).getSkullType()
				: super.getDamageValue(world, blockpos);
	}

	/**+
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
	}

	public void onBlockHarvested(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer) {
		if (entityplayer.capabilities.isCreativeMode) {
			iblockstate = iblockstate.withProperty(NODROP, Boolean.valueOf(true));
			world.setBlockState(blockpos, iblockstate, 4);
		}

		super.onBlockHarvested(world, blockpos, iblockstate, entityplayer);
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {

	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Items.skull;
	}

	public boolean canDispenserPlace(World worldIn, BlockPos pos, ItemStack stack) {
		return false;
	}

	public void checkWitherSpawn(World worldIn, BlockPos pos, TileEntitySkull te) {

	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(i & 7)).withProperty(NODROP,
				Boolean.valueOf((i & 8) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((EnumFacing) iblockstate.getValue(FACING)).getIndex();
		if (((Boolean) iblockstate.getValue(NODROP)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, NODROP });
	}

	protected BlockPattern getWitherBasePattern() {
		if (this.witherBasePattern == null) {
			this.witherBasePattern = FactoryBlockPattern.start().aisle(new String[] { "   ", "###", "~#~" })
					.where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand)))
					.where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
		}

		return this.witherBasePattern;
	}

	protected BlockPattern getWitherPattern() {
		if (this.witherPattern == null) {
			this.witherPattern = FactoryBlockPattern.start().aisle(new String[] { "^^^", "###", "~#~" })
					.where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand)))
					.where('^', IS_WITHER_SKELETON)
					.where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
		}

		return this.witherPattern;
	}
}