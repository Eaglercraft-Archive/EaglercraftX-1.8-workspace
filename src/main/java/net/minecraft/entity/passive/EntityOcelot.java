package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class EntityOcelot extends EntityTameable {

	public EntityOcelot(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 0.7F);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(18, Byte.valueOf((byte) 0));
	}

	/**+
	 * Determines if an entity can be despawned, used on idle far
	 * away entities
	 */
	protected boolean canDespawn() {
		return !this.isTamed() && this.ticksExisted > 2400;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
	}

	public void fall(float var1, float var2) {
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("CatType", this.getTameSkin());
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setTameSkin(nbttagcompound.getInteger("CatType"));
	}

	/**+
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return this.isTamed()
				? (this.isInLove() ? "mob.cat.purr" : (this.rand.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow"))
				: "";
	}

	/**+
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.cat.hitt";
	}

	/**+
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.cat.hitt";
	}

	/**+
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 0.4F;
	}

	protected Item getDropItem() {
		return Items.leather;
	}

	public boolean attackEntityAsMob(Entity entity) {
		return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		if (this.isEntityInvulnerable(damagesource)) {
			return false;
		} else {
			return super.attackEntityFrom(damagesource, f);
		}
	}

	/**+
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean var1, int var2) {
	}

	public EntityOcelot createChild(EntityAgeable var1) {
		EntityOcelot entityocelot = new EntityOcelot(this.worldObj);
		if (this.isTamed()) {
			entityocelot.setOwnerId(this.getOwnerId());
			entityocelot.setTamed(true);
			entityocelot.setTameSkin(this.getTameSkin());
		}

		return entityocelot;
	}

	/**+
	 * Checks if the parameter is an item which this animal can be
	 * fed to breed it (wheat, carrots or seeds depending on the
	 * animal type)
	 */
	public boolean isBreedingItem(ItemStack itemstack) {
		return itemstack != null && itemstack.getItem() == Items.fish;
	}

	/**+
	 * Returns true if the mob is currently able to mate with the
	 * specified mob.
	 */
	public boolean canMateWith(EntityAnimal entityanimal) {
		if (entityanimal == this) {
			return false;
		} else if (!this.isTamed()) {
			return false;
		} else if (!(entityanimal instanceof EntityOcelot)) {
			return false;
		} else {
			EntityOcelot entityocelot = (EntityOcelot) entityanimal;
			return !entityocelot.isTamed() ? false : this.isInLove() && entityocelot.isInLove();
		}
	}

	public int getTameSkin() {
		return this.dataWatcher.getWatchableObjectByte(18);
	}

	public void setTameSkin(int skinId) {
		this.dataWatcher.updateObject(18, Byte.valueOf((byte) skinId));
	}

	/**+
	 * Checks if the entity's current position is a valid location
	 * to spawn this entity.
	 */
	public boolean getCanSpawnHere() {
		return this.worldObj.rand.nextInt(3) != 0;
	}

	/**+
	 * Checks that the entity is not colliding with any blocks /
	 * liquids
	 */
	public boolean isNotColliding() {
		if (this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this)
				&& this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty()
				&& !this.worldObj.isAnyLiquid(this.getEntityBoundingBox())) {
			BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
			if (blockpos.getY() < this.worldObj.func_181545_F()) {
				return false;
			}

			Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();
			if (block == Blocks.grass || block.getMaterial() == Material.leaves) {
				return true;
			}
		}

		return false;
	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return this.hasCustomName() ? this.getCustomNameTag()
				: (this.isTamed() ? StatCollector.translateToLocal("entity.Cat.name") : super.getName());
	}

	public void setTamed(boolean flag) {
		super.setTamed(flag);
	}

	/**+
	 * Called only once on an entity when first time spawned, via
	 * egg, mob spawner, natural spawning etc, but not called when
	 * entity is reloaded from nbt. Mainly used for initializing
	 * attributes and inventory
	 */
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyinstance,
			IEntityLivingData ientitylivingdata) {
		ientitylivingdata = super.onInitialSpawn(difficultyinstance, ientitylivingdata);
		if (this.worldObj.rand.nextInt(7) == 0) {
			for (int i = 0; i < 2; ++i) {
				EntityOcelot entityocelot = new EntityOcelot(this.worldObj);
				entityocelot.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				entityocelot.setGrowingAge(-24000);
				this.worldObj.spawnEntityInWorld(entityocelot);
			}
		}

		return ientitylivingdata;
	}
}