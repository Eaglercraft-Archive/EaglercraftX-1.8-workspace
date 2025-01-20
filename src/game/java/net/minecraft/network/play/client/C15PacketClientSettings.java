package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

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
public class C15PacketClientSettings implements Packet<INetHandlerPlayServer> {
	private String lang;
	private int view;
	private EntityPlayer.EnumChatVisibility chatVisibility;
	private boolean enableColors;
	private int modelPartFlags;

	public C15PacketClientSettings() {
	}

	public C15PacketClientSettings(String langIn, int viewIn, EntityPlayer.EnumChatVisibility chatVisibilityIn,
			boolean enableColorsIn, int modelPartFlagsIn) {
		this.lang = langIn;
		this.view = viewIn;
		this.chatVisibility = chatVisibilityIn;
		this.enableColors = enableColorsIn;
		this.modelPartFlags = modelPartFlagsIn;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.lang = parPacketBuffer.readStringFromBuffer(7);
		this.view = parPacketBuffer.readByte();
		this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(parPacketBuffer.readByte());
		this.enableColors = parPacketBuffer.readBoolean();
		this.modelPartFlags = parPacketBuffer.readUnsignedByte();
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeString(this.lang);
		parPacketBuffer.writeByte(this.view);
		parPacketBuffer.writeByte(this.chatVisibility.getChatVisibility());
		parPacketBuffer.writeBoolean(this.enableColors);
		parPacketBuffer.writeByte(this.modelPartFlags);
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayServer inethandlerplayserver) {
		inethandlerplayserver.processClientSettings(this);
	}

	public String getLang() {
		return this.lang;
	}

	public EntityPlayer.EnumChatVisibility getChatVisibility() {
		return this.chatVisibility;
	}

	public boolean isColorsEnabled() {
		return this.enableColors;
	}

	public int getModelPartFlags() {
		return this.modelPartFlags;
	}

	public int getViewDistance() {
		return this.view;
	}
}