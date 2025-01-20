package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.CombatTracker;

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
public class S42PacketCombatEvent implements Packet<INetHandlerPlayClient> {
	public S42PacketCombatEvent.Event eventType;
	public int field_179774_b;
	public int field_179775_c;
	public int field_179772_d;
	public String deathMessage;

	public S42PacketCombatEvent() {
	}

	public S42PacketCombatEvent(CombatTracker combatTrackerIn, S42PacketCombatEvent.Event combatEventType) {
		this.eventType = combatEventType;
		EntityLivingBase entitylivingbase = combatTrackerIn.func_94550_c();
		switch (combatEventType) {
		case END_COMBAT:
			this.field_179772_d = combatTrackerIn.func_180134_f();
			this.field_179775_c = entitylivingbase == null ? -1 : entitylivingbase.getEntityId();
			break;
		case ENTITY_DIED:
			this.field_179774_b = combatTrackerIn.getFighter().getEntityId();
			this.field_179775_c = entitylivingbase == null ? -1 : entitylivingbase.getEntityId();
			this.deathMessage = combatTrackerIn.getDeathMessage().getUnformattedText();
		}

	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.eventType = (S42PacketCombatEvent.Event) parPacketBuffer.readEnumValue(S42PacketCombatEvent.Event.class);
		if (this.eventType == S42PacketCombatEvent.Event.END_COMBAT) {
			this.field_179772_d = parPacketBuffer.readVarIntFromBuffer();
			this.field_179775_c = parPacketBuffer.readInt();
		} else if (this.eventType == S42PacketCombatEvent.Event.ENTITY_DIED) {
			this.field_179774_b = parPacketBuffer.readVarIntFromBuffer();
			this.field_179775_c = parPacketBuffer.readInt();
			this.deathMessage = parPacketBuffer.readStringFromBuffer(32767);
		}

	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeEnumValue(this.eventType);
		if (this.eventType == S42PacketCombatEvent.Event.END_COMBAT) {
			parPacketBuffer.writeVarIntToBuffer(this.field_179772_d);
			parPacketBuffer.writeInt(this.field_179775_c);
		} else if (this.eventType == S42PacketCombatEvent.Event.ENTITY_DIED) {
			parPacketBuffer.writeVarIntToBuffer(this.field_179774_b);
			parPacketBuffer.writeInt(this.field_179775_c);
			parPacketBuffer.writeString(this.deathMessage);
		}

	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleCombatEvent(this);
	}

	public static enum Event {
		ENTER_COMBAT, END_COMBAT, ENTITY_DIED;
	}
}