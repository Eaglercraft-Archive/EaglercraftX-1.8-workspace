package net.minecraft.command;

import java.util.List;

import org.json.JSONException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import net.lax1dude.eaglercraft.v1_8.ExceptionUtils;
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
public class CommandTitle extends CommandBase {

	private static final Logger LOGGER = LogManager.getLogger();

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "title";
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
		return "commands.title.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 2) {
			throw new WrongUsageException("commands.title.usage", new Object[0]);
		} else {
			if (parArrayOfString.length < 3) {
				if ("title".equals(parArrayOfString[1]) || "subtitle".equals(parArrayOfString[1])) {
					throw new WrongUsageException("commands.title.usage.title", new Object[0]);
				}

				if ("times".equals(parArrayOfString[1])) {
					throw new WrongUsageException("commands.title.usage.times", new Object[0]);
				}
			}

			EntityPlayerMP entityplayermp = getPlayer(parICommandSender, parArrayOfString[0]);
			S45PacketTitle.Type s45packettitle$type = S45PacketTitle.Type.byName(parArrayOfString[1]);
			if (s45packettitle$type != S45PacketTitle.Type.CLEAR && s45packettitle$type != S45PacketTitle.Type.RESET) {
				if (s45packettitle$type == S45PacketTitle.Type.TIMES) {
					if (parArrayOfString.length != 5) {
						throw new WrongUsageException("commands.title.usage", new Object[0]);
					} else {
						int i = parseInt(parArrayOfString[2]);
						int j = parseInt(parArrayOfString[3]);
						int k = parseInt(parArrayOfString[4]);
						S45PacketTitle s45packettitle2 = new S45PacketTitle(i, j, k);
						entityplayermp.playerNetServerHandler.sendPacket(s45packettitle2);
						notifyOperators(parICommandSender, this, "commands.title.success", new Object[0]);
					}
				} else if (parArrayOfString.length < 3) {
					throw new WrongUsageException("commands.title.usage", new Object[0]);
				} else {
					String s = buildString(parArrayOfString, 2);

					IChatComponent ichatcomponent;
					try {
						ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
					} catch (JSONException jsonparseexception) {
						Throwable throwable = ExceptionUtils.getRootCause(jsonparseexception);
						throw new SyntaxErrorException("commands.tellraw.jsonException",
								new Object[] { throwable == null ? "" : throwable.getMessage() });
					}

					S45PacketTitle s45packettitle1 = new S45PacketTitle(s45packettitle$type,
							ChatComponentProcessor.processComponent(parICommandSender, ichatcomponent, entityplayermp));
					entityplayermp.playerNetServerHandler.sendPacket(s45packettitle1);
					notifyOperators(parICommandSender, this, "commands.title.success", new Object[0]);
				}
			} else if (parArrayOfString.length != 2) {
				throw new WrongUsageException("commands.title.usage", new Object[0]);
			} else {
				S45PacketTitle s45packettitle = new S45PacketTitle(s45packettitle$type, (IChatComponent) null);
				entityplayermp.playerNetServerHandler.sendPacket(s45packettitle);
				notifyOperators(parICommandSender, this, "commands.title.success", new Object[0]);
			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		return astring.length == 1
				? getListOfStringsMatchingLastWord(astring, MinecraftServer.getServer().getAllUsernames())
				: (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, S45PacketTitle.Type.getNames())
						: null);
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] var1, int i) {
		return i == 0;
	}
}