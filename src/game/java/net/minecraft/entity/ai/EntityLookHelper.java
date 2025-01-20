package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
public class EntityLookHelper {
	private EntityLiving entity;
	private float deltaLookYaw;
	private float deltaLookPitch;
	private boolean isLooking;
	private double posX;
	private double posY;
	private double posZ;

	public EntityLookHelper(EntityLiving entitylivingIn) {
		this.entity = entitylivingIn;
	}

	/**+
	 * Sets position to look at using entity
	 */
	public void setLookPositionWithEntity(Entity entityIn, float deltaYaw, float deltaPitch) {
		this.posX = entityIn.posX;
		if (entityIn instanceof EntityLivingBase) {
			this.posY = entityIn.posY + (double) entityIn.getEyeHeight();
		} else {
			this.posY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D;
		}

		this.posZ = entityIn.posZ;
		this.deltaLookYaw = deltaYaw;
		this.deltaLookPitch = deltaPitch;
		this.isLooking = true;
	}

	/**+
	 * Sets position to look at
	 */
	public void setLookPosition(double x, double y, double z, float deltaYaw, float deltaPitch) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.deltaLookYaw = deltaYaw;
		this.deltaLookPitch = deltaPitch;
		this.isLooking = true;
	}

	/**+
	 * Updates look
	 */
	public void onUpdateLook() {
		this.entity.rotationPitch = 0.0F;
		if (this.isLooking) {
			this.isLooking = false;
			double d0 = this.posX - this.entity.posX;
			double d1 = this.posY - (this.entity.posY + (double) this.entity.getEyeHeight());
			double d2 = this.posZ - this.entity.posZ;
			double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
			float f = (float) (MathHelper.func_181159_b(d2, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
			float f1 = (float) (-(MathHelper.func_181159_b(d1, d3) * 180.0D / 3.1415927410125732D));
			this.entity.rotationPitch = this.updateRotation(this.entity.rotationPitch, f1, this.deltaLookPitch);
			this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, f, this.deltaLookYaw);
		} else {
			this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset,
					10.0F);
		}

		float f2 = MathHelper.wrapAngleTo180_float(this.entity.rotationYawHead - this.entity.renderYawOffset);
		if (!this.entity.getNavigator().noPath()) {
			if (f2 < -75.0F) {
				this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0F;
			}

			if (f2 > 75.0F) {
				this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0F;
			}
		}

	}

	private float updateRotation(float parFloat1, float parFloat2, float parFloat3) {
		float f = MathHelper.wrapAngleTo180_float(parFloat2 - parFloat1);
		if (f > parFloat3) {
			f = parFloat3;
		}

		if (f < -parFloat3) {
			f = -parFloat3;
		}

		return parFloat1 + f;
	}

	public boolean getIsLooking() {
		return this.isLooking;
	}

	public double getLookPosX() {
		return this.posX;
	}

	public double getLookPosY() {
		return this.posY;
	}

	public double getLookPosZ() {
		return this.posZ;
	}
}