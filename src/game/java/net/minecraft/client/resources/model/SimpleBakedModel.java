package net.minecraft.client.resources.model;

import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.util.EnumFacing;

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
public class SimpleBakedModel implements IBakedModel {
	protected final List<BakedQuad> generalQuads;
	protected final List<List<BakedQuad>> faceQuads;
	protected final boolean ambientOcclusion;
	protected final boolean gui3d;
	protected final EaglerTextureAtlasSprite texture;
	protected final ItemCameraTransforms cameraTransforms;

	public SimpleBakedModel(List<BakedQuad> parList, List<List<BakedQuad>> parList2, boolean parFlag, boolean parFlag2,
			EaglerTextureAtlasSprite parTextureAtlasSprite, ItemCameraTransforms parItemCameraTransforms) {
		this.generalQuads = parList;
		this.faceQuads = parList2;
		this.ambientOcclusion = parFlag;
		this.gui3d = parFlag2;
		this.texture = parTextureAtlasSprite;
		this.cameraTransforms = parItemCameraTransforms;
	}

	public List<BakedQuad> getFaceQuads(EnumFacing enumfacing) {
		return (List) this.faceQuads.get(enumfacing.ordinal());
	}

	public List<BakedQuad> getGeneralQuads() {
		return this.generalQuads;
	}

	public boolean isAmbientOcclusion() {
		return this.ambientOcclusion;
	}

	public boolean isGui3d() {
		return this.gui3d;
	}

	public boolean isBuiltInRenderer() {
		return false;
	}

	public EaglerTextureAtlasSprite getParticleTexture() {
		return this.texture;
	}

	public ItemCameraTransforms getItemCameraTransforms() {
		return this.cameraTransforms;
	}

	public static class Builder {
		private final List<BakedQuad> builderGeneralQuads;
		private final List<List<BakedQuad>> builderFaceQuads;
		private final boolean builderAmbientOcclusion;
		private EaglerTextureAtlasSprite builderTexture;
		private boolean builderGui3d;
		private ItemCameraTransforms builderCameraTransforms;

		public Builder(ModelBlock parModelBlock) {
			this(parModelBlock.isAmbientOcclusion(), parModelBlock.isGui3d(), parModelBlock.func_181682_g());
		}

		public Builder(IBakedModel parIBakedModel, EaglerTextureAtlasSprite parTextureAtlasSprite) {
			this(parIBakedModel.isAmbientOcclusion(), parIBakedModel.isGui3d(),
					parIBakedModel.getItemCameraTransforms());
			this.builderTexture = parIBakedModel.getParticleTexture();

			EnumFacing[] facings = EnumFacing._VALUES;
			for (int i = 0; i < facings.length; ++i) {
				this.addFaceBreakingFours(parIBakedModel, parTextureAtlasSprite, facings[i]);
			}

			this.addGeneralBreakingFours(parIBakedModel, parTextureAtlasSprite);
		}

		private void addFaceBreakingFours(IBakedModel parIBakedModel, EaglerTextureAtlasSprite parTextureAtlasSprite,
				EnumFacing parEnumFacing) {
			List<BakedQuad> quads = parIBakedModel.getFaceQuads(parEnumFacing);
			for (int i = 0, l = quads.size(); i < l; ++i) {
				this.addFaceQuad(parEnumFacing, new BreakingFour(quads.get(i), parTextureAtlasSprite));
			}

		}

		private void addGeneralBreakingFours(IBakedModel parIBakedModel,
				EaglerTextureAtlasSprite parTextureAtlasSprite) {
			List<BakedQuad> quads = parIBakedModel.getGeneralQuads();
			for (int i = 0, l = quads.size(); i < l; ++i) {
				this.addGeneralQuad(new BreakingFour(quads.get(i), parTextureAtlasSprite));
			}

		}

		private Builder(boolean parFlag, boolean parFlag2, ItemCameraTransforms parItemCameraTransforms) {
			this.builderGeneralQuads = Lists.newArrayList();
			this.builderFaceQuads = Lists.newArrayListWithCapacity(6);

			for (int i = 0, l = EnumFacing._VALUES.length; i < l; ++i) {
				this.builderFaceQuads.add(Lists.newArrayList());
			}

			this.builderAmbientOcclusion = parFlag;
			this.builderGui3d = parFlag2;
			this.builderCameraTransforms = parItemCameraTransforms;
		}

		public SimpleBakedModel.Builder addFaceQuad(EnumFacing parEnumFacing, BakedQuad parBakedQuad) {
			((List) this.builderFaceQuads.get(parEnumFacing.ordinal())).add(parBakedQuad);
			return this;
		}

		public SimpleBakedModel.Builder addGeneralQuad(BakedQuad parBakedQuad) {
			this.builderGeneralQuads.add(parBakedQuad);
			return this;
		}

		public SimpleBakedModel.Builder setTexture(EaglerTextureAtlasSprite parTextureAtlasSprite) {
			this.builderTexture = parTextureAtlasSprite;
			return this;
		}

		public IBakedModel makeBakedModel() {
			if (this.builderTexture == null) {
				throw new RuntimeException("Missing particle!");
			} else {
				return new SimpleBakedModel(this.builderGeneralQuads, this.builderFaceQuads,
						this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture,
						this.builderCameraTransforms);
			}
		}
	}
}