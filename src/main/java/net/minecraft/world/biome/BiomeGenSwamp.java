package net.minecraft.world.biome;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.BlockFlower;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.BlockPos;

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
public class BiomeGenSwamp extends BiomeGenBase {
	protected BiomeGenSwamp(int parInt1) {
		super(parInt1);
		this.waterColorMultiplier = 14745518;
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 1, 1, 1));
	}

	public int getGrassColorAtPos(BlockPos blockpos) {
		double d0 = GRASS_COLOR_NOISE.func_151601_a((double) blockpos.getX() * 0.0225D,
				(double) blockpos.getZ() * 0.0225D);
		return d0 < -0.1D ? 5011004 : 6975545;
	}

	public int getFoliageColorAtPos(BlockPos var1) {
		return 6975545;
	}

	public BlockFlower.EnumFlowerType pickRandomFlower(EaglercraftRandom var1, BlockPos var2) {
		return BlockFlower.EnumFlowerType.BLUE_ORCHID;
	}
}