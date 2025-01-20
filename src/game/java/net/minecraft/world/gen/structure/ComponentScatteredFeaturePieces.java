package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
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
public class ComponentScatteredFeaturePieces {
	public static void registerScatteredFeaturePieces() {
		MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.DesertPyramid.class,
				ComponentScatteredFeaturePieces.DesertPyramid::new, "TeDP");
		MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.JunglePyramid.class,
				ComponentScatteredFeaturePieces.JunglePyramid::new, "TeJP");
		MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.SwampHut.class,
				ComponentScatteredFeaturePieces.SwampHut::new, "TeSH");
	}

	public static class DesertPyramid extends ComponentScatteredFeaturePieces.Feature {
		private boolean[] field_74940_h = new boolean[4];
		private static final List<WeightedRandomChestContent> itemsToGenerateInTemple = Lists.newArrayList(
				new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3),
						new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
						new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15),
						new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2),
						new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20),
						new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16),
						new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
						new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1),
						new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1),
						new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });

		public DesertPyramid() {
		}

		public DesertPyramid(EaglercraftRandom parRandom, int parInt1, int parInt2) {
			super(parRandom, parInt1, 64, parInt2, 21, 15, 21);
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("hasPlacedChest0", this.field_74940_h[0]);
			nbttagcompound.setBoolean("hasPlacedChest1", this.field_74940_h[1]);
			nbttagcompound.setBoolean("hasPlacedChest2", this.field_74940_h[2]);
			nbttagcompound.setBoolean("hasPlacedChest3", this.field_74940_h[3]);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.field_74940_h[0] = nbttagcompound.getBoolean("hasPlacedChest0");
			this.field_74940_h[1] = nbttagcompound.getBoolean("hasPlacedChest1");
			this.field_74940_h[2] = nbttagcompound.getBoolean("hasPlacedChest2");
			this.field_74940_h[3] = nbttagcompound.getBoolean("hasPlacedChest3");
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			this.fillWithBlocks(world, structureboundingbox, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0,
					this.scatteredFeatureSizeZ - 1, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);

			for (int i = 1; i <= 9; ++i) {
				this.fillWithBlocks(world, structureboundingbox, i, i, i, this.scatteredFeatureSizeX - 1 - i, i,
						this.scatteredFeatureSizeZ - 1 - i, Blocks.sandstone.getDefaultState(),
						Blocks.sandstone.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, i + 1, i, i + 1, this.scatteredFeatureSizeX - 2 - i, i,
						this.scatteredFeatureSizeZ - 2 - i, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(),
						false);
			}

			for (int i2 = 0; i2 < this.scatteredFeatureSizeX; ++i2) {
				for (int j = 0; j < this.scatteredFeatureSizeZ; ++j) {
					byte b0 = -5;
					this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), i2, b0, j,
							structureboundingbox);
				}
			}

			int j2 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 3);
			int k2 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 2);
			int l2 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 0);
			int k = this.getMetadataWithOffset(Blocks.sandstone_stairs, 1);
			int l = ~EnumDyeColor.ORANGE.getDyeDamage() & 15;
			int i1 = ~EnumDyeColor.BLUE.getDyeDamage() & 15;
			this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 9, 4, Blocks.sandstone.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 10, 1, 3, 10, 3, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(j2), 2, 10, 0, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(k2), 2, 10, 4, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(l2), 0, 10, 2, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(k), 4, 10, 2, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, this.scatteredFeatureSizeX - 5, 0, 0,
					this.scatteredFeatureSizeX - 1, 9, 4, Blocks.sandstone.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, this.scatteredFeatureSizeX - 4, 10, 1,
					this.scatteredFeatureSizeX - 2, 10, 3, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(j2), this.scatteredFeatureSizeX - 3, 10,
					0, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(k2), this.scatteredFeatureSizeX - 3, 10,
					4, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(l2), this.scatteredFeatureSizeX - 5, 10,
					2, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(k), this.scatteredFeatureSizeX - 1, 10,
					2, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 8, 0, 0, 12, 4, 4, Blocks.sandstone.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 9, 1, 0, 11, 3, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					9, 1, 1, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					9, 2, 1, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					9, 3, 1, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					10, 3, 1, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					11, 3, 1, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					11, 2, 1, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					11, 1, 1, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 4, 1, 1, 8, 3, 3, Blocks.sandstone.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 4, 1, 2, 8, 2, 2, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 12, 1, 1, 16, 3, 3, Blocks.sandstone.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 12, 1, 2, 16, 2, 2, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 4, 5, this.scatteredFeatureSizeX - 6, 4,
					this.scatteredFeatureSizeZ - 6, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 9, 4, 9, 11, 4, 11, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 1, 8, 8, 3, 8,
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
			this.fillWithBlocks(world, structureboundingbox, 12, 1, 8, 12, 3, 8,
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
			this.fillWithBlocks(world, structureboundingbox, 8, 1, 12, 8, 3, 12,
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
			this.fillWithBlocks(world, structureboundingbox, 12, 1, 12, 12, 3, 12,
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 5, 4, 4, 11, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, this.scatteredFeatureSizeX - 5, 1, 5,
					this.scatteredFeatureSizeX - 2, 4, 11, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 6, 7, 9, 6, 7, 11, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, this.scatteredFeatureSizeX - 7, 7, 9,
					this.scatteredFeatureSizeX - 7, 7, 11, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 5, 5, 9, 5, 7, 11,
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
			this.fillWithBlocks(world, structureboundingbox, this.scatteredFeatureSizeX - 6, 5, 9,
					this.scatteredFeatureSizeX - 6, 7, 11,
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
			this.setBlockState(world, Blocks.air.getDefaultState(), 5, 5, 10, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 5, 6, 10, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 6, 6, 10, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 5, 10,
					structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 6, 10,
					structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 7, 6, 10,
					structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 2, 4, 4, 2, 6, 4, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, this.scatteredFeatureSizeX - 3, 4, 4,
					this.scatteredFeatureSizeX - 3, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(),
					false);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(j2), 2, 4, 5, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(j2), 2, 3, 4, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(j2), this.scatteredFeatureSizeX - 3, 4,
					5, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(j2), this.scatteredFeatureSizeX - 3, 3,
					4, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 1, 1, 3, 2, 2, 3, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, this.scatteredFeatureSizeX - 3, 1, 3,
					this.scatteredFeatureSizeX - 2, 2, 3, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.setBlockState(world, Blocks.sandstone_stairs.getDefaultState(), 1, 1, 2, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getDefaultState(), this.scatteredFeatureSizeX - 2, 1, 2,
					structureboundingbox);
			this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), 1,
					2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()),
					this.scatteredFeatureSizeX - 2, 2, 2, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(k), 2, 1, 2, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(l2), this.scatteredFeatureSizeX - 3, 1,
					2, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 4, 3, 5, 4, 3, 18, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, this.scatteredFeatureSizeX - 5, 3, 5,
					this.scatteredFeatureSizeX - 5, 3, 17, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 3, 1, 5, 4, 2, 16, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, this.scatteredFeatureSizeX - 6, 1, 5,
					this.scatteredFeatureSizeX - 5, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(),
					false);

			for (int j1 = 5; j1 <= 17; j1 += 2) {
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 4, 1, j1,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 4, 2, j1,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
						this.scatteredFeatureSizeX - 5, 1, j1, structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()),
						this.scatteredFeatureSizeX - 5, 2, j1, structureboundingbox);
			}

			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 10, 0, 7, structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 10, 0, 8, structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 9, 0, 9, structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 11, 0, 9, structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 8, 0, 10, structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 12, 0, 10,
					structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 7, 0, 10, structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 13, 0, 10,
					structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 9, 0, 11, structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 11, 0, 11,
					structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 10, 0, 12,
					structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 10, 0, 13,
					structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(i1), 10, 0, 10,
					structureboundingbox);

			for (int i3 = 0; i3 <= this.scatteredFeatureSizeX - 1; i3 += this.scatteredFeatureSizeX - 1) {
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), i3, 2, 1,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), i3, 2, 2,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), i3, 2, 3,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), i3, 3, 1,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), i3, 3, 2,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), i3, 3, 3,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), i3, 4, 1,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), i3, 4, 2,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), i3, 4, 3,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), i3, 5, 1,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), i3, 5, 2,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), i3, 5, 3,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), i3, 6, 1,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), i3, 6, 2,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), i3, 6, 3,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), i3, 7, 1,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), i3, 7, 2,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), i3, 7, 3,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), i3, 8, 1,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), i3, 8, 2,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), i3, 8, 3,
						structureboundingbox);
			}

			for (int j3 = 2; j3 <= this.scatteredFeatureSizeX - 3; j3 += this.scatteredFeatureSizeX - 3 - 2) {
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3 - 1, 2, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), j3, 2, 0,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3 + 1, 2, 0,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3 - 1, 3, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), j3, 3, 0,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3 + 1, 3, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), j3 - 1, 4, 0,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j3, 4, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), j3 + 1, 4, 0,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3 - 1, 5, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), j3, 5, 0,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3 + 1, 5, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), j3 - 1, 6, 0,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j3, 6, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), j3 + 1, 6, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), j3 - 1, 7, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), j3, 7, 0,
						structureboundingbox);
				this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), j3 + 1, 7, 0,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3 - 1, 8, 0,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 8, 0,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3 + 1, 8, 0,
						structureboundingbox);
			}

			this.fillWithBlocks(world, structureboundingbox, 8, 4, 0, 12, 6, 0,
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
			this.setBlockState(world, Blocks.air.getDefaultState(), 8, 6, 0, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 12, 6, 0, structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 9, 5, 0, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()),
					10, 5, 0, structureboundingbox);
			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(l), 11, 5, 0, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 8, -14, 8, 12, -11, 12,
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
			this.fillWithBlocks(world, structureboundingbox, 8, -10, 8, 12, -10, 12,
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()),
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), false);
			this.fillWithBlocks(world, structureboundingbox, 8, -9, 8, 12, -9, 12,
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
			this.fillWithBlocks(world, structureboundingbox, 8, -8, 8, 12, -1, 12, Blocks.sandstone.getDefaultState(),
					Blocks.sandstone.getDefaultState(), false);
			this.fillWithBlocks(world, structureboundingbox, 9, -11, 9, 11, -1, 11, Blocks.air.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.setBlockState(world, Blocks.stone_pressure_plate.getDefaultState(), 10, -11, 10, structureboundingbox);
			this.fillWithBlocks(world, structureboundingbox, 9, -13, 9, 11, -13, 11, Blocks.tnt.getDefaultState(),
					Blocks.air.getDefaultState(), false);
			this.setBlockState(world, Blocks.air.getDefaultState(), 8, -11, 10, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 8, -10, 10, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()),
					7, -10, 10, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					7, -11, 10, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 12, -11, 10, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 12, -10, 10, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()),
					13, -10, 10, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					13, -11, 10, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 10, -11, 8, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 10, -10, 8, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()),
					10, -10, 7, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					10, -11, 7, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 10, -11, 12, structureboundingbox);
			this.setBlockState(world, Blocks.air.getDefaultState(), 10, -10, 12, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()),
					10, -10, 13, structureboundingbox);
			this.setBlockState(world, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
					10, -11, 13, structureboundingbox);

			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
			for (int m = 0; m < facings.length; ++m) {
				EnumFacing enumfacing = facings[m];
				if (!this.field_74940_h[enumfacing.getHorizontalIndex()]) {
					int k1 = enumfacing.getFrontOffsetX() * 2;
					int l1 = enumfacing.getFrontOffsetZ() * 2;
					this.field_74940_h[enumfacing.getHorizontalIndex()] = this.generateChestContents(world,
							structureboundingbox, random, 10 + k1, -11, 10 + l1,
							WeightedRandomChestContent.func_177629_a(itemsToGenerateInTemple,
									new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(random) }),
							2 + random.nextInt(5));
				}
			}

			return true;
		}
	}

	abstract static class Feature extends StructureComponent {
		protected int scatteredFeatureSizeX;
		protected int scatteredFeatureSizeY;
		protected int scatteredFeatureSizeZ;
		protected int field_74936_d = -1;

		public Feature() {
		}

		protected Feature(EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, int parInt4, int parInt5,
				int parInt6) {
			super(0);
			this.scatteredFeatureSizeX = parInt4;
			this.scatteredFeatureSizeY = parInt5;
			this.scatteredFeatureSizeZ = parInt6;
			this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(parRandom);
			switch (this.coordBaseMode) {
			case NORTH:
			case SOUTH:
				this.boundingBox = new StructureBoundingBox(parInt1, parInt2, parInt3, parInt1 + parInt4 - 1,
						parInt2 + parInt5 - 1, parInt3 + parInt6 - 1);
				break;
			default:
				this.boundingBox = new StructureBoundingBox(parInt1, parInt2, parInt3, parInt1 + parInt6 - 1,
						parInt2 + parInt5 - 1, parInt3 + parInt4 - 1);
			}

		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			nbttagcompound.setInteger("Width", this.scatteredFeatureSizeX);
			nbttagcompound.setInteger("Height", this.scatteredFeatureSizeY);
			nbttagcompound.setInteger("Depth", this.scatteredFeatureSizeZ);
			nbttagcompound.setInteger("HPos", this.field_74936_d);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			this.scatteredFeatureSizeX = nbttagcompound.getInteger("Width");
			this.scatteredFeatureSizeY = nbttagcompound.getInteger("Height");
			this.scatteredFeatureSizeZ = nbttagcompound.getInteger("Depth");
			this.field_74936_d = nbttagcompound.getInteger("HPos");
		}

		protected boolean func_74935_a(World worldIn, StructureBoundingBox parStructureBoundingBox, int parInt1) {
			if (this.field_74936_d >= 0) {
				return true;
			} else {
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
					return false;
				} else {
					this.field_74936_d = i / j;
					this.boundingBox.offset(0, this.field_74936_d - this.boundingBox.minY + parInt1, 0);
					return true;
				}
			}
		}
	}

	public static class JunglePyramid extends ComponentScatteredFeaturePieces.Feature {
		private boolean field_74947_h;
		private boolean field_74948_i;
		private boolean field_74945_j;
		private boolean field_74946_k;
		private static final List<WeightedRandomChestContent> field_175816_i = Lists.newArrayList(
				new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3),
						new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
						new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15),
						new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2),
						new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20),
						new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16),
						new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
						new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1),
						new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1),
						new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });
		private static final List<WeightedRandomChestContent> field_175815_j = Lists.newArrayList(
				new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.arrow, 0, 2, 7, 30) });
		private static ComponentScatteredFeaturePieces.JunglePyramid.Stones junglePyramidsRandomScatteredStones = new ComponentScatteredFeaturePieces.JunglePyramid.Stones();

		public JunglePyramid() {
		}

		public JunglePyramid(EaglercraftRandom parRandom, int parInt1, int parInt2) {
			super(parRandom, parInt1, 64, parInt2, 12, 10, 15);
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("placedMainChest", this.field_74947_h);
			nbttagcompound.setBoolean("placedHiddenChest", this.field_74948_i);
			nbttagcompound.setBoolean("placedTrap1", this.field_74945_j);
			nbttagcompound.setBoolean("placedTrap2", this.field_74946_k);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.field_74947_h = nbttagcompound.getBoolean("placedMainChest");
			this.field_74948_i = nbttagcompound.getBoolean("placedHiddenChest");
			this.field_74945_j = nbttagcompound.getBoolean("placedTrap1");
			this.field_74946_k = nbttagcompound.getBoolean("placedTrap2");
		}

		public boolean addComponentParts(World world, EaglercraftRandom random,
				StructureBoundingBox structureboundingbox) {
			if (!this.func_74935_a(world, structureboundingbox, 0)) {
				return false;
			} else {
				int i = this.getMetadataWithOffset(Blocks.stone_stairs, 3);
				int j = this.getMetadataWithOffset(Blocks.stone_stairs, 2);
				int k = this.getMetadataWithOffset(Blocks.stone_stairs, 0);
				int l = this.getMetadataWithOffset(Blocks.stone_stairs, 1);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0,
						this.scatteredFeatureSizeZ - 1, false, random, junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 2, 1, 2, 9, 2, 2, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 2, 1, 12, 9, 2, 12, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 2, 1, 3, 2, 2, 11, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 9, 1, 3, 9, 2, 11, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 1, 3, 1, 10, 6, 1, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 1, 3, 13, 10, 6, 13, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 1, 3, 2, 1, 6, 12, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 10, 3, 2, 10, 6, 12, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 2, 3, 2, 9, 3, 12, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 2, 6, 2, 9, 6, 12, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 3, 7, 3, 8, 7, 11, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 8, 4, 7, 8, 10, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithAir(world, structureboundingbox, 3, 1, 3, 8, 2, 11);
				this.fillWithAir(world, structureboundingbox, 4, 3, 6, 7, 3, 9);
				this.fillWithAir(world, structureboundingbox, 2, 4, 2, 9, 5, 12);
				this.fillWithAir(world, structureboundingbox, 4, 6, 5, 7, 6, 9);
				this.fillWithAir(world, structureboundingbox, 5, 7, 6, 6, 7, 8);
				this.fillWithAir(world, structureboundingbox, 5, 1, 2, 6, 2, 2);
				this.fillWithAir(world, structureboundingbox, 5, 2, 12, 6, 2, 12);
				this.fillWithAir(world, structureboundingbox, 5, 5, 1, 6, 5, 1);
				this.fillWithAir(world, structureboundingbox, 5, 5, 13, 6, 5, 13);
				this.setBlockState(world, Blocks.air.getDefaultState(), 1, 5, 5, structureboundingbox);
				this.setBlockState(world, Blocks.air.getDefaultState(), 10, 5, 5, structureboundingbox);
				this.setBlockState(world, Blocks.air.getDefaultState(), 1, 5, 9, structureboundingbox);
				this.setBlockState(world, Blocks.air.getDefaultState(), 10, 5, 9, structureboundingbox);

				for (int i1 = 0; i1 <= 14; i1 += 14) {
					this.fillWithRandomizedBlocks(world, structureboundingbox, 2, 4, i1, 2, 5, i1, false, random,
							junglePyramidsRandomScatteredStones);
					this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 4, i1, 4, 5, i1, false, random,
							junglePyramidsRandomScatteredStones);
					this.fillWithRandomizedBlocks(world, structureboundingbox, 7, 4, i1, 7, 5, i1, false, random,
							junglePyramidsRandomScatteredStones);
					this.fillWithRandomizedBlocks(world, structureboundingbox, 9, 4, i1, 9, 5, i1, false, random,
							junglePyramidsRandomScatteredStones);
				}

				this.fillWithRandomizedBlocks(world, structureboundingbox, 5, 6, 0, 6, 6, 0, false, random,
						junglePyramidsRandomScatteredStones);

				for (int k1 = 0; k1 <= 11; k1 += 11) {
					for (int j1 = 2; j1 <= 12; j1 += 2) {
						this.fillWithRandomizedBlocks(world, structureboundingbox, k1, 4, j1, k1, 5, j1, false, random,
								junglePyramidsRandomScatteredStones);
					}

					this.fillWithRandomizedBlocks(world, structureboundingbox, k1, 6, 5, k1, 6, 5, false, random,
							junglePyramidsRandomScatteredStones);
					this.fillWithRandomizedBlocks(world, structureboundingbox, k1, 6, 9, k1, 6, 9, false, random,
							junglePyramidsRandomScatteredStones);
				}

				this.fillWithRandomizedBlocks(world, structureboundingbox, 2, 7, 2, 2, 9, 2, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 9, 7, 2, 9, 9, 2, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 2, 7, 12, 2, 9, 12, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 9, 7, 12, 9, 9, 12, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 9, 4, 4, 9, 4, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 7, 9, 4, 7, 9, 4, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 9, 10, 4, 9, 10, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 7, 9, 10, 7, 9, 10, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 5, 9, 7, 6, 9, 7, false, random,
						junglePyramidsRandomScatteredStones);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 5, 9, 6, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 6, 9, 6, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(j), 5, 9, 8, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(j), 6, 9, 8, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 4, 0, 0, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 5, 0, 0, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 6, 0, 0, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 7, 0, 0, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 4, 1, 8, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 4, 2, 9, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 4, 3, 10, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 7, 1, 8, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 7, 2, 9, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(i), 7, 3, 10, structureboundingbox);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 9, 4, 1, 9, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 7, 1, 9, 7, 1, 9, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 10, 7, 2, 10, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 5, 4, 5, 6, 4, 5, false, random,
						junglePyramidsRandomScatteredStones);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(k), 4, 4, 5, structureboundingbox);
				this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(l), 7, 4, 5, structureboundingbox);

				for (int l1 = 0; l1 < 4; ++l1) {
					this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(j), 5, 0 - l1, 6 + l1,
							structureboundingbox);
					this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(j), 6, 0 - l1, 6 + l1,
							structureboundingbox);
					this.fillWithAir(world, structureboundingbox, 5, 0 - l1, 7 + l1, 6, 0 - l1, 9 + l1);
				}

				this.fillWithAir(world, structureboundingbox, 1, -3, 12, 10, -1, 13);
				this.fillWithAir(world, structureboundingbox, 1, -3, 1, 3, -1, 13);
				this.fillWithAir(world, structureboundingbox, 1, -3, 1, 9, -1, 5);

				for (int i2 = 1; i2 <= 13; i2 += 2) {
					this.fillWithRandomizedBlocks(world, structureboundingbox, 1, -3, i2, 1, -2, i2, false, random,
							junglePyramidsRandomScatteredStones);
				}

				for (int j2 = 2; j2 <= 12; j2 += 2) {
					this.fillWithRandomizedBlocks(world, structureboundingbox, 1, -1, j2, 3, -1, j2, false, random,
							junglePyramidsRandomScatteredStones);
				}

				this.fillWithRandomizedBlocks(world, structureboundingbox, 2, -2, 1, 5, -2, 1, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 7, -2, 1, 9, -2, 1, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 6, -3, 1, 6, -3, 1, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 6, -1, 1, 6, -1, 1, false, random,
						junglePyramidsRandomScatteredStones);
				this.setBlockState(world,
						Blocks.tripwire_hook.getStateFromMeta(
								this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.EAST.getHorizontalIndex()))
								.withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)),
						1, -3, 8, structureboundingbox);
				this.setBlockState(world,
						Blocks.tripwire_hook.getStateFromMeta(
								this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.WEST.getHorizontalIndex()))
								.withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)),
						4, -3, 8, structureboundingbox);
				this.setBlockState(world,
						Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)),
						2, -3, 8, structureboundingbox);
				this.setBlockState(world,
						Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)),
						3, -3, 8, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 7, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 6, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 5, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 4, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 3, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 2, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 5, -3, 1, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 4, -3, 1, structureboundingbox);
				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 3, -3, 1, structureboundingbox);
				if (!this.field_74945_j) {
					this.field_74945_j = this.generateDispenserContents(world, structureboundingbox, random, 3, -2, 1,
							EnumFacing.NORTH.getIndex(), field_175815_j, 2);
				}

				this.setBlockState(world, Blocks.vine.getStateFromMeta(15), 3, -2, 2, structureboundingbox);
				this.setBlockState(world,
						Blocks.tripwire_hook.getStateFromMeta(
								this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.NORTH.getHorizontalIndex()))
								.withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)),
						7, -3, 1, structureboundingbox);
				this.setBlockState(world,
						Blocks.tripwire_hook.getStateFromMeta(
								this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.SOUTH.getHorizontalIndex()))
								.withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)),
						7, -3, 5, structureboundingbox);
				this.setBlockState(world,
						Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)),
						7, -3, 2, structureboundingbox);
				this.setBlockState(world,
						Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)),
						7, -3, 3, structureboundingbox);
				this.setBlockState(world,
						Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)),
						7, -3, 4, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 8, -3, 6, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 9, -3, 6, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 9, -3, 5, structureboundingbox);
				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 4, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 9, -2, 4, structureboundingbox);
				if (!this.field_74946_k) {
					this.field_74946_k = this.generateDispenserContents(world, structureboundingbox, random, 9, -2, 3,
							EnumFacing.WEST.getIndex(), field_175815_j, 2);
				}

				this.setBlockState(world, Blocks.vine.getStateFromMeta(15), 8, -1, 3, structureboundingbox);
				this.setBlockState(world, Blocks.vine.getStateFromMeta(15), 8, -2, 3, structureboundingbox);
				if (!this.field_74947_h) {
					this.field_74947_h = this.generateChestContents(world, structureboundingbox, random, 8, -3, 3,
							WeightedRandomChestContent.func_177629_a(field_175816_i,
									new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(random) }),
							2 + random.nextInt(5));
				}

				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 2, structureboundingbox);
				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 1, structureboundingbox);
				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 4, -3, 5, structureboundingbox);
				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 5, -2, 5, structureboundingbox);
				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 5, -1, 5, structureboundingbox);
				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 6, -3, 5, structureboundingbox);
				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 7, -2, 5, structureboundingbox);
				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 7, -1, 5, structureboundingbox);
				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 5, structureboundingbox);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 9, -1, 1, 9, -1, 5, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithAir(world, structureboundingbox, 8, -3, 8, 10, -1, 10);
				this.setBlockState(world, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 8, -2, 11,
						structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 9, -2, 11,
						structureboundingbox);
				this.setBlockState(world, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 10, -2, 11,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing
								.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))),
						8, -2, 12, structureboundingbox);
				this.setBlockState(world,
						Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing
								.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))),
						9, -2, 12, structureboundingbox);
				this.setBlockState(world,
						Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing
								.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))),
						10, -2, 12, structureboundingbox);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 8, -3, 8, 8, -3, 10, false, random,
						junglePyramidsRandomScatteredStones);
				this.fillWithRandomizedBlocks(world, structureboundingbox, 10, -3, 8, 10, -3, 10, false, random,
						junglePyramidsRandomScatteredStones);
				this.setBlockState(world, Blocks.mossy_cobblestone.getDefaultState(), 10, -2, 9, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 8, -2, 9, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 8, -2, 10, structureboundingbox);
				this.setBlockState(world, Blocks.redstone_wire.getDefaultState(), 10, -1, 9, structureboundingbox);
				this.setBlockState(world, Blocks.sticky_piston.getStateFromMeta(EnumFacing.UP.getIndex()), 9, -2, 8,
						structureboundingbox);
				this.setBlockState(world,
						Blocks.sticky_piston.getStateFromMeta(
								this.getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())),
						10, -2, 8, structureboundingbox);
				this.setBlockState(world,
						Blocks.sticky_piston.getStateFromMeta(
								this.getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())),
						10, -1, 8, structureboundingbox);
				this.setBlockState(world, Blocks.unpowered_repeater.getStateFromMeta(
						this.getMetadataWithOffset(Blocks.unpowered_repeater, EnumFacing.NORTH.getHorizontalIndex())),
						10, -2, 10, structureboundingbox);
				if (!this.field_74948_i) {
					this.field_74948_i = this.generateChestContents(world, structureboundingbox, random, 9, -3, 10,
							WeightedRandomChestContent.func_177629_a(field_175816_i,
									new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(random) }),
							2 + random.nextInt(5));
				}

				return true;
			}
		}

		static class Stones extends StructureComponent.BlockSelector {
			private Stones() {
			}

			public void selectBlocks(EaglercraftRandom rand, int x, int y, int z, boolean parFlag) {
				if (rand.nextFloat() < 0.4F) {
					this.blockstate = Blocks.cobblestone.getDefaultState();
				} else {
					this.blockstate = Blocks.mossy_cobblestone.getDefaultState();
				}

			}
		}
	}

	public static class SwampHut extends ComponentScatteredFeaturePieces.Feature {
		private boolean hasWitch;

		public SwampHut() {
		}

		public SwampHut(EaglercraftRandom parRandom, int parInt1, int parInt2) {
			super(parRandom, parInt1, 64, parInt2, 7, 7, 9);
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			super.writeStructureToNBT(nbttagcompound);
			nbttagcompound.setBoolean("Witch", this.hasWitch);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			super.readStructureFromNBT(nbttagcompound);
			this.hasWitch = nbttagcompound.getBoolean("Witch");
		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			if (!this.func_74935_a(world, structureboundingbox, 0)) {
				return false;
			} else {
				this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 5, 1, 7,
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
				this.fillWithBlocks(world, structureboundingbox, 1, 4, 2, 5, 4, 7,
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
				this.fillWithBlocks(world, structureboundingbox, 2, 1, 0, 4, 1, 0,
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
				this.fillWithBlocks(world, structureboundingbox, 2, 2, 2, 3, 3, 2,
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
				this.fillWithBlocks(world, structureboundingbox, 1, 2, 3, 1, 3, 6,
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
				this.fillWithBlocks(world, structureboundingbox, 5, 2, 3, 5, 3, 6,
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
				this.fillWithBlocks(world, structureboundingbox, 2, 2, 7, 4, 3, 7,
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
						Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
				this.fillWithBlocks(world, structureboundingbox, 1, 0, 2, 1, 3, 2, Blocks.log.getDefaultState(),
						Blocks.log.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 5, 0, 2, 5, 3, 2, Blocks.log.getDefaultState(),
						Blocks.log.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 1, 0, 7, 1, 3, 7, Blocks.log.getDefaultState(),
						Blocks.log.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 5, 0, 7, 5, 3, 7, Blocks.log.getDefaultState(),
						Blocks.log.getDefaultState(), false);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 2, 3, 2, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 3, 3, 7, structureboundingbox);
				this.setBlockState(world, Blocks.air.getDefaultState(), 1, 3, 4, structureboundingbox);
				this.setBlockState(world, Blocks.air.getDefaultState(), 5, 3, 4, structureboundingbox);
				this.setBlockState(world, Blocks.air.getDefaultState(), 5, 3, 5, structureboundingbox);
				this.setBlockState(world, Blocks.flower_pot.getDefaultState().withProperty(BlockFlowerPot.CONTENTS,
						BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 1, 3, 5, structureboundingbox);
				this.setBlockState(world, Blocks.crafting_table.getDefaultState(), 3, 2, 6, structureboundingbox);
				this.setBlockState(world, Blocks.cauldron.getDefaultState(), 4, 2, 6, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 1, 2, 1, structureboundingbox);
				this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 5, 2, 1, structureboundingbox);
				int i = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
				int j = this.getMetadataWithOffset(Blocks.oak_stairs, 1);
				int k = this.getMetadataWithOffset(Blocks.oak_stairs, 0);
				int l = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
				this.fillWithBlocks(world, structureboundingbox, 0, 4, 1, 6, 4, 1,
						Blocks.spruce_stairs.getStateFromMeta(i), Blocks.spruce_stairs.getStateFromMeta(i), false);
				this.fillWithBlocks(world, structureboundingbox, 0, 4, 2, 0, 4, 7,
						Blocks.spruce_stairs.getStateFromMeta(k), Blocks.spruce_stairs.getStateFromMeta(k), false);
				this.fillWithBlocks(world, structureboundingbox, 6, 4, 2, 6, 4, 7,
						Blocks.spruce_stairs.getStateFromMeta(j), Blocks.spruce_stairs.getStateFromMeta(j), false);
				this.fillWithBlocks(world, structureboundingbox, 0, 4, 8, 6, 4, 8,
						Blocks.spruce_stairs.getStateFromMeta(l), Blocks.spruce_stairs.getStateFromMeta(l), false);

				for (int i1 = 2; i1 <= 7; i1 += 5) {
					for (int j1 = 1; j1 <= 5; j1 += 4) {
						this.replaceAirAndLiquidDownwards(world, Blocks.log.getDefaultState(), j1, -1, i1,
								structureboundingbox);
					}
				}

				if (!this.hasWitch) {
					int l1 = this.getXWithOffset(2, 5);
					int i2 = this.getYWithOffset(2);
					int k1 = this.getZWithOffset(2, 5);
					if (structureboundingbox.isVecInside(new BlockPos(l1, i2, k1))) {
						this.hasWitch = true;
						EntityWitch entitywitch = new EntityWitch(world);
						entitywitch.setLocationAndAngles((double) l1 + 0.5D, (double) i2, (double) k1 + 0.5D, 0.0F,
								0.0F);
						entitywitch.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(l1, i2, k1)),
								(IEntityLivingData) null);
						world.spawnEntityInWorld(entitywitch);
					}
				}

				return true;
			}
		}
	}
}