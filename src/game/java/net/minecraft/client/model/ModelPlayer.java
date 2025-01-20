package net.minecraft.client.model;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
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
public class ModelPlayer extends ModelBiped {
	public ModelRenderer bipedLeftArmwear;
	public ModelRenderer bipedRightArmwear;
	public ModelRenderer bipedLeftLegwear;
	public ModelRenderer bipedRightLegwear;
	public ModelRenderer bipedBodyWear;
	private ModelRenderer bipedCape;
	private ModelRenderer bipedDeadmau5Head;
	private boolean smallArms;

	public ModelPlayer(float parFloat1, boolean parFlag) {
		super(parFloat1, 0.0F, 64, 64);
		this.smallArms = parFlag;
		this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0);
		this.bipedDeadmau5Head.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, parFloat1);
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, parFloat1);
		if (parFlag) {
			this.bipedLeftArm = new ModelRenderer(this, 32, 48);
			this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, parFloat1);
			this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
			this.bipedRightArm = new ModelRenderer(this, 40, 16);
			this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, parFloat1);
			this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
			this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
			this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, parFloat1 + 0.25F);
			this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
			this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
			this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, parFloat1 + 0.25F);
			this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
		} else {
			this.bipedLeftArm = new ModelRenderer(this, 32, 48);
			this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, parFloat1);
			this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
			this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
			this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, parFloat1 + 0.25F);
			this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
			this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
			this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, parFloat1 + 0.25F);
			this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
		}

		this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, parFloat1);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
		this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, parFloat1 + 0.25F);
		this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
		this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
		this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, parFloat1 + 0.25F);
		this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
		this.bipedBodyWear = new ModelRenderer(this, 16, 32);
		this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, parFloat1 + 0.25F);
		this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		GlStateManager.pushMatrix();
		if (this.isChild) {
			float f6 = 2.0F;
			GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GlStateManager.translate(0.0F, 24.0F * f5, 0.0F);
			this.bipedLeftLegwear.render(f5);
			this.bipedRightLegwear.render(f5);
			this.bipedLeftArmwear.render(f5);
			this.bipedRightArmwear.render(f5);
			this.bipedBodyWear.render(f5);
		} else {
			if (entity != null && entity.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.bipedLeftLegwear.render(f5);
			this.bipedRightLegwear.render(f5);
			this.bipedLeftArmwear.render(f5);
			this.bipedRightArmwear.render(f5);
			this.bipedBodyWear.render(f5);
		}

		GlStateManager.popMatrix();
	}

	public void renderDeadmau5Head(float parFloat1) {
		copyModelAngles(this.bipedHead, this.bipedDeadmau5Head);
		this.bipedDeadmau5Head.rotationPointX = 0.0F;
		this.bipedDeadmau5Head.rotationPointY = 0.0F;
		this.bipedDeadmau5Head.render(parFloat1);
	}

	public void renderCape(float parFloat1) {
		GlStateManager.matrixMode(GL_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.scale(2.0f, 1.0f, 1.0f);
		GlStateManager.matrixMode(GL_MODELVIEW);
		this.bipedCape.render(parFloat1);
		GlStateManager.matrixMode(GL_TEXTURE);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL_MODELVIEW);
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
		copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
		copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
		copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
		copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
		copyModelAngles(this.bipedBody, this.bipedBodyWear);
		if (entity != null && entity.isSneaking()) {
			this.bipedCape.rotationPointY = 2.0F;
		} else {
			this.bipedCape.rotationPointY = 0.0F;
		}

	}

	public void renderRightArm() {
		this.bipedRightArm.render(0.0625F);
		this.bipedRightArmwear.render(0.0625F);
	}

	public void renderLeftArm() {
		this.bipedLeftArm.render(0.0625F);
		this.bipedLeftArmwear.render(0.0625F);
	}

	public void setInvisible(boolean flag) {
		super.setInvisible(flag);
		this.bipedLeftArmwear.showModel = flag;
		this.bipedRightArmwear.showModel = flag;
		this.bipedLeftLegwear.showModel = flag;
		this.bipedRightLegwear.showModel = flag;
		this.bipedBodyWear.showModel = flag;
		this.bipedCape.showModel = flag;
		this.bipedDeadmau5Head.showModel = flag;
	}

	public void postRenderArm(float f) {
		if (this.smallArms) {
			++this.bipedRightArm.rotationPointX;
			this.bipedRightArm.postRender(f);
			--this.bipedRightArm.rotationPointX;
		} else {
			this.bipedRightArm.postRender(f);
		}

	}
}