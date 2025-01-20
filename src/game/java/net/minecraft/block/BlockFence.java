package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemLead;
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
public class BlockFence extends Block {
	/**+
	 * Whether this fence connects in the northern direction
	 */
	public static final PropertyBool NORTH = PropertyBool.create("north");
	/**+
	 * Whether this fence connects in the eastern direction
	 */
	public static final PropertyBool EAST = PropertyBool.create("east");
	/**+
	 * Whether this fence connects in the southern direction
	 */
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	/**+
	 * Whether this fence connects in the western direction
	 */
	public static final PropertyBool WEST = PropertyBool.create("west");

	public BlockFence(Material materialIn) {
		this(materialIn, materialIn.getMaterialMapColor());
	}

	public BlockFence(Material parMaterial, MapColor parMapColor) {
		super(parMaterial, parMapColor);
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false))
				.withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false))
				.withProperty(WEST, Boolean.valueOf(false)));
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**+
	 * Add all collision boxes of this Block to the list that
	 * intersect with the given mask.
	 */
	public void addCollisionBoxesToList(World world, BlockPos blockpos, IBlockState iblockstate,
			AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, Entity entity) {
		boolean flag = this.canConnectTo(world, blockpos.north());
		boolean flag1 = this.canConnectTo(world, blockpos.south());
		boolean flag2 = this.canConnectTo(world, blockpos.west());
		boolean flag3 = this.canConnectTo(world, blockpos.east());
		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;
		if (flag) {
			f2 = 0.0F;
		}

		if (flag1) {
			f3 = 1.0F;
		}

		if (flag || flag1) {
			this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
			super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		}

		f2 = 0.375F;
		f3 = 0.625F;
		if (flag2) {
			f = 0.0F;
		}

		if (flag3) {
			f1 = 1.0F;
		}

		if (flag2 || flag3 || !flag && !flag1) {
			this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
			super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		}

		if (flag) {
			f2 = 0.0F;
		}

		if (flag1) {
			f3 = 1.0F;
		}

		this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		boolean flag = this.canConnectTo(iblockaccess, blockpos.north());
		boolean flag1 = this.canConnectTo(iblockaccess, blockpos.south());
		boolean flag2 = this.canConnectTo(iblockaccess, blockpos.west());
		boolean flag3 = this.canConnectTo(iblockaccess, blockpos.east());
		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;
		if (flag) {
			f2 = 0.0F;
		}

		if (flag1) {
			f3 = 1.0F;
		}

		if (flag2) {
			f = 0.0F;
		}

		if (flag3) {
			f1 = 1.0F;
		}

		this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
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
		return false;
	}

	public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos).getBlock();
		return block == Blocks.barrier ? false
				: ((!(block instanceof BlockFence) || block.blockMaterial != this.blockMaterial)
						&& !(block instanceof BlockFenceGate)
								? (block.blockMaterial.isOpaque() && block.isFullCube()
										? block.blockMaterial != Material.gourd
										: false)
								: true);
	}

	public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
		return true;
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState var3, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		return world.isRemote ? true : ItemLead.attachToFence(entityplayer, world, blockpos);
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState var1) {
		return 0;
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		return iblockstate.withProperty(NORTH, Boolean.valueOf(this.canConnectTo(iblockaccess, blockpos.north())))
				.withProperty(EAST, Boolean.valueOf(this.canConnectTo(iblockaccess, blockpos.east())))
				.withProperty(SOUTH, Boolean.valueOf(this.canConnectTo(iblockaccess, blockpos.south())))
				.withProperty(WEST, Boolean.valueOf(this.canConnectTo(iblockaccess, blockpos.west())));
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { NORTH, EAST, WEST, SOUTH });
	}
}