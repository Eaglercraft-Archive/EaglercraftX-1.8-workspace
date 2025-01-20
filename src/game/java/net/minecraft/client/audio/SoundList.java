package net.minecraft.client.audio;

import java.util.List;

import com.google.common.collect.Lists;

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
public class SoundList {
	private final List<SoundList.SoundEntry> soundList = Lists.newArrayList();
	private boolean replaceExisting;
	private SoundCategory category;

	public List<SoundList.SoundEntry> getSoundList() {
		return this.soundList;
	}

	public boolean canReplaceExisting() {
		return this.replaceExisting;
	}

	public void setReplaceExisting(boolean parFlag) {
		this.replaceExisting = parFlag;
	}

	public SoundCategory getSoundCategory() {
		return this.category;
	}

	public void setSoundCategory(SoundCategory soundCat) {
		this.category = soundCat;
	}

	public static class SoundEntry {
		private String name;
		private float volume = 1.0F;
		private float pitch = 1.0F;
		private int weight = 1;
		private SoundList.SoundEntry.Type type = SoundList.SoundEntry.Type.FILE;
		private boolean streaming = false;

		public String getSoundEntryName() {
			return this.name;
		}

		public void setSoundEntryName(String nameIn) {
			this.name = nameIn;
		}

		public float getSoundEntryVolume() {
			return this.volume;
		}

		public void setSoundEntryVolume(float volumeIn) {
			this.volume = volumeIn;
		}

		public float getSoundEntryPitch() {
			return this.pitch;
		}

		public void setSoundEntryPitch(float pitchIn) {
			this.pitch = pitchIn;
		}

		public int getSoundEntryWeight() {
			return this.weight;
		}

		public void setSoundEntryWeight(int weightIn) {
			this.weight = weightIn;
		}

		public SoundList.SoundEntry.Type getSoundEntryType() {
			return this.type;
		}

		public void setSoundEntryType(SoundList.SoundEntry.Type typeIn) {
			this.type = typeIn;
		}

		public boolean isStreaming() {
			return this.streaming;
		}

		public void setStreaming(boolean isStreaming) {
			this.streaming = isStreaming;
		}

		public static enum Type {
			FILE("file"), SOUND_EVENT("event");

			private final String field_148583_c;

			private Type(String parString2) {
				this.field_148583_c = parString2;
			}

			public static SoundList.SoundEntry.Type getType(String parString1) {
				SoundList.SoundEntry.Type[] types = values();
				for (int i = 0; i < types.length; ++i) {
					if (types[i].field_148583_c.equals(parString1)) {
						return types[i];
					}
				}

				return null;
			}
		}
	}
}