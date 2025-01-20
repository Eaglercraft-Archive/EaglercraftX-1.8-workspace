package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

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
public class NBTTagList extends NBTBase {
	private static final Logger LOGGER = LogManager.getLogger();
	/**+
	 * The array list containing the tags encapsulated in this list.
	 */
	private List<NBTBase> tagList = Lists.newArrayList();
	/**+
	 * The type byte for the tags in the list - they must all be of
	 * the same type.
	 */
	private byte tagType = 0;

	/**+
	 * Write the actual data contents of the tag, implemented in NBT
	 * extension classes
	 */
	void write(DataOutput parDataOutput) throws IOException {
		if (!this.tagList.isEmpty()) {
			this.tagType = ((NBTBase) this.tagList.get(0)).getId();
		} else {
			this.tagType = 0;
		}

		parDataOutput.writeByte(this.tagType);
		parDataOutput.writeInt(this.tagList.size());

		for (int i = 0; i < this.tagList.size(); ++i) {
			((NBTBase) this.tagList.get(i)).write(parDataOutput);
		}

	}

	void read(DataInput parDataInput, int parInt1, NBTSizeTracker parNBTSizeTracker) throws IOException {
		parNBTSizeTracker.read(296L);
		if (parInt1 > 512) {
			throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
		} else {
			this.tagType = parDataInput.readByte();
			int i = parDataInput.readInt();
			if (this.tagType == 0 && i > 0) {
				throw new RuntimeException("Missing type on ListTag");
			} else {
				parNBTSizeTracker.read(32L * (long) i);
				this.tagList = Lists.newArrayListWithCapacity(i);

				for (int j = 0; j < i; ++j) {
					NBTBase nbtbase = NBTBase.createNewByType(this.tagType);
					nbtbase.read(parDataInput, parInt1 + 1, parNBTSizeTracker);
					this.tagList.add(nbtbase);
				}

			}
		}
	}

	/**+
	 * Gets the type byte for the tag.
	 */
	public byte getId() {
		return (byte) 9;
	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder("[");

		for (int i = 0; i < this.tagList.size(); ++i) {
			if (i != 0) {
				stringbuilder.append(',');
			}

			stringbuilder.append(i).append(':').append(this.tagList.get(i));
		}

		return stringbuilder.append(']').toString();
	}

	/**+
	 * Adds the provided tag to the end of the list. There is no
	 * check to verify this tag is of the same type as any previous
	 * tag.
	 */
	public void appendTag(NBTBase nbt) {
		if (nbt.getId() == 0) {
			LOGGER.warn("Invalid TagEnd added to ListTag");
		} else {
			if (this.tagType == 0) {
				this.tagType = nbt.getId();
			} else if (this.tagType != nbt.getId()) {
				LOGGER.warn("Adding mismatching tag types to tag list");
				return;
			}

			this.tagList.add(nbt);
		}
	}

	/**+
	 * Set the given index to the given tag
	 */
	public void set(int idx, NBTBase nbt) {
		if (nbt.getId() == 0) {
			LOGGER.warn("Invalid TagEnd added to ListTag");
		} else if (idx >= 0 && idx < this.tagList.size()) {
			if (this.tagType == 0) {
				this.tagType = nbt.getId();
			} else if (this.tagType != nbt.getId()) {
				LOGGER.warn("Adding mismatching tag types to tag list");
				return;
			}

			this.tagList.set(idx, nbt);
		} else {
			LOGGER.warn("index out of bounds to set tag in tag list");
		}
	}

	/**+
	 * Removes a tag at the given index.
	 */
	public NBTBase removeTag(int i) {
		return (NBTBase) this.tagList.remove(i);
	}

	/**+
	 * Return whether this compound has no tags.
	 */
	public boolean hasNoTags() {
		return this.tagList.isEmpty();
	}

	/**+
	 * Retrieves the NBTTagCompound at the specified index in the
	 * list
	 */
	public NBTTagCompound getCompoundTagAt(int i) {
		if (i >= 0 && i < this.tagList.size()) {
			NBTBase nbtbase = (NBTBase) this.tagList.get(i);
			return nbtbase.getId() == 10 ? (NBTTagCompound) nbtbase : new NBTTagCompound();
		} else {
			return new NBTTagCompound();
		}
	}

	public int[] getIntArrayAt(int i) {
		if (i >= 0 && i < this.tagList.size()) {
			NBTBase nbtbase = (NBTBase) this.tagList.get(i);
			return nbtbase.getId() == 11 ? ((NBTTagIntArray) nbtbase).getIntArray() : new int[0];
		} else {
			return new int[0];
		}
	}

	public double getDoubleAt(int i) {
		if (i >= 0 && i < this.tagList.size()) {
			NBTBase nbtbase = (NBTBase) this.tagList.get(i);
			return nbtbase.getId() == 6 ? ((NBTTagDouble) nbtbase).getDouble() : 0.0D;
		} else {
			return 0.0D;
		}
	}

	public float getFloatAt(int i) {
		if (i >= 0 && i < this.tagList.size()) {
			NBTBase nbtbase = (NBTBase) this.tagList.get(i);
			return nbtbase.getId() == 5 ? ((NBTTagFloat) nbtbase).getFloat() : 0.0F;
		} else {
			return 0.0F;
		}
	}

	/**+
	 * Retrieves the tag String value at the specified index in the
	 * list
	 */
	public String getStringTagAt(int i) {
		if (i >= 0 && i < this.tagList.size()) {
			NBTBase nbtbase = (NBTBase) this.tagList.get(i);
			return nbtbase.getId() == 8 ? nbtbase.getString() : nbtbase.toString();
		} else {
			return "";
		}
	}

	/**+
	 * Get the tag at the given position
	 */
	public NBTBase get(int idx) {
		return (NBTBase) (idx >= 0 && idx < this.tagList.size() ? (NBTBase) this.tagList.get(idx) : new NBTTagEnd());
	}

	/**+
	 * Returns the number of tags in the list.
	 */
	public int tagCount() {
		return this.tagList.size();
	}

	/**+
	 * Creates a clone of the tag.
	 */
	public NBTBase copy() {
		NBTTagList nbttaglist = new NBTTagList();
		nbttaglist.tagType = this.tagType;

		for (int i = 0, l = this.tagList.size(); i < l; ++i) {
			NBTBase nbtbase1 = this.tagList.get(i).copy();
			nbttaglist.tagList.add(nbtbase1);
		}

		return nbttaglist;
	}

	public boolean equals(Object object) {
		if (super.equals(object)) {
			NBTTagList nbttaglist = (NBTTagList) object;
			if (this.tagType == nbttaglist.tagType) {
				return this.tagList.equals(nbttaglist.tagList);
			}
		}

		return false;
	}

	public int hashCode() {
		return super.hashCode() ^ this.tagList.hashCode();
	}

	public int getTagType() {
		return this.tagType;
	}
}