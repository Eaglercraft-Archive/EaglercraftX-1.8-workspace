package net.minecraft.client.model;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
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
public class ModelWolf extends ModelBase {
	public ModelRenderer wolfHeadMain;
	public ModelRenderer wolfBody;
	public ModelRenderer wolfLeg1;
	public ModelRenderer wolfLeg2;
	public ModelRenderer wolfLeg3;
	public ModelRenderer wolfLeg4;
	ModelRenderer wolfTail;
	ModelRenderer wolfMane;

	public ModelWolf() {
		float f = 0.0F;
		float f1 = 13.5F;
		this.wolfHeadMain = new ModelRenderer(this, 0, 0);
		this.wolfHeadMain.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 4, f);
		this.wolfHeadMain.setRotationPoint(-1.0F, f1, -7.0F);
		this.wolfBody = new ModelRenderer(this, 18, 14);
		this.wolfBody.addBox(-4.0F, -2.0F, -3.0F, 6, 9, 6, f);
		this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
		this.wolfMane = new ModelRenderer(this, 21, 0);
		this.wolfMane.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 7, f);
		this.wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
		this.wolfLeg1 = new ModelRenderer(this, 0, 18);
		this.wolfLeg1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
		this.wolfLeg2 = new ModelRenderer(this, 0, 18);
		this.wolfLeg2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
		this.wolfLeg3 = new ModelRenderer(this, 0, 18);
		this.wolfLeg3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
		this.wolfLeg4 = new ModelRenderer(this, 0, 18);
		this.wolfLeg4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
		this.wolfTail = new ModelRenderer(this, 9, 18);
		this.wolfTail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
		this.wolfHeadMain.setTextureOffset(16, 14).addBox(-3.0F, -5.0F, 0.0F, 2, 2, 1, f);
		this.wolfHeadMain.setTextureOffset(16, 14).addBox(1.0F, -5.0F, 0.0F, 2, 2, 1, f);
		this.wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5F, 0.0F, -5.0F, 3, 3, 4, f);
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (this.isChild) {
			float f6 = 2.0F;
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 5.0F * f5, 2.0F * f5);
			this.wolfHeadMain.renderWithRotation(f5);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GlStateManager.translate(0.0F, 24.0F * f5, 0.0F);
			this.wolfBody.render(f5);
			this.wolfLeg1.render(f5);
			this.wolfLeg2.render(f5);
			this.wolfLeg3.render(f5);
			this.wolfLeg4.render(f5);
			this.wolfTail.renderWithRotation(f5);
			this.wolfMane.render(f5);
			GlStateManager.popMatrix();
		} else {
			this.wolfHeadMain.renderWithRotation(f5);
			this.wolfBody.render(f5);
			this.wolfLeg1.render(f5);
			this.wolfLeg2.render(f5);
			this.wolfLeg3.render(f5);
			this.wolfLeg4.render(f5);
			this.wolfTail.renderWithRotation(f5);
			this.wolfMane.render(f5);
		}

	}

	/**+
	 * Used for easily adding entity-dependent animations. The
	 * second and third float params here are the same second and
	 * third as in the setRotationAngles method.
	 */
	public void setLivingAnimations(EntityLivingBase entitylivingbase, float f, float f1, float f2) {
		EntityWolf entitywolf = (EntityWolf) entitylivingbase;
		if (entitywolf.isAngry()) {
			this.wolfTail.rotateAngleY = 0.0F;
		} else {
			this.wolfTail.rotateAngleY = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		}

		if (entitywolf.isSitting()) {
			this.wolfMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
			this.wolfMane.rotateAngleX = 1.2566371F;
			this.wolfMane.rotateAngleY = 0.0F;
			this.wolfBody.setRotationPoint(0.0F, 18.0F, 0.0F);
			this.wolfBody.rotateAngleX = 0.7853982F;
			this.wolfTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
			this.wolfLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
			this.wolfLeg1.rotateAngleX = 4.712389F;
			this.wolfLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
			this.wolfLeg2.rotateAngleX = 4.712389F;
			this.wolfLeg3.rotateAngleX = 5.811947F;
			this.wolfLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
			this.wolfLeg4.rotateAngleX = 5.811947F;
			this.wolfLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
		} else {
			this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
			this.wolfBody.rotateAngleX = 1.5707964F;
			this.wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
			this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
			this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
			this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
			this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
			this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
			this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
			this.wolfLeg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
			this.wolfLeg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.4F * f1;
			this.wolfLeg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.4F * f1;
			this.wolfLeg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		}

		this.wolfHeadMain.rotateAngleZ = entitywolf.getInterestedAngle(f2) + entitywolf.getShakeAngle(f2, 0.0F);
		this.wolfMane.rotateAngleZ = entitywolf.getShakeAngle(f2, -0.08F);
		this.wolfBody.rotateAngleZ = entitywolf.getShakeAngle(f2, -0.16F);
		this.wolfTail.rotateAngleZ = entitywolf.getShakeAngle(f2, -0.2F);
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
		this.wolfHeadMain.rotateAngleX = f4 / 57.295776F;
		this.wolfHeadMain.rotateAngleY = f3 / 57.295776F;
		this.wolfTail.rotateAngleX = f2;
	}
}