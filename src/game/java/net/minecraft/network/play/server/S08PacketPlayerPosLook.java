package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

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
public class S08PacketPlayerPosLook implements Packet<INetHandlerPlayClient> {
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private Set<S08PacketPlayerPosLook.EnumFlags> field_179835_f;

	public S08PacketPlayerPosLook() {
	}

	public S08PacketPlayerPosLook(double xIn, double yIn, double zIn, float yawIn, float pitchIn,
			Set<S08PacketPlayerPosLook.EnumFlags> parSet) {
		this.x = xIn;
		this.y = yIn;
		this.z = zIn;
		this.yaw = yawIn;
		this.pitch = pitchIn;
		this.field_179835_f = parSet;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.x = parPacketBuffer.readDouble();
		this.y = parPacketBuffer.readDouble();
		this.z = parPacketBuffer.readDouble();
		this.yaw = parPacketBuffer.readFloat();
		this.pitch = parPacketBuffer.readFloat();
		this.field_179835_f = S08PacketPlayerPosLook.EnumFlags.func_180053_a(parPacketBuffer.readUnsignedByte());
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeDouble(this.x);
		parPacketBuffer.writeDouble(this.y);
		parPacketBuffer.writeDouble(this.z);
		parPacketBuffer.writeFloat(this.yaw);
		parPacketBuffer.writeFloat(this.pitch);
		parPacketBuffer.writeByte(S08PacketPlayerPosLook.EnumFlags.func_180056_a(this.field_179835_f));
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handlePlayerPosLook(this);
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	public float getYaw() {
		return this.yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	public Set<S08PacketPlayerPosLook.EnumFlags> func_179834_f() {
		return this.field_179835_f;
	}

	public static enum EnumFlags {
		X(0), Y(1), Z(2), Y_ROT(3), X_ROT(4);

		private int field_180058_f;

		private EnumFlags(int parInt2) {
			this.field_180058_f = parInt2;
		}

		private int func_180055_a() {
			return 1 << this.field_180058_f;
		}

		private boolean func_180054_b(int parInt1) {
			return (parInt1 & this.func_180055_a()) == this.func_180055_a();
		}

		public static Set<S08PacketPlayerPosLook.EnumFlags> func_180053_a(int parInt1) {
			EnumSet enumset = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);

			S08PacketPlayerPosLook.EnumFlags[] types = values();
			for (int i = 0; i < types.length; ++i) {
				S08PacketPlayerPosLook.EnumFlags s08packetplayerposlook$enumflags = types[i];
				if (s08packetplayerposlook$enumflags.func_180054_b(parInt1)) {
					enumset.add(s08packetplayerposlook$enumflags);
				}
			}

			return enumset;
		}

		public static int func_180056_a(Set<S08PacketPlayerPosLook.EnumFlags> parSet) {
			int i = 0;

			for (S08PacketPlayerPosLook.EnumFlags s08packetplayerposlook$enumflags : parSet) {
				i |= s08packetplayerposlook$enumflags.func_180055_a();
			}

			return i;
		}
	}
}