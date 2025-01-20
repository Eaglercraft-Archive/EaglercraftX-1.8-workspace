package net.minecraft.world;

import net.minecraft.util.MathHelper;

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
public class DifficultyInstance {
	private final EnumDifficulty worldDifficulty;
	private final float additionalDifficulty;

	public DifficultyInstance(EnumDifficulty worldDifficulty, long worldTime, long chunkInhabitedTime,
			float moonPhaseFactor) {
		this.worldDifficulty = worldDifficulty;
		this.additionalDifficulty = this.calculateAdditionalDifficulty(worldDifficulty, worldTime, chunkInhabitedTime,
				moonPhaseFactor);
	}

	public float getAdditionalDifficulty() {
		return this.additionalDifficulty;
	}

	public float getClampedAdditionalDifficulty() {
		return this.additionalDifficulty < 2.0F ? 0.0F
				: (this.additionalDifficulty > 4.0F ? 1.0F : (this.additionalDifficulty - 2.0F) / 2.0F);
	}

	private float calculateAdditionalDifficulty(EnumDifficulty difficulty, long worldTime, long chunkInhabitedTime,
			float moonPhaseFactor) {
		if (difficulty == EnumDifficulty.PEACEFUL) {
			return 0.0F;
		} else {
			boolean flag = difficulty == EnumDifficulty.HARD;
			float f = 0.75F;
			float f1 = MathHelper.clamp_float(((float) worldTime + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
			f = f + f1;
			float f2 = 0.0F;
			f2 = f2 + MathHelper.clamp_float((float) chunkInhabitedTime / 3600000.0F, 0.0F, 1.0F)
					* (flag ? 1.0F : 0.75F);
			f2 = f2 + MathHelper.clamp_float(moonPhaseFactor * 0.25F, 0.0F, f1);
			if (difficulty == EnumDifficulty.EASY) {
				f2 *= 0.5F;
			}

			f = f + f2;
			return (float) difficulty.getDifficultyId() * f;
		}
	}
}