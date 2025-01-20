package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import com.google.common.collect.Lists;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

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
public class S20PacketEntityProperties implements Packet<INetHandlerPlayClient> {
	private int entityId;
	private final List<S20PacketEntityProperties.Snapshot> field_149444_b = Lists.newArrayList();

	public S20PacketEntityProperties() {
	}

	public S20PacketEntityProperties(int entityIdIn, Collection<IAttributeInstance> parCollection) {
		this.entityId = entityIdIn;

		for (IAttributeInstance iattributeinstance : parCollection) {
			this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(
					iattributeinstance.getAttribute().getAttributeUnlocalizedName(), iattributeinstance.getBaseValue(),
					iattributeinstance.func_111122_c()));
		}

	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.entityId = parPacketBuffer.readVarIntFromBuffer();
		int i = parPacketBuffer.readInt();

		for (int j = 0; j < i; ++j) {
			String s = parPacketBuffer.readStringFromBuffer(64);
			double d0 = parPacketBuffer.readDouble();
			ArrayList arraylist = Lists.newArrayList();
			int k = parPacketBuffer.readVarIntFromBuffer();

			for (int l = 0; l < k; ++l) {
				EaglercraftUUID uuid = parPacketBuffer.readUuid();
				arraylist.add(new AttributeModifier(uuid, "Unknown synced attribute modifier",
						parPacketBuffer.readDouble(), parPacketBuffer.readByte()));
			}

			this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(s, d0, arraylist));
		}

	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeVarIntToBuffer(this.entityId);
		parPacketBuffer.writeInt(this.field_149444_b.size());

		for (S20PacketEntityProperties.Snapshot s20packetentityproperties$snapshot : this.field_149444_b) {
			parPacketBuffer.writeString(s20packetentityproperties$snapshot.func_151409_a());
			parPacketBuffer.writeDouble(s20packetentityproperties$snapshot.func_151410_b());
			parPacketBuffer.writeVarIntToBuffer(s20packetentityproperties$snapshot.func_151408_c().size());

			for (AttributeModifier attributemodifier : s20packetentityproperties$snapshot.func_151408_c()) {
				parPacketBuffer.writeUuid(attributemodifier.getID());
				parPacketBuffer.writeDouble(attributemodifier.getAmount());
				parPacketBuffer.writeByte(attributemodifier.getOperation());
			}
		}

	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleEntityProperties(this);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public List<S20PacketEntityProperties.Snapshot> func_149441_d() {
		return this.field_149444_b;
	}

	public class Snapshot {
		private final String field_151412_b;
		private final double field_151413_c;
		private final Collection<AttributeModifier> field_151411_d;

		public Snapshot(String parString1, double parDouble1, Collection<AttributeModifier> parCollection) {
			this.field_151412_b = parString1;
			this.field_151413_c = parDouble1;
			this.field_151411_d = parCollection;
		}

		public String func_151409_a() {
			return this.field_151412_b;
		}

		public double func_151410_b() {
			return this.field_151413_c;
		}

		public Collection<AttributeModifier> func_151408_c() {
			return this.field_151411_d;
		}
	}
}