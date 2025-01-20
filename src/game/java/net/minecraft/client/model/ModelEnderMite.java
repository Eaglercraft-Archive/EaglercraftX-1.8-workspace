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
public class ModelEnderMite extends ModelBase {
	private static final int[][] field_178716_a = new int[][] { { 4, 3, 2 }, { 6, 4, 5 }, { 3, 3, 1 }, { 1, 2, 1 } };
	private static final int[][] field_178714_b = new int[][] { { 0, 0 }, { 0, 5 }, { 0, 14 }, { 0, 18 } };
	private static final int field_178715_c = field_178716_a.length;
	private final ModelRenderer[] field_178713_d;

	public ModelEnderMite() {
		this.field_178713_d = new ModelRenderer[field_178715_c];
		float f = -3.5F;

		for (int i = 0; i < this.field_178713_d.length; ++i) {
			this.field_178713_d[i] = new ModelRenderer(this, field_178714_b[i][0], field_178714_b[i][1]);
			this.field_178713_d[i].addBox((float) field_178716_a[i][0] * -0.5F, 0.0F,
					(float) field_178716_a[i][2] * -0.5F, field_178716_a[i][0], field_178716_a[i][1],
					field_178716_a[i][2]);
			this.field_178713_d[i].setRotationPoint(0.0F, (float) (24 - field_178716_a[i][1]), f);
			if (i < this.field_178713_d.length - 1) {
				f += (float) (field_178716_a[i][2] + field_178716_a[i + 1][2]) * 0.5F;
			}
		}

	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		for (int i = 0; i < this.field_178713_d.length; ++i) {
			this.field_178713_d[i].render(f5);
		}

	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float var1, float var2, float f, float var4, float var5, float var6, Entity var7) {
		for (int i = 0; i < this.field_178713_d.length; ++i) {
			this.field_178713_d[i].rotateAngleY = MathHelper.cos(f * 0.9F + (float) i * 0.15F * 3.1415927F) * 3.1415927F
					* 0.01F * (float) (1 + Math.abs(i - 2));
			this.field_178713_d[i].rotationPointX = MathHelper.sin(f * 0.9F + (float) i * 0.15F * 3.1415927F)
					* 3.1415927F * 0.1F * (float) Math.abs(i - 2);
		}

	}
}