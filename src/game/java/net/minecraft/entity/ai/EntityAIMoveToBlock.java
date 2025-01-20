package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
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
public abstract class EntityAIMoveToBlock extends EntityAIBase {
	private final EntityCreature theEntity;
	private final double movementSpeed;
	protected int runDelay;
	private int timeoutCounter;
	private int field_179490_f;
	/**+
	 * Block to move to
	 */
	protected BlockPos destinationBlock = BlockPos.ORIGIN;
	private boolean isAboveDestination;
	private int searchLength;

	public EntityAIMoveToBlock(EntityCreature creature, double speedIn, int length) {
		this.theEntity = creature;
		this.movementSpeed = speedIn;
		this.searchLength = length;
		this.setMutexBits(5);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.runDelay > 0) {
			--this.runDelay;
			return false;
		} else {
			this.runDelay = 200 + this.theEntity.getRNG().nextInt(200);
			return this.searchForDestination();
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return this.timeoutCounter >= -this.field_179490_f && this.timeoutCounter <= 1200
				&& this.shouldMoveTo(this.theEntity.worldObj, this.destinationBlock);
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.theEntity.getNavigator().tryMoveToXYZ((double) ((float) this.destinationBlock.getX()) + 0.5D,
				(double) (this.destinationBlock.getY() + 1), (double) ((float) this.destinationBlock.getZ()) + 0.5D,
				this.movementSpeed);
		this.timeoutCounter = 0;
		this.field_179490_f = this.theEntity.getRNG().nextInt(this.theEntity.getRNG().nextInt(1200) + 1200) + 1200;
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		if (this.theEntity.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0D) {
			this.isAboveDestination = false;
			++this.timeoutCounter;
			if (this.timeoutCounter % 40 == 0) {
				this.theEntity.getNavigator().tryMoveToXYZ((double) ((float) this.destinationBlock.getX()) + 0.5D,
						(double) (this.destinationBlock.getY() + 1),
						(double) ((float) this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
			}
		} else {
			this.isAboveDestination = true;
			--this.timeoutCounter;
		}

	}

	protected boolean getIsAboveDestination() {
		return this.isAboveDestination;
	}

	/**+
	 * Searches and sets new destination block and returns true if a
	 * suitable block (specified in {@link
	 * net.minecraft.entity.ai.EntityAIMoveToBlock#shouldMoveTo(World,
	 * BlockPos) EntityAIMoveToBlock#shouldMoveTo(World, BlockPos)})
	 * can be found.
	 */
	private boolean searchForDestination() {
		int i = this.searchLength;
		boolean flag = true;
		BlockPos blockpos = new BlockPos(this.theEntity);

		for (int j = 0; j <= 1; j = j > 0 ? -j : 1 - j) {
			for (int k = 0; k < i; ++k) {
				for (int l = 0; l <= k; l = l > 0 ? -l : 1 - l) {
					for (int i1 = l < k && l > -k ? k : 0; i1 <= k; i1 = i1 > 0 ? -i1 : 1 - i1) {
						BlockPos blockpos1 = blockpos.add(l, j - 1, i1);
						if (this.theEntity.isWithinHomeDistanceFromPosition(blockpos1)
								&& this.shouldMoveTo(this.theEntity.worldObj, blockpos1)) {
							this.destinationBlock = blockpos1;
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	protected abstract boolean shouldMoveTo(World var1, BlockPos var2);
}