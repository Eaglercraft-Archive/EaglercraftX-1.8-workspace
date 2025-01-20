package net.minecraft.client.multiplayer;

import java.util.List;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.internal.EnumServerRateLimit;
import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapter.DefaultServer;
import net.lax1dude.eaglercraft.v1_8.internal.QueryResponse;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.socket.AddressResolver;
import net.lax1dude.eaglercraft.v1_8.socket.RateLimitTracker;
import net.lax1dude.eaglercraft.v1_8.socket.ServerQueryDispatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;

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
public class ServerList {
	private static final Logger logger = LogManager.getLogger();
	private final Minecraft mc;
	private final List<ServerData> allServers = Lists.newArrayList();
	/**+
	 * List of ServerData instances.
	 */
	private final List<ServerData> servers = Lists.newArrayList();

	private static ServerList instance = null;

	private ServerList(Minecraft mcIn) {
		this.mc = mcIn;
		this.loadServerList();
	}

	public static void initServerList(Minecraft mc) {
		instance = new ServerList(mc);
	}

	public static ServerList getServerList() {
		return instance;
	}

	/**+
	 * Loads a list of servers from servers.dat, by running
	 * ServerData.getServerDataFromNBTCompound on each NBT compound
	 * found in the "servers" tag list.
	 */
	public void loadServerList() {
		loadServerList(EagRuntime.getStorage("s"));
	}

	/**+
	 * Loads a list of servers from servers.dat, by running
	 * ServerData.getServerDataFromNBTCompound on each NBT compound
	 * found in the "servers" tag list.
	 */
	public void loadServerList(byte[] localStorage) {
		try {
			freeServerIcons();

			this.allServers.clear();
			for (DefaultServer srv : EagRuntime.getConfiguration().getDefaultServerList()) {
				ServerData dat = new ServerData(srv.name, srv.addr, true);
				dat.isDefault = true;
				dat.hideAddress = srv.hideAddress;
				this.allServers.add(dat);
			}

			if (localStorage != null) {
				NBTTagCompound nbttagcompound = CompressedStreamTools
						.readCompressed(new EaglerInputStream(localStorage));
				if (nbttagcompound == null) {
					return;
				}

				NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);

				for (int i = 0; i < nbttaglist.tagCount(); ++i) {
					ServerData srv = ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i));
					this.allServers.add(srv);
				}
			}

		} catch (Exception exception) {
			logger.error("Couldn\'t load server list", exception);
		} finally {
			refreshServerPing();
		}

	}

	/**+
	 * Runs getNBTCompound on each ServerData instance, puts
	 * everything into a "servers" NBT list and writes it to
	 * servers.dat.
	 */
	public void saveServerList() {
		byte[] data = writeServerList();
		if (data != null) {
			EagRuntime.setStorage("s", data);
		}
	}

	public byte[] writeServerList() {
		try {
			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 0, l = this.servers.size(); i < l; ++i) {
				ServerData serverdata = this.servers.get(i);
				if (!serverdata.isDefault) {
					nbttaglist.appendTag(serverdata.getNBTCompound());
				}
			}

			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setTag("servers", nbttaglist);

			EaglerOutputStream bao = new EaglerOutputStream();
			CompressedStreamTools.writeCompressed(nbttagcompound, bao);
			return bao.toByteArray();

		} catch (Exception exception) {
			logger.error("Couldn\'t save server list", exception);
			return null;
		}

	}

	/**+
	 * Gets the ServerData instance stored for the given index in
	 * the list.
	 */
	public ServerData getServerData(int parInt1) {
		return (ServerData) this.servers.get(parInt1);
	}

	/**+
	 * Removes the ServerData instance stored for the given index in
	 * the list.
	 */
	public void removeServerData(int parInt1) {
		ServerData data = this.servers.remove(parInt1);
		if (data != null && data.iconTextureObject != null) {
			mc.getTextureManager().deleteTexture(data.iconResourceLocation);
			data.iconTextureObject = null;
		}
	}

	/**+
	 * Adds the given ServerData instance to the list.
	 */
	public void addServerData(ServerData parServerData) {
		this.servers.add(parServerData);
	}

	/**+
	 * Counts the number of ServerData instances in the list.
	 */
	public int countServers() {
		return this.servers.size();
	}

	/**+
	 * Takes two list indexes, and swaps their order around.
	 */
	public void swapServers(int parInt1, int parInt2) {
		ServerData serverdata = this.getServerData(parInt1);
		this.servers.set(parInt1, this.getServerData(parInt2));
		this.servers.set(parInt2, serverdata);
		this.saveServerList();
	}

	public void func_147413_a(int parInt1, ServerData parServerData) {
		this.servers.set(parInt1, parServerData);
	}

	public static void func_147414_b(ServerData parServerData) {
		ServerList serverlist = new ServerList(Minecraft.getMinecraft());
		serverlist.loadServerList();

		for (int i = 0; i < serverlist.countServers(); ++i) {
			ServerData serverdata = serverlist.getServerData(i);
			if (serverdata.serverName.equals(parServerData.serverName)
					&& serverdata.serverIP.equals(parServerData.serverIP)) {
				serverlist.func_147413_a(i, parServerData);
				break;
			}
		}

		serverlist.saveServerList();
	}

	public void freeServerIcons() {
		TextureManager mgr = mc.getTextureManager();
		for (int i = 0, l = allServers.size(); i < l; ++i) {
			ServerData server = allServers.get(i);
			if (server.iconTextureObject != null) {
				mgr.deleteTexture(server.iconResourceLocation);
				server.iconTextureObject = null;
			}
		}
	}

	public void refreshServerPing() {
		this.servers.clear();
		this.servers.addAll(this.allServers);
		for (int i = 0, l = this.servers.size(); i < l; ++i) {
			ServerData dat = this.servers.get(i);
			if (dat.currentQuery != null) {
				if (dat.currentQuery.isOpen()) {
					dat.currentQuery.close();
				}
				dat.currentQuery = null;
			}
			dat.hasPing = false;
			dat.pingSentTime = -1l;
		}
	}

	public void updateServerPing() {
		int total = 0;
		for (int i = 0, l = this.servers.size(); i < l; ++i) {
			ServerData dat = this.servers.get(i);
			if (dat.pingSentTime <= 0l) {
				dat.pingSentTime = EagRuntime.steadyTimeMillis();
				if (RateLimitTracker.isLockedOut(dat.serverIP)) {
					logger.error(
							"Server {} locked this client out on a previous connection, will not attempt to reconnect",
							dat.serverIP);
					dat.serverMOTD = EnumChatFormatting.RED + "Too Many Requests!\nTry again later";
					dat.pingToServer = -1l;
					dat.hasPing = true;
					dat.field_78841_f = true;
				} else {
					dat.pingToServer = -2l;
					String addr = AddressResolver.resolveURI(dat.serverIP);
					dat.currentQuery = ServerQueryDispatch.sendServerQuery(addr, "MOTD");
					if (dat.currentQuery == null) {
						dat.pingToServer = -1l;
						dat.hasPing = true;
						dat.field_78841_f = true;
					} else {
						++total;
					}
				}
			} else if (dat.currentQuery != null) {
				dat.currentQuery.update();
				if (!dat.hasPing) {
					++total;
					EnumServerRateLimit rateLimit = dat.currentQuery.getRateLimit();
					if (rateLimit != EnumServerRateLimit.OK) {
						if (rateLimit == EnumServerRateLimit.BLOCKED) {
							RateLimitTracker.registerBlock(dat.serverIP);
						} else if (rateLimit == EnumServerRateLimit.LOCKED_OUT) {
							RateLimitTracker.registerLockOut(dat.serverIP);
						}
						dat.serverMOTD = EnumChatFormatting.RED + "Too Many Requests!\nTry again later";
						dat.pingToServer = -1l;
						dat.hasPing = true;
						return;
					}
				}
				if (dat.currentQuery.responsesAvailable() > 0) {
					QueryResponse pkt;
					do {
						pkt = dat.currentQuery.getResponse();
					} while (dat.currentQuery.responsesAvailable() > 0);
					if (pkt.responseType.equalsIgnoreCase("MOTD") && pkt.isResponseJSON()) {
						dat.setMOTDFromQuery(pkt);
						if (!dat.hasPing) {
							dat.pingToServer = pkt.clientTime - dat.pingSentTime;
							dat.hasPing = true;
						}
					}
				}
				if (dat.currentQuery.binaryResponsesAvailable() > 0) {
					byte[] r;
					do {
						r = dat.currentQuery.getBinaryResponse();
					} while (dat.currentQuery.binaryResponsesAvailable() > 0);
					dat.setIconPacket(r);
				}
				if (!dat.currentQuery.isOpen() && dat.pingSentTime > 0l
						&& (EagRuntime.steadyTimeMillis() - dat.pingSentTime) > 2000l && !dat.hasPing) {
					if (RateLimitTracker.isProbablyLockedOut(dat.serverIP)) {
						logger.error("Server {} ratelimited this client out on a previous connection, assuming lockout",
								dat.serverIP);
						dat.serverMOTD = EnumChatFormatting.RED + "Too Many Requests!\nTry again later";
					}
					dat.pingToServer = -1l;
					dat.hasPing = true;
				}
			}
			if (total >= 4) {
				break;
			}
		}

	}

}