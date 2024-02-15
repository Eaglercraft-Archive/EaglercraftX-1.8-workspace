package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.play.server;

import java.io.IOException;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemStack;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.Packet;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.PacketBuffer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.play.INetHandlerPlayClient;

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
public class S2FPacketSetSlot implements Packet<INetHandlerPlayClient> {

	static {
		__checkIntegratedContextValid("net/minecraft/network/play/server/S2FPacketSetSlot");
	}

	private int windowId;
	private int slot;
	private ItemStack item;

	public S2FPacketSetSlot() {
	}

	public S2FPacketSetSlot(int windowIdIn, int slotIn, ItemStack itemIn) {
		this.windowId = windowIdIn;
		this.slot = slotIn;
		this.item = itemIn == null ? null : itemIn.copy();
	}

	/**+
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient inethandlerplayclient) {
		inethandlerplayclient.handleSetSlot(this);
	}

	/**+
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer parPacketBuffer) throws IOException {
		this.windowId = parPacketBuffer.readByte();
		this.slot = parPacketBuffer.readShort();
		this.item = parPacketBuffer.readItemStackFromBuffer_server();
	}

	/**+
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer parPacketBuffer) throws IOException {
		parPacketBuffer.writeByte(this.windowId);
		parPacketBuffer.writeShort(this.slot);
		parPacketBuffer.writeItemStackToBuffer_server(this.item);
	}

	public int func_149175_c() {
		return this.windowId;
	}

	public int func_149173_d() {
		return this.slot;
	}

	public ItemStack func_149174_e() {
		return this.item;
	}
}