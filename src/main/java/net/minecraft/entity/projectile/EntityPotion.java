package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
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
public class EntityPotion extends EntityThrowable {
	private ItemStack potionDamage;

	public EntityPotion(World worldIn) {
		super(worldIn);
	}

	public EntityPotion(World worldIn, EntityLivingBase throwerIn, int meta) {
		this(worldIn, throwerIn, new ItemStack(Items.potionitem, 1, meta));
	}

	public EntityPotion(World worldIn, EntityLivingBase throwerIn, ItemStack potionDamageIn) {
		super(worldIn, throwerIn);
		this.potionDamage = potionDamageIn;
	}

	public EntityPotion(World worldIn, double x, double y, double z, int parInt1) {
		this(worldIn, x, y, z, new ItemStack(Items.potionitem, 1, parInt1));
	}

	public EntityPotion(World worldIn, double x, double y, double z, ItemStack potionDamageIn) {
		super(worldIn, x, y, z);
		this.potionDamage = potionDamageIn;
	}

	/**+
	 * Gets the amount of gravity to apply to the thrown entity with
	 * each tick.
	 */
	protected float getGravityVelocity() {
		return 0.05F;
	}

	protected float getVelocity() {
		return 0.5F;
	}

	protected float getInaccuracy() {
		return -20.0F;
	}

	/**+
	 * Sets the PotionEffect by the given id of the potion effect.
	 */
	public void setPotionDamage(int potionId) {
		if (this.potionDamage == null) {
			this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
		}

		this.potionDamage.setItemDamage(potionId);
	}

	/**+
	 * Returns the damage value of the thrown potion that this
	 * EntityPotion represents.
	 */
	public int getPotionDamage() {
		if (this.potionDamage == null) {
			this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
		}

		return this.potionDamage.getMetadata();
	}

	/**+
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition movingobjectposition) {

	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("Potion", 10)) {
			this.potionDamage = ItemStack.loadItemStackFromNBT(nbttagcompound.getCompoundTag("Potion"));
		} else {
			this.setPotionDamage(nbttagcompound.getInteger("potionValue"));
		}

		if (this.potionDamage == null) {
			this.setDead();
		}

	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		if (this.potionDamage != null) {
			nbttagcompound.setTag("Potion", this.potionDamage.writeToNBT(new NBTTagCompound()));
		}

	}
}