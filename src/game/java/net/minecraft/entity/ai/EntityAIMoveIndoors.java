package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

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
public class EntityAIMoveIndoors extends EntityAIBase {
	private EntityCreature entityObj;
	private VillageDoorInfo doorInfo;
	private int insidePosX = -1;
	private int insidePosZ = -1;

	public EntityAIMoveIndoors(EntityCreature entityObjIn) {
		this.entityObj = entityObjIn;
		this.setMutexBits(1);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		BlockPos blockpos = new BlockPos(this.entityObj);
		if ((!this.entityObj.worldObj.isDaytime() || this.entityObj.worldObj.isRaining()
				&& !this.entityObj.worldObj.getBiomeGenForCoords(blockpos).canSpawnLightningBolt())
				&& !this.entityObj.worldObj.provider.getHasNoSky()) {
			if (this.entityObj.getRNG().nextInt(50) != 0) {
				return false;
			} else if (this.insidePosX != -1 && this.entityObj.getDistanceSq((double) this.insidePosX,
					this.entityObj.posY, (double) this.insidePosZ) < 4.0D) {
				return false;
			} else {
				Village village = this.entityObj.worldObj.getVillageCollection().getNearestVillage(blockpos, 14);
				if (village == null) {
					return false;
				} else {
					this.doorInfo = village.getDoorInfo(blockpos);
					return this.doorInfo != null;
				}
			}
		} else {
			return false;
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return !this.entityObj.getNavigator().noPath();
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.insidePosX = -1;
		BlockPos blockpos = this.doorInfo.getInsideBlockPos();
		int i = blockpos.getX();
		int j = blockpos.getY();
		int k = blockpos.getZ();
		if (this.entityObj.getDistanceSq(blockpos) > 256.0D) {
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3,
					new Vec3((double) i + 0.5D, (double) j, (double) k + 0.5D));
			if (vec3 != null) {
				this.entityObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0D);
			}
		} else {
			this.entityObj.getNavigator().tryMoveToXYZ((double) i + 0.5D, (double) j, (double) k + 0.5D, 1.0D);
		}

	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
		this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
		this.doorInfo = null;
	}
}