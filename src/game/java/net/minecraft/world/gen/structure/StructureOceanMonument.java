package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

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
public class StructureOceanMonument extends MapGenStructure {
	private int field_175800_f;
	private int field_175801_g;
	public static final List<BiomeGenBase> field_175802_d = Arrays.asList(new BiomeGenBase[] { BiomeGenBase.ocean,
			BiomeGenBase.deepOcean, BiomeGenBase.river, BiomeGenBase.frozenOcean, BiomeGenBase.frozenRiver });
	private static final List<BiomeGenBase.SpawnListEntry> field_175803_h = Lists.newArrayList();

	public StructureOceanMonument(boolean scramble) {
		super(scramble);
		this.field_175800_f = 32;
		this.field_175801_g = 5;
	}

	public StructureOceanMonument(Map<String, String> parMap, boolean scramble) {
		this(scramble);

		for (Entry entry : parMap.entrySet()) {
			if (((String) entry.getKey()).equals("spacing")) {
				this.field_175800_f = MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(),
						this.field_175800_f, 1);
			} else if (((String) entry.getKey()).equals("separation")) {
				this.field_175801_g = MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(),
						this.field_175801_g, 1);
			}
		}

	}

	public String getStructureName() {
		return "Monument";
	}

	protected boolean canSpawnStructureAtCoords(int i, int j) {
		int k = i;
		int l = j;
		if (i < 0) {
			i -= this.field_175800_f - 1;
		}

		if (j < 0) {
			j -= this.field_175800_f - 1;
		}

		int i1 = i / this.field_175800_f;
		int j1 = j / this.field_175800_f;
		EaglercraftRandom random = this.worldObj.setRandomSeed(i1, j1, 10387313);
		i1 = i1 * this.field_175800_f;
		j1 = j1 * this.field_175800_f;
		i1 = i1 + (random.nextInt(this.field_175800_f - this.field_175801_g)
				+ random.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
		j1 = j1 + (random.nextInt(this.field_175800_f - this.field_175801_g)
				+ random.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
		if (k == i1 && l == j1) {
			if (this.worldObj.getWorldChunkManager().getBiomeGenerator(new BlockPos(k * 16 + 8, 64, l * 16 + 8),
					(BiomeGenBase) null) != BiomeGenBase.deepOcean) {
				return false;
			}

			boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(k * 16 + 8, l * 16 + 8, 29,
					field_175802_d);
			if (flag) {
				return true;
			}
		}

		return false;
	}

	protected StructureStart getStructureStart(int i, int j) {
		return new StructureOceanMonument.StartMonument(this.worldObj, this.rand, i, j);
	}

	public List<BiomeGenBase.SpawnListEntry> func_175799_b() {
		return field_175803_h;
	}

	static {
		field_175803_h.add(new BiomeGenBase.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
	}

	public static class StartMonument extends StructureStart {
		private Set<ChunkCoordIntPair> field_175791_c = Sets.newHashSet();
		private boolean field_175790_d;

		public StartMonument() {
		}

		public StartMonument(World worldIn, EaglercraftRandom parRandom, int parInt1, int parInt2) {
			super(parInt1, parInt2);
			this.func_175789_b(worldIn, parRandom, parInt1, parInt2);
		}

		private void func_175789_b(World worldIn, EaglercraftRandom parRandom, int parInt1, int parInt2) {
			parRandom.setSeed(worldIn.getSeed());
			long i = parRandom.nextLong();
			long j = parRandom.nextLong();
			long k = (long) parInt1 * i;
			long l = (long) parInt2 * j;
			parRandom.setSeed(k ^ l ^ worldIn.getSeed());
			int i1 = parInt1 * 16 + 8 - 29;
			int j1 = parInt2 * 16 + 8 - 29;
			EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(parRandom);
			this.components.add(new StructureOceanMonumentPieces.MonumentBuilding(parRandom, i1, j1, enumfacing));
			this.updateBoundingBox();
			this.field_175790_d = true;
		}

		public void generateStructure(World worldIn, EaglercraftRandom rand, StructureBoundingBox structurebb) {
			if (!this.field_175790_d) {
				this.components.clear();
				this.func_175789_b(worldIn, rand, this.getChunkPosX(), this.getChunkPosZ());
			}

			super.generateStructure(worldIn, rand, structurebb);
		}

		public boolean func_175788_a(ChunkCoordIntPair pair) {
			return this.field_175791_c.contains(pair) ? false : super.func_175788_a(pair);
		}

		public void func_175787_b(ChunkCoordIntPair pair) {
			super.func_175787_b(pair);
			this.field_175791_c.add(pair);
		}

		public void writeToNBT(NBTTagCompound tagCompound) {
			super.writeToNBT(tagCompound);
			NBTTagList nbttaglist = new NBTTagList();

			for (ChunkCoordIntPair chunkcoordintpair : this.field_175791_c) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setInteger("X", chunkcoordintpair.chunkXPos);
				nbttagcompound.setInteger("Z", chunkcoordintpair.chunkZPos);
				nbttaglist.appendTag(nbttagcompound);
			}

			tagCompound.setTag("Processed", nbttaglist);
		}

		public void readFromNBT(NBTTagCompound tagCompound) {
			super.readFromNBT(tagCompound);
			if (tagCompound.hasKey("Processed", 9)) {
				NBTTagList nbttaglist = tagCompound.getTagList("Processed", 10);

				for (int i = 0; i < nbttaglist.tagCount(); ++i) {
					NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
					this.field_175791_c
							.add(new ChunkCoordIntPair(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Z")));
				}
			}

		}
	}
}