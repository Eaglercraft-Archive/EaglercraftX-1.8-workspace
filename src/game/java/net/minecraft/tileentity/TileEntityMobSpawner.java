package net.minecraft.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

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
public class TileEntityMobSpawner extends TileEntity implements ITickable {
	private final MobSpawnerBaseLogic spawnerLogic = new MobSpawnerBaseLogic() {
		public void func_98267_a(int i) {
			TileEntityMobSpawner.this.worldObj.addBlockEvent(TileEntityMobSpawner.this.pos, Blocks.mob_spawner, i, 0);
		}

		public World getSpawnerWorld() {
			return TileEntityMobSpawner.this.worldObj;
		}

		public BlockPos getSpawnerPosition() {
			return TileEntityMobSpawner.this.pos;
		}

		public void setRandomEntity(
				MobSpawnerBaseLogic.WeightedRandomMinecart mobspawnerbaselogic$weightedrandomminecart) {
			super.setRandomEntity(mobspawnerbaselogic$weightedrandomminecart);
			if (this.getSpawnerWorld() != null) {
				this.getSpawnerWorld().markBlockForUpdate(TileEntityMobSpawner.this.pos);
			}

		}
	};

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.spawnerLogic.readFromNBT(nbttagcompound);
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		this.spawnerLogic.writeToNBT(nbttagcompound);
	}

	/**+
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		this.spawnerLogic.updateSpawner();
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
		nbttagcompound.removeTag("SpawnPotentials");
		return new S35PacketUpdateTileEntity(this.pos, 1, nbttagcompound);
	}

	public boolean receiveClientEvent(int i, int j) {
		return this.spawnerLogic.setDelayToMin(i) ? true : super.receiveClientEvent(i, j);
	}

	public boolean func_183000_F() {
		return true;
	}

	public MobSpawnerBaseLogic getSpawnerBaseLogic() {
		return this.spawnerLogic;
	}
}