package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
public class S0CPacketSpawnPlayer implements Packet<INetHandlerPlayClient> {
	private int entityId;
	private EaglercraftUUID playerId;
	private int x;
	private int y;
	private int z;
	private byte yaw;
	private byte pitch;
	private int currentItem;
	private DataWatcher watcher;
	private List<DataWatcher.WatchableObject> field_148958_j;

	public S0CPacketSpawnPlayer() {
	}

	public S0CPacketSpawnPlayer(EntityPlayer player) {
		this.entityId = player.getEntityId();
		this.playerId = player.getGameProfile().getId();
		this.x = MathHelper.floor_double(player.posX * 32.0D);
		this.y = MathHelper.floor_double(player.posY * 32.0D);
		this.z = MathHelper.floor_double(player.posZ * 32.0D);
		this.yaw = (byte) ((int) (player.rotationYaw * 256.0F / 360.0F));
		this.pitch = (byte) ((int) (player.rotationPitch * 256.0F / 360.0F));
		ItemStack itemstack = player.inventory.getCurrentItem();
		this.currentItem = itemstack == null ? 0 : Item.getIdFromItem(itemstack.getItem());
		this.watcher = player.getDataWatcher();
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.entityId = parPacketBuffer.readVarIntFromBuffer();
		this.playerId = parPacketBuffer.readUuid();
		this.x = parPacketBuffer.readInt();
		this.y = parPacketBuffer.readInt();
		this.z = parPacketBuffer.readInt();
		this.yaw = parPacketBuffer.readByte();
		this.pitch = parPacketBuffer.readByte();
		this.currentItem = parPacketBuffer.readShort();
		this.field_148958_j = DataWatcher.readWatchedListFromPacketBuffer(parPacketBuffer);
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeVarIntToBuffer(this.entityId);
		parPacketBuffer.writeUuid(this.playerId);
		parPacketBuffer.writeInt(this.x);
		parPacketBuffer.writeInt(this.y);
		parPacketBuffer.writeInt(this.z);
		parPacketBuffer.writeByte(this.yaw);
		parPacketBuffer.writeByte(this.pitch);
		parPacketBuffer.writeShort(this.currentItem);
		this.watcher.writeTo(parPacketBuffer);
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleSpawnPlayer(this);
	}

	public List<DataWatcher.WatchableObject> func_148944_c() {
		if (this.field_148958_j == null) {
			this.field_148958_j = this.watcher.getAllWatched();
		}

		return this.field_148958_j;
	}

	public int getEntityID() {
		return this.entityId;
	}

	public EaglercraftUUID getPlayer() {
		return this.playerId;
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

	public byte getYaw() {
		return this.yaw;
	}

	public byte getPitch() {
		return this.pitch;
	}

	public int getCurrentItemID() {
		return this.currentItem;
	}
}