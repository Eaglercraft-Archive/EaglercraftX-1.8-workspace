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
public class ColorizerFoliage {
	/**+
	 * Color buffer for foliage
	 */
	private static int[] foliageBuffer = new int[65536];

	public static void setFoliageBiomeColorizer(int[] parArrayOfInt) {
		foliageBuffer = parArrayOfInt;
	}

	/**+
	 * Gets foliage color from temperature and humidity. Args:
	 * temperature, humidity
	 */
	public static int getFoliageColor(double parDouble1, double parDouble2) {
		parDouble2 = parDouble2 * parDouble1;
		int i = (int) ((1.0D - parDouble1) * 255.0D);
		int j = (int) ((1.0D - parDouble2) * 255.0D);
		return foliageBuffer[j << 8 | i];
	}

	/**+
	 * Gets the foliage color for pine type (metadata 1) trees
	 */
	public static int getFoliageColorPine() {
		return 6396257;
	}

	/**+
	 * Gets the foliage color for birch type (metadata 2) trees
	 */
	public static int getFoliageColorBirch() {
		return 8431445;
	}

	public static int getFoliageColorBasic() {
		return 4764952;
	}
}