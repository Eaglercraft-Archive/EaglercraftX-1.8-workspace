package net.minecraft.item;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
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
public class ItemArmor extends Item {
	/**+
	 * Holds the 'base' maxDamage that each armorType have.
	 */
	private static final int[] maxDamageArray = new int[] { 11, 16, 15, 13 };
	public static final String[] EMPTY_SLOT_NAMES = new String[] { "minecraft:items/empty_armor_slot_helmet",
			"minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings",
			"minecraft:items/empty_armor_slot_boots" };
	private static final IBehaviorDispenseItem dispenserBehavior = new BehaviorDefaultDispenseItem() {
		protected ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
			BlockPos blockpos = iblocksource.getBlockPos()
					.offset(BlockDispenser.getFacing(iblocksource.getBlockMetadata()));
			int i = blockpos.getX();
			int j = blockpos.getY();
			int k = blockpos.getZ();
			AxisAlignedBB axisalignedbb = new AxisAlignedBB((double) i, (double) j, (double) k, (double) (i + 1),
					(double) (j + 1), (double) (k + 1));
			List list = iblocksource.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb,
					Predicates.and(EntitySelectors.NOT_SPECTATING, new EntitySelectors.ArmoredMob(itemstack)));
			if (list.size() > 0) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) list.get(0);
				int l = entitylivingbase instanceof EntityPlayer ? 1 : 0;
				int i1 = EntityLiving.getArmorPosition(itemstack);
				ItemStack itemstack1 = itemstack.copy();
				itemstack1.stackSize = 1;
				entitylivingbase.setCurrentItemOrArmor(i1 - l, itemstack1);
				if (entitylivingbase instanceof EntityLiving) {
					((EntityLiving) entitylivingbase).setEquipmentDropChance(i1, 2.0F);
				}

				--itemstack.stackSize;
				return itemstack;
			} else {
				return super.dispenseStack(iblocksource, itemstack);
			}
		}
	};
	public final int armorType;
	public final int damageReduceAmount;
	public final int renderIndex;
	private final ItemArmor.ArmorMaterial material;

	public ItemArmor(ItemArmor.ArmorMaterial material, int renderIndex, int armorType) {
		this.material = material;
		this.armorType = armorType;
		this.renderIndex = renderIndex;
		this.damageReduceAmount = material.getDamageReductionAmount(armorType);
		this.setMaxDamage(material.getDurability(armorType));
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabCombat);
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserBehavior);
	}

	public int getColorFromItemStack(ItemStack itemstack, int i) {
		if (i > 0) {
			return 16777215;
		} else {
			int j = this.getColor(itemstack);
			if (j < 0) {
				j = 16777215;
			}

			return j;
		}
	}

	/**+
	 * Return the enchantability factor of the item, most of the
	 * time is based on material.
	 */
	public int getItemEnchantability() {
		return this.material.getEnchantability();
	}

	/**+
	 * Return the armor material for this armor item.
	 */
	public ItemArmor.ArmorMaterial getArmorMaterial() {
		return this.material;
	}

	/**+
	 * Return whether the specified armor ItemStack has a color.
	 */
	public boolean hasColor(ItemStack parItemStack) {
		return this.material != ItemArmor.ArmorMaterial.LEATHER ? false
				: (!parItemStack.hasTagCompound() ? false
						: (!parItemStack.getTagCompound().hasKey("display", 10) ? false
								: parItemStack.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
	}

	/**+
	 * Return the color for the specified armor ItemStack.
	 */
	public int getColor(ItemStack stack) {
		if (this.material != ItemArmor.ArmorMaterial.LEATHER) {
			return -1;
		} else {
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			if (nbttagcompound != null) {
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
				if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3)) {
					return nbttagcompound1.getInteger("color");
				}
			}

			return 10511680;
		}
	}

	/**+
	 * Remove the color from the specified armor ItemStack.
	 */
	public void removeColor(ItemStack stack) {
		if (this.material == ItemArmor.ArmorMaterial.LEATHER) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			if (nbttagcompound != null) {
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
				if (nbttagcompound1.hasKey("color")) {
					nbttagcompound1.removeTag("color");
				}

			}
		}
	}

	/**+
	 * Sets the color of the specified armor ItemStack
	 */
	public void setColor(ItemStack stack, int color) {
		if (this.material != ItemArmor.ArmorMaterial.LEATHER) {
			throw new UnsupportedOperationException("Can\'t dye non-leather!");
		} else {
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			if (nbttagcompound == null) {
				nbttagcompound = new NBTTagCompound();
				stack.setTagCompound(nbttagcompound);
			}

			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
			if (!nbttagcompound.hasKey("display", 10)) {
				nbttagcompound.setTag("display", nbttagcompound1);
			}

			nbttagcompound1.setInteger("color", color);
		}
	}

	/**+
	 * Return whether this item is repairable in an anvil.
	 */
	public boolean getIsRepairable(ItemStack itemstack, ItemStack itemstack1) {
		return this.material.getRepairItem() == itemstack1.getItem() ? true
				: super.getIsRepairable(itemstack, itemstack1);
	}

	/**+
	 * Called whenever this item is equipped and the right mouse
	 * button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack itemstack, World var2, EntityPlayer entityplayer) {
		int i = EntityLiving.getArmorPosition(itemstack) - 1;
		ItemStack itemstack1 = entityplayer.getCurrentArmor(i);
		if (itemstack1 == null) {
			entityplayer.setCurrentItemOrArmor(i, itemstack.copy());
			itemstack.stackSize = 0;
		}

		return itemstack;
	}

	public static enum ArmorMaterial {
		LEATHER("leather", 5, new int[] { 1, 3, 2, 1 }, 15), CHAIN("chainmail", 15, new int[] { 2, 5, 4, 1 }, 12),
		IRON("iron", 15, new int[] { 2, 6, 5, 2 }, 9), GOLD("gold", 7, new int[] { 2, 5, 3, 1 }, 25),
		DIAMOND("diamond", 33, new int[] { 3, 8, 6, 3 }, 10);

		private final String name;
		private final int maxDamageFactor;
		private final int[] damageReductionAmountArray;
		private final int enchantability;

		private ArmorMaterial(String name, int maxDamage, int[] reductionAmounts, int enchantability) {
			this.name = name;
			this.maxDamageFactor = maxDamage;
			this.damageReductionAmountArray = reductionAmounts;
			this.enchantability = enchantability;
		}

		public int getDurability(int armorType) {
			return ItemArmor.maxDamageArray[armorType] * this.maxDamageFactor;
		}

		public int getDamageReductionAmount(int armorType) {
			return this.damageReductionAmountArray[armorType];
		}

		public int getEnchantability() {
			return this.enchantability;
		}

		public Item getRepairItem() {
			return this == LEATHER ? Items.leather
					: (this == CHAIN ? Items.iron_ingot
							: (this == GOLD ? Items.gold_ingot
									: (this == IRON ? Items.iron_ingot : (this == DIAMOND ? Items.diamond : null))));
		}

		public String getName() {
			return this.name;
		}
	}
}