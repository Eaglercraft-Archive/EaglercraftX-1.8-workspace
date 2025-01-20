package net.minecraft.init;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import java.io.PrintStream;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.LoggingPrintStream;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class Bootstrap {
	private static final PrintStream SYSOUT = System.out;
	/**+
	 * Whether the blocks, items, etc have already been registered
	 */
	private static boolean alreadyRegistered = false;
	private static final Logger LOGGER = LogManager.getLogger();

	/**+
	 * Is Bootstrap registration already done?
	 */
	public static boolean isRegistered() {
		return alreadyRegistered;
	}

	static void registerDispenserBehaviors() {
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense() {
			protected IProjectile getProjectileEntity(World world, IPosition iposition) {
				EntityArrow entityarrow = new EntityArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
				entityarrow.canBePickedUp = 1;
				return entityarrow;
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense() {
			protected IProjectile getProjectileEntity(World world, IPosition iposition) {
				return new EntityEgg(world, iposition.getX(), iposition.getY(), iposition.getZ());
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense() {
			protected IProjectile getProjectileEntity(World world, IPosition iposition) {
				return new EntitySnowball(world, iposition.getX(), iposition.getY(), iposition.getZ());
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense() {
			protected IProjectile getProjectileEntity(World world, IPosition iposition) {
				return new EntityExpBottle(world, iposition.getX(), iposition.getY(), iposition.getZ());
			}

			protected float func_82498_a() {
				return super.func_82498_a() * 0.5F;
			}

			protected float func_82500_b() {
				return super.func_82500_b() * 1.25F;
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem, new IBehaviorDispenseItem() {
			private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();

			public ItemStack dispense(IBlockSource iblocksource, final ItemStack itemstack) {
				return ItemPotion.isSplash(itemstack.getMetadata()) ? (new BehaviorProjectileDispense() {
					protected IProjectile getProjectileEntity(World world, IPosition iposition) {
						return new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ(),
								itemstack.copy());
					}

					protected float func_82498_a() {
						return super.func_82498_a() * 0.5F;
					}

					protected float func_82500_b() {
						return super.func_82500_b() * 1.25F;
					}
				}).dispense(iblocksource, itemstack) : this.field_150843_b.dispense(iblocksource, itemstack);
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem() {
			public ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
				EnumFacing enumfacing = BlockDispenser.getFacing(iblocksource.getBlockMetadata());
				double d0 = iblocksource.getX() + (double) enumfacing.getFrontOffsetX();
				double d1 = (double) ((float) iblocksource.getBlockPos().getY() + 0.2F);
				double d2 = iblocksource.getZ() + (double) enumfacing.getFrontOffsetZ();
				Entity entity = ItemMonsterPlacer.spawnCreature(iblocksource.getWorld(), itemstack.getMetadata(), d0,
						d1, d2);
				if (entity instanceof EntityLivingBase && itemstack.hasDisplayName()) {
					((EntityLiving) entity).setCustomNameTag(itemstack.getDisplayName());
				}

				itemstack.splitStack(1);
				return itemstack;
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem() {
			public ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
				EnumFacing enumfacing = BlockDispenser.getFacing(iblocksource.getBlockMetadata());
				double d0 = iblocksource.getX() + (double) enumfacing.getFrontOffsetX();
				double d1 = (double) ((float) iblocksource.getBlockPos().getY() + 0.2F);
				double d2 = iblocksource.getZ() + (double) enumfacing.getFrontOffsetZ();
				EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(iblocksource.getWorld(), d0, d1,
						d2, itemstack);
				iblocksource.getWorld().spawnEntityInWorld(entityfireworkrocket);
				itemstack.splitStack(1);
				return itemstack;
			}

			protected void playDispenseSound(IBlockSource iblocksource) {
				iblocksource.getWorld().playAuxSFX(1002, iblocksource.getBlockPos(), 0);
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem() {
			public ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
				EnumFacing enumfacing = BlockDispenser.getFacing(iblocksource.getBlockMetadata());
				IPosition iposition = BlockDispenser.getDispensePosition(iblocksource);
				double d0 = iposition.getX() + (double) ((float) enumfacing.getFrontOffsetX() * 0.3F);
				double d1 = iposition.getY() + (double) ((float) enumfacing.getFrontOffsetY() * 0.3F);
				double d2 = iposition.getZ() + (double) ((float) enumfacing.getFrontOffsetZ() * 0.3F);
				World world = iblocksource.getWorld();
				EaglercraftRandom random = world.rand;
				double d3 = random.nextGaussian() * 0.05D + (double) enumfacing.getFrontOffsetX();
				double d4 = random.nextGaussian() * 0.05D + (double) enumfacing.getFrontOffsetY();
				double d5 = random.nextGaussian() * 0.05D + (double) enumfacing.getFrontOffsetZ();
				world.spawnEntityInWorld(new EntitySmallFireball(world, d0, d1, d2, d3, d4, d5));
				itemstack.splitStack(1);
				return itemstack;
			}

			protected void playDispenseSound(IBlockSource iblocksource) {
				iblocksource.getWorld().playAuxSFX(1009, iblocksource.getBlockPos(), 0);
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem() {
			private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();

			public ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
				EnumFacing enumfacing = BlockDispenser.getFacing(iblocksource.getBlockMetadata());
				World world = iblocksource.getWorld();
				double d0 = iblocksource.getX() + (double) ((float) enumfacing.getFrontOffsetX() * 1.125F);
				double d1 = iblocksource.getY() + (double) ((float) enumfacing.getFrontOffsetY() * 1.125F);
				double d2 = iblocksource.getZ() + (double) ((float) enumfacing.getFrontOffsetZ() * 1.125F);
				BlockPos blockpos = iblocksource.getBlockPos().offset(enumfacing);
				Material material = world.getBlockState(blockpos).getBlock().getMaterial();
				double d3;
				if (Material.water.equals(material)) {
					d3 = 1.0D;
				} else {
					if (!Material.air.equals(material)
							|| !Material.water.equals(world.getBlockState(blockpos.down()).getBlock().getMaterial())) {
						return this.field_150842_b.dispense(iblocksource, itemstack);
					}

					d3 = 0.0D;
				}

				EntityBoat entityboat = new EntityBoat(world, d0, d1 + d3, d2);
				world.spawnEntityInWorld(entityboat);
				itemstack.splitStack(1);
				return itemstack;
			}

			protected void playDispenseSound(IBlockSource iblocksource) {
				iblocksource.getWorld().playAuxSFX(1000, iblocksource.getBlockPos(), 0);
			}
		});
		BehaviorDefaultDispenseItem behaviordefaultdispenseitem = new BehaviorDefaultDispenseItem() {
			private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();

			public ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
				ItemBucket itembucket = (ItemBucket) itemstack.getItem();
				BlockPos blockpos = iblocksource.getBlockPos()
						.offset(BlockDispenser.getFacing(iblocksource.getBlockMetadata()));
				if (itembucket.tryPlaceContainedLiquid(iblocksource.getWorld(), blockpos)) {
					itemstack.setItem(Items.bucket);
					itemstack.stackSize = 1;
					return itemstack;
				} else {
					return this.field_150841_b.dispense(iblocksource, itemstack);
				}
			}
		};
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, behaviordefaultdispenseitem);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, behaviordefaultdispenseitem);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem() {
			private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();

			public ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
				World world = iblocksource.getWorld();
				BlockPos blockpos = iblocksource.getBlockPos()
						.offset(BlockDispenser.getFacing(iblocksource.getBlockMetadata()));
				IBlockState iblockstate = world.getBlockState(blockpos);
				Block block = iblockstate.getBlock();
				Material material = block.getMaterial();
				Item item;
				if (Material.water.equals(material) && block instanceof BlockLiquid
						&& ((Integer) iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
					item = Items.water_bucket;
				} else {
					if (!Material.lava.equals(material) || !(block instanceof BlockLiquid)
							|| ((Integer) iblockstate.getValue(BlockLiquid.LEVEL)).intValue() != 0) {
						return super.dispenseStack(iblocksource, itemstack);
					}

					item = Items.lava_bucket;
				}

				world.setBlockToAir(blockpos);
				if (--itemstack.stackSize == 0) {
					itemstack.setItem(item);
					itemstack.stackSize = 1;
				} else if (((TileEntityDispenser) iblocksource.getBlockTileEntity())
						.addItemStack(new ItemStack(item)) < 0) {
					this.field_150840_b.dispense(iblocksource, new ItemStack(item));
				}

				return itemstack;
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem() {
			private boolean field_150839_b = true;

			protected ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
				World world = iblocksource.getWorld();
				BlockPos blockpos = iblocksource.getBlockPos()
						.offset(BlockDispenser.getFacing(iblocksource.getBlockMetadata()));
				if (world.isAirBlock(blockpos)) {
					world.setBlockState(blockpos, Blocks.fire.getDefaultState());
					if (itemstack.attemptDamageItem(1, world.rand)) {
						itemstack.stackSize = 0;
					}
				} else if (world.getBlockState(blockpos).getBlock() == Blocks.tnt) {
					Blocks.tnt.onBlockDestroyedByPlayer(world, blockpos,
							Blocks.tnt.getDefaultState().withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)));
					world.setBlockToAir(blockpos);
				} else {
					this.field_150839_b = false;
				}

				return itemstack;
			}

			protected void playDispenseSound(IBlockSource iblocksource) {
				if (this.field_150839_b) {
					iblocksource.getWorld().playAuxSFX(1000, iblocksource.getBlockPos(), 0);
				} else {
					iblocksource.getWorld().playAuxSFX(1001, iblocksource.getBlockPos(), 0);
				}

			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem() {
			private boolean field_150838_b = true;

			protected ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
				if (EnumDyeColor.WHITE == EnumDyeColor.byDyeDamage(itemstack.getMetadata())) {
					World world = iblocksource.getWorld();
					BlockPos blockpos = iblocksource.getBlockPos()
							.offset(BlockDispenser.getFacing(iblocksource.getBlockMetadata()));
					if (ItemDye.applyBonemeal(itemstack, world, blockpos)) {
						world.playAuxSFX(2005, blockpos, 0);
					} else {
						this.field_150838_b = false;
					}

					return itemstack;
				} else {
					return super.dispenseStack(iblocksource, itemstack);
				}
			}

			protected void playDispenseSound(IBlockSource iblocksource) {
				if (this.field_150838_b) {
					iblocksource.getWorld().playAuxSFX(1000, iblocksource.getBlockPos(), 0);
				} else {
					iblocksource.getWorld().playAuxSFX(1001, iblocksource.getBlockPos(), 0);
				}

			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt),
				new BehaviorDefaultDispenseItem() {
					protected ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
						World world = iblocksource.getWorld();
						BlockPos blockpos = iblocksource.getBlockPos()
								.offset(BlockDispenser.getFacing(iblocksource.getBlockMetadata()));
						EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) blockpos.getX() + 0.5D,
								(double) blockpos.getY(), (double) blockpos.getZ() + 0.5D, (EntityLivingBase) null);
						world.spawnEntityInWorld(entitytntprimed);
						world.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);
						--itemstack.stackSize;
						return itemstack;
					}
				});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.skull, new BehaviorDefaultDispenseItem() {
			private boolean field_179240_b = true;

			protected ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
				World world = iblocksource.getWorld();
				EnumFacing enumfacing = BlockDispenser.getFacing(iblocksource.getBlockMetadata());
				BlockPos blockpos = iblocksource.getBlockPos().offset(enumfacing);
				BlockSkull blockskull = Blocks.skull;
				if (world.isAirBlock(blockpos) && blockskull.canDispenserPlace(world, blockpos, itemstack)) {
					{
						world.setBlockState(blockpos,
								blockskull.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP), 3);
						TileEntity tileentity = world.getTileEntity(blockpos);
						if (tileentity instanceof TileEntitySkull) {
							if (itemstack.getMetadata() == 3) {
								GameProfile gameprofile = null;
								if (itemstack.hasTagCompound()) {
									NBTTagCompound nbttagcompound = itemstack.getTagCompound();
									if (nbttagcompound.hasKey("SkullOwner", 10)) {
										gameprofile = NBTUtil
												.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
									} else if (nbttagcompound.hasKey("SkullOwner", 8)) {
										String s = nbttagcompound.getString("SkullOwner");
										if (!StringUtils.isNullOrEmpty(s)) {
											gameprofile = new GameProfile((EaglercraftUUID) null, s);
										}
									}
								}

								((TileEntitySkull) tileentity).setPlayerProfile(gameprofile);
							} else {
								((TileEntitySkull) tileentity).setType(itemstack.getMetadata());
							}

							((TileEntitySkull) tileentity)
									.setSkullRotation(enumfacing.getOpposite().getHorizontalIndex() * 4);
							Blocks.skull.checkWitherSpawn(world, blockpos, (TileEntitySkull) tileentity);
						}

						--itemstack.stackSize;
					}
				} else {
					this.field_179240_b = false;
				}

				return itemstack;
			}

			protected void playDispenseSound(IBlockSource iblocksource) {
				if (this.field_179240_b) {
					iblocksource.getWorld().playAuxSFX(1000, iblocksource.getBlockPos(), 0);
				} else {
					iblocksource.getWorld().playAuxSFX(1001, iblocksource.getBlockPos(), 0);
				}

			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.pumpkin),
				new BehaviorDefaultDispenseItem() {
					private boolean field_179241_b = true;

					protected ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
						World world = iblocksource.getWorld();
						BlockPos blockpos = iblocksource.getBlockPos()
								.offset(BlockDispenser.getFacing(iblocksource.getBlockMetadata()));
						BlockPumpkin blockpumpkin = (BlockPumpkin) Blocks.pumpkin;
						if (world.isAirBlock(blockpos) && blockpumpkin.canDispenserPlace(world, blockpos)) {
							world.setBlockState(blockpos, blockpumpkin.getDefaultState(), 3);
							--itemstack.stackSize;
						} else {
							this.field_179241_b = false;
						}

						return itemstack;
					}

					protected void playDispenseSound(IBlockSource iblocksource) {
						if (this.field_179241_b) {
							iblocksource.getWorld().playAuxSFX(1000, iblocksource.getBlockPos(), 0);
						} else {
							iblocksource.getWorld().playAuxSFX(1001, iblocksource.getBlockPos(), 0);
						}

					}
				});
	}

	/**+
	 * Registers blocks, items, stats, etc.
	 */
	public static void register() {
		if (!alreadyRegistered) {
			alreadyRegistered = true;
			if (LOGGER.isDebugEnabled()) {
				redirectOutputToLog();
			}

			Block.registerBlocks();
			Blocks.doBootstrap();
			BiomeGenBase.doBootstrap();
			BlockFire.init();
			EntityEnderman.bootstrap();
			ItemAxe.bootstrap();
			ItemPickaxe.bootstrap();
			ItemSpade.bootstrap();
			Item.registerItems();
			Items.doBootstrap();
			EntityVillager.bootstrap();
			StatList.init();
			registerDispenserBehaviors();
		}
	}

	/**+
	 * redirect standard streams to logger
	 */
	private static void redirectOutputToLog() {
		System.setErr(new LoggingPrintStream("STDERR", true, System.err));
		System.setOut(new LoggingPrintStream("STDOUT", false, SYSOUT));
	}

	public static void printToSYSOUT(String parString1) {
		SYSOUT.println(parString1);
	}
}