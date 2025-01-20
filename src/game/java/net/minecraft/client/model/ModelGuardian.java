package net.minecraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

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
public class ModelGuardian extends ModelBase {
	private ModelRenderer guardianBody;
	private ModelRenderer guardianEye;
	private ModelRenderer[] guardianSpines;
	private ModelRenderer[] guardianTail;

	public ModelGuardian() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.guardianSpines = new ModelRenderer[12];
		this.guardianBody = new ModelRenderer(this);
		this.guardianBody.setTextureOffset(0, 0).addBox(-6.0F, 10.0F, -8.0F, 12, 12, 16);
		this.guardianBody.setTextureOffset(0, 28).addBox(-8.0F, 10.0F, -6.0F, 2, 12, 12);
		this.guardianBody.setTextureOffset(0, 28).addBox(6.0F, 10.0F, -6.0F, 2, 12, 12, true);
		this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 8.0F, -6.0F, 12, 2, 12);
		this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 22.0F, -6.0F, 12, 2, 12);

		for (int i = 0; i < this.guardianSpines.length; ++i) {
			this.guardianSpines[i] = new ModelRenderer(this, 0, 0);
			this.guardianSpines[i].addBox(-1.0F, -4.5F, -1.0F, 2, 9, 2);
			this.guardianBody.addChild(this.guardianSpines[i]);
		}

		this.guardianEye = new ModelRenderer(this, 8, 0);
		this.guardianEye.addBox(-1.0F, 15.0F, 0.0F, 2, 2, 1);
		this.guardianBody.addChild(this.guardianEye);
		this.guardianTail = new ModelRenderer[3];
		this.guardianTail[0] = new ModelRenderer(this, 40, 0);
		this.guardianTail[0].addBox(-2.0F, 14.0F, 7.0F, 4, 4, 8);
		this.guardianTail[1] = new ModelRenderer(this, 0, 54);
		this.guardianTail[1].addBox(0.0F, 14.0F, 0.0F, 3, 3, 7);
		this.guardianTail[2] = new ModelRenderer(this);
		this.guardianTail[2].setTextureOffset(41, 32).addBox(0.0F, 14.0F, 0.0F, 2, 2, 6);
		this.guardianTail[2].setTextureOffset(25, 19).addBox(1.0F, 10.5F, 3.0F, 1, 9, 9);
		this.guardianBody.addChild(this.guardianTail[0]);
		this.guardianTail[0].addChild(this.guardianTail[1]);
		this.guardianTail[1].addChild(this.guardianTail[2]);
	}

	public int func_178706_a() {
		return 54;
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.guardianBody.render(f5);
	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float var1, float var2, float f, float f1, float f2, float var6, Entity entity) {
		EntityGuardian entityguardian = (EntityGuardian) entity;
		float f3 = f - (float) entityguardian.ticksExisted;
		this.guardianBody.rotateAngleY = f1 / 57.295776F;
		this.guardianBody.rotateAngleX = f2 / 57.295776F;
		float[] afloat = new float[] { 1.75F, 0.25F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F, 1.25F, 0.75F, 0.0F, 0.0F };
		float[] afloat1 = new float[] { 0.0F, 0.0F, 0.0F, 0.0F, 0.25F, 1.75F, 1.25F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F };
		float[] afloat2 = new float[] { 0.0F, 0.0F, 0.25F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.75F, 1.25F };
		float[] afloat3 = new float[] { 0.0F, 0.0F, 8.0F, -8.0F, -8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 8.0F, -8.0F };
		float[] afloat4 = new float[] { -8.0F, -8.0F, -8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F };
		float[] afloat5 = new float[] { 8.0F, -8.0F, 0.0F, 0.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F };
		float f4 = (1.0F - entityguardian.func_175469_o(f3)) * 0.55F;

		for (int i = 0; i < 12; ++i) {
			this.guardianSpines[i].rotateAngleX = 3.1415927F * afloat[i];
			this.guardianSpines[i].rotateAngleY = 3.1415927F * afloat1[i];
			this.guardianSpines[i].rotateAngleZ = 3.1415927F * afloat2[i];
			this.guardianSpines[i].rotationPointX = afloat3[i]
					* (1.0F + MathHelper.cos(f * 1.5F + (float) i) * 0.01F - f4);
			this.guardianSpines[i].rotationPointY = 16.0F
					+ afloat4[i] * (1.0F + MathHelper.cos(f * 1.5F + (float) i) * 0.01F - f4);
			this.guardianSpines[i].rotationPointZ = afloat5[i]
					* (1.0F + MathHelper.cos(f * 1.5F + (float) i) * 0.01F - f4);
		}

		this.guardianEye.rotationPointZ = -8.25F;
		Object object = Minecraft.getMinecraft().getRenderViewEntity();
		if (entityguardian.hasTargetedEntity()) {
			object = entityguardian.getTargetedEntity();
		}

		if (object != null) {
			Vec3 vec3 = ((Entity) object).getPositionEyes(0.0F);
			Vec3 vec31 = entity.getPositionEyes(0.0F);
			double d0 = vec3.yCoord - vec31.yCoord;
			if (d0 > 0.0D) {
				this.guardianEye.rotationPointY = 0.0F;
			} else {
				this.guardianEye.rotationPointY = 1.0F;
			}

			Vec3 vec32 = entity.getLook(0.0F);
			vec32 = new Vec3(vec32.xCoord, 0.0D, vec32.zCoord);
			Vec3 vec33 = (new Vec3(vec31.xCoord - vec3.xCoord, 0.0D, vec31.zCoord - vec3.zCoord)).normalize()
					.rotateYaw(1.5707964F);
			double d1 = vec32.dotProduct(vec33);
			this.guardianEye.rotationPointX = MathHelper.sqrt_float((float) Math.abs(d1)) * 2.0F
					* (float) Math.signum(d1);
		}

		this.guardianEye.showModel = true;
		float f5 = entityguardian.func_175471_a(f3);
		this.guardianTail[0].rotateAngleY = MathHelper.sin(f5) * 3.1415927F * 0.05F;
		this.guardianTail[1].rotateAngleY = MathHelper.sin(f5) * 3.1415927F * 0.1F;
		this.guardianTail[1].rotationPointX = -1.5F;
		this.guardianTail[1].rotationPointY = 0.5F;
		this.guardianTail[1].rotationPointZ = 14.0F;
		this.guardianTail[2].rotateAngleY = MathHelper.sin(f5) * 3.1415927F * 0.15F;
		this.guardianTail[2].rotationPointX = 0.5F;
		this.guardianTail[2].rotationPointY = 0.5F;
		this.guardianTail[2].rotationPointZ = 6.0F;
	}
}