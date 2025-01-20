package net.minecraft.client.renderer.texture;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.io.IOException;
import java.io.InputStream;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
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
public class TextureUtil {
	private static final Logger logger = LogManager.getLogger();
	private static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
	public static final DynamicTexture missingTexture = new DynamicTexture(16, 16);
	public static final int[] missingTextureData = missingTexture.getTextureData();
	private static final int[] mipmapBuffer;

	public static int glGenTextures() {
		return GlStateManager.generateTexture();
	}

	public static void deleteTexture(int textureId) {
		GlStateManager.deleteTexture(textureId);
	}

	public static int uploadTextureImage(int parInt1, ImageData parBufferedImage) {
		return uploadTextureImageAllocate(parInt1, parBufferedImage, false, false);
	}

	public static void uploadTexture(int textureId, int[] parArrayOfInt, int parInt2, int parInt3) {
		bindTexture(textureId);
		uploadTextureSub(0, parArrayOfInt, parInt2, parInt3, 0, 0, false, false, false);
	}

	public static int[][] generateMipmapData(int parInt1, int parInt2, int[][] parArrayOfarray) {
		int[][] aint = new int[parInt1 + 1][];
		aint[0] = parArrayOfarray[0];
		if (parInt1 > 0) {
			boolean flag = false;

			for (int i = 0; i < parArrayOfarray.length; ++i) {
				if (parArrayOfarray[0][i] >> 24 == 0) {
					flag = true;
					break;
				}
			}

			for (int l1 = 1; l1 <= parInt1; ++l1) {
				if (parArrayOfarray[l1] != null) {
					aint[l1] = parArrayOfarray[l1];
				} else {
					int[] aint1 = aint[l1 - 1];
					int[] aint2 = new int[aint1.length >> 2];
					int j = parInt2 >> l1;
					int k = aint2.length / j;
					int l = j << 1;

					for (int i1 = 0; i1 < j; ++i1) {
						for (int j1 = 0; j1 < k; ++j1) {
							int k1 = 2 * (i1 + j1 * l);
							aint2[i1 + j1 * j] = blendColors(aint1[k1 + 0], aint1[k1 + 1], aint1[k1 + 0 + l],
									aint1[k1 + 1 + l], flag);
						}
					}

					aint[l1] = aint2;
				}
			}
		}

		return aint;
	}

	private static int blendColors(int parInt1, int parInt2, int parInt3, int parInt4, boolean parFlag) {
		if (!parFlag) {
			int i1 = blendColorComponent(parInt1, parInt2, parInt3, parInt4, 24);
			int j1 = blendColorComponent(parInt1, parInt2, parInt3, parInt4, 16);
			int k1 = blendColorComponent(parInt1, parInt2, parInt3, parInt4, 8);
			int l1 = blendColorComponent(parInt1, parInt2, parInt3, parInt4, 0);
			return i1 << 24 | j1 << 16 | k1 << 8 | l1;
		} else {
			mipmapBuffer[0] = parInt1;
			mipmapBuffer[1] = parInt2;
			mipmapBuffer[2] = parInt3;
			mipmapBuffer[3] = parInt4;
			float f = 0.0F;
			float f1 = 0.0F;
			float f2 = 0.0F;
			float f3 = 0.0F;

			for (int i = 0; i < 4; ++i) {
				if (mipmapBuffer[i] >> 24 != 0) {
					f += (float) Math.pow((double) ((float) (mipmapBuffer[i] >> 24 & 255) / 255.0F), 2.2D);
					f1 += (float) Math.pow((double) ((float) (mipmapBuffer[i] >> 16 & 255) / 255.0F), 2.2D);
					f2 += (float) Math.pow((double) ((float) (mipmapBuffer[i] >> 8 & 255) / 255.0F), 2.2D);
					f3 += (float) Math.pow((double) ((float) (mipmapBuffer[i] >> 0 & 255) / 255.0F), 2.2D);
				}
			}

			f = f / 4.0F;
			f1 = f1 / 4.0F;
			f2 = f2 / 4.0F;
			f3 = f3 / 4.0F;
			int i2 = (int) (Math.pow((double) f, 0.45454545454545453D) * 255.0D);
			int j = (int) (Math.pow((double) f1, 0.45454545454545453D) * 255.0D);
			int k = (int) (Math.pow((double) f2, 0.45454545454545453D) * 255.0D);
			int l = (int) (Math.pow((double) f3, 0.45454545454545453D) * 255.0D);
			if (i2 < 96) {
				i2 = 0;
			}

			return i2 << 24 | j << 16 | k << 8 | l;
		}
	}

	private static int blendColorComponent(int parInt1, int parInt2, int parInt3, int parInt4, int parInt5) {
		float f = (float) Math.pow((double) ((float) (parInt1 >> parInt5 & 255) / 255.0F), 2.2D);
		float f1 = (float) Math.pow((double) ((float) (parInt2 >> parInt5 & 255) / 255.0F), 2.2D);
		float f2 = (float) Math.pow((double) ((float) (parInt3 >> parInt5 & 255) / 255.0F), 2.2D);
		float f3 = (float) Math.pow((double) ((float) (parInt4 >> parInt5 & 255) / 255.0F), 2.2D);
		float f4 = (float) Math.pow((double) (f + f1 + f2 + f3) * 0.25D, 0.45454545454545453D);
		return (int) ((double) f4 * 255.0D);
	}

	public static void uploadTextureMipmap(int[][] parArrayOfarray, int parInt1, int parInt2, int parInt3, int parInt4,
			boolean parFlag, boolean parFlag2) {
		for (int i = 0; i < parArrayOfarray.length; ++i) {
			int[] aint = parArrayOfarray[i];
			uploadTextureSub(i, aint, parInt1 >> i, parInt2 >> i, parInt3 >> i, parInt4 >> i, parFlag, parFlag2,
					parArrayOfarray.length > 1);
		}

	}

	private static void uploadTextureSub(int parInt1, int[] parArrayOfInt, int parInt2, int parInt3, int parInt4,
			int parInt5, boolean parFlag, boolean parFlag2, boolean parFlag3) {
		int i = 4194304 / parInt2;
		setTextureBlurMipmap(parFlag, parFlag3);
		if (!parFlag2 && !EaglercraftGPU.checkNPOTCapable() && ImageData.isNPOTStatic(parInt2, parInt3)) {
			parFlag2 = true;
			logger.warn(
					"An NPOT (non-power-of-two) texture was allocated with GL_REPEAT wrapping in an OpenGL context where that isn't supported, changing to GL_CLAMP_TO_EDGE to avoid errors");
		}
		setTextureClamped(parFlag2);

		int l;
		for (int j = 0; j < parInt2 * parInt3; j += parInt2 * l) {
			int k = j / parInt2;
			l = Math.min(i, parInt3 - k);
			int i1 = parInt2 * l;
			copyToBufferPos(parArrayOfInt, j, i1);
			EaglercraftGPU.glTexSubImage2D(GL_TEXTURE_2D, parInt1, parInt4, parInt5 + k, parInt2, l, GL_RGBA,
					GL_UNSIGNED_BYTE, dataBuffer);
		}

	}

	public static int uploadTextureImageAllocate(int parInt1, ImageData parBufferedImage, boolean parFlag,
			boolean parFlag2) {
		allocateTexture(parInt1, parBufferedImage.width, parBufferedImage.height);
		return uploadTextureImageSub(parInt1, parBufferedImage, 0, 0, parFlag, parFlag2);
	}

	public static void allocateTexture(int parInt1, int parInt2, int parInt3) {
		allocateTextureImpl(parInt1, 0, parInt2, parInt3);
	}

	public static void allocateTextureImpl(int parInt1, int parInt2, int parInt3, int parInt4) {
		// deleteTexture(parInt1); //TODO: why
		bindTexture(parInt1);
		if (parInt2 >= 0) {
			if (EaglercraftGPU.checkOpenGLESVersion() >= 300) {
				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, '\u813d', parInt2);
				EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u813a', 0.0F);
				EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u813b', (float) parInt2);
				// EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u8501', 0.0F);
			}
		}
		EaglercraftGPU.glTexStorage2D(GL_TEXTURE_2D, parInt2 + 1, GL_RGBA8, parInt3, parInt4);
	}

	public static int uploadTextureImageSub(int textureId, ImageData parBufferedImage, int parInt2, int parInt3,
			boolean parFlag, boolean parFlag2) {
		bindTexture(textureId);
		uploadTextureImageSubImpl(parBufferedImage, parInt2, parInt3, parFlag, parFlag2);
		return textureId;
	}

	private static void uploadTextureImageSubImpl(ImageData parBufferedImage, int parInt1, int parInt2, boolean parFlag,
			boolean parFlag2) {
		int i = parBufferedImage.width;
		int j = parBufferedImage.height;
		int k = 4194304 / i;
		int[] aint = new int[k * i];
		setTextureBlurred(parFlag);
		if (!parFlag2 && !EaglercraftGPU.checkNPOTCapable() && parBufferedImage.isNPOT()) {
			parFlag2 = true;
			logger.warn(
					"An NPOT (non-power-of-two) texture was allocated with GL_REPEAT wrapping in an OpenGL context where that isn't supported, changing to GL_CLAMP_TO_EDGE to avoid errors");
		}
		setTextureClamped(parFlag2);

		for (int l = 0; l < i * j; l += i * k) {
			int i1 = l / i;
			int j1 = Math.min(k, j - i1);
			int k1 = i * j1;
			parBufferedImage.getRGB(0, i1, i, j1, aint, 0, i);
			copyToBuffer(aint, k1);
			EaglercraftGPU.glTexSubImage2D(GL_TEXTURE_2D, 0, parInt1, parInt2 + i1, i, j1, GL_RGBA, GL_UNSIGNED_BYTE,
					dataBuffer);
		}

	}

	private static void setTextureClamped(boolean parFlag) {
		if (parFlag) {
			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		} else {
			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		}

	}

	private static void setTextureBlurred(boolean parFlag) {
		setTextureBlurMipmap(parFlag, false);
	}

	private static void setTextureBlurMipmap(boolean parFlag, boolean parFlag2) {
		if (parFlag) {
			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, parFlag2 ? 9987 : 9729);
			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		} else {
			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, parFlag2 ? 9986 : 9728);
			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		}

	}

	private static void copyToBuffer(int[] parArrayOfInt, int parInt1) {
		copyToBufferPos(parArrayOfInt, 0, parInt1);
	}

	private static void copyToBufferPos(int[] parArrayOfInt, int parInt1, int parInt2) {
		int[] aint = parArrayOfInt;
		if (Minecraft.getMinecraft().gameSettings.anaglyph) {
			aint = updateAnaglyph(parArrayOfInt);
		}

		dataBuffer.clear();
		dataBuffer.put(aint, parInt1, parInt2);
		dataBuffer.position(0).limit(parInt2);
	}

	static void bindTexture(int parInt1) {
		GlStateManager.bindTexture(parInt1);
	}

	public static int[] readImageData(IResourceManager resourceManager, ResourceLocation imageLocation)
			throws IOException {
		return readBufferedImage(resourceManager.getResource(imageLocation).getInputStream()).pixels;
	}

	public static ImageData readBufferedImage(InputStream imageStream) throws IOException {
		ImageData bufferedimage;
		try {
			bufferedimage = ImageData.loadImageFile(imageStream);
		} finally {
			IOUtils.closeQuietly(imageStream);
		}

		return bufferedimage;
	}

	public static int[] updateAnaglyph(int[] parArrayOfInt) {
		int[] aint = new int[parArrayOfInt.length];

		for (int i = 0; i < parArrayOfInt.length; ++i) {
			aint[i] = anaglyphColor(parArrayOfInt[i]);
		}

		return aint;
	}

	public static int anaglyphColor(int parInt1) {
		int i = parInt1 >> 24 & 255;
		int j = parInt1 >> 16 & 255;
		int k = parInt1 >> 8 & 255;
		int l = parInt1 & 255;
		int i1 = (j * 30 + k * 59 + l * 11) / 100;
		int j1 = (j * 30 + k * 70) / 100;
		int k1 = (j * 30 + l * 70) / 100;
		return i << 24 | i1 << 16 | j1 << 8 | k1;
	}

	public static void processPixelValues(int[] parArrayOfInt, int parInt1, int parInt2) {
		int[] aint = new int[parInt1];
		int i = parInt2 / 2;

		for (int j = 0; j < i; ++j) {
			System.arraycopy(parArrayOfInt, j * parInt1, aint, 0, parInt1);
			System.arraycopy(parArrayOfInt, (parInt2 - 1 - j) * parInt1, parArrayOfInt, j * parInt1, parInt1);
			System.arraycopy(aint, 0, parArrayOfInt, (parInt2 - 1 - j) * parInt1, parInt1);
		}

	}

	public static int[] convertComponentOrder(int[] arr) {
		for (int i = 0; i < arr.length; ++i) {
			int j = arr[i];
			arr[i] = ((j >> 16) & 0xFF) | (j & 0xFF00FF00) | ((j << 16) & 0xFF0000);
		}
		return arr;
	}

	static {
		int i = -16777216;
		int j = -524040;
		int[] aint = new int[] { -524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040 };
		int[] aint1 = new int[] { -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216,
				-16777216 };
		int k = aint.length;

		for (int l = 0; l < 16; ++l) {
			System.arraycopy(l < k ? aint : aint1, 0, missingTextureData, 16 * l, k);
			System.arraycopy(l < k ? aint1 : aint, 0, missingTextureData, 16 * l + k, k);
		}

		missingTexture.updateDynamicTexture();
		mipmapBuffer = new int[4];
	}
}