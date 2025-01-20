package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
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
public class EntityPig extends EntityAnimal {
	private final EntityAIControlledByPlayer aiControlledByPlayer;

	public EntityPig(World worldIn) {
		super(worldIn);
		this.setSize(0.9F, 0.9F);
		((PathNavigateGround) this.getNavigator()).setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
		this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3F));
		this.tasks.addTask(3, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot_on_a_stick, false));
		this.tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot, false));
		this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	/**+
	 * returns true if all the conditions for steering the entity
	 * are met. For pigs, this is true if it is being ridden by a
	 * player and the player is holding a carrot-on-a-stick
	 */
	public boolean canBeSteered() {
		ItemStack itemstack = ((EntityPlayer) this.riddenByEntity).getHeldItem();
		return itemstack != null && itemstack.getItem() == Items.carrot_on_a_stick;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Saddle", this.getSaddled());
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setSaddled(nbttagcompound.getBoolean("Saddle"));
	}

	/**+
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.pig.say";
	}

	/**+
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.pig.say";
	}

	/**+
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.pig.death";
	}

	protected void playStepSound(BlockPos var1, Block var2) {
		this.playSound("mob.pig.step", 0.15F, 1.0F);
	}

	/**+
	 * Called when a player interacts with a mob. e.g. gets milk
	 * from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer entityplayer) {
		if (super.interact(entityplayer)) {
			return true;
		} else if (!this.getSaddled() || this.worldObj.isRemote
				|| this.riddenByEntity != null && this.riddenByEntity != entityplayer) {
			return false;
		} else {
			entityplayer.mountEntity(this);
			return true;
		}
	}

	protected Item getDropItem() {
		return this.isBurning() ? Items.cooked_porkchop : Items.porkchop;
	}

	/**+
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean var1, int i) {
		int j = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + i);

		for (int k = 0; k < j; ++k) {
			if (this.isBurning()) {
				this.dropItem(Items.cooked_porkchop, 1);
			} else {
				this.dropItem(Items.porkchop, 1);
			}
		}

		if (this.getSaddled()) {
			this.dropItem(Items.saddle, 1);
		}

	}

	/**+
	 * Returns true if the pig is saddled.
	 */
	public boolean getSaddled() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	/**+
	 * Set or remove the saddle of the pig.
	 */
	public void setSaddled(boolean saddled) {
		if (saddled) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
		}

	}

	/**+
	 * Called when a lightning bolt hits the entity.
	 */
	public void onStruckByLightning(EntityLightningBolt var1) {
		if (!this.worldObj.isRemote && !this.isDead) {
			EntityPigZombie entitypigzombie = new EntityPigZombie(this.worldObj);
			entitypigzombie.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
			entitypigzombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			entitypigzombie.setNoAI(this.isAIDisabled());
			if (this.hasCustomName()) {
				entitypigzombie.setCustomNameTag(this.getCustomNameTag());
				entitypigzombie.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
			}

			this.worldObj.spawnEntityInWorld(entitypigzombie);
			this.setDead();
		}
	}

	public void fall(float f, float f1) {
		super.fall(f, f1);
		if (f > 5.0F && this.riddenByEntity instanceof EntityPlayer) {
			((EntityPlayer) this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
		}

	}

	public EntityPig createChild(EntityAgeable var1) {
		return new EntityPig(this.worldObj);
	}

	/**+
	 * Checks if the parameter is an item which this animal can be
	 * fed to breed it (wheat, carrots or seeds depending on the
	 * animal type)
	 */
	public boolean isBreedingItem(ItemStack itemstack) {
		return itemstack != null && itemstack.getItem() == Items.carrot;
	}

	/**+
	 * Return the AI task for player control.
	 */
	public EntityAIControlledByPlayer getAIControlledByPlayer() {
		return this.aiControlledByPlayer;
	}
}