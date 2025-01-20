package net.minecraft.client.renderer.entity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.NameTagRenderer;
import net.lax1dude.eaglercraft.v1_8.voice.EnumVoiceChannelStatus;
import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;
import net.lax1dude.eaglercraft.v1_8.voice.VoiceTagRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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
public abstract class Render<T extends Entity> {
	private static final ResourceLocation shadowTextures = new ResourceLocation("textures/misc/shadow.png");
	protected final RenderManager renderManager;
	protected float shadowSize;
	/**+
	 * Determines the darkness of the object's shadow. Higher value
	 * makes a darker shadow.
	 */
	protected float shadowOpaque = 1.0F;

	protected Render(RenderManager renderManager) {
		this.renderManager = renderManager;
	}

	public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
		if (DeferredStateManager.isEnableShadowRender()) {
			return true;
		}
		AxisAlignedBB axisalignedbb = livingEntity.getEntityBoundingBox();
		if (axisalignedbb.func_181656_b() || axisalignedbb.getAverageEdgeLength() == 0.0D) {
			axisalignedbb = new AxisAlignedBB(livingEntity.posX - 2.0D, livingEntity.posY - 2.0D,
					livingEntity.posZ - 2.0D, livingEntity.posX + 2.0D, livingEntity.posY + 2.0D,
					livingEntity.posZ + 2.0D);
		}

		return livingEntity.isInRangeToRender3d(camX, camY, camZ)
				&& (livingEntity.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(axisalignedbb));
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
	public void doRender(T entity, double d0, double d1, double d2, float var8, float var9) {
		this.renderName(entity, d0, d1, d2);
	}

	protected void renderName(T entity, double x, double y, double z) {
		if (this.canRenderName(entity)) {
			this.renderLivingLabel(entity, entity.getDisplayNameProfanityFilter().getFormattedText(), x, y, z, 64);
		}
	}

	public static void renderNameAdapter(Render r, Entity e, double x, double y, double z) {
		r.renderName(e, x, y, z);
	}

	protected boolean canRenderName(T entity) {
		return entity.getAlwaysRenderNameTagForRender() && entity.hasCustomName();
	}

	protected void renderOffsetLivingLabel(T entityIn, double x, double y, double z, String str, float parFloat1,
			double parDouble4) {
		this.renderLivingLabel(entityIn, str, x, y, z, 64);
	}

	protected abstract ResourceLocation getEntityTexture(T var1);

	protected boolean bindEntityTexture(T entity) {
		ResourceLocation resourcelocation = this.getEntityTexture(entity);
		if (resourcelocation == null) {
			return false;
		} else {
			this.bindTexture(resourcelocation);
			return true;
		}
	}

	public void bindTexture(ResourceLocation location) {
		this.renderManager.renderEngine.bindTexture(location);
	}

	/**+
	 * Renders fire on top of the entity. Args: entity, x, y, z,
	 * partialTickTime
	 */
	private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks) {
		if (entity.width == 0 || entity.height == 0) {
			return;
		}
		GlStateManager.disableLighting();
		TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
		EaglerTextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
		EaglerTextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		float f = entity.width * 1.4F;
		GlStateManager.scale(f, f, f);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		float f1 = 0.5F;
		float f2 = 0.0F;
		float f3 = entity.height / f;
		float f4 = (float) (entity.posY - entity.getEntityBoundingBox().minY);
		GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(0.0F, 0.0F, -0.3F + (float) ((int) f3) * 0.02F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float f5 = 0.0F;
		int i = 0;
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

		while (f3 > 0.0F) {
			EaglerTextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
			this.bindTexture(TextureMap.locationBlocksTexture);
			float f6 = textureatlassprite2.getMinU();
			float f7 = textureatlassprite2.getMinV();
			float f8 = textureatlassprite2.getMaxU();
			float f9 = textureatlassprite2.getMaxV();
			if (i / 2 % 2 == 0) {
				float f10 = f8;
				f8 = f6;
				f6 = f10;
			}

			worldrenderer.pos((double) (f1 - f2), (double) (0.0F - f4), (double) f5).tex((double) f8, (double) f9)
					.endVertex();
			worldrenderer.pos((double) (-f1 - f2), (double) (0.0F - f4), (double) f5).tex((double) f6, (double) f9)
					.endVertex();
			worldrenderer.pos((double) (-f1 - f2), (double) (1.4F - f4), (double) f5).tex((double) f6, (double) f7)
					.endVertex();
			worldrenderer.pos((double) (f1 - f2), (double) (1.4F - f4), (double) f5).tex((double) f8, (double) f7)
					.endVertex();
			f3 -= 0.45F;
			f4 -= 0.45F;
			f1 *= 0.9F;
			f5 += 0.03F;
			++i;
		}

		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
	}

	/**+
	 * Renders the entity shadows at the position, shadow alpha and
	 * partialTickTime. Args: entity, x, y, z, shadowAlpha,
	 * partialTickTime
	 */
	private void renderShadow(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks) {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		this.renderManager.renderEngine.bindTexture(shadowTextures);
		World world = this.getWorldFromRenderManager();
		GlStateManager.depthMask(false);
		float f = this.shadowSize;
		if (entityIn instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving) entityIn;
			f *= entityliving.getRenderSizeModifier();
			if (entityliving.isChild()) {
				f *= 0.5F;
			}
		}

		double d5 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
		double d0 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
		double d1 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
		int i = MathHelper.floor_double(d5 - (double) f);
		int j = MathHelper.floor_double(d5 + (double) f);
		int k = MathHelper.floor_double(d0 - (double) f);
		int l = MathHelper.floor_double(d0);
		int i1 = MathHelper.floor_double(d1 - (double) f);
		int j1 = MathHelper.floor_double(d1 + (double) f);
		double d2 = x - d5;
		double d3 = y - d0;
		double d4 = z - d1;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

		for (BlockPos blockpos : BlockPos.getAllInBoxMutable(new BlockPos(i, k, i1), new BlockPos(j, l, j1))) {
			Block block = world.getBlockState(blockpos.down()).getBlock();
			if (block.getRenderType() != -1 && world.getLightFromNeighbors(blockpos) > 3) {
				this.func_180549_a(block, x, y, z, blockpos, shadowAlpha, f, d2, d3, d4);
			}
		}

		tessellator.draw();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
	}

	/**+
	 * Returns the render manager's world object
	 */
	private World getWorldFromRenderManager() {
		return this.renderManager.worldObj;
	}

	private void func_180549_a(Block blockIn, double pos, double parDouble2, double parDouble3, BlockPos parBlockPos,
			float parFloat1, float parFloat2, double parDouble4, double parDouble5, double parDouble6) {
		if (blockIn.isFullCube()) {
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			double d0 = ((double) parFloat1 - (parDouble2 - ((double) parBlockPos.getY() + parDouble5)) / 2.0D) * 0.5D
					* (double) this.getWorldFromRenderManager().getLightBrightness(parBlockPos);
			if (d0 >= 0.0D) {
				if (d0 > 1.0D) {
					d0 = 1.0D;
				}

				double d1 = (double) parBlockPos.getX() + blockIn.getBlockBoundsMinX() + parDouble4;
				double d2 = (double) parBlockPos.getX() + blockIn.getBlockBoundsMaxX() + parDouble4;
				double d3 = (double) parBlockPos.getY() + blockIn.getBlockBoundsMinY() + parDouble5 + 0.015625D;
				double d4 = (double) parBlockPos.getZ() + blockIn.getBlockBoundsMinZ() + parDouble6;
				double d5 = (double) parBlockPos.getZ() + blockIn.getBlockBoundsMaxZ() + parDouble6;
				float f = (float) ((pos - d1) / 2.0D / (double) parFloat2 + 0.5D);
				float f1 = (float) ((pos - d2) / 2.0D / (double) parFloat2 + 0.5D);
				float f2 = (float) ((parDouble3 - d4) / 2.0D / (double) parFloat2 + 0.5D);
				float f3 = (float) ((parDouble3 - d5) / 2.0D / (double) parFloat2 + 0.5D);
				worldrenderer.pos(d1, d3, d4).tex((double) f, (double) f2).color(1.0F, 1.0F, 1.0F, (float) d0)
						.endVertex();
				worldrenderer.pos(d1, d3, d5).tex((double) f, (double) f3).color(1.0F, 1.0F, 1.0F, (float) d0)
						.endVertex();
				worldrenderer.pos(d2, d3, d5).tex((double) f1, (double) f3).color(1.0F, 1.0F, 1.0F, (float) d0)
						.endVertex();
				worldrenderer.pos(d2, d3, d4).tex((double) f1, (double) f2).color(1.0F, 1.0F, 1.0F, (float) d0)
						.endVertex();
			}
		}
	}

	/**+
	 * Renders a white box with the bounds of the AABB translated by
	 * the offset. Args: aabb, x, y, z
	 */
	public static void renderOffsetAABB(AxisAlignedBB boundingBox, double x, double y, double z) {
		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		worldrenderer.setTranslation(x, y, z);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_NORMAL);
		worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
		worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
		worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
		worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
		worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
		worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
		tessellator.draw();
		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.enableTexture2D();
	}

	/**+
	 * Renders the entity's shadow and fire (if its on fire). Args:
	 * entity, x, y, z, yaw, partialTickTime
	 */
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		if (this.renderManager.options != null) {
			if (!DeferredStateManager.isInDeferredPass() && this.renderManager.options.field_181151_V
					&& this.shadowSize > 0.0F && !entityIn.isInvisible() && this.renderManager.isRenderShadow()) {
				double d0 = this.renderManager.getDistanceToCamera(entityIn.posX, entityIn.posY, entityIn.posZ);
				float f = (float) ((1.0D - d0 / 256.0D) * (double) this.shadowOpaque);
				if (f > 0.0F) {
					this.renderShadow(entityIn, x, y, z, f, partialTicks);
				}
			}

			if (entityIn.canRenderOnFire()
					&& (!(entityIn instanceof EntityPlayer) || !((EntityPlayer) entityIn).isSpectator())) {
				this.renderEntityOnFire(entityIn, x, y, z, partialTicks);
			}

		}
	}

	/**+
	 * Returns the font renderer from the set render manager
	 */
	public FontRenderer getFontRendererFromRenderManager() {
		return this.renderManager.getFontRenderer();
	}

	/**+
	 * Renders an entity's name above its head
	 */
	public void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
		double d0 = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);
		if (d0 <= (double) (maxDistance * maxDistance)) {
			if (DeferredStateManager.isInDeferredPass()) {
				NameTagRenderer.renderNameTag(entityIn, str, x, y, z, maxDistance);
				return;
			}
			FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x + 0.0F, (float) y + entityIn.height + 0.5F, (float) z);
			EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(-f1, -f1, f1);
			GlStateManager.disableLighting();
			GlStateManager.depthMask(false);
			GlStateManager.disableDepth();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			byte b0 = 0;
			if (str.equals("deadmau5")) {
				b0 = -10;
			}

			int i = fontrenderer.getStringWidth(str) / 2;
			GlStateManager.disableTexture2D();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			worldrenderer.pos((double) (-i - 1), (double) (-1 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos((double) (-i - 1), (double) (8 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos((double) (i + 1), (double) (8 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos((double) (i + 1), (double) (-1 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, b0, 553648127);
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
			fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, b0, -1);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			if (entityIn instanceof EntityOtherPlayerMP) {
				if (VoiceClientController.getVoiceStatus() == EnumVoiceChannelStatus.CONNECTED) {
					VoiceTagRenderer.renderVoiceNameTag(Minecraft.getMinecraft(), (EntityOtherPlayerMP) entityIn, b0);
				}
			}

			GlStateManager.popMatrix();
		}
	}

	public RenderManager getRenderManager() {
		return this.renderManager;
	}
}