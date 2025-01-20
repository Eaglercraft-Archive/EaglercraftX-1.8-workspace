package net.minecraft.item;

import java.util.List;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multisets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;

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
public class ItemMap extends ItemMapBase {
	protected ItemMap() {
		this.setHasSubtypes(true);
	}

	public static MapData loadMapData(int mapId, World worldIn) {
		String s = "map_" + mapId;
		MapData mapdata = (MapData) worldIn.loadItemData(MapData.class, s);
		if (mapdata == null) {
			mapdata = new MapData(s);
			worldIn.setItemData(s, mapdata);
		}

		return mapdata;
	}

	public MapData getMapData(ItemStack stack, World worldIn) {
		String s = "map_" + stack.getMetadata();
		MapData mapdata = (MapData) worldIn.loadItemData(MapData.class, s);
		if (mapdata == null && !worldIn.isRemote) {
			stack.setItemDamage(worldIn.getUniqueDataId("map"));
			s = "map_" + stack.getMetadata();
			mapdata = new MapData(s);
			mapdata.scale = 3;
			mapdata.calculateMapCenter((double) worldIn.getWorldInfo().getSpawnX(),
					(double) worldIn.getWorldInfo().getSpawnZ(), mapdata.scale);
			mapdata.dimension = (byte) worldIn.provider.getDimensionId();
			mapdata.markDirty();
			worldIn.setItemData(s, mapdata);
		}

		return mapdata;
	}

	public void updateMapData(World worldIn, Entity viewer, MapData data) {
		if (worldIn.provider.getDimensionId() == data.dimension && viewer instanceof EntityPlayer) {
			int i = 1 << data.scale;
			int j = data.xCenter;
			int k = data.zCenter;
			int l = MathHelper.floor_double(viewer.posX - (double) j) / i + 64;
			int i1 = MathHelper.floor_double(viewer.posZ - (double) k) / i + 64;
			int j1 = 128 / i;
			if (worldIn.provider.getHasNoSky()) {
				j1 /= 2;
			}

			MapData.MapInfo mapdata$mapinfo = data.getMapInfo((EntityPlayer) viewer);
			++mapdata$mapinfo.field_82569_d;
			boolean flag = false;

			for (int k1 = l - j1 + 1; k1 < l + j1; ++k1) {
				if ((k1 & 15) == (mapdata$mapinfo.field_82569_d & 15) || flag) {
					flag = false;
					double d0 = 0.0D;

					for (int l1 = i1 - j1 - 1; l1 < i1 + j1; ++l1) {
						if (k1 >= 0 && l1 >= -1 && k1 < 128 && l1 < 128) {
							int i2 = k1 - l;
							int j2 = l1 - i1;
							boolean flag1 = i2 * i2 + j2 * j2 > (j1 - 2) * (j1 - 2);
							int k2 = (j / i + k1 - 64) * i;
							int l2 = (k / i + l1 - 64) * i;
							HashMultiset hashmultiset = HashMultiset.create();
							Chunk chunk = worldIn.getChunkFromBlockCoords(new BlockPos(k2, 0, l2));
							if (!chunk.isEmpty()) {
								int i3 = k2 & 15;
								int j3 = l2 & 15;
								int k3 = 0;
								double d1 = 0.0D;
								if (worldIn.provider.getHasNoSky()) {
									int l3 = k2 + l2 * 231871;
									l3 = l3 * l3 * 31287121 + l3 * 11;
									if ((l3 >> 20 & 1) == 0) {
										hashmultiset.add(Blocks.dirt.getMapColor(Blocks.dirt.getDefaultState()
												.withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT)), 10);
									} else {
										hashmultiset.add(
												Blocks.stone.getMapColor(Blocks.stone.getDefaultState()
														.withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)),
												100);
									}

									d1 = 100.0D;
								} else {
									BlockPos blockpos$mutableblockpos = new BlockPos();

									for (int i4 = 0; i4 < i; ++i4) {
										for (int j4 = 0; j4 < i; ++j4) {
											int k4 = chunk.getHeightValue(i4 + i3, j4 + j3) + 1;
											IBlockState iblockstate = Blocks.air.getDefaultState();
											if (k4 > 1) {
												label541: {
													while (true) {
														--k4;
														iblockstate = chunk.getBlockState(blockpos$mutableblockpos
																.func_181079_c(i4 + i3, k4, j4 + j3));
														if (iblockstate.getBlock()
																.getMapColor(iblockstate) != MapColor.airColor
																|| k4 <= 0) {
															break;
														}
													}

													if (k4 > 0 && iblockstate.getBlock().getMaterial().isLiquid()) {
														int l4 = k4 - 1;

														while (true) {
															Block block = chunk.getBlock(i4 + i3, l4--, j4 + j3);
															++k3;
															if (l4 <= 0 || !block.getMaterial().isLiquid()) {
																break label541;
															}
														}
													}
												}
											}

											d1 += (double) k4 / (double) (i * i);
											hashmultiset.add(iblockstate.getBlock().getMapColor(iblockstate));
										}
									}
								}

								k3 = k3 / (i * i);
								double d2 = (d1 - d0) * 4.0D / (double) (i + 4)
										+ ((double) (k1 + l1 & 1) - 0.5D) * 0.4D;
								byte b0 = 1;
								if (d2 > 0.6D) {
									b0 = 2;
								}

								if (d2 < -0.6D) {
									b0 = 0;
								}

								MapColor mapcolor = (MapColor) Iterables
										.getFirst(Multisets.copyHighestCountFirst(hashmultiset), MapColor.airColor);
								if (mapcolor == MapColor.waterColor) {
									d2 = (double) k3 * 0.1D + (double) (k1 + l1 & 1) * 0.2D;
									b0 = 1;
									if (d2 < 0.5D) {
										b0 = 2;
									}

									if (d2 > 0.9D) {
										b0 = 0;
									}
								}

								d0 = d1;
								if (l1 >= 0 && i2 * i2 + j2 * j2 < j1 * j1 && (!flag1 || (k1 + l1 & 1) != 0)) {
									byte b1 = data.colors[k1 + l1 * 128];
									byte b2 = (byte) (mapcolor.colorIndex * 4 + b0);
									if (b1 != b2) {
										data.colors[k1 + l1 * 128] = b2;
										data.updateMapData(k1, l1);
										flag = true;
									}
								}
							}
						}
					}
				}
			}

		}
	}

	/**+
	 * Called each tick as long the item is on a player inventory.
	 * Uses by maps to check if is on a player hand and update it's
	 * contents.
	 */
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote) {
			MapData mapdata = this.getMapData(stack, worldIn);
			if (entityIn instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entityIn;
				mapdata.updateVisiblePlayers(entityplayer, stack);
			}

			if (isSelected) {
				this.updateMapData(worldIn, entityIn, mapdata);
			}

		}
	}

	public Packet createMapDataPacket(ItemStack stack, World worldIn, EntityPlayer player) {
		return this.getMapData(stack, worldIn).getMapPacket(stack, worldIn, player);
	}

	/**+
	 * Called when item is crafted/smelted. Used only by maps so
	 * far.
	 */
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("map_is_scaling")) {
			MapData mapdata = Items.filled_map.getMapData(stack, worldIn);
			stack.setItemDamage(worldIn.getUniqueDataId("map"));
			MapData mapdata1 = new MapData("map_" + stack.getMetadata());
			mapdata1.scale = (byte) (mapdata.scale + 1);
			if (mapdata1.scale > 4) {
				mapdata1.scale = 4;
			}

			mapdata1.calculateMapCenter((double) mapdata.xCenter, (double) mapdata.zCenter, mapdata1.scale);
			mapdata1.dimension = mapdata.dimension;
			mapdata1.markDirty();
			worldIn.setItemData("map_" + stack.getMetadata(), mapdata1);
		}

	}

	/**+
	 * allows items to add custom lines of information to the
	 * mouseover description
	 */
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		MapData mapdata = this.getMapData(stack, playerIn.worldObj);
		if (advanced) {
			if (mapdata == null) {
				tooltip.add("Unknown map");
			} else {
				tooltip.add("Scaling at 1:" + (1 << mapdata.scale));
				tooltip.add("(Level " + mapdata.scale + "/" + 4 + ")");
			}
		}

	}
}