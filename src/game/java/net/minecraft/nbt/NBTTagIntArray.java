package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

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
public class NBTTagIntArray extends NBTBase {
	private int[] intArray;

	NBTTagIntArray() {
	}

	public NBTTagIntArray(int[] parArrayOfInt) {
		this.intArray = parArrayOfInt;
	}

	/**+
	 * Write the actual data contents of the tag, implemented in NBT
	 * extension classes
	 */
	void write(DataOutput parDataOutput) throws IOException {
		parDataOutput.writeInt(this.intArray.length);

		for (int i = 0; i < this.intArray.length; ++i) {
			parDataOutput.writeInt(this.intArray[i]);
		}

	}

	void read(DataInput parDataInput, int parInt1, NBTSizeTracker parNBTSizeTracker) throws IOException {
		parNBTSizeTracker.read(192L);
		int i = parDataInput.readInt();
		parNBTSizeTracker.read((long) (32 * i));
		this.intArray = new int[i];

		for (int j = 0; j < i; ++j) {
			this.intArray[j] = parDataInput.readInt();
		}

	}

	/**+
	 * Gets the type byte for the tag.
	 */
	public byte getId() {
		return (byte) 11;
	}

	public String toString() {
		String s = "[";

		for (int i = 0; i < this.intArray.length; ++i) {
			s = s + this.intArray[i] + ",";
		}

		return s + "]";
	}

	/**+
	 * Creates a clone of the tag.
	 */
	public NBTBase copy() {
		int[] aint = new int[this.intArray.length];
		System.arraycopy(this.intArray, 0, aint, 0, this.intArray.length);
		return new NBTTagIntArray(aint);
	}

	public boolean equals(Object object) {
		return super.equals(object) ? Arrays.equals(this.intArray, ((NBTTagIntArray) object).intArray) : false;
	}

	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(this.intArray);
	}

	public int[] getIntArray() {
		return this.intArray;
	}
}