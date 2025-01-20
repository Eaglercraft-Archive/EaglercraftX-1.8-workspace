package net.minecraft.client.audio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.EaglercraftSoundManager;
import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.ThreadLocalRandom;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;

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
public class SoundHandler implements IResourceManagerReloadListener, ITickable {
	private static final Logger logger = LogManager.getLogger();
	private static final Logger tipLogger = LogManager.getLogger("EaglercraftX");
	public static final SoundPoolEntry missing_sound = new SoundPoolEntry(new ResourceLocation("meta:missing_sound"),
			0.0D, 0.0D, false);
	private final SoundRegistry sndRegistry = new SoundRegistry();
	private final EaglercraftSoundManager sndManager;
	private final IResourceManager mcResourceManager;

	public SoundHandler(IResourceManager manager, GameSettings gameSettingsIn) {
		this.mcResourceManager = manager;
		this.sndManager = new EaglercraftSoundManager(gameSettingsIn, this);
	}

	public void onResourceManagerReload(IResourceManager iresourcemanager) {
		this.sndManager.reloadSoundSystem();
		this.sndRegistry.clearMap();

		for (String s : iresourcemanager.getResourceDomains()) {
			try {
				for (IResource iresource : iresourcemanager.getAllResources(new ResourceLocation(s, "sounds.json"))) {
					try {
						Map map = this.getSoundMap(iresource.getInputStream());

						for (Entry entry : (Set<Entry>) map.entrySet()) {
							this.loadSoundResource(new ResourceLocation(s, (String) entry.getKey()),
									(SoundList) entry.getValue());
						}
					} catch (RuntimeException runtimeexception) {
						logger.warn("Invalid sounds.json", runtimeexception);
					}
				}
			} catch (IOException var11) {
				;
			}
		}

		if (this.sndRegistry.getObject(new ResourceLocation("minecraft:sounds/music/game/calm1.ogg")) == null) {
			tipLogger.info(
					"Download this resource pack if you want music: https://bafybeiayojww5jfyzvlmtuk7l5ufkt7nlfto7mhwmzf2vs4bvsjd5ouiuq.ipfs.nftstorage.link/?filename=Music_For_Eaglercraft.zip");
		}
	}

	public static class SoundMap {

		protected final Map<String, SoundList> soundMap;

		public SoundMap(Map<String, SoundList> soundMap) {
			this.soundMap = soundMap;
		}

	}

	protected Map<String, SoundList> getSoundMap(InputStream stream) {
		Map<String, SoundList> map = null;
		try {
			map = JSONTypeProvider.deserialize(IOUtils.inputStreamToString(stream, StandardCharsets.UTF_8),
					SoundMap.class).soundMap;
		} catch (IOException e) {
			throw new RuntimeException("Exception caught reading JSON", e);
		} finally {
			IOUtils.closeQuietly(stream);
		}

		return map;
	}

	private void loadSoundResource(ResourceLocation location, SoundList sounds) {
		boolean flag = !this.sndRegistry.containsKey(location);
		SoundEventAccessorComposite soundeventaccessorcomposite;
		if (!flag && !sounds.canReplaceExisting()) {
			soundeventaccessorcomposite = (SoundEventAccessorComposite) this.sndRegistry.getObject(location);
		} else {
			if (!flag) {
				logger.debug("Replaced sound event location {}", new Object[] { location });
			}

			soundeventaccessorcomposite = new SoundEventAccessorComposite(location, 1.0D, 1.0D,
					sounds.getSoundCategory());
			this.sndRegistry.registerSound(soundeventaccessorcomposite);
		}

		for (final SoundList.SoundEntry soundlist$soundentry : sounds.getSoundList()) {
			String s = soundlist$soundentry.getSoundEntryName();
			ResourceLocation resourcelocation = new ResourceLocation(s);
			final String s1 = s.contains(":") ? resourcelocation.getResourceDomain() : location.getResourceDomain();
			Object object;
			switch (soundlist$soundentry.getSoundEntryType()) {
			case FILE:
				ResourceLocation resourcelocation1 = new ResourceLocation(s1,
						"sounds/" + resourcelocation.getResourcePath() + ".ogg");
				InputStream inputstream = null;

				try {
					inputstream = this.mcResourceManager.getResource(resourcelocation1).getInputStream();
				} catch (FileNotFoundException var18) {
					logger.warn("File {} does not exist, cannot add it to event {}",
							new Object[] { resourcelocation1, location });
					continue;
				} catch (IOException ioexception) {
					logger.warn(
							"Could not load sound file " + resourcelocation1 + ", cannot add it to event " + location,
							ioexception);
					continue;
				} finally {
					IOUtils.closeQuietly(inputstream);
				}

				object = new SoundEventAccessor(new SoundPoolEntry(resourcelocation1,
						(double) soundlist$soundentry.getSoundEntryPitch(),
						(double) soundlist$soundentry.getSoundEntryVolume(), soundlist$soundentry.isStreaming()),
						soundlist$soundentry.getSoundEntryWeight());
				break;
			case SOUND_EVENT:
				object = new ISoundEventAccessor<SoundPoolEntry>() {
					final ResourceLocation field_148726_a = new ResourceLocation(s1,
							soundlist$soundentry.getSoundEntryName());

					public int getWeight() {
						SoundEventAccessorComposite soundeventaccessorcomposite1 = (SoundEventAccessorComposite) SoundHandler.this.sndRegistry
								.getObject(this.field_148726_a);
						return soundeventaccessorcomposite1 == null ? 0 : soundeventaccessorcomposite1.getWeight();
					}

					public SoundPoolEntry cloneEntry() {
						SoundEventAccessorComposite soundeventaccessorcomposite1 = (SoundEventAccessorComposite) SoundHandler.this.sndRegistry
								.getObject(this.field_148726_a);
						return soundeventaccessorcomposite1 == null ? SoundHandler.missing_sound
								: soundeventaccessorcomposite1.cloneEntry();
					}
				};
				break;
			default:
				throw new IllegalStateException("IN YOU FACE");
			}

			soundeventaccessorcomposite.addSoundToEventPool((ISoundEventAccessor<SoundPoolEntry>) object);
		}

	}

	public SoundEventAccessorComposite getSound(ResourceLocation location) {
		return (SoundEventAccessorComposite) this.sndRegistry.getObject(location);
	}

	/**+
	 * Play a sound
	 */
	public void playSound(ISound sound) {
		this.sndManager.playSound(sound);
	}

	/**+
	 * Plays the sound in n ticks
	 */
	public void playDelayedSound(ISound sound, int delay) {
		this.sndManager.playDelayedSound(sound, delay);
	}

	public void setListener(EntityPlayer player, float parFloat1) {
		this.sndManager.setListener(player, parFloat1);
	}

	public void pauseSounds() {
		this.sndManager.pauseAllSounds();
	}

	public void stopSounds() {
		this.sndManager.stopAllSounds();
	}

	public void unloadSounds() {
		this.sndManager.unloadSoundSystem();
	}

	/**+
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		this.sndManager.updateAllSounds();
	}

	public void resumeSounds() {
		this.sndManager.resumeAllSounds();
	}

	public void setSoundLevel(SoundCategory category, float volume) {
		if (category == SoundCategory.MASTER && volume <= 0.0F) {
			this.stopSounds();
		}

		this.sndManager.setSoundCategoryVolume(category, volume);
	}

	public void stopSound(ISound parISound) {
		this.sndManager.stopSound(parISound);
	}

	/**+
	 * Returns a random sound from one or more categories
	 */
	public SoundEventAccessorComposite getRandomSoundFromCategories(SoundCategory... categories) {
		ArrayList arraylist = Lists.newArrayList();

		for (ResourceLocation resourcelocation : this.sndRegistry.getKeys()) {
			SoundEventAccessorComposite soundeventaccessorcomposite = (SoundEventAccessorComposite) this.sndRegistry
					.getObject(resourcelocation);
			SoundCategory cat = soundeventaccessorcomposite.getSoundCategory();
			for (int i = 0; i < categories.length; ++i) {
				if (cat == categories[i]) {
					arraylist.add(soundeventaccessorcomposite);
					break;
				}
			}
		}

		if (arraylist.isEmpty()) {
			return null;
		} else {
			return (SoundEventAccessorComposite) arraylist.get(ThreadLocalRandom.current().nextInt(arraylist.size()));
		}
	}

	public boolean isSoundPlaying(ISound sound) {
		return this.sndManager.isSoundPlaying(sound);
	}
}