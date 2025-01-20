package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

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
public class S10PacketSpawnPainting implements Packet<INetHandlerPlayClient> {
	private int entityID;
	private BlockPos position;
	private EnumFacing facing;
	private String title;

	public S10PacketSpawnPainting() {
	}

	public S10PacketSpawnPainting(EntityPainting painting) {
		this.entityID = painting.getEntityId();
		this.position = painting.getHangingPosition();
		this.facing = painting.facingDirection;
		this.title = painting.art.title;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.entityID = parPacketBuffer.readVarIntFromBuffer();
		this.title = parPacketBuffer.readStringFromBuffer(EntityPainting.EnumArt.field_180001_A);
		this.position = parPacketBuffer.readBlockPos();
		this.facing = EnumFacing.getHorizontal(parPacketBuffer.readUnsignedByte());
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeVarIntToBuffer(this.entityID);
		parPacketBuffer.writeString(this.title);
		parPacketBuffer.writeBlockPos(this.position);
		parPacketBuffer.writeByte(this.facing.getHorizontalIndex());
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleSpawnPainting(this);
	}

	public int getEntityID() {
		return this.entityID;
	}

	public BlockPos getPosition() {
		return this.position;
	}

	public EnumFacing getFacing() {
		return this.facing;
	}

	public String getTitle() {
		return this.title;
	}
}