package net.minecraft.client.model;

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
public class ModelSquid extends ModelBase {
	ModelRenderer squidBody;
	ModelRenderer[] squidTentacles = new ModelRenderer[8];

	public ModelSquid() {
		byte b0 = -16;
		this.squidBody = new ModelRenderer(this, 0, 0);
		this.squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);
		this.squidBody.rotationPointY += (float) (24 + b0);

		for (int i = 0; i < this.squidTentacles.length; ++i) {
			this.squidTentacles[i] = new ModelRenderer(this, 48, 0);
			double d0 = (double) i * 3.141592653589793D * 2.0D / (double) this.squidTentacles.length;
			float f = (float) Math.cos(d0) * 5.0F;
			float f1 = (float) Math.sin(d0) * 5.0F;
			this.squidTentacles[i].addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2);
			this.squidTentacles[i].rotationPointX = f;
			this.squidTentacles[i].rotationPointZ = f1;
			this.squidTentacles[i].rotationPointY = (float) (31 + b0);
			d0 = (double) i * 3.141592653589793D * -2.0D / (double) this.squidTentacles.length + 1.5707963267948966D;
			this.squidTentacles[i].rotateAngleY = (float) d0;
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
		for (int i = 0; i < this.squidTentacles.length; ++i) {
			this.squidTentacles[i].rotateAngleX = f;
		}

	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.squidBody.render(f5);

		for (int i = 0; i < this.squidTentacles.length; ++i) {
			this.squidTentacles[i].render(f5);
		}

	}
}