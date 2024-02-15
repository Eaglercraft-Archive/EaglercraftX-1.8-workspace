package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.monster;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.Block;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.EnumCreatureAttribute;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.SharedMonsterAttributes;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.ai.EntityAILookIdle;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.ai.EntityAISwimming;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.ai.EntityAIWander;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.ai.EntityAIWatchClosest;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.monster.EntityMob;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.BlockPos;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.EnumParticleTypes;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.World;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

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
public class EntityEndermite extends EntityMob {

	static {
		__checkIntegratedContextValid("net/minecraft/entity/monster/EntityEndermite");
	}

	private int lifetime = 0;
	private boolean playerSpawned = false;

	public EntityEndermite(World worldIn) {
		super(worldIn);
		this.experienceValue = 3;
		this.setSize(0.4F, 0.3F);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	public float getEyeHeight() {
		return 0.1F;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
	}

	/**+
	 * returns if this entity triggers Block.onEntityWalking on the
	 * blocks they walk on. used for spiders and wolves to prevent
	 * them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	/**+
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.silverfish.say";
	}

	/**+
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.silverfish.hit";
	}

	/**+
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.silverfish.kill";
	}

	protected void playStepSound(BlockPos var1, Block var2) {
		this.playSound("mob.silverfish.step", 0.15F, 1.0F);
	}

	protected Item getDropItem() {
		return null;
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.lifetime = nbttagcompound.getInteger("Lifetime");
		this.playerSpawned = nbttagcompound.getBoolean("PlayerSpawned");
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("Lifetime", this.lifetime);
		nbttagcompound.setBoolean("PlayerSpawned", this.playerSpawned);
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.renderYawOffset = this.rotationYaw;
		super.onUpdate();
	}

	public boolean isSpawnedByPlayer() {
		return this.playerSpawned;
	}

	/**+
	 * Sets if this mob was spawned by a player or not.
	 */
	public void setSpawnedByPlayer(boolean spawnedByPlayer) {
		this.playerSpawned = spawnedByPlayer;
	}

	/**+
	 * Called frequently so the entity can update its state every
	 * tick as required. For example, zombies and skeletons use this
	 * to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();
		{
			if (!this.isNoDespawnRequired()) {
				++this.lifetime;
			}

			if (this.lifetime >= 2400) {
				this.setDead();
			}
		}
	}

	/**+
	 * Checks to make sure the light is not too bright where the mob
	 * is spawning
	 */
	protected boolean isValidLightLevel() {
		return true;
	}

	/**+
	 * Checks if the entity's current position is a valid location
	 * to spawn this entity.
	 */
	public boolean getCanSpawnHere() {
		if (super.getCanSpawnHere()) {
			EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
			return entityplayer == null;
		} else {
			return false;
		}
	}

	/**+
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}
}