package net.minecraft.util;

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
public class MovingObjectPosition {
	private BlockPos blockPos;
	public MovingObjectPosition.MovingObjectType typeOfHit;
	public EnumFacing sideHit;
	public Vec3 hitVec;
	public Entity entityHit;

	public MovingObjectPosition(Vec3 hitVecIn, EnumFacing facing, BlockPos blockPosIn) {
		this(MovingObjectPosition.MovingObjectType.BLOCK, hitVecIn, facing, blockPosIn);
	}

	public MovingObjectPosition(Vec3 parVec3_1, EnumFacing facing) {
		this(MovingObjectPosition.MovingObjectType.BLOCK, parVec3_1, facing, BlockPos.ORIGIN);
	}

	public MovingObjectPosition(Entity parEntity) {
		this(parEntity, new Vec3(parEntity.posX, parEntity.posY, parEntity.posZ));
	}

	public MovingObjectPosition(MovingObjectPosition.MovingObjectType typeOfHitIn, Vec3 hitVecIn, EnumFacing sideHitIn,
			BlockPos blockPosIn) {
		this.typeOfHit = typeOfHitIn;
		this.blockPos = blockPosIn;
		this.sideHit = sideHitIn;
		this.hitVec = new Vec3(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
	}

	public MovingObjectPosition(Entity entityHitIn, Vec3 hitVecIn) {
		this.typeOfHit = MovingObjectPosition.MovingObjectType.ENTITY;
		this.entityHit = entityHitIn;
		this.hitVec = hitVecIn;
	}

	public BlockPos getBlockPos() {
		return this.blockPos;
	}

	public String toString() {
		return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos="
				+ this.hitVec + ", entity=" + this.entityHit + '}';
	}

	public static enum MovingObjectType {
		MISS, BLOCK, ENTITY;
	}
}