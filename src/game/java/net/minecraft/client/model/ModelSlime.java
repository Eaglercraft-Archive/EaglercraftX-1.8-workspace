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
public class ModelSlime extends ModelBase {
	ModelRenderer slimeBodies;
	ModelRenderer slimeRightEye;
	ModelRenderer slimeLeftEye;
	ModelRenderer slimeMouth;

	public ModelSlime(int parInt1) {
		this.slimeBodies = new ModelRenderer(this, 0, parInt1);
		this.slimeBodies.addBox(-4.0F, 16.0F, -4.0F, 8, 8, 8);
		if (parInt1 > 0) {
			this.slimeBodies = new ModelRenderer(this, 0, parInt1);
			this.slimeBodies.addBox(-3.0F, 17.0F, -3.0F, 6, 6, 6);
			this.slimeRightEye = new ModelRenderer(this, 32, 0);
			this.slimeRightEye.addBox(-3.25F, 18.0F, -3.5F, 2, 2, 2);
			this.slimeLeftEye = new ModelRenderer(this, 32, 4);
			this.slimeLeftEye.addBox(1.25F, 18.0F, -3.5F, 2, 2, 2);
			this.slimeMouth = new ModelRenderer(this, 32, 8);
			this.slimeMouth.addBox(0.0F, 21.0F, -3.5F, 1, 1, 1);
		}

	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.slimeBodies.render(f5);
		if (this.slimeRightEye != null) {
			this.slimeRightEye.render(f5);
			this.slimeLeftEye.render(f5);
			this.slimeMouth.render(f5);
		}

	}
}