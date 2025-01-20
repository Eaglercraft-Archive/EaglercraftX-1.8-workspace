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
public class ModelWitch extends ModelVillager {
	public boolean field_82900_g;
	private ModelRenderer field_82901_h = (new ModelRenderer(this)).setTextureSize(64, 128);
	private ModelRenderer witchHat;

	public ModelWitch(float parFloat1) {
		super(parFloat1, 0.0F, 64, 128);
		this.field_82901_h.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.field_82901_h.setTextureOffset(0, 0).addBox(0.0F, 3.0F, -6.75F, 1, 1, 1, -0.25F);
		this.villagerNose.addChild(this.field_82901_h);
		this.witchHat = (new ModelRenderer(this)).setTextureSize(64, 128);
		this.witchHat.setRotationPoint(-5.0F, -10.03125F, -5.0F);
		this.witchHat.setTextureOffset(0, 64).addBox(0.0F, 0.0F, 0.0F, 10, 2, 10);
		this.villagerHead.addChild(this.witchHat);
		ModelRenderer modelrenderer = (new ModelRenderer(this)).setTextureSize(64, 128);
		modelrenderer.setRotationPoint(1.75F, -4.0F, 2.0F);
		modelrenderer.setTextureOffset(0, 76).addBox(0.0F, 0.0F, 0.0F, 7, 4, 7);
		modelrenderer.rotateAngleX = -0.05235988F;
		modelrenderer.rotateAngleZ = 0.02617994F;
		this.witchHat.addChild(modelrenderer);
		ModelRenderer modelrenderer1 = (new ModelRenderer(this)).setTextureSize(64, 128);
		modelrenderer1.setRotationPoint(1.75F, -4.0F, 2.0F);
		modelrenderer1.setTextureOffset(0, 87).addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
		modelrenderer1.rotateAngleX = -0.10471976F;
		modelrenderer1.rotateAngleZ = 0.05235988F;
		modelrenderer.addChild(modelrenderer1);
		ModelRenderer modelrenderer2 = (new ModelRenderer(this)).setTextureSize(64, 128);
		modelrenderer2.setRotationPoint(1.75F, -2.0F, 2.0F);
		modelrenderer2.setTextureOffset(0, 95).addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.25F);
		modelrenderer2.rotateAngleX = -0.20943952F;
		modelrenderer2.rotateAngleZ = 0.10471976F;
		modelrenderer1.addChild(modelrenderer2);
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
		this.villagerNose.offsetX = this.villagerNose.offsetY = this.villagerNose.offsetZ = 0.0F;
		float f6 = 0.01F * (float) (entity.getEntityId() % 10);
		this.villagerNose.rotateAngleX = MathHelper.sin((float) entity.ticksExisted * f6) * 4.5F * 3.1415927F / 180.0F;
		this.villagerNose.rotateAngleY = 0.0F;
		this.villagerNose.rotateAngleZ = MathHelper.cos((float) entity.ticksExisted * f6) * 2.5F * 3.1415927F / 180.0F;
		if (this.field_82900_g) {
			this.villagerNose.rotateAngleX = -0.9F;
			this.villagerNose.offsetZ = -0.09375F;
			this.villagerNose.offsetY = 0.1875F;
		}

	}
}