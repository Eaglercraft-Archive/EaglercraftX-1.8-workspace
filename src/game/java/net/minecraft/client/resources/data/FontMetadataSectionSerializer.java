package net.minecraft.client.resources.data;

import org.apache.commons.lang3.Validate;
import org.json.JSONException;
import org.json.JSONObject;

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
public class FontMetadataSectionSerializer extends BaseMetadataSectionSerializer<FontMetadataSection> {
	public FontMetadataSection deserialize(JSONObject jsonobject) throws JSONException {
		float[] afloat = new float[256];
		float[] afloat1 = new float[256];
		float[] afloat2 = new float[256];
		float f = 1.0F;
		float f1 = 0.0F;
		float f2 = 0.0F;
		if (jsonobject.has("characters")) {
			if (!(jsonobject.get("characters") instanceof JSONObject)) {
				throw new JSONException(
						"Invalid font->characters: expected object, was " + jsonobject.get("characters"));
			}

			JSONObject jsonobject1 = jsonobject.getJSONObject("characters");
			if (jsonobject1.has("default")) {
				if (!(jsonobject1.get("default") instanceof JSONObject)) {
					throw new JSONException(
							"Invalid font->characters->default: expected object, was " + jsonobject1.get("default"));
				}

				JSONObject jsonobject2 = jsonobject1.getJSONObject("default");
				f = jsonobject2.optFloat("width", f);
				Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, (double) f, "Invalid default width");
				f1 = jsonobject2.optFloat("spacing", f1);
				Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, (double) f1, "Invalid default spacing");
				f2 = jsonobject2.optFloat("left", f1);
				Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, (double) f2, "Invalid default left");
			}

			for (int i = 0; i < 256; ++i) {
				JSONObject jsonobject3 = jsonobject1.optJSONObject(Integer.toString(i));
				float f3 = f;
				float f4 = f1;
				float f5 = f2;
				if (jsonobject3 != null) {
					f3 = jsonobject3.optFloat("width", f);
					Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, (double) f3, "Invalid width");
					f4 = jsonobject3.optFloat("spacing", f1);
					Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, (double) f4, "Invalid spacing");
					f5 = jsonobject3.optFloat("left", f2);
					Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, (double) f5, "Invalid left");
				}

				afloat[i] = f3;
				afloat1[i] = f4;
				afloat2[i] = f5;
			}
		}

		return new FontMetadataSection(afloat, afloat2, afloat1);
	}

	/**+
	 * The name of this section type as it appears in JSON.
	 */
	public String getSectionName() {
		return "font";
	}
}