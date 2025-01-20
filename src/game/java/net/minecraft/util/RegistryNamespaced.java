package net.minecraft.util;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

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
public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V> {
	/**+
	 * The backing store that maps Integers to objects.
	 */
	protected final ObjectIntIdentityMap<V> underlyingIntegerMap = new ObjectIntIdentityMap();
	protected final Map<V, K> inverseObjectRegistry;

	public RegistryNamespaced() {
		this.inverseObjectRegistry = ((BiMap) this.registryObjects).inverse();
	}

	public void register(int i, K object, V object1) {
		this.underlyingIntegerMap.put(object1, i);
		this.putObject(object, object1);
	}

	protected Map<K, V> createUnderlyingMap() {
		return HashBiMap.create();
	}

	public V getObject(K object) {
		return super.getObject(object);
	}

	/**+
	 * Gets the name we use to identify the given object.
	 */
	public K getNameForObject(V parObject) {
		return (K) this.inverseObjectRegistry.get(parObject);
	}

	/**+
	 * Does this registry contain an entry for the given key?
	 */
	public boolean containsKey(K parObject) {
		return super.containsKey(parObject);
	}

	/**+
	 * Gets the integer ID we use to identify the given object.
	 */
	public int getIDForObject(V parObject) {
		return this.underlyingIntegerMap.get(parObject);
	}

	/**+
	 * Gets the object identified by the given ID.
	 */
	public V getObjectById(int i) {
		return (V) this.underlyingIntegerMap.getByValue(i);
	}

	public Iterator<V> iterator() {
		return this.underlyingIntegerMap.iterator();
	}
}