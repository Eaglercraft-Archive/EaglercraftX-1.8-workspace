package net.minecraft.world.gen.structure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import java.util.Map.Entry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
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
public class MapGenVillage extends MapGenStructure {
	/**+
	 * A list of all the biomes villages can spawn in.
	 */
	public static final List<BiomeGenBase> villageSpawnBiomes = Arrays
			.asList(new BiomeGenBase[] { BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.savanna });
	private int terrainType;
	private int field_82665_g;
	private int field_82666_h;

	public MapGenVillage(boolean scramble) {
		super(scramble);
		this.field_82665_g = 32;
		this.field_82666_h = 8;
	}

	public MapGenVillage(Map<String, String> parMap, boolean scramble) {
		this(scramble);

		for (Entry entry : parMap.entrySet()) {
			if (((String) entry.getKey()).equals("size")) {
				this.terrainType = MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(), this.terrainType, 0);
			} else if (((String) entry.getKey()).equals("distance")) {
				this.field_82665_g = MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(), this.field_82665_g,
						this.field_82666_h + 1);
			}
		}

	}

	public String getStructureName() {
		return "Village";
	}

	protected boolean canSpawnStructureAtCoords(int i, int j) {
		int k = i;
		int l = j;
		if (i < 0) {
			i -= this.field_82665_g - 1;
		}

		if (j < 0) {
			j -= this.field_82665_g - 1;
		}

		int i1 = i / this.field_82665_g;
		int j1 = j / this.field_82665_g;
		EaglercraftRandom random = this.worldObj.setRandomSeed(i1, j1, 10387312);
		i1 = i1 * this.field_82665_g;
		j1 = j1 * this.field_82665_g;
		i1 = i1 + random.nextInt(this.field_82665_g - this.field_82666_h);
		j1 = j1 + random.nextInt(this.field_82665_g - this.field_82666_h);
		if (k == i1 && l == j1) {
			boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(k * 16 + 8, l * 16 + 8, 0,
					villageSpawnBiomes);
			if (flag) {
				return true;
			}
		}

		return false;
	}

	protected StructureStart getStructureStart(int i, int j) {
		return new MapGenVillage.Start(this.worldObj, this.rand, i, j, this.terrainType);
	}

	public static class Start extends StructureStart {
		private boolean hasMoreThanTwoComponents;

		public Start() {
		}

		public Start(World worldIn, EaglercraftRandom rand, int x, int z, int parInt1) {
			super(x, z);
			List list = StructureVillagePieces.getStructureVillageWeightedPieceList(rand, parInt1);
			StructureVillagePieces.Start structurevillagepieces$start = new StructureVillagePieces.Start(
					worldIn.getWorldChunkManager(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, parInt1);
			this.components.add(structurevillagepieces$start);
			structurevillagepieces$start.buildComponent(structurevillagepieces$start, this.components, rand);
			List list1 = structurevillagepieces$start.field_74930_j;
			List list2 = structurevillagepieces$start.field_74932_i;

			while (!list1.isEmpty() || !list2.isEmpty()) {
				if (list1.isEmpty()) {
					int i = rand.nextInt(list2.size());
					StructureComponent structurecomponent = (StructureComponent) list2.remove(i);
					structurecomponent.buildComponent(structurevillagepieces$start, this.components, rand);
				} else {
					int j = rand.nextInt(list1.size());
					StructureComponent structurecomponent2 = (StructureComponent) list1.remove(j);
					structurecomponent2.buildComponent(structurevillagepieces$start, this.components, rand);
				}
			}

			this.updateBoundingBox();
			int k = 0;

			for (StructureComponent structurecomponent1 : this.components) {
				if (!(structurecomponent1 instanceof StructureVillagePieces.Road)) {
					++k;
				}
			}

			this.hasMoreThanTwoComponents = k > 2;
		}

		public boolean isSizeableStructure() {
			return this.hasMoreThanTwoComponents;
		}

		public void writeToNBT(NBTTagCompound nbttagcompound) {
			super.writeToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
		}

		public void readFromNBT(NBTTagCompound nbttagcompound) {
			super.readFromNBT(nbttagcompound);
			this.hasMoreThanTwoComponents = nbttagcompound.getBoolean("Valid");
		}
	}
}