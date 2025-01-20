package net.minecraft.village;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

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
public class VillageDoorInfo {
	private final BlockPos doorBlockPos;
	private final BlockPos insideBlock;
	private final EnumFacing insideDirection;
	private int lastActivityTimestamp;
	private boolean isDetachedFromVillageFlag;
	private int doorOpeningRestrictionCounter;

	public VillageDoorInfo(BlockPos parBlockPos, int parInt1, int parInt2, int parInt3) {
		this(parBlockPos, getFaceDirection(parInt1, parInt2), parInt3);
	}

	private static EnumFacing getFaceDirection(int deltaX, int deltaZ) {
		return deltaX < 0 ? EnumFacing.WEST
				: (deltaX > 0 ? EnumFacing.EAST : (deltaZ < 0 ? EnumFacing.NORTH : EnumFacing.SOUTH));
	}

	public VillageDoorInfo(BlockPos parBlockPos, EnumFacing parEnumFacing, int parInt1) {
		this.doorBlockPos = parBlockPos;
		this.insideDirection = parEnumFacing;
		this.insideBlock = parBlockPos.offset(parEnumFacing, 2);
		this.lastActivityTimestamp = parInt1;
	}

	/**+
	 * Returns the squared distance between this door and the given
	 * coordinate.
	 */
	public int getDistanceSquared(int parInt1, int parInt2, int parInt3) {
		return (int) this.doorBlockPos.distanceSq((double) parInt1, (double) parInt2, (double) parInt3);
	}

	public int getDistanceToDoorBlockSq(BlockPos parBlockPos) {
		return (int) parBlockPos.distanceSq(this.getDoorBlockPos());
	}

	public int getDistanceToInsideBlockSq(BlockPos parBlockPos) {
		return (int) this.insideBlock.distanceSq(parBlockPos);
	}

	public boolean func_179850_c(BlockPos parBlockPos) {
		int i = parBlockPos.getX() - this.doorBlockPos.getX();
		int j = parBlockPos.getZ() - this.doorBlockPos.getY();
		return i * this.insideDirection.getFrontOffsetX() + j * this.insideDirection.getFrontOffsetZ() >= 0;
	}

	public void resetDoorOpeningRestrictionCounter() {
		this.doorOpeningRestrictionCounter = 0;
	}

	public void incrementDoorOpeningRestrictionCounter() {
		++this.doorOpeningRestrictionCounter;
	}

	public int getDoorOpeningRestrictionCounter() {
		return this.doorOpeningRestrictionCounter;
	}

	public BlockPos getDoorBlockPos() {
		return this.doorBlockPos;
	}

	public BlockPos getInsideBlockPos() {
		return this.insideBlock;
	}

	public int getInsideOffsetX() {
		return this.insideDirection.getFrontOffsetX() * 2;
	}

	public int getInsideOffsetZ() {
		return this.insideDirection.getFrontOffsetZ() * 2;
	}

	public int getInsidePosY() {
		return this.lastActivityTimestamp;
	}

	public void func_179849_a(int parInt1) {
		this.lastActivityTimestamp = parInt1;
	}

	public boolean getIsDetachedFromVillageFlag() {
		return this.isDetachedFromVillageFlag;
	}

	public void setIsDetachedFromVillageFlag(boolean parFlag) {
		this.isDetachedFromVillageFlag = parFlag;
	}
}