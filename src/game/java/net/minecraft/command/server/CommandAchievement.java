package net.minecraft.command.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
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
public class CommandAchievement extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "achievement";
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
		return "commands.achievement.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 2) {
			throw new WrongUsageException("commands.achievement.usage", new Object[0]);
		} else {
			final StatBase statbase = StatList.getOneShotStat(parArrayOfString[1]);
			if (statbase == null && !parArrayOfString[1].equals("*")) {
				throw new CommandException("commands.achievement.unknownAchievement",
						new Object[] { parArrayOfString[1] });
			} else {
				final EntityPlayerMP entityplayermp = parArrayOfString.length >= 3
						? getPlayer(parICommandSender, parArrayOfString[2])
						: getCommandSenderAsPlayer(parICommandSender);
				boolean flag = parArrayOfString[0].equalsIgnoreCase("give");
				boolean flag1 = parArrayOfString[0].equalsIgnoreCase("take");
				if (flag || flag1) {
					if (statbase == null) {
						if (flag) {
							for (int i = 0, l = AchievementList.achievementList.size(); i < l; ++i) {
								entityplayermp.triggerAchievement(AchievementList.achievementList.get(i));
							}

							notifyOperators(parICommandSender, this, "commands.achievement.give.success.all",
									new Object[] { entityplayermp.getName() });
						} else if (flag1) {
							List<Achievement> ach = Lists.reverse(AchievementList.achievementList);
							for (int i = 0, l = ach.size(); i < l; ++i) {
								entityplayermp.func_175145_a(ach.get(i));
							}

							notifyOperators(parICommandSender, this, "commands.achievement.take.success.all",
									new Object[] { entityplayermp.getName() });
						}

					} else {
						if (statbase instanceof Achievement) {
							Achievement achievement = (Achievement) statbase;
							if (flag) {
								if (entityplayermp.getStatFile().hasAchievementUnlocked(achievement)) {
									throw new CommandException("commands.achievement.alreadyHave",
											new Object[] { entityplayermp.getName(), statbase.func_150955_j() });
								}

								ArrayList arraylist;
								for (arraylist = Lists.newArrayList(); achievement.parentAchievement != null
										&& !entityplayermp.getStatFile().hasAchievementUnlocked(
												achievement.parentAchievement); achievement = achievement.parentAchievement) {
									arraylist.add(achievement.parentAchievement);
								}

								List<Achievement> ach = Lists.reverse(AchievementList.achievementList);
								for (int i = 0, l = ach.size(); i < l; ++i) {
									entityplayermp.triggerAchievement(ach.get(i));
								}
							} else if (flag1) {
								if (!entityplayermp.getStatFile().hasAchievementUnlocked(achievement)) {
									throw new CommandException("commands.achievement.dontHave",
											new Object[] { entityplayermp.getName(), statbase.func_150955_j() });
								}

								ArrayList arraylist1 = Lists.newArrayList(Iterators.filter(
										AchievementList.achievementList.iterator(), new Predicate<Achievement>() {
											public boolean apply(Achievement achievement7) {
												return entityplayermp.getStatFile().hasAchievementUnlocked(achievement7)
														&& achievement7 != statbase;
											}
										}));
								ArrayList arraylist2 = Lists.newArrayList(arraylist1);

								for (int i = 0, l = arraylist1.size(); i < l; ++i) {
									Achievement achievement2 = (Achievement) arraylist1.get(i);
									Achievement achievement3 = achievement2;

									boolean flag2;
									for (flag2 = false; achievement3 != null; achievement3 = achievement3.parentAchievement) {
										if (achievement3 == statbase) {
											flag2 = true;
										}
									}

									if (!flag2) {
										for (achievement3 = achievement2; achievement3 != null; achievement3 = achievement3.parentAchievement) {
											arraylist2.remove(achievement2);
										}
									}
								}

								for (int i = 0, l = arraylist2.size(); i < l; ++i) {
									entityplayermp.func_175145_a((Achievement) arraylist2.get(i));
								}
							}
						}

						if (flag) {
							entityplayermp.triggerAchievement(statbase);
							notifyOperators(parICommandSender, this, "commands.achievement.give.success.one",
									new Object[] { entityplayermp.getName(), statbase.func_150955_j() });
						} else if (flag1) {
							entityplayermp.func_175145_a(statbase);
							notifyOperators(parICommandSender, this, "commands.achievement.take.success.one",
									new Object[] { statbase.func_150955_j(), entityplayermp.getName() });
						}

					}
				}
			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		if (astring.length == 1) {
			return getListOfStringsMatchingLastWord(astring, new String[] { "give", "take" });
		} else if (astring.length != 2) {
			return astring.length == 3
					? getListOfStringsMatchingLastWord(astring, MinecraftServer.getServer().getAllUsernames())
					: null;
		} else {
			ArrayList arraylist = Lists.newArrayList();

			for (int i = 0, l = StatList.allStats.size(); i < l; ++i) {
				arraylist.add(StatList.allStats.get(i).statId);
			}

			return getListOfStringsMatchingLastWord(astring, arraylist);
		}
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] var1, int i) {
		return i == 2;
	}
}