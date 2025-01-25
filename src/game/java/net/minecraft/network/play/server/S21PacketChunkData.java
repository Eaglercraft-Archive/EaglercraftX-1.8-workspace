package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

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
public class S21PacketChunkData implements Packet<INetHandlerPlayClient> {
	private int chunkX;
	private int chunkZ;
	private S21PacketChunkData.Extracted extractedData;
	private boolean field_149279_g;

	public S21PacketChunkData() {
	}

	public S21PacketChunkData(Chunk chunkIn, boolean parFlag, int parInt1) {
		this.chunkX = chunkIn.xPosition;
		this.chunkZ = chunkIn.zPosition;
		this.field_149279_g = parFlag;
		chunkIn.alfheim$getLightingEngine().processLightUpdates();
		this.extractedData = func_179756_a(chunkIn, parFlag, !chunkIn.getWorld().provider.getHasNoSky(), parInt1);
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.chunkX = parPacketBuffer.readInt();
		this.chunkZ = parPacketBuffer.readInt();
		this.field_149279_g = parPacketBuffer.readBoolean();
		this.extractedData = new S21PacketChunkData.Extracted();
		this.extractedData.dataSize = parPacketBuffer.readShort();
		this.extractedData.data = parPacketBuffer.readByteArray();
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeInt(this.chunkX);
		parPacketBuffer.writeInt(this.chunkZ);
		parPacketBuffer.writeBoolean(this.field_149279_g);
		parPacketBuffer.writeShort((short) (this.extractedData.dataSize & '\uffff'));
		parPacketBuffer.writeByteArray(this.extractedData.data);
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleChunkData(this);
	}

	public byte[] func_149272_d() {
		return this.extractedData.data;
	}

	protected static int func_180737_a(int parInt1, boolean parFlag, boolean parFlag2) {
		int i = parInt1 * 2 * 16 * 16 * 16;
		int j = parInt1 * 16 * 16 * 16 / 2;
		int k = parFlag ? parInt1 * 16 * 16 * 16 / 2 : 0;
		int l = parFlag2 ? 256 : 0;
		return i + j + k + l;
	}

	public static S21PacketChunkData.Extracted func_179756_a(Chunk parChunk, boolean parFlag, boolean parFlag2,
			int parInt1) {
		ExtendedBlockStorage[] aextendedblockstorage = parChunk.getBlockStorageArray();
		S21PacketChunkData.Extracted s21packetchunkdata$extracted = new S21PacketChunkData.Extracted();
		ArrayList<ExtendedBlockStorage> arraylist = Lists.newArrayList();

		for (int i = 0; i < aextendedblockstorage.length; ++i) {
			ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[i];
			if (extendedblockstorage != null && (!parFlag || !extendedblockstorage.isEmpty())
					&& (parInt1 & 1 << i) != 0) {
				s21packetchunkdata$extracted.dataSize |= 1 << i;
				arraylist.add(extendedblockstorage);
			}
		}

		s21packetchunkdata$extracted.data = new byte[func_180737_a(
				Integer.bitCount(s21packetchunkdata$extracted.dataSize), parFlag2, parFlag)];
		int j = 0;
		int l = arraylist.size();
		for (int k = 0; k < l; ++k) {
			char[] achar = arraylist.get(k).getData();

			for (int m = 0; m < achar.length; ++m) {
				char c0 = achar[m];
				s21packetchunkdata$extracted.data[j++] = (byte) (c0 & 255);
				s21packetchunkdata$extracted.data[j++] = (byte) (c0 >> 8 & 255);
			}
		}

		for (int k = 0; k < l; ++k) {
			j = func_179757_a(arraylist.get(k).getBlocklightArray().getData(), s21packetchunkdata$extracted.data, j);
		}

		if (parFlag2) {
			for (int k = 0; k < l; ++k) {
				j = func_179757_a(arraylist.get(k).getSkylightArray().getData(), s21packetchunkdata$extracted.data, j);
			}
		}

		if (parFlag) {
			func_179757_a(parChunk.getBiomeArray(), s21packetchunkdata$extracted.data, j);
		}

		return s21packetchunkdata$extracted;
	}

	private static int func_179757_a(byte[] parArrayOfByte, byte[] parArrayOfByte2, int parInt1) {
		System.arraycopy(parArrayOfByte, 0, parArrayOfByte2, parInt1, parArrayOfByte.length);
		return parInt1 + parArrayOfByte.length;
	}

	public int getChunkX() {
		return this.chunkX;
	}

	public int getChunkZ() {
		return this.chunkZ;
	}

	public int getExtractedSize() {
		return this.extractedData.dataSize;
	}

	public boolean func_149274_i() {
		return this.field_149279_g;
	}

	public static class Extracted {
		public byte[] data;
		public int dataSize;
	}
}