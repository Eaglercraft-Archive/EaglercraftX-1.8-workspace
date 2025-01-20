package net.minecraft.command.server;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
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
public class CommandTestForBlock extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "testforblock";
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
		return "commands.testforblock.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 4) {
			throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
		} else {
			parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
			BlockPos blockpos = parseBlockPos(parICommandSender, parArrayOfString, 0, false);
			Block block = Block.getBlockFromName(parArrayOfString[3]);
			if (block == null) {
				throw new NumberInvalidException("commands.setblock.notFound", new Object[] { parArrayOfString[3] });
			} else {
				int i = -1;
				if (parArrayOfString.length >= 5) {
					i = parseInt(parArrayOfString[4], -1, 15);
				}

				World world = parICommandSender.getEntityWorld();
				if (!world.isBlockLoaded(blockpos)) {
					throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
				} else {
					NBTTagCompound nbttagcompound = new NBTTagCompound();
					boolean flag = false;
					if (parArrayOfString.length >= 6 && block.hasTileEntity()) {
						String s = getChatComponentFromNthArg(parICommandSender, parArrayOfString, 5)
								.getUnformattedText();

						try {
							nbttagcompound = JsonToNBT.getTagFromJson(s);
							flag = true;
						} catch (NBTException nbtexception) {
							throw new CommandException("commands.setblock.tagError",
									new Object[] { nbtexception.getMessage() });
						}
					}

					IBlockState iblockstate = world.getBlockState(blockpos);
					Block block1 = iblockstate.getBlock();
					if (block1 != block) {
						throw new CommandException("commands.testforblock.failed.tile",
								new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()),
										Integer.valueOf(blockpos.getZ()), block1.getLocalizedName(),
										block.getLocalizedName() });
					} else {
						if (i > -1) {
							int j = iblockstate.getBlock().getMetaFromState(iblockstate);
							if (j != i) {
								throw new CommandException("commands.testforblock.failed.data",
										new Object[] { Integer.valueOf(blockpos.getX()),
												Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()),
												Integer.valueOf(j), Integer.valueOf(i) });
							}
						}

						if (flag) {
							TileEntity tileentity = world.getTileEntity(blockpos);
							if (tileentity == null) {
								throw new CommandException("commands.testforblock.failed.tileEntity",
										new Object[] { Integer.valueOf(blockpos.getX()),
												Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
							}

							NBTTagCompound nbttagcompound1 = new NBTTagCompound();
							tileentity.writeToNBT(nbttagcompound1);
							if (!NBTUtil.func_181123_a(nbttagcompound, nbttagcompound1, true)) {
								throw new CommandException("commands.testforblock.failed.nbt",
										new Object[] { Integer.valueOf(blockpos.getX()),
												Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
							}
						}

						parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
						notifyOperators(parICommandSender, this, "commands.testforblock.success",
								new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()),
										Integer.valueOf(blockpos.getZ()) });
					}
				}
			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos blockpos) {
		return astring.length > 0 && astring.length <= 3 ? func_175771_a(astring, 0, blockpos)
				: (astring.length == 4 ? getListOfStringsMatchingLastWord(astring, Block.blockRegistry.getKeys())
						: null);
	}
}