package net.minecraft.client.renderer.entity.layers;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
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
public class LayerSheepWool implements LayerRenderer<EntitySheep> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
	private final RenderSheep sheepRenderer;
	private final ModelSheep1 sheepModel = new ModelSheep1();

	public LayerSheepWool(RenderSheep sheepRendererIn) {
		this.sheepRenderer = sheepRendererIn;
	}

	public void doRenderLayer(EntitySheep entitysheep, float f, float f1, float f2, float f3, float f4, float f5,
			float f6) {
		if (!entitysheep.getSheared() && !entitysheep.isInvisible()) {
			this.sheepRenderer.bindTexture(TEXTURE);
			if (entitysheep.hasCustomName() && "jeb_".equals(entitysheep.getCustomNameTag())) {
				boolean flag = true;
				int i = entitysheep.ticksExisted / 25 + entitysheep.getEntityId();
				int j = EnumDyeColor.META_LOOKUP.length;
				int k = i % j;
				int l = (i + 1) % j;
				float f7 = ((float) (entitysheep.ticksExisted % 25) + f2) / 25.0F;
				float[] afloat1 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(k));
				float[] afloat2 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(l));
				GlStateManager.color(afloat1[0] * (1.0F - f7) + afloat2[0] * f7,
						afloat1[1] * (1.0F - f7) + afloat2[1] * f7, afloat1[2] * (1.0F - f7) + afloat2[2] * f7);
			} else {
				float[] afloat = EntitySheep.func_175513_a(entitysheep.getFleeceColor());
				GlStateManager.color(afloat[0], afloat[1], afloat[2]);
			}

			this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
			this.sheepModel.setLivingAnimations(entitysheep, f, f1, f2);
			this.sheepModel.render(entitysheep, f, f1, f3, f4, f5, f6);
		}
	}

	public boolean shouldCombineTextures() {
		return true;
	}
}