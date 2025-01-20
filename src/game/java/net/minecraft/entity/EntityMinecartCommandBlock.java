package net.minecraft.entity;

import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
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
public class EntityMinecartCommandBlock extends EntityMinecart {
	private final CommandBlockLogic commandBlockLogic = new CommandBlockLogic() {
		public void updateCommand() {
			EntityMinecartCommandBlock.this.getDataWatcher().updateObject(23, this.getCommand());
			EntityMinecartCommandBlock.this.getDataWatcher().updateObject(24,
					IChatComponent.Serializer.componentToJson(this.getLastOutput()));
		}

		public int func_145751_f() {
			return 1;
		}

		public void func_145757_a(ByteBuf bytebuf) {
			bytebuf.writeInt(EntityMinecartCommandBlock.this.getEntityId());
		}

		public BlockPos getPosition() {
			return new BlockPos(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY + 0.5D,
					EntityMinecartCommandBlock.this.posZ);
		}

		public Vec3 getPositionVector() {
			return new Vec3(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY,
					EntityMinecartCommandBlock.this.posZ);
		}

		public World getEntityWorld() {
			return EntityMinecartCommandBlock.this.worldObj;
		}

		public Entity getCommandSenderEntity() {
			return EntityMinecartCommandBlock.this;
		}
	};
	/**+
	 * Cooldown before command block logic runs again in ticks
	 */
	private int activatorRailCooldown = 0;

	public EntityMinecartCommandBlock(World worldIn) {
		super(worldIn);
	}

	public EntityMinecartCommandBlock(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	protected void entityInit() {
		super.entityInit();
		this.getDataWatcher().addObject(23, "");
		this.getDataWatcher().addObject(24, "");
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.commandBlockLogic.readDataFromNBT(nbttagcompound);
		this.getDataWatcher().updateObject(23, this.getCommandBlockLogic().getCommand());
		this.getDataWatcher().updateObject(24,
				IChatComponent.Serializer.componentToJson(this.getCommandBlockLogic().getLastOutput()));
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		this.commandBlockLogic.writeDataToNBT(nbttagcompound);
	}

	public EntityMinecart.EnumMinecartType getMinecartType() {
		return EntityMinecart.EnumMinecartType.COMMAND_BLOCK;
	}

	public IBlockState getDefaultDisplayTile() {
		return Blocks.command_block.getDefaultState();
	}

	public CommandBlockLogic getCommandBlockLogic() {
		return this.commandBlockLogic;
	}

	/**+
	 * Called every tick the minecart is on an activator rail. Args:
	 * x, y, z, is the rail receiving power
	 */
	public void onActivatorRailPass(int var1, int var2, int var3, boolean flag) {
		if (flag && this.ticksExisted - this.activatorRailCooldown >= 4) {
			this.getCommandBlockLogic().trigger(this.worldObj);
			this.activatorRailCooldown = this.ticksExisted;
		}

	}

	/**+
	 * First layer of player interaction
	 */
	public boolean interactFirst(EntityPlayer entityplayer) {
		this.commandBlockLogic.tryOpenEditCommandBlock(entityplayer);
		return false;
	}

	public void onDataWatcherUpdate(int i) {
		super.onDataWatcherUpdate(i);
		if (i == 24) {
			try {
				this.commandBlockLogic.setLastOutput(
						IChatComponent.Serializer.jsonToComponent(this.getDataWatcher().getWatchableObjectString(24)));
			} catch (Throwable var3) {
				;
			}
		} else if (i == 23) {
			this.commandBlockLogic.setCommand(this.getDataWatcher().getWatchableObjectString(23));
		}

	}
}