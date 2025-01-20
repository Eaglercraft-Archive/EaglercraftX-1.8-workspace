package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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
public class BlockFarmland extends Block {
	public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);

	protected BlockFarmland() {
		super(Material.ground);
		this.setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, Integer.valueOf(0)));
		this.setTickRandomly(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
		this.setLightOpacity(255);
	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos blockpos, IBlockState var3) {
		return new AxisAlignedBB((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(),
				(double) (blockpos.getX() + 1), (double) (blockpos.getY() + 1), (double) (blockpos.getZ() + 1));
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

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {
		int i = ((Integer) iblockstate.getValue(MOISTURE)).intValue();
		if (!this.hasWater(world, blockpos) && !world.canLightningStrike(blockpos.up())) {
			if (i > 0) {
				world.setBlockState(blockpos, iblockstate.withProperty(MOISTURE, Integer.valueOf(i - 1)), 2);
			} else if (!this.hasCrops(world, blockpos)) {
				world.setBlockState(blockpos, Blocks.dirt.getDefaultState());
			}
		} else if (i < 7) {
			world.setBlockState(blockpos, iblockstate.withProperty(MOISTURE, Integer.valueOf(7)), 2);
		}

	}

	/**+
	 * Block's chance to react to a living entity falling on it.
	 */
	public void onFallenUpon(World world, BlockPos blockpos, Entity entity, float f) {
		if (entity instanceof EntityLivingBase) {
			if (!world.isRemote && world.rand.nextFloat() < f - 0.5F) {
				if (!(entity instanceof EntityPlayer) && !world.getGameRules().getBoolean("mobGriefing")) {
					return;
				}

				world.setBlockState(blockpos, Blocks.dirt.getDefaultState());
			}

			super.onFallenUpon(world, blockpos, entity, f);
		}
	}

	private boolean hasCrops(World worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos.up()).getBlock();
		return block instanceof BlockCrops || block instanceof BlockStem;
	}

	private boolean hasWater(World worldIn, BlockPos pos) {
		for (BlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
			if (worldIn.getBlockState(blockpos$mutableblockpos).getBlock().getMaterial() == Material.water) {
				return true;
			}
		}

		return false;
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block block) {
		super.onNeighborBlockChange(world, blockpos, iblockstate, block);
		if (world.getBlockState(blockpos.up()).getBlock().getMaterial().isSolid()) {
			world.setBlockState(blockpos, Blocks.dirt.getDefaultState());
		}

	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, BlockPos blockpos, EnumFacing enumfacing) {
		switch (enumfacing) {
		case UP:
			return true;
		case NORTH:
		case SOUTH:
		case WEST:
		case EAST:
			Block block = iblockaccess.getBlockState(blockpos).getBlock();
			return !block.isOpaqueCube() && block != Blocks.farmland;
		default:
			return super.shouldSideBeRendered(iblockaccess, blockpos, enumfacing);
		}
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom random, int i) {
		return Blocks.dirt.getItemDropped(
				Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), random, i);
	}

	public Item getItem(World var1, BlockPos var2) {
		return Item.getItemFromBlock(Blocks.dirt);
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(MOISTURE, Integer.valueOf(i & 7));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(MOISTURE)).intValue();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { MOISTURE });
	}
}