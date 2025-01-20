package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

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
public class CommandClone extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "clone";
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
		return "commands.clone.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 9) {
			throw new WrongUsageException("commands.clone.usage", new Object[0]);
		} else {
			parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
			BlockPos blockpos = parseBlockPos(parICommandSender, parArrayOfString, 0, false);
			BlockPos blockpos1 = parseBlockPos(parICommandSender, parArrayOfString, 3, false);
			BlockPos blockpos2 = parseBlockPos(parICommandSender, parArrayOfString, 6, false);
			StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos1);
			StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockpos2,
					blockpos2.add(structureboundingbox.func_175896_b()));
			int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
			if (i > '\u8000') {
				throw new CommandException("commands.clone.tooManyBlocks",
						new Object[] { Integer.valueOf(i), Integer.valueOf('\u8000') });
			} else {
				boolean flag = false;
				Block block = null;
				int j = -1;
				if ((parArrayOfString.length < 11
						|| !parArrayOfString[10].equals("force") && !parArrayOfString[10].equals("move"))
						&& structureboundingbox.intersectsWith(structureboundingbox1)) {
					throw new CommandException("commands.clone.noOverlap", new Object[0]);
				} else {
					if (parArrayOfString.length >= 11 && parArrayOfString[10].equals("move")) {
						flag = true;
					}

					if (structureboundingbox.minY >= 0 && structureboundingbox.maxY < 256
							&& structureboundingbox1.minY >= 0 && structureboundingbox1.maxY < 256) {
						World world = parICommandSender.getEntityWorld();
						if (world.isAreaLoaded(structureboundingbox) && world.isAreaLoaded(structureboundingbox1)) {
							boolean flag1 = false;
							if (parArrayOfString.length >= 10) {
								if (parArrayOfString[9].equals("masked")) {
									flag1 = true;
								} else if (parArrayOfString[9].equals("filtered")) {
									if (parArrayOfString.length < 12) {
										throw new WrongUsageException("commands.clone.usage", new Object[0]);
									}

									block = getBlockByText(parICommandSender, parArrayOfString[11]);
									if (parArrayOfString.length >= 13) {
										j = parseInt(parArrayOfString[12], 0, 15);
									}
								}
							}

							ArrayList arraylist = Lists.newArrayList();
							ArrayList arraylist1 = Lists.newArrayList();
							ArrayList arraylist2 = Lists.newArrayList();
							LinkedList linkedlist = Lists.newLinkedList();
							BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX,
									structureboundingbox1.minY - structureboundingbox.minY,
									structureboundingbox1.minZ - structureboundingbox.minZ);

							for (int k = structureboundingbox.minZ; k <= structureboundingbox.maxZ; ++k) {
								for (int l = structureboundingbox.minY; l <= structureboundingbox.maxY; ++l) {
									for (int i1 = structureboundingbox.minX; i1 <= structureboundingbox.maxX; ++i1) {
										BlockPos blockpos4 = new BlockPos(i1, l, k);
										BlockPos blockpos5 = blockpos4.add(blockpos3);
										IBlockState iblockstate = world.getBlockState(blockpos4);
										if ((!flag1 || iblockstate.getBlock() != Blocks.air) && (block == null
												|| iblockstate.getBlock() == block && (j < 0 || iblockstate.getBlock()
														.getMetaFromState(iblockstate) == j))) {
											TileEntity tileentity = world.getTileEntity(blockpos4);
											if (tileentity != null) {
												NBTTagCompound nbttagcompound = new NBTTagCompound();
												tileentity.writeToNBT(nbttagcompound);
												arraylist1.add(new CommandClone.StaticCloneData(blockpos5, iblockstate,
														nbttagcompound));
												linkedlist.addLast(blockpos4);
											} else if (!iblockstate.getBlock().isFullBlock()
													&& !iblockstate.getBlock().isFullCube()) {
												arraylist2.add(new CommandClone.StaticCloneData(blockpos5, iblockstate,
														(NBTTagCompound) null));
												linkedlist.addFirst(blockpos4);
											} else {
												arraylist.add(new CommandClone.StaticCloneData(blockpos5, iblockstate,
														(NBTTagCompound) null));
												linkedlist.addLast(blockpos4);
											}
										}
									}
								}
							}

							if (flag) {
								for (BlockPos blockpos6 : (LinkedList<BlockPos>) linkedlist) {
									TileEntity tileentity1 = world.getTileEntity(blockpos6);
									if (tileentity1 instanceof IInventory) {
										((IInventory) tileentity1).clear();
									}

									world.setBlockState(blockpos6, Blocks.barrier.getDefaultState(), 2);
								}

								for (BlockPos blockpos7 : (LinkedList<BlockPos>) linkedlist) {
									world.setBlockState(blockpos7, Blocks.air.getDefaultState(), 3);
								}
							}

							ArrayList arraylist3 = Lists.newArrayList();
							arraylist3.addAll(arraylist);
							arraylist3.addAll(arraylist1);
							arraylist3.addAll(arraylist2);
							List list = Lists.reverse(arraylist3);

							for (int k = 0, l = list.size(); k < l; ++k) {
								CommandClone.StaticCloneData commandclone$staticclonedata = (CommandClone.StaticCloneData) list
										.get(k);
								TileEntity tileentity2 = world
										.getTileEntity(commandclone$staticclonedata.field_179537_a);
								if (tileentity2 instanceof IInventory) {
									((IInventory) tileentity2).clear();
								}

								world.setBlockState(commandclone$staticclonedata.field_179537_a,
										Blocks.barrier.getDefaultState(), 2);
							}

							i = 0;

							for (int k = 0, l = arraylist3.size(); k < l; ++k) {
								CommandClone.StaticCloneData commandclone$staticclonedata1 = (CommandClone.StaticCloneData) arraylist3
										.get(k);
								if (world.setBlockState(commandclone$staticclonedata1.field_179537_a,
										commandclone$staticclonedata1.blockState, 2)) {
									++i;
								}
							}

							for (int k = 0, l = arraylist1.size(); k < l; ++k) {
								CommandClone.StaticCloneData commandclone$staticclonedata2 = (CommandClone.StaticCloneData) arraylist1
										.get(k);
								TileEntity tileentity3 = world
										.getTileEntity(commandclone$staticclonedata2.field_179537_a);
								if (commandclone$staticclonedata2.field_179536_c != null && tileentity3 != null) {
									commandclone$staticclonedata2.field_179536_c.setInteger("x",
											commandclone$staticclonedata2.field_179537_a.getX());
									commandclone$staticclonedata2.field_179536_c.setInteger("y",
											commandclone$staticclonedata2.field_179537_a.getY());
									commandclone$staticclonedata2.field_179536_c.setInteger("z",
											commandclone$staticclonedata2.field_179537_a.getZ());
									tileentity3.readFromNBT(commandclone$staticclonedata2.field_179536_c);
									tileentity3.markDirty();
								}

								world.setBlockState(commandclone$staticclonedata2.field_179537_a,
										commandclone$staticclonedata2.blockState, 2);
							}

							for (int k = 0, l = list.size(); k < l; ++k) {
								CommandClone.StaticCloneData commandclone$staticclonedata3 = (CommandClone.StaticCloneData) list
										.get(k);
								world.notifyNeighborsRespectDebug(commandclone$staticclonedata3.field_179537_a,
										commandclone$staticclonedata3.blockState.getBlock());
							}

							List list1 = world.func_175712_a(structureboundingbox, false);
							if (list1 != null) {
								for (int k = 0, l = list1.size(); k < l; ++k) {
									NextTickListEntry nextticklistentry = (NextTickListEntry) list1.get(k);
									if (structureboundingbox.isVecInside(nextticklistentry.position)) {
										BlockPos blockpos8 = nextticklistentry.position.add(blockpos3);
										world.scheduleBlockUpdate(blockpos8, nextticklistentry.getBlock(),
												(int) (nextticklistentry.scheduledTime
														- world.getWorldInfo().getWorldTotalTime()),
												nextticklistentry.priority);
									}
								}
							}

							if (i <= 0) {
								throw new CommandException("commands.clone.failed", new Object[0]);
							} else {
								parICommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
								notifyOperators(parICommandSender, this, "commands.clone.success",
										new Object[] { Integer.valueOf(i) });
							}
						} else {
							throw new CommandException("commands.clone.outOfWorld", new Object[0]);
						}
					} else {
						throw new CommandException("commands.clone.outOfWorld", new Object[0]);
					}
				}
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
						: (astring.length > 3
								&& astring.length <= 6
										? func_175771_a(astring, 3, blockpos)
										: (astring.length > 6 && astring.length <= 9
												? func_175771_a(astring, 6, blockpos)
												: (astring.length == 10
														? getListOfStringsMatchingLastWord(astring,
																new String[] { "replace", "masked", "filtered" })
														: (astring.length == 11
																? getListOfStringsMatchingLastWord(astring,
																		new String[] { "normal", "force", "move" })
																: (astring.length == 12 && "filtered".equals(astring[9])
																		? getListOfStringsMatchingLastWord(astring,
																				Block.blockRegistry.getKeys())
																		: null)))));
	}

	static class StaticCloneData {
		public final BlockPos field_179537_a;
		public final IBlockState blockState;
		public final NBTTagCompound field_179536_c;

		public StaticCloneData(BlockPos parBlockPos, IBlockState parIBlockState, NBTTagCompound parNBTTagCompound) {
			this.field_179537_a = parBlockPos;
			this.blockState = parIBlockState;
			this.field_179536_c = parNBTTagCompound;
		}
	}
}