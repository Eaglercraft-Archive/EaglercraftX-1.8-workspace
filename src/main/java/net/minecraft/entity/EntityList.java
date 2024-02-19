package net.minecraft.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class EntityList {
	private static final Logger logger = LogManager.getLogger();
	private static final Map<String, Class<? extends Entity>> stringToClassMapping = Maps.newHashMap();
	private static final Map<String, EntityConstructor<? extends Entity>> stringToConstructorMapping = Maps
			.newHashMap();
	private static final Map<Class<? extends Entity>, String> classToStringMapping = Maps.newHashMap();
	private static final Map<Integer, Class<? extends Entity>> idToClassMapping = Maps.newHashMap();
	private static final Map<Integer, EntityConstructor<? extends Entity>> idToConstructorMapping = Maps.newHashMap();
	private static final Map<Class<? extends Entity>, Integer> classToIDMapping = Maps.newHashMap();
	private static final Map<Class<? extends Entity>, EntityConstructor<? extends Entity>> classToConstructorMapping = Maps
			.newHashMap();
	private static final Map<String, Integer> stringToIDMapping = Maps.newHashMap();
	public static final Map<Integer, EntityList.EntityEggInfo> entityEggs = Maps.newLinkedHashMap();

	/**+
	 * adds a mapping between Entity classes and both a string
	 * representation and an ID
	 */
	private static void addMapping(Class<? extends Entity> entityClass,
			EntityConstructor<? extends Entity> entityConstructor, String entityName, int id) {
		if (stringToClassMapping.containsKey(entityName)) {
			throw new IllegalArgumentException("ID is already registered: " + entityName);
		} else if (idToClassMapping.containsKey(Integer.valueOf(id))) {
			throw new IllegalArgumentException("ID is already registered: " + id);
		} else if (id == 0) {
			throw new IllegalArgumentException("Cannot register to reserved id: " + id);
		} else if (entityClass == null) {
			throw new IllegalArgumentException("Cannot register null clazz for id: " + id);
		} else {
			stringToClassMapping.put(entityName, entityClass);
			stringToConstructorMapping.put(entityName, entityConstructor);
			classToStringMapping.put(entityClass, entityName);
			idToClassMapping.put(Integer.valueOf(id), entityClass);
			idToConstructorMapping.put(Integer.valueOf(id), entityConstructor);
			classToIDMapping.put(entityClass, Integer.valueOf(id));
			classToConstructorMapping.put(entityClass, entityConstructor);
			stringToIDMapping.put(entityName, Integer.valueOf(id));
		}
	}

	/**+
	 * adds a mapping between Entity classes and both a string
	 * representation and an ID
	 */
	private static void addMapping(Class<? extends Entity> entityClass,
			EntityConstructor<? extends Entity> entityConstructor, String entityName, int entityID, int baseColor,
			int spotColor) {
		addMapping(entityClass, entityConstructor, entityName, entityID);
		entityEggs.put(Integer.valueOf(entityID), new EntityList.EntityEggInfo(entityID, baseColor, spotColor));
	}

	/**+
	 * Create a new instance of an entity in the world by using the
	 * entity name.
	 */
	public static Entity createEntityByName(String entityName, World worldIn) {
		Entity entity = null;

		try {
			EntityConstructor<? extends Entity> constructor = stringToConstructorMapping.get(entityName);
			if (constructor != null) {
				entity = constructor.createEntity(worldIn);
			}
		} catch (Exception exception) {
			logger.error("Could not create entity", exception);
		}

		return entity;
	}

	public static Entity createEntityByClass(Class<? extends Entity> entityClass, World worldIn) {
		Entity entity = null;

		try {
			EntityConstructor<? extends Entity> constructor = classToConstructorMapping.get(entityClass);
			if (constructor != null) {
				entity = constructor.createEntity(worldIn);
			}
		} catch (Exception exception) {
			logger.error("Could not create entity", exception);
		}

		return entity;
	}

	public static Entity createEntityByClassUnsafe(Class<? extends Entity> entityClass, World worldIn) {
		EntityConstructor<? extends Entity> constructor = classToConstructorMapping.get(entityClass);
		if (constructor != null) {
			return constructor.createEntity(worldIn);
		}
		return null;
	}

	/**+
	 * create a new instance of an entity from NBT store
	 */
	public static Entity createEntityFromNBT(NBTTagCompound nbt, World worldIn) {
		Entity entity = null;
		if ("Minecart".equals(nbt.getString("id"))) {
			nbt.setString("id", EntityMinecart.EnumMinecartType.byNetworkID(nbt.getInteger("Type")).getName());
			nbt.removeTag("Type");
		}

		try {
			EntityConstructor<? extends Entity> constructor = stringToConstructorMapping.get(nbt.getString("id"));
			if (constructor != null) {
				entity = constructor.createEntity(worldIn);
			}
		} catch (Exception exception) {
			logger.error("Could not create entity", exception);
		}

		if (entity != null) {
			entity.readFromNBT(nbt);
		} else {
			logger.warn("Skipping Entity with id " + nbt.getString("id"));
		}

		return entity;
	}

	/**+
	 * Create a new instance of an entity in the world by using an
	 * entity ID.
	 */
	public static Entity createEntityByID(int entityID, World worldIn) {
		Entity entity = null;

		try {
			EntityConstructor<? extends Entity> constructor = getConstructorFromID(entityID);
			if (constructor != null) {
				entity = constructor.createEntity(worldIn);
			}
		} catch (Exception exception) {
			logger.error("Could not create entity", exception);
		}

		if (entity == null) {
			logger.warn("Skipping Entity with id " + entityID);
		}

		return entity;
	}

	/**+
	 * gets the entityID of a specific entity
	 */
	public static int getEntityID(Entity entityIn) {
		Integer integer = (Integer) classToIDMapping.get(entityIn.getClass());
		return integer == null ? 0 : integer.intValue();
	}

	public static Class<? extends Entity> getClassFromID(int entityID) {
		return (Class) idToClassMapping.get(Integer.valueOf(entityID));
	}

	public static EntityConstructor<? extends Entity> getConstructorFromID(int entityID) {
		return idToConstructorMapping.get(Integer.valueOf(entityID));
	}

	/**+
	 * Gets the string representation of a specific entity.
	 */
	public static String getEntityString(Entity entityIn) {
		return (String) classToStringMapping.get(entityIn.getClass());
	}

	/**+
	 * Returns the ID assigned to it's string representation
	 */
	public static int getIDFromString(String entityName) {
		Integer integer = (Integer) stringToIDMapping.get(entityName);
		return integer == null ? 90 : integer.intValue();
	}

	/**+
	 * Finds the class using IDtoClassMapping and
	 * classToStringMapping
	 */
	public static String getStringFromID(int entityID) {
		return (String) classToStringMapping.get(getClassFromID(entityID));
	}

	public static void func_151514_a() {
	}

	public static List<String> getEntityNameList() {
		Set<String> set = stringToClassMapping.keySet();
		ArrayList arraylist = Lists.newArrayList();

		for (String s : set) {
			Class oclass = (Class) stringToClassMapping.get(s);
			if ((oclass.getModifiers() & 1024) != 1024) {
				arraylist.add(s);
			}
		}

		arraylist.add("LightningBolt");
		return arraylist;
	}

	public static boolean isStringEntityName(Entity entityIn, String entityName) {
		String s = getEntityString(entityIn);
		if (s == null && entityIn instanceof EntityPlayer) {
			s = "Player";
		} else if (s == null && entityIn instanceof EntityLightningBolt) {
			s = "LightningBolt";
		}

		return entityName.equals(s);
	}

	public static boolean isStringValidEntityName(String entityName) {
		return "Player".equals(entityName) || getEntityNameList().contains(entityName);
	}

	static {
		addMapping(EntityItem.class, (w) -> new EntityItem(w), "Item", 1);
		addMapping(EntityXPOrb.class, (w) -> new EntityXPOrb(w), "XPOrb", 2);
		addMapping(EntityEgg.class, (w) -> new EntityEgg(w), "ThrownEgg", 7);
		addMapping(EntityLeashKnot.class, (w) -> new EntityLeashKnot(w), "LeashKnot", 8);
		addMapping(EntityPainting.class, (w) -> new EntityPainting(w), "Painting", 9);
		addMapping(EntityArrow.class, (w) -> new EntityArrow(w), "Arrow", 10);
		addMapping(EntitySnowball.class, (w) -> new EntitySnowball(w), "Snowball", 11);
		addMapping(EntityLargeFireball.class, (w) -> new EntityLargeFireball(w), "Fireball", 12);
		addMapping(EntitySmallFireball.class, (w) -> new EntitySmallFireball(w), "SmallFireball", 13);
		addMapping(EntityEnderPearl.class, (w) -> new EntityEnderPearl(w), "ThrownEnderpearl", 14);
		addMapping(EntityEnderEye.class, (w) -> new EntityEnderEye(w), "EyeOfEnderSignal", 15);
		addMapping(EntityPotion.class, (w) -> new EntityPotion(w), "ThrownPotion", 16);
		addMapping(EntityExpBottle.class, (w) -> new EntityExpBottle(w), "ThrownExpBottle", 17);
		addMapping(EntityItemFrame.class, (w) -> new EntityItemFrame(w), "ItemFrame", 18);
		addMapping(EntityWitherSkull.class, (w) -> new EntityWitherSkull(w), "WitherSkull", 19);
		addMapping(EntityTNTPrimed.class, (w) -> new EntityTNTPrimed(w), "PrimedTnt", 20);
		addMapping(EntityFallingBlock.class, (w) -> new EntityFallingBlock(w), "FallingSand", 21);
		addMapping(EntityFireworkRocket.class, (w) -> new EntityFireworkRocket(w), "FireworksRocketEntity", 22);
		addMapping(EntityArmorStand.class, (w) -> new EntityArmorStand(w), "ArmorStand", 30);
		addMapping(EntityBoat.class, (w) -> new EntityBoat(w), "Boat", 41);
		addMapping(EntityMinecartEmpty.class, (w) -> new EntityMinecartEmpty(w),
				EntityMinecart.EnumMinecartType.RIDEABLE.getName(), 42);
		addMapping(EntityMinecartChest.class, (w) -> new EntityMinecartChest(w),
				EntityMinecart.EnumMinecartType.CHEST.getName(), 43);
		addMapping(EntityMinecartFurnace.class, (w) -> new EntityMinecartFurnace(w),
				EntityMinecart.EnumMinecartType.FURNACE.getName(), 44);
		addMapping(EntityMinecartTNT.class, (w) -> new EntityMinecartTNT(w),
				EntityMinecart.EnumMinecartType.TNT.getName(), 45);
		addMapping(EntityMinecartHopper.class, (w) -> new EntityMinecartHopper(w),
				EntityMinecart.EnumMinecartType.HOPPER.getName(), 46);
		addMapping(EntityMinecartMobSpawner.class, (w) -> new EntityMinecartMobSpawner(w),
				EntityMinecart.EnumMinecartType.SPAWNER.getName(), 47);
		addMapping(EntityMinecartCommandBlock.class, (w) -> new EntityMinecartCommandBlock(w),
				EntityMinecart.EnumMinecartType.COMMAND_BLOCK.getName(), 40);
		addMapping(EntityLiving.class, null, "Mob", 48);
		addMapping(EntityMob.class, null, "Monster", 49);
		addMapping(EntityCreeper.class, (w) -> new EntityCreeper(w), "Creeper", 50, 894731, 0);
		addMapping(EntitySkeleton.class, (w) -> new EntitySkeleton(w), "Skeleton", 51, 12698049, 4802889);
		addMapping(EntitySpider.class, (w) -> new EntitySpider(w), "Spider", 52, 3419431, 11013646);
		addMapping(EntityGiantZombie.class, (w) -> new EntityGiantZombie(w), "Giant", 53);
		addMapping(EntityZombie.class, (w) -> new EntityZombie(w), "Zombie", 54, '\uafaf', 7969893);
		addMapping(EntitySlime.class, (w) -> new EntitySlime(w), "Slime", 55, 5349438, 8306542);
		addMapping(EntityGhast.class, (w) -> new EntityGhast(w), "Ghast", 56, 16382457, 12369084);
		addMapping(EntityPigZombie.class, (w) -> new EntityPigZombie(w), "PigZombie", 57, 15373203, 5009705);
		addMapping(EntityEnderman.class, (w) -> new EntityEnderman(w), "Enderman", 58, 1447446, 0);
		addMapping(EntityCaveSpider.class, (w) -> new EntityCaveSpider(w), "CaveSpider", 59, 803406, 11013646);
		addMapping(EntitySilverfish.class, (w) -> new EntitySilverfish(w), "Silverfish", 60, 7237230, 3158064);
		addMapping(EntityBlaze.class, (w) -> new EntityBlaze(w), "Blaze", 61, 16167425, 16775294);
		addMapping(EntityMagmaCube.class, (w) -> new EntityMagmaCube(w), "LavaSlime", 62, 3407872, 16579584);
		addMapping(EntityDragon.class, (w) -> new EntityDragon(w), "EnderDragon", 63);
		addMapping(EntityWither.class, (w) -> new EntityWither(w), "WitherBoss", 64);
		addMapping(EntityBat.class, (w) -> new EntityBat(w), "Bat", 65, 4996656, 986895);
		addMapping(EntityWitch.class, (w) -> new EntityWitch(w), "Witch", 66, 3407872, 5349438);
		addMapping(EntityEndermite.class, (w) -> new EntityEndermite(w), "Endermite", 67, 1447446, 7237230);
		addMapping(EntityGuardian.class, (w) -> new EntityGuardian(w), "Guardian", 68, 5931634, 15826224);
		addMapping(EntityPig.class, (w) -> new EntityPig(w), "Pig", 90, 15771042, 14377823);
		addMapping(EntitySheep.class, (w) -> new EntitySheep(w), "Sheep", 91, 15198183, 16758197);
		addMapping(EntityCow.class, (w) -> new EntityCow(w), "Cow", 92, 4470310, 10592673);
		addMapping(EntityChicken.class, (w) -> new EntityChicken(w), "Chicken", 93, 10592673, 16711680);
		addMapping(EntitySquid.class, (w) -> new EntitySquid(w), "Squid", 94, 2243405, 7375001);
		addMapping(EntityWolf.class, (w) -> new EntityWolf(w), "Wolf", 95, 14144467, 13545366);
		addMapping(EntityMooshroom.class, (w) -> new EntityMooshroom(w), "MushroomCow", 96, 10489616, 12040119);
		addMapping(EntitySnowman.class, (w) -> new EntitySnowman(w), "SnowMan", 97);
		addMapping(EntityOcelot.class, (w) -> new EntityOcelot(w), "Ozelot", 98, 15720061, 5653556);
		addMapping(EntityIronGolem.class, (w) -> new EntityIronGolem(w), "VillagerGolem", 99);
		addMapping(EntityHorse.class, (w) -> new EntityHorse(w), "EntityHorse", 100, 12623485, 15656192);
		addMapping(EntityRabbit.class, (w) -> new EntityRabbit(w), "Rabbit", 101, 10051392, 7555121);
		addMapping(EntityVillager.class, (w) -> new EntityVillager(w), "Villager", 120, 5651507, 12422002);
		addMapping(EntityEnderCrystal.class, (w) -> new EntityEnderCrystal(w), "EnderCrystal", 200);
	}

	public static class EntityEggInfo {
		public final int spawnedID;
		public final int primaryColor;
		public final int secondaryColor;
		public final StatBase field_151512_d;
		public final StatBase field_151513_e;

		public EntityEggInfo(int id, int baseColor, int spotColor) {
			this.spawnedID = id;
			this.primaryColor = baseColor;
			this.secondaryColor = spotColor;
			this.field_151512_d = StatList.getStatKillEntity(this);
			this.field_151513_e = StatList.getStatEntityKilledBy(this);
		}
	}
}