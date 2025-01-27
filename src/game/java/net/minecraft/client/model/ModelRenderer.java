package net.minecraft.client.model;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;

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
public class ModelRenderer {
	public float textureWidth;
	public float textureHeight;
	private int textureOffsetX;
	private int textureOffsetY;
	public float rotationPointX;
	public float rotationPointY;
	public float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;
	private boolean compiled;
	private int displayList;
	public boolean mirror;
	public boolean showModel;
	public boolean isHidden;
	public List<ModelBox> cubeList;
	public List<ModelRenderer> childModels;
	public final String boxName;
	private ModelBase baseModel;
	public float offsetX;
	public float offsetY;
	public float offsetZ;

	public ModelRenderer(ModelBase model, String boxNameIn) {
		this.textureWidth = 64.0F;
		this.textureHeight = 32.0F;
		this.showModel = true;
		this.cubeList = Lists.newArrayList();
		this.baseModel = model;
		model.boxList.add(this);
		this.boxName = boxNameIn;
		this.setTextureSize(model.textureWidth, model.textureHeight);
	}

	public ModelRenderer(ModelBase model) {
		this(model, (String) null);
	}

	public ModelRenderer(ModelBase model, int texOffX, int texOffY) {
		this(model);
		this.setTextureOffset(texOffX, texOffY);
	}

	/**+
	 * Sets the current box's rotation points and rotation angles to
	 * another box.
	 */
	public void addChild(ModelRenderer renderer) {
		if (this.childModels == null) {
			this.childModels = Lists.newArrayList();
		}

		this.childModels.add(renderer);
	}

	public ModelRenderer setTextureOffset(int x, int y) {
		this.textureOffsetX = x;
		this.textureOffsetY = y;
		return this;
	}

	/**+
	 * Creates a textured box. Args: originX, originY, originZ,
	 * width, height, depth, scaleFactor.
	 */
	public ModelRenderer addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth) {
		partName = this.boxName + "." + partName;
		TextureOffset textureoffset = this.baseModel.getTextureOffset(partName);
		this.setTextureOffset(textureoffset.textureOffsetX, textureoffset.textureOffsetY);
		this.cubeList.add((new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height,
				depth, 0.0F)).setBoxName(partName));
		return this;
	}

	/**+
	 * Creates a textured box. Args: originX, originY, originZ,
	 * width, height, depth, scaleFactor.
	 */
	public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth) {
		this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height,
				depth, 0.0F));
		return this;
	}

	/**+
	 * Creates a textured box. Args: originX, originY, originZ,
	 * width, height, depth, scaleFactor.
	 */
	public ModelRenderer addBox(float parFloat1, float parFloat2, float parFloat3, int parInt1, int parInt2,
			int parInt3, boolean parFlag) {
		this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, parFloat1, parFloat2, parFloat3,
				parInt1, parInt2, parInt3, 0.0F, parFlag));
		return this;
	}

	/**+
	 * Creates a textured box. Args: originX, originY, originZ,
	 * width, height, depth, scaleFactor.
	 */
	public void addBox(float width, float height, float depth, int scaleFactor, int parInt2, int parInt3,
			float parFloat4) {
		this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, width, height, depth,
				scaleFactor, parInt2, parInt3, parFloat4));
	}

	public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
		this.rotationPointX = rotationPointXIn;
		this.rotationPointY = rotationPointYIn;
		this.rotationPointZ = rotationPointZIn;
	}

	public void render(float parFloat1) {
		if (!this.isHidden) {
			if (this.showModel) {
				if (!this.compiled) {
					this.compileDisplayList(parFloat1);
				}

				GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
				if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
					if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
						GlStateManager.callList(this.displayList);
						if (this.childModels != null) {
							for (int k = 0; k < this.childModels.size(); ++k) {
								((ModelRenderer) this.childModels.get(k)).render(parFloat1);
							}
						}
					} else {
						GlStateManager.translate(this.rotationPointX * parFloat1, this.rotationPointY * parFloat1,
								this.rotationPointZ * parFloat1);
						GlStateManager.callList(this.displayList);
						if (this.childModels != null) {
							for (int j = 0; j < this.childModels.size(); ++j) {
								((ModelRenderer) this.childModels.get(j)).render(parFloat1);
							}
						}

						GlStateManager.translate(-this.rotationPointX * parFloat1, -this.rotationPointY * parFloat1,
								-this.rotationPointZ * parFloat1);
					}
				} else {
					GlStateManager.pushMatrix();
					GlStateManager.translate(this.rotationPointX * parFloat1, this.rotationPointY * parFloat1,
							this.rotationPointZ * parFloat1);
					GlStateManager.rotateZYXRad(this.rotateAngleX, this.rotateAngleY, this.rotateAngleZ);

					GlStateManager.callList(this.displayList);
					if (this.childModels != null) {
						for (int i = 0; i < this.childModels.size(); ++i) {
							((ModelRenderer) this.childModels.get(i)).render(parFloat1);
						}
					}

					GlStateManager.popMatrix();
				}

				GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
			}
		}
	}

	public void renderWithRotation(float parFloat1) {
		if (!this.isHidden) {
			if (this.showModel) {
				if (!this.compiled) {
					this.compileDisplayList(parFloat1);
				}

				GlStateManager.pushMatrix();
				GlStateManager.translate(this.rotationPointX * parFloat1, this.rotationPointY * parFloat1,
						this.rotationPointZ * parFloat1);

				// note: vanilla order for this transformation was YXZ not ZYX for some reason
				GlStateManager.rotateZYXRad(this.rotateAngleX, this.rotateAngleY, this.rotateAngleZ);

				GlStateManager.callList(this.displayList);
				GlStateManager.popMatrix();
			}
		}
	}

	/**+
	 * Allows the changing of Angles after a box has been rendered
	 */
	public void postRender(float scale) {
		if (!this.isHidden) {
			if (this.showModel) {
				if (!this.compiled) {
					this.compileDisplayList(scale);
				}

				if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
					if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
						GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale,
								this.rotationPointZ * scale);
					}
				} else {
					GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale,
							this.rotationPointZ * scale);
					GlStateManager.rotateZYXRad(this.rotateAngleX, this.rotateAngleY, this.rotateAngleZ);
				}

			}
		}
	}

	/**+
	 * Compiles a GL display list for this model
	 */
	private void compileDisplayList(float scale) {
		this.displayList = GLAllocation.generateDisplayLists();
		EaglercraftGPU.glNewList(this.displayList, GL_COMPILE);
		WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();

		for (int i = 0; i < this.cubeList.size(); ++i) {
			((ModelBox) this.cubeList.get(i)).render(worldrenderer, scale);
		}

		EaglercraftGPU.glEndList();
		this.compiled = true;
	}

	/**+
	 * Returns the model renderer with the new texture parameters.
	 */
	public ModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn) {
		this.textureWidth = (float) textureWidthIn;
		this.textureHeight = (float) textureHeightIn;
		return this;
	}
}