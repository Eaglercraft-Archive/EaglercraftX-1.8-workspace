package net.minecraft.command;

import net.minecraft.command.server.CommandAchievement;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.command.server.CommandBroadcast;
import net.minecraft.command.server.CommandEmote;
import net.minecraft.command.server.CommandListPlayers;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.command.server.CommandMessageRaw;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.command.server.CommandSetDefaultSpawnpoint;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.command.server.CommandTestFor;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.sp.server.ClientCommandDummy;

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
public class ServerCommandManager extends CommandHandler implements IAdminCommand {

	public ServerCommandManager() {
		this.registerCommand(new CommandTime());
		this.registerCommand(new CommandGameMode());
		this.registerCommand(new CommandDifficulty());
		this.registerCommand(new CommandDefaultGameMode());
		this.registerCommand(new CommandKill());
		this.registerCommand(new CommandToggleDownfall());
		this.registerCommand(new CommandWeather());
		this.registerCommand(new CommandXP());
		this.registerCommand(new CommandTeleport());
		this.registerCommand(new CommandGive());
		this.registerCommand(new CommandReplaceItem());
		this.registerCommand(new CommandStats());
		this.registerCommand(new CommandEffect());
		this.registerCommand(new CommandEnchant());
		this.registerCommand(new CommandParticle());
		this.registerCommand(new CommandEmote());
		this.registerCommand(new CommandShowSeed());
		this.registerCommand(new CommandHelp());
		this.registerCommand(new CommandMessage());
		this.registerCommand(new CommandBroadcast());
		this.registerCommand(new CommandSetSpawnpoint());
		this.registerCommand(new CommandSetDefaultSpawnpoint());
		this.registerCommand(new CommandGameRule());
		this.registerCommand(new CommandClearInventory());
		this.registerCommand(new CommandTestFor());
		this.registerCommand(new CommandSpreadPlayers());
		this.registerCommand(new CommandPlaySound());
		this.registerCommand(new CommandScoreboard());
		this.registerCommand(new CommandExecuteAt());
		this.registerCommand(new CommandTrigger());
		this.registerCommand(new CommandAchievement());
		this.registerCommand(new CommandSummon());
		this.registerCommand(new CommandSetBlock());
		this.registerCommand(new CommandFill());
		this.registerCommand(new CommandClone());
		this.registerCommand(new CommandCompare());
		this.registerCommand(new CommandBlockData());
		this.registerCommand(new CommandTestForBlock());
		this.registerCommand(new CommandMessageRaw());
		this.registerCommand(new CommandWorldBorder());
		this.registerCommand(new CommandTitle());
		this.registerCommand(new CommandEntityData());
		this.registerCommand(new CommandServerKick());
		this.registerCommand(new CommandListPlayers());
		this.registerCommand(new CommandSetPlayerTimeout());
		this.registerCommand(new ClientCommandDummy("eagskull", 2, "command.skull.usage"));
		CommandBase.setAdminCommander(this);
	}

	/**+
	 * Send an informative message to the server operators
	 */
	public void notifyOperators(ICommandSender sender, ICommand command, int flags, String msgFormat,
			Object... msgParams) {
		boolean flag = true;
		MinecraftServer minecraftserver = MinecraftServer.getServer();
		if (!sender.sendCommandFeedback()) {
			flag = false;
		}

		ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.type.admin",
				new Object[] { sender.getName(), new ChatComponentTranslation(msgFormat, msgParams) });
		chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.GRAY);
		chatcomponenttranslation.getChatStyle().setItalic(Boolean.valueOf(true));
		if (flag) {
			List<EntityPlayerMP> players = minecraftserver.getConfigurationManager().func_181057_v();
			for (int i = 0, l = players.size(); i < l; ++i) {
				EntityPlayerMP entityplayer = players.get(i);
				if (entityplayer != sender
						&& minecraftserver.getConfigurationManager().canSendCommands(entityplayer.getGameProfile())
						&& command.canCommandSenderUseCommand(sender)) {
					entityplayer.addChatMessage(chatcomponenttranslation);
				}
			}
		}

		if (sender != minecraftserver
				&& minecraftserver.worldServers[0].getGameRules().getBoolean("logAdminCommands")) {
			minecraftserver.addChatMessage(chatcomponenttranslation);
		}

		boolean flag3 = minecraftserver.worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
		if (sender instanceof CommandBlockLogic) {
			flag3 = ((CommandBlockLogic) sender).shouldTrackOutput();
		}

		if ((flags & 1) != 1 && flag3 || sender instanceof MinecraftServer) {
			sender.addChatMessage(new ChatComponentTranslation(msgFormat, msgParams));
		}

	}
}