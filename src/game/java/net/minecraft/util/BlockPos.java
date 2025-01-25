package net.minecraft.util;

import java.util.Iterator;

import com.google.common.collect.AbstractIterator;

import net.minecraft.entity.Entity;

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
public class BlockPos extends Vec3i {
	/**+
	 * The BlockPos with all coordinates 0
	 */
	public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
	public static final int NUM_X_BITS = 26;
	public static final int NUM_Z_BITS = NUM_X_BITS;
	public static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
	public static final int Y_SHIFT = 0 + NUM_Z_BITS;
	public static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
	public static final long X_MASK = (1L << NUM_X_BITS) - 1L;
	public static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
	public static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

	public BlockPos() {
		super(0, 0, 0);
	}

	public BlockPos(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockPos(double x, double y, double z) {
		super(x, y, z);
	}

	public BlockPos(Entity source) {
		this(source.posX, source.posY, source.posZ);
	}

	public BlockPos(Vec3 source) {
		this(source.xCoord, source.yCoord, source.zCoord);
	}

	public BlockPos(Vec3i source) {
		this(source.x, source.y, source.z);
	}

	/**+
	 * Add the given coordinates to the coordinates of this BlockPos
	 */
	public BlockPos add(double x, double y, double z) {
		return x == 0.0D && y == 0.0D && z == 0.0D ? this
				: new BlockPos((double) this.x + x, (double) this.y + y, (double) this.z + z);
	}

	/**+
	 * Add the given coordinates to the coordinates of this BlockPos
	 */
	public BlockPos add(int x, int y, int z) {
		return x == 0 && y == 0 && z == 0 ? this : new BlockPos(this.x + x, this.y + y, this.z + z);
	}

	/**
	 * eagler
	 */
	/**+
	 * Add the given coordinates to the coordinates of this BlockPos
	 */
	public BlockPos add(int x, int y, int z, BlockPos dst) {
		dst.x = this.x + x;
		dst.y = this.y + y;
		dst.z = this.z + z;
		return dst;
	}

	/**+
	 * Add the given coordinates to the coordinates of this BlockPos
	 */
	public BlockPos add(Vec3i vec) {
		return vec.x == 0 && vec.y == 0 && vec.z == 0 ? this
				: new BlockPos(this.x + vec.x, this.y + vec.y, this.z + vec.z);
	}

	/**+
	 * Subtract the given Vector from this BlockPos
	 */
	public BlockPos subtract(Vec3i vec) {
		return vec.x == 0 && vec.y == 0 && vec.z == 0 ? this
				: new BlockPos(this.x - vec.x, this.y - vec.y, this.z - vec.z);
	}

	/**+
	 * Offset this BlockPos 1 block up
	 */
	public BlockPos up() {
		return this.up(1);
	}

	/**
	 * eagler
	 */
	/**+
	 * Offset this BlockPos 1 block up
	 */
	public BlockPos up(BlockPos dst) {
		dst.x = x;
		dst.y = y + 1;
		dst.z = z;
		return dst;
	}

	/**+
	 * Offset this BlockPos 1 block up
	 */
	public BlockPos up(int n) {
		return this.offset(EnumFacing.UP, n);
	}

	/**+
	 * Offset this BlockPos 1 block down
	 */
	public BlockPos down() {
		return this.down(1);
	}

	/**
	 * eagler
	 */
	/**+
	 * Offset this BlockPos 1 block down
	 */
	public BlockPos down(BlockPos dst) {
		dst.x = x;
		dst.y = y - 1;
		dst.z = z;
		return dst;
	}

	/**+
	 * Offset this BlockPos 1 block down
	 */
	public BlockPos down(int n) {
		return this.offset(EnumFacing.DOWN, n);
	}

	/**+
	 * Offset this BlockPos 1 block in northern direction
	 */
	public BlockPos north() {
		return this.north(1);
	}

	/**
	 * eagler
	 */
	/**+
	 * Offset this BlockPos 1 block in northern direction
	 */
	public BlockPos north(BlockPos dst) {
		dst.x = x;
		dst.y = y;
		dst.z = z - 1;
		return dst;
	}

	/**+
	 * Offset this BlockPos 1 block in northern direction
	 */
	public BlockPos north(int n) {
		return this.offset(EnumFacing.NORTH, n);
	}

	/**+
	 * Offset this BlockPos 1 block in southern direction
	 */
	public BlockPos south() {
		return this.south(1);
	}

	/**
	 * eagler
	 */
	/**+
	 * Offset this BlockPos 1 block in southern direction
	 */
	public BlockPos south(BlockPos dst) {
		dst.x = x;
		dst.y = y;
		dst.z = z + 1;
		return dst;
	}

	/**+
	 * Offset this BlockPos 1 block in southern direction
	 */
	public BlockPos south(int n) {
		return this.offset(EnumFacing.SOUTH, n);
	}

	/**+
	 * Offset this BlockPos 1 block in western direction
	 */
	public BlockPos west() {
		return this.west(1);
	}

	/**+
	 * Offset this BlockPos 1 block in western direction
	 */
	public BlockPos west(int n) {
		return this.offset(EnumFacing.WEST, n);
	}

	/**
	 * eagler
	 */
	/**+
	 * Offset this BlockPos 1 block in western direction
	 */
	public BlockPos west(BlockPos dst) {
		dst.x = x - 1;
		dst.y = y;
		dst.z = z;
		return dst;
	}

	/**+
	 * Offset this BlockPos 1 block in eastern direction
	 */
	public BlockPos east() {
		return this.east(1);
	}

	/**+
	 * Offset this BlockPos 1 block in eastern direction
	 */
	public BlockPos east(int n) {
		return this.offset(EnumFacing.EAST, n);
	}

	/**
	 * eagler
	 */
	/**+
	 * Offset this BlockPos 1 block in eastern direction
	 */
	public BlockPos east(BlockPos dst) {
		dst.x = x + 1;
		dst.y = y;
		dst.z = z;
		return dst;
	}

	/**+
	 * Offset this BlockPos 1 block in the given direction
	 */
	public BlockPos offset(EnumFacing facing) {
		return this.offset(facing, 1);
	}

	public BlockPos offsetFaster(EnumFacing facing, BlockPos ret) {
		ret.x = this.x + facing.getFrontOffsetX();
		ret.y = this.y + facing.getFrontOffsetY();
		ret.z = this.z + facing.getFrontOffsetZ();
		return ret;
	}

	/**
	 * only use with a regular "net.minecraft.util.BlockPos"!
	 */
	public BlockPos offsetEvenFaster(EnumFacing facing, BlockPos ret) {
		ret.x = this.x + facing.getFrontOffsetX();
		ret.y = this.y + facing.getFrontOffsetY();
		ret.z = this.z + facing.getFrontOffsetZ();
		return ret;
	}

	/**+
	 * Offset this BlockPos 1 block in the given direction
	 */
	public BlockPos offset(EnumFacing facing, int n) {
		return n == 0 ? this
				: new BlockPos(this.x + facing.getFrontOffsetX() * n, this.y + facing.getFrontOffsetY() * n,
						this.z + facing.getFrontOffsetZ() * n);
	}

	/**+
	 * Calculate the cross product of this and the given Vector
	 */
	public BlockPos crossProduct(Vec3i vec3i) {
		return new BlockPos(this.y * vec3i.z - this.z * vec3i.y, this.z * vec3i.x - this.x * vec3i.z,
				this.x * vec3i.y - this.y * vec3i.x);
	}

	/**+
	 * Serialize this BlockPos into a long value
	 */
	public long toLong() {
		return ((long) this.x & X_MASK) << X_SHIFT | ((long) this.y & Y_MASK) << Y_SHIFT
				| ((long) this.z & Z_MASK) << 0;
	}

	/**+
	 * Create a BlockPos from a serialized long value (created by
	 * toLong)
	 */
	public static BlockPos fromLong(long serialized) {
		int i = (int) (serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
		int j = (int) (serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
		int k = (int) (serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
		return new BlockPos(i, j, k);
	}

	/**+
	 * Create an Iterable that returns all positions in the box
	 * specified by the given corners
	 */
	public static Iterable<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
		final BlockPos blockpos = new BlockPos(Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z));
		final BlockPos blockpos1 = new BlockPos(Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z));
		return new Iterable<BlockPos>() {
			public Iterator<BlockPos> iterator() {
				return new AbstractIterator<BlockPos>() {
					private BlockPos lastReturned = null;

					protected BlockPos computeNext() {
						if (this.lastReturned == null) {
							this.lastReturned = blockpos;
							return this.lastReturned;
						} else if (this.lastReturned.equals(blockpos1)) {
							return (BlockPos) this.endOfData();
						} else {
							int i = this.lastReturned.x;
							int j = this.lastReturned.y;
							int k = this.lastReturned.z;
							if (i < blockpos1.x) {
								++i;
							} else if (j < blockpos1.y) {
								i = blockpos.x;
								++j;
							} else if (k < blockpos1.z) {
								i = blockpos.x;
								j = blockpos.y;
								++k;
							}

							this.lastReturned = new BlockPos(i, j, k);
							return this.lastReturned;
						}
					}
				};
			}
		};
	}

	/**+
	 * Like getAllInBox but reuses a single MutableBlockPos instead.
	 * If this method is used, the resulting BlockPos instances can
	 * only be used inside the iteration loop.
	 */
	public static Iterable<BlockPos> getAllInBoxMutable(BlockPos from, BlockPos to) {
		final BlockPos blockpos = new BlockPos(Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z));
		final BlockPos blockpos1 = new BlockPos(Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z));
		return new Iterable<BlockPos>() {
			public Iterator<BlockPos> iterator() {
				return new AbstractIterator<BlockPos>() {
					private BlockPos theBlockPos = null;

					protected BlockPos computeNext() {
						if (this.theBlockPos == null) {
							this.theBlockPos = new BlockPos(blockpos.x, blockpos.y, blockpos.z);
							return this.theBlockPos;
						} else if (this.theBlockPos.equals(blockpos1)) {
							return (BlockPos) this.endOfData();
						} else {
							int i = this.theBlockPos.x;
							int j = this.theBlockPos.y;
							int k = this.theBlockPos.z;
							if (i < blockpos1.x) {
								++i;
							} else if (j < blockpos1.y) {
								i = blockpos.x;
								++j;
							} else if (k < blockpos1.z) {
								i = blockpos.x;
								j = blockpos.y;
								++k;
							}

							this.theBlockPos.x = i;
							this.theBlockPos.y = j;
							this.theBlockPos.z = k;
							return this.theBlockPos;
						}
					}
				};
			}
		};
	}

	public BlockPos func_181079_c(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

}