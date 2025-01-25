package net.minecraft.client.renderer.entity.layers;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.Map;

import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.optifine.Config;
import net.optifine.CustomItems;

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
public abstract class LayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase> {
	protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation(
			"textures/misc/enchanted_item_glint.png");
	protected T field_177189_c;
	protected T field_177186_d;
	private final RendererLivingEntity<?> renderer;
	private float alpha = 1.0F;
	private float colorR = 1.0F;
	private float colorG = 1.0F;
	private float colorB = 1.0F;
	private boolean field_177193_i;
	private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

	public LayerArmorBase(RendererLivingEntity<?> rendererIn) {
		this.renderer = rendererIn;
		this.initArmor();
	}

	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float partialTicks, float scale, float parFloat3,
			float parFloat4, float parFloat5, float parFloat6, float parFloat7) {
		this.renderLayer(entitylivingbaseIn, partialTicks, scale, parFloat3, parFloat4, parFloat5, parFloat6, parFloat7,
				4);
		this.renderLayer(entitylivingbaseIn, partialTicks, scale, parFloat3, parFloat4, parFloat5, parFloat6, parFloat7,
				3);
		this.renderLayer(entitylivingbaseIn, partialTicks, scale, parFloat3, parFloat4, parFloat5, parFloat6, parFloat7,
				2);
		this.renderLayer(entitylivingbaseIn, partialTicks, scale, parFloat3, parFloat4, parFloat5, parFloat6, parFloat7,
				1);
	}

	public boolean shouldCombineTextures() {
		return false;
	}

	private void renderLayer(EntityLivingBase entitylivingbaseIn, float armorSlot, float parFloat2, float parFloat3,
			float parFloat4, float parFloat5, float parFloat6, float parFloat7, int parInt1) {
		ItemStack itemstack = this.getCurrentArmor(entitylivingbaseIn, parInt1);
		if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
			ItemArmor itemarmor = (ItemArmor) itemstack.getItem();
			ModelBase modelbase = this.func_177175_a(parInt1);
			modelbase.setModelAttributes(this.renderer.getMainModel());
			modelbase.setLivingAnimations(entitylivingbaseIn, armorSlot, parFloat2, parFloat3);
			this.func_177179_a((T) modelbase, parInt1);
			boolean flag = this.isSlotForLeggings(parInt1);

			if (!Config.isCustomItems()
					|| !CustomItems.bindCustomArmorTexture(itemstack, flag ? 2 : 1, (String) null)) {
				this.renderer.bindTexture(this.getArmorResource(itemarmor, flag));
			}

			DeferredStateManager.setDefaultMaterialConstants();
			switch (itemarmor.getArmorMaterial()) {
			case CHAIN:
			case IRON:
				DeferredStateManager.setRoughnessConstant(0.123f);
				DeferredStateManager.setMetalnessConstant(0.902f);
				break;
			case GOLD:
				DeferredStateManager.setRoughnessConstant(0.108f);
				DeferredStateManager.setMetalnessConstant(0.907f);
				break;
			case DIAMOND:
				DeferredStateManager.setRoughnessConstant(0.078f);
				DeferredStateManager.setMetalnessConstant(0.588f);
				break;
			default:
				break;
			}
			switch (itemarmor.getArmorMaterial()) {
			case LEATHER:
				int i = itemarmor.getColor(itemstack);
				float f = (float) (i >> 16 & 255) / 255.0F;
				float f1 = (float) (i >> 8 & 255) / 255.0F;
				float f2 = (float) (i & 255) / 255.0F;
				GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
				modelbase.render(entitylivingbaseIn, armorSlot, parFloat2, parFloat4, parFloat5, parFloat6, parFloat7);
				if (!Config.isCustomItems()
						|| !CustomItems.bindCustomArmorTexture(itemstack, flag ? 2 : 1, "overlay")) {
					this.renderer.bindTexture(this.getArmorResource(itemarmor, flag, "overlay"));
				}
			case CHAIN:
			case IRON:
			case GOLD:
			case DIAMOND:
				GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
				modelbase.render(entitylivingbaseIn, armorSlot, parFloat2, parFloat4, parFloat5, parFloat6, parFloat7);
				DeferredStateManager.setDefaultMaterialConstants();
			default:
				if (!this.field_177193_i && itemstack.isItemEnchanted()) {
					if (DeferredStateManager.isInDeferredPass()) {
						if (!DeferredStateManager.isEnableShadowRender()
								&& DeferredStateManager.forwardCallbackHandler != null) {
							final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
							final float lx = GlStateManager.getTexCoordX(1), ly = GlStateManager.getTexCoordY(1);
							DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(
									entitylivingbaseIn, EaglerDeferredPipeline.instance.getPartialTicks()) {
								@Override
								public void draw(PassType pass) {
									if (pass == PassType.MAIN) {
										DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
									}
									EntityRenderer.enableLightmapStatic();
									float f = 0.55f;
									GlStateManager.color(1.5F * f, 0.5F * f, 1.5F * f, 1.0F);
									DeferredStateManager.setDefaultMaterialConstants();
									DeferredStateManager.setRoughnessConstant(0.05f);
									DeferredStateManager.setMetalnessConstant(0.01f);
									GlStateManager.pushMatrix();
									GlStateManager.loadMatrix(mat);
									GlStateManager.texCoords2DDirect(1, lx, ly);
									GlStateManager.enableBlend();
									GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, GL_ZERO, GL_ONE);
									modelbase.setModelAttributes(LayerArmorBase.this.renderer.getMainModel());
									modelbase.setLivingAnimations(entitylivingbaseIn, armorSlot, parFloat2, parFloat3);
									LayerArmorBase.this.func_177179_a((T) modelbase, parInt1);
									LayerArmorBase.this.func_177183_a(entitylivingbaseIn, (T) modelbase, armorSlot,
											parFloat2, parFloat3, parFloat4, parFloat5, parFloat6, parFloat7,
											itemstack);
									DeferredStateManager.setHDRTranslucentPassBlendFunc();
									GlStateManager.enableBlend();
									GlStateManager.popMatrix();
									EntityRenderer.disableLightmapStatic();
									GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
								}
							});
						}
						break;
					}
					this.func_177183_a(entitylivingbaseIn, (T) modelbase, armorSlot, parFloat2, parFloat3, parFloat4,
							parFloat5, parFloat6, parFloat7, itemstack);
				}

			}
		}
	}

	public ItemStack getCurrentArmor(EntityLivingBase entitylivingbaseIn, int armorSlot) {
		return entitylivingbaseIn.getCurrentArmor(armorSlot - 1);
	}

	public T func_177175_a(int parInt1) {
		return (T) (this.isSlotForLeggings(parInt1) ? this.field_177189_c : this.field_177186_d);
	}

	private boolean isSlotForLeggings(int armorSlot) {
		return armorSlot == 2;
	}

	private void func_177183_a(EntityLivingBase entitylivingbaseIn, T modelbaseIn, float parFloat1, float parFloat2,
			float parFloat3, float parFloat4, float parFloat5, float parFloat6, float parFloat7, ItemStack itemstack) {
		if (Config.isCustomItems() && CustomItems.renderCustomArmorEffect(entitylivingbaseIn, itemstack, modelbaseIn,
				parFloat1, parFloat2, parFloat3, parFloat4, parFloat5, parFloat6, parFloat7)) {
			return;
		}
		float f = (float) entitylivingbaseIn.ticksExisted + parFloat3;
		this.renderer.bindTexture(ENCHANTED_ITEM_GLINT_RES);
		GlStateManager.enableBlend();
		GlStateManager.depthFunc(GL_EQUAL);
		GlStateManager.depthMask(false);
		float f1 = 0.5F;
		boolean d = !DeferredStateManager.isInDeferredPass();
		if (d) {
			GlStateManager.color(f1, f1, f1, 1.0F);
		}

		for (int i = 0; i < 2; ++i) {
			GlStateManager.disableLighting();
			float f2 = 0.76F;
			if (d) {
				GlStateManager.blendFunc(GL_SRC_COLOR, GL_ONE);
				GlStateManager.color(0.5F * f2, 0.25F * f2, 0.8F * f2, 1.0F);
			}
			GlStateManager.matrixMode(GL_TEXTURE);
			GlStateManager.loadIdentity();
			float f3 = 0.33333334F;
			GlStateManager.scale(f3, f3, f3);
			GlStateManager.rotate(30.0F - (float) i * 60.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate(0.0F, f * (0.001F + (float) i * 0.003F) * 20.0F, 0.0F);
			GlStateManager.matrixMode(GL_MODELVIEW);
			modelbaseIn.render(entitylivingbaseIn, parFloat1, parFloat2, parFloat4, parFloat5, parFloat6, parFloat7);
		}

		GlStateManager.matrixMode(GL_TEXTURE);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(GL_MODELVIEW);
		GlStateManager.enableLighting();
		GlStateManager.depthMask(true);
		GlStateManager.depthFunc(GL_LEQUAL);
		GlStateManager.disableBlend();
	}

	private ResourceLocation getArmorResource(ItemArmor parItemArmor, boolean parFlag) {
		return this.getArmorResource(parItemArmor, parFlag, (String) null);
	}

	private ResourceLocation getArmorResource(ItemArmor parItemArmor, boolean parFlag, String parString1) {
		String s = HString.format("textures/models/armor/%s_layer_%d%s.png",
				new Object[] { parItemArmor.getArmorMaterial().getName(), Integer.valueOf(parFlag ? 2 : 1),
						parString1 == null ? "" : HString.format("_%s", new Object[] { parString1 }) });
		ResourceLocation resourcelocation = (ResourceLocation) ARMOR_TEXTURE_RES_MAP.get(s);
		if (resourcelocation == null) {
			resourcelocation = new ResourceLocation(s);
			ARMOR_TEXTURE_RES_MAP.put(s, resourcelocation);
		}

		return resourcelocation;
	}

	protected abstract void initArmor();

	protected abstract void func_177179_a(T var1, int var2);
}