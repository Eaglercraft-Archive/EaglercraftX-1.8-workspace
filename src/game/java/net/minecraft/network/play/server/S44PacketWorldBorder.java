package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.border.WorldBorder;

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
public class S44PacketWorldBorder implements Packet<INetHandlerPlayClient> {
	private S44PacketWorldBorder.Action action;
	private int size;
	private double centerX;
	private double centerZ;
	private double targetSize;
	private double diameter;
	private long timeUntilTarget;
	private int warningTime;
	private int warningDistance;

	public S44PacketWorldBorder() {
	}

	public S44PacketWorldBorder(WorldBorder border, S44PacketWorldBorder.Action actionIn) {
		this.action = actionIn;
		this.centerX = border.getCenterX();
		this.centerZ = border.getCenterZ();
		this.diameter = border.getDiameter();
		this.targetSize = border.getTargetSize();
		this.timeUntilTarget = border.getTimeUntilTarget();
		this.size = border.getSize();
		this.warningDistance = border.getWarningDistance();
		this.warningTime = border.getWarningTime();
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.action = (S44PacketWorldBorder.Action) parPacketBuffer.readEnumValue(S44PacketWorldBorder.Action.class);
		switch (this.action) {
		case SET_SIZE:
			this.targetSize = parPacketBuffer.readDouble();
			break;
		case LERP_SIZE:
			this.diameter = parPacketBuffer.readDouble();
			this.targetSize = parPacketBuffer.readDouble();
			this.timeUntilTarget = parPacketBuffer.readVarLong();
			break;
		case SET_CENTER:
			this.centerX = parPacketBuffer.readDouble();
			this.centerZ = parPacketBuffer.readDouble();
			break;
		case SET_WARNING_BLOCKS:
			this.warningDistance = parPacketBuffer.readVarIntFromBuffer();
			break;
		case SET_WARNING_TIME:
			this.warningTime = parPacketBuffer.readVarIntFromBuffer();
			break;
		case INITIALIZE:
			this.centerX = parPacketBuffer.readDouble();
			this.centerZ = parPacketBuffer.readDouble();
			this.diameter = parPacketBuffer.readDouble();
			this.targetSize = parPacketBuffer.readDouble();
			this.timeUntilTarget = parPacketBuffer.readVarLong();
			this.size = parPacketBuffer.readVarIntFromBuffer();
			this.warningDistance = parPacketBuffer.readVarIntFromBuffer();
			this.warningTime = parPacketBuffer.readVarIntFromBuffer();
		}

	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeEnumValue(this.action);
		switch (this.action) {
		case SET_SIZE:
			parPacketBuffer.writeDouble(this.targetSize);
			break;
		case LERP_SIZE:
			parPacketBuffer.writeDouble(this.diameter);
			parPacketBuffer.writeDouble(this.targetSize);
			parPacketBuffer.writeVarLong(this.timeUntilTarget);
			break;
		case SET_CENTER:
			parPacketBuffer.writeDouble(this.centerX);
			parPacketBuffer.writeDouble(this.centerZ);
			break;
		case SET_WARNING_BLOCKS:
			parPacketBuffer.writeVarIntToBuffer(this.warningDistance);
			break;
		case SET_WARNING_TIME:
			parPacketBuffer.writeVarIntToBuffer(this.warningTime);
			break;
		case INITIALIZE:
			parPacketBuffer.writeDouble(this.centerX);
			parPacketBuffer.writeDouble(this.centerZ);
			parPacketBuffer.writeDouble(this.diameter);
			parPacketBuffer.writeDouble(this.targetSize);
			parPacketBuffer.writeVarLong(this.timeUntilTarget);
			parPacketBuffer.writeVarIntToBuffer(this.size);
			parPacketBuffer.writeVarIntToBuffer(this.warningDistance);
			parPacketBuffer.writeVarIntToBuffer(this.warningTime);
		}

	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleWorldBorder(this);
	}

	public void func_179788_a(WorldBorder border) {
		switch (this.action) {
		case SET_SIZE:
			border.setTransition(this.targetSize);
			break;
		case LERP_SIZE:
			border.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
			break;
		case SET_CENTER:
			border.setCenter(this.centerX, this.centerZ);
			break;
		case SET_WARNING_BLOCKS:
			border.setWarningDistance(this.warningDistance);
			break;
		case SET_WARNING_TIME:
			border.setWarningTime(this.warningTime);
			break;
		case INITIALIZE:
			border.setCenter(this.centerX, this.centerZ);
			if (this.timeUntilTarget > 0L) {
				border.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
			} else {
				border.setTransition(this.targetSize);
			}

			border.setSize(this.size);
			border.setWarningDistance(this.warningDistance);
			border.setWarningTime(this.warningTime);
		}

	}

	public static enum Action {
		SET_SIZE, LERP_SIZE, SET_CENTER, INITIALIZE, SET_WARNING_TIME, SET_WARNING_BLOCKS;
	}
}