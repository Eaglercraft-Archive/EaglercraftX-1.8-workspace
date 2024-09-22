package net.minecraft.client.renderer;

import java.util.Arrays;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

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
public class RegionRenderCache extends ChunkCache {
	private final IBlockState DEFAULT_STATE = Blocks.air.getDefaultState();
	private final BlockPos position;
	private int[] combinedLights;
	private IBlockState[] blockStates;

	public RegionRenderCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn) {
		super(worldIn, posFromIn, posToIn, subIn);
		this.position = posFromIn.subtract(new Vec3i(subIn, subIn, subIn));
		boolean flag = true;
		this.combinedLights = new int[8000];
		Arrays.fill(this.combinedLights, -1);
		this.blockStates = new IBlockState[8000];
	}

	public TileEntity getTileEntity(BlockPos blockpos) {
		int i = (blockpos.getX() >> 4) - this.chunkX;
		int j = (blockpos.getZ() >> 4) - this.chunkZ;
		return this.chunkArray[i][j].getTileEntity(blockpos, Chunk.EnumCreateEntityType.QUEUED);
	}

	public int getCombinedLight(BlockPos blockpos, int i) {
		int j = this.getPositionIndex(blockpos);
		int k = this.combinedLights[j];
		if (k == -1) {
			k = super.getCombinedLight(blockpos, i);
			this.combinedLights[j] = k;
		}

		return k;
	}

	public IBlockState getBlockState(BlockPos blockpos) {
		int i = this.getPositionIndex(blockpos);
		IBlockState iblockstate = this.blockStates[i];
		if (iblockstate == null) {
			iblockstate = this.getBlockStateRaw(blockpos);
			this.blockStates[i] = iblockstate;
		}

		return iblockstate;
	}

	/**
	 * only use with a regular "net.minecraft.util.BlockPos"!
	 */
	public IBlockState getBlockStateFaster(BlockPos blockpos) {
		int i = this.getPositionIndexFaster(blockpos);
		IBlockState iblockstate = this.blockStates[i];
		if (iblockstate == null) {
			iblockstate = this.getBlockStateRawFaster(blockpos);
			this.blockStates[i] = iblockstate;
		}

		return iblockstate;
	}

	private IBlockState getBlockStateRaw(BlockPos pos) {
		if (pos.getY() >= 0 && pos.getY() < 256) {
			int i = (pos.getX() >> 4) - this.chunkX;
			int j = (pos.getZ() >> 4) - this.chunkZ;
			return this.chunkArray[i][j].getBlockState(pos);
		} else {
			return DEFAULT_STATE;
		}
	}

	/**
	 * only use with a regular "net.minecraft.util.BlockPos"!
	 */
	private IBlockState getBlockStateRawFaster(BlockPos pos) {
		if (pos.y >= 0 && pos.y < 256) {
			int i = (pos.x >> 4) - this.chunkX;
			int j = (pos.z >> 4) - this.chunkZ;
			return this.chunkArray[i][j].getBlockState(pos);
		} else {
			return DEFAULT_STATE;
		}
	}

	private int getPositionIndex(BlockPos parBlockPos) {
		int i = parBlockPos.getX() - this.position.getX();
		int j = parBlockPos.getY() - this.position.getY();
		int k = parBlockPos.getZ() - this.position.getZ();
		return i * 400 + k * 20 + j;
	}

	/**
	 * only use with a regular "net.minecraft.util.BlockPos"!
	 */
	private int getPositionIndexFaster(BlockPos parBlockPos) {
		int i = parBlockPos.x - this.position.x;
		int j = parBlockPos.y - this.position.y;
		int k = parBlockPos.z - this.position.z;
		return i * 400 + k * 20 + j;
	}
}