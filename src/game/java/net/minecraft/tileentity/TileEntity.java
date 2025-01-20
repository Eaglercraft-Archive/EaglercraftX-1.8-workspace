package net.minecraft.tileentity;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
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
public abstract class TileEntity {
	private static final Logger logger = LogManager.getLogger();
	private static Map<String, Class<? extends TileEntity>> nameToClassMap = Maps.newHashMap();
	private static Map<String, Supplier<? extends TileEntity>> nameToCtorMap = Maps.newHashMap();
	private static Map<Class<? extends TileEntity>, String> classToNameMap = Maps.newHashMap();
	protected World worldObj;
	protected BlockPos pos = BlockPos.ORIGIN;
	protected boolean tileEntityInvalid;
	private int blockMetadata = -1;
	protected Block blockType;

	/**+
	 * Adds a new two-way mapping between the class and its string
	 * name in both hashmaps.
	 */
	private static void addMapping(Class<? extends TileEntity> cl, Supplier<? extends TileEntity> ct, String id) {
		if (nameToClassMap.containsKey(id)) {
			throw new IllegalArgumentException("Duplicate id: " + id);
		} else {
			nameToClassMap.put(id, cl);
			nameToCtorMap.put(id, ct);
			classToNameMap.put(cl, id);
		}
	}

	/**+
	 * Returns the worldObj for this tileEntity.
	 */
	public World getWorld() {
		return this.worldObj;
	}

	/**+
	 * Sets the worldObj for this tileEntity.
	 */
	public void setWorldObj(World worldIn) {
		this.worldObj = worldIn;
	}

	/**+
	 * Returns true if the worldObj isn't null.
	 */
	public boolean hasWorldObj() {
		return this.worldObj != null;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		this.pos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"),
				nbttagcompound.getInteger("z"));
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		String s = (String) classToNameMap.get(this.getClass());
		if (s == null) {
			throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
		} else {
			nbttagcompound.setString("id", s);
			nbttagcompound.setInteger("x", this.pos.getX());
			nbttagcompound.setInteger("y", this.pos.getY());
			nbttagcompound.setInteger("z", this.pos.getZ());
		}
	}

	/**+
	 * Creates a new entity and loads its data from the specified
	 * NBT.
	 */
	public static TileEntity createAndLoadEntity(NBTTagCompound nbt) {
		TileEntity tileentity = null;

		try {
			Supplier<? extends TileEntity> oclass = nameToCtorMap.get(nbt.getString("id"));
			if (oclass != null) {
				tileentity = (TileEntity) oclass.get();
			}
		} catch (Exception exception) {
			logger.error("Could not create TileEntity");
			logger.error(exception);
		}

		if (tileentity != null) {
			tileentity.readFromNBT(nbt);
		} else {
			logger.warn("Skipping BlockEntity with id " + nbt.getString("id"));
		}

		return tileentity;
	}

	public int getBlockMetadata() {
		if (this.blockMetadata == -1) {
			IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
			this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
		}

		return this.blockMetadata;
	}

	/**+
	 * For tile entities, ensures the chunk containing the tile
	 * entity is saved to disk later - the game won't think it
	 * hasn't changed and skip it.
	 */
	public void markDirty() {
		if (this.worldObj != null) {
			IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
			this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
			this.worldObj.markChunkDirty(this.pos, this);
			if (this.getBlockType() != Blocks.air) {
				this.worldObj.updateComparatorOutputLevel(this.pos, this.getBlockType());
			}
		}

	}

	/**+
	 * Returns the square of the distance between this entity and
	 * the passed in coordinates.
	 */
	public double getDistanceSq(double x, double y, double z) {
		double d0 = (double) this.pos.getX() + 0.5D - x;
		double d1 = (double) this.pos.getY() + 0.5D - y;
		double d2 = (double) this.pos.getZ() + 0.5D - z;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	public double getMaxRenderDistanceSquared() {
		return 4096.0D;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	/**+
	 * Gets the block type at the location of this entity
	 * (client-only).
	 */
	public Block getBlockType() {
		if (this.blockType == null) {
			this.blockType = this.worldObj.getBlockState(this.pos).getBlock();
		}

		return this.blockType;
	}

	/**+
	 * Allows for a specialized description packet to be created.
	 * This is often used to sync tile entity data from the server
	 * to the client easily. For example this is used by signs to
	 * synchronise the text to be displayed.
	 */
	public Packet getDescriptionPacket() {
		return null;
	}

	public boolean isInvalid() {
		return this.tileEntityInvalid;
	}

	/**+
	 * invalidates a tile entity
	 */
	public void invalidate() {
		this.tileEntityInvalid = true;
	}

	/**+
	 * validates a tile entity
	 */
	public void validate() {
		this.tileEntityInvalid = false;
	}

	public boolean receiveClientEvent(int var1, int var2) {
		return false;
	}

	public void updateContainingBlockInfo() {
		this.blockType = null;
		this.blockMetadata = -1;
	}

	public void addInfoToCrashReport(CrashReportCategory reportCategory) {
		reportCategory.addCrashSectionCallable("Name", new Callable<String>() {
			public String call() throws Exception {
				return (String) TileEntity.classToNameMap.get(TileEntity.this.getClass()) + " // "
						+ TileEntity.this.getClass().getName();
			}
		});
		if (this.worldObj != null) {
			CrashReportCategory.addBlockInfo(reportCategory, this.pos, this.getBlockType(), this.getBlockMetadata());
			reportCategory.addCrashSectionCallable("Actual block type", new Callable<String>() {
				public String call() throws Exception {
					int i = Block
							.getIdFromBlock(TileEntity.this.worldObj.getBlockState(TileEntity.this.pos).getBlock());

					try {
						return HString.format("ID #%d (%s // %s)",
								new Object[] { Integer.valueOf(i), Block.getBlockById(i).getUnlocalizedName(),
										Block.getBlockById(i).getClass().getName() });
					} catch (Throwable var3) {
						return "ID #" + i;
					}
				}
			});
			reportCategory.addCrashSectionCallable("Actual block data value", new Callable<String>() {
				public String call() throws Exception {
					IBlockState iblockstate = TileEntity.this.worldObj.getBlockState(TileEntity.this.pos);
					int i = iblockstate.getBlock().getMetaFromState(iblockstate);
					if (i < 0) {
						return "Unknown? (Got " + i + ")";
					} else {
						String s = HString.format("%4s", new Object[] { Integer.toBinaryString(i) }).replace(" ", "0");
						return HString.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s });
					}
				}
			});
		}
	}

	public void setPos(BlockPos posIn) {
		this.pos = posIn;
	}

	public boolean func_183000_F() {
		return false;
	}

	static {
		addMapping(TileEntityFurnace.class, TileEntityFurnace::new, "Furnace");
		addMapping(TileEntityChest.class, TileEntityChest::new, "Chest");
		addMapping(TileEntityEnderChest.class, TileEntityEnderChest::new, "EnderChest");
		addMapping(BlockJukebox.TileEntityJukebox.class, BlockJukebox.TileEntityJukebox::new, "RecordPlayer");
		addMapping(TileEntityDispenser.class, TileEntityDispenser::new, "Trap");
		addMapping(TileEntityDropper.class, TileEntityDropper::new, "Dropper");
		addMapping(TileEntitySign.class, TileEntitySign::new, "Sign");
		addMapping(TileEntityMobSpawner.class, TileEntityMobSpawner::new, "MobSpawner");
		addMapping(TileEntityNote.class, TileEntityNote::new, "Music");
		addMapping(TileEntityPiston.class, TileEntityPiston::new, "Piston");
		addMapping(TileEntityBrewingStand.class, TileEntityBrewingStand::new, "Cauldron");
		addMapping(TileEntityEnchantmentTable.class, TileEntityEnchantmentTable::new, "EnchantTable");
		addMapping(TileEntityEndPortal.class, TileEntityEndPortal::new, "Airportal");
		addMapping(TileEntityCommandBlock.class, TileEntityCommandBlock::new, "Control");
		addMapping(TileEntityBeacon.class, TileEntityBeacon::new, "Beacon");
		addMapping(TileEntitySkull.class, TileEntitySkull::new, "Skull");
		addMapping(TileEntityDaylightDetector.class, TileEntityDaylightDetector::new, "DLDetector");
		addMapping(TileEntityHopper.class, TileEntityHopper::new, "Hopper");
		addMapping(TileEntityComparator.class, TileEntityComparator::new, "Comparator");
		addMapping(TileEntityFlowerPot.class, TileEntityFlowerPot::new, "FlowerPot");
		addMapping(TileEntityBanner.class, TileEntityBanner::new, "Banner");
	}
}