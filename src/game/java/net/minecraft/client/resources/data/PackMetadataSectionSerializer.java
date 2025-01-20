package net.minecraft.client.resources.data;

import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeSerializer;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
import net.minecraft.util.IChatComponent;

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
public class PackMetadataSectionSerializer extends BaseMetadataSectionSerializer<PackMetadataSection>
		implements JSONTypeSerializer<PackMetadataSection, JSONObject> {
	public PackMetadataSection deserialize(JSONObject jsonobject) throws JSONException {
		IChatComponent ichatcomponent = JSONTypeProvider.deserialize(jsonobject.get("description"),
				IChatComponent.class);
		if (ichatcomponent == null) {
			throw new JSONException("Invalid/missing description!");
		} else {
			int i = jsonobject.getInt("pack_format");
			return new PackMetadataSection(ichatcomponent, i);
		}
	}

	public JSONObject serialize(PackMetadataSection packmetadatasection) {
		JSONObject jsonobject = new JSONObject();
		jsonobject.put("pack_format", packmetadatasection.getPackFormat());
		jsonobject.put("description",
				(JSONObject) JSONTypeProvider.serialize(packmetadatasection.getPackDescription()));
		return jsonobject;
	}

	/**+
	 * The name of this section type as it appears in JSON.
	 */
	public String getSectionName() {
		return "pack";
	}
}