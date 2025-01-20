package net.minecraft.client.model;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.entity.Entity;
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
public class ModelBiped extends ModelBase {
	public ModelRenderer bipedHead;
	public ModelRenderer bipedHeadwear;
	public ModelRenderer bipedBody;
	public ModelRenderer bipedRightArm;
	public ModelRenderer bipedLeftArm;
	public ModelRenderer bipedRightLeg;
	public ModelRenderer bipedLeftLeg;
	public int heldItemLeft;
	public int heldItemRight;
	public boolean isSneak;
	public boolean aimedBow;

	public ModelBiped() {
		this(0.0F);
	}

	public ModelBiped(float modelSize) {
		this(modelSize, 0.0F, 64, 32);
	}

	public ModelBiped(float modelSize, float parFloat1, int textureWidthIn, int textureHeightIn) {
		this.textureWidth = textureWidthIn;
		this.textureHeight = textureHeightIn;
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + parFloat1, 0.0F);
		this.bipedHeadwear = new ModelRenderer(this, 32, 0);
		this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + parFloat1, 0.0F);
		this.bipedBody = new ModelRenderer(this, 16, 16);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + parFloat1, 0.0F);
		this.bipedRightArm = new ModelRenderer(this, 40, 16);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + parFloat1, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 40, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + parFloat1, 0.0F);
		this.bipedRightLeg = new ModelRenderer(this, 0, 16);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + parFloat1, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + parFloat1, 0.0F);
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		GlStateManager.pushMatrix();
		if (this.isChild) {
			float f6 = 2.0F;
			GlStateManager.scale(1.5F / f6, 1.5F / f6, 1.5F / f6);
			GlStateManager.translate(0.0F, 16.0F * f5, 0.0F);
			this.bipedHead.render(f5);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GlStateManager.translate(0.0F, 24.0F * f5, 0.0F);
			this.bipedBody.render(f5);
			this.bipedRightArm.render(f5);
			this.bipedLeftArm.render(f5);
			this.bipedRightLeg.render(f5);
			this.bipedLeftLeg.render(f5);
			this.bipedHeadwear.render(f5);
		} else {
			if (entity != null && entity.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.bipedHead.render(f5);
			this.bipedBody.render(f5);
			this.bipedRightArm.render(f5);
			this.bipedLeftArm.render(f5);
			this.bipedRightLeg.render(f5);
			this.bipedLeftLeg.render(f5);
			this.bipedHeadwear.render(f5);
		}

		GlStateManager.popMatrix();
	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float var6, Entity var7) {
		this.bipedHead.rotateAngleY = f3 / 57.295776F;
		this.bipedHead.rotateAngleX = f4 / 57.295776F;
		this.bipedRightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 2.0F * f1 * 0.5F;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.4F * f1;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;
		if (this.isRiding) {
			this.bipedRightArm.rotateAngleX += -0.62831855F;
			this.bipedLeftArm.rotateAngleX += -0.62831855F;
			this.bipedRightLeg.rotateAngleX = -1.2566371F;
			this.bipedLeftLeg.rotateAngleX = -1.2566371F;
			this.bipedRightLeg.rotateAngleY = 0.31415927F;
			this.bipedLeftLeg.rotateAngleY = -0.31415927F;
		}

		if (this.heldItemLeft != 0) {
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F
					- 0.31415927F * (float) this.heldItemLeft;
		}

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		switch (this.heldItemRight) {
		case 0:
		case 2:
		default:
			break;
		case 1:
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F
					- 0.31415927F * (float) this.heldItemRight;
			break;
		case 3:
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F
					- 0.31415927F * (float) this.heldItemRight;
			this.bipedRightArm.rotateAngleY = -0.5235988F;
		}

		this.bipedLeftArm.rotateAngleY = 0.0F;
		if (this.swingProgress > -9990.0F) {
			float f5 = this.swingProgress;
			this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f5) * 3.1415927F * 2.0F) * 0.2F;
			this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
			f5 = 1.0F - this.swingProgress;
			f5 = f5 * f5;
			f5 = f5 * f5;
			f5 = 1.0F - f5;
			float f6 = MathHelper.sin(f5 * 3.1415927F);
			float f7 = MathHelper.sin(this.swingProgress * 3.1415927F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
			this.bipedRightArm.rotateAngleX = (float) ((double) this.bipedRightArm.rotateAngleX
					- ((double) f6 * 1.2D + (double) f7));
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
			this.bipedRightArm.rotateAngleZ += MathHelper.sin(this.swingProgress * 3.1415927F) * -0.4F;
		}

		if (this.isSneak) {
			this.bipedBody.rotateAngleX = 0.5F;
			this.bipedRightArm.rotateAngleX += 0.4F;
			this.bipedLeftArm.rotateAngleX += 0.4F;
			this.bipedRightLeg.rotationPointZ = 4.0F;
			this.bipedLeftLeg.rotationPointZ = 4.0F;
			this.bipedRightLeg.rotationPointY = 9.0F;
			this.bipedLeftLeg.rotationPointY = 9.0F;
			this.bipedHead.rotationPointY = 1.0F;
		} else {
			this.bipedBody.rotateAngleX = 0.0F;
			this.bipedRightLeg.rotationPointZ = 0.1F;
			this.bipedLeftLeg.rotationPointZ = 0.1F;
			this.bipedRightLeg.rotationPointY = 12.0F;
			this.bipedLeftLeg.rotationPointY = 12.0F;
			this.bipedHead.rotationPointY = 0.0F;
		}

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
		if (this.aimedBow) {
			float f8 = 0.0F;
			float f9 = 0.0F;
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = -(0.1F - f8 * 0.6F) + this.bipedHead.rotateAngleY;
			this.bipedLeftArm.rotateAngleY = 0.1F - f8 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
			this.bipedRightArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
			this.bipedRightArm.rotateAngleX -= f8 * 1.2F - f9 * 0.4F;
			this.bipedLeftArm.rotateAngleX -= f8 * 1.2F - f9 * 0.4F;
			this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
			this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
			this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
		}

		copyModelAngles(this.bipedHead, this.bipedHeadwear);
	}

	public void setModelAttributes(ModelBase model) {
		super.setModelAttributes(model);
		if (model instanceof ModelBiped) {
			ModelBiped modelbiped = (ModelBiped) model;
			this.heldItemLeft = modelbiped.heldItemLeft;
			this.heldItemRight = modelbiped.heldItemRight;
			this.isSneak = modelbiped.isSneak;
			this.aimedBow = modelbiped.aimedBow;
		}

	}

	public void setInvisible(boolean invisible) {
		this.bipedHead.showModel = invisible;
		this.bipedHeadwear.showModel = invisible;
		this.bipedBody.showModel = invisible;
		this.bipedRightArm.showModel = invisible;
		this.bipedLeftArm.showModel = invisible;
		this.bipedRightLeg.showModel = invisible;
		this.bipedLeftLeg.showModel = invisible;
	}

	public void postRenderArm(float f) {
		this.bipedRightArm.postRender(f);
	}
}