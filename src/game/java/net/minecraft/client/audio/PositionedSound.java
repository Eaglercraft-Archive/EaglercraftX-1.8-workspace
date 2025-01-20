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
public abstract class PositionedSound implements ISound {
	protected final ResourceLocation positionedSoundLocation;
	protected float volume = 1.0F;
	protected float pitch = 1.0F;
	protected float xPosF;
	protected float yPosF;
	protected float zPosF;
	protected boolean repeat = false;
	protected ISound.AttenuationType attenuationType = ISound.AttenuationType.LINEAR;

	protected PositionedSound(ResourceLocation soundResource) {
		this.positionedSoundLocation = soundResource;
	}

	public ResourceLocation getSoundLocation() {
		return this.positionedSoundLocation;
	}

	public boolean canRepeat() {
		return this.repeat;
	}

	public float getVolume() {
		return this.volume;
	}

	public float getPitch() {
		return this.pitch;
	}

	public float getXPosF() {
		return this.xPosF;
	}

	public float getYPosF() {
		return this.yPosF;
	}

	public float getZPosF() {
		return this.zPosF;
	}

	public ISound.AttenuationType getAttenuationType() {
		return this.attenuationType;
	}
}