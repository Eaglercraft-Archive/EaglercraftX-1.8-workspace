package net.minecraft.client.gui;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.Map;

import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec4b;
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
public class MapItemRenderer {
	private static final ResourceLocation mapIcons = new ResourceLocation("textures/map/map_icons.png");
	private final TextureManager textureManager;
	private final Map<String, MapItemRenderer.Instance> loadedMaps = Maps.newHashMap();

	public MapItemRenderer(TextureManager textureManagerIn) {
		this.textureManager = textureManagerIn;
	}

	/**+
	 * Updates a map texture
	 */
	public void updateMapTexture(MapData mapdataIn) {
		this.getMapRendererInstance(mapdataIn).updateMapTexture();
	}

	public void renderMap(MapData mapdataIn, boolean parFlag) {
		this.getMapRendererInstance(mapdataIn).render(parFlag);
	}

	/**+
	 * Returns {@link
	 * net.minecraft.client.gui.MapItemRenderer.Instance
	 * MapItemRenderer.Instance} with given map data
	 */
	private MapItemRenderer.Instance getMapRendererInstance(MapData mapdataIn) {
		MapItemRenderer.Instance mapitemrenderer$instance = (MapItemRenderer.Instance) this.loadedMaps
				.get(mapdataIn.mapName);
		if (mapitemrenderer$instance == null) {
			mapitemrenderer$instance = new MapItemRenderer.Instance(mapdataIn);
			this.loadedMaps.put(mapdataIn.mapName, mapitemrenderer$instance);
		}

		return mapitemrenderer$instance;
	}

	/**+
	 * Clears the currently loaded maps and removes their
	 * corresponding textures
	 */
	public void clearLoadedMaps() {
		for (MapItemRenderer.Instance mapitemrenderer$instance : this.loadedMaps.values()) {
			this.textureManager.deleteTexture(mapitemrenderer$instance.location);
		}

		this.loadedMaps.clear();
	}

	class Instance {
		private final MapData mapData;
		private final DynamicTexture mapTexture;
		private final ResourceLocation location;
		private final int[] mapTextureData;

		private Instance(MapData mapdataIn) {
			this.mapData = mapdataIn;
			this.mapTexture = new DynamicTexture(128, 128);
			this.mapTextureData = this.mapTexture.getTextureData();
			this.location = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + mapdataIn.mapName,
					this.mapTexture);

			for (int i = 0; i < this.mapTextureData.length; ++i) {
				this.mapTextureData[i] = 0;
			}

		}

		/**+
		 * Updates a map texture
		 */
		private void updateMapTexture() {
			for (int i = 0; i < 16384; ++i) {
				int j = this.mapData.colors[i] & 255;
				int c;
				if (j / 4 == 0) {
					c = (i + i / 128 & 1) * 8 + 16 << 24;
				} else {
					c = MapColor.mapColorArray[j / 4].func_151643_b(j & 3);
				}
				this.mapTextureData[i] = (c & 0xFF00FF00) | ((c & 0x00FF0000) >>> 16) | ((c & 0x000000FF) << 16);
			}

			this.mapTexture.updateDynamicTexture();
		}

		private void render(boolean noOverlayRendering) {
			byte b0 = 0;
			byte b1 = 0;
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			float f = 0.0F;
			MapItemRenderer.this.textureManager.bindTexture(this.location);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(1, GL_ONE_MINUS_SRC_ALPHA, 0, 1);
			GlStateManager.disableAlpha();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldrenderer
					.pos((double) ((float) (b0 + 0) + f), (double) ((float) (b1 + 128) - f), -0.009999999776482582D)
					.tex(0.0D, 1.0D).endVertex();
			worldrenderer
					.pos((double) ((float) (b0 + 128) - f), (double) ((float) (b1 + 128) - f), -0.009999999776482582D)
					.tex(1.0D, 1.0D).endVertex();
			worldrenderer
					.pos((double) ((float) (b0 + 128) - f), (double) ((float) (b1 + 0) + f), -0.009999999776482582D)
					.tex(1.0D, 0.0D).endVertex();
			worldrenderer.pos((double) ((float) (b0 + 0) + f), (double) ((float) (b1 + 0) + f), -0.009999999776482582D)
					.tex(0.0D, 0.0D).endVertex();
			tessellator.draw();
			GlStateManager.enableAlpha();
			GlStateManager.disableBlend();
			MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.mapIcons);
			int i = 0;

			for (Vec4b vec4b : this.mapData.mapDecorations.values()) {
				if (!noOverlayRendering || vec4b.func_176110_a() == 1) {
					GlStateManager.pushMatrix();
					GlStateManager.translate((float) b0 + (float) vec4b.func_176112_b() / 2.0F + 64.0F,
							(float) b1 + (float) vec4b.func_176113_c() / 2.0F + 64.0F, -0.02F);
					GlStateManager.rotate((float) (vec4b.func_176111_d() * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
					GlStateManager.scale(4.0F, 4.0F, 3.0F);
					GlStateManager.translate(-0.125F, 0.125F, 0.0F);
					byte b2 = vec4b.func_176110_a();
					float f1 = (float) (b2 % 4 + 0) / 4.0F;
					float f2 = (float) (b2 / 4 + 0) / 4.0F;
					float f3 = (float) (b2 % 4 + 1) / 4.0F;
					float f4 = (float) (b2 / 4 + 1) / 4.0F;
					worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
					float f5 = -0.001F;
					worldrenderer.pos(-1.0D, 1.0D, (double) ((float) i * -0.001F)).tex((double) f1, (double) f2)
							.endVertex();
					worldrenderer.pos(1.0D, 1.0D, (double) ((float) i * -0.001F)).tex((double) f3, (double) f2)
							.endVertex();
					worldrenderer.pos(1.0D, -1.0D, (double) ((float) i * -0.001F)).tex((double) f3, (double) f4)
							.endVertex();
					worldrenderer.pos(-1.0D, -1.0D, (double) ((float) i * -0.001F)).tex((double) f1, (double) f4)
							.endVertex();
					tessellator.draw();
					GlStateManager.popMatrix();
					++i;
				}
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 0.0F, -0.04F);
			GlStateManager.scale(1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		}
	}
}