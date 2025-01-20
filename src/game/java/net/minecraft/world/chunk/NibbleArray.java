package net.minecraft.world.chunk;

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
public class NibbleArray {
	private final byte[] data;

	public NibbleArray() {
		this.data = new byte[2048];
	}

	public NibbleArray(byte[] storageArray) {
		this.data = storageArray;
		if (storageArray.length != 2048) {
			throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + storageArray.length);
		}
	}

	/**+
	 * Returns the nibble of data corresponding to the passed in x,
	 * y, z. y is at most 6 bits, z is at most 4.
	 */
	public int get(int x, int y, int z) {
		return this.getFromIndex(this.getCoordinateIndex(x, y, z));
	}

	/**+
	 * Arguments are x, y, z, val. Sets the nibble of data at x <<
	 * 11 | z << 7 | y to val.
	 */
	public void set(int x, int y, int z, int value) {
		this.setIndex(this.getCoordinateIndex(x, y, z), value);
	}

	private int getCoordinateIndex(int x, int y, int z) {
		return y << 8 | z << 4 | x;
	}

	public int getFromIndex(int index) {
		int shift = (index & 1) << 2;
		return data[index >> 1] >> shift & 15;
	}

	public void setIndex(int index, int value) {
		int i = index >> 1;
		int shift = (index & 1) << 2;
		data[i] = (byte) (data[i] & ~(15 << shift) | (value & 15) << shift);
	}

	private boolean isLowerNibble(int index) {
		return (index & 1) == 0;
	}

	private int getNibbleIndex(int index) {
		return index >> 1;
	}

	public byte[] getData() {
		return this.data;
	}
}