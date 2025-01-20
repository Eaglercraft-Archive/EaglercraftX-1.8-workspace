package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

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
public class PlayerSelector {
	/**+
	 * This matches the at-tokens introduced for command blocks,
	 * including their arguments, if any.
	 */
	private static final Pattern tokenPattern = Pattern.compile("^@([pare])(?:\\[([\\w=,!-]*)\\])?$");
	/**+
	 * This matches things like "-1,,4", and is used for getting
	 * x,y,z,range from the token's argument list.
	 */
	private static final Pattern intListPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
	/**+
	 * This matches things like "rm=4,c=2" and is used for handling
	 * named token arguments.
	 */
	private static final Pattern keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");
	/**+
	 * A set of arguments that will change the selector's world list
	 * to the sender's world instead of all the worlds when present
	 */
	private static final Set<String> WORLD_BINDING_ARGS = Sets
			.newHashSet(new String[] { "x", "y", "z", "dx", "dy", "dz", "rm", "r" });

	/**+
	 * Returns the one player that matches the given at-token.
	 * Returns null if more than one player matches.
	 */
	public static EntityPlayerMP matchOnePlayer(ICommandSender sender, String token) {
		return (EntityPlayerMP) matchOneEntity(sender, token, EntityPlayerMP.class);
	}

	public static <T extends Entity> T matchOneEntity(ICommandSender sender, String token,
			Class<? extends T> targetClass) {
		List list = matchEntities(sender, token, targetClass);
		return (T) (list.size() == 1 ? (Entity) list.get(0) : null);
	}

	public static IChatComponent matchEntitiesToChatComponent(ICommandSender sender, String token) {
		List<Entity> list = matchEntities(sender, token, Entity.class);
		if (list.isEmpty()) {
			return null;
		} else {
			ArrayList arraylist = Lists.newArrayList();

			for (int i = 0, l = list.size(); i < l; ++i) {
				arraylist.add(list.get(i).getDisplayName());
			}

			return CommandBase.join(arraylist);
		}
	}

	public static <T extends Entity> List<T> matchEntities(ICommandSender sender, String token,
			Class<? extends T> targetClass) {
		Matcher matcher = tokenPattern.matcher(token);
		if (matcher.matches() && sender.canCommandSenderUseCommand(1, "@")) {
			Map map = getArgumentMap(matcher.group(2));
			if (!isEntityTypeValid(sender, map)) {
				return Collections.emptyList();
			} else {
				String s = matcher.group(1);
				BlockPos blockpos = func_179664_b(map, sender.getPosition());
				List<World> list = getWorlds(sender, map);
				ArrayList arraylist = Lists.newArrayList();

				for (int i = 0, l = list.size(); i < l; ++i) {
					World world = list.get(i);
					if (world != null) {
						ArrayList arraylist1 = Lists.newArrayList();
						arraylist1.addAll(func_179663_a(map, s));
						arraylist1.addAll(func_179648_b(map));
						arraylist1.addAll(func_179649_c(map));
						arraylist1.addAll(func_179659_d(map));
						arraylist1.addAll(func_179657_e(map));
						arraylist1.addAll(func_179647_f(map));
						arraylist1.addAll(func_180698_a(map, blockpos));
						arraylist1.addAll(func_179662_g(map));
						arraylist.addAll(filterResults(map, targetClass, arraylist1, s, world, blockpos));
					}
				}

				return func_179658_a(arraylist, map, sender, targetClass, s, blockpos);
			}
		} else {
			return Collections.emptyList();
		}
	}

	/**+
	 * Returns the worlds to match the entities in for the specified
	 * command sender and token. This returns the sender's world if
	 * the selector specifies a location or all currently loaded
	 * worlds on the server if not.
	 */
	private static List<World> getWorlds(ICommandSender sender, Map<String, String> argumentMap) {
		ArrayList arraylist = Lists.newArrayList();
		if (func_179665_h(argumentMap)) {
			arraylist.add(sender.getEntityWorld());
		} else {
			Collections.addAll(arraylist, MinecraftServer.getServer().worldServers);
		}

		return arraylist;
	}

	private static <T extends Entity> boolean isEntityTypeValid(ICommandSender commandSender,
			Map<String, String> params) {
		String s = func_179651_b(params, "type");
		s = s != null && s.startsWith("!") ? s.substring(1) : s;
		if (s != null && !EntityList.isStringValidEntityName(s)) {
			ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
					"commands.generic.entity.invalidType", new Object[] { s });
			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
			commandSender.addChatMessage(chatcomponenttranslation);
			return false;
		} else {
			return true;
		}
	}

	private static List<Predicate<Entity>> func_179663_a(Map<String, String> parMap, String parString1) {
		ArrayList arraylist = Lists.newArrayList();
		String s = func_179651_b(parMap, "type");
		final boolean flag = s != null && s.startsWith("!");
		if (flag) {
			s = s.substring(1);
		}

		boolean flag1 = !parString1.equals("e");
		boolean flag2 = parString1.equals("r") && s != null;
		if ((s == null || !parString1.equals("e")) && !flag2) {
			if (flag1) {
				arraylist.add(new Predicate<Entity>() {
					public boolean apply(Entity entity) {
						return entity instanceof EntityPlayer;
					}
				});
			}
		} else {
			final String ss = s;
			arraylist.add(new Predicate<Entity>() {
				public boolean apply(Entity entity) {
					return EntityList.isStringEntityName(entity, ss) != flag;
				}
			});
		}

		return arraylist;
	}

	private static List<Predicate<Entity>> func_179648_b(Map<String, String> parMap) {
		ArrayList arraylist = Lists.newArrayList();
		final int i = parseIntWithDefault(parMap, "lm", -1);
		final int j = parseIntWithDefault(parMap, "l", -1);
		if (i > -1 || j > -1) {
			arraylist.add(new Predicate<Entity>() {
				public boolean apply(Entity entity) {
					if (!(entity instanceof EntityPlayerMP)) {
						return false;
					} else {
						EntityPlayerMP entityplayermp = (EntityPlayerMP) entity;
						return (i <= -1 || entityplayermp.experienceLevel >= i)
								&& (j <= -1 || entityplayermp.experienceLevel <= j);
					}
				}
			});
		}

		return arraylist;
	}

	private static List<Predicate<Entity>> func_179649_c(Map<String, String> parMap) {
		ArrayList arraylist = Lists.newArrayList();
		final int i = parseIntWithDefault(parMap, "m", WorldSettings.GameType.NOT_SET.getID());
		if (i != WorldSettings.GameType.NOT_SET.getID()) {
			arraylist.add(new Predicate<Entity>() {
				public boolean apply(Entity entity) {
					if (!(entity instanceof EntityPlayerMP)) {
						return false;
					} else {
						EntityPlayerMP entityplayermp = (EntityPlayerMP) entity;
						return entityplayermp.theItemInWorldManager.getGameType().getID() == i;
					}
				}
			});
		}

		return arraylist;
	}

	private static List<Predicate<Entity>> func_179659_d(Map<String, String> parMap) {
		ArrayList arraylist = Lists.newArrayList();
		String s = func_179651_b(parMap, "team");
		final boolean flag = s != null && s.startsWith("!");
		if (flag) {
			s = s.substring(1);
		}

		if (s != null) {
			final String ss = s;
			arraylist.add(new Predicate<Entity>() {
				public boolean apply(Entity entity) {
					if (!(entity instanceof EntityLivingBase)) {
						return false;
					} else {
						EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
						Team team = entitylivingbase.getTeam();
						String s1 = team == null ? "" : team.getRegisteredName();
						return s1.equals(ss) != flag;
					}
				}
			});
		}

		return arraylist;
	}

	private static List<Predicate<Entity>> func_179657_e(Map<String, String> parMap) {
		ArrayList arraylist = Lists.newArrayList();
		final Map map = func_96560_a(parMap);
		if (map != null && map.size() > 0) {
			arraylist.add(new Predicate<Entity>() {
				public boolean apply(Entity entity) {
					Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();

					for (Entry entry : (Set<Entry>) map.entrySet()) {
						String s = (String) entry.getKey();
						boolean flag = false;
						if (s.endsWith("_min") && s.length() > 4) {
							flag = true;
							s = s.substring(0, s.length() - 4);
						}

						ScoreObjective scoreobjective = scoreboard.getObjective(s);
						if (scoreobjective == null) {
							return false;
						}

						String s1 = entity instanceof EntityPlayerMP ? entity.getName()
								: entity.getUniqueID().toString();
						if (!scoreboard.entityHasObjective(s1, scoreobjective)) {
							return false;
						}

						Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
						int i = score.getScorePoints();
						if (i < ((Integer) entry.getValue()).intValue() && flag) {
							return false;
						}

						if (i > ((Integer) entry.getValue()).intValue() && !flag) {
							return false;
						}
					}

					return true;
				}
			});
		}

		return arraylist;
	}

	private static List<Predicate<Entity>> func_179647_f(Map<String, String> parMap) {
		ArrayList arraylist = Lists.newArrayList();
		String s = func_179651_b(parMap, "name");
		final boolean flag = s != null && s.startsWith("!");
		if (flag) {
			s = s.substring(1);
		}

		if (s != null) {
			final String ss = s;
			arraylist.add(new Predicate<Entity>() {
				public boolean apply(Entity entity) {
					return entity.getName().equals(ss) != flag;
				}
			});
		}

		return arraylist;
	}

	private static List<Predicate<Entity>> func_180698_a(Map<String, String> parMap, final BlockPos parBlockPos) {
		ArrayList arraylist = Lists.newArrayList();
		final int i = parseIntWithDefault(parMap, "rm", -1);
		final int j = parseIntWithDefault(parMap, "r", -1);
		if (parBlockPos != null && (i >= 0 || j >= 0)) {
			final int k = i * i;
			final int l = j * j;
			arraylist.add(new Predicate<Entity>() {
				public boolean apply(Entity entity) {
					int i1 = (int) entity.getDistanceSqToCenter(parBlockPos);
					return (i < 0 || i1 >= k) && (j < 0 || i1 <= l);
				}
			});
		}

		return arraylist;
	}

	private static List<Predicate<Entity>> func_179662_g(Map<String, String> parMap) {
		ArrayList arraylist = Lists.newArrayList();
		if (parMap.containsKey("rym") || parMap.containsKey("ry")) {
			final int i = func_179650_a(parseIntWithDefault(parMap, "rym", 0));
			final int j = func_179650_a(parseIntWithDefault(parMap, "ry", 359));
			arraylist.add(new Predicate<Entity>() {
				public boolean apply(Entity entity) {
					int i1 = PlayerSelector.func_179650_a((int) Math.floor((double) entity.rotationYaw));
					return i > j ? i1 >= i || i1 <= j : i1 >= i && i1 <= j;
				}
			});
		}

		if (parMap.containsKey("rxm") || parMap.containsKey("rx")) {
			final int k = func_179650_a(parseIntWithDefault(parMap, "rxm", 0));
			final int l = func_179650_a(parseIntWithDefault(parMap, "rx", 359));
			arraylist.add(new Predicate<Entity>() {
				public boolean apply(Entity entity) {
					int i1 = PlayerSelector.func_179650_a((int) Math.floor((double) entity.rotationPitch));
					return k > l ? i1 >= k || i1 <= l : i1 >= k && i1 <= l;
				}
			});
		}

		return arraylist;
	}

	private static <T extends Entity> List<T> filterResults(Map<String, String> params, Class<? extends T> entityClass,
			List<Predicate<Entity>> inputList, String type, World worldIn, BlockPos position) {
		ArrayList arraylist = Lists.newArrayList();
		String s = func_179651_b(params, "type");
		s = s != null && s.startsWith("!") ? s.substring(1) : s;
		boolean flag = !type.equals("e");
		boolean flag1 = type.equals("r") && s != null;
		int i = parseIntWithDefault(params, "dx", 0);
		int j = parseIntWithDefault(params, "dy", 0);
		int k = parseIntWithDefault(params, "dz", 0);
		int l = parseIntWithDefault(params, "r", -1);
		Predicate predicate = Predicates.and(inputList);
		Predicate predicate1 = Predicates.and(EntitySelectors.selectAnything, predicate);
		if (position != null) {
			int i1 = worldIn.playerEntities.size();
			int j1 = worldIn.loadedEntityList.size();
			boolean flag2 = i1 < j1 / 16;
			if (!params.containsKey("dx") && !params.containsKey("dy") && !params.containsKey("dz")) {
				if (l >= 0) {
					AxisAlignedBB axisalignedbb1 = new AxisAlignedBB((double) (position.getX() - l),
							(double) (position.getY() - l), (double) (position.getZ() - l),
							(double) (position.getX() + l + 1), (double) (position.getY() + l + 1),
							(double) (position.getZ() + l + 1));
					if (flag && flag2 && !flag1) {
						arraylist.addAll(worldIn.getPlayers(entityClass, predicate1));
					} else {
						arraylist.addAll(worldIn.getEntitiesWithinAABB(entityClass, axisalignedbb1, predicate1));
					}
				} else if (type.equals("a")) {
					arraylist.addAll(worldIn.getPlayers(entityClass, predicate));
				} else if (!type.equals("p") && (!type.equals("r") || flag1)) {
					arraylist.addAll(worldIn.getEntities(entityClass, predicate1));
				} else {
					arraylist.addAll(worldIn.getPlayers(entityClass, predicate1));
				}
			} else {
				final AxisAlignedBB axisalignedbb = func_179661_a(position, i, j, k);
				if (flag && flag2 && !flag1) {
					Predicate predicate2 = new Predicate<Entity>() {
						public boolean apply(Entity entity) {
							return entity.posX >= axisalignedbb.minX && entity.posY >= axisalignedbb.minY
									&& entity.posZ >= axisalignedbb.minZ
											? entity.posX < axisalignedbb.maxX && entity.posY < axisalignedbb.maxY
													&& entity.posZ < axisalignedbb.maxZ
											: false;
						}
					};
					arraylist.addAll(worldIn.getPlayers(entityClass, Predicates.and(predicate1, predicate2)));
				} else {
					arraylist.addAll(worldIn.getEntitiesWithinAABB(entityClass, axisalignedbb, predicate1));
				}
			}
		} else if (type.equals("a")) {
			arraylist.addAll(worldIn.getPlayers(entityClass, predicate));
		} else if (!type.equals("p") && (!type.equals("r") || flag1)) {
			arraylist.addAll(worldIn.getEntities(entityClass, predicate1));
		} else {
			arraylist.addAll(worldIn.getPlayers(entityClass, predicate1));
		}

		return arraylist;
	}

	private static <T extends Entity> List<T> func_179658_a(List<T> parList, Map<String, String> parMap,
			ICommandSender parICommandSender, Class<? extends T> parClass1, String parString1,
			final BlockPos parBlockPos) {
		int i = parseIntWithDefault(parMap, "c", !parString1.equals("a") && !parString1.equals("e") ? 1 : 0);
		if (!parString1.equals("p") && !parString1.equals("a") && !parString1.equals("e")) {
			if (parString1.equals("r")) {
				Collections.shuffle((List) parList);
			}
		} else if (parBlockPos != null) {
			Collections.sort((List) parList, new Comparator<Entity>() {
				public int compare(Entity entity1, Entity entity2) {
					return ComparisonChain.start()
							.compare(entity1.getDistanceSq(parBlockPos), entity2.getDistanceSq(parBlockPos)).result();
				}
			});
		}

		Entity entity = parICommandSender.getCommandSenderEntity();
		if (entity != null && parClass1.isAssignableFrom(entity.getClass()) && i == 1
				&& ((List) parList).contains(entity) && !"r".equals(parString1)) {
			parList = (List<T>) Lists.newArrayList(entity);
		}

		if (i != 0) {
			if (i < 0) {
				Collections.reverse((List) parList);
			}

			parList = ((List) parList).subList(0, Math.min(Math.abs(i), ((List) parList).size()));
		}

		return (List) parList;
	}

	private static AxisAlignedBB func_179661_a(BlockPos parBlockPos, int parInt1, int parInt2, int parInt3) {
		boolean flag = parInt1 < 0;
		boolean flag1 = parInt2 < 0;
		boolean flag2 = parInt3 < 0;
		int i = parBlockPos.getX() + (flag ? parInt1 : 0);
		int j = parBlockPos.getY() + (flag1 ? parInt2 : 0);
		int k = parBlockPos.getZ() + (flag2 ? parInt3 : 0);
		int l = parBlockPos.getX() + (flag ? 0 : parInt1) + 1;
		int i1 = parBlockPos.getY() + (flag1 ? 0 : parInt2) + 1;
		int j1 = parBlockPos.getZ() + (flag2 ? 0 : parInt3) + 1;
		return new AxisAlignedBB((double) i, (double) j, (double) k, (double) l, (double) i1, (double) j1);
	}

	public static int func_179650_a(int parInt1) {
		parInt1 = parInt1 % 360;
		if (parInt1 >= 160) {
			parInt1 -= 360;
		}

		if (parInt1 < 0) {
			parInt1 += 360;
		}

		return parInt1;
	}

	private static BlockPos func_179664_b(Map<String, String> parMap, BlockPos parBlockPos) {
		return new BlockPos(parseIntWithDefault(parMap, "x", parBlockPos.getX()),
				parseIntWithDefault(parMap, "y", parBlockPos.getY()),
				parseIntWithDefault(parMap, "z", parBlockPos.getZ()));
	}

	private static boolean func_179665_h(Map<String, String> parMap) {
		for (String s : WORLD_BINDING_ARGS) {
			if (parMap.containsKey(s)) {
				return true;
			}
		}

		return false;
	}

	private static int parseIntWithDefault(Map<String, String> parMap, String parString1, int parInt1) {
		return parMap.containsKey(parString1) ? MathHelper.parseIntWithDefault((String) parMap.get(parString1), parInt1)
				: parInt1;
	}

	private static String func_179651_b(Map<String, String> parMap, String parString1) {
		return (String) parMap.get(parString1);
	}

	public static Map<String, Integer> func_96560_a(Map<String, String> parMap) {
		HashMap hashmap = Maps.newHashMap();

		for (String s : parMap.keySet()) {
			if (s.startsWith("score_") && s.length() > "score_".length()) {
				hashmap.put(s.substring("score_".length()),
						Integer.valueOf(MathHelper.parseIntWithDefault((String) parMap.get(s), 1)));
			}
		}

		return hashmap;
	}

	/**+
	 * Returns whether the given pattern can match more than one
	 * player.
	 */
	public static boolean matchesMultiplePlayers(String parString1) {
		Matcher matcher = tokenPattern.matcher(parString1);
		if (!matcher.matches()) {
			return false;
		} else {
			Map map = getArgumentMap(matcher.group(2));
			String s = matcher.group(1);
			int i = !"a".equals(s) && !"e".equals(s) ? 1 : 0;
			return parseIntWithDefault(map, "c", i) != 1;
		}
	}

	/**+
	 * Returns whether the given token has any arguments set.
	 */
	public static boolean hasArguments(String parString1) {
		return tokenPattern.matcher(parString1).matches();
	}

	private static Map<String, String> getArgumentMap(String argumentString) {
		HashMap hashmap = Maps.newHashMap();
		if (argumentString == null) {
			return hashmap;
		} else {
			int i = 0;
			int j = -1;

			for (Matcher matcher = intListPattern.matcher(argumentString); matcher.find(); j = matcher.end()) {
				String s = null;
				switch (i++) {
				case 0:
					s = "x";
					break;
				case 1:
					s = "y";
					break;
				case 2:
					s = "z";
					break;
				case 3:
					s = "r";
				}

				if (s != null && matcher.group(1).length() > 0) {
					hashmap.put(s, matcher.group(1));
				}
			}

			if (j < argumentString.length()) {
				Matcher matcher1 = keyValueListPattern.matcher(j == -1 ? argumentString : argumentString.substring(j));

				while (matcher1.find()) {
					hashmap.put(matcher1.group(1), matcher1.group(2));
				}
			}

			return hashmap;
		}
	}
}