package net.minecraft.item;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IStringSerializable;

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
public enum EnumDyeColor implements IStringSerializable {
	WHITE(0, 15, "white", "white", MapColor.snowColor, EnumChatFormatting.WHITE),
	ORANGE(1, 14, "orange", "orange", MapColor.adobeColor, EnumChatFormatting.GOLD),
	MAGENTA(2, 13, "magenta", "magenta", MapColor.magentaColor, EnumChatFormatting.AQUA),
	LIGHT_BLUE(3, 12, "light_blue", "lightBlue", MapColor.lightBlueColor, EnumChatFormatting.BLUE),
	YELLOW(4, 11, "yellow", "yellow", MapColor.yellowColor, EnumChatFormatting.YELLOW),
	LIME(5, 10, "lime", "lime", MapColor.limeColor, EnumChatFormatting.GREEN),
	PINK(6, 9, "pink", "pink", MapColor.pinkColor, EnumChatFormatting.LIGHT_PURPLE),
	GRAY(7, 8, "gray", "gray", MapColor.grayColor, EnumChatFormatting.DARK_GRAY),
	SILVER(8, 7, "silver", "silver", MapColor.silverColor, EnumChatFormatting.GRAY),
	CYAN(9, 6, "cyan", "cyan", MapColor.cyanColor, EnumChatFormatting.DARK_AQUA),
	PURPLE(10, 5, "purple", "purple", MapColor.purpleColor, EnumChatFormatting.DARK_PURPLE),
	BLUE(11, 4, "blue", "blue", MapColor.blueColor, EnumChatFormatting.DARK_BLUE),
	BROWN(12, 3, "brown", "brown", MapColor.brownColor, EnumChatFormatting.GOLD),
	GREEN(13, 2, "green", "green", MapColor.greenColor, EnumChatFormatting.DARK_GREEN),
	RED(14, 1, "red", "red", MapColor.redColor, EnumChatFormatting.DARK_RED),
	BLACK(15, 0, "black", "black", MapColor.blackColor, EnumChatFormatting.BLACK);

	public static final EnumDyeColor[] META_LOOKUP = new EnumDyeColor[16];
	public static final EnumDyeColor[] DYE_DMG_LOOKUP = new EnumDyeColor[16];
	private final int meta;
	private final int dyeDamage;
	private final String name;
	private final String unlocalizedName;
	private final MapColor mapColor;
	private final EnumChatFormatting chatColor;

	private EnumDyeColor(int meta, int dyeDamage, String name, String unlocalizedName, MapColor mapColorIn,
			EnumChatFormatting chatColor) {
		this.meta = meta;
		this.dyeDamage = dyeDamage;
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.mapColor = mapColorIn;
		this.chatColor = chatColor;
	}

	public int getMetadata() {
		return this.meta;
	}

	public int getDyeDamage() {
		return this.dyeDamage;
	}

	public String getUnlocalizedName() {
		return this.unlocalizedName;
	}

	public MapColor getMapColor() {
		return this.mapColor;
	}

	public static EnumDyeColor byDyeDamage(int damage) {
		if (damage < 0 || damage >= DYE_DMG_LOOKUP.length) {
			damage = 0;
		}

		return DYE_DMG_LOOKUP[damage];
	}

	public static EnumDyeColor byMetadata(int meta) {
		if (meta < 0 || meta >= META_LOOKUP.length) {
			meta = 0;
		}

		return META_LOOKUP[meta];
	}

	public String toString() {
		return this.unlocalizedName;
	}

	public String getName() {
		return this.name;
	}

	static {
		EnumDyeColor[] colors = values();
		for (int i = 0; i < colors.length; ++i) {
			EnumDyeColor enumdyecolor = colors[i];
			META_LOOKUP[enumdyecolor.getMetadata()] = enumdyecolor;
			DYE_DMG_LOOKUP[enumdyecolor.getDyeDamage()] = enumdyecolor;
		}

	}
}