package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;

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
public class CommandTime extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "time";
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
		return "commands.time.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length > 1) {
			if (parArrayOfString[0].equals("set")) {
				int l;
				if (parArrayOfString[1].equals("day")) {
					l = 1000;
				} else if (parArrayOfString[1].equals("night")) {
					l = 13000;
				} else {
					l = parseInt(parArrayOfString[1], 0);
				}

				this.setTime(parICommandSender, l);
				notifyOperators(parICommandSender, this, "commands.time.set", new Object[] { Integer.valueOf(l) });
				return;
			}

			if (parArrayOfString[0].equals("add")) {
				int k = parseInt(parArrayOfString[1], 0);
				this.addTime(parICommandSender, k);
				notifyOperators(parICommandSender, this, "commands.time.added", new Object[] { Integer.valueOf(k) });
				return;
			}

			if (parArrayOfString[0].equals("query")) {
				if (parArrayOfString[1].equals("daytime")) {
					int j = (int) (parICommandSender.getEntityWorld().getWorldTime() % 2147483647L);
					parICommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j);
					notifyOperators(parICommandSender, this, "commands.time.query",
							new Object[] { Integer.valueOf(j) });
					return;
				}

				if (parArrayOfString[1].equals("gametime")) {
					int i = (int) (parICommandSender.getEntityWorld().getTotalWorldTime() % 2147483647L);
					parICommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
					notifyOperators(parICommandSender, this, "commands.time.query",
							new Object[] { Integer.valueOf(i) });
					return;
				}
			}
		}

		throw new WrongUsageException("commands.time.usage", new Object[0]);
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "set", "add", "query" })
				: (astring.length == 2 && astring[0].equals("set")
						? getListOfStringsMatchingLastWord(astring, new String[] { "day", "night" })
						: (astring.length == 2 && astring[0].equals("query")
								? getListOfStringsMatchingLastWord(astring, new String[] { "daytime", "gametime" })
								: null));
	}

	/**+
	 * Set the time in the server object.
	 */
	protected void setTime(ICommandSender parICommandSender, int parInt1) {
		for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
			MinecraftServer.getServer().worldServers[i].setWorldTime((long) parInt1);
		}

	}

	/**+
	 * Adds (or removes) time in the server object.
	 */
	protected void addTime(ICommandSender parICommandSender, int parInt1) {
		for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
			WorldServer worldserver = MinecraftServer.getServer().worldServers[i];
			worldserver.setWorldTime(worldserver.getWorldTime() + (long) parInt1);
		}

	}
}