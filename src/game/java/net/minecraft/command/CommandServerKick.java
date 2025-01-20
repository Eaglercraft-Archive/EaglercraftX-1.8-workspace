package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
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
public class CommandServerKick extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "kick";
	}

	/**+
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 3;
	}

	/**+
	 * Gets the usage string for the command.
	 */
	public String getCommandUsage(ICommandSender var1) {
		return "commands.kick.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length > 0 && parArrayOfString[0].length() > 1) {
			EntityPlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager()
					.getPlayerByUsername(parArrayOfString[0]);
			String s = "Kicked by an operator.";
			boolean flag = false;
			if (entityplayermp == null) {
				throw new PlayerNotFoundException();
			} else {
				if (parArrayOfString.length >= 2) {
					s = getChatComponentFromNthArg(parICommandSender, parArrayOfString, 1).getUnformattedText();
					if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
							.getBoolean("colorCodes")) {
						s = StringUtils.translateControlCodesAlternate(s);
					}
					flag = true;
				}

				entityplayermp.playerNetServerHandler.kickPlayerFromServer(s);
				if (flag) {
					notifyOperators(parICommandSender, this, "commands.kick.success.reason",
							new Object[] { entityplayermp.getName(), s });
				} else {
					notifyOperators(parICommandSender, this, "commands.kick.success",
							new Object[] { entityplayermp.getName() });
				}

			}
		} else {
			throw new WrongUsageException("commands.kick.usage", new Object[0]);
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		return astring.length >= 1
				? getListOfStringsMatchingLastWord(astring, MinecraftServer.getServer().getAllUsernames())
				: null;
	}
}