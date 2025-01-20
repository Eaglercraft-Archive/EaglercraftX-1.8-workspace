package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
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
public class BlockVine extends Block {
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool[] ALL_FACES = new PropertyBool[] { UP, NORTH, SOUTH, WEST, EAST };

	public BlockVine() {
		super(Material.vine);
		this.setDefaultState(this.blockState.getBaseState().withProperty(UP, Boolean.valueOf(false))
				.withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false))
				.withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		return iblockstate.withProperty(UP,
				Boolean.valueOf(iblockaccess.getBlockState(blockpos.up()).getBlock().isBlockNormalCube()));
	}

	/**+
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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

	/**+
	 * Whether this Block can be replaced directly by other blocks
	 * (true for e.g. tall grass)
	 */
	public boolean isReplaceable(World var1, BlockPos var2) {
		return true;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		float f = 0.0625F;
		float f1 = 1.0F;
		float f2 = 1.0F;
		float f3 = 1.0F;
		float f4 = 0.0F;
		float f5 = 0.0F;
		float f6 = 0.0F;
		boolean flag = false;
		if (((Boolean) iblockaccess.getBlockState(blockpos).getValue(WEST)).booleanValue()) {
			f4 = Math.max(f4, 0.0625F);
			f1 = 0.0F;
			f2 = 0.0F;
			f5 = 1.0F;
			f3 = 0.0F;
			f6 = 1.0F;
			flag = true;
		}

		if (((Boolean) iblockaccess.getBlockState(blockpos).getValue(EAST)).booleanValue()) {
			f1 = Math.min(f1, 0.9375F);
			f4 = 1.0F;
			f2 = 0.0F;
			f5 = 1.0F;
			f3 = 0.0F;
			f6 = 1.0F;
			flag = true;
		}

		if (((Boolean) iblockaccess.getBlockState(blockpos).getValue(NORTH)).booleanValue()) {
			f6 = Math.max(f6, 0.0625F);
			f3 = 0.0F;
			f1 = 0.0F;
			f4 = 1.0F;
			f2 = 0.0F;
			f5 = 1.0F;
			flag = true;
		}

		if (((Boolean) iblockaccess.getBlockState(blockpos).getValue(SOUTH)).booleanValue()) {
			f3 = Math.min(f3, 0.9375F);
			f6 = 1.0F;
			f1 = 0.0F;
			f4 = 1.0F;
			f2 = 0.0F;
			f5 = 1.0F;
			flag = true;
		}

		if (!flag && this.canPlaceOn(iblockaccess.getBlockState(blockpos.up()).getBlock())) {
			f2 = Math.min(f2, 0.9375F);
			f5 = 1.0F;
			f1 = 0.0F;
			f4 = 1.0F;
			f3 = 0.0F;
			f6 = 1.0F;
		}

		this.setBlockBounds(f1, f2, f3, f4, f5, f6);
	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
		return null;
	}

	/**+
	 * Check whether this Block can be placed on the given side
	 */
	public boolean canPlaceBlockOnSide(World world, BlockPos blockpos, EnumFacing enumfacing) {
		switch (enumfacing) {
		case UP:
			return this.canPlaceOn(world.getBlockState(blockpos.up()).getBlock());
		case NORTH:
		case SOUTH:
		case EAST:
		case WEST:
			return this.canPlaceOn(world.getBlockState(blockpos.offset(enumfacing.getOpposite())).getBlock());
		default:
			return false;
		}
	}

	private boolean canPlaceOn(Block blockIn) {
		return blockIn.isFullCube() && blockIn.blockMaterial.blocksMovement();
	}

	private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state) {
		IBlockState iblockstate = state;

		BlockPos tmp = new BlockPos(0, 0, 0);
		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
		for (int i = 0; i < facings.length; ++i) {
			EnumFacing enumfacing = facings[i];
			PropertyBool propertybool = getPropertyFor(enumfacing);
			if (((Boolean) state.getValue(propertybool)).booleanValue()
					&& !this.canPlaceOn(worldIn.getBlockState(pos.offsetEvenFaster(enumfacing, tmp)).getBlock())) {
				IBlockState iblockstate1 = worldIn.getBlockState(pos.offsetEvenFaster(EnumFacing.UP, tmp));
				if (iblockstate1.getBlock() != this
						|| !((Boolean) iblockstate1.getValue(propertybool)).booleanValue()) {
					state = state.withProperty(propertybool, Boolean.valueOf(false));
				}
			}
		}

		if (getNumGrownFaces(state) == 0) {
			return false;
		} else {
			if (iblockstate != state) {
				worldIn.setBlockState(pos, state, 2);
			}

			return true;
		}
	}

	public int getBlockColor() {
		return ColorizerFoliage.getFoliageColorBasic();
	}

	public int getRenderColor(IBlockState var1) {
		return ColorizerFoliage.getFoliageColorBasic();
	}

	public int colorMultiplier(IBlockAccess iblockaccess, BlockPos blockpos, int var3) {
		return iblockaccess.getBiomeGenForCoords(blockpos).getFoliageColorAtPos(blockpos);
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		if (!world.isRemote && !this.recheckGrownSides(world, blockpos, iblockstate)) {
			this.dropBlockAsItem(world, blockpos, iblockstate, 0);
			world.setBlockToAir(blockpos);
		}

	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		if (!world.isRemote) {
			if (world.rand.nextInt(4) == 0) {
				byte b0 = 4;
				int i = 5;
				boolean flag = false;

				BlockPos tmp = new BlockPos(0, 0, 0);
				label62: for (int j = -b0; j <= b0; ++j) {
					for (int k = -b0; k <= b0; ++k) {
						for (int l = -1; l <= 1; ++l) {
							if (world.getBlockState(blockpos.add(j, l, k, tmp)).getBlock() == this) {
								--i;
								if (i <= 0) {
									flag = true;
									break label62;
								}
							}
						}
					}
				}

				EnumFacing enumfacing1 = EnumFacing.random(random);
				BlockPos blockpos2 = blockpos.up();
				if (enumfacing1 == EnumFacing.UP && blockpos.getY() < 255 && world.isAirBlock(blockpos2)) {
					if (!flag) {
						IBlockState iblockstate3 = iblockstate;

						EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
						for (int j = 0; j < facings.length; ++j) {
							EnumFacing enumfacing3 = facings[j];
							if (random.nextBoolean() || !this.canPlaceOn(
									world.getBlockState(blockpos2.offsetEvenFaster(enumfacing3, tmp)).getBlock())) {
								iblockstate3 = iblockstate3.withProperty(getPropertyFor(enumfacing3),
										Boolean.valueOf(false));
							}
						}

						if (((Boolean) iblockstate3.getValue(NORTH)).booleanValue()
								|| ((Boolean) iblockstate3.getValue(EAST)).booleanValue()
								|| ((Boolean) iblockstate3.getValue(SOUTH)).booleanValue()
								|| ((Boolean) iblockstate3.getValue(WEST)).booleanValue()) {
							world.setBlockState(blockpos2, iblockstate3, 2);
						}

					}
				} else if (enumfacing1.getAxis().isHorizontal()
						&& !((Boolean) iblockstate.getValue(getPropertyFor(enumfacing1))).booleanValue()) {
					if (!flag) {
						BlockPos blockpos4 = blockpos.offsetEvenFaster(enumfacing1, blockpos2);
						Block block1 = world.getBlockState(blockpos4).getBlock();
						if (block1.blockMaterial == Material.air) {
							EnumFacing enumfacing2 = enumfacing1.rotateY();
							EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
							boolean flag1 = ((Boolean) iblockstate.getValue(getPropertyFor(enumfacing2)))
									.booleanValue();
							boolean flag2 = ((Boolean) iblockstate.getValue(getPropertyFor(enumfacing4)))
									.booleanValue();
							BlockPos blockpos5 = blockpos4.offset(enumfacing2);
							BlockPos blockpos1 = blockpos4.offset(enumfacing4);
							if (flag1 && this.canPlaceOn(world.getBlockState(blockpos5).getBlock())) {
								world.setBlockState(blockpos4, this.getDefaultState()
										.withProperty(getPropertyFor(enumfacing2), Boolean.valueOf(true)), 2);
							} else if (flag2 && this.canPlaceOn(world.getBlockState(blockpos1).getBlock())) {
								world.setBlockState(blockpos4, this.getDefaultState()
										.withProperty(getPropertyFor(enumfacing4), Boolean.valueOf(true)), 2);
							} else if (flag1 && world.isAirBlock(blockpos5) && this.canPlaceOn(
									world.getBlockState(blockpos.offsetEvenFaster(enumfacing2, tmp)).getBlock())) {
								world.setBlockState(blockpos5, this.getDefaultState().withProperty(
										getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
							} else if (flag2 && world.isAirBlock(blockpos1) && this.canPlaceOn(
									world.getBlockState(blockpos.offsetEvenFaster(enumfacing4, tmp)).getBlock())) {
								world.setBlockState(blockpos1, this.getDefaultState().withProperty(
										getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
							} else if (this.canPlaceOn(world.getBlockState(blockpos4.up(tmp)).getBlock())) {
								world.setBlockState(blockpos4, this.getDefaultState(), 2);
							}
						} else if (block1.blockMaterial.isOpaque() && block1.isFullCube()) {
							world.setBlockState(blockpos,
									iblockstate.withProperty(getPropertyFor(enumfacing1), Boolean.valueOf(true)), 2);
						}

					}
				} else {
					if (blockpos.getY() > 1) {
						BlockPos blockpos3 = blockpos.down(blockpos2);
						IBlockState iblockstate1 = world.getBlockState(blockpos3);
						Block block = iblockstate1.getBlock();
						EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
						if (block.blockMaterial == Material.air) {
							IBlockState iblockstate2 = iblockstate;

							for (int j = 0; j < facings.length; ++j) {
								if (random.nextBoolean()) {
									iblockstate2 = iblockstate2.withProperty(getPropertyFor(facings[j]),
											Boolean.valueOf(false));
								}
							}

							if (((Boolean) iblockstate2.getValue(NORTH)).booleanValue()
									|| ((Boolean) iblockstate2.getValue(EAST)).booleanValue()
									|| ((Boolean) iblockstate2.getValue(SOUTH)).booleanValue()
									|| ((Boolean) iblockstate2.getValue(WEST)).booleanValue()) {
								world.setBlockState(blockpos3, iblockstate2, 2);
							}
						} else if (block == this) {
							IBlockState iblockstate4 = iblockstate1;

							for (int j = 0; j < facings.length; ++j) {
								PropertyBool propertybool = getPropertyFor(facings[j]);
								if (random.nextBoolean()
										&& ((Boolean) iblockstate.getValue(propertybool)).booleanValue()) {
									iblockstate4 = iblockstate4.withProperty(propertybool, Boolean.valueOf(true));
								}
							}

							if (((Boolean) iblockstate4.getValue(NORTH)).booleanValue()
									|| ((Boolean) iblockstate4.getValue(EAST)).booleanValue()
									|| ((Boolean) iblockstate4.getValue(SOUTH)).booleanValue()
									|| ((Boolean) iblockstate4.getValue(WEST)).booleanValue()) {
								world.setBlockState(blockpos3, iblockstate4, 2);
							}
						}
					}
				}
			}
		}
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing enumfacing, float var4, float var5,
			float var6, int var7, EntityLivingBase var8) {
		IBlockState iblockstate = this.getDefaultState().withProperty(UP, Boolean.valueOf(false))
				.withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false))
				.withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false));
		return enumfacing.getAxis().isHorizontal()
				? iblockstate.withProperty(getPropertyFor(enumfacing.getOpposite()), Boolean.valueOf(true))
				: iblockstate;
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return null;
	}

	/**+
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, BlockPos blockpos, IBlockState iblockstate,
			TileEntity tileentity) {
		if (!world.isRemote && entityplayer.getCurrentEquippedItem() != null
				&& entityplayer.getCurrentEquippedItem().getItem() == Items.shears) {
			entityplayer.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
			spawnAsEntity(world, blockpos, new ItemStack(Blocks.vine, 1, 0));
		} else {
			super.harvestBlock(world, entityplayer, blockpos, iblockstate, tileentity);
		}

	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(SOUTH, Boolean.valueOf((i & 1) > 0))
				.withProperty(WEST, Boolean.valueOf((i & 2) > 0)).withProperty(NORTH, Boolean.valueOf((i & 4) > 0))
				.withProperty(EAST, Boolean.valueOf((i & 8) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		if (((Boolean) iblockstate.getValue(SOUTH)).booleanValue()) {
			i |= 1;
		}

		if (((Boolean) iblockstate.getValue(WEST)).booleanValue()) {
			i |= 2;
		}

		if (((Boolean) iblockstate.getValue(NORTH)).booleanValue()) {
			i |= 4;
		}

		if (((Boolean) iblockstate.getValue(EAST)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { UP, NORTH, EAST, SOUTH, WEST });
	}

	public static PropertyBool getPropertyFor(EnumFacing side) {
		switch (side) {
		case UP:
			return UP;
		case NORTH:
			return NORTH;
		case SOUTH:
			return SOUTH;
		case EAST:
			return EAST;
		case WEST:
			return WEST;
		default:
			throw new IllegalArgumentException(side + " is an invalid choice");
		}
	}

	public static int getNumGrownFaces(IBlockState state) {
		int i = 0;

		for (int j = 0; j < ALL_FACES.length; ++j) {
			if (((Boolean) state.getValue(ALL_FACES[j])).booleanValue()) {
				++i;
			}
		}

		return i;
	}
}