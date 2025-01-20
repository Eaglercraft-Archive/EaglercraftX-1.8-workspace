package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
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
public class S45PacketTitle implements Packet<INetHandlerPlayClient> {
	private S45PacketTitle.Type type;
	private IChatComponent message;
	private int fadeInTime;
	private int displayTime;
	private int fadeOutTime;

	public S45PacketTitle() {
	}

	public S45PacketTitle(S45PacketTitle.Type type, IChatComponent message) {
		this(type, message, -1, -1, -1);
	}

	public S45PacketTitle(int fadeInTime, int displayTime, int fadeOutTime) {
		this(S45PacketTitle.Type.TIMES, (IChatComponent) null, fadeInTime, displayTime, fadeOutTime);
	}

	public S45PacketTitle(S45PacketTitle.Type type, IChatComponent message, int fadeInTime, int displayTime,
			int fadeOutTime) {
		this.type = type;
		this.message = message;
		this.fadeInTime = fadeInTime;
		this.displayTime = displayTime;
		this.fadeOutTime = fadeOutTime;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.type = (S45PacketTitle.Type) parPacketBuffer.readEnumValue(S45PacketTitle.Type.class);
		if (this.type == S45PacketTitle.Type.TITLE || this.type == S45PacketTitle.Type.SUBTITLE) {
			this.message = parPacketBuffer.readChatComponent();
		}

		if (this.type == S45PacketTitle.Type.TIMES) {
			this.fadeInTime = parPacketBuffer.readInt();
			this.displayTime = parPacketBuffer.readInt();
			this.fadeOutTime = parPacketBuffer.readInt();
		}

	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeEnumValue(this.type);
		if (this.type == S45PacketTitle.Type.TITLE || this.type == S45PacketTitle.Type.SUBTITLE) {
			parPacketBuffer.writeChatComponent(this.message);
		}

		if (this.type == S45PacketTitle.Type.TIMES) {
			parPacketBuffer.writeInt(this.fadeInTime);
			parPacketBuffer.writeInt(this.displayTime);
			parPacketBuffer.writeInt(this.fadeOutTime);
		}

	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleTitle(this);
	}

	public S45PacketTitle.Type getType() {
		return this.type;
	}

	public IChatComponent getMessage() {
		return this.message;
	}

	public int getFadeInTime() {
		return this.fadeInTime;
	}

	public int getDisplayTime() {
		return this.displayTime;
	}

	public int getFadeOutTime() {
		return this.fadeOutTime;
	}

	public static enum Type {
		TITLE, SUBTITLE, TIMES, CLEAR, RESET;

		public static S45PacketTitle.Type byName(String name) {
			S45PacketTitle.Type[] types = values();
			for (int i = 0; i < types.length; ++i) {
				S45PacketTitle.Type s45packettitle$type = types[i];
				if (s45packettitle$type.name().equalsIgnoreCase(name)) {
					return s45packettitle$type;
				}
			}

			return TITLE;
		}

		public static String[] getNames() {
			S45PacketTitle.Type[] types = values();
			String[] astring = new String[types.length];

			for (int i = 0; i < types.length; ++i) {
				astring[i] = types[i].name().toLowerCase();
			}

			return astring;
		}
	}
}