package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Collection;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;

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
public class S34PacketMaps implements Packet<INetHandlerPlayClient> {
	private int mapId;
	private byte mapScale;
	private Vec4b[] mapVisiblePlayersVec4b;
	private int mapMinX;
	private int mapMinY;
	private int mapMaxX;
	private int mapMaxY;
	private byte[] mapDataBytes;

	public S34PacketMaps() {
	}

	public S34PacketMaps(int mapIdIn, byte scale, Collection<Vec4b> visiblePlayers, byte[] colors, int minX, int minY,
			int maxX, int maxY) {
		this.mapId = mapIdIn;
		this.mapScale = scale;
		this.mapVisiblePlayersVec4b = (Vec4b[]) visiblePlayers.toArray(new Vec4b[visiblePlayers.size()]);
		this.mapMinX = minX;
		this.mapMinY = minY;
		this.mapMaxX = maxX;
		this.mapMaxY = maxY;
		this.mapDataBytes = new byte[maxX * maxY];

		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				this.mapDataBytes[i + j * maxX] = colors[minX + i + (minY + j) * 128];
			}
		}

	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.mapId = parPacketBuffer.readVarIntFromBuffer();
		this.mapScale = parPacketBuffer.readByte();
		this.mapVisiblePlayersVec4b = new Vec4b[parPacketBuffer.readVarIntFromBuffer()];

		for (int i = 0; i < this.mapVisiblePlayersVec4b.length; ++i) {
			short short1 = (short) parPacketBuffer.readByte();
			this.mapVisiblePlayersVec4b[i] = new Vec4b((byte) (short1 >> 4 & 15), parPacketBuffer.readByte(),
					parPacketBuffer.readByte(), (byte) (short1 & 15));
		}

		this.mapMaxX = parPacketBuffer.readUnsignedByte();
		if (this.mapMaxX > 0) {
			this.mapMaxY = parPacketBuffer.readUnsignedByte();
			this.mapMinX = parPacketBuffer.readUnsignedByte();
			this.mapMinY = parPacketBuffer.readUnsignedByte();
			this.mapDataBytes = parPacketBuffer.readByteArray();
		}

	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeVarIntToBuffer(this.mapId);
		parPacketBuffer.writeByte(this.mapScale);
		parPacketBuffer.writeVarIntToBuffer(this.mapVisiblePlayersVec4b.length);

		for (int i = 0; i < this.mapVisiblePlayersVec4b.length; ++i) {
			Vec4b vec4b = this.mapVisiblePlayersVec4b[i];
			parPacketBuffer.writeByte((vec4b.func_176110_a() & 15) << 4 | vec4b.func_176111_d() & 15);
			parPacketBuffer.writeByte(vec4b.func_176112_b());
			parPacketBuffer.writeByte(vec4b.func_176113_c());
		}

		parPacketBuffer.writeByte(this.mapMaxX);
		if (this.mapMaxX > 0) {
			parPacketBuffer.writeByte(this.mapMaxY);
			parPacketBuffer.writeByte(this.mapMinX);
			parPacketBuffer.writeByte(this.mapMinY);
			parPacketBuffer.writeByteArray(this.mapDataBytes);
		}

	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleMaps(this);
	}

	public int getMapId() {
		return this.mapId;
	}

	/**+
	 * Sets new MapData from the packet to given MapData param
	 */
	public void setMapdataTo(MapData mapdataIn) {
		mapdataIn.scale = this.mapScale;
		mapdataIn.mapDecorations.clear();

		for (int i = 0; i < this.mapVisiblePlayersVec4b.length; ++i) {
			Vec4b vec4b = this.mapVisiblePlayersVec4b[i];
			mapdataIn.mapDecorations.put("icon-" + i, vec4b);
		}

		for (int j = 0; j < this.mapMaxX; ++j) {
			for (int k = 0; k < this.mapMaxY; ++k) {
				mapdataIn.colors[this.mapMinX + j + (this.mapMinY + k) * 128] = this.mapDataBytes[j + k * this.mapMaxX];
			}
		}

	}
}