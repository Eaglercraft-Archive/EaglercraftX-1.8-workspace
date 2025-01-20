package net.minecraft.client.renderer.texture;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.EnumDyeColor;
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
public class LayeredColorMaskTexture extends AbstractTexture {
	/**+
	 * Access to the Logger, for all your logging needs.
	 */
	private static final Logger LOG = LogManager.getLogger();
	private final ResourceLocation textureLocation;
	private final List<String> field_174949_h;
	private final List<EnumDyeColor> field_174950_i;

	public LayeredColorMaskTexture(ResourceLocation textureLocationIn, List<String> parList,
			List<EnumDyeColor> parList2) {
		this.textureLocation = textureLocationIn;
		this.field_174949_h = parList;
		this.field_174950_i = parList2;
	}

	public void loadTexture(IResourceManager parIResourceManager) throws IOException {
		this.deleteGlTexture();

		ImageData bufferedimage;
		try {
			ImageData bufferedimage1 = TextureUtil
					.readBufferedImage(parIResourceManager.getResource(this.textureLocation).getInputStream());

			bufferedimage = new ImageData(bufferedimage1.width, bufferedimage1.height, false);
			bufferedimage.drawLayer(bufferedimage1, 0, 0, bufferedimage1.width, bufferedimage1.height, 0, 0,
					bufferedimage1.width, bufferedimage1.height);

			for (int j = 0; j < 17 && j < this.field_174949_h.size() && j < this.field_174950_i.size(); ++j) {
				String s = (String) this.field_174949_h.get(j);
				MapColor mapcolor = ((EnumDyeColor) this.field_174950_i.get(j)).getMapColor();
				if (s != null) {
					InputStream inputstream = parIResourceManager.getResource(new ResourceLocation(s)).getInputStream();
					ImageData bufferedimage2 = TextureUtil.readBufferedImage(inputstream);
					if (bufferedimage2.width == bufferedimage.width && bufferedimage2.height == bufferedimage.height) {
						for (int k = 0; k < bufferedimage2.height; ++k) {
							for (int l = 0; l < bufferedimage2.width; ++l) {
								int i1 = bufferedimage2.pixels[k * bufferedimage2.width + l];
								if ((i1 & -16777216) != 0) {
									int j1 = (i1 & 16711680) << 8 & -16777216;
									int k1 = bufferedimage1.pixels[k * bufferedimage1.width + l];
									int l1 = MathHelper.func_180188_d(k1, ImageData.swapRB(mapcolor.colorValue))
											& 16777215;
									bufferedimage2.pixels[k * bufferedimage2.width + l] = j1 | l1;
								}
							}
						}

						bufferedimage.drawLayer(bufferedimage2, 0, 0, bufferedimage2.width, bufferedimage2.height, 0, 0,
								bufferedimage2.width, bufferedimage2.height);
					}
				}
			}
		} catch (IOException ioexception) {
			LOG.error("Couldn\'t load layered image", ioexception);
			return;
		}

		regenerateIfNotAllocated();
		TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
	}
}