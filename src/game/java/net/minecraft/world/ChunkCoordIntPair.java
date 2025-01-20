package net.minecraft.world;

import net.minecraft.util.BlockPos;

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
public class ChunkCoordIntPair {
	public final int chunkXPos;
	public final int chunkZPos;

	public ChunkCoordIntPair(int x, int z) {
		this.chunkXPos = x;
		this.chunkZPos = z;
	}

	/**+
	 * converts a chunk coordinate pair to an integer (suitable for
	 * hashing)
	 */
	public static long chunkXZ2Int(int x, int z) {
		return (long) x & 4294967295L | ((long) z & 4294967295L) << 32;
	}

	public int hashCode() {
		int i = 1664525 * this.chunkXPos + 1013904223;
		int j = 1664525 * (this.chunkZPos ^ -559038737) + 1013904223;
		return i ^ j;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof ChunkCoordIntPair)) {
			return false;
		} else {
			ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) object;
			return this.chunkXPos == chunkcoordintpair.chunkXPos && this.chunkZPos == chunkcoordintpair.chunkZPos;
		}
	}

	public int getCenterXPos() {
		return (this.chunkXPos << 4) + 8;
	}

	public int getCenterZPosition() {
		return (this.chunkZPos << 4) + 8;
	}

	/**+
	 * Get the first world X coordinate that belongs to this Chunk
	 */
	public int getXStart() {
		return this.chunkXPos << 4;
	}

	/**+
	 * Get the first world Z coordinate that belongs to this Chunk
	 */
	public int getZStart() {
		return this.chunkZPos << 4;
	}

	/**+
	 * Get the last world X coordinate that belongs to this Chunk
	 */
	public int getXEnd() {
		return (this.chunkXPos << 4) + 15;
	}

	/**+
	 * Get the last world Z coordinate that belongs to this Chunk
	 */
	public int getZEnd() {
		return (this.chunkZPos << 4) + 15;
	}

	/**+
	 * Get the World coordinates of the Block with the given Chunk
	 * coordinates relative to this chunk
	 */
	public BlockPos getBlock(int x, int y, int z) {
		return new BlockPos((this.chunkXPos << 4) + x, y, (this.chunkZPos << 4) + z);
	}

	/**+
	 * Get the coordinates of the Block in the center of this chunk
	 * with the given Y coordinate
	 */
	public BlockPos getCenterBlock(int y) {
		return new BlockPos(this.getCenterXPos(), y, this.getCenterZPosition());
	}

	public String toString() {
		return "[" + this.chunkXPos + ", " + this.chunkZPos + "]";
	}
}