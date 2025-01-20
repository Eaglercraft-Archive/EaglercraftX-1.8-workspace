package net.minecraft.network.play.server;

import java.io.IOException;

import org.apache.commons.lang3.Validate;

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
public class S29PacketSoundEffect implements Packet<INetHandlerPlayClient> {
	private String soundName;
	private int posX;
	private int posY = Integer.MAX_VALUE;
	private int posZ;
	private float soundVolume;
	private int soundPitch;

	public S29PacketSoundEffect() {
	}

	public S29PacketSoundEffect(String soundNameIn, double soundX, double soundY, double soundZ, float volume,
			float pitch) {
		Validate.notNull(soundNameIn, "name", new Object[0]);
		this.soundName = soundNameIn;
		this.posX = (int) (soundX * 8.0D);
		this.posY = (int) (soundY * 8.0D);
		this.posZ = (int) (soundZ * 8.0D);
		this.soundVolume = volume;
		this.soundPitch = (int) (pitch * 63.0F);
		pitch = MathHelper.clamp_float(pitch, 0.0F, 255.0F);
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.soundName = parPacketBuffer.readStringFromBuffer(256);
		this.posX = parPacketBuffer.readInt();
		this.posY = parPacketBuffer.readInt();
		this.posZ = parPacketBuffer.readInt();
		this.soundVolume = parPacketBuffer.readFloat();
		this.soundPitch = parPacketBuffer.readUnsignedByte();
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeString(this.soundName);
		parPacketBuffer.writeInt(this.posX);
		parPacketBuffer.writeInt(this.posY);
		parPacketBuffer.writeInt(this.posZ);
		parPacketBuffer.writeFloat(this.soundVolume);
		parPacketBuffer.writeByte(this.soundPitch);
	}

	public String getSoundName() {
		return this.soundName;
	}

	public double getX() {
		return (double) ((float) this.posX / 8.0F);
	}

	public double getY() {
		return (double) ((float) this.posY / 8.0F);
	}

	public double getZ() {
		return (double) ((float) this.posZ / 8.0F);
	}

	public float getVolume() {
		return this.soundVolume;
	}

	public float getPitch() {
		return (float) this.soundPitch / 63.0F;
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleSoundEffect(this);
	}
}