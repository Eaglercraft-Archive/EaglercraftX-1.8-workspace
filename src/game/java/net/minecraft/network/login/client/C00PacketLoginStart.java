package net.minecraft.network.login.client;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import java.io.IOException;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;

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
public class C00PacketLoginStart implements Packet<INetHandlerLoginServer> {
	private GameProfile profile;
	private byte[] skin;
	private byte[] cape;
	private byte[] protocols;
	private EaglercraftUUID brandUUID;

	public C00PacketLoginStart() {
	}

	public C00PacketLoginStart(GameProfile profileIn, byte[] skin, byte[] cape, byte[] protocols,
			EaglercraftUUID brandUUID) {
		this.profile = profileIn;
		this.skin = skin;
		this.cape = cape;
		this.protocols = protocols;
		this.brandUUID = brandUUID;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.profile = new GameProfile((EaglercraftUUID) null, parPacketBuffer.readStringFromBuffer(16));
		this.skin = parPacketBuffer.readByteArray();
		this.cape = parPacketBuffer.readableBytes() > 0 ? parPacketBuffer.readByteArray() : null;
		this.protocols = parPacketBuffer.readableBytes() > 0 ? parPacketBuffer.readByteArray() : null;
		this.brandUUID = parPacketBuffer.readableBytes() > 0 ? parPacketBuffer.readUuid() : null;
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeString(this.profile.getName());
		parPacketBuffer.writeByteArray(this.skin);
		parPacketBuffer.writeByteArray(this.cape);
		parPacketBuffer.writeByteArray(this.protocols);
		parPacketBuffer.writeUuid(brandUUID);
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerLoginServer inethandlerloginserver) {
		inethandlerloginserver.processLoginStart(this);
	}

	public GameProfile getProfile() {
		return this.profile;
	}

	public byte[] getSkin() {
		return this.skin;
	}

	public byte[] getCape() {
		return this.cape;
	}

	public byte[] getProtocols() {
		return this.protocols;
	}

	public EaglercraftUUID getBrandUUID() {
		return this.brandUUID;
	}
}