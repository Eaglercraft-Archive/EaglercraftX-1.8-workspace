package net.minecraft.block;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
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
public class BlockPistonExtension extends Block {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;
	public static final PropertyBool SHORT = PropertyBool.create("short");

	public BlockPistonExtension() {
		super(Material.piston);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT)
				.withProperty(SHORT, Boolean.valueOf(false)));
		this.setStepSound(soundTypePiston);
		this.setHardness(0.5F);
	}

	public static void bootstrapStates() {
		TYPE = PropertyEnum.<BlockPistonExtension.EnumPistonType>create("type",
				BlockPistonExtension.EnumPistonType.class);
	}

	public void onBlockHarvested(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer) {
		if (entityplayer.capabilities.isCreativeMode) {
			EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
			if (enumfacing != null) {
				BlockPos blockpos1 = blockpos.offset(enumfacing.getOpposite());
				Block block = world.getBlockState(blockpos1).getBlock();
				if (block == Blocks.piston || block == Blocks.sticky_piston) {
					world.setBlockToAir(blockpos1);
				}
			}
		}

		super.onBlockHarvested(world, blockpos, iblockstate, entityplayer);
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		super.breakBlock(world, blockpos, iblockstate);
		EnumFacing enumfacing = ((EnumFacing) iblockstate.getValue(FACING)).getOpposite();
		blockpos = blockpos.offset(enumfacing);
		IBlockState iblockstate1 = world.getBlockState(blockpos);
		if ((iblockstate1.getBlock() == Blocks.piston || iblockstate1.getBlock() == Blocks.sticky_piston)
				&& ((Boolean) iblockstate1.getValue(BlockPistonBase.EXTENDED)).booleanValue()) {
			iblockstate1.getBlock().dropBlockAsItem(world, blockpos, iblockstate1, 0);
			world.setBlockToAir(blockpos);
		}

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

	public boolean canPlaceBlockAt(World var1, BlockPos var2) {
		return false;
	}

	/**+
	 * Check whether this Block can be placed on the given side
	 */
	public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
		return false;
	}

	/**+
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	/**+
	 * Add all collision boxes of this Block to the list that
	 * intersect with the given mask.
	 */
	public void addCollisionBoxesToList(World world, BlockPos blockpos, IBlockState iblockstate,
			AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, Entity entity) {
		this.applyHeadBounds(iblockstate);
		super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		this.applyCoreBounds(iblockstate);
		super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	private void applyCoreBounds(IBlockState state) {
		float f = 0.25F;
		float f1 = 0.375F;
		float f2 = 0.625F;
		float f3 = 0.25F;
		float f4 = 0.75F;
		switch ((EnumFacing) state.getValue(FACING)) {
		case DOWN:
			this.setBlockBounds(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
			break;
		case UP:
			this.setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
			break;
		case NORTH:
			this.setBlockBounds(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
			break;
		case SOUTH:
			this.setBlockBounds(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
			break;
		case WEST:
			this.setBlockBounds(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
			break;
		case EAST:
			this.setBlockBounds(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
		}

	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		this.applyHeadBounds(iblockaccess.getBlockState(blockpos));
	}

	public void applyHeadBounds(IBlockState state) {
		float f = 0.25F;
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
		if (enumfacing != null) {
			switch (enumfacing) {
			case DOWN:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
				break;
			case UP:
				this.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case NORTH:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
				break;
			case SOUTH:
				this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
				break;
			case WEST:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
				break;
			case EAST:
				this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			}

		}
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block block) {
		EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
		BlockPos blockpos1 = blockpos.offset(enumfacing.getOpposite());
		IBlockState iblockstate1 = world.getBlockState(blockpos1);
		if (iblockstate1.getBlock() != Blocks.piston && iblockstate1.getBlock() != Blocks.sticky_piston) {
			world.setBlockToAir(blockpos);
		} else {
			iblockstate1.getBlock().onNeighborBlockChange(world, blockpos1, iblockstate1, block);
		}

	}

	public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
		return true;
	}

	public static EnumFacing getFacing(int meta) {
		int i = meta & 7;
		return i > 5 ? null : EnumFacing.getFront(i);
	}

	public Item getItem(World world, BlockPos blockpos) {
		return world.getBlockState(blockpos).getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY
				? Item.getItemFromBlock(Blocks.sticky_piston)
				: Item.getItemFromBlock(Blocks.piston);
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, getFacing(i)).withProperty(TYPE,
				(i & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((EnumFacing) iblockstate.getValue(FACING)).getIndex();
		if (iblockstate.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, TYPE, SHORT });
	}

	public static enum EnumPistonType implements IStringSerializable {
		DEFAULT("normal"), STICKY("sticky");

		private final String VARIANT;

		private EnumPistonType(String name) {
			this.VARIANT = name;
		}

		public String toString() {
			return this.VARIANT;
		}

		public String getName() {
			return this.VARIANT;
		}
	}
}