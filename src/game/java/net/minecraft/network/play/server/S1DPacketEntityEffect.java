package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.PotionEffect;

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
public class S1DPacketEntityEffect implements Packet<INetHandlerPlayClient> {
	private int entityId;
	private byte effectId;
	private byte amplifier;
	private int duration;
	private byte hideParticles;

	public S1DPacketEntityEffect() {
	}

	public S1DPacketEntityEffect(int entityIdIn, PotionEffect effect) {
		this.entityId = entityIdIn;
		this.effectId = (byte) (effect.getPotionID() & 255);
		this.amplifier = (byte) (effect.getAmplifier() & 255);
		if (effect.getDuration() > 32767) {
			this.duration = 32767;
		} else {
			this.duration = effect.getDuration();
		}

		this.hideParticles = (byte) (effect.getIsShowParticles() ? 1 : 0);
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.entityId = parPacketBuffer.readVarIntFromBuffer();
		this.effectId = parPacketBuffer.readByte();
		this.amplifier = parPacketBuffer.readByte();
		this.duration = parPacketBuffer.readVarIntFromBuffer();
		this.hideParticles = parPacketBuffer.readByte();
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeVarIntToBuffer(this.entityId);
		parPacketBuffer.writeByte(this.effectId);
		parPacketBuffer.writeByte(this.amplifier);
		parPacketBuffer.writeVarIntToBuffer(this.duration);
		parPacketBuffer.writeByte(this.hideParticles);
	}

	public boolean func_149429_c() {
		return this.duration == 32767;
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleEntityEffect(this);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public byte getEffectId() {
		return this.effectId;
	}

	public byte getAmplifier() {
		return this.amplifier;
	}

	public int getDuration() {
		return this.duration;
	}

	public boolean func_179707_f() {
		return this.hideParticles != 0;
	}
}