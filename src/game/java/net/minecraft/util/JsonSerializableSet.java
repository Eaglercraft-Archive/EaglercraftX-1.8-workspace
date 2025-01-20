package net.minecraft.util;

import java.util.Set;

import org.json.JSONArray;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;

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
public class JsonSerializableSet extends ForwardingSet<String> implements IJsonSerializable {
	/**+
	 * The set for this ForwardingSet to forward methods to.
	 */
	private final Set<String> underlyingSet = Sets.newHashSet();

	public void fromJson(Object jsonelement) {
		if (jsonelement instanceof JSONArray) {
			JSONArray arr = (JSONArray) jsonelement;
			for (int i = 0; i < arr.length(); ++i) {
				underlyingSet.add(arr.getString(i));
			}
		}

	}

	/**+
	 * Gets the JsonElement that can be serialized.
	 */
	public Object getSerializableElement() {
		JSONArray jsonarray = new JSONArray();

		for (String s : this) {
			jsonarray.put(s);
		}

		return jsonarray;
	}

	protected Set<String> delegate() {
		return this.underlyingSet;
	}
}