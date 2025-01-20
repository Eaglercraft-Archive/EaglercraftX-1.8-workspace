package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
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
public class StructureStrongholdPieces {
	private static final StructureStrongholdPieces.PieceWeight[] pieceWeightArray = new StructureStrongholdPieces.PieceWeight[] {
			new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Straight.class, 40, 0),
			new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Prison.class, 5, 5),
			new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.LeftTurn.class, 20, 0),
			new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RightTurn.class, 20, 0),
			new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RoomCrossing.class, 10, 6),
			new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.StairsStraight.class, 5, 5),
			new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Stairs.class, 5, 5),
			new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Crossing.class, 5, 4),
			new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.ChestCorridor.class, 5, 4),
			new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Library.class, 10, 2) {
				public boolean canSpawnMoreStructuresOfType(int parInt1) {
					return super.canSpawnMoreStructuresOfType(parInt1) && parInt1 > 4;
				}
			}, new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.PortalRoom.class, 20, 1) {
				public boolean canSpawnMoreStructuresOfType(int i) {
					return super.canSpawnMoreStructuresOfType(i) && i > 5;
				}
			} };
	private static List<StructureStrongholdPieces.PieceWeight> structurePieceList;
	private static Class<? extends StructureStrongholdPieces.Stronghold> strongComponentType;
	static int totalWeight;
	private static final StructureStrongholdPieces.Stones strongholdStones = new StructureStrongholdPieces.Stones();

	public static void registerStrongholdPieces() {
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.ChestCorridor.class,
				StructureStrongholdPieces.ChestCorridor::new, "SHCC");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Corridor.class,
				StructureStrongholdPieces.Corridor::new, "SHFC");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Crossing.class,
				StructureStrongholdPieces.Crossing::new, "SH5C");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.LeftTurn.class,
				StructureStrongholdPieces.LeftTurn::new, "SHLT");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Library.class,
				StructureStrongholdPieces.Library::new, "SHLi");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.PortalRoom.class,
				StructureStrongholdPieces.PortalRoom::new, "SHPR");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Prison.class,
				StructureStrongholdPieces.Prison::new, "SHPH");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RightTurn.class,
				StructureStrongholdPieces.RightTurn::new, "SHRT");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RoomCrossing.class,
				StructureStrongholdPieces.RoomCrossing::new, "SHRC");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs.class,
				StructureStrongholdPieces.Stairs::new, "SHSD");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs2.class,
				StructureStrongholdPieces.Stairs2::new, "SHStart");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Straight.class,
				StructureStrongholdPieces.Straight::new, "SHS");
		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.StairsStraight.class,
				StructureStrongholdPieces.StairsStraight::new, "SHSSD");
	}

	/**+
	 * sets up Arrays with the Structure pieces and their weights
	 */
	public static void prepareStructurePieces() {
		structurePieceList = Lists.newArrayList();

		for (StructureStrongholdPieces.PieceWeight structurestrongholdpieces$pieceweight : pieceWeightArray) {
			structurestrongholdpieces$pieceweight.instancesSpawned = 0;
			structurePieceList.add(structurestrongholdpieces$pieceweight);
		}

		strongComponentType = null;
	}

	private static boolean canAddStructurePieces() {
		boolean flag = false;
		totalWeight = 0;

		for (StructureStrongholdPieces.PieceWeight structurestrongholdpieces$pieceweight : structurePieceList) {
			if (structurestrongholdpieces$pieceweight.instancesLimit > 0
					&& structurestrongholdpieces$pieceweight.instancesSpawned < structurestrongholdpieces$pieceweight.instancesLimit) {
				flag = true;
			}

			totalWeight += structurestrongholdpieces$pieceweight.pieceWeight;
		}

		return flag;
	}

	private static StructureStrongholdPieces.Stronghold func_175954_a(
			Class<? extends StructureStrongholdPieces.Stronghold> parClass1, List<StructureComponent> parList,
			EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing, int parInt4) {
		Object object = null;
		if (parClass1 == StructureStrongholdPieces.Straight.class) {
			object = StructureStrongholdPieces.Straight.func_175862_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (parClass1 == StructureStrongholdPieces.Prison.class) {
			object = StructureStrongholdPieces.Prison.func_175860_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (parClass1 == StructureStrongholdPieces.LeftTurn.class) {
			object = StructureStrongholdPieces.LeftTurn.func_175867_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (parClass1 == StructureStrongholdPieces.RightTurn.class) {
			object = StructureStrongholdPieces.RightTurn.func_175867_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (parClass1 == StructureStrongholdPieces.RoomCrossing.class) {
			object = StructureStrongholdPieces.RoomCrossing.func_175859_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (parClass1 == StructureStrongholdPieces.StairsStraight.class) {
			object = StructureStrongholdPieces.StairsStraight.func_175861_a(parList, parRandom, parInt1, parInt2,
					parInt3, parEnumFacing, parInt4);
		} else if (parClass1 == StructureStrongholdPieces.Stairs.class) {
			object = StructureStrongholdPieces.Stairs.func_175863_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (parClass1 == StructureStrongholdPieces.Crossing.class) {
			object = StructureStrongholdPieces.Crossing.func_175866_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (parClass1 == StructureStrongholdPieces.ChestCorridor.class) {
			object = StructureStrongholdPieces.ChestCorridor.func_175868_a(parList, parRandom, parInt1, parInt2,
					parInt3, parEnumFacing, parInt4);
		} else if (parClass1 == StructureStrongholdPieces.Library.class) {
			object = StructureStrongholdPieces.Library.func_175864_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (parClass1 == StructureStrongholdPieces.PortalRoom.class) {
			object = StructureStrongholdPieces.PortalRoom.func_175865_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		}

		return (StructureStrongholdPieces.Stronghold) object;
	}

	private static StructureStrongholdPieces.Stronghold func_175955_b(StructureStrongholdPieces.Stairs2 parStairs2_1,
			List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3,
			EnumFacing parEnumFacing, int parInt4) {
		if (!canAddStructurePieces()) {
			return null;
		} else {
			if (strongComponentType != null) {
				StructureStrongholdPieces.Stronghold structurestrongholdpieces$stronghold = func_175954_a(
						strongComponentType, parList, parRandom, parInt1, parInt2, parInt3, parEnumFacing, parInt4);
				strongComponentType = null;
				if (structurestrongholdpieces$stronghold != null) {
					return structurestrongholdpieces$stronghold;
				}
			}

			int j = 0;

			while (j < 5) {
				++j;
				int i = parRandom.nextInt(totalWeight);

				for (StructureStrongholdPieces.PieceWeight structurestrongholdpieces$pieceweight : structurePieceList) {
					i -= structurestrongholdpieces$pieceweight.pieceWeight;
					if (i < 0) {
						if (!structurestrongholdpieces$pieceweight.canSpawnMoreStructuresOfType(parInt4)
								|| structurestrongholdpieces$pieceweight == parStairs2_1.strongholdPieceWeight) {
							break;
						}

						StructureStrongholdPieces.Stronghold structurestrongholdpieces$stronghold1 = func_175954_a(
								structurestrongholdpieces$pieceweight.pieceClass, parList, parRandom, parInt1, parInt2,
								parInt3, parEnumFacing, parInt4);
						if (structurestrongholdpieces$stronghold1 != null) {
							++structurestrongholdpieces$pieceweight.instancesSpawned;
							parStairs2_1.strongholdPieceWeight = structurestrongholdpieces$pieceweight;
							if (!structurestrongholdpieces$pieceweight.canSpawnMoreStructures()) {
								structurePieceList.remove(structurestrongholdpieces$pieceweight);
							}

							return structurestrongholdpieces$stronghold1;
						}
					}
				}
			}

			StructureBoundingBox structureboundingbox = StructureStrongholdPieces.Corridor.func_175869_a(parList,
					parRandom, parInt1, parInt2, parInt3, parEnumFacing);
			if (structureboundingbox != null && structureboundingbox.minY > 1) {
				return new StructureStrongholdPieces.Corridor(parInt4, parRandom, structureboundingbox, parEnumFacing);
			} else {
				return null;
			}
		}
	}

	private static StructureComponent func_175953_c(StructureStrongholdPieces.Stairs2 parStairs2_1,
			List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3,
			EnumFacing parEnumFacing, int parInt4) {
		if (parInt4 > 50) {
			return null;
		} else if (Math.abs(parInt1 - parStairs2_1.getBoundingBox().minX) <= 112
				&& Math.abs(parInt3 - parStairs2_1.getBoundingBox().minZ) <= 112) {
			StructureStrongholdPieces.Stronghold structurestrongholdpieces$stronghold = func_175955_b(parStairs2_1,
					parList, parRandom, parInt1, parInt2, parInt3, parEnumFacing, parInt4 + 1);
			if (structurestrongholdpieces$stronghold != null) {
				parList.add(structurestrongholdpieces$stronghold);
				parStairs2_1.field_75026_c.add(structurestrongholdpieces$stronghold);
			}

			return structurestrongholdpieces$stronghold;
		} else {
			return null;
		}
	}

	public static class ChestCorridor extends StructureStrongholdPieces.Stronghold {
		private static final List<WeightedRandomChestContent> strongholdChestContents = Lists.newArrayList(
				new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.ender_pearl, 0, 1, 1, 10),
						new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3),
						new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
						new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5),
						new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5),
						new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15),
						new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15),
						new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1),
						new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 1),
						new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1),
						new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1),
						new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });
		private boolean hasMadeChest;

		public ChestCorridor() {
		}

		public ChestCorridor(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.field_143013_d = this.getRandomDoor(parRandom);
			this.boundingBox = parStructureBoundingBox;
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Chest", this.hasMadeChest);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.hasMadeChest = nbttagcompound.getBoolean("Chest");
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 1);
		}

		public static StructureStrongholdPieces.ChestCorridor func_175868_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, -1, 0, 5, 5, 7, parEnumFacing);
			return canStrongholdGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureStrongholdPieces.ChestCorridor(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 4, 6, true, random,
						StructureStrongholdPieces.strongholdStones);
				this.placeDoor(world, random, structureboundingbox, this.field_143013_d, 1, 1, 0);
				this.placeDoor(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING,
						1, 1, 6);
				this.fillWithBlocks(world, structureboundingbox, 3, 1, 2, 3, 1, 4, Blocks.stonebrick.getDefaultState(),
						Blocks.stonebrick.getDefaultState(), false);
				this.setBlockState(world,
						Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 1,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 5,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 2,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 4,
						structureboundingbox);

				for (int i = 2; i <= 4; ++i) {
					this.setBlockState(world,
							Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 2, 1,
							i, structureboundingbox);
				}

				if (!this.hasMadeChest && structureboundingbox.isVecInside(
						new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3)))) {
					this.hasMadeChest = true;
					this.generateChestContents(world, structureboundingbox, random, 3, 2, 3,
							WeightedRandomChestContent.func_177629_a(strongholdChestContents,
									new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(random) }),
							2 + random.nextInt(2));
				}

				return true;
			}
		}
	}

	public static class Corridor extends StructureStrongholdPieces.Stronghold {
		private int field_74993_a;

		public Corridor() {
		}

		public Corridor(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
			this.field_74993_a = parEnumFacing != EnumFacing.NORTH && parEnumFacing != EnumFacing.SOUTH
					? parStructureBoundingBox.getXSize()
					: parStructureBoundingBox.getZSize();
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setInteger("Steps", this.field_74993_a);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.field_74993_a = nbttagcompound.getInteger("Steps");
		}

		public static StructureBoundingBox func_175869_a(List<StructureComponent> parList, EaglercraftRandom parRandom,
				int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing) {
			boolean flag = true;
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, -1, 0, 5, 5, 4, parEnumFacing);
			StructureComponent structurecomponent = StructureComponent.findIntersecting(parList, structureboundingbox);
			if (structurecomponent == null) {
				return null;
			} else {
				if (structurecomponent.getBoundingBox().minY == structureboundingbox.minY) {
					for (int i = 3; i >= 1; --i) {
						structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1, parInt2,
								parInt3, -1, -1, 0, 5, 5, i - 1, parEnumFacing);
						if (!structurecomponent.getBoundingBox().intersectsWith(structureboundingbox)) {
							return StructureBoundingBox.getComponentToAddBoundingBox(parInt1, parInt2, parInt3, -1, -1,
									0, 5, 5, i, parEnumFacing);
						}
					}
				}

				return null;
			}
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				for (int i = 0; i < this.field_74993_a; ++i) {
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 0, 0, i, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 0, i, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 0, i, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 0, i, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 4, 0, i, structureboundingbox);

					for (int j = 1; j <= 3; ++j) {
						this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 0, j, i, structureboundingbox);
						this.setBlockState(world, Blocks.air.getDefaultState(), 1, j, i, structureboundingbox);
						this.setBlockState(world, Blocks.air.getDefaultState(), 2, j, i, structureboundingbox);
						this.setBlockState(world, Blocks.air.getDefaultState(), 3, j, i, structureboundingbox);
						this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 4, j, i, structureboundingbox);
					}

					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 0, 4, i, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 4, i, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 4, i, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 4, i, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 4, 4, i, structureboundingbox);
				}

				return true;
			}
		}
	}

	public static class Crossing extends StructureStrongholdPieces.Stronghold {
		private boolean field_74996_b;
		private boolean field_74997_c;
		private boolean field_74995_d;
		private boolean field_74999_h;

		public Crossing() {
		}

		public Crossing(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.field_143013_d = this.getRandomDoor(parRandom);
			this.boundingBox = parStructureBoundingBox;
			this.field_74996_b = parRandom.nextBoolean();
			this.field_74997_c = parRandom.nextBoolean();
			this.field_74995_d = parRandom.nextBoolean();
			this.field_74999_h = parRandom.nextInt(3) > 0;
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("leftLow", this.field_74996_b);
			nbttagcompound.setBoolean("leftHigh", this.field_74997_c);
			nbttagcompound.setBoolean("rightLow", this.field_74995_d);
			nbttagcompound.setBoolean("rightHigh", this.field_74999_h);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.field_74996_b = nbttagcompound.getBoolean("leftLow");
			this.field_74997_c = nbttagcompound.getBoolean("leftHigh");
			this.field_74995_d = nbttagcompound.getBoolean("rightLow");
			this.field_74999_h = nbttagcompound.getBoolean("rightHigh");
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			int i = 3;
			int j = 5;
			if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.NORTH) {
				i = 8 - i;
				j = 8 - j;
			}

			this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 5, 1);
			if (this.field_74996_b) {
				this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, i, 1);
			}

			if (this.field_74997_c) {
				this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, j, 7);
			}

			if (this.field_74995_d) {
				this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, i, 1);
			}

			if (this.field_74999_h) {
				this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, j, 7);
			}

		}

		public static StructureStrongholdPieces.Crossing func_175866_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -4, -3, 0, 10, 9, 11, parEnumFacing);
			return canStrongholdGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureStrongholdPieces.Crossing(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 9, 8, 10, true, random,
						StructureStrongholdPieces.strongholdStones);
				this.placeDoor(world, random, structureboundingbox, this.field_143013_d, 4, 3, 0);
				if (this.field_74996_b) {
					this.fillWithBlocks(world, structureboundingbox, 0, 3, 1, 0, 5, 3, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				}

				if (this.field_74995_d) {
					this.fillWithBlocks(world, structureboundingbox, 9, 3, 1, 9, 5, 3, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				}

				if (this.field_74997_c) {
					this.fillWithBlocks(world, structureboundingbox, 0, 5, 7, 0, 7, 9, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				}

				if (this.field_74999_h) {
					this.fillWithBlocks(world, structureboundingbox, 9, 5, 7, 9, 7, 9, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				}

				this.fillWithBlocks(world, structureboundingbox, 5, 1, 10, 7, 3, 10, Blocks.air.getDefaultState(),
						Blocks.air.getDefaultState(), false);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 1, 2, 1, 8, 2, 6, false, random,
						StructureStrongholdPieces.strongholdStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 5, 4, 4, 9, false, random,
						StructureStrongholdPieces.strongholdStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 8, 1, 5, 8, 4, 9, false, random,
						StructureStrongholdPieces.strongholdStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 1, 4, 7, 3, 4, 9, false, random,
						StructureStrongholdPieces.strongholdStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 1, 3, 5, 3, 3, 6, false, random,
						StructureStrongholdPieces.strongholdStones);
				this.fillWithBlocks(world, structureboundingbox, 1, 3, 4, 3, 3, 4, Blocks.stone_slab.getDefaultState(),
						Blocks.stone_slab.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 1, 4, 6, 3, 4, 6, Blocks.stone_slab.getDefaultState(),
						Blocks.stone_slab.getDefaultState(), false);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 5, 1, 7, 7, 1, 8, false, random,
						StructureStrongholdPieces.strongholdStones);
				this.fillWithBlocks(world, structureboundingbox, 5, 1, 9, 7, 1, 9, Blocks.stone_slab.getDefaultState(),
						Blocks.stone_slab.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 5, 2, 7, 7, 2, 7, Blocks.stone_slab.getDefaultState(),
						Blocks.stone_slab.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 4, 5, 7, 4, 5, 9, Blocks.stone_slab.getDefaultState(),
						Blocks.stone_slab.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 8, 5, 7, 8, 5, 9, Blocks.stone_slab.getDefaultState(),
						Blocks.stone_slab.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 5, 5, 7, 7, 5, 9,
						Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
				this.setBlockState(world, Blocks.torch.getDefaultState(), 6, 5, 6, structureboundingbox);
				return true;
			}
		}
	}

	public static class LeftTurn extends StructureStrongholdPieces.Stronghold {
		public LeftTurn() {
		}

		public LeftTurn(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.field_143013_d = this.getRandomDoor(parRandom);
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
				this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 1);
			} else {
				this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 1);
			}

		}

		public static StructureStrongholdPieces.LeftTurn func_175867_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, -1, 0, 5, 5, 5, parEnumFacing);
			return canStrongholdGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureStrongholdPieces.LeftTurn(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 4, 4, true, random,
						StructureStrongholdPieces.strongholdStones);
				this.placeDoor(world, random, structureboundingbox, this.field_143013_d, 1, 1, 0);
				if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
					this.fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				} else {
					this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				}

				return true;
			}
		}
	}

	public static class Library extends StructureStrongholdPieces.Stronghold {
		private static final List<WeightedRandomChestContent> strongholdLibraryChestContents = Lists.newArrayList(
				new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.book, 0, 1, 3, 20),
						new WeightedRandomChestContent(Items.paper, 0, 2, 7, 20),
						new WeightedRandomChestContent(Items.map, 0, 1, 1, 1),
						new WeightedRandomChestContent(Items.compass, 0, 1, 1, 1) });
		private boolean isLargeRoom;

		public Library() {
		}

		public Library(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.field_143013_d = this.getRandomDoor(parRandom);
			this.boundingBox = parStructureBoundingBox;
			this.isLargeRoom = parStructureBoundingBox.getYSize() > 6;
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Tall", this.isLargeRoom);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.isLargeRoom = nbttagcompound.getBoolean("Tall");
		}

		public static StructureStrongholdPieces.Library func_175864_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -4, -1, 0, 14, 11, 15, parEnumFacing);
			if (!canStrongholdGoDeeper(structureboundingbox)
					|| StructureComponent.findIntersecting(parList, structureboundingbox) != null) {
				structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1, parInt2, parInt3, -4,
						-1, 0, 14, 6, 15, parEnumFacing);
				if (!canStrongholdGoDeeper(structureboundingbox)
						|| StructureComponent.findIntersecting(parList, structureboundingbox) != null) {
					return null;
				}
			}

			return new StructureStrongholdPieces.Library(parInt4, parRandom, structureboundingbox, parEnumFacing);
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				byte b0 = 11;
				if (!this.isLargeRoom) {
					b0 = 6;
				}

				this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 13, b0 - 1, 14, true, random,
						StructureStrongholdPieces.strongholdStones);
				this.placeDoor(world, random, structureboundingbox, this.field_143013_d, 4, 1, 0);
				this.func_175805_a(world, structureboundingbox, random, 0.07F, 2, 1, 1, 11, 4, 13,
						Blocks.web.getDefaultState(), Blocks.web.getDefaultState(), false);
				boolean flag = true;
				boolean flag1 = true;

				for (int i = 1; i <= 13; ++i) {
					if ((i - 1) % 4 == 0) {
						this.fillWithBlocks(world, structureboundingbox, 1, 1, i, 1, 4, i,
								Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
						this.fillWithBlocks(world, structureboundingbox, 12, 1, i, 12, 4, i,
								Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
						this.setBlockState(world, Blocks.torch.getDefaultState(), 2, 3, i, structureboundingbox);
						this.setBlockState(world, Blocks.torch.getDefaultState(), 11, 3, i, structureboundingbox);
						if (this.isLargeRoom) {
							this.fillWithBlocks(world, structureboundingbox, 1, 6, i, 1, 9, i,
									Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
							this.fillWithBlocks(world, structureboundingbox, 12, 6, i, 12, 9, i,
									Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
						}
					} else {
						this.fillWithBlocks(world, structureboundingbox, 1, 1, i, 1, 4, i,
								Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
						this.fillWithBlocks(world, structureboundingbox, 12, 1, i, 12, 4, i,
								Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
						if (this.isLargeRoom) {
							this.fillWithBlocks(world, structureboundingbox, 1, 6, i, 1, 9, i,
									Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
							this.fillWithBlocks(world, structureboundingbox, 12, 6, i, 12, 9, i,
									Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
						}
					}
				}

				for (int j = 3; j < 12; j += 2) {
					this.fillWithBlocks(world, structureboundingbox, 3, 1, j, 4, 3, j,
							Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
					this.fillWithBlocks(world, structureboundingbox, 6, 1, j, 7, 3, j,
							Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
					this.fillWithBlocks(world, structureboundingbox, 9, 1, j, 10, 3, j,
							Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
				}

				if (this.isLargeRoom) {
					this.fillWithBlocks(world, structureboundingbox, 1, 5, 1, 3, 5, 13, Blocks.planks.getDefaultState(),
							Blocks.planks.getDefaultState(), false);
					this.fillWithBlocks(world, structureboundingbox, 10, 5, 1, 12, 5, 13,
							Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
					this.fillWithBlocks(world, structureboundingbox, 4, 5, 1, 9, 5, 2, Blocks.planks.getDefaultState(),
							Blocks.planks.getDefaultState(), false);
					this.fillWithBlocks(world, structureboundingbox, 4, 5, 12, 9, 5, 13,
							Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
					this.setBlockState(world, Blocks.planks.getDefaultState(), 9, 5, 11, structureboundingbox);
					this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 5, 11, structureboundingbox);
					this.setBlockState(world, Blocks.planks.getDefaultState(), 9, 5, 10, structureboundingbox);
					this.fillWithBlocks(world, structureboundingbox, 3, 6, 2, 3, 6, 12,
							Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
					this.fillWithBlocks(world, structureboundingbox, 10, 6, 2, 10, 6, 10,
							Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
					this.fillWithBlocks(world, structureboundingbox, 4, 6, 2, 9, 6, 2,
							Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
					this.fillWithBlocks(world, structureboundingbox, 4, 6, 12, 8, 6, 12,
							Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 9, 6, 11, structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 8, 6, 11, structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 9, 6, 10, structureboundingbox);
					int k = this.getMetadataWithOffset(Blocks.ladder, 3);
					this.setBlockState(world, Blocks.ladder.getStateFromMeta(k), 10, 1, 13, structureboundingbox);
					this.setBlockState(world, Blocks.ladder.getStateFromMeta(k), 10, 2, 13, structureboundingbox);
					this.setBlockState(world, Blocks.ladder.getStateFromMeta(k), 10, 3, 13, structureboundingbox);
					this.setBlockState(world, Blocks.ladder.getStateFromMeta(k), 10, 4, 13, structureboundingbox);
					this.setBlockState(world, Blocks.ladder.getStateFromMeta(k), 10, 5, 13, structureboundingbox);
					this.setBlockState(world, Blocks.ladder.getStateFromMeta(k), 10, 6, 13, structureboundingbox);
					this.setBlockState(world, Blocks.ladder.getStateFromMeta(k), 10, 7, 13, structureboundingbox);
					byte b1 = 7;
					byte b2 = 7;
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1 - 1, 9, b2, structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1, 9, b2, structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1 - 1, 8, b2, structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1, 8, b2, structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1 - 1, 7, b2, structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1, 7, b2, structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1 - 2, 7, b2, structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1 + 1, 7, b2, structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1 - 1, 7, b2 - 1,
							structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1 - 1, 7, b2 + 1,
							structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1, 7, b2 - 1, structureboundingbox);
					this.setBlockState(world, Blocks.oak_fence.getDefaultState(), b1, 7, b2 + 1, structureboundingbox);
					this.setBlockState(world, Blocks.torch.getDefaultState(), b1 - 2, 8, b2, structureboundingbox);
					this.setBlockState(world, Blocks.torch.getDefaultState(), b1 + 1, 8, b2, structureboundingbox);
					this.setBlockState(world, Blocks.torch.getDefaultState(), b1 - 1, 8, b2 - 1, structureboundingbox);
					this.setBlockState(world, Blocks.torch.getDefaultState(), b1 - 1, 8, b2 + 1, structureboundingbox);
					this.setBlockState(world, Blocks.torch.getDefaultState(), b1, 8, b2 - 1, structureboundingbox);
					this.setBlockState(world, Blocks.torch.getDefaultState(), b1, 8, b2 + 1, structureboundingbox);
				}

				this.generateChestContents(world, structureboundingbox, random, 3, 3, 5,
						WeightedRandomChestContent.func_177629_a(strongholdLibraryChestContents,
								new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(random, 1, 5, 2) }),
						1 + random.nextInt(4));
				if (this.isLargeRoom) {
					this.setBlockState(world, Blocks.air.getDefaultState(), 12, 9, 1, structureboundingbox);
					this.generateChestContents(world, structureboundingbox, random, 12, 8, 1,
							WeightedRandomChestContent
									.func_177629_a(strongholdLibraryChestContents,
											new WeightedRandomChestContent[] {
													Items.enchanted_book.getRandom(random, 1, 5, 2) }),
							1 + random.nextInt(4));
				}

				return true;
			}
		}
	}

	static class PieceWeight {
		public Class<? extends StructureStrongholdPieces.Stronghold> pieceClass;
		public final int pieceWeight;
		public int instancesSpawned;
		public int instancesLimit;

		public PieceWeight(Class<? extends StructureStrongholdPieces.Stronghold> parClass1, int parInt1, int parInt2) {
			this.pieceClass = parClass1;
			this.pieceWeight = parInt1;
			this.instancesLimit = parInt2;
		}

		public boolean canSpawnMoreStructuresOfType(int var1) {
			return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
		}

		public boolean canSpawnMoreStructures() {
			return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
		}
	}

	public static class PortalRoom extends StructureStrongholdPieces.Stronghold {
		private boolean hasSpawner;

		public PortalRoom() {
		}

		public PortalRoom(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Mob", this.hasSpawner);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.hasSpawner = nbttagcompound.getBoolean("Mob");
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> var2,
				EaglercraftRandom var3) {
			if (structurecomponent != null) {
				((StructureStrongholdPieces.Stairs2) structurecomponent).strongholdPortalRoom = this;
			}

		}

		public static StructureStrongholdPieces.PortalRoom func_175865_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -4, -1, 0, 11, 8, 16, parEnumFacing);
			return canStrongholdGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureStrongholdPieces.PortalRoom(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 10, 7, 15, false, random,
					StructureStrongholdPieces.strongholdStones);
			this.placeDoor(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.GRATES, 4, 1,
					0);
			int i = 6;
			this.fillWithRandomizedBlocks(world, structureboundingbox, 1, i, 1, 1, i, 14, false, random,
					StructureStrongholdPieces.strongholdStones);
			this.fillWithRandomizedBlocks(world, structureboundingbox, 9, i, 1, 9, i, 14, false, random,
					StructureStrongholdPieces.strongholdStones);
			this.fillWithRandomizedBlocks(world, structureboundingbox, 2, i, 1, 8, i, 2, false, random,
					StructureStrongholdPieces.strongholdStones);
			this.fillWithRandomizedBlocks(world, structureboundingbox, 2, i, 14, 8, i, 14, false, random,
					StructureStrongholdPieces.strongholdStones);
			this.fillWithRandomizedBlocks(world, structureboundingbox, 1, 1, 1, 2, 1, 4, false, random,
					StructureStrongholdPieces.strongholdStones);
			this.fillWithRandomizedBlocks(world, structureboundingbox, 8, 1, 1, 9, 1, 4, false, random,
					StructureStrongholdPieces.strongholdStones);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 1, 1, 3, Blocks.flowing_lava.getDefaultState(),
					Blocks.flowing_lava.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 9, 1, 1, 9, 1, 3, Blocks.flowing_lava.getDefaultState(),
					Blocks.flowing_lava.getDefaultState(), false);
			this.fillWithRandomizedBlocks(world, structureboundingbox, 3, 1, 8, 7, 1, 12, false, random,
					StructureStrongholdPieces.strongholdStones);
			this.fillWithBlocks(world, structureboundingbox, 4, 1, 9, 6, 1, 11, Blocks.flowing_lava.getDefaultState(),
					Blocks.flowing_lava.getDefaultState(), false);

			for (int j = 3; j < 14; j += 2) {
				this.fillWithBlocks(world, structureboundingbox, 0, 3, j, 0, 4, j, Blocks.iron_bars.getDefaultState(),
						Blocks.iron_bars.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 10, 3, j, 10, 4, j, Blocks.iron_bars.getDefaultState(),
						Blocks.iron_bars.getDefaultState(), false);
			}

			for (int k1 = 2; k1 < 9; k1 += 2) {
				this.fillWithBlocks(world, structureboundingbox, k1, 3, 15, k1, 4, 15,
						Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
			}

			int l1 = this.getMetadataWithOffset(Blocks.stone_brick_stairs, 3);
			this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 5, 6, 1, 7, false, random,
					StructureStrongholdPieces.strongholdStones);
			this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 2, 6, 6, 2, 7, false, random,
					StructureStrongholdPieces.strongholdStones);
			this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 3, 7, 6, 3, 7, false, random,
					StructureStrongholdPieces.strongholdStones);

			for (int k = 4; k <= 6; ++k) {
				this.setBlockState(world, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 1, 4,
						structureboundingbox);
				this.setBlockState(world, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 2, 5,
						structureboundingbox);
				this.setBlockState(world, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 3, 6,
						structureboundingbox);
			}

			int i2 = EnumFacing.NORTH.getHorizontalIndex();
			int l = EnumFacing.SOUTH.getHorizontalIndex();
			int i1 = EnumFacing.EAST.getHorizontalIndex();
			int j1 = EnumFacing.WEST.getHorizontalIndex();
			if (this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case SOUTH:
					i2 = EnumFacing.SOUTH.getHorizontalIndex();
					l = EnumFacing.NORTH.getHorizontalIndex();
					break;
				case WEST:
					i2 = EnumFacing.WEST.getHorizontalIndex();
					l = EnumFacing.EAST.getHorizontalIndex();
					i1 = EnumFacing.SOUTH.getHorizontalIndex();
					j1 = EnumFacing.NORTH.getHorizontalIndex();
					break;
				case EAST:
					i2 = EnumFacing.EAST.getHorizontalIndex();
					l = EnumFacing.WEST.getHorizontalIndex();
					i1 = EnumFacing.SOUTH.getHorizontalIndex();
					j1 = EnumFacing.NORTH.getHorizontalIndex();
				}
			}

			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 4, 3, 8, structureboundingbox);
			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 5, 3, 8, structureboundingbox);
			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 6, 3, 8, structureboundingbox);
			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(l).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 4, 3, 12, structureboundingbox);
			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(l).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 5, 3, 12, structureboundingbox);
			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(l).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 6, 3, 12, structureboundingbox);
			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 3, 3, 9, structureboundingbox);
			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 3, 3, 10, structureboundingbox);
			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 3, 3, 11, structureboundingbox);
			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 7, 3, 9, structureboundingbox);
			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 7, 3, 10, structureboundingbox);
			this.setBlockState(world, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty(BlockEndPortalFrame.EYE,
					Boolean.valueOf(random.nextFloat() > 0.9F)), 7, 3, 11, structureboundingbox);
			if (!this.hasSpawner) {
				i = this.getYWithOffset(3);
				BlockPos blockpos = new BlockPos(this.getXWithOffset(5, 6), i, this.getZWithOffset(5, 6));
				if (structureboundingbox.isVecInside(blockpos)) {
					this.hasSpawner = true;
					world.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
					TileEntity tileentity = world.getTileEntity(blockpos);
					if (tileentity instanceof TileEntityMobSpawner) {
						((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setEntityName("Silverfish");
					}
				}
			}

			return true;
		}
	}

	public static class Prison extends StructureStrongholdPieces.Stronghold {
		public Prison() {
		}

		public Prison(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.field_143013_d = this.getRandomDoor(parRandom);
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 1);
		}

		public static StructureStrongholdPieces.Prison func_175860_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, -1, 0, 9, 5, 11, parEnumFacing);
			return canStrongholdGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureStrongholdPieces.Prison(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 8, 4, 10, true, random,
						StructureStrongholdPieces.strongholdStones);
				this.placeDoor(world, random, structureboundingbox, this.field_143013_d, 1, 1, 0);
				this.fillWithBlocks(world, structureboundingbox, 1, 1, 10, 3, 3, 10, Blocks.air.getDefaultState(),
						Blocks.air.getDefaultState(), false);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 1, 4, 3, 1, false, random,
						StructureStrongholdPieces.strongholdStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 3, 4, 3, 3, false, random,
						StructureStrongholdPieces.strongholdStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 7, 4, 3, 7, false, random,
						StructureStrongholdPieces.strongholdStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 9, 4, 3, 9, false, random,
						StructureStrongholdPieces.strongholdStones);
				this.fillWithBlocks(world, structureboundingbox, 4, 1, 4, 4, 3, 6, Blocks.iron_bars.getDefaultState(),
						Blocks.iron_bars.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 5, 1, 5, 7, 3, 5, Blocks.iron_bars.getDefaultState(),
						Blocks.iron_bars.getDefaultState(), false);
				this.setBlockState(world, Blocks.iron_bars.getDefaultState(), 4, 3, 2, structureboundingbox);
				this.setBlockState(world, Blocks.iron_bars.getDefaultState(), 4, 3, 8, structureboundingbox);
				this.setBlockState(world,
						Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 2,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 2,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 8,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 8,
						structureboundingbox);
				return true;
			}
		}
	}

	public static class RightTurn extends StructureStrongholdPieces.LeftTurn {
		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
				this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 1);
			} else {
				this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 1);
			}

		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 4, 4, true, random,
						StructureStrongholdPieces.strongholdStones);
				this.placeDoor(world, random, structureboundingbox, this.field_143013_d, 1, 1, 0);
				if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
					this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				} else {
					this.fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				}

				return true;
			}
		}
	}

	public static class RoomCrossing extends StructureStrongholdPieces.Stronghold {
		private static final List<WeightedRandomChestContent> strongholdRoomCrossingChestContents = Lists.newArrayList(
				new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
						new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5),
						new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5),
						new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10),
						new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15),
						new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15),
						new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1) });
		protected int roomType;

		public RoomCrossing() {
		}

		public RoomCrossing(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.field_143013_d = this.getRandomDoor(parRandom);
			this.boundingBox = parStructureBoundingBox;
			this.roomType = parRandom.nextInt(5);
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setInteger("Type", this.roomType);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.roomType = nbttagcompound.getInteger("Type");
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 4, 1);
			this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 4);
			this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 4);
		}

		public static StructureStrongholdPieces.RoomCrossing func_175859_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -4, -1, 0, 11, 7, 11, parEnumFacing);
			return canStrongholdGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureStrongholdPieces.RoomCrossing(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 10, 6, 10, true, random,
						StructureStrongholdPieces.strongholdStones);
				this.placeDoor(world, random, structureboundingbox, this.field_143013_d, 4, 1, 0);
				this.fillWithBlocks(world, structureboundingbox, 4, 1, 10, 6, 3, 10, Blocks.air.getDefaultState(),
						Blocks.air.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 0, 1, 4, 0, 3, 6, Blocks.air.getDefaultState(),
						Blocks.air.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 10, 1, 4, 10, 3, 6, Blocks.air.getDefaultState(),
						Blocks.air.getDefaultState(), false);
				switch (this.roomType) {
				case 0:
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 1, 5, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 2, 5, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 3, 5, structureboundingbox);
					this.setBlockState(world, Blocks.torch.getDefaultState(), 4, 3, 5, structureboundingbox);
					this.setBlockState(world, Blocks.torch.getDefaultState(), 6, 3, 5, structureboundingbox);
					this.setBlockState(world, Blocks.torch.getDefaultState(), 5, 3, 4, structureboundingbox);
					this.setBlockState(world, Blocks.torch.getDefaultState(), 5, 3, 6, structureboundingbox);
					this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 4, 1, 4, structureboundingbox);
					this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 4, 1, 5, structureboundingbox);
					this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 4, 1, 6, structureboundingbox);
					this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 6, 1, 4, structureboundingbox);
					this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 6, 1, 5, structureboundingbox);
					this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 6, 1, 6, structureboundingbox);
					this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 5, 1, 4, structureboundingbox);
					this.setBlockState(world, Blocks.stone_slab.getDefaultState(), 5, 1, 6, structureboundingbox);
					break;
				case 1:
					for (int i1 = 0; i1 < 5; ++i1) {
						this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 1, 3 + i1,
								structureboundingbox);
						this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 7, 1, 3 + i1,
								structureboundingbox);
						this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3 + i1, 1, 3,
								structureboundingbox);
						this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3 + i1, 1, 7,
								structureboundingbox);
					}

					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 1, 5, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 2, 5, structureboundingbox);
					this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 5, 3, 5, structureboundingbox);
					this.setBlockState(world, Blocks.flowing_water.getDefaultState(), 5, 4, 5, structureboundingbox);
					break;
				case 2:
					for (int i = 1; i <= 9; ++i) {
						this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 1, 3, i, structureboundingbox);
						this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 9, 3, i, structureboundingbox);
					}

					for (int j = 1; j <= 9; ++j) {
						this.setBlockState(world, Blocks.cobblestone.getDefaultState(), j, 3, 1, structureboundingbox);
						this.setBlockState(world, Blocks.cobblestone.getDefaultState(), j, 3, 9, structureboundingbox);
					}

					this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 5, 1, 4, structureboundingbox);
					this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 5, 1, 6, structureboundingbox);
					this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 5, 3, 4, structureboundingbox);
					this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 5, 3, 6, structureboundingbox);
					this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 1, 5, structureboundingbox);
					this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, 1, 5, structureboundingbox);
					this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, 3, 5, structureboundingbox);
					this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, 3, 5, structureboundingbox);

					for (int k = 1; k <= 3; ++k) {
						this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, k, 4, structureboundingbox);
						this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, k, 4, structureboundingbox);
						this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 4, k, 6, structureboundingbox);
						this.setBlockState(world, Blocks.cobblestone.getDefaultState(), 6, k, 6, structureboundingbox);
					}

					this.setBlockState(world, Blocks.torch.getDefaultState(), 5, 3, 5, structureboundingbox);

					for (int l = 2; l <= 8; ++l) {
						this.setBlockState(world, Blocks.planks.getDefaultState(), 2, 3, l, structureboundingbox);
						this.setBlockState(world, Blocks.planks.getDefaultState(), 3, 3, l, structureboundingbox);
						if (l <= 3 || l >= 7) {
							this.setBlockState(world, Blocks.planks.getDefaultState(), 4, 3, l, structureboundingbox);
							this.setBlockState(world, Blocks.planks.getDefaultState(), 5, 3, l, structureboundingbox);
							this.setBlockState(world, Blocks.planks.getDefaultState(), 6, 3, l, structureboundingbox);
						}

						this.setBlockState(world, Blocks.planks.getDefaultState(), 7, 3, l, structureboundingbox);
						this.setBlockState(world, Blocks.planks.getDefaultState(), 8, 3, l, structureboundingbox);
					}

					this.setBlockState(world,
							Blocks.ladder.getStateFromMeta(
									this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())),
							9, 1, 3, structureboundingbox);
					this.setBlockState(world,
							Blocks.ladder.getStateFromMeta(
									this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())),
							9, 2, 3, structureboundingbox);
					this.setBlockState(world,
							Blocks.ladder.getStateFromMeta(
									this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())),
							9, 3, 3, structureboundingbox);
					this.generateChestContents(world, structureboundingbox, random, 3, 4, 8,
							WeightedRandomChestContent.func_177629_a(strongholdRoomCrossingChestContents,
									new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(random) }),
							1 + random.nextInt(4));
				}

				return true;
			}
		}
	}

	public static class Stairs extends StructureStrongholdPieces.Stronghold {
		private boolean field_75024_a;

		public Stairs() {
		}

		public Stairs(int parInt1, EaglercraftRandom parRandom, int parInt2, int parInt3) {
			super(parInt1);
			this.field_75024_a = true;
			this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(parRandom);
			this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
			switch (this.coordBaseMode) {
			case NORTH:
			case SOUTH:
				this.boundingBox = new StructureBoundingBox(parInt2, 64, parInt3, parInt2 + 5 - 1, 74, parInt3 + 5 - 1);
				break;
			default:
				this.boundingBox = new StructureBoundingBox(parInt2, 64, parInt3, parInt2 + 5 - 1, 74, parInt3 + 5 - 1);
			}

		}

		public Stairs(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.field_75024_a = false;
			this.coordBaseMode = parEnumFacing;
			this.field_143013_d = this.getRandomDoor(parRandom);
			this.boundingBox = parStructureBoundingBox;
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Source", this.field_75024_a);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.field_75024_a = nbttagcompound.getBoolean("Source");
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			if (this.field_75024_a) {
				StructureStrongholdPieces.strongComponentType = StructureStrongholdPieces.Crossing.class;
			}

			this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 1);
		}

		public static StructureStrongholdPieces.Stairs func_175863_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, -7, 0, 5, 11, 5, parEnumFacing);
			return canStrongholdGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureStrongholdPieces.Stairs(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 10, 4, true, random,
						StructureStrongholdPieces.strongholdStones);
				this.placeDoor(world, random, structureboundingbox, this.field_143013_d, 1, 7, 0);
				this.placeDoor(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING,
						1, 1, 4);
				this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 6, 1, structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 5, 1, structureboundingbox);
				this.setBlockState(world,
						Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 6, 1,
						structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 5, 2, structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 4, 3, structureboundingbox);
				this.setBlockState(world,
						Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 5, 3,
						structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 4, 3, structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 3, 3, structureboundingbox);
				this.setBlockState(world,
						Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 4, 3,
						structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 3, 2, structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 2, 1, structureboundingbox);
				this.setBlockState(world,
						Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 3, 1,
						structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 2, 1, structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 1, 1, structureboundingbox);
				this.setBlockState(world,
						Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 2, 1,
						structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 1, 2, structureboundingbox);
				this.setBlockState(world,
						Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 1, 3,
						structureboundingbox);
				return true;
			}
		}
	}

	public static class Stairs2 extends StructureStrongholdPieces.Stairs {
		public StructureStrongholdPieces.PieceWeight strongholdPieceWeight;
		public StructureStrongholdPieces.PortalRoom strongholdPortalRoom;
		public List<StructureComponent> field_75026_c = Lists.newArrayList();

		public Stairs2() {
		}

		public Stairs2(int parInt1, EaglercraftRandom parRandom, int parInt2, int parInt3) {
			super(0, parRandom, parInt2, parInt3);
		}

		public BlockPos getBoundingBoxCenter() {
			return this.strongholdPortalRoom != null ? this.strongholdPortalRoom.getBoundingBoxCenter()
					: super.getBoundingBoxCenter();
		}
	}

	public static class StairsStraight extends StructureStrongholdPieces.Stronghold {
		public StairsStraight() {
		}

		public StairsStraight(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.field_143013_d = this.getRandomDoor(parRandom);
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 1);
		}

		public static StructureStrongholdPieces.StairsStraight func_175861_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, -7, 0, 5, 11, 8, parEnumFacing);
			return canStrongholdGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureStrongholdPieces.StairsStraight(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 10, 7, true, random,
						StructureStrongholdPieces.strongholdStones);
				this.placeDoor(world, random, structureboundingbox, this.field_143013_d, 1, 7, 0);
				this.placeDoor(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING,
						1, 1, 7);
				int i = this.getMetadataWithOffset(Blocks.stone_stairs, 2);

				for (int j = 0; j < 6; ++j) {
					this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 1, 6 - j, 1 + j,
							structureboundingbox);
					this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 2, 6 - j, 1 + j,
							structureboundingbox);
					this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 3, 6 - j, 1 + j,
							structureboundingbox);
					if (j < 5) {
						this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 1, 5 - j, 1 + j,
								structureboundingbox);
						this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 2, 5 - j, 1 + j,
								structureboundingbox);
						this.setBlockState(world, Blocks.stonebrick.getDefaultState(), 3, 5 - j, 1 + j,
								structureboundingbox);
					}
				}

				return true;
			}
		}
	}

	static class Stones extends StructureComponent.BlockSelector {
		private Stones() {
		}

		public void selectBlocks(EaglercraftRandom random, int var2, int var3, int var4, boolean flag) {
			if (flag) {
				float f = random.nextFloat();
				if (f < 0.2F) {
					this.blockstate = Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CRACKED_META);
				} else if (f < 0.5F) {
					this.blockstate = Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.MOSSY_META);
				} else if (f < 0.55F) {
					this.blockstate = Blocks.monster_egg
							.getStateFromMeta(BlockSilverfish.EnumType.STONEBRICK.getMetadata());
				} else {
					this.blockstate = Blocks.stonebrick.getDefaultState();
				}
			} else {
				this.blockstate = Blocks.air.getDefaultState();
			}

		}
	}

	public static class Straight extends StructureStrongholdPieces.Stronghold {
		private boolean expandsX;
		private boolean expandsZ;

		public Straight() {
		}

		public Straight(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.field_143013_d = this.getRandomDoor(parRandom);
			this.boundingBox = parStructureBoundingBox;
			this.expandsX = parRandom.nextInt(2) == 0;
			this.expandsZ = parRandom.nextInt(2) == 0;
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Left", this.expandsX);
			nbttagcompound.setBoolean("Right", this.expandsZ);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.expandsX = nbttagcompound.getBoolean("Left");
			this.expandsZ = nbttagcompound.getBoolean("Right");
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 1);
			if (this.expandsX) {
				this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 2);
			}

			if (this.expandsZ) {
				this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurecomponent, list, random, 1, 2);
			}

		}

		public static StructureStrongholdPieces.Straight func_175862_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, -1, 0, 5, 5, 7, parEnumFacing);
			return canStrongholdGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureStrongholdPieces.Straight(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 4, 6, true, random,
						StructureStrongholdPieces.strongholdStones);
				this.placeDoor(world, random, structureboundingbox, this.field_143013_d, 1, 1, 0);
				this.placeDoor(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING,
						1, 1, 6);
				this.randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 1, 2, 1,
						Blocks.torch.getDefaultState());
				this.randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 3, 2, 1,
						Blocks.torch.getDefaultState());
				this.randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 1, 2, 5,
						Blocks.torch.getDefaultState());
				this.randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 3, 2, 5,
						Blocks.torch.getDefaultState());
				if (this.expandsX) {
					this.fillWithBlocks(world, structureboundingbox, 0, 1, 2, 0, 3, 4, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				}

				if (this.expandsZ) {
					this.fillWithBlocks(world, structureboundingbox, 4, 1, 2, 4, 3, 4, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				}

				return true;
			}
		}
	}

	abstract static class Stronghold extends StructureComponent {
		protected StructureStrongholdPieces.Stronghold.Door field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;

		public Stronghold() {
		}

		protected Stronghold(int parInt1) {
			super(parInt1);
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			nbttagcompound.setString("EntryDoor", this.field_143013_d.name());
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			this.field_143013_d = StructureStrongholdPieces.Stronghold.Door
					.valueOf(nbttagcompound.getString("EntryDoor"));
		}

		protected void placeDoor(World worldIn, EaglercraftRandom parRandom,
				StructureBoundingBox parStructureBoundingBox, StructureStrongholdPieces.Stronghold.Door parDoor,
				int parInt1, int parInt2, int parInt3) {
			switch (parDoor) {
			case OPENING:
			default:
				this.fillWithBlocks(worldIn, parStructureBoundingBox, parInt1, parInt2, parInt3, parInt1 + 3 - 1,
						parInt2 + 3 - 1, parInt3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
				break;
			case WOOD_DOOR:
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1, parInt2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1, parInt2 + 1, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1, parInt2 + 2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1 + 1, parInt2 + 2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1 + 2, parInt2 + 2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1 + 2, parInt2 + 1, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1 + 2, parInt2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.oak_door.getDefaultState(), parInt1 + 1, parInt2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.oak_door.getStateFromMeta(8), parInt1 + 1, parInt2 + 1, parInt3,
						parStructureBoundingBox);
				break;
			case GRATES:
				this.setBlockState(worldIn, Blocks.air.getDefaultState(), parInt1 + 1, parInt2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.air.getDefaultState(), parInt1 + 1, parInt2 + 1, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), parInt1, parInt2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), parInt1, parInt2 + 1, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), parInt1, parInt2 + 2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), parInt1 + 1, parInt2 + 2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), parInt1 + 2, parInt2 + 2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), parInt1 + 2, parInt2 + 1, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), parInt1 + 2, parInt2, parInt3,
						parStructureBoundingBox);
				break;
			case IRON_DOOR:
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1, parInt2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1, parInt2 + 1, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1, parInt2 + 2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1 + 1, parInt2 + 2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1 + 2, parInt2 + 2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1 + 2, parInt2 + 1, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), parInt1 + 2, parInt2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.iron_door.getDefaultState(), parInt1 + 1, parInt2, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(8), parInt1 + 1, parInt2 + 1, parInt3,
						parStructureBoundingBox);
				this.setBlockState(worldIn,
						Blocks.stone_button.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_button, 4)),
						parInt1 + 2, parInt2 + 1, parInt3 + 1, parStructureBoundingBox);
				this.setBlockState(worldIn,
						Blocks.stone_button.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_button, 3)),
						parInt1 + 2, parInt2 + 1, parInt3 - 1, parStructureBoundingBox);
			}

		}

		protected StructureStrongholdPieces.Stronghold.Door getRandomDoor(EaglercraftRandom parRandom) {
			int i = parRandom.nextInt(5);
			switch (i) {
			case 0:
			case 1:
			default:
				return StructureStrongholdPieces.Stronghold.Door.OPENING;
			case 2:
				return StructureStrongholdPieces.Stronghold.Door.WOOD_DOOR;
			case 3:
				return StructureStrongholdPieces.Stronghold.Door.GRATES;
			case 4:
				return StructureStrongholdPieces.Stronghold.Door.IRON_DOOR;
			}
		}

		protected StructureComponent getNextComponentNormal(StructureStrongholdPieces.Stairs2 parStairs2_1,
				List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2) {
			if (this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.minX + parInt1, this.boundingBox.minY + parInt2, this.boundingBox.minZ - 1,
							this.coordBaseMode, this.getComponentType());
				case SOUTH:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.minX + parInt1, this.boundingBox.minY + parInt2, this.boundingBox.maxZ + 1,
							this.coordBaseMode, this.getComponentType());
				case WEST:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.minX - 1, this.boundingBox.minY + parInt2, this.boundingBox.minZ + parInt1,
							this.coordBaseMode, this.getComponentType());
				case EAST:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.maxX + 1, this.boundingBox.minY + parInt2, this.boundingBox.minZ + parInt1,
							this.coordBaseMode, this.getComponentType());
				}
			}

			return null;
		}

		protected StructureComponent getNextComponentX(StructureStrongholdPieces.Stairs2 parStairs2_1,
				List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2) {
			if (this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.minX - 1, this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2,
							EnumFacing.WEST, this.getComponentType());
				case SOUTH:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.minX - 1, this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2,
							EnumFacing.WEST, this.getComponentType());
				case WEST:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.minX + parInt2, this.boundingBox.minY + parInt1, this.boundingBox.minZ - 1,
							EnumFacing.NORTH, this.getComponentType());
				case EAST:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.minX + parInt2, this.boundingBox.minY + parInt1, this.boundingBox.minZ - 1,
							EnumFacing.NORTH, this.getComponentType());
				}
			}

			return null;
		}

		protected StructureComponent getNextComponentZ(StructureStrongholdPieces.Stairs2 parStairs2_1,
				List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2) {
			if (this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.maxX + 1, this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2,
							EnumFacing.EAST, this.getComponentType());
				case SOUTH:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.maxX + 1, this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2,
							EnumFacing.EAST, this.getComponentType());
				case WEST:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.minX + parInt2, this.boundingBox.minY + parInt1, this.boundingBox.maxZ + 1,
							EnumFacing.SOUTH, this.getComponentType());
				case EAST:
					return StructureStrongholdPieces.func_175953_c(parStairs2_1, parList, parRandom,
							this.boundingBox.minX + parInt2, this.boundingBox.minY + parInt1, this.boundingBox.maxZ + 1,
							EnumFacing.SOUTH, this.getComponentType());
				}
			}

			return null;
		}

		protected static boolean canStrongholdGoDeeper(StructureBoundingBox parStructureBoundingBox) {
			return parStructureBoundingBox != null && parStructureBoundingBox.minY > 10;
		}

		public static enum Door {
			OPENING, WOOD_DOOR, GRATES, IRON_DOOR;
		}
	}
}