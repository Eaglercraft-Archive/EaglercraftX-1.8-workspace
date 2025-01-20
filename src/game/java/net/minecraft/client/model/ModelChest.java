package net.minecraft.client.model;

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
public class ModelChest extends ModelBase {
	/**+
	 * The chest lid in the chest's model.
	 */
	public ModelRenderer chestLid = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
	public ModelRenderer chestBelow;
	public ModelRenderer chestKnob;

	public ModelChest() {
		this.chestLid.addBox(0.0F, -5.0F, -14.0F, 14, 5, 14, 0.0F);
		this.chestLid.rotationPointX = 1.0F;
		this.chestLid.rotationPointY = 7.0F;
		this.chestLid.rotationPointZ = 15.0F;
		this.chestKnob = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
		this.chestKnob.addBox(-1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F);
		this.chestKnob.rotationPointX = 8.0F;
		this.chestKnob.rotationPointY = 7.0F;
		this.chestKnob.rotationPointZ = 15.0F;
		this.chestBelow = (new ModelRenderer(this, 0, 19)).setTextureSize(64, 64);
		this.chestBelow.addBox(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
		this.chestBelow.rotationPointX = 1.0F;
		this.chestBelow.rotationPointY = 6.0F;
		this.chestBelow.rotationPointZ = 1.0F;
	}

	/**+
	 * This method renders out all parts of the chest model.
	 */
	public void renderAll() {
		this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
		this.chestLid.render(0.0625F);
		this.chestKnob.render(0.0625F);
		this.chestBelow.render(0.0625F);
	}
}