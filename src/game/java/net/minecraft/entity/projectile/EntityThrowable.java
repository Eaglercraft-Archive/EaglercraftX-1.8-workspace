package net.minecraft.entity.projectile;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
public abstract class EntityThrowable extends Entity implements IProjectile {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private Block inTile;
	protected boolean inGround;
	public int throwableShake;
	private EntityLivingBase thrower;
	private String throwerName;
	private int ticksInGround;
	private int ticksInAir;

	public EntityThrowable(World worldIn) {
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}

	protected void entityInit() {
	}

	/**+
	 * Checks if the entity is in range to render by using the past
	 * in distance and comparing it to its average edge length * 64
	 * * renderDistanceWeight Args: distance
	 */
	public boolean isInRangeToRenderDist(double d0) {
		double d1 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
		if (Double.isNaN(d1)) {
			d1 = 4.0D;
		}

		d1 = d1 * 64.0D;
		return d0 < d1 * d1;
	}

	public EntityThrowable(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn);
		this.thrower = throwerIn;
		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(throwerIn.posX, throwerIn.posY + (double) throwerIn.getEyeHeight(), throwerIn.posZ,
				throwerIn.rotationYaw, throwerIn.rotationPitch);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		float f = 0.4F;
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F)
				* MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F)
				* MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
		this.motionY = (double) (-MathHelper.sin((this.rotationPitch + this.getInaccuracy()) / 180.0F * 3.1415927F)
				* f);
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.getVelocity(), 1.0F);
	}

	public EntityThrowable(World worldIn, double x, double y, double z) {
		super(worldIn);
		this.ticksInGround = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(x, y, z);
	}

	protected float getVelocity() {
		return 1.5F;
	}

	protected float getInaccuracy() {
		return 0.0F;
	}

	/**+
	 * Similar to setArrowHeading, it's point the throwable entity
	 * to a x, y, z direction.
	 */
	public void setThrowableHeading(double d0, double d1, double d2, float f, float f1) {
		float f2 = MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
		d0 = d0 / (double) f2;
		d1 = d1 / (double) f2;
		d2 = d2 / (double) f2;
		d0 = d0 + this.rand.nextGaussian() * 0.007499999832361937D * (double) f1;
		d1 = d1 + this.rand.nextGaussian() * 0.007499999832361937D * (double) f1;
		d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double) f1;
		d0 = d0 * (double) f;
		d1 = d1 * (double) f;
		d2 = d2 * (double) f;
		this.motionX = d0;
		this.motionY = d1;
		this.motionZ = d2;
		float f3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		this.prevRotationYaw = this.rotationYaw = (float) (MathHelper.func_181159_b(d0, d2) * 180.0D
				/ 3.1415927410125732D);
		this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.func_181159_b(d1, (double) f3) * 180.0D
				/ 3.1415927410125732D);
		this.ticksInGround = 0;
	}

	/**+
	 * Sets the velocity to the args. Args: x, y, z
	 */
	public void setVelocity(double d0, double d1, double d2) {
		this.motionX = d0;
		this.motionY = d1;
		this.motionZ = d2;
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
			this.prevRotationYaw = this.rotationYaw = (float) (MathHelper.func_181159_b(d0, d2) * 180.0D
					/ 3.1415927410125732D);
			this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.func_181159_b(d1, (double) f) * 180.0D
					/ 3.1415927410125732D);
		}

	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();
		if (this.throwableShake > 0) {
			--this.throwableShake;
		}

		if (this.inGround) {
			if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile))
					.getBlock() == this.inTile) {
				++this.ticksInGround;
				if (this.ticksInGround == 1200) {
					this.setDead();
				}

				return;
			}

			this.inGround = false;
			this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
			this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
			this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
			this.ticksInGround = 0;
			this.ticksInAir = 0;
		} else {
			++this.ticksInAir;
		}

		Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
		Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
		vec3 = new Vec3(this.posX, this.posY, this.posZ);
		vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if (movingobjectposition != null) {
			vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
					movingobjectposition.hitVec.zCoord);
		}

		if (!this.worldObj.isRemote) {
			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()
					.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			EntityLivingBase entitylivingbase = this.getThrower();

			for (int j = 0; j < list.size(); ++j) {
				Entity entity1 = (Entity) list.get(j);
				if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || this.ticksInAir >= 5)) {
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f, (double) f,
							(double) f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);
					if (movingobjectposition1 != null) {
						double d1 = vec3.squareDistanceTo(movingobjectposition1.hitVec);
						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}
		}

		if (movingobjectposition != null) {
			if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
					&& this.worldObj.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.portal) {
				this.func_181015_d(movingobjectposition.getBlockPos());
			} else {
				this.onImpact(movingobjectposition);
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D
				/ 3.1415927410125732D);

		for (this.rotationPitch = (float) (MathHelper.func_181159_b(this.motionY, (double) f1) * 180.0D
				/ 3.1415927410125732D); this.rotationPitch
						- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f2 = 0.99F;
		float f3 = this.getGravityVelocity();
		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				float f4 = 0.25F;
				this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double) f4,
						this.posY - this.motionY * (double) f4, this.posZ - this.motionZ * (double) f4, this.motionX,
						this.motionY, this.motionZ, new int[0]);
			}

			f2 = 0.8F;
		}

		this.motionX *= (double) f2;
		this.motionY *= (double) f2;
		this.motionZ *= (double) f2;
		this.motionY -= (double) f3;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	/**+
	 * Gets the amount of gravity to apply to the thrown entity with
	 * each tick.
	 */
	protected float getGravityVelocity() {
		return 0.03F;
	}

	protected abstract void onImpact(MovingObjectPosition var1);

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("xTile", (short) this.xTile);
		nbttagcompound.setShort("yTile", (short) this.yTile);
		nbttagcompound.setShort("zTile", (short) this.zTile);
		ResourceLocation resourcelocation = (ResourceLocation) Block.blockRegistry.getNameForObject(this.inTile);
		nbttagcompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		nbttagcompound.setByte("shake", (byte) this.throwableShake);
		nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower instanceof EntityPlayer) {
			this.throwerName = this.thrower.getName();
		}

		nbttagcompound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.xTile = nbttagcompound.getShort("xTile");
		this.yTile = nbttagcompound.getShort("yTile");
		this.zTile = nbttagcompound.getShort("zTile");
		if (nbttagcompound.hasKey("inTile", 8)) {
			this.inTile = Block.getBlockFromName(nbttagcompound.getString("inTile"));
		} else {
			this.inTile = Block.getBlockById(nbttagcompound.getByte("inTile") & 255);
		}

		this.throwableShake = nbttagcompound.getByte("shake") & 255;
		this.inGround = nbttagcompound.getByte("inGround") == 1;
		this.thrower = null;
		this.throwerName = nbttagcompound.getString("ownerName");
		if (this.throwerName != null && this.throwerName.length() == 0) {
			this.throwerName = null;
		}

		this.thrower = this.getThrower();
	}

	public EntityLivingBase getThrower() {
		if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
			this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
			if (this.thrower == null && this.worldObj instanceof WorldServer) {
				try {
					Entity entity = ((WorldServer) this.worldObj)
							.getEntityFromUuid(EaglercraftUUID.fromString(this.throwerName));
					if (entity instanceof EntityLivingBase) {
						this.thrower = (EntityLivingBase) entity;
					}
				} catch (Throwable var2) {
					this.thrower = null;
				}
			}
		}

		return this.thrower;
	}
}