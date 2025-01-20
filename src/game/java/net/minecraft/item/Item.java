package net.minecraft.item;

import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
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
public class Item {
	public static final RegistryNamespaced<ResourceLocation, Item> itemRegistry = new RegistryNamespaced();
	private static final Map<Block, Item> BLOCK_TO_ITEM = Maps.newHashMap();
	protected static final EaglercraftUUID itemModifierUUID = EaglercraftUUID
			.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
	private CreativeTabs tabToDisplayOn;
	/**+
	 * The RNG used by the Item subclasses.
	 */
	protected static EaglercraftRandom itemRand = new EaglercraftRandom();
	/**+
	 * Maximum size of the stack.
	 */
	protected int maxStackSize = 64;
	private int maxDamage;
	protected boolean bFull3D;
	protected boolean hasSubtypes;
	private Item containerItem;
	private String potionEffect;
	private String unlocalizedName;

	public static int getIdFromItem(Item itemIn) {
		return itemIn == null ? 0 : itemRegistry.getIDForObject(itemIn);
	}

	public static Item getItemById(int id) {
		return (Item) itemRegistry.getObjectById(id);
	}

	public static Item getItemFromBlock(Block blockIn) {
		return (Item) BLOCK_TO_ITEM.get(blockIn);
	}

	/**+
	 * Tries to get an Item by it's name (e.g. minecraft:apple) or a
	 * String representation of a numerical ID. If both fail, null
	 * is returned.
	 */
	public static Item getByNameOrId(String id) {
		Item item = (Item) itemRegistry.getObject(new ResourceLocation(id));
		if (item == null) {
			try {
				return getItemById(Integer.parseInt(id));
			} catch (NumberFormatException var3) {
				;
			}
		}

		return item;
	}

	/**+
	 * Called when an ItemStack with NBT data is read to potentially
	 * that ItemStack's NBT data
	 */
	public boolean updateItemStackNBT(NBTTagCompound var1) {
		return false;
	}

	public Item setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
		return this;
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6,
			float var7, float var8) {
		return false;
	}

	public float getStrVsBlock(ItemStack var1, Block var2) {
		return 1.0F;
	}

	/**+
	 * Called whenever this item is equipped and the right mouse
	 * button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack itemstack, World var2, EntityPlayer var3) {
		return itemstack;
	}

	/**+
	 * Called when the player finishes using this Item (E.g.
	 * finishes eating.). Not called when the player stops using the
	 * Item before the action is complete.
	 */
	public ItemStack onItemUseFinish(ItemStack itemstack, World var2, EntityPlayer var3) {
		return itemstack;
	}

	/**+
	 * Returns the maximum size of the stack for a specific item.
	 * *Isn't this more a Set than a Get?*
	 */
	public int getItemStackLimit() {
		return this.maxStackSize;
	}

	/**+
	 * Converts the given ItemStack damage value into a metadata
	 * value to be placed in the world when this Item is placed as a
	 * Block (mostly used with ItemBlocks).
	 */
	public int getMetadata(int var1) {
		return 0;
	}

	public boolean getHasSubtypes() {
		return this.hasSubtypes;
	}

	protected Item setHasSubtypes(boolean hasSubtypes) {
		this.hasSubtypes = hasSubtypes;
		return this;
	}

	/**+
	 * Returns the maximum damage an item can take.
	 */
	public int getMaxDamage() {
		return this.maxDamage;
	}

	/**+
	 * set max damage of an Item
	 */
	protected Item setMaxDamage(int maxDamageIn) {
		this.maxDamage = maxDamageIn;
		return this;
	}

	public boolean isDamageable() {
		return this.maxDamage > 0 && !this.hasSubtypes;
	}

	/**+
	 * Current implementations of this method in child classes do
	 * not use the entry argument beside ev. They just raise the
	 * damage on the stack.
	 */
	public boolean hitEntity(ItemStack var1, EntityLivingBase var2, EntityLivingBase var3) {
		return false;
	}

	/**+
	 * Called when a Block is destroyed using this Item. Return true
	 * to trigger the "Use Item" statistic.
	 */
	public boolean onBlockDestroyed(ItemStack var1, World var2, Block var3, BlockPos var4, EntityLivingBase var5) {
		return false;
	}

	/**+
	 * Check whether this Item can harvest the given Block
	 */
	public boolean canHarvestBlock(Block var1) {
		return false;
	}

	/**+
	 * Returns true if the item can be used on the given entity,
	 * e.g. shears on sheep.
	 */
	public boolean itemInteractionForEntity(ItemStack var1, EntityPlayer var2, EntityLivingBase var3) {
		return false;
	}

	/**+
	 * Sets bFull3D to True and return the object.
	 */
	public Item setFull3D() {
		this.bFull3D = true;
		return this;
	}

	/**+
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	public boolean isFull3D() {
		return this.bFull3D;
	}

	/**+
	 * Returns true if this item should be rotated by 180 degrees
	 * around the Y axis when being held in an entities hands.
	 */
	public boolean shouldRotateAroundWhenRendering() {
		return false;
	}

	/**+
	 * Sets the unlocalized name of this item to the string passed
	 * as the parameter, prefixed by "item."
	 */
	public Item setUnlocalizedName(String s) {
		this.unlocalizedName = s;
		return this;
	}

	/**+
	 * Translates the unlocalized name of this item, but without the
	 * .name suffix, so the translation fails and the unlocalized
	 * name itself is returned.
	 */
	public String getUnlocalizedNameInefficiently(ItemStack stack) {
		String s = this.getUnlocalizedName(stack);
		return s == null ? "" : StatCollector.translateToLocal(s);
	}

	/**+
	 * Returns the unlocalized name of this item.
	 */
	public String getUnlocalizedName() {
		return "item." + this.unlocalizedName;
	}

	/**+
	 * Returns the unlocalized name of this item.
	 */
	public String getUnlocalizedName(ItemStack var1) {
		return "item." + this.unlocalizedName;
	}

	public Item setContainerItem(Item containerItem) {
		this.containerItem = containerItem;
		return this;
	}

	/**+
	 * If this function returns true (or the item is damageable),
	 * the ItemStack's NBT tag will be sent to the client.
	 */
	public boolean getShareTag() {
		return true;
	}

	public Item getContainerItem() {
		return this.containerItem;
	}

	/**+
	 * True if this Item has a container item (a.k.a. crafting
	 * result)
	 */
	public boolean hasContainerItem() {
		return this.containerItem != null;
	}

	public int getColorFromItemStack(ItemStack var1, int var2) {
		return 16777215;
	}

	/**+
	 * Called each tick as long the item is on a player inventory.
	 * Uses by maps to check if is on a player hand and update it's
	 * contents.
	 */
	public void onUpdate(ItemStack var1, World var2, Entity var3, int var4, boolean var5) {
	}

	/**+
	 * Called when item is crafted/smelted. Used only by maps so
	 * far.
	 */
	public void onCreated(ItemStack var1, World var2, EntityPlayer var3) {
	}

	/**+
	 * false for all Items except sub-classes of ItemMapBase
	 */
	public boolean isMap() {
		return false;
	}

	/**+
	 * returns the action that specifies what animation to play when
	 * the items is being used
	 */
	public EnumAction getItemUseAction(ItemStack var1) {
		return EnumAction.NONE;
	}

	/**+
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack var1) {
		return 0;
	}

	/**+
	 * Called when the player stops using an Item (stops holding the
	 * right mouse button).
	 */
	public void onPlayerStoppedUsing(ItemStack var1, World var2, EntityPlayer var3, int var4) {
	}

	/**+
	 * Sets the string representing this item's effect on a potion
	 * when used as an ingredient.
	 */
	protected Item setPotionEffect(String potionEffect) {
		this.potionEffect = potionEffect;
		return this;
	}

	public String getPotionEffect(ItemStack var1) {
		return this.potionEffect;
	}

	public boolean isPotionIngredient(ItemStack stack) {
		return this.getPotionEffect(stack) != null;
	}

	/**+
	 * allows items to add custom lines of information to the
	 * mouseover description
	 */
	public void addInformation(ItemStack var1, EntityPlayer var2, List<String> var3, boolean var4) {
	}

	public String getItemStackDisplayName(ItemStack itemstack) {
		return ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(itemstack) + ".name")).trim();
	}

	public boolean hasEffect(ItemStack itemstack) {
		return itemstack.isItemEnchanted();
	}

	/**+
	 * Return an item rarity from EnumRarity
	 */
	public EnumRarity getRarity(ItemStack itemstack) {
		return itemstack.isItemEnchanted() ? EnumRarity.RARE : EnumRarity.COMMON;
	}

	/**+
	 * Checks isDamagable and if it cannot be stacked
	 */
	public boolean isItemTool(ItemStack var1) {
		return this.getItemStackLimit() == 1 && this.isDamageable();
	}

	protected MovingObjectPosition getMovingObjectPositionFromPlayer(World worldIn, EntityPlayer playerIn,
			boolean useLiquids) {
		float f = playerIn.rotationPitch;
		float f1 = playerIn.rotationYaw;
		double d0 = playerIn.posX;
		double d1 = playerIn.posY + (double) playerIn.getEyeHeight();
		double d2 = playerIn.posZ;
		Vec3 vec3 = new Vec3(d0, d1, d2);
		float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
		float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = 5.0D;
		Vec3 vec31 = vec3.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
		return worldIn.rayTraceBlocks(vec3, vec31, useLiquids, !useLiquids, false);
	}

	/**+
	 * Return the enchantability factor of the item, most of the
	 * time is based on material.
	 */
	public int getItemEnchantability() {
		return 0;
	}

	/**+
	 * returns a list of items with the same ID, but different meta
	 * (eg: dye returns 16 items)
	 */
	public void getSubItems(Item item, CreativeTabs var2, List<ItemStack> list) {
		list.add(new ItemStack(item, 1, 0));
	}

	/**+
	 * gets the CreativeTab this item is displayed on
	 */
	public CreativeTabs getCreativeTab() {
		return this.tabToDisplayOn;
	}

	/**+
	 * returns this;
	 */
	public Item setCreativeTab(CreativeTabs tab) {
		this.tabToDisplayOn = tab;
		return this;
	}

	/**+
	 * Returns true if players can use this item to affect the world
	 * (e.g. placing blocks, placing ender eyes in portal) when not
	 * in creative
	 */
	public boolean canItemEditBlocks() {
		return false;
	}

	/**+
	 * Return whether this item is repairable in an anvil.
	 */
	public boolean getIsRepairable(ItemStack var1, ItemStack var2) {
		return false;
	}

	public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
		return HashMultimap.create();
	}

	public static void registerItems() {
		registerItemBlock(Blocks.stone,
				(new ItemMultiTexture(Blocks.stone, Blocks.stone, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockStone.EnumType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("stone"));
		registerItemBlock(Blocks.grass, new ItemColored(Blocks.grass, false));
		registerItemBlock(Blocks.dirt,
				(new ItemMultiTexture(Blocks.dirt, Blocks.dirt, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockDirt.DirtType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("dirt"));
		registerItemBlock(Blocks.cobblestone);
		registerItemBlock(Blocks.planks,
				(new ItemMultiTexture(Blocks.planks, Blocks.planks, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockPlanks.EnumType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("wood"));
		registerItemBlock(Blocks.sapling,
				(new ItemMultiTexture(Blocks.sapling, Blocks.sapling, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockPlanks.EnumType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("sapling"));
		registerItemBlock(Blocks.bedrock);
		registerItemBlock(Blocks.sand,
				(new ItemMultiTexture(Blocks.sand, Blocks.sand, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockSand.EnumType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("sand"));
		registerItemBlock(Blocks.gravel);
		registerItemBlock(Blocks.gold_ore);
		registerItemBlock(Blocks.iron_ore);
		registerItemBlock(Blocks.coal_ore);
		registerItemBlock(Blocks.log, (new ItemMultiTexture(Blocks.log, Blocks.log, new Function<ItemStack, String>() {
			public String apply(ItemStack itemstack) {
				return BlockPlanks.EnumType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
			}
		})).setUnlocalizedName("log"));
		registerItemBlock(Blocks.log2,
				(new ItemMultiTexture(Blocks.log2, Blocks.log2, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockPlanks.EnumType.byMetadata(itemstack.getMetadata() + 4).getUnlocalizedName();
					}
				})).setUnlocalizedName("log"));
		registerItemBlock(Blocks.leaves, (new ItemLeaves(Blocks.leaves)).setUnlocalizedName("leaves"));
		registerItemBlock(Blocks.leaves2, (new ItemLeaves(Blocks.leaves2)).setUnlocalizedName("leaves"));
		registerItemBlock(Blocks.sponge,
				(new ItemMultiTexture(Blocks.sponge, Blocks.sponge, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return (itemstack.getMetadata() & 1) == 1 ? "wet" : "dry";
					}
				})).setUnlocalizedName("sponge"));
		registerItemBlock(Blocks.glass);
		registerItemBlock(Blocks.lapis_ore);
		registerItemBlock(Blocks.lapis_block);
		registerItemBlock(Blocks.dispenser);
		registerItemBlock(Blocks.sandstone,
				(new ItemMultiTexture(Blocks.sandstone, Blocks.sandstone, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockSandStone.EnumType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("sandStone"));
		registerItemBlock(Blocks.noteblock);
		registerItemBlock(Blocks.golden_rail);
		registerItemBlock(Blocks.detector_rail);
		registerItemBlock(Blocks.sticky_piston, new ItemPiston(Blocks.sticky_piston));
		registerItemBlock(Blocks.web);
		registerItemBlock(Blocks.tallgrass,
				(new ItemColored(Blocks.tallgrass, true)).setSubtypeNames(new String[] { "shrub", "grass", "fern" }));
		registerItemBlock(Blocks.deadbush);
		registerItemBlock(Blocks.piston, new ItemPiston(Blocks.piston));
		registerItemBlock(Blocks.wool, (new ItemCloth(Blocks.wool)).setUnlocalizedName("cloth"));
		registerItemBlock(Blocks.yellow_flower,
				(new ItemMultiTexture(Blocks.yellow_flower, Blocks.yellow_flower, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockFlower.EnumFlowerType
								.getType(BlockFlower.EnumFlowerColor.YELLOW, itemstack.getMetadata())
								.getUnlocalizedName();
					}
				})).setUnlocalizedName("flower"));
		registerItemBlock(Blocks.red_flower,
				(new ItemMultiTexture(Blocks.red_flower, Blocks.red_flower, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockFlower.EnumFlowerType
								.getType(BlockFlower.EnumFlowerColor.RED, itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("rose"));
		registerItemBlock(Blocks.brown_mushroom);
		registerItemBlock(Blocks.red_mushroom);
		registerItemBlock(Blocks.gold_block);
		registerItemBlock(Blocks.iron_block);
		registerItemBlock(Blocks.stone_slab,
				(new ItemSlab(Blocks.stone_slab, Blocks.stone_slab, Blocks.double_stone_slab))
						.setUnlocalizedName("stoneSlab"));
		registerItemBlock(Blocks.brick_block);
		registerItemBlock(Blocks.tnt);
		registerItemBlock(Blocks.bookshelf);
		registerItemBlock(Blocks.mossy_cobblestone);
		registerItemBlock(Blocks.obsidian);
		registerItemBlock(Blocks.torch);
		registerItemBlock(Blocks.mob_spawner);
		registerItemBlock(Blocks.oak_stairs);
		registerItemBlock(Blocks.chest);
		registerItemBlock(Blocks.diamond_ore);
		registerItemBlock(Blocks.diamond_block);
		registerItemBlock(Blocks.crafting_table);
		registerItemBlock(Blocks.farmland);
		registerItemBlock(Blocks.furnace);
		registerItemBlock(Blocks.lit_furnace);
		registerItemBlock(Blocks.ladder);
		registerItemBlock(Blocks.rail);
		registerItemBlock(Blocks.stone_stairs);
		registerItemBlock(Blocks.lever);
		registerItemBlock(Blocks.stone_pressure_plate);
		registerItemBlock(Blocks.wooden_pressure_plate);
		registerItemBlock(Blocks.redstone_ore);
		registerItemBlock(Blocks.redstone_torch);
		registerItemBlock(Blocks.stone_button);
		registerItemBlock(Blocks.snow_layer, new ItemSnow(Blocks.snow_layer));
		registerItemBlock(Blocks.ice);
		registerItemBlock(Blocks.snow);
		registerItemBlock(Blocks.cactus);
		registerItemBlock(Blocks.clay);
		registerItemBlock(Blocks.jukebox);
		registerItemBlock(Blocks.oak_fence);
		registerItemBlock(Blocks.spruce_fence);
		registerItemBlock(Blocks.birch_fence);
		registerItemBlock(Blocks.jungle_fence);
		registerItemBlock(Blocks.dark_oak_fence);
		registerItemBlock(Blocks.acacia_fence);
		registerItemBlock(Blocks.pumpkin);
		registerItemBlock(Blocks.netherrack);
		registerItemBlock(Blocks.soul_sand);
		registerItemBlock(Blocks.glowstone);
		registerItemBlock(Blocks.lit_pumpkin);
		registerItemBlock(Blocks.trapdoor);
		registerItemBlock(Blocks.monster_egg,
				(new ItemMultiTexture(Blocks.monster_egg, Blocks.monster_egg, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockSilverfish.EnumType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("monsterStoneEgg"));
		registerItemBlock(Blocks.stonebrick,
				(new ItemMultiTexture(Blocks.stonebrick, Blocks.stonebrick, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockStoneBrick.EnumType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("stonebricksmooth"));
		registerItemBlock(Blocks.brown_mushroom_block);
		registerItemBlock(Blocks.red_mushroom_block);
		registerItemBlock(Blocks.iron_bars);
		registerItemBlock(Blocks.glass_pane);
		registerItemBlock(Blocks.melon_block);
		registerItemBlock(Blocks.vine, new ItemColored(Blocks.vine, false));
		registerItemBlock(Blocks.oak_fence_gate);
		registerItemBlock(Blocks.spruce_fence_gate);
		registerItemBlock(Blocks.birch_fence_gate);
		registerItemBlock(Blocks.jungle_fence_gate);
		registerItemBlock(Blocks.dark_oak_fence_gate);
		registerItemBlock(Blocks.acacia_fence_gate);
		registerItemBlock(Blocks.brick_stairs);
		registerItemBlock(Blocks.stone_brick_stairs);
		registerItemBlock(Blocks.mycelium);
		registerItemBlock(Blocks.waterlily, new ItemLilyPad(Blocks.waterlily));
		registerItemBlock(Blocks.nether_brick);
		registerItemBlock(Blocks.nether_brick_fence);
		registerItemBlock(Blocks.nether_brick_stairs);
		registerItemBlock(Blocks.enchanting_table);
		registerItemBlock(Blocks.end_portal_frame);
		registerItemBlock(Blocks.end_stone);
		registerItemBlock(Blocks.dragon_egg);
		registerItemBlock(Blocks.redstone_lamp);
		registerItemBlock(Blocks.wooden_slab,
				(new ItemSlab(Blocks.wooden_slab, Blocks.wooden_slab, Blocks.double_wooden_slab))
						.setUnlocalizedName("woodSlab"));
		registerItemBlock(Blocks.sandstone_stairs);
		registerItemBlock(Blocks.emerald_ore);
		registerItemBlock(Blocks.ender_chest);
		registerItemBlock(Blocks.tripwire_hook);
		registerItemBlock(Blocks.emerald_block);
		registerItemBlock(Blocks.spruce_stairs);
		registerItemBlock(Blocks.birch_stairs);
		registerItemBlock(Blocks.jungle_stairs);
		registerItemBlock(Blocks.command_block);
		registerItemBlock(Blocks.beacon);
		registerItemBlock(Blocks.cobblestone_wall, (new ItemMultiTexture(Blocks.cobblestone_wall,
				Blocks.cobblestone_wall, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockWall.EnumType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("cobbleWall"));
		registerItemBlock(Blocks.wooden_button);
		registerItemBlock(Blocks.anvil, (new ItemAnvilBlock(Blocks.anvil)).setUnlocalizedName("anvil"));
		registerItemBlock(Blocks.trapped_chest);
		registerItemBlock(Blocks.light_weighted_pressure_plate);
		registerItemBlock(Blocks.heavy_weighted_pressure_plate);
		registerItemBlock(Blocks.daylight_detector);
		registerItemBlock(Blocks.redstone_block);
		registerItemBlock(Blocks.quartz_ore);
		registerItemBlock(Blocks.hopper);
		registerItemBlock(Blocks.quartz_block, (new ItemMultiTexture(Blocks.quartz_block, Blocks.quartz_block,
				new String[] { "default", "chiseled", "lines" })).setUnlocalizedName("quartzBlock"));
		registerItemBlock(Blocks.quartz_stairs);
		registerItemBlock(Blocks.activator_rail);
		registerItemBlock(Blocks.dropper);
		registerItemBlock(Blocks.stained_hardened_clay,
				(new ItemCloth(Blocks.stained_hardened_clay)).setUnlocalizedName("clayHardenedStained"));
		registerItemBlock(Blocks.barrier);
		registerItemBlock(Blocks.iron_trapdoor);
		registerItemBlock(Blocks.hay_block);
		registerItemBlock(Blocks.carpet, (new ItemCloth(Blocks.carpet)).setUnlocalizedName("woolCarpet"));
		registerItemBlock(Blocks.hardened_clay);
		registerItemBlock(Blocks.coal_block);
		registerItemBlock(Blocks.packed_ice);
		registerItemBlock(Blocks.acacia_stairs);
		registerItemBlock(Blocks.dark_oak_stairs);
		registerItemBlock(Blocks.slime_block);
		registerItemBlock(Blocks.double_plant,
				(new ItemDoublePlant(Blocks.double_plant, Blocks.double_plant, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockDoublePlant.EnumPlantType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("doublePlant"));
		registerItemBlock(Blocks.stained_glass,
				(new ItemCloth(Blocks.stained_glass)).setUnlocalizedName("stainedGlass"));
		registerItemBlock(Blocks.stained_glass_pane,
				(new ItemCloth(Blocks.stained_glass_pane)).setUnlocalizedName("stainedGlassPane"));
		registerItemBlock(Blocks.prismarine,
				(new ItemMultiTexture(Blocks.prismarine, Blocks.prismarine, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockPrismarine.EnumType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("prismarine"));
		registerItemBlock(Blocks.sea_lantern);
		registerItemBlock(Blocks.red_sandstone,
				(new ItemMultiTexture(Blocks.red_sandstone, Blocks.red_sandstone, new Function<ItemStack, String>() {
					public String apply(ItemStack itemstack) {
						return BlockRedSandstone.EnumType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
					}
				})).setUnlocalizedName("redSandStone"));
		registerItemBlock(Blocks.red_sandstone_stairs);
		registerItemBlock(Blocks.stone_slab2,
				(new ItemSlab(Blocks.stone_slab2, Blocks.stone_slab2, Blocks.double_stone_slab2))
						.setUnlocalizedName("stoneSlab2"));
		registerItem(256, (String) "iron_shovel",
				(new ItemSpade(Item.ToolMaterial.IRON)).setUnlocalizedName("shovelIron"));
		registerItem(257, (String) "iron_pickaxe",
				(new ItemPickaxe(Item.ToolMaterial.IRON)).setUnlocalizedName("pickaxeIron"));
		registerItem(258, (String) "iron_axe", (new ItemAxe(Item.ToolMaterial.IRON)).setUnlocalizedName("hatchetIron"));
		registerItem(259, (String) "flint_and_steel", (new ItemFlintAndSteel()).setUnlocalizedName("flintAndSteel"));
		registerItem(260, (String) "apple", (new ItemFood(4, 0.3F, false)).setUnlocalizedName("apple"));
		registerItem(261, (String) "bow", (new ItemBow()).setUnlocalizedName("bow"));
		registerItem(262, (String) "arrow",
				(new Item()).setUnlocalizedName("arrow").setCreativeTab(CreativeTabs.tabCombat));
		registerItem(263, (String) "coal", (new ItemCoal()).setUnlocalizedName("coal"));
		registerItem(264, (String) "diamond",
				(new Item()).setUnlocalizedName("diamond").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(265, (String) "iron_ingot",
				(new Item()).setUnlocalizedName("ingotIron").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(266, (String) "gold_ingot",
				(new Item()).setUnlocalizedName("ingotGold").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(267, (String) "iron_sword",
				(new ItemSword(Item.ToolMaterial.IRON)).setUnlocalizedName("swordIron"));
		registerItem(268, (String) "wooden_sword",
				(new ItemSword(Item.ToolMaterial.WOOD)).setUnlocalizedName("swordWood"));
		registerItem(269, (String) "wooden_shovel",
				(new ItemSpade(Item.ToolMaterial.WOOD)).setUnlocalizedName("shovelWood"));
		registerItem(270, (String) "wooden_pickaxe",
				(new ItemPickaxe(Item.ToolMaterial.WOOD)).setUnlocalizedName("pickaxeWood"));
		registerItem(271, (String) "wooden_axe",
				(new ItemAxe(Item.ToolMaterial.WOOD)).setUnlocalizedName("hatchetWood"));
		registerItem(272, (String) "stone_sword",
				(new ItemSword(Item.ToolMaterial.STONE)).setUnlocalizedName("swordStone"));
		registerItem(273, (String) "stone_shovel",
				(new ItemSpade(Item.ToolMaterial.STONE)).setUnlocalizedName("shovelStone"));
		registerItem(274, (String) "stone_pickaxe",
				(new ItemPickaxe(Item.ToolMaterial.STONE)).setUnlocalizedName("pickaxeStone"));
		registerItem(275, (String) "stone_axe",
				(new ItemAxe(Item.ToolMaterial.STONE)).setUnlocalizedName("hatchetStone"));
		registerItem(276, (String) "diamond_sword",
				(new ItemSword(Item.ToolMaterial.EMERALD)).setUnlocalizedName("swordDiamond"));
		registerItem(277, (String) "diamond_shovel",
				(new ItemSpade(Item.ToolMaterial.EMERALD)).setUnlocalizedName("shovelDiamond"));
		registerItem(278, (String) "diamond_pickaxe",
				(new ItemPickaxe(Item.ToolMaterial.EMERALD)).setUnlocalizedName("pickaxeDiamond"));
		registerItem(279, (String) "diamond_axe",
				(new ItemAxe(Item.ToolMaterial.EMERALD)).setUnlocalizedName("hatchetDiamond"));
		registerItem(280, (String) "stick",
				(new Item()).setFull3D().setUnlocalizedName("stick").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(281, (String) "bowl",
				(new Item()).setUnlocalizedName("bowl").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(282, (String) "mushroom_stew", (new ItemSoup(6)).setUnlocalizedName("mushroomStew"));
		registerItem(283, (String) "golden_sword",
				(new ItemSword(Item.ToolMaterial.GOLD)).setUnlocalizedName("swordGold"));
		registerItem(284, (String) "golden_shovel",
				(new ItemSpade(Item.ToolMaterial.GOLD)).setUnlocalizedName("shovelGold"));
		registerItem(285, (String) "golden_pickaxe",
				(new ItemPickaxe(Item.ToolMaterial.GOLD)).setUnlocalizedName("pickaxeGold"));
		registerItem(286, (String) "golden_axe",
				(new ItemAxe(Item.ToolMaterial.GOLD)).setUnlocalizedName("hatchetGold"));
		registerItem(287, (String) "string",
				(new ItemReed(Blocks.tripwire)).setUnlocalizedName("string").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(288, (String) "feather",
				(new Item()).setUnlocalizedName("feather").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(289, (String) "gunpowder", (new Item()).setUnlocalizedName("sulphur")
				.setPotionEffect(PotionHelper.gunpowderEffect).setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(290, (String) "wooden_hoe", (new ItemHoe(Item.ToolMaterial.WOOD)).setUnlocalizedName("hoeWood"));
		registerItem(291, (String) "stone_hoe", (new ItemHoe(Item.ToolMaterial.STONE)).setUnlocalizedName("hoeStone"));
		registerItem(292, (String) "iron_hoe", (new ItemHoe(Item.ToolMaterial.IRON)).setUnlocalizedName("hoeIron"));
		registerItem(293, (String) "diamond_hoe",
				(new ItemHoe(Item.ToolMaterial.EMERALD)).setUnlocalizedName("hoeDiamond"));
		registerItem(294, (String) "golden_hoe", (new ItemHoe(Item.ToolMaterial.GOLD)).setUnlocalizedName("hoeGold"));
		registerItem(295, (String) "wheat_seeds",
				(new ItemSeeds(Blocks.wheat, Blocks.farmland)).setUnlocalizedName("seeds"));
		registerItem(296, (String) "wheat",
				(new Item()).setUnlocalizedName("wheat").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(297, (String) "bread", (new ItemFood(5, 0.6F, false)).setUnlocalizedName("bread"));
		registerItem(298, (String) "leather_helmet",
				(new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 0)).setUnlocalizedName("helmetCloth"));
		registerItem(299, (String) "leather_chestplate",
				(new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 1)).setUnlocalizedName("chestplateCloth"));
		registerItem(300, (String) "leather_leggings",
				(new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 2)).setUnlocalizedName("leggingsCloth"));
		registerItem(301, (String) "leather_boots",
				(new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, 3)).setUnlocalizedName("bootsCloth"));
		registerItem(302, (String) "chainmail_helmet",
				(new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 0)).setUnlocalizedName("helmetChain"));
		registerItem(303, (String) "chainmail_chestplate",
				(new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 1)).setUnlocalizedName("chestplateChain"));
		registerItem(304, (String) "chainmail_leggings",
				(new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 2)).setUnlocalizedName("leggingsChain"));
		registerItem(305, (String) "chainmail_boots",
				(new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, 3)).setUnlocalizedName("bootsChain"));
		registerItem(306, (String) "iron_helmet",
				(new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 0)).setUnlocalizedName("helmetIron"));
		registerItem(307, (String) "iron_chestplate",
				(new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 1)).setUnlocalizedName("chestplateIron"));
		registerItem(308, (String) "iron_leggings",
				(new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 2)).setUnlocalizedName("leggingsIron"));
		registerItem(309, (String) "iron_boots",
				(new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, 3)).setUnlocalizedName("bootsIron"));
		registerItem(310, (String) "diamond_helmet",
				(new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 0)).setUnlocalizedName("helmetDiamond"));
		registerItem(311, (String) "diamond_chestplate",
				(new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 1)).setUnlocalizedName("chestplateDiamond"));
		registerItem(312, (String) "diamond_leggings",
				(new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 2)).setUnlocalizedName("leggingsDiamond"));
		registerItem(313, (String) "diamond_boots",
				(new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, 3)).setUnlocalizedName("bootsDiamond"));
		registerItem(314, (String) "golden_helmet",
				(new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 0)).setUnlocalizedName("helmetGold"));
		registerItem(315, (String) "golden_chestplate",
				(new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 1)).setUnlocalizedName("chestplateGold"));
		registerItem(316, (String) "golden_leggings",
				(new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 2)).setUnlocalizedName("leggingsGold"));
		registerItem(317, (String) "golden_boots",
				(new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, 3)).setUnlocalizedName("bootsGold"));
		registerItem(318, (String) "flint",
				(new Item()).setUnlocalizedName("flint").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(319, (String) "porkchop", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("porkchopRaw"));
		registerItem(320, (String) "cooked_porkchop",
				(new ItemFood(8, 0.8F, true)).setUnlocalizedName("porkchopCooked"));
		registerItem(321, (String) "painting",
				(new ItemHangingEntity(EntityPainting.class)).setUnlocalizedName("painting"));
		registerItem(322, (String) "golden_apple", (new ItemAppleGold(4, 1.2F, false)).setAlwaysEdible()
				.setPotionEffect(Potion.regeneration.id, 5, 1, 1.0F).setUnlocalizedName("appleGold"));
		registerItem(323, (String) "sign", (new ItemSign()).setUnlocalizedName("sign"));
		registerItem(324, (String) "wooden_door", (new ItemDoor(Blocks.oak_door)).setUnlocalizedName("doorOak"));
		Item item = (new ItemBucket(Blocks.air)).setUnlocalizedName("bucket").setMaxStackSize(16);
		registerItem(325, (String) "bucket", item);
		registerItem(326, (String) "water_bucket",
				(new ItemBucket(Blocks.flowing_water)).setUnlocalizedName("bucketWater").setContainerItem(item));
		registerItem(327, (String) "lava_bucket",
				(new ItemBucket(Blocks.flowing_lava)).setUnlocalizedName("bucketLava").setContainerItem(item));
		registerItem(328, (String) "minecart",
				(new ItemMinecart(EntityMinecart.EnumMinecartType.RIDEABLE)).setUnlocalizedName("minecart"));
		registerItem(329, (String) "saddle", (new ItemSaddle()).setUnlocalizedName("saddle"));
		registerItem(330, (String) "iron_door", (new ItemDoor(Blocks.iron_door)).setUnlocalizedName("doorIron"));
		registerItem(331, (String) "redstone",
				(new ItemRedstone()).setUnlocalizedName("redstone").setPotionEffect(PotionHelper.redstoneEffect));
		registerItem(332, (String) "snowball", (new ItemSnowball()).setUnlocalizedName("snowball"));
		registerItem(333, (String) "boat", (new ItemBoat()).setUnlocalizedName("boat"));
		registerItem(334, (String) "leather",
				(new Item()).setUnlocalizedName("leather").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(335, (String) "milk_bucket",
				(new ItemBucketMilk()).setUnlocalizedName("milk").setContainerItem(item));
		registerItem(336, (String) "brick",
				(new Item()).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(337, (String) "clay_ball",
				(new Item()).setUnlocalizedName("clay").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(338, (String) "reeds",
				(new ItemReed(Blocks.reeds)).setUnlocalizedName("reeds").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(339, (String) "paper",
				(new Item()).setUnlocalizedName("paper").setCreativeTab(CreativeTabs.tabMisc));
		registerItem(340, (String) "book",
				(new ItemBook()).setUnlocalizedName("book").setCreativeTab(CreativeTabs.tabMisc));
		registerItem(341, (String) "slime_ball",
				(new Item()).setUnlocalizedName("slimeball").setCreativeTab(CreativeTabs.tabMisc));
		registerItem(342, (String) "chest_minecart",
				(new ItemMinecart(EntityMinecart.EnumMinecartType.CHEST)).setUnlocalizedName("minecartChest"));
		registerItem(343, (String) "furnace_minecart",
				(new ItemMinecart(EntityMinecart.EnumMinecartType.FURNACE)).setUnlocalizedName("minecartFurnace"));
		registerItem(344, (String) "egg", (new ItemEgg()).setUnlocalizedName("egg"));
		registerItem(345, (String) "compass",
				(new Item()).setUnlocalizedName("compass").setCreativeTab(CreativeTabs.tabTools));
		registerItem(346, (String) "fishing_rod", (new ItemFishingRod()).setUnlocalizedName("fishingRod"));
		registerItem(347, (String) "clock",
				(new Item()).setUnlocalizedName("clock").setCreativeTab(CreativeTabs.tabTools));
		registerItem(348, (String) "glowstone_dust", (new Item()).setUnlocalizedName("yellowDust")
				.setPotionEffect(PotionHelper.glowstoneEffect).setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(349, (String) "fish", (new ItemFishFood(false)).setUnlocalizedName("fish").setHasSubtypes(true));
		registerItem(350, (String) "cooked_fish",
				(new ItemFishFood(true)).setUnlocalizedName("fish").setHasSubtypes(true));
		registerItem(351, (String) "dye", (new ItemDye()).setUnlocalizedName("dyePowder"));
		registerItem(352, (String) "bone",
				(new Item()).setUnlocalizedName("bone").setFull3D().setCreativeTab(CreativeTabs.tabMisc));
		registerItem(353, (String) "sugar", (new Item()).setUnlocalizedName("sugar")
				.setPotionEffect(PotionHelper.sugarEffect).setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(354, (String) "cake", (new ItemReed(Blocks.cake)).setMaxStackSize(1).setUnlocalizedName("cake")
				.setCreativeTab(CreativeTabs.tabFood));
		registerItem(355, (String) "bed", (new ItemBed()).setMaxStackSize(1).setUnlocalizedName("bed"));
		registerItem(356, (String) "repeater", (new ItemReed(Blocks.unpowered_repeater)).setUnlocalizedName("diode")
				.setCreativeTab(CreativeTabs.tabRedstone));
		registerItem(357, (String) "cookie", (new ItemFood(2, 0.1F, false)).setUnlocalizedName("cookie"));
		registerItem(358, (String) "filled_map", (new ItemMap()).setUnlocalizedName("map"));
		registerItem(359, (String) "shears", (new ItemShears()).setUnlocalizedName("shears"));
		registerItem(360, (String) "melon", (new ItemFood(2, 0.3F, false)).setUnlocalizedName("melon"));
		registerItem(361, (String) "pumpkin_seeds",
				(new ItemSeeds(Blocks.pumpkin_stem, Blocks.farmland)).setUnlocalizedName("seeds_pumpkin"));
		registerItem(362, (String) "melon_seeds",
				(new ItemSeeds(Blocks.melon_stem, Blocks.farmland)).setUnlocalizedName("seeds_melon"));
		registerItem(363, (String) "beef", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("beefRaw"));
		registerItem(364, (String) "cooked_beef", (new ItemFood(8, 0.8F, true)).setUnlocalizedName("beefCooked"));
		registerItem(365, (String) "chicken", (new ItemFood(2, 0.3F, true))
				.setPotionEffect(Potion.hunger.id, 30, 0, 0.3F).setUnlocalizedName("chickenRaw"));
		registerItem(366, (String) "cooked_chicken", (new ItemFood(6, 0.6F, true)).setUnlocalizedName("chickenCooked"));
		registerItem(367, (String) "rotten_flesh", (new ItemFood(4, 0.1F, true))
				.setPotionEffect(Potion.hunger.id, 30, 0, 0.8F).setUnlocalizedName("rottenFlesh"));
		registerItem(368, (String) "ender_pearl", (new ItemEnderPearl()).setUnlocalizedName("enderPearl"));
		registerItem(369, (String) "blaze_rod",
				(new Item()).setUnlocalizedName("blazeRod").setCreativeTab(CreativeTabs.tabMaterials).setFull3D());
		registerItem(370, (String) "ghast_tear", (new Item()).setUnlocalizedName("ghastTear")
				.setPotionEffect(PotionHelper.ghastTearEffect).setCreativeTab(CreativeTabs.tabBrewing));
		registerItem(371, (String) "gold_nugget",
				(new Item()).setUnlocalizedName("goldNugget").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(372, (String) "nether_wart", (new ItemSeeds(Blocks.nether_wart, Blocks.soul_sand))
				.setUnlocalizedName("netherStalkSeeds").setPotionEffect("+4"));
		registerItem(373, (String) "potion", (new ItemPotion()).setUnlocalizedName("potion"));
		registerItem(374, (String) "glass_bottle", (new ItemGlassBottle()).setUnlocalizedName("glassBottle"));
		registerItem(375, (String) "spider_eye",
				(new ItemFood(2, 0.8F, false)).setPotionEffect(Potion.poison.id, 5, 0, 1.0F)
						.setUnlocalizedName("spiderEye").setPotionEffect(PotionHelper.spiderEyeEffect));
		registerItem(376, (String) "fermented_spider_eye", (new Item()).setUnlocalizedName("fermentedSpiderEye")
				.setPotionEffect(PotionHelper.fermentedSpiderEyeEffect).setCreativeTab(CreativeTabs.tabBrewing));
		registerItem(377, (String) "blaze_powder", (new Item()).setUnlocalizedName("blazePowder")
				.setPotionEffect(PotionHelper.blazePowderEffect).setCreativeTab(CreativeTabs.tabBrewing));
		registerItem(378, (String) "magma_cream", (new Item()).setUnlocalizedName("magmaCream")
				.setPotionEffect(PotionHelper.magmaCreamEffect).setCreativeTab(CreativeTabs.tabBrewing));
		registerItem(379, (String) "brewing_stand", (new ItemReed(Blocks.brewing_stand))
				.setUnlocalizedName("brewingStand").setCreativeTab(CreativeTabs.tabBrewing));
		registerItem(380, (String) "cauldron",
				(new ItemReed(Blocks.cauldron)).setUnlocalizedName("cauldron").setCreativeTab(CreativeTabs.tabBrewing));
		registerItem(381, (String) "ender_eye", (new ItemEnderEye()).setUnlocalizedName("eyeOfEnder"));
		registerItem(382, (String) "speckled_melon", (new Item()).setUnlocalizedName("speckledMelon")
				.setPotionEffect(PotionHelper.speckledMelonEffect).setCreativeTab(CreativeTabs.tabBrewing));
		registerItem(383, (String) "spawn_egg", (new ItemMonsterPlacer()).setUnlocalizedName("monsterPlacer"));
		registerItem(384, (String) "experience_bottle", (new ItemExpBottle()).setUnlocalizedName("expBottle"));
		registerItem(385, (String) "fire_charge", (new ItemFireball()).setUnlocalizedName("fireball"));
		registerItem(386, (String) "writable_book",
				(new ItemWritableBook()).setUnlocalizedName("writingBook").setCreativeTab(CreativeTabs.tabMisc));
		registerItem(387, (String) "written_book",
				(new ItemEditableBook()).setUnlocalizedName("writtenBook").setMaxStackSize(16));
		registerItem(388, (String) "emerald",
				(new Item()).setUnlocalizedName("emerald").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(389, (String) "item_frame",
				(new ItemHangingEntity(EntityItemFrame.class)).setUnlocalizedName("frame"));
		registerItem(390, (String) "flower_pot", (new ItemReed(Blocks.flower_pot)).setUnlocalizedName("flowerPot")
				.setCreativeTab(CreativeTabs.tabDecorations));
		registerItem(391, (String) "carrot",
				(new ItemSeedFood(3, 0.6F, Blocks.carrots, Blocks.farmland)).setUnlocalizedName("carrots"));
		registerItem(392, (String) "potato",
				(new ItemSeedFood(1, 0.3F, Blocks.potatoes, Blocks.farmland)).setUnlocalizedName("potato"));
		registerItem(393, (String) "baked_potato", (new ItemFood(5, 0.6F, false)).setUnlocalizedName("potatoBaked"));
		registerItem(394, (String) "poisonous_potato", (new ItemFood(2, 0.3F, false))
				.setPotionEffect(Potion.poison.id, 5, 0, 0.6F).setUnlocalizedName("potatoPoisonous"));
		registerItem(395, (String) "map", (new ItemEmptyMap()).setUnlocalizedName("emptyMap"));
		registerItem(396, (String) "golden_carrot", (new ItemFood(6, 1.2F, false)).setUnlocalizedName("carrotGolden")
				.setPotionEffect(PotionHelper.goldenCarrotEffect).setCreativeTab(CreativeTabs.tabBrewing));
		registerItem(397, (String) "skull", (new ItemSkull()).setUnlocalizedName("skull"));
		registerItem(398, (String) "carrot_on_a_stick",
				(new ItemCarrotOnAStick()).setUnlocalizedName("carrotOnAStick"));
		registerItem(399, (String) "nether_star",
				(new ItemSimpleFoiled()).setUnlocalizedName("netherStar").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(400, (String) "pumpkin_pie",
				(new ItemFood(8, 0.3F, false)).setUnlocalizedName("pumpkinPie").setCreativeTab(CreativeTabs.tabFood));
		registerItem(401, (String) "fireworks", (new ItemFirework()).setUnlocalizedName("fireworks"));
		registerItem(402, (String) "firework_charge",
				(new ItemFireworkCharge()).setUnlocalizedName("fireworksCharge").setCreativeTab(CreativeTabs.tabMisc));
		registerItem(403, (String) "enchanted_book",
				(new ItemEnchantedBook()).setMaxStackSize(1).setUnlocalizedName("enchantedBook"));
		registerItem(404, (String) "comparator", (new ItemReed(Blocks.unpowered_comparator))
				.setUnlocalizedName("comparator").setCreativeTab(CreativeTabs.tabRedstone));
		registerItem(405, (String) "netherbrick",
				(new Item()).setUnlocalizedName("netherbrick").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(406, (String) "quartz",
				(new Item()).setUnlocalizedName("netherquartz").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(407, (String) "tnt_minecart",
				(new ItemMinecart(EntityMinecart.EnumMinecartType.TNT)).setUnlocalizedName("minecartTnt"));
		registerItem(408, (String) "hopper_minecart",
				(new ItemMinecart(EntityMinecart.EnumMinecartType.HOPPER)).setUnlocalizedName("minecartHopper"));
		registerItem(409, (String) "prismarine_shard",
				(new Item()).setUnlocalizedName("prismarineShard").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(410, (String) "prismarine_crystals",
				(new Item()).setUnlocalizedName("prismarineCrystals").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(411, (String) "rabbit", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("rabbitRaw"));
		registerItem(412, (String) "cooked_rabbit", (new ItemFood(5, 0.6F, true)).setUnlocalizedName("rabbitCooked"));
		registerItem(413, (String) "rabbit_stew", (new ItemSoup(10)).setUnlocalizedName("rabbitStew"));
		registerItem(414, (String) "rabbit_foot", (new Item()).setUnlocalizedName("rabbitFoot")
				.setPotionEffect(PotionHelper.rabbitFootEffect).setCreativeTab(CreativeTabs.tabBrewing));
		registerItem(415, (String) "rabbit_hide",
				(new Item()).setUnlocalizedName("rabbitHide").setCreativeTab(CreativeTabs.tabMaterials));
		registerItem(416, (String) "armor_stand",
				(new ItemArmorStand()).setUnlocalizedName("armorStand").setMaxStackSize(16));
		registerItem(417, (String) "iron_horse_armor", (new Item()).setUnlocalizedName("horsearmormetal")
				.setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
		registerItem(418, (String) "golden_horse_armor", (new Item()).setUnlocalizedName("horsearmorgold")
				.setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
		registerItem(419, (String) "diamond_horse_armor", (new Item()).setUnlocalizedName("horsearmordiamond")
				.setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc));
		registerItem(420, (String) "lead", (new ItemLead()).setUnlocalizedName("leash"));
		registerItem(421, (String) "name_tag", (new ItemNameTag()).setUnlocalizedName("nameTag"));
		registerItem(422, (String) "command_block_minecart",
				(new ItemMinecart(EntityMinecart.EnumMinecartType.COMMAND_BLOCK))
						.setUnlocalizedName("minecartCommandBlock").setCreativeTab((CreativeTabs) null));
		registerItem(423, (String) "mutton", (new ItemFood(2, 0.3F, true)).setUnlocalizedName("muttonRaw"));
		registerItem(424, (String) "cooked_mutton", (new ItemFood(6, 0.8F, true)).setUnlocalizedName("muttonCooked"));
		registerItem(425, (String) "banner", (new ItemBanner()).setUnlocalizedName("banner"));
		registerItem(427, (String) "spruce_door", (new ItemDoor(Blocks.spruce_door)).setUnlocalizedName("doorSpruce"));
		registerItem(428, (String) "birch_door", (new ItemDoor(Blocks.birch_door)).setUnlocalizedName("doorBirch"));
		registerItem(429, (String) "jungle_door", (new ItemDoor(Blocks.jungle_door)).setUnlocalizedName("doorJungle"));
		registerItem(430, (String) "acacia_door", (new ItemDoor(Blocks.acacia_door)).setUnlocalizedName("doorAcacia"));
		registerItem(431, (String) "dark_oak_door",
				(new ItemDoor(Blocks.dark_oak_door)).setUnlocalizedName("doorDarkOak"));
		registerItem(2256, (String) "record_13", (new ItemRecord("13")).setUnlocalizedName("record"));
		registerItem(2257, (String) "record_cat", (new ItemRecord("cat")).setUnlocalizedName("record"));
		registerItem(2258, (String) "record_blocks", (new ItemRecord("blocks")).setUnlocalizedName("record"));
		registerItem(2259, (String) "record_chirp", (new ItemRecord("chirp")).setUnlocalizedName("record"));
		registerItem(2260, (String) "record_far", (new ItemRecord("far")).setUnlocalizedName("record"));
		registerItem(2261, (String) "record_mall", (new ItemRecord("mall")).setUnlocalizedName("record"));
		registerItem(2262, (String) "record_mellohi", (new ItemRecord("mellohi")).setUnlocalizedName("record"));
		registerItem(2263, (String) "record_stal", (new ItemRecord("stal")).setUnlocalizedName("record"));
		registerItem(2264, (String) "record_strad", (new ItemRecord("strad")).setUnlocalizedName("record"));
		registerItem(2265, (String) "record_ward", (new ItemRecord("ward")).setUnlocalizedName("record"));
		registerItem(2266, (String) "record_11", (new ItemRecord("11")).setUnlocalizedName("record"));
		registerItem(2267, (String) "record_wait", (new ItemRecord("wait")).setUnlocalizedName("record"));
	}

	/**+
	 * Register the given Item as the ItemBlock for the given Block.
	 */
	private static void registerItemBlock(Block blockIn) {
		registerItemBlock(blockIn, new ItemBlock(blockIn));
	}

	/**+
	 * Register the given Item as the ItemBlock for the given Block.
	 */
	protected static void registerItemBlock(Block blockIn, Item itemIn) {
		registerItem(Block.getIdFromBlock(blockIn), (ResourceLocation) Block.blockRegistry.getNameForObject(blockIn),
				itemIn);
		BLOCK_TO_ITEM.put(blockIn, itemIn);
	}

	private static void registerItem(int id, String textualID, Item itemIn) {
		registerItem(id, new ResourceLocation(textualID), itemIn);
	}

	private static void registerItem(int id, ResourceLocation textualID, Item itemIn) {
		itemRegistry.register(id, textualID, itemIn);
	}

	public static enum ToolMaterial {
		WOOD(0, 59, 2.0F, 0.0F, 15), STONE(1, 131, 4.0F, 1.0F, 5), IRON(2, 250, 6.0F, 2.0F, 14),
		EMERALD(3, 1561, 8.0F, 3.0F, 10), GOLD(0, 32, 12.0F, 0.0F, 22);

		private final int harvestLevel;
		private final int maxUses;
		private final float efficiencyOnProperMaterial;
		private final float damageVsEntity;
		private final int enchantability;

		private ToolMaterial(int harvestLevel, int maxUses, float efficiency, float damageVsEntity,
				int enchantability) {
			this.harvestLevel = harvestLevel;
			this.maxUses = maxUses;
			this.efficiencyOnProperMaterial = efficiency;
			this.damageVsEntity = damageVsEntity;
			this.enchantability = enchantability;
		}

		public int getMaxUses() {
			return this.maxUses;
		}

		public float getEfficiencyOnProperMaterial() {
			return this.efficiencyOnProperMaterial;
		}

		public float getDamageVsEntity() {
			return this.damageVsEntity;
		}

		public int getHarvestLevel() {
			return this.harvestLevel;
		}

		public int getEnchantability() {
			return this.enchantability;
		}

		public Item getRepairItem() {
			return this == WOOD ? Item.getItemFromBlock(Blocks.planks)
					: (this == STONE ? Item.getItemFromBlock(Blocks.cobblestone)
							: (this == GOLD ? Items.gold_ingot
									: (this == IRON ? Items.iron_ingot : (this == EMERALD ? Items.diamond : null))));
		}
	}

	public float getHeldItemBrightnessEagler(ItemStack itemStack) {
		return 0.0f;
	}

	public boolean shouldUseOnTouchEagler(ItemStack itemStack) {
		/**+
		 * returns the action that specifies what animation to play when
		 * the items is being used
		 */
		return getItemUseAction(itemStack) != EnumAction.NONE;
	}
}