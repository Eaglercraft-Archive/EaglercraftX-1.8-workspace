package net.minecraft.entity.item;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
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
public class EntityMinecartFurnace extends EntityMinecart {
	private int fuel;
	public double pushX;
	public double pushZ;

	public EntityMinecartFurnace(World worldIn) {
		super(worldIn);
	}

	public EntityMinecartFurnace(World worldIn, double parDouble1, double parDouble2, double parDouble3) {
		super(worldIn, parDouble1, parDouble2, parDouble3);
	}

	public EntityMinecart.EnumMinecartType getMinecartType() {
		return EntityMinecart.EnumMinecartType.FURNACE;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		if (this.fuel > 0) {
			--this.fuel;
		}

		if (this.fuel <= 0) {
			this.pushX = this.pushZ = 0.0D;
		}

		this.setMinecartPowered(this.fuel > 0);
		if (this.isMinecartPowered() && this.rand.nextInt(4) == 0) {
			this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.8D, this.posZ, 0.0D,
					0.0D, 0.0D, new int[0]);
		}

	}

	/**+
	 * Get's the maximum speed for a minecart
	 */
	protected double getMaximumSpeed() {
		return 0.2D;
	}

	public void killMinecart(DamageSource damagesource) {
		super.killMinecart(damagesource);
		if (!damagesource.isExplosion() && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
			this.entityDropItem(new ItemStack(Blocks.furnace, 1), 0.0F);
		}

	}

	protected void func_180460_a(BlockPos blockpos, IBlockState iblockstate) {
		super.func_180460_a(blockpos, iblockstate);
		double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
		if (d0 > 1.0E-4D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D) {
			d0 = (double) MathHelper.sqrt_double(d0);
			this.pushX /= d0;
			this.pushZ /= d0;
			if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D) {
				this.pushX = 0.0D;
				this.pushZ = 0.0D;
			} else {
				double d1 = d0 / this.getMaximumSpeed();
				this.pushX *= d1;
				this.pushZ *= d1;
			}
		}

	}

	protected void applyDrag() {
		double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
		if (d0 > 1.0E-4D) {
			d0 = (double) MathHelper.sqrt_double(d0);
			this.pushX /= d0;
			this.pushZ /= d0;
			double d1 = 1.0D;
			this.motionX *= 0.800000011920929D;
			this.motionY *= 0.0D;
			this.motionZ *= 0.800000011920929D;
			this.motionX += this.pushX * d1;
			this.motionZ += this.pushZ * d1;
		} else {
			this.motionX *= 0.9800000190734863D;
			this.motionY *= 0.0D;
			this.motionZ *= 0.9800000190734863D;
		}

		super.applyDrag();
	}

	/**+
	 * First layer of player interaction
	 */
	public boolean interactFirst(EntityPlayer entityplayer) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack != null && itemstack.getItem() == Items.coal) {
			if (!entityplayer.capabilities.isCreativeMode && --itemstack.stackSize == 0) {
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack) null);
			}

			this.fuel += 3600;
		}

		this.pushX = this.posX - entityplayer.posX;
		this.pushZ = this.posZ - entityplayer.posZ;
		return true;
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setDouble("PushX", this.pushX);
		nbttagcompound.setDouble("PushZ", this.pushZ);
		nbttagcompound.setShort("Fuel", (short) this.fuel);
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.pushX = nbttagcompound.getDouble("PushX");
		this.pushZ = nbttagcompound.getDouble("PushZ");
		this.fuel = nbttagcompound.getShort("Fuel");
	}

	protected boolean isMinecartPowered() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	protected void setMinecartPowered(boolean parFlag) {
		if (parFlag) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (this.dataWatcher.getWatchableObjectByte(16) | 1)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (this.dataWatcher.getWatchableObjectByte(16) & -2)));
		}

	}

	public IBlockState getDefaultDisplayTile() {
		return (this.isMinecartPowered() ? Blocks.lit_furnace : Blocks.furnace).getDefaultState()
				.withProperty(BlockFurnace.FACING, EnumFacing.NORTH);
	}
}