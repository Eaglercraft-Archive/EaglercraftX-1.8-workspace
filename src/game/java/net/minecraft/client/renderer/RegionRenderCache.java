package net.minecraft.client.renderer;

import java.util.Arrays;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

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
public class RegionRenderCache extends ChunkCache {
	private final IBlockState DEFAULT_STATE = Blocks.air.getDefaultState();
	private final BlockPos position;
	private final BlockPos tmpStupid = new BlockPos();
	private static final int[] combinedLights = new int[8000];
	private static final IBlockState[] blockStates = new IBlockState[8000];
	private static final int[] biomeColors = new int[1200];
	private static final int[] biomeColorsBlended = new int[768];

	public RegionRenderCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn) {
		super(worldIn, posFromIn, posToIn, subIn);
		this.position = posFromIn.subtract(new Vec3i(subIn, subIn, subIn));
		Arrays.fill(combinedLights, -1);
		Arrays.fill(blockStates, null);
		Arrays.fill(biomeColors, 0);
		Arrays.fill(biomeColorsBlended, 0);
	}

	public TileEntity getTileEntity(BlockPos blockpos) {
		int i = (blockpos.getX() >> 4) - this.chunkX;
		int j = (blockpos.getZ() >> 4) - this.chunkZ;
		return this.chunkArray[i][j].getTileEntity(blockpos, Chunk.EnumCreateEntityType.QUEUED);
	}

	public int getCombinedLight(BlockPos blockpos, int i) {
		int j = this.getPositionIndex(blockpos);
		int k = combinedLights[j];
		if (k == -1) {
			k = super.getCombinedLight(blockpos, i);
			combinedLights[j] = k;
		}

		return k;
	}

	public IBlockState getBlockState(BlockPos blockpos) {
		int i = this.getPositionIndex(blockpos);
		IBlockState iblockstate = blockStates[i];
		if (iblockstate == null) {
			iblockstate = this.getBlockStateRaw(blockpos);
			blockStates[i] = iblockstate;
		}

		return iblockstate;
	}

	/**
	 * only use with a regular "net.minecraft.util.BlockPos"!
	 */
	public IBlockState getBlockStateFaster(BlockPos blockpos) {
		int i = this.getPositionIndexFaster(blockpos);
		IBlockState iblockstate = blockStates[i];
		if (iblockstate == null) {
			iblockstate = this.getBlockStateRawFaster(blockpos);
			blockStates[i] = iblockstate;
		}

		return iblockstate;
	}

	/**
	 * only use with a regular "net.minecraft.util.BlockPos"!
	 */
	public int getBiomeColorForCoords(BlockPos blockpos, int colorIndex) {
		int i = this.getPositionIndex16Faster(blockpos);
		i += colorIndex * 256;
		int j = biomeColorsBlended[i];
		if (j == 0) {
			j = getBiomeColorBlended(blockpos, colorIndex);
			biomeColorsBlended[i] = j;
		}
		return j;
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

	private int getBiomeColorBlended(BlockPos blockpos, int colorIndex) {
		BlockPos blockpos2 = tmpStupid;
		blockpos2.y = blockpos.y;
		int rad = 1;
		int xmin = blockpos.x - rad;
		int zmin = blockpos.z - rad;
		int xmax = blockpos.x + rad;
		int zmax = blockpos.z + rad;
		int r = 0;
		int g = 0;
		int b = 0;
		int rgb;
		for (int x = xmin; x <= xmax; ++x) {
			for (int z = zmin; z <= zmax; ++z) {
				blockpos2.x = x;
				blockpos2.z = z;
				rgb = getBiomeColorRaw(blockpos2, colorIndex);
				r += (rgb >> 16) & 0xFF;
				g += (rgb >> 8) & 0xFF;
				b += rgb & 0xFF;
			}
		}
		rad = 1 + rad * 2;
		rad *= rad;
		return 0xFF000000 | ((r / rad) << 16) | ((g / rad) << 8) | (b / rad);
	}

	private int getBiomeColorRaw(BlockPos blockpos, int colorIndex) {
		int i = getPositionIndex20Faster(blockpos);
		i += colorIndex * 400;
		int j = biomeColors[i];
		BiomeGenBase biome;
		int ii, jj;
		if (j == 0) {
			if (blockpos.y >= 0 && blockpos.y < 256) {
				ii = (blockpos.x >> 4) - this.chunkX;
				jj = (blockpos.z >> 4) - this.chunkZ;
				biome = this.chunkArray[ii][jj].getBiome(blockpos, worldObj.getWorldChunkManager());
				if (colorIndex == 0) {
					j = biome.getGrassColorAtPos(blockpos);
				} else if (colorIndex == 1) {
					j = biome.getFoliageColorAtPos(blockpos);
				} else {
					j = biome.waterColorMultiplier;
				}
			} else {
				j = 0xFFFFFFFF;
			}
			biomeColors[i] = j;
		}
		return j;
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

	/**
	 * only use with a regular "net.minecraft.util.BlockPos"!
	 */
	private int getPositionIndex16Faster(BlockPos parBlockPos) {
		int i = parBlockPos.x - this.position.x - 2;
		int k = parBlockPos.z - this.position.z - 2;
		return i * 16 + k;
	}

	private int getPositionIndex20Faster(BlockPos parBlockPos) {
		int i = parBlockPos.x - this.position.x;
		int k = parBlockPos.z - this.position.z;
		return i * 20 + k;
	}
}