package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;

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
public class ModelIronGolem extends ModelBase {
	public ModelRenderer ironGolemHead;
	public ModelRenderer ironGolemBody;
	public ModelRenderer ironGolemRightArm;
	public ModelRenderer ironGolemLeftArm;
	public ModelRenderer ironGolemLeftLeg;
	public ModelRenderer ironGolemRightLeg;

	public ModelIronGolem() {
		this(0.0F);
	}

	public ModelIronGolem(float parFloat1) {
		this(parFloat1, -7.0F);
	}

	public ModelIronGolem(float parFloat1, float parFloat2) {
		short short1 = 128;
		short short2 = 128;
		this.ironGolemHead = (new ModelRenderer(this)).setTextureSize(short1, short2);
		this.ironGolemHead.setRotationPoint(0.0F, 0.0F + parFloat2, -2.0F);
		this.ironGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, parFloat1);
		this.ironGolemHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4, 2, parFloat1);
		this.ironGolemBody = (new ModelRenderer(this)).setTextureSize(short1, short2);
		this.ironGolemBody.setRotationPoint(0.0F, 0.0F + parFloat2, 0.0F);
		this.ironGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, parFloat1);
		this.ironGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, parFloat1 + 0.5F);
		this.ironGolemRightArm = (new ModelRenderer(this)).setTextureSize(short1, short2);
		this.ironGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		this.ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, parFloat1);
		this.ironGolemLeftArm = (new ModelRenderer(this)).setTextureSize(short1, short2);
		this.ironGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		this.ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, parFloat1);
		this.ironGolemLeftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(short1, short2);
		this.ironGolemLeftLeg.setRotationPoint(-4.0F, 18.0F + parFloat2, 0.0F);
		this.ironGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, parFloat1);
		this.ironGolemRightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(short1, short2);
		this.ironGolemRightLeg.mirror = true;
		this.ironGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 18.0F + parFloat2, 0.0F);
		this.ironGolemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, parFloat1);
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.ironGolemHead.render(f5);
		this.ironGolemBody.render(f5);
		this.ironGolemLeftLeg.render(f5);
		this.ironGolemRightLeg.render(f5);
		this.ironGolemRightArm.render(f5);
		this.ironGolemLeftArm.render(f5);
	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float f, float f1, float var3, float f2, float f3, float var6, Entity var7) {
		this.ironGolemHead.rotateAngleY = f2 / 57.295776F;
		this.ironGolemHead.rotateAngleX = f3 / 57.295776F;
		this.ironGolemLeftLeg.rotateAngleX = -1.5F * this.func_78172_a(f, 13.0F) * f1;
		this.ironGolemRightLeg.rotateAngleX = 1.5F * this.func_78172_a(f, 13.0F) * f1;
		this.ironGolemLeftLeg.rotateAngleY = 0.0F;
		this.ironGolemRightLeg.rotateAngleY = 0.0F;
	}

	/**+
	 * Used for easily adding entity-dependent animations. The
	 * second and third float params here are the same second and
	 * third as in the setRotationAngles method.
	 */
	public void setLivingAnimations(EntityLivingBase entitylivingbase, float f, float f1, float f2) {
		EntityIronGolem entityirongolem = (EntityIronGolem) entitylivingbase;
		int i = entityirongolem.getAttackTimer();
		if (i > 0) {
			this.ironGolemRightArm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float) i - f2, 10.0F);
			this.ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float) i - f2, 10.0F);
		} else {
			int j = entityirongolem.getHoldRoseTick();
			if (j > 0) {
				this.ironGolemRightArm.rotateAngleX = -0.8F + 0.025F * this.func_78172_a((float) j, 70.0F);
				this.ironGolemLeftArm.rotateAngleX = 0.0F;
			} else {
				this.ironGolemRightArm.rotateAngleX = (-0.2F + 1.5F * this.func_78172_a(f, 13.0F)) * f1;
				this.ironGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * this.func_78172_a(f, 13.0F)) * f1;
			}
		}

	}

	private float func_78172_a(float parFloat1, float parFloat2) {
		return (Math.abs(parFloat1 % parFloat2 - parFloat2 * 0.5F) - parFloat2 * 0.25F) / (parFloat2 * 0.25F);
	}
}