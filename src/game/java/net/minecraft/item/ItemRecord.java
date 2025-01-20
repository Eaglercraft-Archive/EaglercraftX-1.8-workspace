package net.minecraft.item;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
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
public class ItemRecord extends Item {
	private static final Map<String, ItemRecord> RECORDS = Maps.newHashMap();
	public final String recordName;

	protected ItemRecord(String name) {
		this.recordName = name;
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabMisc);
		RECORDS.put("records." + name, this);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing var5, float var6, float var7, float var8) {
		IBlockState iblockstate = world.getBlockState(blockpos);
		if (iblockstate.getBlock() == Blocks.jukebox
				&& !((Boolean) iblockstate.getValue(BlockJukebox.HAS_RECORD)).booleanValue()) {
			if (world.isRemote) {
				return true;
			} else {
				((BlockJukebox) Blocks.jukebox).insertRecord(world, blockpos, iblockstate, itemstack);
				world.playAuxSFXAtEntity((EntityPlayer) null, 1005, blockpos, Item.getIdFromItem(this));
				--itemstack.stackSize;
				entityplayer.triggerAchievement(StatList.field_181740_X);
				return true;
			}
		} else {
			return false;
		}
	}

	/**+
	 * allows items to add custom lines of information to the
	 * mouseover description
	 */
	public void addInformation(ItemStack var1, EntityPlayer var2, List<String> list, boolean var4) {
		list.add(this.getRecordNameLocal());
	}

	public String getRecordNameLocal() {
		return StatCollector.translateToLocal("item.record." + this.recordName + ".desc");
	}

	/**+
	 * Return an item rarity from EnumRarity
	 */
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

	/**+
	 * Return the record item corresponding to the given name.
	 */
	public static ItemRecord getRecord(String name) {
		return (ItemRecord) RECORDS.get(name);
	}
}