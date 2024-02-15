package net.minecraft.entity.projectile;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;

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
public class EntityFishHook extends Entity {
	private static final List<WeightedRandomFishable> JUNK = Arrays.asList(new WeightedRandomFishable[] {
			(new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10)).setMaxDamagePercent(0.9F),
			new WeightedRandomFishable(new ItemStack(Items.leather), 10),
			new WeightedRandomFishable(new ItemStack(Items.bone), 10),
			new WeightedRandomFishable(new ItemStack(Items.potionitem), 10),
			new WeightedRandomFishable(new ItemStack(Items.string), 5),
			(new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2)).setMaxDamagePercent(0.9F),
			new WeightedRandomFishable(new ItemStack(Items.bowl), 10),
			new WeightedRandomFishable(new ItemStack(Items.stick), 5),
			new WeightedRandomFishable(new ItemStack(Items.dye, 10, EnumDyeColor.BLACK.getDyeDamage()), 1),
			new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10),
			new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10) });
	private static final List<WeightedRandomFishable> TREASURE = Arrays.asList(new WeightedRandomFishable[] {
			new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1),
			new WeightedRandomFishable(new ItemStack(Items.name_tag), 1),
			new WeightedRandomFishable(new ItemStack(Items.saddle), 1),
			(new WeightedRandomFishable(new ItemStack(Items.bow), 1)).setMaxDamagePercent(0.25F).setEnchantable(),
			(new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1)).setMaxDamagePercent(0.25F)
					.setEnchantable(),
			(new WeightedRandomFishable(new ItemStack(Items.book), 1)).setEnchantable() });
	private static final List<WeightedRandomFishable> FISH = Arrays.asList(new WeightedRandomFishable[] {
			new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 60),
			new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.getMetadata()), 25),
			new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), 2),
			new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()),
					13) });
	private int xTile;
	private int yTile;
	private int zTile;
	private Block inTile;
	private boolean inGround;
	public int shake;
	public EntityPlayer angler;
	private int ticksInGround;
	private int ticksInAir;
	private int ticksCatchable;
	private int ticksCaughtDelay;
	private int ticksCatchableDelay;
	private float fishApproachAngle;
	public Entity caughtEntity;
	private int fishPosRotationIncrements;
	private double fishX;
	private double fishY;
	private double fishZ;
	private double fishYaw;
	private double fishPitch;
	private double clientMotionX;
	private double clientMotionY;
	private double clientMotionZ;

	public static List<WeightedRandomFishable> func_174855_j() {
		return FISH;
	}

	public EntityFishHook(World worldIn) {
		super(worldIn);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.setSize(0.25F, 0.25F);
		this.ignoreFrustumCheck = true;
	}

	public EntityFishHook(World worldIn, double x, double y, double z, EntityPlayer anglerIn) {
		this(worldIn);
		this.setPosition(x, y, z);
		this.ignoreFrustumCheck = true;
		this.angler = anglerIn;
		anglerIn.fishEntity = this;
	}

	public EntityFishHook(World worldIn, EntityPlayer fishingPlayer) {
		super(worldIn);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.ignoreFrustumCheck = true;
		this.angler = fishingPlayer;
		this.angler.fishEntity = this;
		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(fishingPlayer.posX, fishingPlayer.posY + (double) fishingPlayer.getEyeHeight(),
				fishingPlayer.posZ, fishingPlayer.rotationYaw, fishingPlayer.rotationPitch);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		float f = 0.4F;
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F)
				* MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F)
				* MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * f);
		this.handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}

	protected void entityInit() {
	}

	/**+
	 * Checks if the entity is in range to render by using the past
	 * in distance and comparing it to its average edge length * 64
	 * * renderDistanceWeight Args: distance
	 */
	public boolean isInRangeToRenderDist(double d0) {
		double d1 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
		if (Double.isNaN(d1)) {
			d1 = 4.0D;
		}

		d1 = d1 * 64.0D;
		return d0 < d1 * d1;
	}

	public void handleHookCasting(double parDouble1, double parDouble2, double parDouble3, float parFloat1,
			float parFloat2) {
		float f = MathHelper.sqrt_double(parDouble1 * parDouble1 + parDouble2 * parDouble2 + parDouble3 * parDouble3);
		parDouble1 = parDouble1 / (double) f;
		parDouble2 = parDouble2 / (double) f;
		parDouble3 = parDouble3 / (double) f;
		parDouble1 = parDouble1 + this.rand.nextGaussian() * 0.007499999832361937D * (double) parFloat2;
		parDouble2 = parDouble2 + this.rand.nextGaussian() * 0.007499999832361937D * (double) parFloat2;
		parDouble3 = parDouble3 + this.rand.nextGaussian() * 0.007499999832361937D * (double) parFloat2;
		parDouble1 = parDouble1 * (double) parFloat1;
		parDouble2 = parDouble2 * (double) parFloat1;
		parDouble3 = parDouble3 * (double) parFloat1;
		this.motionX = parDouble1;
		this.motionY = parDouble2;
		this.motionZ = parDouble3;
		float f1 = MathHelper.sqrt_double(parDouble1 * parDouble1 + parDouble3 * parDouble3);
		this.prevRotationYaw = this.rotationYaw = (float) (MathHelper.func_181159_b(parDouble1, parDouble3) * 180.0D
				/ 3.1415927410125732D);
		this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.func_181159_b(parDouble2, (double) f1)
				* 180.0D / 3.1415927410125732D);
		this.ticksInGround = 0;
	}

	public void setPositionAndRotation2(double d0, double d1, double d2, float f, float f1, int i, boolean var10) {
		this.fishX = d0;
		this.fishY = d1;
		this.fishZ = d2;
		this.fishYaw = (double) f;
		this.fishPitch = (double) f1;
		this.fishPosRotationIncrements = i;
		this.motionX = this.clientMotionX;
		this.motionY = this.clientMotionY;
		this.motionZ = this.clientMotionZ;
	}

	/**+
	 * Sets the velocity to the args. Args: x, y, z
	 */
	public void setVelocity(double d0, double d1, double d2) {
		this.clientMotionX = this.motionX = d0;
		this.clientMotionY = this.motionY = d1;
		this.clientMotionZ = this.motionZ = d2;
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		if (this.fishPosRotationIncrements > 0) {
			double d7 = this.posX + (this.fishX - this.posX) / (double) this.fishPosRotationIncrements;
			double d8 = this.posY + (this.fishY - this.posY) / (double) this.fishPosRotationIncrements;
			double d9 = this.posZ + (this.fishZ - this.posZ) / (double) this.fishPosRotationIncrements;
			double d1 = MathHelper.wrapAngleTo180_double(this.fishYaw - (double) this.rotationYaw);
			this.rotationYaw = (float) ((double) this.rotationYaw + d1 / (double) this.fishPosRotationIncrements);
			this.rotationPitch = (float) ((double) this.rotationPitch
					+ (this.fishPitch - (double) this.rotationPitch) / (double) this.fishPosRotationIncrements);
			--this.fishPosRotationIncrements;
			this.setPosition(d7, d8, d9);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		} else {

			if (this.shake > 0) {
				--this.shake;
			}

			if (this.inGround) {
				if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile))
						.getBlock() == this.inTile) {
					++this.ticksInGround;
					if (this.ticksInGround == 1200) {
						this.setDead();
					}

					return;
				}

				this.inGround = false;
				this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			} else {
				++this.ticksInAir;
			}

			Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
			Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3);
			vec31 = new Vec3(this.posX, this.posY, this.posZ);
			vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			if (movingobjectposition != null) {
				vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
						movingobjectposition.hitVec.zCoord);
			}

			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()
					.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;

			for (int i = 0; i < list.size(); ++i) {
				Entity entity1 = (Entity) list.get(i);
				if (entity1.canBeCollidedWith() && (entity1 != this.angler || this.ticksInAir >= 5)) {
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f, (double) f,
							(double) f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec31, vec3);
					if (movingobjectposition1 != null) {
						double d2 = vec31.squareDistanceTo(movingobjectposition1.hitVec);
						if (d2 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d2;
						}
					}
				}
			}

			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}

			if (movingobjectposition != null) {
				if (movingobjectposition.entityHit != null) {
					if (movingobjectposition.entityHit
							.attackEntityFrom(DamageSource.causeThrownDamage(this, this.angler), 0.0F)) {
						this.caughtEntity = movingobjectposition.entityHit;
					}
				} else {
					this.inGround = true;
				}
			}

			if (!this.inGround) {
				this.moveEntity(this.motionX, this.motionY, this.motionZ);
				float f5 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
				this.rotationYaw = (float) (MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D
						/ 3.1415927410125732D);

				for (this.rotationPitch = (float) (MathHelper.func_181159_b(this.motionY, (double) f5) * 180.0D
						/ 3.1415927410125732D); this.rotationPitch
								- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
					;
				}

				while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
					this.prevRotationPitch += 360.0F;
				}

				while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
					this.prevRotationYaw -= 360.0F;
				}

				while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
					this.prevRotationYaw += 360.0F;
				}

				this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
				this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
				float f6 = 0.92F;
				if (this.onGround || this.isCollidedHorizontally) {
					f6 = 0.5F;
				}

				byte b0 = 5;
				double d10 = 0.0D;

				for (int j = 0; j < b0; ++j) {
					AxisAlignedBB axisalignedbb1 = this.getEntityBoundingBox();
					double d3 = axisalignedbb1.maxY - axisalignedbb1.minY;
					double d4 = axisalignedbb1.minY + d3 * (double) j / (double) b0;
					double d5 = axisalignedbb1.minY + d3 * (double) (j + 1) / (double) b0;
					AxisAlignedBB axisalignedbb2 = new AxisAlignedBB(axisalignedbb1.minX, d4, axisalignedbb1.minZ,
							axisalignedbb1.maxX, d5, axisalignedbb1.maxZ);
					if (this.worldObj.isAABBInMaterial(axisalignedbb2, Material.water)) {
						d10 += 1.0D / (double) b0;
					}
				}

				double d11 = d10 * 2.0D - 1.0D;
				this.motionY += 0.03999999910593033D * d11;
				if (d10 > 0.0D) {
					f6 = (float) ((double) f6 * 0.9D);
					this.motionY *= 0.8D;
				}

				this.motionX *= (double) f6;
				this.motionY *= (double) f6;
				this.motionZ *= (double) f6;
				this.setPosition(this.posX, this.posY, this.posZ);
			}
		}
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("xTile", (short) this.xTile);
		nbttagcompound.setShort("yTile", (short) this.yTile);
		nbttagcompound.setShort("zTile", (short) this.zTile);
		ResourceLocation resourcelocation = (ResourceLocation) Block.blockRegistry.getNameForObject(this.inTile);
		nbttagcompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		nbttagcompound.setByte("shake", (byte) this.shake);
		nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.xTile = nbttagcompound.getShort("xTile");
		this.yTile = nbttagcompound.getShort("yTile");
		this.zTile = nbttagcompound.getShort("zTile");
		if (nbttagcompound.hasKey("inTile", 8)) {
			this.inTile = Block.getBlockFromName(nbttagcompound.getString("inTile"));
		} else {
			this.inTile = Block.getBlockById(nbttagcompound.getByte("inTile") & 255);
		}

		this.shake = nbttagcompound.getByte("shake") & 255;
		this.inGround = nbttagcompound.getByte("inGround") == 1;
	}

	public int handleHookRetraction() {
		return 0;
	}

	/**+
	 * Will get destroyed next tick.
	 */
	public void setDead() {
		super.setDead();
		if (this.angler != null) {
			this.angler.fishEntity = null;
		}

	}
}