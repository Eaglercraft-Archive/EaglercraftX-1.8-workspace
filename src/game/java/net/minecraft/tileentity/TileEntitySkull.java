package net.minecraft.tileentity;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.StringUtils;

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
public class TileEntitySkull extends TileEntity {
	private int skullType;
	private int skullRotation;
	private GameProfile playerProfile = null;

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setByte("SkullType", (byte) (this.skullType & 255));
		nbttagcompound.setByte("Rot", (byte) (this.skullRotation & 255));
		if (this.playerProfile != null) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			NBTUtil.writeGameProfile(nbttagcompound1, this.playerProfile);
			nbttagcompound.setTag("Owner", nbttagcompound1);
		}

	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.skullType = nbttagcompound.getByte("SkullType");
		this.skullRotation = nbttagcompound.getByte("Rot");
		if (this.skullType == 3) {
			if (nbttagcompound.hasKey("Owner", 10)) {
				this.playerProfile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("Owner"));
			} else if (nbttagcompound.hasKey("ExtraType", 8)) {
				String s = nbttagcompound.getString("ExtraType");
				if (!StringUtils.isNullOrEmpty(s)) {
					this.playerProfile = new GameProfile((EaglercraftUUID) null, s);
					this.updatePlayerProfile();
				}
			}
		}

	}

	public GameProfile getPlayerProfile() {
		return this.playerProfile;
	}

	/**+
	 * Allows for a specialized description packet to be created.
	 * This is often used to sync tile entity data from the server
	 * to the client easily. For example this is used by signs to
	 * synchronise the text to be displayed.
	 */
	public Packet getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(this.pos, 4, nbttagcompound);
	}

	public void setType(int type) {
		this.skullType = type;
		this.playerProfile = null;
	}

	public void setPlayerProfile(GameProfile playerProfile) {
		this.skullType = 3;
		this.playerProfile = playerProfile;
		this.updatePlayerProfile();
	}

	private void updatePlayerProfile() {
		this.playerProfile = updateGameprofile(this.playerProfile);
		this.markDirty();
	}

	public static GameProfile updateGameprofile(GameProfile input) {
		return input;
	}

	public int getSkullType() {
		return this.skullType;
	}

	public int getSkullRotation() {
		return this.skullRotation;
	}

	public void setSkullRotation(int rotation) {
		this.skullRotation = rotation;
	}
}