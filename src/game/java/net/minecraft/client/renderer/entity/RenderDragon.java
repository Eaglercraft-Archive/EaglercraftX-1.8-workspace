package net.minecraft.client.renderer.entity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonDeath;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityDragon;
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
public class RenderDragon extends RenderLiving<EntityDragon> {
	private static final ResourceLocation enderDragonCrystalBeamTextures = new ResourceLocation(
			"textures/entity/endercrystal/endercrystal_beam.png");
	private static final ResourceLocation enderDragonExplodingTextures = new ResourceLocation(
			"textures/entity/enderdragon/dragon_exploding.png");
	private static final ResourceLocation enderDragonTextures = new ResourceLocation(
			"textures/entity/enderdragon/dragon.png");
	protected ModelDragon modelDragon;

	public RenderDragon(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelDragon(0.0F), 0.5F);
		this.modelDragon = (ModelDragon) this.mainModel;
		this.addLayer(new LayerEnderDragonEyes(this));
		this.addLayer(new LayerEnderDragonDeath());
	}

	protected void rotateCorpse(EntityDragon entitydragon, float var2, float var3, float f) {
		float f1 = (float) entitydragon.getMovementOffsets(7, f)[0];
		float f2 = (float) (entitydragon.getMovementOffsets(5, f)[1] - entitydragon.getMovementOffsets(10, f)[1]);
		GlStateManager.rotate(-f1, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(f2 * 10.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(0.0F, 0.0F, 1.0F);
		if (entitydragon.deathTime > 0) {
			float f3 = ((float) entitydragon.deathTime + f - 1.0F) / 20.0F * 1.6F;
			f3 = MathHelper.sqrt_float(f3);
			if (f3 > 1.0F) {
				f3 = 1.0F;
			}

			GlStateManager.rotate(f3 * this.getDeathMaxRotation(entitydragon), 0.0F, 0.0F, 1.0F);
		}

	}

	/**+
	 * Renders the model in RenderLiving
	 */
	protected void renderModel(EntityDragon entitydragon, float f, float f1, float f2, float f3, float f4, float f5) {
		if (DeferredStateManager.isDeferredRenderer()) {
			if (entitydragon.deathTicks > 0) {
				float f6 = (float) entitydragon.deathTicks / 200.0F;
				GlStateManager.depthFunc(GL_LEQUAL);
				GlStateManager.enableAlpha();
				GlStateManager.alphaFunc(GL_GREATER, f6);
				this.bindTexture(enderDragonExplodingTextures);
				this.mainModel.render(entitydragon, f, f1, f2, f3, f4, f5);
				GlStateManager.alphaFunc(GL_GREATER, 0.1F);
				GlStateManager.depthFunc(GL_EQUAL);
			}
			if (entitydragon.hurtTime > 0) {
				GlStateManager.enableShaderBlendAdd();
				GlStateManager.setShaderBlendSrc(0.5f, 0.5f, 0.5f, 1.0f);
				GlStateManager.setShaderBlendAdd(1.0f, 0.0f, 0.0f, 0.0f);
			}
			this.bindEntityTexture(entitydragon);
			this.mainModel.render(entitydragon, f, f1, f2, f3, f4, f5);
			GlStateManager.depthFunc(GL_LEQUAL);
			if (entitydragon.hurtTime > 0) {
				GlStateManager.disableShaderBlendAdd();
			}
			return;
		}
		if (entitydragon.deathTicks > 0) {
			float f6 = (float) entitydragon.deathTicks / 200.0F;
			GlStateManager.depthFunc(GL_LEQUAL);
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(GL_GREATER, f6);
			this.bindTexture(enderDragonExplodingTextures);
			this.mainModel.render(entitydragon, f, f1, f2, f3, f4, f5);
			GlStateManager.alphaFunc(GL_GREATER, 0.1F);
			GlStateManager.depthFunc(GL_EQUAL);
		}

		this.bindEntityTexture(entitydragon);
		this.mainModel.render(entitydragon, f, f1, f2, f3, f4, f5);
		if (entitydragon.hurtTime > 0) {
			GlStateManager.depthFunc(GL_EQUAL);
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 0.0F, 0.0F, 0.5F);
			this.mainModel.render(entitydragon, f, f1, f2, f3, f4, f5);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
			GlStateManager.depthFunc(GL_LEQUAL);
		}

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
	public void doRender(EntityDragon entitydragon, double d0, double d1, double d2, float f, float f1) {
		BossStatus.setBossStatus(entitydragon, false);
		super.doRender(entitydragon, d0, d1, d2, f, f1);
		if (entitydragon.healingEnderCrystal != null) {
			this.drawRechargeRay(entitydragon, d0, d1, d2, f1);
		}

	}

	/**+
	 * Draws the ray from the dragon to it's crystal
	 */
	protected void drawRechargeRay(EntityDragon dragon, double parDouble1, double parDouble2, double parDouble3,
			float parFloat1) {
		float f = (float) dragon.healingEnderCrystal.innerRotation + parFloat1;
		float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
		f1 = (f1 * f1 + f1) * 0.2F;
		float f2 = (float) (dragon.healingEnderCrystal.posX - dragon.posX
				- (dragon.prevPosX - dragon.posX) * (double) (1.0F - parFloat1));
		float f3 = (float) ((double) f1 + dragon.healingEnderCrystal.posY - 1.0D - dragon.posY
				- (dragon.prevPosY - dragon.posY) * (double) (1.0F - parFloat1));
		float f4 = (float) (dragon.healingEnderCrystal.posZ - dragon.posZ
				- (dragon.prevPosZ - dragon.posZ) * (double) (1.0F - parFloat1));
		float f5 = MathHelper.sqrt_float(f2 * f2 + f4 * f4);
		float f6 = MathHelper.sqrt_float(f2 * f2 + f3 * f3 + f4 * f4);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) parDouble1, (float) parDouble2 + 2.0F, (float) parDouble3);
		GlStateManager.rotate((float) (-Math.atan2((double) f4, (double) f2)) * 180.0F / 3.1415927F - 90.0F, 0.0F, 1.0F,
				0.0F);
		GlStateManager.rotate((float) (-Math.atan2((double) f5, (double) f3)) * 180.0F / 3.1415927F - 90.0F, 1.0F, 0.0F,
				0.0F);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		this.bindTexture(enderDragonCrystalBeamTextures);
		GlStateManager.shadeModel(GL_SMOOTH);
		float f7 = 0.0F - ((float) dragon.ticksExisted + parFloat1) * 0.01F;
		float f8 = MathHelper.sqrt_float(f2 * f2 + f3 * f3 + f4 * f4) / 32.0F
				- ((float) dragon.ticksExisted + parFloat1) * 0.01F;
		worldrenderer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
		boolean flag = true;

		for (int i = 0; i <= 8; ++i) {
			float f9 = MathHelper.sin((float) (i % 8) * 3.1415927F * 2.0F / 8.0F) * 0.75F;
			float f10 = MathHelper.cos((float) (i % 8) * 3.1415927F * 2.0F / 8.0F) * 0.75F;
			float f11 = (float) (i % 8) * 1.0F / 8.0F;
			worldrenderer.pos((double) (f9 * 0.2F), (double) (f10 * 0.2F), 0.0D).tex((double) f11, (double) f8)
					.color(0, 0, 0, 255).endVertex();
			worldrenderer.pos((double) f9, (double) f10, (double) f6).tex((double) f11, (double) f7)
					.color(255, 255, 255, 255).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableCull();
		GlStateManager.shadeModel(GL_FLAT);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityDragon var1) {
		return enderDragonTextures;
	}
}