package net.minecraft.stats;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.StatCollector;

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
public class Achievement extends StatBase {
	public final int displayColumn;
	public final int displayRow;
	public final Achievement parentAchievement;
	private final String achievementDescription;
	private IStatStringFormat statStringFormatter;
	public final ItemStack theItemStack;
	private boolean isSpecial;

	public Achievement(String parString1, String parString2, int column, int row, Item parItem, Achievement parent) {
		this(parString1, parString2, column, row, new ItemStack(parItem), parent);
	}

	public Achievement(String parString1, String parString2, int column, int row, Block parBlock, Achievement parent) {
		this(parString1, parString2, column, row, new ItemStack(parBlock), parent);
	}

	public Achievement(String parString1, String parString2, int column, int row, ItemStack parItemStack,
			Achievement parent) {
		super(parString1, new ChatComponentTranslation("achievement." + parString2, new Object[0]));
		this.theItemStack = parItemStack;
		this.achievementDescription = "achievement." + parString2 + ".desc";
		this.displayColumn = column;
		this.displayRow = row;
		if (column < AchievementList.minDisplayColumn) {
			AchievementList.minDisplayColumn = column;
		}

		if (row < AchievementList.minDisplayRow) {
			AchievementList.minDisplayRow = row;
		}

		if (column > AchievementList.maxDisplayColumn) {
			AchievementList.maxDisplayColumn = column;
		}

		if (row > AchievementList.maxDisplayRow) {
			AchievementList.maxDisplayRow = row;
		}

		this.parentAchievement = parent;
	}

	/**+
	 * Initializes the current stat as independent (i.e., lacking
	 * prerequisites for being updated) and returns the current
	 * instance.
	 */
	public Achievement initIndependentStat() {
		this.isIndependent = true;
		return this;
	}

	/**+
	 * Special achievements have a 'spiked' (on normal texture pack)
	 * frame, special achievements are the hardest ones to achieve.
	 */
	public Achievement setSpecial() {
		this.isSpecial = true;
		return this;
	}

	/**+
	 * Register the stat into StatList.
	 */
	public Achievement registerStat() {
		super.registerStat();
		AchievementList.achievementList.add(this);
		return this;
	}

	/**+
	 * Returns whether or not the StatBase-derived class is a
	 * statistic (running counter) or an achievement (one-shot).
	 */
	public boolean isAchievement() {
		return true;
	}

	public IChatComponent getStatName() {
		IChatComponent ichatcomponent = super.getStatName();
		ichatcomponent.getChatStyle()
				.setColor(this.getSpecial() ? EnumChatFormatting.DARK_PURPLE : EnumChatFormatting.GREEN);
		return ichatcomponent;
	}

	public Achievement func_150953_b(Class<? extends IJsonSerializable> parClass1,
			Supplier<? extends IJsonSerializable> ctor) {
		return (Achievement) super.func_150953_b(parClass1, ctor);
	}

	/**+
	 * Returns the fully description of the achievement - ready to
	 * be displayed on screen.
	 */
	public String getDescription() {
		return this.statStringFormatter != null
				? this.statStringFormatter.formatString(StatCollector.translateToLocal(this.achievementDescription))
				: StatCollector.translateToLocal(this.achievementDescription);
	}

	/**+
	 * Defines a string formatter for the achievement.
	 */
	public Achievement setStatStringFormatter(IStatStringFormat parIStatStringFormat) {
		this.statStringFormatter = parIStatStringFormat;
		return this;
	}

	/**+
	 * Special achievements have a 'spiked' (on normal texture pack)
	 * frame, special achievements are the hardest ones to achieve.
	 */
	public boolean getSpecial() {
		return this.isSpecial;
	}
}