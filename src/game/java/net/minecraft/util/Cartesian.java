package net.minecraft.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

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
public class Cartesian {
	public static <T> Iterable<T[]> cartesianProduct(Class<T> clazz, Iterable<? extends Iterable<? extends T>> sets) {
		return new Cartesian.Product(clazz, (Iterable[]) toArray(Iterable.class, sets));
	}

	public static <T> Iterable<List<T>> cartesianProduct(Iterable<? extends Iterable<? extends T>> sets) {
		/**+
		 * Convert an Iterable of Arrays (Object[]) to an Iterable of
		 * Lists
		 */
		return arraysAsLists(cartesianProduct(Object.class, sets));
	}

	private static <T> Iterable<List<T>> arraysAsLists(Iterable<Object[]> arrays) {
		return Iterables.transform(arrays, new Cartesian.GetList());
	}

	private static <T> T[] toArray(Class<? super T> clazz, Iterable<? extends T> it) {
		ArrayList arraylist = Lists.newArrayList();

		for (Object object : it) {
			arraylist.add(object);
		}

		return (T[]) ((Object[]) arraylist.toArray(createArray(clazz, arraylist.size())));
	}

	private static <T> T[] createArray(Class<? super T> parClass1, int parInt1) {
		return (T[]) ((Object[]) ((Object[]) Array.newInstance(parClass1, parInt1)));
	}

	static class GetList<T> implements Function<Object[], List<T>> {
		private GetList() {
		}

		public List<T> apply(Object[] aobject) {
			return (List<T>) Arrays.asList((Object[]) aobject);
		}
	}

	static class Product<T> implements Iterable<T[]> {
		private final Class<T> clazz;
		private final Iterable<? extends T>[] iterables;

		private Product(Class<T> clazz, Iterable<? extends T>[] iterables) {
			this.clazz = clazz;
			this.iterables = iterables;
		}

		public Iterator<T[]> iterator() {
			return (Iterator<T[]>) (this.iterables.length <= 0
					? Collections.singletonList((T[]) Cartesian.createArray(this.clazz, 0)).iterator()
					: new Cartesian.Product.ProductIterator(this.clazz, this.iterables));
		}

		static class ProductIterator<T> extends UnmodifiableIterator<T[]> {
			private int index;
			private final Iterable<? extends T>[] iterables;
			private final Iterator<? extends T>[] iterators;
			private final T[] results;

			private ProductIterator(Class<T> clazz, Iterable<? extends T>[] iterables) {
				this.index = -2;
				this.iterables = iterables;
				this.iterators = (Iterator[]) Cartesian.createArray(Iterator.class, this.iterables.length);

				for (int i = 0; i < this.iterables.length; ++i) {
					this.iterators[i] = iterables[i].iterator();
				}

				this.results = Cartesian.createArray(clazz, this.iterators.length);
			}

			private void endOfData() {
				this.index = -1;
				Arrays.fill(this.iterators, (Object) null);
				Arrays.fill(this.results, (Object) null);
			}

			public boolean hasNext() {
				if (this.index == -2) {
					this.index = 0;

					for (Iterator iterator1 : this.iterators) {
						if (!iterator1.hasNext()) {
							this.endOfData();
							break;
						}
					}

					return true;
				} else {
					if (this.index >= this.iterators.length) {
						for (this.index = this.iterators.length - 1; this.index >= 0; --this.index) {
							Iterator iterator = this.iterators[this.index];
							if (iterator.hasNext()) {
								break;
							}

							if (this.index == 0) {
								this.endOfData();
								break;
							}

							iterator = this.iterables[this.index].iterator();
							this.iterators[this.index] = iterator;
							if (!iterator.hasNext()) {
								this.endOfData();
								break;
							}
						}
					}

					return this.index >= 0;
				}
			}

			public T[] next() {
				if (!this.hasNext()) {
					throw new NoSuchElementException();
				} else {
					while (this.index < this.iterators.length) {
						this.results[this.index] = this.iterators[this.index].next();
						++this.index;
					}

					return (T[]) ((Object[]) this.results.clone());
				}
			}
		}
	}
}