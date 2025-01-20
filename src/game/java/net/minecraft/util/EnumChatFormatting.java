package net.minecraft.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
public enum EnumChatFormatting {
	BLACK("BLACK", '0', 0), DARK_BLUE("DARK_BLUE", '1', 1), DARK_GREEN("DARK_GREEN", '2', 2),
	DARK_AQUA("DARK_AQUA", '3', 3), DARK_RED("DARK_RED", '4', 4), DARK_PURPLE("DARK_PURPLE", '5', 5),
	GOLD("GOLD", '6', 6), GRAY("GRAY", '7', 7), DARK_GRAY("DARK_GRAY", '8', 8), BLUE("BLUE", '9', 9),
	GREEN("GREEN", 'a', 10), AQUA("AQUA", 'b', 11), RED("RED", 'c', 12), LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13),
	YELLOW("YELLOW", 'e', 14), WHITE("WHITE", 'f', 15), OBFUSCATED("OBFUSCATED", 'k', true), BOLD("BOLD", 'l', true),
	STRIKETHROUGH("STRIKETHROUGH", 'm', true), UNDERLINE("UNDERLINE", 'n', true), ITALIC("ITALIC", 'o', true),
	RESET("RESET", 'r', -1);

	public static final EnumChatFormatting[] _VALUES = values();

	private static final Map<String, EnumChatFormatting> nameMapping = Maps.newHashMap();
	/**+
	 * Matches formatting codes that indicate that the client should
	 * treat the following text as bold, recolored, obfuscated, etc.
	 */
	private static final Pattern formattingCodePattern = Pattern
			.compile("(?i)" + String.valueOf('\u00a7') + "[0-9A-FK-OR]");
	private final String name;
	private final char formattingCode;
	private final boolean fancyStyling;
	private final String controlString;
	private final int colorIndex;

	private static String func_175745_c(String parString1) {
		return parString1.toLowerCase().replaceAll("[^a-z]", "");
	}

	private EnumChatFormatting(String formattingName, char formattingCodeIn, int colorIndex) {
		this(formattingName, formattingCodeIn, false, colorIndex);
	}

	private EnumChatFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn) {
		this(formattingName, formattingCodeIn, fancyStylingIn, -1);
	}

	private EnumChatFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn, int colorIndex) {
		this.name = formattingName;
		this.formattingCode = formattingCodeIn;
		this.fancyStyling = fancyStylingIn;
		this.colorIndex = colorIndex;
		this.controlString = "\u00a7" + formattingCodeIn;
	}

	/**+
	 * Returns the numerical color index that represents this
	 * formatting
	 */
	public int getColorIndex() {
		return this.colorIndex;
	}

	/**+
	 * False if this is just changing the color or resetting; true
	 * otherwise.
	 */
	public boolean isFancyStyling() {
		return this.fancyStyling;
	}

	/**+
	 * Checks if this is a color code.
	 */
	public boolean isColor() {
		return !this.fancyStyling && this != RESET;
	}

	/**+
	 * Gets the friendly name of this value.
	 */
	public String getFriendlyName() {
		return this.name().toLowerCase();
	}

	public String toString() {
		return this.controlString;
	}

	/**+
	 * Returns a copy of the given string, with formatting codes
	 * stripped away.
	 */
	public static String getTextWithoutFormattingCodes(String text) {
		return text == null ? null : formattingCodePattern.matcher(text).replaceAll("");
	}

	/**+
	 * Gets a value by its friendly name; null if the given name
	 * does not map to a defined value.
	 */
	public static EnumChatFormatting getValueByName(String friendlyName) {
		return friendlyName == null ? null : (EnumChatFormatting) nameMapping.get(func_175745_c(friendlyName));
	}

	public static EnumChatFormatting func_175744_a(int parInt1) {
		if (parInt1 < 0) {
			return RESET;
		} else {
			EnumChatFormatting[] types = _VALUES;
			for (int i = 0; i < types.length; ++i) {
				EnumChatFormatting enumchatformatting = types[i];
				if (enumchatformatting.getColorIndex() == parInt1) {
					return enumchatformatting;
				}
			}

			return null;
		}
	}

	/**+
	 * Gets all the valid values. Args: @param par0: Whether or not
	 * to include color values. @param par1: Whether or not to
	 * include fancy-styling values (anything that isn't a color
	 * value or the "reset" value).
	 */
	public static Collection<String> getValidValues(boolean parFlag, boolean parFlag2) {
		ArrayList arraylist = Lists.newArrayList();

		EnumChatFormatting[] types = _VALUES;
		for (int i = 0; i < types.length; ++i) {
			EnumChatFormatting enumchatformatting = types[i];
			if ((!enumchatformatting.isColor() || parFlag) && (!enumchatformatting.isFancyStyling() || parFlag2)) {
				arraylist.add(enumchatformatting.getFriendlyName());
			}
		}

		return arraylist;
	}

	static {
		EnumChatFormatting[] types = _VALUES;
		for (int i = 0; i < types.length; ++i) {
			nameMapping.put(func_175745_c(types[i].name), types[i]);
		}

	}
}