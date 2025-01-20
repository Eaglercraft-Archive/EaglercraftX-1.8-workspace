package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;

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
public interface ICommand extends Comparable<ICommand> {
	/**+
	 * Gets the name of the command
	 */
	String getCommandName();

	/**+
	 * Gets the usage string for the command.
	 */
	String getCommandUsage(ICommandSender var1);

	/**+
	 * Gets a list of aliases for this command
	 */
	List<String> getCommandAliases();

	/**+
	 * Callback when the command is invoked
	 */
	void processCommand(ICommandSender var1, String[] var2) throws CommandException;

	/**+
	 * Returns true if the given command sender is allowed to use
	 * this command.
	 */
	boolean canCommandSenderUseCommand(ICommandSender var1);

	/**+
	 * Return a list of options when the user types TAB
	 */
	List<String> addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3);

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	boolean isUsernameIndex(String[] var1, int var2);
}