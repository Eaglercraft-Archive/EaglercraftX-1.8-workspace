package net.minecraft.world.gen.structure;

import com.google.common.base.Objects;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

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
public class StructureBoundingBox {
	public int minX;
	public int minY;
	public int minZ;
	public int maxX;
	public int maxY;
	public int maxZ;

	public StructureBoundingBox() {
	}

	public StructureBoundingBox(int[] coords) {
		if (coords.length == 6) {
			this.minX = coords[0];
			this.minY = coords[1];
			this.minZ = coords[2];
			this.maxX = coords[3];
			this.maxY = coords[4];
			this.maxZ = coords[5];
		}

	}

	/**+
	 * returns a new StructureBoundingBox with MAX values
	 */
	public static StructureBoundingBox getNewBoundingBox() {
		return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE,
				Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	/**+
	 * Create a bounding box with the specified dimensions and
	 * rotate it. Used to project a possible new component Bounding
	 * Box - to check if it would cut anything already spawned
	 */
	public static StructureBoundingBox getComponentToAddBoundingBox(int parInt1, int parInt2, int parInt3, int parInt4,
			int parInt5, int parInt6, int parInt7, int parInt8, int parInt9, EnumFacing parEnumFacing) {
		switch (parEnumFacing) {
		case NORTH:
			return new StructureBoundingBox(parInt1 + parInt4, parInt2 + parInt5, parInt3 - parInt9 + 1 + parInt6,
					parInt1 + parInt7 - 1 + parInt4, parInt2 + parInt8 - 1 + parInt5, parInt3 + parInt6);
		case SOUTH:
			return new StructureBoundingBox(parInt1 + parInt4, parInt2 + parInt5, parInt3 + parInt6,
					parInt1 + parInt7 - 1 + parInt4, parInt2 + parInt8 - 1 + parInt5, parInt3 + parInt9 - 1 + parInt6);
		case WEST:
			return new StructureBoundingBox(parInt1 - parInt9 + 1 + parInt6, parInt2 + parInt5, parInt3 + parInt4,
					parInt1 + parInt6, parInt2 + parInt8 - 1 + parInt5, parInt3 + parInt7 - 1 + parInt4);
		case EAST:
			return new StructureBoundingBox(parInt1 + parInt6, parInt2 + parInt5, parInt3 + parInt4,
					parInt1 + parInt9 - 1 + parInt6, parInt2 + parInt8 - 1 + parInt5, parInt3 + parInt7 - 1 + parInt4);
		default:
			return new StructureBoundingBox(parInt1 + parInt4, parInt2 + parInt5, parInt3 + parInt6,
					parInt1 + parInt7 - 1 + parInt4, parInt2 + parInt8 - 1 + parInt5, parInt3 + parInt9 - 1 + parInt6);
		}
	}

	public static StructureBoundingBox func_175899_a(int parInt1, int parInt2, int parInt3, int parInt4, int parInt5,
			int parInt6) {
		return new StructureBoundingBox(Math.min(parInt1, parInt4), Math.min(parInt2, parInt5),
				Math.min(parInt3, parInt6), Math.max(parInt1, parInt4), Math.max(parInt2, parInt5),
				Math.max(parInt3, parInt6));
	}

	public StructureBoundingBox(StructureBoundingBox structurebb) {
		this.minX = structurebb.minX;
		this.minY = structurebb.minY;
		this.minZ = structurebb.minZ;
		this.maxX = structurebb.maxX;
		this.maxY = structurebb.maxY;
		this.maxZ = structurebb.maxZ;
	}

	public StructureBoundingBox(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
		this.minX = xMin;
		this.minY = yMin;
		this.minZ = zMin;
		this.maxX = xMax;
		this.maxY = yMax;
		this.maxZ = zMax;
	}

	public StructureBoundingBox(Vec3i vec1, Vec3i vec2) {
		this.minX = Math.min(vec1.getX(), vec2.getX());
		this.minY = Math.min(vec1.getY(), vec2.getY());
		this.minZ = Math.min(vec1.getZ(), vec2.getZ());
		this.maxX = Math.max(vec1.getX(), vec2.getX());
		this.maxY = Math.max(vec1.getY(), vec2.getY());
		this.maxZ = Math.max(vec1.getZ(), vec2.getZ());
	}

	public StructureBoundingBox(int xMin, int zMin, int xMax, int zMax) {
		this.minX = xMin;
		this.minZ = zMin;
		this.maxX = xMax;
		this.maxZ = zMax;
		this.minY = 1;
		this.maxY = 512;
	}

	/**+
	 * Discover if a coordinate is inside the bounding box area.
	 */
	public boolean intersectsWith(StructureBoundingBox structurebb) {
		return this.maxX >= structurebb.minX && this.minX <= structurebb.maxX && this.maxZ >= structurebb.minZ
				&& this.minZ <= structurebb.maxZ && this.maxY >= structurebb.minY && this.minY <= structurebb.maxY;
	}

	/**+
	 * Discover if a coordinate is inside the bounding box area.
	 */
	public boolean intersectsWith(int minXIn, int minZIn, int maxXIn, int maxZIn) {
		return this.maxX >= minXIn && this.minX <= maxXIn && this.maxZ >= minZIn && this.minZ <= maxZIn;
	}

	/**+
	 * Expands a bounding box's dimensions to include the supplied
	 * bounding box.
	 */
	public void expandTo(StructureBoundingBox sbb) {
		this.minX = Math.min(this.minX, sbb.minX);
		this.minY = Math.min(this.minY, sbb.minY);
		this.minZ = Math.min(this.minZ, sbb.minZ);
		this.maxX = Math.max(this.maxX, sbb.maxX);
		this.maxY = Math.max(this.maxY, sbb.maxY);
		this.maxZ = Math.max(this.maxZ, sbb.maxZ);
	}

	/**+
	 * Offsets the current bounding box by the specified
	 * coordinates. Args: x, y, z
	 */
	public void offset(int x, int y, int z) {
		this.minX += x;
		this.minY += y;
		this.minZ += z;
		this.maxX += x;
		this.maxY += y;
		this.maxZ += z;
	}

	/**+
	 * Checks if given Vec3i is inside of StructureBoundingBox
	 */
	public boolean isVecInside(Vec3i vec) {
		return vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ
				&& vec.getY() >= this.minY && vec.getY() <= this.maxY;
	}

	public Vec3i func_175896_b() {
		return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
	}

	/**+
	 * Get dimension of the bounding box in the x direction.
	 */
	public int getXSize() {
		return this.maxX - this.minX + 1;
	}

	/**+
	 * Get dimension of the bounding box in the y direction.
	 */
	public int getYSize() {
		return this.maxY - this.minY + 1;
	}

	/**+
	 * Get dimension of the bounding box in the z direction.
	 */
	public int getZSize() {
		return this.maxZ - this.minZ + 1;
	}

	public Vec3i getCenter() {
		return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2,
				this.minZ + (this.maxZ - this.minZ + 1) / 2);
	}

	public String toString() {
		return Objects.toStringHelper(this).add("x0", this.minX).add("y0", this.minY).add("z0", this.minZ)
				.add("x1", this.maxX).add("y1", this.maxY).add("z1", this.maxZ).toString();
	}

	public NBTTagIntArray toNBTTagIntArray() {
		return new NBTTagIntArray(new int[] { this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ });
	}
}