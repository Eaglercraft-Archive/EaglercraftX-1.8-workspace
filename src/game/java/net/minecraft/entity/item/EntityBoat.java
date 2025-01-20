package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
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
public class EntityBoat extends Entity {
	private boolean isBoatEmpty;
	private double speedMultiplier;
	private int boatPosRotationIncrements;
	private double boatX;
	private double boatY;
	private double boatZ;
	private double boatYaw;
	private double boatPitch;
	private double velocityX;
	private double velocityY;
	private double velocityZ;

	public EntityBoat(World worldIn) {
		super(worldIn);
		this.isBoatEmpty = true;
		this.speedMultiplier = 0.07D;
		this.preventEntitySpawning = true;
		this.setSize(1.5F, 0.6F);
	}

	/**+
	 * returns if this entity triggers Block.onEntityWalking on the
	 * blocks they walk on. used for spiders and wolves to prevent
	 * them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(17, Integer.valueOf(0));
		this.dataWatcher.addObject(18, Integer.valueOf(1));
		this.dataWatcher.addObject(19, Float.valueOf(0.0F));
	}

	/**+
	 * Returns a boundingBox used to collide the entity with other
	 * entities and blocks. This enables the entity to be pushable
	 * on contact, like boats or minecarts.
	 */
	public AxisAlignedBB getCollisionBox(Entity entity) {
		return entity.getEntityBoundingBox();
	}

	/**+
	 * Returns the collision bounding box for this entity
	 */
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}

	/**+
	 * Returns true if this entity should push and be pushed by
	 * other entities when colliding.
	 */
	public boolean canBePushed() {
		return true;
	}

	public EntityBoat(World worldIn, double parDouble1, double parDouble2, double parDouble3) {
		this(worldIn);
		this.setPosition(parDouble1, parDouble2, parDouble3);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = parDouble1;
		this.prevPosY = parDouble2;
		this.prevPosZ = parDouble3;
	}

	/**+
	 * Returns the Y offset from the entity's position for any
	 * entity riding this one.
	 */
	public double getMountedYOffset() {
		return -0.3D;
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		if (this.isEntityInvulnerable(damagesource)) {
			return false;
		} else if (!this.worldObj.isRemote && !this.isDead) {
			if (this.riddenByEntity != null && this.riddenByEntity == damagesource.getEntity()
					&& damagesource instanceof EntityDamageSourceIndirect) {
				return false;
			} else {
				this.setForwardDirection(-this.getForwardDirection());
				this.setTimeSinceHit(10);
				this.setDamageTaken(this.getDamageTaken() + f * 10.0F);
				this.setBeenAttacked();
				boolean flag = damagesource.getEntity() instanceof EntityPlayer
						&& ((EntityPlayer) damagesource.getEntity()).capabilities.isCreativeMode;
				if (flag || this.getDamageTaken() > 40.0F) {
					if (this.riddenByEntity != null) {
						this.riddenByEntity.mountEntity(this);
					}

					if (!flag && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
						this.dropItemWithOffset(Items.boat, 1, 0.0F);
					}

					this.setDead();
				}

				return true;
			}
		} else {
			return true;
		}
	}

	/**+
	 * Setups the entity to do the hurt animation. Only used by
	 * packets in multiplayer.
	 */
	public void performHurtAnimation() {
		this.setForwardDirection(-this.getForwardDirection());
		this.setTimeSinceHit(10);
		this.setDamageTaken(this.getDamageTaken() * 11.0F);
	}

	/**+
	 * Returns true if other Entities should be prevented from
	 * moving through this Entity.
	 */
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void setPositionAndRotation2(double d0, double d1, double d2, float f, float f1, int i, boolean flag) {
		if (flag && this.riddenByEntity != null) {
			this.prevPosX = this.posX = d0;
			this.prevPosY = this.posY = d1;
			this.prevPosZ = this.posZ = d2;
			this.rotationYaw = f;
			this.rotationPitch = f1;
			this.boatPosRotationIncrements = 0;
			this.setPosition(d0, d1, d2);
			this.motionX = this.velocityX = 0.0D;
			this.motionY = this.velocityY = 0.0D;
			this.motionZ = this.velocityZ = 0.0D;
		} else {
			if (this.isBoatEmpty) {
				this.boatPosRotationIncrements = i + 5;
			} else {
				double d3 = d0 - this.posX;
				double d4 = d1 - this.posY;
				double d5 = d2 - this.posZ;
				double d6 = d3 * d3 + d4 * d4 + d5 * d5;
				if (d6 <= 1.0D) {
					return;
				}

				this.boatPosRotationIncrements = 3;
			}

			this.boatX = d0;
			this.boatY = d1;
			this.boatZ = d2;
			this.boatYaw = (double) f;
			this.boatPitch = (double) f1;
			this.motionX = this.velocityX;
			this.motionY = this.velocityY;
			this.motionZ = this.velocityZ;
		}
	}

	/**+
	 * Sets the velocity to the args. Args: x, y, z
	 */
	public void setVelocity(double d0, double d1, double d2) {
		this.velocityX = this.motionX = d0;
		this.velocityY = this.motionY = d1;
		this.velocityZ = this.motionZ = d2;
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		if (this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}

		if (this.getDamageTaken() > 0.0F) {
			this.setDamageTaken(this.getDamageTaken() - 1.0F);
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		byte b0 = 5;
		double d0 = 0.0D;

		for (int i = 0; i < b0; ++i) {
			double d1 = this.getEntityBoundingBox().minY
					+ (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double) (i + 0)
							/ (double) b0
					- 0.125D;
			double d3 = this.getEntityBoundingBox().minY
					+ (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double) (i + 1)
							/ (double) b0
					- 0.125D;
			AxisAlignedBB axisalignedbb = new AxisAlignedBB(this.getEntityBoundingBox().minX, d1,
					this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().maxX, d3,
					this.getEntityBoundingBox().maxZ);
			if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
				d0 += 1.0D / (double) b0;
			}
		}

		double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		if (d9 > 0.2975D) {
			double d2 = Math.cos((double) this.rotationYaw * 3.141592653589793D / 180.0D);
			double d4 = Math.sin((double) this.rotationYaw * 3.141592653589793D / 180.0D);

			for (int j = 0; (double) j < 1.0D + d9 * 60.0D; ++j) {
				double d5 = (double) (this.rand.nextFloat() * 2.0F - 1.0F);
				double d6 = (double) (this.rand.nextInt(2) * 2 - 1) * 0.7D;
				if (this.rand.nextBoolean()) {
					double d7 = this.posX - d2 * d5 * 0.8D + d4 * d6;
					double d8 = this.posZ - d4 * d5 * 0.8D - d2 * d6;
					this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d7, this.posY - 0.125D, d8,
							this.motionX, this.motionY, this.motionZ, new int[0]);
				} else {
					double d24 = this.posX + d2 + d4 * d5 * 0.7D;
					double d25 = this.posZ + d4 - d2 * d5 * 0.7D;
					this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d24, this.posY - 0.125D, d25,
							this.motionX, this.motionY, this.motionZ, new int[0]);
				}
			}
		}

		if (this.worldObj.isRemote && this.isBoatEmpty) {
			if (this.boatPosRotationIncrements > 0) {
				double d12 = this.posX + (this.boatX - this.posX) / (double) this.boatPosRotationIncrements;
				double d16 = this.posY + (this.boatY - this.posY) / (double) this.boatPosRotationIncrements;
				double d19 = this.posZ + (this.boatZ - this.posZ) / (double) this.boatPosRotationIncrements;
				double d22 = MathHelper.wrapAngleTo180_double(this.boatYaw - (double) this.rotationYaw);
				this.rotationYaw = (float) ((double) this.rotationYaw + d22 / (double) this.boatPosRotationIncrements);
				this.rotationPitch = (float) ((double) this.rotationPitch
						+ (this.boatPitch - (double) this.rotationPitch) / (double) this.boatPosRotationIncrements);
				--this.boatPosRotationIncrements;
				this.setPosition(d12, d16, d19);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			} else {
				double d13 = this.posX + this.motionX;
				double d17 = this.posY + this.motionY;
				double d20 = this.posZ + this.motionZ;
				this.setPosition(d13, d17, d20);
				if (this.onGround) {
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
				}

				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}

		} else {
			if (d0 < 1.0D) {
				double d10 = d0 * 2.0D - 1.0D;
				this.motionY += 0.03999999910593033D * d10;
			} else {
				if (this.motionY < 0.0D) {
					this.motionY /= 2.0D;
				}

				this.motionY += 0.007000000216066837D;
			}

			if (this.riddenByEntity instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) this.riddenByEntity;
				float f = this.riddenByEntity.rotationYaw + -entitylivingbase.moveStrafing * 90.0F;
				this.motionX += -Math.sin((double) (f * 3.1415927F / 180.0F)) * this.speedMultiplier
						* (double) entitylivingbase.moveForward * 0.05000000074505806D;
				this.motionZ += Math.cos((double) (f * 3.1415927F / 180.0F)) * this.speedMultiplier
						* (double) entitylivingbase.moveForward * 0.05000000074505806D;
			}

			double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			if (d11 > 0.35D) {
				double d14 = 0.35D / d11;
				this.motionX *= d14;
				this.motionZ *= d14;
				d11 = 0.35D;
			}

			if (d11 > d9 && this.speedMultiplier < 0.35D) {
				this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;
				if (this.speedMultiplier > 0.35D) {
					this.speedMultiplier = 0.35D;
				}
			} else {
				this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;
				if (this.speedMultiplier < 0.07D) {
					this.speedMultiplier = 0.07D;
				}
			}

			for (int l = 0; l < 4; ++l) {
				int k1 = MathHelper.floor_double(this.posX + ((double) (l % 2) - 0.5D) * 0.8D);
				int l1 = MathHelper.floor_double(this.posZ + ((double) (l / 2) - 0.5D) * 0.8D);

				for (int i2 = 0; i2 < 2; ++i2) {
					int k = MathHelper.floor_double(this.posY) + i2;
					BlockPos blockpos = new BlockPos(k1, k, l1);
					Block block = this.worldObj.getBlockState(blockpos).getBlock();
					if (block == Blocks.snow_layer) {
						this.worldObj.setBlockToAir(blockpos);
						this.isCollidedHorizontally = false;
					} else if (block == Blocks.waterlily) {
						this.worldObj.destroyBlock(blockpos, true);
						this.isCollidedHorizontally = false;
					}
				}
			}

			if (this.onGround) {
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
				this.motionZ *= 0.5D;
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			if (this.isCollidedHorizontally && d9 > 0.2975D) {
				if (!this.worldObj.isRemote && !this.isDead) {
					this.setDead();
					if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
						for (int i1 = 0; i1 < 3; ++i1) {
							this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
						}

						for (int j1 = 0; j1 < 2; ++j1) {
							this.dropItemWithOffset(Items.stick, 1, 0.0F);
						}
					}
				}
			} else {
				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}

			this.rotationPitch = 0.0F;
			double d15 = (double) this.rotationYaw;
			double d18 = this.prevPosX - this.posX;
			double d21 = this.prevPosZ - this.posZ;
			if (d18 * d18 + d21 * d21 > 0.001D) {
				d15 = (double) ((float) (MathHelper.func_181159_b(d21, d18) * 180.0D / 3.141592653589793D));
			}

			double d23 = MathHelper.wrapAngleTo180_double(d15 - (double) this.rotationYaw);
			if (d23 > 20.0D) {
				d23 = 20.0D;
			}

			if (d23 < -20.0D) {
				d23 = -20.0D;
			}

			this.rotationYaw = (float) ((double) this.rotationYaw + d23);
			this.setRotation(this.rotationYaw, this.rotationPitch);
			if (!this.worldObj.isRemote) {
				List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
						this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
				if (list != null && !list.isEmpty()) {
					for (int j2 = 0; j2 < list.size(); ++j2) {
						Entity entity = (Entity) list.get(j2);
						if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityBoat) {
							entity.applyEntityCollision(this);
						}
					}
				}

				if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
					this.riddenByEntity = null;
				}

			}
		}
	}

	public void updateRiderPosition() {
		if (this.riddenByEntity != null) {
			double d0 = Math.cos((double) this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
			double d1 = Math.sin((double) this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
			this.riddenByEntity.setPosition(this.posX + d0,
					this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + d1);
		}
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound var1) {
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound var1) {
	}

	/**+
	 * First layer of player interaction
	 */
	public boolean interactFirst(EntityPlayer entityplayer) {
		if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer
				&& this.riddenByEntity != entityplayer) {
			return true;
		} else {
			if (!this.worldObj.isRemote) {
				entityplayer.mountEntity(this);
			}

			return true;
		}
	}

	protected void updateFallState(double d0, boolean flag, Block var4, BlockPos var5) {
		if (flag) {
			if (this.fallDistance > 3.0F) {
				this.fall(this.fallDistance, 1.0F);
				if (!this.worldObj.isRemote && !this.isDead) {
					this.setDead();
					if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
						for (int i = 0; i < 3; ++i) {
							this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
						}

						for (int j = 0; j < 2; ++j) {
							this.dropItemWithOffset(Items.stick, 1, 0.0F);
						}
					}
				}

				this.fallDistance = 0.0F;
			}
		} else if (this.worldObj.getBlockState((new BlockPos(this)).down()).getBlock().getMaterial() != Material.water
				&& d0 < 0.0D) {
			this.fallDistance = (float) ((double) this.fallDistance - d0);
		}

	}

	/**+
	 * Sets the damage taken from the last hit.
	 */
	public void setDamageTaken(float parFloat1) {
		this.dataWatcher.updateObject(19, Float.valueOf(parFloat1));
	}

	/**+
	 * Gets the damage taken from the last hit.
	 */
	public float getDamageTaken() {
		return this.dataWatcher.getWatchableObjectFloat(19);
	}

	/**+
	 * Sets the time to count down from since the last time entity
	 * was hit.
	 */
	public void setTimeSinceHit(int parInt1) {
		this.dataWatcher.updateObject(17, Integer.valueOf(parInt1));
	}

	/**+
	 * Gets the time since the last hit.
	 */
	public int getTimeSinceHit() {
		return this.dataWatcher.getWatchableObjectInt(17);
	}

	/**+
	 * Sets the forward direction of the entity.
	 */
	public void setForwardDirection(int parInt1) {
		this.dataWatcher.updateObject(18, Integer.valueOf(parInt1));
	}

	/**+
	 * Gets the forward direction of the entity.
	 */
	public int getForwardDirection() {
		return this.dataWatcher.getWatchableObjectInt(18);
	}

	/**+
	 * true if no player in boat
	 */
	public void setIsBoatEmpty(boolean parFlag) {
		this.isBoatEmpty = parFlag;
	}
}