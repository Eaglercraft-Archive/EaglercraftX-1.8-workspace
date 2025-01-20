package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
public class EntityFireworkRocket extends Entity {
	private int fireworkAge;
	private int lifetime;

	public EntityFireworkRocket(World worldIn) {
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}

	protected void entityInit() {
		this.dataWatcher.addObjectByDataType(8, 5);
	}

	/**+
	 * Checks if the entity is in range to render by using the past
	 * in distance and comparing it to its average edge length * 64
	 * * renderDistanceWeight Args: distance
	 */
	public boolean isInRangeToRenderDist(double d0) {
		return d0 < 4096.0D;
	}

	public EntityFireworkRocket(World worldIn, double x, double y, double z, ItemStack givenItem) {
		super(worldIn);
		this.fireworkAge = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(x, y, z);
		int i = 1;
		if (givenItem != null && givenItem.hasTagCompound()) {
			this.dataWatcher.updateObject(8, givenItem);
			NBTTagCompound nbttagcompound = givenItem.getTagCompound();
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");
			if (nbttagcompound1 != null) {
				i += nbttagcompound1.getByte("Flight");
			}
		}

		this.motionX = this.rand.nextGaussian() * 0.001D;
		this.motionZ = this.rand.nextGaussian() * 0.001D;
		this.motionY = 0.05D;
		this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);
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
		this.motionX *= 1.15D;
		this.motionZ *= 1.15D;
		this.motionY += 0.04D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
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
		if (this.fireworkAge == 0 && !this.isSilent()) {
			this.worldObj.playSoundAtEntity(this, "fireworks.launch", 3.0F, 1.0F);
		}

		++this.fireworkAge;
		if (this.worldObj.isRemote && this.fireworkAge % 2 < 2) {
			this.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3D, this.posZ,
					this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D,
					new int[0]);
		}

		if (!this.worldObj.isRemote && this.fireworkAge > this.lifetime) {
			this.worldObj.setEntityState(this, (byte) 17);
			this.setDead();
		}

	}

	public void handleStatusUpdate(byte b0) {
		if (b0 == 17 && this.worldObj.isRemote) {
			ItemStack itemstack = this.dataWatcher.getWatchableObjectItemStack(8);
			NBTTagCompound nbttagcompound = null;
			if (itemstack != null && itemstack.hasTagCompound()) {
				nbttagcompound = itemstack.getTagCompound().getCompoundTag("Fireworks");
			}

			this.worldObj.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ,
					nbttagcompound);
		}

		super.handleStatusUpdate(b0);
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("Life", this.fireworkAge);
		nbttagcompound.setInteger("LifeTime", this.lifetime);
		ItemStack itemstack = this.dataWatcher.getWatchableObjectItemStack(8);
		if (itemstack != null) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			itemstack.writeToNBT(nbttagcompound1);
			nbttagcompound.setTag("FireworksItem", nbttagcompound1);
		}

	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.fireworkAge = nbttagcompound.getInteger("Life");
		this.lifetime = nbttagcompound.getInteger("LifeTime");
		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("FireworksItem");
		if (nbttagcompound1 != null) {
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			if (itemstack != null) {
				this.dataWatcher.updateObject(8, itemstack);
			}
		}

	}

	/**+
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float f) {
		return super.getBrightness(f);
	}

	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
		return 1.0f;
	}

	public int getBrightnessForRender(float f) {
		return super.getBrightnessForRender(f);
	}

	/**+
	 * If returns false, the item will not inflict any damage
	 * against entities.
	 */
	public boolean canAttackWithItem() {
		return false;
	}
}