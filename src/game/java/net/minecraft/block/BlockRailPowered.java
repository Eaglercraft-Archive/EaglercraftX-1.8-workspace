package net.minecraft.block;

import com.google.common.base.Predicate;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
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
public class BlockRailPowered extends BlockRailBase {
	public static PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE;
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	protected BlockRailPowered() {
		super(true);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH)
						.withProperty(POWERED, Boolean.valueOf(false)));
	}

	public static void bootstrapStates() {
		SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class,
				new Predicate<BlockRailBase.EnumRailDirection>() {
					public boolean apply(BlockRailBase.EnumRailDirection blockrailbase$enumraildirection) {
						return blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_EAST
								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_WEST
								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.SOUTH_EAST
								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.SOUTH_WEST;
					}
				});
	}

	protected boolean func_176566_a(World worldIn, BlockPos pos, IBlockState state, boolean parFlag, int parInt1) {
		if (parInt1 >= 8) {
			return false;
		} else {
			int i = pos.getX();
			int j = pos.getY();
			int k = pos.getZ();
			boolean flag = true;
			BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection) state
					.getValue(SHAPE);
			switch (blockrailbase$enumraildirection) {
			case NORTH_SOUTH:
				if (parFlag) {
					++k;
				} else {
					--k;
				}
				break;
			case EAST_WEST:
				if (parFlag) {
					--i;
				} else {
					++i;
				}
				break;
			case ASCENDING_EAST:
				if (parFlag) {
					--i;
				} else {
					++i;
					++j;
					flag = false;
				}

				blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
				break;
			case ASCENDING_WEST:
				if (parFlag) {
					--i;
					++j;
					flag = false;
				} else {
					++i;
				}

				blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
				break;
			case ASCENDING_NORTH:
				if (parFlag) {
					++k;
				} else {
					--k;
					++j;
					flag = false;
				}

				blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
				break;
			case ASCENDING_SOUTH:
				if (parFlag) {
					++k;
					++j;
					flag = false;
				} else {
					--k;
				}

				blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
			}

			return this.func_176567_a(worldIn, new BlockPos(i, j, k), parFlag, parInt1, blockrailbase$enumraildirection)
					? true
					: flag && this.func_176567_a(worldIn, new BlockPos(i, j - 1, k), parFlag, parInt1,
							blockrailbase$enumraildirection);
		}
	}

	protected boolean func_176567_a(World worldIn, BlockPos distance, boolean parFlag, int parInt1,
			BlockRailBase.EnumRailDirection parEnumRailDirection) {
		IBlockState iblockstate = worldIn.getBlockState(distance);
		if (iblockstate.getBlock() != this) {
			return false;
		} else {
			BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection) iblockstate
					.getValue(SHAPE);
			return parEnumRailDirection != BlockRailBase.EnumRailDirection.EAST_WEST
					|| blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_SOUTH
							&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_NORTH
							&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_SOUTH
									? (parEnumRailDirection != BlockRailBase.EnumRailDirection.NORTH_SOUTH
											|| blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.EAST_WEST
													&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_EAST
													&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_WEST
															? (((Boolean) iblockstate.getValue(POWERED)).booleanValue()
																	? (worldIn.isBlockPowered(distance) ? true
																			: this.func_176566_a(worldIn, distance,
																					iblockstate, parFlag, parInt1 + 1))
																	: false)
															: false)
									: false;
		}
	}

	protected void onNeighborChangedInternal(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		boolean flag = ((Boolean) iblockstate.getValue(POWERED)).booleanValue();
		boolean flag1 = world.isBlockPowered(blockpos) || this.func_176566_a(world, blockpos, iblockstate, true, 0)
				|| this.func_176566_a(world, blockpos, iblockstate, false, 0);
		if (flag1 != flag) {
			world.setBlockState(blockpos, iblockstate.withProperty(POWERED, Boolean.valueOf(flag1)), 3);
			world.notifyNeighborsOfStateChange(blockpos.down(), this);
			if (((BlockRailBase.EnumRailDirection) iblockstate.getValue(SHAPE)).isAscending()) {
				world.notifyNeighborsOfStateChange(blockpos.up(), this);
			}
		}

	}

	public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
		return SHAPE;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(i & 7))
				.withProperty(POWERED, Boolean.valueOf((i & 8) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((BlockRailBase.EnumRailDirection) iblockstate.getValue(SHAPE)).getMetadata();
		if (((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { SHAPE, POWERED });
	}
}