package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import java.util.Map.Entry;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.BlockPos;
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
public class MapGenScatteredFeature extends MapGenStructure {
	private static final List<BiomeGenBase> biomelist = Arrays.asList(new BiomeGenBase[] { BiomeGenBase.desert,
			BiomeGenBase.desertHills, BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.swampland });
	private List<BiomeGenBase.SpawnListEntry> scatteredFeatureSpawnList;
	private int maxDistanceBetweenScatteredFeatures;
	private int minDistanceBetweenScatteredFeatures;

	public MapGenScatteredFeature(boolean scramble) {
		super(scramble);
		this.scatteredFeatureSpawnList = Lists.newArrayList();
		this.maxDistanceBetweenScatteredFeatures = 32;
		this.minDistanceBetweenScatteredFeatures = 8;
		this.scatteredFeatureSpawnList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 1, 1, 1));
	}

	public MapGenScatteredFeature(Map<String, String> parMap, boolean scramble) {
		this(scramble);

		for (Entry entry : parMap.entrySet()) {
			if (((String) entry.getKey()).equals("distance")) {
				this.maxDistanceBetweenScatteredFeatures = MathHelper.parseIntWithDefaultAndMax(
						(String) entry.getValue(), this.maxDistanceBetweenScatteredFeatures,
						this.minDistanceBetweenScatteredFeatures + 1);
			}
		}

	}

	public String getStructureName() {
		return "Temple";
	}

	protected boolean canSpawnStructureAtCoords(int i, int j) {
		int k = i;
		int l = j;
		if (i < 0) {
			i -= this.maxDistanceBetweenScatteredFeatures - 1;
		}

		if (j < 0) {
			j -= this.maxDistanceBetweenScatteredFeatures - 1;
		}

		int i1 = i / this.maxDistanceBetweenScatteredFeatures;
		int j1 = j / this.maxDistanceBetweenScatteredFeatures;
		EaglercraftRandom random = this.worldObj.setRandomSeed(i1, j1, 14357617);
		i1 = i1 * this.maxDistanceBetweenScatteredFeatures;
		j1 = j1 * this.maxDistanceBetweenScatteredFeatures;
		i1 = i1 + random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
		j1 = j1 + random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
		if (k == i1 && l == j1) {
			BiomeGenBase biomegenbase = this.worldObj.getWorldChunkManager()
					.getBiomeGenerator(new BlockPos(k * 16 + 8, 0, l * 16 + 8));
			if (biomegenbase == null) {
				return false;
			}

			for (int m = 0, n = biomelist.size(); m < n; ++m) {
				if (biomegenbase == biomelist.get(m)) {
					return true;
				}
			}
		}

		return false;
	}

	protected StructureStart getStructureStart(int i, int j) {
		return new MapGenScatteredFeature.Start(this.worldObj, this.rand, i, j);
	}

	public boolean func_175798_a(BlockPos parBlockPos) {
		StructureStart structurestart = this.func_175797_c(parBlockPos);
		if (structurestart != null && structurestart instanceof MapGenScatteredFeature.Start
				&& !structurestart.components.isEmpty()) {
			StructureComponent structurecomponent = (StructureComponent) structurestart.components.getFirst();
			return structurecomponent instanceof ComponentScatteredFeaturePieces.SwampHut;
		} else {
			return false;
		}
	}

	/**+
	 * returns possible spawns for scattered features
	 */
	public List<BiomeGenBase.SpawnListEntry> getScatteredFeatureSpawnList() {
		return this.scatteredFeatureSpawnList;
	}

	public static class Start extends StructureStart {
		public Start() {
		}

		public Start(World worldIn, EaglercraftRandom parRandom, int parInt1, int parInt2) {
			super(parInt1, parInt2);
			BiomeGenBase biomegenbase = worldIn
					.getBiomeGenForCoords(new BlockPos(parInt1 * 16 + 8, 0, parInt2 * 16 + 8));
			if (biomegenbase != BiomeGenBase.jungle && biomegenbase != BiomeGenBase.jungleHills) {
				if (biomegenbase == BiomeGenBase.swampland) {
					ComponentScatteredFeaturePieces.SwampHut componentscatteredfeaturepieces$swamphut = new ComponentScatteredFeaturePieces.SwampHut(
							parRandom, parInt1 * 16, parInt2 * 16);
					this.components.add(componentscatteredfeaturepieces$swamphut);
				} else if (biomegenbase == BiomeGenBase.desert || biomegenbase == BiomeGenBase.desertHills) {
					ComponentScatteredFeaturePieces.DesertPyramid componentscatteredfeaturepieces$desertpyramid = new ComponentScatteredFeaturePieces.DesertPyramid(
							parRandom, parInt1 * 16, parInt2 * 16);
					this.components.add(componentscatteredfeaturepieces$desertpyramid);
				}
			} else {
				ComponentScatteredFeaturePieces.JunglePyramid componentscatteredfeaturepieces$junglepyramid = new ComponentScatteredFeaturePieces.JunglePyramid(
						parRandom, parInt1 * 16, parInt2 * 16);
				this.components.add(componentscatteredfeaturepieces$junglepyramid);
			}

			this.updateBoundingBox();
		}
	}
}