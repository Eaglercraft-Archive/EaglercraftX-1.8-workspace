package net.minecraft.client.model;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
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
public class ModelOcelot extends ModelBase {
	ModelRenderer ocelotBackLeftLeg;
	ModelRenderer ocelotBackRightLeg;
	ModelRenderer ocelotFrontLeftLeg;
	ModelRenderer ocelotFrontRightLeg;
	ModelRenderer ocelotTail;
	ModelRenderer ocelotTail2;
	ModelRenderer ocelotHead;
	ModelRenderer ocelotBody;
	int field_78163_i = 1;

	public ModelOcelot() {
		this.setTextureOffset("head.main", 0, 0);
		this.setTextureOffset("head.nose", 0, 24);
		this.setTextureOffset("head.ear1", 0, 10);
		this.setTextureOffset("head.ear2", 6, 10);
		this.ocelotHead = new ModelRenderer(this, "head");
		this.ocelotHead.addBox("main", -2.5F, -2.0F, -3.0F, 5, 4, 5);
		this.ocelotHead.addBox("nose", -1.5F, 0.0F, -4.0F, 3, 2, 2);
		this.ocelotHead.addBox("ear1", -2.0F, -3.0F, 0.0F, 1, 1, 2);
		this.ocelotHead.addBox("ear2", 1.0F, -3.0F, 0.0F, 1, 1, 2);
		this.ocelotHead.setRotationPoint(0.0F, 15.0F, -9.0F);
		this.ocelotBody = new ModelRenderer(this, 20, 0);
		this.ocelotBody.addBox(-2.0F, 3.0F, -8.0F, 4, 16, 6, 0.0F);
		this.ocelotBody.setRotationPoint(0.0F, 12.0F, -10.0F);
		this.ocelotTail = new ModelRenderer(this, 0, 15);
		this.ocelotTail.addBox(-0.5F, 0.0F, 0.0F, 1, 8, 1);
		this.ocelotTail.rotateAngleX = 0.9F;
		this.ocelotTail.setRotationPoint(0.0F, 15.0F, 8.0F);
		this.ocelotTail2 = new ModelRenderer(this, 4, 15);
		this.ocelotTail2.addBox(-0.5F, 0.0F, 0.0F, 1, 8, 1);
		this.ocelotTail2.setRotationPoint(0.0F, 20.0F, 14.0F);
		this.ocelotBackLeftLeg = new ModelRenderer(this, 8, 13);
		this.ocelotBackLeftLeg.addBox(-1.0F, 0.0F, 1.0F, 2, 6, 2);
		this.ocelotBackLeftLeg.setRotationPoint(1.1F, 18.0F, 5.0F);
		this.ocelotBackRightLeg = new ModelRenderer(this, 8, 13);
		this.ocelotBackRightLeg.addBox(-1.0F, 0.0F, 1.0F, 2, 6, 2);
		this.ocelotBackRightLeg.setRotationPoint(-1.1F, 18.0F, 5.0F);
		this.ocelotFrontLeftLeg = new ModelRenderer(this, 40, 0);
		this.ocelotFrontLeftLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 10, 2);
		this.ocelotFrontLeftLeg.setRotationPoint(1.2F, 13.8F, -5.0F);
		this.ocelotFrontRightLeg = new ModelRenderer(this, 40, 0);
		this.ocelotFrontRightLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 10, 2);
		this.ocelotFrontRightLeg.setRotationPoint(-1.2F, 13.8F, -5.0F);
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (this.isChild) {
			float f6 = 2.0F;
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.5F / f6, 1.5F / f6, 1.5F / f6);
			GlStateManager.translate(0.0F, 10.0F * f5, 4.0F * f5);
			this.ocelotHead.render(f5);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GlStateManager.translate(0.0F, 24.0F * f5, 0.0F);
			this.ocelotBody.render(f5);
			this.ocelotBackLeftLeg.render(f5);
			this.ocelotBackRightLeg.render(f5);
			this.ocelotFrontLeftLeg.render(f5);
			this.ocelotFrontRightLeg.render(f5);
			this.ocelotTail.render(f5);
			this.ocelotTail2.render(f5);
			GlStateManager.popMatrix();
		} else {
			this.ocelotHead.render(f5);
			this.ocelotBody.render(f5);
			this.ocelotTail.render(f5);
			this.ocelotTail2.render(f5);
			this.ocelotBackLeftLeg.render(f5);
			this.ocelotBackRightLeg.render(f5);
			this.ocelotFrontLeftLeg.render(f5);
			this.ocelotFrontRightLeg.render(f5);
		}

	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float f, float f1, float var3, float f2, float f3, float var6, Entity var7) {
		this.ocelotHead.rotateAngleX = f3 / 57.295776F;
		this.ocelotHead.rotateAngleY = f2 / 57.295776F;
		if (this.field_78163_i != 3) {
			this.ocelotBody.rotateAngleX = 1.5707964F;
			if (this.field_78163_i == 2) {
				this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.0F * f1;
				this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 0.3F) * 1.0F * f1;
				this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F + 0.3F) * 1.0F * f1;
				this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.0F * f1;
				this.ocelotTail2.rotateAngleX = 1.7278761F + 0.31415927F * MathHelper.cos(f) * f1;
			} else {
				this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.0F * f1;
				this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.0F * f1;
				this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.0F * f1;
				this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.0F * f1;
				if (this.field_78163_i == 1) {
					this.ocelotTail2.rotateAngleX = 1.7278761F + 0.7853982F * MathHelper.cos(f) * f1;
				} else {
					this.ocelotTail2.rotateAngleX = 1.7278761F + 0.47123894F * MathHelper.cos(f) * f1;
				}
			}
		}

	}

	/**+
	 * Used for easily adding entity-dependent animations. The
	 * second and third float params here are the same second and
	 * third as in the setRotationAngles method.
	 */
	public void setLivingAnimations(EntityLivingBase entitylivingbase, float var2, float var3, float var4) {
		EntityOcelot entityocelot = (EntityOcelot) entitylivingbase;
		this.ocelotBody.rotationPointY = 12.0F;
		this.ocelotBody.rotationPointZ = -10.0F;
		this.ocelotHead.rotationPointY = 15.0F;
		this.ocelotHead.rotationPointZ = -9.0F;
		this.ocelotTail.rotationPointY = 15.0F;
		this.ocelotTail.rotationPointZ = 8.0F;
		this.ocelotTail2.rotationPointY = 20.0F;
		this.ocelotTail2.rotationPointZ = 14.0F;
		this.ocelotFrontLeftLeg.rotationPointY = this.ocelotFrontRightLeg.rotationPointY = 13.8F;
		this.ocelotFrontLeftLeg.rotationPointZ = this.ocelotFrontRightLeg.rotationPointZ = -5.0F;
		this.ocelotBackLeftLeg.rotationPointY = this.ocelotBackRightLeg.rotationPointY = 18.0F;
		this.ocelotBackLeftLeg.rotationPointZ = this.ocelotBackRightLeg.rotationPointZ = 5.0F;
		this.ocelotTail.rotateAngleX = 0.9F;
		if (entityocelot.isSneaking()) {
			++this.ocelotBody.rotationPointY;
			this.ocelotHead.rotationPointY += 2.0F;
			++this.ocelotTail.rotationPointY;
			this.ocelotTail2.rotationPointY += -4.0F;
			this.ocelotTail2.rotationPointZ += 2.0F;
			this.ocelotTail.rotateAngleX = 1.5707964F;
			this.ocelotTail2.rotateAngleX = 1.5707964F;
			this.field_78163_i = 0;
		} else if (entityocelot.isSprinting()) {
			this.ocelotTail2.rotationPointY = this.ocelotTail.rotationPointY;
			this.ocelotTail2.rotationPointZ += 2.0F;
			this.ocelotTail.rotateAngleX = 1.5707964F;
			this.ocelotTail2.rotateAngleX = 1.5707964F;
			this.field_78163_i = 2;
		} else if (entityocelot.isSitting()) {
			this.ocelotBody.rotateAngleX = 0.7853982F;
			this.ocelotBody.rotationPointY += -4.0F;
			this.ocelotBody.rotationPointZ += 5.0F;
			this.ocelotHead.rotationPointY += -3.3F;
			++this.ocelotHead.rotationPointZ;
			this.ocelotTail.rotationPointY += 8.0F;
			this.ocelotTail.rotationPointZ += -2.0F;
			this.ocelotTail2.rotationPointY += 2.0F;
			this.ocelotTail2.rotationPointZ += -0.8F;
			this.ocelotTail.rotateAngleX = 1.7278761F;
			this.ocelotTail2.rotateAngleX = 2.670354F;
			this.ocelotFrontLeftLeg.rotateAngleX = this.ocelotFrontRightLeg.rotateAngleX = -0.15707964F;
			this.ocelotFrontLeftLeg.rotationPointY = this.ocelotFrontRightLeg.rotationPointY = 15.8F;
			this.ocelotFrontLeftLeg.rotationPointZ = this.ocelotFrontRightLeg.rotationPointZ = -7.0F;
			this.ocelotBackLeftLeg.rotateAngleX = this.ocelotBackRightLeg.rotateAngleX = -1.5707964F;
			this.ocelotBackLeftLeg.rotationPointY = this.ocelotBackRightLeg.rotationPointY = 21.0F;
			this.ocelotBackLeftLeg.rotationPointZ = this.ocelotBackRightLeg.rotationPointZ = 1.0F;
			this.field_78163_i = 3;
		} else {
			this.field_78163_i = 1;
		}

	}
}