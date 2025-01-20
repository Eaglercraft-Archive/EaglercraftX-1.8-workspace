package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

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
public class StructureVillagePieces {
	public static void registerVillagePieces() {
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House1.class,
				StructureVillagePieces.House1::new, "ViBH");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Field1.class,
				StructureVillagePieces.Field1::new, "ViDF");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Field2.class,
				StructureVillagePieces.Field2::new, "ViF");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Torch.class,
				StructureVillagePieces.Torch::new, "ViL");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Hall.class,
				StructureVillagePieces.Hall::new, "ViPH");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House4Garden.class,
				StructureVillagePieces.House4Garden::new, "ViSH");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.WoodHut.class,
				StructureVillagePieces.WoodHut::new, "ViSmH");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Church.class,
				StructureVillagePieces.Church::new, "ViST");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House2.class,
				StructureVillagePieces.House2::new, "ViS");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Start.class,
				StructureVillagePieces.Start::new, "ViStart");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Path.class,
				StructureVillagePieces.Path::new, "ViSR");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House3.class,
				StructureVillagePieces.House3::new, "ViTRH");
		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Well.class,
				StructureVillagePieces.Well::new, "ViW");
	}

	public static List<StructureVillagePieces.PieceWeight> getStructureVillageWeightedPieceList(
			EaglercraftRandom random, int parInt1) {
		ArrayList arraylist = Lists.newArrayList();
		arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House4Garden.class, 4,
				MathHelper.getRandomIntegerInRange(random, 2 + parInt1, 4 + parInt1 * 2)));
		arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Church.class, 20,
				MathHelper.getRandomIntegerInRange(random, 0 + parInt1, 1 + parInt1)));
		arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House1.class, 20,
				MathHelper.getRandomIntegerInRange(random, 0 + parInt1, 2 + parInt1)));
		arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.WoodHut.class, 3,
				MathHelper.getRandomIntegerInRange(random, 2 + parInt1, 5 + parInt1 * 3)));
		arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Hall.class, 15,
				MathHelper.getRandomIntegerInRange(random, 0 + parInt1, 2 + parInt1)));
		arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field1.class, 3,
				MathHelper.getRandomIntegerInRange(random, 1 + parInt1, 4 + parInt1)));
		arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field2.class, 3,
				MathHelper.getRandomIntegerInRange(random, 2 + parInt1, 4 + parInt1 * 2)));
		arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House2.class, 15,
				MathHelper.getRandomIntegerInRange(random, 0, 1 + parInt1)));
		arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House3.class, 8,
				MathHelper.getRandomIntegerInRange(random, 0 + parInt1, 3 + parInt1 * 2)));
		Iterator iterator = arraylist.iterator();

		while (iterator.hasNext()) {
			if (((StructureVillagePieces.PieceWeight) iterator.next()).villagePiecesLimit == 0) {
				iterator.remove();
			}
		}

		return arraylist;
	}

	private static int func_75079_a(List<StructureVillagePieces.PieceWeight> parList) {
		boolean flag = false;
		int i = 0;

		for (StructureVillagePieces.PieceWeight structurevillagepieces$pieceweight : parList) {
			if (structurevillagepieces$pieceweight.villagePiecesLimit > 0
					&& structurevillagepieces$pieceweight.villagePiecesSpawned < structurevillagepieces$pieceweight.villagePiecesLimit) {
				flag = true;
			}

			i += structurevillagepieces$pieceweight.villagePieceWeight;
		}

		return flag ? i : -1;
	}

	private static StructureVillagePieces.Village func_176065_a(StructureVillagePieces.Start start,
			StructureVillagePieces.PieceWeight weight, List<StructureComponent> rand, EaglercraftRandom facing,
			int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing, int parInt4) {
		Class oclass = weight.villagePieceClass;
		Object object = null;
		if (oclass == StructureVillagePieces.House4Garden.class) {
			object = StructureVillagePieces.House4Garden.func_175858_a(start, rand, facing, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureVillagePieces.Church.class) {
			object = StructureVillagePieces.Church.func_175854_a(start, rand, facing, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureVillagePieces.House1.class) {
			object = StructureVillagePieces.House1.func_175850_a(start, rand, facing, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureVillagePieces.WoodHut.class) {
			object = StructureVillagePieces.WoodHut.func_175853_a(start, rand, facing, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureVillagePieces.Hall.class) {
			object = StructureVillagePieces.Hall.func_175857_a(start, rand, facing, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureVillagePieces.Field1.class) {
			object = StructureVillagePieces.Field1.func_175851_a(start, rand, facing, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureVillagePieces.Field2.class) {
			object = StructureVillagePieces.Field2.func_175852_a(start, rand, facing, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureVillagePieces.House2.class) {
			object = StructureVillagePieces.House2.func_175855_a(start, rand, facing, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureVillagePieces.House3.class) {
			object = StructureVillagePieces.House3.func_175849_a(start, rand, facing, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		}

		return (StructureVillagePieces.Village) object;
	}

	private static StructureVillagePieces.Village func_176067_c(StructureVillagePieces.Start start,
			List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
			EnumFacing parEnumFacing, int parInt4) {
		int i = func_75079_a(start.structureVillageWeightedPieceList);
		if (i <= 0) {
			return null;
		} else {
			int j = 0;

			while (j < 5) {
				++j;
				int k = facing.nextInt(i);

				for (StructureVillagePieces.PieceWeight structurevillagepieces$pieceweight : start.structureVillageWeightedPieceList) {
					k -= structurevillagepieces$pieceweight.villagePieceWeight;
					if (k < 0) {
						if (!structurevillagepieces$pieceweight.canSpawnMoreVillagePiecesOfType(parInt4)
								|| structurevillagepieces$pieceweight == start.structVillagePieceWeight
										&& start.structureVillageWeightedPieceList.size() > 1) {
							break;
						}

						StructureVillagePieces.Village structurevillagepieces$village = func_176065_a(start,
								structurevillagepieces$pieceweight, rand, facing, parInt1, parInt2, parInt3,
								parEnumFacing, parInt4);
						if (structurevillagepieces$village != null) {
							++structurevillagepieces$pieceweight.villagePiecesSpawned;
							start.structVillagePieceWeight = structurevillagepieces$pieceweight;
							if (!structurevillagepieces$pieceweight.canSpawnMoreVillagePieces()) {
								start.structureVillageWeightedPieceList.remove(structurevillagepieces$pieceweight);
							}

							return structurevillagepieces$village;
						}
					}
				}
			}

			StructureBoundingBox structureboundingbox = StructureVillagePieces.Torch.func_175856_a(start, rand, facing,
					parInt1, parInt2, parInt3, parEnumFacing);
			if (structureboundingbox != null) {
				return new StructureVillagePieces.Torch(start, parInt4, facing, structureboundingbox, parEnumFacing);
			} else {
				return null;
			}
		}
	}

	private static StructureComponent func_176066_d(StructureVillagePieces.Start start, List<StructureComponent> rand,
			EaglercraftRandom facing, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing, int parInt4) {
		if (parInt4 > 50) {
			return null;
		} else if (Math.abs(parInt1 - start.getBoundingBox().minX) <= 112
				&& Math.abs(parInt3 - start.getBoundingBox().minZ) <= 112) {
			StructureVillagePieces.Village structurevillagepieces$village = func_176067_c(start, rand, facing, parInt1,
					parInt2, parInt3, parEnumFacing, parInt4 + 1);
			if (structurevillagepieces$village != null) {
				int i = (structurevillagepieces$village.boundingBox.minX
						+ structurevillagepieces$village.boundingBox.maxX) / 2;
				int j = (structurevillagepieces$village.boundingBox.minZ
						+ structurevillagepieces$village.boundingBox.maxZ) / 2;
				int k = structurevillagepieces$village.boundingBox.maxX
						- structurevillagepieces$village.boundingBox.minX;
				int l = structurevillagepieces$village.boundingBox.maxZ
						- structurevillagepieces$village.boundingBox.minZ;
				int i1 = k > l ? k : l;
				if (start.getWorldChunkManager().areBiomesViable(i, j, i1 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
					rand.add(structurevillagepieces$village);
					start.field_74932_i.add(structurevillagepieces$village);
					return structurevillagepieces$village;
				}
			}

			return null;
		} else {
			return null;
		}
	}

	private static StructureComponent func_176069_e(StructureVillagePieces.Start start, List<StructureComponent> rand,
			EaglercraftRandom facing, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing, int parInt4) {
		if (parInt4 > 3 + start.terrainType) {
			return null;
		} else if (Math.abs(parInt1 - start.getBoundingBox().minX) <= 112
				&& Math.abs(parInt3 - start.getBoundingBox().minZ) <= 112) {
			StructureBoundingBox structureboundingbox = StructureVillagePieces.Path.func_175848_a(start, rand, facing,
					parInt1, parInt2, parInt3, parEnumFacing);
			if (structureboundingbox != null && structureboundingbox.minY > 10) {
				StructureVillagePieces.Path structurevillagepieces$path = new StructureVillagePieces.Path(start,
						parInt4, facing, structureboundingbox, parEnumFacing);
				int i = (structurevillagepieces$path.boundingBox.minX + structurevillagepieces$path.boundingBox.maxX)
						/ 2;
				int j = (structurevillagepieces$path.boundingBox.minZ + structurevillagepieces$path.boundingBox.maxZ)
						/ 2;
				int k = structurevillagepieces$path.boundingBox.maxX - structurevillagepieces$path.boundingBox.minX;
				int l = structurevillagepieces$path.boundingBox.maxZ - structurevillagepieces$path.boundingBox.minZ;
				int i1 = k > l ? k : l;
				if (start.getWorldChunkManager().areBiomesViable(i, j, i1 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
					rand.add(structurevillagepieces$path);
					start.field_74930_j.add(structurevillagepieces$path);
					return structurevillagepieces$path;
				}
			}

			return null;
		} else {
			return null;
		}
	}

	public static class Church extends StructureVillagePieces.Village {
		public Church() {
		}

		public Church(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,
				StructureBoundingBox parStructureBoundingBox, EnumFacing facing) {
			super(start, parInt1);
			this.coordBaseMode = facing;
			this.boundingBox = parStructureBoundingBox;
		}

		public static StructureVillagePieces.Church func_175854_a(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing, int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, 0, 0, 0, 5, 12, 9, parEnumFacing);
			return canVillageGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(rand, structureboundingbox) == null
							? new StructureVillagePieces.Church(start, parInt4, facing, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.field_143015_k < 0) {
				this.field_143015_k = this.getAverageGroundLevel(world, structureboundingbox);
				if (this.field_143015_k < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 12 - 1, 0);
			}

			this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 3, 3, 7, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 5, 1, 3, 9, 3, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 3, 0, 8, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 3, 10, 0, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 10, 3, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 10, 3, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 4, 0, 4, 7, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 0, 4, 4, 4, 7, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 8, 3, 4, 8, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 5, 4, 3, 10, 4, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 5, 5, 3, 5, 7, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 9, 0, 4, 9, 4, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 4, 0, 4, 4, 4, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 11, 2, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 11, 2, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 2, 11, 0, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 2, 11, 4, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 1, 1, 6, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 1, 1, 7, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 2, 1, 7, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 3, 1, 6, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 3, 1, 7, structureboundingbox);
			this.setBlockState(world,
					Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 1, 5,
					structureboundingbox);
			this.setBlockState(world,
					Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 1, 6,
					structureboundingbox);
			this.setBlockState(world,
					Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 3, 1, 5,
					structureboundingbox);
			this.setBlockState(world,
					Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 1)), 1, 2, 7,
					structureboundingbox);
			this.setBlockState(world,
					Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 0)), 3, 2, 7,
					structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 3, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 3, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 6, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 7, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 6, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 7, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 6, 0, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 7, 0, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 6, 4, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 7, 4, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 3, 6, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 3, 6, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 3, 8, structureboundingbox);
			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 2,
					4, 7, structureboundingbox);
			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateY()), 1, 4,
					6, structureboundingbox);
			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateYCCW()), 3,
					4, 6, structureboundingbox);
			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 4, 5,
					structureboundingbox);
			int i = this.getMetadataWithOffset(Blocks.ladder, 4);

			for (int j = 1; j <= 9; ++j) {
				this.setBlockState(world, Blocks.ladder.getStateFromMeta(i), 3, j, 3, structureboundingbox);
			}

			this.setBlockState(world, Blocks.air.getDefaultState(), 2, 1, 0, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 2, 2, 0, structureboundingbox);
			this.placeDoorCurrentPosition(world, structureboundingbox, random, 2, 1, 0,
					EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
			if (this.getBlockStateFromPos(world, 2, 0, -1, structureboundingbox).getBlock()
					.getMaterial() == Material.air
					&& this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getBlock()
							.getMaterial() != Material.air) {
				this.setBlockState(world,
						Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0,
						-1, structureboundingbox);
			}

			for (int l = 0; l < 9; ++l) {
				for (int k = 0; k < 5; ++k) {
					this.clearCurrentPositionBlocksUpwards(world, k, 12, l, structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), k, -1, l,
							structureboundingbox);
				}
			}

			this.spawnVillagers(world, structureboundingbox, 2, 1, 2, 1);
			return true;
		}

		protected int func_180779_c(int var1, int var2) {
			return 2;
		}
	}

	public static class Field1 extends StructureVillagePieces.Village {
		private Block cropTypeA;
		private Block cropTypeB;
		private Block cropTypeC;
		private Block cropTypeD;

		public Field1() {
		}

		public Field1(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,
				StructureBoundingBox parStructureBoundingBox, EnumFacing facing) {
			super(start, parInt1);
			this.coordBaseMode = facing;
			this.boundingBox = parStructureBoundingBox;
			this.cropTypeA = this.func_151559_a(rand);
			this.cropTypeB = this.func_151559_a(rand);
			this.cropTypeC = this.func_151559_a(rand);
			this.cropTypeD = this.func_151559_a(rand);
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setInteger("CA", Block.blockRegistry.getIDForObject(this.cropTypeA));
			nbttagcompound.setInteger("CB", Block.blockRegistry.getIDForObject(this.cropTypeB));
			nbttagcompound.setInteger("CC", Block.blockRegistry.getIDForObject(this.cropTypeC));
			nbttagcompound.setInteger("CD", Block.blockRegistry.getIDForObject(this.cropTypeD));
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.cropTypeA = Block.getBlockById(nbttagcompound.getInteger("CA"));
			this.cropTypeB = Block.getBlockById(nbttagcompound.getInteger("CB"));
			this.cropTypeC = Block.getBlockById(nbttagcompound.getInteger("CC"));
			this.cropTypeD = Block.getBlockById(nbttagcompound.getInteger("CD"));
		}

		private Block func_151559_a(EaglercraftRandom rand) {
			switch (rand.nextInt(5)) {
			case 0:
				return Blocks.carrots;
			case 1:
				return Blocks.potatoes;
			default:
				return Blocks.wheat;
			}
		}

		public static StructureVillagePieces.Field1 func_175851_a(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing, int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, 0, 0, 0, 13, 4, 9, parEnumFacing);
			return canVillageGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(rand, structureboundingbox) == null
							? new StructureVillagePieces.Field1(start, parInt4, facing, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.field_143015_k < 0) {
				this.field_143015_k = this.getAverageGroundLevel(world, structureboundingbox);
				if (this.field_143015_k < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
			}

			this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 12, 4, 8, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(),
					Blocks.farmland.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(),
					Blocks.farmland.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 7, 0, 1, 8, 0, 7, Blocks.farmland.getDefaultState(),
					Blocks.farmland.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 10, 0, 1, 11, 0, 7, Blocks.farmland.getDefaultState(),
					Blocks.farmland.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 12, 0, 0, 12, 0, 8, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 11, 0, 0, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 8, 11, 0, 8, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 0, 1, 3, 0, 7, Blocks.water.getDefaultState(),
					Blocks.water.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 9, 0, 1, 9, 0, 7, Blocks.water.getDefaultState(),
					Blocks.water.getDefaultState(), false);

			for (int i = 1; i <= 7; ++i) {
				this.setBlockState(world,
						this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 1, 1, i,
						structureboundingbox);
				this.setBlockState(world,
						this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 2, 1, i,
						structureboundingbox);
				this.setBlockState(world,
						this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 4, 1, i,
						structureboundingbox);
				this.setBlockState(world,
						this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 5, 1, i,
						structureboundingbox);
				this.setBlockState(world,
						this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 7, 1, i,
						structureboundingbox);
				this.setBlockState(world,
						this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 8, 1, i,
						structureboundingbox);
				this.setBlockState(world,
						this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 10, 1, i,
						structureboundingbox);
				this.setBlockState(world,
						this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 11, 1, i,
						structureboundingbox);
			}

			for (int k = 0; k < 9; ++k) {
				for (int j = 0; j < 13; ++j) {
					this.clearCurrentPositionBlocksUpwards(world, j, 4, k, structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.dirt.getDefaultState(), j, -1, k,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	public static class Field2 extends StructureVillagePieces.Village {
		private Block cropTypeA;
		private Block cropTypeB;

		public Field2() {
		}

		public Field2(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,
				StructureBoundingBox parStructureBoundingBox, EnumFacing facing) {
			super(start, parInt1);
			this.coordBaseMode = facing;
			this.boundingBox = parStructureBoundingBox;
			this.cropTypeA = this.func_151560_a(rand);
			this.cropTypeB = this.func_151560_a(rand);
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setInteger("CA", Block.blockRegistry.getIDForObject(this.cropTypeA));
			nbttagcompound.setInteger("CB", Block.blockRegistry.getIDForObject(this.cropTypeB));
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.cropTypeA = Block.getBlockById(nbttagcompound.getInteger("CA"));
			this.cropTypeB = Block.getBlockById(nbttagcompound.getInteger("CB"));
		}

		private Block func_151560_a(EaglercraftRandom rand) {
			switch (rand.nextInt(5)) {
			case 0:
				return Blocks.carrots;
			case 1:
				return Blocks.potatoes;
			default:
				return Blocks.wheat;
			}
		}

		public static StructureVillagePieces.Field2 func_175852_a(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing, int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, 0, 0, 0, 7, 4, 9, parEnumFacing);
			return canVillageGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(rand, structureboundingbox) == null
							? new StructureVillagePieces.Field2(start, parInt4, facing, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.field_143015_k < 0) {
				this.field_143015_k = this.getAverageGroundLevel(world, structureboundingbox);
				if (this.field_143015_k < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
			}

			this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 6, 4, 8, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(),
					Blocks.farmland.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(),
					Blocks.farmland.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 5, 0, 0, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 8, 5, 0, 8, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 0, 1, 3, 0, 7, Blocks.water.getDefaultState(),
					Blocks.water.getDefaultState(), false);

			for (int i = 1; i <= 7; ++i) {
				this.setBlockState(world,
						this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 1, 1, i,
						structureboundingbox);
				this.setBlockState(world,
						this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 2, 1, i,
						structureboundingbox);
				this.setBlockState(world,
						this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 4, 1, i,
						structureboundingbox);
				this.setBlockState(world,
						this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(random, 2, 7)), 5, 1, i,
						structureboundingbox);
			}

			for (int k = 0; k < 9; ++k) {
				for (int j = 0; j < 7; ++j) {
					this.clearCurrentPositionBlocksUpwards(world, j, 4, k, structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.dirt.getDefaultState(), j, -1, k,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	public static class Hall extends StructureVillagePieces.Village {
		public Hall() {
		}

		public Hall(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,
				StructureBoundingBox parStructureBoundingBox, EnumFacing facing) {
			super(start, parInt1);
			this.coordBaseMode = facing;
			this.boundingBox = parStructureBoundingBox;
		}

		public static StructureVillagePieces.Hall func_175857_a(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing, int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, 0, 0, 0, 9, 7, 11, parEnumFacing);
			return canVillageGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(rand, structureboundingbox) == null
							? new StructureVillagePieces.Hall(start, parInt4, facing, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.field_143015_k < 0) {
				this.field_143015_k = this.getAverageGroundLevel(world, structureboundingbox);
				if (this.field_143015_k < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
			}

			this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 7, 4, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 1, 6, 8, 4, 10, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 0, 6, 8, 0, 10, Blocks.dirt.getDefaultState(),
					Blocks.dirt.getDefaultState(), false);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, 0, 6, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 2, 1, 6, 2, 1, 10, Blocks.oak_fence.getDefaultState(),
					Blocks.oak_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 1, 6, 8, 1, 10, Blocks.oak_fence.getDefaultState(),
					Blocks.oak_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 1, 10, 7, 1, 10, Blocks.oak_fence.getDefaultState(),
					Blocks.oak_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 7, 0, 4, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 0, 3, 5, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 0, 0, 8, 3, 5, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 5, 7, 1, 5, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 7, 3, 0, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 2, 5, 7, 3, 5, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 4, 1, 8, 4, 1, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 4, 4, 8, 4, 4, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 2, 8, 5, 3, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 0, 4, 2, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 0, 4, 3, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 4, 2, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 4, 3, structureboundingbox);
			int i = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
			int j = this.getMetadataWithOffset(Blocks.oak_stairs, 2);

			for (int k = -1; k <= 2; ++k) {
				for (int l = 0; l <= 8; ++l) {
					this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(i), l, 4 + k, k, structureboundingbox);
					this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(j), l, 4 + k, 5 - k,
							structureboundingbox);
				}
			}

			this.setBlockState(world, Blocks.log.getDefaultState(), 0, 2, 1, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 0, 2, 4, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 1, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 4, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 3, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 3, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 5, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 3, 2, 5, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 6, 2, 5, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 2, 1, 3, structureboundingbox);
			this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 2, 2, 3, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 1, 1, 4, structureboundingbox);
			this.setBlockState(world,
					Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 3)), 2, 1, 4,
					structureboundingbox);
			this.setBlockState(world,
					Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), 1, 1, 3,
					structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 5, 0, 1, 7, 0, 3,
					Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
			this.setBlockState(world, Blocks.double_stone_slab.getDefaultState(), 6, 1, 1, structureboundingbox);
			this.setBlockState(world, Blocks.double_stone_slab.getDefaultState(), 6, 1, 2, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 2, 1, 0, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 2, 2, 0, structureboundingbox);
			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1,
					structureboundingbox);
			this.placeDoorCurrentPosition(world, structureboundingbox, random, 2, 1, 0,
					EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
			if (this.getBlockStateFromPos(world, 2, 0, -1, structureboundingbox).getBlock()
					.getMaterial() == Material.air
					&& this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getBlock()
							.getMaterial() != Material.air) {
				this.setBlockState(world,
						Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0,
						-1, structureboundingbox);
			}

			this.setBlockState(world, Blocks.air.getDefaultState(), 6, 1, 5, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 6, 2, 5, structureboundingbox);
			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 6,
					3, 4, structureboundingbox);
			this.placeDoorCurrentPosition(world, structureboundingbox, random, 6, 1, 5,
					EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));

			for (int i1 = 0; i1 < 5; ++i1) {
				for (int j1 = 0; j1 < 9; ++j1) {
					this.clearCurrentPositionBlocksUpwards(world, j1, 7, i1, structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), j1, -1, i1,
							structureboundingbox);
				}
			}

			this.spawnVillagers(world, structureboundingbox, 4, 1, 2, 2);
			return true;
		}

		protected int func_180779_c(int i, int j) {
			return i == 0 ? 4 : super.func_180779_c(i, j);
		}
	}

	public static class House1 extends StructureVillagePieces.Village {
		public House1() {
		}

		public House1(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,
				StructureBoundingBox parStructureBoundingBox, EnumFacing facing) {
			super(start, parInt1);
			this.coordBaseMode = facing;
			this.boundingBox = parStructureBoundingBox;
		}

		public static StructureVillagePieces.House1 func_175850_a(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing, int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, 0, 0, 0, 9, 9, 6, parEnumFacing);
			return canVillageGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(rand, structureboundingbox) == null
							? new StructureVillagePieces.House1(start, parInt4, facing, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.field_143015_k < 0) {
				this.field_143015_k = this.getAverageGroundLevel(world, structureboundingbox);
				if (this.field_143015_k < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 9 - 1, 0);
			}

			this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 7, 5, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 8, 0, 5, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 8, 5, 5, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 6, 1, 8, 6, 4, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 7, 2, 8, 7, 3, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			int i = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
			int j = this.getMetadataWithOffset(Blocks.oak_stairs, 2);

			for (int k = -1; k <= 2; ++k) {
				for (int l = 0; l <= 8; ++l) {
					this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(i), l, 6 + k, k, structureboundingbox);
					this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(j), l, 6 + k, 5 - k,
							structureboundingbox);
				}
			}

			this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 1, 5, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 5, 8, 1, 5, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 1, 0, 8, 1, 4, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 1, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 4, 0, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 5, 0, 4, 5, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 2, 5, 8, 4, 5, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 2, 0, 8, 4, 0, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 1, 0, 4, 4, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 2, 5, 7, 4, 5, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 2, 1, 8, 4, 4, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 7, 4, 0, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 6, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 3, 0, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 3, 0, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 6, 3, 0, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 3, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 3, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 3, 3, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 3, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 3, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 3, 3, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 5, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 3, 2, 5, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 2, 5, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 6, 2, 5, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 1, 4, 1, 7, 4, 1, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 4, 4, 7, 4, 4, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 3, 4, 7, 3, 4, Blocks.bookshelf.getDefaultState(),
					Blocks.bookshelf.getDefaultState(), false);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 7, 1, 4, structureboundingbox);
			this.setBlockState(world,
					Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), 7, 1, 3,
					structureboundingbox);
			int j1 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
			this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(j1), 6, 1, 4, structureboundingbox);
			this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(j1), 5, 1, 4, structureboundingbox);
			this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(j1), 4, 1, 4, structureboundingbox);
			this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(j1), 3, 1, 4, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 6, 1, 3, structureboundingbox);
			this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 6, 2, 3, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 1, 3, structureboundingbox);
			this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 4, 2, 3, structureboundingbox);
			this.setBlockState(world, Blocks.crafting_table.getDefaultState(), 7, 1, 1, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 1, 1, 0, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 1, 2, 0, structureboundingbox);
			this.placeDoorCurrentPosition(world, structureboundingbox, random, 1, 1, 0,
					EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
			if (this.getBlockStateFromPos(world, 1, 0, -1, structureboundingbox).getBlock()
					.getMaterial() == Material.air
					&& this.getBlockStateFromPos(world, 1, -1, -1, structureboundingbox).getBlock()
							.getMaterial() != Material.air) {
				this.setBlockState(world,
						Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0,
						-1, structureboundingbox);
			}

			for (int k1 = 0; k1 < 6; ++k1) {
				for (int i1 = 0; i1 < 9; ++i1) {
					this.clearCurrentPositionBlocksUpwards(world, i1, 9, k1, structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), i1, -1, k1,
							structureboundingbox);
				}
			}

			this.spawnVillagers(world, structureboundingbox, 2, 1, 2, 1);
			return true;
		}

		protected int func_180779_c(int parInt1, int parInt2) {
			return 1;
		}
	}

	public static class House2 extends StructureVillagePieces.Village {
		private static final List<WeightedRandomChestContent> villageBlacksmithChestContents = Lists.newArrayList(
				new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3),
						new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
						new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5),
						new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15),
						new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15),
						new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5),
						new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.obsidian), 0, 3, 7, 5),
						new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.sapling), 0, 3, 7, 5),
						new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
						new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1),
						new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1),
						new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });
		private boolean hasMadeChest;

		public House2() {
		}

		public House2(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,
				StructureBoundingBox parStructureBoundingBox, EnumFacing facing) {
			super(start, parInt1);
			this.coordBaseMode = facing;
			this.boundingBox = parStructureBoundingBox;
		}

		public static StructureVillagePieces.House2 func_175855_a(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing, int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, 0, 0, 0, 10, 6, 7, parEnumFacing);
			return canVillageGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(rand, structureboundingbox) == null
							? new StructureVillagePieces.House2(start, parInt4, facing, structureboundingbox,
									parEnumFacing)
							: null;
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Chest", this.hasMadeChest);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.hasMadeChest = nbttagcompound.getBoolean("Chest");
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.field_143015_k < 0) {
				this.field_143015_k = this.getAverageGroundLevel(world, structureboundingbox);
				if (this.field_143015_k < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
			}

			this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 9, 4, 6, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 9, 0, 6, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 4, 0, 9, 4, 6, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 9, 5, 6, Blocks.stone_slab.getDefaultState(),
					Blocks.stone_slab.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 5, 1, 8, 5, 5, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 2, 3, 0, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 4, 0, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 1, 0, 3, 4, 0, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 1, 6, 0, 4, 6, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 3, 3, 1, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 3, 1, 2, 3, 3, 2, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 1, 3, 5, 3, 3, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 5, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 6, 5, 3, 6, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 1, 0, 5, 3, 0, Blocks.oak_fence.getDefaultState(),
					Blocks.oak_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 9, 1, 0, 9, 3, 0, Blocks.oak_fence.getDefaultState(),
					Blocks.oak_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 1, 4, 9, 4, 6, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.setBlockState(world, Blocks.flowing_lava.getDefaultState(), 7, 1, 5, structureboundingbox);
			this.setBlockState(world, Blocks.flowing_lava.getDefaultState(), 8, 1, 5, structureboundingbox);
			this.setBlockState(world, Blocks.iron_bars.getDefaultState(), 9, 2, 5, structureboundingbox);
			this.setBlockState(world, Blocks.iron_bars.getDefaultState(), 9, 2, 4, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 7, 2, 4, 8, 2, 5, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, 1, 3, structureboundingbox);
			this.setBlockState(world, Blocks.furnace.getDefaultState(), 6, 2, 3, structureboundingbox);
			this.setBlockState(world, Blocks.furnace.getDefaultState(), 6, 3, 3, structureboundingbox);
			this.setBlockState(world, Blocks.double_stone_slab.getDefaultState(), 8, 1, 1, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 4, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 6, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 2, 6, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 2, 1, 4, structureboundingbox);
			this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 2, 2, 4, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 1, 1, 5, structureboundingbox);
			this.setBlockState(world,
					Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 3)), 2, 1, 5,
					structureboundingbox);
			this.setBlockState(world,
					Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), 1, 1, 4,
					structureboundingbox);
			if (!this.hasMadeChest && structureboundingbox.isVecInside(
					new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(1), this.getZWithOffset(5, 5)))) {
				this.hasMadeChest = true;
				this.generateChestContents(world, structureboundingbox, random, 5, 1, 5, villageBlacksmithChestContents,
						3 + random.nextInt(6));
			}

			for (int i = 6; i <= 8; ++i) {
				if (this.getBlockStateFromPos(world, i, 0, -1, structureboundingbox).getBlock()
						.getMaterial() == Material.air
						&& this.getBlockStateFromPos(world, i, -1, -1, structureboundingbox).getBlock()
								.getMaterial() != Material.air) {
					this.setBlockState(world,
							Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), i,
							0, -1, structureboundingbox);
				}
			}

			for (int k = 0; k < 7; ++k) {
				for (int j = 0; j < 10; ++j) {
					this.clearCurrentPositionBlocksUpwards(world, j, 6, k, structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), j, -1, k,
							structureboundingbox);
				}
			}

			this.spawnVillagers(world, structureboundingbox, 7, 1, 1, 1);
			return true;
		}

		protected int func_180779_c(int var1, int var2) {
			return 3;
		}
	}

	public static class House3 extends StructureVillagePieces.Village {
		public House3() {
		}

		public House3(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,
				StructureBoundingBox parStructureBoundingBox, EnumFacing facing) {
			super(start, parInt1);
			this.coordBaseMode = facing;
			this.boundingBox = parStructureBoundingBox;
		}

		public static StructureVillagePieces.House3 func_175849_a(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing, int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, 0, 0, 0, 9, 7, 12, parEnumFacing);
			return canVillageGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(rand, structureboundingbox) == null
							? new StructureVillagePieces.House3(start, parInt4, facing, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.field_143015_k < 0) {
				this.field_143015_k = this.getAverageGroundLevel(world, structureboundingbox);
				if (this.field_143015_k < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
			}

			this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 7, 4, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 1, 6, 8, 4, 10, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 0, 5, 8, 0, 10, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 7, 0, 4, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 0, 3, 5, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 0, 0, 8, 3, 10, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 7, 2, 0, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 5, 2, 1, 5, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 0, 6, 2, 3, 10, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 0, 10, 7, 3, 10, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 7, 3, 0, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 2, 5, 2, 3, 5, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 4, 1, 8, 4, 1, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 4, 4, 3, 4, 4, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 2, 8, 5, 3, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 0, 4, 2, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 0, 4, 3, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 4, 2, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 4, 3, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 4, 4, structureboundingbox);
			int i = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
			int j = this.getMetadataWithOffset(Blocks.oak_stairs, 2);

			for (int k = -1; k <= 2; ++k) {
				for (int l = 0; l <= 8; ++l) {
					this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(i), l, 4 + k, k, structureboundingbox);
					if ((k > -1 || l <= 1) && (k > 0 || l <= 3) && (k > 1 || l <= 4 || l >= 6)) {
						this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(j), l, 4 + k, 5 - k,
								structureboundingbox);
					}
				}
			}

			this.fillWithBlocks(world, structureboundingbox, 3, 4, 5, 3, 4, 10, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 7, 4, 2, 7, 4, 10, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 5, 4, 4, 5, 10, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 5, 4, 6, 5, 10, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 6, 3, 5, 6, 10, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			int k1 = this.getMetadataWithOffset(Blocks.oak_stairs, 0);

			for (int l1 = 4; l1 >= 1; --l1) {
				this.setBlockState(world, Blocks.planks.getDefaultState(), l1, 2 + l1, 7 - l1, structureboundingbox);

				for (int i1 = 8 - l1; i1 <= 10; ++i1) {
					this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(k1), l1, 2 + l1, i1,
							structureboundingbox);
				}
			}

			int i2 = this.getMetadataWithOffset(Blocks.oak_stairs, 1);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 6, 6, 3, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 7, 5, 4, structureboundingbox);
			this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(i2), 6, 6, 4, structureboundingbox);

			for (int j2 = 6; j2 <= 8; ++j2) {
				for (int j1 = 5; j1 <= 10; ++j1) {
					this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(i2), j2, 12 - j2, j1,
							structureboundingbox);
				}
			}

			this.setBlockState(world, Blocks.log.getDefaultState(), 0, 2, 1, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 0, 2, 4, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 3, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 4, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 6, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 1, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 3, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 4, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 2, 5, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 6, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 7, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 8, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 8, 2, 9, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 2, 2, 6, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 7, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 8, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 2, 2, 9, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 4, 4, 10, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 4, 10, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 6, 4, 10, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 5, 5, 10, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 2, 1, 0, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 2, 2, 0, structureboundingbox);
			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1,
					structureboundingbox);
			this.placeDoorCurrentPosition(world, structureboundingbox, random, 2, 1, 0,
					EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
			this.fillWithBlocks(world, structureboundingbox, 1, 0, -1, 3, 2, -1, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			if (this.getBlockStateFromPos(world, 2, 0, -1, structureboundingbox).getBlock()
					.getMaterial() == Material.air
					&& this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getBlock()
							.getMaterial() != Material.air) {
				this.setBlockState(world,
						Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0,
						-1, structureboundingbox);
			}

			for (int k2 = 0; k2 < 5; ++k2) {
				for (int i3 = 0; i3 < 9; ++i3) {
					this.clearCurrentPositionBlocksUpwards(world, i3, 7, k2, structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), i3, -1, k2,
							structureboundingbox);
				}
			}

			for (int l2 = 5; l2 < 11; ++l2) {
				for (int j3 = 2; j3 < 9; ++j3) {
					this.clearCurrentPositionBlocksUpwards(world, j3, 7, l2, structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), j3, -1, l2,
							structureboundingbox);
				}
			}

			this.spawnVillagers(world, structureboundingbox, 4, 1, 2, 2);
			return true;
		}
	}

	public static class House4Garden extends StructureVillagePieces.Village {
		private boolean isRoofAccessible;

		public House4Garden() {
		}

		public House4Garden(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,
				StructureBoundingBox parStructureBoundingBox, EnumFacing facing) {
			super(start, parInt1);
			this.coordBaseMode = facing;
			this.boundingBox = parStructureBoundingBox;
			this.isRoofAccessible = rand.nextBoolean();
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Terrace", this.isRoofAccessible);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.isRoofAccessible = nbttagcompound.getBoolean("Terrace");
		}

		public static StructureVillagePieces.House4Garden func_175858_a(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing, int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, 0, 0, 0, 5, 6, 5, parEnumFacing);
			return StructureComponent.findIntersecting(rand, structureboundingbox) != null ? null
					: new StructureVillagePieces.House4Garden(start, parInt4, facing, structureboundingbox,
							parEnumFacing);
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			if (this.field_143015_k < 0) {
				this.field_143015_k = this.getAverageGroundLevel(world, structureboundingbox);
				if (this.field_143015_k < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
			}

			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 0, 4, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 4, 0, 4, 4, 4, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 4, 1, 3, 4, 3, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 1, 0, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 3, 0, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 1, 0, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 3, 0, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 1, 4, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 2, 4, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 0, 3, 4, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 1, 4, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 2, 4, structureboundingbox);
			this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 3, 4, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 3, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 3, 3, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 4, 3, 3, 4, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 4, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 1, 1, 0, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 1, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 1, 3, 0, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 2, 3, 0, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 3, 3, 0, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 3, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.planks.getDefaultState(), 3, 1, 0, structureboundingbox);
			if (this.getBlockStateFromPos(world, 2, 0, -1, structureboundingbox).getBlock()
					.getMaterial() == Material.air
					&& this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getBlock()
							.getMaterial() != Material.air) {
				this.setBlockState(world,
						Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0,
						-1, structureboundingbox);
			}

			this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 3, 3, 3, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			if (this.isRoofAccessible) {
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 0, 5, 0, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 5, 0, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 2, 5, 0, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 3, 5, 0, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 5, 0, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 0, 5, 4, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 5, 4, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 2, 5, 4, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 3, 5, 4, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 5, 4, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 5, 1, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 5, 2, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 5, 3, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 0, 5, 1, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 0, 5, 2, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 0, 5, 3, structureboundingbox);
			}

			if (this.isRoofAccessible) {
				int i = this.getMetadataWithOffset(Blocks.ladder, 3);
				this.setBlockState(world, Blocks.ladder.getStateFromMeta(i), 3, 1, 3, structureboundingbox);
				this.setBlockState(world, Blocks.ladder.getStateFromMeta(i), 3, 2, 3, structureboundingbox);
				this.setBlockState(world, Blocks.ladder.getStateFromMeta(i), 3, 3, 3, structureboundingbox);
				this.setBlockState(world, Blocks.ladder.getStateFromMeta(i), 3, 4, 3, structureboundingbox);
			}

			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1,
					structureboundingbox);

			for (int k = 0; k < 5; ++k) {
				for (int j = 0; j < 5; ++j) {
					this.clearCurrentPositionBlocksUpwards(world, j, 6, k, structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), j, -1, k,
							structureboundingbox);
				}
			}

			this.spawnVillagers(world, structureboundingbox, 1, 1, 2, 1);
			return true;
		}
	}

	public static class Path extends StructureVillagePieces.Road {
		private int length;

		public Path() {
		}

		public Path(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,
				StructureBoundingBox parStructureBoundingBox, EnumFacing facing) {
			super(start, parInt1);
			this.coordBaseMode = facing;
			this.boundingBox = parStructureBoundingBox;
			this.length = Math.max(parStructureBoundingBox.getXSize(), parStructureBoundingBox.getZSize());
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setInteger("Length", this.length);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.length = nbttagcompound.getInteger("Length");
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			boolean flag = false;

			for (int i = random.nextInt(5); i < this.length - 8; i += 2 + random.nextInt(5)) {
				StructureComponent structurecomponent1 = this
						.getNextComponentNN((StructureVillagePieces.Start) structurecomponent, list, random, 0, i);
				if (structurecomponent1 != null) {
					i += Math.max(structurecomponent1.boundingBox.getXSize(),
							structurecomponent1.boundingBox.getZSize());
					flag = true;
				}
			}

			for (int j = random.nextInt(5); j < this.length - 8; j += 2 + random.nextInt(5)) {
				StructureComponent structurecomponent2 = this
						.getNextComponentPP((StructureVillagePieces.Start) structurecomponent, list, random, 0, j);
				if (structurecomponent2 != null) {
					j += Math.max(structurecomponent2.boundingBox.getXSize(),
							structurecomponent2.boundingBox.getZSize());
					flag = true;
				}
			}

			if (flag && random.nextInt(3) > 0 && this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list,
							random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ,
							EnumFacing.WEST, this.getComponentType());
					break;
				case SOUTH:
					StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list,
							random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2,
							EnumFacing.WEST, this.getComponentType());
					break;
				case WEST:
					StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list,
							random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1,
							EnumFacing.NORTH, this.getComponentType());
					break;
				case EAST:
					StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list,
							random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1,
							EnumFacing.NORTH, this.getComponentType());
				}
			}

			if (flag && random.nextInt(3) > 0 && this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list,
							random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ,
							EnumFacing.EAST, this.getComponentType());
					break;
				case SOUTH:
					StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list,
							random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2,
							EnumFacing.EAST, this.getComponentType());
					break;
				case WEST:
					StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list,
							random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1,
							EnumFacing.SOUTH, this.getComponentType());
					break;
				case EAST:
					StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list,
							random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1,
							EnumFacing.SOUTH, this.getComponentType());
				}
			}

		}

		public static StructureBoundingBox func_175848_a(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing) {
			for (int i = 7 * MathHelper.getRandomIntegerInRange(facing, 3, 5); i >= 7; i -= 7) {
				StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
						parInt2, parInt3, 0, 0, 0, 3, 3, i, parEnumFacing);
				if (StructureComponent.findIntersecting(rand, structureboundingbox) == null) {
					return structureboundingbox;
				}
			}

			return null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			IBlockState iblockstate = this.func_175847_a(Blocks.gravel.getDefaultState());
			IBlockState iblockstate1 = this.func_175847_a(Blocks.cobblestone.getDefaultState());

			for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
				for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
					BlockPos blockpos = new BlockPos(i, 64, j);
					if (structureboundingbox.isVecInside(blockpos)) {
						blockpos = world.getTopSolidOrLiquidBlock(blockpos).down();
						world.setBlockState(blockpos, iblockstate, 2);
						world.setBlockState(blockpos.down(), iblockstate1, 2);
					}
				}
			}

			return true;
		}
	}

	public static class PieceWeight {
		public Class<? extends StructureVillagePieces.Village> villagePieceClass;
		public final int villagePieceWeight;
		public int villagePiecesSpawned;
		public int villagePiecesLimit;

		public PieceWeight(Class<? extends StructureVillagePieces.Village> parClass1, int parInt1, int parInt2) {
			this.villagePieceClass = parClass1;
			this.villagePieceWeight = parInt1;
			this.villagePiecesLimit = parInt2;
		}

		public boolean canSpawnMoreVillagePiecesOfType(int parInt1) {
			return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
		}

		public boolean canSpawnMoreVillagePieces() {
			return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
		}
	}

	public abstract static class Road extends StructureVillagePieces.Village {
		public Road() {
		}

		protected Road(StructureVillagePieces.Start start, int type) {
			super(start, type);
		}
	}

	public static class Start extends StructureVillagePieces.Well {
		public WorldChunkManager worldChunkMngr;
		public boolean inDesert;
		public int terrainType;
		public StructureVillagePieces.PieceWeight structVillagePieceWeight;
		public List<StructureVillagePieces.PieceWeight> structureVillageWeightedPieceList;
		public List<StructureComponent> field_74932_i = Lists.newArrayList();
		public List<StructureComponent> field_74930_j = Lists.newArrayList();

		public Start() {
		}

		public Start(WorldChunkManager chunkManagerIn, int parInt1, EaglercraftRandom rand, int parInt2, int parInt3,
				List<StructureVillagePieces.PieceWeight> parList, int parInt4) {
			super((StructureVillagePieces.Start) null, 0, rand, parInt2, parInt3);
			this.worldChunkMngr = chunkManagerIn;
			this.structureVillageWeightedPieceList = parList;
			this.terrainType = parInt4;
			BiomeGenBase biomegenbase = chunkManagerIn.getBiomeGenerator(new BlockPos(parInt2, 0, parInt3),
					BiomeGenBase.field_180279_ad);
			this.inDesert = biomegenbase == BiomeGenBase.desert || biomegenbase == BiomeGenBase.desertHills;
			this.func_175846_a(this.inDesert);
		}

		public WorldChunkManager getWorldChunkManager() {
			return this.worldChunkMngr;
		}
	}

	public static class Torch extends StructureVillagePieces.Village {
		public Torch() {
		}

		public Torch(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,
				StructureBoundingBox parStructureBoundingBox, EnumFacing facing) {
			super(start, parInt1);
			this.coordBaseMode = facing;
			this.boundingBox = parStructureBoundingBox;
		}

		public static StructureBoundingBox func_175856_a(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, 0, 0, 0, 3, 4, 2, parEnumFacing);
			return StructureComponent.findIntersecting(rand, structureboundingbox) != null ? null
					: structureboundingbox;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			if (this.field_143015_k < 0) {
				this.field_143015_k = this.getAverageGroundLevel(world, structureboundingbox);
				if (this.field_143015_k < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
			}

			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 2, 3, 1, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 0, 0, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 1, 0, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 2, 0, structureboundingbox);
			this.setBlockState(world, Blocks.wool.getStateFromMeta(EnumDyeColor.WHITE.getDyeDamage()), 1, 3, 0,
					structureboundingbox);
			boolean flag = this.coordBaseMode == EnumFacing.EAST || this.coordBaseMode == EnumFacing.NORTH;
			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateY()),
					flag ? 2 : 0, 3, 0, structureboundingbox);
			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 1, 3, 1,
					structureboundingbox);
			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateYCCW()),
					flag ? 0 : 2, 3, 0, structureboundingbox);
			this.setBlockState(world,
					Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 1,
					3, -1, structureboundingbox);
			return true;
		}
	}

	abstract static class Village extends StructureComponent {
		protected int field_143015_k = -1;
		private int villagersSpawned;
		private boolean isDesertVillage;

		public Village() {
		}

		protected Village(StructureVillagePieces.Start start, int type) {
			super(type);
			if (start != null) {
				this.isDesertVillage = start.inDesert;
			}

		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			nbttagcompound.setInteger("HPos", this.field_143015_k);
			nbttagcompound.setInteger("VCount", this.villagersSpawned);
			nbttagcompound.setBoolean("Desert", this.isDesertVillage);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			this.field_143015_k = nbttagcompound.getInteger("HPos");
			this.villagersSpawned = nbttagcompound.getInteger("VCount");
			this.isDesertVillage = nbttagcompound.getBoolean("Desert");
		}

		protected StructureComponent getNextComponentNN(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom parRandom, int parInt1, int parInt2) {
			if (this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					return StructureVillagePieces.func_176066_d(start, rand, parRandom, this.boundingBox.minX - 1,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2, EnumFacing.WEST,
							this.getComponentType());
				case SOUTH:
					return StructureVillagePieces.func_176066_d(start, rand, parRandom, this.boundingBox.minX - 1,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2, EnumFacing.WEST,
							this.getComponentType());
				case WEST:
					return StructureVillagePieces.func_176066_d(start, rand, parRandom, this.boundingBox.minX + parInt2,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ - 1, EnumFacing.NORTH,
							this.getComponentType());
				case EAST:
					return StructureVillagePieces.func_176066_d(start, rand, parRandom, this.boundingBox.minX + parInt2,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ - 1, EnumFacing.NORTH,
							this.getComponentType());
				}
			}

			return null;
		}

		protected StructureComponent getNextComponentPP(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom parRandom, int parInt1, int parInt2) {
			if (this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					return StructureVillagePieces.func_176066_d(start, rand, parRandom, this.boundingBox.maxX + 1,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2, EnumFacing.EAST,
							this.getComponentType());
				case SOUTH:
					return StructureVillagePieces.func_176066_d(start, rand, parRandom, this.boundingBox.maxX + 1,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2, EnumFacing.EAST,
							this.getComponentType());
				case WEST:
					return StructureVillagePieces.func_176066_d(start, rand, parRandom, this.boundingBox.minX + parInt2,
							this.boundingBox.minY + parInt1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH,
							this.getComponentType());
				case EAST:
					return StructureVillagePieces.func_176066_d(start, rand, parRandom, this.boundingBox.minX + parInt2,
							this.boundingBox.minY + parInt1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH,
							this.getComponentType());
				}
			}

			return null;
		}

		protected int getAverageGroundLevel(World worldIn, StructureBoundingBox parStructureBoundingBox) {
			int i = 0;
			int j = 0;
			BlockPos blockpos$mutableblockpos = new BlockPos();

			for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
				for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
					blockpos$mutableblockpos.func_181079_c(l, 64, k);
					if (parStructureBoundingBox.isVecInside(blockpos$mutableblockpos)) {
						i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(),
								worldIn.provider.getAverageGroundLevel());
						++j;
					}
				}
			}

			if (j == 0) {
				return -1;
			} else {
				return i / j;
			}
		}

		protected static boolean canVillageGoDeeper(StructureBoundingBox parStructureBoundingBox) {
			return parStructureBoundingBox != null && parStructureBoundingBox.minY > 10;
		}

		protected void spawnVillagers(World worldIn, StructureBoundingBox parStructureBoundingBox, int parInt1,
				int parInt2, int parInt3, int parInt4) {
			if (this.villagersSpawned < parInt4) {
				for (int i = this.villagersSpawned; i < parInt4; ++i) {
					int j = this.getXWithOffset(parInt1 + i, parInt3);
					int k = this.getYWithOffset(parInt2);
					int l = this.getZWithOffset(parInt1 + i, parInt3);
					if (!parStructureBoundingBox.isVecInside(new BlockPos(j, k, l))) {
						break;
					}

					++this.villagersSpawned;
					EntityVillager entityvillager = new EntityVillager(worldIn);
					entityvillager.setLocationAndAngles((double) j + 0.5D, (double) k, (double) l + 0.5D, 0.0F, 0.0F);
					entityvillager.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityvillager)),
							(IEntityLivingData) null);
					entityvillager.setProfession(this.func_180779_c(i, entityvillager.getProfession()));
					worldIn.spawnEntityInWorld(entityvillager);
				}

			}
		}

		protected int func_180779_c(int var1, int i) {
			return i;
		}

		protected IBlockState func_175847_a(IBlockState parIBlockState) {
			if (this.isDesertVillage) {
				if (parIBlockState.getBlock() == Blocks.log || parIBlockState.getBlock() == Blocks.log2) {
					return Blocks.sandstone.getDefaultState();
				}

				if (parIBlockState.getBlock() == Blocks.cobblestone) {
					return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
				}

				if (parIBlockState.getBlock() == Blocks.planks) {
					return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
				}

				if (parIBlockState.getBlock() == Blocks.oak_stairs) {
					return Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING,
							parIBlockState.getValue(BlockStairs.FACING));
				}

				if (parIBlockState.getBlock() == Blocks.stone_stairs) {
					return Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING,
							parIBlockState.getValue(BlockStairs.FACING));
				}

				if (parIBlockState.getBlock() == Blocks.gravel) {
					return Blocks.sandstone.getDefaultState();
				}
			}

			return parIBlockState;
		}

		protected void setBlockState(World world, IBlockState iblockstate, int i, int j, int k,
				StructureBoundingBox structureboundingbox) {
			IBlockState iblockstate1 = this.func_175847_a(iblockstate);
			super.setBlockState(world, iblockstate1, i, j, k, structureboundingbox);
		}

		protected void fillWithBlocks(World world, StructureBoundingBox structureboundingbox, int i, int j, int k,
				int l, int i1, int j1, IBlockState iblockstate, IBlockState iblockstate1, boolean flag) {
			IBlockState iblockstate2 = this.func_175847_a(iblockstate);
			IBlockState iblockstate3 = this.func_175847_a(iblockstate1);
			super.fillWithBlocks(world, structureboundingbox, i, j, k, l, i1, j1, iblockstate2, iblockstate3, flag);
		}

		protected void replaceAirAndLiquidDownwards(World world, IBlockState iblockstate, int i, int j, int k,
				StructureBoundingBox structureboundingbox) {
			IBlockState iblockstate1 = this.func_175847_a(iblockstate);
			super.replaceAirAndLiquidDownwards(world, iblockstate1, i, j, k, structureboundingbox);
		}

		protected void func_175846_a(boolean parFlag) {
			this.isDesertVillage = parFlag;
		}
	}

	public static class Well extends StructureVillagePieces.Village {
		public Well() {
		}

		public Well(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand, int parInt2, int parInt3) {
			super(start, parInt1);
			this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(rand);
			switch (this.coordBaseMode) {
			case NORTH:
			case SOUTH:
				this.boundingBox = new StructureBoundingBox(parInt2, 64, parInt3, parInt2 + 6 - 1, 78, parInt3 + 6 - 1);
				break;
			default:
				this.boundingBox = new StructureBoundingBox(parInt2, 64, parInt3, parInt2 + 6 - 1, 78, parInt3 + 6 - 1);
			}

		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list, random,
					this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST,
					this.getComponentType());
			StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list, random,
					this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST,
					this.getComponentType());
			StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list, random,
					this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH,
					this.getComponentType());
			StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurecomponent, list, random,
					this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH,
					this.getComponentType());
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			if (this.field_143015_k < 0) {
				this.field_143015_k = this.getAverageGroundLevel(world, structureboundingbox);
				if (this.field_143015_k < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 3, 0);
			}

			this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 4, 12, 4, Blocks.cobblestone.getDefaultState(),
					Blocks.flowing_water.getDefaultState(), false);
			this.setBlockState(world, Blocks.air.getDefaultState(), 2, 12, 2, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 3, 12, 2, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 2, 12, 3, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 3, 12, 3, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 13, 1, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 14, 1, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 13, 1, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 14, 1, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 13, 4, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 14, 4, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 13, 4, structureboundingbox);
			this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 14, 4, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 1, 15, 1, 4, 15, 4, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);

			for (int i = 0; i <= 5; ++i) {
				for (int j = 0; j <= 5; ++j) {
					if (j == 0 || j == 5 || i == 0 || i == 5) {
						this.setBlockState(world, Blocks.gravel.getDefaultState(), j, 11, i, structureboundingbox);
						this.clearCurrentPositionBlocksUpwards(world, j, 12, i, structureboundingbox);
					}
				}
			}

			return true;
		}
	}

	public static class WoodHut extends StructureVillagePieces.Village {
		private boolean isTallHouse;
		private int tablePosition;

		public WoodHut() {
		}

		public WoodHut(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,
				StructureBoundingBox parStructureBoundingBox, EnumFacing facing) {
			super(start, parInt1);
			this.coordBaseMode = facing;
			this.boundingBox = parStructureBoundingBox;
			this.isTallHouse = rand.nextBoolean();
			this.tablePosition = rand.nextInt(3);
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setInteger("T", this.tablePosition);
			nbttagcompound.setBoolean("C", this.isTallHouse);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.tablePosition = nbttagcompound.getInteger("T");
			this.isTallHouse = nbttagcompound.getBoolean("C");
		}

		public static StructureVillagePieces.WoodHut func_175853_a(StructureVillagePieces.Start start,
				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing, int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, 0, 0, 0, 4, 6, 5, parEnumFacing);
			return canVillageGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(rand, structureboundingbox) == null
							? new StructureVillagePieces.WoodHut(start, parInt4, facing, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.field_143015_k < 0) {
				this.field_143015_k = this.getAverageGroundLevel(world, structureboundingbox);
				if (this.field_143015_k < 0) {
					return true;
				}

				this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
			}

			this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 3, 5, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 3, 0, 4, Blocks.cobblestone.getDefaultState(),
					Blocks.cobblestone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 2, 0, 3, Blocks.dirt.getDefaultState(),
					Blocks.dirt.getDefaultState(), false);
			if (this.isTallHouse) {
				this.fillWithBlocks(world, structureboundingbox, 1, 4, 1, 2, 4, 3, Blocks.log.getDefaultState(),
						Blocks.log.getDefaultState(), false);
			} else {
				this.fillWithBlocks(world, structureboundingbox, 1, 5, 1, 2, 5, 3, Blocks.log.getDefaultState(),
						Blocks.log.getDefaultState(), false);
			}

			this.setBlockState(world, Blocks.log.getDefaultState(), 1, 4, 0, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 2, 4, 0, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 1, 4, 4, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 2, 4, 4, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 0, 4, 1, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 0, 4, 2, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 0, 4, 3, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 3, 4, 1, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 3, 4, 2, structureboundingbox);
			this.setBlockState(world, Blocks.log.getDefaultState(), 3, 4, 3, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 3, 0, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 1, 0, 3, 3, 0, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 1, 4, 0, 3, 4, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 1, 4, 3, 3, 4, Blocks.log.getDefaultState(),
					Blocks.log.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 3, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 1, 1, 3, 3, 3, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 2, 3, 0, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 4, 2, 3, 4, Blocks.planks.getDefaultState(),
					Blocks.planks.getDefaultState(), false);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 3, 2, 2, structureboundingbox);
			if (this.tablePosition > 0) {
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), this.tablePosition, 1, 3,
						structureboundingbox);
				this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), this.tablePosition, 2, 3,
						structureboundingbox);
			}

			this.setBlockState(world, Blocks.air.getDefaultState(), 1, 1, 0, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 1, 2, 0, structureboundingbox);
			this.placeDoorCurrentPosition(world, structureboundingbox, random, 1, 1, 0,
					EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
			if (this.getBlockStateFromPos(world, 1, 0, -1, structureboundingbox).getBlock()
					.getMaterial() == Material.air
					&& this.getBlockStateFromPos(world, 1, -1, -1, structureboundingbox).getBlock()
							.getMaterial() != Material.air) {
				this.setBlockState(world,
						Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0,
						-1, structureboundingbox);
			}

			for (int i = 0; i < 5; ++i) {
				for (int j = 0; j < 4; ++j) {
					this.clearCurrentPositionBlocksUpwards(world, j, 6, i, structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), j, -1, i,
							structureboundingbox);
				}
			}

			this.spawnVillagers(world, structureboundingbox, 1, 1, 2, 1);
			return true;
		}
	}
}