package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.stats.StatList;
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
public class BlockCake extends Block {
	public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);

	protected BlockCake() {
		super(Material.cake);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, Integer.valueOf(0)));
		this.setTickRandomly(true);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		float f = 0.0625F;
		float f1 = (float) (1 + ((Integer) iblockaccess.getBlockState(blockpos).getValue(BITES)).intValue() * 2)
				/ 16.0F;
		float f2 = 0.5F;
		this.setBlockBounds(f1, 0.0F, f, 1.0F - f, f2, 1.0F - f);
	}

	/**+
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		float f = 0.0625F;
		float f1 = 0.5F;
		this.setBlockBounds(f, 0.0F, f, 1.0F - f, f1, 1.0F - f);
	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos blockpos, IBlockState iblockstate) {
		float f = 0.0625F;
		float f1 = (float) (1 + ((Integer) iblockstate.getValue(BITES)).intValue() * 2) / 16.0F;
		float f2 = 0.5F;
		return new AxisAlignedBB((double) ((float) blockpos.getX() + f1), (double) blockpos.getY(),
				(double) ((float) blockpos.getZ() + f), (double) ((float) (blockpos.getX() + 1) - f),
				(double) ((float) blockpos.getY() + f2), (double) ((float) (blockpos.getZ() + 1) - f));
	}

	public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockpos) {
		return this.getCollisionBoundingBox(world, blockpos, world.getBlockState(blockpos));
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

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		this.eatCake(world, blockpos, iblockstate, entityplayer);
		return true;
	}

	public void onBlockClicked(World world, BlockPos blockpos, EntityPlayer entityplayer) {
		this.eatCake(world, blockpos, world.getBlockState(blockpos), entityplayer);
	}

	private void eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (player.canEat(false)) {
			player.triggerAchievement(StatList.field_181724_H);
			player.getFoodStats().addStats(2, 0.1F);
			int i = ((Integer) state.getValue(BITES)).intValue();
			if (i < 6) {
				worldIn.setBlockState(pos, state.withProperty(BITES, Integer.valueOf(i + 1)), 3);
			} else {
				worldIn.setBlockToAir(pos);
			}

		}
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		return super.canPlaceBlockAt(world, blockpos) ? this.canBlockStay(world, blockpos) : false;
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState var3, Block var4) {
		if (!this.canBlockStay(world, blockpos)) {
			world.setBlockToAir(blockpos);
		}

	}

	private boolean canBlockStay(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid();
	}

	/**+
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return null;
	}

	public Item getItem(World var1, BlockPos var2) {
		return Items.cake;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(BITES, Integer.valueOf(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(BITES)).intValue();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BITES });
	}

	public int getComparatorInputOverride(World world, BlockPos blockpos) {
		return (7 - ((Integer) world.getBlockState(blockpos).getValue(BITES)).intValue()) * 2;
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}
}