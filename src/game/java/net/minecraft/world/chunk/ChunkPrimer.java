package net.minecraft.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

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
public class ChunkPrimer {
	private final short[] data = new short[65536];
	private final IBlockState defaultState = Blocks.air.getDefaultState();

	public IBlockState getBlockState(int x, int y, int z) {
		int i = x << 12 | z << 8 | y;
		return this.getBlockState(i);
	}

	public IBlockState getBlockState(int index) {
		if (index >= 0 && index < this.data.length) {
			IBlockState iblockstate = (IBlockState) Block.BLOCK_STATE_IDS.getByValue(this.data[index]);
			return iblockstate != null ? iblockstate : this.defaultState;
		} else {
			throw new IndexOutOfBoundsException("The coordinate is out of range");
		}
	}

	public void setBlockState(int x, int y, int z, IBlockState state) {
		int i = x << 12 | z << 8 | y;
		this.setBlockState(i, state);
	}

	public void setBlockState(int index, IBlockState state) {
		if (index >= 0 && index < this.data.length) {
			this.data[index] = (short) Block.BLOCK_STATE_IDS.get(state);
		} else {
			throw new IndexOutOfBoundsException("The coordinate is out of range");
		}
	}
}