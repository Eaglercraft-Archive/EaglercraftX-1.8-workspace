package net.minecraft.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class RegistrySimple<K, V> implements IRegistry<K, V> {
	private static final Logger logger = LogManager.getLogger();
	protected final Map<K, V> registryObjects = this.createUnderlyingMap();

	protected Map<K, V> createUnderlyingMap() {
		return Maps.newHashMap();
	}

	public V getObject(K object) {
		return (V) this.registryObjects.get(object);
	}

	/**+
	 * Register an object on this registry.
	 */
	public void putObject(K object, V object1) {
		Validate.notNull(object);
		Validate.notNull(object1);
		if (this.registryObjects.containsKey(object)) {
			logger.debug("Adding duplicate key \'" + object + "\' to registry");
		}

		this.registryObjects.put(object, object1);
	}

	/**+
	 * Gets all the keys recognized by this registry.
	 */
	public Set<K> getKeys() {
		return Collections.unmodifiableSet(this.registryObjects.keySet());
	}

	/**+
	 * Does this registry contain an entry for the given key?
	 */
	public boolean containsKey(K object) {
		return this.registryObjects.containsKey(object);
	}

	public Iterator<V> iterator() {
		return this.registryObjects.values().iterator();
	}
}