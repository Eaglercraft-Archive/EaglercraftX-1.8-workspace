package net.minecraft.client.renderer.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.BlockPos;
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
public class TileEntityBannerRenderer extends TileEntitySpecialRenderer<TileEntityBanner> {
	private static final Map<String, TileEntityBannerRenderer.TimedBannerTexture> DESIGNS = Maps.newHashMap();
	private static final ResourceLocation BANNERTEXTURES = new ResourceLocation("textures/entity/banner_base.png");
	private ModelBanner bannerModel = new ModelBanner();

	public void renderTileEntityAt(TileEntityBanner te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		boolean flag = te.getWorld() != null;
		boolean flag1 = !flag || te.getBlockType() == Blocks.standing_banner;
		int i = flag ? te.getBlockMetadata() : 0;
		long j = flag ? te.getWorld().getTotalWorldTime() : 0L;
		GlStateManager.pushMatrix();
		float f = 0.6666667F;
		if (flag1) {
			GlStateManager.translate((float) x + 0.5F, (float) y + 0.75F * f, (float) z + 0.5F);
			float f1 = (float) (i * 360) / 16.0F;
			GlStateManager.rotate(-f1, 0.0F, 1.0F, 0.0F);
			this.bannerModel.bannerStand.showModel = true;
		} else {
			float f2 = 0.0F;
			if (i == 2) {
				f2 = 180.0F;
			}

			if (i == 4) {
				f2 = 90.0F;
			}

			if (i == 5) {
				f2 = -90.0F;
			}

			GlStateManager.translate((float) x + 0.5F, (float) y - 0.25F * f, (float) z + 0.5F);
			GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
			this.bannerModel.bannerStand.showModel = false;
		}

		BlockPos blockpos = te.getPos();
		float f3 = (float) (blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13) + (float) j
				+ partialTicks;
		this.bannerModel.bannerSlate.rotateAngleX = (-0.0125F + 0.01F * MathHelper.cos(f3 * 3.1415927F * 0.02F))
				* 3.1415927F;
		GlStateManager.enableRescaleNormal();
		ResourceLocation resourcelocation = this.func_178463_a(te);
		if (resourcelocation != null) {
			this.bindTexture(resourcelocation);
			GlStateManager.pushMatrix();
			GlStateManager.scale(f, -f, -f);
			this.bannerModel.renderBanner();
			GlStateManager.popMatrix();
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	private ResourceLocation func_178463_a(TileEntityBanner bannerObj) {
		String s = bannerObj.func_175116_e();
		if (s.isEmpty()) {
			return null;
		} else {
			TileEntityBannerRenderer.TimedBannerTexture tileentitybannerrenderer$timedbannertexture = (TileEntityBannerRenderer.TimedBannerTexture) DESIGNS
					.get(s);
			if (tileentitybannerrenderer$timedbannertexture == null) {
				if (DESIGNS.size() >= 256) {
					long i = EagRuntime.steadyTimeMillis();
					Iterator iterator = DESIGNS.keySet().iterator();

					while (iterator.hasNext()) {
						String s1 = (String) iterator.next();
						TileEntityBannerRenderer.TimedBannerTexture tileentitybannerrenderer$timedbannertexture1 = (TileEntityBannerRenderer.TimedBannerTexture) DESIGNS
								.get(s1);
						if (i - tileentitybannerrenderer$timedbannertexture1.systemTime > 60000L) {
							Minecraft.getMinecraft().getTextureManager()
									.deleteTexture(tileentitybannerrenderer$timedbannertexture1.bannerTexture);
							iterator.remove();
						}
					}

					if (DESIGNS.size() >= 256) {
						return null;
					}
				}

				List list1 = bannerObj.getPatternList();
				List list = bannerObj.getColorList();
				ArrayList arraylist = Lists.newArrayList();

				for (int i = 0, l = list1.size(); i < l; ++i) {
					arraylist.add("textures/entity/banner/"
							+ ((TileEntityBanner.EnumBannerPattern) list1.get(i)).getPatternName() + ".png");
				}

				tileentitybannerrenderer$timedbannertexture = new TileEntityBannerRenderer.TimedBannerTexture();
				tileentitybannerrenderer$timedbannertexture.bannerTexture = new ResourceLocation(s);
				Minecraft.getMinecraft().getTextureManager().loadTexture(
						tileentitybannerrenderer$timedbannertexture.bannerTexture,
						new LayeredColorMaskTexture(BANNERTEXTURES, arraylist, list));
				DESIGNS.put(s, tileentitybannerrenderer$timedbannertexture);
			}

			tileentitybannerrenderer$timedbannertexture.systemTime = EagRuntime.steadyTimeMillis();
			return tileentitybannerrenderer$timedbannertexture.bannerTexture;
		}
	}

	static class TimedBannerTexture {
		public long systemTime;
		public ResourceLocation bannerTexture;

		private TimedBannerTexture() {
		}
	}
}