package net.minecraft.client.audio;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.google.common.collect.Lists;

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
public class SoundEventAccessorComposite implements ISoundEventAccessor<SoundPoolEntry> {
	/**+
	 * A composite (List) of ISoundEventAccessors
	 */
	private final List<ISoundEventAccessor<SoundPoolEntry>> soundPool = Lists.newArrayList();
	private final EaglercraftRandom rnd = new EaglercraftRandom();
	private final ResourceLocation soundLocation;
	private final SoundCategory category;
	private double eventPitch;
	private double eventVolume;

	public SoundEventAccessorComposite(ResourceLocation soundLocation, double pitch, double volume,
			SoundCategory category) {
		this.soundLocation = soundLocation;
		this.eventVolume = volume;
		this.eventPitch = pitch;
		this.category = category;
	}

	public int getWeight() {
		int i = 0;

		for (int j = 0, l = this.soundPool.size(); j < l; ++j) {
			i += this.soundPool.get(j).getWeight();
		}

		return i;
	}

	public SoundPoolEntry cloneEntry() {
		int i = this.getWeight();
		if (!this.soundPool.isEmpty() && i != 0) {
			int j = this.rnd.nextInt(i);

			for (int k = 0, l = this.soundPool.size(); k < l; ++k) {
				ISoundEventAccessor isoundeventaccessor = this.soundPool.get(k);
				j -= isoundeventaccessor.getWeight();
				if (j < 0) {
					SoundPoolEntry soundpoolentry = (SoundPoolEntry) isoundeventaccessor.cloneEntry();
					soundpoolentry.setPitch(soundpoolentry.getPitch() * this.eventPitch);
					soundpoolentry.setVolume(soundpoolentry.getVolume() * this.eventVolume);
					return soundpoolentry;
				}
			}

			return SoundHandler.missing_sound;
		} else {
			return SoundHandler.missing_sound;
		}
	}

	public void addSoundToEventPool(ISoundEventAccessor<SoundPoolEntry> sound) {
		this.soundPool.add(sound);
	}

	public ResourceLocation getSoundEventLocation() {
		return this.soundLocation;
	}

	public SoundCategory getSoundCategory() {
		return this.category;
	}
}