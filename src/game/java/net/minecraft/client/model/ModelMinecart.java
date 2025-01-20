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
public class ModelMinecart extends ModelBase {
	public ModelRenderer[] sideModels = new ModelRenderer[7];

	public ModelMinecart() {
		this.sideModels[0] = new ModelRenderer(this, 0, 10);
		this.sideModels[1] = new ModelRenderer(this, 0, 0);
		this.sideModels[2] = new ModelRenderer(this, 0, 0);
		this.sideModels[3] = new ModelRenderer(this, 0, 0);
		this.sideModels[4] = new ModelRenderer(this, 0, 0);
		this.sideModels[5] = new ModelRenderer(this, 44, 10);
		byte b0 = 20;
		byte b1 = 8;
		byte b2 = 16;
		byte b3 = 4;
		this.sideModels[0].addBox((float) (-b0 / 2), (float) (-b2 / 2), -1.0F, b0, b2, 2, 0.0F);
		this.sideModels[0].setRotationPoint(0.0F, (float) b3, 0.0F);
		this.sideModels[5].addBox((float) (-b0 / 2 + 1), (float) (-b2 / 2 + 1), -1.0F, b0 - 2, b2 - 2, 1, 0.0F);
		this.sideModels[5].setRotationPoint(0.0F, (float) b3, 0.0F);
		this.sideModels[1].addBox((float) (-b0 / 2 + 2), (float) (-b1 - 1), -1.0F, b0 - 4, b1, 2, 0.0F);
		this.sideModels[1].setRotationPoint((float) (-b0 / 2 + 1), (float) b3, 0.0F);
		this.sideModels[2].addBox((float) (-b0 / 2 + 2), (float) (-b1 - 1), -1.0F, b0 - 4, b1, 2, 0.0F);
		this.sideModels[2].setRotationPoint((float) (b0 / 2 - 1), (float) b3, 0.0F);
		this.sideModels[3].addBox((float) (-b0 / 2 + 2), (float) (-b1 - 1), -1.0F, b0 - 4, b1, 2, 0.0F);
		this.sideModels[3].setRotationPoint(0.0F, (float) b3, (float) (-b2 / 2 + 1));
		this.sideModels[4].addBox((float) (-b0 / 2 + 2), (float) (-b1 - 1), -1.0F, b0 - 4, b1, 2, 0.0F);
		this.sideModels[4].setRotationPoint(0.0F, (float) b3, (float) (b2 / 2 - 1));
		this.sideModels[0].rotateAngleX = 1.5707964F;
		this.sideModels[1].rotateAngleY = 4.712389F;
		this.sideModels[2].rotateAngleY = 1.5707964F;
		this.sideModels[3].rotateAngleY = 3.1415927F;
		this.sideModels[5].rotateAngleX = -1.5707964F;
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity var1, float var2, float var3, float f, float var5, float var6, float f1) {
		this.sideModels[5].rotationPointY = 4.0F - f;

		for (int i = 0; i < 6; ++i) {
			this.sideModels[i].render(f1);
		}

	}
}