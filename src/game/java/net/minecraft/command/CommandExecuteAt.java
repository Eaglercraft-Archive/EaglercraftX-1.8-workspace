package net.minecraft.command;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

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
public class CommandExecuteAt extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "execute";
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
		return "commands.execute.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(final ICommandSender parICommandSender, String[] parArrayOfString)
			throws CommandException {
		if (parArrayOfString.length < 5) {
			throw new WrongUsageException("commands.execute.usage", new Object[0]);
		} else {
			final Entity entity = getEntity(parICommandSender, parArrayOfString[0], Entity.class);
			final double d0 = parseDouble(entity.posX, parArrayOfString[1], false);
			final double d1 = parseDouble(entity.posY, parArrayOfString[2], false);
			final double d2 = parseDouble(entity.posZ, parArrayOfString[3], false);
			final BlockPos blockpos = new BlockPos(d0, d1, d2);
			byte b0 = 4;
			if ("detect".equals(parArrayOfString[4]) && parArrayOfString.length > 10) {
				World world = entity.getEntityWorld();
				double d3 = parseDouble(d0, parArrayOfString[5], false);
				double d4 = parseDouble(d1, parArrayOfString[6], false);
				double d5 = parseDouble(d2, parArrayOfString[7], false);
				Block block = getBlockByText(parICommandSender, parArrayOfString[8]);
				int j = parseInt(parArrayOfString[9], -1, 15);
				BlockPos blockpos1 = new BlockPos(d3, d4, d5);
				IBlockState iblockstate = world.getBlockState(blockpos1);
				if (iblockstate.getBlock() != block
						|| j >= 0 && iblockstate.getBlock().getMetaFromState(iblockstate) != j) {
					throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.getName() });
				}

				b0 = 10;
			}

			String s = buildString(parArrayOfString, b0);
			ICommandSender icommandsender = new ICommandSender() {
				public String getName() {
					return entity.getName();
				}

				public IChatComponent getDisplayName() {
					return entity.getDisplayName();
				}

				public void addChatMessage(IChatComponent component) {
					parICommandSender.addChatMessage(component);
				}

				public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
					return parICommandSender.canCommandSenderUseCommand(permLevel, commandName);
				}

				public BlockPos getPosition() {
					return blockpos;
				}

				public Vec3 getPositionVector() {
					return new Vec3(d0, d1, d2);
				}

				public World getEntityWorld() {
					return entity.worldObj;
				}

				public Entity getCommandSenderEntity() {
					return entity;
				}

				public boolean sendCommandFeedback() {
					MinecraftServer minecraftserver = MinecraftServer.getServer();
					return minecraftserver == null
							|| minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
				}

				public void setCommandStat(CommandResultStats.Type type, int amount) {
					entity.setCommandStat(type, amount);
				}
			};
			ICommandManager icommandmanager = MinecraftServer.getServer().getCommandManager();

			try {
				int i = icommandmanager.executeCommand(icommandsender, s);
				if (i < 1) {
					throw new CommandException("commands.execute.allInvocationsFailed", new Object[] { s });
				}
			} catch (Throwable var23) {
				throw new CommandException("commands.execute.failed", new Object[] { s, entity.getName() });
			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos blockpos) {
		return astring.length == 1
				? getListOfStringsMatchingLastWord(astring, MinecraftServer.getServer().getAllUsernames())
				: (astring.length > 1 && astring.length <= 4 ? func_175771_a(astring, 1, blockpos)
						: (astring.length > 5 && astring.length <= 8 && "detect".equals(astring[4])
								? func_175771_a(astring, 5, blockpos)
								: (astring.length == 9 && "detect".equals(astring[4])
										? getListOfStringsMatchingLastWord(astring, Block.blockRegistry.getKeys())
										: null)));
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] var1, int i) {
		return i == 0;
	}
}