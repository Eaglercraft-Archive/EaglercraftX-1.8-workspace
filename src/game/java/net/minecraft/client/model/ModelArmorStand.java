package net.minecraft.client.model;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

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
public class ModelArmorStand extends ModelArmorStandArmor {
	public ModelRenderer standRightSide;
	public ModelRenderer standLeftSide;
	public ModelRenderer standWaist;
	public ModelRenderer standBase;

	public ModelArmorStand() {
		this(0.0F);
	}

	public ModelArmorStand(float parFloat1) {
		super(parFloat1, 64, 64);
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-1.0F, -7.0F, -1.0F, 2, 7, 2, parFloat1);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody = new ModelRenderer(this, 0, 26);
		this.bipedBody.addBox(-6.0F, 0.0F, -1.5F, 12, 3, 3, parFloat1);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedRightArm = new ModelRenderer(this, 24, 0);
		this.bipedRightArm.addBox(-2.0F, -2.0F, -1.0F, 2, 12, 2, parFloat1);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 32, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(0.0F, -2.0F, -1.0F, 2, 12, 2, parFloat1);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedRightLeg = new ModelRenderer(this, 8, 0);
		this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, parFloat1);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(this, 40, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, parFloat1);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		this.standRightSide = new ModelRenderer(this, 16, 0);
		this.standRightSide.addBox(-3.0F, 3.0F, -1.0F, 2, 7, 2, parFloat1);
		this.standRightSide.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.standRightSide.showModel = true;
		this.standLeftSide = new ModelRenderer(this, 48, 16);
		this.standLeftSide.addBox(1.0F, 3.0F, -1.0F, 2, 7, 2, parFloat1);
		this.standLeftSide.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.standWaist = new ModelRenderer(this, 0, 48);
		this.standWaist.addBox(-4.0F, 10.0F, -1.0F, 8, 2, 2, parFloat1);
		this.standWaist.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.standBase = new ModelRenderer(this, 0, 32);
		this.standBase.addBox(-6.0F, 11.0F, -6.0F, 12, 1, 12, parFloat1);
		this.standBase.setRotationPoint(0.0F, 12.0F, 0.0F);
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
		if (entity instanceof EntityArmorStand) {
			EntityArmorStand entityarmorstand = (EntityArmorStand) entity;
			this.bipedLeftArm.showModel = entityarmorstand.getShowArms();
			this.bipedRightArm.showModel = entityarmorstand.getShowArms();
			this.standBase.showModel = !entityarmorstand.hasNoBasePlate();
			this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
			this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
			this.standRightSide.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
			this.standRightSide.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
			this.standRightSide.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
			this.standLeftSide.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
			this.standLeftSide.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
			this.standLeftSide.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
			this.standWaist.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
			this.standWaist.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
			this.standWaist.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
			float f6 = (entityarmorstand.getLeftLegRotation().getX() + entityarmorstand.getRightLegRotation().getX())
					/ 2.0F;
			float f7 = (entityarmorstand.getLeftLegRotation().getY() + entityarmorstand.getRightLegRotation().getY())
					/ 2.0F;
			float f8 = (entityarmorstand.getLeftLegRotation().getZ() + entityarmorstand.getRightLegRotation().getZ())
					/ 2.0F;
			this.standBase.rotateAngleX = 0.0F;
			this.standBase.rotateAngleY = 0.017453292F * -entity.rotationYaw;
			this.standBase.rotateAngleZ = 0.0F;
		}
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entityIn, float scale, float parFloat2, float parFloat3, float parFloat4, float parFloat5,
			float parFloat6) {
		super.render(entityIn, scale, parFloat2, parFloat3, parFloat4, parFloat5, parFloat6);
		GlStateManager.pushMatrix();
		if (this.isChild) {
			float f = 2.0F;
			GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
			GlStateManager.translate(0.0F, 24.0F * parFloat6, 0.0F);
			this.standRightSide.render(parFloat6);
			this.standLeftSide.render(parFloat6);
			this.standWaist.render(parFloat6);
			this.standBase.render(parFloat6);
		} else {
			if (entityIn.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.standRightSide.render(parFloat6);
			this.standLeftSide.render(parFloat6);
			this.standWaist.render(parFloat6);
			this.standBase.render(parFloat6);
		}

		GlStateManager.popMatrix();
	}

	public void postRenderArm(float scale) {
		boolean flag = this.bipedRightArm.showModel;
		this.bipedRightArm.showModel = true;
		super.postRenderArm(scale);
		this.bipedRightArm.showModel = flag;
	}
}