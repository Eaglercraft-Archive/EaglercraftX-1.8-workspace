package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

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
public class S27PacketExplosion implements Packet<INetHandlerPlayClient> {
	private double posX;
	private double posY;
	private double posZ;
	private float strength;
	private List<BlockPos> affectedBlockPositions;
	private float field_149152_f;
	private float field_149153_g;
	private float field_149159_h;

	public S27PacketExplosion() {
	}

	public S27PacketExplosion(double parDouble1, double y, double z, float strengthIn, List<BlockPos> affectedBlocksIn,
			Vec3 parVec3_1) {
		this.posX = parDouble1;
		this.posY = y;
		this.posZ = z;
		this.strength = strengthIn;
		this.affectedBlockPositions = Lists.newArrayList(affectedBlocksIn);
		if (parVec3_1 != null) {
			this.field_149152_f = (float) parVec3_1.xCoord;
			this.field_149153_g = (float) parVec3_1.yCoord;
			this.field_149159_h = (float) parVec3_1.zCoord;
		}

	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.posX = (double) parPacketBuffer.readFloat();
		this.posY = (double) parPacketBuffer.readFloat();
		this.posZ = (double) parPacketBuffer.readFloat();
		this.strength = parPacketBuffer.readFloat();
		int i = parPacketBuffer.readInt();
		this.affectedBlockPositions = Lists.newArrayListWithCapacity(i);
		int j = (int) this.posX;
		int k = (int) this.posY;
		int l = (int) this.posZ;

		for (int i1 = 0; i1 < i; ++i1) {
			int j1 = parPacketBuffer.readByte() + j;
			int k1 = parPacketBuffer.readByte() + k;
			int l1 = parPacketBuffer.readByte() + l;
			this.affectedBlockPositions.add(new BlockPos(j1, k1, l1));
		}

		this.field_149152_f = parPacketBuffer.readFloat();
		this.field_149153_g = parPacketBuffer.readFloat();
		this.field_149159_h = parPacketBuffer.readFloat();
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeFloat((float) this.posX);
		parPacketBuffer.writeFloat((float) this.posY);
		parPacketBuffer.writeFloat((float) this.posZ);
		parPacketBuffer.writeFloat(this.strength);
		parPacketBuffer.writeInt(this.affectedBlockPositions.size());
		int i = (int) this.posX;
		int j = (int) this.posY;
		int k = (int) this.posZ;

		for (int m = 0, n = this.affectedBlockPositions.size(); m < n; ++m) {
			BlockPos blockpos = this.affectedBlockPositions.get(m);
			int l = blockpos.getX() - i;
			int i1 = blockpos.getY() - j;
			int j1 = blockpos.getZ() - k;
			parPacketBuffer.writeByte(l);
			parPacketBuffer.writeByte(i1);
			parPacketBuffer.writeByte(j1);
		}

		parPacketBuffer.writeFloat(this.field_149152_f);
		parPacketBuffer.writeFloat(this.field_149153_g);
		parPacketBuffer.writeFloat(this.field_149159_h);
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleExplosion(this);
	}

	public float func_149149_c() {
		return this.field_149152_f;
	}

	public float func_149144_d() {
		return this.field_149153_g;
	}

	public float func_149147_e() {
		return this.field_149159_h;
	}

	public double getX() {
		return this.posX;
	}

	public double getY() {
		return this.posY;
	}

	public double getZ() {
		return this.posZ;
	}

	public float getStrength() {
		return this.strength;
	}

	public List<BlockPos> getAffectedBlockPositions() {
		return this.affectedBlockPositions;
	}
}