package net.minecraft.util;

import java.util.Iterator;
import java.util.List;

import com.carrotsearch.hppc.ObjectIntIdentityHashMap;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

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
public class ObjectIntIdentityMap<T> implements IObjectIntIterable<T> {
	private final ObjectIntIdentityHashMap<T> identityMap = new ObjectIntIdentityHashMap<>(512);
	private final List<T> objectList = Lists.newArrayList();

	public void put(T key, int value) {
		this.identityMap.put(key, value);

		while (this.objectList.size() <= value) {
			this.objectList.add((T) null);
		}

		this.objectList.set(value, key);
	}

	public int get(T key) {
		return this.identityMap.getOrDefault(key, -1);
	}

	public final T getByValue(int value) {
		return (T) (value >= 0 && value < this.objectList.size() ? this.objectList.get(value) : null);
	}

	public Iterator<T> iterator() {
		return Iterators.filter(this.objectList.iterator(), Predicates.notNull());
	}
}