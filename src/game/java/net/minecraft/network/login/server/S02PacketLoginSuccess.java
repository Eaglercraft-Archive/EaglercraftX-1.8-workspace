package net.minecraft.network.login.server;

import java.io.IOException;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

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
public class S02PacketLoginSuccess implements Packet<INetHandlerLoginClient> {
	private GameProfile profile;
	private int selectedProtocol = 3;

	public S02PacketLoginSuccess() {
	}

	public S02PacketLoginSuccess(GameProfile profileIn, int selectedProtocol) {
		this.profile = profileIn;
		this.selectedProtocol = selectedProtocol;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		String s = parPacketBuffer.readStringFromBuffer(36);
		String s1 = parPacketBuffer.readStringFromBuffer(16);
		selectedProtocol = parPacketBuffer.readableBytes() > 0 ? parPacketBuffer.readShort() : 3;
		EaglercraftUUID uuid = EaglercraftUUID.fromString(s);
		this.profile = new GameProfile(uuid, s1);
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		EaglercraftUUID uuid = this.profile.getId();
		parPacketBuffer.writeString(uuid == null ? "" : uuid.toString());
		parPacketBuffer.writeString(this.profile.getName());
		if (selectedProtocol != 3) {
			parPacketBuffer.writeShort(selectedProtocol);
		}
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerLoginClient inethandlerloginclient) {
		inethandlerloginclient.handleLoginSuccess(this);
	}

	public GameProfile getProfile() {
		return this.profile;
	}

	public int getSelectedProtocol() {
		return selectedProtocol;
	}
}