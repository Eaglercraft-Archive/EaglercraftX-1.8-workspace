package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

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
public class ChunkProviderDebug implements IChunkProvider {
	private static final List<IBlockState> field_177464_a = Lists.newArrayList();
	private static final int field_177462_b;
	private static final int field_181039_c;
	private final World world;

	public ChunkProviderDebug(World worldIn) {
		this.world = worldIn;
	}

	/**+
	 * Will return back a chunk, if it doesn't exist and its not a
	 * MP client it will generates all the blocks for the specified
	 * chunk from the map seed and chunk seed
	 */
	public Chunk provideChunk(int i, int j) {
		ChunkPrimer chunkprimer = new ChunkPrimer();

		for (int k = 0; k < 16; ++k) {
			for (int l = 0; l < 16; ++l) {
				int i1 = i * 16 + k;
				int j1 = j * 16 + l;
				chunkprimer.setBlockState(k, 60, l, Blocks.barrier.getDefaultState());
				IBlockState iblockstate = func_177461_b(i1, j1);
				if (iblockstate != null) {
					chunkprimer.setBlockState(k, 70, l, iblockstate);
				}
			}
		}

		Chunk chunk = new Chunk(this.world, chunkprimer, i, j);
		chunk.generateSkylightMap();
		BiomeGenBase[] abiomegenbase = this.world.getWorldChunkManager().loadBlockGeneratorData((BiomeGenBase[]) null,
				i * 16, j * 16, 16, 16);
		byte[] abyte = chunk.getBiomeArray();

		for (int k1 = 0; k1 < abyte.length; ++k1) {
			abyte[k1] = (byte) abiomegenbase[k1].biomeID;
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	public static IBlockState func_177461_b(int parInt1, int parInt2) {
		IBlockState iblockstate = null;
		if (parInt1 > 0 && parInt2 > 0 && parInt1 % 2 != 0 && parInt2 % 2 != 0) {
			parInt1 = parInt1 / 2;
			parInt2 = parInt2 / 2;
			if (parInt1 <= field_177462_b && parInt2 <= field_181039_c) {
				int i = MathHelper.abs_int(parInt1 * field_177462_b + parInt2);
				if (i < field_177464_a.size()) {
					iblockstate = (IBlockState) field_177464_a.get(i);
				}
			}
		}

		return iblockstate;
	}

	/**+
	 * Checks to see if a chunk exists at x, z
	 */
	public boolean chunkExists(int var1, int var2) {
		return true;
	}

	/**+
	 * Populates chunk with ores etc etc
	 */
	public void populate(IChunkProvider var1, int var2, int var3) {
	}

	public boolean func_177460_a(IChunkProvider var1, Chunk var2, int var3, int var4) {
		return false;
	}

	/**+
	 * Two modes of operation: if passed true, save all Chunks in
	 * one go. If passed false, save up to two chunks. Return true
	 * if all chunks have been saved.
	 */
	public boolean saveChunks(boolean var1, IProgressUpdate var2) {
		return true;
	}

	/**+
	 * Save extra data not associated with any Chunk. Not saved
	 * during autosave, only during world unload. Currently
	 * unimplemented.
	 */
	public void saveExtraData() {
	}

	/**+
	 * Unloads chunks that are marked to be unloaded. This is not
	 * guaranteed to unload every such chunk.
	 */
	public boolean unloadQueuedChunks() {
		return false;
	}

	/**+
	 * Returns if the IChunkProvider supports saving.
	 */
	public boolean canSave() {
		return true;
	}

	/**+
	 * Converts the instance data to a readable string.
	 */
	public String makeString() {
		return "DebugLevelSource";
	}

	public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype,
			BlockPos blockpos) {
		BiomeGenBase biomegenbase = this.world.getBiomeGenForCoords(blockpos);
		return biomegenbase.getSpawnableList(enumcreaturetype);
	}

	public BlockPos getStrongholdGen(World var1, String var2, BlockPos var3) {
		return null;
	}

	public int getLoadedChunkCount() {
		return 0;
	}

	public void recreateStructures(Chunk var1, int var2, int var3) {
	}

	/**+
	 * Will return back a chunk, if it doesn't exist and its not a
	 * MP client it will generates all the blocks for the specified
	 * chunk from the map seed and chunk seed
	 */
	public Chunk provideChunk(BlockPos blockpos) {
		return this.provideChunk(blockpos.getX() >> 4, blockpos.getZ() >> 4);
	}

	static {
		for (Block block : Block.blockRegistry) {
			field_177464_a.addAll(block.getBlockState().getValidStates());
		}

		field_177462_b = MathHelper.ceiling_float_int(MathHelper.sqrt_float((float) field_177464_a.size()));
		field_181039_c = MathHelper.ceiling_float_int((float) field_177464_a.size() / (float) field_177462_b);
	}

	public Chunk getLoadedChunk(int var1, int var2) {
		/**+
		 * Will return back a chunk, if it doesn't exist and its not a
		 * MP client it will generates all the blocks for the specified
		 * chunk from the map seed and chunk seed
		 */
		return provideChunk(var1, var2);
	}
}