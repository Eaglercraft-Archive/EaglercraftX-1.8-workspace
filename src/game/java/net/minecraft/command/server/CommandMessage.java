package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;

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
public class CommandMessage extends CommandBase {

	/**+
	 * Gets a list of aliases for this command
	 */
	public List<String> getCommandAliases() {
		return Arrays.asList(new String[] { "w", "msg" });
	}

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "tell";
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
		return "commands.message.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 2) {
			throw new WrongUsageException("commands.message.usage", new Object[0]);
		} else {
			EntityPlayerMP entityplayermp = getPlayer(parICommandSender, parArrayOfString[0]);
			if (entityplayermp == parICommandSender) {
				throw new PlayerNotFoundException("commands.message.sameTarget", new Object[0]);
			} else {
				IChatComponent ichatcomponent = getChatComponentFromNthArg(parICommandSender, parArrayOfString, 1,
						!(parICommandSender instanceof EntityPlayer));
				if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
						.getBoolean("colorCodes")) {
					ichatcomponent = new ChatComponentText(
							StringUtils.translateControlCodesAlternate(ichatcomponent.getFormattedText()));
				}
				ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
						"commands.message.display.incoming",
						new Object[] { parICommandSender.getDisplayName(), ichatcomponent.createCopy() });
				ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(
						"commands.message.display.outgoing",
						new Object[] { entityplayermp.getDisplayName(), ichatcomponent.createCopy() });
				chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.GRAY)
						.setItalic(Boolean.valueOf(true));
				chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.GRAY)
						.setItalic(Boolean.valueOf(true));
				entityplayermp.addChatMessage(chatcomponenttranslation);
				parICommandSender.addChatMessage(chatcomponenttranslation1);
			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		return getListOfStringsMatchingLastWord(astring, MinecraftServer.getServer().getAllUsernames());
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] var1, int i) {
		return i == 0;
	}
}