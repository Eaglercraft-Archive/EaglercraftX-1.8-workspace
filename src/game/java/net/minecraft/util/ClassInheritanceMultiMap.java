package net.minecraft.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
public class ClassInheritanceMultiMap<T> extends AbstractSet<T> {
	private static final Set<Class<?>> field_181158_a = Sets.newHashSet();
	private final Map<Class<?>, List<T>> map = Maps.newHashMap();
	private final Set<Class<?>> knownKeys = Sets.newIdentityHashSet();
	private final Class<T> baseClass;
	private final List<T> field_181745_e = Lists.newArrayList();

	public ClassInheritanceMultiMap(Class<T> baseClassIn) {
		this.baseClass = baseClassIn;
		this.knownKeys.add(baseClassIn);
		this.map.put(baseClassIn, this.field_181745_e);

		for (Class oclass : field_181158_a) {
			this.createLookup(oclass);
		}

	}

	protected void createLookup(Class<?> clazz) {
		field_181158_a.add(clazz);

		for (int i = 0, l = this.field_181745_e.size(); i < l; ++i) {
			T object = this.field_181745_e.get(i);
			if (clazz.isAssignableFrom(object.getClass())) {
				this.func_181743_a(object, clazz);
			}
		}

		this.knownKeys.add(clazz);
	}

	protected Class<?> func_181157_b(Class<?> parClass1) {
		if (this.baseClass.isAssignableFrom(parClass1)) {
			if (!this.knownKeys.contains(parClass1)) {
				this.createLookup(parClass1);
			}

			return parClass1;
		} else {
			throw new IllegalArgumentException("Don\'t know how to search for " + parClass1);
		}
	}

	public boolean add(T parObject) {
		for (Class oclass : this.knownKeys) {
			if (oclass.isAssignableFrom(parObject.getClass())) {
				this.func_181743_a(parObject, oclass);
			}
		}

		return true;
	}

	private void func_181743_a(T parObject, Class<?> parClass1) {
		List list = (List) this.map.get(parClass1);
		if (list == null) {
			this.map.put(parClass1, (List<T>) Lists.newArrayList(new Object[] { parObject }));
		} else {
			list.add(parObject);
		}

	}

	public boolean remove(Object parObject) {
		Object object = parObject;
		boolean flag = false;

		for (Class oclass : this.knownKeys) {
			if (oclass.isAssignableFrom(object.getClass())) {
				List list = (List) this.map.get(oclass);
				if (list != null && list.remove(object)) {
					flag = true;
				}
			}
		}

		return flag;
	}

	public boolean contains(Object parObject) {
		return Iterators.contains(this.getByClass(parObject.getClass()).iterator(), parObject);
	}

	public <S> Iterable<S> getByClass(final Class<S> clazz) {
		return new Iterable<S>() {
			public Iterator<S> iterator() {
				List list = (List) ClassInheritanceMultiMap.this.map
						.get(ClassInheritanceMultiMap.this.func_181157_b(clazz));
				if (list == null) {
					return Iterators.emptyIterator();
				} else {
					Iterator iterator = list.iterator();
					return Iterators.filter(iterator, clazz);
				}
			}
		};
	}

	public Iterator<T> iterator() {
		return this.field_181745_e.isEmpty() ? Iterators.emptyIterator()
				: Iterators.unmodifiableIterator(this.field_181745_e.iterator());
	}

	public int size() {
		return this.field_181745_e.size();
	}
}