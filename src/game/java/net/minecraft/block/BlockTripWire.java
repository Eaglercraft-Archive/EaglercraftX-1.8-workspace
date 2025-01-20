package net.minecraft.block;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
public class BlockTripWire extends Block {
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");
	public static final PropertyBool ATTACHED = PropertyBool.create("attached");
	public static final PropertyBool DISARMED = PropertyBool.create("disarmed");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");

	public BlockTripWire() {
		super(Material.circuits);
		this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false))
				.withProperty(SUSPENDED, Boolean.valueOf(false)).withProperty(ATTACHED, Boolean.valueOf(false))
				.withProperty(DISARMED, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false))
				.withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false))
				.withProperty(WEST, Boolean.valueOf(false)));
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
		this.setTickRandomly(true);
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		return iblockstate
				.withProperty(NORTH,
						Boolean.valueOf(isConnectedTo(iblockaccess, blockpos, iblockstate, EnumFacing.NORTH)))
				.withProperty(EAST,
						Boolean.valueOf(isConnectedTo(iblockaccess, blockpos, iblockstate, EnumFacing.EAST)))
				.withProperty(SOUTH,
						Boolean.valueOf(isConnectedTo(iblockaccess, blockpos, iblockstate, EnumFacing.SOUTH)))
				.withProperty(WEST,
						Boolean.valueOf(isConnectedTo(iblockaccess, blockpos, iblockstate, EnumFacing.WEST)));
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

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Items.string;
	}

	public Item getItem(World var1, BlockPos var2) {
		return Items.string;
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		boolean flag = ((Boolean) iblockstate.getValue(SUSPENDED)).booleanValue();
		boolean flag1 = !World.doesBlockHaveSolidTopSurface(world, blockpos.down());
		if (flag != flag1) {
			this.dropBlockAsItem(world, blockpos, iblockstate, 0);
			world.setBlockToAir(blockpos);
		}

	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		IBlockState iblockstate = iblockaccess.getBlockState(blockpos);
		boolean flag = ((Boolean) iblockstate.getValue(ATTACHED)).booleanValue();
		boolean flag1 = ((Boolean) iblockstate.getValue(SUSPENDED)).booleanValue();
		if (!flag1) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
		} else if (!flag) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		} else {
			this.setBlockBounds(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
		}

	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		iblockstate = iblockstate.withProperty(SUSPENDED,
				Boolean.valueOf(!World.doesBlockHaveSolidTopSurface(world, blockpos.down())));
		world.setBlockState(blockpos, iblockstate, 3);
		this.notifyHook(world, blockpos, iblockstate);
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.notifyHook(world, blockpos, iblockstate.withProperty(POWERED, Boolean.valueOf(true)));
	}

	public void onBlockHarvested(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer) {
		if (!world.isRemote) {
			if (entityplayer.getCurrentEquippedItem() != null
					&& entityplayer.getCurrentEquippedItem().getItem() == Items.shears) {
				world.setBlockState(blockpos, iblockstate.withProperty(DISARMED, Boolean.valueOf(true)), 4);
			}
		}
	}

	private void notifyHook(World worldIn, BlockPos pos, IBlockState state) {
		for (EnumFacing enumfacing : new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST }) {
			for (int i = 1; i < 42; ++i) {
				BlockPos blockpos = pos.offset(enumfacing, i);
				IBlockState iblockstate = worldIn.getBlockState(blockpos);
				if (iblockstate.getBlock() == Blocks.tripwire_hook) {
					if (iblockstate.getValue(BlockTripWireHook.FACING) == enumfacing.getOpposite()) {
						Blocks.tripwire_hook.func_176260_a(worldIn, blockpos, iblockstate, false, true, i, state);
					}
					break;
				}

				if (iblockstate.getBlock() != Blocks.tripwire) {
					break;
				}
			}
		}

	}

	/**+
	 * Called When an Entity Collided with the Block
	 */
	public void onEntityCollidedWithBlock(World world, BlockPos blockpos, IBlockState iblockstate, Entity var4) {
		if (!world.isRemote) {
			if (!((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
				this.updateState(world, blockpos);
			}
		}
	}

	/**+
	 * Called randomly when setTickRandomly is set to true (used by
	 * e.g. crops to grow, etc.)
	 */
	public void randomTick(World var1, BlockPos var2, IBlockState var3, EaglercraftRandom var4) {
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {
		if (!world.isRemote) {
			if (((Boolean) world.getBlockState(blockpos).getValue(POWERED)).booleanValue()) {
				this.updateState(world, blockpos);
			}
		}
	}

	private void updateState(World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		boolean flag = ((Boolean) iblockstate.getValue(POWERED)).booleanValue();
		boolean flag1 = false;
		List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity) null,
				new AxisAlignedBB((double) pos.getX() + this.minX, (double) pos.getY() + this.minY,
						(double) pos.getZ() + this.minZ, (double) pos.getX() + this.maxX,
						(double) pos.getY() + this.maxY, (double) pos.getZ() + this.maxZ));
		if (!list.isEmpty()) {
			for (int i = 0, l = list.size(); i < l; ++i) {
				if (!list.get(i).doesEntityNotTriggerPressurePlate()) {
					flag1 = true;
					break;
				}
			}
		}

		if (flag1 != flag) {
			iblockstate = iblockstate.withProperty(POWERED, Boolean.valueOf(flag1));
			worldIn.setBlockState(pos, iblockstate, 3);
			this.notifyHook(worldIn, pos, iblockstate);
		}

		if (flag1) {
			worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
		}

	}

	public static boolean isConnectedTo(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing direction) {
		BlockPos blockpos = pos.offset(direction);
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		Block block = iblockstate.getBlock();
		if (block == Blocks.tripwire_hook) {
			EnumFacing enumfacing = direction.getOpposite();
			return iblockstate.getValue(BlockTripWireHook.FACING) == enumfacing;
		} else if (block == Blocks.tripwire) {
			boolean flag = ((Boolean) state.getValue(SUSPENDED)).booleanValue();
			boolean flag1 = ((Boolean) iblockstate.getValue(SUSPENDED)).booleanValue();
			return flag == flag1;
		} else {
			return false;
		}
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(POWERED, Boolean.valueOf((i & 1) > 0))
				.withProperty(SUSPENDED, Boolean.valueOf((i & 2) > 0))
				.withProperty(ATTACHED, Boolean.valueOf((i & 4) > 0))
				.withProperty(DISARMED, Boolean.valueOf((i & 8) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		if (((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
			i |= 1;
		}

		if (((Boolean) iblockstate.getValue(SUSPENDED)).booleanValue()) {
			i |= 2;
		}

		if (((Boolean) iblockstate.getValue(ATTACHED)).booleanValue()) {
			i |= 4;
		}

		if (((Boolean) iblockstate.getValue(DISARMED)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this,
				new IProperty[] { POWERED, SUSPENDED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH });
	}
}