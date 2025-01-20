package net.minecraft.world;

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
public enum EnumDifficulty {
	PEACEFUL(0, "options.difficulty.peaceful"), EASY(1, "options.difficulty.easy"),
	NORMAL(2, "options.difficulty.normal"), HARD(3, "options.difficulty.hard");

	private static final EnumDifficulty[] difficultyEnums = new EnumDifficulty[4];
	private final int difficultyId;
	private final String difficultyResourceKey;

	private EnumDifficulty(int difficultyIdIn, String difficultyResourceKeyIn) {
		this.difficultyId = difficultyIdIn;
		this.difficultyResourceKey = difficultyResourceKeyIn;
	}

	public int getDifficultyId() {
		return this.difficultyId;
	}

	public static EnumDifficulty getDifficultyEnum(int parInt1) {
		return difficultyEnums[parInt1 % difficultyEnums.length];
	}

	public String getDifficultyResourceKey() {
		return this.difficultyResourceKey;
	}

	static {
		EnumDifficulty[] types = values();
		for (int i = 0; i < types.length; ++i) {
			difficultyEnums[types[i].difficultyId] = types[i];
		}

	}
}