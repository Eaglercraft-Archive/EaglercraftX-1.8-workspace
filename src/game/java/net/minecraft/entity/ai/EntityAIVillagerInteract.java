package net.minecraft.entity.ai;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

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
public class EntityAIVillagerInteract extends EntityAIWatchClosest2 {
	private int interactionDelay;
	private EntityVillager villager;

	public EntityAIVillagerInteract(EntityVillager villagerIn) {
		super(villagerIn, EntityVillager.class, 3.0F, 0.02F);
		this.villager = villagerIn;
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		super.startExecuting();
		if (this.villager.canAbondonItems() && this.closestEntity instanceof EntityVillager
				&& ((EntityVillager) this.closestEntity).func_175557_cr()) {
			this.interactionDelay = 10;
		} else {
			this.interactionDelay = 0;
		}

	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		super.updateTask();
		if (this.interactionDelay > 0) {
			--this.interactionDelay;
			if (this.interactionDelay == 0) {
				InventoryBasic inventorybasic = this.villager.getVillagerInventory();

				for (int i = 0; i < inventorybasic.getSizeInventory(); ++i) {
					ItemStack itemstack = inventorybasic.getStackInSlot(i);
					ItemStack itemstack1 = null;
					if (itemstack != null) {
						Item item = itemstack.getItem();
						if ((item == Items.bread || item == Items.potato || item == Items.carrot)
								&& itemstack.stackSize > 3) {
							int l = itemstack.stackSize / 2;
							itemstack.stackSize -= l;
							itemstack1 = new ItemStack(item, l, itemstack.getMetadata());
						} else if (item == Items.wheat && itemstack.stackSize > 5) {
							int j = itemstack.stackSize / 2 / 3 * 3;
							int k = j / 3;
							itemstack.stackSize -= j;
							itemstack1 = new ItemStack(Items.bread, k, 0);
						}

						if (itemstack.stackSize <= 0) {
							inventorybasic.setInventorySlotContents(i, (ItemStack) null);
						}
					}

					if (itemstack1 != null) {
						double d0 = this.villager.posY - 0.30000001192092896D + (double) this.villager.getEyeHeight();
						EntityItem entityitem = new EntityItem(this.villager.worldObj, this.villager.posX, d0,
								this.villager.posZ, itemstack1);
						float f = 0.3F;
						float f1 = this.villager.rotationYawHead;
						float f2 = this.villager.rotationPitch;
						entityitem.motionX = (double) (-MathHelper.sin(f1 / 180.0F * 3.1415927F)
								* MathHelper.cos(f2 / 180.0F * 3.1415927F) * f);
						entityitem.motionZ = (double) (MathHelper.cos(f1 / 180.0F * 3.1415927F)
								* MathHelper.cos(f2 / 180.0F * 3.1415927F) * f);
						entityitem.motionY = (double) (-MathHelper.sin(f2 / 180.0F * 3.1415927F) * f + 0.1F);
						entityitem.setDefaultPickupDelay();
						this.villager.worldObj.spawnEntityInWorld(entityitem);
						break;
					}
				}
			}
		}

	}
}