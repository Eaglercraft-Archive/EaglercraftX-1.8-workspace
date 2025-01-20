package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;

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
public abstract class EntityAIDoorInteract extends EntityAIBase {
	protected EntityLiving theEntity;
	protected BlockPos doorPosition = BlockPos.ORIGIN;
	protected BlockDoor doorBlock;
	boolean hasStoppedDoorInteraction;
	float entityPositionX;
	float entityPositionZ;

	public EntityAIDoorInteract(EntityLiving entityIn) {
		this.theEntity = entityIn;
		if (!(entityIn.getNavigator() instanceof PathNavigateGround)) {
			throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
		}
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (!this.theEntity.isCollidedHorizontally) {
			return false;
		} else {
			PathNavigateGround pathnavigateground = (PathNavigateGround) this.theEntity.getNavigator();
			PathEntity pathentity = pathnavigateground.getPath();
			if (pathentity != null && !pathentity.isFinished() && pathnavigateground.getEnterDoors()) {
				for (int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2,
						pathentity.getCurrentPathLength()); ++i) {
					PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
					this.doorPosition = new BlockPos(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord);
					if (this.theEntity.getDistanceSq((double) this.doorPosition.getX(), this.theEntity.posY,
							(double) this.doorPosition.getZ()) <= 2.25D) {
						this.doorBlock = this.getBlockDoor(this.doorPosition);
						if (this.doorBlock != null) {
							return true;
						}
					}
				}

				this.doorPosition = (new BlockPos(this.theEntity)).up();
				this.doorBlock = this.getBlockDoor(this.doorPosition);
				return this.doorBlock != null;
			} else {
				return false;
			}
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return !this.hasStoppedDoorInteraction;
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.hasStoppedDoorInteraction = false;
		this.entityPositionX = (float) ((double) ((float) this.doorPosition.getX() + 0.5F) - this.theEntity.posX);
		this.entityPositionZ = (float) ((double) ((float) this.doorPosition.getZ() + 0.5F) - this.theEntity.posZ);
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		float f = (float) ((double) ((float) this.doorPosition.getX() + 0.5F) - this.theEntity.posX);
		float f1 = (float) ((double) ((float) this.doorPosition.getZ() + 0.5F) - this.theEntity.posZ);
		float f2 = this.entityPositionX * f + this.entityPositionZ * f1;
		if (f2 < 0.0F) {
			this.hasStoppedDoorInteraction = true;
		}

	}

	private BlockDoor getBlockDoor(BlockPos pos) {
		Block block = this.theEntity.worldObj.getBlockState(pos).getBlock();
		return block instanceof BlockDoor && block.getMaterial() == Material.wood ? (BlockDoor) block : null;
	}
}