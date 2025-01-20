package net.minecraft.client.renderer.tileentity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.NameTagRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;

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
public class RenderItemFrame extends Render<EntityItemFrame> {
	private static final ResourceLocation mapBackgroundTextures = new ResourceLocation(
			"textures/map/map_background.png");
	private final Minecraft mc = Minecraft.getMinecraft();
	private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
	private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
	private RenderItem itemRenderer;

	public RenderItemFrame(RenderManager renderManagerIn, RenderItem itemRendererIn) {
		super(renderManagerIn);
		this.itemRenderer = itemRendererIn;
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
	public void doRender(EntityItemFrame entityitemframe, double d0, double d1, double d2, float var8, float var9) {
		GlStateManager.pushMatrix();
		BlockPos blockpos = entityitemframe.getHangingPosition();
		double d3 = (double) blockpos.getX() - entityitemframe.posX + d0;
		double d4 = (double) blockpos.getY() - entityitemframe.posY + d1;
		double d5 = (double) blockpos.getZ() - entityitemframe.posZ + d2;
		GlStateManager.translate(d3 + 0.5D, d4 + 0.5D, d5 + 0.5D);
		GlStateManager.rotate(180.0F - entityitemframe.rotationYaw, 0.0F, 1.0F, 0.0F);
		this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
		ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
		IBakedModel ibakedmodel;
		if (entityitemframe.getDisplayedItem() != null
				&& entityitemframe.getDisplayedItem().getItem() == Items.filled_map) {
			ibakedmodel = modelmanager.getModel(this.mapModel);
		} else {
			ibakedmodel = modelmanager.getModel(this.itemFrameModel);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableLighting();
		GlStateManager.enableColorMaterial();
		GlStateManager.popMatrix();
		GlStateManager.translate(0.0F, 0.0F, 0.4375F);
		this.renderItem(entityitemframe);
		GlStateManager.popMatrix();
		this.renderName(entityitemframe,
				d0 + (double) ((float) entityitemframe.facingDirection.getFrontOffsetX() * 0.3F), d1 - 0.25D,
				d2 + (double) ((float) entityitemframe.facingDirection.getFrontOffsetZ() * 0.3F));
		GlStateManager.enableLighting();
		GlStateManager.enableColorMaterial();
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityItemFrame var1) {
		return null;
	}

	private void renderItem(EntityItemFrame itemFrame) {
		ItemStack itemstack = itemFrame.getDisplayedItem();
		if (itemstack != null) {
			EntityItem entityitem = new EntityItem(itemFrame.worldObj, 0.0D, 0.0D, 0.0D, itemstack);
			Item item = entityitem.getEntityItem().getItem();
			entityitem.getEntityItem().stackSize = 1;
			entityitem.hoverStart = 0.0F;
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			int i = itemFrame.getRotation();
			if (item == Items.filled_map) {
				i = i % 4 * 2;
			}

			GlStateManager.rotate((float) i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
			if (item == Items.filled_map) {
				this.renderManager.renderEngine.bindTexture(mapBackgroundTextures);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
				float f = 0.0078125F;
				GlStateManager.scale(f, f, f);
				GlStateManager.translate(-64.0F, -64.0F, 0.0F);
				MapData mapdata = Items.filled_map.getMapData(entityitem.getEntityItem(), itemFrame.worldObj);
				GlStateManager.translate(0.0F, 0.0F, -1.0F);
				if (mapdata != null) {
					this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
				}
			} else {
				boolean emissive = itemFrame.eaglerEmissiveFlag;
				itemFrame.eaglerEmissiveFlag = false;
				if (emissive) {
					DeferredStateManager.setEmissionConstant(1.0f);
				}
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				if (!this.itemRenderer.shouldRenderItemIn3D(entityitem.getEntityItem()) || item instanceof ItemSkull) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				GlStateManager.pushLightCoords();
				RenderHelper.enableStandardItemLighting();
				this.itemRenderer.func_181564_a(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
				RenderHelper.disableStandardItemLighting();
				GlStateManager.popLightCoords();
				if (emissive) {
					DeferredStateManager.setEmissionConstant(0.0f);
				}
			}

			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}

	protected void renderName(EntityItemFrame entityitemframe, double d0, double d1, double d2) {
		if (Minecraft.isGuiEnabled() && entityitemframe.getDisplayedItem() != null
				&& entityitemframe.getDisplayedItem().hasDisplayName()
				&& this.renderManager.pointedEntity == entityitemframe) {
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			double d3 = entityitemframe.getDistanceSqToEntity(this.renderManager.livingPlayer);
			float f2 = entityitemframe.isSneaking() ? 32.0F : 64.0F;
			if (d3 < (double) (f2 * f2)) {
				String s = entityitemframe.getDisplayedItem().getDisplayNameProfanityFilter();
				if (entityitemframe.isSneaking()) {
					if (DeferredStateManager.isInDeferredPass()) {
						NameTagRenderer.renderNameTag(entityitemframe, null, d0, d1, d2, -69);
						return;
					}
					FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
					GlStateManager.pushMatrix();
					GlStateManager.translate((float) d0 + 0.0F, (float) d1 + entityitemframe.height + 0.5F, (float) d2);
					EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
					GlStateManager.scale(-f1, -f1, f1);
					GlStateManager.disableLighting();
					GlStateManager.translate(0.0F, 0.25F / f1, 0.0F);
					GlStateManager.depthMask(false);
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
					Tessellator tessellator = Tessellator.getInstance();
					WorldRenderer worldrenderer = tessellator.getWorldRenderer();
					int i = fontrenderer.getStringWidth(s) / 2;
					GlStateManager.disableTexture2D();
					worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
					worldrenderer.pos((double) (-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
					worldrenderer.pos((double) (-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
					worldrenderer.pos((double) (i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
					worldrenderer.pos((double) (i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
					tessellator.draw();
					GlStateManager.enableTexture2D();
					GlStateManager.depthMask(true);
					fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
					GlStateManager.enableLighting();
					GlStateManager.disableBlend();
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					GlStateManager.popMatrix();
				} else {
					this.renderLivingLabel(entityitemframe, s, d0, d1, d2, 64);
				}
			}
		}

	}
}