package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.command;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.command.CommandException;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.command.CommandGameMode;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.command.ICommandSender;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.command.WrongUsageException;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayerMP;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.server.MinecraftServer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.ChatComponentTranslation;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.WorldSettings;

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
public class CommandDefaultGameMode extends CommandGameMode {

	static {
		__checkIntegratedContextValid("net/minecraft/command/CommandDefaultGameMode");
	}

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "defaultgamemode";
	}

	/**+
	 * Gets the usage string for the command.
	 */
	public String getCommandUsage(ICommandSender var1) {
		return "commands.defaultgamemode.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length <= 0) {
			throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
		} else {
			WorldSettings.GameType worldsettings$gametype = this.getGameModeFromCommand(parICommandSender,
					parArrayOfString[0]);
			this.setGameType(worldsettings$gametype);
			notifyOperators(parICommandSender, this, "commands.defaultgamemode.success", new Object[] {
					new ChatComponentTranslation("gameMode." + worldsettings$gametype.getName(), new Object[0]) });
		}
	}

	protected void setGameType(WorldSettings.GameType parGameType) {
		MinecraftServer minecraftserver = MinecraftServer.getServer();
		minecraftserver.setGameType(parGameType);
		if (minecraftserver.getForceGamemode()) {
			for (EntityPlayerMP entityplayermp : MinecraftServer.getServer().getConfigurationManager()
					.func_181057_v()) {
				entityplayermp.setGameType(parGameType);
				entityplayermp.fallDistance = 0.0F;
			}
		}

	}
}