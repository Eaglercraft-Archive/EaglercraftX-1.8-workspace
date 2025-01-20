package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
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
public class ModelBat extends ModelBase {
	private ModelRenderer batHead;
	private ModelRenderer batBody;
	private ModelRenderer batRightWing;
	private ModelRenderer batLeftWing;
	private ModelRenderer batOuterRightWing;
	private ModelRenderer batOuterLeftWing;

	public ModelBat() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.batHead = new ModelRenderer(this, 0, 0);
		this.batHead.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
		ModelRenderer modelrenderer = new ModelRenderer(this, 24, 0);
		modelrenderer.addBox(-4.0F, -6.0F, -2.0F, 3, 4, 1);
		this.batHead.addChild(modelrenderer);
		ModelRenderer modelrenderer1 = new ModelRenderer(this, 24, 0);
		modelrenderer1.mirror = true;
		modelrenderer1.addBox(1.0F, -6.0F, -2.0F, 3, 4, 1);
		this.batHead.addChild(modelrenderer1);
		this.batBody = new ModelRenderer(this, 0, 16);
		this.batBody.addBox(-3.0F, 4.0F, -3.0F, 6, 12, 6);
		this.batBody.setTextureOffset(0, 34).addBox(-5.0F, 16.0F, 0.0F, 10, 6, 1);
		this.batRightWing = new ModelRenderer(this, 42, 0);
		this.batRightWing.addBox(-12.0F, 1.0F, 1.5F, 10, 16, 1);
		this.batOuterRightWing = new ModelRenderer(this, 24, 16);
		this.batOuterRightWing.setRotationPoint(-12.0F, 1.0F, 1.5F);
		this.batOuterRightWing.addBox(-8.0F, 1.0F, 0.0F, 8, 12, 1);
		this.batLeftWing = new ModelRenderer(this, 42, 0);
		this.batLeftWing.mirror = true;
		this.batLeftWing.addBox(2.0F, 1.0F, 1.5F, 10, 16, 1);
		this.batOuterLeftWing = new ModelRenderer(this, 24, 16);
		this.batOuterLeftWing.mirror = true;
		this.batOuterLeftWing.setRotationPoint(12.0F, 1.0F, 1.5F);
		this.batOuterLeftWing.addBox(0.0F, 1.0F, 0.0F, 8, 12, 1);
		this.batBody.addChild(this.batRightWing);
		this.batBody.addChild(this.batLeftWing);
		this.batRightWing.addChild(this.batOuterRightWing);
		this.batLeftWing.addChild(this.batOuterLeftWing);
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.batHead.render(f5);
		this.batBody.render(f5);
	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float var1, float var2, float f, float f1, float f2, float var6, Entity entity) {
		if (((EntityBat) entity).getIsBatHanging()) {
			float f3 = 57.295776F;
			this.batHead.rotateAngleX = f2 / 57.295776F;
			this.batHead.rotateAngleY = 3.1415927F - f1 / 57.295776F;
			this.batHead.rotateAngleZ = 3.1415927F;
			this.batHead.setRotationPoint(0.0F, -2.0F, 0.0F);
			this.batRightWing.setRotationPoint(-3.0F, 0.0F, 3.0F);
			this.batLeftWing.setRotationPoint(3.0F, 0.0F, 3.0F);
			this.batBody.rotateAngleX = 3.1415927F;
			this.batRightWing.rotateAngleX = -0.15707964F;
			this.batRightWing.rotateAngleY = -1.2566371F;
			this.batOuterRightWing.rotateAngleY = -1.7278761F;
			this.batLeftWing.rotateAngleX = this.batRightWing.rotateAngleX;
			this.batLeftWing.rotateAngleY = -this.batRightWing.rotateAngleY;
			this.batOuterLeftWing.rotateAngleY = -this.batOuterRightWing.rotateAngleY;
		} else {
			float f4 = 57.295776F;
			this.batHead.rotateAngleX = f2 / 57.295776F;
			this.batHead.rotateAngleY = f1 / 57.295776F;
			this.batHead.rotateAngleZ = 0.0F;
			this.batHead.setRotationPoint(0.0F, 0.0F, 0.0F);
			this.batRightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
			this.batLeftWing.setRotationPoint(0.0F, 0.0F, 0.0F);
			this.batBody.rotateAngleX = 0.7853982F + MathHelper.cos(f * 0.1F) * 0.15F;
			this.batBody.rotateAngleY = 0.0F;
			this.batRightWing.rotateAngleY = MathHelper.cos(f * 1.3F) * 3.1415927F * 0.25F;
			this.batLeftWing.rotateAngleY = -this.batRightWing.rotateAngleY;
			this.batOuterRightWing.rotateAngleY = this.batRightWing.rotateAngleY * 0.5F;
			this.batOuterLeftWing.rotateAngleY = -this.batRightWing.rotateAngleY * 0.5F;
		}

	}
}