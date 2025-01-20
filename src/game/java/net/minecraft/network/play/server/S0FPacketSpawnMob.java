package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
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
public class S0FPacketSpawnMob implements Packet<INetHandlerPlayClient> {
	private int entityId;
	private int type;
	private int x;
	private int y;
	private int z;
	private int velocityX;
	private int velocityY;
	private int velocityZ;
	private byte yaw;
	private byte pitch;
	private byte headPitch;
	private DataWatcher field_149043_l;
	private List<DataWatcher.WatchableObject> watcher;

	public S0FPacketSpawnMob() {
	}

	public S0FPacketSpawnMob(EntityLivingBase entityIn) {
		this.entityId = entityIn.getEntityId();
		this.type = (byte) EntityList.getEntityID(entityIn);
		this.x = MathHelper.floor_double(entityIn.posX * 32.0D);
		this.y = MathHelper.floor_double(entityIn.posY * 32.0D);
		this.z = MathHelper.floor_double(entityIn.posZ * 32.0D);
		this.yaw = (byte) ((int) (entityIn.rotationYaw * 256.0F / 360.0F));
		this.pitch = (byte) ((int) (entityIn.rotationPitch * 256.0F / 360.0F));
		this.headPitch = (byte) ((int) (entityIn.rotationYawHead * 256.0F / 360.0F));
		double d0 = 3.9D;
		double d1 = entityIn.motionX;
		double d2 = entityIn.motionY;
		double d3 = entityIn.motionZ;
		if (d1 < -d0) {
			d1 = -d0;
		}

		if (d2 < -d0) {
			d2 = -d0;
		}

		if (d3 < -d0) {
			d3 = -d0;
		}

		if (d1 > d0) {
			d1 = d0;
		}

		if (d2 > d0) {
			d2 = d0;
		}

		if (d3 > d0) {
			d3 = d0;
		}

		this.velocityX = (int) (d1 * 8000.0D);
		this.velocityY = (int) (d2 * 8000.0D);
		this.velocityZ = (int) (d3 * 8000.0D);
		this.field_149043_l = entityIn.getDataWatcher();
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.entityId = parPacketBuffer.readVarIntFromBuffer();
		this.type = parPacketBuffer.readByte() & 255;
		this.x = parPacketBuffer.readInt();
		this.y = parPacketBuffer.readInt();
		this.z = parPacketBuffer.readInt();
		this.yaw = parPacketBuffer.readByte();
		this.pitch = parPacketBuffer.readByte();
		this.headPitch = parPacketBuffer.readByte();
		this.velocityX = parPacketBuffer.readShort();
		this.velocityY = parPacketBuffer.readShort();
		this.velocityZ = parPacketBuffer.readShort();
		this.watcher = DataWatcher.readWatchedListFromPacketBuffer(parPacketBuffer);
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeVarIntToBuffer(this.entityId);
		parPacketBuffer.writeByte(this.type & 255);
		parPacketBuffer.writeInt(this.x);
		parPacketBuffer.writeInt(this.y);
		parPacketBuffer.writeInt(this.z);
		parPacketBuffer.writeByte(this.yaw);
		parPacketBuffer.writeByte(this.pitch);
		parPacketBuffer.writeByte(this.headPitch);
		parPacketBuffer.writeShort(this.velocityX);
		parPacketBuffer.writeShort(this.velocityY);
		parPacketBuffer.writeShort(this.velocityZ);
		this.field_149043_l.writeTo(parPacketBuffer);
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleSpawnMob(this);
	}

	public List<DataWatcher.WatchableObject> func_149027_c() {
		if (this.watcher == null) {
			this.watcher = this.field_149043_l.getAllWatched();
		}

		return this.watcher;
	}

	public int getEntityID() {
		return this.entityId;
	}

	public int getEntityType() {
		return this.type;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public int getVelocityX() {
		return this.velocityX;
	}

	public int getVelocityY() {
		return this.velocityY;
	}

	public int getVelocityZ() {
		return this.velocityZ;
	}

	public byte getYaw() {
		return this.yaw;
	}

	public byte getPitch() {
		return this.pitch;
	}

	public byte getHeadPitch() {
		return this.headPitch;
	}
}