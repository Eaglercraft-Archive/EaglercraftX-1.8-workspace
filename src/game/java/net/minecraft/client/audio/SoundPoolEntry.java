package net.minecraft.client.audio;

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
public class SoundPoolEntry {
	private final ResourceLocation location;
	private final boolean streamingSound;
	private double pitch;
	private double volume;

	public SoundPoolEntry(ResourceLocation locationIn, double pitchIn, double volumeIn, boolean streamingSoundIn) {
		this.location = locationIn;
		this.pitch = pitchIn;
		this.volume = volumeIn;
		this.streamingSound = streamingSoundIn;
	}

	public SoundPoolEntry(SoundPoolEntry locationIn) {
		this.location = locationIn.location;
		this.pitch = locationIn.pitch;
		this.volume = locationIn.volume;
		this.streamingSound = locationIn.streamingSound;
	}

	public ResourceLocation getSoundPoolEntryLocation() {
		return this.location;
	}

	public double getPitch() {
		return this.pitch;
	}

	public void setPitch(double pitchIn) {
		this.pitch = pitchIn;
	}

	public double getVolume() {
		return this.volume;
	}

	public void setVolume(double volumeIn) {
		this.volume = volumeIn;
	}

	public boolean isStreamingSound() {
		return this.streamingSound;
	}
}