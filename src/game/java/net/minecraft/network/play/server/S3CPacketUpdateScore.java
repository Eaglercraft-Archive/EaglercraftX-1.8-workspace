package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.Score;
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
public class S3CPacketUpdateScore implements Packet<INetHandlerPlayClient> {
	private String name = "";
	private String objective = "";
	private int value;
	private S3CPacketUpdateScore.Action action;

	public S3CPacketUpdateScore() {
	}

	public S3CPacketUpdateScore(Score scoreIn) {
		this.name = scoreIn.getPlayerName();
		this.objective = scoreIn.getObjective().getName();
		this.value = scoreIn.getScorePoints();
		this.action = S3CPacketUpdateScore.Action.CHANGE;
	}

	public S3CPacketUpdateScore(String nameIn) {
		this.name = nameIn;
		this.objective = "";
		this.value = 0;
		this.action = S3CPacketUpdateScore.Action.REMOVE;
	}

	public S3CPacketUpdateScore(String nameIn, ScoreObjective objectiveIn) {
		this.name = nameIn;
		this.objective = objectiveIn.getName();
		this.value = 0;
		this.action = S3CPacketUpdateScore.Action.REMOVE;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.name = parPacketBuffer.readStringFromBuffer(40);
		this.action = (S3CPacketUpdateScore.Action) parPacketBuffer.readEnumValue(S3CPacketUpdateScore.Action.class);
		this.objective = parPacketBuffer.readStringFromBuffer(16);
		if (this.action != S3CPacketUpdateScore.Action.REMOVE) {
			this.value = parPacketBuffer.readVarIntFromBuffer();
		}

	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeString(this.name);
		parPacketBuffer.writeEnumValue(this.action);
		parPacketBuffer.writeString(this.objective);
		if (this.action != S3CPacketUpdateScore.Action.REMOVE) {
			parPacketBuffer.writeVarIntToBuffer(this.value);
		}

	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleUpdateScore(this);
	}

	public String getPlayerName() {
		return this.name;
	}

	public String getObjectiveName() {
		return this.objective;
	}

	public int getScoreValue() {
		return this.value;
	}

	public S3CPacketUpdateScore.Action getScoreAction() {
		return this.action;
	}

	public static enum Action {
		CHANGE, REMOVE;
	}
}