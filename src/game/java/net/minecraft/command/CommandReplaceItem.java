package net.minecraft.command;

import java.util.List;

import com.carrotsearch.hppc.ObjectIntHashMap;
import com.carrotsearch.hppc.ObjectIntMap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
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
public class CommandReplaceItem extends CommandBase {

	private static final ObjectIntMap<String> SHORTCUTS = new ObjectIntHashMap<>();

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "replaceitem";
	}

	/**+
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 2;
	}

	/**+
	 * Gets the usage string for the command.
	 */
	public String getCommandUsage(ICommandSender var1) {
		return "commands.replaceitem.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 1) {
			throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
		} else {
			boolean flag;
			if (parArrayOfString[0].equals("entity")) {
				flag = false;
			} else {
				if (!parArrayOfString[0].equals("block")) {
					throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
				}

				flag = true;
			}

			int i;
			if (flag) {
				if (parArrayOfString.length < 6) {
					throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
				}

				i = 4;
			} else {
				if (parArrayOfString.length < 4) {
					throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
				}

				i = 2;
			}

			int j = this.getSlotForShortcut(parArrayOfString[i++]);

			Item item;
			try {
				item = getItemByText(parICommandSender, parArrayOfString[i]);
			} catch (NumberInvalidException numberinvalidexception) {
				if (Block.getBlockFromName(parArrayOfString[i]) != Blocks.air) {
					throw numberinvalidexception;
				}

				item = null;
			}

			++i;
			int k = parArrayOfString.length > i ? parseInt(parArrayOfString[i++], 1, 64) : 1;
			int l = parArrayOfString.length > i ? parseInt(parArrayOfString[i++]) : 0;
			ItemStack itemstack = new ItemStack(item, k, l);
			if (parArrayOfString.length > i) {
				String s = getChatComponentFromNthArg(parICommandSender, parArrayOfString, i).getUnformattedText();

				try {
					itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
				} catch (NBTException nbtexception) {
					throw new CommandException("commands.replaceitem.tagError",
							new Object[] { nbtexception.getMessage() });
				}
			}

			if (itemstack.getItem() == null) {
				itemstack = null;
			}

			if (flag) {
				parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
				BlockPos blockpos = parseBlockPos(parICommandSender, parArrayOfString, 1, false);
				World world = parICommandSender.getEntityWorld();
				TileEntity tileentity = world.getTileEntity(blockpos);
				if (tileentity == null || !(tileentity instanceof IInventory)) {
					throw new CommandException("commands.replaceitem.noContainer",
							new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()),
									Integer.valueOf(blockpos.getZ()) });
				}

				IInventory iinventory = (IInventory) tileentity;
				if (j >= 0 && j < iinventory.getSizeInventory()) {
					iinventory.setInventorySlotContents(j, itemstack);
				}
			} else {
				Entity entity = func_175768_b(parICommandSender, parArrayOfString[1]);
				parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
				if (entity instanceof EntityPlayer) {
					((EntityPlayer) entity).inventoryContainer.detectAndSendChanges();
				}

				if (!entity.replaceItemInInventory(j, itemstack)) {
					throw new CommandException("commands.replaceitem.failed", new Object[] { Integer.valueOf(j),
							Integer.valueOf(k), itemstack == null ? "Air" : itemstack.getChatComponent() });
				}

				if (entity instanceof EntityPlayer) {
					((EntityPlayer) entity).inventoryContainer.detectAndSendChanges();
				}
			}

			parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
			notifyOperators(parICommandSender, this, "commands.replaceitem.success", new Object[] { Integer.valueOf(j),
					Integer.valueOf(k), itemstack == null ? "Air" : itemstack.getChatComponent() });
		}
	}

	private int getSlotForShortcut(String shortcut) throws CommandException {
		if (!SHORTCUTS.containsKey(shortcut)) {
			throw new CommandException("commands.generic.parameter.invalid", new Object[] { shortcut });
		} else {
			return SHORTCUTS.get(shortcut);
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos blockpos) {
		return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "entity", "block" })
				: (astring.length == 2 && astring[0].equals("entity")
						? getListOfStringsMatchingLastWord(astring, this.getUsernames())
						: (astring.length >= 2 && astring.length <= 4 && astring[0].equals("block")
								? func_175771_a(astring, 1, blockpos)
								: ((astring.length != 3 || !astring[0].equals("entity"))
										&& (astring.length != 5 || !astring[0].equals("block"))
												? ((astring.length != 4 || !astring[0].equals("entity"))
														&& (astring.length != 6 || !astring[0].equals("block"))
																? null
																: getListOfStringsMatchingLastWord(astring,
																		Item.itemRegistry.getKeys()))
												: getListOfStringsMatchingLastWord(astring, SHORTCUTS.keys()))));
	}

	protected String[] getUsernames() {
		return MinecraftServer.getServer().getAllUsernames();
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] astring, int i) {
		return astring.length > 0 && astring[0].equals("entity") && i == 1;
	}

	static {
		for (int i = 0; i < 54; ++i) {
			SHORTCUTS.put("slot.container." + i, i);
		}

		for (int j = 0; j < 9; ++j) {
			SHORTCUTS.put("slot.hotbar." + j, j);
		}

		for (int k = 0; k < 27; ++k) {
			SHORTCUTS.put("slot.inventory." + k, 9 + k);
		}

		for (int l = 0; l < 27; ++l) {
			SHORTCUTS.put("slot.enderchest." + l, 200 + l);
		}

		for (int i1 = 0; i1 < 8; ++i1) {
			SHORTCUTS.put("slot.villager." + i1, 300 + i1);
		}

		for (int j1 = 0; j1 < 15; ++j1) {
			SHORTCUTS.put("slot.horse." + j1, 500 + j1);
		}

		SHORTCUTS.put("slot.weapon", 99);
		SHORTCUTS.put("slot.armor.head", 103);
		SHORTCUTS.put("slot.armor.chest", 102);
		SHORTCUTS.put("slot.armor.legs", 101);
		SHORTCUTS.put("slot.armor.feet", 100);
		SHORTCUTS.put("slot.horse.saddle", 400);
		SHORTCUTS.put("slot.horse.armor", 401);
		SHORTCUTS.put("slot.horse.chest", 499);
	}
}