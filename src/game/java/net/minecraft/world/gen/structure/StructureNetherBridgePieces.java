package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
public class StructureNetherBridgePieces {
	private static final StructureNetherBridgePieces.PieceWeight[] primaryComponents = new StructureNetherBridgePieces.PieceWeight[] {
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Straight.class, 30, 0, true),
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing3.class, 10, 4),
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing.class, 10, 4),
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Stairs.class, 10, 3),
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Throne.class, 5, 2),
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Entrance.class, 5, 1) };
	private static final StructureNetherBridgePieces.PieceWeight[] secondaryComponents = new StructureNetherBridgePieces.PieceWeight[] {
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor5.class, 25, 0, true),
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing2.class, 15, 5),
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor2.class, 5, 10),
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor.class, 5, 10),
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor3.class, 10, 3, true),
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor4.class, 7, 2),
			new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.NetherStalkRoom.class, 5, 2) };

	public static void registerNetherFortressPieces() {
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Crossing3.class,
				StructureNetherBridgePieces.Crossing3::new, "NeBCr");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.End.class,
				StructureNetherBridgePieces.End::new, "NeBEF");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Straight.class,
				StructureNetherBridgePieces.Straight::new, "NeBS");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor3.class,
				StructureNetherBridgePieces.Corridor3::new, "NeCCS");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor4.class,
				StructureNetherBridgePieces.Corridor4::new, "NeCTB");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Entrance.class,
				StructureNetherBridgePieces.Entrance::new, "NeCE");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Crossing2.class,
				StructureNetherBridgePieces.Crossing2::new, "NeSCSC");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor.class,
				StructureNetherBridgePieces.Corridor::new, "NeSCLT");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor5.class,
				StructureNetherBridgePieces.Corridor5::new, "NeSC");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor2.class,
				StructureNetherBridgePieces.Corridor2::new, "NeSCRT");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.NetherStalkRoom.class,
				StructureNetherBridgePieces.NetherStalkRoom::new, "NeCSR");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Throne.class,
				StructureNetherBridgePieces.Throne::new, "NeMT");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Crossing.class,
				StructureNetherBridgePieces.Crossing::new, "NeRC");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Stairs.class,
				StructureNetherBridgePieces.Stairs::new, "NeSR");
		MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Start.class,
				StructureNetherBridgePieces.Start::new, "NeStart");
	}

	private static StructureNetherBridgePieces.Piece func_175887_b(
			StructureNetherBridgePieces.PieceWeight parPieceWeight, List<StructureComponent> parList,
			EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing, int parInt4) {
		Class oclass = parPieceWeight.weightClass;
		Object object = null;
		if (oclass == StructureNetherBridgePieces.Straight.class) {
			object = StructureNetherBridgePieces.Straight.func_175882_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureNetherBridgePieces.Crossing3.class) {
			object = StructureNetherBridgePieces.Crossing3.func_175885_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureNetherBridgePieces.Crossing.class) {
			object = StructureNetherBridgePieces.Crossing.func_175873_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureNetherBridgePieces.Stairs.class) {
			object = StructureNetherBridgePieces.Stairs.func_175872_a(parList, parRandom, parInt1, parInt2, parInt3,
					parInt4, parEnumFacing);
		} else if (oclass == StructureNetherBridgePieces.Throne.class) {
			object = StructureNetherBridgePieces.Throne.func_175874_a(parList, parRandom, parInt1, parInt2, parInt3,
					parInt4, parEnumFacing);
		} else if (oclass == StructureNetherBridgePieces.Entrance.class) {
			object = StructureNetherBridgePieces.Entrance.func_175881_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureNetherBridgePieces.Corridor5.class) {
			object = StructureNetherBridgePieces.Corridor5.func_175877_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureNetherBridgePieces.Corridor2.class) {
			object = StructureNetherBridgePieces.Corridor2.func_175876_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureNetherBridgePieces.Corridor.class) {
			object = StructureNetherBridgePieces.Corridor.func_175879_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureNetherBridgePieces.Corridor3.class) {
			object = StructureNetherBridgePieces.Corridor3.func_175883_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureNetherBridgePieces.Corridor4.class) {
			object = StructureNetherBridgePieces.Corridor4.func_175880_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureNetherBridgePieces.Crossing2.class) {
			object = StructureNetherBridgePieces.Crossing2.func_175878_a(parList, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		} else if (oclass == StructureNetherBridgePieces.NetherStalkRoom.class) {
			object = StructureNetherBridgePieces.NetherStalkRoom.func_175875_a(parList, parRandom, parInt1, parInt2,
					parInt3, parEnumFacing, parInt4);
		}

		return (StructureNetherBridgePieces.Piece) object;
	}

	public static class Corridor extends StructureNetherBridgePieces.Piece {
		private boolean field_111021_b;

		public Corridor() {
		}

		public Corridor(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
			this.field_111021_b = parRandom.nextInt(3) == 0;
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.field_111021_b = nbttagcompound.getBoolean("Chest");
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Chest", this.field_111021_b);
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentX((StructureNetherBridgePieces.Start) structurecomponent, list, random, 0, 1, true);
		}

		public static StructureNetherBridgePieces.Corridor func_175879_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, 0, 0, 5, 7, 5, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Corridor(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 4, 5, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 3, 1, 4, 4, 1,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 3, 3, 4, 4, 3,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 5, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 4, 3, 5, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 3, 4, 1, 4, 4,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 3, 4, 3, 4, 4,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			if (this.field_111021_b && structureboundingbox.isVecInside(
					new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3)))) {
				this.field_111021_b = false;
				this.generateChestContents(world, structureboundingbox, random, 3, 2, 3, field_111019_a,
						2 + random.nextInt(4));
			}

			this.fillWithBlocks(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);

			for (int i = 0; i <= 4; ++i) {
				for (int j = 0; j <= 4; ++j) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i, -1, j,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	public static class Corridor2 extends StructureNetherBridgePieces.Piece {
		private boolean field_111020_b;

		public Corridor2() {
		}

		public Corridor2(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
			this.field_111020_b = parRandom.nextInt(3) == 0;
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.field_111020_b = nbttagcompound.getBoolean("Chest");
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Chest", this.field_111020_b);
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentZ((StructureNetherBridgePieces.Start) structurecomponent, list, random, 0, 1, true);
		}

		public static StructureNetherBridgePieces.Corridor2 func_175876_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, 0, 0, 5, 7, 5, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Corridor2(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 5, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 1, 0, 4, 1,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 3, 0, 4, 3,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 4, 5, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 2, 4, 4, 5, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 3, 4, 1, 4, 4,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 3, 4, 3, 4, 4,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			if (this.field_111020_b && structureboundingbox.isVecInside(
					new BlockPos(this.getXWithOffset(1, 3), this.getYWithOffset(2), this.getZWithOffset(1, 3)))) {
				this.field_111020_b = false;
				this.generateChestContents(world, structureboundingbox, random, 1, 2, 3, field_111019_a,
						2 + random.nextInt(4));
			}

			this.fillWithBlocks(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);

			for (int i = 0; i <= 4; ++i) {
				for (int j = 0; j <= 4; ++j) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i, -1, j,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	public static class Corridor3 extends StructureNetherBridgePieces.Piece {
		public Corridor3() {
		}

		public Corridor3(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurecomponent, list, random, 1, 0,
					true);
		}

		public static StructureNetherBridgePieces.Corridor3 func_175883_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, -7, 0, 5, 14, 10, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Corridor3(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			int i = this.getMetadataWithOffset(Blocks.nether_brick_stairs, 2);

			for (int j = 0; j <= 9; ++j) {
				int k = Math.max(1, 7 - j);
				int l = Math.min(Math.max(k + 5, 14 - j), 13);
				int i1 = j;
				this.fillWithBlocks(world, structureboundingbox, 0, 0, j, 4, k, j,
						Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 1, k + 1, j, 3, l - 1, j, Blocks.air.getDefaultState(),
						Blocks.air.getDefaultState(), false);
				if (j <= 6) {
					this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(i), 1, k + 1, j,
							structureboundingbox);
					this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(i), 2, k + 1, j,
							structureboundingbox);
					this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(i), 3, k + 1, j,
							structureboundingbox);
				}

				this.fillWithBlocks(world, structureboundingbox, 0, l, j, 4, l, j,
						Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 0, k + 1, j, 0, l - 1, j,
						Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 4, k + 1, j, 4, l - 1, j,
						Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
				if ((j & 1) == 0) {
					this.fillWithBlocks(world, structureboundingbox, 0, k + 2, j, 0, k + 3, j,
							Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
							false);
					this.fillWithBlocks(world, structureboundingbox, 4, k + 2, j, 4, k + 3, j,
							Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
							false);
				}

				for (int j1 = 0; j1 <= 4; ++j1) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), j1, -1, i1,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	public static class Corridor4 extends StructureNetherBridgePieces.Piece {
		public Corridor4() {
		}

		public Corridor4(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			byte b0 = 1;
			if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.NORTH) {
				b0 = 5;
			}

			this.getNextComponentX((StructureNetherBridgePieces.Start) structurecomponent, list, random, 0, b0,
					random.nextInt(8) > 0);
			this.getNextComponentZ((StructureNetherBridgePieces.Start) structurecomponent, list, random, 0, b0,
					random.nextInt(8) > 0);
		}

		public static StructureNetherBridgePieces.Corridor4 func_175880_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -3, 0, 0, 9, 7, 9, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Corridor4(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 8, 1, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 8, 5, 8, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 6, 0, 8, 6, 5, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 2, 5, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 2, 0, 8, 5, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 3, 0, 1, 4, 0,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 7, 3, 0, 7, 4, 0,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 4, 8, 2, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 4, 2, 2, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 1, 4, 7, 2, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 8, 8, 3, 8,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 6, 0, 3, 7,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 3, 6, 8, 3, 7,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 4, 0, 5, 5, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 3, 4, 8, 5, 5, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 3, 5, 2, 5, 5, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 3, 5, 7, 5, 5, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 4, 5, 1, 5, 5,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 7, 4, 5, 7, 5, 5,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);

			for (int i = 0; i <= 5; ++i) {
				for (int j = 0; j <= 8; ++j) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), j, -1, i,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	public static class Corridor5 extends StructureNetherBridgePieces.Piece {
		public Corridor5() {
		}

		public Corridor5(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurecomponent, list, random, 1, 0,
					true);
		}

		public static StructureNetherBridgePieces.Corridor5 func_175877_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, 0, 0, 5, 7, 5, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Corridor5(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 5, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 4, 5, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 1, 0, 4, 1,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 3, 0, 4, 3,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 3, 1, 4, 4, 1,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 3, 3, 4, 4, 3,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);

			for (int i = 0; i <= 4; ++i) {
				for (int j = 0; j <= 4; ++j) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i, -1, j,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	public static class Crossing extends StructureNetherBridgePieces.Piece {
		public Crossing() {
		}

		public Crossing(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurecomponent, list, random, 2, 0,
					false);
			this.getNextComponentX((StructureNetherBridgePieces.Start) structurecomponent, list, random, 0, 2, false);
			this.getNextComponentZ((StructureNetherBridgePieces.Start) structurecomponent, list, random, 0, 2, false);
		}

		public static StructureNetherBridgePieces.Crossing func_175873_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -2, 0, 0, 7, 9, 7, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Crossing(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 6, 1, 6, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 6, 7, 6, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 1, 6, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 6, 1, 6, 6, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 2, 0, 6, 6, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 2, 6, 6, 6, 6, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 6, 1, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 5, 0, 6, 6, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 2, 0, 6, 6, 1, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 2, 5, 6, 6, 6, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 6, 0, 4, 6, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 5, 0, 4, 5, 0,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 6, 6, 4, 6, 6, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 5, 6, 4, 5, 6,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 6, 2, 0, 6, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 2, 0, 5, 4,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 6, 2, 6, 6, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 5, 2, 6, 5, 4,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);

			for (int i = 0; i <= 6; ++i) {
				for (int j = 0; j <= 6; ++j) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i, -1, j,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	public static class Crossing2 extends StructureNetherBridgePieces.Piece {
		public Crossing2() {
		}

		public Crossing2(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurecomponent, list, random, 1, 0,
					true);
			this.getNextComponentX((StructureNetherBridgePieces.Start) structurecomponent, list, random, 0, 1, true);
			this.getNextComponentZ((StructureNetherBridgePieces.Start) structurecomponent, list, random, 0, 1, true);
		}

		public static StructureNetherBridgePieces.Crossing2 func_175878_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, 0, 0, 5, 7, 5, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Crossing2(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 5, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 4, 5, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 4, 0, 5, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 2, 4, 4, 5, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);

			for (int i = 0; i <= 4; ++i) {
				for (int j = 0; j <= 4; ++j) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i, -1, j,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	public static class Crossing3 extends StructureNetherBridgePieces.Piece {
		public Crossing3() {
		}

		public Crossing3(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		protected Crossing3(EaglercraftRandom parRandom, int parInt1, int parInt2) {
			super(0);
			this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(parRandom);
			switch (this.coordBaseMode) {
			case NORTH:
			case SOUTH:
				this.boundingBox = new StructureBoundingBox(parInt1, 64, parInt2, parInt1 + 19 - 1, 73,
						parInt2 + 19 - 1);
				break;
			default:
				this.boundingBox = new StructureBoundingBox(parInt1, 64, parInt2, parInt1 + 19 - 1, 73,
						parInt2 + 19 - 1);
			}

		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurecomponent, list, random, 8, 3,
					false);
			this.getNextComponentX((StructureNetherBridgePieces.Start) structurecomponent, list, random, 3, 8, false);
			this.getNextComponentZ((StructureNetherBridgePieces.Start) structurecomponent, list, random, 3, 8, false);
		}

		public static StructureNetherBridgePieces.Crossing3 func_175885_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -8, -3, 0, 19, 10, 19, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Crossing3(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 7, 3, 0, 11, 4, 18, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 7, 18, 4, 11, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 5, 0, 10, 7, 18, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 8, 18, 7, 10, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 7, 5, 0, 7, 5, 7, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 7, 5, 11, 7, 5, 18, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 11, 5, 0, 11, 5, 7, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 11, 5, 11, 11, 5, 18,
					Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 7, 7, 5, 7, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 11, 5, 7, 18, 5, 7, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 11, 7, 5, 11, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 11, 5, 11, 18, 5, 11,
					Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 7, 2, 0, 11, 2, 5, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 7, 2, 13, 11, 2, 18, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 7, 0, 0, 11, 1, 3, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 7, 0, 15, 11, 1, 18, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);

			for (int i = 7; i <= 11; ++i) {
				for (int j = 0; j <= 2; ++j) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i, -1, j,
							structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i, -1, 18 - j,
							structureboundingbox);
				}
			}

			this.fillWithBlocks(world, structureboundingbox, 0, 2, 7, 5, 2, 11, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 13, 2, 7, 18, 2, 11, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 7, 3, 1, 11, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 15, 0, 7, 18, 1, 11, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);

			for (int k = 0; k <= 2; ++k) {
				for (int l = 7; l <= 11; ++l) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), k, -1, l,
							structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), 18 - k, -1, l,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	public static class End extends StructureNetherBridgePieces.Piece {
		private int fillSeed;

		public End() {
		}

		public End(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
			this.fillSeed = parRandom.nextInt();
		}

		public static StructureNetherBridgePieces.End func_175884_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, -3, 0, 5, 10, 8, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.End(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.fillSeed = nbttagcompound.getInteger("Seed");
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setInteger("Seed", this.fillSeed);
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			EaglercraftRandom random = new EaglercraftRandom((long) this.fillSeed);

			for (int i = 0; i <= 4; ++i) {
				for (int j = 3; j <= 4; ++j) {
					int k = random.nextInt(8);
					this.fillWithBlocks(world, structureboundingbox, i, j, 0, i, j, k,
							Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
				}
			}

			int l = random.nextInt(8);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 0, 5, l, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			l = random.nextInt(8);
			this.fillWithBlocks(world, structureboundingbox, 4, 5, 0, 4, 5, l, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);

			for (l = 0; l <= 4; ++l) {
				int i1 = random.nextInt(5);
				this.fillWithBlocks(world, structureboundingbox, l, 2, 0, l, 2, i1,
						Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			}

			for (l = 0; l <= 4; ++l) {
				for (int j1 = 0; j1 <= 1; ++j1) {
					int k1 = random.nextInt(3);
					this.fillWithBlocks(world, structureboundingbox, l, j1, 0, l, j1, k1,
							Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
				}
			}

			return true;
		}
	}

	public static class Entrance extends StructureNetherBridgePieces.Piece {
		public Entrance() {
		}

		public Entrance(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurecomponent, list, random, 5, 3,
					true);
		}

		public static StructureNetherBridgePieces.Entrance func_175881_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -5, -3, 0, 13, 14, 13, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Entrance(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 12, 4, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 12, 13, 12, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 1, 12, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 11, 5, 0, 12, 12, 12,
					Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 5, 11, 4, 12, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 5, 11, 10, 12, 12,
					Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 9, 11, 7, 12, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 5, 0, 4, 12, 1, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 5, 0, 10, 12, 1, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 9, 0, 7, 12, 1, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 11, 2, 10, 12, 10,
					Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 8, 0, 7, 8, 0,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);

			for (int i = 1; i <= 11; i += 2) {
				this.fillWithBlocks(world, structureboundingbox, i, 10, 0, i, 11, 0,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
				this.fillWithBlocks(world, structureboundingbox, i, 10, 12, i, 11, 12,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
				this.fillWithBlocks(world, structureboundingbox, 0, 10, i, 0, 11, i,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
				this.fillWithBlocks(world, structureboundingbox, 12, 10, i, 12, 11, i,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
				this.setBlockState(world, Blocks.nether_brick.getDefaultState(), i, 13, 0, structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick.getDefaultState(), i, 13, 12, structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 0, 13, i, structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 12, 13, i, structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), i + 1, 13, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), i + 1, 13, 12,
						structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, i + 1,
						structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 12, 13, i + 1,
						structureboundingbox);
			}

			this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 12, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 12, 13, 0, structureboundingbox);

			for (int k = 3; k <= 9; k += 2) {
				this.fillWithBlocks(world, structureboundingbox, 1, 7, k, 1, 8, k,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
				this.fillWithBlocks(world, structureboundingbox, 11, 7, k, 11, 8, k,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
			}

			this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 8, 2, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 4, 12, 2, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 0, 0, 8, 1, 3, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 0, 9, 8, 1, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 4, 3, 1, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 9, 0, 4, 12, 1, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);

			for (int l = 4; l <= 8; ++l) {
				for (int j = 0; j <= 2; ++j) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), l, -1, j,
							structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), l, -1, 12 - j,
							structureboundingbox);
				}
			}

			for (int i1 = 0; i1 <= 2; ++i1) {
				for (int j1 = 4; j1 <= 8; ++j1) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i1, -1, j1,
							structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), 12 - i1, -1, j1,
							structureboundingbox);
				}
			}

			this.fillWithBlocks(world, structureboundingbox, 5, 5, 5, 7, 5, 7, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 1, 6, 6, 4, 6, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 6, 0, 6, structureboundingbox);
			this.setBlockState(world, Blocks.flowing_lava.getDefaultState(), 6, 5, 6, structureboundingbox);
			BlockPos blockpos = new BlockPos(this.getXWithOffset(6, 6), this.getYWithOffset(5),
					this.getZWithOffset(6, 6));
			if (structureboundingbox.isVecInside(blockpos)) {
				world.forceBlockUpdateTick(Blocks.flowing_lava, blockpos, random);
			}

			return true;
		}
	}

	public static class NetherStalkRoom extends StructureNetherBridgePieces.Piece {
		public NetherStalkRoom() {
		}

		public NetherStalkRoom(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurecomponent, list, random, 5, 3,
					true);
			this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurecomponent, list, random, 5, 11,
					true);
		}

		public static StructureNetherBridgePieces.NetherStalkRoom func_175875_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -5, -3, 0, 13, 14, 13, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.NetherStalkRoom(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 12, 4, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 12, 13, 12, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 1, 12, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 11, 5, 0, 12, 12, 12,
					Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 5, 11, 4, 12, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 5, 11, 10, 12, 12,
					Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 9, 11, 7, 12, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 5, 0, 4, 12, 1, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 5, 0, 10, 12, 1, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 9, 0, 7, 12, 1, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 11, 2, 10, 12, 10,
					Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);

			for (int i = 1; i <= 11; i += 2) {
				this.fillWithBlocks(world, structureboundingbox, i, 10, 0, i, 11, 0,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
				this.fillWithBlocks(world, structureboundingbox, i, 10, 12, i, 11, 12,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
				this.fillWithBlocks(world, structureboundingbox, 0, 10, i, 0, 11, i,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
				this.fillWithBlocks(world, structureboundingbox, 12, 10, i, 12, 11, i,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
				this.setBlockState(world, Blocks.nether_brick.getDefaultState(), i, 13, 0, structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick.getDefaultState(), i, 13, 12, structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 0, 13, i, structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 12, 13, i, structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), i + 1, 13, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), i + 1, 13, 12,
						structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, i + 1,
						structureboundingbox);
				this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 12, 13, i + 1,
						structureboundingbox);
			}

			this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 12, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 12, 13, 0, structureboundingbox);

			for (int j1 = 3; j1 <= 9; j1 += 2) {
				this.fillWithBlocks(world, structureboundingbox, 1, 7, j1, 1, 8, j1,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
				this.fillWithBlocks(world, structureboundingbox, 11, 7, j1, 11, 8, j1,
						Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(),
						false);
			}

			int k1 = this.getMetadataWithOffset(Blocks.nether_brick_stairs, 3);

			for (int j = 0; j <= 6; ++j) {
				int k = j + 4;

				for (int l = 5; l <= 7; ++l) {
					this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(k1), l, 5 + j, k,
							structureboundingbox);
				}

				if (k >= 5 && k <= 8) {
					this.fillWithBlocks(world, structureboundingbox, 5, 5, k, 7, j + 4, k,
							Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
				} else if (k >= 9 && k <= 10) {
					this.fillWithBlocks(world, structureboundingbox, 5, 8, k, 7, j + 4, k,
							Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
				}

				if (j >= 1) {
					this.fillWithBlocks(world, structureboundingbox, 5, 6 + j, k, 7, 9 + j, k,
							Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
				}
			}

			for (int l1 = 5; l1 <= 7; ++l1) {
				this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(k1), l1, 12, 11,
						structureboundingbox);
			}

			this.fillWithBlocks(world, structureboundingbox, 5, 6, 7, 5, 7, 7,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 7, 6, 7, 7, 7, 7,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 13, 12, 7, 13, 12, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 5, 2, 3, 5, 3, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 5, 9, 3, 5, 10, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 5, 4, 2, 5, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 9, 5, 2, 10, 5, 3, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 9, 5, 9, 10, 5, 10, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 10, 5, 4, 10, 5, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			int i2 = this.getMetadataWithOffset(Blocks.nether_brick_stairs, 0);
			int j2 = this.getMetadataWithOffset(Blocks.nether_brick_stairs, 1);
			this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(j2), 4, 5, 2, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(j2), 4, 5, 3, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(j2), 4, 5, 9, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(j2), 4, 5, 10, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(i2), 8, 5, 2, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(i2), 8, 5, 3, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(i2), 8, 5, 9, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_stairs.getStateFromMeta(i2), 8, 5, 10, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 3, 4, 4, 4, 4, 8, Blocks.soul_sand.getDefaultState(),
					Blocks.soul_sand.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 4, 4, 9, 4, 8, Blocks.soul_sand.getDefaultState(),
					Blocks.soul_sand.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 5, 4, 4, 5, 8, Blocks.nether_wart.getDefaultState(),
					Blocks.nether_wart.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 5, 4, 9, 5, 8, Blocks.nether_wart.getDefaultState(),
					Blocks.nether_wart.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 8, 2, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 4, 12, 2, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 0, 0, 8, 1, 3, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 0, 9, 8, 1, 12, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 4, 3, 1, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 9, 0, 4, 12, 1, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);

			for (int k2 = 4; k2 <= 8; ++k2) {
				for (int i1 = 0; i1 <= 2; ++i1) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), k2, -1, i1,
							structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), k2, -1, 12 - i1,
							structureboundingbox);
				}
			}

			for (int l2 = 0; l2 <= 2; ++l2) {
				for (int i3 = 4; i3 <= 8; ++i3) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), l2, -1, i3,
							structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), 12 - l2, -1, i3,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	abstract static class Piece extends StructureComponent {
		protected static final List<WeightedRandomChestContent> field_111019_a = Lists.newArrayList(
				new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 5),
						new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 5),
						new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 15),
						new WeightedRandomChestContent(Items.golden_sword, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.golden_chestplate, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.flint_and_steel, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.nether_wart, 0, 3, 7, 5),
						new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10),
						new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 8),
						new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5),
						new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 3),
						new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.obsidian), 0, 2, 4, 2) });

		public Piece() {
		}

		protected Piece(int parInt1) {
			super(parInt1);
		}

		protected void readStructureFromNBT(NBTTagCompound var1) {
		}

		protected void writeStructureToNBT(NBTTagCompound var1) {
		}

		private int getTotalWeight(List<StructureNetherBridgePieces.PieceWeight> parList) {
			boolean flag = false;
			int i = 0;

			for (StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight : parList) {
				if (structurenetherbridgepieces$pieceweight.field_78824_d > 0
						&& structurenetherbridgepieces$pieceweight.field_78827_c < structurenetherbridgepieces$pieceweight.field_78824_d) {
					flag = true;
				}

				i += structurenetherbridgepieces$pieceweight.field_78826_b;
			}

			return flag ? i : -1;
		}

		private StructureNetherBridgePieces.Piece func_175871_a(StructureNetherBridgePieces.Start parStart,
				List<StructureNetherBridgePieces.PieceWeight> parList, List<StructureComponent> parList2,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			int i = this.getTotalWeight(parList);
			boolean flag = i > 0 && parInt4 <= 30;
			int j = 0;

			while (j < 5 && flag) {
				++j;
				int k = parRandom.nextInt(i);

				for (StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight : parList) {
					k -= structurenetherbridgepieces$pieceweight.field_78826_b;
					if (k < 0) {
						if (!structurenetherbridgepieces$pieceweight.func_78822_a(parInt4)
								|| structurenetherbridgepieces$pieceweight == parStart.theNetherBridgePieceWeight
										&& !structurenetherbridgepieces$pieceweight.field_78825_e) {
							break;
						}

						StructureNetherBridgePieces.Piece structurenetherbridgepieces$piece = StructureNetherBridgePieces
								.func_175887_b(structurenetherbridgepieces$pieceweight, parList2, parRandom, parInt1,
										parInt2, parInt3, parEnumFacing, parInt4);
						if (structurenetherbridgepieces$piece != null) {
							++structurenetherbridgepieces$pieceweight.field_78827_c;
							parStart.theNetherBridgePieceWeight = structurenetherbridgepieces$pieceweight;
							if (!structurenetherbridgepieces$pieceweight.func_78823_a()) {
								parList.remove(structurenetherbridgepieces$pieceweight);
							}

							return structurenetherbridgepieces$piece;
						}
					}
				}
			}

			return StructureNetherBridgePieces.End.func_175884_a(parList2, parRandom, parInt1, parInt2, parInt3,
					parEnumFacing, parInt4);
		}

		private StructureComponent func_175870_a(StructureNetherBridgePieces.Start parStart,
				List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3,
				EnumFacing parEnumFacing, int parInt4, boolean parFlag) {
			if (Math.abs(parInt1 - parStart.getBoundingBox().minX) <= 112
					&& Math.abs(parInt3 - parStart.getBoundingBox().minZ) <= 112) {
				List list = parStart.primaryWeights;
				if (parFlag) {
					list = parStart.secondaryWeights;
				}

				StructureNetherBridgePieces.Piece structurenetherbridgepieces$piece = this.func_175871_a(parStart, list,
						parList, parRandom, parInt1, parInt2, parInt3, parEnumFacing, parInt4 + 1);
				if (structurenetherbridgepieces$piece != null) {
					parList.add(structurenetherbridgepieces$piece);
					parStart.field_74967_d.add(structurenetherbridgepieces$piece);
				}

				return structurenetherbridgepieces$piece;
			} else {
				return StructureNetherBridgePieces.End.func_175884_a(parList, parRandom, parInt1, parInt2, parInt3,
						parEnumFacing, parInt4);
			}
		}

		protected StructureComponent getNextComponentNormal(StructureNetherBridgePieces.Start parStart,
				List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2,
				boolean parFlag) {
			if (this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.minX + parInt1,
							this.boundingBox.minY + parInt2, this.boundingBox.minZ - 1, this.coordBaseMode,
							this.getComponentType(), parFlag);
				case SOUTH:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.minX + parInt1,
							this.boundingBox.minY + parInt2, this.boundingBox.maxZ + 1, this.coordBaseMode,
							this.getComponentType(), parFlag);
				case WEST:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.minX - 1,
							this.boundingBox.minY + parInt2, this.boundingBox.minZ + parInt1, this.coordBaseMode,
							this.getComponentType(), parFlag);
				case EAST:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.maxX + 1,
							this.boundingBox.minY + parInt2, this.boundingBox.minZ + parInt1, this.coordBaseMode,
							this.getComponentType(), parFlag);
				}
			}

			return null;
		}

		protected StructureComponent getNextComponentX(StructureNetherBridgePieces.Start parStart,
				List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2,
				boolean parFlag) {
			if (this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.minX - 1,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2, EnumFacing.WEST,
							this.getComponentType(), parFlag);
				case SOUTH:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.minX - 1,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2, EnumFacing.WEST,
							this.getComponentType(), parFlag);
				case WEST:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.minX + parInt2,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ - 1, EnumFacing.NORTH,
							this.getComponentType(), parFlag);
				case EAST:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.minX + parInt2,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ - 1, EnumFacing.NORTH,
							this.getComponentType(), parFlag);
				}
			}

			return null;
		}

		protected StructureComponent getNextComponentZ(StructureNetherBridgePieces.Start parStart,
				List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2,
				boolean parFlag) {
			if (this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.maxX + 1,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2, EnumFacing.EAST,
							this.getComponentType(), parFlag);
				case SOUTH:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.maxX + 1,
							this.boundingBox.minY + parInt1, this.boundingBox.minZ + parInt2, EnumFacing.EAST,
							this.getComponentType(), parFlag);
				case WEST:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.minX + parInt2,
							this.boundingBox.minY + parInt1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH,
							this.getComponentType(), parFlag);
				case EAST:
					return this.func_175870_a(parStart, parList, parRandom, this.boundingBox.minX + parInt2,
							this.boundingBox.minY + parInt1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH,
							this.getComponentType(), parFlag);
				}
			}

			return null;
		}

		protected static boolean isAboveGround(StructureBoundingBox parStructureBoundingBox) {
			return parStructureBoundingBox != null && parStructureBoundingBox.minY > 10;
		}
	}

	static class PieceWeight {
		public Class<? extends StructureNetherBridgePieces.Piece> weightClass;
		public final int field_78826_b;
		public int field_78827_c;
		public int field_78824_d;
		public boolean field_78825_e;

		public PieceWeight(Class<? extends StructureNetherBridgePieces.Piece> parClass1, int parInt1, int parInt2,
				boolean parFlag) {
			this.weightClass = parClass1;
			this.field_78826_b = parInt1;
			this.field_78824_d = parInt2;
			this.field_78825_e = parFlag;
		}

		public PieceWeight(Class<? extends StructureNetherBridgePieces.Piece> parClass1, int parInt1, int parInt2) {
			this(parClass1, parInt1, parInt2, false);
		}

		public boolean func_78822_a(int parInt1) {
			return this.field_78824_d == 0 || this.field_78827_c < this.field_78824_d;
		}

		public boolean func_78823_a() {
			return this.field_78824_d == 0 || this.field_78827_c < this.field_78824_d;
		}
	}

	public static class Stairs extends StructureNetherBridgePieces.Piece {
		public Stairs() {
		}

		public Stairs(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentZ((StructureNetherBridgePieces.Start) structurecomponent, list, random, 6, 2, false);
		}

		public static StructureNetherBridgePieces.Stairs func_175872_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, int parInt4,
				EnumFacing parEnumFacing) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -2, 0, 0, 7, 11, 7, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Stairs(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 6, 1, 6, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 6, 10, 6, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 1, 8, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 2, 0, 6, 8, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 1, 0, 8, 6, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 2, 1, 6, 8, 6, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 2, 6, 5, 8, 6, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 2, 0, 5, 4,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 3, 2, 6, 5, 2,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 3, 4, 6, 5, 4,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.setBlockState(world, Blocks.nether_brick.getDefaultState(), 5, 2, 5, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 4, 2, 5, 4, 3, 5, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 2, 5, 3, 4, 5, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 2, 5, 2, 5, 5, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 2, 5, 1, 6, 5, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 7, 1, 5, 7, 4, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 8, 2, 6, 8, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 6, 0, 4, 8, 0, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 5, 0, 4, 5, 0,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);

			for (int i = 0; i <= 6; ++i) {
				for (int j = 0; j <= 6; ++j) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i, -1, j,
							structureboundingbox);
				}
			}

			return true;
		}
	}

	public static class Start extends StructureNetherBridgePieces.Crossing3 {
		public StructureNetherBridgePieces.PieceWeight theNetherBridgePieceWeight;
		public List<StructureNetherBridgePieces.PieceWeight> primaryWeights;
		public List<StructureNetherBridgePieces.PieceWeight> secondaryWeights;
		public List<StructureComponent> field_74967_d = Lists.newArrayList();

		public Start() {
		}

		public Start(EaglercraftRandom parRandom, int parInt1, int parInt2) {
			super(parRandom, parInt1, parInt2);
			this.primaryWeights = Lists.newArrayList();

			for (StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight : StructureNetherBridgePieces.primaryComponents) {
				structurenetherbridgepieces$pieceweight.field_78827_c = 0;
				this.primaryWeights.add(structurenetherbridgepieces$pieceweight);
			}

			this.secondaryWeights = Lists.newArrayList();

			for (StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight1 : StructureNetherBridgePieces.secondaryComponents) {
				structurenetherbridgepieces$pieceweight1.field_78827_c = 0;
				this.secondaryWeights.add(structurenetherbridgepieces$pieceweight1);
			}

		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
		}
	}

	public static class Straight extends StructureNetherBridgePieces.Piece {
		public Straight() {
		}

		public Straight(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurecomponent, list, random, 1, 3,
					false);
		}

		public static StructureNetherBridgePieces.Straight func_175882_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
				int parInt4) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -1, -3, 0, 5, 10, 19, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Straight(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 4, 4, 18, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 5, 0, 3, 7, 18, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 0, 5, 18, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 5, 0, 4, 5, 18, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 4, 2, 5, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 13, 4, 2, 18, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 1, 3, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 15, 4, 1, 18, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);

			for (int i = 0; i <= 4; ++i) {
				for (int j = 0; j <= 2; ++j) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i, -1, j,
							structureboundingbox);
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i, -1, 18 - j,
							structureboundingbox);
				}
			}

			this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 4, 1,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 4, 0, 4, 4,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 3, 14, 0, 4, 14,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 1, 17, 0, 4, 17,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 4, 1,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 3, 4, 4, 4, 4,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 3, 14, 4, 4, 14,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 1, 17, 4, 4, 17,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			return true;
		}
	}

	public static class Throne extends StructureNetherBridgePieces.Piece {
		private boolean hasSpawner;

		public Throne() {
		}

		public Throne(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,
				EnumFacing parEnumFacing) {
			super(parInt1);
			this.coordBaseMode = parEnumFacing;
			this.boundingBox = parStructureBoundingBox;
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.hasSpawner = nbttagcompound.getBoolean("Mob");
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Mob", this.hasSpawner);
		}

		public static StructureNetherBridgePieces.Throne func_175874_a(List<StructureComponent> parList,
				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, int parInt4,
				EnumFacing parEnumFacing) {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parInt1,
					parInt2, parInt3, -2, 0, 0, 7, 8, 9, parEnumFacing);
			return isAboveGround(structureboundingbox)
					&& StructureComponent.findIntersecting(parList, structureboundingbox) == null
							? new StructureNetherBridgePieces.Throne(parInt4, parRandom, structureboundingbox,
									parEnumFacing)
							: null;
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 6, 7, 7, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 5, 1, 7, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 2, 1, 5, 2, 7, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 3, 2, 5, 3, 7, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 4, 3, 5, 4, 7, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 1, 4, 2, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 2, 0, 5, 4, 2, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 5, 2, 1, 5, 3, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 5, 2, 5, 5, 3, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 0, 5, 3, 0, 5, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 5, 3, 6, 5, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 5, 8, 5, 5, 8, Blocks.nether_brick.getDefaultState(),
					Blocks.nether_brick.getDefaultState(), false);
			this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 1, 6, 3, structureboundingbox);
			this.setBlockState(world, Blocks.nether_brick_fence.getDefaultState(), 5, 6, 3, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 0, 6, 3, 0, 6, 8,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 6, 3, 6, 6, 8,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 6, 8, 5, 7, 8,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 2, 8, 8, 4, 8, 8,
					Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
			if (!this.hasSpawner) {
				BlockPos blockpos = new BlockPos(this.getXWithOffset(3, 5), this.getYWithOffset(5),
						this.getZWithOffset(3, 5));
				if (structureboundingbox.isVecInside(blockpos)) {
					this.hasSpawner = true;
					world.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
					TileEntity tileentity = world.getTileEntity(blockpos);
					if (tileentity instanceof TileEntityMobSpawner) {
						((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setEntityName("Blaze");
					}
				}
			}

			for (int i = 0; i <= 6; ++i) {
				for (int j = 0; j <= 6; ++j) {
					this.replaceAirAndLiquidDownwards(world, Blocks.nether_brick.getDefaultState(), i, -1, j,
							structureboundingbox);
				}
			}

			return true;
		}
	}
}