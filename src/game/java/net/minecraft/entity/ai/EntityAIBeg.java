package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
public class EntityAIBeg extends EntityAIBase {
	private EntityWolf theWolf;
	private EntityPlayer thePlayer;
	private World worldObject;
	private float minPlayerDistance;
	private int timeoutCounter;

	public EntityAIBeg(EntityWolf wolf, float minDistance) {
		this.theWolf = wolf;
		this.worldObject = wolf.worldObj;
		this.minPlayerDistance = minDistance;
		this.setMutexBits(2);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.theWolf, (double) this.minPlayerDistance);
		return this.thePlayer == null ? false : this.hasPlayerGotBoneInHand(this.thePlayer);
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return !this.thePlayer.isEntityAlive() ? false
				: (this.theWolf.getDistanceSqToEntity(
						this.thePlayer) > (double) (this.minPlayerDistance * this.minPlayerDistance) ? false
								: this.timeoutCounter > 0 && this.hasPlayerGotBoneInHand(this.thePlayer));
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.theWolf.setBegging(true);
		this.timeoutCounter = 40 + this.theWolf.getRNG().nextInt(40);
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.theWolf.setBegging(false);
		this.thePlayer = null;
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		this.theWolf.getLookHelper().setLookPosition(this.thePlayer.posX,
				this.thePlayer.posY + (double) this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0F,
				(float) this.theWolf.getVerticalFaceSpeed());
		--this.timeoutCounter;
	}

	/**+
	 * Gets if the Player has the Bone in the hand.
	 */
	private boolean hasPlayerGotBoneInHand(EntityPlayer player) {
		ItemStack itemstack = player.inventory.getCurrentItem();
		return itemstack == null ? false
				: (!this.theWolf.isTamed() && itemstack.getItem() == Items.bone ? true
						: this.theWolf.isBreedingItem(itemstack));
	}
}