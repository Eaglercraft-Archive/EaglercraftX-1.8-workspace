package net.minecraft.entity.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.BlockPos;
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
public class EntityMinecartMobSpawner extends EntityMinecart {
	/**+
	 * Mob spawner logic for this spawner minecart.
	 */
	private final MobSpawnerBaseLogic mobSpawnerLogic = new MobSpawnerBaseLogic() {
		public void func_98267_a(int i) {
			EntityMinecartMobSpawner.this.worldObj.setEntityState(EntityMinecartMobSpawner.this, (byte) i);
		}

		public World getSpawnerWorld() {
			return EntityMinecartMobSpawner.this.worldObj;
		}

		public BlockPos getSpawnerPosition() {
			return new BlockPos(EntityMinecartMobSpawner.this);
		}
	};

	public EntityMinecartMobSpawner(World worldIn) {
		super(worldIn);
	}

	public EntityMinecartMobSpawner(World worldIn, double parDouble1, double parDouble2, double parDouble3) {
		super(worldIn, parDouble1, parDouble2, parDouble3);
	}

	public EntityMinecart.EnumMinecartType getMinecartType() {
		return EntityMinecart.EnumMinecartType.SPAWNER;
	}

	public IBlockState getDefaultDisplayTile() {
		return Blocks.mob_spawner.getDefaultState();
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.mobSpawnerLogic.readFromNBT(nbttagcompound);
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		this.mobSpawnerLogic.writeToNBT(nbttagcompound);
	}

	public void handleStatusUpdate(byte b0) {
		this.mobSpawnerLogic.setDelayToMin(b0);
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		this.mobSpawnerLogic.updateSpawner();
	}

	public MobSpawnerBaseLogic func_98039_d() {
		return this.mobSpawnerLogic;
	}
}