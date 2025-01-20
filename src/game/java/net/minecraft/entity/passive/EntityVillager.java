package net.minecraft.entity.passive;

import java.util.Arrays;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
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
public class EntityVillager extends EntityAgeable implements IMerchant, INpc {
	private int randomTickDivider;
	private boolean isMating;
	private boolean isPlaying;
	Village villageObj;
	private EntityPlayer buyingPlayer;
	private MerchantRecipeList buyingList;
	private int timeUntilReset;
	private boolean needsInitilization;
	private boolean isWillingToMate;
	private int wealth;
	private String lastBuyingPlayer;
	private int careerId;
	private int careerLevel;
	private boolean isLookingForHome;
	private boolean areAdditionalTasksSet;
	private InventoryBasic villagerInventory;
	/**+
	 * A multi-dimensional array mapping the various professions,
	 * careers and career levels that a Villager may offer
	 */
	private static List<List<List<List<EntityVillager.ITradeList>>>> DEFAULT_TRADE_LIST_MAP = null;

	public static void bootstrap() {
		DEFAULT_TRADE_LIST_MAP = Arrays.asList(
				Arrays.asList(Arrays.asList(Arrays.asList(
						new EntityVillager.EmeraldForItems(Items.wheat, new EntityVillager.PriceInfo(18, 22)),
						new EntityVillager.EmeraldForItems(Items.potato, new EntityVillager.PriceInfo(15, 19)),
						new EntityVillager.EmeraldForItems(Items.carrot,
								new EntityVillager.PriceInfo(15, 19)),
						new EntityVillager.ListItemForEmeralds(Items.bread, new EntityVillager.PriceInfo(-4, -2))),
						Arrays.asList(
								new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.pumpkin),
										new EntityVillager.PriceInfo(8, 13)),
								new EntityVillager.ListItemForEmeralds(Items.pumpkin_pie,
										new EntityVillager.PriceInfo(-3, -2))),
						Arrays.asList(
								new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.melon_block),
										new EntityVillager.PriceInfo(7, 12)),
								new EntityVillager.ListItemForEmeralds(Items.apple,
										new EntityVillager.PriceInfo(-5, -7))),
						Arrays.asList(
								new EntityVillager.ListItemForEmeralds(Items.cookie,
										new EntityVillager.PriceInfo(-6, -10)),
								new EntityVillager.ListItemForEmeralds(Items.cake,
										new EntityVillager.PriceInfo(1, 1)))),
						Arrays.asList(Arrays.asList(
								new EntityVillager.EmeraldForItems(Items.string, new EntityVillager.PriceInfo(15, 20)),
								new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)),
								new EntityVillager.ItemAndEmeraldToItem(Items.fish, new EntityVillager.PriceInfo(6, 6),
										Items.cooked_fish, new EntityVillager.PriceInfo(6, 6))),
								Arrays.asList(new EntityVillager.ListEnchantedItemForEmeralds(Items.fishing_rod,
										new EntityVillager.PriceInfo(7, 8)))),
						Arrays.asList(
								Arrays.asList(
										new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.wool),
												new EntityVillager.PriceInfo(16, 22)),
										new EntityVillager.ListItemForEmeralds(Items.shears,
												new EntityVillager.PriceInfo(3, 4))),
								Arrays.asList(
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 1),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 2),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 3),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 4),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 5),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 6),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 7),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 8),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 9),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 10),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 13),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 14),
												new EntityVillager.PriceInfo(1, 2)),
										new EntityVillager.ListItemForEmeralds(
												new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15),
												new EntityVillager.PriceInfo(1, 2)))),
						Arrays.asList(Arrays.asList(
								new EntityVillager.EmeraldForItems(Items.string, new EntityVillager.PriceInfo(15, 20)),
								new EntityVillager.ListItemForEmeralds(Items.arrow,
										new EntityVillager.PriceInfo(-12, -8))),
								Arrays.asList(
										new EntityVillager.ListItemForEmeralds(Items.bow,
												new EntityVillager.PriceInfo(2, 3)),
										new EntityVillager.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.gravel),
												new EntityVillager.PriceInfo(10, 10), Items.flint,
												new EntityVillager.PriceInfo(6, 10))))),
				Arrays.asList(Arrays.asList(
						Arrays.asList(
								new EntityVillager.EmeraldForItems(Items.paper, new EntityVillager.PriceInfo(24, 36)),
								new EntityVillager.ListEnchantedBookForEmeralds()),
						Arrays.asList(
								new EntityVillager.EmeraldForItems(Items.book, new EntityVillager.PriceInfo(8, 10)),
								new EntityVillager.ListItemForEmeralds(Items.compass,
										new EntityVillager.PriceInfo(10, 12)),
								new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.bookshelf),
										new EntityVillager.PriceInfo(3, 4))),
						Arrays.asList(
								new EntityVillager.EmeraldForItems(Items.written_book,
										new EntityVillager.PriceInfo(2, 2)),
								new EntityVillager.ListItemForEmeralds(Items.clock,
										new EntityVillager.PriceInfo(10, 12)),
								new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glass),
										new EntityVillager.PriceInfo(-5, -3))),
						Arrays.asList(new EntityVillager.ListEnchantedBookForEmeralds()),
						Arrays.asList(new EntityVillager.ListEnchantedBookForEmeralds()),
						Arrays.asList(new EntityVillager.ListItemForEmeralds(Items.name_tag,
								new EntityVillager.PriceInfo(20, 22))))),
				Arrays.asList(Arrays.asList(Arrays.asList(
						new EntityVillager.EmeraldForItems(Items.rotten_flesh, new EntityVillager.PriceInfo(36, 40)),
						new EntityVillager.EmeraldForItems(Items.gold_ingot, new EntityVillager.PriceInfo(8, 10))),
						Arrays.asList(
								new EntityVillager.ListItemForEmeralds(Items.redstone,
										new EntityVillager.PriceInfo(-4, -1)),
								new EntityVillager.ListItemForEmeralds(
										new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()),
										new EntityVillager.PriceInfo(-2, -1))),
						Arrays.asList(
								new EntityVillager.ListItemForEmeralds(Items.ender_eye,
										new EntityVillager.PriceInfo(7, 11)),
								new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glowstone),
										new EntityVillager.PriceInfo(-3, -1))),
						Arrays.asList(new EntityVillager.ListItemForEmeralds(Items.experience_bottle,
								new EntityVillager.PriceInfo(3, 11))))),
				Arrays.asList(
						Arrays.asList(Arrays.asList(
								new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)),
								new EntityVillager.ListItemForEmeralds(Items.iron_helmet,
										new EntityVillager.PriceInfo(4, 6))),
								Arrays.asList(
										new EntityVillager.EmeraldForItems(Items.iron_ingot,
												new EntityVillager.PriceInfo(7, 9)),
										new EntityVillager.ListItemForEmeralds(Items.iron_chestplate,
												new EntityVillager.PriceInfo(10, 14))),
								Arrays.asList(
										new EntityVillager.EmeraldForItems(Items.diamond,
												new EntityVillager.PriceInfo(3, 4)),
										new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_chestplate,
												new EntityVillager.PriceInfo(16, 19))),
								Arrays.asList(
										new EntityVillager.ListItemForEmeralds(Items.chainmail_boots,
												new EntityVillager.PriceInfo(5, 7)),
										new EntityVillager.ListItemForEmeralds(Items.chainmail_leggings,
												new EntityVillager.PriceInfo(9, 11)),
										new EntityVillager.ListItemForEmeralds(Items.chainmail_helmet,
												new EntityVillager.PriceInfo(5, 7)),
										new EntityVillager.ListItemForEmeralds(Items.chainmail_chestplate,
												new EntityVillager.PriceInfo(11, 15)))),
						Arrays.asList(Arrays.asList(
								new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)),
								new EntityVillager.ListItemForEmeralds(Items.iron_axe,
										new EntityVillager.PriceInfo(6, 8))),
								Arrays.asList(
										new EntityVillager.EmeraldForItems(Items.iron_ingot,
												new EntityVillager.PriceInfo(7, 9)),
										new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_sword,
												new EntityVillager.PriceInfo(9, 10))),
								Arrays.asList(
										new EntityVillager.EmeraldForItems(Items.diamond,
												new EntityVillager.PriceInfo(3, 4)),
										new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_sword,
												new EntityVillager.PriceInfo(12, 15)),
										new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_axe,
												new EntityVillager.PriceInfo(9, 12)))),
						Arrays.asList(Arrays.asList(
								new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)),
								new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_shovel,
										new EntityVillager.PriceInfo(5, 7))),
								Arrays.asList(
										new EntityVillager.EmeraldForItems(Items.iron_ingot,
												new EntityVillager.PriceInfo(7, 9)),
										new EntityVillager.ListEnchantedItemForEmeralds(Items.iron_pickaxe,
												new EntityVillager.PriceInfo(9, 11))),
								Arrays.asList(
										new EntityVillager.EmeraldForItems(Items.diamond,
												new EntityVillager.PriceInfo(3, 4)),
										new EntityVillager.ListEnchantedItemForEmeralds(Items.diamond_pickaxe,
												new EntityVillager.PriceInfo(12, 15))))),
				Arrays.asList(Arrays.asList(Arrays.asList(
						new EntityVillager.EmeraldForItems(Items.porkchop, new EntityVillager.PriceInfo(14, 18)),
						new EntityVillager.EmeraldForItems(Items.chicken, new EntityVillager.PriceInfo(14, 18))),
						Arrays.asList(
								new EntityVillager.EmeraldForItems(Items.coal, new EntityVillager.PriceInfo(16, 24)),
								new EntityVillager.ListItemForEmeralds(Items.cooked_porkchop,
										new EntityVillager.PriceInfo(-7, -5)),
								new EntityVillager.ListItemForEmeralds(Items.cooked_chicken,
										new EntityVillager.PriceInfo(-8, -6)))),
						Arrays.asList(Arrays.asList(
								new EntityVillager.EmeraldForItems(Items.leather, new EntityVillager.PriceInfo(9, 12)),
								new EntityVillager.ListItemForEmeralds(Items.leather_leggings,
										new EntityVillager.PriceInfo(2, 4))),
								Arrays.asList(new EntityVillager.ListEnchantedItemForEmeralds(Items.leather_chestplate,
										new EntityVillager.PriceInfo(7, 12))),
								Arrays.asList(new EntityVillager.ListItemForEmeralds(Items.saddle,
										new EntityVillager.PriceInfo(8, 10))))));
	}

	public EntityVillager(World worldIn) {
		this(worldIn, 0);
	}

	public EntityVillager(World worldIn, int professionId) {
		super(worldIn);
		this.villagerInventory = new InventoryBasic("Items", false, 8);
		this.setProfession(professionId);
		this.setSize(0.6F, 1.8F);
		((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
		((PathNavigateGround) this.getNavigator()).setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
		this.tasks.addTask(1, new EntityAITradePlayer(this));
		this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
		this.tasks.addTask(2, new EntityAIMoveIndoors(this));
		this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
		this.tasks.addTask(6, new EntityAIVillagerMate(this));
		this.tasks.addTask(7, new EntityAIFollowGolem(this));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(9, new EntityAIVillagerInteract(this));
		this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.setCanPickUpLoot(true);
	}

	private void setAdditionalAItasks() {
		if (!this.areAdditionalTasksSet) {
			this.areAdditionalTasksSet = true;
			if (this.isChild()) {
				this.tasks.addTask(8, new EntityAIPlay(this, 0.32D));
			} else if (this.getProfession() == 0) {
				this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6D));
			}

		}
	}

	/**+
	 * This is called when Entity's growing age timer reaches 0
	 * (negative values are considered as a child, positive as an
	 * adult)
	 */
	protected void onGrowingAdult() {
		if (this.getProfession() == 0) {
			this.tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6D));
		}

		super.onGrowingAdult();
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
	}

	protected void updateAITasks() {
		if (--this.randomTickDivider <= 0) {
			BlockPos blockpos = new BlockPos(this);
			this.worldObj.getVillageCollection().addToVillagerPositionList(blockpos);
			this.randomTickDivider = 70 + this.rand.nextInt(50);
			this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(blockpos, 32);
			if (this.villageObj == null) {
				this.detachHome();
			} else {
				BlockPos blockpos1 = this.villageObj.getCenter();
				this.setHomePosAndDistance(blockpos1, (int) ((float) this.villageObj.getVillageRadius() * 1.0F));
				if (this.isLookingForHome) {
					this.isLookingForHome = false;
					this.villageObj.setDefaultPlayerReputation(5);
				}
			}
		}

		if (!this.isTrading() && this.timeUntilReset > 0) {
			--this.timeUntilReset;
			if (this.timeUntilReset <= 0) {
				if (this.needsInitilization) {
					for (int i = 0, l = this.buyingList.size(); i < l; ++i) {
						MerchantRecipe merchantrecipe = this.buyingList.get(i);
						if (merchantrecipe.isRecipeDisabled()) {
							merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
						}
					}

					this.populateBuyingList();
					this.needsInitilization = false;
					if (this.villageObj != null && this.lastBuyingPlayer != null) {
						this.worldObj.setEntityState(this, (byte) 14);
						this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
					}
				}

				this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
			}
		}

		super.updateAITasks();
	}

	/**+
	 * Called when a player interacts with a mob. e.g. gets milk
	 * from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer entityplayer) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		boolean flag = itemstack != null && itemstack.getItem() == Items.spawn_egg;
		if (!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
			if (!this.worldObj.isRemote && (this.buyingList == null || this.buyingList.size() > 0)) {
				this.setCustomer(entityplayer);
				entityplayer.displayVillagerTradeGui(this);
			}

			entityplayer.triggerAchievement(StatList.timesTalkedToVillagerStat);
			return true;
		} else {
			return super.interact(entityplayer);
		}
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Integer.valueOf(0));
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("Profession", this.getProfession());
		nbttagcompound.setInteger("Riches", this.wealth);
		nbttagcompound.setInteger("Career", this.careerId);
		nbttagcompound.setInteger("CareerLevel", this.careerLevel);
		nbttagcompound.setBoolean("Willing", this.isWillingToMate);
		if (this.buyingList != null) {
			try {
				nbttagcompound.setTag("Offers", this.buyingList.getRecipiesAsTags());
			} catch (Throwable t) {
				this.buyingList = null; // workaround
			}
		}

		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
			ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
			if (itemstack != null) {
				nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
			}
		}

		nbttagcompound.setTag("Inventory", nbttaglist);
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setProfession(nbttagcompound.getInteger("Profession"));
		this.wealth = nbttagcompound.getInteger("Riches");
		this.careerId = nbttagcompound.getInteger("Career");
		this.careerLevel = nbttagcompound.getInteger("CareerLevel");
		this.isWillingToMate = nbttagcompound.getBoolean("Willing");
		if (nbttagcompound.hasKey("Offers", 10)) {
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Offers");
			this.buyingList = new MerchantRecipeList(nbttagcompound1);
		}

		NBTTagList nbttaglist = nbttagcompound.getTagList("Inventory", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
			if (itemstack != null) {
				this.villagerInventory.func_174894_a(itemstack);
			}
		}

		this.setCanPickUpLoot(true);
		this.setAdditionalAItasks();
	}

	/**+
	 * Determines if an entity can be despawned, used on idle far
	 * away entities
	 */
	protected boolean canDespawn() {
		return false;
	}

	/**+
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return this.isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
	}

	/**+
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.villager.hit";
	}

	/**+
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.villager.death";
	}

	public void setProfession(int professionId) {
		this.dataWatcher.updateObject(16, Integer.valueOf(professionId));
	}

	public int getProfession() {
		return Math.max(this.dataWatcher.getWatchableObjectInt(16) % 5, 0);
	}

	public boolean isMating() {
		return this.isMating;
	}

	public void setMating(boolean mating) {
		this.isMating = mating;
	}

	public void setPlaying(boolean playing) {
		this.isPlaying = playing;
	}

	public boolean isPlaying() {
		return this.isPlaying;
	}

	public void setRevengeTarget(EntityLivingBase entitylivingbase) {
		super.setRevengeTarget(entitylivingbase);
		if (this.villageObj != null && entitylivingbase != null) {
			this.villageObj.addOrRenewAgressor(entitylivingbase);
			if (entitylivingbase instanceof EntityPlayer) {
				byte b0 = -1;
				if (this.isChild()) {
					b0 = -3;
				}

				this.villageObj.setReputationForPlayer(entitylivingbase.getName(), b0);
				if (this.isEntityAlive()) {
					this.worldObj.setEntityState(this, (byte) 13);
				}
			}
		}

	}

	/**+
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource damagesource) {
		if (this.villageObj != null) {
			Entity entity = damagesource.getEntity();
			if (entity != null) {
				if (entity instanceof EntityPlayer) {
					this.villageObj.setReputationForPlayer(entity.getName(), -2);
				} else if (entity instanceof IMob) {
					this.villageObj.endMatingSeason();
				}
			} else {
				EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
				if (entityplayer != null) {
					this.villageObj.endMatingSeason();
				}
			}
		}

		super.onDeath(damagesource);
	}

	public void setCustomer(EntityPlayer entityplayer) {
		this.buyingPlayer = entityplayer;
	}

	public EntityPlayer getCustomer() {
		return this.buyingPlayer;
	}

	public boolean isTrading() {
		return this.buyingPlayer != null;
	}

	/**+
	 * Returns current or updated value of {@link #isWillingToMate}
	 */
	public boolean getIsWillingToMate(boolean updateFirst) {
		if (!this.isWillingToMate && updateFirst && this.func_175553_cp()) {
			boolean flag = false;

			for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
				ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
				if (itemstack != null) {
					if (itemstack.getItem() == Items.bread && itemstack.stackSize >= 3) {
						flag = true;
						this.villagerInventory.decrStackSize(i, 3);
					} else if ((itemstack.getItem() == Items.potato || itemstack.getItem() == Items.carrot)
							&& itemstack.stackSize >= 12) {
						flag = true;
						this.villagerInventory.decrStackSize(i, 12);
					}
				}

				if (flag) {
					this.worldObj.setEntityState(this, (byte) 18);
					this.isWillingToMate = true;
					break;
				}
			}
		}

		return this.isWillingToMate;
	}

	public void setIsWillingToMate(boolean willingToTrade) {
		this.isWillingToMate = willingToTrade;
	}

	public void useRecipe(MerchantRecipe merchantrecipe) {
		merchantrecipe.incrementToolUses();
		this.livingSoundTime = -this.getTalkInterval();
		this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
		int i = 3 + this.rand.nextInt(4);
		if (merchantrecipe.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
			this.timeUntilReset = 40;
			this.needsInitilization = true;
			this.isWillingToMate = true;
			if (this.buyingPlayer != null) {
				this.lastBuyingPlayer = this.buyingPlayer.getName();
			} else {
				this.lastBuyingPlayer = null;
			}

			i += 5;
		}

		if (merchantrecipe.getItemToBuy().getItem() == Items.emerald) {
			this.wealth += merchantrecipe.getItemToBuy().stackSize;
		}

		if (merchantrecipe.getRewardsExp()) {
			this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, i));
		}

	}

	/**+
	 * Notifies the merchant of a possible merchantrecipe being
	 * fulfilled or not. Usually, this is just a sound byte being
	 * played depending if the suggested itemstack is not null.
	 */
	public void verifySellingItem(ItemStack itemstack) {
		if (!this.worldObj.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
			this.livingSoundTime = -this.getTalkInterval();
			if (itemstack != null) {
				this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
			} else {
				this.playSound("mob.villager.no", this.getSoundVolume(), this.getSoundPitch());
			}
		}

	}

	public MerchantRecipeList getRecipes(EntityPlayer var1) {
		if (this.buyingList == null) {
			this.populateBuyingList();
		}

		return this.buyingList;
	}

	private void populateBuyingList() {
		List<List<List<EntityVillager.ITradeList>>> aentityvillager$itradelist = DEFAULT_TRADE_LIST_MAP
				.get(this.getProfession());
		if (this.careerId != 0 && this.careerLevel != 0) {
			++this.careerLevel;
		} else {
			this.careerId = this.rand.nextInt(aentityvillager$itradelist.size()) + 1;
			this.careerLevel = 1;
		}

		if (this.buyingList == null) {
			this.buyingList = new MerchantRecipeList();
		}

		int i = this.careerId - 1;
		int j = this.careerLevel - 1;
		List<List<EntityVillager.ITradeList>> aentityvillager$itradelist1 = aentityvillager$itradelist.get(i);
		if (j >= 0 && j < aentityvillager$itradelist1.size()) {
			List<EntityVillager.ITradeList> aentityvillager$itradelist2 = aentityvillager$itradelist1.get(j);

			for (int k = 0, l = aentityvillager$itradelist2.size(); k < l; ++k) {
				aentityvillager$itradelist2.get(k).modifyMerchantRecipeList(this.buyingList, this.rand);
			}
		}

	}

	public void setRecipes(MerchantRecipeList var1) {
	}

	/**+
	 * Get the formatted ChatComponent that will be used for the
	 * sender's username in chat
	 */
	public IChatComponent getDisplayName() {
		return getDisplayNameImpl(false);
	}

	public IChatComponent getDisplayNameProfanityFilter() {
		return getDisplayNameImpl(true);
	}

	private IChatComponent getDisplayNameImpl(boolean filter) {
		String s = filter ? this.getCustomNameTagProfanityFilter() : this.getCustomNameTag();
		if (s != null && s.length() > 0) {
			ChatComponentText chatcomponenttext = new ChatComponentText(s);
			chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
			chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
			return chatcomponenttext;
		} else {
			if (this.buyingList == null) {
				this.populateBuyingList();
			}

			String s1 = null;
			switch (this.getProfession()) {
			case 0:
				if (this.careerId == 1) {
					s1 = "farmer";
				} else if (this.careerId == 2) {
					s1 = "fisherman";
				} else if (this.careerId == 3) {
					s1 = "shepherd";
				} else if (this.careerId == 4) {
					s1 = "fletcher";
				}
				break;
			case 1:
				s1 = "librarian";
				break;
			case 2:
				s1 = "cleric";
				break;
			case 3:
				if (this.careerId == 1) {
					s1 = "armor";
				} else if (this.careerId == 2) {
					s1 = "weapon";
				} else if (this.careerId == 3) {
					s1 = "tool";
				}
				break;
			case 4:
				if (this.careerId == 1) {
					s1 = "butcher";
				} else if (this.careerId == 2) {
					s1 = "leather";
				}
			}

			if (s1 != null) {
				ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
						"entity.Villager." + s1, new Object[0]);
				chatcomponenttranslation.getChatStyle().setChatHoverEvent(this.getHoverEvent());
				chatcomponenttranslation.getChatStyle().setInsertion(this.getUniqueID().toString());
				return chatcomponenttranslation;
			} else {
				return filter ? super.getDisplayNameProfanityFilter() : super.getDisplayName();
			}
		}
	}

	public float getEyeHeight() {
		float f = 1.62F;
		if (this.isChild()) {
			f = (float) ((double) f - 0.81D);
		}

		return f;
	}

	public void handleStatusUpdate(byte b0) {
		if (b0 == 12) {
			this.spawnParticles(EnumParticleTypes.HEART);
		} else if (b0 == 13) {
			this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
		} else if (b0 == 14) {
			this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
		} else {
			super.handleStatusUpdate(b0);
		}

	}

	private void spawnParticles(EnumParticleTypes particleType) {
		for (int i = 0; i < 5; ++i) {
			double d0 = this.rand.nextGaussian() * 0.02D;
			double d1 = this.rand.nextGaussian() * 0.02D;
			double d2 = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle(particleType,
					this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
					this.posY + 1.0D + (double) (this.rand.nextFloat() * this.height),
					this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2,
					new int[0]);
		}

	}

	/**+
	 * Called only once on an entity when first time spawned, via
	 * egg, mob spawner, natural spawning etc, but not called when
	 * entity is reloaded from nbt. Mainly used for initializing
	 * attributes and inventory
	 */
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyinstance,
			IEntityLivingData ientitylivingdata) {
		ientitylivingdata = super.onInitialSpawn(difficultyinstance, ientitylivingdata);
		this.setProfession(this.worldObj.rand.nextInt(5));
		this.setAdditionalAItasks();
		return ientitylivingdata;
	}

	public void setLookingForHome() {
		this.isLookingForHome = true;
	}

	public EntityVillager createChild(EntityAgeable var1) {
		EntityVillager entityvillager = new EntityVillager(this.worldObj);
		entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityvillager)),
				(IEntityLivingData) null);
		return entityvillager;
	}

	public boolean allowLeashing() {
		return false;
	}

	/**+
	 * Called when a lightning bolt hits the entity.
	 */
	public void onStruckByLightning(EntityLightningBolt var1) {
		if (!this.worldObj.isRemote && !this.isDead) {
			EntityWitch entitywitch = new EntityWitch(this.worldObj);
			entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			entitywitch.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entitywitch)),
					(IEntityLivingData) null);
			entitywitch.setNoAI(this.isAIDisabled());
			if (this.hasCustomName()) {
				entitywitch.setCustomNameTag(this.getCustomNameTag());
				entitywitch.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
			}

			this.worldObj.spawnEntityInWorld(entitywitch);
			this.setDead();
		}
	}

	public InventoryBasic getVillagerInventory() {
		return this.villagerInventory;
	}

	/**+
	 * Tests if this entity should pickup a weapon or an armor.
	 * Entity drops current weapon or armor if the new one is
	 * better.
	 */
	protected void updateEquipmentIfNeeded(EntityItem entityitem) {
		ItemStack itemstack = entityitem.getEntityItem();
		Item item = itemstack.getItem();
		if (this.canVillagerPickupItem(item)) {
			ItemStack itemstack1 = this.villagerInventory.func_174894_a(itemstack);
			if (itemstack1 == null) {
				entityitem.setDead();
			} else {
				itemstack.stackSize = itemstack1.stackSize;
			}
		}

	}

	private boolean canVillagerPickupItem(Item itemIn) {
		return itemIn == Items.bread || itemIn == Items.potato || itemIn == Items.carrot || itemIn == Items.wheat
				|| itemIn == Items.wheat_seeds;
	}

	public boolean func_175553_cp() {
		return this.hasEnoughItems(1);
	}

	/**+
	 * Used by {@link
	 * net.minecraft.entity.ai.EntityAIVillagerInteract
	 * EntityAIVillagerInteract} to check if the villager can give
	 * some items from an inventory to another villager.
	 */
	public boolean canAbondonItems() {
		return this.hasEnoughItems(2);
	}

	public boolean func_175557_cr() {
		boolean flag = this.getProfession() == 0;
		return flag ? !this.hasEnoughItems(5) : !this.hasEnoughItems(1);
	}

	/**+
	 * Returns true if villager has enough items in inventory
	 */
	private boolean hasEnoughItems(int multiplier) {
		boolean flag = this.getProfession() == 0;

		for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
			ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
			if (itemstack != null) {
				if (itemstack.getItem() == Items.bread && itemstack.stackSize >= 3 * multiplier
						|| itemstack.getItem() == Items.potato && itemstack.stackSize >= 12 * multiplier
						|| itemstack.getItem() == Items.carrot && itemstack.stackSize >= 12 * multiplier) {
					return true;
				}

				if (flag && itemstack.getItem() == Items.wheat && itemstack.stackSize >= 9 * multiplier) {
					return true;
				}
			}
		}

		return false;
	}

	/**+
	 * Returns true if villager has seeds, potatoes or carrots in
	 * inventory
	 */
	public boolean isFarmItemInInventory() {
		for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
			ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
			if (itemstack != null && (itemstack.getItem() == Items.wheat_seeds || itemstack.getItem() == Items.potato
					|| itemstack.getItem() == Items.carrot)) {
				return true;
			}
		}

		return false;
	}

	public boolean replaceItemInInventory(int i, ItemStack itemstack) {
		if (super.replaceItemInInventory(i, itemstack)) {
			return true;
		} else {
			int j = i - 300;
			if (j >= 0 && j < this.villagerInventory.getSizeInventory()) {
				this.villagerInventory.setInventorySlotContents(j, itemstack);
				return true;
			} else {
				return false;
			}
		}
	}

	static class EmeraldForItems implements EntityVillager.ITradeList {
		public Item sellItem;
		public EntityVillager.PriceInfo price;

		public EmeraldForItems(Item itemIn, EntityVillager.PriceInfo priceIn) {
			this.sellItem = itemIn;
			this.price = priceIn;
		}

		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, EaglercraftRandom random) {
			int i = 1;
			if (this.price != null) {
				i = this.price.getPrice(random);
			}

			recipeList.add(new MerchantRecipe(new ItemStack(this.sellItem, i, 0), Items.emerald));
		}
	}

	interface ITradeList {
		void modifyMerchantRecipeList(MerchantRecipeList var1, EaglercraftRandom var2);
	}

	static class ItemAndEmeraldToItem implements EntityVillager.ITradeList {
		public ItemStack field_179411_a;
		public EntityVillager.PriceInfo field_179409_b;
		public ItemStack field_179410_c;
		public EntityVillager.PriceInfo field_179408_d;

		public ItemAndEmeraldToItem(Item parItem, EntityVillager.PriceInfo parPriceInfo, Item parItem2,
				EntityVillager.PriceInfo parPriceInfo2) {
			this.field_179411_a = new ItemStack(parItem);
			this.field_179409_b = parPriceInfo;
			this.field_179410_c = new ItemStack(parItem2);
			this.field_179408_d = parPriceInfo2;
		}

		public void modifyMerchantRecipeList(MerchantRecipeList merchantrecipelist, EaglercraftRandom random) {
			int i = 1;
			if (this.field_179409_b != null) {
				i = this.field_179409_b.getPrice(random);
			}

			int j = 1;
			if (this.field_179408_d != null) {
				j = this.field_179408_d.getPrice(random);
			}

			merchantrecipelist.add(new MerchantRecipe(
					new ItemStack(this.field_179411_a.getItem(), i, this.field_179411_a.getMetadata()),
					new ItemStack(Items.emerald),
					new ItemStack(this.field_179410_c.getItem(), j, this.field_179410_c.getMetadata())));
		}
	}

	static class ListEnchantedBookForEmeralds implements EntityVillager.ITradeList {
		public void modifyMerchantRecipeList(MerchantRecipeList merchantrecipelist, EaglercraftRandom random) {
			Enchantment enchantment = Enchantment.enchantmentsBookList[random
					.nextInt(Enchantment.enchantmentsBookList.length)];
			int i = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
			ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i));
			int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
			if (j > 64) {
				j = 64;
			}

			merchantrecipelist
					.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, j), itemstack));
		}
	}

	static class ListEnchantedItemForEmeralds implements EntityVillager.ITradeList {
		public ItemStack field_179407_a;
		public EntityVillager.PriceInfo field_179406_b;

		public ListEnchantedItemForEmeralds(Item parItem, EntityVillager.PriceInfo parPriceInfo) {
			this.field_179407_a = new ItemStack(parItem);
			this.field_179406_b = parPriceInfo;
		}

		public void modifyMerchantRecipeList(MerchantRecipeList merchantrecipelist, EaglercraftRandom random) {
			int i = 1;
			if (this.field_179406_b != null) {
				i = this.field_179406_b.getPrice(random);
			}

			ItemStack itemstack = new ItemStack(Items.emerald, i, 0);
			ItemStack itemstack1 = new ItemStack(this.field_179407_a.getItem(), 1, this.field_179407_a.getMetadata());
			itemstack1 = EnchantmentHelper.addRandomEnchantment(random, itemstack1, 5 + random.nextInt(15));
			merchantrecipelist.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}

	static class ListItemForEmeralds implements EntityVillager.ITradeList {
		public ItemStack field_179403_a;
		public EntityVillager.PriceInfo field_179402_b;

		public ListItemForEmeralds(Item par1Item, EntityVillager.PriceInfo priceInfo) {
			this.field_179403_a = new ItemStack(par1Item);
			this.field_179402_b = priceInfo;
		}

		public ListItemForEmeralds(ItemStack stack, EntityVillager.PriceInfo priceInfo) {
			this.field_179403_a = stack;
			this.field_179402_b = priceInfo;
		}

		public void modifyMerchantRecipeList(MerchantRecipeList merchantrecipelist, EaglercraftRandom random) {
			int i = 1;
			if (this.field_179402_b != null) {
				i = this.field_179402_b.getPrice(random);
			}

			ItemStack itemstack;
			ItemStack itemstack1;
			if (i < 0) {
				itemstack = new ItemStack(Items.emerald, 1, 0);
				itemstack1 = new ItemStack(this.field_179403_a.getItem(), -i, this.field_179403_a.getMetadata());
			} else {
				itemstack = new ItemStack(Items.emerald, i, 0);
				itemstack1 = new ItemStack(this.field_179403_a.getItem(), 1, this.field_179403_a.getMetadata());
			}

			merchantrecipelist.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}

	static class PriceInfo {
		private int a;
		private int b;

		public PriceInfo(int parInt1, int parInt2) {
			this.a = parInt1;
			this.b = parInt2;
		}

		public int getFirst() {
			return this.a;
		}

		public int getSecond() {
			return this.b;
		}

		public int getPrice(EaglercraftRandom rand) {
			return (a >= b) ? a : a + rand.nextInt(b - a + 1);
		}
	}
}