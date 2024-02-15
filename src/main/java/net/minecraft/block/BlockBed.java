package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
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
public class BlockBed extends BlockDirectional {
	public static PropertyEnum<BlockBed.EnumPartType> PART;
	public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");

	public BlockBed() {
		super(Material.cloth);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PART, BlockBed.EnumPartType.FOOT)
				.withProperty(OCCUPIED, Boolean.valueOf(false)));
		this.setBedBounds();
	}

	public static void bootstrapStates() {
		PART = PropertyEnum.<BlockBed.EnumPartType>create("part", BlockBed.EnumPartType.class);
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		return true;
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

	public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
		this.setBedBounds();
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
		if (iblockstate.getValue(PART) == BlockBed.EnumPartType.HEAD) {
			if (world.getBlockState(blockpos.offset(enumfacing.getOpposite())).getBlock() != this) {
				world.setBlockToAir(blockpos);
			}
		} else if (world.getBlockState(blockpos.offset(enumfacing)).getBlock() != this) {
			world.setBlockToAir(blockpos);
		}

	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {
		return iblockstate.getValue(PART) == BlockBed.EnumPartType.HEAD ? null : Items.bed;
	}

	private void setBedBounds() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
	}

	/**+
	 * Returns a safe BlockPos to disembark the bed
	 */
	public static BlockPos getSafeExitLocation(World worldIn, BlockPos pos, int tries) {
		EnumFacing enumfacing = (EnumFacing) worldIn.getBlockState(pos).getValue(FACING);
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();

		for (int l = 0; l <= 1; ++l) {
			int i1 = i - enumfacing.getFrontOffsetX() * l - 1;
			int j1 = k - enumfacing.getFrontOffsetZ() * l - 1;
			int k1 = i1 + 2;
			int l1 = j1 + 2;

			for (int i2 = i1; i2 <= k1; ++i2) {
				for (int j2 = j1; j2 <= l1; ++j2) {
					BlockPos blockpos = new BlockPos(i2, j, j2);
					if (hasRoomForPlayer(worldIn, blockpos)) {
						if (tries <= 0) {
							return blockpos;
						}

						--tries;
					}
				}
			}
		}

		return null;
	}

	protected static boolean hasRoomForPlayer(World worldIn, BlockPos pos) {
		return World.doesBlockHaveSolidTopSurface(worldIn, pos.down())
				&& !worldIn.getBlockState(pos).getBlock().getMaterial().isSolid()
				&& !worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid();
	}

	/**+
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World world, BlockPos blockpos, IBlockState iblockstate, float f, int var5) {
		if (iblockstate.getValue(PART) == BlockBed.EnumPartType.FOOT) {
			super.dropBlockAsItemWithChance(world, blockpos, iblockstate, f, 0);
		}

	}

	public int getMobilityFlag() {
		return 1;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	public Item getItem(World var1, BlockPos var2) {
		return Items.bed;
	}

	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode && state.getValue(PART) == BlockBed.EnumPartType.HEAD) {
			BlockPos blockpos = pos.offset(((EnumFacing) state.getValue(FACING)).getOpposite());
			if (worldIn.getBlockState(blockpos).getBlock() == this) {
				worldIn.setBlockToAir(blockpos);
			}
		}

	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		EnumFacing enumfacing = EnumFacing.getHorizontal(i);
		return (i & 8) > 0
				? this.getDefaultState().withProperty(PART, BlockBed.EnumPartType.HEAD).withProperty(FACING, enumfacing)
						.withProperty(OCCUPIED, Boolean.valueOf((i & 4) > 0))
				: this.getDefaultState().withProperty(PART, BlockBed.EnumPartType.FOOT).withProperty(FACING,
						enumfacing);
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if (state.getValue(PART) == BlockBed.EnumPartType.FOOT) {
			IBlockState iblockstate = worldIn.getBlockState(pos.offset((EnumFacing) state.getValue(FACING)));
			if (iblockstate.getBlock() == this) {
				state = state.withProperty(OCCUPIED, iblockstate.getValue(OCCUPIED));
			}
		}

		return state;
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((EnumFacing) iblockstate.getValue(FACING)).getHorizontalIndex();
		if (iblockstate.getValue(PART) == BlockBed.EnumPartType.HEAD) {
			i |= 8;
			if (((Boolean) iblockstate.getValue(OCCUPIED)).booleanValue()) {
				i |= 4;
			}
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, PART, OCCUPIED });
	}

	public static enum EnumPartType implements IStringSerializable {
		HEAD("head"), FOOT("foot");

		private final String name;

		private EnumPartType(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}
}