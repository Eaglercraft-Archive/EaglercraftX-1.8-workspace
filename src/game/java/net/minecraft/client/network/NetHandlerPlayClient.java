package net.minecraft.client.network;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.ClientUUIDLoadingCache;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import com.carrotsearch.hppc.cursors.ObjectIntCursor;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.lax1dude.eaglercraft.v1_8.notifications.ServerNotificationManager;
import net.lax1dude.eaglercraft.v1_8.skin_cache.ServerTextureCache;
import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageConstants;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.client.ClientMessageHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.handshake.StandardCaps;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.message.InjectedMessageController;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.message.LegacyMessageController;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.message.MessageController;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANClientNetworkManager;
import net.lax1dude.eaglercraft.v1_8.sp.socket.ClientIntegratedServerNetworkManager;
import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;
import net.lax1dude.eaglercraft.v1_8.webview.WebViewOverlayController;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S20PacketEntityProperties.Snapshot;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S22PacketMultiBlockChange.BlockUpdateData;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S38PacketPlayerListItem.AddPlayerData;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.Explosion;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;

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
public class NetHandlerPlayClient implements INetHandlerPlayClient {
	private static final Logger logger = LogManager.getLogger();
	private final EaglercraftNetworkManager netManager;
	private final GameProfile profile;
	private final GuiScreen guiScreenServer;
	private Minecraft gameController;
	private WorldClient clientWorldController;
	private boolean doneLoadingTerrain;
	private final Map<EaglercraftUUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();
	public int currentServerMaxPlayers = 20;
	private boolean field_147308_k = false;
	private boolean isIntegratedServer = false;
	/**+
	 * Just an ordinary random number generator, used to randomize
	 * audio pitch of item/orb pickup and randomize both
	 * particlespawn offset and velocity
	 */
	private final EaglercraftRandom avRandomizer = new EaglercraftRandom();
	private final ServerTextureCache textureCache;
	private final ServerNotificationManager notifManager;
	public boolean currentFNAWSkinAllowedState = true;
	public boolean currentFNAWSkinForcedState = true;
	private final MessageController eaglerMessageController;
	public byte[] cachedServerInfoHash = null;
	public byte[] cachedServerInfoData = null;
	public boolean allowedDisplayWebview = false;
	public boolean allowedDisplayWebviewYes = false;

	public NetHandlerPlayClient(Minecraft mcIn, GuiScreen parGuiScreen, EaglercraftNetworkManager parNetworkManager,
			GameProfile parGameProfile, GamePluginMessageProtocol eaglerProtocol) {
		this.gameController = mcIn;
		this.guiScreenServer = parGuiScreen;
		this.netManager = parNetworkManager;
		this.netManager.setNetHandler(this);
		this.profile = parGameProfile;
		this.isIntegratedServer = (parNetworkManager instanceof ClientIntegratedServerNetworkManager)
				|| (parNetworkManager instanceof LANClientNetworkManager);
		ClientMessageHandler handler = ClientMessageHandler.createClientHandler(eaglerProtocol.ver, this);
		if (eaglerProtocol.ver >= 5) {
			this.eaglerMessageController = new InjectedMessageController(eaglerProtocol, handler,
					GamePluginMessageConstants.CLIENT_TO_SERVER, parNetworkManager::injectRawFrame);
			parNetworkManager.setInjectedMessageController((InjectedMessageController) eaglerMessageController);
		} else {
			this.eaglerMessageController = new LegacyMessageController(eaglerProtocol, handler,
					GamePluginMessageConstants.CLIENT_TO_SERVER,
					(ch, msg) -> addToSendQueue(new C17PacketCustomPayload(ch, msg)));
		}
		this.textureCache = ServerTextureCache.create(this, mcIn.getTextureManager());
		this.notifManager = new ServerNotificationManager(mcIn.getTextureManager());
	}

	/**+
	 * Clears the WorldClient instance associated with this
	 * NetHandlerPlayClient
	 */
	public void cleanup() {
		this.clientWorldController = null;
		this.textureCache.destroy();
		this.notifManager.destroy();
	}

	public ServerTextureCache getTextureCache() {
		return this.textureCache;
	}

	public ServerNotificationManager getNotifManager() {
		return this.notifManager;
	}

	public MessageController getEaglerMessageController() {
		return eaglerMessageController;
	}

	public GamePluginMessageProtocol getEaglerMessageProtocol() {
		return eaglerMessageController != null ? eaglerMessageController.getProtocol() : null;
	}

	public void sendEaglerMessage(GameMessagePacket packet) {
		eaglerMessageController.sendPacket(packet);
	}

	public boolean webViewSendHandler(GameMessagePacket pkt) {
		if (eaglerMessageController == null) {
			return false;
		}
		if (this.gameController.thePlayer == null || this.gameController.thePlayer.sendQueue != this) {
			logger.error("WebView sent message on a dead handler!");
			return false;
		}
		if (eaglerMessageController.getProtocol().ver >= 4) {
			sendEaglerMessage(pkt);
			return true;
		} else {
			return false;
		}
	}

	/**+
	 * Registers some server properties
	 * (gametype,hardcore-mode,terraintype,difficulty,player limit),
	 * creates a new WorldClient and sets the player initial
	 * dimension
	 */
	public void handleJoinGame(S01PacketJoinGame packetIn) {
		this.gameController.playerController = new PlayerControllerMP(this.gameController, this);
		this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false,
				packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty());
		this.gameController.gameSettings.difficulty = packetIn.getDifficulty();
		this.gameController.loadWorld(this.clientWorldController);
		this.gameController.thePlayer.dimension = packetIn.getDimension();
		this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
		this.gameController.thePlayer.setEntityId(packetIn.getEntityId());
		this.currentServerMaxPlayers = packetIn.getMaxPlayers();
		this.gameController.thePlayer.setReducedDebug(packetIn.isReducedDebugInfo());
		this.gameController.playerController.setGameType(packetIn.getGameType());
		this.gameController.gameSettings.sendSettingsToServer();
		this.netManager.sendPacket(new C17PacketCustomPayload("MC|Brand",
				(new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));
		if (VoiceClientController.isClientSupported()) {
			if (netManager.getServerCapabilities().hasCapability(StandardCaps.VOICE, 0)) {
				VoiceClientController.initializeVoiceClient(this::sendEaglerMessage,
						eaglerMessageController.getProtocol().ver);
			} else {
				VoiceClientController.initializeVoiceClient(null, -1);
			}
		}
		if (netManager.getServerCapabilities().hasCapability(StandardCaps.WEBVIEW, 0)) {
			WebViewOverlayController.setPacketSendCallback(this::webViewSendHandler);
		} else {
			WebViewOverlayController.setPacketSendCallback(null);
		}
	}

	/**+
	 * Spawns an instance of the objecttype indicated by the packet
	 * and sets its position and momentum
	 */
	public void handleSpawnObject(S0EPacketSpawnObject packetIn) {
		double d0 = (double) packetIn.getX() / 32.0D;
		double d1 = (double) packetIn.getY() / 32.0D;
		double d2 = (double) packetIn.getZ() / 32.0D;
		Entity object = null;
		boolean b = false;
		switch (packetIn.getType()) {
		case 10:
			object = EntityMinecart.func_180458_a(this.clientWorldController, d0, d1, d2,
					EntityMinecart.EnumMinecartType.byNetworkID(packetIn.func_149009_m()));
			break;
		case 90:
			b = true;
			Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
			if (entity instanceof EntityPlayer) {
				object = new EntityFishHook(this.clientWorldController, d0, d1, d2, (EntityPlayer) entity);
			}
			break;
		case 60:
			object = new EntityArrow(this.clientWorldController, d0, d1, d2);
			break;
		case 61:
			object = new EntitySnowball(this.clientWorldController, d0, d1, d2);
			break;
		case 71:
			b = true;
			object = new EntityItemFrame(this.clientWorldController,
					new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)),
					EnumFacing.getHorizontal(packetIn.func_149009_m()));
			break;
		case 77:
			b = true;
			object = new EntityLeashKnot(this.clientWorldController, new BlockPos(MathHelper.floor_double(d0),
					MathHelper.floor_double(d1), MathHelper.floor_double(d2)));
			break;
		case 65:
			object = new EntityEnderPearl(this.clientWorldController, d0, d1, d2);
			break;
		case 72:
			object = new EntityEnderEye(this.clientWorldController, d0, d1, d2);
			break;
		case 76:
			object = new EntityFireworkRocket(this.clientWorldController, d0, d1, d2, (ItemStack) null);
			break;
		case 63:
			b = true;
			object = new EntityLargeFireball(this.clientWorldController, d0, d1, d2,
					(double) packetIn.getSpeedX() / 8000.0D, (double) packetIn.getSpeedY() / 8000.0D,
					(double) packetIn.getSpeedZ() / 8000.0D);
			break;
		case 64:
			b = true;
			object = new EntitySmallFireball(this.clientWorldController, d0, d1, d2,
					(double) packetIn.getSpeedX() / 8000.0D, (double) packetIn.getSpeedY() / 8000.0D,
					(double) packetIn.getSpeedZ() / 8000.0D);
			break;
		case 66:
			b = true;
			object = new EntityWitherSkull(this.clientWorldController, d0, d1, d2,
					(double) packetIn.getSpeedX() / 8000.0D, (double) packetIn.getSpeedY() / 8000.0D,
					(double) packetIn.getSpeedZ() / 8000.0D);
			break;
		case 62:
			object = new EntityEgg(this.clientWorldController, d0, d1, d2);
			break;
		case 73:
			b = true;
			object = new EntityPotion(this.clientWorldController, d0, d1, d2, packetIn.func_149009_m());
			break;
		case 75:
			b = true;
			object = new EntityExpBottle(this.clientWorldController, d0, d1, d2);
			break;
		case 1:
			object = new EntityBoat(this.clientWorldController, d0, d1, d2);
			break;
		case 50:
			object = new EntityTNTPrimed(this.clientWorldController, d0, d1, d2, (EntityLivingBase) null);
			break;
		case 78:
			object = new EntityArmorStand(this.clientWorldController, d0, d1, d2);
			break;
		case 51:
			object = new EntityEnderCrystal(this.clientWorldController, d0, d1, d2);
			break;
		case 2:
			object = new EntityItem(this.clientWorldController, d0, d1, d2);
			break;
		case 70:
			b = true;
			object = new EntityFallingBlock(this.clientWorldController, d0, d1, d2,
					Block.getStateById(packetIn.func_149009_m() & '\uffff'));
			break;
		}

		if (b) {
			// fix for compiler bug
			packetIn.func_149002_g(0);
		}

		if (object != null) {
			object.serverPosX = packetIn.getX();
			object.serverPosY = packetIn.getY();
			object.serverPosZ = packetIn.getZ();
			object.rotationPitch = (float) (packetIn.getPitch() * 360) / 256.0F;
			object.rotationYaw = (float) (packetIn.getYaw() * 360) / 256.0F;
			Entity[] aentity = object.getParts();
			if (aentity != null) {
				int i = packetIn.getEntityID() - object.getEntityId();

				for (int j = 0; j < aentity.length; ++j) {
					aentity[j].setEntityId(aentity[j].getEntityId() + i);
				}
			}

			object.setEntityId(packetIn.getEntityID());
			this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), object);
			if (packetIn.func_149009_m() > 0) {
				if (packetIn.getType() == 60) {
					Entity entity1 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
					if (entity1 instanceof EntityLivingBase && object instanceof EntityArrow) {
						((EntityArrow) object).shootingEntity = entity1;
					}
				}

				object.setVelocity((double) packetIn.getSpeedX() / 8000.0D, (double) packetIn.getSpeedY() / 8000.0D,
						(double) packetIn.getSpeedZ() / 8000.0D);
			}
		}

	}

	/**+
	 * Spawns an experience orb and sets its value (amount of XP)
	 */
	public void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb packetIn) {
		EntityXPOrb entityxporb = new EntityXPOrb(this.clientWorldController, (double) packetIn.getX() / 32.0D,
				(double) packetIn.getY() / 32.0D, (double) packetIn.getZ() / 32.0D, packetIn.getXPValue());
		entityxporb.serverPosX = packetIn.getX();
		entityxporb.serverPosY = packetIn.getY();
		entityxporb.serverPosZ = packetIn.getZ();
		entityxporb.rotationYaw = 0.0F;
		entityxporb.rotationPitch = 0.0F;
		entityxporb.setEntityId(packetIn.getEntityID());
		this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entityxporb);
	}

	/**+
	 * Handles globally visible entities. Used in vanilla for
	 * lightning bolts
	 */
	public void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity packetIn) {
		double d0 = (double) packetIn.func_149051_d() / 32.0D;
		double d1 = (double) packetIn.func_149050_e() / 32.0D;
		double d2 = (double) packetIn.func_149049_f() / 32.0D;
		EntityLightningBolt entitylightningbolt = null;
		if (packetIn.func_149053_g() == 1) {
			entitylightningbolt = new EntityLightningBolt(this.clientWorldController, d0, d1, d2);
		}

		if (entitylightningbolt != null) {
			entitylightningbolt.serverPosX = packetIn.func_149051_d();
			entitylightningbolt.serverPosY = packetIn.func_149050_e();
			entitylightningbolt.serverPosZ = packetIn.func_149049_f();
			entitylightningbolt.rotationYaw = 0.0F;
			entitylightningbolt.rotationPitch = 0.0F;
			entitylightningbolt.setEntityId(packetIn.func_149052_c());
			this.clientWorldController.addWeatherEffect(entitylightningbolt);
		}

	}

	/**+
	 * Handles the spawning of a painting object
	 */
	public void handleSpawnPainting(S10PacketSpawnPainting packetIn) {
		EntityPainting entitypainting = new EntityPainting(this.clientWorldController, packetIn.getPosition(),
				packetIn.getFacing(), packetIn.getTitle());
		this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitypainting);
	}

	/**+
	 * Sets the velocity of the specified entity to the specified
	 * value
	 */
	public void handleEntityVelocity(S12PacketEntityVelocity packetIn) {
		Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
		if (entity != null) {
			entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D,
					(double) packetIn.getMotionZ() / 8000.0D);
		}
	}

	/**+
	 * Invoked when the server registers new proximate objects in
	 * your watchlist or when objects in your watchlist have changed
	 * -> Registers any changes locally
	 */
	public void handleEntityMetadata(S1CPacketEntityMetadata packetIn) {
		Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
		if (entity != null && packetIn.func_149376_c() != null) {
			entity.getDataWatcher().updateWatchedObjectsFromList(packetIn.func_149376_c());
		}

	}

	/**+
	 * Handles the creation of a nearby player entity, sets the
	 * position and held item
	 */
	public void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn) {
		double d0 = (double) packetIn.getX() / 32.0D;
		double d1 = (double) packetIn.getY() / 32.0D;
		double d2 = (double) packetIn.getZ() / 32.0D;
		float f = (float) (packetIn.getYaw() * 360) / 256.0F;
		float f1 = (float) (packetIn.getPitch() * 360) / 256.0F;
		EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(this.gameController.theWorld,
				this.getPlayerInfo(packetIn.getPlayer()).getGameProfile());
		entityotherplayermp.prevPosX = entityotherplayermp.lastTickPosX = (double) (entityotherplayermp.serverPosX = packetIn
				.getX());
		entityotherplayermp.prevPosY = entityotherplayermp.lastTickPosY = (double) (entityotherplayermp.serverPosY = packetIn
				.getY());
		entityotherplayermp.prevPosZ = entityotherplayermp.lastTickPosZ = (double) (entityotherplayermp.serverPosZ = packetIn
				.getZ());
		int i = packetIn.getCurrentItemID();
		if (i == 0) {
			entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = null;
		} else {
			entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = new ItemStack(
					Item.getItemById(i), 1, 0);
		}

		entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
		this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entityotherplayermp);
		List list = packetIn.func_148944_c();
		if (list != null) {
			entityotherplayermp.getDataWatcher().updateWatchedObjectsFromList(list);
		}

	}

	/**+
	 * Updates an entity's position and rotation as specified by the
	 * packet
	 */
	public void handleEntityTeleport(S18PacketEntityTeleport packetIn) {
		Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
		if (entity != null) {
			entity.serverPosX = packetIn.getX();
			entity.serverPosY = packetIn.getY();
			entity.serverPosZ = packetIn.getZ();
			double d0 = (double) entity.serverPosX / 32.0D;
			double d1 = (double) entity.serverPosY / 32.0D;
			double d2 = (double) entity.serverPosZ / 32.0D;
			float f = (float) (packetIn.getYaw() * 360) / 256.0F;
			float f1 = (float) (packetIn.getPitch() * 360) / 256.0F;
			if (Math.abs(entity.posX - d0) < 0.03125D && Math.abs(entity.posY - d1) < 0.015625D
					&& Math.abs(entity.posZ - d2) < 0.03125D) {
				entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f1, 3, true);
			} else {
				entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, true);
			}

			entity.onGround = packetIn.getOnGround();
		}
	}

	/**+
	 * Updates which hotbar slot of the player is currently selected
	 */
	public void handleHeldItemChange(S09PacketHeldItemChange packetIn) {
		if (packetIn.getHeldItemHotbarIndex() >= 0
				&& packetIn.getHeldItemHotbarIndex() < InventoryPlayer.getHotbarSize()) {
			this.gameController.thePlayer.inventory.currentItem = packetIn.getHeldItemHotbarIndex();
		}

	}

	/**+
	 * Updates the specified entity's position by the specified
	 * relative moment and absolute rotation. Note that subclassing
	 * of the packet allows for the specification of a subset of
	 * this data (e.g. only rel. position, abs. rotation or both).
	 */
	public void handleEntityMovement(S14PacketEntity packetIn) {
		Entity entity = packetIn.getEntity(this.clientWorldController);
		if (entity != null) {
			entity.serverPosX += packetIn.func_149062_c();
			entity.serverPosY += packetIn.func_149061_d();
			entity.serverPosZ += packetIn.func_149064_e();
			double d0 = (double) entity.serverPosX / 32.0D;
			double d1 = (double) entity.serverPosY / 32.0D;
			double d2 = (double) entity.serverPosZ / 32.0D;
			float f = packetIn.func_149060_h() ? (float) (packetIn.func_149066_f() * 360) / 256.0F : entity.rotationYaw;
			float f1 = packetIn.func_149060_h() ? (float) (packetIn.func_149063_g() * 360) / 256.0F
					: entity.rotationPitch;
			entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, false);
			entity.onGround = packetIn.getOnGround();
		}
	}

	/**+
	 * Updates the direction in which the specified entity is
	 * looking, normally this head rotation is independent of the
	 * rotation of the entity itself
	 */
	public void handleEntityHeadLook(S19PacketEntityHeadLook packetIn) {
		Entity entity = packetIn.getEntity(this.clientWorldController);
		if (entity != null) {
			float f = (float) (packetIn.getYaw() * 360) / 256.0F;
			entity.setRotationYawHead(f);
		}
	}

	/**+
	 * Locally eliminates the entities. Invoked by the server when
	 * the items are in fact destroyed, or the player is no longer
	 * registered as required to monitor them. The latter happens
	 * when distance between the player and item increases beyond a
	 * certain treshold (typically the viewing distance)
	 */
	public void handleDestroyEntities(S13PacketDestroyEntities packetIn) {
		for (int i = 0; i < packetIn.getEntityIDs().length; ++i) {
			this.clientWorldController.removeEntityFromWorld(packetIn.getEntityIDs()[i]);
		}

	}

	/**+
	 * Handles changes in player positioning and rotation such as
	 * when travelling to a new dimension, (re)spawning, mounting
	 * horses etc. Seems to immediately reply to the server with the
	 * clients post-processing perspective on the player positioning
	 */
	public void handlePlayerPosLook(S08PacketPlayerPosLook packetIn) {
		EntityPlayerSP entityplayersp = this.gameController.thePlayer;
		double d0 = packetIn.getX();
		double d1 = packetIn.getY();
		double d2 = packetIn.getZ();
		float f = packetIn.getYaw();
		float f1 = packetIn.getPitch();
		if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) {
			d0 += entityplayersp.posX;
		} else {
			entityplayersp.motionX = 0.0D;
		}

		if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
			d1 += entityplayersp.posY;
		} else {
			entityplayersp.motionY = 0.0D;
		}

		if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
			d2 += entityplayersp.posZ;
		} else {
			entityplayersp.motionZ = 0.0D;
		}

		if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
			f1 += entityplayersp.rotationPitch;
		}

		if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
			f += entityplayersp.rotationYaw;
		}

		entityplayersp.setPositionAndRotation(d0, d1, d2, f, f1);
		this.netManager.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(entityplayersp.posX,
				entityplayersp.getEntityBoundingBox().minY, entityplayersp.posZ, entityplayersp.rotationYaw,
				entityplayersp.rotationPitch, false));
		if (!this.doneLoadingTerrain) {
			this.gameController.thePlayer.prevPosX = this.gameController.thePlayer.posX;
			this.gameController.thePlayer.prevPosY = this.gameController.thePlayer.posY;
			this.gameController.thePlayer.prevPosZ = this.gameController.thePlayer.posZ;
			this.doneLoadingTerrain = true;
			this.gameController.displayGuiScreen((GuiScreen) null);
		}

	}

	/**+
	 * Received from the servers PlayerManager if between 1 and 64
	 * blocks in a chunk are changed. If only one block requires an
	 * update, the server sends S23PacketBlockChange and if 64 or
	 * more blocks are changed, the server sends S21PacketChunkData
	 */
	public void handleMultiBlockChange(S22PacketMultiBlockChange packetIn) {
		BlockUpdateData[] dat = packetIn.getChangedBlocks();
		for (int i = 0; i < dat.length; ++i) {
			BlockUpdateData s22packetmultiblockchange$blockupdatedata = dat[i];
			this.clientWorldController.invalidateRegionAndSetBlock(s22packetmultiblockchange$blockupdatedata.getPos(),
					s22packetmultiblockchange$blockupdatedata.getBlockState());
		}

	}

	/**+
	 * Updates the specified chunk with the supplied data, marks it
	 * for re-rendering and lighting recalculation
	 */
	public void handleChunkData(S21PacketChunkData packetIn) {
		if (packetIn.func_149274_i()) {
			if (packetIn.getExtractedSize() == 0) {
				this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), false);
				return;
			}

			this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true);
		}

		this.clientWorldController.invalidateBlockReceiveRegion(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4,
				(packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
		Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(packetIn.getChunkX(), packetIn.getChunkZ());
		chunk.fillChunk(packetIn.func_149272_d(), packetIn.getExtractedSize(), packetIn.func_149274_i());
		this.clientWorldController.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0,
				packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
		if (!packetIn.func_149274_i() || !(this.clientWorldController.provider instanceof WorldProviderSurface)) {
			chunk.resetRelightChecks();
		}

	}

	/**+
	 * Updates the block and metadata and generates a blockupdate
	 * (and notify the clients)
	 */
	public void handleBlockChange(S23PacketBlockChange packetIn) {
		this.clientWorldController.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
	}

	/**+
	 * Closes the network channel
	 */
	public void handleDisconnect(S40PacketDisconnect packetIn) {
		this.netManager.closeChannel(packetIn.getReason());
	}

	/**+
	 * Invoked when disconnecting, the parameter is a ChatComponent
	 * describing the reason for termination
	 */
	public void onDisconnect(IChatComponent ichatcomponent) {
		VoiceClientController.handleServerDisconnect();
		Minecraft.getMinecraft().getRenderManager()
				.setEnableFNAWSkins(this.gameController.gameSettings.enableFNAWSkins);
		if (this.gameController.theWorld != null) {
			this.gameController.loadWorld((WorldClient) null);
		}
		if (this.guiScreenServer != null) {
			this.gameController.shutdownIntegratedServer(
					new GuiDisconnected(this.guiScreenServer, "disconnect.lost", ichatcomponent));
		} else {
			this.gameController.shutdownIntegratedServer(
					new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", ichatcomponent));
		}
	}

	public void addToSendQueue(Packet parPacket) {
		this.netManager.sendPacket(parPacket);
	}

	public void handleCollectItem(S0DPacketCollectItem packetIn) {
		Entity entity = this.clientWorldController.getEntityByID(packetIn.getCollectedItemEntityID());
		Object object = (EntityLivingBase) this.clientWorldController.getEntityByID(packetIn.getEntityID());
		if (object == null) {
			object = this.gameController.thePlayer;
		}

		if (entity != null) {
			if (entity instanceof EntityXPOrb) {
				this.clientWorldController.playSoundAtEntity(entity, "random.orb", 0.2F,
						((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			} else {
				this.clientWorldController.playSoundAtEntity(entity, "random.pop", 0.2F,
						((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			}

			this.gameController.effectRenderer
					.addEffect(new EntityPickupFX(this.clientWorldController, entity, (Entity) object, 0.5F));
			this.clientWorldController.removeEntityFromWorld(packetIn.getCollectedItemEntityID());
		}

	}

	/**+
	 * Prints a chatmessage in the chat GUI
	 */
	public void handleChat(S02PacketChat packetIn) {
		if (packetIn.getType() == 2) {
			this.gameController.ingameGUI.setRecordPlaying(packetIn.getChatComponent(), false);
		} else {
			this.gameController.ingameGUI.getChatGUI().printChatMessage(packetIn.getChatComponent());
		}

	}

	/**+
	 * Renders a specified animation: Waking up a player, a living
	 * entity swinging its currently held item, being hurt or
	 * receiving a critical hit by normal or magical means
	 */
	public void handleAnimation(S0BPacketAnimation packetIn) {
		Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
		if (entity != null) {
			if (packetIn.getAnimationType() == 0) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
				entitylivingbase.swingItem();
			} else if (packetIn.getAnimationType() == 1) {
				entity.performHurtAnimation();
			} else if (packetIn.getAnimationType() == 2) {
				EntityPlayer entityplayer = (EntityPlayer) entity;
				entityplayer.wakeUpPlayer(false, false, false);
			} else if (packetIn.getAnimationType() == 4) {
				this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
			} else if (packetIn.getAnimationType() == 5) {
				this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
			}

		}
	}

	/**+
	 * Retrieves the player identified by the packet, puts him to
	 * sleep if possible (and flags whether all players are asleep)
	 */
	public void handleUseBed(S0APacketUseBed packetIn) {
		packetIn.getPlayer(this.clientWorldController).trySleep(packetIn.getBedPosition());
	}

	/**+
	 * Spawns the mob entity at the specified location, with the
	 * specified rotation, momentum and type. Updates the entities
	 * Datawatchers with the entity metadata specified in the packet
	 */
	public void handleSpawnMob(S0FPacketSpawnMob packetIn) {
		double d0 = (double) packetIn.getX() / 32.0D;
		double d1 = (double) packetIn.getY() / 32.0D;
		double d2 = (double) packetIn.getZ() / 32.0D;
		float f = (float) (packetIn.getYaw() * 360) / 256.0F;
		float f1 = (float) (packetIn.getPitch() * 360) / 256.0F;
		EntityLivingBase entitylivingbase = (EntityLivingBase) EntityList.createEntityByID(packetIn.getEntityType(),
				this.gameController.theWorld);
		entitylivingbase.serverPosX = packetIn.getX();
		entitylivingbase.serverPosY = packetIn.getY();
		entitylivingbase.serverPosZ = packetIn.getZ();
		entitylivingbase.renderYawOffset = entitylivingbase.rotationYawHead = (float) (packetIn.getHeadPitch() * 360)
				/ 256.0F;
		Entity[] aentity = entitylivingbase.getParts();
		if (aentity != null) {
			int i = packetIn.getEntityID() - entitylivingbase.getEntityId();

			for (int j = 0; j < aentity.length; ++j) {
				aentity[j].setEntityId(aentity[j].getEntityId() + i);
			}
		}

		entitylivingbase.setEntityId(packetIn.getEntityID());
		entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
		entitylivingbase.motionX = (double) ((float) packetIn.getVelocityX() / 8000.0F);
		entitylivingbase.motionY = (double) ((float) packetIn.getVelocityY() / 8000.0F);
		entitylivingbase.motionZ = (double) ((float) packetIn.getVelocityZ() / 8000.0F);
		this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitylivingbase);
		List list = packetIn.func_149027_c();
		if (list != null) {
			entitylivingbase.getDataWatcher().updateWatchedObjectsFromList(list);
		}

	}

	public void handleTimeUpdate(S03PacketTimeUpdate packetIn) {
		this.gameController.theWorld.setTotalWorldTime(packetIn.getTotalWorldTime());
		this.gameController.theWorld.setWorldTime(packetIn.getWorldTime());
	}

	public void handleSpawnPosition(S05PacketSpawnPosition packetIn) {
		this.gameController.thePlayer.setSpawnPoint(packetIn.getSpawnPos(), true);
		this.gameController.theWorld.getWorldInfo().setSpawn(packetIn.getSpawnPos());
	}

	public void handleEntityAttach(S1BPacketEntityAttach packetIn) {
		Object object = this.clientWorldController.getEntityByID(packetIn.getEntityId());
		Entity entity = this.clientWorldController.getEntityByID(packetIn.getVehicleEntityId());
		if (packetIn.getLeash() == 0) {
			boolean flag = false;
			if (packetIn.getEntityId() == this.gameController.thePlayer.getEntityId()) {
				object = this.gameController.thePlayer;
				if (entity instanceof EntityBoat) {
					((EntityBoat) entity).setIsBoatEmpty(false);
				}

				flag = ((Entity) object).ridingEntity == null && entity != null;
			} else if (entity instanceof EntityBoat) {
				((EntityBoat) entity).setIsBoatEmpty(true);
			}

			if (object == null) {
				return;
			}

			((Entity) object).mountEntity(entity);
			if (flag) {
				GameSettings gamesettings = this.gameController.gameSettings;
				this.gameController.ingameGUI.setRecordPlaying(
						I18n.format("mount.onboard",
								new Object[] {
										GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.getKeyCode()) }),
						false);
			}
		} else if (packetIn.getLeash() == 1 && object instanceof EntityLiving) {
			if (entity != null) {
				((EntityLiving) object).setLeashedToEntity(entity, false);
			} else {
				((EntityLiving) object).clearLeashed(false, false);
			}
		}

	}

	/**+
	 * Invokes the entities' handleUpdateHealth method which is
	 * implemented in LivingBase (hurt/death), MinecartMobSpawner
	 * (spawn delay), FireworkRocket & MinecartTNT (explosion),
	 * IronGolem (throwing,...), Witch (spawn particles), Zombie
	 * (villager transformation), Animal (breeding mode particles),
	 * Horse (breeding/smoke particles), Sheep (...), Tameable
	 * (...), Villager (particles for breeding mode, angry and
	 * happy), Wolf (...)
	 */
	public void handleEntityStatus(S19PacketEntityStatus packetIn) {
		Entity entity = packetIn.getEntity(this.clientWorldController);
		if (entity != null) {
			if (packetIn.getOpCode() == 21) {
				this.gameController.getSoundHandler().playSound(new GuardianSound((EntityGuardian) entity));
			} else {
				entity.handleStatusUpdate(packetIn.getOpCode());
			}
		}

	}

	public void handleUpdateHealth(S06PacketUpdateHealth packetIn) {
		this.gameController.thePlayer.setPlayerSPHealth(packetIn.getHealth());
		this.gameController.thePlayer.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
		this.gameController.thePlayer.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
	}

	public void handleSetExperience(S1FPacketSetExperience packetIn) {
		this.gameController.thePlayer.setXPStats(packetIn.func_149397_c(), packetIn.getTotalExperience(),
				packetIn.getLevel());
	}

	public void handleRespawn(S07PacketRespawn packetIn) {
		if (packetIn.getDimensionID() != this.gameController.thePlayer.dimension) {
			this.doneLoadingTerrain = false;
			Scoreboard scoreboard = this.clientWorldController.getScoreboard();
			this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false,
					this.gameController.theWorld.getWorldInfo().isHardcoreModeEnabled(), packetIn.getWorldType()),
					packetIn.getDimensionID(), packetIn.getDifficulty());
			this.clientWorldController.setWorldScoreboard(scoreboard);
			this.gameController.loadWorld(this.clientWorldController);
			this.gameController.thePlayer.dimension = packetIn.getDimensionID();
			this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
		}

		this.gameController.setDimensionAndSpawnPlayer(packetIn.getDimensionID());
		this.gameController.playerController.setGameType(packetIn.getGameType());
	}

	/**+
	 * Initiates a new explosion (sound, particles, drop spawn) for
	 * the affected blocks indicated by the packet.
	 */
	public void handleExplosion(S27PacketExplosion packetIn) {
		Explosion explosion = new Explosion(this.gameController.theWorld, (Entity) null, packetIn.getX(),
				packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
		explosion.doExplosionB(true);
		this.gameController.thePlayer.motionX += (double) packetIn.func_149149_c();
		this.gameController.thePlayer.motionY += (double) packetIn.func_149144_d();
		this.gameController.thePlayer.motionZ += (double) packetIn.func_149147_e();
	}

	/**+
	 * Displays a GUI by ID. In order starting from id 0: Chest,
	 * Workbench, Furnace, Dispenser, Enchanting table, Brewing
	 * stand, Villager merchant, Beacon, Anvil, Hopper, Dropper,
	 * Horse
	 */
	public void handleOpenWindow(S2DPacketOpenWindow packetIn) {
		EntityPlayerSP entityplayersp = this.gameController.thePlayer;
		if ("minecraft:container".equals(packetIn.getGuiId())) {
			entityplayersp.displayGUIChest(new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
			entityplayersp.openContainer.windowId = packetIn.getWindowId();
		} else if ("minecraft:villager".equals(packetIn.getGuiId())) {
			entityplayersp.displayVillagerTradeGui(new NpcMerchant(entityplayersp, packetIn.getWindowTitle()));
			entityplayersp.openContainer.windowId = packetIn.getWindowId();
		} else if ("EntityHorse".equals(packetIn.getGuiId())) {
			Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
			if (entity instanceof EntityHorse) {
				entityplayersp.displayGUIHorse((EntityHorse) entity,
						new AnimalChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
				entityplayersp.openContainer.windowId = packetIn.getWindowId();
			}
		} else if (!packetIn.hasSlots()) {
			entityplayersp.displayGui(new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
			entityplayersp.openContainer.windowId = packetIn.getWindowId();
		} else {
			ContainerLocalMenu containerlocalmenu = new ContainerLocalMenu(packetIn.getGuiId(),
					packetIn.getWindowTitle(), packetIn.getSlotCount());
			entityplayersp.displayGUIChest(containerlocalmenu);
			entityplayersp.openContainer.windowId = packetIn.getWindowId();
		}

	}

	/**+
	 * Handles pickin up an ItemStack or dropping one in your
	 * inventory or an open (non-creative) container
	 */
	public void handleSetSlot(S2FPacketSetSlot packetIn) {
		EntityPlayerSP entityplayersp = this.gameController.thePlayer;
		if (packetIn.func_149175_c() == -1) {
			entityplayersp.inventory.setItemStack(packetIn.func_149174_e());
		} else {
			boolean flag = false;
			if (this.gameController.currentScreen instanceof GuiContainerCreative) {
				GuiContainerCreative guicontainercreative = (GuiContainerCreative) this.gameController.currentScreen;
				flag = guicontainercreative.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex();
			}

			if (packetIn.func_149175_c() == 0 && packetIn.func_149173_d() >= 36 && packetIn.func_149173_d() < 45) {
				ItemStack itemstack = entityplayersp.inventoryContainer.getSlot(packetIn.func_149173_d()).getStack();
				if (packetIn.func_149174_e() != null
						&& (itemstack == null || itemstack.stackSize < packetIn.func_149174_e().stackSize)) {
					packetIn.func_149174_e().animationsToGo = 5;
				}

				entityplayersp.inventoryContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
			} else if (packetIn.func_149175_c() == entityplayersp.openContainer.windowId
					&& (packetIn.func_149175_c() != 0 || !flag)) {
				entityplayersp.openContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
			}
		}

	}

	/**+
	 * Verifies that the server and client are synchronized with
	 * respect to the inventory/container opened by the player and
	 * confirms if it is the case.
	 */
	public void handleConfirmTransaction(S32PacketConfirmTransaction packetIn) {
		Container container = null;
		EntityPlayerSP entityplayersp = this.gameController.thePlayer;
		if (packetIn.getWindowId() == 0) {
			container = entityplayersp.inventoryContainer;
		} else if (packetIn.getWindowId() == entityplayersp.openContainer.windowId) {
			container = entityplayersp.openContainer;
		}

		if (container != null && !packetIn.func_148888_e()) {
			this.addToSendQueue(
					new C0FPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
		}

	}

	/**+
	 * Handles the placement of a specified ItemStack in a specified
	 * container/inventory slot
	 */
	public void handleWindowItems(S30PacketWindowItems packetIn) {
		EntityPlayerSP entityplayersp = this.gameController.thePlayer;
		if (packetIn.func_148911_c() == 0) {
			entityplayersp.inventoryContainer.putStacksInSlots(packetIn.getItemStacks());
		} else if (packetIn.func_148911_c() == entityplayersp.openContainer.windowId) {
			entityplayersp.openContainer.putStacksInSlots(packetIn.getItemStacks());
		}

	}

	/**+
	 * Creates a sign in the specified location if it didn't exist
	 * and opens the GUI to edit its text
	 */
	public void handleSignEditorOpen(S36PacketSignEditorOpen packetIn) {
		Object object = this.clientWorldController.getTileEntity(packetIn.getSignPosition());
		if (!(object instanceof TileEntitySign)) {
			object = new TileEntitySign();
			((TileEntity) object).setWorldObj(this.clientWorldController);
			((TileEntity) object).setPos(packetIn.getSignPosition());
		}

		this.gameController.thePlayer.openEditSign((TileEntitySign) object);
	}

	/**+
	 * Updates a specified sign with the specified text lines
	 */
	public void handleUpdateSign(S33PacketUpdateSign packetIn) {
		boolean flag = false;
		if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos())) {
			TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
			if (tileentity instanceof TileEntitySign) {
				TileEntitySign tileentitysign = (TileEntitySign) tileentity;
				if (tileentitysign.getIsEditable()) {
					System.arraycopy(packetIn.getLines(), 0, tileentitysign.signText, 0, 4);
					tileentitysign.markDirty();
					tileentitysign.clearProfanityFilterCache();
				}

				flag = true;
			}
		}

		if (!flag && this.gameController.thePlayer != null) {
			this.gameController.thePlayer.addChatMessage(new ChatComponentText("Unable to locate sign at "
					+ packetIn.getPos().getX() + ", " + packetIn.getPos().getY() + ", " + packetIn.getPos().getZ()));
		}

	}

	/**+
	 * Updates the NBTTagCompound metadata of instances of the
	 * following entitytypes: Mob spawners, command blocks, beacons,
	 * skulls, flowerpot
	 */
	public void handleUpdateTileEntity(S35PacketUpdateTileEntity packetIn) {
		if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos())) {
			TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
			int i = packetIn.getTileEntityType();
			if (i == 1 && tileentity instanceof TileEntityMobSpawner
					|| i == 2 && tileentity instanceof TileEntityCommandBlock
					|| i == 3 && tileentity instanceof TileEntityBeacon
					|| i == 4 && tileentity instanceof TileEntitySkull
					|| i == 5 && tileentity instanceof TileEntityFlowerPot
					|| i == 6 && tileentity instanceof TileEntityBanner) {
				tileentity.readFromNBT(packetIn.getNbtCompound());
			}
		}

	}

	/**+
	 * Sets the progressbar of the opened window to the specified
	 * value
	 */
	public void handleWindowProperty(S31PacketWindowProperty packetIn) {
		EntityPlayerSP entityplayersp = this.gameController.thePlayer;
		if (entityplayersp.openContainer != null && entityplayersp.openContainer.windowId == packetIn.getWindowId()) {
			entityplayersp.openContainer.updateProgressBar(packetIn.getVarIndex(), packetIn.getVarValue());
		}

	}

	public void handleEntityEquipment(S04PacketEntityEquipment packetIn) {
		Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
		if (entity != null) {
			entity.setCurrentItemOrArmor(packetIn.getEquipmentSlot(), packetIn.getItemStack());
		}

	}

	/**+
	 * Resets the ItemStack held in hand and closes the window that
	 * is opened
	 */
	public void handleCloseWindow(S2EPacketCloseWindow packetIn) {
		this.gameController.thePlayer.closeScreenAndDropStack();
	}

	/**+
	 * Triggers Block.onBlockEventReceived, which is implemented in
	 * BlockPistonBase for extension/retraction, BlockNote for
	 * setting the instrument (including audiovisual feedback) and
	 * in BlockContainer to set the number of players accessing a
	 * (Ender)Chest
	 */
	public void handleBlockAction(S24PacketBlockAction packetIn) {
		this.gameController.theWorld.addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(),
				packetIn.getData1(), packetIn.getData2());
	}

	/**+
	 * Updates all registered IWorldAccess instances with
	 * destroyBlockInWorldPartially
	 */
	public void handleBlockBreakAnim(S25PacketBlockBreakAnim packetIn) {
		this.gameController.theWorld.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(),
				packetIn.getProgress());
	}

	public void handleMapChunkBulk(S26PacketMapChunkBulk packetIn) {
		for (int i = 0; i < packetIn.getChunkCount(); ++i) {
			int j = packetIn.getChunkX(i);
			int k = packetIn.getChunkZ(i);
			this.clientWorldController.doPreChunk(j, k, true);
			this.clientWorldController.invalidateBlockReceiveRegion(j << 4, 0, k << 4, (j << 4) + 15, 256,
					(k << 4) + 15);
			Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(j, k);
			chunk.fillChunk(packetIn.getChunkBytes(i), packetIn.getChunkSize(i), true);
			this.clientWorldController.markBlockRangeForRenderUpdate(j << 4, 0, k << 4, (j << 4) + 15, 256,
					(k << 4) + 15);
			if (!(this.clientWorldController.provider instanceof WorldProviderSurface)) {
				chunk.resetRelightChecks();
			}
		}

	}

	public void handleChangeGameState(S2BPacketChangeGameState packetIn) {
		EntityPlayerSP entityplayersp = this.gameController.thePlayer;
		int i = packetIn.getGameState();
		float f = packetIn.func_149137_d();
		int j = MathHelper.floor_float(f + 0.5F);
		if (i >= 0 && i < S2BPacketChangeGameState.MESSAGE_NAMES.length
				&& S2BPacketChangeGameState.MESSAGE_NAMES[i] != null) {
			entityplayersp.addChatComponentMessage(
					new ChatComponentTranslation(S2BPacketChangeGameState.MESSAGE_NAMES[i], new Object[0]));
		}

		if (i == 1) {
			this.clientWorldController.getWorldInfo().setRaining(true);
			this.clientWorldController.setRainStrength(0.0F);
		} else if (i == 2) {
			this.clientWorldController.getWorldInfo().setRaining(false);
			this.clientWorldController.setRainStrength(1.0F);
		} else if (i == 3) {
			this.gameController.playerController.setGameType(WorldSettings.GameType.getByID(j));
		} else if (i == 4) {
			this.gameController.displayGuiScreen(new GuiWinGame());
		} else if (i == 5) {

			// minecraft demo screen

		} else if (i == 6) {
			this.clientWorldController.playSound(entityplayersp.posX,
					entityplayersp.posY + (double) entityplayersp.getEyeHeight(), entityplayersp.posZ,
					"random.successful_hit", 0.18F, 0.45F, false);
		} else if (i == 7) {
			this.clientWorldController.setRainStrength(f);
		} else if (i == 8) {
			this.clientWorldController.setThunderStrength(f);
		} else if (i == 10) {
			this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityplayersp.posX,
					entityplayersp.posY, entityplayersp.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
			this.clientWorldController.playSound(entityplayersp.posX, entityplayersp.posY, entityplayersp.posZ,
					"mob.guardian.curse", 1.0F, 1.0F, false);
		}

	}

	/**+
	 * Updates the worlds MapStorage with the specified MapData for
	 * the specified map-identifier and invokes a MapItemRenderer
	 * for it
	 */
	public void handleMaps(S34PacketMaps packetIn) {
		MapData mapdata = ItemMap.loadMapData(packetIn.getMapId(), this.gameController.theWorld);
		packetIn.setMapdataTo(mapdata);
		this.gameController.entityRenderer.getMapItemRenderer().updateMapTexture(mapdata);
	}

	public void handleEffect(S28PacketEffect packetIn) {
		if (packetIn.isSoundServerwide()) {
			this.gameController.theWorld.playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(),
					packetIn.getSoundData());
		} else {
			this.gameController.theWorld.playAuxSFX(packetIn.getSoundType(), packetIn.getSoundPos(),
					packetIn.getSoundData());
		}

	}

	/**+
	 * Updates the players statistics or achievements
	 */
	public void handleStatistics(S37PacketStatistics packetIn) {
		boolean flag = false;

		for (ObjectIntCursor<StatBase> entry : packetIn.func_148974_c()) {
			StatBase statbase = entry.key;
			int i = entry.value;
			if (statbase.isAchievement() && i > 0) {
				if (this.field_147308_k && this.gameController.thePlayer.getStatFileWriter().readStat(statbase) == 0) {
					Achievement achievement = (Achievement) statbase;
					this.gameController.guiAchievement.displayAchievement(achievement);
					if (statbase == AchievementList.openInventory) {
						this.gameController.gameSettings.showInventoryAchievementHint = false;
						this.gameController.gameSettings.saveOptions();
					}
				}

				flag = true;
			}

			this.gameController.thePlayer.getStatFileWriter().unlockAchievement(this.gameController.thePlayer, statbase,
					i);
		}

		if (!this.field_147308_k && !flag && this.gameController.gameSettings.showInventoryAchievementHint) {
			this.gameController.guiAchievement.displayUnformattedAchievement(AchievementList.openInventory);
		}

		this.field_147308_k = true;
		if (this.gameController.currentScreen instanceof IProgressMeter) {
			((IProgressMeter) this.gameController.currentScreen).doneLoading();
		}

	}

	public void handleEntityEffect(S1DPacketEntityEffect packetIn) {
		Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
		if (entity instanceof EntityLivingBase) {
			PotionEffect potioneffect = new PotionEffect(packetIn.getEffectId(), packetIn.getDuration(),
					packetIn.getAmplifier(), false, packetIn.func_179707_f());
			potioneffect.setPotionDurationMax(packetIn.func_149429_c());
			((EntityLivingBase) entity).addPotionEffect(potioneffect);
		}
	}

	public void handleCombatEvent(S42PacketCombatEvent packetIn) {

		// used by twitch stream

	}

	public void handleServerDifficulty(S41PacketServerDifficulty packetIn) {
		this.gameController.theWorld.getWorldInfo().setDifficulty(packetIn.getDifficulty());
		this.gameController.theWorld.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
	}

	public void handleCamera(S43PacketCamera packetIn) {

		Entity entity = packetIn.getEntity(this.clientWorldController);
		if (entity != null) {
			this.gameController.setRenderViewEntity(entity);
		}

	}

	public void handleWorldBorder(S44PacketWorldBorder packetIn) {
		packetIn.func_179788_a(this.clientWorldController.getWorldBorder());
	}

	public void handleTitle(S45PacketTitle packetIn) {
		S45PacketTitle.Type s45packettitle$type = packetIn.getType();
		String s = null;
		String s1 = null;
		String s2 = packetIn.getMessage() != null ? packetIn.getMessage().getFormattedText() : "";
		switch (s45packettitle$type) {
		case TITLE:
			s = s2;
			break;
		case SUBTITLE:
			s1 = s2;
			break;
		case RESET:
			this.gameController.ingameGUI.displayTitle("", "", -1, -1, -1);
			this.gameController.ingameGUI.func_175177_a();
			return;
		}

		this.gameController.ingameGUI.displayTitle(s, s1, packetIn.getFadeInTime(), packetIn.getDisplayTime(),
				packetIn.getFadeOutTime());
	}

	public void handleSetCompressionLevel(S46PacketSetCompressionLevel packetIn) {
		if (!this.netManager.isLocalChannel()) {
			this.netManager.setCompressionTreshold(packetIn.func_179760_a());
		}

	}

	public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter packetIn) {
		this.gameController.ingameGUI.getTabList()
				.setHeader(packetIn.getHeader().getFormattedText().length() == 0 ? null : packetIn.getHeader());
		this.gameController.ingameGUI.getTabList()
				.setFooter(packetIn.getFooter().getFormattedText().length() == 0 ? null : packetIn.getFooter());
	}

	public void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect packetIn) {
		Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
		if (entity instanceof EntityLivingBase) {
			((EntityLivingBase) entity).removePotionEffectClient(packetIn.getEffectId());
		}

	}

	public void handlePlayerListItem(S38PacketPlayerListItem packetIn) {
		List<AddPlayerData> lst = packetIn.func_179767_a();
		for (int i = 0, l = lst.size(); i < l; ++i) {
			S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata = lst.get(i);
			if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
				EaglercraftUUID uuid = s38packetplayerlistitem$addplayerdata.getProfile().getId();
				this.playerInfoMap.remove(uuid);
				this.textureCache.evictPlayer(uuid);
				ClientUUIDLoadingCache.evict(uuid);
			} else {
				NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo) this.playerInfoMap
						.get(s38packetplayerlistitem$addplayerdata.getProfile().getId());
				if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
					networkplayerinfo = new NetworkPlayerInfo(s38packetplayerlistitem$addplayerdata);
					this.playerInfoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
				}

				if (networkplayerinfo != null) {
					switch (packetIn.func_179768_b()) {
					case ADD_PLAYER:
						networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
						networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
						break;
					case UPDATE_GAME_MODE:
						networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
						break;
					case UPDATE_LATENCY:
						networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
						break;
					case UPDATE_DISPLAY_NAME:
						networkplayerinfo.setDisplayName(s38packetplayerlistitem$addplayerdata.getDisplayName());
					}
				}
			}
		}

	}

	public void handleKeepAlive(S00PacketKeepAlive packetIn) {
		this.addToSendQueue(new C00PacketKeepAlive(packetIn.func_149134_c()));
	}

	public void handlePlayerAbilities(S39PacketPlayerAbilities packetIn) {
		EntityPlayerSP entityplayersp = this.gameController.thePlayer;
		entityplayersp.capabilities.isFlying = packetIn.isFlying();
		entityplayersp.capabilities.isCreativeMode = packetIn.isCreativeMode();
		entityplayersp.capabilities.disableDamage = packetIn.isInvulnerable();
		entityplayersp.capabilities.allowFlying = packetIn.isAllowFlying();
		entityplayersp.capabilities.setFlySpeed(packetIn.getFlySpeed());
		entityplayersp.capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
	}

	/**+
	 * Displays the available command-completion options the server
	 * knows of
	 */
	public void handleTabComplete(S3APacketTabComplete packetIn) {
		String[] astring = packetIn.func_149630_c();
		if (this.gameController.currentScreen instanceof GuiChat) {
			GuiChat guichat = (GuiChat) this.gameController.currentScreen;
			guichat.onAutocompleteResponse(astring);
		}

	}

	public void handleSoundEffect(S29PacketSoundEffect packetIn) {
		this.gameController.theWorld.playSound(packetIn.getX(), packetIn.getY(), packetIn.getZ(),
				packetIn.getSoundName(), packetIn.getVolume(), packetIn.getPitch(), false);
	}

	public void handleResourcePack(S48PacketResourcePackSend packetIn) {
		final String s = packetIn.getURL();
		final String s1 = packetIn.getHash();
		if (!EaglerFolderResourcePack.isSupported() || s.startsWith("level://")) {
			this.netManager
					.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
			return;
		}
		if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData()
				.getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
			NetHandlerPlayClient.this.netManager
					.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
			NetHandlerPlayClient.this.gameController.getResourcePackRepository().downloadResourcePack(s, s1,
					success -> {
						if (success) {
							NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1,
									C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
						} else {
							NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1,
									C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
						}
					});
		} else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData()
				.getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
			this.netManager
					.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
		} else {
			NetHandlerPlayClient.this.gameController.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
				public void confirmClicked(boolean flag, int var2) {
					NetHandlerPlayClient.this.gameController = Minecraft.getMinecraft();
					if (flag) {
						if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
							NetHandlerPlayClient.this.gameController.getCurrentServerData()
									.setResourceMode(ServerData.ServerResourceMode.ENABLED);
						}

						NetHandlerPlayClient.this.netManager.sendPacket(
								new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
						NetHandlerPlayClient.this.gameController.getResourcePackRepository().downloadResourcePack(s, s1,
								success -> {
									if (success) {
										NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(
												s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
									} else {
										NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(
												s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
									}
								});
					} else {
						if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
							NetHandlerPlayClient.this.gameController.getCurrentServerData()
									.setResourceMode(ServerData.ServerResourceMode.DISABLED);
						}

						NetHandlerPlayClient.this.netManager.sendPacket(
								new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
					}

					ServerList.func_147414_b(NetHandlerPlayClient.this.gameController.getCurrentServerData());
					NetHandlerPlayClient.this.gameController.displayGuiScreen((GuiScreen) null);
				}
			}, I18n.format("multiplayer.texturePrompt.line1", new Object[0]),
					I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));
		}
	}

	public void handleEntityNBT(S49PacketUpdateEntityNBT packetIn) {
		Entity entity = packetIn.getEntity(this.clientWorldController);
		if (entity != null) {
			entity.clientUpdateEntityNBT(packetIn.getTagCompound());
		}

	}

	/**+
	 * Handles packets that have room for a channel specification.
	 * Vanilla implemented channels are "MC|TrList" to acquire a
	 * MerchantRecipeList trades for a villager merchant, "MC|Brand"
	 * which sets the server brand? on the player instance and
	 * finally "MC|RPack" which the server uses to communicate the
	 * identifier of the default server resourcepack for the client
	 * to load.
	 */
	public void handleCustomPayload(S3FPacketCustomPayload packetIn) {
		if ("MC|TrList".equals(packetIn.getChannelName())) {
			PacketBuffer packetbuffer = packetIn.getBufferData();
			try {
				int i = packetbuffer.readInt();
				GuiScreen guiscreen = this.gameController.currentScreen;
				if (guiscreen != null && guiscreen instanceof GuiMerchant
						&& i == this.gameController.thePlayer.openContainer.windowId) {
					IMerchant imerchant = ((GuiMerchant) guiscreen).getMerchant();
					MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(packetbuffer);
					imerchant.setRecipes(merchantrecipelist);
				}
			} catch (IOException ioexception) {
				logger.error("Couldn\'t load trade info", ioexception);
			}
		} else if ("MC|Brand".equals(packetIn.getChannelName())) {
			this.gameController.thePlayer.setClientBrand(packetIn.getBufferData().readStringFromBuffer(32767));
		} else if ("MC|BOpen".equals(packetIn.getChannelName())) {
			ItemStack itemstack = this.gameController.thePlayer.getCurrentEquippedItem();
			if (itemstack != null && itemstack.getItem() == Items.written_book) {
				this.gameController
						.displayGuiScreen(new GuiScreenBook(this.gameController.thePlayer, itemstack, false));
			}
		} else if (eaglerMessageController instanceof LegacyMessageController) {
			try {
				((LegacyMessageController) eaglerMessageController).handlePacket(packetIn.getChannelName(),
						packetIn.getBufferData());
			} catch (IOException e) {
				logger.error("Couldn't read \"{}\" packet as an eaglercraft plugin message!",
						packetIn.getChannelName());
				logger.error(e);
			}
		}
	}

	/**+
	 * May create a scoreboard objective, remove an objective from
	 * the scoreboard or update an objectives' displayname
	 */
	public void handleScoreboardObjective(S3BPacketScoreboardObjective packetIn) {
		Scoreboard scoreboard = this.clientWorldController.getScoreboard();
		if (packetIn.func_149338_e() == 0) {
			ScoreObjective scoreobjective = scoreboard.addScoreObjective(packetIn.func_149339_c(),
					IScoreObjectiveCriteria.DUMMY);
			scoreobjective.setDisplayName(packetIn.func_149337_d());
			scoreobjective.setRenderType(packetIn.func_179817_d());
		} else {
			ScoreObjective scoreobjective1 = scoreboard.getObjective(packetIn.func_149339_c());
			if (packetIn.func_149338_e() == 1) {
				scoreboard.removeObjective(scoreobjective1);
			} else if (packetIn.func_149338_e() == 2) {
				scoreobjective1.setDisplayName(packetIn.func_149337_d());
				scoreobjective1.setRenderType(packetIn.func_179817_d());
			}
		}

	}

	/**+
	 * Either updates the score with a specified value or removes
	 * the score for an objective
	 */
	public void handleUpdateScore(S3CPacketUpdateScore packetIn) {
		Scoreboard scoreboard = this.clientWorldController.getScoreboard();
		ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getObjectiveName());
		if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.CHANGE) {
			Score score = scoreboard.getValueFromObjective(packetIn.getPlayerName(), scoreobjective);
			score.setScorePoints(packetIn.getScoreValue());
		} else if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.REMOVE) {
			if (StringUtils.isNullOrEmpty(packetIn.getObjectiveName())) {
				scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), (ScoreObjective) null);
			} else if (scoreobjective != null) {
				scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), scoreobjective);
			}
		}

	}

	/**+
	 * Removes or sets the ScoreObjective to be displayed at a
	 * particular scoreboard position (list, sidebar, below name)
	 */
	public void handleDisplayScoreboard(S3DPacketDisplayScoreboard packetIn) {
		Scoreboard scoreboard = this.clientWorldController.getScoreboard();
		if (packetIn.func_149370_d().length() == 0) {
			scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), (ScoreObjective) null);
		} else {
			ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.func_149370_d());
			scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), scoreobjective);
		}

	}

	/**+
	 * Updates a team managed by the scoreboard: Create/Remove the
	 * team registration, Register/Remove the
	 * player-team-memberships, Set team displayname/prefix/suffix
	 * and/or whether friendly fire is enabled
	 */
	public void handleTeams(S3EPacketTeams packetIn) {
		Scoreboard scoreboard = this.clientWorldController.getScoreboard();
		ScorePlayerTeam scoreplayerteam;
		if (packetIn.func_149307_h() == 0) {
			scoreplayerteam = scoreboard.createTeam(packetIn.func_149312_c());
		} else {
			scoreplayerteam = scoreboard.getTeam(packetIn.func_149312_c());
		}

		if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 2) {
			scoreplayerteam.setTeamName(packetIn.func_149306_d());
			scoreplayerteam.setNamePrefix(packetIn.func_149311_e());
			scoreplayerteam.setNameSuffix(packetIn.func_149309_f());
			scoreplayerteam.setChatFormat(EnumChatFormatting.func_175744_a(packetIn.func_179813_h()));
			scoreplayerteam.func_98298_a(packetIn.func_149308_i());
			Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a(packetIn.func_179814_i());
			if (team$enumvisible != null) {
				scoreplayerteam.setNameTagVisibility(team$enumvisible);
			}
		}

		if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 3) {
			for (String s : packetIn.func_149310_g()) {
				scoreboard.addPlayerToTeam(s, packetIn.func_149312_c());
			}
		}

		if (packetIn.func_149307_h() == 4) {
			for (String s1 : packetIn.func_149310_g()) {
				scoreboard.removePlayerFromTeam(s1, scoreplayerteam);
			}
		}

		if (packetIn.func_149307_h() == 1) {
			scoreboard.removeTeam(scoreplayerteam);
		}

	}

	/**+
	 * Spawns a specified number of particles at the specified
	 * location with a randomized displacement according to
	 * specified bounds
	 */
	public void handleParticles(S2APacketParticles packetIn) {
		if (packetIn.getParticleCount() == 0) {
			double d0 = (double) (packetIn.getParticleSpeed() * packetIn.getXOffset());
			double d2 = (double) (packetIn.getParticleSpeed() * packetIn.getYOffset());
			double d4 = (double) (packetIn.getParticleSpeed() * packetIn.getZOffset());

			try {
				this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(),
						packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d4,
						packetIn.getParticleArgs());
			} catch (Throwable var17) {
				logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
			}
		} else {
			for (int i = 0; i < packetIn.getParticleCount(); ++i) {
				double d1 = this.avRandomizer.nextGaussian() * (double) packetIn.getXOffset();
				double d3 = this.avRandomizer.nextGaussian() * (double) packetIn.getYOffset();
				double d5 = this.avRandomizer.nextGaussian() * (double) packetIn.getZOffset();
				double d6 = this.avRandomizer.nextGaussian() * (double) packetIn.getParticleSpeed();
				double d7 = this.avRandomizer.nextGaussian() * (double) packetIn.getParticleSpeed();
				double d8 = this.avRandomizer.nextGaussian() * (double) packetIn.getParticleSpeed();

				try {
					this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(),
							packetIn.getXCoordinate() + d1, packetIn.getYCoordinate() + d3,
							packetIn.getZCoordinate() + d5, d6, d7, d8, packetIn.getParticleArgs());
				} catch (Throwable var16) {
					logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
					return;
				}
			}
		}

	}

	/**+
	 * Updates en entity's attributes and their respective
	 * modifiers, which are used for speed bonusses (player
	 * sprinting, animals fleeing, baby speed), weapon/tool
	 * attackDamage, hostiles followRange randomization, zombie
	 * maxHealth and knockback resistance as well as reinforcement
	 * spawning chance.
	 */
	public void handleEntityProperties(S20PacketEntityProperties packetIn) {
		Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
		if (entity != null) {
			if (!(entity instanceof EntityLivingBase)) {
				throw new IllegalStateException(
						"Server tried to update attributes of a non-living entity (actually: " + entity + ")");
			} else {
				BaseAttributeMap baseattributemap = ((EntityLivingBase) entity).getAttributeMap();

				List<Snapshot> lst = packetIn.func_149441_d();
				for (int i = 0, l = lst.size(); i < l; ++i) {
					S20PacketEntityProperties.Snapshot s20packetentityproperties$snapshot = lst.get(i);
					IAttributeInstance iattributeinstance = baseattributemap
							.getAttributeInstanceByName(s20packetentityproperties$snapshot.func_151409_a());
					if (iattributeinstance == null) {
						iattributeinstance = baseattributemap.registerAttribute(new RangedAttribute((IAttribute) null,
								s20packetentityproperties$snapshot.func_151409_a(), 0.0D, 2.2250738585072014E-308D,
								Double.MAX_VALUE));
					}

					iattributeinstance.setBaseValue(s20packetentityproperties$snapshot.func_151410_b());
					iattributeinstance.removeAllModifiers();

					for (AttributeModifier attributemodifier : s20packetentityproperties$snapshot.func_151408_c()) {
						iattributeinstance.applyModifier(attributemodifier);
					}
				}

			}
		}
	}

	/**+
	 * Returns this the NetworkManager instance registered with this
	 * NetworkHandlerPlayClient
	 */
	public EaglercraftNetworkManager getNetworkManager() {
		return this.netManager;
	}

	public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
		return this.playerInfoMap.values();
	}

	/**+
	 * Gets the client's description information about another
	 * player on the server.
	 */
	public NetworkPlayerInfo getPlayerInfo(EaglercraftUUID parUUID) {
		return (NetworkPlayerInfo) this.playerInfoMap.get(parUUID);
	}

	/**+
	 * Gets the client's description information about another
	 * player on the server.
	 */
	public NetworkPlayerInfo getPlayerInfo(String parString1) {
		for (NetworkPlayerInfo networkplayerinfo : this.playerInfoMap.values()) {
			if (networkplayerinfo.getGameProfile().getName().equals(parString1)) {
				return networkplayerinfo;
			}
		}

		return null;
	}

	public GameProfile getGameProfile() {
		return this.profile;
	}

	public boolean isClientInEaglerSingleplayerOrLAN() {
		return isIntegratedServer;
	}
}