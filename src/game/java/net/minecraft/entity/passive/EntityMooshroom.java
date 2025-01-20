package net.minecraft.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
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
public class EntityMooshroom extends EntityCow {
	public EntityMooshroom(World worldIn) {
		super(worldIn);
		this.setSize(0.9F, 1.3F);
		this.spawnableBlock = Blocks.mycelium;
	}

	/**+
	 * Called when a player interacts with a mob. e.g. gets milk
	 * from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer entityplayer) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack != null && itemstack.getItem() == Items.bowl && this.getGrowingAge() >= 0) {
			if (itemstack.stackSize == 1) {
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem,
						new ItemStack(Items.mushroom_stew));
				return true;
			}

			if (entityplayer.inventory.addItemStackToInventory(new ItemStack(Items.mushroom_stew))
					&& !entityplayer.capabilities.isCreativeMode) {
				entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);
				return true;
			}
		}

		if (itemstack != null && itemstack.getItem() == Items.shears && this.getGrowingAge() >= 0) {
			this.setDead();
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX,
					this.posY + (double) (this.height / 2.0F), this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
			if (!this.worldObj.isRemote) {
				EntityCow entitycow = new EntityCow(this.worldObj);
				entitycow.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
				entitycow.setHealth(this.getHealth());
				entitycow.renderYawOffset = this.renderYawOffset;
				if (this.hasCustomName()) {
					entitycow.setCustomNameTag(this.getCustomNameTag());
				}

				this.worldObj.spawnEntityInWorld(entitycow);

				for (int i = 0; i < 5; ++i) {
					this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX,
							this.posY + (double) this.height, this.posZ, new ItemStack(Blocks.red_mushroom)));
				}

				itemstack.damageItem(1, entityplayer);
				this.playSound("mob.sheep.shear", 1.0F, 1.0F);
			}

			return true;
		} else {
			return super.interact(entityplayer);
		}
	}

	public EntityMooshroom createChild(EntityAgeable var1) {
		return new EntityMooshroom(this.worldObj);
	}
}