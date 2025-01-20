package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
public class StructureMineshaftPieces {
	private static final List<WeightedRandomChestContent> CHEST_CONTENT_WEIGHT_LIST = Lists.newArrayList(
			new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
					new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5),
					new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5),
					new WeightedRandomChestContent(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), 4, 9, 5),
					new WeightedRandomChestContent(Items.diamond, 0, 1, 2, 3),
					new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10),
					new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15),
					new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1),
					new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.rail), 0, 4, 8, 1),
					new WeightedRandomChestContent(Items.melon_seeds, 0, 2, 4, 10),
					new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 2, 4, 10),
					new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
					new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1) });

	public static void registerStructurePieces() {
		MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Corridor.class,
				StructureMineshaftPieces.Corridor::new, "MSCorridor");
		MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Cross.class,
				StructureMineshaftPieces.Cross::new, "MSCrossing");
		MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Room.class,
				StructureMineshaftPieces.Room::new, "MSRoom");
		MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Stairs.class,
				StructureMineshaftPieces.Stairs::new, "MSStairs");
	}

	private static StructureComponent func_175892_a(List<StructureComponent> listIn, EaglercraftRandom rand, int x,
			int y, int z, EnumFacing facing, int type) {
		int i = rand.nextInt(100);
		if (i >= 80) {
			StructureBoundingBox structureboundingbox = StructureMineshaftPieces.Cross.func_175813_a(listIn, rand, x, y,
					z, facing);
			if (structureboundingbox != null) {
				return new StructureMineshaftPieces.Cross(type, rand, structureboundingbox, facing);
			}
		} else if (i >= 70) {
			StructureBoundingBox structureboundingbox1 = StructureMineshaftPieces.Stairs.func_175812_a(listIn, rand, x,
					y, z, facing);
			if (structureboundingbox1 != null) {
				return new StructureMineshaftPieces.Stairs(type, rand, structureboundingbox1, facing);
			}
		} else {
			StructureBoundingBox structureboundingbox2 = StructureMineshaftPieces.Corridor.func_175814_a(listIn, rand,
					x, y, z, facing);
			if (structureboundingbox2 != null) {
				return new StructureMineshaftPieces.Corridor(type, rand, structureboundingbox2, facing);
			}
		}

		return null;
	}

	private static StructureComponent func_175890_b(StructureComponent componentIn, List<StructureComponent> listIn,
			EaglercraftRandom rand, int x, int y, int z, EnumFacing facing, int type) {
		if (type > 8) {
			return null;
		} else if (Math.abs(x - componentIn.getBoundingBox().minX) <= 80
				&& Math.abs(z - componentIn.getBoundingBox().minZ) <= 80) {
			StructureComponent structurecomponent = func_175892_a(listIn, rand, x, y, z, facing, type + 1);
			if (structurecomponent != null) {
				listIn.add(structurecomponent);
				structurecomponent.buildComponent(componentIn, listIn, rand);
			}

			return structurecomponent;
		} else {
			return null;
		}
	}

	public static class Corridor extends StructureComponent {
		private boolean hasRails;
		private boolean hasSpiders;
		private boolean spawnerPlaced;
		private int sectionCount;

		public Corridor() {
		}

		protected void writeStructureToNBT(NBTTagCompound tagCompound) {
			tagCompound.setBoolean("hr", this.hasRails);
			tagCompound.setBoolean("sc", this.hasSpiders);
			tagCompound.setBoolean("hps", this.spawnerPlaced);
			tagCompound.setInteger("Num", this.sectionCount);
		}

		protected void readStructureFromNBT(NBTTagCompound tagCompound) {
			this.hasRails = tagCompound.getBoolean("hr");
			this.hasSpiders = tagCompound.getBoolean("sc");
			this.spawnerPlaced = tagCompound.getBoolean("hps");
			this.sectionCount = tagCompound.getInteger("Num");
		}

		public Corridor(int type, EaglercraftRandom rand, StructureBoundingBox structurebb, EnumFacing facing) {
			super(type);
			this.coordBaseMode = facing;
			this.boundingBox = structurebb;
			this.hasRails = rand.nextInt(3) == 0;
			this.hasSpiders = !this.hasRails && rand.nextInt(23) == 0;
			if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH) {
				this.sectionCount = structurebb.getXSize() / 5;
			} else {
				this.sectionCount = structurebb.getZSize() / 5;
			}

		}

		public static StructureBoundingBox func_175814_a(List<StructureComponent> rand, EaglercraftRandom x, int y,
				int z, int facing, EnumFacing parEnumFacing) {
			StructureBoundingBox structureboundingbox = new StructureBoundingBox(y, z, facing, y, z + 2, facing);

			int i;
			for (i = x.nextInt(3) + 2; i > 0; --i) {
				int j = i * 5;
				switch (parEnumFacing) {
				case NORTH:
					structureboundingbox.maxX = y + 2;
					structureboundingbox.minZ = facing - (j - 1);
					break;
				case SOUTH:
					structureboundingbox.maxX = y + 2;
					structureboundingbox.maxZ = facing + (j - 1);
					break;
				case WEST:
					structureboundingbox.minX = y - (j - 1);
					structureboundingbox.maxZ = facing + 2;
					break;
				case EAST:
					structureboundingbox.maxX = y + (j - 1);
					structureboundingbox.maxZ = facing + 2;
				}

				if (StructureComponent.findIntersecting(rand, structureboundingbox) == null) {
					break;
				}
			}

			return i > 0 ? structureboundingbox : null;
		}

		public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn,
				EaglercraftRandom rand) {
			int i = this.getComponentType();
			int j = rand.nextInt(4);
			if (this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					if (j <= 1) {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1,
								this.coordBaseMode, i);
					} else if (j == 2) {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, i);
					} else {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, i);
					}
					break;
				case SOUTH:
					if (j <= 1) {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1,
								this.coordBaseMode, i);
					} else if (j == 2) {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST,
								i);
					} else {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST,
								i);
					}
					break;
				case WEST:
					if (j <= 1) {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, this.coordBaseMode,
								i);
					} else if (j == 2) {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1,
								EnumFacing.NORTH, i);
					} else {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1,
								EnumFacing.SOUTH, i);
					}
					break;
				case EAST:
					if (j <= 1) {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, this.coordBaseMode,
								i);
					} else if (j == 2) {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX - 3,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1,
								EnumFacing.NORTH, i);
					} else {
						StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX - 3,
								this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1,
								EnumFacing.SOUTH, i);
					}
				}
			}

			if (i < 8) {
				if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH) {
					for (int i1 = this.boundingBox.minX + 3; i1 + 3 <= this.boundingBox.maxX; i1 += 5) {
						int j1 = rand.nextInt(5);
						if (j1 == 0) {
							StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, i1, this.boundingBox.minY,
									this.boundingBox.minZ - 1, EnumFacing.NORTH, i + 1);
						} else if (j1 == 1) {
							StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, i1, this.boundingBox.minY,
									this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i + 1);
						}
					}
				} else {
					for (int k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5) {
						int l = rand.nextInt(5);
						if (l == 0) {
							StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1,
									this.boundingBox.minY, k, EnumFacing.WEST, i + 1);
						} else if (l == 1) {
							StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1,
									this.boundingBox.minY, k, EnumFacing.EAST, i + 1);
						}
					}
				}
			}

		}

		protected boolean generateChestContents(World worldIn, StructureBoundingBox boundingBoxIn,
				EaglercraftRandom rand, int x, int y, int z, List<WeightedRandomChestContent> listIn, int max) {
			BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y),
					this.getZWithOffset(x, z));
			if (boundingBoxIn.isVecInside(blockpos)
					&& worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air) {
				int i = rand.nextBoolean() ? 1 : 0;
				worldIn.setBlockState(blockpos,
						Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, i)), 2);
				EntityMinecartChest entityminecartchest = new EntityMinecartChest(worldIn,
						(double) ((float) blockpos.getX() + 0.5F), (double) ((float) blockpos.getY() + 0.5F),
						(double) ((float) blockpos.getZ() + 0.5F));
				WeightedRandomChestContent.generateChestContents(rand, listIn, entityminecartchest, max);
				worldIn.spawnEntityInWorld(entityminecartchest);
				return true;
			} else {
				return false;
			}
		}

		public boolean addComponentParts(World worldIn, EaglercraftRandom randomIn,
				StructureBoundingBox structureBoundingBoxIn) {
			if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
				return false;
			} else {
				boolean flag = false;
				boolean flag1 = true;
				boolean flag2 = false;
				boolean flag3 = true;
				int i = this.sectionCount * 5 - 1;
				this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 1, i, Blocks.air.getDefaultState(),
						Blocks.air.getDefaultState(), false);
				this.func_175805_a(worldIn, structureBoundingBoxIn, randomIn, 0.8F, 0, 2, 0, 2, 2, i,
						Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
				if (this.hasSpiders) {
					this.func_175805_a(worldIn, structureBoundingBoxIn, randomIn, 0.6F, 0, 0, 0, 2, 1, i,
							Blocks.web.getDefaultState(), Blocks.air.getDefaultState(), false);
				}

				for (int j = 0; j < this.sectionCount; ++j) {
					int k = 2 + j * 5;
					this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, k, 0, 1, k,
							Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
					this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, k, 2, 1, k,
							Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
					if (randomIn.nextInt(4) == 0) {
						this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, k, 0, 2, k,
								Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
						this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, k, 2, 2, k,
								Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
					} else {
						this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, k, 2, 2, k,
								Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
					}

					this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 0, 2, k - 1,
							Blocks.web.getDefaultState());
					this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 2, 2, k - 1,
							Blocks.web.getDefaultState());
					this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 0, 2, k + 1,
							Blocks.web.getDefaultState());
					this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 2, 2, k + 1,
							Blocks.web.getDefaultState());
					this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 0, 2, k - 2,
							Blocks.web.getDefaultState());
					this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 2, 2, k - 2,
							Blocks.web.getDefaultState());
					this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 0, 2, k + 2,
							Blocks.web.getDefaultState());
					this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 2, 2, k + 2,
							Blocks.web.getDefaultState());
					this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 1, 2, k - 1,
							Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
					this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 1, 2, k + 1,
							Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
					if (randomIn.nextInt(100) == 0) {
						this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 2, 0, k - 1,
								WeightedRandomChestContent.func_177629_a(
										StructureMineshaftPieces.CHEST_CONTENT_WEIGHT_LIST,
										new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn) }),
								3 + randomIn.nextInt(4));
					}

					if (randomIn.nextInt(100) == 0) {
						this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 0, 0, k + 1,
								WeightedRandomChestContent.func_177629_a(
										StructureMineshaftPieces.CHEST_CONTENT_WEIGHT_LIST,
										new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn) }),
								3 + randomIn.nextInt(4));
					}

					if (this.hasSpiders && !this.spawnerPlaced) {
						int l = this.getYWithOffset(0);
						int i1 = k - 1 + randomIn.nextInt(3);
						int j1 = this.getXWithOffset(1, i1);
						i1 = this.getZWithOffset(1, i1);
						BlockPos blockpos = new BlockPos(j1, l, i1);
						if (structureBoundingBoxIn.isVecInside(blockpos)) {
							this.spawnerPlaced = true;
							worldIn.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
							TileEntity tileentity = worldIn.getTileEntity(blockpos);
							if (tileentity instanceof TileEntityMobSpawner) {
								((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setEntityName("CaveSpider");
							}
						}
					}
				}

				for (int k1 = 0; k1 <= 2; ++k1) {
					for (int i2 = 0; i2 <= i; ++i2) {
						byte b0 = -1;
						IBlockState iblockstate1 = this.getBlockStateFromPos(worldIn, k1, b0, i2,
								structureBoundingBoxIn);
						if (iblockstate1.getBlock().getMaterial() == Material.air) {
							byte b1 = -1;
							this.setBlockState(worldIn, Blocks.planks.getDefaultState(), k1, b1, i2,
									structureBoundingBoxIn);
						}
					}
				}

				if (this.hasRails) {
					for (int l1 = 0; l1 <= i; ++l1) {
						IBlockState iblockstate = this.getBlockStateFromPos(worldIn, 1, -1, l1, structureBoundingBoxIn);
						if (iblockstate.getBlock().getMaterial() != Material.air
								&& iblockstate.getBlock().isFullBlock()) {
							this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.7F, 1, 0, l1,
									Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, 0)));
						}
					}
				}

				return true;
			}
		}
	}

	public static class Cross extends StructureComponent {
		private EnumFacing corridorDirection;
		private boolean isMultipleFloors;

		public Cross() {
		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			nbttagcompound.setBoolean("tf", this.isMultipleFloors);
			nbttagcompound.setInteger("D", this.corridorDirection.getHorizontalIndex());
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			this.isMultipleFloors = nbttagcompound.getBoolean("tf");
			this.corridorDirection = EnumFacing.getHorizontal(nbttagcompound.getInteger("D"));
		}

		public Cross(int type, EaglercraftRandom rand, StructureBoundingBox structurebb, EnumFacing facing) {
			super(type);
			this.corridorDirection = facing;
			this.boundingBox = structurebb;
			this.isMultipleFloors = structurebb.getYSize() > 3;
		}

		public static StructureBoundingBox func_175813_a(List<StructureComponent> listIn, EaglercraftRandom rand, int x,
				int y, int z, EnumFacing facing) {
			StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y, z, x, y + 2, z);
			if (rand.nextInt(4) == 0) {
				structureboundingbox.maxY += 4;
			}

			switch (facing) {
			case NORTH:
				structureboundingbox.minX = x - 1;
				structureboundingbox.maxX = x + 3;
				structureboundingbox.minZ = z - 4;
				break;
			case SOUTH:
				structureboundingbox.minX = x - 1;
				structureboundingbox.maxX = x + 3;
				structureboundingbox.maxZ = z + 4;
				break;
			case WEST:
				structureboundingbox.minX = x - 4;
				structureboundingbox.minZ = z - 1;
				structureboundingbox.maxZ = z + 3;
				break;
			case EAST:
				structureboundingbox.maxX = x + 4;
				structureboundingbox.minZ = z - 1;
				structureboundingbox.maxZ = z + 3;
			}

			return StructureComponent.findIntersecting(listIn, structureboundingbox) != null ? null
					: structureboundingbox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			int i = this.getComponentType();
			switch (this.corridorDirection) {
			case NORTH:
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX + 1,
						this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX - 1,
						this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.maxX + 1,
						this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
				break;
			case SOUTH:
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX + 1,
						this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX - 1,
						this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.maxX + 1,
						this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
				break;
			case WEST:
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX + 1,
						this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX + 1,
						this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX - 1,
						this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
				break;
			case EAST:
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX + 1,
						this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX + 1,
						this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.maxX + 1,
						this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
			}

			if (this.isMultipleFloors) {
				if (random.nextBoolean()) {
					StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX + 1,
							this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				}

				if (random.nextBoolean()) {
					StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX - 1,
							this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
				}

				if (random.nextBoolean()) {
					StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.maxX + 1,
							this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
				}

				if (random.nextBoolean()) {
					StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX + 1,
							this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				}
			}

		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				if (this.isMultipleFloors) {
					this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX + 1, this.boundingBox.minY,
							this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1,
							this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
					this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX, this.boundingBox.minY,
							this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1,
							this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(),
							false);
					this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX + 1,
							this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1,
							this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
					this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX, this.boundingBox.maxY - 2,
							this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY,
							this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(),
							false);
					this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX + 1,
							this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1,
							this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				} else {
					this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX + 1, this.boundingBox.minY,
							this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY,
							this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
					this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX, this.boundingBox.minY,
							this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY,
							this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(),
							false);
				}

				this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX + 1, this.boundingBox.minY,
						this.boundingBox.minZ + 1, this.boundingBox.minX + 1, this.boundingBox.maxY,
						this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(),
						false);
				this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX + 1, this.boundingBox.minY,
						this.boundingBox.maxZ - 1, this.boundingBox.minX + 1, this.boundingBox.maxY,
						this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(),
						false);
				this.fillWithBlocks(world, structureboundingbox, this.boundingBox.maxX - 1, this.boundingBox.minY,
						this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY,
						this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(),
						false);
				this.fillWithBlocks(world, structureboundingbox, this.boundingBox.maxX - 1, this.boundingBox.minY,
						this.boundingBox.maxZ - 1, this.boundingBox.maxX - 1, this.boundingBox.maxY,
						this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(),
						false);

				for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
					for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
						if (this.getBlockStateFromPos(world, i, this.boundingBox.minY - 1, j, structureboundingbox)
								.getBlock().getMaterial() == Material.air) {
							this.setBlockState(world, Blocks.planks.getDefaultState(), i, this.boundingBox.minY - 1, j,
									structureboundingbox);
						}
					}
				}

				return true;
			}
		}
	}

	public static class Room extends StructureComponent {
		private List<StructureBoundingBox> roomsLinkedToTheRoom = Lists.newLinkedList();

		public Room() {
		}

		public Room(int type, EaglercraftRandom rand, int x, int z) {
			super(type);
			this.boundingBox = new StructureBoundingBox(x, 50, z, x + 7 + rand.nextInt(6), 54 + rand.nextInt(6),
					z + 7 + rand.nextInt(6));
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			int i = this.getComponentType();
			int j = this.boundingBox.getYSize() - 3 - 1;
			if (j <= 0) {
				j = 1;
			}

			int k;
			for (k = 0; k < this.boundingBox.getXSize(); k = k + 4) {
				k = k + random.nextInt(this.boundingBox.getXSize());
				if (k + 3 > this.boundingBox.getXSize()) {
					break;
				}

				StructureComponent structurecomponent1 = StructureMineshaftPieces.func_175890_b(structurecomponent,
						list, random, this.boundingBox.minX + k, this.boundingBox.minY + random.nextInt(j) + 1,
						this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
				if (structurecomponent1 != null) {
					StructureBoundingBox structureboundingbox = structurecomponent1.getBoundingBox();
					this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox.minX,
							structureboundingbox.minY, this.boundingBox.minZ, structureboundingbox.maxX,
							structureboundingbox.maxY, this.boundingBox.minZ + 1));
				}
			}

			for (k = 0; k < this.boundingBox.getXSize(); k = k + 4) {
				k = k + random.nextInt(this.boundingBox.getXSize());
				if (k + 3 > this.boundingBox.getXSize()) {
					break;
				}

				StructureComponent structurecomponent2 = StructureMineshaftPieces.func_175890_b(structurecomponent,
						list, random, this.boundingBox.minX + k, this.boundingBox.minY + random.nextInt(j) + 1,
						this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
				if (structurecomponent2 != null) {
					StructureBoundingBox structureboundingbox1 = structurecomponent2.getBoundingBox();
					this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox1.minX,
							structureboundingbox1.minY, this.boundingBox.maxZ - 1, structureboundingbox1.maxX,
							structureboundingbox1.maxY, this.boundingBox.maxZ));
				}
			}

			for (k = 0; k < this.boundingBox.getZSize(); k = k + 4) {
				k = k + random.nextInt(this.boundingBox.getZSize());
				if (k + 3 > this.boundingBox.getZSize()) {
					break;
				}

				StructureComponent structurecomponent3 = StructureMineshaftPieces.func_175890_b(structurecomponent,
						list, random, this.boundingBox.minX - 1, this.boundingBox.minY + random.nextInt(j) + 1,
						this.boundingBox.minZ + k, EnumFacing.WEST, i);
				if (structurecomponent3 != null) {
					StructureBoundingBox structureboundingbox2 = structurecomponent3.getBoundingBox();
					this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.minX,
							structureboundingbox2.minY, structureboundingbox2.minZ, this.boundingBox.minX + 1,
							structureboundingbox2.maxY, structureboundingbox2.maxZ));
				}
			}

			for (k = 0; k < this.boundingBox.getZSize(); k = k + 4) {
				k = k + random.nextInt(this.boundingBox.getZSize());
				if (k + 3 > this.boundingBox.getZSize()) {
					break;
				}

				StructureComponent structurecomponent4 = StructureMineshaftPieces.func_175890_b(structurecomponent,
						list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + random.nextInt(j) + 1,
						this.boundingBox.minZ + k, EnumFacing.EAST, i);
				if (structurecomponent4 != null) {
					StructureBoundingBox structureboundingbox3 = structurecomponent4.getBoundingBox();
					this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.maxX - 1,
							structureboundingbox3.minY, structureboundingbox3.minZ, this.boundingBox.maxX,
							structureboundingbox3.maxY, structureboundingbox3.maxZ));
				}
			}

		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX, this.boundingBox.minY,
						this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ,
						Blocks.dirt.getDefaultState(), Blocks.air.getDefaultState(), true);
				this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX, this.boundingBox.minY + 1,
						this.boundingBox.minZ, this.boundingBox.maxX,
						Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ,
						Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);

				for (StructureBoundingBox structureboundingbox1 : this.roomsLinkedToTheRoom) {
					this.fillWithBlocks(world, structureboundingbox, structureboundingbox1.minX,
							structureboundingbox1.maxY - 2, structureboundingbox1.minZ, structureboundingbox1.maxX,
							structureboundingbox1.maxY, structureboundingbox1.maxZ, Blocks.air.getDefaultState(),
							Blocks.air.getDefaultState(), false);
				}

				this.randomlyRareFillWithBlocks(world, structureboundingbox, this.boundingBox.minX,
						this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY,
						this.boundingBox.maxZ, Blocks.air.getDefaultState(), false);
				return true;
			}
		}

		public void func_181138_a(int parInt1, int parInt2, int parInt3) {
			super.func_181138_a(parInt1, parInt2, parInt3);

			for (StructureBoundingBox structureboundingbox : this.roomsLinkedToTheRoom) {
				structureboundingbox.offset(parInt1, parInt2, parInt3);
			}

		}

		protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
			NBTTagList nbttaglist = new NBTTagList();

			for (StructureBoundingBox structureboundingbox : this.roomsLinkedToTheRoom) {
				nbttaglist.appendTag(structureboundingbox.toNBTTagIntArray());
			}

			nbttagcompound.setTag("Entrances", nbttaglist);
		}

		protected void readStructureFromNBT(NBTTagCompound nbttagcompound) {
			NBTTagList nbttaglist = nbttagcompound.getTagList("Entrances", 11);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				this.roomsLinkedToTheRoom.add(new StructureBoundingBox(nbttaglist.getIntArrayAt(i)));
			}

		}
	}

	public static class Stairs extends StructureComponent {
		public Stairs() {
		}

		public Stairs(int type, EaglercraftRandom rand, StructureBoundingBox structurebb, EnumFacing facing) {
			super(type);
			this.coordBaseMode = facing;
			this.boundingBox = structurebb;
		}

		protected void writeStructureToNBT(NBTTagCompound var1) {
		}

		protected void readStructureFromNBT(NBTTagCompound var1) {
		}

		public static StructureBoundingBox func_175812_a(List<StructureComponent> listIn, EaglercraftRandom rand, int x,
				int y, int z, EnumFacing facing) {
			StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y - 5, z, x, y + 2, z);
			switch (facing) {
			case NORTH:
				structureboundingbox.maxX = x + 2;
				structureboundingbox.minZ = z - 8;
				break;
			case SOUTH:
				structureboundingbox.maxX = x + 2;
				structureboundingbox.maxZ = z + 8;
				break;
			case WEST:
				structureboundingbox.minX = x - 8;
				structureboundingbox.maxZ = z + 2;
				break;
			case EAST:
				structureboundingbox.maxX = x + 8;
				structureboundingbox.maxZ = z + 2;
			}

			return StructureComponent.findIntersecting(listIn, structureboundingbox) != null ? null
					: structureboundingbox;
		}

		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list,
				EaglercraftRandom random) {
			int i = this.getComponentType();
			if (this.coordBaseMode != null) {
				switch (this.coordBaseMode) {
				case NORTH:
					StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX,
							this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
					break;
				case SOUTH:
					StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX,
							this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
					break;
				case WEST:
					StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.minX - 1,
							this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, i);
					break;
				case EAST:
					StructureMineshaftPieces.func_175890_b(structurecomponent, list, random, this.boundingBox.maxX + 1,
							this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, i);
				}
			}

		}

		public boolean addComponentParts(World world, EaglercraftRandom var2,
				StructureBoundingBox structureboundingbox) {
			if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
				return false;
			} else {
				this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 2, 7, 1, Blocks.air.getDefaultState(),
						Blocks.air.getDefaultState(), false);
				this.fillWithBlocks(world, structureboundingbox, 0, 0, 7, 2, 2, 8, Blocks.air.getDefaultState(),
						Blocks.air.getDefaultState(), false);

				for (int i = 0; i < 5; ++i) {
					this.fillWithBlocks(world, structureboundingbox, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i,
							Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
				}

				return true;
			}
		}
	}
}