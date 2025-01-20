package net.minecraft.network.login.server;

import java.io.IOException;

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
public class S01PacketEncryptionRequest implements Packet<INetHandlerLoginClient> {
	private String hashedServerId;
	// private PublicKey publicKey;
	private byte[] verifyToken;

	public S01PacketEncryptionRequest() {
	}

//	public S01PacketEncryptionRequest(String serverId, PublicKey key, byte[] verifyToken) {
//		this.hashedServerId = serverId;
//		this.publicKey = key;
//		this.verifyToken = verifyToken;
//	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.hashedServerId = parPacketBuffer.readStringFromBuffer(20);
		// this.publicKey =
		// CryptManager.decodePublicKey(parPacketBuffer.readByteArray());
		parPacketBuffer.readByteArray(); // skip
		this.verifyToken = parPacketBuffer.readByteArray();
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
//		parPacketBuffer.writeString(this.hashedServerId);
//		parPacketBuffer.writeByteArray(this.publicKey.getEncoded());
//		parPacketBuffer.writeByteArray(this.verifyToken);
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerLoginClient inethandlerloginclient) {
		inethandlerloginclient.handleEncryptionRequest(this);
	}

	public String getServerId() {
		return this.hashedServerId;
	}

//	public PublicKey getPublicKey() {
//		return this.publicKey;
//	}

	public byte[] getVerifyToken() {
		return this.verifyToken;
	}
}