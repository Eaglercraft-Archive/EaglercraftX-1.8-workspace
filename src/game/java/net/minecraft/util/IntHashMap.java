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
public class IntHashMap<V> {
	/**+
	 * An array of HashEntries representing the heads of hash slot
	 * lists
	 */
	private transient IntHashMap.Entry<V>[] slots = new IntHashMap.Entry[16];
	private transient int count;
	/**+
	 * The grow threshold
	 */
	private int threshold = 12;
	/**+
	 * The scale factor used to determine when to grow the table
	 */
	private final float growFactor = 0.75F;

	/**+
	 * Makes the passed in integer suitable for hashing by a number
	 * of shifts
	 */
	private static int computeHash(int integer) {
		integer = integer ^ integer >>> 20 ^ integer >>> 12;
		return integer ^ integer >>> 7 ^ integer >>> 4;
	}

	/**+
	 * Computes the index of the slot for the hash and slot count
	 * passed in.
	 */
	private static int getSlotIndex(int hash, int slotCount) {
		return hash & slotCount - 1;
	}

	/**+
	 * Returns the object associated to a key
	 */
	public V lookup(int parInt1) {
		int i = computeHash(parInt1);

		for (IntHashMap.Entry inthashmap$entry = this.slots[getSlotIndex(i,
				this.slots.length)]; inthashmap$entry != null; inthashmap$entry = inthashmap$entry.nextEntry) {
			if (inthashmap$entry.hashEntry == parInt1) {
				return (V) inthashmap$entry.valueEntry;
			}
		}

		return (V) null;
	}

	/**+
	 * Returns true if this hash table contains the specified item.
	 */
	public boolean containsItem(int parInt1) {
		return this.lookupEntry(parInt1) != null;
	}

	/**+
	 * Returns the internal entry for a key
	 */
	final IntHashMap.Entry<V> lookupEntry(int parInt1) {
		int i = computeHash(parInt1);

		for (IntHashMap.Entry inthashmap$entry = this.slots[getSlotIndex(i,
				this.slots.length)]; inthashmap$entry != null; inthashmap$entry = inthashmap$entry.nextEntry) {
			if (inthashmap$entry.hashEntry == parInt1) {
				return inthashmap$entry;
			}
		}

		return null;
	}

	/**+
	 * Adds a key and associated value to this map
	 */
	public void addKey(int parInt1, V parObject) {
		int i = computeHash(parInt1);
		int j = getSlotIndex(i, this.slots.length);

		for (IntHashMap.Entry inthashmap$entry = this.slots[j]; inthashmap$entry != null; inthashmap$entry = inthashmap$entry.nextEntry) {
			if (inthashmap$entry.hashEntry == parInt1) {
				inthashmap$entry.valueEntry = parObject;
				return;
			}
		}

		this.insert(i, parInt1, parObject, j);
	}

	/**+
	 * Increases the number of hash slots
	 */
	private void grow(int parInt1) {
		IntHashMap.Entry[] ainthashmap$entry = this.slots;
		int i = ainthashmap$entry.length;
		if (i == 1073741824) {
			this.threshold = Integer.MAX_VALUE;
		} else {
			IntHashMap.Entry[] ainthashmap$entry1 = new IntHashMap.Entry[parInt1];
			this.copyTo(ainthashmap$entry1);
			this.slots = ainthashmap$entry1;
			this.threshold = (int) ((float) parInt1 * this.growFactor);
		}
	}

	/**+
	 * Copies the hash slots to a new array
	 */
	private void copyTo(IntHashMap.Entry<V>[] parArrayOfEntry) {
		IntHashMap.Entry[] ainthashmap$entry = this.slots;
		int i = parArrayOfEntry.length;

		for (int j = 0; j < ainthashmap$entry.length; ++j) {
			IntHashMap.Entry inthashmap$entry = ainthashmap$entry[j];
			if (inthashmap$entry != null) {
				ainthashmap$entry[j] = null;

				while (true) {
					IntHashMap.Entry inthashmap$entry1 = inthashmap$entry.nextEntry;
					int k = getSlotIndex(inthashmap$entry.slotHash, i);
					inthashmap$entry.nextEntry = parArrayOfEntry[k];
					parArrayOfEntry[k] = inthashmap$entry;
					inthashmap$entry = inthashmap$entry1;
					if (inthashmap$entry1 == null) {
						break;
					}
				}
			}
		}

	}

	/**+
	 * Removes the specified object from the map and returns it
	 */
	public V removeObject(int parInt1) {
		IntHashMap.Entry inthashmap$entry = this.removeEntry(parInt1);
		return (V) (inthashmap$entry == null ? null : inthashmap$entry.valueEntry);
	}

	/**+
	 * Removes the specified entry from the map and returns it
	 */
	final IntHashMap.Entry<V> removeEntry(int parInt1) {
		int i = computeHash(parInt1);
		int j = getSlotIndex(i, this.slots.length);
		IntHashMap.Entry inthashmap$entry = this.slots[j];

		IntHashMap.Entry inthashmap$entry1;
		IntHashMap.Entry inthashmap$entry2;
		for (inthashmap$entry1 = inthashmap$entry; inthashmap$entry1 != null; inthashmap$entry1 = inthashmap$entry2) {
			inthashmap$entry2 = inthashmap$entry1.nextEntry;
			if (inthashmap$entry1.hashEntry == parInt1) {
				--this.count;
				if (inthashmap$entry == inthashmap$entry1) {
					this.slots[j] = inthashmap$entry2;
				} else {
					inthashmap$entry.nextEntry = inthashmap$entry2;
				}

				return inthashmap$entry1;
			}

			inthashmap$entry = inthashmap$entry1;
		}

		return inthashmap$entry1;
	}

	/**+
	 * Removes all entries from the map
	 */
	public void clearMap() {
		IntHashMap.Entry[] ainthashmap$entry = this.slots;

		for (int i = 0; i < ainthashmap$entry.length; ++i) {
			ainthashmap$entry[i] = null;
		}

		this.count = 0;
	}

	/**+
	 * Adds an object to a slot
	 */
	private void insert(int parInt1, int parInt2, V parObject, int parInt3) {
		IntHashMap.Entry inthashmap$entry = this.slots[parInt3];
		this.slots[parInt3] = new IntHashMap.Entry(parInt1, parInt2, parObject, inthashmap$entry);
		if (this.count++ >= this.threshold) {
			this.grow(2 * this.slots.length);
		}

	}

	static class Entry<V> {
		final int hashEntry;
		V valueEntry;
		IntHashMap.Entry<V> nextEntry;
		final int slotHash;

		Entry(int parInt1, int parInt2, V parObject, IntHashMap.Entry<V> parEntry) {
			this.valueEntry = parObject;
			this.nextEntry = parEntry;
			this.hashEntry = parInt2;
			this.slotHash = parInt1;
		}

		public final int getHash() {
			return this.hashEntry;
		}

		public final V getValue() {
			return this.valueEntry;
		}

		public final boolean equals(Object object) {
			if (!(object instanceof IntHashMap.Entry)) {
				return false;
			} else {
				IntHashMap.Entry inthashmap$entry = (IntHashMap.Entry) object;
				Integer integer = Integer.valueOf(this.getHash());
				Integer integer1 = Integer.valueOf(inthashmap$entry.getHash());
				if (integer == integer1 || integer != null && integer.equals(integer1)) {
					Object object1 = this.getValue();
					Object object2 = inthashmap$entry.getValue();
					if (object1 == object2 || object1 != null && object1.equals(object2)) {
						return true;
					}
				}

				return false;
			}
		}

		public final int hashCode() {
			return IntHashMap.computeHash(this.hashEntry);
		}

		public final String toString() {
			return this.getHash() + "=" + this.getValue();
		}
	}
}