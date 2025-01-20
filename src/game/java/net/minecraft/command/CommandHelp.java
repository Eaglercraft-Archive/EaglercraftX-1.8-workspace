package net.minecraft.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

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
public class CommandHelp extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "help";
	}

	/**+
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 0;
	}

	/**+
	 * Gets the usage string for the command.
	 */
	public String getCommandUsage(ICommandSender var1) {
		return "commands.help.usage";
	}

	/**+
	 * Gets a list of aliases for this command
	 */
	public List<String> getCommandAliases() {
		return Arrays.asList(new String[] { "?" });
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		List list = this.getSortedPossibleCommands(parICommandSender);
		boolean flag = true;
		int i = (list.size() - 1) / 7;
		int j = 0;

		try {
			j = parArrayOfString.length == 0 ? 0 : parseInt(parArrayOfString[0], 1, i + 1) - 1;
		} catch (NumberInvalidException numberinvalidexception) {
			Map map = this.getCommands();
			ICommand icommand = (ICommand) map.get(parArrayOfString[0]);
			if (icommand != null) {
				throw new WrongUsageException(icommand.getCommandUsage(parICommandSender), new Object[0]);
			}

			if (MathHelper.parseIntWithDefault(parArrayOfString[0], -1) != -1) {
				throw numberinvalidexception;
			}

			throw new CommandNotFoundException();
		}

		int k = Math.min((j + 1) * 7, list.size());
		ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.help.header",
				new Object[] { Integer.valueOf(j + 1), Integer.valueOf(i + 1) });
		chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
		parICommandSender.addChatMessage(chatcomponenttranslation1);

		for (int l = j * 7; l < k; ++l) {
			ICommand icommand1 = (ICommand) list.get(l);
			ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
					icommand1.getCommandUsage(parICommandSender), new Object[0]);
			chatcomponenttranslation.getChatStyle().setChatClickEvent(
					new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand1.getCommandName() + " "));
			parICommandSender.addChatMessage(chatcomponenttranslation);
		}

		if (j == 0 && parICommandSender instanceof EntityPlayer) {
			ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.help.footer",
					new Object[0]);
			chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.GREEN);
			parICommandSender.addChatMessage(chatcomponenttranslation2);
		}

	}

	/**+
	 * Returns a sorted list of all possible commands for the given
	 * ICommandSender.
	 */
	protected List<ICommand> getSortedPossibleCommands(ICommandSender parICommandSender) {
		List list = MinecraftServer.getServer().getCommandManager().getPossibleCommands(parICommandSender);
		Collections.sort(list);
		return list;
	}

	protected Map<String, ICommand> getCommands() {
		return MinecraftServer.getServer().getCommandManager().getCommands();
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		if (astring.length == 1) {
			Set set = this.getCommands().keySet();
			return getListOfStringsMatchingLastWord(astring, (String[]) set.toArray(new String[set.size()]));
		} else {
			return null;
		}
	}
}