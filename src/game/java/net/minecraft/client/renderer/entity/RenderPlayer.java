package net.minecraft.client.renderer.entity;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
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
public class RenderPlayer extends RendererLivingEntity<AbstractClientPlayer> {
	private boolean smallArms;
	private boolean zombieModel;

	public RenderPlayer(RenderManager renderManager) {
		this(renderManager, false, false);
	}

	public RenderPlayer(RenderManager renderManager, boolean useSmallArms, boolean zombieModel) {
		super(renderManager, zombieModel ? new ModelZombie(0.0F, true) : new ModelPlayer(0.0F, useSmallArms), 0.5F);
		this.smallArms = useSmallArms;
		this.zombieModel = zombieModel;
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerDeadmau5Head(this));
		this.addLayer(new LayerCape(this));
		this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
	}

	protected RenderPlayer(RenderManager renderManager, ModelBase modelBase, float size) {
		super(renderManager, modelBase, size);
	}

	public ModelBiped getMainModel() {
		return (ModelBiped) super.getMainModel();
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
	public void doRender(AbstractClientPlayer abstractclientplayer, double d0, double d1, double d2, float f,
			float f1) {
		if (!abstractclientplayer.isUser() || this.renderManager.livingPlayer == abstractclientplayer) {
			double d3 = d1;
			if (abstractclientplayer.isSneaking() && !(abstractclientplayer instanceof EntityPlayerSP)) {
				d3 = d1 - 0.125D;
			}

			this.setModelVisibilities(abstractclientplayer);
			super.doRender(abstractclientplayer, d0, d3, d2, f, f1);
		}
	}

	private void setModelVisibilities(AbstractClientPlayer clientPlayer) {
		ModelBiped modelplayer = this.getMainModel();
		if (clientPlayer.isSpectator()) {
			modelplayer.setInvisible(false);
			modelplayer.bipedHead.showModel = true;
			modelplayer.bipedHeadwear.showModel = true;
		} else {
			ItemStack itemstack = clientPlayer.inventory.getCurrentItem();
			modelplayer.setInvisible(true);
			modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
			if (!zombieModel) {
				ModelPlayer modelplayer_ = (ModelPlayer) modelplayer;
				modelplayer_.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
				modelplayer_.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
				modelplayer_.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
				modelplayer_.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
				modelplayer_.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
			}
			modelplayer.heldItemLeft = 0;
			modelplayer.aimedBow = false;
			modelplayer.isSneak = clientPlayer.isSneaking();
			if (itemstack == null) {
				modelplayer.heldItemRight = 0;
			} else {
				modelplayer.heldItemRight = 1;
				if (clientPlayer.getItemInUseCount() > 0) {
					EnumAction enumaction = itemstack.getItemUseAction();
					if (enumaction == EnumAction.BLOCK) {
						modelplayer.heldItemRight = 3;
					} else if (enumaction == EnumAction.BOW) {
						modelplayer.aimedBow = true;
					}
				}
			}
		}

	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(AbstractClientPlayer abstractclientplayer) {
		return abstractclientplayer.getLocationSkin();
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	/**+
	 * Allows the render to do any OpenGL state modifications
	 * necessary before the model is rendered. Args: entityLiving,
	 * partialTickTime
	 */
	protected void preRenderCallback(AbstractClientPlayer var1, float var2) {
		float f = 0.9375F;
		GlStateManager.scale(f, f, f);
	}

	protected void renderOffsetLivingLabel(AbstractClientPlayer abstractclientplayer, double d0, double d1, double d2,
			String s, float f, double d3) {
		if (d3 < 100.0D) {
			Scoreboard scoreboard = abstractclientplayer.getWorldScoreboard();
			ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
			if (scoreobjective != null) {
				Score score = scoreboard.getValueFromObjective(abstractclientplayer.getName(), scoreobjective);
				this.renderLivingLabel(abstractclientplayer,
						score.getScorePoints() + " " + scoreobjective.getDisplayNameProfanityFilter(), d0, d1, d2, 64);
				d1 += (double) ((float) this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * f);
			}
		}

		super.renderOffsetLivingLabel(abstractclientplayer, d0, d1, d2, s, f, d3);
	}

	public void renderRightArm(AbstractClientPlayer clientPlayer) {
		if (!zombieModel) {
			float f = 1.0F;
			GlStateManager.color(f, f, f);
			ModelBiped modelplayer = this.getMainModel();
			this.setModelVisibilities(clientPlayer);
			modelplayer.swingProgress = 0.0F;
			modelplayer.isSneak = false;
			modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
			((ModelPlayer) modelplayer).renderRightArm();
		}
	}

	public void renderLeftArm(AbstractClientPlayer clientPlayer) {
		if (!zombieModel) {
			float f = 1.0F;
			GlStateManager.color(f, f, f);
			ModelBiped modelplayer = this.getMainModel();
			this.setModelVisibilities(clientPlayer);
			modelplayer.isSneak = false;
			modelplayer.swingProgress = 0.0F;
			modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
			((ModelPlayer) modelplayer).renderLeftArm();
		}
	}

	/**+
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	public void renderLivingAt(AbstractClientPlayer abstractclientplayer, double d0, double d1, double d2) {
		if (abstractclientplayer.isEntityAlive() && abstractclientplayer.isPlayerSleeping()) {
			super.renderLivingAt(abstractclientplayer, d0 + (double) abstractclientplayer.renderOffsetX,
					d1 + (double) abstractclientplayer.renderOffsetY, d2 + (double) abstractclientplayer.renderOffsetZ);
		} else {
			super.renderLivingAt(abstractclientplayer, d0, d1, d2);
		}

	}

	protected void rotateCorpse(AbstractClientPlayer abstractclientplayer, float f, float f1, float f2) {
		if (abstractclientplayer.isEntityAlive() && abstractclientplayer.isPlayerSleeping()) {
			GlStateManager.rotate(abstractclientplayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(this.getDeathMaxRotation(abstractclientplayer), 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
		} else {
			super.rotateCorpse(abstractclientplayer, f, f1, f2);
		}

	}
}