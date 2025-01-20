package net.minecraft.entity;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.Packet;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
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
public class EntityTracker {
	private static final Logger logger = LogManager.getLogger();
	private final WorldServer theWorld;
	/**+
	 * List of tracked entities, used for iteration operations on
	 * tracked entities.
	 */
	private Set<EntityTrackerEntry> trackedEntities = Sets.newHashSet();
	/**+
	 * Used for identity lookup of tracked entities.
	 */
	private IntObjectMap<EntityTrackerEntry> trackedEntityHashTable = new IntObjectHashMap<>();
	private int maxTrackingDistanceThreshold;

	public EntityTracker(WorldServer theWorldIn) {
		this.theWorld = theWorldIn;
		this.maxTrackingDistanceThreshold = theWorldIn.getMinecraftServer().getConfigurationManager()
				.getEntityViewDistance();
	}

	public void updateMaxTrackingThreshold(int dist) {
		maxTrackingDistanceThreshold = dist;
	}

	public void trackEntity(Entity parEntity) {
		if (parEntity instanceof EntityPlayerMP) {
			this.trackEntity(parEntity, 512, 2);
			EntityPlayerMP entityplayermp = (EntityPlayerMP) parEntity;

			for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
				if (entitytrackerentry.trackedEntity != entityplayermp) {
					entitytrackerentry.updatePlayerEntity(entityplayermp, maxTrackingDistanceThreshold);
				}
			}
		} else if (parEntity instanceof EntityFishHook) {
			this.addEntityToTracker(parEntity, 64, 5, true);
		} else if (parEntity instanceof EntityArrow) {
			this.addEntityToTracker(parEntity, 64, 20, false);
		} else if (parEntity instanceof EntitySmallFireball) {
			this.addEntityToTracker(parEntity, 64, 10, false);
		} else if (parEntity instanceof EntityFireball) {
			this.addEntityToTracker(parEntity, 64, 10, false);
		} else if (parEntity instanceof EntitySnowball) {
			this.addEntityToTracker(parEntity, 64, 10, true);
		} else if (parEntity instanceof EntityEnderPearl) {
			this.addEntityToTracker(parEntity, 64, 10, true);
		} else if (parEntity instanceof EntityEnderEye) {
			this.addEntityToTracker(parEntity, 64, 4, true);
		} else if (parEntity instanceof EntityEgg) {
			this.addEntityToTracker(parEntity, 64, 10, true);
		} else if (parEntity instanceof EntityPotion) {
			this.addEntityToTracker(parEntity, 64, 10, true);
		} else if (parEntity instanceof EntityExpBottle) {
			this.addEntityToTracker(parEntity, 64, 10, true);
		} else if (parEntity instanceof EntityFireworkRocket) {
			this.addEntityToTracker(parEntity, 64, 10, true);
		} else if (parEntity instanceof EntityItem) {
			this.addEntityToTracker(parEntity, 64, 20, true);
		} else if (parEntity instanceof EntityMinecart) {
			this.addEntityToTracker(parEntity, 80, 3, true);
		} else if (parEntity instanceof EntityBoat) {
			this.addEntityToTracker(parEntity, 80, 3, true);
		} else if (parEntity instanceof EntitySquid) {
			this.addEntityToTracker(parEntity, 64, 3, true);
		} else if (parEntity instanceof EntityWither) {
			this.addEntityToTracker(parEntity, 80, 3, false);
		} else if (parEntity instanceof EntityBat) {
			this.addEntityToTracker(parEntity, 80, 3, false);
		} else if (parEntity instanceof EntityDragon) {
			this.addEntityToTracker(parEntity, 160, 3, true);
		} else if (parEntity instanceof IAnimals) {
			this.addEntityToTracker(parEntity, 80, 3, true);
		} else if (parEntity instanceof EntityTNTPrimed) {
			this.addEntityToTracker(parEntity, 160, 10, true);
		} else if (parEntity instanceof EntityFallingBlock) {
			this.addEntityToTracker(parEntity, 160, 20, true);
		} else if (parEntity instanceof EntityHanging) {
			this.addEntityToTracker(parEntity, 160, Integer.MAX_VALUE, false);
		} else if (parEntity instanceof EntityArmorStand) {
			this.addEntityToTracker(parEntity, 160, 3, true);
		} else if (parEntity instanceof EntityXPOrb) {
			this.addEntityToTracker(parEntity, 160, 20, true);
		} else if (parEntity instanceof EntityEnderCrystal) {
			this.addEntityToTracker(parEntity, 256, Integer.MAX_VALUE, false);
		}

	}

	public void trackEntity(Entity entityIn, int trackingRange, int updateFrequency) {
		this.addEntityToTracker(entityIn, trackingRange, updateFrequency, false);
	}

	/**+
	 * Args : Entity, trackingRange, updateFrequency,
	 * sendVelocityUpdates
	 */
	public void addEntityToTracker(Entity entityIn, int trackingRange, final int updateFrequency,
			boolean sendVelocityUpdates) {
		try {
			if (this.trackedEntityHashTable.containsKey(entityIn.getEntityId())) {
				throw new IllegalStateException("Entity is already tracked!");
			}

			EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entityIn, trackingRange, updateFrequency,
					sendVelocityUpdates);
			this.trackedEntities.add(entitytrackerentry);
			this.trackedEntityHashTable.put(entityIn.getEntityId(), entitytrackerentry);
			entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities, maxTrackingDistanceThreshold);
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity To Track");
			crashreportcategory.addCrashSection("Tracking range", trackingRange + " blocks");
			crashreportcategory.addCrashSectionCallable("Update interval", new Callable<String>() {
				public String call() throws Exception {
					String s = "Once per " + updateFrequency + " ticks";
					if (updateFrequency == Integer.MAX_VALUE) {
						s = "Maximum (" + s + ")";
					}

					return s;
				}
			});
			entityIn.addEntityCrashInfo(crashreportcategory);
			CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Entity That Is Already Tracked");
			this.trackedEntityHashTable.get(entityIn.getEntityId()).trackedEntity
					.addEntityCrashInfo(crashreportcategory1);

			try {
				throw new ReportedException(crashreport);
			} catch (ReportedException reportedexception) {
				logger.error("\"Silently\" catching entity tracking error.", reportedexception);
			}
		}

	}

	public void untrackEntity(Entity entityIn) {
		if (entityIn instanceof EntityPlayerMP) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) entityIn;

			for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
				entitytrackerentry.removeFromTrackedPlayers(entityplayermp);
			}
		}

		EntityTrackerEntry entitytrackerentry1 = this.trackedEntityHashTable.remove(entityIn.getEntityId());
		if (entitytrackerentry1 != null) {
			this.trackedEntities.remove(entitytrackerentry1);
			entitytrackerentry1.sendDestroyEntityPacketToTrackedPlayers();
		}

	}

	public void updateTrackedEntities() {
		ArrayList arraylist = Lists.newArrayList();

		for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
			entitytrackerentry.updatePlayerList(this.theWorld.playerEntities, maxTrackingDistanceThreshold);
			if (entitytrackerentry.playerEntitiesUpdated
					&& entitytrackerentry.trackedEntity instanceof EntityPlayerMP) {
				arraylist.add((EntityPlayerMP) entitytrackerentry.trackedEntity);
			}
		}

		for (int i = 0; i < arraylist.size(); ++i) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) arraylist.get(i);

			for (EntityTrackerEntry entitytrackerentry1 : this.trackedEntities) {
				if (entitytrackerentry1.trackedEntity != entityplayermp) {
					entitytrackerentry1.updatePlayerEntity(entityplayermp, maxTrackingDistanceThreshold);
				}
			}
		}

	}

	public void func_180245_a(EntityPlayerMP parEntityPlayerMP) {
		for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
			if (entitytrackerentry.trackedEntity == parEntityPlayerMP) {
				entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities, maxTrackingDistanceThreshold);
			} else {
				entitytrackerentry.updatePlayerEntity(parEntityPlayerMP, maxTrackingDistanceThreshold);
			}
		}

	}

	public void sendToAllTrackingEntity(Entity entityIn, Packet parPacket) {
		EntityTrackerEntry entitytrackerentry = this.trackedEntityHashTable.get(entityIn.getEntityId());
		if (entitytrackerentry != null) {
			entitytrackerentry.sendPacketToTrackedPlayers(parPacket);
		}

	}

	public void func_151248_b(Entity entityIn, Packet parPacket) {
		EntityTrackerEntry entitytrackerentry = this.trackedEntityHashTable.get(entityIn.getEntityId());
		if (entitytrackerentry != null) {
			entitytrackerentry.func_151261_b(parPacket);
		}

	}

	public void removePlayerFromTrackers(EntityPlayerMP parEntityPlayerMP) {
		for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
			entitytrackerentry.removeTrackedPlayerSymmetric(parEntityPlayerMP);
		}

	}

	public void func_85172_a(EntityPlayerMP parEntityPlayerMP, Chunk parChunk) {
		for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
			if (entitytrackerentry.trackedEntity != parEntityPlayerMP
					&& entitytrackerentry.trackedEntity.chunkCoordX == parChunk.xPosition
					&& entitytrackerentry.trackedEntity.chunkCoordZ == parChunk.zPosition) {
				entitytrackerentry.updatePlayerEntity(parEntityPlayerMP, maxTrackingDistanceThreshold);
			}
		}

	}
}