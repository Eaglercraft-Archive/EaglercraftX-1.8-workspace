package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.entity.player.PlayerCapabilities;
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
public class C13PacketPlayerAbilities implements Packet<INetHandlerPlayServer> {
	private boolean invulnerable;
	private boolean flying;
	private boolean allowFlying;
	private boolean creativeMode;
	private float flySpeed;
	private float walkSpeed;

	public C13PacketPlayerAbilities() {
	}

	public C13PacketPlayerAbilities(PlayerCapabilities capabilities) {
		this.setInvulnerable(capabilities.disableDamage);
		this.setFlying(capabilities.isFlying);
		this.setAllowFlying(capabilities.allowFlying);
		this.setCreativeMode(capabilities.isCreativeMode);
		this.setFlySpeed(capabilities.getFlySpeed());
		this.setWalkSpeed(capabilities.getWalkSpeed());
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		byte b0 = parPacketBuffer.readByte();
		this.setInvulnerable((b0 & 1) > 0);
		this.setFlying((b0 & 2) > 0);
		this.setAllowFlying((b0 & 4) > 0);
		this.setCreativeMode((b0 & 8) > 0);
		this.setFlySpeed(parPacketBuffer.readFloat());
		this.setWalkSpeed(parPacketBuffer.readFloat());
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		byte b0 = 0;
		if (this.isInvulnerable()) {
			b0 = (byte) (b0 | 1);
		}

		if (this.isFlying()) {
			b0 = (byte) (b0 | 2);
		}

		if (this.isAllowFlying()) {
			b0 = (byte) (b0 | 4);
		}

		if (this.isCreativeMode()) {
			b0 = (byte) (b0 | 8);
		}

		parPacketBuffer.writeByte(b0);
		parPacketBuffer.writeFloat(this.flySpeed);
		parPacketBuffer.writeFloat(this.walkSpeed);
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayServer inethandlerplayserver) {
		inethandlerplayserver.processPlayerAbilities(this);
	}

	public boolean isInvulnerable() {
		return this.invulnerable;
	}

	public void setInvulnerable(boolean isInvulnerable) {
		this.invulnerable = isInvulnerable;
	}

	public boolean isFlying() {
		return this.flying;
	}

	public void setFlying(boolean isFlying) {
		this.flying = isFlying;
	}

	public boolean isAllowFlying() {
		return this.allowFlying;
	}

	public void setAllowFlying(boolean isAllowFlying) {
		this.allowFlying = isAllowFlying;
	}

	public boolean isCreativeMode() {
		return this.creativeMode;
	}

	public void setCreativeMode(boolean isCreativeMode) {
		this.creativeMode = isCreativeMode;
	}

	public void setFlySpeed(float flySpeedIn) {
		this.flySpeed = flySpeedIn;
	}

	public void setWalkSpeed(float walkSpeedIn) {
		this.walkSpeed = walkSpeedIn;
	}
}