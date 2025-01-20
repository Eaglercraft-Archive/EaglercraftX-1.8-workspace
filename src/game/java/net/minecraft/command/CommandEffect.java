package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

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
public class CommandEffect extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "effect";
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
		return "commands.effect.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 2) {
			throw new WrongUsageException("commands.effect.usage", new Object[0]);
		} else {
			EntityLivingBase entitylivingbase = (EntityLivingBase) getEntity(parICommandSender, parArrayOfString[0],
					EntityLivingBase.class);
			if (parArrayOfString[1].equals("clear")) {
				if (entitylivingbase.getActivePotionEffects().isEmpty()) {
					throw new CommandException("commands.effect.failure.notActive.all",
							new Object[] { entitylivingbase.getName() });
				} else {
					entitylivingbase.clearActivePotions();
					notifyOperators(parICommandSender, this, "commands.effect.success.removed.all",
							new Object[] { entitylivingbase.getName() });
				}
			} else {
				int i;
				try {
					i = parseInt(parArrayOfString[1], 1);
				} catch (NumberInvalidException numberinvalidexception) {
					Potion potion = Potion.getPotionFromResourceLocation(parArrayOfString[1]);
					if (potion == null) {
						throw numberinvalidexception;
					}

					i = potion.id;
				}

				int j = 600;
				int l = 30;
				int k = 0;
				if (i >= 0 && i < Potion.potionTypes.length && Potion.potionTypes[i] != null) {
					Potion potion1 = Potion.potionTypes[i];
					if (parArrayOfString.length >= 3) {
						l = parseInt(parArrayOfString[2], 0, 1000000);
						if (potion1.isInstant()) {
							j = l;
						} else {
							j = l * 20;
						}
					} else if (potion1.isInstant()) {
						j = 1;
					}

					if (parArrayOfString.length >= 4) {
						k = parseInt(parArrayOfString[3], 0, 255);
					}

					boolean flag = true;
					if (parArrayOfString.length >= 5 && "true".equalsIgnoreCase(parArrayOfString[4])) {
						flag = false;
					}

					if (l > 0) {
						PotionEffect potioneffect = new PotionEffect(i, j, k, false, flag);
						entitylivingbase.addPotionEffect(potioneffect);
						notifyOperators(parICommandSender, this, "commands.effect.success",
								new Object[] {
										new ChatComponentTranslation(potioneffect.getEffectName(), new Object[0]),
										Integer.valueOf(i), Integer.valueOf(k), entitylivingbase.getName(),
										Integer.valueOf(l) });
					} else if (entitylivingbase.isPotionActive(i)) {
						entitylivingbase.removePotionEffect(i);
						notifyOperators(parICommandSender, this, "commands.effect.success.removed",
								new Object[] { new ChatComponentTranslation(potion1.getName(), new Object[0]),
										entitylivingbase.getName() });
					} else {
						throw new CommandException("commands.effect.failure.notActive",
								new Object[] { new ChatComponentTranslation(potion1.getName(), new Object[0]),
										entitylivingbase.getName() });
					}
				} else {
					throw new NumberInvalidException("commands.effect.notFound", new Object[] { Integer.valueOf(i) });
				}
			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, this.getAllUsernames())
				: (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, Potion.func_181168_c())
						: (astring.length == 5
								? getListOfStringsMatchingLastWord(astring, new String[] { "true", "false" })
								: null));
	}

	protected String[] getAllUsernames() {
		return MinecraftServer.getServer().getAllUsernames();
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] var1, int i) {
		return i == 0;
	}
}