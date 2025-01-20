package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
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
public class EntityArrow extends Entity implements IProjectile {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private Block inTile;
	private int inData;
	private boolean inGround;
	public int canBePickedUp;
	public int arrowShake;
	public Entity shootingEntity;
	private int ticksInGround;
	private int ticksInAir;
	private double damage = 2.0D;
	private int knockbackStrength;

	public EntityArrow(World worldIn) {
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
	}

	public EntityArrow(World worldIn, double x, double y, double z) {
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
		this.setPosition(x, y, z);
	}

	public EntityArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase parEntityLivingBase, float parFloat1,
			float parFloat2) {
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = shooter;
		if (shooter instanceof EntityPlayer) {
			this.canBePickedUp = 1;
		}

		this.posY = shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D;
		double d0 = parEntityLivingBase.posX - shooter.posX;
		double d1 = parEntityLivingBase.getEntityBoundingBox().minY + (double) (parEntityLivingBase.height / 3.0F)
				- this.posY;
		double d2 = parEntityLivingBase.posZ - shooter.posZ;
		double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		if (d3 >= 1.0E-7D) {
			float f = (float) (MathHelper.func_181159_b(d2, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
			float f1 = (float) (-(MathHelper.func_181159_b(d1, d3) * 180.0D / 3.1415927410125732D));
			double d4 = d0 / d3;
			double d5 = d2 / d3;
			this.setLocationAndAngles(shooter.posX + d4, this.posY, shooter.posZ + d5, f, f1);
			float f2 = (float) (d3 * 0.20000000298023224D);
			this.setThrowableHeading(d0, d1 + (double) f2, d2, parFloat1, parFloat2);
		}
	}

	public EntityArrow(World worldIn, EntityLivingBase shooter, float velocity) {
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = shooter;
		if (shooter instanceof EntityPlayer) {
			this.canBePickedUp = 1;
		}

		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(shooter.posX, shooter.posY + (double) shooter.getEyeHeight(), shooter.posZ,
				shooter.rotationYaw, shooter.rotationPitch);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F)
				* MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F)
				* MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F));
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, velocity * 1.5F, 1.0F);
	}

	protected void entityInit() {
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	/**+
	 * Similar to setArrowHeading, it's point the throwable entity
	 * to a x, y, z direction.
	 */
	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt_double(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D
				* (double) inaccuracy;
		y = y + this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D
				* (double) inaccuracy;
		z = z + this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D
				* (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt_double(x * x + z * z);
		this.prevRotationYaw = this.rotationYaw = (float) (MathHelper.func_181159_b(x, z) * 180.0D
				/ 3.1415927410125732D);
		this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.func_181159_b(y, (double) f1) * 180.0D
				/ 3.1415927410125732D);
		this.ticksInGround = 0;
	}

	public void setPositionAndRotation2(double d0, double d1, double d2, float f, float f1, int var9, boolean var10) {
		this.setPosition(d0, d1, d2);
		this.setRotation(f, f1);
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
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}

	}

	public boolean isChair = false;

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		if (isChair) {
			if (!(riddenByEntity instanceof EntityPlayer)) {
				isChair = false;
				setDead();
			}
			return;
		}
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float) (MathHelper.func_181159_b(this.motionX, this.motionZ)
					* 180.0D / 3.1415927410125732D);
			this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.func_181159_b(this.motionY, (double) f)
					* 180.0D / 3.1415927410125732D);
		}

		BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
		IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
		Block block = iblockstate.getBlock();
		if (block.getMaterial() != Material.air) {
			block.setBlockBoundsBasedOnState(this.worldObj, blockpos);
			AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(this.worldObj, blockpos, iblockstate);
			if (axisalignedbb != null && axisalignedbb.isVecInside(new Vec3(this.posX, this.posY, this.posZ))) {
				this.inGround = true;
			}
		}

		if (this.arrowShake > 0) {
			--this.arrowShake;
		}

		if (this.inGround) {
			int j = block.getMetaFromState(iblockstate);
			if (block == this.inTile && j == this.inData) {
				++this.ticksInGround;
				if (this.ticksInGround >= 1200) {
					this.setDead();
				}
			} else {
				this.inGround = false;
				this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			}

		} else {
			++this.ticksInAir;
			Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
			Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
			vec31 = new Vec3(this.posX, this.posY, this.posZ);
			vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			if (movingobjectposition != null) {
				vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
						movingobjectposition.hitVec.zCoord);
			}

			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()
					.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;

			for (int i = 0; i < list.size(); ++i) {
				Entity entity1 = (Entity) list.get(i);
				if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
					float f1 = 0.3F;
					AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand((double) f1, (double) f1,
							(double) f1);
					MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);
					if (movingobjectposition1 != null) {
						double d1 = vec31.squareDistanceTo(movingobjectposition1.hitVec);
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

			if (movingobjectposition != null && movingobjectposition.entityHit != null
					&& movingobjectposition.entityHit instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;
				if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer
						&& !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
					movingobjectposition = null;
				}
			}

			if (movingobjectposition != null) {
				if (movingobjectposition.entityHit != null) {
					float f2 = MathHelper.sqrt_double(
							this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					int l = MathHelper.ceiling_double_int((double) f2 * this.damage);
					if (this.getIsCritical()) {
						l += this.rand.nextInt(l / 2 + 2);
					}

					DamageSource damagesource;
					if (this.shootingEntity == null) {
						damagesource = DamageSource.causeArrowDamage(this, this);
					} else {
						damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
					}

					if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
						movingobjectposition.entityHit.setFire(5);
					}

					if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) l)) {
						if (movingobjectposition.entityHit instanceof EntityLivingBase) {
							EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;
							if (!this.worldObj.isRemote) {
								entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
							}

							if (this.knockbackStrength > 0) {
								float f7 = MathHelper
										.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
								if (f7 > 0.0F) {
									movingobjectposition.entityHit.addVelocity(
											this.motionX * (double) this.knockbackStrength * 0.6000000238418579D
													/ (double) f7,
											0.1D, this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D
													/ (double) f7);
								}
							}

							if (this.shootingEntity instanceof EntityLivingBase) {
								EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
								EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity,
										entitylivingbase);
							}

							if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity
									&& movingobjectposition.entityHit instanceof EntityPlayer
									&& this.shootingEntity instanceof EntityPlayerMP) {
								((EntityPlayerMP) this.shootingEntity).playerNetServerHandler
										.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
							}
						}

						this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
						if (!(movingobjectposition.entityHit instanceof EntityEnderman)) {
							this.setDead();
						}
					} else {
						this.motionX *= -0.10000000149011612D;
						this.motionY *= -0.10000000149011612D;
						this.motionZ *= -0.10000000149011612D;
						this.rotationYaw += 180.0F;
						this.prevRotationYaw += 180.0F;
						this.ticksInAir = 0;
					}
				} else {
					BlockPos blockpos1 = movingobjectposition.getBlockPos();
					this.xTile = blockpos1.getX();
					this.yTile = blockpos1.getY();
					this.zTile = blockpos1.getZ();
					IBlockState iblockstate1 = this.worldObj.getBlockState(blockpos1);
					this.inTile = iblockstate1.getBlock();
					this.inData = this.inTile.getMetaFromState(iblockstate1);
					this.motionX = (double) ((float) (movingobjectposition.hitVec.xCoord - this.posX));
					this.motionY = (double) ((float) (movingobjectposition.hitVec.yCoord - this.posY));
					this.motionZ = (double) ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
					float f5 = MathHelper.sqrt_double(
							this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					this.posX -= this.motionX / (double) f5 * 0.05000000074505806D;
					this.posY -= this.motionY / (double) f5 * 0.05000000074505806D;
					this.posZ -= this.motionZ / (double) f5 * 0.05000000074505806D;
					this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					this.inGround = true;
					this.arrowShake = 7;
					this.setIsCritical(false);
					if (this.inTile.getMaterial() != Material.air) {
						this.inTile.onEntityCollidedWithBlock(this.worldObj, blockpos1, iblockstate1, this);
					}
				}
			}

			if (this.getIsCritical()) {
				for (int k = 0; k < 4; ++k) {
					this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double) k / 4.0D,
							this.posY + this.motionY * (double) k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D,
							-this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
				}
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			float f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D
					/ 3.1415927410125732D);

			for (this.rotationPitch = (float) (MathHelper.func_181159_b(this.motionY, (double) f3) * 180.0D
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
			float f4 = 0.99F;
			float f6 = 0.05F;
			if (this.isInWater()) {
				for (int i1 = 0; i1 < 4; ++i1) {
					float f8 = 0.25F;
					this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double) f8,
							this.posY - this.motionY * (double) f8, this.posZ - this.motionZ * (double) f8,
							this.motionX, this.motionY, this.motionZ, new int[0]);
				}

				f4 = 0.6F;
			}

			if (this.isWet()) {
				this.extinguish();
			}

			this.motionX *= (double) f4;
			this.motionY *= (double) f4;
			this.motionZ *= (double) f4;
			this.motionY -= (double) f6;
			this.setPosition(this.posX, this.posY, this.posZ);
			this.doBlockCollisions();
		}
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("xTile", (short) this.xTile);
		nbttagcompound.setShort("yTile", (short) this.yTile);
		nbttagcompound.setShort("zTile", (short) this.zTile);
		nbttagcompound.setShort("life", (short) this.ticksInGround);
		ResourceLocation resourcelocation = (ResourceLocation) Block.blockRegistry.getNameForObject(this.inTile);
		nbttagcompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		nbttagcompound.setByte("inData", (byte) this.inData);
		nbttagcompound.setByte("shake", (byte) this.arrowShake);
		nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		nbttagcompound.setByte("pickup", (byte) this.canBePickedUp);
		nbttagcompound.setDouble("damage", this.damage);
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.xTile = nbttagcompound.getShort("xTile");
		this.yTile = nbttagcompound.getShort("yTile");
		this.zTile = nbttagcompound.getShort("zTile");
		this.ticksInGround = nbttagcompound.getShort("life");
		if (nbttagcompound.hasKey("inTile", 8)) {
			this.inTile = Block.getBlockFromName(nbttagcompound.getString("inTile"));
		} else {
			this.inTile = Block.getBlockById(nbttagcompound.getByte("inTile") & 255);
		}

		this.inData = nbttagcompound.getByte("inData") & 255;
		this.arrowShake = nbttagcompound.getByte("shake") & 255;
		this.inGround = nbttagcompound.getByte("inGround") == 1;
		if (nbttagcompound.hasKey("damage", 99)) {
			this.damage = nbttagcompound.getDouble("damage");
		}

		if (nbttagcompound.hasKey("pickup", 99)) {
			this.canBePickedUp = nbttagcompound.getByte("pickup");
		} else if (nbttagcompound.hasKey("player", 99)) {
			this.canBePickedUp = nbttagcompound.getBoolean("player") ? 1 : 0;
		}

	}

	/**+
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer entityplayer) {
		if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
			boolean flag = this.canBePickedUp == 1
					|| this.canBePickedUp == 2 && entityplayer.capabilities.isCreativeMode;
			if (this.canBePickedUp == 1
					&& !entityplayer.inventory.addItemStackToInventory(new ItemStack(Items.arrow, 1))) {
				flag = false;
			}

			if (flag) {
				this.playSound("random.pop", 0.2F,
						((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				entityplayer.onItemPickup(this, 1);
				this.setDead();
			}

		}
	}

	/**+
	 * returns if this entity triggers Block.onEntityWalking on the
	 * blocks they walk on. used for spiders and wolves to prevent
	 * them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	public void setDamage(double damageIn) {
		this.damage = damageIn;
	}

	public double getDamage() {
		return this.damage;
	}

	/**+
	 * Sets the amount of knockback the arrow applies when it hits a
	 * mob.
	 */
	public void setKnockbackStrength(int knockbackStrengthIn) {
		this.knockbackStrength = knockbackStrengthIn;
	}

	/**+
	 * If returns false, the item will not inflict any damage
	 * against entities.
	 */
	public boolean canAttackWithItem() {
		return false;
	}

	public float getEyeHeight() {
		return 0.0F;
	}

	/**+
	 * Whether the arrow has a stream of critical hit particles
	 * flying behind it.
	 */
	public void setIsCritical(boolean critical) {
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);
		if (critical) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
		}

	}

	/**+
	 * Whether the arrow has a stream of critical hit particles
	 * flying behind it.
	 */
	public boolean getIsCritical() {
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);
		return (b0 & 1) != 0;
	}
}