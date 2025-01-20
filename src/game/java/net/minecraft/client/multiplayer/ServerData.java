package net.minecraft.client.multiplayer;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IServerQuery;
import net.lax1dude.eaglercraft.v1_8.internal.QueryResponse;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerSkinTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

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
public class ServerData {
	public String serverName;
	public String serverIP;
	/**+
	 * the string indicating number of players on and capacity of
	 * the server that is shown on the server browser (i.e. "5/20"
	 * meaning 5 slots used out of 20 slots total)
	 */
	public String populationInfo = "";
	/**+
	 * (better variable name would be 'hostname') server name as
	 * displayed in the server browser's second line (grey text)
	 */
	public String serverMOTD = "";
	/**+
	 * last server ping that showed up in the server browser
	 */
	public long pingToServer = -1l;
	public int version = 47;
	/**+
	 * Game version for this server.
	 */
	public String gameVersion = "1.8.8";
	public boolean field_78841_f;
	public String playerList;
	private ServerData.ServerResourceMode resourceMode = ServerData.ServerResourceMode.PROMPT;
	public boolean hideAddress = false;
	private boolean field_181042_l;
	public IServerQuery currentQuery = null;
	public final ResourceLocation iconResourceLocation;
	public EaglerSkinTexture iconTextureObject = null;
	public long pingSentTime = -1l;
	public boolean serverIconDirty = false;
	public boolean hasPing = false;
	public boolean serverIconEnabled = false;
	public boolean isDefault = false;
	public boolean enableCookies;

	private static final Logger logger = LogManager.getLogger("MOTDQuery");

	private static int serverTextureId = 0;

	public ServerData(String parString1, String parString2, boolean parFlag) {
		this.serverName = parString1;
		this.serverIP = parString2;
		this.field_181042_l = parFlag;
		this.iconResourceLocation = new ResourceLocation("eagler:servers/icons/tex_" + serverTextureId++);
		this.enableCookies = EagRuntime.getConfiguration().isEnableServerCookies();
	}

	/**+
	 * Returns an NBTTagCompound with the server's name, IP and
	 * maybe acceptTextures.
	 */
	public NBTTagCompound getNBTCompound() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setString("name", this.serverName);
		nbttagcompound.setString("ip", this.serverIP);

		if (this.resourceMode == ServerData.ServerResourceMode.ENABLED) {
			nbttagcompound.setBoolean("acceptTextures", true);
		} else if (this.resourceMode == ServerData.ServerResourceMode.DISABLED) {
			nbttagcompound.setBoolean("acceptTextures", false);
		}

		nbttagcompound.setBoolean("hideAddress", this.hideAddress);
		nbttagcompound.setBoolean("enableCookies", this.enableCookies);

		return nbttagcompound;
	}

	public ServerData.ServerResourceMode getResourceMode() {
		return this.resourceMode;
	}

	public void setResourceMode(ServerData.ServerResourceMode mode) {
		this.resourceMode = mode;
	}

	/**+
	 * Takes an NBTTagCompound with 'name' and 'ip' keys, returns a
	 * ServerData instance.
	 */
	public static ServerData getServerDataFromNBTCompound(NBTTagCompound nbtCompound) {
		ServerData serverdata = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"), false);

		if (nbtCompound.hasKey("acceptTextures", 1)) {
			if (nbtCompound.getBoolean("acceptTextures")) {
				serverdata.setResourceMode(ServerData.ServerResourceMode.ENABLED);
			} else {
				serverdata.setResourceMode(ServerData.ServerResourceMode.DISABLED);
			}
		} else {
			serverdata.setResourceMode(ServerData.ServerResourceMode.PROMPT);
		}

		if (nbtCompound.hasKey("hideAddress", 1)) {
			serverdata.hideAddress = nbtCompound.getBoolean("hideAddress");
		} else {
			serverdata.hideAddress = false;
		}

		if (nbtCompound.hasKey("enableCookies", 1)) {
			serverdata.enableCookies = nbtCompound.getBoolean("enableCookies");
		} else {
			serverdata.enableCookies = true;
		}

		return serverdata;
	}

	public boolean func_181041_d() {
		return this.field_181042_l;
	}

	public void copyFrom(ServerData serverDataIn) {
		this.serverIP = serverDataIn.serverIP;
		this.serverName = serverDataIn.serverName;
		this.setResourceMode(serverDataIn.getResourceMode());
		this.hideAddress = serverDataIn.hideAddress;
		this.field_181042_l = serverDataIn.field_181042_l;
		this.enableCookies = serverDataIn.enableCookies;
	}

	public static enum ServerResourceMode {
		ENABLED("enabled"), DISABLED("disabled"), PROMPT("prompt");

		public static final ServerResourceMode[] _VALUES = values();

		private final IChatComponent motd;

		private ServerResourceMode(String parString2) {
			this.motd = new ChatComponentTranslation("addServer.resourcePack." + parString2, new Object[0]);
		}

		public IChatComponent getMotd() {
			return this.motd;
		}
	}

	public void setMOTDFromQuery(QueryResponse pkt) {
		try {
			if (pkt.isResponseJSON()) {
				JSONObject motdData = pkt.getResponseJSON();
				JSONArray motd = motdData.getJSONArray("motd");
				this.serverMOTD = motd.length() > 0
						? (motd.length() > 1 ? motd.getString(0) + "\n" + motd.getString(1) : motd.getString(0))
						: "";
				int max = motdData.getInt("max");
				if (max > 0) {
					this.populationInfo = "" + motdData.getInt("online") + "/" + max;
				} else {
					this.populationInfo = "" + motdData.getInt("online");
				}
				this.playerList = null;
				JSONArray players = motdData.optJSONArray("players");
				if (players.length() > 0) {
					StringBuilder builder = new StringBuilder();
					for (int i = 0, l = players.length(); i < l; ++i) {
						if (i > 0) {
							builder.append('\n');
						}
						builder.append(players.getString(i));
					}
					this.playerList = builder.toString();
				}
				serverIconEnabled = motdData.getBoolean("icon");
				if (!serverIconEnabled) {
					if (iconTextureObject != null) {
						Minecraft.getMinecraft().getTextureManager().deleteTexture(iconResourceLocation);
						iconTextureObject = null;
					}
				}
			} else {
				throw new IOException("Response was not JSON!");
			}
		} catch (Throwable t) {
			pingToServer = -1l;
			logger.error("Could not decode QueryResponse from: {}", serverIP);
			logger.error(t);
		}
	}

	public void setIconPacket(byte[] pkt) {
		try {
			if (!serverIconEnabled) {
				throw new IOException("Unexpected icon packet on text-only MOTD");
			}
			if (pkt.length != 16384) {
				throw new IOException("MOTD icon packet is the wrong size!");
			}
			int[] pixels = new int[4096];
			for (int i = 0, j; i < 4096; ++i) {
				j = i << 2;
				pixels[i] = ((int) pkt[j] & 0xFF) | (((int) pkt[j + 1] & 0xFF) << 8) | (((int) pkt[j + 2] & 0xFF) << 16)
						| (((int) pkt[j + 3] & 0xFF) << 24);
			}
			if (iconTextureObject != null) {
				iconTextureObject.copyPixelsIn(pixels);
			} else {
				iconTextureObject = new EaglerSkinTexture(pixels, 64, 64);
				Minecraft.getMinecraft().getTextureManager().loadTexture(iconResourceLocation, iconTextureObject);
			}
		} catch (Throwable t) {
			pingToServer = -1l;
			logger.error("Could not decode MOTD icon from: {}", serverIP);
			logger.error(t);
		}
	}

}