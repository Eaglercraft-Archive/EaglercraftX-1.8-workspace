package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
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
public class CommandResultStats {

	/**+
	 * The number of result command result types that are possible.
	 */
	private static final int NUM_RESULT_TYPES = CommandResultStats.Type._VALUES.length;
	private static final String[] STRING_RESULT_TYPES = new String[NUM_RESULT_TYPES];
	private String[] field_179675_c;
	private String[] field_179673_d;

	public CommandResultStats() {
		this.field_179675_c = STRING_RESULT_TYPES;
		this.field_179673_d = STRING_RESULT_TYPES;
	}

	public void func_179672_a(final ICommandSender sender, CommandResultStats.Type resultTypeIn, int parInt1) {
		String s = this.field_179675_c[resultTypeIn.getTypeID()];
		if (s != null) {
			ICommandSender icommandsender = new ICommandSender() {
				public String getName() {
					return sender.getName();
				}

				public IChatComponent getDisplayName() {
					return sender.getDisplayName();
				}

				public void addChatMessage(IChatComponent ichatcomponent) {
					sender.addChatMessage(ichatcomponent);
				}

				public boolean canCommandSenderUseCommand(int var1, String var2) {
					return true;
				}

				public BlockPos getPosition() {
					return sender.getPosition();
				}

				public Vec3 getPositionVector() {
					return sender.getPositionVector();
				}

				public World getEntityWorld() {
					return sender.getEntityWorld();
				}

				public Entity getCommandSenderEntity() {
					return sender.getCommandSenderEntity();
				}

				public boolean sendCommandFeedback() {
					return sender.sendCommandFeedback();
				}

				public void setCommandStat(CommandResultStats.Type commandresultstats$type, int i) {
					sender.setCommandStat(commandresultstats$type, i);
				}
			};

			String s1;
			try {
				s1 = CommandBase.getEntityName(icommandsender, s);
			} catch (EntityNotFoundException var11) {
				return;
			}

			String s2 = this.field_179673_d[resultTypeIn.getTypeID()];
			if (s2 != null) {
				Scoreboard scoreboard = sender.getEntityWorld().getScoreboard();
				ScoreObjective scoreobjective = scoreboard.getObjective(s2);
				if (scoreobjective != null) {
					if (scoreboard.entityHasObjective(s1, scoreobjective)) {
						Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
						score.setScorePoints(parInt1);
					}
				}
			}
		}
	}

	public void readStatsFromNBT(NBTTagCompound tagcompound) {
		if (tagcompound.hasKey("CommandStats", 10)) {
			NBTTagCompound nbttagcompound = tagcompound.getCompoundTag("CommandStats");

			CommandResultStats.Type[] types = CommandResultStats.Type.values();
			for (int i = 0; i < types.length; ++i) {
				CommandResultStats.Type commandresultstats$type = types[i];
				String s = commandresultstats$type.getTypeName() + "Name";
				String s1 = commandresultstats$type.getTypeName() + "Objective";
				if (nbttagcompound.hasKey(s, 8) && nbttagcompound.hasKey(s1, 8)) {
					String s2 = nbttagcompound.getString(s);
					String s3 = nbttagcompound.getString(s1);
					func_179667_a(this, commandresultstats$type, s2, s3);
				}
			}

		}
	}

	public void writeStatsToNBT(NBTTagCompound tagcompound) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();

		CommandResultStats.Type[] types = CommandResultStats.Type.values();
		for (int i = 0; i < types.length; ++i) {
			CommandResultStats.Type commandresultstats$type = types[i];
			String s = this.field_179675_c[commandresultstats$type.getTypeID()];
			String s1 = this.field_179673_d[commandresultstats$type.getTypeID()];
			if (s != null && s1 != null) {
				nbttagcompound.setString(commandresultstats$type.getTypeName() + "Name", s);
				nbttagcompound.setString(commandresultstats$type.getTypeName() + "Objective", s1);
			}
		}

		if (!nbttagcompound.hasNoTags()) {
			tagcompound.setTag("CommandStats", nbttagcompound);
		}

	}

	public static void func_179667_a(CommandResultStats stats, CommandResultStats.Type resultType, String parString1,
			String parString2) {
		if (parString1 != null && parString1.length() != 0 && parString2 != null && parString2.length() != 0) {
			if (stats.field_179675_c == STRING_RESULT_TYPES || stats.field_179673_d == STRING_RESULT_TYPES) {
				stats.field_179675_c = new String[NUM_RESULT_TYPES];
				stats.field_179673_d = new String[NUM_RESULT_TYPES];
			}

			stats.field_179675_c[resultType.getTypeID()] = parString1;
			stats.field_179673_d[resultType.getTypeID()] = parString2;
		} else {
			func_179669_a(stats, resultType);
		}
	}

	private static void func_179669_a(CommandResultStats resultStatsIn, CommandResultStats.Type resultTypeIn) {
		if (resultStatsIn.field_179675_c != STRING_RESULT_TYPES
				&& resultStatsIn.field_179673_d != STRING_RESULT_TYPES) {
			resultStatsIn.field_179675_c[resultTypeIn.getTypeID()] = null;
			resultStatsIn.field_179673_d[resultTypeIn.getTypeID()] = null;
			boolean flag = true;

			CommandResultStats.Type[] types = CommandResultStats.Type.values();
			for (int i = 0; i < types.length; ++i) {
				CommandResultStats.Type commandresultstats$type = types[i];
				if (resultStatsIn.field_179675_c[commandresultstats$type.getTypeID()] != null
						&& resultStatsIn.field_179673_d[commandresultstats$type.getTypeID()] != null) {
					flag = false;
					break;
				}
			}

			if (flag) {
				resultStatsIn.field_179675_c = STRING_RESULT_TYPES;
				resultStatsIn.field_179673_d = STRING_RESULT_TYPES;
			}

		}
	}

	public void func_179671_a(CommandResultStats resultStatsIn) {
		CommandResultStats.Type[] types = CommandResultStats.Type.values();
		for (int i = 0; i < types.length; ++i) {
			CommandResultStats.Type commandresultstats$type = types[i];
			func_179667_a(this, commandresultstats$type,
					resultStatsIn.field_179675_c[commandresultstats$type.getTypeID()],
					resultStatsIn.field_179673_d[commandresultstats$type.getTypeID()]);
		}

	}

	public static enum Type {
		SUCCESS_COUNT(0, "SuccessCount"), AFFECTED_BLOCKS(1, "AffectedBlocks"),
		AFFECTED_ENTITIES(2, "AffectedEntities"), AFFECTED_ITEMS(3, "AffectedItems"), QUERY_RESULT(4, "QueryResult");

		public static final Type[] _VALUES = values();

		final int typeID;
		final String typeName;

		private Type(int id, String name) {
			this.typeID = id;
			this.typeName = name;
		}

		public int getTypeID() {
			return this.typeID;
		}

		public String getTypeName() {
			return this.typeName;
		}

		public static String[] getTypeNames() {
			String[] astring = new String[_VALUES.length];
			int i = 0;

			CommandResultStats.Type[] types = _VALUES;
			for (int j = 0; j < types.length; ++j) {
				astring[i++] = types[j].getTypeName();
			}

			return astring;
		}

		public static CommandResultStats.Type getTypeByName(String name) {
			CommandResultStats.Type[] types = _VALUES;
			for (int i = 0; i < types.length; ++i) {
				CommandResultStats.Type commandresultstats$type = types[i];
				if (commandresultstats$type.getTypeName().equals(name)) {
					return commandresultstats$type;
				}
			}

			return null;
		}
	}
}