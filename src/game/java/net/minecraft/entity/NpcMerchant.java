package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

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
public class NpcMerchant implements IMerchant {
	private InventoryMerchant theMerchantInventory;
	private EntityPlayer customer;
	private MerchantRecipeList recipeList;
	private IChatComponent field_175548_d;

	public NpcMerchant(EntityPlayer parEntityPlayer, IChatComponent parIChatComponent) {
		this.customer = parEntityPlayer;
		this.field_175548_d = parIChatComponent;
		this.theMerchantInventory = new InventoryMerchant(parEntityPlayer, this);
	}

	public EntityPlayer getCustomer() {
		return this.customer;
	}

	public void setCustomer(EntityPlayer var1) {
	}

	public MerchantRecipeList getRecipes(EntityPlayer var1) {
		return this.recipeList;
	}

	public void setRecipes(MerchantRecipeList merchantrecipelist) {
		this.recipeList = merchantrecipelist;
	}

	public void useRecipe(MerchantRecipe merchantrecipe) {
		merchantrecipe.incrementToolUses();
	}

	/**+
	 * Notifies the merchant of a possible merchantrecipe being
	 * fulfilled or not. Usually, this is just a sound byte being
	 * played depending if the suggested itemstack is not null.
	 */
	public void verifySellingItem(ItemStack var1) {
	}

	/**+
	 * Get the formatted ChatComponent that will be used for the
	 * sender's username in chat
	 */
	public IChatComponent getDisplayName() {
		return (IChatComponent) (this.field_175548_d != null ? this.field_175548_d
				: new ChatComponentTranslation("entity.Villager.name", new Object[0]));
	}
}