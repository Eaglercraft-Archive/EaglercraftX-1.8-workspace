package net.minecraft.entity;

import java.util.List;

import net.minecraft.block.BlockFence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
public class EntityLeashKnot extends EntityHanging {
	public EntityLeashKnot(World worldIn) {
		super(worldIn);
	}

	public EntityLeashKnot(World worldIn, BlockPos hangingPositionIn) {
		super(worldIn, hangingPositionIn);
		this.setPosition((double) hangingPositionIn.getX() + 0.5D, (double) hangingPositionIn.getY() + 0.5D,
				(double) hangingPositionIn.getZ() + 0.5D);
		float f = 0.125F;
		float f1 = 0.1875F;
		float f2 = 0.25F;
		this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.1875D, this.posY - 0.25D + 0.125D,
				this.posZ - 0.1875D, this.posX + 0.1875D, this.posY + 0.25D + 0.125D, this.posZ + 0.1875D));
	}

	protected void entityInit() {
		super.entityInit();
	}

	/**+
	 * Updates facing and bounding box based on it
	 */
	public void updateFacingWithBoundingBox(EnumFacing var1) {
	}

	public int getWidthPixels() {
		return 9;
	}

	public int getHeightPixels() {
		return 9;
	}

	public float getEyeHeight() {
		return -0.0625F;
	}

	/**+
	 * Checks if the entity is in range to render by using the past
	 * in distance and comparing it to its average edge length * 64
	 * * renderDistanceWeight Args: distance
	 */
	public boolean isInRangeToRenderDist(double d0) {
		return d0 < 1024.0D;
	}

	/**+
	 * Called when this entity is broken. Entity parameter may be
	 * null.
	 */
	public void onBroken(Entity var1) {
	}

	/**+
	 * Either write this entity to the NBT tag given and return
	 * true, or return false without doing anything. If this returns
	 * false the entity is not saved on disk. Ridden entities return
	 * false here as they are saved with their rider.
	 */
	public boolean writeToNBTOptional(NBTTagCompound var1) {
		return false;
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound var1) {
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound var1) {
	}

	/**+
	 * First layer of player interaction
	 */
	public boolean interactFirst(EntityPlayer entityplayer) {
		ItemStack itemstack = entityplayer.getHeldItem();
		boolean flag = false;
		if (itemstack != null && itemstack.getItem() == Items.lead && !this.worldObj.isRemote) {
			double d0 = 7.0D;

			List<EntityLiving> entities = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(
					this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + d0, this.posZ + d0));
			for (int i = 0, l = entities.size(); i < l; ++i) {
				EntityLiving entityliving = entities.get(i);
				if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == entityplayer) {
					entityliving.setLeashedToEntity(this, true);
					flag = true;
				}
			}
		}

		if (!this.worldObj.isRemote && !flag) {
			this.setDead();
			if (entityplayer.capabilities.isCreativeMode) {
				double d1 = 7.0D;

				List<EntityLiving> entities = this.worldObj.getEntitiesWithinAABB(EntityLiving.class,
						new AxisAlignedBB(this.posX - d1, this.posY - d1, this.posZ - d1, this.posX + d1,
								this.posY + d1, this.posZ + d1));
				for (int i = 0, l = entities.size(); i < l; ++i) {
					EntityLiving entityliving1 = entities.get(i);
					if (entityliving1.getLeashed() && entityliving1.getLeashedToEntity() == this) {
						entityliving1.clearLeashed(true, false);
					}
				}
			}
		}

		return true;
	}

	/**+
	 * checks to make sure painting can be placed there
	 */
	public boolean onValidSurface() {
		return this.worldObj.getBlockState(this.hangingPosition).getBlock() instanceof BlockFence;
	}

	public static EntityLeashKnot createKnot(World worldIn, BlockPos fence) {
		EntityLeashKnot entityleashknot = new EntityLeashKnot(worldIn, fence);
		entityleashknot.forceSpawn = true;
		worldIn.spawnEntityInWorld(entityleashknot);
		return entityleashknot;
	}

	public static EntityLeashKnot getKnotForPosition(World worldIn, BlockPos pos) {
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();

		List<EntityLeashKnot> entities = worldIn.getEntitiesWithinAABB(EntityLeashKnot.class,
				new AxisAlignedBB((double) i - 1.0D, (double) j - 1.0D, (double) k - 1.0D, (double) i + 1.0D,
						(double) j + 1.0D, (double) k + 1.0D));
		for (int m = 0, l = entities.size(); m < l; ++m) {
			EntityLeashKnot entityleashknot = entities.get(m);
			if (entityleashknot.getHangingPosition().equals(pos)) {
				return entityleashknot;
			}
		}

		return null;
	}
}