package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
public class SwimNodeProcessor extends NodeProcessor {
	public void initProcessor(IBlockAccess iblockaccess, Entity entity) {
		super.initProcessor(iblockaccess, entity);
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
	}

	/**+
	 * Returns given entity's position as PathPoint
	 */
	public PathPoint getPathPointTo(Entity entity) {
		return this.openPoint(MathHelper.floor_double(entity.getEntityBoundingBox().minX),
				MathHelper.floor_double(entity.getEntityBoundingBox().minY + 0.5D),
				MathHelper.floor_double(entity.getEntityBoundingBox().minZ));
	}

	/**+
	 * Returns PathPoint for given coordinates
	 */
	public PathPoint getPathPointToCoords(Entity entity, double d0, double d1, double d2) {
		return this.openPoint(MathHelper.floor_double(d0 - (double) (entity.width / 2.0F)),
				MathHelper.floor_double(d1 + 0.5D), MathHelper.floor_double(d2 - (double) (entity.width / 2.0F)));
	}

	public int findPathOptions(PathPoint[] apathpoint, Entity entity, PathPoint pathpoint, PathPoint pathpoint1,
			float f) {
		int i = 0;

		EnumFacing[] facings = EnumFacing._VALUES;
		for (int j = 0; j < facings.length; ++j) {
			EnumFacing enumfacing = facings[j];
			PathPoint pathpoint2 = this.getSafePoint(entity, pathpoint.xCoord + enumfacing.getFrontOffsetX(),
					pathpoint.yCoord + enumfacing.getFrontOffsetY(), pathpoint.zCoord + enumfacing.getFrontOffsetZ());
			if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(pathpoint1) < f) {
				apathpoint[i++] = pathpoint2;
			}
		}

		return i;
	}

	/**+
	 * Returns a point that the entity can safely move to
	 */
	private PathPoint getSafePoint(Entity entityIn, int x, int y, int z) {
		int i = this.func_176186_b(entityIn, x, y, z);
		return i == -1 ? this.openPoint(x, y, z) : null;
	}

	private int func_176186_b(Entity entityIn, int x, int y, int z) {
		BlockPos blockpos$mutableblockpos = new BlockPos();

		for (int i = x; i < x + this.entitySizeX; ++i) {
			for (int j = y; j < y + this.entitySizeY; ++j) {
				for (int k = z; k < z + this.entitySizeZ; ++k) {
					Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos.func_181079_c(i, j, k))
							.getBlock();
					if (block.getMaterial() != Material.water) {
						return 0;
					}
				}
			}
		}

		return -1;
	}
}