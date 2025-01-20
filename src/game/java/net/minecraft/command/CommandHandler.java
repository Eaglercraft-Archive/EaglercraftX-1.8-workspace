package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class CommandHandler implements ICommandManager {

	private static final Logger logger = LogManager.getLogger();
	private final Map<String, ICommand> commandMap = Maps.newHashMap();
	/**+
	 * The set of ICommand objects currently loaded.
	 */
	private final Set<ICommand> commandSet = Sets.newHashSet();

	public int executeCommand(ICommandSender sender, String rawCommand) {
		rawCommand = rawCommand.trim();
		if (rawCommand.startsWith("/")) {
			rawCommand = rawCommand.substring(1);
		}

		String[] astring = rawCommand.split(" ");
		String s = astring[0];
		astring = dropFirstString(astring);
		ICommand icommand = (ICommand) this.commandMap.get(s);
		int i = this.getUsernameIndex(icommand, astring);
		int j = 0;
		if (icommand == null) {
			ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
					"commands.generic.notFound", new Object[0]);
			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatcomponenttranslation);
		} else if (icommand.canCommandSenderUseCommand(sender)) {
			if (i > -1) {
				List list = PlayerSelector.matchEntities(sender, astring[i], Entity.class);
				String s1 = astring[i];
				sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());

				for (int k = 0, l = list.size(); k < l; ++k) {
					astring[i] = ((Entity) list.get(k)).getUniqueID().toString();
					if (this.tryExecute(sender, astring, icommand, rawCommand)) {
						++j;
					}
				}

				astring[i] = s1;
			} else {
				sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
				if (this.tryExecute(sender, astring, icommand, rawCommand)) {
					++j;
				}
			}
		} else {
			ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(
					"commands.generic.permission", new Object[0]);
			chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatcomponenttranslation1);
		}

		sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, j);
		return j;
	}

	protected boolean tryExecute(ICommandSender sender, String[] args, ICommand command, String input) {
		try {
			command.processCommand(sender, args);
			return true;
		} catch (WrongUsageException wrongusageexception) {
			ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.generic.usage",
					new Object[] { new ChatComponentTranslation(wrongusageexception.getMessage(),
							wrongusageexception.getErrorObjects()) });
			chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatcomponenttranslation2);
		} catch (CommandException commandexception) {
			ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(
					commandexception.getMessage(), commandexception.getErrorObjects());
			chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatcomponenttranslation1);
		} catch (Throwable var9) {
			ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
					"commands.generic.exception", new Object[0]);
			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatcomponenttranslation);
			logger.warn("Couldn\'t process command: \'" + input + "\'");
		}

		return false;
	}

	/**+
	 * adds the command and any aliases it has to the internal map
	 * of available commands
	 */
	public ICommand registerCommand(ICommand command) {
		this.commandMap.put(command.getCommandName(), command);
		this.commandSet.add(command);

		for (String s : command.getCommandAliases()) {
			ICommand icommand = (ICommand) this.commandMap.get(s);
			if (icommand == null || !icommand.getCommandName().equals(s)) {
				this.commandMap.put(s, command);
			}
		}

		return command;
	}

	/**+
	 * creates a new array and sets elements 0..n-2 to be 0..n-1 of
	 * the input (n elements)
	 */
	private static String[] dropFirstString(String[] input) {
		String[] astring = new String[input.length - 1];
		System.arraycopy(input, 1, astring, 0, input.length - 1);
		return astring;
	}

	public List<String> getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos) {
		String[] astring = input.split(" ", -1);
		String s = astring[0];
		if (astring.length == 1) {
			ArrayList arraylist = Lists.newArrayList();

			for (Entry entry : this.commandMap.entrySet()) {
				if (CommandBase.doesStringStartWith(s, (String) entry.getKey())
						&& ((ICommand) entry.getValue()).canCommandSenderUseCommand(sender)) {
					arraylist.add(entry.getKey());
				}
			}

			return arraylist;
		} else {
			if (astring.length > 1) {
				ICommand icommand = (ICommand) this.commandMap.get(s);
				if (icommand != null && icommand.canCommandSenderUseCommand(sender)) {
					return icommand.addTabCompletionOptions(sender, dropFirstString(astring), pos);
				}
			}

			return null;
		}
	}

	/**+
	 * returns all commands that the commandSender can use
	 */
	public List<ICommand> getPossibleCommands(ICommandSender sender) {
		ArrayList arraylist = Lists.newArrayList();

		for (ICommand icommand : this.commandSet) {
			if (icommand.canCommandSenderUseCommand(sender)) {
				arraylist.add(icommand);
			}
		}

		return arraylist;
	}

	public Map<String, ICommand> getCommands() {
		return this.commandMap;
	}

	/**+
	 * Return a command's first parameter index containing a valid
	 * username.
	 */
	private int getUsernameIndex(ICommand command, String[] args) {
		if (command == null) {
			return -1;
		} else {
			for (int i = 0; i < args.length; ++i) {
				if (command.isUsernameIndex(args, i) && PlayerSelector.matchesMultiplePlayers(args[i])) {
					return i;
				}
			}

			return -1;
		}
	}
}