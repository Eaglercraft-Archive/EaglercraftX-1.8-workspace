package net.minecraft.server.network;

import com.google.common.base.Charsets;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.ClientUUIDLoadingCache;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.lax1dude.eaglercraft.v1_8.sp.server.socket.IntegratedServerPlayerNetworkManager;
import net.lax1dude.eaglercraft.v1_8.sp.server.voice.IntegratedVoiceService;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.commons.lang3.Validate;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class NetHandlerLoginServer implements INetHandlerLoginServer, ITickable {
	private static final Logger logger = LogManager.getLogger();
	private final MinecraftServer server;
	public final IntegratedServerPlayerNetworkManager networkManager;
	private NetHandlerLoginServer.LoginState currentLoginState = NetHandlerLoginServer.LoginState.HELLO;
	private int connectionTimer;
	private GameProfile loginGameProfile;
	private byte[] loginSkinPacket;
	private byte[] loginCapePacket;
	private int selectedProtocol = 3;
	private EaglercraftUUID clientBrandUUID;
	private String serverId = "";
	private EntityPlayerMP field_181025_l;

	public NetHandlerLoginServer(MinecraftServer parMinecraftServer,
			IntegratedServerPlayerNetworkManager parNetworkManager) {
		this.server = parMinecraftServer;
		this.networkManager = parNetworkManager;
	}

	/**+
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		if (this.currentLoginState == NetHandlerLoginServer.LoginState.READY_TO_ACCEPT) {
			this.tryAcceptPlayer();
		} else if (this.currentLoginState == NetHandlerLoginServer.LoginState.DELAY_ACCEPT) {
			EntityPlayerMP entityplayermp = this.server.getConfigurationManager()
					.getPlayerByUUID(this.loginGameProfile.getId());
			if (entityplayermp == null) {
				this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
				this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager,
						this.field_181025_l, this.selectedProtocol, this.clientBrandUUID);
				((EaglerMinecraftServer) field_181025_l.mcServer).getSkinService()
						.processLoginPacket(this.loginSkinPacket, field_181025_l, 3); // singleplayer always sends V3
																						// skin in handshake
				if (this.loginCapePacket != null) {
					((EaglerMinecraftServer) field_181025_l.mcServer).getCapeService()
							.processLoginPacket(this.loginCapePacket, field_181025_l);
				}
				IntegratedVoiceService svc = ((EaglerMinecraftServer) field_181025_l.mcServer).getVoiceService();
				if (svc != null) {
					svc.handlePlayerLoggedIn(entityplayermp);
				}
				this.field_181025_l = null;
			}
		}

		if (this.connectionTimer++ == 600) {
			this.closeConnection("Took too long to log in");
		}

	}

	public void closeConnection(String reason) {
		try {
			logger.info("Disconnecting " + this.getConnectionInfo() + ": " + reason);
			ChatComponentText chatcomponenttext = new ChatComponentText(reason);
			this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext));
			this.networkManager.closeChannel(chatcomponenttext);
		} catch (Exception exception) {
			logger.error("Error whilst disconnecting player", exception);
		}

	}

	public void tryAcceptPlayer() {
		String s = this.server.getConfigurationManager().allowUserToConnect(this.loginGameProfile);
		if (s != null) {
			this.closeConnection(s);
		} else {
			this.currentLoginState = NetHandlerLoginServer.LoginState.ACCEPTED;
			this.networkManager.sendPacket(new S02PacketLoginSuccess(this.loginGameProfile, this.selectedProtocol));
			this.networkManager.setConnectionState(EnumConnectionState.PLAY);
			EntityPlayerMP entityplayermp = this.server.getConfigurationManager()
					.getPlayerByUUID(this.loginGameProfile.getId());
			if (entityplayermp != null) {
				this.currentLoginState = NetHandlerLoginServer.LoginState.DELAY_ACCEPT;
				this.field_181025_l = this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile);
			} else {
				entityplayermp = this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile);
				this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, entityplayermp,
						this.selectedProtocol, this.clientBrandUUID);
				((EaglerMinecraftServer) entityplayermp.mcServer).getSkinService()
						.processLoginPacket(this.loginSkinPacket, entityplayermp, 3); // singleplayer always sends V3
																						// skin in handshake
				if (this.loginCapePacket != null) {
					((EaglerMinecraftServer) entityplayermp.mcServer).getCapeService()
							.processLoginPacket(this.loginCapePacket, entityplayermp);
				}
				IntegratedVoiceService svc = ((EaglerMinecraftServer) entityplayermp.mcServer).getVoiceService();
				if (svc != null) {
					svc.handlePlayerLoggedIn(entityplayermp);
				}
			}
		}

	}

	/**+
	 * Invoked when disconnecting, the parameter is a ChatComponent
	 * describing the reason for termination
	 */
	public void onDisconnect(IChatComponent ichatcomponent) {
		logger.info(this.getConnectionInfo() + " lost connection: " + ichatcomponent.getUnformattedText());
	}

	public String getConnectionInfo() {
		return this.loginGameProfile != null
				? this.loginGameProfile.toString() + " (channel:" + this.networkManager.playerChannel + ")"
				: ("channel:" + this.networkManager.playerChannel);
	}

	public void processLoginStart(C00PacketLoginStart c00packetloginstart) {
		Validate.validState(this.currentLoginState == NetHandlerLoginServer.LoginState.HELLO, "Unexpected hello packet",
				new Object[0]);
		if (c00packetloginstart.getProtocols() != null) {
			try {
				DataInputStream dis = new DataInputStream(new EaglerInputStream(c00packetloginstart.getProtocols()));
				int maxSupported = -1;
				int protocolCount = dis.readUnsignedShort();
				for (int i = 0; i < protocolCount; ++i) {
					int p = dis.readUnsignedShort();
					if ((p == 3 || p == 4) && p > maxSupported) {
						maxSupported = p;
					}
				}
				if (maxSupported != -1) {
					selectedProtocol = maxSupported;
				} else {
					this.closeConnection("Unknown protocol!");
					return;
				}
			} catch (IOException ex) {
				selectedProtocol = 3;
			}
		} else {
			selectedProtocol = 3;
		}
		this.loginGameProfile = this.getOfflineProfile(c00packetloginstart.getProfile());
		this.loginSkinPacket = c00packetloginstart.getSkin();
		this.loginCapePacket = c00packetloginstart.getCape();
		this.clientBrandUUID = selectedProtocol <= 3 ? EaglercraftVersion.legacyClientUUIDInSharedWorld
				: c00packetloginstart.getBrandUUID();
		if (ClientUUIDLoadingCache.PENDING_UUID.equals(clientBrandUUID)
				|| ClientUUIDLoadingCache.VANILLA_UUID.equals(clientBrandUUID)) {
			this.clientBrandUUID = null;
		}
		this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
	}

	public void processEncryptionResponse(C01PacketEncryptionResponse c01packetencryptionresponse) {

	}

	protected GameProfile getOfflineProfile(GameProfile original) {
		EaglercraftUUID uuid = EaglercraftUUID
				.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(Charsets.UTF_8));
		return new GameProfile(uuid, original.getName());
	}

	static enum LoginState {
		HELLO, KEY, AUTHENTICATING, READY_TO_ACCEPT, DELAY_ACCEPT, ACCEPTED;
	}
}