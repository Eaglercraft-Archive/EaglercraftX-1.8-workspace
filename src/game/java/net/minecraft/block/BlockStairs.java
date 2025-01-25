package net.minecraft.block;

import java.util.Arrays;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
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
public class BlockStairs extends Block {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static PropertyEnum<BlockStairs.EnumHalf> HALF;
	public static PropertyEnum<BlockStairs.EnumShape> SHAPE;
	private static final int[][] field_150150_a = new int[][] { { 4, 5 }, { 5, 7 }, { 6, 7 }, { 4, 6 }, { 0, 1 },
			{ 1, 3 }, { 2, 3 }, { 0, 2 } };
	private final Block modelBlock;
	private final IBlockState modelState;
	private boolean hasRaytraced;
	private int rayTracePass;

	protected BlockStairs(IBlockState modelState) {
		super(modelState.getBlock().blockMaterial);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT));
		this.modelBlock = modelState.getBlock();
		this.modelState = modelState;
		this.setHardness(this.modelBlock.blockHardness);
		this.setResistance(this.modelBlock.blockResistance / 3.0F);
		this.setStepSound(this.modelBlock.stepSound);
		this.setLightOpacity(255);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public static void bootstrapStates() {
		HALF = PropertyEnum.<BlockStairs.EnumHalf>create("half", BlockStairs.EnumHalf.class);
		SHAPE = PropertyEnum.<BlockStairs.EnumShape>create("shape", BlockStairs.EnumShape.class);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
		if (this.hasRaytraced) {
			this.setBlockBounds(0.5F * (float) (this.rayTracePass % 2), 0.5F * (float) (this.rayTracePass / 4 % 2),
					0.5F * (float) (this.rayTracePass / 2 % 2), 0.5F + 0.5F * (float) (this.rayTracePass % 2),
					0.5F + 0.5F * (float) (this.rayTracePass / 4 % 2),
					0.5F + 0.5F * (float) (this.rayTracePass / 2 % 2));
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

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
	 * Set the block bounds as the collision bounds for the stairs
	 * at the given position
	 */
	public void setBaseCollisionBounds(IBlockAccess worldIn, BlockPos pos) {
		if (worldIn.getBlockState(pos).getValue(HALF) == BlockStairs.EnumHalf.TOP) {
			this.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

	}

	/**+
	 * Checks if a block is stairs
	 */
	public static boolean isBlockStairs(Block blockIn) {
		return blockIn instanceof BlockStairs;
	}

	/**+
	 * Check whether there is a stair block at the given position
	 * and it has the same properties as the given BlockState
	 */
	public static boolean isSameStair(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();
		/**+
		 * Checks if a block is stairs
		 */
		return isBlockStairs(block) && iblockstate.getValue(HALF) == state.getValue(HALF)
				&& iblockstate.getValue(FACING) == state.getValue(FACING);
	}

	public int func_176307_f(IBlockAccess blockAccess, BlockPos pos) {
		IBlockState iblockstate = blockAccess.getBlockState(pos);
		EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
		BlockStairs.EnumHalf blockstairs$enumhalf = (BlockStairs.EnumHalf) iblockstate.getValue(HALF);
		boolean flag = blockstairs$enumhalf == BlockStairs.EnumHalf.TOP;
		if (enumfacing == EnumFacing.EAST) {
			IBlockState iblockstate1 = blockAccess.getBlockState(pos.east());
			Block block = iblockstate1.getBlock();
			if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate1.getValue(HALF)) {
				EnumFacing enumfacing1 = (EnumFacing) iblockstate1.getValue(FACING);
				if (enumfacing1 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
					return flag ? 1 : 2;
				}

				if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
					return flag ? 2 : 1;
				}
			}
		} else if (enumfacing == EnumFacing.WEST) {
			IBlockState iblockstate2 = blockAccess.getBlockState(pos.west());
			Block block1 = iblockstate2.getBlock();
			if (isBlockStairs(block1) && blockstairs$enumhalf == iblockstate2.getValue(HALF)) {
				EnumFacing enumfacing2 = (EnumFacing) iblockstate2.getValue(FACING);
				if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
					return flag ? 2 : 1;
				}

				if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
					return flag ? 1 : 2;
				}
			}
		} else if (enumfacing == EnumFacing.SOUTH) {
			IBlockState iblockstate3 = blockAccess.getBlockState(pos.south());
			Block block2 = iblockstate3.getBlock();
			if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue(HALF)) {
				EnumFacing enumfacing3 = (EnumFacing) iblockstate3.getValue(FACING);
				if (enumfacing3 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
					return flag ? 2 : 1;
				}

				if (enumfacing3 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
					return flag ? 1 : 2;
				}
			}
		} else if (enumfacing == EnumFacing.NORTH) {
			IBlockState iblockstate4 = blockAccess.getBlockState(pos.north());
			Block block3 = iblockstate4.getBlock();
			if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue(HALF)) {
				EnumFacing enumfacing4 = (EnumFacing) iblockstate4.getValue(FACING);
				if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
					return flag ? 1 : 2;
				}

				if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
					return flag ? 2 : 1;
				}
			}
		}

		return 0;
	}

	public int func_176305_g(IBlockAccess blockAccess, BlockPos pos) {
		IBlockState iblockstate = blockAccess.getBlockState(pos);
		EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
		BlockStairs.EnumHalf blockstairs$enumhalf = (BlockStairs.EnumHalf) iblockstate.getValue(HALF);
		boolean flag = blockstairs$enumhalf == BlockStairs.EnumHalf.TOP;
		if (enumfacing == EnumFacing.EAST) {
			IBlockState iblockstate1 = blockAccess.getBlockState(pos.west());
			Block block = iblockstate1.getBlock();
			if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate1.getValue(HALF)) {
				EnumFacing enumfacing1 = (EnumFacing) iblockstate1.getValue(FACING);
				if (enumfacing1 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
					return flag ? 1 : 2;
				}

				if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
					return flag ? 2 : 1;
				}
			}
		} else if (enumfacing == EnumFacing.WEST) {
			IBlockState iblockstate2 = blockAccess.getBlockState(pos.east());
			Block block1 = iblockstate2.getBlock();
			if (isBlockStairs(block1) && blockstairs$enumhalf == iblockstate2.getValue(HALF)) {
				EnumFacing enumfacing2 = (EnumFacing) iblockstate2.getValue(FACING);
				if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
					return flag ? 2 : 1;
				}

				if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
					return flag ? 1 : 2;
				}
			}
		} else if (enumfacing == EnumFacing.SOUTH) {
			IBlockState iblockstate3 = blockAccess.getBlockState(pos.north());
			Block block2 = iblockstate3.getBlock();
			if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue(HALF)) {
				EnumFacing enumfacing3 = (EnumFacing) iblockstate3.getValue(FACING);
				if (enumfacing3 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
					return flag ? 2 : 1;
				}

				if (enumfacing3 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
					return flag ? 1 : 2;
				}
			}
		} else if (enumfacing == EnumFacing.NORTH) {
			IBlockState iblockstate4 = blockAccess.getBlockState(pos.south());
			Block block3 = iblockstate4.getBlock();
			if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue(HALF)) {
				EnumFacing enumfacing4 = (EnumFacing) iblockstate4.getValue(FACING);
				if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
					return flag ? 1 : 2;
				}

				if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
					return flag ? 2 : 1;
				}
			}
		}

		return 0;
	}

	public boolean func_176306_h(IBlockAccess blockAccess, BlockPos pos) {
		IBlockState iblockstate = blockAccess.getBlockState(pos);
		EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
		BlockStairs.EnumHalf blockstairs$enumhalf = (BlockStairs.EnumHalf) iblockstate.getValue(HALF);
		boolean flag = blockstairs$enumhalf == BlockStairs.EnumHalf.TOP;
		float f = 0.5F;
		float f1 = 1.0F;
		if (flag) {
			f = 0.0F;
			f1 = 0.5F;
		}

		float f2 = 0.0F;
		float f3 = 1.0F;
		float f4 = 0.0F;
		float f5 = 0.5F;
		boolean flag1 = true;
		if (enumfacing == EnumFacing.EAST) {
			f2 = 0.5F;
			f5 = 1.0F;
			IBlockState iblockstate1 = blockAccess.getBlockState(pos.east());
			Block block = iblockstate1.getBlock();
			if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate1.getValue(HALF)) {
				EnumFacing enumfacing1 = (EnumFacing) iblockstate1.getValue(FACING);
				if (enumfacing1 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
					f5 = 0.5F;
					flag1 = false;
				} else if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
					f4 = 0.5F;
					flag1 = false;
				}
			}
		} else if (enumfacing == EnumFacing.WEST) {
			f3 = 0.5F;
			f5 = 1.0F;
			IBlockState iblockstate2 = blockAccess.getBlockState(pos.west());
			Block block1 = iblockstate2.getBlock();
			if (isBlockStairs(block1) && blockstairs$enumhalf == iblockstate2.getValue(HALF)) {
				EnumFacing enumfacing2 = (EnumFacing) iblockstate2.getValue(FACING);
				if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
					f5 = 0.5F;
					flag1 = false;
				} else if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
					f4 = 0.5F;
					flag1 = false;
				}
			}
		} else if (enumfacing == EnumFacing.SOUTH) {
			f4 = 0.5F;
			f5 = 1.0F;
			IBlockState iblockstate3 = blockAccess.getBlockState(pos.south());
			Block block2 = iblockstate3.getBlock();
			if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue(HALF)) {
				EnumFacing enumfacing3 = (EnumFacing) iblockstate3.getValue(FACING);
				if (enumfacing3 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
					f3 = 0.5F;
					flag1 = false;
				} else if (enumfacing3 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
					f2 = 0.5F;
					flag1 = false;
				}
			}
		} else if (enumfacing == EnumFacing.NORTH) {
			IBlockState iblockstate4 = blockAccess.getBlockState(pos.north());
			Block block3 = iblockstate4.getBlock();
			if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue(HALF)) {
				EnumFacing enumfacing4 = (EnumFacing) iblockstate4.getValue(FACING);
				if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
					f3 = 0.5F;
					flag1 = false;
				} else if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
					f2 = 0.5F;
					flag1 = false;
				}
			}
		}

		this.setBlockBounds(f2, f, f4, f3, f1, f5);
		return flag1;
	}

	public boolean func_176304_i(IBlockAccess blockAccess, BlockPos pos) {
		IBlockState iblockstate = blockAccess.getBlockState(pos);
		EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
		BlockStairs.EnumHalf blockstairs$enumhalf = (BlockStairs.EnumHalf) iblockstate.getValue(HALF);
		boolean flag = blockstairs$enumhalf == BlockStairs.EnumHalf.TOP;
		float f = 0.5F;
		float f1 = 1.0F;
		if (flag) {
			f = 0.0F;
			f1 = 0.5F;
		}

		float f2 = 0.0F;
		float f3 = 0.5F;
		float f4 = 0.5F;
		float f5 = 1.0F;
		boolean flag1 = false;
		if (enumfacing == EnumFacing.EAST) {
			IBlockState iblockstate1 = blockAccess.getBlockState(pos.west());
			Block block = iblockstate1.getBlock();
			if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate1.getValue(HALF)) {
				EnumFacing enumfacing1 = (EnumFacing) iblockstate1.getValue(FACING);
				if (enumfacing1 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
					f4 = 0.0F;
					f5 = 0.5F;
					flag1 = true;
				} else if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
					f4 = 0.5F;
					f5 = 1.0F;
					flag1 = true;
				}
			}
		} else if (enumfacing == EnumFacing.WEST) {
			IBlockState iblockstate2 = blockAccess.getBlockState(pos.east());
			Block block1 = iblockstate2.getBlock();
			if (isBlockStairs(block1) && blockstairs$enumhalf == iblockstate2.getValue(HALF)) {
				f2 = 0.5F;
				f3 = 1.0F;
				EnumFacing enumfacing2 = (EnumFacing) iblockstate2.getValue(FACING);
				if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
					f4 = 0.0F;
					f5 = 0.5F;
					flag1 = true;
				} else if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
					f4 = 0.5F;
					f5 = 1.0F;
					flag1 = true;
				}
			}
		} else if (enumfacing == EnumFacing.SOUTH) {
			IBlockState iblockstate3 = blockAccess.getBlockState(pos.north());
			Block block2 = iblockstate3.getBlock();
			if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue(HALF)) {
				f4 = 0.0F;
				f5 = 0.5F;
				EnumFacing enumfacing3 = (EnumFacing) iblockstate3.getValue(FACING);
				if (enumfacing3 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
					flag1 = true;
				} else if (enumfacing3 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
					f2 = 0.5F;
					f3 = 1.0F;
					flag1 = true;
				}
			}
		} else if (enumfacing == EnumFacing.NORTH) {
			IBlockState iblockstate4 = blockAccess.getBlockState(pos.south());
			Block block3 = iblockstate4.getBlock();
			if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue(HALF)) {
				EnumFacing enumfacing4 = (EnumFacing) iblockstate4.getValue(FACING);
				if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
					flag1 = true;
				} else if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
					f2 = 0.5F;
					f3 = 1.0F;
					flag1 = true;
				}
			}
		}

		if (flag1) {
			this.setBlockBounds(f2, f, f4, f3, f1, f5);
		}

		return flag1;
	}

	/**+
	 * Add all collision boxes of this Block to the list that
	 * intersect with the given mask.
	 */
	public void addCollisionBoxesToList(World world, BlockPos blockpos, IBlockState iblockstate,
			AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, Entity entity) {
		this.setBaseCollisionBounds(world, blockpos);
		super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		boolean flag = this.func_176306_h(world, blockpos);
		super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		if (flag && this.func_176304_i(world, blockpos)) {
			super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		}

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		this.modelBlock.randomDisplayTick(world, blockpos, iblockstate, random);
	}

	public void onBlockClicked(World world, BlockPos blockpos, EntityPlayer entityplayer) {
		this.modelBlock.onBlockClicked(world, blockpos, entityplayer);
	}

	/**+
	 * Called when a player destroys this Block
	 */
	public void onBlockDestroyedByPlayer(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.modelBlock.onBlockDestroyedByPlayer(world, blockpos, iblockstate);
	}

	public int getMixedBrightnessForBlock(IBlockAccess iblockaccess, BlockPos blockpos) {
		return this.modelBlock.getMixedBrightnessForBlock(iblockaccess, blockpos);
	}

	/**+
	 * Returns how much this block can resist explosions from the
	 * passed in entity.
	 */
	public float getExplosionResistance(Entity entity) {
		return this.modelBlock.getExplosionResistance(entity);
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return this.modelBlock.getBlockLayer();
	}

	/**+
	 * How many world ticks before ticking
	 */
	public int tickRate(World world) {
		return this.modelBlock.tickRate(world);
	}

	public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockpos) {
		return this.modelBlock.getSelectedBoundingBox(world, blockpos);
	}

	public Vec3 modifyAcceleration(World world, BlockPos blockpos, Entity entity, Vec3 vec3) {
		return this.modelBlock.modifyAcceleration(world, blockpos, entity, vec3);
	}

	/**+
	 * Returns if this block is collidable (only used by Fire).
	 * Args: x, y, z
	 */
	public boolean isCollidable() {
		return this.modelBlock.isCollidable();
	}

	public boolean canCollideCheck(IBlockState iblockstate, boolean flag) {
		return this.modelBlock.canCollideCheck(iblockstate, flag);
	}

	public boolean canPlaceBlockAt(World world, BlockPos blockpos) {
		return this.modelBlock.canPlaceBlockAt(world, blockpos);
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState var3) {
		this.onNeighborBlockChange(world, blockpos, this.modelState, Blocks.air);
		this.modelBlock.onBlockAdded(world, blockpos, this.modelState);
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState var3) {
		this.modelBlock.breakBlock(world, blockpos, this.modelState);
	}

	/**+
	 * Triggered whenever an entity collides with this block (enters
	 * into the block)
	 */
	public void onEntityCollidedWithBlock(World world, BlockPos blockpos, Entity entity) {
		this.modelBlock.onEntityCollidedWithBlock(world, blockpos, entity);
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		this.modelBlock.updateTick(world, blockpos, iblockstate, random);
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState var3, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		if (!world.isRemote && MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
				.getBoolean("clickToSit") && entityplayer.getHeldItem() == null) {
			EntityArrow arrow = new EntityArrow(world, blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D);
			arrow.isChair = true;
			world.spawnEntityInWorld(arrow);
			entityplayer.mountEntity(arrow);
			return true;
		}
		return this.modelBlock.onBlockActivated(world, blockpos, this.modelState, entityplayer, EnumFacing.DOWN, 0.0F,
				0.0F, 0.0F);
	}

	/**+
	 * Called when this Block is destroyed by an Explosion
	 */
	public void onBlockDestroyedByExplosion(World world, BlockPos blockpos, Explosion explosion) {
		this.modelBlock.onBlockDestroyedByExplosion(world, blockpos, explosion);
	}

	/**+
	 * Get the MapColor for this Block and the given BlockState
	 */
	public MapColor getMapColor(IBlockState var1) {
		return this.modelBlock.getMapColor(this.modelState);
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World world, BlockPos blockpos, EnumFacing enumfacing, float f, float f1, float f2,
			int i, EntityLivingBase entitylivingbase) {
		IBlockState iblockstate = super.onBlockPlaced(world, blockpos, enumfacing, f, f1, f2, i, entitylivingbase);
		iblockstate = iblockstate.withProperty(FACING, entitylivingbase.getHorizontalFacing()).withProperty(SHAPE,
				BlockStairs.EnumShape.STRAIGHT);
		return enumfacing != EnumFacing.DOWN && (enumfacing == EnumFacing.UP || (double) f1 <= 0.5D)
				? iblockstate.withProperty(HALF, BlockStairs.EnumHalf.BOTTOM)
				: iblockstate.withProperty(HALF, BlockStairs.EnumHalf.TOP);
	}

	/**+
	 * Ray traces through the blocks collision from start vector to
	 * end vector returning a ray trace hit.
	 */
	public MovingObjectPosition collisionRayTrace(World world, BlockPos blockpos, Vec3 vec3, Vec3 vec31) {
		MovingObjectPosition[] amovingobjectposition = new MovingObjectPosition[8];
		IBlockState iblockstate = world.getBlockState(blockpos);
		int i = ((EnumFacing) iblockstate.getValue(FACING)).getHorizontalIndex();
		boolean flag = iblockstate.getValue(HALF) == BlockStairs.EnumHalf.TOP;
		int[] aint = field_150150_a[i + (flag ? 4 : 0)];
		this.hasRaytraced = true;

		for (int j = 0; j < 8; ++j) {
			this.rayTracePass = j;
			if (Arrays.binarySearch(aint, j) < 0) {
				amovingobjectposition[j] = super.collisionRayTrace(world, blockpos, vec3, vec31);
			}
		}

		for (int l = 0; l < aint.length; ++l) {
			amovingobjectposition[aint[l]] = null;
		}

		MovingObjectPosition movingobjectposition1 = null;
		double d1 = 0.0D;

		for (int l = 0; l < amovingobjectposition.length; ++l) {
			MovingObjectPosition movingobjectposition = amovingobjectposition[l];
			if (movingobjectposition != null) {
				double d0 = movingobjectposition.hitVec.squareDistanceTo(vec31);
				if (d0 > d1) {
					movingobjectposition1 = movingobjectposition;
					d1 = d0;
				}
			}
		}

		return movingobjectposition1;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		IBlockState iblockstate = this.getDefaultState().withProperty(HALF,
				(i & 4) > 0 ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM);
		iblockstate = iblockstate.withProperty(FACING, EnumFacing.getFront(5 - (i & 3)));
		return iblockstate;
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		if (iblockstate.getValue(HALF) == BlockStairs.EnumHalf.TOP) {
			i |= 4;
		}

		i = i | 5 - ((EnumFacing) iblockstate.getValue(FACING)).getIndex();
		return i;
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		if (this.func_176306_h(iblockaccess, blockpos)) {
			switch (this.func_176305_g(iblockaccess, blockpos)) {
			case 0:
				iblockstate = iblockstate.withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
				break;
			case 1:
				iblockstate = iblockstate.withProperty(SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
				break;
			case 2:
				iblockstate = iblockstate.withProperty(SHAPE, BlockStairs.EnumShape.INNER_LEFT);
			}
		} else {
			switch (this.func_176307_f(iblockaccess, blockpos)) {
			case 0:
				iblockstate = iblockstate.withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
				break;
			case 1:
				iblockstate = iblockstate.withProperty(SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);
				break;
			case 2:
				iblockstate = iblockstate.withProperty(SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
			}
		}

		return iblockstate;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, HALF, SHAPE });
	}

	public static enum EnumHalf implements IStringSerializable {
		TOP("top"), BOTTOM("bottom");

		private final String name;

		private EnumHalf(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}

	public static enum EnumShape implements IStringSerializable {
		STRAIGHT("straight"), INNER_LEFT("inner_left"), INNER_RIGHT("inner_right"), OUTER_LEFT("outer_left"),
		OUTER_RIGHT("outer_right");

		private final String name;

		private EnumShape(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}

	public boolean alfheim$useNeighborBrightness(final IBlockState blockState, final EnumFacing facing,
			final IBlockAccess blockAccess, final BlockPos blockPos) {
		return facing == (blockState.getValue(HALF) == EnumHalf.TOP ? EnumFacing.DOWN : EnumFacing.UP)
				|| facing == blockState.getValue(FACING).getOpposite();
	}
}