package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

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
public class CommandGameMode extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "gamemode";
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
		return "commands.gamemode.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length <= 0) {
			throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
		} else {
			WorldSettings.GameType worldsettings$gametype = this.getGameModeFromCommand(parICommandSender,
					parArrayOfString[0]);
			EntityPlayerMP entityplayermp = parArrayOfString.length >= 2
					? getPlayer(parICommandSender, parArrayOfString[1])
					: getCommandSenderAsPlayer(parICommandSender);
			entityplayermp.setGameType(worldsettings$gametype);
			entityplayermp.fallDistance = 0.0F;
			if (parICommandSender.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback")) {
				entityplayermp.addChatMessage(new ChatComponentTranslation("gameMode.changed", new Object[0]));
			}

			ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
					"gameMode." + worldsettings$gametype.getName(), new Object[0]);
			if (entityplayermp != parICommandSender) {
				notifyOperators(parICommandSender, this, 1, "commands.gamemode.success.other",
						new Object[] { entityplayermp.getName(), chatcomponenttranslation });
			} else {
				notifyOperators(parICommandSender, this, 1, "commands.gamemode.success.self",
						new Object[] { chatcomponenttranslation });
			}

		}
	}

	/**+
	 * Gets the Game Mode specified in the command.
	 */
	protected WorldSettings.GameType getGameModeFromCommand(ICommandSender parICommandSender, String parString1)
			throws NumberInvalidException {
		return !parString1.equalsIgnoreCase(WorldSettings.GameType.SURVIVAL.getName())
				&& !parString1.equalsIgnoreCase("s")
						? (!parString1.equalsIgnoreCase(WorldSettings.GameType.CREATIVE.getName())
								&& !parString1.equalsIgnoreCase("c")
										? (!parString1.equalsIgnoreCase(WorldSettings.GameType.ADVENTURE.getName())
												&& !parString1.equalsIgnoreCase("a")
														? (!parString1.equalsIgnoreCase(
																WorldSettings.GameType.SPECTATOR.getName())
																&& !parString1.equalsIgnoreCase("sp")
																		? WorldSettings.getGameTypeById(parseInt(
																				parString1, 0,
																				WorldSettings.GameType._VALUES.length
																						- 2))
																		: WorldSettings.GameType.SPECTATOR)
														: WorldSettings.GameType.ADVENTURE)
										: WorldSettings.GameType.CREATIVE)
						: WorldSettings.GameType.SURVIVAL;
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		return astring.length == 1
				? getListOfStringsMatchingLastWord(astring,
						new String[] { "survival", "creative", "adventure", "spectator" })
				: (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, this.getListOfPlayerUsernames())
						: null);
	}

	/**+
	 * Returns String array containing all player usernames in the
	 * server.
	 */
	protected String[] getListOfPlayerUsernames() {
		return MinecraftServer.getServer().getAllUsernames();
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] var1, int i) {
		return i == 1;
	}
}