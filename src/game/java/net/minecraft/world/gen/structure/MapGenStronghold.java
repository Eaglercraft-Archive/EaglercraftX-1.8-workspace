package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import java.util.Map.Entry;
import net.minecraft.util.BlockPos;
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
public class MapGenStronghold extends MapGenStructure {
	private List<BiomeGenBase> field_151546_e;
	private boolean ranBiomeCheck;
	private ChunkCoordIntPair[] structureCoords;
	private double field_82671_h;
	private int field_82672_i;

	public MapGenStronghold(boolean scramble) {
		super(scramble);
		this.structureCoords = new ChunkCoordIntPair[3];
		this.field_82671_h = 32.0D;
		this.field_82672_i = 3;
		this.field_151546_e = Lists.newArrayList();

		BiomeGenBase[] biomes = BiomeGenBase.getBiomeGenArray();
		for (int i = 0; i < biomes.length; ++i) {
			BiomeGenBase biomegenbase = biomes[i];
			if (biomegenbase != null && biomegenbase.minHeight > 0.0F) {
				this.field_151546_e.add(biomegenbase);
			}
		}

	}

	public MapGenStronghold(Map<String, String> parMap, boolean scramble) {
		this(scramble);

		for (Entry entry : parMap.entrySet()) {
			if (((String) entry.getKey()).equals("distance")) {
				this.field_82671_h = MathHelper.parseDoubleWithDefaultAndMax((String) entry.getValue(),
						this.field_82671_h, 1.0D);
			} else if (((String) entry.getKey()).equals("count")) {
				this.structureCoords = new ChunkCoordIntPair[MathHelper
						.parseIntWithDefaultAndMax((String) entry.getValue(), this.structureCoords.length, 1)];
			} else if (((String) entry.getKey()).equals("spread")) {
				this.field_82672_i = MathHelper.parseIntWithDefaultAndMax((String) entry.getValue(), this.field_82672_i,
						1);
			}
		}

	}

	public String getStructureName() {
		return "Stronghold";
	}

	protected boolean canSpawnStructureAtCoords(int i, int j) {
		if (!this.ranBiomeCheck) {
			EaglercraftRandom random = new EaglercraftRandom(!this.worldObj.getWorldInfo().isOldEaglercraftRandom());
			random.setSeed(this.worldObj.getSeed());
			double d0 = random.nextDouble() * 3.141592653589793D * 2.0D;
			int k = 1;

			for (int l = 0; l < this.structureCoords.length; ++l) {
				double d1 = (1.25D * (double) k + random.nextDouble()) * this.field_82671_h * (double) k;
				int i1 = (int) Math.round(Math.cos(d0) * d1);
				int j1 = (int) Math.round(Math.sin(d0) * d1);
				BlockPos blockpos = this.worldObj.getWorldChunkManager().findBiomePosition((i1 << 4) + 8, (j1 << 4) + 8,
						112, this.field_151546_e, random);
				if (blockpos != null) {
					i1 = blockpos.getX() >> 4;
					j1 = blockpos.getZ() >> 4;
				}

				this.structureCoords[l] = new ChunkCoordIntPair(i1, j1);
				d0 += 6.283185307179586D * (double) k / (double) this.field_82672_i;
				if (l == this.field_82672_i) {
					k += 2 + random.nextInt(5);
					this.field_82672_i += 1 + random.nextInt(2);
				}
			}

			this.ranBiomeCheck = true;
		}

		for (int l = 0; l < this.structureCoords.length; ++l) {
			ChunkCoordIntPair chunkcoordintpair = this.structureCoords[l];
			if (i == chunkcoordintpair.chunkXPos && j == chunkcoordintpair.chunkZPos) {
				return true;
			}
		}

		return false;
	}

	/**+
	 * Returns a list of other locations at which the structure
	 * generation has been run, or null if not relevant to this
	 * structure generator.
	 */
	protected List<BlockPos> getCoordList() {
		ArrayList arraylist = Lists.newArrayList();

		for (int l = 0; l < this.structureCoords.length; ++l) {
			ChunkCoordIntPair chunkcoordintpair = this.structureCoords[l];
			if (chunkcoordintpair != null) {
				arraylist.add(chunkcoordintpair.getCenterBlock(64));
			}
		}

		return arraylist;
	}

	protected StructureStart getStructureStart(int i, int j) {
		MapGenStronghold.Start mapgenstronghold$start;
		for (mapgenstronghold$start = new MapGenStronghold.Start(this.worldObj, this.rand, i, j); mapgenstronghold$start
				.getComponents().isEmpty()
				|| ((StructureStrongholdPieces.Stairs2) mapgenstronghold$start.getComponents()
						.get(0)).strongholdPortalRoom == null; mapgenstronghold$start = new MapGenStronghold.Start(
								this.worldObj, this.rand, i, j)) {
			;
		}

		return mapgenstronghold$start;
	}

	public static class Start extends StructureStart {
		public Start() {
		}

		public Start(World worldIn, EaglercraftRandom parRandom, int parInt1, int parInt2) {
			super(parInt1, parInt2);
			StructureStrongholdPieces.prepareStructurePieces();
			StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(
					0, parRandom, (parInt1 << 4) + 2, (parInt2 << 4) + 2);
			this.components.add(structurestrongholdpieces$stairs2);
			structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, this.components,
					parRandom);
			List list = structurestrongholdpieces$stairs2.field_75026_c;

			while (!list.isEmpty()) {
				int i = parRandom.nextInt(list.size());
				StructureComponent structurecomponent = (StructureComponent) list.remove(i);
				structurecomponent.buildComponent(structurestrongholdpieces$stairs2, this.components, parRandom);
			}

			this.updateBoundingBox();
			this.markAvailableHeight(worldIn, parRandom, 10);
		}
	}
}