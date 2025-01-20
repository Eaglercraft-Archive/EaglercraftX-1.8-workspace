package net.minecraft.block;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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
public class BlockPane extends Block {
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	private final boolean canDrop;

	protected BlockPane(Material materialIn, boolean canDrop) {
		super(materialIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false))
				.withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false))
				.withProperty(WEST, Boolean.valueOf(false)));
		this.canDrop = canDrop;
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		return iblockstate
				.withProperty(NORTH,
						Boolean.valueOf(
								this.canPaneConnectToBlock(iblockaccess.getBlockState(blockpos.north()).getBlock())))
				.withProperty(SOUTH,
						Boolean.valueOf(
								this.canPaneConnectToBlock(iblockaccess.getBlockState(blockpos.south()).getBlock())))
				.withProperty(WEST,
						Boolean.valueOf(
								this.canPaneConnectToBlock(iblockaccess.getBlockState(blockpos.west()).getBlock())))
				.withProperty(EAST, Boolean
						.valueOf(this.canPaneConnectToBlock(iblockaccess.getBlockState(blockpos.east()).getBlock())));
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom random, int i) {
		return !this.canDrop ? null : super.getItemDropped(iblockstate, random, i);
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

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, BlockPos blockpos, EnumFacing enumfacing) {
		return iblockaccess.getBlockState(blockpos).getBlock() == this ? false
				: super.shouldSideBeRendered(iblockaccess, blockpos, enumfacing);
	}

	/**+
	 * Add all collision boxes of this Block to the list that
	 * intersect with the given mask.
	 */
	public void addCollisionBoxesToList(World world, BlockPos blockpos, IBlockState iblockstate,
			AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, Entity entity) {
		boolean flag = this.canPaneConnectToBlock(world.getBlockState(blockpos.north()).getBlock());
		boolean flag1 = this.canPaneConnectToBlock(world.getBlockState(blockpos.south()).getBlock());
		boolean flag2 = this.canPaneConnectToBlock(world.getBlockState(blockpos.west()).getBlock());
		boolean flag3 = this.canPaneConnectToBlock(world.getBlockState(blockpos.east()).getBlock());
		if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1)) {
			if (flag2) {
				this.setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
				super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
			} else if (flag3) {
				this.setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
				super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
			}
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
			super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		}

		if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1)) {
			if (flag) {
				this.setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
				super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
			} else if (flag1) {
				this.setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
				super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
			}
		} else {
			this.setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
			super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		}

	}

	/**+
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		float f = 0.4375F;
		float f1 = 0.5625F;
		float f2 = 0.4375F;
		float f3 = 0.5625F;
		boolean flag = this.canPaneConnectToBlock(iblockaccess.getBlockState(blockpos.north()).getBlock());
		boolean flag1 = this.canPaneConnectToBlock(iblockaccess.getBlockState(blockpos.south()).getBlock());
		boolean flag2 = this.canPaneConnectToBlock(iblockaccess.getBlockState(blockpos.west()).getBlock());
		boolean flag3 = this.canPaneConnectToBlock(iblockaccess.getBlockState(blockpos.east()).getBlock());
		if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1)) {
			if (flag2) {
				f = 0.0F;
			} else if (flag3) {
				f1 = 1.0F;
			}
		} else {
			f = 0.0F;
			f1 = 1.0F;
		}

		if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1)) {
			if (flag) {
				f2 = 0.0F;
			} else if (flag1) {
				f3 = 1.0F;
			}
		} else {
			f2 = 0.0F;
			f3 = 1.0F;
		}

		this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
	}

	public final boolean canPaneConnectToBlock(Block blockIn) {
		return blockIn.isFullBlock() || blockIn == this || blockIn == Blocks.glass || blockIn == Blocks.stained_glass
				|| blockIn == Blocks.stained_glass_pane || blockIn instanceof BlockPane;
	}

	protected boolean canSilkHarvest() {
		return true;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	public boolean eaglerShadersShouldRenderGlassHighlights() {
		return this == Blocks.glass_pane && DeferredStateManager.isRenderingGlassHighlights();
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState var1) {
		return 0;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { NORTH, EAST, WEST, SOUTH });
	}
}