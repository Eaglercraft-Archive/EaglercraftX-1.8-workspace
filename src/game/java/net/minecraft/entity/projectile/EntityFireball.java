package net.minecraft.entity.projectile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
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
public abstract class EntityFireball extends Entity {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private Block inTile;
	private boolean inGround;
	public EntityLivingBase shootingEntity;
	private int ticksAlive;
	private int ticksInAir;
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;

	public EntityFireball(World worldIn) {
		super(worldIn);
		this.setSize(1.0F, 1.0F);
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

	public EntityFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
		super(worldIn);
		this.setSize(1.0F, 1.0F);
		this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
		this.setPosition(x, y, z);
		double d0 = (double) MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
		if (d0 == 0.0) {
			this.accelerationX = this.accelerationY = this.accelerationZ = 0.0D;
			return;
		}
		this.accelerationX = accelX / d0 * 0.1D;
		this.accelerationY = accelY / d0 * 0.1D;
		this.accelerationZ = accelZ / d0 * 0.1D;
	}

	public EntityFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn);
		this.shootingEntity = shooter;
		this.setSize(1.0F, 1.0F);
		this.setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = this.motionY = this.motionZ = 0.0D;
		accelX = accelX + this.rand.nextGaussian() * 0.4D;
		accelY = accelY + this.rand.nextGaussian() * 0.4D;
		accelZ = accelZ + this.rand.nextGaussian() * 0.4D;
		double d0 = (double) MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
		if (d0 == 0.0) {
			this.accelerationX = this.accelerationY = this.accelerationZ = 0.0D;
			return;
		}
		this.accelerationX = accelX / d0 * 0.1D;
		this.accelerationY = accelY / d0 * 0.1D;
		this.accelerationZ = accelZ / d0 * 0.1D;
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		if (this.worldObj.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead)
				&& this.worldObj.isBlockLoaded(new BlockPos(this))) {
			super.onUpdate();
			this.setFire(1);
			if (this.inGround) {
				if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile))
						.getBlock() == this.inTile) {
					++this.ticksAlive;
					if (this.ticksAlive == 600) {
						this.setDead();
					}

					return;
				}

				this.inGround = false;
				this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
				this.ticksAlive = 0;
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

			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()
					.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;

			for (int i = 0, l = list.size(); i < l; ++i) {
				Entity entity1 = (Entity) list.get(i);
				if (entity1.canBeCollidedWith()
						&& (!entity1.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25)) {
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

			if (movingobjectposition != null) {
				this.onImpact(movingobjectposition);
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.func_181159_b(this.motionZ, this.motionX) * 180.0D
					/ 3.1415927410125732D) + 90.0F;

			for (this.rotationPitch = (float) (MathHelper.func_181159_b((double) f1, this.motionY) * 180.0D
					/ 3.1415927410125732D)
					- 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
			float f2 = this.getMotionFactor();
			if (this.isInWater()) {
				for (int j = 0; j < 4; ++j) {
					float f3 = 0.25F;
					this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double) f3,
							this.posY - this.motionY * (double) f3, this.posZ - this.motionZ * (double) f3,
							this.motionX, this.motionY, this.motionZ, new int[0]);
				}

				f2 = 0.8F;
			}

			this.motionX += this.accelerationX;
			this.motionY += this.accelerationY;
			this.motionZ += this.accelerationZ;
			this.motionX *= (double) f2;
			this.motionY *= (double) f2;
			this.motionZ *= (double) f2;
			this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D,
					0.0D, 0.0D, new int[0]);
			this.setPosition(this.posX, this.posY, this.posZ);
		} else {
			this.setDead();
		}
	}

	/**+
	 * Return the motion factor for this projectile. The factor is
	 * multiplied by the original motion.
	 */
	protected float getMotionFactor() {
		return 0.95F;
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
		nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		nbttagcompound.setTag("direction",
				this.newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
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

		this.inGround = nbttagcompound.getByte("inGround") == 1;
		if (nbttagcompound.hasKey("direction", 9)) {
			NBTTagList nbttaglist = nbttagcompound.getTagList("direction", 6);
			this.motionX = nbttaglist.getDoubleAt(0);
			this.motionY = nbttaglist.getDoubleAt(1);
			this.motionZ = nbttaglist.getDoubleAt(2);
		} else {
			this.setDead();
		}

	}

	/**+
	 * Returns true if other Entities should be prevented from
	 * moving through this Entity.
	 */
	public boolean canBeCollidedWith() {
		return true;
	}

	public float getCollisionBorderSize() {
		return 1.0F;
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float var2) {
		if (this.isEntityInvulnerable(damagesource)) {
			return false;
		} else {
			this.setBeenAttacked();
			if (damagesource.getEntity() != null) {
				Vec3 vec3 = damagesource.getEntity().getLookVec();
				if (vec3 != null) {
					this.motionX = vec3.xCoord;
					this.motionY = vec3.yCoord;
					this.motionZ = vec3.zCoord;
					this.accelerationX = this.motionX * 0.1D;
					this.accelerationY = this.motionY * 0.1D;
					this.accelerationZ = this.motionZ * 0.1D;
				}

				if (damagesource.getEntity() instanceof EntityLivingBase) {
					this.shootingEntity = (EntityLivingBase) damagesource.getEntity();
				}

				return true;
			} else {
				return false;
			}
		}
	}

	/**+
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float var1) {
		return 1.0F;
	}

	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
		return 1.0f;
	}

	public int getBrightnessForRender(float var1) {
		return 15728880;
	}
}