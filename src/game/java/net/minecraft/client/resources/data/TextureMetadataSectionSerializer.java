package net.minecraft.client.resources.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.carrotsearch.hppc.IntArrayList;

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
public class TextureMetadataSectionSerializer extends BaseMetadataSectionSerializer<TextureMetadataSection> {
	public TextureMetadataSection deserialize(JSONObject jsonobject) throws JSONException {
		boolean flag = jsonobject.optBoolean("blur", false);
		boolean flag1 = jsonobject.optBoolean("clamp", false);
		IntArrayList arraylist = new IntArrayList();
		if (jsonobject.has("mipmaps")) {
			try {
				JSONArray jsonarray = jsonobject.getJSONArray("mipmaps");

				for (int i = 0; i < jsonarray.length(); ++i) {
					Object jsonelement = jsonarray.get(i);
					if (jsonelement instanceof Number) {
						try {
							arraylist.add(((Number) jsonelement).intValue());
						} catch (NumberFormatException numberformatexception) {
							throw new JSONException(
									"Invalid texture->mipmap->" + i + ": expected number, was " + jsonelement,
									numberformatexception);
						}
					} else if (jsonelement instanceof JSONObject) {
						throw new JSONException(
								"Invalid texture->mipmap->" + i + ": expected number, was " + jsonelement);
					}
				}
			} catch (ClassCastException classcastexception) {
				throw new JSONException("Invalid texture->mipmaps: expected array, was " + jsonobject.get("mipmaps"),
						classcastexception);
			}
		}

		return new TextureMetadataSection(flag, flag1, arraylist);
	}

	/**+
	 * The name of this section type as it appears in JSON.
	 */
	public String getSectionName() {
		return "texture";
	}
}