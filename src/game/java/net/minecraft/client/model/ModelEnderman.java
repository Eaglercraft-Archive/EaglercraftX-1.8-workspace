package net.minecraft.client.model;

import net.minecraft.entity.Entity;

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
public class ModelEnderman extends ModelBiped {
	public boolean isCarrying;
	public boolean isAttacking;

	public ModelEnderman(float parFloat1) {
		super(0.0F, -14.0F, 64, 32);
		float f = -14.0F;
		this.bipedHeadwear = new ModelRenderer(this, 0, 16);
		this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, parFloat1 - 0.5F);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + f, 0.0F);
		this.bipedBody = new ModelRenderer(this, 32, 16);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, parFloat1);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + f, 0.0F);
		this.bipedRightArm = new ModelRenderer(this, 56, 0);
		this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, parFloat1);
		this.bipedRightArm.setRotationPoint(-3.0F, 2.0F + f, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 56, 0);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, parFloat1);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + f, 0.0F);
		this.bipedRightLeg = new ModelRenderer(this, 56, 0);
		this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, parFloat1);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + f, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(this, 56, 0);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, parFloat1);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + f, 0.0F);
	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.bipedHead.showModel = true;
		float f6 = -14.0F;
		this.bipedBody.rotateAngleX = 0.0F;
		this.bipedBody.rotationPointY = f6;
		this.bipedBody.rotationPointZ = 0.0F;
		this.bipedRightLeg.rotateAngleX -= 0.0F;
		this.bipedLeftLeg.rotateAngleX -= 0.0F;
		this.bipedRightArm.rotateAngleX = (float) ((double) this.bipedRightArm.rotateAngleX * 0.5D);
		this.bipedLeftArm.rotateAngleX = (float) ((double) this.bipedLeftArm.rotateAngleX * 0.5D);
		this.bipedRightLeg.rotateAngleX = (float) ((double) this.bipedRightLeg.rotateAngleX * 0.5D);
		this.bipedLeftLeg.rotateAngleX = (float) ((double) this.bipedLeftLeg.rotateAngleX * 0.5D);
		float f7 = 0.4F;
		if (this.bipedRightArm.rotateAngleX > f7) {
			this.bipedRightArm.rotateAngleX = f7;
		}

		if (this.bipedLeftArm.rotateAngleX > f7) {
			this.bipedLeftArm.rotateAngleX = f7;
		}

		if (this.bipedRightArm.rotateAngleX < -f7) {
			this.bipedRightArm.rotateAngleX = -f7;
		}

		if (this.bipedLeftArm.rotateAngleX < -f7) {
			this.bipedLeftArm.rotateAngleX = -f7;
		}

		if (this.bipedRightLeg.rotateAngleX > f7) {
			this.bipedRightLeg.rotateAngleX = f7;
		}

		if (this.bipedLeftLeg.rotateAngleX > f7) {
			this.bipedLeftLeg.rotateAngleX = f7;
		}

		if (this.bipedRightLeg.rotateAngleX < -f7) {
			this.bipedRightLeg.rotateAngleX = -f7;
		}

		if (this.bipedLeftLeg.rotateAngleX < -f7) {
			this.bipedLeftLeg.rotateAngleX = -f7;
		}

		if (this.isCarrying) {
			this.bipedRightArm.rotateAngleX = -0.5F;
			this.bipedLeftArm.rotateAngleX = -0.5F;
			this.bipedRightArm.rotateAngleZ = 0.05F;
			this.bipedLeftArm.rotateAngleZ = -0.05F;
		}

		this.bipedRightArm.rotationPointZ = 0.0F;
		this.bipedLeftArm.rotationPointZ = 0.0F;
		this.bipedRightLeg.rotationPointZ = 0.0F;
		this.bipedLeftLeg.rotationPointZ = 0.0F;
		this.bipedRightLeg.rotationPointY = 9.0F + f6;
		this.bipedLeftLeg.rotationPointY = 9.0F + f6;
		this.bipedHead.rotationPointZ = 0.0F;
		this.bipedHead.rotationPointY = f6 + 1.0F;
		this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX;
		this.bipedHeadwear.rotationPointY = this.bipedHead.rotationPointY;
		this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;
		if (this.isAttacking) {
			float f8 = 1.0F;
			this.bipedHead.rotationPointY -= f8 * 5.0F;
		}

	}
}