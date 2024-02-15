package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.ai;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.EntityCreature;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.ai.EntityAIBase;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.pathfinding.PathNavigateGround;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.BlockPos;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.village.Village;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.village.VillageDoorInfo;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class EntityAIRestrictOpenDoor extends EntityAIBase {

	static {
		__checkIntegratedContextValid("net/minecraft/entity/ai/EntityAIRestrictOpenDoor");
	}

	private EntityCreature entityObj;
	private VillageDoorInfo frontDoor;

	public EntityAIRestrictOpenDoor(EntityCreature creatureIn) {
		this.entityObj = creatureIn;
		if (!(creatureIn.getNavigator() instanceof PathNavigateGround)) {
			throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
		}
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.entityObj.worldObj.isDaytime()) {
			return false;
		} else {
			BlockPos blockpos = new BlockPos(this.entityObj);
			Village village = this.entityObj.worldObj.getVillageCollection().getNearestVillage(blockpos, 16);
			if (village == null) {
				return false;
			} else {
				this.frontDoor = village.getNearestDoor(blockpos);
				return this.frontDoor == null ? false
						: (double) this.frontDoor.getDistanceToInsideBlockSq(blockpos) < 2.25D;
			}
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return this.entityObj.worldObj.isDaytime() ? false
				: !this.frontDoor.getIsDetachedFromVillageFlag()
						&& this.frontDoor.func_179850_c(new BlockPos(this.entityObj));
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		((PathNavigateGround) this.entityObj.getNavigator()).setBreakDoors(false);
		((PathNavigateGround) this.entityObj.getNavigator()).setEnterDoors(false);
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		((PathNavigateGround) this.entityObj.getNavigator()).setBreakDoors(true);
		((PathNavigateGround) this.entityObj.getNavigator()).setEnterDoors(true);
		this.frontDoor = null;
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		this.frontDoor.incrementDoorOpeningRestrictionCounter();
	}
}