package net.minecraft.village;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
public class MerchantRecipe {
	private ItemStack itemToBuy;
	private ItemStack secondItemToBuy;
	private ItemStack itemToSell;
	private int toolUses;
	private int maxTradeUses;
	private boolean rewardsExp;

	public MerchantRecipe(NBTTagCompound tagCompound) {
		this.readFromTags(tagCompound);
	}

	public MerchantRecipe(ItemStack buy1, ItemStack buy2, ItemStack sell) {
		this(buy1, buy2, sell, 0, 7);
	}

	public MerchantRecipe(ItemStack buy1, ItemStack buy2, ItemStack sell, int toolUsesIn, int maxTradeUsesIn) {
		this.itemToBuy = buy1;
		this.secondItemToBuy = buy2;
		this.itemToSell = sell;
		this.toolUses = toolUsesIn;
		this.maxTradeUses = maxTradeUsesIn;
		this.rewardsExp = true;
	}

	public MerchantRecipe(ItemStack buy1, ItemStack sell) {
		this(buy1, (ItemStack) null, sell);
	}

	public MerchantRecipe(ItemStack buy1, Item sellItem) {
		this(buy1, new ItemStack(sellItem));
	}

	/**+
	 * Gets the itemToBuy.
	 */
	public ItemStack getItemToBuy() {
		return this.itemToBuy;
	}

	/**+
	 * Gets secondItemToBuy.
	 */
	public ItemStack getSecondItemToBuy() {
		return this.secondItemToBuy;
	}

	/**+
	 * Gets if Villager has secondItemToBuy.
	 */
	public boolean hasSecondItemToBuy() {
		return this.secondItemToBuy != null;
	}

	/**+
	 * Gets itemToSell.
	 */
	public ItemStack getItemToSell() {
		return this.itemToSell;
	}

	public int getToolUses() {
		return this.toolUses;
	}

	public int getMaxTradeUses() {
		return this.maxTradeUses;
	}

	public void incrementToolUses() {
		++this.toolUses;
	}

	public void increaseMaxTradeUses(int increment) {
		this.maxTradeUses += increment;
	}

	public boolean isRecipeDisabled() {
		return this.toolUses >= this.maxTradeUses;
	}

	/**+
	 * Compensates {@link
	 * net.minecraft.village.MerchantRecipe#toolUses toolUses} with
	 * {@link net.minecraft.village.MerchantRecipe#maxTradeUses
	 * maxTradeUses}
	 */
	public void compensateToolUses() {
		this.toolUses = this.maxTradeUses;
	}

	public boolean getRewardsExp() {
		return this.rewardsExp;
	}

	public void readFromTags(NBTTagCompound tagCompound) {
		NBTTagCompound nbttagcompound = tagCompound.getCompoundTag("buy");
		this.itemToBuy = ItemStack.loadItemStackFromNBT(nbttagcompound);
		NBTTagCompound nbttagcompound1 = tagCompound.getCompoundTag("sell");
		this.itemToSell = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		if (tagCompound.hasKey("buyB", 10)) {
			this.secondItemToBuy = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("buyB"));
		}

		if (tagCompound.hasKey("uses", 99)) {
			this.toolUses = tagCompound.getInteger("uses");
		}

		if (tagCompound.hasKey("maxUses", 99)) {
			this.maxTradeUses = tagCompound.getInteger("maxUses");
		} else {
			this.maxTradeUses = 7;
		}

		if (tagCompound.hasKey("rewardExp", 1)) {
			this.rewardsExp = tagCompound.getBoolean("rewardExp");
		} else {
			this.rewardsExp = true;
		}

	}

	public NBTTagCompound writeToTags() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setTag("buy", this.itemToBuy.writeToNBT(new NBTTagCompound()));
		nbttagcompound.setTag("sell", this.itemToSell.writeToNBT(new NBTTagCompound()));
		if (this.secondItemToBuy != null) {
			nbttagcompound.setTag("buyB", this.secondItemToBuy.writeToNBT(new NBTTagCompound()));
		}

		nbttagcompound.setInteger("uses", this.toolUses);
		nbttagcompound.setInteger("maxUses", this.maxTradeUses);
		nbttagcompound.setBoolean("rewardExp", this.rewardsExp);
		return nbttagcompound;
	}
}