package net.minecraft.entity.monster;

import java.util.Set;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
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
public class EntityEnderman extends EntityMob {
	private static final EaglercraftUUID attackingSpeedBoostModifierUUID = EaglercraftUUID
			.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
	private static final AttributeModifier attackingSpeedBoostModifier = (new AttributeModifier(
			attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.15000000596046448D, 0)).setSaved(false);
	private static final Set<Block> carriableBlocks = Sets.newIdentityHashSet();
	private boolean isAggressive;

	public EntityEnderman(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 2.9F);
		this.stepHeight = 1.0F;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Short((short) 0));
		this.dataWatcher.addObject(17, new Byte((byte) 0));
		this.dataWatcher.addObject(18, new Byte((byte) 0));
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		IBlockState iblockstate = this.getHeldBlockState();
		nbttagcompound.setShort("carried", (short) Block.getIdFromBlock(iblockstate.getBlock()));
		nbttagcompound.setShort("carriedData", (short) iblockstate.getBlock().getMetaFromState(iblockstate));
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		IBlockState iblockstate;
		if (nbttagcompound.hasKey("carried", 8)) {
			iblockstate = Block.getBlockFromName(nbttagcompound.getString("carried"))
					.getStateFromMeta(nbttagcompound.getShort("carriedData") & '\uffff');
		} else {
			iblockstate = Block.getBlockById(nbttagcompound.getShort("carried"))
					.getStateFromMeta(nbttagcompound.getShort("carriedData") & '\uffff');
		}

		this.setHeldBlockState(iblockstate);
	}

	public float getEyeHeight() {
		return 2.55F;
	}

	/**+
	 * Called frequently so the entity can update its state every
	 * tick as required. For example, zombies and skeletons use this
	 * to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		for (int i = 0; i < 2; ++i) {
			this.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
					this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
					this.posY + this.rand.nextDouble() * (double) this.height - 0.25D,
					this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width,
					(this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
					(this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
		}
		this.isJumping = false;
		super.onLivingUpdate();
	}

	protected void updateAITasks() {
		if (this.isWet()) {
			this.attackEntityFrom(DamageSource.drown, 1.0F);
		}

		if (this.isScreaming() && !this.isAggressive && this.rand.nextInt(100) == 0) {
			this.setScreaming(false);
		}

		if (this.worldObj.isDaytime()) {
			float f = this.getBrightness(1.0F);
			if (f > 0.5F && this.worldObj.canSeeSky(new BlockPos(this))
					&& this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
				this.setAttackTarget((EntityLivingBase) null);
				this.setScreaming(false);
				this.isAggressive = false;
				this.teleportRandomly();
			}
		}

		super.updateAITasks();
	}

	/**+
	 * Teleport the enderman to a random nearby position
	 */
	protected boolean teleportRandomly() {
		double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = this.posY + (double) (this.rand.nextInt(64) - 32);
		double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
		return this.teleportTo(d0, d1, d2);
	}

	/**+
	 * Teleport the enderman to another entity
	 */
	protected boolean teleportToEntity(Entity parEntity) {
		Vec3 vec3 = new Vec3(this.posX - parEntity.posX, this.getEntityBoundingBox().minY
				+ (double) (this.height / 2.0F) - parEntity.posY + (double) parEntity.getEyeHeight(),
				this.posZ - parEntity.posZ);
		vec3 = vec3.normalize();
		double d0 = 16.0D;
		double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.xCoord * d0;
		double d2 = this.posY + (double) (this.rand.nextInt(16) - 8) - vec3.yCoord * d0;
		double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.zCoord * d0;
		return this.teleportTo(d1, d2, d3);
	}

	/**+
	 * Teleport the enderman
	 */
	protected boolean teleportTo(double x, double y, double z) {
		double d0 = this.posX;
		double d1 = this.posY;
		double d2 = this.posZ;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		boolean flag = false;
		BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
		if (this.worldObj.isBlockLoaded(blockpos)) {
			boolean flag1 = false;

			while (!flag1 && blockpos.getY() > 0) {
				BlockPos blockpos1 = blockpos.down();
				Block block = this.worldObj.getBlockState(blockpos1).getBlock();
				if (block.getMaterial().blocksMovement()) {
					flag1 = true;
				} else {
					--this.posY;
					blockpos = blockpos1;
				}
			}

			if (flag1) {
				super.setPositionAndUpdate(this.posX, this.posY, this.posZ);
				if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty()
						&& !this.worldObj.isAnyLiquid(this.getEntityBoundingBox())) {
					flag = true;
				}
			}
		}

		if (!flag) {
			this.setPosition(d0, d1, d2);
			return false;
		} else {
			short short1 = 128;

			for (int i = 0; i < short1; ++i) {
				double d6 = (double) i / ((double) short1 - 1.0D);
				float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
				float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
				float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
				double d3 = d0 + (this.posX - d0) * d6 + (this.rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
				double d4 = d1 + (this.posY - d1) * d6 + this.rand.nextDouble() * (double) this.height;
				double d5 = d2 + (this.posZ - d2) * d6 + (this.rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
				this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, (double) f, (double) f1, (double) f2,
						new int[0]);
			}

			this.worldObj.playSoundEffect(d0, d1, d2, "mob.endermen.portal", 1.0F, 1.0F);
			this.playSound("mob.endermen.portal", 1.0F, 1.0F);
			return true;
		}
	}

	/**+
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return this.isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
	}

	/**+
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.endermen.hit";
	}

	/**+
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.endermen.death";
	}

	protected Item getDropItem() {
		return Items.ender_pearl;
	}

	/**+
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean var1, int i) {
		Item item = this.getDropItem();
		if (item != null) {
			int j = this.rand.nextInt(2 + i);

			for (int k = 0; k < j; ++k) {
				this.dropItem(item, 1);
			}
		}

	}

	/**+
	 * Sets this enderman's held block state
	 */
	public void setHeldBlockState(IBlockState state) {
		this.dataWatcher.updateObject(16, Short.valueOf((short) (Block.getStateId(state) & '\uffff')));
	}

	/**+
	 * Gets this enderman's held block state
	 */
	public IBlockState getHeldBlockState() {
		return Block.getStateById(this.dataWatcher.getWatchableObjectShort(16) & '\uffff');
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		if (this.isEntityInvulnerable(damagesource)) {
			return false;
		} else {
			if (damagesource.getEntity() == null || !(damagesource.getEntity() instanceof EntityEndermite)) {

				if (damagesource instanceof EntityDamageSource && damagesource.getEntity() instanceof EntityPlayer) {
					this.isAggressive = true;
				}

				if (damagesource instanceof EntityDamageSourceIndirect) {
					this.isAggressive = false;

					for (int i = 0; i < 64; ++i) {
						if (this.teleportRandomly()) {
							return true;
						}
					}

					return false;
				}
			}

			boolean flag = super.attackEntityFrom(damagesource, f);
			if (damagesource.isUnblockable() && this.rand.nextInt(10) != 0) {
				this.teleportRandomly();
			}

			return flag;
		}
	}

	public boolean isScreaming() {
		return this.dataWatcher.getWatchableObjectByte(18) > 0;
	}

	public void setScreaming(boolean screaming) {
		this.dataWatcher.updateObject(18, Byte.valueOf((byte) (screaming ? 1 : 0)));
	}

	public static void bootstrap() {
		carriableBlocks.add(Blocks.grass);
		carriableBlocks.add(Blocks.dirt);
		carriableBlocks.add(Blocks.sand);
		carriableBlocks.add(Blocks.gravel);
		carriableBlocks.add(Blocks.yellow_flower);
		carriableBlocks.add(Blocks.red_flower);
		carriableBlocks.add(Blocks.brown_mushroom);
		carriableBlocks.add(Blocks.red_mushroom);
		carriableBlocks.add(Blocks.tnt);
		carriableBlocks.add(Blocks.cactus);
		carriableBlocks.add(Blocks.clay);
		carriableBlocks.add(Blocks.pumpkin);
		carriableBlocks.add(Blocks.melon_block);
		carriableBlocks.add(Blocks.mycelium);
	}

}