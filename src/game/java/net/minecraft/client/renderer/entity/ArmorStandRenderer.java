package net.minecraft.client.renderer.entity;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;

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
public class ArmorStandRenderer extends RendererLivingEntity<EntityArmorStand> {
	/**+
	 * A constant instance of the armor stand texture, wrapped
	 * inside a ResourceLocation wrapper.
	 */
	public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation(
			"textures/entity/armorstand/wood.png");

	public ArmorStandRenderer(RenderManager parRenderManager) {
		super(parRenderManager, new ModelArmorStand(), 0.0F);
		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
			protected void initArmor() {
				this.field_177189_c = new ModelArmorStandArmor(0.5F);
				this.field_177186_d = new ModelArmorStandArmor(1.0F);
			}
		};
		this.addLayer(layerbipedarmor);
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityArmorStand var1) {
		return TEXTURE_ARMOR_STAND;
	}

	public ModelArmorStand getMainModel() {
		return (ModelArmorStand) super.getMainModel();
	}

	protected void rotateCorpse(EntityArmorStand var1, float var2, float f, float var4) {
		GlStateManager.rotate(180.0F - f, 0.0F, 1.0F, 0.0F);
	}

	protected boolean canRenderName(EntityArmorStand entityarmorstand) {
		return entityarmorstand.getAlwaysRenderNameTag();
	}
}