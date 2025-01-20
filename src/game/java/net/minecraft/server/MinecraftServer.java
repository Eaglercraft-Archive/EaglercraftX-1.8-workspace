package net.minecraft.server;

import com.google.common.collect.Lists;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.futures.FutureTask;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ITickable;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Util;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public abstract class MinecraftServer implements Runnable, ICommandSender, IThreadListener {
	private static final Logger logger = LogManager.getLogger();
	private static MinecraftServer mcServer;
	/**+
	 * List of names of players who are online.
	 */
	protected final List<ITickable> playersOnline = Lists.newArrayList();
	protected final ICommandManager commandManager;
	private final EaglercraftRandom random = new EaglercraftRandom();
	/**+
	 * The server's port.
	 */
	private int serverPort = -1;
	public WorldServer[] worldServers;
	private ServerConfigurationManager serverConfigManager;
	/**+
	 * Indicates whether the server is running or not. Set to false
	 * to initiate a shutdown.
	 */
	protected boolean serverRunning = false;
	private boolean serverStopped;
	private int tickCounter;
	public String currentTask;
	public int percentDone;
	private boolean onlineMode;
	private boolean canSpawnAnimals;
	private boolean canSpawnNPCs;
	private boolean pvpEnabled;
	private boolean allowFlight;
	private String motd;
	private int buildLimit;
	private int maxPlayerIdleMinutes = 0;
	public final long[] tickTimeArray = new long[100];
	public long[][] timeOfLastDimensionTick;
	private String serverOwner;
	private String worldName;
	private boolean isDemo;
	private boolean enableBonusChest;
	private boolean worldIsBeingDeleted;
	/**+
	 * The texture pack for the server
	 */
	private String resourcePackUrl = "";
	private String resourcePackHash = "";
	private boolean serverIsRunning;
	protected long timeOfLastWarning;
	private String userMessage;
	private boolean startProfiling;
	private boolean isGamemodeForced;
	private long nanoTimeSinceStatusRefresh = 0L;
	protected final Queue<FutureTask<?>> futureTaskQueue = new LinkedList<>();
	private Thread serverThread;
	protected long currentTime = getCurrentTimeMillis();
	private boolean paused = false;
	private boolean isSpawnChunksLoaded = false;

	public MinecraftServer(String worldName) {
		mcServer = this;
		this.worldName = worldName;
		this.commandManager = new ServerCommandManager();
	}

	protected ServerCommandManager createNewCommandManager() {
		return new ServerCommandManager();
	}

	protected abstract boolean startServer() throws IOException;

	protected void convertMapIfNeeded(String worldNameIn) {

	}

	/**+
	 * Typically "menu.convertingLevel", "menu.loadingLevel" or
	 * others.
	 */
	protected synchronized void setUserMessage(String message) {
		this.userMessage = message;
	}

	public synchronized String getUserMessage() {
		return this.userMessage;
	}

	protected void loadAllWorlds(ISaveHandler isavehandler, String s1, WorldSettings worldsettings) {
		this.setUserMessage("menu.loadingLevel");
		this.worldServers = new WorldServer[3];
		this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
		WorldInfo worldinfo = isavehandler.loadWorldInfo();
		if (worldinfo == null) {
			if (this.isDemo() || worldsettings == null) {
				worldsettings = DemoWorldServer.demoWorldSettings;
			}
			worldinfo = new WorldInfo(worldsettings, s1);
		} else {
			worldinfo.setWorldName(s1);
			worldsettings = new WorldSettings(worldinfo);
		}

		if (worldinfo.isOldEaglercraftRandom()) {
			LogManager.getLogger("EaglerMinecraftServer")
					.info("Detected a pre-u34 world, using old EaglercraftRandom implementation for world generation");
		}

		for (int j = 0; j < this.worldServers.length; ++j) {
			byte b0 = 0;
			if (j == 1) {
				b0 = -1;
			}

			if (j == 2) {
				b0 = 1;
			}

			if (j == 0) {
				if (this.isDemo()) {
					this.worldServers[j] = (WorldServer) (new DemoWorldServer(this, isavehandler, worldinfo, b0))
							.init();
				} else {
					this.worldServers[j] = (WorldServer) (new WorldServer(this, isavehandler, worldinfo, b0)).init();
				}

				this.worldServers[j].initialize(worldsettings);
			} else {
				this.worldServers[j] = (WorldServer) (new WorldServerMulti(this, isavehandler, b0,
						this.worldServers[0])).init();
			}

			this.worldServers[j].addWorldAccess(new WorldManager(this, this.worldServers[j]));
		}

		this.serverConfigManager.setPlayerManager(this.worldServers);
		if (this.worldServers[0].getWorldInfo().getDifficulty() == null) {
			this.setDifficultyForAllWorlds(this.getDifficulty());
		}
		this.isSpawnChunksLoaded = this.worldServers[0].getWorldInfo().getGameRulesInstance()
				.getBoolean("loadSpawnChunks");
		if (this.isSpawnChunksLoaded) {
			this.initialWorldChunkLoad();
		}
	}

	protected void initialWorldChunkLoad() {
		boolean flag = true;
		boolean flag1 = true;
		boolean flag2 = true;
		boolean flag3 = true;
		int i = 0;
		this.setUserMessage("menu.generatingTerrain");
		byte b0 = 0;
		logger.info("Preparing start region for level " + b0);
		WorldServer worldserver = this.worldServers[b0];
		BlockPos blockpos = worldserver.getSpawnPoint();
		long j = getCurrentTimeMillis();

		for (int k = -192; k <= 192; k += 16) {
			for (int l = -192; l <= 192; l += 16) {
				long i1 = getCurrentTimeMillis();
				if (i1 - j > 1000L) {
					this.outputPercentRemaining("Preparing spawn area", i * 100 / 625);
					j = i1;
				}

				++i;
				worldserver.theChunkProviderServer.loadChunk(blockpos.getX() + k >> 4, blockpos.getZ() + l >> 4);
			}
		}

		this.clearCurrentTask();
	}

	protected void unloadSpawnChunks() {
		WorldServer worldserver = this.worldServers[0];
		BlockPos blockpos = worldserver.getSpawnPoint();
		int cnt = 0;

		for (int k = -192; k <= 192 && this.isServerRunning(); k += 16) {
			for (int l = -192; l <= 192 && this.isServerRunning(); l += 16) {
				Chunk chunk = worldserver.theChunkProviderServer.loadChunk(blockpos.getX() + k >> 4,
						blockpos.getZ() + l >> 4);
				if (chunk != null
						&& !worldserver.getPlayerManager().hasPlayerInstance(chunk.xPosition, chunk.zPosition)) {
					worldserver.theChunkProviderServer.dropChunk(chunk.xPosition, chunk.zPosition);
					++cnt;
				}
			}
		}

		logger.info("Dropped {} spawn chunks with no players in them", cnt);
	}

	public abstract boolean canStructuresSpawn();

	public abstract WorldSettings.GameType getGameType();

	public abstract EnumDifficulty getDifficulty();

	public abstract boolean isHardcore();

	public abstract int getOpPermissionLevel();

	public abstract boolean func_181034_q();

	public abstract boolean func_183002_r();

	/**+
	 * Used to display a percent remaining given text and the
	 * percentage.
	 */
	protected void outputPercentRemaining(String parString1, int parInt1) {
		this.currentTask = parString1;
		this.percentDone = parInt1;
		logger.info(parString1 + ": " + parInt1 + "%");
		EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.startingIntegratedServer", parInt1 * 0.01f);
	}

	/**+
	 * Set current task to null and set its percentage to 0.
	 */
	protected void clearCurrentTask() {
		this.currentTask = null;
		this.percentDone = 0;
	}

	/**+
	 * par1 indicates if a log message should be output.
	 */
	public void saveAllWorlds(boolean dontLog) {
		if (!this.worldIsBeingDeleted) {
			for (int i = 0; i < this.worldServers.length; ++i) {
				WorldServer worldserver = this.worldServers[i];
				if (worldserver != null) {
					if (!dontLog) {
						logger.info("Saving chunks for level \'" + worldserver.getWorldInfo().getWorldName() + "\'/"
								+ worldserver.provider.getDimensionName());
					}

					worldserver.saveAllChunks(true, (IProgressUpdate) null);
				}
			}

		}
	}

	/**+
	 * Saves all necessary data as preparation for stopping the
	 * server.
	 */
	public void stopServer() {
		if (!this.worldIsBeingDeleted) {
			logger.info("Stopping server");

			if (this.serverConfigManager != null) {
				logger.info("Saving players");
				this.serverConfigManager.saveAllPlayerData();
				this.serverConfigManager.removeAllPlayers();
			}

			if (this.worldServers != null) {
				logger.info("Saving worlds");
				this.saveAllWorlds(false);

				for (int i = 0; i < this.worldServers.length; ++i) {
					WorldServer worldserver = this.worldServers[i];
					worldserver.flush();
				}
			}
		} else {
			logger.info("Stopping server without saving");
			String str = getFolderName();
			logger.info("Deleting world \"{}\"...", str);
			EaglerIntegratedServerWorker.saveFormat.deleteWorldDirectory(str);
			logger.info("Deletion successful!");
		}
	}

	/**+
	 * WARNING : directly calls
	 * getActiveAnvilConverter().deleteWorldDirectory(theWorldServer[0].getSaveHandler().getWorldDirectoryName());
	 */
	public void deleteWorldAndStopServer() {
		this.worldIsBeingDeleted = true;
		this.initiateShutdown();
	}

	public boolean isServerRunning() {
		return this.serverRunning;
	}

	/**+
	 * Sets the serverRunning variable to false, in order to get the
	 * server to shut down.
	 */
	public void initiateShutdown() {
		this.serverRunning = false;
	}

	protected void setInstance() {
		mcServer = this;
	}

	public void run() {
		try {
			if (this.startServer()) {
				this.currentTime = getCurrentTimeMillis();
				long i = 0L;

				while (this.serverRunning) {
					long k = getCurrentTimeMillis();
					long j = k - this.currentTime;
					if (j > 2000L && this.currentTime - this.timeOfLastWarning >= 15000L) {
						logger.warn(
								"Can\'t keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)",
								new Object[] { Long.valueOf(j), Long.valueOf(j / 50L) });
						j = 2000L;
						this.timeOfLastWarning = this.currentTime;
					}

					if (j < 0L) {
						logger.warn("Time ran backwards! Did the system time change?");
						j = 0L;
					}

					i += j;
					this.currentTime = k;
					if (this.worldServers[0].areAllPlayersAsleep()) {
						this.tick();
						i = 0L;
					} else {
						while (i > 50L) {
							i -= 50L;
							this.tick();
						}
					}

					EagUtils.sleep(Math.max(1L, 50L - i));
					this.serverIsRunning = true;
				}
			} else {
				this.finalTick((CrashReport) null);
			}
		} catch (Throwable throwable1) {
			logger.error("Encountered an unexpected exception", throwable1);
			CrashReport crashreport = null;
			if (throwable1 instanceof ReportedException) {
				crashreport = this.addServerInfoToCrashReport(((ReportedException) throwable1).getCrashReport());
			} else {
				crashreport = this
						.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", throwable1));
			}

			this.finalTick(crashreport);
		} finally {
			try {
				this.serverStopped = true;
				this.stopServer();
			} catch (Throwable throwable) {
				logger.error("Exception stopping the server", throwable);
			} finally {
				this.systemExitNow();
			}

		}

	}

	/**+
	 * Called on exit from the main run() loop.
	 */
	protected void finalTick(CrashReport var1) {
	}

	/**+
	 * Directly calls System.exit(0), instantly killing the program.
	 */
	protected void systemExitNow() {
	}

	/**+
	 * Main function called by run() every loop.
	 */
	public void tick() {
		long i = EagRuntime.nanoTime();
		++this.tickCounter;
		if (this.startProfiling) {
			this.startProfiling = false;
		}

		this.updateTimeLightAndEntities();

		boolean loadSpawnChunks = this.worldServers[0].getWorldInfo().getGameRulesInstance()
				.getBoolean("loadSpawnChunks");
		if (this.isSpawnChunksLoaded != loadSpawnChunks) {
			if (loadSpawnChunks) {
				this.initialWorldChunkLoad();
			} else {
				this.unloadSpawnChunks();
			}
			this.isSpawnChunksLoaded = loadSpawnChunks;
		}

		if (this.tickCounter % 900 == 0) {
			this.serverConfigManager.saveAllPlayerData();
			this.saveAllWorlds(true);
		}

		this.tickTimeArray[this.tickCounter % 100] = EagRuntime.nanoTime() - i;
	}

	public void updateTimeLightAndEntities() {
		synchronized (this.futureTaskQueue) {
			while (!this.futureTaskQueue.isEmpty()) {
				Util.func_181617_a((FutureTask) this.futureTaskQueue.poll(), logger);
			}
		}

		for (int j = 0; j < this.worldServers.length; ++j) {
			long i = EagRuntime.nanoTime();
			if (j == 0 || this.getAllowNether()) {
				WorldServer worldserver = this.worldServers[j];
				if (this.tickCounter % 20 == 0) {
					this.serverConfigManager.sendPacketToAllPlayersInDimension(
							new S03PacketTimeUpdate(worldserver.getTotalWorldTime(), worldserver.getWorldTime(),
									worldserver.getGameRules().getBoolean("doDaylightCycle")),
							worldserver.provider.getDimensionId());
				}

				try {
					worldserver.tick();
				} catch (Throwable throwable1) {
					CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Exception ticking world");
					worldserver.addWorldInfoToCrashReport(crashreport);
					throw new ReportedException(crashreport);
				}

				try {
					worldserver.updateEntities();
				} catch (Throwable throwable) {
					CrashReport crashreport1 = CrashReport.makeCrashReport(throwable,
							"Exception ticking world entities");
					worldserver.addWorldInfoToCrashReport(crashreport1);
					throw new ReportedException(crashreport1);
				}

				worldserver.getEntityTracker().updateTrackedEntities();
			}

			this.timeOfLastDimensionTick[j][this.tickCounter % 100] = EagRuntime.nanoTime() - i;
		}

		EaglerIntegratedServerWorker.tick();
		this.serverConfigManager.onTick();

		for (int k = 0; k < this.playersOnline.size(); ++k) {
			((ITickable) this.playersOnline.get(k)).update();
		}
	}

	public boolean getAllowNether() {
		return true;
	}

	public void startServerThread() {
		this.serverThread = new Thread(this, "Server thread");
		this.serverThread.start();
	}

	/**+
	 * Logs the message with a level of WARN.
	 */
	public void logWarning(String s) {
		logger.warn(s);
	}

	/**+
	 * Gets the worldServer by the given dimension.
	 */
	public WorldServer worldServerForDimension(int dimension) {
		return dimension == -1 ? this.worldServers[1] : (dimension == 1 ? this.worldServers[2] : this.worldServers[0]);
	}

	/**+
	 * Returns the server's Minecraft version as string.
	 */
	public String getMinecraftVersion() {
		return "1.8.8";
	}

	/**+
	 * Returns the number of players currently on the server.
	 */
	public int getCurrentPlayerCount() {
		return this.serverConfigManager.getCurrentPlayerCount();
	}

	/**+
	 * Returns the maximum number of players allowed on the server.
	 */
	public int getMaxPlayers() {
		return this.serverConfigManager.getMaxPlayers();
	}

	/**+
	 * Returns an array of the usernames of all the connected
	 * players.
	 */
	public String[] getAllUsernames() {
		return this.serverConfigManager.getAllUsernames();
	}

	/**+
	 * Returns an array of the GameProfiles of all the connected
	 * players
	 */
	public GameProfile[] getGameProfiles() {
		return this.serverConfigManager.getAllProfiles();
	}

	public String getServerModName() {
		return "eagler";
	}

	/**+
	 * Adds the server info, including from theWorldServer, to the
	 * crash report.
	 */
	public CrashReport addServerInfoToCrashReport(CrashReport crashreport) {
		crashreport.getCategory().addCrashSectionCallable("Profiler Position", new Callable<String>() {
			public String call() throws Exception {
				return "N/A (disabled)";
			}
		});
		if (this.serverConfigManager != null) {
			crashreport.getCategory().addCrashSectionCallable("Player Count", new Callable<String>() {
				public String call() {
					return MinecraftServer.this.serverConfigManager.getCurrentPlayerCount() + " / "
							+ MinecraftServer.this.serverConfigManager.getMaxPlayers() + "; "
							+ MinecraftServer.this.serverConfigManager.func_181057_v();
				}
			});
		}

		return crashreport;
	}

	public List<String> getTabCompletions(ICommandSender sender, String input, BlockPos pos) {
		ArrayList arraylist = Lists.newArrayList();
		if (input.startsWith("/")) {
			input = input.substring(1);
			boolean flag = !input.contains(" ");
			List<String> list = this.commandManager.getTabCompletionOptions(sender, input, pos);
			if (list != null) {
				for (int i = 0, l = list.size(); i < l; ++i) {
					String s2 = list.get(i);
					if (flag) {
						arraylist.add("/" + s2);
					} else {
						arraylist.add(s2);
					}
				}
			}

			return arraylist;
		} else {
			String[] astring = input.split(" ", -1);
			String s = astring[astring.length - 1];
			String[] unames = this.serverConfigManager.getAllUsernames();
			for (int i = 0; i < unames.length; ++i) {
				String s1 = unames[i];
				if (CommandBase.doesStringStartWith(s, s1)) {
					arraylist.add(s1);
				}
			}

			return arraylist;
		}
	}

	/**+
	 * Gets mcServer.
	 */
	public static MinecraftServer getServer() {
		return mcServer;
	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return "Server";
	}

	/**+
	 * Send a chat message to the CommandSender
	 */
	public void addChatMessage(IChatComponent ichatcomponent) {
		logger.info(ichatcomponent.getUnformattedText());
	}

	/**+
	 * Returns {@code true} if the CommandSender is allowed to
	 * execute the command, {@code false} if not
	 */
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return true;
	}

	public ICommandManager getCommandManager() {
		return this.commandManager;
	}

	/**+
	 * Returns the username of the server owner (for integrated
	 * servers)
	 */
	public String getServerOwner() {
		return this.serverOwner;
	}

	/**+
	 * Sets the username of the owner of this server (in the case of
	 * an integrated server)
	 */
	public void setServerOwner(String owner) {
		this.serverOwner = owner;
	}

	public boolean isSinglePlayer() {
		return this.serverOwner != null;
	}

	public String getFolderName() {
		return this.worldName;
	}

	public String getWorldName() {
		return this.worldName;
	}

	public void setDifficultyForAllWorlds(EnumDifficulty enumdifficulty) {
		for (int i = 0; i < this.worldServers.length; ++i) {
			WorldServer worldserver = this.worldServers[i];
			if (worldserver != null) {
				if (worldserver.getWorldInfo().isHardcoreModeEnabled()) {
					worldserver.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
					worldserver.setAllowedSpawnTypes(true, true);
				} else if (this.isSinglePlayer()) {
					worldserver.getWorldInfo().setDifficulty(enumdifficulty);
					worldserver.setAllowedSpawnTypes(worldserver.getDifficulty() != EnumDifficulty.PEACEFUL, true);
				} else {
					worldserver.getWorldInfo().setDifficulty(enumdifficulty);
					worldserver.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
				}
			}
		}
		this.getConfigurationManager().sendPacketToAllPlayers(new S41PacketServerDifficulty(
				this.worldServers[0].getDifficulty(), this.worldServers[0].getWorldInfo().isDifficultyLocked()));
	}

	public void setDifficultyLockedForAllWorlds(boolean locked) {
		for (int i = 0; i < this.worldServers.length; ++i) {
			WorldServer worldserver = this.worldServers[i];
			if (worldserver != null) {
				worldserver.getWorldInfo().setDifficultyLocked(locked);
			}
		}

	}

	protected boolean allowSpawnMonsters() {
		return true;
	}

	/**+
	 * Gets whether this is a demo or not.
	 */
	public boolean isDemo() {
		return this.isDemo;
	}

	/**+
	 * Sets whether this is a demo or not.
	 */
	public void setDemo(boolean demo) {
		this.isDemo = demo;
	}

	public void canCreateBonusChest(boolean enable) {
		this.enableBonusChest = enable;
	}

	public String getResourcePackUrl() {
		return this.resourcePackUrl;
	}

	public String getResourcePackHash() {
		return this.resourcePackHash;
	}

	public void setResourcePack(String parString1, String parString2) {
		this.resourcePackUrl = parString1;
		this.resourcePackHash = parString2;
	}

	/**+
	 * Returns whether snooping is enabled or not.
	 */
	public boolean isSnooperEnabled() {
		return true;
	}

	public abstract boolean isDedicatedServer();

	public boolean isServerInOnlineMode() {
		return this.onlineMode;
	}

	public void setOnlineMode(boolean online) {
		this.onlineMode = online;
	}

	public boolean getCanSpawnAnimals() {
		return this.canSpawnAnimals;
	}

	public void setCanSpawnAnimals(boolean spawnAnimals) {
		this.canSpawnAnimals = spawnAnimals;
	}

	public boolean getCanSpawnNPCs() {
		return this.canSpawnNPCs;
	}

	public abstract boolean func_181035_ah();

	public void setCanSpawnNPCs(boolean spawnNpcs) {
		this.canSpawnNPCs = spawnNpcs;
	}

	public boolean isPVPEnabled() {
		return this.pvpEnabled;
	}

	public void setAllowPvp(boolean allowPvp) {
		this.pvpEnabled = allowPvp;
	}

	public boolean isFlightAllowed() {
		return this.allowFlight;
	}

	public void setAllowFlight(boolean allow) {
		this.allowFlight = allow;
	}

	public abstract boolean isCommandBlockEnabled();

	public String getMOTD() {
		return this.motd;
	}

	public void setMOTD(String motdIn) {
		this.motd = motdIn;
	}

	public int getBuildLimit() {
		return this.buildLimit;
	}

	public void setBuildLimit(int maxBuildHeight) {
		this.buildLimit = maxBuildHeight;
	}

	public boolean isServerStopped() {
		return this.serverStopped;
	}

	public ServerConfigurationManager getConfigurationManager() {
		return this.serverConfigManager;
	}

	public void setConfigManager(ServerConfigurationManager configManager) {
		this.serverConfigManager = configManager;
	}

	/**+
	 * Sets the game type for all worlds.
	 */
	public void setGameType(WorldSettings.GameType worldsettings$gametype) {
		for (int i = 0; i < this.worldServers.length; ++i) {
			getServer().worldServers[i].getWorldInfo().setGameType(worldsettings$gametype);
		}

	}

	public boolean serverIsInRunLoop() {
		return this.serverIsRunning;
	}

	public boolean getGuiEnabled() {
		return false;
	}

	public abstract String shareToLAN(WorldSettings.GameType var1, boolean var2);

	public int getTickCounter() {
		return this.tickCounter;
	}

	public void enableProfiling() {
		this.startProfiling = true;
	}

	/**+
	 * Get the position in the world. <b>{@code null} is not
	 * allowed!</b> If you are not an entity in the world, return
	 * the coordinates 0, 0, 0
	 */
	public BlockPos getPosition() {
		return BlockPos.ORIGIN;
	}

	/**+
	 * Get the position vector. <b>{@code null} is not allowed!</b>
	 * If you are not an entity in the world, return 0.0D, 0.0D,
	 * 0.0D
	 */
	public Vec3 getPositionVector() {
		return new Vec3(0.0D, 0.0D, 0.0D);
	}

	/**+
	 * Get the world, if available. <b>{@code null} is not
	 * allowed!</b> If you are not an entity in the world, return
	 * the overworld
	 */
	public World getEntityWorld() {
		return this.worldServers[0];
	}

	/**+
	 * Returns the entity associated with the command sender. MAY BE
	 * NULL!
	 */
	public Entity getCommandSenderEntity() {
		return null;
	}

	/**+
	 * Return the spawn protection area's size.
	 */
	public int getSpawnProtectionSize() {
		return 16;
	}

	public boolean isBlockProtected(World var1, BlockPos var2, EntityPlayer var3) {
		return false;
	}

	public boolean getForceGamemode() {
		return this.isGamemodeForced;
	}

	public static long getCurrentTimeMillis() {
		return EagRuntime.steadyTimeMillis();
	}

	public int getMaxPlayerIdleMinutes() {
		return this.maxPlayerIdleMinutes;
	}

	public void setPlayerIdleTimeout(int i) {
		this.maxPlayerIdleMinutes = i;
	}

	/**+
	 * Get the formatted ChatComponent that will be used for the
	 * sender's username in chat
	 */
	public IChatComponent getDisplayName() {
		return new ChatComponentText(this.getName());
	}

	public boolean isAnnouncingPlayerAchievements() {
		return true;
	}

	public void refreshStatusNextTick() {
		this.nanoTimeSinceStatusRefresh = 0L;
	}

	public Entity getEntityFromUuid(EaglercraftUUID uuid) {
		for (int i = 0; i < this.worldServers.length; ++i) {
			WorldServer worldserver = this.worldServers[i];
			if (worldserver != null) {
				Entity entity = worldserver.getEntityFromUuid(uuid);
				if (entity != null) {
					return entity;
				}
			}
		}

		return null;
	}

	/**+
	 * Returns true if the command sender should be sent feedback
	 * about executed commands
	 */
	public boolean sendCommandFeedback() {
		/**+
		 * Gets mcServer.
		 */
		return getServer().worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
	}

	public void setCommandStat(CommandResultStats.Type var1, int var2) {
	}

	public int getMaxWorldSize() {
		return 29999984;
	}

	public boolean isCallingFromMinecraftThread() {
		return true;
	}

	/**+
	 * The compression treshold. If the packet is larger than the
	 * specified amount of bytes, it will be compressed
	 */
	public int getNetworkCompressionTreshold() {
		return 256;
	}

	public void setPaused(boolean pause) {
		this.paused = pause;
	}

	public boolean getPaused() {
		return paused;
	}
}