package net.minecraft.client.resources.data;

import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Sets;

import net.minecraft.client.resources.Language;

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
public class LanguageMetadataSectionSerializer extends BaseMetadataSectionSerializer<LanguageMetadataSection> {
	public LanguageMetadataSection deserialize(JSONObject jsonobject) throws JSONException {
		HashSet hashset = Sets.newHashSet();

		for (String s : jsonobject.keySet()) {
			JSONObject jsonobject1 = jsonobject.getJSONObject(s);
			String s1 = jsonobject1.getString("region");
			String s2 = jsonobject1.getString("name");
			boolean flag = jsonobject1.optBoolean("bidirectional", false);
			if (s1.isEmpty()) {
				throw new JSONException("Invalid language->\'" + s + "\'->region: empty value");
			}

			if (s2.isEmpty()) {
				throw new JSONException("Invalid language->\'" + s + "\'->name: empty value");
			}

			if (!hashset.add(new Language(s, s1, s2, flag))) {
				throw new JSONException("Duplicate language->\'" + s + "\' defined");
			}
		}

		return new LanguageMetadataSection(hashset);
	}

	/**+
	 * The name of this section type as it appears in JSON.
	 */
	public String getSectionName() {
		return "language";
	}
}