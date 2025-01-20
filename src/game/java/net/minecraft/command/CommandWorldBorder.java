package net.minecraft.command;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.HString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.border.WorldBorder;

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
public class CommandWorldBorder extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "worldborder";
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
		return "commands.worldborder.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 1) {
			throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
		} else {
			WorldBorder worldborder = this.getWorldBorder();
			if (parArrayOfString[0].equals("set")) {
				if (parArrayOfString.length != 2 && parArrayOfString.length != 3) {
					throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
				}

				double d0 = worldborder.getTargetSize();
				double d2 = parseDouble(parArrayOfString[1], 1.0D, 6.0E7D);
				long i = parArrayOfString.length > 2 ? parseLong(parArrayOfString[2], 0L, 9223372036854775L) * 1000L
						: 0L;
				if (i > 0L) {
					worldborder.setTransition(d0, d2, i);
					if (d0 > d2) {
						notifyOperators(parICommandSender, this, "commands.worldborder.setSlowly.shrink.success",
								new Object[] { HString.format("%.1f", new Object[] { Double.valueOf(d2) }),
										HString.format("%.1f", new Object[] { Double.valueOf(d0) }),
										Long.toString(i / 1000L) });
					} else {
						notifyOperators(parICommandSender, this, "commands.worldborder.setSlowly.grow.success",
								new Object[] { HString.format("%.1f", new Object[] { Double.valueOf(d2) }),
										HString.format("%.1f", new Object[] { Double.valueOf(d0) }),
										Long.toString(i / 1000L) });
					}
				} else {
					worldborder.setTransition(d2);
					notifyOperators(parICommandSender, this, "commands.worldborder.set.success",
							new Object[] { HString.format("%.1f", new Object[] { Double.valueOf(d2) }),
									HString.format("%.1f", new Object[] { Double.valueOf(d0) }) });
				}
			} else if (parArrayOfString[0].equals("add")) {
				if (parArrayOfString.length != 2 && parArrayOfString.length != 3) {
					throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
				}

				double d4 = worldborder.getDiameter();
				double d8 = d4 + parseDouble(parArrayOfString[1], -d4, 6.0E7D - d4);
				long i1 = worldborder.getTimeUntilTarget()
						+ (parArrayOfString.length > 2 ? parseLong(parArrayOfString[2], 0L, 9223372036854775L) * 1000L
								: 0L);
				if (i1 > 0L) {
					worldborder.setTransition(d4, d8, i1);
					if (d4 > d8) {
						notifyOperators(parICommandSender, this, "commands.worldborder.setSlowly.shrink.success",
								new Object[] { HString.format("%.1f", new Object[] { Double.valueOf(d8) }),
										HString.format("%.1f", new Object[] { Double.valueOf(d4) }),
										Long.toString(i1 / 1000L) });
					} else {
						notifyOperators(parICommandSender, this, "commands.worldborder.setSlowly.grow.success",
								new Object[] { HString.format("%.1f", new Object[] { Double.valueOf(d8) }),
										HString.format("%.1f", new Object[] { Double.valueOf(d4) }),
										Long.toString(i1 / 1000L) });
					}
				} else {
					worldborder.setTransition(d8);
					notifyOperators(parICommandSender, this, "commands.worldborder.set.success",
							new Object[] { HString.format("%.1f", new Object[] { Double.valueOf(d8) }),
									HString.format("%.1f", new Object[] { Double.valueOf(d4) }) });
				}
			} else if (parArrayOfString[0].equals("center")) {
				if (parArrayOfString.length != 3) {
					throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
				}

				BlockPos blockpos = parICommandSender.getPosition();
				double d1 = parseDouble((double) blockpos.getX() + 0.5D, parArrayOfString[1], true);
				double d3 = parseDouble((double) blockpos.getZ() + 0.5D, parArrayOfString[2], true);
				worldborder.setCenter(d1, d3);
				notifyOperators(parICommandSender, this, "commands.worldborder.center.success",
						new Object[] { Double.valueOf(d1), Double.valueOf(d3) });
			} else if (parArrayOfString[0].equals("damage")) {
				if (parArrayOfString.length < 2) {
					throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
				}

				if (parArrayOfString[1].equals("buffer")) {
					if (parArrayOfString.length != 3) {
						throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
					}

					double d5 = parseDouble(parArrayOfString[2], 0.0D);
					double d9 = worldborder.getDamageBuffer();
					worldborder.setDamageBuffer(d5);
					notifyOperators(parICommandSender, this, "commands.worldborder.damage.buffer.success",
							new Object[] { HString.format("%.1f", new Object[] { Double.valueOf(d5) }),
									HString.format("%.1f", new Object[] { Double.valueOf(d9) }) });
				} else if (parArrayOfString[1].equals("amount")) {
					if (parArrayOfString.length != 3) {
						throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
					}

					double d6 = parseDouble(parArrayOfString[2], 0.0D);
					double d10 = worldborder.getDamageAmount();
					worldborder.setDamageAmount(d6);
					notifyOperators(parICommandSender, this, "commands.worldborder.damage.amount.success",
							new Object[] { HString.format("%.2f", new Object[] { Double.valueOf(d6) }),
									HString.format("%.2f", new Object[] { Double.valueOf(d10) }) });
				}
			} else if (parArrayOfString[0].equals("warning")) {
				if (parArrayOfString.length < 2) {
					throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
				}

				int j = parseInt(parArrayOfString[2], 0);
				if (parArrayOfString[1].equals("time")) {
					if (parArrayOfString.length != 3) {
						throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
					}

					int k = worldborder.getWarningTime();
					worldborder.setWarningTime(j);
					notifyOperators(parICommandSender, this, "commands.worldborder.warning.time.success",
							new Object[] { Integer.valueOf(j), Integer.valueOf(k) });
				} else if (parArrayOfString[1].equals("distance")) {
					if (parArrayOfString.length != 3) {
						throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
					}

					int l = worldborder.getWarningDistance();
					worldborder.setWarningDistance(j);
					notifyOperators(parICommandSender, this, "commands.worldborder.warning.distance.success",
							new Object[] { Integer.valueOf(j), Integer.valueOf(l) });
				}
			} else {
				if (!parArrayOfString[0].equals("get")) {
					throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
				}

				double d7 = worldborder.getDiameter();
				parICommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT,
						MathHelper.floor_double(d7 + 0.5D));
				parICommandSender.addChatMessage(new ChatComponentTranslation("commands.worldborder.get.success",
						new Object[] { HString.format("%.0f", new Object[] { Double.valueOf(d7) }) }));
			}

		}
	}

	protected WorldBorder getWorldBorder() {
		return MinecraftServer.getServer().worldServers[0].getWorldBorder();
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos blockpos) {
		return astring.length == 1
				? getListOfStringsMatchingLastWord(astring,
						new String[] { "set", "center", "damage", "warning", "add", "get" })
				: (astring.length == 2 && astring[0].equals("damage")
						? getListOfStringsMatchingLastWord(astring, new String[] { "buffer", "amount" })
						: (astring.length >= 2 && astring.length <= 3 && astring[0].equals("center")
								? func_181043_b(astring, 1, blockpos)
								: (astring.length == 2 && astring[0].equals("warning")
										? getListOfStringsMatchingLastWord(astring, new String[] { "time", "distance" })
										: null)));
	}
}