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
public class ModelQuadruped extends ModelBase {
	public ModelRenderer head = new ModelRenderer(this, 0, 0);
	public ModelRenderer body;
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer leg3;
	public ModelRenderer leg4;
	protected float childYOffset = 8.0F;
	protected float childZOffset = 4.0F;

	public ModelQuadruped(int parInt1, float parFloat1) {
		this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, parFloat1);
		this.head.setRotationPoint(0.0F, (float) (18 - parInt1), -6.0F);
		this.body = new ModelRenderer(this, 28, 8);
		this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, parFloat1);
		this.body.setRotationPoint(0.0F, (float) (17 - parInt1), 2.0F);
		this.leg1 = new ModelRenderer(this, 0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, parInt1, 4, parFloat1);
		this.leg1.setRotationPoint(-3.0F, (float) (24 - parInt1), 7.0F);
		this.leg2 = new ModelRenderer(this, 0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, parInt1, 4, parFloat1);
		this.leg2.setRotationPoint(3.0F, (float) (24 - parInt1), 7.0F);
		this.leg3 = new ModelRenderer(this, 0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, parInt1, 4, parFloat1);
		this.leg3.setRotationPoint(-3.0F, (float) (24 - parInt1), -5.0F);
		this.leg4 = new ModelRenderer(this, 0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, parInt1, 4, parFloat1);
		this.leg4.setRotationPoint(3.0F, (float) (24 - parInt1), -5.0F);
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
			GlStateManager.translate(0.0F, this.childYOffset * f5, this.childZOffset * f5);
			this.head.render(f5);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GlStateManager.translate(0.0F, 24.0F * f5, 0.0F);
			this.body.render(f5);
			this.leg1.render(f5);
			this.leg2.render(f5);
			this.leg3.render(f5);
			this.leg4.render(f5);
			GlStateManager.popMatrix();
		} else {
			this.head.render(f5);
			this.body.render(f5);
			this.leg1.render(f5);
			this.leg2.render(f5);
			this.leg3.render(f5);
			this.leg4.render(f5);
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
		float f4 = 57.295776F;
		this.head.rotateAngleX = f3 / 57.295776F;
		this.head.rotateAngleY = f2 / 57.295776F;
		this.body.rotateAngleX = 1.5707964F;
		this.leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		this.leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.4F * f1;
		this.leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.4F * f1;
		this.leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
	}
}