package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;

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
public class S3BPacketScoreboardObjective implements Packet<INetHandlerPlayClient> {
	private String objectiveName;
	private String objectiveValue;
	private IScoreObjectiveCriteria.EnumRenderType type;
	private int field_149342_c;

	public S3BPacketScoreboardObjective() {
	}

	public S3BPacketScoreboardObjective(ScoreObjective parScoreObjective, int parInt1) {
		this.objectiveName = parScoreObjective.getName();
		this.objectiveValue = parScoreObjective.getDisplayName();
		this.type = parScoreObjective.getCriteria().getRenderType();
		this.field_149342_c = parInt1;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.objectiveName = parPacketBuffer.readStringFromBuffer(16);
		this.field_149342_c = parPacketBuffer.readByte();
		if (this.field_149342_c == 0 || this.field_149342_c == 2) {
			this.objectiveValue = parPacketBuffer.readStringFromBuffer(32);
			this.type = IScoreObjectiveCriteria.EnumRenderType.func_178795_a(parPacketBuffer.readStringFromBuffer(16));
		}

	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeString(this.objectiveName);
		parPacketBuffer.writeByte(this.field_149342_c);
		if (this.field_149342_c == 0 || this.field_149342_c == 2) {
			parPacketBuffer.writeString(this.objectiveValue);
			parPacketBuffer.writeString(this.type.func_178796_a());
		}

	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleScoreboardObjective(this);
	}

	public String func_149339_c() {
		return this.objectiveName;
	}

	public String func_149337_d() {
		return this.objectiveValue;
	}

	public int func_149338_e() {
		return this.field_149342_c;
	}

	public IScoreObjectiveCriteria.EnumRenderType func_179817_d() {
		return this.type;
	}
}