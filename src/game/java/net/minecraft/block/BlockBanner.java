package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
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
public class BlockBanner extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

	protected BlockBanner() {
		super(Material.wood);
		float f = 0.25F;
		float f1 = 1.0F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
	}

	/**+
	 * Gets the localized name of this block. Used for the
	 * statistics page.
	 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal("item.banner.white.name");
	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
		return null;
	}

	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		this.setBlockBoundsBasedOnState(worldIn, pos);
		return super.getSelectedBoundingBox(worldIn, pos);
	}

	public boolean isFullCube() {
		return false;
	}

	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	public boolean func_181623_g() {
		return true;
	}

	/**+
	 * Returns a new instance of a block's tile entity class. Called
	 * on placing the block.
	 */
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBanner();
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState state, EaglercraftRandom rand, int fortune) {
		return Items.banner;
	}

	public Item getItem(World worldIn, BlockPos pos) {
		return Items.banner;
	}

	/**+
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World world, BlockPos blockpos, IBlockState iblockstate, float f, int i) {
		TileEntity tileentity = world.getTileEntity(blockpos);
		if (tileentity instanceof TileEntityBanner) {
			ItemStack itemstack = new ItemStack(Items.banner, 1, ((TileEntityBanner) tileentity).getBaseColor());
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			tileentity.writeToNBT(nbttagcompound);
			nbttagcompound.removeTag("x");
			nbttagcompound.removeTag("y");
			nbttagcompound.removeTag("z");
			nbttagcompound.removeTag("id");
			itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
			spawnAsEntity(world, blockpos, itemstack);
		} else {
			super.dropBlockAsItemWithChance(world, blockpos, iblockstate, f, i);
		}

	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return !this.func_181087_e(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos);
	}

	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
		if (te instanceof TileEntityBanner) {
			TileEntityBanner tileentitybanner = (TileEntityBanner) te;
			ItemStack itemstack = new ItemStack(Items.banner, 1, ((TileEntityBanner) te).getBaseColor());
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			TileEntityBanner.func_181020_a(nbttagcompound, tileentitybanner.getBaseColor(),
					tileentitybanner.func_181021_d());
			itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
			spawnAsEntity(worldIn, pos, itemstack);
		} else {
			super.harvestBlock(worldIn, player, pos, state, (TileEntity) null);
		}

	}

	public static class BlockBannerHanging extends BlockBanner {
		public BlockBannerHanging() {
			this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		}

		public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
			EnumFacing enumfacing = (EnumFacing) iblockaccess.getBlockState(blockpos).getValue(FACING);
			float f = 0.0F;
			float f1 = 0.78125F;
			float f2 = 0.0F;
			float f3 = 1.0F;
			float f4 = 0.125F;
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			switch (enumfacing) {
			case NORTH:
			default:
				this.setBlockBounds(f2, f, 1.0F - f4, f3, f1, 1.0F);
				break;
			case SOUTH:
				this.setBlockBounds(f2, f, 0.0F, f3, f1, f4);
				break;
			case WEST:
				this.setBlockBounds(1.0F - f4, f, f2, 1.0F, f1, f3);
				break;
			case EAST:
				this.setBlockBounds(0.0F, f, f2, f4, f1, f3);
			}

		}

		public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block block) {
			EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
			if (!world.getBlockState(blockpos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid()) {
				this.dropBlockAsItem(world, blockpos, iblockstate, 0);
				world.setBlockToAir(blockpos);
			}

			super.onNeighborBlockChange(world, blockpos, iblockstate, block);
		}

		public IBlockState getStateFromMeta(int i) {
			EnumFacing enumfacing = EnumFacing.getFront(i);
			if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
				enumfacing = EnumFacing.NORTH;
			}

			return this.getDefaultState().withProperty(FACING, enumfacing);
		}

		public int getMetaFromState(IBlockState iblockstate) {
			return ((EnumFacing) iblockstate.getValue(FACING)).getIndex();
		}

		protected BlockState createBlockState() {
			return new BlockState(this, new IProperty[] { FACING });
		}
	}

	public static class BlockBannerStanding extends BlockBanner {
		public BlockBannerStanding() {
			this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, Integer.valueOf(0)));
		}

		public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
			if (!worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid()) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
			}

			super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		}

		public IBlockState getStateFromMeta(int i) {
			return this.getDefaultState().withProperty(ROTATION, Integer.valueOf(i));
		}

		public int getMetaFromState(IBlockState iblockstate) {
			return ((Integer) iblockstate.getValue(ROTATION)).intValue();
		}

		protected BlockState createBlockState() {
			return new BlockState(this, new IProperty[] { ROTATION });
		}
	}
}