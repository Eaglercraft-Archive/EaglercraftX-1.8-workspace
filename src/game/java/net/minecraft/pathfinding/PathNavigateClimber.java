package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
public class PathNavigateClimber extends PathNavigateGround {
	private BlockPos targetPosition;

	public PathNavigateClimber(EntityLiving entityLivingIn, World worldIn) {
		super(entityLivingIn, worldIn);
	}

	/**+
	 * Returns path to given BlockPos
	 */
	public PathEntity getPathToPos(BlockPos blockpos) {
		this.targetPosition = blockpos;
		return super.getPathToPos(blockpos);
	}

	/**+
	 * Returns the path to the given EntityLiving. Args : entity
	 */
	public PathEntity getPathToEntityLiving(Entity entity) {
		this.targetPosition = new BlockPos(entity);
		return super.getPathToEntityLiving(entity);
	}

	/**+
	 * Try to find and set a path to EntityLiving. Returns true if
	 * successful. Args : entity, speed
	 */
	public boolean tryMoveToEntityLiving(Entity entity, double d0) {
		PathEntity pathentity = this.getPathToEntityLiving(entity);
		if (pathentity != null) {
			return this.setPath(pathentity, d0);
		} else {
			this.targetPosition = new BlockPos(entity);
			this.speed = d0;
			return true;
		}
	}

	public void onUpdateNavigation() {
		if (!this.noPath()) {
			super.onUpdateNavigation();
		} else {
			if (this.targetPosition != null) {
				double d0 = (double) (this.theEntity.width * this.theEntity.width);
				if (this.theEntity.getDistanceSqToCenter(this.targetPosition) >= d0
						&& (this.theEntity.posY <= (double) this.targetPosition.getY()
								|| this.theEntity.getDistanceSqToCenter(new BlockPos(this.targetPosition.getX(),
										MathHelper.floor_double(this.theEntity.posY),
										this.targetPosition.getZ())) >= d0)) {
					this.theEntity.getMoveHelper().setMoveTo((double) this.targetPosition.getX(),
							(double) this.targetPosition.getY(), (double) this.targetPosition.getZ(), this.speed);
				} else {
					this.targetPosition = null;
				}
			}

		}
	}
}