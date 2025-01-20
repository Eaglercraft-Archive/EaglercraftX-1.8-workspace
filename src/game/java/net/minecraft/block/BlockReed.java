package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
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
public class BlockReed extends Block {
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);

	protected BlockReed() {
		super(Material.plants);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		float f = 0.375F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
		this.setTickRandomly(true);
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {
		BlockPos tmp = new BlockPos(0, 0, 0);
		if (world.getBlockState(blockpos.offsetEvenFaster(EnumFacing.DOWN, tmp)).getBlock() == Blocks.reeds
				|| this.checkForDrop(world, blockpos, iblockstate)) {
			if (world.isAirBlock(blockpos.offsetEvenFaster(EnumFacing.UP, tmp))) {
				int i;
				--tmp.y;
				for (i = 1; world.getBlockState(tmp.offsetEvenFaster(EnumFacing.DOWN, tmp)).getBlock() == this; ++i) {
					;
				}

				if (i < 3) {
					int j = ((Integer) iblockstate.getValue(AGE)).intValue();
					if (j == 15) {
						world.setBlockState(blockpos.offsetEvenFaster(EnumFacing.UP, tmp), this.getDefaultState());
						world.setBlockState(blockpos, iblockstate.withProperty(AGE, Integer.valueOf(0)), 4);
					} else {
						world.setBlockState(blockpos, iblockstate.withProperty(AGE, Integer.valueOf(j + 1)), 4);
					}
				}
			}

		}
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		BlockPos down = blockpos.down();
		Block block = world.getBlockState(down).getBlock();
		if (block == this) {
			return true;
		} else if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.sand) {
			return false;
		} else {
			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
			for (int i = 0; i < facings.length; ++i) {
				EnumFacing enumfacing = facings[i];
				down = blockpos.offsetEvenFaster(enumfacing, down);
				--down.y;
				if (world.getBlockState(down).getBlock().getMaterial() == Material.water) {
					return true;
				}
			}

			return false;
		}
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		this.checkForDrop(world, blockpos, iblockstate);
	}

	protected final boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
		if (this.canBlockStay(worldIn, pos)) {
			return true;
		} else {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
			return false;
		}
	}

	public boolean canBlockStay(World worldIn, BlockPos pos) {
		return this.canPlaceBlockAt(worldIn, pos);
	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
		return null;
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Items.reeds;
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

	public Item getItem(World var1, BlockPos var2) {
		return Items.reeds;
	}

	public int colorMultiplier(IBlockAccess iblockaccess, BlockPos blockpos, int var3) {
		return iblockaccess.getBiomeGenForCoords(blockpos).getGrassColorAtPos(blockpos);
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(AGE)).intValue();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { AGE });
	}
}