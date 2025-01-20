package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
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
public class CommandTrigger extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "trigger";
	}

	/**+
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 0;
	}

	/**+
	 * Gets the usage string for the command.
	 */
	public String getCommandUsage(ICommandSender var1) {
		return "commands.trigger.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 3) {
			throw new WrongUsageException("commands.trigger.usage", new Object[0]);
		} else {
			EntityPlayerMP entityplayermp;
			if (parICommandSender instanceof EntityPlayerMP) {
				entityplayermp = (EntityPlayerMP) parICommandSender;
			} else {
				Entity entity = parICommandSender.getCommandSenderEntity();
				if (!(entity instanceof EntityPlayerMP)) {
					throw new CommandException("commands.trigger.invalidPlayer", new Object[0]);
				}

				entityplayermp = (EntityPlayerMP) entity;
			}

			Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
			ScoreObjective scoreobjective = scoreboard.getObjective(parArrayOfString[0]);
			if (scoreobjective != null && scoreobjective.getCriteria() == IScoreObjectiveCriteria.TRIGGER) {
				int i = parseInt(parArrayOfString[2]);
				if (!scoreboard.entityHasObjective(entityplayermp.getName(), scoreobjective)) {
					throw new CommandException("commands.trigger.invalidObjective",
							new Object[] { parArrayOfString[0] });
				} else {
					Score score = scoreboard.getValueFromObjective(entityplayermp.getName(), scoreobjective);
					if (score.isLocked()) {
						throw new CommandException("commands.trigger.disabled", new Object[] { parArrayOfString[0] });
					} else {
						if ("set".equals(parArrayOfString[1])) {
							score.setScorePoints(i);
						} else {
							if (!"add".equals(parArrayOfString[1])) {
								throw new CommandException("commands.trigger.invalidMode",
										new Object[] { parArrayOfString[1] });
							}

							score.increseScore(i);
						}

						score.setLocked(true);
						if (entityplayermp.theItemInWorldManager.isCreative()) {
							notifyOperators(parICommandSender, this, "commands.trigger.success",
									new Object[] { parArrayOfString[0], parArrayOfString[1], parArrayOfString[2] });
						}

					}
				}
			} else {
				throw new CommandException("commands.trigger.invalidObjective", new Object[] { parArrayOfString[0] });
			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		if (astring.length == 1) {
			Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
			ArrayList arraylist = Lists.newArrayList();

			for (ScoreObjective scoreobjective : scoreboard.getScoreObjectives()) {
				if (scoreobjective.getCriteria() == IScoreObjectiveCriteria.TRIGGER) {
					arraylist.add(scoreobjective.getName());
				}
			}

			return getListOfStringsMatchingLastWord(astring,
					(String[]) arraylist.toArray(new String[arraylist.size()]));
		} else {
			return astring.length == 2 ? getListOfStringsMatchingLastWord(astring, new String[] { "add", "set" })
					: null;
		}
	}
}