package net.minecraft.server.management;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.demo.DemoWorldManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.WorldInfo;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketUpdateCertEAG;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;
import net.lax1dude.eaglercraft.v1_8.sp.server.WorldsDB;
import net.lax1dude.eaglercraft.v1_8.sp.server.skins.PlayerTextureData;
import net.lax1dude.eaglercraft.v1_8.sp.server.socket.IntegratedServerPlayerNetworkManager;
import net.lax1dude.eaglercraft.v1_8.sp.server.voice.IntegratedVoiceService;
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
public abstract class ServerConfigurationManager {
	private static final Logger logger = LogManager.getLogger();
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd \'at\' HH:mm:ss z");
	private final MinecraftServer mcServer;
	/**+
	 * A list of player entities that exist on this server.
	 */
	private final List<EntityPlayerMP> playerEntityList = Lists.newArrayList();
	private final Map<EaglercraftUUID, EntityPlayerMP> uuidToPlayerMap = Maps.newHashMap();
	private final Map<String, StatisticsFile> playerStatFiles;
	private IPlayerFileData playerNBTManagerObj;
	private boolean whiteListEnforced;
	protected int maxPlayers;
	protected int viewDistance;
	private WorldSettings.GameType gameType;
	private boolean commandsAllowedForAll;
	private int playerPingIndex;

	private WorldSettings.GameType lanGamemode = WorldSettings.GameType.SURVIVAL;
	private boolean lanCheats = false;

	public ServerConfigurationManager(MinecraftServer server) {
		this.playerStatFiles = Maps.newHashMap();
		this.mcServer = server;
		this.maxPlayers = 100;
	}

	public void initializeConnectionToPlayer(IntegratedServerPlayerNetworkManager netManager, EntityPlayerMP playerIn,
			GamePluginMessageProtocol protocolVersion, PlayerTextureData textureData, EaglercraftUUID clientBrandUUID) {
		playerIn.textureData = textureData;
		playerIn.clientBrandUUID = clientBrandUUID;
		NBTTagCompound nbttagcompound = this.readPlayerDataFromFile(playerIn);
		playerIn.setWorld(this.mcServer.worldServerForDimension(playerIn.dimension));
		playerIn.theItemInWorldManager.setWorld((WorldServer) playerIn.worldObj);
		String s1 = "channel:" + netManager.playerChannel;

		logger.info(playerIn.getName() + "[" + s1 + "] logged in with entity id " + playerIn.getEntityId() + " at ("
				+ playerIn.posX + ", " + playerIn.posY + ", " + playerIn.posZ + ")");
		WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
		WorldInfo worldinfo = worldserver.getWorldInfo();
		BlockPos blockpos = worldserver.getSpawnPoint();
		this.setPlayerGameTypeBasedOnOther(playerIn, (EntityPlayerMP) null, worldserver);
		NetHandlerPlayServer nethandlerplayserver = new NetHandlerPlayServer(this.mcServer, netManager, playerIn,
				protocolVersion);
		nethandlerplayserver.sendPacket(new S01PacketJoinGame(playerIn.getEntityId(),
				playerIn.theItemInWorldManager.getGameType(), worldinfo.isHardcoreModeEnabled(),
				worldserver.provider.getDimensionId(), worldserver.getDifficulty(), this.getMaxPlayers(),
				worldinfo.getTerrainType(), worldserver.getGameRules().getBoolean("reducedDebugInfo")));
		nethandlerplayserver
				.sendPacket(new S3FPacketCustomPayload("MC|Brand", (PacketBuffer) (new PacketBuffer(Unpooled.buffer()))
						.writeString(this.getServerInstance().getServerModName())));
		nethandlerplayserver
				.sendPacket(new S41PacketServerDifficulty(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
		nethandlerplayserver.sendPacket(new S05PacketSpawnPosition(blockpos));
		nethandlerplayserver.sendPacket(new S39PacketPlayerAbilities(playerIn.capabilities));
		nethandlerplayserver.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
		playerIn.getStatFile().func_150877_d();
		playerIn.getStatFile().sendAchievements(playerIn);
		this.sendScoreboard((ServerScoreboard) worldserver.getScoreboard(), playerIn);
		this.mcServer.refreshStatusNextTick();
		ChatComponentTranslation chatcomponenttranslation;
		chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.joined",
				new Object[] { playerIn.getDisplayName() });
		chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
		this.sendChatMsg(chatcomponenttranslation);
		if (playerIn.canCommandSenderUseCommand(2, "give")) {
			ChatComponentText shaderF4Msg = new ChatComponentText("[EaglercraftX] ");
			shaderF4Msg.getChatStyle().setColor(EnumChatFormatting.GOLD);
			ChatComponentTranslation shaderF4Msg2 = new ChatComponentTranslation("command.skull.tip");
			shaderF4Msg2.getChatStyle().setColor(EnumChatFormatting.AQUA);
			shaderF4Msg.appendSibling(shaderF4Msg2);
			playerIn.addChatMessage(shaderF4Msg);
		}
		this.playerLoggedIn(playerIn);
		nethandlerplayserver.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw,
				playerIn.rotationPitch);
		this.updateTimeAndWeatherForPlayer(playerIn, worldserver);
		if (this.mcServer.getResourcePackUrl().length() > 0) {
			playerIn.loadResourcePack(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
		}

		for (ObjectCursor<PotionEffect> potioneffect : playerIn.getActivePotionEffects()) {
			nethandlerplayserver.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), potioneffect.value));
		}

		playerIn.addSelfToInternalCraftingInventory();
		if (nbttagcompound != null && nbttagcompound.hasKey("Riding", 10)) {
			Entity entity = EntityList.createEntityFromNBT(nbttagcompound.getCompoundTag("Riding"), worldserver);
			if (entity != null) {
				entity.forceSpawn = true;
				worldserver.spawnEntityInWorld(entity);
				playerIn.mountEntity(entity);
				entity.forceSpawn = false;
			}
		}

		if (EagRuntime.getConfiguration().allowUpdateSvc()) {
			for (int i = 0, l = playerEntityList.size(); i < l; ++i) {
				EntityPlayerMP playerItr = playerEntityList.get(i);
				if (playerItr != playerIn && playerItr.updateCertificate != null) {
					nethandlerplayserver.sendEaglerMessage(new SPacketUpdateCertEAG(playerItr.updateCertificate));
				}
			}
		}
	}

	protected void sendScoreboard(ServerScoreboard scoreboardIn, EntityPlayerMP playerIn) {
		HashSet hashset = Sets.newHashSet();

		for (ScorePlayerTeam scoreplayerteam : scoreboardIn.getTeams()) {
			playerIn.playerNetServerHandler.sendPacket(new S3EPacketTeams(scoreplayerteam, 0));
		}

		for (int i = 0; i < 19; ++i) {
			ScoreObjective scoreobjective = scoreboardIn.getObjectiveInDisplaySlot(i);
			if (scoreobjective != null && !hashset.contains(scoreobjective)) {
				List<Packet> lst = scoreboardIn.func_96550_d(scoreobjective);
				for (int j = 0, l = lst.size(); j < l; ++j) {
					playerIn.playerNetServerHandler.sendPacket(lst.get(j));
				}

				hashset.add(scoreobjective);
			}
		}

	}

	/**+
	 * Sets the NBT manager to the one for the WorldServer given.
	 */
	public void setPlayerManager(WorldServer[] worldServers) {
		this.playerNBTManagerObj = worldServers[0].getSaveHandler().getPlayerNBTManager();
		worldServers[0].getWorldBorder().addListener(new IBorderListener() {
			public void onSizeChanged(WorldBorder worldborder, double var2) {
				ServerConfigurationManager.this.sendPacketToAllPlayers(
						new S44PacketWorldBorder(worldborder, S44PacketWorldBorder.Action.SET_SIZE));
			}

			public void onTransitionStarted(WorldBorder worldborder, double var2, double var4, long var6) {
				ServerConfigurationManager.this.sendPacketToAllPlayers(
						new S44PacketWorldBorder(worldborder, S44PacketWorldBorder.Action.LERP_SIZE));
			}

			public void onCenterChanged(WorldBorder worldborder, double var2, double var4) {
				ServerConfigurationManager.this.sendPacketToAllPlayers(
						new S44PacketWorldBorder(worldborder, S44PacketWorldBorder.Action.SET_CENTER));
			}

			public void onWarningTimeChanged(WorldBorder worldborder, int var2) {
				ServerConfigurationManager.this.sendPacketToAllPlayers(
						new S44PacketWorldBorder(worldborder, S44PacketWorldBorder.Action.SET_WARNING_TIME));
			}

			public void onWarningDistanceChanged(WorldBorder worldborder, int var2) {
				ServerConfigurationManager.this.sendPacketToAllPlayers(
						new S44PacketWorldBorder(worldborder, S44PacketWorldBorder.Action.SET_WARNING_BLOCKS));
			}

			public void onDamageAmountChanged(WorldBorder var1, double var2) {
			}

			public void onDamageBufferChanged(WorldBorder var1, double var2) {
			}
		});
	}

	public void preparePlayer(EntityPlayerMP playerIn, WorldServer worldIn) {
		WorldServer worldserver = playerIn.getServerForPlayer();
		if (worldIn != null) {
			worldIn.getPlayerManager().removePlayer(playerIn);
		}

		worldserver.getPlayerManager().addPlayer(playerIn);
		worldserver.theChunkProviderServer.loadChunk((int) playerIn.posX >> 4, (int) playerIn.posZ >> 4);
	}

	public int getEntityViewDistance() {
		return PlayerManager.getFurthestViewableBlock(this.getViewDistance());
	}

	/**+
	 * called during player login. reads the player information from
	 * disk.
	 */
	public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP playerIn) {
		NBTTagCompound nbttagcompound = this.mcServer.worldServers[0].getWorldInfo().getPlayerNBTTagCompound();
		NBTTagCompound nbttagcompound1;
		if (playerIn.getName().equals(this.mcServer.getServerOwner()) && nbttagcompound != null) {
			playerIn.readFromNBT(nbttagcompound);
			nbttagcompound1 = nbttagcompound;
			logger.debug("loading single player");
		} else {
			nbttagcompound1 = this.playerNBTManagerObj.readPlayerData(playerIn);
		}

		return nbttagcompound1;
	}

	/**+
	 * also stores the NBTTags if this is an intergratedPlayerList
	 */
	protected void writePlayerData(EntityPlayerMP entityplayermp) {
		this.playerNBTManagerObj.writePlayerData(entityplayermp);
		StatisticsFile statisticsfile = (StatisticsFile) this.playerStatFiles.get(entityplayermp.getName());
		if (statisticsfile != null) {
			statisticsfile.saveStatFile();
		}

	}

	/**+
	 * Called when a player successfully logs in. Reads player data
	 * from disk and inserts the player into the world.
	 */
	public void playerLoggedIn(EntityPlayerMP playerIn) {
		this.playerEntityList.add(playerIn);
		this.uuidToPlayerMap.put(playerIn.getUniqueID(), playerIn);
		this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER,
				new EntityPlayerMP[] { playerIn }));
		WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
		worldserver.spawnEntityInWorld(playerIn);
		this.preparePlayer(playerIn, (WorldServer) null);

		for (int i = 0; i < this.playerEntityList.size(); ++i) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) this.playerEntityList.get(i);
			playerIn.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(
					S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { entityplayermp }));
		}

	}

	/**+
	 * using player's dimension, update their movement when in a
	 * vehicle (e.g. cart, boat)
	 */
	public void serverUpdateMountedMovingPlayer(EntityPlayerMP playerIn) {
		playerIn.getServerForPlayer().getPlayerManager().updateMountedMovingPlayer(playerIn);
	}

	/**+
	 * Called when a player disconnects from the game. Writes player
	 * data to disk and removes them from the world.
	 */
	public void playerLoggedOut(EntityPlayerMP playerIn) {
		playerIn.triggerAchievement(StatList.leaveGameStat);
		this.writePlayerData(playerIn);
		WorldServer worldserver = playerIn.getServerForPlayer();
		if (playerIn.ridingEntity != null) {
			worldserver.removePlayerEntityDangerously(playerIn.ridingEntity);
			logger.debug("removing player mount");
		}

		worldserver.removeEntity(playerIn);
		worldserver.getPlayerManager().removePlayer(playerIn);
		this.playerEntityList.remove(playerIn);
		EaglercraftUUID uuid = playerIn.getUniqueID();
		EntityPlayerMP entityplayermp = (EntityPlayerMP) this.uuidToPlayerMap.get(uuid);
		if (entityplayermp == playerIn) {
			this.uuidToPlayerMap.remove(uuid);
			this.playerStatFiles.remove(entityplayermp.getName());
		}

		IntegratedVoiceService vcs = ((EaglerMinecraftServer) mcServer).getVoiceService();
		if (vcs != null) {
			vcs.handlePlayerLoggedOut(playerIn);
		}

		this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.REMOVE_PLAYER,
				new EntityPlayerMP[] { playerIn }));
	}

	/**+
	 * checks ban-lists, then white-lists, then space for the
	 * server. Returns null on success, or an error message
	 */
	public String allowUserToConnect(GameProfile gameprofile) {
		return doesPlayerAlreadyExist(gameprofile)
				? "\"" + gameprofile.getName() + "\" is already playing on this world!"
				: null;
	}

	private boolean doesPlayerAlreadyExist(GameProfile gameprofile) {
		for (int i = 0, l = playerEntityList.size(); i < l; ++i) {
			EntityPlayerMP player = playerEntityList.get(i);
			if (player.getName().equalsIgnoreCase(gameprofile.getName())
					|| player.getUniqueID().equals(gameprofile.getId())) {
				return true;
			}
		}
		return false;
	}

	/**+
	 * also checks for multiple logins across servers
	 */
	public EntityPlayerMP createPlayerForUser(GameProfile profile) {
		EaglercraftUUID uuid = EntityPlayer.getUUID(profile);
		ArrayList<EntityPlayerMP> arraylist = Lists.newArrayList();

		for (int i = 0, l = this.playerEntityList.size(); i < l; ++i) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) this.playerEntityList.get(i);
			if (entityplayermp.getUniqueID().equals(uuid)
					|| entityplayermp.getName().equalsIgnoreCase(profile.getName())) {
				arraylist.add(entityplayermp);
			}
		}

		EntityPlayerMP entityplayermp2 = (EntityPlayerMP) this.uuidToPlayerMap.get(profile.getId());
		if (entityplayermp2 != null && !arraylist.contains(entityplayermp2)) {
			arraylist.add(entityplayermp2);
		}

		for (int i = 0, l = arraylist.size(); i < l; ++i) {
			arraylist.get(i).playerNetServerHandler.kickPlayerFromServer("You logged in from another location");
		}

		Object object;
		if (this.mcServer.isDemo()) {
			object = new DemoWorldManager(this.mcServer.worldServerForDimension(0));
		} else {
			object = new ItemInWorldManager(this.mcServer.worldServerForDimension(0));
		}

		return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), profile,
				(ItemInWorldManager) object);
	}

	/**+
	 * Called on respawn
	 */
	public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP playerIn, int dimension, boolean conqueredEnd) {
		playerIn.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(playerIn);
		playerIn.getServerForPlayer().getEntityTracker().untrackEntity(playerIn);
		playerIn.getServerForPlayer().getPlayerManager().removePlayer(playerIn);
		this.playerEntityList.remove(playerIn);
		this.mcServer.worldServerForDimension(playerIn.dimension).removePlayerEntityDangerously(playerIn);
		BlockPos blockpos = playerIn.getBedLocation();
		boolean flag = playerIn.isSpawnForced();
		playerIn.dimension = dimension;
		Object object;
		if (this.mcServer.isDemo()) {
			object = new DemoWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension));
		} else {
			object = new ItemInWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension));
		}

		EntityPlayerMP entityplayermp = new EntityPlayerMP(this.mcServer,
				this.mcServer.worldServerForDimension(playerIn.dimension), playerIn.getGameProfile(),
				(ItemInWorldManager) object);
		entityplayermp.updateCertificate = playerIn.updateCertificate;
		entityplayermp.clientBrandUUID = playerIn.clientBrandUUID;
		entityplayermp.playerNetServerHandler = playerIn.playerNetServerHandler;
		entityplayermp.clonePlayer(playerIn, conqueredEnd);
		entityplayermp.setEntityId(playerIn.getEntityId());
		entityplayermp.func_174817_o(playerIn);
		WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
		this.setPlayerGameTypeBasedOnOther(entityplayermp, playerIn, worldserver);
		if (blockpos != null) {
			BlockPos blockpos1 = EntityPlayer
					.getBedSpawnLocation(this.mcServer.worldServerForDimension(playerIn.dimension), blockpos, flag);
			if (blockpos1 != null) {
				entityplayermp.setLocationAndAngles((double) ((float) blockpos1.getX() + 0.5F),
						(double) ((float) blockpos1.getY() + 0.1F), (double) ((float) blockpos1.getZ() + 0.5F), 0.0F,
						0.0F);
				entityplayermp.setSpawnPoint(blockpos, flag);
			} else {
				entityplayermp.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(0, 0.0F));
			}
		}

		worldserver.theChunkProviderServer.loadChunk((int) entityplayermp.posX >> 4, (int) entityplayermp.posZ >> 4);

		while (!worldserver.getCollidingBoundingBoxes(entityplayermp, entityplayermp.getEntityBoundingBox()).isEmpty()
				&& entityplayermp.posY < 256.0D) {
			entityplayermp.setPosition(entityplayermp.posX, entityplayermp.posY + 1.0D, entityplayermp.posZ);
		}

		entityplayermp.playerNetServerHandler.sendPacket(new S07PacketRespawn(entityplayermp.dimension,
				entityplayermp.worldObj.getDifficulty(), entityplayermp.worldObj.getWorldInfo().getTerrainType(),
				entityplayermp.theItemInWorldManager.getGameType()));
		BlockPos blockpos2 = worldserver.getSpawnPoint();
		entityplayermp.playerNetServerHandler.setPlayerLocation(entityplayermp.posX, entityplayermp.posY,
				entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
		entityplayermp.playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(blockpos2));
		entityplayermp.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(entityplayermp.experience,
				entityplayermp.experienceTotal, entityplayermp.experienceLevel));
		this.updateTimeAndWeatherForPlayer(entityplayermp, worldserver);
		worldserver.getPlayerManager().addPlayer(entityplayermp);
		worldserver.spawnEntityInWorld(entityplayermp);
		this.playerEntityList.add(entityplayermp);
		this.uuidToPlayerMap.put(entityplayermp.getUniqueID(), entityplayermp);
		entityplayermp.addSelfToInternalCraftingInventory();
		entityplayermp.setHealth(entityplayermp.getHealth());
		return entityplayermp;
	}

	/**+
	 * moves provided player from overworld to nether or vice versa
	 */
	public void transferPlayerToDimension(EntityPlayerMP playerIn, int dimension) {
		int i = playerIn.dimension;
		WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
		playerIn.dimension = dimension;
		WorldServer worldserver1 = this.mcServer.worldServerForDimension(playerIn.dimension);
		playerIn.playerNetServerHandler.sendPacket(new S07PacketRespawn(playerIn.dimension,
				playerIn.worldObj.getDifficulty(), playerIn.worldObj.getWorldInfo().getTerrainType(),
				playerIn.theItemInWorldManager.getGameType()));
		worldserver.removePlayerEntityDangerously(playerIn);
		playerIn.isDead = false;
		this.transferEntityToWorld(playerIn, i, worldserver, worldserver1);
		this.preparePlayer(playerIn, worldserver);
		playerIn.playerNetServerHandler.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ,
				playerIn.rotationYaw, playerIn.rotationPitch);
		playerIn.theItemInWorldManager.setWorld(worldserver1);
		this.updateTimeAndWeatherForPlayer(playerIn, worldserver1);
		this.syncPlayerInventory(playerIn);

		for (ObjectCursor<PotionEffect> potioneffect : playerIn.getActivePotionEffects()) {
			playerIn.playerNetServerHandler
					.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), potioneffect.value));
		}

	}

	/**+
	 * Transfers an entity from a world to another world.
	 */
	public void transferEntityToWorld(Entity entityIn, int parInt1, WorldServer parWorldServer,
			WorldServer parWorldServer2) {
		double d0 = entityIn.posX;
		double d1 = entityIn.posZ;
		double d2 = 8.0D;
		float f = entityIn.rotationYaw;
		if (entityIn.dimension == -1) {
			d0 = MathHelper.clamp_double(d0 / d2, parWorldServer2.getWorldBorder().minX() + 16.0D,
					parWorldServer2.getWorldBorder().maxX() - 16.0D);
			d1 = MathHelper.clamp_double(d1 / d2, parWorldServer2.getWorldBorder().minZ() + 16.0D,
					parWorldServer2.getWorldBorder().maxZ() - 16.0D);
			entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
			if (entityIn.isEntityAlive()) {
				parWorldServer.updateEntityWithOptionalForce(entityIn, false);
			}
		} else if (entityIn.dimension == 0) {
			d0 = MathHelper.clamp_double(d0 * d2, parWorldServer2.getWorldBorder().minX() + 16.0D,
					parWorldServer2.getWorldBorder().maxX() - 16.0D);
			d1 = MathHelper.clamp_double(d1 * d2, parWorldServer2.getWorldBorder().minZ() + 16.0D,
					parWorldServer2.getWorldBorder().maxZ() - 16.0D);
			entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
			if (entityIn.isEntityAlive()) {
				parWorldServer.updateEntityWithOptionalForce(entityIn, false);
			}
		} else {
			BlockPos blockpos;
			if (parInt1 == 1) {
				blockpos = parWorldServer2.getSpawnPoint();
			} else {
				blockpos = parWorldServer2.getSpawnCoordinate();
			}

			d0 = (double) blockpos.getX();
			entityIn.posY = (double) blockpos.getY();
			d1 = (double) blockpos.getZ();
			entityIn.setLocationAndAngles(d0, entityIn.posY, d1, 90.0F, 0.0F);
			if (entityIn.isEntityAlive()) {
				parWorldServer.updateEntityWithOptionalForce(entityIn, false);
			}
		}

		if (parInt1 != 1) {
			d0 = (double) MathHelper.clamp_int((int) d0, -29999872, 29999872);
			d1 = (double) MathHelper.clamp_int((int) d1, -29999872, 29999872);
			if (entityIn.isEntityAlive()) {
				entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
				parWorldServer2.getDefaultTeleporter().placeInPortal(entityIn, f);
				parWorldServer2.spawnEntityInWorld(entityIn);
				parWorldServer2.updateEntityWithOptionalForce(entityIn, false);
			}
		}

		entityIn.setWorld(parWorldServer2);
	}

	/**+
	 * self explanitory
	 */
	public void onTick() {
		if (++this.playerPingIndex > 600) {
			this.sendPacketToAllPlayers(
					new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_LATENCY, this.playerEntityList));
			this.playerPingIndex = 0;
		}

	}

	public void sendPacketToAllPlayers(Packet packetIn) {
		for (int i = 0; i < this.playerEntityList.size(); ++i) {
			((EntityPlayerMP) this.playerEntityList.get(i)).playerNetServerHandler.sendPacket(packetIn);
		}

	}

	public void sendPacketToAllPlayersInDimension(Packet packetIn, int dimension) {
		for (int i = 0; i < this.playerEntityList.size(); ++i) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) this.playerEntityList.get(i);
			if (entityplayermp.dimension == dimension) {
				entityplayermp.playerNetServerHandler.sendPacket(packetIn);
			}
		}

	}

	public void sendMessageToAllTeamMembers(EntityPlayer player, IChatComponent message) {
		Team team = player.getTeam();
		if (team != null) {
			for (String s : team.getMembershipCollection()) {
				EntityPlayerMP entityplayermp = this.getPlayerByUsername(s);
				if (entityplayermp != null && entityplayermp != player) {
					entityplayermp.addChatMessage(message);
				}
			}

		}
	}

	public void sendMessageToTeamOrEvryPlayer(EntityPlayer player, IChatComponent message) {
		Team team = player.getTeam();
		if (team == null) {
			this.sendChatMsg(message);
		} else {
			for (int i = 0, l = this.playerEntityList.size(); i < l; ++i) {
				EntityPlayerMP entityplayermp = (EntityPlayerMP) this.playerEntityList.get(i);
				if (entityplayermp.getTeam() != team) {
					entityplayermp.addChatMessage(message);
				}
			}

		}
	}

	public String func_181058_b(boolean parFlag) {
		String s = "";
		ArrayList arraylist = Lists.newArrayList(this.playerEntityList);

		for (int i = 0; i < arraylist.size(); ++i) {
			if (i > 0) {
				s = s + ", ";
			}

			s = s + ((EntityPlayerMP) arraylist.get(i)).getName();
			if (parFlag) {
				s = s + " (" + ((EntityPlayerMP) arraylist.get(i)).getUniqueID().toString() + ")";
			}
		}

		return s;
	}

	/**+
	 * Returns an array of the usernames of all the connected
	 * players.
	 */
	public String[] getAllUsernames() {
		String[] astring = new String[this.playerEntityList.size()];

		for (int i = 0; i < astring.length; ++i) {
			astring[i] = ((EntityPlayerMP) this.playerEntityList.get(i)).getName();
		}

		return astring;
	}

	public GameProfile[] getAllProfiles() {
		GameProfile[] agameprofile = new GameProfile[this.playerEntityList.size()];

		for (int i = 0; i < agameprofile.length; ++i) {
			agameprofile[i] = ((EntityPlayerMP) this.playerEntityList.get(i)).getGameProfile();
		}

		return agameprofile;
	}

	public boolean canJoin(GameProfile gameprofile) {
		return true;
	}

	public boolean canSendCommands(GameProfile profile) {
		return lanCheats
				|| this.mcServer.isSinglePlayer() && this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed()
						&& this.mcServer.getServerOwner().equalsIgnoreCase(profile.getName())
				|| this.commandsAllowedForAll;
	}

	public EntityPlayerMP getPlayerByUsername(String username) {
		for (EntityPlayerMP entityplayermp : this.playerEntityList) {
			if (entityplayermp.getName().equalsIgnoreCase(username)) {
				return entityplayermp;
			}
		}

		return null;
	}

	/**+
	 * params: x,y,z,r,dimension. The packet is sent to all players
	 * within r radius of x,y,z (r^2>x^2+y^2+z^2)
	 */
	public void sendToAllNear(double x, double y, double z, double radius, int dimension, Packet packetIn) {
		this.sendToAllNearExcept((EntityPlayer) null, x, y, z, radius, dimension, packetIn);
	}

	/**+
	 * params: srcPlayer,x,y,z,r,dimension. The packet is not sent
	 * to the srcPlayer, but all other players within the search
	 * radius
	 */
	public void sendToAllNearExcept(EntityPlayer x, double y, double z, double radius, double dimension, int parInt1,
			Packet parPacket) {
		for (int i = 0, l = this.playerEntityList.size(); i < l; ++i) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) this.playerEntityList.get(i);
			if (entityplayermp != x && entityplayermp.dimension == parInt1) {
				double d0 = y - entityplayermp.posX;
				double d1 = z - entityplayermp.posY;
				double d2 = radius - entityplayermp.posZ;
				if (d0 * d0 + d1 * d1 + d2 * d2 < dimension * dimension) {
					entityplayermp.playerNetServerHandler.sendPacket(parPacket);
				}
			}
		}

	}

	/**+
	 * Saves all of the players' current states.
	 */
	public void saveAllPlayerData() {
		for (int i = 0, l = this.playerEntityList.size(); i < l; ++i) {
			this.writePlayerData((EntityPlayerMP) this.playerEntityList.get(i));
		}

	}

	/**+
	 * Updates the time and weather for the given player to those of
	 * the given world
	 */
	public void updateTimeAndWeatherForPlayer(EntityPlayerMP playerIn, WorldServer worldIn) {
		WorldBorder worldborder = this.mcServer.worldServers[0].getWorldBorder();
		playerIn.playerNetServerHandler
				.sendPacket(new S44PacketWorldBorder(worldborder, S44PacketWorldBorder.Action.INITIALIZE));
		playerIn.playerNetServerHandler.sendPacket(new S03PacketTimeUpdate(worldIn.getTotalWorldTime(),
				worldIn.getWorldTime(), worldIn.getGameRules().getBoolean("doDaylightCycle")));
		if (worldIn.isRaining()) {
			playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0.0F));
			playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(7, worldIn.getRainStrength(1.0F)));
			playerIn.playerNetServerHandler
					.sendPacket(new S2BPacketChangeGameState(8, worldIn.getThunderStrength(1.0F)));
		}

	}

	/**+
	 * sends the players inventory to himself
	 */
	public void syncPlayerInventory(EntityPlayerMP playerIn) {
		playerIn.sendContainerToPlayer(playerIn.inventoryContainer);
		playerIn.setPlayerHealthUpdated();
		playerIn.playerNetServerHandler.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
	}

	/**+
	 * Returns the number of players currently on the server.
	 */
	public int getCurrentPlayerCount() {
		return this.playerEntityList.size();
	}

	/**+
	 * Returns the maximum number of players allowed on the server.
	 */
	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	/**+
	 * Returns an array of usernames for which player.dat exists
	 * for.
	 */
	public String[] getAvailablePlayerDat() {
		return this.mcServer.worldServers[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
	}

	public void setWhiteListEnabled(boolean flag) {
		this.whiteListEnforced = flag;
	}

	public List<EntityPlayerMP> getPlayersMatchingAddress(String address) {
		ArrayList arraylist = Lists.newArrayList();

		for (int i = 0, l = playerEntityList.size(); i < l; ++i) {
			EntityPlayerMP entityplayermp = playerEntityList.get(i);
			if (entityplayermp.getPlayerIP().equals(address)) {
				arraylist.add(entityplayermp);
			}
		}

		return arraylist;
	}

	/**+
	 * Gets the View Distance.
	 */
	public int getViewDistance() {
		return this.viewDistance;
	}

	public MinecraftServer getServerInstance() {
		return this.mcServer;
	}

	/**+
	 * On integrated servers, returns the host's player data to be
	 * written to level.dat.
	 */
	public NBTTagCompound getHostPlayerData() {
		return null;
	}

	public void setGameType(WorldSettings.GameType parGameType) {
		this.gameType = parGameType;
	}

	private void setPlayerGameTypeBasedOnOther(EntityPlayerMP worldIn, EntityPlayerMP parEntityPlayerMP2,
			World parWorld) {
		if (parEntityPlayerMP2 == null || parEntityPlayerMP2.getName().equals(mcServer.getServerOwner())) {
			if (parEntityPlayerMP2 != null) {
				worldIn.theItemInWorldManager.setGameType(parEntityPlayerMP2.theItemInWorldManager.getGameType());
			} else if (this.gameType != null) {
				worldIn.theItemInWorldManager.setGameType(this.gameType);
			}

			worldIn.theItemInWorldManager.initializeGameType(parWorld.getWorldInfo().getGameType());
		} else {
			worldIn.theItemInWorldManager.setGameType(lanGamemode);
		}
	}

	/**+
	 * Sets whether all players are allowed to use commands (cheats)
	 * on the server.
	 */
	public void setCommandsAllowedForAll(boolean parFlag) {
		this.commandsAllowedForAll = parFlag;
	}

	/**+
	 * Kicks everyone with "Server closed" as reason.
	 */
	public void removeAllPlayers() {
		for (int i = 0, l = this.playerEntityList.size(); i < l; ++i) {
			((EntityPlayerMP) this.playerEntityList.get(i)).playerNetServerHandler
					.kickPlayerFromServer("Server closed");
		}

	}

	public void sendChatMsgImpl(IChatComponent component, boolean isChat) {
		this.mcServer.addChatMessage(component);
		int i = isChat ? 1 : 0;
		this.sendPacketToAllPlayers(new S02PacketChat(component, (byte) i));
	}

	/**+
	 * Sends the given string to every player as chat message.
	 */
	public void sendChatMsg(IChatComponent component) {
		this.sendChatMsgImpl(component, true);
	}

	public StatisticsFile getPlayerStatsFile(EntityPlayer playerIn) {
		String name = playerIn.getName();
		StatisticsFile statisticsfile = (StatisticsFile) this.playerStatFiles.get(name);
		if (statisticsfile == null) {
			VFile2 file1 = WorldsDB
					.newVFile(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
			VFile2 file2 = WorldsDB.newVFile(file1, name + ".json");
			statisticsfile = new StatisticsFile(this.mcServer, file2);
			statisticsfile.readStatFile();
			this.playerStatFiles.put(name, statisticsfile);
		}

		return statisticsfile;
	}

	public void setViewDistance(int distance) {
		this.viewDistance = distance;
		int entityViewDist = getEntityViewDistance();
		if (this.mcServer.worldServers != null) {
			WorldServer[] srv = this.mcServer.worldServers;
			for (int i = 0; i < srv.length; ++i) {
				WorldServer worldserver = srv[i];
				if (worldserver != null) {
					worldserver.getPlayerManager().setPlayerViewRadius(distance);
					worldserver.getEntityTracker().updateMaxTrackingThreshold(entityViewDist);
				}
			}
		}
	}

	public List<EntityPlayerMP> func_181057_v() {
		return this.playerEntityList;
	}

	/**+
	 * Get's the EntityPlayerMP object representing the player with
	 * the UUID.
	 */
	public EntityPlayerMP getPlayerByUUID(EaglercraftUUID playerUUID) {
		return (EntityPlayerMP) this.uuidToPlayerMap.get(playerUUID);
	}

	public boolean func_183023_f(GameProfile var1) {
		return false;
	}

	public void updatePlayerViewDistance(EntityPlayerMP entityPlayerMP, int viewDistance2) {
		if (entityPlayerMP.getName().equals(mcServer.getServerOwner())) {
			if (viewDistance != viewDistance2) {
				logger.info("Owner is setting view distance: {}", viewDistance2);
				setViewDistance(viewDistance2);
			}
		}
	}

	public void configureLAN(int gamemode, boolean cheats) {
		lanGamemode = WorldSettings.GameType.getByID(gamemode);
		lanCheats = cheats;
	}
}