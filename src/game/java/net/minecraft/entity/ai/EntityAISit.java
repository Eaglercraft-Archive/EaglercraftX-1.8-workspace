package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

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
public class EntityAISit extends EntityAIBase {
	private EntityTameable theEntity;
	private boolean isSitting;

	public EntityAISit(EntityTameable entityIn) {
		this.theEntity = entityIn;
		this.setMutexBits(5);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (!this.theEntity.isTamed()) {
			return false;
		} else if (this.theEntity.isInWater()) {
			return false;
		} else if (!this.theEntity.onGround) {
			return false;
		} else {
			EntityLivingBase entitylivingbase = this.theEntity.getOwner();
			return entitylivingbase == null ? true
					: (this.theEntity.getDistanceSqToEntity(entitylivingbase) < 144.0D
							&& entitylivingbase.getAITarget() != null ? false : this.isSitting);
		}
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.theEntity.getNavigator().clearPathEntity();
		this.theEntity.setSitting(true);
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.theEntity.setSitting(false);
	}

	/**+
	 * Sets the sitting flag.
	 */
	public void setSitting(boolean sitting) {
		this.isSitting = sitting;
	}
}