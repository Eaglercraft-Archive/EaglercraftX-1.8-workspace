package net.minecraft.command;

import com.carrotsearch.hppc.ObjectContainer;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

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
public abstract class CommandBase implements ICommand {

	private static IAdminCommand theAdmin;

	/**+
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 4;
	}

	/**+
	 * Gets a list of aliases for this command
	 */
	public List<String> getCommandAliases() {
		return Collections.emptyList();
	}

	/**+
	 * Returns true if the given command sender is allowed to use
	 * this command.
	 */
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return icommandsender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
		return null;
	}

	public static int parseInt(String input) throws NumberInvalidException {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException var2) {
			throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
		}
	}

	public static int parseInt(String input, int min) throws NumberInvalidException {
		return parseInt(input, min, Integer.MAX_VALUE);
	}

	public static int parseInt(String input, int min, int max) throws NumberInvalidException {
		int i = parseInt(input);
		if (i < min) {
			throw new NumberInvalidException("commands.generic.num.tooSmall",
					new Object[] { Integer.valueOf(i), Integer.valueOf(min) });
		} else if (i > max) {
			throw new NumberInvalidException("commands.generic.num.tooBig",
					new Object[] { Integer.valueOf(i), Integer.valueOf(max) });
		} else {
			return i;
		}
	}

	public static long parseLong(String input) throws NumberInvalidException {
		try {
			return Long.parseLong(input);
		} catch (NumberFormatException var2) {
			throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
		}
	}

	public static long parseLong(String input, long min, long max) throws NumberInvalidException {
		long i = parseLong(input);
		if (i < min) {
			throw new NumberInvalidException("commands.generic.num.tooSmall",
					new Object[] { Long.valueOf(i), Long.valueOf(min) });
		} else if (i > max) {
			throw new NumberInvalidException("commands.generic.num.tooBig",
					new Object[] { Long.valueOf(i), Long.valueOf(max) });
		} else {
			return i;
		}
	}

	public static BlockPos parseBlockPos(ICommandSender sender, String[] args, int startIndex, boolean centerBlock)
			throws NumberInvalidException {
		BlockPos blockpos = sender.getPosition();
		return new BlockPos(parseDouble((double) blockpos.getX(), args[startIndex], -30000000, 30000000, centerBlock),
				parseDouble((double) blockpos.getY(), args[startIndex + 1], 0, 256, false),
				parseDouble((double) blockpos.getZ(), args[startIndex + 2], -30000000, 30000000, centerBlock));
	}

	public static double parseDouble(String input) throws NumberInvalidException {
		try {
			double d0 = Double.parseDouble(input);
			if (!Doubles.isFinite(d0)) {
				throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
			} else {
				return d0;
			}
		} catch (NumberFormatException var3) {
			throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
		}
	}

	public static double parseDouble(String input, double min) throws NumberInvalidException {
		return parseDouble(input, min, Double.MAX_VALUE);
	}

	public static double parseDouble(String input, double min, double max) throws NumberInvalidException {
		double d0 = parseDouble(input);
		if (d0 < min) {
			throw new NumberInvalidException("commands.generic.double.tooSmall",
					new Object[] { Double.valueOf(d0), Double.valueOf(min) });
		} else if (d0 > max) {
			throw new NumberInvalidException("commands.generic.double.tooBig",
					new Object[] { Double.valueOf(d0), Double.valueOf(max) });
		} else {
			return d0;
		}
	}

	public static boolean parseBoolean(String input) throws CommandException {
		if (!input.equals("true") && !input.equals("1")) {
			if (!input.equals("false") && !input.equals("0")) {
				throw new CommandException("commands.generic.boolean.invalid", new Object[] { input });
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**+
	 * Returns the given ICommandSender as a EntityPlayer or throw
	 * an exception.
	 */
	public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender sender) throws PlayerNotFoundException {
		if (sender instanceof EntityPlayerMP) {
			return (EntityPlayerMP) sender;
		} else {
			throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.",
					new Object[0]);
		}
	}

	public static EntityPlayerMP getPlayer(ICommandSender sender, String username) throws PlayerNotFoundException {
		EntityPlayerMP entityplayermp = PlayerSelector.matchOnePlayer(sender, username);
		if (entityplayermp == null) {
			try {
				entityplayermp = MinecraftServer.getServer().getConfigurationManager()
						.getPlayerByUUID(EaglercraftUUID.fromString(username));
			} catch (IllegalArgumentException var4) {
				;
			}
		}

		if (entityplayermp == null) {
			entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
		}

		if (entityplayermp == null) {
			throw new PlayerNotFoundException();
		} else {
			return entityplayermp;
		}
	}

	public static Entity func_175768_b(ICommandSender parICommandSender, String parString1)
			throws EntityNotFoundException {
		return getEntity(parICommandSender, parString1, Entity.class);
	}

	public static <T extends Entity> T getEntity(ICommandSender commandSender, String parString1,
			Class<? extends T> parClass1) throws EntityNotFoundException {
		Object object = PlayerSelector.matchOneEntity(commandSender, parString1, parClass1);
		MinecraftServer minecraftserver = MinecraftServer.getServer();
		if (object == null) {
			object = minecraftserver.getConfigurationManager().getPlayerByUsername(parString1);
		}

		if (object == null) {
			try {
				EaglercraftUUID uuid = EaglercraftUUID.fromString(parString1);
				object = minecraftserver.getEntityFromUuid(uuid);
				if (object == null) {
					object = minecraftserver.getConfigurationManager().getPlayerByUUID(uuid);
				}
			} catch (IllegalArgumentException var6) {
				throw new EntityNotFoundException("commands.generic.entity.invalidUuid", new Object[0]);
			}
		}

		if (object != null && parClass1.isAssignableFrom(object.getClass())) {
			return (T) object;
		} else {
			throw new EntityNotFoundException();
		}
	}

	public static List<Entity> func_175763_c(ICommandSender parICommandSender, String parString1)
			throws EntityNotFoundException {
		return (List<Entity>) (PlayerSelector.hasArguments(parString1)
				? PlayerSelector.matchEntities(parICommandSender, parString1, Entity.class)
				: Lists.newArrayList(new Entity[] { func_175768_b(parICommandSender, parString1) }));
	}

	public static String getPlayerName(ICommandSender sender, String query) throws PlayerNotFoundException {
		try {
			return getPlayer(sender, query).getName();
		} catch (PlayerNotFoundException playernotfoundexception) {
			if (PlayerSelector.hasArguments(query)) {
				throw playernotfoundexception;
			} else {
				return query;
			}
		}
	}

	/**+
	 * Attempts to retrieve an entity's name, first assuming that
	 * the entity is a player, and then exhausting all other
	 * possibilities.
	 */
	public static String getEntityName(ICommandSender parICommandSender, String parString1)
			throws EntityNotFoundException {
		try {
			return getPlayer(parICommandSender, parString1).getName();
		} catch (PlayerNotFoundException var5) {
			try {
				return func_175768_b(parICommandSender, parString1).getUniqueID().toString();
			} catch (EntityNotFoundException entitynotfoundexception) {
				if (PlayerSelector.hasArguments(parString1)) {
					throw entitynotfoundexception;
				} else {
					return parString1;
				}
			}
		}
	}

	public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int parInt1)
			throws PlayerNotFoundException {
		return getChatComponentFromNthArg(sender, args, parInt1, false);
	}

	public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int index,
			boolean parFlag) throws PlayerNotFoundException {
		ChatComponentText chatcomponenttext = new ChatComponentText("");

		for (int i = index; i < args.length; ++i) {
			if (i > index) {
				chatcomponenttext.appendText(" ");
			}

			Object object = new ChatComponentText(args[i]);
			if (parFlag) {
				IChatComponent ichatcomponent = PlayerSelector.matchEntitiesToChatComponent(sender, args[i]);
				if (ichatcomponent == null) {
					if (PlayerSelector.hasArguments(args[i])) {
						throw new PlayerNotFoundException();
					}
				} else {
					object = ichatcomponent;
				}
			}

			chatcomponenttext.appendSibling((IChatComponent) object);
		}

		return chatcomponenttext;
	}

	/**+
	 * Builds a string starting at startPos
	 */
	public static String buildString(String[] args, int startPos) {
		StringBuilder stringbuilder = new StringBuilder();

		for (int i = startPos; i < args.length; ++i) {
			if (i > startPos) {
				stringbuilder.append(" ");
			}

			String s = args[i];
			stringbuilder.append(s);
		}

		return stringbuilder.toString();
	}

	public static CommandBase.CoordinateArg parseCoordinate(double base, String centerBlock, boolean parFlag)
			throws NumberInvalidException {
		return parseCoordinate(base, centerBlock, -30000000, 30000000, parFlag);
	}

	public static CommandBase.CoordinateArg parseCoordinate(double min, String max, int centerBlock, int parInt2,
			boolean parFlag) throws NumberInvalidException {
		boolean flag = max.startsWith("~");
		if (flag && Double.isNaN(min)) {
			throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { Double.valueOf(min) });
		} else {
			double d0 = 0.0D;
			if (!flag || max.length() > 1) {
				boolean flag1 = max.contains(".");
				if (flag) {
					max = max.substring(1);
				}

				d0 += parseDouble(max);
				if (!flag1 && !flag && parFlag) {
					d0 += 0.5D;
				}
			}

			if (centerBlock != 0 || parInt2 != 0) {
				if (d0 < (double) centerBlock) {
					throw new NumberInvalidException("commands.generic.double.tooSmall",
							new Object[] { Double.valueOf(d0), Integer.valueOf(centerBlock) });
				}

				if (d0 > (double) parInt2) {
					throw new NumberInvalidException("commands.generic.double.tooBig",
							new Object[] { Double.valueOf(d0), Integer.valueOf(parInt2) });
				}
			}

			return new CommandBase.CoordinateArg(d0 + (flag ? min : 0.0D), d0, flag);
		}
	}

	public static double parseDouble(double base, String input, boolean centerBlock) throws NumberInvalidException {
		return parseDouble(base, input, -30000000, 30000000, centerBlock);
	}

	public static double parseDouble(double base, String input, int min, int max, boolean centerBlock)
			throws NumberInvalidException {
		boolean flag = input.startsWith("~");
		if (flag && Double.isNaN(base)) {
			throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { Double.valueOf(base) });
		} else {
			double d0 = flag ? base : 0.0D;
			if (!flag || input.length() > 1) {
				boolean flag1 = input.contains(".");
				if (flag) {
					input = input.substring(1);
				}

				d0 += parseDouble(input);
				if (!flag1 && !flag && centerBlock) {
					d0 += 0.5D;
				}
			}

			if (min != 0 || max != 0) {
				if (d0 < (double) min) {
					throw new NumberInvalidException("commands.generic.double.tooSmall",
							new Object[] { Double.valueOf(d0), Integer.valueOf(min) });
				}

				if (d0 > (double) max) {
					throw new NumberInvalidException("commands.generic.double.tooBig",
							new Object[] { Double.valueOf(d0), Integer.valueOf(max) });
				}
			}

			return d0;
		}
	}

	/**+
	 * Gets the Item specified by the given text string. First
	 * checks the item registry, then tries by parsing the string as
	 * an integer ID (deprecated). Warns the sender if we matched by
	 * parsing the ID. Throws if the item wasn't found. Returns the
	 * item if it was found.
	 */
	public static Item getItemByText(ICommandSender sender, String id) throws NumberInvalidException {
		ResourceLocation resourcelocation = new ResourceLocation(id);
		Item item = (Item) Item.itemRegistry.getObject(resourcelocation);
		if (item == null) {
			throw new NumberInvalidException("commands.give.item.notFound", new Object[] { resourcelocation });
		} else {
			return item;
		}
	}

	/**+
	 * Gets the Block specified by the given text string. First
	 * checks the block registry, then tries by parsing the string
	 * as an integer ID (deprecated). Warns the sender if we matched
	 * by parsing the ID. Throws if the block wasn't found. Returns
	 * the block if it was found.
	 */
	public static Block getBlockByText(ICommandSender sender, String id) throws NumberInvalidException {
		ResourceLocation resourcelocation = new ResourceLocation(id);
		if (!Block.blockRegistry.containsKey(resourcelocation)) {
			throw new NumberInvalidException("commands.give.block.notFound", new Object[] { resourcelocation });
		} else {
			Block block = (Block) Block.blockRegistry.getObject(resourcelocation);
			if (block == null) {
				throw new NumberInvalidException("commands.give.block.notFound", new Object[] { resourcelocation });
			} else {
				return block;
			}
		}
	}

	/**+
	 * Creates a linguistic series joining the input objects
	 * together. Examples: 1) {} --> "", 2) {"Steve"} --> "Steve",
	 * 3) {"Steve", "Phil"} --> "Steve and Phil", 4) {"Steve",
	 * "Phil", "Mark"} --> "Steve, Phil and Mark"
	 */
	public static String joinNiceString(Object[] elements) {
		StringBuilder stringbuilder = new StringBuilder();

		for (int i = 0; i < elements.length; ++i) {
			String s = elements[i].toString();
			if (i > 0) {
				if (i == elements.length - 1) {
					stringbuilder.append(" and ");
				} else {
					stringbuilder.append(", ");
				}
			}

			stringbuilder.append(s);
		}

		return stringbuilder.toString();
	}

	public static IChatComponent join(List<IChatComponent> components) {
		ChatComponentText chatcomponenttext = new ChatComponentText("");

		for (int i = 0; i < components.size(); ++i) {
			if (i > 0) {
				if (i == components.size() - 1) {
					chatcomponenttext.appendText(" and ");
				} else if (i > 0) {
					chatcomponenttext.appendText(", ");
				}
			}

			chatcomponenttext.appendSibling((IChatComponent) components.get(i));
		}

		return chatcomponenttext;
	}

	/**+
	 * Creates a linguistic series joining together the elements of
	 * the given collection. Examples: 1) {} --> "", 2) {"Steve"}
	 * --> "Steve", 3) {"Steve", "Phil"} --> "Steve and Phil", 4)
	 * {"Steve", "Phil", "Mark"} --> "Steve, Phil and Mark"
	 */
	public static String joinNiceStringFromCollection(Collection<String> strings) {
		/**+
		 * Creates a linguistic series joining the input objects
		 * together. Examples: 1) {} --> "", 2) {"Steve"} --> "Steve",
		 * 3) {"Steve", "Phil"} --> "Steve and Phil", 4) {"Steve",
		 * "Phil", "Mark"} --> "Steve, Phil and Mark"
		 */
		return joinNiceString(strings.toArray(new String[strings.size()]));
	}

	public static List<String> func_175771_a(String[] parArrayOfString, int parInt1, BlockPos parBlockPos) {
		if (parBlockPos == null) {
			return null;
		} else {
			int i = parArrayOfString.length - 1;
			String s;
			if (i == parInt1) {
				s = Integer.toString(parBlockPos.getX());
			} else if (i == parInt1 + 1) {
				s = Integer.toString(parBlockPos.getY());
			} else {
				if (i != parInt1 + 2) {
					return null;
				}

				s = Integer.toString(parBlockPos.getZ());
			}

			return Lists.newArrayList(new String[] { s });
		}
	}

	public static List<String> func_181043_b(String[] parArrayOfString, int parInt1, BlockPos parBlockPos) {
		if (parBlockPos == null) {
			return null;
		} else {
			int i = parArrayOfString.length - 1;
			String s;
			if (i == parInt1) {
				s = Integer.toString(parBlockPos.getX());
			} else {
				if (i != parInt1 + 1) {
					return null;
				}

				s = Integer.toString(parBlockPos.getZ());
			}

			return Lists.newArrayList(new String[] { s });
		}
	}

	/**+
	 * Returns true if the given substring is exactly equal to the
	 * start of the given string (case insensitive).
	 */
	public static boolean doesStringStartWith(String original, String region) {
		return region.regionMatches(true, 0, original, 0, original.length());
	}

	/**+
	 * Returns a List of strings (chosen from the given strings)
	 * which the last word in the given string array is a
	 * beginning-match for. (Tab completion).
	 */
	public static List<String> getListOfStringsMatchingLastWord(String[] args, String... possibilities) {
		/**+
		 * Returns a List of strings (chosen from the given strings)
		 * which the last word in the given string array is a
		 * beginning-match for. (Tab completion).
		 */
		return getListOfStringsMatchingLastWord(args, (Collection) Arrays.asList(possibilities));
	}

	/**+
	 * Returns a List of strings (chosen from the given strings)
	 * which the last word in the given string array is a
	 * beginning-match for. (Tab completion).
	 */
	public static List<String> getListOfStringsMatchingLastWord(String[] parArrayOfString,
			Collection<?> parCollection) {
		String s = parArrayOfString[parArrayOfString.length - 1];
		ArrayList arraylist = Lists.newArrayList();
		if (!parCollection.isEmpty()) {
			for (String s1 : Iterables.transform(parCollection, Functions.toStringFunction())) {
				if (doesStringStartWith(s, s1)) {
					arraylist.add(s1);
				}
			}

			if (arraylist.isEmpty()) {
				for (Object object : parCollection) {
					if (object instanceof ResourceLocation
							&& doesStringStartWith(s, ((ResourceLocation) object).getResourcePath())) {
						arraylist.add(String.valueOf(object));
					}
				}
			}
		}

		return arraylist;
	}

	/**+
	 * Returns a List of strings (chosen from the given strings)
	 * which the last word in the given string array is a
	 * beginning-match for. (Tab completion).
	 */
	public static List<String> getListOfStringsMatchingLastWord(String[] parArrayOfString,
			ObjectContainer<String> parCollection) {
		String s = parArrayOfString[parArrayOfString.length - 1];
		ArrayList arraylist = Lists.newArrayList();
		if (!parCollection.isEmpty()) {
			for (ObjectCursor<String> s1_ : parCollection) {
				String s1 = s1_.value;
				if (doesStringStartWith(s, s1)) {
					arraylist.add(s1);
				}
			}
		}

		return arraylist;
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] var1, int var2) {
		return false;
	}

	public static void notifyOperators(ICommandSender sender, ICommand command, String msgFormat, Object... msgParams) {
		notifyOperators(sender, command, 0, msgFormat, msgParams);
	}

	public static void notifyOperators(ICommandSender sender, ICommand command, int msgFormat, String msgParams,
			Object... parArrayOfObject) {
		if (theAdmin != null) {
			theAdmin.notifyOperators(sender, command, msgFormat, msgParams, parArrayOfObject);
		}

	}

	/**+
	 * Sets the static IAdminCommander.
	 */
	public static void setAdminCommander(IAdminCommand command) {
		theAdmin = command;
	}

	public int compareTo(ICommand icommand) {
		return this.getCommandName().compareTo(icommand.getCommandName());
	}

	public static class CoordinateArg {
		private final double field_179633_a;
		private final double field_179631_b;
		private final boolean field_179632_c;

		protected CoordinateArg(double parDouble1, double parDouble2, boolean parFlag) {
			this.field_179633_a = parDouble1;
			this.field_179631_b = parDouble2;
			this.field_179632_c = parFlag;
		}

		public double func_179628_a() {
			return this.field_179633_a;
		}

		public double func_179629_b() {
			return this.field_179631_b;
		}

		public boolean func_179630_c() {
			return this.field_179632_c;
		}
	}
}