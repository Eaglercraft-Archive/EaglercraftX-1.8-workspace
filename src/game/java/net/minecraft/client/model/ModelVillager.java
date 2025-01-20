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
public class ModelVillager extends ModelBase {
	public ModelRenderer villagerHead;
	public ModelRenderer villagerBody;
	public ModelRenderer villagerArms;
	public ModelRenderer rightVillagerLeg;
	public ModelRenderer leftVillagerLeg;
	public ModelRenderer villagerNose;

	public ModelVillager(float parFloat1) {
		this(parFloat1, 0.0F, 64, 64);
	}

	public ModelVillager(float parFloat1, float parFloat2, int parInt1, int parInt2) {
		this.villagerHead = (new ModelRenderer(this)).setTextureSize(parInt1, parInt2);
		this.villagerHead.setRotationPoint(0.0F, 0.0F + parFloat2, 0.0F);
		this.villagerHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, parFloat1);
		this.villagerNose = (new ModelRenderer(this)).setTextureSize(parInt1, parInt2);
		this.villagerNose.setRotationPoint(0.0F, parFloat2 - 2.0F, 0.0F);
		this.villagerNose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, parFloat1);
		this.villagerHead.addChild(this.villagerNose);
		this.villagerBody = (new ModelRenderer(this)).setTextureSize(parInt1, parInt2);
		this.villagerBody.setRotationPoint(0.0F, 0.0F + parFloat2, 0.0F);
		this.villagerBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, parFloat1);
		this.villagerBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, parFloat1 + 0.5F);
		this.villagerArms = (new ModelRenderer(this)).setTextureSize(parInt1, parInt2);
		this.villagerArms.setRotationPoint(0.0F, 0.0F + parFloat2 + 2.0F, 0.0F);
		this.villagerArms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, parFloat1);
		this.villagerArms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, parFloat1);
		this.villagerArms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, parFloat1);
		this.rightVillagerLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(parInt1, parInt2);
		this.rightVillagerLeg.setRotationPoint(-2.0F, 12.0F + parFloat2, 0.0F);
		this.rightVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, parFloat1);
		this.leftVillagerLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(parInt1, parInt2);
		this.leftVillagerLeg.mirror = true;
		this.leftVillagerLeg.setRotationPoint(2.0F, 12.0F + parFloat2, 0.0F);
		this.leftVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, parFloat1);
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.villagerHead.render(f5);
		this.villagerBody.render(f5);
		this.rightVillagerLeg.render(f5);
		this.leftVillagerLeg.render(f5);
		this.villagerArms.render(f5);
	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float f, float f1, float var3, float f2, float f3, float var6, Entity var7) {
		this.villagerHead.rotateAngleY = f2 / 57.295776F;
		this.villagerHead.rotateAngleX = f3 / 57.295776F;
		this.villagerArms.rotationPointY = 3.0F;
		this.villagerArms.rotationPointZ = -1.0F;
		this.villagerArms.rotateAngleX = -0.75F;
		this.rightVillagerLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1 * 0.5F;
		this.leftVillagerLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.4F * f1 * 0.5F;
		this.rightVillagerLeg.rotateAngleY = 0.0F;
		this.leftVillagerLeg.rotateAngleY = 0.0F;
	}
}