package net.minecraft.block.state.pattern;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;

import net.lax1dude.eaglercraft.v1_8.cache.EaglerCacheProvider;
import net.lax1dude.eaglercraft.v1_8.cache.EaglerLoadingCache;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

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
public class BlockPattern {
	private final Predicate<BlockWorldState>[][][] blockMatches;
	private final int fingerLength;
	private final int thumbLength;
	private final int palmLength;

	public BlockPattern(Predicate<BlockWorldState>[][][] predicatesIn) {
		this.blockMatches = predicatesIn;
		this.fingerLength = predicatesIn.length;
		if (this.fingerLength > 0) {
			this.thumbLength = predicatesIn[0].length;
			if (this.thumbLength > 0) {
				this.palmLength = predicatesIn[0][0].length;
			} else {
				this.palmLength = 0;
			}
		} else {
			this.thumbLength = 0;
			this.palmLength = 0;
		}

	}

	public int getThumbLength() {
		return this.thumbLength;
	}

	public int getPalmLength() {
		return this.palmLength;
	}

	/**+
	 * checks that the given pattern & rotation is at the block
	 * co-ordinates.
	 */
	private BlockPattern.PatternHelper checkPatternAt(BlockPos pos, EnumFacing finger, EnumFacing thumb,
			EaglerLoadingCache<BlockPos, BlockWorldState> lcache) {
		for (int i = 0; i < this.palmLength; ++i) {
			for (int j = 0; j < this.thumbLength; ++j) {
				for (int k = 0; k < this.fingerLength; ++k) {
					if (!this.blockMatches[k][j][i].apply(lcache.get(translateOffset(pos, finger, thumb, i, j, k)))) {
						return null;
					}
				}
			}
		}

		return new BlockPattern.PatternHelper(pos, finger, thumb, lcache, this.palmLength, this.thumbLength,
				this.fingerLength);
	}

	/**+
	 * Calculates whether the given world position matches the
	 * pattern. Warning, fairly heavy function. @return a
	 * BlockPattern.PatternHelper if found, null otherwise.
	 */
	public BlockPattern.PatternHelper match(World worldIn, BlockPos pos) {
		EaglerLoadingCache loadingcache = func_181627_a(worldIn, false);
		int i = Math.max(Math.max(this.palmLength, this.thumbLength), this.fingerLength);

		for (BlockPos blockpos : BlockPos.getAllInBox(pos, pos.add(i - 1, i - 1, i - 1))) {
			EnumFacing[] facings = EnumFacing._VALUES;
			for (int j = 0; j < facings.length; ++j) {
				EnumFacing enumfacing = facings[j];
				for (int k = 0; k < facings.length; ++k) {
					EnumFacing enumfacing1 = facings[k];
					if (enumfacing1 != enumfacing && enumfacing1 != enumfacing.getOpposite()) {
						BlockPattern.PatternHelper blockpattern$patternhelper = this.checkPatternAt(blockpos,
								enumfacing, enumfacing1, loadingcache);
						if (blockpattern$patternhelper != null) {
							return blockpattern$patternhelper;
						}
					}
				}
			}
		}

		return null;
	}

	public static EaglerLoadingCache<BlockPos, BlockWorldState> func_181627_a(World parWorld, boolean parFlag) {
		return new EaglerLoadingCache<BlockPos, BlockWorldState>(new BlockPattern.CacheLoader(parWorld, parFlag));
	}

	/**+
	 * Offsets the position of pos in the direction of finger and
	 * thumb facing by offset amounts, follows the right-hand rule
	 * for cross products (finger, thumb, palm) @return A new
	 * BlockPos offset in the facing directions
	 */
	protected static BlockPos translateOffset(BlockPos pos, EnumFacing finger, EnumFacing thumb, int palmOffset,
			int thumbOffset, int fingerOffset) {
		if (finger != thumb && finger != thumb.getOpposite()) {
			Vec3i vec3i = new Vec3i(finger.getFrontOffsetX(), finger.getFrontOffsetY(), finger.getFrontOffsetZ());
			Vec3i vec3i1 = new Vec3i(thumb.getFrontOffsetX(), thumb.getFrontOffsetY(), thumb.getFrontOffsetZ());
			Vec3i vec3i2 = vec3i.crossProduct(vec3i1);
			return pos.add(vec3i1.getX() * -thumbOffset + vec3i2.getX() * palmOffset + vec3i.getX() * fingerOffset,
					vec3i1.getY() * -thumbOffset + vec3i2.getY() * palmOffset + vec3i.getY() * fingerOffset,
					vec3i1.getZ() * -thumbOffset + vec3i2.getZ() * palmOffset + vec3i.getZ() * fingerOffset);
		} else {
			throw new IllegalArgumentException("Invalid forwards & up combination");
		}
	}

	static class CacheLoader implements EaglerCacheProvider<BlockPos, BlockWorldState> {
		private final World world;
		private final boolean field_181626_b;

		public CacheLoader(World parWorld, boolean parFlag) {
			this.world = parWorld;
			this.field_181626_b = parFlag;
		}

		public BlockWorldState create(BlockPos parBlockPos) {
			return new BlockWorldState(this.world, parBlockPos, this.field_181626_b);
		}
	}

	public static class PatternHelper {
		private final BlockPos pos;
		private final EnumFacing finger;
		private final EnumFacing thumb;
		private final EaglerLoadingCache<BlockPos, BlockWorldState> lcache;
		private final int field_181120_e;
		private final int field_181121_f;
		private final int field_181122_g;

		public PatternHelper(BlockPos parBlockPos, EnumFacing parEnumFacing, EnumFacing parEnumFacing2,
				EaglerLoadingCache<BlockPos, BlockWorldState> parLoadingCache, int parInt1, int parInt2, int parInt3) {
			this.pos = parBlockPos;
			this.finger = parEnumFacing;
			this.thumb = parEnumFacing2;
			this.lcache = parLoadingCache;
			this.field_181120_e = parInt1;
			this.field_181121_f = parInt2;
			this.field_181122_g = parInt3;
		}

		public BlockPos func_181117_a() {
			return this.pos;
		}

		public EnumFacing getFinger() {
			return this.finger;
		}

		public EnumFacing getThumb() {
			return this.thumb;
		}

		public int func_181118_d() {
			return this.field_181120_e;
		}

		public int func_181119_e() {
			return this.field_181121_f;
		}

		/**+
		 * Offsets the position of pos in the direction of finger and
		 * thumb facing by offset amounts, follows the right-hand rule
		 * for cross products (finger, thumb, palm) @return A new
		 * BlockPos offset in the facing directions
		 */
		public BlockWorldState translateOffset(int palmOffset, int thumbOffset, int fingerOffset) {
			return (BlockWorldState) this.lcache.get(BlockPattern.translateOffset(this.pos, this.getFinger(),
					this.getThumb(), palmOffset, thumbOffset, fingerOffset));
		}

		public String toString() {
			return Objects.toStringHelper(this).add("up", this.thumb).add("forwards", this.finger)
					.add("frontTopLeft", this.pos).toString();
		}
	}
}