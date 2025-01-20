package net.minecraft.client.model;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityRabbit;
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
public class ModelRabbit extends ModelBase {
	ModelRenderer rabbitLeftFoot;
	ModelRenderer rabbitRightFoot;
	ModelRenderer rabbitLeftThigh;
	ModelRenderer rabbitRightThigh;
	ModelRenderer rabbitBody;
	ModelRenderer rabbitLeftArm;
	ModelRenderer rabbitRightArm;
	ModelRenderer rabbitHead;
	ModelRenderer rabbitRightEar;
	ModelRenderer rabbitLeftEar;
	ModelRenderer rabbitTail;
	ModelRenderer rabbitNose;
	private float field_178701_m = 0.0F;
	private float field_178699_n = 0.0F;

	public ModelRabbit() {
		this.setTextureOffset("head.main", 0, 0);
		this.setTextureOffset("head.nose", 0, 24);
		this.setTextureOffset("head.ear1", 0, 10);
		this.setTextureOffset("head.ear2", 6, 10);
		this.rabbitLeftFoot = new ModelRenderer(this, 26, 24);
		this.rabbitLeftFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
		this.rabbitLeftFoot.setRotationPoint(3.0F, 17.5F, 3.7F);
		this.rabbitLeftFoot.mirror = true;
		this.setRotationOffset(this.rabbitLeftFoot, 0.0F, 0.0F, 0.0F);
		this.rabbitRightFoot = new ModelRenderer(this, 8, 24);
		this.rabbitRightFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
		this.rabbitRightFoot.setRotationPoint(-3.0F, 17.5F, 3.7F);
		this.rabbitRightFoot.mirror = true;
		this.setRotationOffset(this.rabbitRightFoot, 0.0F, 0.0F, 0.0F);
		this.rabbitLeftThigh = new ModelRenderer(this, 30, 15);
		this.rabbitLeftThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
		this.rabbitLeftThigh.setRotationPoint(3.0F, 17.5F, 3.7F);
		this.rabbitLeftThigh.mirror = true;
		this.setRotationOffset(this.rabbitLeftThigh, -0.34906584F, 0.0F, 0.0F);
		this.rabbitRightThigh = new ModelRenderer(this, 16, 15);
		this.rabbitRightThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
		this.rabbitRightThigh.setRotationPoint(-3.0F, 17.5F, 3.7F);
		this.rabbitRightThigh.mirror = true;
		this.setRotationOffset(this.rabbitRightThigh, -0.34906584F, 0.0F, 0.0F);
		this.rabbitBody = new ModelRenderer(this, 0, 0);
		this.rabbitBody.addBox(-3.0F, -2.0F, -10.0F, 6, 5, 10);
		this.rabbitBody.setRotationPoint(0.0F, 19.0F, 8.0F);
		this.rabbitBody.mirror = true;
		this.setRotationOffset(this.rabbitBody, -0.34906584F, 0.0F, 0.0F);
		this.rabbitLeftArm = new ModelRenderer(this, 8, 15);
		this.rabbitLeftArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
		this.rabbitLeftArm.setRotationPoint(3.0F, 17.0F, -1.0F);
		this.rabbitLeftArm.mirror = true;
		this.setRotationOffset(this.rabbitLeftArm, -0.17453292F, 0.0F, 0.0F);
		this.rabbitRightArm = new ModelRenderer(this, 0, 15);
		this.rabbitRightArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
		this.rabbitRightArm.setRotationPoint(-3.0F, 17.0F, -1.0F);
		this.rabbitRightArm.mirror = true;
		this.setRotationOffset(this.rabbitRightArm, -0.17453292F, 0.0F, 0.0F);
		this.rabbitHead = new ModelRenderer(this, 32, 0);
		this.rabbitHead.addBox(-2.5F, -4.0F, -5.0F, 5, 4, 5);
		this.rabbitHead.setRotationPoint(0.0F, 16.0F, -1.0F);
		this.rabbitHead.mirror = true;
		this.setRotationOffset(this.rabbitHead, 0.0F, 0.0F, 0.0F);
		this.rabbitRightEar = new ModelRenderer(this, 52, 0);
		this.rabbitRightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
		this.rabbitRightEar.setRotationPoint(0.0F, 16.0F, -1.0F);
		this.rabbitRightEar.mirror = true;
		this.setRotationOffset(this.rabbitRightEar, 0.0F, -0.2617994F, 0.0F);
		this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
		this.rabbitLeftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
		this.rabbitLeftEar.setRotationPoint(0.0F, 16.0F, -1.0F);
		this.rabbitLeftEar.mirror = true;
		this.setRotationOffset(this.rabbitLeftEar, 0.0F, 0.2617994F, 0.0F);
		this.rabbitTail = new ModelRenderer(this, 52, 6);
		this.rabbitTail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2);
		this.rabbitTail.setRotationPoint(0.0F, 20.0F, 7.0F);
		this.rabbitTail.mirror = true;
		this.setRotationOffset(this.rabbitTail, -0.3490659F, 0.0F, 0.0F);
		this.rabbitNose = new ModelRenderer(this, 32, 9);
		this.rabbitNose.addBox(-0.5F, -2.5F, -5.5F, 1, 1, 1);
		this.rabbitNose.setRotationPoint(0.0F, 16.0F, -1.0F);
		this.rabbitNose.mirror = true;
		this.setRotationOffset(this.rabbitNose, 0.0F, 0.0F, 0.0F);
	}

	private void setRotationOffset(ModelRenderer parModelRenderer, float parFloat1, float parFloat2, float parFloat3) {
		parModelRenderer.rotateAngleX = parFloat1;
		parModelRenderer.rotateAngleY = parFloat2;
		parModelRenderer.rotateAngleZ = parFloat3;
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
			GlStateManager.translate(0.0F, 5.0F * f5, 2.0F * f5);
			this.rabbitHead.render(f5);
			this.rabbitLeftEar.render(f5);
			this.rabbitRightEar.render(f5);
			this.rabbitNose.render(f5);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GlStateManager.translate(0.0F, 24.0F * f5, 0.0F);
			this.rabbitLeftFoot.render(f5);
			this.rabbitRightFoot.render(f5);
			this.rabbitLeftThigh.render(f5);
			this.rabbitRightThigh.render(f5);
			this.rabbitBody.render(f5);
			this.rabbitLeftArm.render(f5);
			this.rabbitRightArm.render(f5);
			this.rabbitTail.render(f5);
			GlStateManager.popMatrix();
		} else {
			this.rabbitLeftFoot.render(f5);
			this.rabbitRightFoot.render(f5);
			this.rabbitLeftThigh.render(f5);
			this.rabbitRightThigh.render(f5);
			this.rabbitBody.render(f5);
			this.rabbitLeftArm.render(f5);
			this.rabbitRightArm.render(f5);
			this.rabbitHead.render(f5);
			this.rabbitRightEar.render(f5);
			this.rabbitLeftEar.render(f5);
			this.rabbitTail.render(f5);
			this.rabbitNose.render(f5);
		}

	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float var1, float var2, float f, float f1, float f2, float var6, Entity entity) {
		float f3 = f - (float) entity.ticksExisted;
		EntityRabbit entityrabbit = (EntityRabbit) entity;
		this.rabbitNose.rotateAngleX = this.rabbitHead.rotateAngleX = this.rabbitRightEar.rotateAngleX = this.rabbitLeftEar.rotateAngleX = f2
				* 0.017453292F;
		this.rabbitNose.rotateAngleY = this.rabbitHead.rotateAngleY = f1 * 0.017453292F;
		this.rabbitRightEar.rotateAngleY = this.rabbitNose.rotateAngleY - 0.2617994F;
		this.rabbitLeftEar.rotateAngleY = this.rabbitNose.rotateAngleY + 0.2617994F;
		this.field_178701_m = MathHelper.sin(entityrabbit.func_175521_o(f3) * 3.1415927F);
		this.rabbitLeftThigh.rotateAngleX = this.rabbitRightThigh.rotateAngleX = (this.field_178701_m * 50.0F - 21.0F)
				* 0.017453292F;
		this.rabbitLeftFoot.rotateAngleX = this.rabbitRightFoot.rotateAngleX = this.field_178701_m * 50.0F
				* 0.017453292F;
		this.rabbitLeftArm.rotateAngleX = this.rabbitRightArm.rotateAngleX = (this.field_178701_m * -40.0F - 11.0F)
				* 0.017453292F;
	}

	/**+
	 * Used for easily adding entity-dependent animations. The
	 * second and third float params here are the same second and
	 * third as in the setRotationAngles method.
	 */
	public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
	}
}