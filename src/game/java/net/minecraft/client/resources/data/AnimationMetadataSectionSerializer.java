package net.minecraft.client.resources.data;

import java.util.ArrayList;

import org.apache.commons.lang3.Validate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeSerializer;

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
public class AnimationMetadataSectionSerializer extends BaseMetadataSectionSerializer<AnimationMetadataSection>
		implements JSONTypeSerializer<AnimationMetadataSection, JSONObject> {
	public AnimationMetadataSection deserialize(JSONObject jsonobject) throws JSONException {
		ArrayList arraylist = Lists.newArrayList();
		int i = jsonobject.optInt("frametime", 1);
		if (i != 1) {
			Validate.inclusiveBetween(1L, 2147483647L, (long) i, "Invalid default frame time");
		}

		if (jsonobject.has("frames")) {
			try {
				JSONArray jsonarray = jsonobject.getJSONArray("frames");

				for (int j = 0; j < jsonarray.length(); ++j) {
					AnimationFrame animationframe = this.parseAnimationFrame(j, jsonarray.get(j));
					if (animationframe != null) {
						arraylist.add(animationframe);
					}
				}
			} catch (ClassCastException classcastexception) {
				throw new JSONException("Invalid animation->frames: expected array, was " + jsonobject.get("frames"),
						classcastexception);
			}
		}

		int k = jsonobject.optInt("width", -1);
		int l = jsonobject.optInt("height", -1);
		if (k != -1) {
			Validate.inclusiveBetween(1L, 2147483647L, (long) k, "Invalid width");
		}

		if (l != -1) {
			Validate.inclusiveBetween(1L, 2147483647L, (long) l, "Invalid height");
		}

		boolean flag = jsonobject.optBoolean("interpolate", false);
		return new AnimationMetadataSection(arraylist, k, l, i, flag);
	}

	private AnimationFrame parseAnimationFrame(int parInt1, Object parJsonElement) {
		if (parJsonElement instanceof Number) {
			return new AnimationFrame(((Number) parJsonElement).intValue());
		} else if (parJsonElement instanceof JSONObject) {
			JSONObject jsonobject = (JSONObject) parJsonElement;
			int i = jsonobject.optInt("time", -1);
			if (jsonobject.has("time")) {
				Validate.inclusiveBetween(1L, 2147483647L, (long) i, "Invalid frame time");
			}

			int j = jsonobject.getInt(getSectionName());
			Validate.inclusiveBetween(0L, 2147483647L, (long) j, "Invalid frame index");
			return new AnimationFrame(j, i);
		} else {
			return null;
		}
	}

	public JSONObject serialize(AnimationMetadataSection animationmetadatasection) {
		JSONObject jsonobject = new JSONObject();
		jsonobject.put("frametime", Integer.valueOf(animationmetadatasection.getFrameTime()));
		if (animationmetadatasection.getFrameWidth() != -1) {
			jsonobject.put("width", Integer.valueOf(animationmetadatasection.getFrameWidth()));
		}

		if (animationmetadatasection.getFrameHeight() != -1) {
			jsonobject.put("height", Integer.valueOf(animationmetadatasection.getFrameHeight()));
		}

		if (animationmetadatasection.getFrameCount() > 0) {
			JSONArray jsonarray = new JSONArray();

			for (int i = 0; i < animationmetadatasection.getFrameCount(); ++i) {
				if (animationmetadatasection.frameHasTime(i)) {
					JSONObject jsonobject1 = new JSONObject();
					jsonobject1.put("index", Integer.valueOf(animationmetadatasection.getFrameIndex(i)));
					jsonobject1.put("time", Integer.valueOf(animationmetadatasection.getFrameTimeSingle(i)));
					jsonarray.put(jsonobject1);
				} else {
					jsonarray.put(Integer.valueOf(animationmetadatasection.getFrameIndex(i)));
				}
			}

			jsonobject.put("frames", jsonarray);
		}

		return jsonobject;
	}

	/**+
	 * The name of this section type as it appears in JSON.
	 */
	public String getSectionName() {
		return "animation";
	}
}