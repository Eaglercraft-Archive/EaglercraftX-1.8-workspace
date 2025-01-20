package net.minecraft.world.biome;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

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
public class BiomeColorHelper {

	private static final BiomeColorHelper.ColorResolver field_180291_a = new BiomeColorHelper.ColorResolver() {
		public int getColorAtPos(BiomeGenBase blockPosition, BlockPos parBlockPos) {
			return blockPosition.getGrassColorAtPos(parBlockPos);
		}
	};
	private static final BiomeColorHelper.ColorResolver field_180289_b = new BiomeColorHelper.ColorResolver() {
		public int getColorAtPos(BiomeGenBase biomegenbase, BlockPos blockpos) {
			return biomegenbase.getFoliageColorAtPos(blockpos);
		}
	};
	private static final BiomeColorHelper.ColorResolver field_180290_c = new BiomeColorHelper.ColorResolver() {
		public int getColorAtPos(BiomeGenBase biomegenbase, BlockPos var2) {
			return biomegenbase.waterColorMultiplier;
		}
	};

	private static int func_180285_a(IBlockAccess parIBlockAccess, BlockPos parBlockPos,
			BiomeColorHelper.ColorResolver parColorResolver) {
		int i = 0;
		int j = 0;
		int k = 0;

		for (BlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(parBlockPos.add(-1, 0, -1),
				parBlockPos.add(1, 0, 1))) {
			int l = parColorResolver.getColorAtPos(parIBlockAccess.getBiomeGenForCoords(blockpos$mutableblockpos),
					blockpos$mutableblockpos);
			i += (l & 16711680) >> 16;
			j += (l & '\uff00') >> 8;
			k += l & 255;
		}

		return (i / 9 & 255) << 16 | (j / 9 & 255) << 8 | k / 9 & 255;
	}

	public static int getGrassColorAtPos(IBlockAccess parIBlockAccess, BlockPos parBlockPos) {
		return parIBlockAccess.getBiomeColorForCoords(parBlockPos, 0);
	}

	public static int getFoliageColorAtPos(IBlockAccess parIBlockAccess, BlockPos parBlockPos) {
		return parIBlockAccess.getBiomeColorForCoords(parBlockPos, 1);
	}

	public static int getWaterColorAtPos(IBlockAccess parIBlockAccess, BlockPos parBlockPos) {
		return parIBlockAccess.getBiomeColorForCoords(parBlockPos, 2);
	}

	public static int getBiomeColorForCoordsOld(IBlockAccess parIBlockAccess, BlockPos parBlockPos, int index) {
		if (index == 0) {
			return func_180285_a(parIBlockAccess, parBlockPos, field_180291_a);
		} else if (index == 1) {
			return func_180285_a(parIBlockAccess, parBlockPos, field_180289_b);
		} else {
			return func_180285_a(parIBlockAccess, parBlockPos, field_180290_c);
		}
	}

	interface ColorResolver {
		int getColorAtPos(BiomeGenBase var1, BlockPos var2);
	}
}