package net.minecraft.command;

import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
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
public class CommandEnchant extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "enchant";
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
		return "commands.enchant.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 2) {
			throw new WrongUsageException("commands.enchant.usage", new Object[0]);
		} else {
			EntityPlayerMP entityplayermp = getPlayer(parICommandSender, parArrayOfString[0]);
			parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);

			int i;
			try {
				i = parseInt(parArrayOfString[1], 0);
			} catch (NumberInvalidException numberinvalidexception) {
				Enchantment enchantment = Enchantment.getEnchantmentByLocation(parArrayOfString[1]);
				if (enchantment == null) {
					throw numberinvalidexception;
				}

				i = enchantment.effectId;
			}

			int j = 1;
			ItemStack itemstack = entityplayermp.getCurrentEquippedItem();
			if (itemstack == null) {
				throw new CommandException("commands.enchant.noItem", new Object[0]);
			} else {
				Enchantment enchantment1 = Enchantment.getEnchantmentById(i);
				if (enchantment1 == null) {
					throw new NumberInvalidException("commands.enchant.notFound", new Object[] { Integer.valueOf(i) });
				} else if (!enchantment1.canApply(itemstack)) {
					throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
				} else {
					if (parArrayOfString.length >= 3) {
						j = parseInt(parArrayOfString[2], enchantment1.getMinLevel(), enchantment1.getMaxLevel());
					}

					if (itemstack.hasTagCompound()) {
						NBTTagList nbttaglist = itemstack.getEnchantmentTagList();
						if (nbttaglist != null) {
							for (int k = 0; k < nbttaglist.tagCount(); ++k) {
								short short1 = nbttaglist.getCompoundTagAt(k).getShort("id");
								if (Enchantment.getEnchantmentById(short1) != null) {
									Enchantment enchantment2 = Enchantment.getEnchantmentById(short1);
									if (!enchantment2.canApplyTogether(enchantment1)) {
										throw new CommandException("commands.enchant.cantCombine",
												new Object[] { enchantment1.getTranslatedName(j),
														enchantment2.getTranslatedName(
																nbttaglist.getCompoundTagAt(k).getShort("lvl")) });
									}
								}
							}
						}
					}

					itemstack.addEnchantment(enchantment1, j);
					notifyOperators(parICommandSender, this, "commands.enchant.success", new Object[0]);
					parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
				}
			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, this.getListOfPlayers())
				: (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, Enchantment.func_181077_c()) : null);
	}

	protected String[] getListOfPlayers() {
		return MinecraftServer.getServer().getAllUsernames();
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0;
	}
}