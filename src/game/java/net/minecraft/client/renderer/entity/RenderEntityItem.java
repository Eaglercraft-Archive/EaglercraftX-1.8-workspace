package net.minecraft.client.renderer.entity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
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
public class RenderEntityItem extends Render<EntityItem> {
	private final RenderItem itemRenderer;
	private EaglercraftRandom field_177079_e = new EaglercraftRandom();

	public RenderEntityItem(RenderManager renderManagerIn, RenderItem parRenderItem) {
		super(renderManagerIn);
		this.itemRenderer = parRenderItem;
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
	}

	private int func_177077_a(EntityItem itemIn, double parDouble1, double parDouble2, double parDouble3,
			float parFloat1, IBakedModel parIBakedModel) {
		ItemStack itemstack = itemIn.getEntityItem();
		Item item = itemstack.getItem();
		if (item == null) {
			return 0;
		} else {
			boolean flag = parIBakedModel.isGui3d();
			int i = this.func_177078_a(itemstack);
			float f = 0.25F;
			float f1 = MathHelper.sin(((float) itemIn.getAge() + parFloat1) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F;
			float f2 = parIBakedModel.getItemCameraTransforms()
					.getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
			GlStateManager.translate((float) parDouble1, (float) parDouble2 + f1 + 0.25F * f2, (float) parDouble3);
			if (flag || this.renderManager.options != null) {
				float f3 = (((float) itemIn.getAge() + parFloat1) / 20.0F + itemIn.hoverStart) * 57.295776F;
				GlStateManager.rotate(f3, 0.0F, 1.0F, 0.0F);
			}

			if (!flag) {
				float f6 = 0.0F * (float) (i - 1) * 0.5F;
				float f4 = 0.0F * (float) (i - 1) * 0.5F;
				float f5 = -0.046875F * (float) (i - 1) * 0.5F;
				GlStateManager.translate(f6, f4, f5);
			}

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			return i;
		}
	}

	private int func_177078_a(ItemStack stack) {
		byte b0 = 1;
		if (stack.stackSize > 48) {
			b0 = 5;
		} else if (stack.stackSize > 32) {
			b0 = 4;
		} else if (stack.stackSize > 16) {
			b0 = 3;
		} else if (stack.stackSize > 1) {
			b0 = 2;
		}

		return b0;
	}

	/**+
	 * Actually renders the given argument. This is a synthetic
	 * bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual
	 * work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity>) and this method has signature
	 * public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doe
	 */
	public void doRender(EntityItem entityitem, double d0, double d1, double d2, float f, float f1) {
		boolean emissive = entityitem.eaglerEmissiveFlag;
		entityitem.eaglerEmissiveFlag = false;
		ItemStack itemstack = entityitem.getEntityItem();
		this.field_177079_e.setSeed(187L);
		boolean flag = false;
		if (this.bindEntityTexture(entityitem)) {
			this.renderManager.renderEngine.getTexture(this.getEntityTexture(entityitem)).setBlurMipmap(false, false);
			flag = true;
		}

		if (emissive) {
			DeferredStateManager.setEmissionConstant(1.0f);
		}
		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(GL_GREATER, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.pushMatrix();
		IBakedModel ibakedmodel = this.itemRenderer.getItemModelMesher().getItemModel(itemstack);
		int i = this.func_177077_a(entityitem, d0, d1, d2, f1, ibakedmodel);

		for (int j = 0; j < i; ++j) {
			if (ibakedmodel.isGui3d()) {
				GlStateManager.pushMatrix();
				if (j > 0) {
					float f2 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float f3 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float f4 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
					GlStateManager.translate(f2, f3, f4);
				}

				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
				this.itemRenderer.renderItem(itemstack, ibakedmodel);
				GlStateManager.popMatrix();
			} else {
				GlStateManager.pushMatrix();
				ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
				this.itemRenderer.renderItem(itemstack, ibakedmodel);
				GlStateManager.popMatrix();
				float f5 = ibakedmodel.getItemCameraTransforms().ground.scale.x;
				float f6 = ibakedmodel.getItemCameraTransforms().ground.scale.y;
				float f7 = ibakedmodel.getItemCameraTransforms().ground.scale.z;
				GlStateManager.translate(0.0F * f5, 0.0F * f6, 0.046875F * f7);
			}
		}

		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		this.bindEntityTexture(entityitem);
		if (flag) {
			this.renderManager.renderEngine.getTexture(this.getEntityTexture(entityitem)).restoreLastBlurMipmap();
		}

		super.doRender(entityitem, d0, d1, d2, f, f1);
		if (emissive) {
			DeferredStateManager.setEmissionConstant(0.0f);
		}
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityItem var1) {
		return TextureMap.locationBlocksTexture;
	}
}