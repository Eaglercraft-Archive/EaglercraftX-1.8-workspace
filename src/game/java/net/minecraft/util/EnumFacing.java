package net.minecraft.util;

import java.util.Iterator;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

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
public enum EnumFacing implements IStringSerializable {
	DOWN(0, 1, -1, "down", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Y, new Vec3i(0, -1, 0)),
	UP(1, 0, -1, "up", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Y, new Vec3i(0, 1, 0)),
	NORTH(2, 3, 2, "north", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, -1)),
	SOUTH(3, 2, 0, "south", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, 1)),
	WEST(4, 5, 1, "west", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.X, new Vec3i(-1, 0, 0)),
	EAST(5, 4, 3, "east", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.X, new Vec3i(1, 0, 0));

	public static final EnumFacing[] _VALUES = values();

	private final int index;
	private final int opposite;
	private final int horizontalIndex;
	private final String name;
	private final EnumFacing.Axis axis;
	private final EnumFacing.AxisDirection axisDirection;
	private final Vec3i directionVec;
	/**+
	 * All facings in D-U-N-S-W-E order
	 */
	private static final EnumFacing[] VALUES = new EnumFacing[6];
	/**+
	 * All Facings with horizontal axis in order S-W-N-E
	 */
	public static final EnumFacing[] HORIZONTALS = new EnumFacing[4];
	private static final Map<String, EnumFacing> NAME_LOOKUP = Maps.newHashMap();

	private EnumFacing(int indexIn, int oppositeIn, int horizontalIndexIn, String nameIn,
			EnumFacing.AxisDirection axisDirectionIn, EnumFacing.Axis axisIn, Vec3i directionVecIn) {
		this.index = indexIn;
		this.horizontalIndex = horizontalIndexIn;
		this.opposite = oppositeIn;
		this.name = nameIn;
		this.axis = axisIn;
		this.axisDirection = axisDirectionIn;
		this.directionVec = directionVecIn;
	}

	/**+
	 * Get the Index of this Facing (0-5). The order is D-U-N-S-W-E
	 */
	public int getIndex() {
		return this.index;
	}

	/**+
	 * Get the index of this horizontal facing (0-3). The order is
	 * S-W-N-E
	 */
	public int getHorizontalIndex() {
		return this.horizontalIndex;
	}

	/**+
	 * Get the AxisDirection of this Facing.
	 */
	public EnumFacing.AxisDirection getAxisDirection() {
		return this.axisDirection;
	}

	/**+
	 * Get the opposite Facing (e.g. DOWN => UP)
	 */
	public EnumFacing getOpposite() {
		/**+
		 * Get a Facing by it's index (0-5). The order is D-U-N-S-W-E.
		 * Named getFront for legacy reasons.
		 */
		return getFront(this.opposite);
	}

	/**+
	 * Rotate this Facing around the given axis clockwise. If this
	 * facing cannot be rotated around the given axis, returns this
	 * facing without rotating.
	 */
	public EnumFacing rotateAround(EnumFacing.Axis axis) {
		switch (axis) {
		case X:
			if (this != WEST && this != EAST) {
				return this.rotateX();
			}

			return this;
		case Y:
			if (this != UP && this != DOWN) {
				return this.rotateY();
			}

			return this;
		case Z:
			if (this != NORTH && this != SOUTH) {
				return this.rotateZ();
			}

			return this;
		default:
			throw new IllegalStateException("Unable to get CW facing for axis " + axis);
		}
	}

	/**+
	 * Rotate this Facing around the Y axis clockwise (NORTH => EAST
	 * => SOUTH => WEST => NORTH)
	 */
	public EnumFacing rotateY() {
		switch (this) {
		case NORTH:
			return EAST;
		case EAST:
			return SOUTH;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		default:
			throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
		}
	}

	/**+
	 * Rotate this Facing around the X axis (NORTH => DOWN => SOUTH
	 * => UP => NORTH)
	 */
	private EnumFacing rotateX() {
		switch (this) {
		case NORTH:
			return DOWN;
		case EAST:
		case WEST:
		default:
			throw new IllegalStateException("Unable to get X-rotated facing of " + this);
		case SOUTH:
			return UP;
		case UP:
			return NORTH;
		case DOWN:
			return SOUTH;
		}
	}

	/**+
	 * Rotate this Facing around the Z axis (EAST => DOWN => WEST =>
	 * UP => EAST)
	 */
	private EnumFacing rotateZ() {
		switch (this) {
		case EAST:
			return DOWN;
		case SOUTH:
		default:
			throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
		case WEST:
			return UP;
		case UP:
			return EAST;
		case DOWN:
			return WEST;
		}
	}

	/**+
	 * Rotate this Facing around the Y axis counter-clockwise (NORTH
	 * => WEST => SOUTH => EAST => NORTH)
	 */
	public EnumFacing rotateYCCW() {
		switch (this) {
		case NORTH:
			return WEST;
		case EAST:
			return NORTH;
		case SOUTH:
			return EAST;
		case WEST:
			return SOUTH;
		default:
			throw new IllegalStateException("Unable to get CCW facing of " + this);
		}
	}

	/**+
	 * Returns a offset that addresses the block in front of this
	 * facing.
	 */
	public int getFrontOffsetX() {
		return this.axis == EnumFacing.Axis.X ? this.axisDirection.getOffset() : 0;
	}

	public int getFrontOffsetY() {
		return this.axis == EnumFacing.Axis.Y ? this.axisDirection.getOffset() : 0;
	}

	/**+
	 * Returns a offset that addresses the block in front of this
	 * facing.
	 */
	public int getFrontOffsetZ() {
		return this.axis == EnumFacing.Axis.Z ? this.axisDirection.getOffset() : 0;
	}

	/**+
	 * Same as getName, but does not override the method from Enum.
	 */
	public String getName2() {
		return this.name;
	}

	public EnumFacing.Axis getAxis() {
		return this.axis;
	}

	/**+
	 * Get the facing specified by the given name
	 */
	public static EnumFacing byName(String name) {
		return name == null ? null : (EnumFacing) NAME_LOOKUP.get(name.toLowerCase());
	}

	/**+
	 * Get a Facing by it's index (0-5). The order is D-U-N-S-W-E.
	 * Named getFront for legacy reasons.
	 */
	public static EnumFacing getFront(int index) {
		return VALUES[MathHelper.abs_int(index % VALUES.length)];
	}

	/**+
	 * Get a Facing by it's horizontal index (0-3). The order is
	 * S-W-N-E.
	 */
	public static EnumFacing getHorizontal(int parInt1) {
		return HORIZONTALS[MathHelper.abs_int(parInt1 % HORIZONTALS.length)];
	}

	/**+
	 * Get the Facing corresponding to the given angle (0-360). An
	 * angle of 0 is SOUTH, an angle of 90 would be WEST.
	 */
	public static EnumFacing fromAngle(double angle) {
		/**+
		 * Get a Facing by it's horizontal index (0-3). The order is
		 * S-W-N-E.
		 */
		return getHorizontal(MathHelper.floor_double(angle / 90.0D + 0.5D) & 3);
	}

	/**+
	 * Choose a random Facing using the given Random
	 */
	public static EnumFacing random(EaglercraftRandom rand) {
		return _VALUES[rand.nextInt(_VALUES.length)];
	}

	public static EnumFacing getFacingFromVector(float parFloat1, float parFloat2, float parFloat3) {
		EnumFacing enumfacing = NORTH;
		float f = Float.MIN_VALUE;

		EnumFacing[] facings = _VALUES;
		for (int i = 0; i < facings.length; ++i) {
			EnumFacing enumfacing1 = facings[i];
			float f1 = parFloat1 * (float) enumfacing1.directionVec.getX()
					+ parFloat2 * (float) enumfacing1.directionVec.getY()
					+ parFloat3 * (float) enumfacing1.directionVec.getZ();
			if (f1 > f) {
				f = f1;
				enumfacing = enumfacing1;
			}
		}

		return enumfacing;
	}

	public String toString() {
		return this.name;
	}

	public String getName() {
		return this.name;
	}

	public static EnumFacing func_181076_a(EnumFacing.AxisDirection parAxisDirection, EnumFacing.Axis parAxis) {
		EnumFacing[] facings = EnumFacing._VALUES;
		for (int i = 0; i < facings.length; ++i) {
			EnumFacing enumfacing = facings[i];
			if (enumfacing.getAxisDirection() == parAxisDirection && enumfacing.getAxis() == parAxis) {
				return enumfacing;
			}
		}

		throw new IllegalArgumentException("No such direction: " + parAxisDirection + " " + parAxis);
	}

	/**+
	 * Get a normalized Vector that points in the direction of this
	 * Facing.
	 */
	public Vec3i getDirectionVec() {
		return this.directionVec;
	}

	static {
		Plane.bootstrap();
		EnumFacing[] facings = EnumFacing._VALUES;
		for (int i = 0; i < facings.length; ++i) {
			EnumFacing enumfacing = facings[i];
			VALUES[enumfacing.index] = enumfacing;
			if (enumfacing.getAxis().isHorizontal()) {
				HORIZONTALS[enumfacing.horizontalIndex] = enumfacing;
			}

			NAME_LOOKUP.put(enumfacing.getName2().toLowerCase(), enumfacing);
		}

	}

	public static enum Axis implements Predicate<EnumFacing>, IStringSerializable {
		X("x", EnumFacing.Plane.HORIZONTAL), Y("y", EnumFacing.Plane.VERTICAL), Z("z", EnumFacing.Plane.HORIZONTAL);

		private static final Map<String, EnumFacing.Axis> NAME_LOOKUP = Maps.newHashMap();
		private final String name;
		private final EnumFacing.Plane plane;

		private Axis(String name, EnumFacing.Plane plane) {
			this.name = name;
			this.plane = plane;
		}

		/**+
		 * Get the facing specified by the given name
		 */
		public static EnumFacing.Axis byName(String name) {
			return name == null ? null : (EnumFacing.Axis) NAME_LOOKUP.get(name.toLowerCase());
		}

		/**+
		 * Same as getName, but does not override the method from Enum.
		 */
		public String getName2() {
			return this.name;
		}

		public boolean isVertical() {
			return this.plane == EnumFacing.Plane.VERTICAL;
		}

		public boolean isHorizontal() {
			return this.plane == EnumFacing.Plane.HORIZONTAL;
		}

		public String toString() {
			return this.name;
		}

		public boolean apply(EnumFacing enumfacing) {
			return enumfacing != null && enumfacing.getAxis() == this;
		}

		public EnumFacing.Plane getPlane() {
			return this.plane;
		}

		public String getName() {
			return this.name;
		}

		static {
			EnumFacing.Axis[] axis = values();
			for (int i = 0; i < axis.length; ++i) {
				NAME_LOOKUP.put(axis[i].getName2().toLowerCase(), axis[i]);
			}

		}
	}

	public static enum AxisDirection {
		POSITIVE(1, "Towards positive"), NEGATIVE(-1, "Towards negative");

		public static final AxisDirection[] _VALUES = values();

		private final int offset;
		private final String description;

		private AxisDirection(int offset, String description) {
			this.offset = offset;
			this.description = description;
		}

		public int getOffset() {
			return this.offset;
		}

		public String toString() {
			return this.description;
		}
	}

	public static enum Plane implements Predicate<EnumFacing>, Iterable<EnumFacing> {
		HORIZONTAL(new EnumFacing[4]), VERTICAL(new EnumFacing[2]);

		public final EnumFacing[] facingsArray;

		private Plane(EnumFacing[] facingsArray) {
			this.facingsArray = facingsArray;
		}

		public EnumFacing[] facings() {
			return facingsArray;
		}

		/**+
		 * Choose a random Facing using the given Random
		 */
		public EnumFacing random(EaglercraftRandom rand) {
			EnumFacing[] aenumfacing = this.facings();
			return aenumfacing[rand.nextInt(aenumfacing.length)];
		}

		public boolean apply(EnumFacing enumfacing) {
			return enumfacing != null && enumfacing.getAxis().getPlane() == this;
		}

		public Iterator<EnumFacing> iterator() {
			return Iterators.forArray(facingsArray);
		}

		private static void bootstrap() {
			HORIZONTAL.facingsArray[0] = EnumFacing.NORTH;
			HORIZONTAL.facingsArray[1] = EnumFacing.EAST;
			HORIZONTAL.facingsArray[2] = EnumFacing.SOUTH;
			HORIZONTAL.facingsArray[3] = EnumFacing.WEST;
			VERTICAL.facingsArray[0] = EnumFacing.UP;
			VERTICAL.facingsArray[1] = EnumFacing.DOWN;
		}
	}
}