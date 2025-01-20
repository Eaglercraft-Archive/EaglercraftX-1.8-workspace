package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
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
public abstract class BlockLiquid extends Block {
	public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);

	protected BlockLiquid(Material materialIn) {
		super(materialIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.setTickRandomly(true);
	}

	public boolean isPassable(IBlockAccess var1, BlockPos var2) {
		return this.blockMaterial != Material.lava;
	}

	public int colorMultiplier(IBlockAccess iblockaccess, BlockPos blockpos, int var3) {
		return this.blockMaterial == Material.water ? BiomeColorHelper.getWaterColorAtPos(iblockaccess, blockpos)
				: 16777215;
	}

	/**+
	 * Returns the percentage of the liquid block that is air, based
	 * on the given flow decay of the liquid
	 */
	public static float getLiquidHeightPercent(int meta) {
		if (meta >= 8) {
			meta = 0;
		}

		return (float) (meta + 1) / 9.0F;
	}

	protected int getLevel(IBlockAccess worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getBlock().getMaterial() == this.blockMaterial
				? ((Integer) worldIn.getBlockState(pos).getValue(LEVEL)).intValue()
				: -1;
	}

	protected int getEffectiveFlowDecay(IBlockAccess worldIn, BlockPos pos) {
		int i = this.getLevel(worldIn, pos);
		return i >= 8 ? 0 : i;
	}

	public boolean isFullCube() {
		return false;
	}

	/**+
	 * Used to determine ambient occlusion and culling when
	 * rebuilding chunks for render
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	public boolean canCollideCheck(IBlockState iblockstate, boolean flag) {
		return flag && ((Integer) iblockstate.getValue(LEVEL)).intValue() == 0;
	}

	/**+
	 * Whether this Block is solid on the given Side
	 */
	public boolean isBlockSolid(IBlockAccess iblockaccess, BlockPos blockpos, EnumFacing enumfacing) {
		Material material = iblockaccess.getBlockState(blockpos).getBlock().getMaterial();
		return material == this.blockMaterial ? false
				: (enumfacing == EnumFacing.UP ? true
						: (material == Material.ice ? false : super.isBlockSolid(iblockaccess, blockpos, enumfacing)));
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, BlockPos blockpos, EnumFacing enumfacing) {
		return iblockaccess.getBlockState(blockpos).getBlock().getMaterial() == this.blockMaterial ? false
				: (enumfacing == EnumFacing.UP ? true : super.shouldSideBeRendered(iblockaccess, blockpos, enumfacing));
	}

	public boolean func_176364_g(IBlockAccess blockAccess, BlockPos pos) {
		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				IBlockState iblockstate = blockAccess.getBlockState(pos.add(i, 0, j));
				Block block = iblockstate.getBlock();
				Material material = block.getMaterial();
				if (material != this.blockMaterial && !block.isFullBlock()) {
					return true;
				}
			}
		}

		return false;
	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
		return null;
	}

	/**+
	 * The type of render function called. 3 for standard block
	 * models, 2 for TESR's, 1 for liquids, -1 is no render
	 */
	public int getRenderType() {
		return 1;
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

	protected Vec3 getFlowVector(IBlockAccess worldIn, BlockPos pos) {
		Vec3 vec3 = new Vec3(0.0D, 0.0D, 0.0D);
		int i = this.getEffectiveFlowDecay(worldIn, pos);

		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
		for (int m = 0; m < facings.length; ++m) {
			EnumFacing enumfacing = facings[m];
			BlockPos blockpos = pos.offset(enumfacing);
			int j = this.getEffectiveFlowDecay(worldIn, blockpos);
			if (j < 0) {
				if (!worldIn.getBlockState(blockpos).getBlock().getMaterial().blocksMovement()) {
					j = this.getEffectiveFlowDecay(worldIn, blockpos.down());
					if (j >= 0) {
						int k = j - (i - 8);
						vec3 = vec3.addVector((double) ((blockpos.getX() - pos.getX()) * k),
								(double) ((blockpos.getY() - pos.getY()) * k),
								(double) ((blockpos.getZ() - pos.getZ()) * k));
					}
				}
			} else if (j >= 0) {
				int l = j - i;
				vec3 = vec3.addVector((double) ((blockpos.getX() - pos.getX()) * l),
						(double) ((blockpos.getY() - pos.getY()) * l), (double) ((blockpos.getZ() - pos.getZ()) * l));
			}
		}

		if (((Integer) worldIn.getBlockState(pos).getValue(LEVEL)).intValue() >= 8) {
			for (int j = 0; j < facings.length; ++j) {
				EnumFacing enumfacing1 = facings[j];
				BlockPos blockpos1 = pos.offset(enumfacing1);
				if (this.isBlockSolid(worldIn, blockpos1, enumfacing1)
						|| this.isBlockSolid(worldIn, blockpos1.up(), enumfacing1)) {
					vec3 = vec3.normalize().addVector(0.0D, -6.0D, 0.0D);
					break;
				}
			}
		}

		return vec3.normalize();
	}

	public Vec3 modifyAcceleration(World world, BlockPos blockpos, Entity var3, Vec3 vec3) {
		return vec3.add(this.getFlowVector(world, blockpos));
	}

	/**+
	 * How many world ticks before ticking
	 */
	public int tickRate(World world) {
		return this.blockMaterial == Material.water ? 5
				: (this.blockMaterial == Material.lava ? (world.provider.getHasNoSky() ? 10 : 30) : 0);
	}

	public int getMixedBrightnessForBlock(IBlockAccess iblockaccess, BlockPos blockpos) {
		int i = iblockaccess.getCombinedLight(blockpos, 0);
		int j = iblockaccess.getCombinedLight(blockpos.up(), 0);
		int k = i & 255;
		int l = j & 255;
		int i1 = i >> 16 & 255;
		int j1 = j >> 16 & 255;
		return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return this.blockMaterial == Material.water
				? (DeferredStateManager.isRenderingRealisticWater() ? EnumWorldBlockLayer.REALISTIC_WATER
						: EnumWorldBlockLayer.TRANSLUCENT)
				: EnumWorldBlockLayer.SOLID;
	}

	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		double d0 = (double) blockpos.getX();
		double d1 = (double) blockpos.getY();
		double d2 = (double) blockpos.getZ();
		if (this.blockMaterial == Material.water) {
			int i = ((Integer) iblockstate.getValue(LEVEL)).intValue();
			if (i > 0 && i < 8) {
				if (random.nextInt(64) == 0) {
					world.playSound(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D, "liquid.water", random.nextFloat() * 0.25F + 0.75F,
							random.nextFloat() * 1.0F + 0.5F, false);
				}
			} else if (random.nextInt(10) == 0) {
				world.spawnParticle(EnumParticleTypes.SUSPENDED, d0 + (double) random.nextFloat(),
						d1 + (double) random.nextFloat(), d2 + (double) random.nextFloat(), 0.0D, 0.0D, 0.0D,
						new int[0]);
			}
		}

		if (this.blockMaterial == Material.lava
				&& world.getBlockState(blockpos.up()).getBlock().getMaterial() == Material.air
				&& !world.getBlockState(blockpos.up()).getBlock().isOpaqueCube()) {
			if (random.nextInt(100) == 0) {
				double d8 = d0 + (double) random.nextFloat();
				double d4 = d1 + this.maxY;
				double d6 = d2 + (double) random.nextFloat();
				world.spawnParticle(EnumParticleTypes.LAVA, d8, d4, d6, 0.0D, 0.0D, 0.0D, new int[0]);
				world.playSound(d8, d4, d6, "liquid.lavapop", 0.2F + random.nextFloat() * 0.2F,
						0.9F + random.nextFloat() * 0.15F, false);
			}

			if (random.nextInt(200) == 0) {
				world.playSound(d0, d1, d2, "liquid.lava", 0.2F + random.nextFloat() * 0.2F,
						0.9F + random.nextFloat() * 0.15F, false);
			}
		}

		if (random.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(world, blockpos.down())) {
			Material material = world.getBlockState(blockpos.down(2)).getBlock().getMaterial();
			if (!material.blocksMovement() && !material.isLiquid()) {
				double d3 = d0 + (double) random.nextFloat();
				double d5 = d1 - 1.05D;
				double d7 = d2 + (double) random.nextFloat();
				if (this.blockMaterial == Material.water) {
					world.spawnParticle(EnumParticleTypes.DRIP_WATER, d3, d5, d7, 0.0D, 0.0D, 0.0D, new int[0]);
				} else {
					world.spawnParticle(EnumParticleTypes.DRIP_LAVA, d3, d5, d7, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
		}

	}

	public static double getFlowDirection(IBlockAccess worldIn, BlockPos pos, Material materialIn) {
		Vec3 vec3 = getFlowingBlock(materialIn).getFlowVector(worldIn, pos);
		return vec3.xCoord == 0.0D && vec3.zCoord == 0.0D ? -1000.0D
				: MathHelper.func_181159_b(vec3.zCoord, vec3.xCoord) - 1.5707963267948966D;
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.checkForMixing(world, blockpos, iblockstate);
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		this.checkForMixing(world, blockpos, iblockstate);
	}

	public boolean checkForMixing(World worldIn, BlockPos pos, IBlockState state) {
		if (this.blockMaterial == Material.lava) {
			boolean flag = false;

			EnumFacing[] facings = EnumFacing._VALUES;
			for (int j = 0; j < facings.length; ++j) {
				EnumFacing enumfacing = facings[j];
				if (enumfacing != EnumFacing.DOWN
						&& worldIn.getBlockState(pos.offset(enumfacing)).getBlock().getMaterial() == Material.water) {
					flag = true;
					break;
				}
			}

			if (flag) {
				Integer integer = (Integer) state.getValue(LEVEL);
				if (integer.intValue() == 0) {
					worldIn.setBlockState(pos, Blocks.obsidian.getDefaultState());
					this.triggerMixEffects(worldIn, pos);
					return true;
				}

				if (integer.intValue() <= 4) {
					worldIn.setBlockState(pos, Blocks.cobblestone.getDefaultState());
					this.triggerMixEffects(worldIn, pos);
					return true;
				}
			}
		}

		return false;
	}

	protected void triggerMixEffects(World worldIn, BlockPos pos) {
		double d0 = (double) pos.getX();
		double d1 = (double) pos.getY();
		double d2 = (double) pos.getZ();
		worldIn.playSoundEffect(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D, "random.fizz", 0.5F,
				2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

		for (int i = 0; i < 8; ++i) {
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(),
					0.0D, 0.0D, 0.0D, new int[0]);
		}

	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(LEVEL)).intValue();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { LEVEL });
	}

	public static BlockDynamicLiquid getFlowingBlock(Material materialIn) {
		if (materialIn == Material.water) {
			return Blocks.flowing_water;
		} else if (materialIn == Material.lava) {
			return Blocks.flowing_lava;
		} else {
			throw new IllegalArgumentException("Invalid material");
		}
	}

	public static BlockStaticLiquid getStaticBlock(Material materialIn) {
		if (materialIn == Material.water) {
			return Blocks.water;
		} else if (materialIn == Material.lava) {
			return Blocks.lava;
		} else {
			throw new IllegalArgumentException("Invalid material");
		}
	}
}