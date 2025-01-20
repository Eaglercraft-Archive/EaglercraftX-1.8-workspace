package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

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
public class WalkNodeProcessor extends NodeProcessor {
	private boolean canEnterDoors;
	private boolean canBreakDoors;
	private boolean avoidsWater;
	private boolean canSwim;
	private boolean shouldAvoidWater;

	public void initProcessor(IBlockAccess iblockaccess, Entity entity) {
		super.initProcessor(iblockaccess, entity);
		this.shouldAvoidWater = this.avoidsWater;
	}

	/**+
	 * This method is called when all nodes have been processed and
	 * PathEntity is created.\n {@link
	 * net.minecraft.world.pathfinder.WalkNodeProcessor
	 * WalkNodeProcessor} uses this to change its field {@link
	 * net.minecraft.world.pathfinder.WalkNodeProcessor#avoidsWater
	 * avoidsWater}
	 */
	public void postProcess() {
		super.postProcess();
		this.avoidsWater = this.shouldAvoidWater;
	}

	/**+
	 * Returns given entity's position as PathPoint
	 */
	public PathPoint getPathPointTo(Entity entity) {
		int i;
		if (this.canSwim && entity.isInWater()) {
			i = (int) entity.getEntityBoundingBox().minY;
			BlockPos blockpos$mutableblockpos = new BlockPos(MathHelper.floor_double(entity.posX), i,
					MathHelper.floor_double(entity.posZ));

			for (Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos)
					.getBlock(); block == Blocks.flowing_water
							|| block == Blocks.water; block = this.blockaccess.getBlockState(blockpos$mutableblockpos)
									.getBlock()) {
				++i;
				blockpos$mutableblockpos.func_181079_c(MathHelper.floor_double(entity.posX), i,
						MathHelper.floor_double(entity.posZ));
			}

			this.avoidsWater = false;
		} else {
			i = MathHelper.floor_double(entity.getEntityBoundingBox().minY + 0.5D);
		}

		return this.openPoint(MathHelper.floor_double(entity.getEntityBoundingBox().minX), i,
				MathHelper.floor_double(entity.getEntityBoundingBox().minZ));
	}

	/**+
	 * Returns PathPoint for given coordinates
	 */
	public PathPoint getPathPointToCoords(Entity entity, double d0, double d1, double d2) {
		return this.openPoint(MathHelper.floor_double(d0 - (double) (entity.width / 2.0F)), MathHelper.floor_double(d1),
				MathHelper.floor_double(d2 - (double) (entity.width / 2.0F)));
	}

	public int findPathOptions(PathPoint[] apathpoint, Entity entity, PathPoint pathpoint, PathPoint pathpoint1,
			float f) {
		int i = 0;
		byte b0 = 0;
		if (this.getVerticalOffset(entity, pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord) == 1) {
			b0 = 1;
		}

		PathPoint pathpoint2 = this.getSafePoint(entity, pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord + 1, b0);
		PathPoint pathpoint3 = this.getSafePoint(entity, pathpoint.xCoord - 1, pathpoint.yCoord, pathpoint.zCoord, b0);
		PathPoint pathpoint4 = this.getSafePoint(entity, pathpoint.xCoord + 1, pathpoint.yCoord, pathpoint.zCoord, b0);
		PathPoint pathpoint5 = this.getSafePoint(entity, pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord - 1, b0);
		if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(pathpoint1) < f) {
			apathpoint[i++] = pathpoint2;
		}

		if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(pathpoint1) < f) {
			apathpoint[i++] = pathpoint3;
		}

		if (pathpoint4 != null && !pathpoint4.visited && pathpoint4.distanceTo(pathpoint1) < f) {
			apathpoint[i++] = pathpoint4;
		}

		if (pathpoint5 != null && !pathpoint5.visited && pathpoint5.distanceTo(pathpoint1) < f) {
			apathpoint[i++] = pathpoint5;
		}

		return i;
	}

	/**+
	 * Returns a point that the entity can safely move to
	 */
	private PathPoint getSafePoint(Entity entityIn, int x, int y, int z, int parInt4) {
		PathPoint pathpoint = null;
		int i = this.getVerticalOffset(entityIn, x, y, z);
		if (i == 2) {
			return this.openPoint(x, y, z);
		} else {
			if (i == 1) {
				pathpoint = this.openPoint(x, y, z);
			}

			if (pathpoint == null && parInt4 > 0 && i != -3 && i != -4
					&& this.getVerticalOffset(entityIn, x, y + parInt4, z) == 1) {
				pathpoint = this.openPoint(x, y + parInt4, z);
				y += parInt4;
			}

			if (pathpoint != null) {
				int j = 0;

				int k;
				for (k = 0; y > 0; pathpoint = this.openPoint(x, y, z)) {
					k = this.getVerticalOffset(entityIn, x, y - 1, z);
					if (this.avoidsWater && k == -1) {
						return null;
					}

					if (k != 1) {
						break;
					}

					if (j++ >= entityIn.getMaxFallHeight()) {
						return null;
					}

					--y;
					if (y <= 0) {
						return null;
					}
				}

				if (k == -2) {
					return null;
				}
			}

			return pathpoint;
		}
	}

	/**+
	 * Checks if an entity collides with blocks at a
	 * position.\nReturns 1 if clear, 0 for colliding with any solid
	 * block, -1 for water(if avoids water),\n-2 for lava, -3 for
	 * fence and wall, -4 for closed trapdoor, 2 if otherwise clear
	 * except for open trapdoor or water(if not avoiding)
	 */
	private int getVerticalOffset(Entity entityIn, int x, int y, int z) {
		return func_176170_a(this.blockaccess, entityIn, x, y, z, this.entitySizeX, this.entitySizeY, this.entitySizeZ,
				this.avoidsWater, this.canBreakDoors, this.canEnterDoors);
	}

	public static int func_176170_a(IBlockAccess blockaccessIn, Entity entityIn, int x, int y, int z, int sizeX,
			int sizeY, int sizeZ, boolean avoidWater, boolean breakDoors, boolean enterDoors) {
		boolean flag = false;
		BlockPos blockpos = new BlockPos(entityIn);
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int i = x; i < x + sizeX; ++i) {
			for (int j = y; j < y + sizeY; ++j) {
				for (int k = z; k < z + sizeZ; ++k) {
					blockpos$mutableblockpos.func_181079_c(i, j, k);
					Block block = blockaccessIn.getBlockState(blockpos$mutableblockpos).getBlock();
					if (block.getMaterial() != Material.air) {
						if (block != Blocks.trapdoor && block != Blocks.iron_trapdoor) {
							if (block != Blocks.flowing_water && block != Blocks.water) {
								if (!enterDoors && block instanceof BlockDoor && block.getMaterial() == Material.wood) {
									return 0;
								}
							} else {
								if (avoidWater) {
									return -1;
								}

								flag = true;
							}
						} else {
							flag = true;
						}

						if (entityIn.worldObj.getBlockState(blockpos$mutableblockpos)
								.getBlock() instanceof BlockRailBase) {
							if (!(entityIn.worldObj.getBlockState(blockpos).getBlock() instanceof BlockRailBase)
									&& !(entityIn.worldObj.getBlockState(blockpos.down())
											.getBlock() instanceof BlockRailBase)) {
								return -3;
							}
						} else if (!block.isPassable(blockaccessIn, blockpos$mutableblockpos) && (!breakDoors
								|| !(block instanceof BlockDoor) || block.getMaterial() != Material.wood)) {
							if (block instanceof BlockFence || block instanceof BlockFenceGate
									|| block instanceof BlockWall) {
								return -3;
							}

							if (block == Blocks.trapdoor || block == Blocks.iron_trapdoor) {
								return -4;
							}

							Material material = block.getMaterial();
							if (material != Material.lava) {
								return 0;
							}

							if (!entityIn.isInLava()) {
								return -2;
							}
						}
					}
				}
			}
		}

		return flag ? 2 : 1;
	}

	public void setEnterDoors(boolean canEnterDoorsIn) {
		this.canEnterDoors = canEnterDoorsIn;
	}

	public void setBreakDoors(boolean canBreakDoorsIn) {
		this.canBreakDoors = canBreakDoorsIn;
	}

	public void setAvoidsWater(boolean avoidsWaterIn) {
		this.avoidsWater = avoidsWaterIn;
	}

	public void setCanSwim(boolean canSwimIn) {
		this.canSwim = canSwimIn;
	}

	public boolean getEnterDoors() {
		return this.canEnterDoors;
	}

	public boolean getCanSwim() {
		return this.canSwim;
	}

	public boolean getAvoidsWater() {
		return this.avoidsWater;
	}
}