package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

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
public class CommandEntityData extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "entitydata";
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
		return "commands.entitydata.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 2) {
			throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
		} else {
			Entity entity = func_175768_b(parICommandSender, parArrayOfString[0]);
			if (entity instanceof EntityPlayer) {
				throw new CommandException("commands.entitydata.noPlayers", new Object[] { entity.getDisplayName() });
			} else {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				entity.writeToNBT(nbttagcompound);
				NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttagcompound.copy();

				NBTTagCompound nbttagcompound2;
				try {
					nbttagcompound2 = JsonToNBT.getTagFromJson(
							getChatComponentFromNthArg(parICommandSender, parArrayOfString, 1).getUnformattedText());
				} catch (NBTException nbtexception) {
					throw new CommandException("commands.entitydata.tagError",
							new Object[] { nbtexception.getMessage() });
				}

				nbttagcompound2.removeTag("UUIDMost");
				nbttagcompound2.removeTag("UUIDLeast");
				nbttagcompound.merge(nbttagcompound2);
				if (nbttagcompound.equals(nbttagcompound1)) {
					throw new CommandException("commands.entitydata.failed",
							new Object[] { nbttagcompound.toString() });
				} else {
					entity.readFromNBT(nbttagcompound);
					notifyOperators(parICommandSender, this, "commands.entitydata.success",
							new Object[] { nbttagcompound.toString() });
				}
			}
		}
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] var1, int i) {
		return i == 0;
	}
}