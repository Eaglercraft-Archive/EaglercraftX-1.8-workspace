package net.minecraft.entity.monster;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
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
public class EntitySnowman extends EntityGolem implements IRangedAttackMob {
	public EntitySnowman(World worldIn) {
		super(worldIn);
		this.setSize(0.7F, 1.9F);
		((PathNavigateGround) this.getNavigator()).setAvoidsWater(true);
		this.tasks.addTask(1, new EntityAIArrowAttack(this, 1.25D, 20, 10.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(4, new EntityAILookIdle(this));
		this.targetTasks.addTask(1,
				new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, false, IMob.mobSelector));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
	}

	/**+
	 * Called frequently so the entity can update its state every
	 * tick as required. For example, zombies and skeletons use this
	 * to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!this.worldObj.isRemote) {
			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY);
			int k = MathHelper.floor_double(this.posZ);
			if (this.isWet()) {
				this.attackEntityFrom(DamageSource.drown, 1.0F);
			}

			if (this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, k))
					.getFloatTemperature(new BlockPos(i, j, k)) > 1.0F) {
				this.attackEntityFrom(DamageSource.onFire, 1.0F);
			}

			for (int l = 0; l < 4; ++l) {
				i = MathHelper.floor_double(this.posX + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
				j = MathHelper.floor_double(this.posY);
				k = MathHelper.floor_double(this.posZ + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
				BlockPos blockpos = new BlockPos(i, j, k);
				if (this.worldObj.getBlockState(blockpos).getBlock().getMaterial() == Material.air
						&& this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, k))
								.getFloatTemperature(blockpos) < 0.8F
						&& Blocks.snow_layer.canPlaceBlockAt(this.worldObj, blockpos)) {
					this.worldObj.setBlockState(blockpos, Blocks.snow_layer.getDefaultState());
				}
			}
		}

	}

	protected Item getDropItem() {
		return Items.snowball;
	}

	/**+
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean var1, int var2) {
		int i = this.rand.nextInt(16);

		for (int j = 0; j < i; ++j) {
			this.dropItem(Items.snowball, 1);
		}

	}

	/**+
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLivingBase parEntityLivingBase, float parFloat1) {
		EntitySnowball entitysnowball = new EntitySnowball(this.worldObj, this);
		double d0 = parEntityLivingBase.posY + (double) parEntityLivingBase.getEyeHeight() - 1.100000023841858D;
		double d1 = parEntityLivingBase.posX - this.posX;
		double d2 = d0 - entitysnowball.posY;
		double d3 = parEntityLivingBase.posZ - this.posZ;
		float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3) * 0.2F;
		entitysnowball.setThrowableHeading(d1, d2 + (double) f, d3, 1.6F, 12.0F);
		this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(entitysnowball);
	}

	public float getEyeHeight() {
		return 1.7F;
	}
}