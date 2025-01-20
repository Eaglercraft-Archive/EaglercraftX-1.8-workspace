package net.minecraft.client.model;

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
public class ModelSpider extends ModelBase {
	public ModelRenderer spiderHead;
	public ModelRenderer spiderNeck;
	public ModelRenderer spiderBody;
	public ModelRenderer spiderLeg1;
	public ModelRenderer spiderLeg2;
	public ModelRenderer spiderLeg3;
	public ModelRenderer spiderLeg4;
	public ModelRenderer spiderLeg5;
	public ModelRenderer spiderLeg6;
	public ModelRenderer spiderLeg7;
	public ModelRenderer spiderLeg8;

	public ModelSpider() {
		float f = 0.0F;
		byte b0 = 15;
		this.spiderHead = new ModelRenderer(this, 32, 4);
		this.spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, f);
		this.spiderHead.setRotationPoint(0.0F, (float) b0, -3.0F);
		this.spiderNeck = new ModelRenderer(this, 0, 0);
		this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, f);
		this.spiderNeck.setRotationPoint(0.0F, (float) b0, 0.0F);
		this.spiderBody = new ModelRenderer(this, 0, 12);
		this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, f);
		this.spiderBody.setRotationPoint(0.0F, (float) b0, 9.0F);
		this.spiderLeg1 = new ModelRenderer(this, 18, 0);
		this.spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg1.setRotationPoint(-4.0F, (float) b0, 2.0F);
		this.spiderLeg2 = new ModelRenderer(this, 18, 0);
		this.spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg2.setRotationPoint(4.0F, (float) b0, 2.0F);
		this.spiderLeg3 = new ModelRenderer(this, 18, 0);
		this.spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg3.setRotationPoint(-4.0F, (float) b0, 1.0F);
		this.spiderLeg4 = new ModelRenderer(this, 18, 0);
		this.spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg4.setRotationPoint(4.0F, (float) b0, 1.0F);
		this.spiderLeg5 = new ModelRenderer(this, 18, 0);
		this.spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg5.setRotationPoint(-4.0F, (float) b0, 0.0F);
		this.spiderLeg6 = new ModelRenderer(this, 18, 0);
		this.spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg6.setRotationPoint(4.0F, (float) b0, 0.0F);
		this.spiderLeg7 = new ModelRenderer(this, 18, 0);
		this.spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg7.setRotationPoint(-4.0F, (float) b0, -1.0F);
		this.spiderLeg8 = new ModelRenderer(this, 18, 0);
		this.spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg8.setRotationPoint(4.0F, (float) b0, -1.0F);
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.spiderHead.render(f5);
		this.spiderNeck.render(f5);
		this.spiderBody.render(f5);
		this.spiderLeg1.render(f5);
		this.spiderLeg2.render(f5);
		this.spiderLeg3.render(f5);
		this.spiderLeg4.render(f5);
		this.spiderLeg5.render(f5);
		this.spiderLeg6.render(f5);
		this.spiderLeg7.render(f5);
		this.spiderLeg8.render(f5);
	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float f, float f1, float var3, float f2, float f3, float var6, Entity var7) {
		this.spiderHead.rotateAngleY = f2 / 57.295776F;
		this.spiderHead.rotateAngleX = f3 / 57.295776F;
		float f4 = 0.7853982F;
		this.spiderLeg1.rotateAngleZ = -f4;
		this.spiderLeg2.rotateAngleZ = f4;
		this.spiderLeg3.rotateAngleZ = -f4 * 0.74F;
		this.spiderLeg4.rotateAngleZ = f4 * 0.74F;
		this.spiderLeg5.rotateAngleZ = -f4 * 0.74F;
		this.spiderLeg6.rotateAngleZ = f4 * 0.74F;
		this.spiderLeg7.rotateAngleZ = -f4;
		this.spiderLeg8.rotateAngleZ = f4;
		float f5 = 0.0F;
		float f6 = 0.3926991F;
		this.spiderLeg1.rotateAngleY = f6 * 2.0F + f5;
		this.spiderLeg2.rotateAngleY = -f6 * 2.0F - f5;
		this.spiderLeg3.rotateAngleY = f6 * 1.0F + f5;
		this.spiderLeg4.rotateAngleY = -f6 * 1.0F - f5;
		this.spiderLeg5.rotateAngleY = -f6 * 1.0F + f5;
		this.spiderLeg6.rotateAngleY = f6 * 1.0F - f5;
		this.spiderLeg7.rotateAngleY = -f6 * 2.0F + f5;
		this.spiderLeg8.rotateAngleY = f6 * 2.0F - f5;
		float f7 = -(MathHelper.cos(f * 0.6662F * 2.0F + 0.0F) * 0.4F) * f1;
		float f8 = -(MathHelper.cos(f * 0.6662F * 2.0F + 3.1415927F) * 0.4F) * f1;
		float f9 = -(MathHelper.cos(f * 0.6662F * 2.0F + 1.5707964F) * 0.4F) * f1;
		float f10 = -(MathHelper.cos(f * 0.6662F * 2.0F + 4.712389F) * 0.4F) * f1;
		float f11 = Math.abs(MathHelper.sin(f * 0.6662F + 0.0F) * 0.4F) * f1;
		float f12 = Math.abs(MathHelper.sin(f * 0.6662F + 3.1415927F) * 0.4F) * f1;
		float f13 = Math.abs(MathHelper.sin(f * 0.6662F + 1.5707964F) * 0.4F) * f1;
		float f14 = Math.abs(MathHelper.sin(f * 0.6662F + 4.712389F) * 0.4F) * f1;
		this.spiderLeg1.rotateAngleY += f7;
		this.spiderLeg2.rotateAngleY += -f7;
		this.spiderLeg3.rotateAngleY += f8;
		this.spiderLeg4.rotateAngleY += -f8;
		this.spiderLeg5.rotateAngleY += f9;
		this.spiderLeg6.rotateAngleY += -f9;
		this.spiderLeg7.rotateAngleY += f10;
		this.spiderLeg8.rotateAngleY += -f10;
		this.spiderLeg1.rotateAngleZ += f11;
		this.spiderLeg2.rotateAngleZ += -f11;
		this.spiderLeg3.rotateAngleZ += f12;
		this.spiderLeg4.rotateAngleZ += -f12;
		this.spiderLeg5.rotateAngleZ += f13;
		this.spiderLeg6.rotateAngleZ += -f13;
		this.spiderLeg7.rotateAngleZ += f14;
		this.spiderLeg8.rotateAngleZ += -f14;
	}
}