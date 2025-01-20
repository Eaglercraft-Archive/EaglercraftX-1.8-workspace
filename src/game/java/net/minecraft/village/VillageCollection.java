package net.minecraft.village;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSavedData;

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
public class VillageCollection extends WorldSavedData {
	private World worldObj;
	/**+
	 * This is a black hole. You can add data to this list through a
	 * public interface, but you can't query that information in any
	 * way and it's not used internally either.
	 */
	private final List<BlockPos> villagerPositionsList = Lists.newArrayList();
	private final List<VillageDoorInfo> newDoors = Lists.newArrayList();
	private final List<Village> villageList = Lists.newArrayList();
	private int tickCounter;

	public VillageCollection(String name) {
		super(name);
	}

	public VillageCollection(World worldIn) {
		super(fileNameForProvider(worldIn.provider));
		this.worldObj = worldIn;
		this.markDirty();
	}

	public void setWorldsForAll(World worldIn) {
		this.worldObj = worldIn;

		for (int i = 0, l = this.villageList.size(); i < l; ++i) {
			this.villageList.get(i).setWorld(worldIn);
		}

	}

	public void addToVillagerPositionList(BlockPos pos) {
		if (this.villagerPositionsList.size() <= 64) {
			if (!this.positionInList(pos)) {
				this.villagerPositionsList.add(pos);
			}

		}
	}

	/**+
	 * Runs a single tick for the village collection
	 */
	public void tick() {
		++this.tickCounter;

		for (int i = 0, l = this.villageList.size(); i < l; ++i) {
			this.villageList.get(i).tick(this.tickCounter);
		}

		this.removeAnnihilatedVillages();
		this.dropOldestVillagerPosition();
		this.addNewDoorsToVillageOrCreateVillage();
		if (this.tickCounter % 400 == 0) {
			this.markDirty();
		}

	}

	private void removeAnnihilatedVillages() {
		Iterator iterator = this.villageList.iterator();

		while (iterator.hasNext()) {
			Village village = (Village) iterator.next();
			if (village.isAnnihilated()) {
				iterator.remove();
				this.markDirty();
			}
		}

	}

	/**+
	 * Get a list of villages.
	 */
	public List<Village> getVillageList() {
		return this.villageList;
	}

	public Village getNearestVillage(BlockPos doorBlock, int radius) {
		Village village = null;
		double d0 = 3.4028234663852886E38D;

		for (int i = 0, l = this.villageList.size(); i < l; ++i) {
			Village village1 = this.villageList.get(i);
			double d1 = village1.getCenter().distanceSq(doorBlock);
			if (d1 < d0) {
				float f = (float) (radius + village1.getVillageRadius());
				if (d1 <= (double) (f * f)) {
					village = village1;
					d0 = d1;
				}
			}
		}

		return village;
	}

	private void dropOldestVillagerPosition() {
		if (!this.villagerPositionsList.isEmpty()) {
			this.addDoorsAround((BlockPos) this.villagerPositionsList.remove(0));
		}
	}

	private void addNewDoorsToVillageOrCreateVillage() {
		for (int i = 0; i < this.newDoors.size(); ++i) {
			VillageDoorInfo villagedoorinfo = (VillageDoorInfo) this.newDoors.get(i);
			Village village = this.getNearestVillage(villagedoorinfo.getDoorBlockPos(), 32);
			if (village == null) {
				village = new Village(this.worldObj);
				this.villageList.add(village);
				this.markDirty();
			}

			village.addVillageDoorInfo(villagedoorinfo);
		}

		this.newDoors.clear();
	}

	private void addDoorsAround(BlockPos central) {
		byte b0 = 16;
		byte b1 = 4;
		byte b2 = 16;

		for (int i = -b0; i < b0; ++i) {
			for (int j = -b1; j < b1; ++j) {
				for (int k = -b2; k < b2; ++k) {
					BlockPos blockpos = central.add(i, j, k);
					if (this.isWoodDoor(blockpos)) {
						VillageDoorInfo villagedoorinfo = this.checkDoorExistence(blockpos);
						if (villagedoorinfo == null) {
							this.addToNewDoorsList(blockpos);
						} else {
							villagedoorinfo.func_179849_a(this.tickCounter);
						}
					}
				}
			}
		}

	}

	/**+
	 * returns the VillageDoorInfo if it exists in any village or in
	 * the newDoor list, otherwise returns null
	 */
	private VillageDoorInfo checkDoorExistence(BlockPos doorBlock) {
		for (int i = 0, l = this.newDoors.size(); i < l; ++i) {
			VillageDoorInfo villagedoorinfo = this.newDoors.get(i);
			if (villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX()
					&& villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ()
					&& Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1) {
				return villagedoorinfo;
			}
		}

		for (int i = 0, l = this.villageList.size(); i < l; ++i) {
			VillageDoorInfo villagedoorinfo1 = this.villageList.get(i).getExistedDoor(doorBlock);
			if (villagedoorinfo1 != null) {
				return villagedoorinfo1;
			}
		}

		return null;
	}

	private void addToNewDoorsList(BlockPos doorBlock) {
		EnumFacing enumfacing = BlockDoor.getFacing(this.worldObj, doorBlock);
		EnumFacing enumfacing1 = enumfacing.getOpposite();
		int i = this.countBlocksCanSeeSky(doorBlock, enumfacing, 5);
		int j = this.countBlocksCanSeeSky(doorBlock, enumfacing1, i + 1);
		if (i != j) {
			this.newDoors.add(new VillageDoorInfo(doorBlock, i < j ? enumfacing : enumfacing1, this.tickCounter));
		}

	}

	/**+
	 * Check five blocks in the direction. The centerPos will not be
	 * checked.
	 */
	private int countBlocksCanSeeSky(BlockPos centerPos, EnumFacing direction, int limitation) {
		int i = 0;

		for (int j = 1; j <= 5; ++j) {
			if (this.worldObj.canSeeSky(centerPos.offset(direction, j))) {
				++i;
				if (i >= limitation) {
					return i;
				}
			}
		}

		return i;
	}

	private boolean positionInList(BlockPos pos) {
		for (int i = 0, l = this.villagerPositionsList.size(); i < l; ++i) {
			if (this.villagerPositionsList.get(i).equals(pos)) {
				return true;
			}
		}

		return false;
	}

	private boolean isWoodDoor(BlockPos doorPos) {
		Block block = this.worldObj.getBlockState(doorPos).getBlock();
		return block instanceof BlockDoor ? block.getMaterial() == Material.wood : false;
	}

	/**+
	 * reads in data from the NBTTagCompound into this MapDataBase
	 */
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		this.tickCounter = nbttagcompound.getInteger("Tick");
		NBTTagList nbttaglist = nbttagcompound.getTagList("Villages", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			Village village = new Village();
			village.readVillageDataFromNBT(nbttagcompound1);
			this.villageList.add(village);
		}

	}

	/**+
	 * write data to NBTTagCompound from this MapDataBase, similar
	 * to Entities and TileEntities
	 */
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("Tick", this.tickCounter);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0, l = this.villageList.size(); i < l; ++i) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			this.villageList.get(i).writeVillageDataToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}

		nbttagcompound.setTag("Villages", nbttaglist);
	}

	public static String fileNameForProvider(WorldProvider provider) {
		return "villages" + provider.getInternalNameSuffix();
	}
}