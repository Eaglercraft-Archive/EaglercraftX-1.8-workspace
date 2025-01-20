package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.EnumDifficulty;

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
public class EntityAIBreakDoor extends EntityAIDoorInteract {
	private int breakingTime;
	private int previousBreakProgress = -1;

	public EntityAIBreakDoor(EntityLiving entityIn) {
		super(entityIn);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (!super.shouldExecute()) {
			return false;
		} else if (!this.theEntity.worldObj.getGameRules().getBoolean("mobGriefing")) {
			return false;
		} else {
			BlockDoor blockdoor = this.doorBlock;
			return !BlockDoor.isOpen(this.theEntity.worldObj, this.doorPosition);
		}
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		super.startExecuting();
		this.breakingTime = 0;
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		double d0 = this.theEntity.getDistanceSq(this.doorPosition);
		boolean flag;
		if (this.breakingTime <= 240) {
			BlockDoor blockdoor = this.doorBlock;
			if (!BlockDoor.isOpen(this.theEntity.worldObj, this.doorPosition) && d0 < 4.0D) {
				flag = true;
				return flag;
			}
		}

		flag = false;
		return flag;
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		super.resetTask();
		this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, -1);
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		super.updateTask();
		if (this.theEntity.getRNG().nextInt(20) == 0) {
			this.theEntity.worldObj.playAuxSFX(1010, this.doorPosition, 0);
		}

		++this.breakingTime;
		int i = (int) ((float) this.breakingTime / 240.0F * 10.0F);
		if (i != this.previousBreakProgress) {
			this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, i);
			this.previousBreakProgress = i;
		}

		if (this.breakingTime == 240 && this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD) {
			this.theEntity.worldObj.setBlockToAir(this.doorPosition);
			this.theEntity.worldObj.playAuxSFX(1012, this.doorPosition, 0);
			this.theEntity.worldObj.playAuxSFX(2001, this.doorPosition, Block.getIdFromBlock(this.doorBlock));
		}

	}
}