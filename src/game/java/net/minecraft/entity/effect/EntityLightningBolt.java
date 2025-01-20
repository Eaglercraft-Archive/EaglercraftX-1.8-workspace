package net.minecraft.entity.effect;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
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
public class EntityLightningBolt extends EntityWeatherEffect {
	private int lightningState;
	public long boltVertex;
	private int boltLivingTime;

	public EntityLightningBolt(World worldIn, double posX, double posY, double posZ) {
		super(worldIn);
		this.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
		this.lightningState = 2;
		this.boltVertex = this.rand.nextLong();
		this.boltLivingTime = this.rand.nextInt(3) + 1;
		BlockPos blockpos = new BlockPos(this);
		if (!worldIn.isRemote && worldIn.getGameRules().getBoolean("doFireTick")
				&& (worldIn.getDifficulty() == EnumDifficulty.NORMAL || worldIn.getDifficulty() == EnumDifficulty.HARD)
				&& worldIn.isAreaLoaded(blockpos, 10)) {
			if (worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air
					&& Blocks.fire.canPlaceBlockAt(worldIn, blockpos)) {
				worldIn.setBlockState(blockpos, Blocks.fire.getDefaultState());
			}

			for (int i = 0; i < 4; ++i) {
				BlockPos blockpos1 = blockpos.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1,
						this.rand.nextInt(3) - 1);
				if (worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.air
						&& Blocks.fire.canPlaceBlockAt(worldIn, blockpos1)) {
					worldIn.setBlockState(blockpos1, Blocks.fire.getDefaultState());
				}
			}
		}

	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		if (this.lightningState == 2) {
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F,
					0.8F + this.rand.nextFloat() * 0.2F);
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F,
					0.5F + this.rand.nextFloat() * 0.2F);
		}

		--this.lightningState;
		if (this.lightningState < 0) {
			if (this.boltLivingTime == 0) {
				this.setDead();
			} else if (this.lightningState < -this.rand.nextInt(10)) {
				--this.boltLivingTime;
				this.lightningState = 1;
				this.boltVertex = this.rand.nextLong();
				BlockPos blockpos = new BlockPos(this);
				if (!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("doFireTick")
						&& this.worldObj.isAreaLoaded(blockpos, 10)
						&& this.worldObj.getBlockState(blockpos).getBlock().getMaterial() == Material.air
						&& Blocks.fire.canPlaceBlockAt(this.worldObj, blockpos)) {
					this.worldObj.setBlockState(blockpos, Blocks.fire.getDefaultState());
				}
			}
		}

		if (this.lightningState >= 0) {
			if (this.worldObj.isRemote) {
				this.worldObj.setLastLightningBolt(2);
			} else {
				double d0 = 3.0D;
				List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - d0,
						this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + 6.0D + d0, this.posZ + d0));

				for (int i = 0; i < list.size(); ++i) {
					Entity entity = (Entity) list.get(i);
					entity.onStruckByLightning(this);
				}
			}
		}

	}

	protected void entityInit() {
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound var1) {
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound var1) {
	}
}