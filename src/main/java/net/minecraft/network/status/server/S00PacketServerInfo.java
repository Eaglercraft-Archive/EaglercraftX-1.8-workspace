package net.minecraft.network.status.server;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

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
public class S00PacketServerInfo implements Packet<INetHandlerStatusClient> {

	private ServerStatusResponse response;

	public S00PacketServerInfo() {
	}

	public S00PacketServerInfo(ServerStatusResponse responseIn) {
		this.response = responseIn;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		try {
			this.response = (ServerStatusResponse) JSONTypeProvider.deserialize(
					new JSONObject(parPacketBuffer.readStringFromBuffer(32767)), ServerStatusResponse.class);
		} catch (JSONException exc) {
			throw new IOException("Invalid ServerStatusResponse JSON payload", exc);
		}
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		try {
			parPacketBuffer.writeString(((JSONObject) JSONTypeProvider.serialize(this.response)).toString());
		} catch (JSONException exc) {
			throw new IOException("Invalid ServerStatusResponse JSON payload", exc);
		}
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerStatusClient inethandlerstatusclient) {
		inethandlerstatusclient.handleServerInfo(this);
	}

	public ServerStatusResponse getResponse() {
		return this.response;
	}
}