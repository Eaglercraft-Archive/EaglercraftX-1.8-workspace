package net.minecraft.client.gui.inventory;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

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
public class CreativeCrafting implements ICrafting {
	private final Minecraft mc;

	public CreativeCrafting(Minecraft mc) {
		this.mc = mc;
	}

	/**+
	 * update the crafting window inventory with the items in the
	 * list
	 */
	public void updateCraftingInventory(Container var1, List<ItemStack> var2) {
	}

	/**+
	 * Sends the contents of an inventory slot to the client-side
	 * Container. This doesn't have to match the actual contents of
	 * that slot. Args: Container, slot number, slot contents
	 */
	public void sendSlotContents(Container var1, int i, ItemStack itemstack) {
		this.mc.playerController.sendSlotPacket(itemstack, i);
	}

	/**+
	 * Sends two ints to the client-side Container. Used for furnace
	 * burning time, smelting progress, brewing progress, and
	 * enchanting level. Normally the first int identifies which
	 * variable to update, and the second contains the new value.
	 * Both are truncated to shorts in non-local SMP.
	 */
	public void sendProgressBarUpdate(Container var1, int var2, int var3) {
	}

	public void func_175173_a(Container var1, IInventory var2) {
	}
}