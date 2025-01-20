package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

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
public class CommandClearInventory extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "clear";
	}

	/**+
	 * Gets the usage string for the command.
	 */
	public String getCommandUsage(ICommandSender var1) {
		return "commands.clear.usage";
	}

	/**+
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 2;
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		EntityPlayerMP entityplayermp = parArrayOfString.length == 0 ? getCommandSenderAsPlayer(parICommandSender)
				: getPlayer(parICommandSender, parArrayOfString[0]);
		Item item = parArrayOfString.length >= 2 ? getItemByText(parICommandSender, parArrayOfString[1]) : null;
		int i = parArrayOfString.length >= 3 ? parseInt(parArrayOfString[2], -1) : -1;
		int j = parArrayOfString.length >= 4 ? parseInt(parArrayOfString[3], -1) : -1;
		NBTTagCompound nbttagcompound = null;
		if (parArrayOfString.length >= 5) {
			try {
				nbttagcompound = JsonToNBT.getTagFromJson(buildString(parArrayOfString, 4));
			} catch (NBTException nbtexception) {
				throw new CommandException("commands.clear.tagError", new Object[] { nbtexception.getMessage() });
			}
		}

		if (parArrayOfString.length >= 2 && item == null) {
			throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() });
		} else {
			int k = entityplayermp.inventory.clearMatchingItems(item, i, j, nbttagcompound);
			entityplayermp.inventoryContainer.detectAndSendChanges();
			if (!entityplayermp.capabilities.isCreativeMode) {
				entityplayermp.updateHeldItem();
			}

			parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
			if (k == 0) {
				throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() });
			} else {
				if (j == 0) {
					parICommandSender.addChatMessage(new ChatComponentTranslation("commands.clear.testing",
							new Object[] { entityplayermp.getName(), Integer.valueOf(k) }));
				} else {
					notifyOperators(parICommandSender, this, "commands.clear.success",
							new Object[] { entityplayermp.getName(), Integer.valueOf(k) });
				}

			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, this.func_147209_d())
				: (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, Item.itemRegistry.getKeys()) : null);
	}

	protected String[] func_147209_d() {
		return MinecraftServer.getServer().getAllUsernames();
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] var1, int i) {
		return i == 0;
	}
}