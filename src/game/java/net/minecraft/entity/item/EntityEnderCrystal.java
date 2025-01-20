package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

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
public class EntityEnderCrystal extends Entity {
	public int innerRotation;
	public int health;

	public EntityEnderCrystal(World worldIn) {
		super(worldIn);
		this.preventEntitySpawning = true;
		this.setSize(2.0F, 2.0F);
		this.health = 5;
		this.innerRotation = this.rand.nextInt(100000);
	}

	public EntityEnderCrystal(World worldIn, double parDouble1, double parDouble2, double parDouble3) {
		this(worldIn);
		this.setPosition(parDouble1, parDouble2, parDouble3);
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
		this.dataWatcher.addObject(8, Integer.valueOf(this.health));
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		++this.innerRotation;
		this.dataWatcher.updateObject(8, Integer.valueOf(this.health));
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.posY);
		int k = MathHelper.floor_double(this.posZ);
		if (this.worldObj.provider instanceof WorldProviderEnd
				&& this.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock() != Blocks.fire) {
			this.worldObj.setBlockState(new BlockPos(i, j, k), Blocks.fire.getDefaultState());
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
	 * Returns true if other Entities should be prevented from
	 * moving through this Entity.
	 */
	public boolean canBeCollidedWith() {
		return true;
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource damagesource, float var2) {
		if (this.isEntityInvulnerable(damagesource)) {
			return false;
		} else {
			if (!this.isDead && !this.worldObj.isRemote) {
				this.health = 0;
				if (this.health <= 0) {
					this.setDead();
					if (!this.worldObj.isRemote) {
						this.worldObj.createExplosion((Entity) null, this.posX, this.posY, this.posZ, 6.0F, true);
					}
				}
			}

			return true;
		}
	}
}