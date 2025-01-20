package net.minecraft.client.resources.model;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

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
public class WeightedBakedModel implements IBakedModel {
	private final int totalWeight;
	private final List<WeightedBakedModel.MyWeighedRandomItem> models;
	private final IBakedModel baseModel;

	public WeightedBakedModel(List<WeightedBakedModel.MyWeighedRandomItem> parList) {
		this.models = parList;
		this.totalWeight = WeightedRandom.getTotalWeight(parList);
		this.baseModel = ((WeightedBakedModel.MyWeighedRandomItem) parList.get(0)).model;
	}

	public List<BakedQuad> getFaceQuads(EnumFacing enumfacing) {
		return this.baseModel.getFaceQuads(enumfacing);
	}

	public List<BakedQuad> getGeneralQuads() {
		return this.baseModel.getGeneralQuads();
	}

	public boolean isAmbientOcclusion() {
		return this.baseModel.isAmbientOcclusion();
	}

	public boolean isGui3d() {
		return this.baseModel.isGui3d();
	}

	public boolean isBuiltInRenderer() {
		return this.baseModel.isBuiltInRenderer();
	}

	public EaglerTextureAtlasSprite getParticleTexture() {
		return this.baseModel.getParticleTexture();
	}

	public ItemCameraTransforms getItemCameraTransforms() {
		return this.baseModel.getItemCameraTransforms();
	}

	public IBakedModel getAlternativeModel(long parLong1) {
		return ((WeightedBakedModel.MyWeighedRandomItem) WeightedRandom.getRandomItem(this.models,
				Math.abs((int) parLong1 >> 16) % this.totalWeight)).model;
	}

	public static class Builder {
		private List<WeightedBakedModel.MyWeighedRandomItem> listItems = Lists.newArrayList();

		public WeightedBakedModel.Builder add(IBakedModel parIBakedModel, int parInt1) {
			this.listItems.add(new WeightedBakedModel.MyWeighedRandomItem(parIBakedModel, parInt1));
			return this;
		}

		public WeightedBakedModel build() {
			Collections.sort(this.listItems);
			return new WeightedBakedModel(this.listItems);
		}

		public IBakedModel first() {
			return ((WeightedBakedModel.MyWeighedRandomItem) this.listItems.get(0)).model;
		}
	}

	static class MyWeighedRandomItem extends WeightedRandom.Item
			implements Comparable<WeightedBakedModel.MyWeighedRandomItem> {
		protected final IBakedModel model;

		public MyWeighedRandomItem(IBakedModel parIBakedModel, int parInt1) {
			super(parInt1);
			this.model = parIBakedModel;
		}

		public int compareTo(WeightedBakedModel.MyWeighedRandomItem weightedbakedmodel$myweighedrandomitem) {
			return ComparisonChain.start().compare(weightedbakedmodel$myweighedrandomitem.itemWeight, this.itemWeight)
					.compare(this.getCountQuads(), weightedbakedmodel$myweighedrandomitem.getCountQuads()).result();
		}

		protected int getCountQuads() {
			int i = this.model.getGeneralQuads().size();

			EnumFacing[] facings = EnumFacing._VALUES;
			for (int j = 0; j < facings.length; ++j) {
				i += this.model.getFaceQuads(facings[j]).size();
			}

			return i;
		}

		public String toString() {
			return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
		}
	}
}