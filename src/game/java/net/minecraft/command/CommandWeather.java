package net.minecraft.command;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.ThreadLocalRandom;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

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
public class CommandWeather extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "weather";
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
		return "commands.weather.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length >= 1 && parArrayOfString.length <= 2) {
			int i = (300 + ThreadLocalRandom.current().nextInt(600)) * 20 * 2;
			if (parArrayOfString.length >= 2) {
				i = parseInt(parArrayOfString[1], 1, 1000000) * 20;
			}

			WorldServer worldserver = MinecraftServer.getServer().worldServers[0];
			WorldInfo worldinfo = worldserver.getWorldInfo();
			if ("clear".equalsIgnoreCase(parArrayOfString[0])) {
				worldinfo.setCleanWeatherTime(i);
				worldinfo.setRainTime(0);
				worldinfo.setThunderTime(0);
				worldinfo.setRaining(false);
				worldinfo.setThundering(false);
				notifyOperators(parICommandSender, this, "commands.weather.clear", new Object[0]);
			} else if ("rain".equalsIgnoreCase(parArrayOfString[0])) {
				worldinfo.setCleanWeatherTime(0);
				worldinfo.setRainTime(i);
				worldinfo.setThunderTime(i);
				worldinfo.setRaining(true);
				worldinfo.setThundering(false);
				notifyOperators(parICommandSender, this, "commands.weather.rain", new Object[0]);
			} else {
				if (!"thunder".equalsIgnoreCase(parArrayOfString[0])) {
					throw new WrongUsageException("commands.weather.usage", new Object[0]);
				}

				worldinfo.setCleanWeatherTime(0);
				worldinfo.setRainTime(i);
				worldinfo.setThunderTime(i);
				worldinfo.setRaining(true);
				worldinfo.setThundering(true);
				notifyOperators(parICommandSender, this, "commands.weather.thunder", new Object[0]);
			}

		} else {
			throw new WrongUsageException("commands.weather.usage", new Object[0]);
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		return astring.length == 1
				? getListOfStringsMatchingLastWord(astring, new String[] { "clear", "rain", "thunder" })
				: null;
	}
}