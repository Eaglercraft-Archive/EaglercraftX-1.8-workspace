package net.minecraft.server.management;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

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
public class LowerStringMap<V> implements Map<String, V> {
	private final Map<String, V> internalMap = Maps.newLinkedHashMap();

	public int size() {
		return this.internalMap.size();
	}

	public boolean isEmpty() {
		return this.internalMap.isEmpty();
	}

	public boolean containsKey(Object parObject) {
		return this.internalMap.containsKey(parObject.toString().toLowerCase());
	}

	public boolean containsValue(Object parObject) {
		return this.internalMap.containsKey(parObject);
	}

	public V get(Object parObject) {
		return (V) this.internalMap.get(parObject.toString().toLowerCase());
	}

	public V put(String parString1, V parObject) {
		return (V) this.internalMap.put(parString1.toLowerCase(), parObject);
	}

	public V remove(Object object) {
		return (V) this.internalMap.remove(object.toString().toLowerCase());
	}

	public void putAll(Map<? extends String, ? extends V> parMap) {
		for (Entry entry : parMap.entrySet()) {
			this.put((String) entry.getKey(), (V) entry.getValue());
		}

	}

	public void clear() {
		this.internalMap.clear();
	}

	public Set<String> keySet() {
		return this.internalMap.keySet();
	}

	public Collection<V> values() {
		return this.internalMap.values();
	}

	public Set<Entry<String, V>> entrySet() {
		return this.internalMap.entrySet();
	}
}