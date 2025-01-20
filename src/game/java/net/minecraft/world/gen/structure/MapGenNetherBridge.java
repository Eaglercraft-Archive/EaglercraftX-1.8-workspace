package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
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
public class MapGenNetherBridge extends MapGenStructure {
	private List<BiomeGenBase.SpawnListEntry> spawnList = Lists.newArrayList();

	public MapGenNetherBridge(boolean scramble) {
		super(scramble);
		this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityBlaze.class, 10, 2, 3));
		this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
		this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
		this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
	}

	public String getStructureName() {
		return "Fortress";
	}

	public List<BiomeGenBase.SpawnListEntry> getSpawnList() {
		return this.spawnList;
	}

	protected boolean canSpawnStructureAtCoords(int i, int j) {
		int k = i >> 4;
		int l = j >> 4;
		this.rand.setSeed((long) (k ^ l << 4) ^ this.worldObj.getSeed());
		this.rand.nextInt();
		return this.rand.nextInt(3) != 0 ? false
				: (i != (k << 4) + 4 + this.rand.nextInt(8) ? false : j == (l << 4) + 4 + this.rand.nextInt(8));
	}

	protected StructureStart getStructureStart(int i, int j) {
		return new MapGenNetherBridge.Start(this.worldObj, this.rand, i, j);
	}

	public static class Start extends StructureStart {
		public Start() {
		}

		public Start(World worldIn, EaglercraftRandom parRandom, int parInt1, int parInt2) {
			super(parInt1, parInt2);
			StructureNetherBridgePieces.Start structurenetherbridgepieces$start = new StructureNetherBridgePieces.Start(
					parRandom, (parInt1 << 4) + 2, (parInt2 << 4) + 2);
			this.components.add(structurenetherbridgepieces$start);
			structurenetherbridgepieces$start.buildComponent(structurenetherbridgepieces$start, this.components,
					parRandom);
			List list = structurenetherbridgepieces$start.field_74967_d;

			while (!list.isEmpty()) {
				int i = parRandom.nextInt(list.size());
				StructureComponent structurecomponent = (StructureComponent) list.remove(i);
				structurecomponent.buildComponent(structurenetherbridgepieces$start, this.components, parRandom);
			}

			this.updateBoundingBox();
			this.setRandomHeight(worldIn, parRandom, 48, 70);
		}
	}
}