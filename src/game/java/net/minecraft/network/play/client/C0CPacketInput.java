package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

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
public class C0CPacketInput implements Packet<INetHandlerPlayServer> {
	private float strafeSpeed;
	private float forwardSpeed;
	private boolean jumping;
	private boolean sneaking;

	public C0CPacketInput() {
	}

	public C0CPacketInput(float strafeSpeed, float forwardSpeed, boolean jumping, boolean sneaking) {
		this.strafeSpeed = strafeSpeed;
		this.forwardSpeed = forwardSpeed;
		this.jumping = jumping;
		this.sneaking = sneaking;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.strafeSpeed = parPacketBuffer.readFloat();
		this.forwardSpeed = parPacketBuffer.readFloat();
		byte b0 = parPacketBuffer.readByte();
		this.jumping = (b0 & 1) > 0;
		this.sneaking = (b0 & 2) > 0;
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeFloat(this.strafeSpeed);
		parPacketBuffer.writeFloat(this.forwardSpeed);
		byte b0 = 0;
		if (this.jumping) {
			b0 = (byte) (b0 | 1);
		}

		if (this.sneaking) {
			b0 = (byte) (b0 | 2);
		}

		parPacketBuffer.writeByte(b0);
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayServer inethandlerplayserver) {
		inethandlerplayserver.processInput(this);
	}

	public float getStrafeSpeed() {
		return this.strafeSpeed;
	}

	public float getForwardSpeed() {
		return this.forwardSpeed;
	}

	public boolean isJumping() {
		return this.jumping;
	}

	public boolean isSneaking() {
		return this.sneaking;
	}
}