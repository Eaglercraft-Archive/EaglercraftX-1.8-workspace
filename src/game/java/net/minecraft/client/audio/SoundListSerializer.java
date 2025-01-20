package net.minecraft.client.audio;

import org.apache.commons.lang3.Validate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;

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
public class SoundListSerializer implements JSONTypeDeserializer<JSONObject, SoundList> {
	public SoundList deserialize(JSONObject jsonobject) throws JSONException {
		SoundList soundlist = new SoundList();
		soundlist.setReplaceExisting(jsonobject.optBoolean("replace", false));
		SoundCategory soundcategory = SoundCategory
				.getCategory(jsonobject.optString("category", SoundCategory.MASTER.getCategoryName()));
		soundlist.setSoundCategory(soundcategory);
		Validate.notNull(soundcategory, "Invalid category", new Object[0]);
		if (jsonobject.has("sounds")) {
			JSONArray jsonarray = jsonobject.getJSONArray("sounds");

			for (int i = 0; i < jsonarray.length(); ++i) {
				Object jsonelement = jsonarray.get(i);
				SoundList.SoundEntry soundlist$soundentry = new SoundList.SoundEntry();
				if (jsonelement instanceof String) {
					soundlist$soundentry.setSoundEntryName((String) jsonelement);
				} else if (jsonelement instanceof JSONObject) {
					JSONObject jsonobject1 = (JSONObject) jsonelement;
					soundlist$soundentry.setSoundEntryName(jsonobject1.getString("name"));
					if (jsonobject1.has("type")) {
						SoundList.SoundEntry.Type soundlist$soundentry$type = SoundList.SoundEntry.Type
								.getType(jsonobject1.getString("type"));
						Validate.notNull(soundlist$soundentry$type, "Invalid type", new Object[0]);
						soundlist$soundentry.setSoundEntryType(soundlist$soundentry$type);
					}

					if (jsonobject1.has("volume")) {
						float f = jsonobject1.getFloat("volume");
						Validate.isTrue(f > 0.0F, "Invalid volume", new Object[0]);
						soundlist$soundentry.setSoundEntryVolume(f);
					}

					if (jsonobject1.has("pitch")) {
						float f1 = jsonobject1.getFloat("pitch");
						Validate.isTrue(f1 > 0.0F, "Invalid pitch", new Object[0]);
						soundlist$soundentry.setSoundEntryPitch(f1);
					}

					if (jsonobject1.has("weight")) {
						int j = jsonobject1.getInt("weight");
						Validate.isTrue(j > 0, "Invalid weight", new Object[0]);
						soundlist$soundentry.setSoundEntryWeight(j);
					}

					if (jsonobject1.has("stream")) {
						soundlist$soundentry.setStreaming(jsonobject1.getBoolean("stream"));
					}
				}

				soundlist.getSoundList().add(soundlist$soundentry);
			}
		}

		return soundlist;
	}
}