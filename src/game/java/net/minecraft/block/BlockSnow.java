package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
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
public class BlockSnow extends Block {
	public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 8);

	protected BlockSnow() {
		super(Material.snow);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LAYERS, Integer.valueOf(1)));
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setBlockBoundsForItemRender();
	}

	public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockpos) {
		return ((Integer) iblockaccess.getBlockState(blockpos).getValue(LAYERS)).intValue() < 5;
	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos blockpos, IBlockState iblockstate) {
		int i = ((Integer) iblockstate.getValue(LAYERS)).intValue() - 1;
		float f = 0.125F;
		return new AxisAlignedBB((double) blockpos.getX() + this.minX, (double) blockpos.getY() + this.minY,
				(double) blockpos.getZ() + this.minZ, (double) blockpos.getX() + this.maxX,
				(double) ((float) blockpos.getY() + (float) i * f), (double) blockpos.getZ() + this.maxZ);
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
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		this.getBoundsForLayers(0);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		IBlockState iblockstate = iblockaccess.getBlockState(blockpos);
		this.getBoundsForLayers(((Integer) iblockstate.getValue(LAYERS)).intValue());
	}

	protected void getBoundsForLayers(int parInt1) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, (float) parInt1 / 8.0F, 1.0F);
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		IBlockState iblockstate = world.getBlockState(blockpos.down());
		Block block = iblockstate.getBlock();
		return block != Blocks.ice && block != Blocks.packed_ice ? (block.getMaterial() == Material.leaves ? true
				: (block == this && ((Integer) iblockstate.getValue(LAYERS)).intValue() >= 7 ? true
						: block.isOpaqueCube() && block.blockMaterial.blocksMovement()))
				: false;
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		this.checkAndDropBlock(world, blockpos, iblockstate);
	}

	private boolean checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canPlaceBlockAt(worldIn, pos)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
			return false;
		} else {
			return true;
		}
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, BlockPos blockpos, IBlockState iblockstate,
			TileEntity var5) {
		spawnAsEntity(world, blockpos,
				new ItemStack(Items.snowball, ((Integer) iblockstate.getValue(LAYERS)).intValue() + 1, 0));
		world.setBlockToAir(blockpos);
		entityplayer.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Items.snowball;
	}

	/**+
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {
		if (world.getLightFor(EnumSkyBlock.BLOCK, blockpos) > 11) {
			this.dropBlockAsItem(world, blockpos, world.getBlockState(blockpos), 0);
			world.setBlockToAir(blockpos);
		}

	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, BlockPos blockpos, EnumFacing enumfacing) {
		return enumfacing == EnumFacing.UP ? true : super.shouldSideBeRendered(iblockaccess, blockpos, enumfacing);
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(LAYERS, Integer.valueOf((i & 7) + 1));
	}

	/**+
	 * Whether this Block can be replaced directly by other blocks
	 * (true for e.g. tall grass)
	 */
	public boolean isReplaceable(World world, BlockPos blockpos) {
		return ((Integer) world.getBlockState(blockpos).getValue(LAYERS)).intValue() == 1;
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(LAYERS)).intValue() - 1;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { LAYERS });
	}
}