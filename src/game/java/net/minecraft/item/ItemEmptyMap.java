package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

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
public class ItemEmptyMap extends ItemMapBase {
	protected ItemEmptyMap() {
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	/**+
	 * Called whenever this item is equipped and the right mouse
	 * button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		ItemStack itemstack1 = new ItemStack(Items.filled_map, 1, world.getUniqueDataId("map"));
		String s = "map_" + itemstack1.getMetadata();
		MapData mapdata = new MapData(s);
		world.setItemData(s, mapdata);
		mapdata.scale = 0;
		mapdata.calculateMapCenter(entityplayer.posX, entityplayer.posZ, mapdata.scale);
		mapdata.dimension = (byte) world.provider.getDimensionId();
		mapdata.markDirty();
		--itemstack.stackSize;
		if (itemstack.stackSize <= 0) {
			return itemstack1;
		} else {
			if (!entityplayer.inventory.addItemStackToInventory(itemstack1.copy())) {
				entityplayer.dropPlayerItemWithRandomChoice(itemstack1, false);
			}

			entityplayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
			return itemstack;
		}
	}
}