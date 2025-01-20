package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
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
public class EntityAIFollowOwner extends EntityAIBase {
	private EntityTameable thePet;
	private EntityLivingBase theOwner;
	World theWorld;
	private double followSpeed;
	private PathNavigate petPathfinder;
	private int field_75343_h;
	float maxDist;
	float minDist;
	private boolean field_75344_i;

	public EntityAIFollowOwner(EntityTameable thePetIn, double followSpeedIn, float minDistIn, float maxDistIn) {
		this.thePet = thePetIn;
		this.theWorld = thePetIn.worldObj;
		this.followSpeed = followSpeedIn;
		this.petPathfinder = thePetIn.getNavigator();
		this.minDist = minDistIn;
		this.maxDist = maxDistIn;
		this.setMutexBits(3);
		if (!(thePetIn.getNavigator() instanceof PathNavigateGround)) {
			throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
		}
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = this.thePet.getOwner();
		if (entitylivingbase == null) {
			return false;
		} else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer) entitylivingbase).isSpectator()) {
			return false;
		} else if (this.thePet.isSitting()) {
			return false;
		} else if (this.thePet.getDistanceSqToEntity(entitylivingbase) < (double) (this.minDist * this.minDist)) {
			return false;
		} else {
			this.theOwner = entitylivingbase;
			return true;
		}
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return !this.petPathfinder.noPath()
				&& this.thePet.getDistanceSqToEntity(this.theOwner) > (double) (this.maxDist * this.maxDist)
				&& !this.thePet.isSitting();
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.field_75343_h = 0;
		this.field_75344_i = ((PathNavigateGround) this.thePet.getNavigator()).getAvoidsWater();
		((PathNavigateGround) this.thePet.getNavigator()).setAvoidsWater(false);
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.theOwner = null;
		this.petPathfinder.clearPathEntity();
		((PathNavigateGround) this.thePet.getNavigator()).setAvoidsWater(true);
	}

	private boolean func_181065_a(BlockPos parBlockPos) {
		IBlockState iblockstate = this.theWorld.getBlockState(parBlockPos);
		Block block = iblockstate.getBlock();
		return block == Blocks.air ? true : !block.isFullCube();
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		this.thePet.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F,
				(float) this.thePet.getVerticalFaceSpeed());
		if (!this.thePet.isSitting()) {
			if (--this.field_75343_h <= 0) {
				this.field_75343_h = 10;
				if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.followSpeed)) {
					if (!this.thePet.getLeashed()) {
						if (this.thePet.getDistanceSqToEntity(this.theOwner) >= 144.0D) {
							int i = MathHelper.floor_double(this.theOwner.posX) - 2;
							int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
							int k = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);

							for (int l = 0; l <= 4; ++l) {
								for (int i1 = 0; i1 <= 4; ++i1) {
									if ((l < 1 || i1 < 1 || l > 3 || i1 > 3)
											&& World.doesBlockHaveSolidTopSurface(this.theWorld,
													new BlockPos(i + l, k - 1, j + i1))
											&& this.func_181065_a(new BlockPos(i + l, k, j + i1))
											&& this.func_181065_a(new BlockPos(i + l, k + 1, j + i1))) {
										this.thePet.setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k,
												(double) ((float) (j + i1) + 0.5F), this.thePet.rotationYaw,
												this.thePet.rotationPitch);
										this.petPathfinder.clearPathEntity();
										return;
									}
								}
							}

						}
					}
				}
			}
		}
	}
}