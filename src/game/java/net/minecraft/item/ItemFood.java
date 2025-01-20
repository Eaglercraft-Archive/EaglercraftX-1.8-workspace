package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
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
public class ItemFood extends Item {
	public final int itemUseDuration;
	private final int healAmount;
	private final float saturationModifier;
	private final boolean isWolfsFavoriteMeat;
	private boolean alwaysEdible;
	private int potionId;
	private int potionDuration;
	private int potionAmplifier;
	private float potionEffectProbability;

	public ItemFood(int amount, float saturation, boolean isWolfFood) {
		this.itemUseDuration = 32;
		this.healAmount = amount;
		this.isWolfsFavoriteMeat = isWolfFood;
		this.saturationModifier = saturation;
		this.setCreativeTab(CreativeTabs.tabFood);
	}

	public ItemFood(int amount, boolean isWolfFood) {
		this(amount, 0.6F, isWolfFood);
	}

	/**+
	 * Called when the player finishes using this Item (E.g.
	 * finishes eating.). Not called when the player stops using the
	 * Item before the action is complete.
	 */
	public ItemStack onItemUseFinish(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		--itemstack.stackSize;
		entityplayer.getFoodStats().addStats(this, itemstack);
		world.playSoundAtEntity(entityplayer, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		this.onFoodEaten(itemstack, world, entityplayer);
		entityplayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
		return itemstack;
	}

	protected void onFoodEaten(ItemStack var1, World world, EntityPlayer entityplayer) {
		if (!world.isRemote && this.potionId > 0 && world.rand.nextFloat() < this.potionEffectProbability) {
			entityplayer
					.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier));
		}

	}

	/**+
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack var1) {
		return 32;
	}

	/**+
	 * returns the action that specifies what animation to play when
	 * the items is being used
	 */
	public EnumAction getItemUseAction(ItemStack var1) {
		return EnumAction.EAT;
	}

	/**+
	 * Called whenever this item is equipped and the right mouse
	 * button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack itemstack, World var2, EntityPlayer entityplayer) {
		if (entityplayer.canEat(this.alwaysEdible)) {
			entityplayer.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
		}

		return itemstack;
	}

	public int getHealAmount(ItemStack var1) {
		return this.healAmount;
	}

	public float getSaturationModifier(ItemStack var1) {
		return this.saturationModifier;
	}

	/**+
	 * Whether wolves like this food (true for raw and cooked
	 * porkchop).
	 */
	public boolean isWolfsFavoriteMeat() {
		return this.isWolfsFavoriteMeat;
	}

	/**+
	 * sets a potion effect on the item. Args: int potionId, int
	 * duration (will be multiplied by 20), int amplifier, float
	 * probability of effect happening
	 */
	public ItemFood setPotionEffect(int id, int duration, int amplifier, float probability) {
		this.potionId = id;
		this.potionDuration = duration;
		this.potionAmplifier = amplifier;
		this.potionEffectProbability = probability;
		return this;
	}

	/**+
	 * Set the field 'alwaysEdible' to true, and make the food
	 * edible even if the player don't need to eat.
	 */
	public ItemFood setAlwaysEdible() {
		this.alwaysEdible = true;
		return this;
	}
}