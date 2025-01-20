package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;

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
public class ModelMagmaCube extends ModelBase {
	ModelRenderer[] segments = new ModelRenderer[8];
	ModelRenderer core;

	public ModelMagmaCube() {
		for (int i = 0; i < this.segments.length; ++i) {
			byte b0 = 0;
			int j = i;
			if (i == 2) {
				b0 = 24;
				j = 10;
			} else if (i == 3) {
				b0 = 24;
				j = 19;
			}

			this.segments[i] = new ModelRenderer(this, b0, j);
			this.segments[i].addBox(-4.0F, (float) (16 + i), -4.0F, 8, 1, 8);
		}

		this.core = new ModelRenderer(this, 0, 16);
		this.core.addBox(-2.0F, 18.0F, -2.0F, 4, 4, 4);
	}

	/**+
	 * Used for easily adding entity-dependent animations. The
	 * second and third float params here are the same second and
	 * third as in the setRotationAngles method.
	 */
	public void setLivingAnimations(EntityLivingBase entitylivingbase, float var2, float var3, float f) {
		EntityMagmaCube entitymagmacube = (EntityMagmaCube) entitylivingbase;
		float f1 = entitymagmacube.prevSquishFactor
				+ (entitymagmacube.squishFactor - entitymagmacube.prevSquishFactor) * f;
		if (f1 < 0.0F) {
			f1 = 0.0F;
		}

		for (int i = 0; i < this.segments.length; ++i) {
			this.segments[i].rotationPointY = (float) (-(4 - i)) * f1 * 1.7F;
		}

	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.core.render(f5);

		for (int i = 0; i < this.segments.length; ++i) {
			this.segments[i].render(f5);
		}

	}
}