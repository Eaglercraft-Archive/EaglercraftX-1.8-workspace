package net.minecraft.entity.monster;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
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
public class EntitySilverfish extends EntityMob {
	private EntitySilverfish.AISummonSilverfish summonSilverfish;

	public EntitySilverfish(World worldIn) {
		super(worldIn);
		this.setSize(0.4F, 0.3F);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, this.summonSilverfish = new EntitySilverfish.AISummonSilverfish(this));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.tasks.addTask(5, new EntitySilverfish.AIHideInStone(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	/**+
	 * Returns the Y Offset of this entity.
	 */
	public double getYOffset() {
		return 0.2D;
	}

	public float getEyeHeight() {
		return 0.1F;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
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

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		if (this.isEntityInvulnerable(damagesource)) {
			return false;
		} else {
			if (damagesource instanceof EntityDamageSource || damagesource == DamageSource.magic) {
				this.summonSilverfish.func_179462_f();
			}

			return super.attackEntityFrom(damagesource, f);
		}
	}

	protected void playStepSound(BlockPos var1, Block var2) {
		this.playSound("mob.silverfish.step", 0.15F, 1.0F);
	}

	protected Item getDropItem() {
		return null;
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.renderYawOffset = this.rotationYaw;
		super.onUpdate();
	}

	public float getBlockPathWeight(BlockPos blockpos) {
		return this.worldObj.getBlockState(blockpos.down()).getBlock() == Blocks.stone ? 10.0F
				: super.getBlockPathWeight(blockpos);
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

	static class AIHideInStone extends EntityAIWander {
		private final EntitySilverfish field_179485_a;
		private EnumFacing facing;
		private boolean field_179484_c;

		public AIHideInStone(EntitySilverfish parEntitySilverfish) {
			super(parEntitySilverfish, 1.0D, 10);
			this.field_179485_a = parEntitySilverfish;
			this.setMutexBits(1);
		}

		public boolean shouldExecute() {
			if (this.field_179485_a.getAttackTarget() != null) {
				return false;
			} else if (!this.field_179485_a.getNavigator().noPath()) {
				return false;
			} else {
				EaglercraftRandom random = this.field_179485_a.getRNG();
				if (random.nextInt(10) == 0) {
					this.facing = EnumFacing.random(random);
					BlockPos blockpos = (new BlockPos(this.field_179485_a.posX, this.field_179485_a.posY + 0.5D,
							this.field_179485_a.posZ)).offset(this.facing);
					IBlockState iblockstate = this.field_179485_a.worldObj.getBlockState(blockpos);
					if (BlockSilverfish.canContainSilverfish(iblockstate)) {
						this.field_179484_c = true;
						return true;
					}
				}

				this.field_179484_c = false;
				return super.shouldExecute();
			}
		}

		public boolean continueExecuting() {
			return this.field_179484_c ? false : super.continueExecuting();
		}

		public void startExecuting() {
			if (!this.field_179484_c) {
				super.startExecuting();
			} else {
				World world = this.field_179485_a.worldObj;
				BlockPos blockpos = (new BlockPos(this.field_179485_a.posX, this.field_179485_a.posY + 0.5D,
						this.field_179485_a.posZ)).offset(this.facing);
				IBlockState iblockstate = world.getBlockState(blockpos);
				if (BlockSilverfish.canContainSilverfish(iblockstate)) {
					world.setBlockState(blockpos, Blocks.monster_egg.getDefaultState().withProperty(
							BlockSilverfish.VARIANT, BlockSilverfish.EnumType.forModelBlock(iblockstate)), 3);
					this.field_179485_a.spawnExplosionParticle();
					this.field_179485_a.setDead();
				}

			}
		}
	}

	static class AISummonSilverfish extends EntityAIBase {
		private EntitySilverfish silverfish;
		private int field_179463_b;

		public AISummonSilverfish(EntitySilverfish parEntitySilverfish) {
			this.silverfish = parEntitySilverfish;
		}

		public void func_179462_f() {
			if (this.field_179463_b == 0) {
				this.field_179463_b = 20;
			}

		}

		public boolean shouldExecute() {
			return this.field_179463_b > 0;
		}

		public void updateTask() {
			--this.field_179463_b;
			if (this.field_179463_b <= 0) {
				World world = this.silverfish.worldObj;
				EaglercraftRandom random = this.silverfish.getRNG();
				BlockPos blockpos = new BlockPos(this.silverfish);

				for (int i = 0; i <= 5 && i >= -5; i = i <= 0 ? 1 - i : 0 - i) {
					for (int j = 0; j <= 10 && j >= -10; j = j <= 0 ? 1 - j : 0 - j) {
						for (int k = 0; k <= 10 && k >= -10; k = k <= 0 ? 1 - k : 0 - k) {
							BlockPos blockpos1 = blockpos.add(j, i, k);
							IBlockState iblockstate = world.getBlockState(blockpos1);
							if (iblockstate.getBlock() == Blocks.monster_egg) {
								if (world.getGameRules().getBoolean("mobGriefing")) {
									world.destroyBlock(blockpos1, true);
								} else {
									world.setBlockState(blockpos1,
											((BlockSilverfish.EnumType) iblockstate.getValue(BlockSilverfish.VARIANT))
													.getModelBlock(),
											3);
								}

								if (random.nextBoolean()) {
									return;
								}
							}
						}
					}
				}
			}

		}
	}
}