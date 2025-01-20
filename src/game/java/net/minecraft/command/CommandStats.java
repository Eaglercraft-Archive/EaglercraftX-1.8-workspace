package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
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
public class CommandStats extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "stats";
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
		return "commands.stats.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 1) {
			throw new WrongUsageException("commands.stats.usage", new Object[0]);
		} else {
			boolean flag;
			if (parArrayOfString[0].equals("entity")) {
				flag = false;
			} else {
				if (!parArrayOfString[0].equals("block")) {
					throw new WrongUsageException("commands.stats.usage", new Object[0]);
				}

				flag = true;
			}

			int i;
			if (flag) {
				if (parArrayOfString.length < 5) {
					throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
				}

				i = 4;
			} else {
				if (parArrayOfString.length < 3) {
					throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
				}

				i = 2;
			}

			String s = parArrayOfString[i++];
			if ("set".equals(s)) {
				if (parArrayOfString.length < i + 3) {
					if (i == 5) {
						throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
					}

					throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
				}
			} else {
				if (!"clear".equals(s)) {
					throw new WrongUsageException("commands.stats.usage", new Object[0]);
				}

				if (parArrayOfString.length < i + 1) {
					if (i == 5) {
						throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
					}

					throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
				}
			}

			CommandResultStats.Type commandresultstats$type = CommandResultStats.Type
					.getTypeByName(parArrayOfString[i++]);
			if (commandresultstats$type == null) {
				throw new CommandException("commands.stats.failed", new Object[0]);
			} else {
				World world = parICommandSender.getEntityWorld();
				CommandResultStats commandresultstats;
				if (flag) {
					BlockPos blockpos = parseBlockPos(parICommandSender, parArrayOfString, 1, false);
					TileEntity tileentity = world.getTileEntity(blockpos);
					if (tileentity == null) {
						throw new CommandException("commands.stats.noCompatibleBlock",
								new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()),
										Integer.valueOf(blockpos.getZ()) });
					}

					if (tileentity instanceof TileEntityCommandBlock) {
						commandresultstats = ((TileEntityCommandBlock) tileentity).getCommandResultStats();
					} else {
						if (!(tileentity instanceof TileEntitySign)) {
							throw new CommandException("commands.stats.noCompatibleBlock",
									new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()),
											Integer.valueOf(blockpos.getZ()) });
						}

						commandresultstats = ((TileEntitySign) tileentity).getStats();
					}
				} else {
					Entity entity = func_175768_b(parICommandSender, parArrayOfString[1]);
					commandresultstats = entity.getCommandStats();
				}

				if ("set".equals(s)) {
					String s1 = parArrayOfString[i++];
					String s2 = parArrayOfString[i];
					if (s1.length() == 0 || s2.length() == 0) {
						throw new CommandException("commands.stats.failed", new Object[0]);
					}

					CommandResultStats.func_179667_a(commandresultstats, commandresultstats$type, s1, s2);
					notifyOperators(parICommandSender, this, "commands.stats.success",
							new Object[] { commandresultstats$type.getTypeName(), s2, s1 });
				} else if ("clear".equals(s)) {
					CommandResultStats.func_179667_a(commandresultstats, commandresultstats$type, (String) null,
							(String) null);
					notifyOperators(parICommandSender, this, "commands.stats.cleared",
							new Object[] { commandresultstats$type.getTypeName() });
				}

				if (flag) {
					BlockPos blockpos1 = parseBlockPos(parICommandSender, parArrayOfString, 1, false);
					TileEntity tileentity1 = world.getTileEntity(blockpos1);
					tileentity1.markDirty();
				}

			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos blockpos) {
		return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "entity", "block" })
				: (astring.length == 2 && astring[0].equals("entity")
						? getListOfStringsMatchingLastWord(astring, this.func_175776_d())
						: (astring.length >= 2 && astring.length <= 4 && astring[0].equals("block")
								? func_175771_a(astring, 1, blockpos)
								: ((astring.length != 3 || !astring[0].equals("entity"))
										&& (astring.length != 5 || !astring[0].equals("block"))
												? ((astring.length != 4 || !astring[0].equals("entity"))
														&& (astring.length != 6 || !astring[0].equals("block"))
																? ((astring.length != 6 || !astring[0].equals("entity"))
																		&& (astring.length != 8
																				|| !astring[0].equals("block"))
																						? null
																						: getListOfStringsMatchingLastWord(
																								astring,
																								this.func_175777_e()))
																: getListOfStringsMatchingLastWord(astring,
																		CommandResultStats.Type.getTypeNames()))
												: getListOfStringsMatchingLastWord(astring,
														new String[] { "set", "clear" }))));
	}

	protected String[] func_175776_d() {
		return MinecraftServer.getServer().getAllUsernames();
	}

	protected List<String> func_175777_e() {
		Collection collection = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard()
				.getScoreObjectives();
		ArrayList arraylist = Lists.newArrayList();

		for (ScoreObjective scoreobjective : (Collection<ScoreObjective>) collection) {
			if (!scoreobjective.getCriteria().isReadOnly()) {
				arraylist.add(scoreobjective.getName());
			}
		}

		return arraylist;
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] astring, int i) {
		return astring.length > 0 && astring[0].equals("entity") && i == 1;
	}
}