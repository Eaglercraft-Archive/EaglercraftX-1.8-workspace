package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayerMP;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.inventory.Slot;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.Item;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemStack;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.play.server.S2FPacketSetSlot;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.stats.StatList;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.ChatComponentProcessor;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.ChatComponentText;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.EnumChatFormatting;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.IChatComponent;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.StatCollector;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.StringUtils;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.World;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class ItemEditableBook extends Item {

	static {
		__checkIntegratedContextValid("net/minecraft/item/ItemEditableBook");
	}

	public ItemEditableBook() {
		this.setMaxStackSize(1);
	}

	public static boolean validBookTagContents(NBTTagCompound nbt) {
		if (!ItemWritableBook.isNBTValid(nbt)) {
			return false;
		} else if (!nbt.hasKey("title", 8)) {
			return false;
		} else {
			String s = nbt.getString("title");
			return s != null && s.length() <= 32 ? nbt.hasKey("author", 8) : false;
		}
	}

	/**+
	 * Gets the generation of the book (how many times it has been
	 * cloned)
	 */
	public static int getGeneration(ItemStack book) {
		return book.getTagCompound().getInteger("generation");
	}

	public String getItemStackDisplayName(ItemStack itemstack) {
		if (itemstack.hasTagCompound()) {
			NBTTagCompound nbttagcompound = itemstack.getTagCompound();
			String s = nbttagcompound.getString("title");
			if (!StringUtils.isNullOrEmpty(s)) {
				return s;
			}
		}

		return super.getItemStackDisplayName(itemstack);
	}

	/**+
	 * allows items to add custom lines of information to the
	 * mouseover description
	 */
	public void addInformation(ItemStack itemstack, EntityPlayer var2, List<String> list, boolean var4) {
		if (itemstack.hasTagCompound()) {
			NBTTagCompound nbttagcompound = itemstack.getTagCompound();
			String s = nbttagcompound.getString("author");
			if (!StringUtils.isNullOrEmpty(s)) {
				list.add(EnumChatFormatting.GRAY
						+ StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] { s }));
			}

			list.add(EnumChatFormatting.GRAY
					+ StatCollector.translateToLocal("book.generation." + nbttagcompound.getInteger("generation")));
		}

	}

	/**+
	 * Called whenever this item is equipped and the right mouse
	 * button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		this.resolveContents(itemstack, entityplayer);

		entityplayer.displayGUIBook(itemstack);
		entityplayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
		return itemstack;
	}

	private void resolveContents(ItemStack stack, EntityPlayer player) {
		if (stack != null && stack.getTagCompound() != null) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			if (!nbttagcompound.getBoolean("resolved")) {
				nbttagcompound.setBoolean("resolved", true);
				if (validBookTagContents(nbttagcompound)) {
					NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);

					for (int i = 0; i < nbttaglist.tagCount(); ++i) {
						String s = nbttaglist.getStringTagAt(i);

						IChatComponent ichatcomponent;
						try {
							ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
							ichatcomponent = ChatComponentProcessor.processComponent(player, ichatcomponent, player);
						} catch (Exception var9) {
							ichatcomponent = new ChatComponentText(s);
						}

						nbttaglist.set(i, new NBTTagString(IChatComponent.Serializer.componentToJson(ichatcomponent)));
					}

					nbttagcompound.setTag("pages", nbttaglist);
					if (player instanceof EntityPlayerMP && player.getCurrentEquippedItem() == stack) {
						Slot slot = player.openContainer.getSlotFromInventory(player.inventory,
								player.inventory.currentItem);
						((EntityPlayerMP) player).playerNetServerHandler
								.sendPacket(new S2FPacketSetSlot(0, slot.slotNumber, stack));
					}

				}
			}
		}
	}

	public boolean hasEffect(ItemStack var1) {
		return true;
	}
}