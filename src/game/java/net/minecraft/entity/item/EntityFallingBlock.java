package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
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
public class EntityFallingBlock extends Entity {
	private IBlockState fallTile;
	public int fallTime;
	public boolean shouldDropItem = true;
	private boolean canSetAsBlock;
	private boolean hurtEntities;
	private int fallHurtMax = 40;
	private float fallHurtAmount = 2.0F;
	public NBTTagCompound tileEntityData;

	public EntityFallingBlock(World worldIn) {
		super(worldIn);
	}

	public EntityFallingBlock(World worldIn, double x, double y, double z, IBlockState fallingBlockState) {
		super(worldIn);
		this.fallTile = fallingBlockState;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.setPosition(x, y, z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
	}

	/**+
	 * returns if this entity triggers Block.onEntityWalking on the
	 * blocks they walk on. used for spiders and wolves to prevent
	 * them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
	}

	/**+
	 * Returns true if other Entities should be prevented from
	 * moving through this Entity.
	 */
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		Block block = this.fallTile.getBlock();
		if (block.getMaterial() == Material.air) {
			this.setDead();
		} else {
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			if (this.fallTime++ == 0) {
				BlockPos blockpos = new BlockPos(this);
				if (this.worldObj.getBlockState(blockpos).getBlock() == block) {
					this.worldObj.setBlockToAir(blockpos);
				} else if (!this.worldObj.isRemote) {
					this.setDead();
					return;
				}
			}

			this.motionY -= 0.03999999910593033D;
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.9800000190734863D;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= 0.9800000190734863D;
			if (!this.worldObj.isRemote) {
				BlockPos blockpos1 = new BlockPos(this);
				if (this.onGround) {
					this.motionX *= 0.699999988079071D;
					this.motionZ *= 0.699999988079071D;
					this.motionY *= -0.5D;
					if (this.worldObj.getBlockState(blockpos1).getBlock() != Blocks.piston_extension) {
						this.setDead();
						if (!this.canSetAsBlock) {
							if (this.worldObj.canBlockBePlaced(block, blockpos1, true, EnumFacing.UP, (Entity) null,
									(ItemStack) null) && !BlockFalling.canFallInto(this.worldObj, blockpos1.down())
									&& this.worldObj.setBlockState(blockpos1, this.fallTile, 3)) {
								if (block instanceof BlockFalling) {
									((BlockFalling) block).onEndFalling(this.worldObj, blockpos1);
								}

								if (this.tileEntityData != null && block instanceof ITileEntityProvider) {
									TileEntity tileentity = this.worldObj.getTileEntity(blockpos1);
									if (tileentity != null) {
										NBTTagCompound nbttagcompound = new NBTTagCompound();
										tileentity.writeToNBT(nbttagcompound);

										for (String s : this.tileEntityData.getKeySet()) {
											NBTBase nbtbase = this.tileEntityData.getTag(s);
											if (!s.equals("x") && !s.equals("y") && !s.equals("z")) {
												nbttagcompound.setTag(s, nbtbase.copy());
											}
										}

										tileentity.readFromNBT(nbttagcompound);
										tileentity.markDirty();
									}
								}
							} else if (this.shouldDropItem
									&& this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
								this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
							}
						}
					}
				} else if (this.fallTime > 100 && !this.worldObj.isRemote
						&& (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600) {
					if (this.shouldDropItem && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
						this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
					}

					this.setDead();
				}
			}

		}
	}

	public void fall(float f, float var2) {
		Block block = this.fallTile.getBlock();
		if (this.hurtEntities) {
			int i = MathHelper.ceiling_float_int(f - 1.0F);
			if (i > 0) {
				ArrayList<Entity> arraylist = Lists.newArrayList(
						this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
				boolean flag = block == Blocks.anvil;
				DamageSource damagesource = flag ? DamageSource.anvil : DamageSource.fallingBlock;

				for (int j = 0, l = arraylist.size(); j < l; ++j) {
					arraylist.get(j).attackEntityFrom(damagesource, (float) Math
							.min(MathHelper.floor_float((float) i * this.fallHurtAmount), this.fallHurtMax));
				}

				if (flag && (double) this.rand.nextFloat() < 0.05000000074505806D + (double) i * 0.05D) {
					int j = ((Integer) this.fallTile.getValue(BlockAnvil.DAMAGE)).intValue();
					++j;
					if (j > 2) {
						this.canSetAsBlock = true;
					} else {
						this.fallTile = this.fallTile.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(j));
					}
				}
			}
		}

	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		Block block = this.fallTile != null ? this.fallTile.getBlock() : Blocks.air;
		ResourceLocation resourcelocation = (ResourceLocation) Block.blockRegistry.getNameForObject(block);
		nbttagcompound.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());
		nbttagcompound.setByte("Data", (byte) block.getMetaFromState(this.fallTile));
		nbttagcompound.setByte("Time", (byte) this.fallTime);
		nbttagcompound.setBoolean("DropItem", this.shouldDropItem);
		nbttagcompound.setBoolean("HurtEntities", this.hurtEntities);
		nbttagcompound.setFloat("FallHurtAmount", this.fallHurtAmount);
		nbttagcompound.setInteger("FallHurtMax", this.fallHurtMax);
		if (this.tileEntityData != null) {
			nbttagcompound.setTag("TileEntityData", this.tileEntityData);
		}

	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		int i = nbttagcompound.getByte("Data") & 255;
		if (nbttagcompound.hasKey("Block", 8)) {
			this.fallTile = Block.getBlockFromName(nbttagcompound.getString("Block")).getStateFromMeta(i);
		} else if (nbttagcompound.hasKey("TileID", 99)) {
			this.fallTile = Block.getBlockById(nbttagcompound.getInteger("TileID")).getStateFromMeta(i);
		} else {
			this.fallTile = Block.getBlockById(nbttagcompound.getByte("Tile") & 255).getStateFromMeta(i);
		}

		this.fallTime = nbttagcompound.getByte("Time") & 255;
		Block block = this.fallTile.getBlock();
		if (nbttagcompound.hasKey("HurtEntities", 99)) {
			this.hurtEntities = nbttagcompound.getBoolean("HurtEntities");
			this.fallHurtAmount = nbttagcompound.getFloat("FallHurtAmount");
			this.fallHurtMax = nbttagcompound.getInteger("FallHurtMax");
		} else if (block == Blocks.anvil) {
			this.hurtEntities = true;
		}

		if (nbttagcompound.hasKey("DropItem", 99)) {
			this.shouldDropItem = nbttagcompound.getBoolean("DropItem");
		}

		if (nbttagcompound.hasKey("TileEntityData", 10)) {
			this.tileEntityData = nbttagcompound.getCompoundTag("TileEntityData");
		}

		if (block == null || block.getMaterial() == Material.air) {
			this.fallTile = Blocks.sand.getDefaultState();
		}

	}

	public World getWorldObj() {
		return this.worldObj;
	}

	public void setHurtEntities(boolean parFlag) {
		this.hurtEntities = parFlag;
	}

	/**+
	 * Return whether this entity should be rendered as on fire.
	 */
	public boolean canRenderOnFire() {
		return false;
	}

	public void addEntityCrashInfo(CrashReportCategory crashreportcategory) {
		super.addEntityCrashInfo(crashreportcategory);
		if (this.fallTile != null) {
			Block block = this.fallTile.getBlock();
			crashreportcategory.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(block)));
			crashreportcategory.addCrashSection("Immitating block data",
					Integer.valueOf(block.getMetaFromState(this.fallTile)));
		}

	}

	public IBlockState getBlock() {
		return this.fallTile;
	}
}