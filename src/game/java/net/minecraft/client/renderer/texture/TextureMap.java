package net.minecraft.client.renderer.texture;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import com.carrotsearch.hppc.IntIndexedContainer;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.EaglerTextureAtlasSpritePBR;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.PBRTextureMapUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.optifine.BetterGrass;
import net.optifine.ConnectedTextures;
import net.optifine.CustomItems;
import net.optifine.util.CounterInt;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

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
public class TextureMap extends AbstractTexture implements ITickableTextureObject {
	private static final Logger logger = LogManager.getLogger();
	public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
	public static final ResourceLocation locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
	private final List<EaglerTextureAtlasSprite> listAnimatedSprites;
	private final Map<String, EaglerTextureAtlasSprite> mapRegisteredSprites;
	private final Map<String, EaglerTextureAtlasSprite> mapUploadedSprites;
	private final String basePath;
	private final IIconCreator iconCreator;
	private int mipmapLevels;
	private final EaglerTextureAtlasSprite missingImage;
	private final EaglerTextureAtlasSpritePBR missingImagePBR;
	private int width;
	private int height;
	private boolean isEaglerPBRMode = false;
	public int eaglerPBRMaterialTexture = -1;
	private boolean hasAllocatedEaglerPBRMaterialTexture = false;
	private boolean isGLES2 = false;
	private CounterInt counterIndexInMap;

	public static final int _GL_FRAMEBUFFER = 0x8D40;
	public static final int _GL_COLOR_ATTACHMENT0 = 0x8CE0;

	private IFramebufferGL[] copyColorFramebuffer = null;
	private IFramebufferGL[] copyMaterialFramebuffer = null;

	public TextureMap(String parString1) {
		this(parString1, (IIconCreator) null);
	}

	public TextureMap(String parString1, IIconCreator iconCreatorIn) {
		this.listAnimatedSprites = Lists.newArrayList();
		this.mapRegisteredSprites = Maps.newHashMap();
		this.mapUploadedSprites = Maps.newHashMap();
		this.missingImage = new EaglerTextureAtlasSprite("missingno");
		this.missingImagePBR = new EaglerTextureAtlasSpritePBR("missingno");
		this.basePath = parString1;
		this.iconCreator = iconCreatorIn;
		this.isGLES2 = EaglercraftGPU.checkOpenGLESVersion() == 200;
		this.counterIndexInMap = new CounterInt(0);
	}

	private void initMissingImage() {
		int[] aint = TextureUtil.missingTextureData;
		this.missingImage.setIconWidth(16);
		this.missingImage.setIconHeight(16);
		int[][] aint1 = new int[this.mipmapLevels + 1][];
		aint1[0] = aint;
		this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][] { aint1 }));
		int idx = this.counterIndexInMap.nextValue();
		this.missingImage.setIndexInMap(idx);
		this.missingImagePBR.setIconWidth(16);
		this.missingImagePBR.setIconHeight(16);
		int[][][] aint2 = new int[3][this.mipmapLevels + 1][];
		aint2[0][0] = aint;
		int[] missingNormals = new int[256];
		for (int i = 0; i < missingNormals.length; ++i) {
			missingNormals[i] = 0xFF7F7F;
		}
		aint2[1][0] = missingNormals;
		int[] missingMaterial = new int[256];
		for (int i = 0; i < missingMaterial.length; ++i) {
			missingMaterial[i] = 0x00000077;
		}
		aint2[2][0] = missingMaterial;
		this.missingImagePBR.setFramesTextureDataPBR(new List[] { Lists.newArrayList(new int[][][] { aint2[0] }),
				Lists.newArrayList(new int[][][] { aint2[1] }), Lists.newArrayList(new int[][][] { aint2[2] }) });
		this.missingImagePBR.setIndexInMap(idx);
	}

	public void loadTexture(IResourceManager parIResourceManager) throws IOException {
		if (this.iconCreator != null) {
			this.loadSprites(parIResourceManager, this.iconCreator);
		}
	}

	public void loadSprites(IResourceManager resourceManager, IIconCreator parIIconCreator) {
		destroyAnimationCaches();
		this.mapRegisteredSprites.clear();
		this.counterIndexInMap.reset();
		parIIconCreator.registerSprites(this);
		this.initMissingImage();
		this.deleteGlTexture();
		this.loadTextureAtlas(resourceManager);
	}

	public void deleteGlTexture() {
		super.deleteGlTexture();
		if (eaglerPBRMaterialTexture != -1) {
			GlStateManager.deleteTexture(eaglerPBRMaterialTexture);
			eaglerPBRMaterialTexture = -1;
		}
		if (copyColorFramebuffer != null) {
			for (int i = 0; i < copyColorFramebuffer.length; ++i) {
				_wglDeleteFramebuffer(copyColorFramebuffer[i]);
			}
			copyColorFramebuffer = null;
		}
		if (copyMaterialFramebuffer != null) {
			for (int i = 0; i < copyMaterialFramebuffer.length; ++i) {
				_wglDeleteFramebuffer(copyMaterialFramebuffer[i]);
			}
			copyMaterialFramebuffer = null;
		}
	}

	public void loadTextureAtlas(IResourceManager resourceManager) {
		ConnectedTextures.updateIcons(this);
		CustomItems.updateIcons(this);
		BetterGrass.updateIcons(this);
		int i = Minecraft.getGLMaximumTextureSize();
		Stitcher stitcher = new Stitcher(i, i, true, 0, this.mipmapLevels);
		this.mapUploadedSprites.clear();
		this.listAnimatedSprites.clear();
		int j = Integer.MAX_VALUE;
		int k = 1 << this.mipmapLevels;

		if (copyColorFramebuffer != null) {
			for (int l = 0; l < copyColorFramebuffer.length; ++l) {
				_wglDeleteFramebuffer(copyColorFramebuffer[l]);
			}
			copyColorFramebuffer = null;
		}

		if (isEaglerPBRMode) {
			if (eaglerPBRMaterialTexture == -1) {
				eaglerPBRMaterialTexture = GlStateManager.generateTexture();
				hasAllocatedEaglerPBRMaterialTexture = false;
			}
			if (copyMaterialFramebuffer == null) {
				GlStateManager.bindTexture(eaglerPBRMaterialTexture);
				copyMaterialFramebuffer = new IFramebufferGL[this.mipmapLevels + 1];
				for (int l = 0; l < copyMaterialFramebuffer.length; ++l) {
					copyMaterialFramebuffer[l] = _wglCreateFramebuffer();
					_wglBindFramebuffer(_GL_FRAMEBUFFER, copyMaterialFramebuffer[l]);
					_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
							EaglercraftGPU.getNativeTexture(eaglerPBRMaterialTexture), l);
				}
				_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
			}
		} else {
			if (eaglerPBRMaterialTexture != -1) {
				GlStateManager.deleteTexture(eaglerPBRMaterialTexture);
				eaglerPBRMaterialTexture = -1;
			}
			if (copyMaterialFramebuffer != null) {
				for (int l = 0; l < copyMaterialFramebuffer.length; ++l) {
					_wglDeleteFramebuffer(copyMaterialFramebuffer[l]);
				}
				copyMaterialFramebuffer = null;
			}
		}

		for (Entry entry : this.mapRegisteredSprites.entrySet()) {
			EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) entry.getValue();
			textureatlassprite.updateIndexInMap(this.counterIndexInMap);
			ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
			ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation, 0);

			if (isEaglerPBRMode) {
				try {
					IResource iresource = resourceManager.getResource(resourcelocation1);
					ImageData[] abufferedimageColor = new ImageData[1 + this.mipmapLevels];
					ImageData[] abufferedimageNormal = new ImageData[1 + this.mipmapLevels];
					ImageData[] abufferedimageMaterial = new ImageData[1 + this.mipmapLevels];
					abufferedimageColor[0] = TextureUtil.readBufferedImage(iresource.getInputStream());
					abufferedimageNormal[0] = PBRTextureMapUtils.locateCompanionTexture(resourceManager, iresource,
							"_n");
					abufferedimageMaterial[0] = PBRTextureMapUtils.locateCompanionTexture(resourceManager, iresource,
							"_s");
					boolean dontAnimateNormals = false;
					boolean dontAnimateMaterial = false;
					if (abufferedimageNormal[0] == null) {
						abufferedimageNormal[0] = PBRTextureMapUtils.defaultNormalsTexture;
						dontAnimateNormals = true;
					}
					if (abufferedimageMaterial[0] == null) {
						abufferedimageMaterial[0] = PBRTextureMapUtils.generateMaterialTextureFor(
								textureatlassprite.getIconName(), textureatlassprite.optifineBaseTextureName);
						dontAnimateMaterial = true;
					}
					PBRTextureMapUtils.unifySizes(0, abufferedimageColor, abufferedimageNormal, abufferedimageMaterial);

					TextureMetadataSection texturemetadatasection = (TextureMetadataSection) iresource
							.getMetadata("texture");
					if (texturemetadatasection != null) {
						IntIndexedContainer list = texturemetadatasection.getListMipmaps();
						if (!list.isEmpty()) {
							int l = abufferedimageColor[0].width;
							int i1 = abufferedimageColor[0].height;
							if (MathHelper.roundUpToPowerOfTwo(l) != l || MathHelper.roundUpToPowerOfTwo(i1) != i1) {
								throw new RuntimeException(
										"Unable to load extra miplevels, source-texture is not power of two");
							}
						}
						for (IntCursor cur : list) {
							int i2 = cur.value;
							if (i2 > 0 && i2 < abufferedimageColor.length - 1 && abufferedimageColor[i2] == null) {
								ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation,
										i2);

								try {
									IResource mipLevelResource = resourceManager.getResource(resourcelocation2);
									abufferedimageColor[i2] = TextureUtil
											.readBufferedImage(mipLevelResource.getInputStream());
									abufferedimageNormal[i2] = PBRTextureMapUtils
											.locateCompanionTexture(resourceManager, mipLevelResource, "_n");
									abufferedimageMaterial[i2] = PBRTextureMapUtils
											.locateCompanionTexture(resourceManager, mipLevelResource, "_s");
									if (abufferedimageNormal[i2] == null) {
										abufferedimageNormal[i2] = PBRTextureMapUtils.defaultNormalsTexture;
									}
									if (abufferedimageMaterial[i2] == null) {
										abufferedimageMaterial[i2] = PBRTextureMapUtils.generateMaterialTextureFor(
												textureatlassprite.getIconName(),
												textureatlassprite.optifineBaseTextureName);
									}
									PBRTextureMapUtils.unifySizes(i2, abufferedimageColor, abufferedimageNormal,
											abufferedimageMaterial);
									if ((abufferedimageColor[0].width >> i2) != abufferedimageColor[i2].width) {
										throw new IOException("Mipmap level " + i2 + " is the wrong size, should be "
												+ (abufferedimageColor[0].width >> i2) + " pixels");
									}
								} catch (Throwable exc) {
									logger.error("Unable to load miplevel {} from: {}", i2, resourcelocation2);
									logger.error(exc);
								}
							}
						}
					}

					AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection) iresource
							.getMetadata("animation");
					textureatlassprite.loadSpritePBR(
							new ImageData[][] { abufferedimageColor, abufferedimageNormal, abufferedimageMaterial },
							animationmetadatasection, dontAnimateNormals, dontAnimateMaterial);
				} catch (RuntimeException runtimeexception) {
					logger.error("Unable to parse metadata from " + resourcelocation1);
					logger.error(runtimeexception);
					continue;
				} catch (IOException ioexception1) {
					logger.error("Using missing texture, unable to load " + resourcelocation1);
					logger.error(ioexception1);
					continue;
				}

				j = Math.min(j, Math.min(textureatlassprite.getIconWidth(), textureatlassprite.getIconHeight()));
				int l1 = Math.min(Integer.lowestOneBit(textureatlassprite.getIconWidth()),
						Integer.lowestOneBit(textureatlassprite.getIconHeight()));
				if (l1 < k) {
					logger.warn("Texture {} with size {}x{} limits mip level from {} to {}",
							new Object[] { resourcelocation1, Integer.valueOf(textureatlassprite.getIconWidth()),
									Integer.valueOf(textureatlassprite.getIconHeight()),
									Integer.valueOf(MathHelper.calculateLogBaseTwo(k)),
									Integer.valueOf(MathHelper.calculateLogBaseTwo(l1)) });
					k = l1;
				}

				stitcher.addSprite(textureatlassprite);
				continue;
			}

			try {
				IResource iresource = resourceManager.getResource(resourcelocation1);
				ImageData[] abufferedimage = new ImageData[1 + this.mipmapLevels];
				abufferedimage[0] = TextureUtil.readBufferedImage(iresource.getInputStream());
				TextureMetadataSection texturemetadatasection = (TextureMetadataSection) iresource
						.getMetadata("texture");
				if (texturemetadatasection != null) {
					IntIndexedContainer list = texturemetadatasection.getListMipmaps();
					if (!list.isEmpty()) {
						int l = abufferedimage[0].width;
						int i1 = abufferedimage[0].height;
						if (MathHelper.roundUpToPowerOfTwo(l) != l || MathHelper.roundUpToPowerOfTwo(i1) != i1) {
							throw new RuntimeException(
									"Unable to load extra miplevels, source-texture is not power of two");
						}
					}

					for (IntCursor cur : list) {
						int i2 = cur.value;
						if (i2 > 0 && i2 < abufferedimage.length - 1 && abufferedimage[i2] == null) {
							ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation, i2);

							try {
								abufferedimage[i2] = TextureUtil.readBufferedImage(
										resourceManager.getResource(resourcelocation2).getInputStream());
							} catch (IOException ioexception) {
								logger.error("Unable to load miplevel {} from: {}",
										new Object[] { Integer.valueOf(i2), resourcelocation2 });
								logger.error(ioexception);
							}
						}
					}
				}

				AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection) iresource
						.getMetadata("animation");
				textureatlassprite.loadSprite(abufferedimage, animationmetadatasection);
			} catch (RuntimeException runtimeexception) {
				logger.error("Unable to parse metadata from " + resourcelocation1);
				logger.error(runtimeexception);
				continue;
			} catch (IOException ioexception1) {
				logger.error("Using missing texture, unable to load " + resourcelocation1);
				logger.error(ioexception1);
				continue;
			}

			j = Math.min(j, Math.min(textureatlassprite.getIconWidth(), textureatlassprite.getIconHeight()));
			int l1 = Math.min(Integer.lowestOneBit(textureatlassprite.getIconWidth()),
					Integer.lowestOneBit(textureatlassprite.getIconHeight()));
			if (l1 < k) {
				logger.warn("Texture {} with size {}x{} limits mip level from {} to {}",
						new Object[] { resourcelocation1, Integer.valueOf(textureatlassprite.getIconWidth()),
								Integer.valueOf(textureatlassprite.getIconHeight()),
								Integer.valueOf(MathHelper.calculateLogBaseTwo(k)),
								Integer.valueOf(MathHelper.calculateLogBaseTwo(l1)) });
				k = l1;
			}

			stitcher.addSprite(textureatlassprite);
		}

		int j1 = Math.min(j, k);
		int k1 = MathHelper.calculateLogBaseTwo(j1);
		if (k1 < this.mipmapLevels) {
			logger.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[] {
					this.basePath, Integer.valueOf(this.mipmapLevels), Integer.valueOf(k1), Integer.valueOf(j1) });
			this.mipmapLevels = k1;
		}

		for (final EaglerTextureAtlasSprite textureatlassprite1 : this.mapRegisteredSprites.values()) {
			try {
				textureatlassprite1.generateMipmaps(this.mipmapLevels);
			} catch (Throwable throwable1) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
				crashreportcategory.addCrashSectionCallable("Sprite name", new Callable<String>() {
					public String call() throws Exception {
						return textureatlassprite1.getIconName();
					}
				});
				crashreportcategory.addCrashSectionCallable("Sprite size", new Callable<String>() {
					public String call() throws Exception {
						return textureatlassprite1.getIconWidth() + " x " + textureatlassprite1.getIconHeight();
					}
				});
				crashreportcategory.addCrashSectionCallable("Sprite frames", new Callable<String>() {
					public String call() throws Exception {
						return textureatlassprite1.getFrameCount() + " frames";
					}
				});
				crashreportcategory.addCrashSection("Mipmap levels", Integer.valueOf(this.mipmapLevels));
				throw new ReportedException(crashreport);
			}
		}

		if (isEaglerPBRMode) {
			this.missingImagePBR.generateMipmaps(this.mipmapLevels);
			stitcher.addSprite(this.missingImagePBR);
		} else {
			this.missingImage.generateMipmaps(this.mipmapLevels);
			stitcher.addSprite(this.missingImage);
		}

		try {
			stitcher.doStitch();
		} catch (StitcherException stitcherexception) {
			throw stitcherexception;
		}

		logger.info("Created: {}x{} {}-atlas", new Object[] { Integer.valueOf(stitcher.getCurrentWidth()),
				Integer.valueOf(stitcher.getCurrentHeight()), this.basePath });
		regenerateIfNotAllocated();
		TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(),
				stitcher.getCurrentHeight());

		if (isEaglerPBRMode) {
			if (hasAllocatedEaglerPBRMaterialTexture) {
				EaglercraftGPU.regenerateTexture(eaglerPBRMaterialTexture);
			}
			hasAllocatedEaglerPBRMaterialTexture = true;
			TextureUtil.allocateTextureImpl(eaglerPBRMaterialTexture, this.mipmapLevels, stitcher.getCurrentWidth(),
					stitcher.getCurrentHeight() * 2);
		}

		TextureUtil.bindTexture(this.glTextureId);

		copyColorFramebuffer = new IFramebufferGL[this.mipmapLevels + 1];
		for (int l = 0; l < copyColorFramebuffer.length; ++l) {
			copyColorFramebuffer[l] = _wglCreateFramebuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, copyColorFramebuffer[l]);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
					EaglercraftGPU.getNativeTexture(this.glTextureId), l);
		}

		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);

		HashMap hashmap = Maps.newHashMap(this.mapRegisteredSprites);

		width = stitcher.getCurrentWidth();
		height = stitcher.getCurrentHeight();

		List<EaglerTextureAtlasSprite> spriteList = stitcher.getStichSlots();
		for (int l = 0, m = spriteList.size(); l < m; ++l) {
			EaglerTextureAtlasSprite textureatlassprite2 = spriteList.get(l);
			String s = textureatlassprite2.getIconName();
			hashmap.remove(s);
			this.mapUploadedSprites.put(s, textureatlassprite2);

			try {
				TextureUtil.bindTexture(this.glTextureId);
				TextureUtil.uploadTextureMipmap(textureatlassprite2.getFrameTextureData(0),
						textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(),
						textureatlassprite2.getOriginX(), textureatlassprite2.getOriginY(), false, false);
				if (isEaglerPBRMode) {
					TextureUtil.bindTexture(eaglerPBRMaterialTexture);
					int[][][] pixels = ((EaglerTextureAtlasSpritePBR) textureatlassprite2).getFramePBRTextureData(0);
					TextureUtil.uploadTextureMipmap(pixels[1], textureatlassprite2.getIconWidth(),
							textureatlassprite2.getIconHeight(), textureatlassprite2.getOriginX(),
							textureatlassprite2.getOriginY(), false, false);
					TextureUtil.uploadTextureMipmap(pixels[2], textureatlassprite2.getIconWidth(),
							textureatlassprite2.getIconHeight(), textureatlassprite2.getOriginX(),
							textureatlassprite2.getOriginY() + height, false, false);
				}
			} catch (Throwable throwable) {
				CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
				CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Texture being stitched together");
				crashreportcategory1.addCrashSection("Atlas path", this.basePath);
				crashreportcategory1.addCrashSection("Sprite", textureatlassprite2);
				throw new ReportedException(crashreport1);
			}

			if (textureatlassprite2.hasAnimationMetadata()) {
				this.listAnimatedSprites.add(textureatlassprite2);
			}
		}

		for (EaglerTextureAtlasSprite textureatlassprite3 : (Collection<EaglerTextureAtlasSprite>) hashmap.values()) {
			textureatlassprite3.copyFrom(this.missingImage);
		}

		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
	}

	private ResourceLocation completeResourceLocation(ResourceLocation location, int parInt1) {
		return isAbsoluteLocation(location)
				? new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".png")
				: (parInt1 == 0
						? new ResourceLocation(location.getResourceDomain(),
								HString.format("%s/%s%s",
										new Object[] { this.basePath, location.getResourcePath(), ".png" }))
						: new ResourceLocation(location.getResourceDomain(),
								HString.format("%s/mipmaps/%s.%d%s", new Object[] { this.basePath,
										location.getResourcePath(), Integer.valueOf(parInt1), ".png" })));
	}

	private boolean isAbsoluteLocation(ResourceLocation p_isAbsoluteLocation_1_) {
		String s = p_isAbsoluteLocation_1_.getResourcePath();
		return this.isAbsoluteLocationPath(s);
	}

	private boolean isAbsoluteLocationPath(String p_isAbsoluteLocationPath_1_) {
		String s = p_isAbsoluteLocationPath_1_.toLowerCase();
		return s.startsWith("mcpatcher/") || s.startsWith("optifine/");
	}

	public EaglerTextureAtlasSprite getAtlasSprite(String iconName) {
		EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) this.mapUploadedSprites.get(iconName);
		if (textureatlassprite == null) {
			textureatlassprite = isEaglerPBRMode ? missingImagePBR : missingImage;
		}

		return textureatlassprite;
	}

	public void updateAnimations() {
		if (isEaglerPBRMode) {
			for (int j = 0, l = this.listAnimatedSprites.size(); j < l; ++j) {
				this.listAnimatedSprites.get(j).updateAnimationPBR();
			}
			for (int i = 0; i < copyColorFramebuffer.length; ++i) {
				int w = width >> i;
				int h = height >> i;
				_wglBindFramebuffer(_GL_FRAMEBUFFER, copyColorFramebuffer[i]);
				GlStateManager.viewport(0, 0, w, h);
				for (int j = 0, l = this.listAnimatedSprites.size(); j < l; ++j) {
					this.listAnimatedSprites.get(j).copyAnimationFramePBR(0, w, h, i);
				}
				_wglBindFramebuffer(_GL_FRAMEBUFFER, copyMaterialFramebuffer[i]);
				h <<= 1;
				GlStateManager.viewport(0, 0, w, h);
				for (int j = 0, l = this.listAnimatedSprites.size(); j < l; ++j) {
					this.listAnimatedSprites.get(j).copyAnimationFramePBR(1, w, h, i);
				}
			}
			_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
			return;
		}

		for (int j = 0, l = this.listAnimatedSprites.size(); j < l; ++j) {
			this.listAnimatedSprites.get(j).updateAnimation();
		}

		for (int i = 0; i < copyColorFramebuffer.length; ++i) {
			int w = width >> i;
			int h = height >> i;
			_wglBindFramebuffer(_GL_FRAMEBUFFER, copyColorFramebuffer[i]);
			GlStateManager.viewport(0, 0, w, h);
			for (int j = 0, l = this.listAnimatedSprites.size(); j < l; ++j) {
				this.listAnimatedSprites.get(j).copyAnimationFrame(w, h, i);
			}
		}

		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
	}

	private void destroyAnimationCaches() {
		for (int i = 0, l = this.listAnimatedSprites.size(); i < l; ++i) {
			this.listAnimatedSprites.get(i).clearFramesTextureData();
		}
	}

	public EaglerTextureAtlasSprite registerSprite(ResourceLocation location) {
		return registerSprite(location, null);
	}

	public EaglerTextureAtlasSprite registerSprite(ResourceLocation location, String locationOptifineBase) {
		if (location == null) {
			throw new IllegalArgumentException("Location cannot be null!");
		} else {
			EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) this.mapRegisteredSprites
					.get(location.toString());
			if (textureatlassprite == null) {
				if (isEaglerPBRMode) {
					textureatlassprite = EaglerTextureAtlasSpritePBR.makeAtlasSprite(location);
				} else {
					textureatlassprite = EaglerTextureAtlasSprite.makeAtlasSprite(location);
				}
				textureatlassprite.optifineBaseTextureName = locationOptifineBase;
				this.mapRegisteredSprites.put(location.toString(), textureatlassprite);
				textureatlassprite.updateIndexInMap(this.counterIndexInMap);
			}

			return textureatlassprite;
		}
	}

	public void tick() {
		this.updateAnimations();
	}

	public void setMipmapLevels(int mipmapLevelsIn) {
		if (!isGLES2) {
			this.mipmapLevels = mipmapLevelsIn;
		} else {
			this.mipmapLevels = 0; // Due to limitations in OpenGL ES 2.0 texture completeness, its easier to just
									// make this zero
		}
	}

	public EaglerTextureAtlasSprite getMissingSprite() {
		return isEaglerPBRMode ? missingImagePBR : missingImage;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setEnablePBREagler(boolean enable) {
		isEaglerPBRMode = enable;
	}

	public void setBlurMipmapDirect0(boolean parFlag, boolean parFlag2) {
		if (isGLES2) {
			super.setBlurMipmapDirect0(parFlag, false);
		} else {
			super.setBlurMipmapDirect0(parFlag, parFlag2);
			if (isEaglerPBRMode && eaglerPBRMaterialTexture != -1) {
				GlStateManager.setActiveTexture(GL_TEXTURE2);
				GlStateManager.bindTexture(eaglerPBRMaterialTexture);
				super.setBlurMipmapDirect0(parFlag, parFlag2);
				GlStateManager.setActiveTexture(GL_TEXTURE0);
			}
		}
	}

	public EaglerTextureAtlasSprite getSpriteSafe(String iconName) {
		ResourceLocation resourcelocation = new ResourceLocation(iconName);
		return this.mapRegisteredSprites.get(resourcelocation.toString());
	}

	public int getCountRegisteredSprites() {
		return this.counterIndexInMap.getValue();
	}
}