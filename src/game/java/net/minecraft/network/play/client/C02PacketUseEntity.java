package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

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
public class C02PacketUseEntity implements Packet<INetHandlerPlayServer> {
	private int entityId;
	private C02PacketUseEntity.Action action;
	private Vec3 hitVec;

	public C02PacketUseEntity() {
	}

	public C02PacketUseEntity(Entity entity, C02PacketUseEntity.Action action) {
		this.entityId = entity.getEntityId();
		this.action = action;
	}

	public C02PacketUseEntity(Entity entity, Vec3 hitVec) {
		this(entity, C02PacketUseEntity.Action.INTERACT_AT);
		this.hitVec = hitVec;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.entityId = parPacketBuffer.readVarIntFromBuffer();
		this.action = (C02PacketUseEntity.Action) parPacketBuffer.readEnumValue(C02PacketUseEntity.Action.class);
		if (this.action == C02PacketUseEntity.Action.INTERACT_AT) {
			this.hitVec = new Vec3((double) parPacketBuffer.readFloat(), (double) parPacketBuffer.readFloat(),
					(double) parPacketBuffer.readFloat());
		}

	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeVarIntToBuffer(this.entityId);
		parPacketBuffer.writeEnumValue(this.action);
		if (this.action == C02PacketUseEntity.Action.INTERACT_AT) {
			parPacketBuffer.writeFloat((float) this.hitVec.xCoord);
			parPacketBuffer.writeFloat((float) this.hitVec.yCoord);
			parPacketBuffer.writeFloat((float) this.hitVec.zCoord);
		}

	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayServer inethandlerplayserver) {
		inethandlerplayserver.processUseEntity(this);
	}

	public Entity getEntityFromWorld(World worldIn) {
		return worldIn.getEntityByID(this.entityId);
	}

	public C02PacketUseEntity.Action getAction() {
		return this.action;
	}

	public Vec3 getHitVec() {
		return this.hitVec;
	}

	public static enum Action {
		INTERACT, ATTACK, INTERACT_AT;
	}
}