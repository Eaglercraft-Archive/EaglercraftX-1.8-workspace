package net.minecraft.util;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class LongHashMap<V> {
	/**+
	 * the array of all elements in the hash
	 */
	private transient LongHashMap.Entry<V>[] hashArray = new LongHashMap.Entry[4096];
	private transient int numHashElements;
	private int mask;
	/**+
	 * the maximum amount of elements in the hash (probably 3/4 the
	 * size due to meh hashing function)
	 */
	private int capacity = 3072;
	/**+
	 * percent of the hasharray that can be used without hash
	 * colliding probably
	 */
	private final float percentUseable = 0.75F;
	private transient volatile int modCount;

	public LongHashMap() {
		this.mask = this.hashArray.length - 1;
	}

	/**+
	 * returns the hashed key given the original key
	 */
	private static int getHashedKey(long originalKey) {
		/**+
		 * the hash function
		 */
		return hash((int) (originalKey ^ originalKey >>> 32));
	}

	/**+
	 * the hash function
	 */
	private static int hash(int integer) {
		integer = integer ^ integer >>> 20 ^ integer >>> 12;
		return integer ^ integer >>> 7 ^ integer >>> 4;
	}

	/**+
	 * gets the index in the hash given the array length and the
	 * hashed key
	 */
	private static int getHashIndex(int parInt1, int parInt2) {
		return parInt1 & parInt2;
	}

	public int getNumHashElements() {
		return this.numHashElements;
	}

	/**+
	 * get the value from the map given the key
	 */
	public V getValueByKey(long parLong1) {
		int i = getHashedKey(parLong1);

		for (LongHashMap.Entry longhashmap$entry = this.hashArray[getHashIndex(i,
				this.mask)]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.nextEntry) {
			if (longhashmap$entry.key == parLong1) {
				return (V) longhashmap$entry.value;
			}
		}

		return (V) null;
	}

	public boolean containsItem(long parLong1) {
		return this.getEntry(parLong1) != null;
	}

	final LongHashMap.Entry<V> getEntry(long parLong1) {
		int i = getHashedKey(parLong1);

		for (LongHashMap.Entry longhashmap$entry = this.hashArray[getHashIndex(i,
				this.mask)]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.nextEntry) {
			if (longhashmap$entry.key == parLong1) {
				return longhashmap$entry;
			}
		}

		return null;
	}

	/**+
	 * Add a key-value pair.
	 */
	public void add(long parLong1, V parObject) {
		int i = getHashedKey(parLong1);
		int j = getHashIndex(i, this.mask);

		for (LongHashMap.Entry longhashmap$entry = this.hashArray[j]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.nextEntry) {
			if (longhashmap$entry.key == parLong1) {
				longhashmap$entry.value = parObject;
				return;
			}
		}

		++this.modCount;
		this.createKey(i, parLong1, parObject, j);
	}

	/**+
	 * resizes the table
	 */
	private void resizeTable(int parInt1) {
		LongHashMap.Entry[] alonghashmap$entry = this.hashArray;
		int i = alonghashmap$entry.length;
		if (i == 1073741824) {
			this.capacity = Integer.MAX_VALUE;
		} else {
			LongHashMap.Entry[] alonghashmap$entry1 = new LongHashMap.Entry[parInt1];
			this.copyHashTableTo(alonghashmap$entry1);
			this.hashArray = alonghashmap$entry1;
			this.mask = this.hashArray.length - 1;
			this.capacity = (int) ((float) parInt1 * this.percentUseable);
		}
	}

	/**+
	 * copies the hash table to the specified array
	 */
	private void copyHashTableTo(LongHashMap.Entry<V>[] parArrayOfEntry) {
		LongHashMap.Entry[] alonghashmap$entry = this.hashArray;
		int i = parArrayOfEntry.length;

		for (int j = 0; j < alonghashmap$entry.length; ++j) {
			LongHashMap.Entry longhashmap$entry = alonghashmap$entry[j];
			if (longhashmap$entry != null) {
				alonghashmap$entry[j] = null;

				while (true) {
					LongHashMap.Entry longhashmap$entry1 = longhashmap$entry.nextEntry;
					int k = getHashIndex(longhashmap$entry.hash, i - 1);
					longhashmap$entry.nextEntry = parArrayOfEntry[k];
					parArrayOfEntry[k] = longhashmap$entry;
					longhashmap$entry = longhashmap$entry1;
					if (longhashmap$entry1 == null) {
						break;
					}
				}
			}
		}

	}

	/**+
	 * calls the removeKey method and returns removed object
	 */
	public V remove(long parLong1) {
		LongHashMap.Entry longhashmap$entry = this.removeKey(parLong1);
		return (V) (longhashmap$entry == null ? null : longhashmap$entry.value);
	}

	/**+
	 * removes the key from the hash linked list
	 */
	final LongHashMap.Entry<V> removeKey(long parLong1) {
		int i = getHashedKey(parLong1);
		int j = getHashIndex(i, this.mask);
		LongHashMap.Entry longhashmap$entry = this.hashArray[j];

		LongHashMap.Entry longhashmap$entry1;
		LongHashMap.Entry longhashmap$entry2;
		for (longhashmap$entry1 = longhashmap$entry; longhashmap$entry1 != null; longhashmap$entry1 = longhashmap$entry2) {
			longhashmap$entry2 = longhashmap$entry1.nextEntry;
			if (longhashmap$entry1.key == parLong1) {
				++this.modCount;
				--this.numHashElements;
				if (longhashmap$entry == longhashmap$entry1) {
					this.hashArray[j] = longhashmap$entry2;
				} else {
					longhashmap$entry.nextEntry = longhashmap$entry2;
				}

				return longhashmap$entry1;
			}

			longhashmap$entry = longhashmap$entry1;
		}

		return longhashmap$entry1;
	}

	/**+
	 * creates the key in the hash table
	 */
	private void createKey(int parInt1, long parLong1, V parObject, int parInt2) {
		LongHashMap.Entry longhashmap$entry = this.hashArray[parInt2];
		this.hashArray[parInt2] = new LongHashMap.Entry(parInt1, parLong1, parObject, longhashmap$entry);
		if (this.numHashElements++ >= this.capacity) {
			this.resizeTable(2 * this.hashArray.length);
		}

	}

	static class Entry<V> {
		final long key;
		V value;
		LongHashMap.Entry<V> nextEntry;
		final int hash;

		Entry(int parInt1, long parLong1, V parObject, LongHashMap.Entry<V> parEntry) {
			this.value = parObject;
			this.nextEntry = parEntry;
			this.key = parLong1;
			this.hash = parInt1;
		}

		public final long getKey() {
			return this.key;
		}

		public final V getValue() {
			return this.value;
		}

		public final boolean equals(Object object) {
			if (!(object instanceof LongHashMap.Entry)) {
				return false;
			} else {
				LongHashMap.Entry longhashmap$entry = (LongHashMap.Entry) object;
				Long olong = Long.valueOf(this.getKey());
				Long olong1 = Long.valueOf(longhashmap$entry.getKey());
				if (olong == olong1 || olong != null && olong.equals(olong1)) {
					Object object1 = this.getValue();
					Object object2 = longhashmap$entry.getValue();
					if (object1 == object2 || object1 != null && object1.equals(object2)) {
						return true;
					}
				}

				return false;
			}
		}

		public final int hashCode() {
			return LongHashMap.getHashedKey(this.key);
		}

		public final String toString() {
			return this.getKey() + "=" + this.getValue();
		}
	}
}