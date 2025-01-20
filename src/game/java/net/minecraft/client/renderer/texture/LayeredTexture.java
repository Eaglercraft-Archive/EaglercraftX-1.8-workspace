package net.minecraft.client.renderer.texture;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.client.resources.IResourceManager;
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
public class LayeredTexture extends AbstractTexture {
	private static final Logger logger = LogManager.getLogger();
	public final List<String> layeredTextureNames;

	public LayeredTexture(String... textureNames) {
		this.layeredTextureNames = Lists.newArrayList(textureNames);
	}

	public void loadTexture(IResourceManager parIResourceManager) throws IOException {
		this.deleteGlTexture();
		ImageData bufferedimage = null;

		try {
			for (int i = 0, l = this.layeredTextureNames.size(); i < l; ++i) {
				String s = this.layeredTextureNames.get(i);
				if (s != null) {
					InputStream inputstream = parIResourceManager.getResource(new ResourceLocation(s)).getInputStream();
					ImageData bufferedimage1 = TextureUtil.readBufferedImage(inputstream);
					if (bufferedimage == null) {
						bufferedimage = new ImageData(bufferedimage1.width, bufferedimage1.height, true);
					}

					bufferedimage.drawLayer(bufferedimage1, 0, 0, bufferedimage1.width, bufferedimage1.height, 0, 0,
							bufferedimage1.width, bufferedimage1.height);
				}
			}
		} catch (IOException ioexception) {
			logger.error("Couldn\'t load layered image", ioexception);
			return;
		}

		regenerateIfNotAllocated();
		TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
	}
}