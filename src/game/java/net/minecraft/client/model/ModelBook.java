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
public class ModelBook extends ModelBase {
	/**+
	 * Right cover renderer (when facing the book)
	 */
	public ModelRenderer coverRight = (new ModelRenderer(this)).setTextureOffset(0, 0).addBox(-6.0F, -5.0F, 0.0F, 6, 10,
			0);
	/**+
	 * Left cover renderer (when facing the book)
	 */
	public ModelRenderer coverLeft = (new ModelRenderer(this)).setTextureOffset(16, 0).addBox(0.0F, -5.0F, 0.0F, 6, 10,
			0);
	/**+
	 * The right pages renderer (when facing the book)
	 */
	public ModelRenderer pagesRight = (new ModelRenderer(this)).setTextureOffset(0, 10).addBox(0.0F, -4.0F, -0.99F, 5,
			8, 1);
	/**+
	 * The left pages renderer (when facing the book)
	 */
	public ModelRenderer pagesLeft = (new ModelRenderer(this)).setTextureOffset(12, 10).addBox(0.0F, -4.0F, -0.01F, 5,
			8, 1);
	/**+
	 * Right cover renderer (when facing the book)
	 */
	public ModelRenderer flippingPageRight = (new ModelRenderer(this)).setTextureOffset(24, 10).addBox(0.0F, -4.0F,
			0.0F, 5, 8, 0);
	/**+
	 * Right cover renderer (when facing the book)
	 */
	public ModelRenderer flippingPageLeft = (new ModelRenderer(this)).setTextureOffset(24, 10).addBox(0.0F, -4.0F, 0.0F,
			5, 8, 0);
	/**+
	 * The renderer of spine of the book
	 */
	public ModelRenderer bookSpine = (new ModelRenderer(this)).setTextureOffset(12, 0).addBox(-1.0F, -5.0F, 0.0F, 2, 10,
			0);

	public ModelBook() {
		this.coverRight.setRotationPoint(0.0F, 0.0F, -1.0F);
		this.coverLeft.setRotationPoint(0.0F, 0.0F, 1.0F);
		this.bookSpine.rotateAngleY = 1.5707964F;
	}

	/**+
	 * Sets the models various rotation angles then renders the
	 * model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.coverRight.render(f5);
		this.coverLeft.render(f5);
		this.bookSpine.render(f5);
		this.pagesRight.render(f5);
		this.pagesLeft.render(f5);
		this.flippingPageRight.render(f5);
		this.flippingPageLeft.render(f5);
	}

	/**+
	 * Sets the model's various rotation angles. For bipeds, par1
	 * and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so that arms and legs
	 * swing back and forth) and par2 represents how "far" arms and
	 * legs can swing at most.
	 */
	public void setRotationAngles(float f, float f1, float f2, float f3, float var5, float var6, Entity var7) {
		float f4 = (MathHelper.sin(f * 0.02F) * 0.1F + 1.25F) * f3;
		this.coverRight.rotateAngleY = 3.1415927F + f4;
		this.coverLeft.rotateAngleY = -f4;
		this.pagesRight.rotateAngleY = f4;
		this.pagesLeft.rotateAngleY = -f4;
		this.flippingPageRight.rotateAngleY = f4 - f4 * 2.0F * f1;
		this.flippingPageLeft.rotateAngleY = f4 - f4 * 2.0F * f2;
		this.pagesRight.rotationPointX = MathHelper.sin(f4);
		this.pagesLeft.rotationPointX = MathHelper.sin(f4);
		this.flippingPageRight.rotationPointX = MathHelper.sin(f4);
		this.flippingPageLeft.rotationPointX = MathHelper.sin(f4);
	}
}