package net.minecraft.entity.item;

import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
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
public class EntityEnderEye extends Entity {
	private double targetX;
	private double targetY;
	private double targetZ;
	private int despawnTimer;
	private boolean shatterOrDrop;

	public EntityEnderEye(World worldIn) {
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

	public EntityEnderEye(World worldIn, double x, double y, double z) {
		super(worldIn);
		this.despawnTimer = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(x, y, z);
	}

	public void moveTowards(BlockPos parBlockPos) {
		double d0 = (double) parBlockPos.getX();
		int i = parBlockPos.getY();
		double d1 = (double) parBlockPos.getZ();
		double d2 = d0 - this.posX;
		double d3 = d1 - this.posZ;
		float f = MathHelper.sqrt_double(d2 * d2 + d3 * d3);
		if (f > 12.0F) {
			this.targetX = this.posX + d2 / (double) f * 12.0D;
			this.targetZ = this.posZ + d3 / (double) f * 12.0D;
			this.targetY = this.posY + 8.0D;
		} else {
			this.targetX = d0;
			this.targetY = (double) i;
			this.targetZ = d1;
		}

		this.despawnTimer = 0;
		this.shatterOrDrop = this.rand.nextInt(5) > 0;
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
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D
				/ 3.1415927410125732D);

		for (this.rotationPitch = (float) (MathHelper.func_181159_b(this.motionY, (double) f) * 180.0D
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
		if (!this.worldObj.isRemote) {
			double d0 = this.targetX - this.posX;
			double d1 = this.targetZ - this.posZ;
			float f1 = (float) Math.sqrt(d0 * d0 + d1 * d1);
			float f2 = (float) MathHelper.func_181159_b(d1, d0);
			double d2 = (double) f + (double) (f1 - f) * 0.0025D;
			if (f1 < 1.0F) {
				d2 *= 0.8D;
				this.motionY *= 0.8D;
			}

			this.motionX = Math.cos((double) f2) * d2;
			this.motionZ = Math.sin((double) f2) * d2;
			if (this.posY < this.targetY) {
				this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
			} else {
				this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
			}
		}

		float f3 = 0.25F;
		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double) f3,
						this.posY - this.motionY * (double) f3, this.posZ - this.motionZ * (double) f3, this.motionX,
						this.motionY, this.motionZ, new int[0]);
			}
		} else {
			this.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
					this.posX - this.motionX * (double) f3 + this.rand.nextDouble() * 0.6D - 0.3D,
					this.posY - this.motionY * (double) f3 - 0.5D,
					this.posZ - this.motionZ * (double) f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX,
					this.motionY, this.motionZ, new int[0]);
		}

		if (!this.worldObj.isRemote) {
			this.setPosition(this.posX, this.posY, this.posZ);
			++this.despawnTimer;
			if (this.despawnTimer > 80 && !this.worldObj.isRemote) {
				this.setDead();
				if (this.shatterOrDrop) {
					this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ,
							new ItemStack(Items.ender_eye)));
				} else {
					this.worldObj.playAuxSFX(2003, new BlockPos(this), 0);
				}
			}
		}

	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound var1) {
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound var1) {
	}

	/**+
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float var1) {
		return 1.0F;
	}

	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
		return 0.5f;
	}

	protected void renderDynamicLightsEaglerAt(double entityX, double entityY, double entityZ, double renderX,
			double renderY, double renderZ, float partialTicks, boolean isInFrustum) {
		super.renderDynamicLightsEaglerAt(entityX, entityY, entityZ, renderX, renderY, renderZ, partialTicks,
				isInFrustum);
		if (isInFrustum && renderX * renderX + renderY * renderY + renderZ * renderZ < 150.0) {
			float mag = 0.5f;
			DynamicLightManager.renderDynamicLight("entity_" + getEntityId() + "_endereye", entityX, entityY + 0.2,
					entityZ, mag * 0.1990f, mag * 0.7750f, mag * 0.4130f, false);
		}
	}

	public int getBrightnessForRender(float var1) {
		return 15728880;
	}

	/**+
	 * If returns false, the item will not inflict any damage
	 * against entities.
	 */
	public boolean canAttackWithItem() {
		return false;
	}
}