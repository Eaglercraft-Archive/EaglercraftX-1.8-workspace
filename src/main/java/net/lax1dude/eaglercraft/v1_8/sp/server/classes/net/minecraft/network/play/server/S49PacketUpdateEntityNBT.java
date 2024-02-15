package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.play.server;

import java.io.IOException;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.Packet;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.PacketBuffer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.play.INetHandlerPlayClient;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.World;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class S49PacketUpdateEntityNBT implements Packet<INetHandlerPlayClient> {

	static {
		__checkIntegratedContextValid("net/minecraft/network/play/server/S49PacketUpdateEntityNBT");
	}

	private int entityId;
	private NBTTagCompound tagCompound;

	public S49PacketUpdateEntityNBT() {
	}

	public S49PacketUpdateEntityNBT(int entityIdIn, NBTTagCompound tagCompoundIn) {
		this.entityId = entityIdIn;
		this.tagCompound = tagCompoundIn;
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.entityId = parPacketBuffer.readVarIntFromBuffer();
		this.tagCompound = parPacketBuffer.readNBTTagCompoundFromBuffer();
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeVarIntToBuffer(this.entityId);
		parPacketBuffer.writeNBTTagCompoundToBuffer(this.tagCompound);
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleEntityNBT(this);
	}

	public NBTTagCompound getTagCompound() {
		return this.tagCompound;
	}

	public Entity getEntity(World worldIn) {
		return worldIn.getEntityByID(this.entityId);
	}
}