package net.minecraft.command.server;

import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;
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
public abstract class CommandBlockLogic implements ICommandSender {
	/**+
	 * The formatting for the timestamp on commands run.
	 */
	private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");
	private int successCount;
	private boolean trackOutput = true;
	/**+
	 * The previously run command.
	 */
	private IChatComponent lastOutput = null;
	/**+
	 * The command stored in the command block.
	 */
	private String commandStored = "";
	/**+
	 * The custom name of the command block. (defaults to "@")
	 */
	private String customName = "@";
	private final CommandResultStats resultStats = new CommandResultStats();

	/**+
	 * returns the successCount int.
	 */
	public int getSuccessCount() {
		return this.successCount;
	}

	/**+
	 * Returns the lastOutput.
	 */
	public IChatComponent getLastOutput() {
		return this.lastOutput;
	}

	/**+
	 * Stores data to NBT format.
	 */
	public void writeDataToNBT(NBTTagCompound tagCompound) {
		tagCompound.setString("Command", this.commandStored);
		tagCompound.setInteger("SuccessCount", this.successCount);
		tagCompound.setString("CustomName", this.customName);
		tagCompound.setBoolean("TrackOutput", this.trackOutput);
		if (this.lastOutput != null && this.trackOutput) {
			tagCompound.setString("LastOutput", IChatComponent.Serializer.componentToJson(this.lastOutput));
		}

		this.resultStats.writeStatsToNBT(tagCompound);
	}

	/**+
	 * Reads NBT formatting and stored data into variables.
	 */
	public void readDataFromNBT(NBTTagCompound nbt) {
		this.commandStored = nbt.getString("Command");
		this.successCount = nbt.getInteger("SuccessCount");
		if (nbt.hasKey("CustomName", 8)) {
			this.customName = nbt.getString("CustomName");
		}

		if (nbt.hasKey("TrackOutput", 1)) {
			this.trackOutput = nbt.getBoolean("TrackOutput");
		}

		if (nbt.hasKey("LastOutput", 8) && this.trackOutput) {
			this.lastOutput = IChatComponent.Serializer.jsonToComponent(nbt.getString("LastOutput"));
		}

		this.resultStats.readStatsFromNBT(nbt);
	}

	/**+
	 * Returns {@code true} if the CommandSender is allowed to
	 * execute the command, {@code false} if not
	 */
	public boolean canCommandSenderUseCommand(int i, String var2) {
		return i <= 2;
	}

	/**+
	 * Sets the command.
	 */
	public void setCommand(String command) {
		this.commandStored = command;
		this.successCount = 0;
	}

	/**+
	 * Returns the command of the command block.
	 */
	public String getCommand() {
		return this.commandStored;
	}

	public void trigger(World worldIn) {
		if (worldIn.isRemote) {
			this.successCount = 0;
		}

		MinecraftServer minecraftserver = MinecraftServer.getServer();
		if (minecraftserver != null && minecraftserver.isCommandBlockEnabled()) {
			ICommandManager icommandmanager = minecraftserver.getCommandManager();

			try {
				this.lastOutput = null;
				this.successCount = icommandmanager.executeCommand(this, this.commandStored);
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Executing command block");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Command to be executed");
				crashreportcategory.addCrashSectionCallable("Command", new Callable<String>() {
					public String call() throws Exception {
						return CommandBlockLogic.this.getCommand();
					}
				});
				crashreportcategory.addCrashSectionCallable("Name", new Callable<String>() {
					public String call() throws Exception {
						return CommandBlockLogic.this.getName();
					}
				});
				throw new ReportedException(crashreport);
			}
		} else {
			this.successCount = 0;
		}

	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return this.customName;
	}

	/**+
	 * Get the formatted ChatComponent that will be used for the
	 * sender's username in chat
	 */
	public IChatComponent getDisplayName() {
		return new ChatComponentText(this.getName());
	}

	public void setName(String parString1) {
		this.customName = parString1;
	}

	/**+
	 * Send a chat message to the CommandSender
	 */
	public void addChatMessage(IChatComponent ichatcomponent) {
		if (this.trackOutput && this.getEntityWorld() != null && !this.getEntityWorld().isRemote) {
			this.lastOutput = (new ChatComponentText("[" + timestampFormat.format(new Date()) + "] "))
					.appendSibling(ichatcomponent);
			this.updateCommand();
		}

	}

	/**+
	 * Returns true if the command sender should be sent feedback
	 * about executed commands
	 */
	public boolean sendCommandFeedback() {
		MinecraftServer minecraftserver = MinecraftServer.getServer();
		return minecraftserver == null
				|| minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
	}

	public void setCommandStat(CommandResultStats.Type commandresultstats$type, int i) {
		this.resultStats.func_179672_a(this, commandresultstats$type, i);
	}

	public abstract void updateCommand();

	public abstract int func_145751_f();

	public abstract void func_145757_a(ByteBuf var1);

	public void setLastOutput(IChatComponent lastOutputMessage) {
		this.lastOutput = lastOutputMessage;
	}

	public void setTrackOutput(boolean shouldTrackOutput) {
		this.trackOutput = shouldTrackOutput;
	}

	public boolean shouldTrackOutput() {
		return this.trackOutput;
	}

	public boolean tryOpenEditCommandBlock(EntityPlayer playerIn) {
		if (!playerIn.capabilities.isCreativeMode) {
			return false;
		} else {
			if (playerIn.getEntityWorld().isRemote) {
				playerIn.openEditCommandBlock(this);
			}

			return true;
		}
	}

	public CommandResultStats getCommandResultStats() {
		return this.resultStats;
	}
}