package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

import com.google.common.collect.Maps;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;

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
public class NBTTagCompound extends NBTBase {
	private Map<String, NBTBase> tagMap = Maps.newHashMap();

	/**+
	 * Write the actual data contents of the tag, implemented in NBT
	 * extension classes
	 */
	void write(DataOutput parDataOutput) throws IOException {
		for (String s : this.tagMap.keySet()) {
			NBTBase nbtbase = (NBTBase) this.tagMap.get(s);
			writeEntry(s, nbtbase, parDataOutput);
		}

		parDataOutput.writeByte(0);
	}

	void read(DataInput parDataInput, int parInt1, NBTSizeTracker parNBTSizeTracker) throws IOException {
		parNBTSizeTracker.read(384L);
		if (parInt1 > 512) {
			throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
		} else {
			this.tagMap.clear();

			byte b0;
			while ((b0 = readType(parDataInput, parNBTSizeTracker)) != 0) {
				String s = readKey(parDataInput, parNBTSizeTracker);
				parNBTSizeTracker.read((long) (224 + 16 * s.length()));
				NBTBase nbtbase = readNBT(b0, s, parDataInput, parInt1 + 1, parNBTSizeTracker);
				if (this.tagMap.put(s, nbtbase) != null) {
					parNBTSizeTracker.read(288L);
				}
			}

		}
	}

	/**+
	 * Gets a set with the names of the keys in the tag compound.
	 */
	public Set<String> getKeySet() {
		return this.tagMap.keySet();
	}

	/**+
	 * Gets the type byte for the tag.
	 */
	public byte getId() {
		return (byte) 10;
	}

	/**+
	 * Stores the given tag into the map with the given string key.
	 * This is mostly used to store tag lists.
	 */
	public void setTag(String key, NBTBase value) {
		this.tagMap.put(key, value);
	}

	/**+
	 * Stores a new NBTTagByte with the given byte value into the
	 * map with the given string key.
	 */
	public void setByte(String key, byte value) {
		this.tagMap.put(key, new NBTTagByte(value));
	}

	/**+
	 * Stores a new NBTTagShort with the given short value into the
	 * map with the given string key.
	 */
	public void setShort(String key, short value) {
		this.tagMap.put(key, new NBTTagShort(value));
	}

	/**+
	 * Stores a new NBTTagInt with the given integer value into the
	 * map with the given string key.
	 */
	public void setInteger(String key, int value) {
		this.tagMap.put(key, new NBTTagInt(value));
	}

	/**+
	 * Stores a new NBTTagLong with the given long value into the
	 * map with the given string key.
	 */
	public void setLong(String key, long value) {
		this.tagMap.put(key, new NBTTagLong(value));
	}

	/**+
	 * Stores a new NBTTagFloat with the given float value into the
	 * map with the given string key.
	 */
	public void setFloat(String key, float value) {
		this.tagMap.put(key, new NBTTagFloat(value));
	}

	/**+
	 * Stores a new NBTTagDouble with the given double value into
	 * the map with the given string key.
	 */
	public void setDouble(String key, double value) {
		this.tagMap.put(key, new NBTTagDouble(value));
	}

	/**+
	 * Stores a new NBTTagString with the given string value into
	 * the map with the given string key.
	 */
	public void setString(String key, String value) {
		this.tagMap.put(key, new NBTTagString(value));
	}

	/**+
	 * Stores a new NBTTagByteArray with the given array as data
	 * into the map with the given string key.
	 */
	public void setByteArray(String key, byte[] value) {
		this.tagMap.put(key, new NBTTagByteArray(value));
	}

	/**+
	 * Stores a new NBTTagIntArray with the given array as data into
	 * the map with the given string key.
	 */
	public void setIntArray(String key, int[] value) {
		this.tagMap.put(key, new NBTTagIntArray(value));
	}

	/**+
	 * Stores the given boolean value as a NBTTagByte, storing 1 for
	 * true and 0 for false, using the given string key.
	 */
	public void setBoolean(String key, boolean value) {
		this.setByte(key, (byte) (value ? 1 : 0));
	}

	/**+
	 * gets a generic tag with the specified name
	 */
	public NBTBase getTag(String key) {
		return (NBTBase) this.tagMap.get(key);
	}

	/**+
	 * Gets the ID byte for the given tag key
	 */
	public byte getTagId(String key) {
		NBTBase nbtbase = (NBTBase) this.tagMap.get(key);
		return nbtbase != null ? nbtbase.getId() : 0;
	}

	/**+
	 * Returns whether the given string has been previously stored
	 * as a key in the map.
	 */
	public boolean hasKey(String key) {
		return this.tagMap.containsKey(key);
	}

	/**+
	 * Returns whether the given string has been previously stored
	 * as a key in the map.
	 */
	public boolean hasKey(String key, int type) {
		byte b0 = this.getTagId(key);
		if (b0 == type) {
			return true;
		} else if (type != 99) {
			if (b0 > 0) {
				;
			}

			return false;
		} else {
			return b0 == 1 || b0 == 2 || b0 == 3 || b0 == 4 || b0 == 5 || b0 == 6;
		}
	}

	/**+
	 * Retrieves a byte value using the specified key, or 0 if no
	 * such key was stored.
	 */
	public byte getByte(String key) {
		try {
			return !this.hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getByte();
		} catch (ClassCastException var3) {
			return (byte) 0;
		}
	}

	/**+
	 * Retrieves a short value using the specified key, or 0 if no
	 * such key was stored.
	 */
	public short getShort(String key) {
		try {
			return !this.hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getShort();
		} catch (ClassCastException var3) {
			return (short) 0;
		}
	}

	/**+
	 * Retrieves an integer value using the specified key, or 0 if
	 * no such key was stored.
	 */
	public int getInteger(String key) {
		try {
			return !this.hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getInt();
		} catch (ClassCastException var3) {
			return 0;
		}
	}

	/**+
	 * Retrieves a long value using the specified key, or 0 if no
	 * such key was stored.
	 */
	public long getLong(String key) {
		try {
			return !this.hasKey(key, 99) ? 0L : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getLong();
		} catch (ClassCastException var3) {
			return 0L;
		}
	}

	/**+
	 * Retrieves a float value using the specified key, or 0 if no
	 * such key was stored.
	 */
	public float getFloat(String key) {
		try {
			return !this.hasKey(key, 99) ? 0.0F : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getFloat();
		} catch (ClassCastException var3) {
			return 0.0F;
		}
	}

	/**+
	 * Retrieves a double value using the specified key, or 0 if no
	 * such key was stored.
	 */
	public double getDouble(String key) {
		try {
			return !this.hasKey(key, 99) ? 0.0D : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getDouble();
		} catch (ClassCastException var3) {
			return 0.0D;
		}
	}

	/**+
	 * Retrieves a string value using the specified key, or an empty
	 * string if no such key was stored.
	 */
	public String getString(String key) {
		try {
			return !this.hasKey(key, 8) ? "" : ((NBTBase) this.tagMap.get(key)).getString();
		} catch (ClassCastException var3) {
			return "";
		}
	}

	/**+
	 * Retrieves a byte array using the specified key, or a
	 * zero-length array if no such key was stored.
	 */
	public byte[] getByteArray(String key) {
		try {
			return !this.hasKey(key, 7) ? new byte[0] : ((NBTTagByteArray) this.tagMap.get(key)).getByteArray();
		} catch (ClassCastException classcastexception) {
			throw new ReportedException(this.createCrashReport(key, 7, classcastexception));
		}
	}

	/**+
	 * Retrieves an int array using the specified key, or a
	 * zero-length array if no such key was stored.
	 */
	public int[] getIntArray(String key) {
		try {
			return !this.hasKey(key, 11) ? new int[0] : ((NBTTagIntArray) this.tagMap.get(key)).getIntArray();
		} catch (ClassCastException classcastexception) {
			throw new ReportedException(this.createCrashReport(key, 11, classcastexception));
		}
	}

	/**+
	 * Retrieves a NBTTagCompound subtag matching the specified key,
	 * or a new empty NBTTagCompound if no such key was stored.
	 */
	public NBTTagCompound getCompoundTag(String key) {
		try {
			return !this.hasKey(key, 10) ? new NBTTagCompound() : (NBTTagCompound) this.tagMap.get(key);
		} catch (ClassCastException classcastexception) {
			throw new ReportedException(this.createCrashReport(key, 10, classcastexception));
		}
	}

	/**+
	 * Gets the NBTTagList object with the given name. Args: name,
	 * NBTBase type
	 */
	public NBTTagList getTagList(String key, int type) {
		try {
			if (this.getTagId(key) != 9) {
				return new NBTTagList();
			} else {
				NBTTagList nbttaglist = (NBTTagList) this.tagMap.get(key);
				return nbttaglist.tagCount() > 0 && nbttaglist.getTagType() != type ? new NBTTagList() : nbttaglist;
			}
		} catch (ClassCastException classcastexception) {
			throw new ReportedException(this.createCrashReport(key, 9, classcastexception));
		}
	}

	/**+
	 * Retrieves a boolean value using the specified key, or false
	 * if no such key was stored. This uses the getByte method.
	 */
	public boolean getBoolean(String key) {
		return this.getByte(key) != 0;
	}

	/**+
	 * Remove the specified tag.
	 */
	public void removeTag(String key) {
		this.tagMap.remove(key);
	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder("{");

		for (Entry entry : this.tagMap.entrySet()) {
			if (stringbuilder.length() != 1) {
				stringbuilder.append(',');
			}

			stringbuilder.append((String) entry.getKey()).append(':').append(entry.getValue());
		}

		return stringbuilder.append('}').toString();
	}

	/**+
	 * Return whether this compound has no tags.
	 */
	public boolean hasNoTags() {
		return this.tagMap.isEmpty();
	}

	/**+
	 * Create a crash report which indicates a NBT read error.
	 */
	private CrashReport createCrashReport(final String key, final int expectedType, ClassCastException ex) {
		CrashReport crashreport = CrashReport.makeCrashReport(ex, "Reading NBT data");
		CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
		crashreportcategory.addCrashSectionCallable("Tag type found", new Callable<String>() {
			public String call() throws Exception {
				return NBTBase.NBT_TYPES[((NBTBase) NBTTagCompound.this.tagMap.get(key)).getId()];
			}
		});
		crashreportcategory.addCrashSectionCallable("Tag type expected", new Callable<String>() {
			public String call() throws Exception {
				return NBTBase.NBT_TYPES[expectedType];
			}
		});
		crashreportcategory.addCrashSection("Tag name", key);
		return crashreport;
	}

	/**+
	 * Creates a clone of the tag.
	 */
	public NBTBase copy() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();

		for (String s : this.tagMap.keySet()) {
			nbttagcompound.setTag(s, ((NBTBase) this.tagMap.get(s)).copy());
		}

		return nbttagcompound;
	}

	public boolean equals(Object object) {
		if (super.equals(object)) {
			NBTTagCompound nbttagcompound = (NBTTagCompound) object;
			return this.tagMap.entrySet().equals(nbttagcompound.tagMap.entrySet());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return super.hashCode() ^ this.tagMap.hashCode();
	}

	private static void writeEntry(String name, NBTBase data, DataOutput output) throws IOException {
		output.writeByte(data.getId());
		if (data.getId() != 0) {
			output.writeUTF(name);
			data.write(output);
		}
	}

	private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
		return input.readByte();
	}

	private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
		return input.readUTF();
	}

	static NBTBase readNBT(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker)
			throws IOException {
		NBTBase nbtbase = NBTBase.createNewByType(id);

		try {
			nbtbase.read(input, depth, sizeTracker);
			return nbtbase;
		} catch (IOException ioexception) {
			CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
			crashreportcategory.addCrashSection("Tag name", key);
			crashreportcategory.addCrashSection("Tag type", Byte.valueOf(id));
			throw new ReportedException(crashreport);
		}
	}

	/**+
	 * Merges this NBTTagCompound with the given compound. Any
	 * sub-compounds are merged using the same methods, other types
	 * of tags are overwritten from the given compound.
	 */
	public void merge(NBTTagCompound other) {
		for (String s : other.tagMap.keySet()) {
			NBTBase nbtbase = (NBTBase) other.tagMap.get(s);
			if (nbtbase.getId() == 10) {
				if (this.hasKey(s, 10)) {
					NBTTagCompound nbttagcompound = this.getCompoundTag(s);
					nbttagcompound.merge((NBTTagCompound) nbtbase);
				} else {
					this.setTag(s, nbtbase.copy());
				}
			} else {
				this.setTag(s, nbtbase.copy());
			}
		}

	}
}