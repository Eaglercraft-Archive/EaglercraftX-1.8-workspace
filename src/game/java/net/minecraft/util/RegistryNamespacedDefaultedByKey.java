package net.minecraft.util;

import org.apache.commons.lang3.Validate;

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
public class RegistryNamespacedDefaultedByKey<K, V> extends RegistryNamespaced<K, V> {
	private final K defaultValueKey;
	private V defaultValue;

	public RegistryNamespacedDefaultedByKey(K parObject) {
		this.defaultValueKey = parObject;
	}

	public void register(int id, K parObject, V parObject2) {
		if (this.defaultValueKey.equals(parObject)) {
			this.defaultValue = parObject2;
		}

		super.register(id, parObject, parObject2);
	}

	/**+
	 * validates that this registry's key is non-null
	 */
	public void validateKey() {
		Validate.notNull(this.defaultValueKey);
	}

	public V getObject(K name) {
		Object object = super.getObject(name);
		return (V) (object == null ? this.defaultValue : object);
	}

	/**+
	 * Gets the object identified by the given ID.
	 */
	public V getObjectById(int id) {
		Object object = super.getObjectById(id);
		return (V) (object == null ? this.defaultValue : object);
	}
}