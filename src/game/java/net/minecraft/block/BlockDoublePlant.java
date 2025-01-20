package net.minecraft.block;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

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
public class BlockDoublePlant extends BlockBush implements IGrowable {
	public static PropertyEnum<BlockDoublePlant.EnumPlantType> VARIANT;
	public static PropertyEnum<BlockDoublePlant.EnumBlockHalf> HALF;
	public static final PropertyEnum<EnumFacing> field_181084_N = BlockDirectional.FACING;

	public BlockDoublePlant() {
		super(Material.vine);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(VARIANT, BlockDoublePlant.EnumPlantType.SUNFLOWER)
						.withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER)
						.withProperty(field_181084_N, EnumFacing.NORTH));
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		this.setUnlocalizedName("doublePlant");
	}

	public static void bootstrapStates() {
		VARIANT = PropertyEnum.<BlockDoublePlant.EnumPlantType>create("variant", BlockDoublePlant.EnumPlantType.class);
		HALF = PropertyEnum.<BlockDoublePlant.EnumBlockHalf>create("half", BlockDoublePlant.EnumBlockHalf.class);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public BlockDoublePlant.EnumPlantType getVariant(IBlockAccess worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		if (iblockstate.getBlock() == this) {
			iblockstate = this.getActualState(iblockstate, worldIn, pos);
			return (BlockDoublePlant.EnumPlantType) iblockstate.getValue(VARIANT);
		} else {
			return BlockDoublePlant.EnumPlantType.FERN;
		}
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		return super.canPlaceBlockAt(world, blockpos) && world.isAirBlock(blockpos.up());
	}

	/**+
	 * Whether this Block can be replaced directly by other blocks
	 * (true for e.g. tall grass)
	 */
	public boolean isReplaceable(World world, BlockPos blockpos) {
		IBlockState iblockstate = world.getBlockState(blockpos);
		if (iblockstate.getBlock() != this) {
			return true;
		} else {
			BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType) this
					.getActualState(iblockstate, world, blockpos).getValue(VARIANT);
			return blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.FERN
					|| blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.GRASS;
		}
	}

	protected void checkAndDropBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		if (!this.canBlockStay(world, blockpos, iblockstate)) {
			boolean flag = iblockstate.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER;
			BlockPos blockpos1 = flag ? blockpos : blockpos.up();
			BlockPos blockpos2 = flag ? blockpos.down() : blockpos;
			Object object = flag ? this : world.getBlockState(blockpos1).getBlock();
			Object object1 = flag ? world.getBlockState(blockpos2).getBlock() : this;
			if (object == this) {
				world.setBlockState(blockpos1, Blocks.air.getDefaultState(), 2);
			}

			if (object1 == this) {
				world.setBlockState(blockpos2, Blocks.air.getDefaultState(), 3);
				if (!flag) {
					this.dropBlockAsItem(world, blockpos2, iblockstate, 0);
				}
			}

		}
	}

	public boolean canBlockStay(World world, BlockPos blockpos, IBlockState iblockstate) {
		if (iblockstate.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
			return world.getBlockState(blockpos.down()).getBlock() == this;
		} else {
			IBlockState iblockstate1 = world.getBlockState(blockpos.up());
			return iblockstate1.getBlock() == this && super.canBlockStay(world, blockpos, iblockstate1);
		}
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom random, int var3) {
		if (iblockstate.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
			return null;
		} else {
			BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType) iblockstate
					.getValue(VARIANT);
			return blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.FERN ? null
					: (blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.GRASS
							? (random.nextInt(8) == 0 ? Items.wheat_seeds : null)
							: Item.getItemFromBlock(this));
		}
	}

	/**+
	 * Gets the metadata of the item this Block can drop. This
	 * method is called when the block gets destroyed. It returns
	 * the metadata of the dropped item based on the old metadata of
	 * the block.
	 */
	public int damageDropped(IBlockState iblockstate) {
		return iblockstate.getValue(HALF) != BlockDoublePlant.EnumBlockHalf.UPPER
				&& iblockstate.getValue(VARIANT) != BlockDoublePlant.EnumPlantType.GRASS
						? ((BlockDoublePlant.EnumPlantType) iblockstate.getValue(VARIANT)).getMeta()
						: 0;
	}

	public int colorMultiplier(IBlockAccess iblockaccess, BlockPos blockpos, int var3) {
		BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = this.getVariant(iblockaccess, blockpos);
		return blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS
				&& blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN ? 16777215
						: BiomeColorHelper.getGrassColorAtPos(iblockaccess, blockpos);
	}

	public void placeAt(World worldIn, BlockPos lowerPos, BlockDoublePlant.EnumPlantType variant, int flags) {
		worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER)
				.withProperty(VARIANT, variant), flags);
		worldIn.setBlockState(lowerPos.up(),
				this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), flags);
	}

	/**+
	 * Called by ItemBlocks after a block is set in the world, to
	 * allow post-place logic
	 */
	public void onBlockPlacedBy(World world, BlockPos blockpos, IBlockState var3, EntityLivingBase var4,
			ItemStack var5) {
		world.setBlockState(blockpos.up(),
				this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, BlockPos blockpos, IBlockState iblockstate,
			TileEntity tileentity) {
		if (world.isRemote || entityplayer.getCurrentEquippedItem() == null
				|| entityplayer.getCurrentEquippedItem().getItem() != Items.shears
				|| iblockstate.getValue(HALF) != BlockDoublePlant.EnumBlockHalf.LOWER
				|| !this.onHarvest(world, blockpos, iblockstate, entityplayer)) {
			super.harvestBlock(world, entityplayer, blockpos, iblockstate, tileentity);
		}
	}

	public void onBlockHarvested(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer) {
		if (iblockstate.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
			if (world.getBlockState(blockpos.down()).getBlock() == this) {
				if (!entityplayer.capabilities.isCreativeMode) {
					IBlockState iblockstate1 = world.getBlockState(blockpos.down());
					BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType) iblockstate1
							.getValue(VARIANT);
					if (blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN
							&& blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS) {
						world.destroyBlock(blockpos.down(), true);
					} else if (!world.isRemote) {
						if (entityplayer.getCurrentEquippedItem() != null
								&& entityplayer.getCurrentEquippedItem().getItem() == Items.shears) {
							this.onHarvest(world, blockpos, iblockstate1, entityplayer);
							world.setBlockToAir(blockpos.down());
						} else {
							world.destroyBlock(blockpos.down(), true);
						}
					} else {
						world.setBlockToAir(blockpos.down());
					}
				} else {
					world.setBlockToAir(blockpos.down());
				}
			}
		} else if (entityplayer.capabilities.isCreativeMode && world.getBlockState(blockpos.up()).getBlock() == this) {
			world.setBlockState(blockpos.up(), Blocks.air.getDefaultState(), 2);
		}

		super.onBlockHarvested(world, blockpos, iblockstate, entityplayer);
	}

	private boolean onHarvest(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType) state
				.getValue(VARIANT);
		if (blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN
				&& blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS) {
			return false;
		} else {
			player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
			int i = (blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.GRASS
					? BlockTallGrass.EnumType.GRASS
					: BlockTallGrass.EnumType.FERN).getMeta();
			spawnAsEntity(worldIn, pos, new ItemStack(Blocks.tallgrass, 2, i));
			return true;
		}
	}

	/**+
	 * returns a list of blocks with the same ID, but different meta
	 * (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(Item item, CreativeTabs var2, List<ItemStack> list) {
		BlockDoublePlant.EnumPlantType[] types = BlockDoublePlant.EnumPlantType.META_LOOKUP;
		for (int i = 0; i < types.length; ++i) {
			list.add(new ItemStack(item, 1, types[i].getMeta()));
		}

	}

	public int getDamageValue(World world, BlockPos blockpos) {
		return this.getVariant(world, blockpos).getMeta();
	}

	/**+
	 * Whether this IGrowable can grow
	 */
	public boolean canGrow(World world, BlockPos blockpos, IBlockState var3, boolean var4) {
		BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = this.getVariant(world, blockpos);
		return blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS
				&& blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN;
	}

	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {
		return true;
	}

	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState var4) {
		spawnAsEntity(world, blockpos, new ItemStack(this, 1, this.getVariant(world, blockpos).getMeta()));
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return (i & 8) > 0 ? this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER)
				: this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(VARIANT,
						BlockDoublePlant.EnumPlantType.byMetadata(i & 7));
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		if (iblockstate.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
			IBlockState iblockstate1 = iblockaccess.getBlockState(blockpos.down());
			if (iblockstate1.getBlock() == this) {
				iblockstate = iblockstate.withProperty(VARIANT, iblockstate1.getValue(VARIANT));
			}
		}

		return iblockstate;
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return iblockstate.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER
				? 8 | ((EnumFacing) iblockstate.getValue(field_181084_N)).getHorizontalIndex()
				: ((BlockDoublePlant.EnumPlantType) iblockstate.getValue(VARIANT)).getMeta();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { HALF, VARIANT, field_181084_N });
	}

	/**+
	 * Get the OffsetType for this Block. Determines if the model is
	 * rendered slightly offset.
	 */
	public Block.EnumOffsetType getOffsetType() {
		return Block.EnumOffsetType.XZ;
	}

	public static enum EnumBlockHalf implements IStringSerializable {
		UPPER, LOWER;

		public String toString() {
			return this.getName();
		}

		public String getName() {
			return this == UPPER ? "upper" : "lower";
		}
	}

	public static enum EnumPlantType implements IStringSerializable {
		SUNFLOWER(0, "sunflower"), SYRINGA(1, "syringa"), GRASS(2, "double_grass", "grass"),
		FERN(3, "double_fern", "fern"), ROSE(4, "double_rose", "rose"), PAEONIA(5, "paeonia");

		private static final BlockDoublePlant.EnumPlantType[] META_LOOKUP = new BlockDoublePlant.EnumPlantType[6];
		private final int meta;
		private final String name;
		private final String unlocalizedName;

		private EnumPlantType(int meta, String name) {
			this(meta, name, name);
		}

		private EnumPlantType(int meta, String name, String unlocalizedName) {
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		public int getMeta() {
			return this.meta;
		}

		public String toString() {
			return this.name;
		}

		public static BlockDoublePlant.EnumPlantType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName() {
			return this.name;
		}

		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		static {
			BlockDoublePlant.EnumPlantType[] types = BlockDoublePlant.EnumPlantType.values();
			for (int i = 0; i < types.length; ++i) {
				META_LOOKUP[types[i].getMeta()] = types[i];
			}

		}
	}
}