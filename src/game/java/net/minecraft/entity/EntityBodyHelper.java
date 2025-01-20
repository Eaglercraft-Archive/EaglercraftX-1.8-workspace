package net.minecraft.entity;

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
public class EntityBodyHelper {
	private EntityLivingBase theLiving;
	private int rotationTickCounter;
	private float prevRenderYawHead;

	public EntityBodyHelper(EntityLivingBase parEntityLivingBase) {
		this.theLiving = parEntityLivingBase;
	}

	/**+
	 * Update the Head and Body rendenring angles
	 */
	public void updateRenderAngles() {
		double d0 = this.theLiving.posX - this.theLiving.prevPosX;
		double d1 = this.theLiving.posZ - this.theLiving.prevPosZ;
		if (d0 * d0 + d1 * d1 > 2.500000277905201E-7D) {
			this.theLiving.renderYawOffset = this.theLiving.rotationYaw;
			this.theLiving.rotationYawHead = this.computeAngleWithBound(this.theLiving.renderYawOffset,
					this.theLiving.rotationYawHead, 75.0F);
			this.prevRenderYawHead = this.theLiving.rotationYawHead;
			this.rotationTickCounter = 0;
		} else {
			float f = 75.0F;
			if (Math.abs(this.theLiving.rotationYawHead - this.prevRenderYawHead) > 15.0F) {
				this.rotationTickCounter = 0;
				this.prevRenderYawHead = this.theLiving.rotationYawHead;
			} else {
				++this.rotationTickCounter;
				boolean flag = true;
				if (this.rotationTickCounter > 10) {
					f = Math.max(1.0F - (float) (this.rotationTickCounter - 10) / 10.0F, 0.0F) * 75.0F;
				}
			}

			this.theLiving.renderYawOffset = this.computeAngleWithBound(this.theLiving.rotationYawHead,
					this.theLiving.renderYawOffset, f);
		}
	}

	/**+
	 * Return the new angle2 such that the difference between angle1
	 * and angle2 is lower than angleMax. Args : angle1, angle2,
	 * angleMax
	 */
	private float computeAngleWithBound(float parFloat1, float parFloat2, float parFloat3) {
		float f = MathHelper.wrapAngleTo180_float(parFloat1 - parFloat2);
		if (f < -parFloat3) {
			f = -parFloat3;
		}

		if (f >= parFloat3) {
			f = parFloat3;
		}

		return parFloat1 - f;
	}
}