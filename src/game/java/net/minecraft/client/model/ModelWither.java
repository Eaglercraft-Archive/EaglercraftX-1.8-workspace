package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
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
public class ModelWither extends ModelBase {
	private ModelRenderer[] field_82905_a;
	private ModelRenderer[] field_82904_b;

	public ModelWither(float parFloat1) {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.field_82905_a = new ModelRenderer[3];
		this.field_82905_a[0] = new ModelRenderer(this, 0, 16);
		this.field_82905_a[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3, parFloat1);
		this.field_82905_a[1] = (new ModelRenderer(this)).setTextureSize(this.textureWidth, this.textureHeight);
		this.field_82905_a[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
		this.field_82905_a[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3, 10, 3, parFloat1);
		this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2, parFloat1);
		this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2, parFloat1);
		this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2, parFloat1);
		this.field_82905_a[2] = new ModelRenderer(this, 12, 22);
		this.field_82905_a[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, parFloat1);
		this.field_82904_b = new ModelRenderer[3];
		this.field_82904_b[0] = new ModelRenderer(this, 0, 0);
		this.field_82904_b[0].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, parFloat1);
		this.field_82904_b[1] = new ModelRenderer(this, 32, 0);
		this.field_82904_b[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, parFloat1);
		this.field_82904_b[1].rotationPointX = -8.0F;
		this.field_82904_b[1].rotationPointY = 4.0F;
		this.field_82904_b[2] = new ModelRenderer(this, 32, 0);
		this.field_82904_b[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, parFloat1);
		this.field_82904_b[2].rotationPointX = 10.0F;
		this.field_82904_b[2].rotationPointY = 4.0F;
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		for (int i = 0; i < this.field_82904_b.length; ++i) {
			this.field_82904_b[i].render(f5);
		}

		for (int i = 0; i < this.field_82905_a.length; ++i) {
			this.field_82905_a[i].render(f5);
		}

	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float var1, float var2, float f, float f1, float f2, float var6, Entity var7) {
		float f3 = MathHelper.cos(f * 0.1F);
		this.field_82905_a[1].rotateAngleX = (0.065F + 0.05F * f3) * 3.1415927F;
		this.field_82905_a[2].setRotationPoint(-2.0F, 6.9F + MathHelper.cos(this.field_82905_a[1].rotateAngleX) * 10.0F,
				-0.5F + MathHelper.sin(this.field_82905_a[1].rotateAngleX) * 10.0F);
		this.field_82905_a[2].rotateAngleX = (0.265F + 0.1F * f3) * 3.1415927F;
		this.field_82904_b[0].rotateAngleY = f1 / 57.295776F;
		this.field_82904_b[0].rotateAngleX = f2 / 57.295776F;
	}

	/**+
	 * Used for easily adding entity-dependent animations. The
	 * second and third float params here are the same second and
	 * third as in the setRotationAngles method.
	 */
	public void setLivingAnimations(EntityLivingBase entitylivingbase, float var2, float var3, float var4) {
		EntityWither entitywither = (EntityWither) entitylivingbase;

		for (int i = 1; i < 3; ++i) {
			this.field_82904_b[i].rotateAngleY = (entitywither.func_82207_a(i - 1) - entitylivingbase.renderYawOffset)
					/ 57.295776F;
			this.field_82904_b[i].rotateAngleX = entitywither.func_82210_r(i - 1) / 57.295776F;
		}

	}
}