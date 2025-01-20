package net.minecraft.client.renderer.block.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
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
public class ModelBlock {
	private static final Logger LOGGER = LogManager.getLogger();

	private final List<BlockPart> elements;
	private final boolean gui3d;
	private final boolean ambientOcclusion;
	private ItemCameraTransforms cameraTransforms;
	public String name;
	protected final Map<String, String> textures;
	protected ModelBlock parent;
	protected ResourceLocation parentLocation;

	public static ModelBlock deserialize(String parString1) {
		return (ModelBlock) JSONTypeProvider.deserialize(new JSONObject(parString1), ModelBlock.class);
	}

	protected ModelBlock(List<BlockPart> parList, Map<String, String> parMap, boolean parFlag, boolean parFlag2,
			ItemCameraTransforms parItemCameraTransforms) {
		this((ResourceLocation) null, parList, parMap, parFlag, parFlag2, parItemCameraTransforms);
	}

	protected ModelBlock(ResourceLocation parResourceLocation, Map<String, String> parMap, boolean parFlag,
			boolean parFlag2, ItemCameraTransforms parItemCameraTransforms) {
		this(parResourceLocation, Collections.emptyList(), parMap, parFlag, parFlag2, parItemCameraTransforms);
	}

	private ModelBlock(ResourceLocation parentLocationIn, List<BlockPart> elementsIn, Map<String, String> texturesIn,
			boolean ambientOcclusionIn, boolean gui3dIn, ItemCameraTransforms cameraTransformsIn) {
		this.name = "";
		this.elements = elementsIn;
		this.ambientOcclusion = ambientOcclusionIn;
		this.gui3d = gui3dIn;
		this.textures = texturesIn;
		this.parentLocation = parentLocationIn;
		this.cameraTransforms = cameraTransformsIn;
	}

	public List<BlockPart> getElements() {
		return this.hasParent() ? this.parent.getElements() : this.elements;
	}

	private boolean hasParent() {
		return this.parent != null;
	}

	public boolean isAmbientOcclusion() {
		return this.hasParent() ? this.parent.isAmbientOcclusion() : this.ambientOcclusion;
	}

	public boolean isGui3d() {
		return this.gui3d;
	}

	public boolean isResolved() {
		return this.parentLocation == null || this.parent != null && this.parent.isResolved();
	}

	public void getParentFromMap(Map<ResourceLocation, ModelBlock> parMap) {
		if (this.parentLocation != null) {
			this.parent = (ModelBlock) parMap.get(this.parentLocation);
		}

	}

	public boolean isTexturePresent(String textureName) {
		return !"missingno".equals(this.resolveTextureName(textureName));
	}

	public String resolveTextureName(String textureName) {
		if (!this.startsWithHash(textureName)) {
			textureName = '#' + textureName;
		}

		return this.resolveTextureName(textureName, new ModelBlock.Bookkeep(this));
	}

	private String resolveTextureName(String textureName, ModelBlock.Bookkeep parBookkeep) {
		if (this.startsWithHash(textureName)) {
			if (this == parBookkeep.modelExt) {
				LOGGER.warn("Unable to resolve texture due to upward reference: " + textureName + " in " + this.name);
				return "missingno";
			} else {
				String s = (String) this.textures.get(textureName.substring(1));
				if (s == null && this.hasParent()) {
					s = this.parent.resolveTextureName(textureName, parBookkeep);
				}

				parBookkeep.modelExt = this;
				if (s != null && this.startsWithHash(s)) {
					s = parBookkeep.model.resolveTextureName(s, parBookkeep);
				}

				return s != null && !this.startsWithHash(s) ? s : "missingno";
			}
		} else {
			return textureName;
		}
	}

	private boolean startsWithHash(String hash) {
		return hash.charAt(0) == 35;
	}

	public ResourceLocation getParentLocation() {
		return this.parentLocation;
	}

	public ModelBlock getRootModel() {
		return this.hasParent() ? this.parent.getRootModel() : this;
	}

	public ItemCameraTransforms func_181682_g() {
		ItemTransformVec3f itemtransformvec3f = this.func_181681_a(ItemCameraTransforms.TransformType.THIRD_PERSON);
		ItemTransformVec3f itemtransformvec3f1 = this.func_181681_a(ItemCameraTransforms.TransformType.FIRST_PERSON);
		ItemTransformVec3f itemtransformvec3f2 = this.func_181681_a(ItemCameraTransforms.TransformType.HEAD);
		ItemTransformVec3f itemtransformvec3f3 = this.func_181681_a(ItemCameraTransforms.TransformType.GUI);
		ItemTransformVec3f itemtransformvec3f4 = this.func_181681_a(ItemCameraTransforms.TransformType.GROUND);
		ItemTransformVec3f itemtransformvec3f5 = this.func_181681_a(ItemCameraTransforms.TransformType.FIXED);
		return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2,
				itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5);
	}

	private ItemTransformVec3f func_181681_a(ItemCameraTransforms.TransformType parTransformType) {
		return this.parent != null && !this.cameraTransforms.func_181687_c(parTransformType)
				? this.parent.func_181681_a(parTransformType)
				: this.cameraTransforms.getTransform(parTransformType);
	}

	public static void checkModelHierarchy(Map<ResourceLocation, ModelBlock> parMap) {
		for (ModelBlock modelblock : parMap.values()) {
			try {
				ModelBlock modelblock1 = modelblock.parent;

				for (ModelBlock modelblock2 = modelblock1.parent; modelblock1 != modelblock2; modelblock2 = modelblock2.parent.parent) {
					modelblock1 = modelblock1.parent;
				}

				throw new ModelBlock.LoopException();
			} catch (ModelBlock.LoopException var5) {
				throw var5;
			} catch (Throwable var6) {
				;
			}
		}

	}

	static final class Bookkeep {
		public final ModelBlock model;
		public ModelBlock modelExt;

		private Bookkeep(ModelBlock parModelBlock) {
			this.model = parModelBlock;
		}
	}

	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ModelBlock> {
		public ModelBlock deserialize(JSONObject jsonobject) throws JSONException {
			List list = this.getModelElements(jsonobject);
			String s = this.getParent(jsonobject);
			boolean flag = StringUtils.isEmpty(s);
			boolean flag1 = list.isEmpty();
			if (flag1 && flag) {
				throw new JSONException("BlockModel requires either elements or parent, found neither");
			} else if (!flag && !flag1) {
				throw new JSONException("BlockModel requires either elements or parent, found both");
			} else {
				Map map = this.getTextures(jsonobject);
				boolean flag2 = this.getAmbientOcclusionEnabled(jsonobject);
				ItemCameraTransforms itemcameratransforms = ItemCameraTransforms.DEFAULT;
				if (jsonobject.has("display")) {
					JSONObject jsonobject1 = jsonobject.getJSONObject("display");
					itemcameratransforms = JSONTypeProvider.deserialize(jsonobject1, ItemCameraTransforms.class);
				}

				return flag1 ? new ModelBlock(new ResourceLocation(s), map, flag2, true, itemcameratransforms)
						: new ModelBlock(list, map, flag2, true, itemcameratransforms);
			}
		}

		private Map<String, String> getTextures(JSONObject parJsonObject) {
			HashMap hashmap = Maps.newHashMap();
			if (parJsonObject.has("textures")) {
				JSONObject jsonobject = parJsonObject.getJSONObject("textures");

				for (String entry : jsonobject.keySet()) {
					hashmap.put(entry, jsonobject.getString(entry));
				}
			}

			return hashmap;
		}

		private String getParent(JSONObject parJsonObject) {
			return parJsonObject.optString("parent", "");
		}

		protected boolean getAmbientOcclusionEnabled(JSONObject parJsonObject) {
			return parJsonObject.optBoolean("ambientocclusion", true);
		}

		protected List<BlockPart> getModelElements(JSONObject parJsonObject) {
			ArrayList arraylist = Lists.newArrayList();
			if (parJsonObject.has("elements")) {
				for (Object jsonelement : parJsonObject.getJSONArray("elements")) {
					arraylist.add((BlockPart) JSONTypeProvider.deserialize(jsonelement, BlockPart.class));
				}
			}

			return arraylist;
		}
	}

	public static class LoopException extends RuntimeException {
	}
}