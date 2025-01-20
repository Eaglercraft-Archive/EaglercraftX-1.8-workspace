package net.minecraft.client.renderer.entity;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

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
public class RenderMinecart<T extends EntityMinecart> extends Render<T> {
	private static final ResourceLocation minecartTextures = new ResourceLocation("textures/entity/minecart.png");
	/**+
	 * instance of ModelMinecart for rendering
	 */
	protected ModelBase modelMinecart = new ModelMinecart();

	public RenderMinecart(RenderManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0.5F;
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
	public void doRender(T entityminecart, double d0, double d1, double d2, float f, float f1) {
		GlStateManager.pushMatrix();
		this.bindEntityTexture(entityminecart);
		long i = (long) entityminecart.getEntityId() * 493286711L;
		i = i * i * 4392167121L + i * 98761L;
		float f2 = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float f3 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float f4 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		GlStateManager.translate(f2, f3, f4);
		double d3 = entityminecart.lastTickPosX + (entityminecart.posX - entityminecart.lastTickPosX) * (double) f1;
		double d4 = entityminecart.lastTickPosY + (entityminecart.posY - entityminecart.lastTickPosY) * (double) f1;
		double d5 = entityminecart.lastTickPosZ + (entityminecart.posZ - entityminecart.lastTickPosZ) * (double) f1;
		double d6 = 0.30000001192092896D;
		Vec3 vec3 = entityminecart.func_70489_a(d3, d4, d5);
		float f5 = entityminecart.prevRotationPitch
				+ (entityminecart.rotationPitch - entityminecart.prevRotationPitch) * f1;
		if (vec3 != null) {
			Vec3 vec31 = entityminecart.func_70495_a(d3, d4, d5, d6);
			Vec3 vec32 = entityminecart.func_70495_a(d3, d4, d5, -d6);
			if (vec31 == null) {
				vec31 = vec3;
			}

			if (vec32 == null) {
				vec32 = vec3;
			}

			d0 += vec3.xCoord - d3;
			d1 += (vec31.yCoord + vec32.yCoord) / 2.0D - d4;
			d2 += vec3.zCoord - d5;
			Vec3 vec33 = vec32.addVector(-vec31.xCoord, -vec31.yCoord, -vec31.zCoord);
			if (vec33.lengthVector() != 0.0D) {
				vec33 = vec33.normalize();
				f = (float) (Math.atan2(vec33.zCoord, vec33.xCoord) * 180.0D / 3.141592653589793D);
				f5 = (float) (Math.atan(vec33.yCoord) * 73.0D);
			}
		}

		GlStateManager.translate((float) d0, (float) d1 + 0.375F, (float) d2);
		GlStateManager.rotate(180.0F - f, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-f5, 0.0F, 0.0F, 1.0F);
		float f7 = (float) entityminecart.getRollingAmplitude() - f1;
		float f8 = entityminecart.getDamage() - f1;
		if (f8 < 0.0F) {
			f8 = 0.0F;
		}

		if (f7 > 0.0F) {
			GlStateManager.rotate(MathHelper.sin(f7) * f7 * f8 / 10.0F * (float) entityminecart.getRollingDirection(),
					1.0F, 0.0F, 0.0F);
		}

		int j = entityminecart.getDisplayTileOffset();
		IBlockState iblockstate = entityminecart.getDisplayTile();
		if (iblockstate.getBlock().getRenderType() != -1) {
			GlStateManager.pushMatrix();
			this.bindTexture(TextureMap.locationBlocksTexture);
			float f6 = 0.75F;
			GlStateManager.scale(f6, f6, f6);
			GlStateManager.translate(-0.5F, (float) (j - 8) / 16.0F, 0.5F);
			this.func_180560_a(entityminecart, f1, iblockstate);
			GlStateManager.popMatrix();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.bindEntityTexture(entityminecart);
		}

		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		this.modelMinecart.render(entityminecart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
		super.doRender(entityminecart, d0, d1, d2, f, f1);
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(T var1) {
		return minecartTextures;
	}

	protected void func_180560_a(T minecart, float partialTicks, IBlockState state) {
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(state,
				minecart.getBrightness(partialTicks));
		GlStateManager.popMatrix();
	}
}