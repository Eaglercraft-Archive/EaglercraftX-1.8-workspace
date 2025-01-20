package net.minecraft.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeCodec;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.minecraft.util.IChatComponent;

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
public class ServerStatusResponse {
	private IChatComponent serverMotd;
	private ServerStatusResponse.PlayerCountData playerCount;
	private ServerStatusResponse.MinecraftProtocolVersionIdentifier protocolVersion;
	private String favicon;

	public IChatComponent getServerDescription() {
		return this.serverMotd;
	}

	public void setServerDescription(IChatComponent motd) {
		this.serverMotd = motd;
	}

	public ServerStatusResponse.PlayerCountData getPlayerCountData() {
		return this.playerCount;
	}

	public void setPlayerCountData(ServerStatusResponse.PlayerCountData countData) {
		this.playerCount = countData;
	}

	public ServerStatusResponse.MinecraftProtocolVersionIdentifier getProtocolVersionInfo() {
		return this.protocolVersion;
	}

	public void setProtocolVersionInfo(ServerStatusResponse.MinecraftProtocolVersionIdentifier protocolVersionData) {
		this.protocolVersion = protocolVersionData;
	}

	public void setFavicon(String faviconBlob) {
		this.favicon = faviconBlob;
	}

	public String getFavicon() {
		return this.favicon;
	}

	public static class MinecraftProtocolVersionIdentifier {
		private final String name;
		private final int protocol;

		public MinecraftProtocolVersionIdentifier(String nameIn, int protocolIn) {
			this.name = nameIn;
			this.protocol = protocolIn;
		}

		public String getName() {
			return this.name;
		}

		public int getProtocol() {
			return this.protocol;
		}

		public static class Serializer
				implements JSONTypeCodec<ServerStatusResponse.MinecraftProtocolVersionIdentifier, JSONObject> {
			public ServerStatusResponse.MinecraftProtocolVersionIdentifier deserialize(JSONObject jsonobject)
					throws JSONException {
				return new ServerStatusResponse.MinecraftProtocolVersionIdentifier(jsonobject.getString("name"),
						jsonobject.getInt("protocol"));
			}

			public JSONObject serialize(
					ServerStatusResponse.MinecraftProtocolVersionIdentifier serverstatusresponse$minecraftprotocolversionidentifier) {
				JSONObject jsonobject = new JSONObject();
				jsonobject.put("name", serverstatusresponse$minecraftprotocolversionidentifier.getName());
				jsonobject.put("protocol",
						Integer.valueOf(serverstatusresponse$minecraftprotocolversionidentifier.getProtocol()));
				return jsonobject;
			}
		}
	}

	public static class PlayerCountData {
		private final int maxPlayers;
		private final int onlinePlayerCount;
		private GameProfile[] players;

		public PlayerCountData(int maxOnlinePlayers, int onlinePlayers) {
			this.maxPlayers = maxOnlinePlayers;
			this.onlinePlayerCount = onlinePlayers;
		}

		public int getMaxPlayers() {
			return this.maxPlayers;
		}

		public int getOnlinePlayerCount() {
			return this.onlinePlayerCount;
		}

		public GameProfile[] getPlayers() {
			return this.players;
		}

		public void setPlayers(GameProfile[] playersIn) {
			this.players = playersIn;
		}

		public static class Serializer implements JSONTypeCodec<ServerStatusResponse.PlayerCountData, JSONObject> {
			public ServerStatusResponse.PlayerCountData deserialize(JSONObject jsonobject) throws JSONException {
				ServerStatusResponse.PlayerCountData serverstatusresponse$playercountdata = new ServerStatusResponse.PlayerCountData(
						jsonobject.getInt("max"), jsonobject.getInt("online"));
				JSONArray jsonarray = jsonobject.optJSONArray("sample");
				if (jsonarray != null) {
					if (jsonarray.length() > 0) {
						GameProfile[] agameprofile = new GameProfile[jsonarray.length()];

						for (int i = 0; i < agameprofile.length; ++i) {
							JSONObject jsonobject1 = jsonarray.getJSONObject(i);
							String s = jsonobject1.getString("id");
							agameprofile[i] = new GameProfile(EaglercraftUUID.fromString(s),
									jsonobject1.getString("name"));
						}

						serverstatusresponse$playercountdata.setPlayers(agameprofile);
					}
				}

				return serverstatusresponse$playercountdata;
			}

			public JSONObject serialize(ServerStatusResponse.PlayerCountData serverstatusresponse$playercountdata)
					throws JSONException {
				JSONObject jsonobject = new JSONObject();
				jsonobject.put("max", Integer.valueOf(serverstatusresponse$playercountdata.getMaxPlayers()));
				jsonobject.put("online", Integer.valueOf(serverstatusresponse$playercountdata.getOnlinePlayerCount()));
				if (serverstatusresponse$playercountdata.getPlayers() != null
						&& serverstatusresponse$playercountdata.getPlayers().length > 0) {
					JSONArray jsonarray = new JSONArray();

					for (int i = 0; i < serverstatusresponse$playercountdata.getPlayers().length; ++i) {
						JSONObject jsonobject1 = new JSONObject();
						EaglercraftUUID uuid = serverstatusresponse$playercountdata.getPlayers()[i].getId();
						jsonobject1.put("id", uuid == null ? "" : uuid.toString());
						jsonobject1.put("name", serverstatusresponse$playercountdata.getPlayers()[i].getName());
						jsonarray.put(jsonobject1);
					}

					jsonobject.put("sample", jsonarray);
				}

				return jsonobject;
			}
		}
	}

	public static class Serializer implements JSONTypeCodec<ServerStatusResponse, JSONObject> {
		public ServerStatusResponse deserialize(JSONObject jsonobject) throws JSONException {
			ServerStatusResponse serverstatusresponse = new ServerStatusResponse();
			if (jsonobject.has("description")) {
				serverstatusresponse.setServerDescription((IChatComponent) JSONTypeProvider
						.deserialize(jsonobject.get("description"), IChatComponent.class));
			}

			if (jsonobject.has("players")) {
				serverstatusresponse.setPlayerCountData((ServerStatusResponse.PlayerCountData) JSONTypeProvider
						.deserialize(jsonobject.get("players"), ServerStatusResponse.PlayerCountData.class));
			}

			if (jsonobject.has("version")) {
				serverstatusresponse.setProtocolVersionInfo(
						(ServerStatusResponse.MinecraftProtocolVersionIdentifier) JSONTypeProvider.deserialize(
								jsonobject.get("version"),
								ServerStatusResponse.MinecraftProtocolVersionIdentifier.class));
			}

			if (jsonobject.has("favicon")) {
				serverstatusresponse.setFavicon(jsonobject.getString("favicon"));
			}

			return serverstatusresponse;
		}

		public JSONObject serialize(ServerStatusResponse serverstatusresponse) {
			JSONObject jsonobject = new JSONObject();
			if (serverstatusresponse.getServerDescription() != null) {
				jsonobject.put("description",
						(Object) JSONTypeProvider.serialize(serverstatusresponse.getServerDescription()));
			}

			if (serverstatusresponse.getPlayerCountData() != null) {
				jsonobject.put("players",
						(Object) JSONTypeProvider.serialize(serverstatusresponse.getPlayerCountData()));
			}

			if (serverstatusresponse.getProtocolVersionInfo() != null) {
				jsonobject.put("version",
						(Object) JSONTypeProvider.serialize(serverstatusresponse.getProtocolVersionInfo()));
			}

			if (serverstatusresponse.getFavicon() != null) {
				jsonobject.put("favicon", serverstatusresponse.getFavicon());
			}

			return jsonobject;
		}
	}
}