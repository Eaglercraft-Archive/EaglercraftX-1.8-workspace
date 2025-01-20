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
public class PositionedSoundRecord extends PositionedSound {
	public static PositionedSoundRecord create(ResourceLocation soundResource, float pitch) {
		return new PositionedSoundRecord(soundResource, 0.25F, pitch, false, ISound.AttenuationType.NONE, 0.0F, 0.0F,
				0.0F);
	}

	public static PositionedSoundRecord create(ResourceLocation soundResource) {
		return new PositionedSoundRecord(soundResource, 1.0F, 1.0F, false, ISound.AttenuationType.NONE, 0.0F, 0.0F,
				0.0F);
	}

	public static PositionedSoundRecord create(ResourceLocation soundResource, float xPosition, float yPosition,
			float zPosition) {
		return new PositionedSoundRecord(soundResource, 4.0F, 1.0F, false, ISound.AttenuationType.LINEAR, xPosition,
				yPosition, zPosition);
	}

	public PositionedSoundRecord(ResourceLocation soundResource, float volume, float pitch, float xPosition,
			float yPosition, float zPosition) {
		this(soundResource, volume, pitch, false, ISound.AttenuationType.LINEAR, xPosition, yPosition, zPosition);
	}

	private PositionedSoundRecord(ResourceLocation soundResource, float volume, float pitch, boolean repeat,
			ISound.AttenuationType attenuationType, float xPosition, float yPosition, float zPosition) {
		super(soundResource);
		this.volume = volume;
		this.pitch = pitch;
		this.xPosF = xPosition;
		this.yPosF = yPosition;
		this.zPosF = zPosition;
		this.repeat = repeat;
		this.attenuationType = attenuationType;
	}
}