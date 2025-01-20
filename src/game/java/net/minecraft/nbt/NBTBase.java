package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

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
public abstract class NBTBase {
	public static final String[] NBT_TYPES = new String[] { "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE",
			"BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]" };

	abstract void write(DataOutput var1) throws IOException;

	abstract void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException;

	public abstract String toString();

	public abstract byte getId();

	/**+
	 * Creates a new NBTBase object that corresponds with the passed
	 * in id.
	 */
	protected static NBTBase createNewByType(byte id) {
		switch (id) {
		case 0:
			return new NBTTagEnd();
		case 1:
			return new NBTTagByte();
		case 2:
			return new NBTTagShort();
		case 3:
			return new NBTTagInt();
		case 4:
			return new NBTTagLong();
		case 5:
			return new NBTTagFloat();
		case 6:
			return new NBTTagDouble();
		case 7:
			return new NBTTagByteArray();
		case 8:
			return new NBTTagString();
		case 9:
			return new NBTTagList();
		case 10:
			return new NBTTagCompound();
		case 11:
			return new NBTTagIntArray();
		default:
			return null;
		}
	}

	public abstract NBTBase copy();

	/**+
	 * Return whether this compound has no tags.
	 */
	public boolean hasNoTags() {
		return false;
	}

	public boolean equals(Object object) {
		if (!(object instanceof NBTBase)) {
			return false;
		} else {
			NBTBase nbtbase = (NBTBase) object;
			return this.getId() == nbtbase.getId();
		}
	}

	public int hashCode() {
		return this.getId();
	}

	protected String getString() {
		return this.toString();
	}

	public abstract static class NBTPrimitive extends NBTBase {
		public abstract long getLong();

		public abstract int getInt();

		public abstract short getShort();

		public abstract byte getByte();

		public abstract double getDouble();

		public abstract float getFloat();
	}
}