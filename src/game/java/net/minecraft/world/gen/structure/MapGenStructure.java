package net.minecraft.world.gen.structure;

import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.LongObjectMap;
import com.carrotsearch.hppc.cursors.ObjectCursor;

import java.util.Iterator;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.HString;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

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
public abstract class MapGenStructure extends MapGenBase {
	private MapGenStructureData structureData;
	/**+
	 * Used to store a list of all structures that have been
	 * recursively generated. Used so that during recursive
	 * generation, the structure generator can avoid generating
	 * structures that intersect ones that have already been placed.
	 */
	protected LongObjectMap<StructureStart> structureMap = new LongObjectHashMap<>();

	public abstract String getStructureName();

	public MapGenStructure() {
		super();
	}

	public MapGenStructure(boolean scramble) {
		super(scramble);
	}

	/**+
	 * Recursively called by generate()
	 */
	protected final void recursiveGenerate(World world, final int i, final int j, int var4, int var5,
			ChunkPrimer var6) {
		this.func_143027_a(world);
		if (!this.structureMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(i, j))) {
			this.rand.nextInt();

			try {
				if (this.canSpawnStructureAtCoords(i, j)) {
					StructureStart structurestart = this.getStructureStart(i, j);
					this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(i, j), structurestart);
					this.func_143026_a(i, j, structurestart);
				}

			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable,
						"Exception preparing structure feature");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Feature being prepared");
				crashreportcategory.addCrashSectionCallable("Is feature chunk", new Callable<String>() {
					public String call() throws Exception {
						return MapGenStructure.this.canSpawnStructureAtCoords(i, j) ? "True" : "False";
					}
				});
				crashreportcategory.addCrashSection("Chunk location",
						HString.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));
				crashreportcategory.addCrashSectionCallable("Chunk pos hash", new Callable<String>() {
					public String call() throws Exception {
						return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j));
					}
				});
				crashreportcategory.addCrashSectionCallable("Structure type", new Callable<String>() {
					public String call() throws Exception {
						return MapGenStructure.this.getClass().getCanonicalName();
					}
				});
				throw new ReportedException(crashreport);
			}
		}
	}

	public boolean generateStructure(World worldIn, EaglercraftRandom randomIn, ChunkCoordIntPair chunkCoord) {
		this.func_143027_a(worldIn);
		int i = (chunkCoord.chunkXPos << 4) + 8;
		int j = (chunkCoord.chunkZPos << 4) + 8;
		boolean flag = false;

		for (ObjectCursor<StructureStart> structurestart_ : this.structureMap.values()) {
			StructureStart structurestart = structurestart_.value;
			if (structurestart.isSizeableStructure() && structurestart.func_175788_a(chunkCoord)
					&& structurestart.getBoundingBox().intersectsWith(i, j, i + 15, j + 15)) {
				structurestart.generateStructure(worldIn, randomIn, new StructureBoundingBox(i, j, i + 15, j + 15));
				structurestart.func_175787_b(chunkCoord);
				flag = true;
				this.func_143026_a(structurestart.getChunkPosX(), structurestart.getChunkPosZ(), structurestart);
			}
		}

		return flag;
	}

	public boolean func_175795_b(BlockPos pos) {
		this.func_143027_a(this.worldObj);
		return this.func_175797_c(pos) != null;
	}

	protected StructureStart func_175797_c(BlockPos pos) {
		label24: for (ObjectCursor<StructureStart> structurestart_ : this.structureMap.values()) {
			StructureStart structurestart = structurestart_.value;
			if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().isVecInside(pos)) {
				Iterator iterator = structurestart.getComponents().iterator();

				while (true) {
					if (!iterator.hasNext()) {
						continue label24;
					}

					StructureComponent structurecomponent = (StructureComponent) iterator.next();
					if (structurecomponent.getBoundingBox().isVecInside(pos)) {
						break;
					}
				}

				return structurestart;
			}
		}

		return null;
	}

	public boolean func_175796_a(World worldIn, BlockPos pos) {
		this.func_143027_a(worldIn);

		for (ObjectCursor<StructureStart> structurestart_ : this.structureMap.values()) {
			StructureStart structurestart = structurestart_.value;
			if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().isVecInside(pos)) {
				return true;
			}
		}

		return false;
	}

	public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos) {
		this.worldObj = worldIn;
		this.func_143027_a(worldIn);
		this.rand.setSeed(worldIn.getSeed());
		long i = this.rand.nextLong();
		long j = this.rand.nextLong();
		long k = (long) (pos.getX() >> 4) * i;
		long l = (long) (pos.getZ() >> 4) * j;
		this.rand.setSeed(k ^ l ^ worldIn.getSeed());
		this.recursiveGenerate(worldIn, pos.getX() >> 4, pos.getZ() >> 4, 0, 0, (ChunkPrimer) null);
		double d0 = Double.MAX_VALUE;
		BlockPos blockpos = null;

		for (ObjectCursor<StructureStart> structurestart_ : this.structureMap.values()) {
			StructureStart structurestart = structurestart_.value;
			if (structurestart.isSizeableStructure()) {
				StructureComponent structurecomponent = (StructureComponent) structurestart.getComponents().get(0);
				BlockPos blockpos1 = structurecomponent.getBoundingBoxCenter();
				double d1 = blockpos1.distanceSq(pos);
				if (d1 < d0) {
					d0 = d1;
					blockpos = blockpos1;
				}
			}
		}

		if (blockpos != null) {
			return blockpos;
		} else {
			List<BlockPos> list = this.getCoordList();
			if (list != null) {
				BlockPos blockpos2 = null;

				for (int m = 0, n = list.size(); m < n; ++m) {
					BlockPos blockpos3 = list.get(m);
					double d2 = blockpos3.distanceSq(pos);
					if (d2 < d0) {
						d0 = d2;
						blockpos2 = blockpos3;
					}
				}

				return blockpos2;
			} else {
				return null;
			}
		}
	}

	/**+
	 * Returns a list of other locations at which the structure
	 * generation has been run, or null if not relevant to this
	 * structure generator.
	 */
	protected List<BlockPos> getCoordList() {
		return null;
	}

	private void func_143027_a(World worldIn) {
		if (this.structureData == null) {
			this.structureData = (MapGenStructureData) worldIn.loadItemData(MapGenStructureData.class,
					this.getStructureName());
			if (this.structureData == null) {
				this.structureData = new MapGenStructureData(this.getStructureName());
				worldIn.setItemData(this.getStructureName(), this.structureData);
			} else {
				NBTTagCompound nbttagcompound = this.structureData.getTagCompound();

				for (String s : nbttagcompound.getKeySet()) {
					NBTBase nbtbase = nbttagcompound.getTag(s);
					if (nbtbase.getId() == 10) {
						NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbtbase;
						if (nbttagcompound1.hasKey("ChunkX") && nbttagcompound1.hasKey("ChunkZ")) {
							int i = nbttagcompound1.getInteger("ChunkX");
							int j = nbttagcompound1.getInteger("ChunkZ");
							StructureStart structurestart = MapGenStructureIO.getStructureStart(nbttagcompound1,
									worldIn);
							if (structurestart != null) {
								this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(i, j), structurestart);
							}
						}
					}
				}
			}
		}

	}

	private void func_143026_a(int start, int parInt2, StructureStart parStructureStart) {
		this.structureData.writeInstance(parStructureStart.writeStructureComponentsToNBT(start, parInt2), start,
				parInt2);
		this.structureData.markDirty();
	}

	protected abstract boolean canSpawnStructureAtCoords(int var1, int var2);

	protected abstract StructureStart getStructureStart(int var1, int var2);
}