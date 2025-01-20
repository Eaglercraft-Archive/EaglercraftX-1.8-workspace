package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumDifficulty;
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
public class BlockSkull extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool NODROP = PropertyBool.create("nodrop");
	private static final Predicate<BlockWorldState> IS_WITHER_SKELETON = new Predicate<BlockWorldState>() {
		public boolean apply(BlockWorldState blockworldstate) {
			return blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getBlock() == Blocks.skull
					&& blockworldstate.getTileEntity() instanceof TileEntitySkull
					&& ((TileEntitySkull) blockworldstate.getTileEntity()).getSkullType() == 1;
		}
	};
	private BlockPattern witherBasePattern;
	private BlockPattern witherPattern;

	protected BlockSkull() {
		super(Material.circuits);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(NODROP,
				Boolean.valueOf(false)));
		this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
	}

	/**+
	 * Gets the localized name of this block. Used for the
	 * statistics page.
	 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal("tile.skull.skeleton.name");
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

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos blockpos) {
		switch ((EnumFacing) iblockaccess.getBlockState(blockpos).getValue(FACING)) {
		case UP:
		default:
			this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
			break;
		case NORTH:
			this.setBlockBounds(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
			break;
		case SOUTH:
			this.setBlockBounds(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
			break;
		case WEST:
			this.setBlockBounds(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
			break;
		case EAST:
			this.setBlockBounds(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
		}

	}

	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.setBlockBoundsBasedOnState(world, blockpos);
		return super.getCollisionBoundingBox(world, blockpos, iblockstate);
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6,
			int var7, EntityLivingBase entitylivingbase) {
		return this.getDefaultState().withProperty(FACING, entitylivingbase.getHorizontalFacing()).withProperty(NODROP,
				Boolean.valueOf(false));
	}

	/**+
	 * Returns a new instance of a block's tile entity class. Called
	 * on placing the block.
	 */
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntitySkull();
	}

	public Item getItem(World var1, BlockPos var2) {
		return Items.skull;
	}

	public int getDamageValue(World world, BlockPos blockpos) {
		TileEntity tileentity = world.getTileEntity(blockpos);
		return tileentity instanceof TileEntitySkull ? ((TileEntitySkull) tileentity).getSkullType()
				: super.getDamageValue(world, blockpos);
	}

	/**+
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
	}

	public void onBlockHarvested(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer) {
		if (entityplayer.capabilities.isCreativeMode) {
			iblockstate = iblockstate.withProperty(NODROP, Boolean.valueOf(true));
			world.setBlockState(blockpos, iblockstate, 4);
		}

		super.onBlockHarvested(world, blockpos, iblockstate, entityplayer);
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		if (!world.isRemote) {
			if (!((Boolean) iblockstate.getValue(NODROP)).booleanValue()) {
				TileEntity tileentity = world.getTileEntity(blockpos);
				if (tileentity instanceof TileEntitySkull) {
					TileEntitySkull tileentityskull = (TileEntitySkull) tileentity;
					ItemStack itemstack = new ItemStack(Items.skull, 1, this.getDamageValue(world, blockpos));
					if (tileentityskull.getSkullType() == 3 && tileentityskull.getPlayerProfile() != null) {
						itemstack.setTagCompound(new NBTTagCompound());
						NBTTagCompound nbttagcompound = new NBTTagCompound();
						NBTUtil.writeGameProfile(nbttagcompound, tileentityskull.getPlayerProfile());
						itemstack.getTagCompound().setTag("SkullOwner", nbttagcompound);
					}

					spawnAsEntity(world, blockpos, itemstack);
				}
			}

			super.breakBlock(world, blockpos, iblockstate);
		}
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Items.skull;
	}

	public boolean canDispenserPlace(World worldIn, BlockPos pos, ItemStack stack) {
		return stack.getMetadata() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL
				&& !worldIn.isRemote ? this.getWitherBasePattern().match(worldIn, pos) != null : false;
	}

	public void checkWitherSpawn(World worldIn, BlockPos pos, TileEntitySkull te) {
		if (te.getSkullType() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL
				&& !worldIn.isRemote) {
			BlockPattern blockpattern = this.getWitherPattern();
			BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(worldIn, pos);
			if (blockpattern$patternhelper != null) {
				for (int i = 0; i < 3; ++i) {
					BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, 0, 0);
					worldIn.setBlockState(blockworldstate.getPos(),
							blockworldstate.getBlockState().withProperty(NODROP, Boolean.valueOf(true)), 2);
				}

				for (int j = 0; j < blockpattern.getPalmLength(); ++j) {
					for (int k = 0; k < blockpattern.getThumbLength(); ++k) {
						BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(j, k, 0);
						worldIn.setBlockState(blockworldstate1.getPos(), Blocks.air.getDefaultState(), 2);
					}
				}

				BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 0, 0).getPos();
				EntityWither entitywither = new EntityWither(worldIn);
				BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
				entitywither.setLocationAndAngles((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.55D,
						(double) blockpos1.getZ() + 0.5D,
						blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F);
				entitywither.renderYawOffset = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X
						? 0.0F
						: 90.0F;
				entitywither.func_82206_m();

				List<EntityPlayer> list = worldIn.getEntitiesWithinAABB(EntityPlayer.class,
						entitywither.getEntityBoundingBox().expand(50.0D, 50.0D, 50.0D));
				for (int j = 0, l = list.size(); j < l; ++j) {
					list.get(j).triggerAchievement(AchievementList.spawnWither);
				}

				worldIn.spawnEntityInWorld(entitywither);

				for (int l = 0; l < 120; ++l) {
					worldIn.spawnParticle(EnumParticleTypes.SNOWBALL,
							(double) blockpos.getX() + worldIn.rand.nextDouble(),
							(double) (blockpos.getY() - 2) + worldIn.rand.nextDouble() * 3.9D,
							(double) blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
				}

				for (int i1 = 0; i1 < blockpattern.getPalmLength(); ++i1) {
					for (int j1 = 0; j1 < blockpattern.getThumbLength(); ++j1) {
						BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(i1, j1, 0);
						worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.air);
					}
				}

			}
		}
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(i & 7)).withProperty(NODROP,
				Boolean.valueOf((i & 8) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((EnumFacing) iblockstate.getValue(FACING)).getIndex();
		if (((Boolean) iblockstate.getValue(NODROP)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, NODROP });
	}

	protected BlockPattern getWitherBasePattern() {
		if (this.witherBasePattern == null) {
			this.witherBasePattern = FactoryBlockPattern.start().aisle(new String[] { "   ", "###", "~#~" })
					.where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand)))
					.where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
		}

		return this.witherBasePattern;
	}

	protected BlockPattern getWitherPattern() {
		if (this.witherPattern == null) {
			this.witherPattern = FactoryBlockPattern.start().aisle(new String[] { "^^^", "###", "~#~" })
					.where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand)))
					.where('^', IS_WITHER_SKELETON)
					.where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
		}

		return this.witherPattern;
	}
}