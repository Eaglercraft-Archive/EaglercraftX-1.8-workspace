package net.minecraft.network.play.server;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.Property;
import java.io.IOException;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

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
public class S38PacketPlayerListItem implements Packet<INetHandlerPlayClient> {
	private S38PacketPlayerListItem.Action action;
	private final List<S38PacketPlayerListItem.AddPlayerData> players = Lists.newArrayList();

	public S38PacketPlayerListItem() {
	}

	public S38PacketPlayerListItem(S38PacketPlayerListItem.Action actionIn, EntityPlayerMP... players) {
		this.action = actionIn;

		for (int i = 0; i < players.length; ++i) {
			EntityPlayerMP entityplayermp = players[i];
			this.players.add(new S38PacketPlayerListItem.AddPlayerData(entityplayermp.getGameProfile(),
					entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(),
					entityplayermp.getTabListDisplayName()));
		}

	}

	public S38PacketPlayerListItem(S38PacketPlayerListItem.Action actionIn, Iterable<EntityPlayerMP> players) {
		this.action = actionIn;

		for (EntityPlayerMP entityplayermp : players) {
			this.players.add(new S38PacketPlayerListItem.AddPlayerData(entityplayermp.getGameProfile(),
					entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(),
					entityplayermp.getTabListDisplayName()));
		}

	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.action = (S38PacketPlayerListItem.Action) parPacketBuffer
				.readEnumValue(S38PacketPlayerListItem.Action.class);
		int i = parPacketBuffer.readVarIntFromBuffer();

		for (int j = 0; j < i; ++j) {
			GameProfile gameprofile = null;
			int k = 0;
			WorldSettings.GameType worldsettings$gametype = null;
			IChatComponent ichatcomponent = null;
			switch (this.action) {
			case ADD_PLAYER:
				gameprofile = new GameProfile(parPacketBuffer.readUuid(), parPacketBuffer.readStringFromBuffer(16));
				int l = parPacketBuffer.readVarIntFromBuffer();
				int i1 = 0;

				for (; i1 < l; ++i1) {
					String s = parPacketBuffer.readStringFromBuffer(32767);
					String s1 = parPacketBuffer.readStringFromBuffer(32767);
					if (parPacketBuffer.readBoolean()) {
						gameprofile.getProperties().put(s,
								new Property(s, s1, parPacketBuffer.readStringFromBuffer(32767)));
					} else {
						gameprofile.getProperties().put(s, new Property(s, s1));
					}
				}

				worldsettings$gametype = WorldSettings.GameType.getByID(parPacketBuffer.readVarIntFromBuffer());
				k = parPacketBuffer.readVarIntFromBuffer();
				if (parPacketBuffer.readBoolean()) {
					ichatcomponent = parPacketBuffer.readChatComponent();
				}
				break;
			case UPDATE_GAME_MODE:
				gameprofile = new GameProfile(parPacketBuffer.readUuid(), (String) null);
				worldsettings$gametype = WorldSettings.GameType.getByID(parPacketBuffer.readVarIntFromBuffer());
				break;
			case UPDATE_LATENCY:
				gameprofile = new GameProfile(parPacketBuffer.readUuid(), (String) null);
				k = parPacketBuffer.readVarIntFromBuffer();
				break;
			case UPDATE_DISPLAY_NAME:
				gameprofile = new GameProfile(parPacketBuffer.readUuid(), (String) null);
				if (parPacketBuffer.readBoolean()) {
					ichatcomponent = parPacketBuffer.readChatComponent();
				}
				break;
			case REMOVE_PLAYER:
				gameprofile = new GameProfile(parPacketBuffer.readUuid(), (String) null);
			}

			this.players.add(
					new S38PacketPlayerListItem.AddPlayerData(gameprofile, k, worldsettings$gametype, ichatcomponent));
		}

	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeEnumValue(this.action);
		parPacketBuffer.writeVarIntToBuffer(this.players.size());

		for (int i = 0, l = this.players.size(); i < l; ++i) {
			S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata = this.players.get(i);
			switch (this.action) {
			case ADD_PLAYER:
				parPacketBuffer.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
				parPacketBuffer.writeString(s38packetplayerlistitem$addplayerdata.getProfile().getName());
				parPacketBuffer
						.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getProfile().getProperties().size());

				for (Property property : s38packetplayerlistitem$addplayerdata.getProfile().getProperties().values()) {
					parPacketBuffer.writeString(property.getName());
					parPacketBuffer.writeString(property.getValue());
					if (property.hasSignature()) {
						parPacketBuffer.writeBoolean(true);
						parPacketBuffer.writeString(property.getSignature());
					} else {
						parPacketBuffer.writeBoolean(false);
					}
				}

				parPacketBuffer.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getGameMode().getID());
				parPacketBuffer.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getPing());
				if (s38packetplayerlistitem$addplayerdata.getDisplayName() == null) {
					parPacketBuffer.writeBoolean(false);
				} else {
					parPacketBuffer.writeBoolean(true);
					parPacketBuffer.writeChatComponent(s38packetplayerlistitem$addplayerdata.getDisplayName());
				}
				break;
			case UPDATE_GAME_MODE:
				parPacketBuffer.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
				parPacketBuffer.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getGameMode().getID());
				break;
			case UPDATE_LATENCY:
				parPacketBuffer.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
				parPacketBuffer.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getPing());
				break;
			case UPDATE_DISPLAY_NAME:
				parPacketBuffer.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
				if (s38packetplayerlistitem$addplayerdata.getDisplayName() == null) {
					parPacketBuffer.writeBoolean(false);
				} else {
					parPacketBuffer.writeBoolean(true);
					parPacketBuffer.writeChatComponent(s38packetplayerlistitem$addplayerdata.getDisplayName());
				}
				break;
			case REMOVE_PLAYER:
				parPacketBuffer.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
			}
		}

	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handlePlayerListItem(this);
	}

	public List<S38PacketPlayerListItem.AddPlayerData> func_179767_a() {
		return this.players;
	}

	public S38PacketPlayerListItem.Action func_179768_b() {
		return this.action;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("action", this.action).add("entries", this.players).toString();
	}

	public static enum Action {
		ADD_PLAYER, UPDATE_GAME_MODE, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, REMOVE_PLAYER;
	}

	public class AddPlayerData {
		private final int ping;
		private final WorldSettings.GameType gamemode;
		private final GameProfile profile;
		private final IChatComponent displayName;

		public AddPlayerData(GameProfile profile, int pingIn, WorldSettings.GameType gamemodeIn,
				IChatComponent displayNameIn) {
			this.profile = profile;
			this.ping = pingIn;
			this.gamemode = gamemodeIn;
			this.displayName = displayNameIn;
		}

		public GameProfile getProfile() {
			return this.profile;
		}

		public int getPing() {
			return this.ping;
		}

		public WorldSettings.GameType getGameMode() {
			return this.gamemode;
		}

		public IChatComponent getDisplayName() {
			return this.displayName;
		}

		public String toString() {
			return Objects.toStringHelper(this).add("latency", this.ping).add("gameMode", this.gamemode)
					.add("profile", this.profile).add("displayName", this.displayName == null ? null
							: IChatComponent.Serializer.componentToJson(this.displayName))
					.toString();
		}
	}
}