package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
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
public class CommandFill extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "fill";
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
		return "commands.fill.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 7) {
			throw new WrongUsageException("commands.fill.usage", new Object[0]);
		} else {
			parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
			BlockPos blockpos = parseBlockPos(parICommandSender, parArrayOfString, 0, false);
			BlockPos blockpos1 = parseBlockPos(parICommandSender, parArrayOfString, 3, false);
			Block block = CommandBase.getBlockByText(parICommandSender, parArrayOfString[6]);
			int i = 0;
			if (parArrayOfString.length >= 8) {
				i = parseInt(parArrayOfString[7], 0, 15);
			}

			BlockPos blockpos2 = new BlockPos(Math.min(blockpos.getX(), blockpos1.getX()),
					Math.min(blockpos.getY(), blockpos1.getY()), Math.min(blockpos.getZ(), blockpos1.getZ()));
			BlockPos blockpos3 = new BlockPos(Math.max(blockpos.getX(), blockpos1.getX()),
					Math.max(blockpos.getY(), blockpos1.getY()), Math.max(blockpos.getZ(), blockpos1.getZ()));
			int j = (blockpos3.getX() - blockpos2.getX() + 1) * (blockpos3.getY() - blockpos2.getY() + 1)
					* (blockpos3.getZ() - blockpos2.getZ() + 1);
			if (j > '\u8000') {
				throw new CommandException("commands.fill.tooManyBlocks",
						new Object[] { Integer.valueOf(j), Integer.valueOf('\u8000') });
			} else if (blockpos2.getY() >= 0 && blockpos3.getY() < 256) {
				World world = parICommandSender.getEntityWorld();

				for (int k = blockpos2.getZ(); k < blockpos3.getZ() + 16; k += 16) {
					for (int l = blockpos2.getX(); l < blockpos3.getX() + 16; l += 16) {
						if (!world.isBlockLoaded(new BlockPos(l, blockpos3.getY() - blockpos2.getY(), k))) {
							throw new CommandException("commands.fill.outOfWorld", new Object[0]);
						}
					}
				}

				NBTTagCompound nbttagcompound = new NBTTagCompound();
				boolean flag = false;
				if (parArrayOfString.length >= 10 && block.hasTileEntity()) {
					String s = getChatComponentFromNthArg(parICommandSender, parArrayOfString, 9).getUnformattedText();

					try {
						nbttagcompound = JsonToNBT.getTagFromJson(s);
						flag = true;
					} catch (NBTException nbtexception) {
						throw new CommandException("commands.fill.tagError",
								new Object[] { nbtexception.getMessage() });
					}
				}

				ArrayList arraylist = Lists.newArrayList();
				j = 0;

				for (int i1 = blockpos2.getZ(); i1 <= blockpos3.getZ(); ++i1) {
					for (int j1 = blockpos2.getY(); j1 <= blockpos3.getY(); ++j1) {
						for (int k1 = blockpos2.getX(); k1 <= blockpos3.getX(); ++k1) {
							BlockPos blockpos4 = new BlockPos(k1, j1, i1);
							if (parArrayOfString.length >= 9) {
								if (!parArrayOfString[8].equals("outline") && !parArrayOfString[8].equals("hollow")) {
									if (parArrayOfString[8].equals("destroy")) {
										world.destroyBlock(blockpos4, true);
									} else if (parArrayOfString[8].equals("keep")) {
										if (!world.isAirBlock(blockpos4)) {
											continue;
										}
									} else if (parArrayOfString[8].equals("replace") && !block.hasTileEntity()) {
										if (parArrayOfString.length > 9) {
											Block block1 = CommandBase.getBlockByText(parICommandSender,
													parArrayOfString[9]);
											if (world.getBlockState(blockpos4).getBlock() != block1) {
												continue;
											}
										}

										if (parArrayOfString.length > 10) {
											int l1 = CommandBase.parseInt(parArrayOfString[10]);
											IBlockState iblockstate = world.getBlockState(blockpos4);
											if (iblockstate.getBlock().getMetaFromState(iblockstate) != l1) {
												continue;
											}
										}
									}
								} else if (k1 != blockpos2.getX() && k1 != blockpos3.getX() && j1 != blockpos2.getY()
										&& j1 != blockpos3.getY() && i1 != blockpos2.getZ() && i1 != blockpos3.getZ()) {
									if (parArrayOfString[8].equals("hollow")) {
										world.setBlockState(blockpos4, Blocks.air.getDefaultState(), 2);
										arraylist.add(blockpos4);
									}
									continue;
								}
							}

							TileEntity tileentity1 = world.getTileEntity(blockpos4);
							if (tileentity1 != null) {
								if (tileentity1 instanceof IInventory) {
									((IInventory) tileentity1).clear();
								}

								world.setBlockState(blockpos4, Blocks.barrier.getDefaultState(),
										block == Blocks.barrier ? 2 : 4);
							}

							IBlockState iblockstate1 = block.getStateFromMeta(i);
							if (world.setBlockState(blockpos4, iblockstate1, 2)) {
								arraylist.add(blockpos4);
								++j;
								if (flag) {
									TileEntity tileentity = world.getTileEntity(blockpos4);
									if (tileentity != null) {
										nbttagcompound.setInteger("x", blockpos4.getX());
										nbttagcompound.setInteger("y", blockpos4.getY());
										nbttagcompound.setInteger("z", blockpos4.getZ());
										tileentity.readFromNBT(nbttagcompound);
									}
								}
							}
						}
					}
				}

				for (int m = 0, n = arraylist.size(); m < n; ++m) {
					BlockPos blockpos5 = (BlockPos) arraylist.get(m);
					Block block2 = world.getBlockState(blockpos5).getBlock();
					world.notifyNeighborsRespectDebug(blockpos5, block2);
				}

				if (j <= 0) {
					throw new CommandException("commands.fill.failed", new Object[0]);
				} else {
					parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, j);
					notifyOperators(parICommandSender, this, "commands.fill.success",
							new Object[] { Integer.valueOf(j) });
				}
			} else {
				throw new CommandException("commands.fill.outOfWorld", new Object[0]);
			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos blockpos) {
		return astring.length > 0
				&& astring.length <= 3
						? func_175771_a(astring, 0, blockpos)
						: (astring.length > 3 && astring.length <= 6 ? func_175771_a(astring, 3, blockpos)
								: (astring.length == 7
										? getListOfStringsMatchingLastWord(astring, Block.blockRegistry.getKeys())
										: (astring.length == 9
												? getListOfStringsMatchingLastWord(astring,
														new String[] { "replace", "destroy", "keep", "hollow",
																"outline" })
												: (astring.length == 10 && "replace".equals(astring[8])
														? getListOfStringsMatchingLastWord(astring,
																Block.blockRegistry.getKeys())
														: null))));
	}
}