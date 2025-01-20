package net.minecraft.block;

import com.google.common.base.Predicate;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
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
public class BlockPumpkin extends BlockDirectional {
	private BlockPattern snowmanBasePattern;
	private BlockPattern snowmanPattern;
	private BlockPattern golemBasePattern;
	private BlockPattern golemPattern;
	private static final Predicate<IBlockState> field_181085_Q = new Predicate<IBlockState>() {
		public boolean apply(IBlockState iblockstate) {
			return iblockstate != null
					&& (iblockstate.getBlock() == Blocks.pumpkin || iblockstate.getBlock() == Blocks.lit_pumpkin);
		}
	};

	protected BlockPumpkin() {
		super(Material.gourd, MapColor.adobeColor);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		super.onBlockAdded(world, blockpos, iblockstate);
		this.trySpawnGolem(world, blockpos);
	}

	public boolean canDispenserPlace(World worldIn, BlockPos pos) {
		return this.getSnowmanBasePattern().match(worldIn, pos) != null
				|| this.getGolemBasePattern().match(worldIn, pos) != null;
	}

	private void trySpawnGolem(World worldIn, BlockPos pos) {
		BlockPattern.PatternHelper blockpattern$patternhelper;
		if ((blockpattern$patternhelper = this.getSnowmanPattern().match(worldIn, pos)) != null) {
			for (int i = 0; i < this.getSnowmanPattern().getThumbLength(); ++i) {
				BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(0, i, 0);
				worldIn.setBlockState(blockworldstate.getPos(), Blocks.air.getDefaultState(), 2);
			}

			EntitySnowman entitysnowman = new EntitySnowman(worldIn);
			BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(0, 2, 0).getPos();
			entitysnowman.setLocationAndAngles((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.05D,
					(double) blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
			worldIn.spawnEntityInWorld(entitysnowman);

			for (int j = 0; j < 120; ++j) {
				worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL,
						(double) blockpos1.getX() + worldIn.rand.nextDouble(),
						(double) blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D,
						(double) blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
			}

			for (int i1 = 0; i1 < this.getSnowmanPattern().getThumbLength(); ++i1) {
				BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(0, i1, 0);
				worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.air);
			}
		} else if ((blockpattern$patternhelper = this.getGolemPattern().match(worldIn, pos)) != null) {
			for (int k = 0; k < this.getGolemPattern().getPalmLength(); ++k) {
				for (int l = 0; l < this.getGolemPattern().getThumbLength(); ++l) {
					worldIn.setBlockState(blockpattern$patternhelper.translateOffset(k, l, 0).getPos(),
							Blocks.air.getDefaultState(), 2);
				}
			}

			BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
			EntityIronGolem entityirongolem = new EntityIronGolem(worldIn);
			entityirongolem.setPlayerCreated(true);
			entityirongolem.setLocationAndAngles((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.05D,
					(double) blockpos.getZ() + 0.5D, 0.0F, 0.0F);
			worldIn.spawnEntityInWorld(entityirongolem);

			for (int j1 = 0; j1 < 120; ++j1) {
				worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, (double) blockpos.getX() + worldIn.rand.nextDouble(),
						(double) blockpos.getY() + worldIn.rand.nextDouble() * 3.9D,
						(double) blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
			}

			for (int k1 = 0; k1 < this.getGolemPattern().getPalmLength(); ++k1) {
				for (int l1 = 0; l1 < this.getGolemPattern().getThumbLength(); ++l1) {
					BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(k1, l1, 0);
					worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.air);
				}
			}
		}

	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		return world.getBlockState(blockpos).getBlock().blockMaterial.isReplaceable()
				&& World.doesBlockHaveSolidTopSurface(world, blockpos.down());
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6,
			int var7, EntityLivingBase entitylivingbase) {
		return this.getDefaultState().withProperty(FACING, entitylivingbase.getHorizontalFacing().getOpposite());
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((EnumFacing) iblockstate.getValue(FACING)).getHorizontalIndex();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING });
	}

	protected BlockPattern getSnowmanBasePattern() {
		if (this.snowmanBasePattern == null) {
			this.snowmanBasePattern = FactoryBlockPattern.start().aisle(new String[] { " ", "#", "#" })
					.where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
		}

		return this.snowmanBasePattern;
	}

	protected BlockPattern getSnowmanPattern() {
		if (this.snowmanPattern == null) {
			this.snowmanPattern = FactoryBlockPattern.start().aisle(new String[] { "^", "#", "#" })
					.where('^', BlockWorldState.hasState(field_181085_Q))
					.where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
		}

		return this.snowmanPattern;
	}

	protected BlockPattern getGolemBasePattern() {
		if (this.golemBasePattern == null) {
			this.golemBasePattern = FactoryBlockPattern.start().aisle(new String[] { "~ ~", "###", "~#~" })
					.where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block)))
					.where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
		}

		return this.golemBasePattern;
	}

	protected BlockPattern getGolemPattern() {
		if (this.golemPattern == null) {
			this.golemPattern = FactoryBlockPattern.start().aisle(new String[] { "~^~", "###", "~#~" })
					.where('^', BlockWorldState.hasState(field_181085_Q))
					.where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block)))
					.where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
		}

		return this.golemPattern;
	}
}