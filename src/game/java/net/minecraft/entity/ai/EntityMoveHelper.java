package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;

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
public class EntityMoveHelper {
	protected EntityLiving entity;
	protected double posX;
	protected double posY;
	protected double posZ;
	protected double speed;
	protected boolean update;

	public EntityMoveHelper(EntityLiving entitylivingIn) {
		this.entity = entitylivingIn;
		this.posX = entitylivingIn.posX;
		this.posY = entitylivingIn.posY;
		this.posZ = entitylivingIn.posZ;
	}

	public boolean isUpdating() {
		return this.update;
	}

	public double getSpeed() {
		return this.speed;
	}

	/**+
	 * Sets the speed and location to move to
	 */
	public void setMoveTo(double x, double y, double z, double speedIn) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.speed = speedIn;
		this.update = true;
	}

	public void onUpdateMoveHelper() {
		this.entity.setMoveForward(0.0F);
		if (this.update) {
			this.update = false;
			int i = MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5D);
			double d0 = this.posX - this.entity.posX;
			double d1 = this.posZ - this.entity.posZ;
			double d2 = this.posY - (double) i;
			double d3 = d0 * d0 + d2 * d2 + d1 * d1;
			if (d3 >= 2.500000277905201E-7D) {
				float f = (float) (MathHelper.func_181159_b(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
				this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 30.0F);
				this.entity.setAIMoveSpeed((float) (this.speed
						* this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
				if (d2 > 0.0D && d0 * d0 + d1 * d1 < 1.0D) {
					this.entity.getJumpHelper().setJumping();
				}

			}
		}
	}

	/**+
	 * Limits the given angle to a upper and lower limit.
	 */
	protected float limitAngle(float parFloat1, float parFloat2, float parFloat3) {
		float f = MathHelper.wrapAngleTo180_float(parFloat2 - parFloat1);
		if (f > parFloat3) {
			f = parFloat3;
		}

		if (f < -parFloat3) {
			f = -parFloat3;
		}

		float f1 = parFloat1 + f;
		if (f1 < 0.0F) {
			f1 += 360.0F;
		} else if (f1 > 360.0F) {
			f1 -= 360.0F;
		}

		return f1;
	}

	public double getX() {
		return this.posX;
	}

	public double getY() {
		return this.posY;
	}

	public double getZ() {
		return this.posZ;
	}
}