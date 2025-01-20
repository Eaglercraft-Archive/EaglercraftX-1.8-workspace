package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.EnumDifficulty;

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
public class CommandDifficulty extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "difficulty";
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
		return "commands.difficulty.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length <= 0) {
			throw new WrongUsageException("commands.difficulty.usage", new Object[0]);
		} else {
			EnumDifficulty enumdifficulty = this.getDifficultyFromCommand(parArrayOfString[0]);
			MinecraftServer.getServer().setDifficultyForAllWorlds(enumdifficulty);
			notifyOperators(parICommandSender, this, "commands.difficulty.success", new Object[] {
					new ChatComponentTranslation(enumdifficulty.getDifficultyResourceKey(), new Object[0]) });
		}
	}

	protected EnumDifficulty getDifficultyFromCommand(String parString1) throws NumberInvalidException {
		return !parString1.equalsIgnoreCase("peaceful") && !parString1.equalsIgnoreCase("p")
				? (!parString1.equalsIgnoreCase("easy") && !parString1.equalsIgnoreCase("e")
						? (!parString1.equalsIgnoreCase("normal") && !parString1.equalsIgnoreCase("n")
								? (!parString1.equalsIgnoreCase("hard") && !parString1.equalsIgnoreCase("h")
										? EnumDifficulty.getDifficultyEnum(parseInt(parString1, 0, 3))
										: EnumDifficulty.HARD)
								: EnumDifficulty.NORMAL)
						: EnumDifficulty.EASY)
				: EnumDifficulty.PEACEFUL;
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		return astring.length == 1
				? getListOfStringsMatchingLastWord(astring, new String[] { "peaceful", "easy", "normal", "hard" })
				: null;
	}
}