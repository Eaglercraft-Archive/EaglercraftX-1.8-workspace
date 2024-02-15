package net.minecraft.entity.monster;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
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
public class EntityGhast extends EntityFlying implements IMob {
	/**+
	 * The explosion radius of spawned fireballs.
	 */
	private int explosionStrength = 1;

	public EntityGhast(World worldIn) {
		super(worldIn);
		this.setSize(4.0F, 4.0F);
		this.isImmuneToFire = true;
		this.experienceValue = 5;
	}

	public boolean isAttacking() {
		return this.dataWatcher.getWatchableObjectByte(16) != 0;
	}

	public void setAttacking(boolean parFlag) {
		this.dataWatcher.updateObject(16, Byte.valueOf((byte) (parFlag ? 1 : 0)));
	}

	public int getFireballStrength() {
		return this.explosionStrength;
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		if (this.isEntityInvulnerable(damagesource)) {
			return false;
		} else if ("fireball".equals(damagesource.getDamageType())
				&& damagesource.getEntity() instanceof EntityPlayer) {
			super.attackEntityFrom(damagesource, 1000.0F);
			((EntityPlayer) damagesource.getEntity()).triggerAchievement(AchievementList.ghast);
			return true;
		} else {
			return super.attackEntityFrom(damagesource, f);
		}
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0D);
	}

	/**+
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.ghast.moan";
	}

	/**+
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.ghast.scream";
	}

	/**+
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.ghast.death";
	}

	protected Item getDropItem() {
		return Items.gunpowder;
	}

	/**+
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean var1, int i) {
		int j = this.rand.nextInt(2) + this.rand.nextInt(1 + i);

		for (int k = 0; k < j; ++k) {
			this.dropItem(Items.ghast_tear, 1);
		}

		j = this.rand.nextInt(3) + this.rand.nextInt(1 + i);

		for (int l = 0; l < j; ++l) {
			this.dropItem(Items.gunpowder, 1);
		}

	}

	/**+
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 10.0F;
	}

	/**+
	 * Checks if the entity's current position is a valid location
	 * to spawn this entity.
	 */
	public boolean getCanSpawnHere() {
		return this.rand.nextInt(20) == 0 && super.getCanSpawnHere()
				&& this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	/**+
	 * Will return how many at most can spawn in a chunk at once.
	 */
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("ExplosionPower", this.explosionStrength);
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("ExplosionPower", 99)) {
			this.explosionStrength = nbttagcompound.getInteger("ExplosionPower");
		}

	}

	public float getEyeHeight() {
		return 2.6F;
	}

}