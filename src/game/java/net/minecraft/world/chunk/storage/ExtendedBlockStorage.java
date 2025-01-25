package net.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.NibbleArray;

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
public class ExtendedBlockStorage {
	private int yBase;
	private int blockRefCount;
	private int tickRefCount;
	private char[] data;
	private NibbleArray blocklightArray;
	private NibbleArray skylightArray;

	private int alfheim$lightRefCount = -1;

	public ExtendedBlockStorage(int y, boolean storeSkylight) {
		this.yBase = y;
		this.data = new char[4096];
		this.blocklightArray = new NibbleArray();
		if (storeSkylight) {
			this.skylightArray = new NibbleArray();
		}

	}

	public IBlockState get(int x, int y, int z) {
		IBlockState iblockstate = (IBlockState) Block.BLOCK_STATE_IDS.getByValue(this.data[y << 8 | z << 4 | x]);
		return iblockstate != null ? iblockstate : Blocks.air.getDefaultState();
	}

	public void set(int x, int y, int z, IBlockState state) {
		IBlockState iblockstate = this.get(x, y, z);
		Block block = iblockstate.getBlock();
		Block block1 = state.getBlock();
		if (block != Blocks.air) {
			--this.blockRefCount;
			if (block.getTickRandomly()) {
				--this.tickRefCount;
			}
		}

		if (block1 != Blocks.air) {
			++this.blockRefCount;
			if (block1.getTickRandomly()) {
				++this.tickRefCount;
			}
		}

		this.data[y << 8 | z << 4 | x] = (char) Block.BLOCK_STATE_IDS.get(state);
	}

	/**+
	 * Returns the block for a location in a chunk, with the
	 * extended ID merged from a byte array and a NibbleArray to
	 * form a full 12-bit block ID.
	 */
	public Block getBlockByExtId(int x, int y, int z) {
		return this.get(x, y, z).getBlock();
	}

	/**+
	 * Returns the metadata associated with the block at the given
	 * coordinates in this ExtendedBlockStorage.
	 */
	public int getExtBlockMetadata(int x, int y, int z) {
		IBlockState iblockstate = this.get(x, y, z);
		return iblockstate.getBlock().getMetaFromState(iblockstate);
	}

	/**+
	 * Returns whether or not this block storage's Chunk is fully
	 * empty, based on its internal reference count.
	 */
	public boolean isEmpty() {
		if (blockRefCount != 0)
			return false;

		// -1 indicates the lightRefCount needs to be re-calculated
		if (alfheim$lightRefCount == -1) {
			if (alfheim$checkLightArrayEqual(skylightArray, (byte) 255)
					&& alfheim$checkLightArrayEqual(blocklightArray, (byte) 0))
				alfheim$lightRefCount = 0; // Lighting is trivial, don't send to clients
			else
				alfheim$lightRefCount = 1; // Lighting is not trivial, send to clients
		}

		return alfheim$lightRefCount == 0;
	}

	/**+
	 * Returns whether or not this block storage's Chunk will
	 * require random ticking, used to avoid looping through random
	 * block ticks when there are no blocks that would randomly
	 * tick.
	 */
	public boolean getNeedsRandomTick() {
		return this.tickRefCount > 0;
	}

	/**+
	 * Returns the Y location of this ExtendedBlockStorage.
	 */
	public int getYLocation() {
		return this.yBase;
	}

	/**+
	 * Sets the saved Sky-light value in the extended block storage
	 * structure.
	 */
	public void setExtSkylightValue(int x, int y, int z, int value) {
		this.skylightArray.set(x, y, z, value);
		alfheim$lightRefCount = -1;
	}

	/**+
	 * Gets the saved Sky-light value in the extended block storage
	 * structure.
	 */
	public int getExtSkylightValue(int x, int y, int z) {
		return this.skylightArray.get(x, y, z);
	}

	/**+
	 * Sets the saved Block-light value in the extended block
	 * storage structure.
	 */
	public void setExtBlocklightValue(int x, int y, int z, int value) {
		this.blocklightArray.set(x, y, z, value);
		alfheim$lightRefCount = -1;
	}

	/**+
	 * Gets the saved Block-light value in the extended block
	 * storage structure.
	 */
	public int getExtBlocklightValue(int x, int y, int z) {
		return this.blocklightArray.get(x, y, z);
	}

	public void removeInvalidBlocks() {
		this.blockRefCount = 0;
		this.tickRefCount = 0;

		for (int i = 0; i < 16; ++i) {
			for (int j = 0; j < 16; ++j) {
				for (int k = 0; k < 16; ++k) {
					Block block = this.getBlockByExtId(i, j, k);
					if (block != Blocks.air) {
						++this.blockRefCount;
						if (block.getTickRandomly()) {
							++this.tickRefCount;
						}
					}
				}
			}
		}

	}

	public char[] getData() {
		return this.data;
	}

	public void setData(char[] dataArray) {
		this.data = dataArray;
	}

	/**+
	 * Returns the NibbleArray instance containing Block-light data.
	 */
	public NibbleArray getBlocklightArray() {
		return this.blocklightArray;
	}

	/**+
	 * Returns the NibbleArray instance containing Sky-light data.
	 */
	public NibbleArray getSkylightArray() {
		return this.skylightArray;
	}

	/**+
	 * Sets the NibbleArray instance used for Block-light values in
	 * this particular storage block.
	 */
	public void setBlocklightArray(NibbleArray newBlocklightArray) {
		this.blocklightArray = newBlocklightArray;
		alfheim$lightRefCount = -1;
	}

	/**+
	 * Sets the NibbleArray instance used for Sky-light values in
	 * this particular storage block.
	 */
	public void setSkylightArray(NibbleArray newSkylightArray) {
		this.skylightArray = newSkylightArray;
		alfheim$lightRefCount = -1;
	}

	private boolean alfheim$checkLightArrayEqual(final NibbleArray storage, final byte targetValue) {
		if (storage == null)
			return true;

		for (final byte currentByte : storage.getData())
			if (currentByte != targetValue)
				return false;

		return true;
	}
}