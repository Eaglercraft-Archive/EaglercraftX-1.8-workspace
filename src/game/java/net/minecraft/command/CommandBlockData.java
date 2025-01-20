package net.minecraft.command;

import java.util.List;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
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
public class CommandBlockData extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "blockdata";
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
		return "commands.blockdata.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 4) {
			throw new WrongUsageException("commands.blockdata.usage", new Object[0]);
		} else {
			parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
			BlockPos blockpos = parseBlockPos(parICommandSender, parArrayOfString, 0, false);
			World world = parICommandSender.getEntityWorld();
			if (!world.isBlockLoaded(blockpos)) {
				throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
			} else {
				TileEntity tileentity = world.getTileEntity(blockpos);
				if (tileentity == null) {
					throw new CommandException("commands.blockdata.notValid", new Object[0]);
				} else {
					NBTTagCompound nbttagcompound = new NBTTagCompound();
					tileentity.writeToNBT(nbttagcompound);
					NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttagcompound.copy();

					NBTTagCompound nbttagcompound2;
					try {
						nbttagcompound2 = JsonToNBT
								.getTagFromJson(getChatComponentFromNthArg(parICommandSender, parArrayOfString, 3)
										.getUnformattedText());
					} catch (NBTException nbtexception) {
						throw new CommandException("commands.blockdata.tagError",
								new Object[] { nbtexception.getMessage() });
					}

					nbttagcompound.merge(nbttagcompound2);
					nbttagcompound.setInteger("x", blockpos.getX());
					nbttagcompound.setInteger("y", blockpos.getY());
					nbttagcompound.setInteger("z", blockpos.getZ());
					if (nbttagcompound.equals(nbttagcompound1)) {
						throw new CommandException("commands.blockdata.failed",
								new Object[] { nbttagcompound.toString() });
					} else {
						tileentity.readFromNBT(nbttagcompound);
						tileentity.markDirty();
						world.markBlockForUpdate(blockpos);
						parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
						notifyOperators(parICommandSender, this, "commands.blockdata.success",
								new Object[] { nbttagcompound.toString() });
					}
				}
			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos blockpos) {
		return astring.length > 0 && astring.length <= 3 ? func_175771_a(astring, 0, blockpos) : null;
	}
}