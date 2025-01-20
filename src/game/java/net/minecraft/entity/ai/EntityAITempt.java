package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;

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
public class EntityAITempt extends EntityAIBase {
	private EntityCreature temptedEntity;
	private double speed;
	private double targetX;
	private double targetY;
	private double targetZ;
	private double pitch;
	private double yaw;
	private EntityPlayer temptingPlayer;
	private int delayTemptCounter;
	private boolean isRunning;
	private Item temptItem;
	private boolean scaredByPlayerMovement;
	private boolean avoidWater;

	public EntityAITempt(EntityCreature temptedEntityIn, double speedIn, Item temptItemIn,
			boolean scaredByPlayerMovementIn) {
		this.temptedEntity = temptedEntityIn;
		this.speed = speedIn;
		this.temptItem = temptItemIn;
		this.scaredByPlayerMovement = scaredByPlayerMovementIn;
		this.setMutexBits(3);
		if (!(temptedEntityIn.getNavigator() instanceof PathNavigateGround)) {
			throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
		}
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.delayTemptCounter > 0) {
			--this.delayTemptCounter;
			return false;
		} else {
			this.temptingPlayer = this.temptedEntity.worldObj.getClosestPlayerToEntity(this.temptedEntity, 10.0D);
			if (this.temptingPlayer == null) {
				return false;
			} else {
				ItemStack itemstack = this.temptingPlayer.getCurrentEquippedItem();
				return itemstack == null ? false : itemstack.getItem() == this.temptItem;
			}
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		if (this.scaredByPlayerMovement) {
			if (this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 36.0D) {
				if (this.temptingPlayer.getDistanceSq(this.targetX, this.targetY,
						this.targetZ) > 0.010000000000000002D) {
					return false;
				}

				if (Math.abs((double) this.temptingPlayer.rotationPitch - this.pitch) > 5.0D
						|| Math.abs((double) this.temptingPlayer.rotationYaw - this.yaw) > 5.0D) {
					return false;
				}
			} else {
				this.targetX = this.temptingPlayer.posX;
				this.targetY = this.temptingPlayer.posY;
				this.targetZ = this.temptingPlayer.posZ;
			}

			this.pitch = (double) this.temptingPlayer.rotationPitch;
			this.yaw = (double) this.temptingPlayer.rotationYaw;
		}

		return this.shouldExecute();
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.targetX = this.temptingPlayer.posX;
		this.targetY = this.temptingPlayer.posY;
		this.targetZ = this.temptingPlayer.posZ;
		this.isRunning = true;
		this.avoidWater = ((PathNavigateGround) this.temptedEntity.getNavigator()).getAvoidsWater();
		((PathNavigateGround) this.temptedEntity.getNavigator()).setAvoidsWater(false);
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.temptingPlayer = null;
		this.temptedEntity.getNavigator().clearPathEntity();
		this.delayTemptCounter = 100;
		this.isRunning = false;
		((PathNavigateGround) this.temptedEntity.getNavigator()).setAvoidsWater(this.avoidWater);
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, 30.0F,
				(float) this.temptedEntity.getVerticalFaceSpeed());
		if (this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 6.25D) {
			this.temptedEntity.getNavigator().clearPathEntity();
		} else {
			this.temptedEntity.getNavigator().tryMoveToEntityLiving(this.temptingPlayer, this.speed);
		}

	}

	/**+
	 * @see #isRunning
	 */
	public boolean isRunning() {
		return this.isRunning;
	}
}