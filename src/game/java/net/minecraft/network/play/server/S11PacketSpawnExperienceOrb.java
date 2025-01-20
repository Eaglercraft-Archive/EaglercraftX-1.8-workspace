package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

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
public class S11PacketSpawnExperienceOrb implements Packet<INetHandlerPlayClient> {
	private int entityID;
	private int posX;
	private int posY;
	private int posZ;
	private int xpValue;

	public S11PacketSpawnExperienceOrb() {
	}

	public S11PacketSpawnExperienceOrb(EntityXPOrb xpOrb) {
		this.entityID = xpOrb.getEntityId();
		this.posX = MathHelper.floor_double(xpOrb.posX * 32.0D);
		this.posY = MathHelper.floor_double(xpOrb.posY * 32.0D);
		this.posZ = MathHelper.floor_double(xpOrb.posZ * 32.0D);
		this.xpValue = xpOrb.getXpValue();
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.entityID = parPacketBuffer.readVarIntFromBuffer();
		this.posX = parPacketBuffer.readInt();
		this.posY = parPacketBuffer.readInt();
		this.posZ = parPacketBuffer.readInt();
		this.xpValue = parPacketBuffer.readShort();
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeVarIntToBuffer(this.entityID);
		parPacketBuffer.writeInt(this.posX);
		parPacketBuffer.writeInt(this.posY);
		parPacketBuffer.writeInt(this.posZ);
		parPacketBuffer.writeShort(this.xpValue);
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleSpawnExperienceOrb(this);
	}

	public int getEntityID() {
		return this.entityID;
	}

	public int getX() {
		return this.posX;
	}

	public int getY() {
		return this.posY;
	}

	public int getZ() {
		return this.posZ;
	}

	public int getXPValue() {
		return this.xpValue;
	}
}