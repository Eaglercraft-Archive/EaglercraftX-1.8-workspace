package net.minecraft.village;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
public class Village {
	private World worldObj;
	/**+
	 * list of VillageDoorInfo objects
	 */
	private final List<VillageDoorInfo> villageDoorInfoList = Lists.newArrayList();
	/**+
	 * This is the sum of all door coordinates and used to calculate
	 * the actual village center by dividing by the number of doors.
	 */
	private BlockPos centerHelper = BlockPos.ORIGIN;
	/**+
	 * This is the actual village center.
	 */
	private BlockPos center = BlockPos.ORIGIN;
	private int villageRadius;
	private int lastAddDoorTimestamp;
	private int tickCounter;
	private int numVillagers;
	private int noBreedTicks;
	private TreeMap<String, Integer> playerReputation = new TreeMap();
	private List<Village.VillageAggressor> villageAgressors = Lists.newArrayList();
	private int numIronGolems;

	public Village() {
	}

	public Village(World worldIn) {
		this.worldObj = worldIn;
	}

	private BlockPos[] positions = null;

	private void calculateNewCheckPositions() {
		if (this.center == null || this.center.equals(BlockPos.ORIGIN)) {
			this.positions = null;
		} else {
			this.positions = new BlockPos[] { this.center.add(-this.villageRadius, 0, -this.villageRadius),
					this.center.add(-this.villageRadius, 0, this.villageRadius),
					this.center.add(this.villageRadius, 0, -this.villageRadius),
					this.center.add(this.villageRadius, 0, this.villageRadius), this.center };
		}
	}

	public boolean isVillageAreaLoaded() {
		for (int i = 0; this.positions != null && i < this.positions.length; i++) {
			if (this.worldObj.isBlockLoaded(this.positions[i])) {
				return true;
			}
		}
		return false;
	}

	public void setWorld(World worldIn) {
		this.worldObj = worldIn;
	}

	/**+
	 * Called periodically by VillageCollection
	 */
	public void tick(int parInt1) {
		if (!isVillageAreaLoaded()) {
			return;
		}
		this.tickCounter = parInt1;
		this.removeDeadAndOutOfRangeDoors();
		this.removeDeadAndOldAgressors();
		if (parInt1 % 20 == 0) {
			this.updateNumVillagers();
		}

		if (parInt1 % 30 == 0) {
			this.updateNumIronGolems();
		}

		int i = this.numVillagers / 10;
		if (this.numIronGolems < i && this.villageDoorInfoList.size() > 20 && this.worldObj.rand.nextInt(7000) == 0) {
			Vec3 vec3 = this.func_179862_a(this.center, 2, 4, 2);
			if (vec3 != null) {
				EntityIronGolem entityirongolem = new EntityIronGolem(this.worldObj);
				entityirongolem.setPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord);
				this.worldObj.spawnEntityInWorld(entityirongolem);
				++this.numIronGolems;
			}
		}

	}

	private Vec3 func_179862_a(BlockPos parBlockPos, int parInt1, int parInt2, int parInt3) {
		for (int i = 0; i < 10; ++i) {
			BlockPos blockpos = parBlockPos.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3,
					this.worldObj.rand.nextInt(16) - 8);
			if (this.func_179866_a(blockpos) && this.func_179861_a(new BlockPos(parInt1, parInt2, parInt3), blockpos)) {
				return new Vec3((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
			}
		}

		return null;
	}

	private boolean func_179861_a(BlockPos parBlockPos, BlockPos parBlockPos2) {
		if (!World.doesBlockHaveSolidTopSurface(this.worldObj, parBlockPos2.down())) {
			return false;
		} else {
			int i = parBlockPos2.getX() - parBlockPos.getX() / 2;
			int j = parBlockPos2.getZ() - parBlockPos.getZ() / 2;

			for (int k = i; k < i + parBlockPos.getX(); ++k) {
				for (int l = parBlockPos2.getY(); l < parBlockPos2.getY() + parBlockPos.getY(); ++l) {
					for (int i1 = j; i1 < j + parBlockPos.getZ(); ++i1) {
						if (this.worldObj.getBlockState(new BlockPos(k, l, i1)).getBlock().isNormalCube()) {
							return false;
						}
					}
				}
			}

			return true;
		}
	}

	private void updateNumIronGolems() {
		List list = this.worldObj.getEntitiesWithinAABB(EntityIronGolem.class,
				new AxisAlignedBB((double) (this.center.getX() - this.villageRadius), (double) (this.center.getY() - 4),
						(double) (this.center.getZ() - this.villageRadius),
						(double) (this.center.getX() + this.villageRadius), (double) (this.center.getY() + 4),
						(double) (this.center.getZ() + this.villageRadius)));
		this.numIronGolems = list.size();
	}

	private void updateNumVillagers() {
		List list = this.worldObj.getEntitiesWithinAABB(EntityVillager.class,
				new AxisAlignedBB((double) (this.center.getX() - this.villageRadius), (double) (this.center.getY() - 4),
						(double) (this.center.getZ() - this.villageRadius),
						(double) (this.center.getX() + this.villageRadius), (double) (this.center.getY() + 4),
						(double) (this.center.getZ() + this.villageRadius)));
		this.numVillagers = list.size();
		if (this.numVillagers == 0) {
			this.playerReputation.clear();
		}

	}

	public BlockPos getCenter() {
		return this.center;
	}

	public int getVillageRadius() {
		return this.villageRadius;
	}

	/**+
	 * Actually get num village door info entries, but that boils
	 * down to number of doors. Called by EntityAIVillagerMate and
	 * VillageSiege
	 */
	public int getNumVillageDoors() {
		return this.villageDoorInfoList.size();
	}

	public int getTicksSinceLastDoorAdding() {
		return this.tickCounter - this.lastAddDoorTimestamp;
	}

	public int getNumVillagers() {
		return this.numVillagers;
	}

	public boolean func_179866_a(BlockPos pos) {
		return this.center.distanceSq(pos) < (double) (this.villageRadius * this.villageRadius);
	}

	/**+
	 * called only by class EntityAIMoveThroughVillage
	 */
	public List<VillageDoorInfo> getVillageDoorInfoList() {
		return this.villageDoorInfoList;
	}

	public VillageDoorInfo getNearestDoor(BlockPos pos) {
		VillageDoorInfo villagedoorinfo = null;
		int i = Integer.MAX_VALUE;

		for (int m = 0, n = this.villageDoorInfoList.size(); m < n; ++m) {
			VillageDoorInfo villagedoorinfo1 = this.villageDoorInfoList.get(m);
			int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);
			if (j < i) {
				villagedoorinfo = villagedoorinfo1;
				i = j;
			}
		}

		return villagedoorinfo;
	}

	/**+
	 * Returns {@link net.minecraft.village.VillageDoorInfo
	 * VillageDoorInfo} from given block position
	 */
	public VillageDoorInfo getDoorInfo(BlockPos pos) {
		VillageDoorInfo villagedoorinfo = null;
		int i = Integer.MAX_VALUE;

		for (int m = 0, n = this.villageDoorInfoList.size(); m < n; ++m) {
			VillageDoorInfo villagedoorinfo1 = this.villageDoorInfoList.get(m);
			int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);
			if (j > 256) {
				j = j * 1000;
			} else {
				j = villagedoorinfo1.getDoorOpeningRestrictionCounter();
			}

			if (j < i) {
				villagedoorinfo = villagedoorinfo1;
				i = j;
			}
		}

		return villagedoorinfo;
	}

	/**+
	 * if door not existed in this village, null will be returned
	 */
	public VillageDoorInfo getExistedDoor(BlockPos doorBlock) {
		if (this.center.distanceSq(doorBlock) > (double) (this.villageRadius * this.villageRadius)) {
			return null;
		} else {
			for (int m = 0, n = this.villageDoorInfoList.size(); m < n; ++m) {
				VillageDoorInfo villagedoorinfo = this.villageDoorInfoList.get(m);
				if (villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX()
						&& villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ()
						&& Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1) {
					return villagedoorinfo;
				}
			}

			return null;
		}
	}

	public void addVillageDoorInfo(VillageDoorInfo doorInfo) {
		this.villageDoorInfoList.add(doorInfo);
		this.centerHelper = this.centerHelper.add(doorInfo.getDoorBlockPos());
		this.updateVillageRadiusAndCenter();
		this.lastAddDoorTimestamp = doorInfo.getInsidePosY();
	}

	/**+
	 * Returns true, if there is not a single village door left.
	 * Called by VillageCollection
	 */
	public boolean isAnnihilated() {
		return this.villageDoorInfoList.isEmpty();
	}

	public void addOrRenewAgressor(EntityLivingBase entitylivingbaseIn) {
		for (int i = 0, l = this.villageAgressors.size(); i < l; ++i) {
			Village.VillageAggressor village$villageaggressor = this.villageAgressors.get(i);
			if (village$villageaggressor.agressor == entitylivingbaseIn) {
				village$villageaggressor.agressionTime = this.tickCounter;
				return;
			}
		}

		this.villageAgressors.add(new Village.VillageAggressor(entitylivingbaseIn, this.tickCounter));
	}

	public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entitylivingbaseIn) {
		double d0 = Double.MAX_VALUE;
		Village.VillageAggressor village$villageaggressor = null;

		for (int i = 0; i < this.villageAgressors.size(); ++i) {
			Village.VillageAggressor village$villageaggressor1 = (Village.VillageAggressor) this.villageAgressors
					.get(i);
			double d1 = village$villageaggressor1.agressor.getDistanceSqToEntity(entitylivingbaseIn);
			if (d1 <= d0) {
				village$villageaggressor = village$villageaggressor1;
				d0 = d1;
			}
		}

		return village$villageaggressor != null ? village$villageaggressor.agressor : null;
	}

	public EntityPlayer getNearestTargetPlayer(EntityLivingBase villageDefender) {
		double d0 = Double.MAX_VALUE;
		EntityPlayer entityplayer = null;

		for (String s : this.playerReputation.keySet()) {
			if (this.isPlayerReputationTooLow(s)) {
				EntityPlayer entityplayer1 = this.worldObj.getPlayerEntityByName(s);
				if (entityplayer1 != null) {
					double d1 = entityplayer1.getDistanceSqToEntity(villageDefender);
					if (d1 <= d0) {
						entityplayer = entityplayer1;
						d0 = d1;
					}
				}
			}
		}

		return entityplayer;
	}

	private void removeDeadAndOldAgressors() {
		Iterator iterator = this.villageAgressors.iterator();

		while (iterator.hasNext()) {
			Village.VillageAggressor village$villageaggressor = (Village.VillageAggressor) iterator.next();
			if (!village$villageaggressor.agressor.isEntityAlive()
					|| Math.abs(this.tickCounter - village$villageaggressor.agressionTime) > 300) {
				iterator.remove();
			}
		}

	}

	private void removeDeadAndOutOfRangeDoors() {
		boolean flag = false;
		boolean flag1 = this.worldObj.rand.nextInt(50) == 0;
		Iterator iterator = this.villageDoorInfoList.iterator();

		while (iterator.hasNext()) {
			VillageDoorInfo villagedoorinfo = (VillageDoorInfo) iterator.next();
			if (flag1) {
				villagedoorinfo.resetDoorOpeningRestrictionCounter();
			}

			if (!this.isWoodDoor(villagedoorinfo.getDoorBlockPos())
					|| Math.abs(this.tickCounter - villagedoorinfo.getInsidePosY()) > 1200) {
				this.centerHelper = this.centerHelper.subtract(villagedoorinfo.getDoorBlockPos());
				flag = true;
				villagedoorinfo.setIsDetachedFromVillageFlag(true);
				iterator.remove();
			}
		}

		if (flag) {
			this.updateVillageRadiusAndCenter();
		}

	}

	private boolean isWoodDoor(BlockPos pos) {
		Block block = this.worldObj.getBlockState(pos).getBlock();
		return block instanceof BlockDoor ? block.getMaterial() == Material.wood : false;
	}

	private void updateVillageRadiusAndCenter() {
		int i = this.villageDoorInfoList.size();
		if (i == 0) {
			this.center = new BlockPos(0, 0, 0);
			this.villageRadius = 0;
		} else {
			this.center = new BlockPos(this.centerHelper.getX() / i, this.centerHelper.getY() / i,
					this.centerHelper.getZ() / i);
			int j = 0;

			for (int m = 0, n = this.villageDoorInfoList.size(); m < n; ++m) {
				j = Math.max(this.villageDoorInfoList.get(m).getDistanceToDoorBlockSq(this.center), j);
			}

			this.villageRadius = Math.max(32, (int) Math.sqrt((double) j) + 1);
		}

		calculateNewCheckPositions();
	}

	/**+
	 * Return the village reputation for a player
	 */
	public int getReputationForPlayer(String parString1) {
		Integer integer = (Integer) this.playerReputation.get(parString1);
		return integer != null ? integer.intValue() : 0;
	}

	/**+
	 * Set the village reputation for a player.
	 */
	public int setReputationForPlayer(String parString1, int parInt1) {
		int i = this.getReputationForPlayer(parString1);
		int j = MathHelper.clamp_int(i + parInt1, -30, 10);
		this.playerReputation.put(parString1, Integer.valueOf(j));
		return j;
	}

	/**+
	 * Return whether this player has a too low reputation with this
	 * village.
	 */
	public boolean isPlayerReputationTooLow(String parString1) {
		return this.getReputationForPlayer(parString1) <= -15;
	}

	/**+
	 * Read this village's data from NBT.
	 */
	public void readVillageDataFromNBT(NBTTagCompound parNBTTagCompound) {
		this.numVillagers = parNBTTagCompound.getInteger("PopSize");
		this.villageRadius = parNBTTagCompound.getInteger("Radius");
		this.numIronGolems = parNBTTagCompound.getInteger("Golems");
		this.lastAddDoorTimestamp = parNBTTagCompound.getInteger("Stable");
		this.tickCounter = parNBTTagCompound.getInteger("Tick");
		this.noBreedTicks = parNBTTagCompound.getInteger("MTick");
		this.center = new BlockPos(parNBTTagCompound.getInteger("CX"), parNBTTagCompound.getInteger("CY"),
				parNBTTagCompound.getInteger("CZ"));
		this.centerHelper = new BlockPos(parNBTTagCompound.getInteger("ACX"), parNBTTagCompound.getInteger("ACY"),
				parNBTTagCompound.getInteger("ACZ"));
		NBTTagList nbttaglist = parNBTTagCompound.getTagList("Doors", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			VillageDoorInfo villagedoorinfo = new VillageDoorInfo(
					new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"),
							nbttagcompound.getInteger("Z")),
					nbttagcompound.getInteger("IDX"), nbttagcompound.getInteger("IDZ"),
					nbttagcompound.getInteger("TS"));
			this.villageDoorInfoList.add(villagedoorinfo);
		}

		NBTTagList nbttaglist1 = parNBTTagCompound.getTagList("Players", 10);

		for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
			NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(j);
			if (nbttagcompound1.hasKey("Name")) {
				this.playerReputation.put(nbttagcompound1.getString("Name"),
						Integer.valueOf(nbttagcompound1.getInteger("S")));
			}
		}

		calculateNewCheckPositions();
	}

	/**+
	 * Write this village's data to NBT.
	 */
	public void writeVillageDataToNBT(NBTTagCompound parNBTTagCompound) {
		parNBTTagCompound.setInteger("PopSize", this.numVillagers);
		parNBTTagCompound.setInteger("Radius", this.villageRadius);
		parNBTTagCompound.setInteger("Golems", this.numIronGolems);
		parNBTTagCompound.setInteger("Stable", this.lastAddDoorTimestamp);
		parNBTTagCompound.setInteger("Tick", this.tickCounter);
		parNBTTagCompound.setInteger("MTick", this.noBreedTicks);
		parNBTTagCompound.setInteger("CX", this.center.getX());
		parNBTTagCompound.setInteger("CY", this.center.getY());
		parNBTTagCompound.setInteger("CZ", this.center.getZ());
		parNBTTagCompound.setInteger("ACX", this.centerHelper.getX());
		parNBTTagCompound.setInteger("ACY", this.centerHelper.getY());
		parNBTTagCompound.setInteger("ACZ", this.centerHelper.getZ());
		NBTTagList nbttaglist = new NBTTagList();

		for (int m = 0, n = this.villageDoorInfoList.size(); m < n; ++m) {
			VillageDoorInfo villagedoorinfo = this.villageDoorInfoList.get(m);
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("X", villagedoorinfo.getDoorBlockPos().getX());
			nbttagcompound.setInteger("Y", villagedoorinfo.getDoorBlockPos().getY());
			nbttagcompound.setInteger("Z", villagedoorinfo.getDoorBlockPos().getZ());
			nbttagcompound.setInteger("IDX", villagedoorinfo.getInsideOffsetX());
			nbttagcompound.setInteger("IDZ", villagedoorinfo.getInsideOffsetZ());
			nbttagcompound.setInteger("TS", villagedoorinfo.getInsidePosY());
			nbttaglist.appendTag(nbttagcompound);
		}

		parNBTTagCompound.setTag("Doors", nbttaglist);
		NBTTagList nbttaglist1 = new NBTTagList();

		for (String s : this.playerReputation.keySet()) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setString("Name", s);
			nbttagcompound1.setInteger("S", ((Integer) this.playerReputation.get(s)).intValue());
			nbttaglist1.appendTag(nbttagcompound1);
		}

		parNBTTagCompound.setTag("Players", nbttaglist1);
	}

	/**+
	 * Prevent villager breeding for a fixed interval of time
	 */
	public void endMatingSeason() {
		this.noBreedTicks = this.tickCounter;
	}

	/**+
	 * Return whether villagers mating refractory period has passed
	 */
	public boolean isMatingSeason() {
		return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
	}

	public void setDefaultPlayerReputation(int parInt1) {
		for (String s : this.playerReputation.keySet()) {
			this.setReputationForPlayer(s, parInt1);
		}

	}

	class VillageAggressor {
		public EntityLivingBase agressor;
		public int agressionTime;

		VillageAggressor(EntityLivingBase parEntityLivingBase, int parInt1) {
			this.agressor = parEntityLivingBase;
			this.agressionTime = parInt1;
		}
	}
}