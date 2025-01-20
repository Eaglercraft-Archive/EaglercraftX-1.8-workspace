package net.minecraft.client.renderer.entity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.NameTagRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
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
public abstract class RendererLivingEntity<T extends EntityLivingBase> extends Render<T> {
	private static final Logger logger = LogManager.getLogger();
	private static final DynamicTexture field_177096_e = new DynamicTexture(16, 16);
	protected ModelBase mainModel;
	protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
	protected List<LayerRenderer<T>> layerRenderers = Lists.newArrayList();
	protected boolean renderOutlines = false;

	public RendererLivingEntity(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
		super(renderManagerIn);
		this.mainModel = modelBaseIn;
		this.shadowSize = shadowSizeIn;
	}

	protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer) {
		return this.layerRenderers.add((LayerRenderer<T>) layer);
	}

	protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean removeLayer(U layer) {
		return this.layerRenderers.remove(layer);
	}

	public ModelBase getMainModel() {
		return this.mainModel;
	}

	/**+
	 * Returns a rotation angle that is inbetween two other rotation
	 * angles. par1 and par2 are the angles between which to
	 * interpolate, par3 is probably a float between 0.0 and 1.0
	 * that tells us where "between" the two angles we are. Example:
	 * par1 = 30, par2 = 50, par3 = 0.5, then return = 40
	 */
	protected float interpolateRotation(float par1, float par2, float par3) {
		float f;
		for (f = par2 - par1; f < -180.0F; f += 360.0F) {
			;
		}

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return par1 + par3 * f;
	}

	public void transformHeldFull3DItemLayer() {
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
	public void doRender(T entitylivingbase, double d0, double d1, double d2, float f, float f1) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		this.mainModel.swingProgress = this.getSwingProgress(entitylivingbase, f1);
		this.mainModel.isRiding = entitylivingbase.isRiding();
		this.mainModel.isChild = entitylivingbase.isChild();

		try {
			float f2 = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset,
					f1);
			float f3 = this.interpolateRotation(entitylivingbase.prevRotationYawHead, entitylivingbase.rotationYawHead,
					f1);
			float f4 = f3 - f2;
			if (entitylivingbase.isRiding() && entitylivingbase.ridingEntity instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase1 = (EntityLivingBase) entitylivingbase.ridingEntity;
				f2 = this.interpolateRotation(entitylivingbase1.prevRenderYawOffset, entitylivingbase1.renderYawOffset,
						f1);
				f4 = f3 - f2;
				float f5 = MathHelper.wrapAngleTo180_float(f4);
				if (f5 < -85.0F) {
					f5 = -85.0F;
				}

				if (f5 >= 85.0F) {
					f5 = 85.0F;
				}

				f2 = f3 - f5;
				if (f5 * f5 > 2500.0F) {
					f2 += f5 * 0.2F;
				}
			}

			float f9 = entitylivingbase.prevRotationPitch
					+ (entitylivingbase.rotationPitch - entitylivingbase.prevRotationPitch) * f1;
			this.renderLivingAt(entitylivingbase, d0, d1, d2);
			float f10 = this.handleRotationFloat(entitylivingbase, f1);
			this.rotateCorpse(entitylivingbase, f10, f2, f1);
			GlStateManager.enableRescaleNormal();
			GlStateManager.scale(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(entitylivingbase, f1);
			float f6 = 0.0625F;
			GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
			float f7 = entitylivingbase.prevLimbSwingAmount
					+ (entitylivingbase.limbSwingAmount - entitylivingbase.prevLimbSwingAmount) * f1;
			float f8 = entitylivingbase.limbSwing - entitylivingbase.limbSwingAmount * (1.0F - f1);
			if (entitylivingbase.isChild()) {
				f8 *= 3.0F;
			}

			if (f7 > 1.0F) {
				f7 = 1.0F;
			}

			GlStateManager.enableAlpha();
			this.mainModel.setLivingAnimations(entitylivingbase, f8, f7, f1);
			this.mainModel.setRotationAngles(f8, f7, f10, f4, f9, 0.0625F, entitylivingbase);
			if (this.renderOutlines) {
				boolean flag1 = this.setScoreTeamColor(entitylivingbase);
				this.renderModel(entitylivingbase, f8, f7, f10, f4, f9, 0.0625F);
				if (flag1) {
					this.unsetScoreTeamColor();
				}
			} else {
				boolean flag = this.setDoRenderBrightness(entitylivingbase, f1);
				this.renderModel(entitylivingbase, f8, f7, f10, f4, f9, 0.0625F);
				if (flag) {
					this.unsetBrightness();
				}

				GlStateManager.depthMask(true);
				if (!(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer) entitylivingbase).isSpectator()) {
					this.renderLayers(entitylivingbase, f8, f7, f1, f10, f4, f9, 0.0625F);
				}
			}

			GlStateManager.disableRescaleNormal();
		} catch (Exception exception) {
			logger.error("Couldn\'t render entity");
			logger.error(exception);
		}

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		if (!this.renderOutlines) {
			super.doRender(entitylivingbase, d0, d1, d2, f, f1);
		}
	}

	protected boolean setScoreTeamColor(T entityLivingBaseIn) {
		int i = 16777215;
		if (entityLivingBaseIn instanceof EntityPlayer) {
			ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam) entityLivingBaseIn.getTeam();
			if (scoreplayerteam != null) {
				String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());
				if (s.length() >= 2) {
					i = this.getFontRendererFromRenderManager().getColorCode(s.charAt(1));
				}
			}
		}

		float f1 = (float) (i >> 16 & 255) / 255.0F;
		float f2 = (float) (i >> 8 & 255) / 255.0F;
		float f = (float) (i & 255) / 255.0F;
		GlStateManager.disableLighting();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.color(f1, f2, f, 1.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		return true;
	}

	protected void unsetScoreTeamColor() {
		GlStateManager.enableLighting();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	/**+
	 * Renders the model in RenderLiving
	 */
	protected void renderModel(T entitylivingbase, float f, float f1, float f2, float f3, float f4, float f5) { // f8,
																												// f7,
																												// f10,
																												// f4,
																												// f9,
																												// 0.0625
		boolean flag = !entitylivingbase.isInvisible();
		boolean flag1 = !flag && !entitylivingbase.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
		if (flag || flag1) {
			if (!this.bindEntityTexture(entitylivingbase)) {
				return;
			}
			if (flag1 && DeferredStateManager.isDeferredRenderer()) {
				if (!DeferredStateManager.isEnableShadowRender()
						&& DeferredStateManager.forwardCallbackHandler != null) {
					final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
					final float lx = GlStateManager.getTexCoordX(1), ly = GlStateManager.getTexCoordY(1);
					DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(entitylivingbase,
							EaglerDeferredPipeline.instance.getPartialTicks()) {
						@Override
						public void draw(PassType pass) {
							if (pass == PassType.MAIN) {
								DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
							}
							EntityRenderer.enableLightmapStatic();
							GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
							DeferredStateManager.setDefaultMaterialConstants();
							DeferredStateManager.setRoughnessConstant(0.05f);
							DeferredStateManager.setMetalnessConstant(0.2f);
							DeferredStateManager.setEmissionConstant(0.5f);
							GlStateManager.pushMatrix();
							GlStateManager.loadMatrix(mat);
							GlStateManager.texCoords2DDirect(1, lx, ly);
							DeferredStateManager.setHDRTranslucentPassBlendFunc();
							GlStateManager.enableAlpha();
							GlStateManager.alphaFunc(GL_GREATER, 0.003921569F);
							GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
							RendererLivingEntity.this.bindEntityTexture(entitylivingbase);
							RendererLivingEntity.this.mainModel.swingProgress = RendererLivingEntity.this
									.getSwingProgress(entitylivingbase, f1);
							RendererLivingEntity.this.mainModel.isRiding = entitylivingbase.isRiding();
							RendererLivingEntity.this.mainModel.isChild = entitylivingbase.isChild();
							if (RendererLivingEntity.this.mainModel instanceof ModelBiped) {
								if ((entitylivingbase instanceof EntityPlayer)
										&& ((EntityPlayer) entitylivingbase).isSpectator()) {
									((ModelBiped) RendererLivingEntity.this.mainModel).setInvisible(false);
									((ModelBiped) RendererLivingEntity.this.mainModel).bipedHead.showModel = true;
									((ModelBiped) RendererLivingEntity.this.mainModel).bipedHeadwear.showModel = true;
								} else {
									((ModelBiped) RendererLivingEntity.this.mainModel).setInvisible(true);
								}
							}
							RendererLivingEntity.this.mainModel.setLivingAnimations(entitylivingbase, f, f1, f1);
							RendererLivingEntity.this.mainModel.setRotationAngles(f, f1, f2, f3, f4, f5,
									entitylivingbase);
							RendererLivingEntity.this.mainModel.render(entitylivingbase, f, f1, f2, f3, f4, f5);
							if (RendererLivingEntity.this.mainModel instanceof ModelBiped) {
								if ((entitylivingbase instanceof EntityPlayer)
										&& ((EntityPlayer) entitylivingbase).isSpectator()) {
									((ModelBiped) RendererLivingEntity.this.mainModel).setInvisible(true);
								}
							}
							GlStateManager.alphaFunc(GL_GREATER, 0.1F);
							GlStateManager.popMatrix();
							EntityRenderer.disableLightmapStatic();
							GlStateManager.disableAlpha();
						}
					});
				}
				return;
			}

			if (flag1) {
				GlStateManager.pushMatrix();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
				GlStateManager.depthMask(false);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.alphaFunc(GL_GREATER, 0.003921569F);
			}

			this.mainModel.render(entitylivingbase, f, f1, f2, f3, f4, f5);
			if (flag1) {
				GlStateManager.disableBlend();
				GlStateManager.alphaFunc(GL_GREATER, 0.1F);
				GlStateManager.popMatrix();
				GlStateManager.depthMask(true);
			}
		}

	}

	protected boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks) {
		return this.setBrightness(entityLivingBaseIn, partialTicks, true);
	}

	public boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
		float f = entitylivingbaseIn.getBrightness(partialTicks);
		int i = this.getColorMultiplier(entitylivingbaseIn, f, partialTicks);
		boolean flag = (i >> 24 & 255) > 0;
		boolean flag1 = entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0;
		if (!flag && !flag1) {
			return false;
		} else if (!flag && !combineTextures) {
			return false;
		} else {
			GlStateManager.enableShaderBlendAdd();
			float f1 = 1.0F - (float) (i >>> 24 & 255) / 255.0F;
			float f2 = (float) (i >>> 16 & 255) / 255.0F;
			float f3 = (float) (i >>> 8 & 255) / 255.0F;
			float f4 = (float) (i & 255) / 255.0F;
			GlStateManager.setShaderBlendSrc(f1, f1, f1, 1.0F);
			GlStateManager.setShaderBlendAdd(f2 * f1 + 0.4F, f3 * f1, f4 * f1, 0.0f);
			return true;
		}
	}

	public void unsetBrightness() {
		GlStateManager.disableShaderBlendAdd();
	}

	/**+
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	public void renderLivingAt(T entityLivingBaseIn, double x, double y, double z) {
		GlStateManager.translate((float) x, (float) y, (float) z);
	}

	protected void rotateCorpse(T entitylivingbase, float var2, float f, float f1) {
		GlStateManager.rotate(180.0F - f, 0.0F, 1.0F, 0.0F);
		if (entitylivingbase.deathTime > 0) {
			float f2 = ((float) entitylivingbase.deathTime + f1 - 1.0F) / 20.0F * 1.6F;
			f2 = MathHelper.sqrt_float(f2);
			if (f2 > 1.0F) {
				f2 = 1.0F;
			}

			GlStateManager.rotate(f2 * this.getDeathMaxRotation(entitylivingbase), 0.0F, 0.0F, 1.0F);
		} else {
			String s = EnumChatFormatting.getTextWithoutFormattingCodes(entitylivingbase.getName());
			if (s != null && (s.equals("Dinnerbone") || s.equals("Grumm"))
					&& (!(entitylivingbase instanceof EntityPlayer)
							|| ((EntityPlayer) entitylivingbase).isWearing(EnumPlayerModelParts.CAPE))) {
				GlStateManager.translate(0.0F, entitylivingbase.height + 0.1F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			}
		}

	}

	/**+
	 * Returns where in the swing animation the living entity is
	 * (from 0 to 1). Args : entity, partialTickTime
	 */
	protected float getSwingProgress(T livingBase, float partialTickTime) {
		return livingBase.getSwingProgress(partialTickTime);
	}

	/**+
	 * Defines what float the third param in setRotationAngles of
	 * ModelBase is
	 */
	protected float handleRotationFloat(T entitylivingbase, float f) {
		return (float) entitylivingbase.ticksExisted + f;
	}

	protected void renderLayers(T entitylivingbaseIn, float partialTicks, float parFloat2, float parFloat3,
			float parFloat4, float parFloat5, float parFloat6, float parFloat7) {
		for (int i = 0, l = this.layerRenderers.size(); i < l; ++i) {
			LayerRenderer layerrenderer = this.layerRenderers.get(i);
			boolean flag = this.setBrightness(entitylivingbaseIn, parFloat3, layerrenderer.shouldCombineTextures());
			layerrenderer.doRenderLayer(entitylivingbaseIn, partialTicks, parFloat2, parFloat3, parFloat4, parFloat5,
					parFloat6, parFloat7);
			if (flag) {
				this.unsetBrightness();
			}
		}

	}

	protected float getDeathMaxRotation(T var1) {
		return 90.0F;
	}

	/**+
	 * Returns an ARGB int color back. Args: entityLiving,
	 * lightBrightness, partialTickTime
	 */
	protected int getColorMultiplier(T var1, float var2, float var3) {
		return 0;
	}

	/**+
	 * Allows the render to do any OpenGL state modifications
	 * necessary before the model is rendered. Args: entityLiving,
	 * partialTickTime
	 */
	protected void preRenderCallback(T var1, float var2) {
	}

	public void renderName(T entitylivingbase, double d0, double d1, double d2) {
		if (this.canRenderName(entitylivingbase)) {
			double d3 = entitylivingbase.getDistanceSqToEntity(this.renderManager.livingPlayer);
			float f = entitylivingbase.isSneaking() ? 32.0F : 64.0F;
			if (d3 < (double) (f * f)) {
				String s = entitylivingbase.getDisplayNameProfanityFilter().getFormattedText();
				float f1 = 0.02666667F;
				GlStateManager.alphaFunc(GL_GREATER, 0.1F);
				if (entitylivingbase.isSneaking()) {
					if (DeferredStateManager.isInDeferredPass()) {
						NameTagRenderer.renderNameTag(entitylivingbase, null, d0, d1, d2, -69);
						return;
					}
					FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
					GlStateManager.pushMatrix();
					GlStateManager.translate((float) d0, (float) d1 + entitylivingbase.height + 0.5F
							- (entitylivingbase.isChild() ? entitylivingbase.height / 2.0F : 0.0F), (float) d2);
					EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
					GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
					GlStateManager.translate(0.0F, 9.374999F, 0.0F);
					GlStateManager.disableLighting();
					GlStateManager.depthMask(false);
					GlStateManager.enableBlend();
					GlStateManager.disableTexture2D();
					GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
					int i = fontrenderer.getStringWidth(s) / 2;
					Tessellator tessellator = Tessellator.getInstance();
					WorldRenderer worldrenderer = tessellator.getWorldRenderer();
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
					this.renderOffsetLivingLabel(entitylivingbase, d0,
							d1 - (entitylivingbase.isChild() ? (double) (entitylivingbase.height / 2.0F) : 0.0D), d2, s,
							0.02666667F, d3);
				}

			}
		}
	}

	protected boolean canRenderName(T entitylivingbase) {
		EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
		if (entitylivingbase instanceof EntityPlayer && entitylivingbase != entityplayersp) {
			Team team = entitylivingbase.getTeam();
			Team team1 = entityplayersp.getTeam();
			if (team != null) {
				Team.EnumVisible team$enumvisible = team.getNameTagVisibility();
				switch (team$enumvisible) {
				case ALWAYS:
					return true;
				case NEVER:
					return false;
				case HIDE_FOR_OTHER_TEAMS:
					return team1 == null || team.isSameTeam(team1);
				case HIDE_FOR_OWN_TEAM:
					return team1 == null || !team.isSameTeam(team1);
				default:
					return true;
				}
			}
		}

		return Minecraft.isGuiEnabled() && entitylivingbase != this.renderManager.livingPlayer
				&& !entitylivingbase.isInvisibleToPlayer(entityplayersp) && entitylivingbase.riddenByEntity == null;
	}

	public void setRenderOutlines(boolean renderOutlinesIn) {
		this.renderOutlines = renderOutlinesIn;
	}

	static {
		int[] aint = field_177096_e.getTextureData();

		for (int i = 0; i < 256; ++i) {
			aint[i] = -1;
		}

		field_177096_e.updateDynamicTexture();
	}
}