package net.minecraft.client.renderer.block.model;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
import net.minecraft.client.resources.model.ModelRotation;
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
public class ModelBlockDefinition {

	private final Map<String, ModelBlockDefinition.Variants> mapVariants = Maps.newHashMap();

	public static ModelBlockDefinition parseFromReader(Reader parReader) {
		return (ModelBlockDefinition) JSONTypeProvider.deserialize(parReader, ModelBlockDefinition.class);
	}

	public ModelBlockDefinition(Collection<ModelBlockDefinition.Variants> parCollection) {
		for (ModelBlockDefinition.Variants modelblockdefinition$variants : parCollection) {
			this.mapVariants.put(modelblockdefinition$variants.name, modelblockdefinition$variants);
		}

	}

	public ModelBlockDefinition(List<ModelBlockDefinition> parList) {
		for (int i = 0, l = parList.size(); i < l; ++i) {
			this.mapVariants.putAll(parList.get(i).mapVariants);
		}

	}

	public ModelBlockDefinition.Variants getVariants(String parString1) {
		ModelBlockDefinition.Variants modelblockdefinition$variants = (ModelBlockDefinition.Variants) this.mapVariants
				.get(parString1);
		if (modelblockdefinition$variants == null) {
			throw new ModelBlockDefinition.MissingVariantException();
		} else {
			return modelblockdefinition$variants;
		}
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof ModelBlockDefinition) {
			ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition) object;
			return this.mapVariants.equals(modelblockdefinition.mapVariants);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.mapVariants.hashCode();
	}

	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlockDefinition> {
		public ModelBlockDefinition deserialize(JSONObject jsonobject) throws JSONException {
			List list = this.parseVariantsList(jsonobject);
			return new ModelBlockDefinition((Collection<ModelBlockDefinition.Variants>) list);
		}

		protected List<ModelBlockDefinition.Variants> parseVariantsList(JSONObject parJsonObject) {
			JSONObject jsonobject = parJsonObject.getJSONObject("variants");
			ArrayList arraylist = Lists.newArrayList();

			for (String entry : jsonobject.keySet()) {
				arraylist.add(this.parseVariants(entry, jsonobject.get(entry)));
			}

			return arraylist;
		}

		protected ModelBlockDefinition.Variants parseVariants(String s, Object jsonelement) {
			ArrayList arraylist = Lists.newArrayList();
			if (jsonelement instanceof JSONArray) {
				for (Object jsonelement1 : (JSONArray) jsonelement) {
					arraylist.add(JSONTypeProvider.deserialize(jsonelement1, ModelBlockDefinition.Variant.class));
				}
			} else {
				arraylist.add(JSONTypeProvider.deserialize(jsonelement, ModelBlockDefinition.Variant.class));
			}

			return new ModelBlockDefinition.Variants(s, arraylist);
		}
	}

	public class MissingVariantException extends RuntimeException {
	}

	public static class Variant {
		private final ResourceLocation modelLocation;
		private final ModelRotation modelRotation;
		private final boolean uvLock;
		private final int weight;

		public Variant(ResourceLocation modelLocationIn, ModelRotation modelRotationIn, boolean uvLockIn,
				int weightIn) {
			this.modelLocation = modelLocationIn;
			this.modelRotation = modelRotationIn;
			this.uvLock = uvLockIn;
			this.weight = weightIn;
		}

		public ResourceLocation getModelLocation() {
			return this.modelLocation;
		}

		public ModelRotation getRotation() {
			return this.modelRotation;
		}

		public boolean isUvLocked() {
			return this.uvLock;
		}

		public int getWeight() {
			return this.weight;
		}

		public boolean equals(Object object) {
			if (this == object) {
				return true;
			} else if (!(object instanceof ModelBlockDefinition.Variant)) {
				return false;
			} else {
				ModelBlockDefinition.Variant modelblockdefinition$variant = (ModelBlockDefinition.Variant) object;
				return this.modelLocation.equals(modelblockdefinition$variant.modelLocation)
						&& this.modelRotation == modelblockdefinition$variant.modelRotation
						&& this.uvLock == modelblockdefinition$variant.uvLock;
			}
		}

		public int hashCode() {
			int i = this.modelLocation.hashCode();
			i = 31 * i + (this.modelRotation != null ? this.modelRotation.hashCode() : 0);
			i = 31 * i + (this.uvLock ? 1 : 0);
			return i;
		}

		public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlockDefinition.Variant> {
			public ModelBlockDefinition.Variant deserialize(JSONObject jsonobject) throws JSONException {
				String s = this.parseModel(jsonobject);
				ModelRotation modelrotation = this.parseRotation(jsonobject);
				boolean flag = this.parseUvLock(jsonobject);
				int i = this.parseWeight(jsonobject);
				return new ModelBlockDefinition.Variant(this.makeModelLocation(s), modelrotation, flag, i);
			}

			private ResourceLocation makeModelLocation(String parString1) {
				ResourceLocation resourcelocation = new ResourceLocation(parString1);
				resourcelocation = new ResourceLocation(resourcelocation.getResourceDomain(),
						"block/" + resourcelocation.getResourcePath());
				return resourcelocation;
			}

			private boolean parseUvLock(JSONObject parJsonObject) {
				return parJsonObject.optBoolean("uvlock", false);
			}

			protected ModelRotation parseRotation(JSONObject parJsonObject) {
				int i = parJsonObject.optInt("x", 0);
				int j = parJsonObject.optInt("y", 0);
				ModelRotation modelrotation = ModelRotation.getModelRotation(i, j);
				if (modelrotation == null) {
					throw new JSONException("Invalid BlockModelRotation x: " + i + ", y: " + j);
				} else {
					return modelrotation;
				}
			}

			protected String parseModel(JSONObject parJsonObject) {
				return parJsonObject.getString("model");
			}

			protected int parseWeight(JSONObject parJsonObject) {
				return parJsonObject.optInt("weight", 1);
			}
		}
	}

	public static class Variants {
		private final String name;
		private final List<ModelBlockDefinition.Variant> listVariants;

		public Variants(String nameIn, List<ModelBlockDefinition.Variant> listVariantsIn) {
			this.name = nameIn;
			this.listVariants = listVariantsIn;
		}

		public List<ModelBlockDefinition.Variant> getVariants() {
			return this.listVariants;
		}

		public boolean equals(Object object) {
			if (this == object) {
				return true;
			} else if (!(object instanceof ModelBlockDefinition.Variants)) {
				return false;
			} else {
				ModelBlockDefinition.Variants modelblockdefinition$variants = (ModelBlockDefinition.Variants) object;
				return !this.name.equals(modelblockdefinition$variants.name) ? false
						: this.listVariants.equals(modelblockdefinition$variants.listVariants);
			}
		}

		public int hashCode() {
			int i = this.name.hashCode();
			i = 31 * i + this.listVariants.hashCode();
			return i;
		}
	}
}