package net.minecraft.world.gen.layer;

import com.google.common.collect.Lists;
import java.util.List;

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
public class IntCache {
	private static int intCacheSize = 256;
	/**+
	 * A list of pre-allocated int[256] arrays that are currently
	 * unused and can be returned by getIntCache()
	 */
	private static List<int[]> freeSmallArrays = Lists.newArrayList();
	/**+
	 * A list of pre-allocated int[256] arrays that were previously
	 * returned by getIntCache() and which will not be re-used again
	 * until resetIntCache() is called.
	 */
	private static List<int[]> inUseSmallArrays = Lists.newArrayList();
	/**+
	 * A list of pre-allocated int[cacheSize] arrays that are
	 * currently unused and can be returned by getIntCache()
	 */
	private static List<int[]> freeLargeArrays = Lists.newArrayList();
	/**+
	 * A list of pre-allocated int[cacheSize] arrays that were
	 * previously returned by getIntCache() and which will not be
	 * re-used again until resetIntCache() is called.
	 */
	private static List<int[]> inUseLargeArrays = Lists.newArrayList();

	public static synchronized int[] getIntCache(int parInt1) {
		if (parInt1 <= 256) {
			if (freeSmallArrays.isEmpty()) {
				int[] aint4 = new int[256];
				inUseSmallArrays.add(aint4);
				return aint4;
			} else {
				int[] aint3 = (int[]) freeSmallArrays.remove(freeSmallArrays.size() - 1);
				inUseSmallArrays.add(aint3);
				return aint3;
			}
		} else if (parInt1 > intCacheSize) {
			intCacheSize = parInt1;
			freeLargeArrays.clear();
			inUseLargeArrays.clear();
			int[] aint2 = new int[intCacheSize];
			inUseLargeArrays.add(aint2);
			return aint2;
		} else if (freeLargeArrays.isEmpty()) {
			int[] aint1 = new int[intCacheSize];
			inUseLargeArrays.add(aint1);
			return aint1;
		} else {
			int[] aint = (int[]) freeLargeArrays.remove(freeLargeArrays.size() - 1);
			inUseLargeArrays.add(aint);
			return aint;
		}
	}

	/**+
	 * Mark all pre-allocated arrays as available for re-use by
	 * moving them to the appropriate free lists.
	 */
	public static synchronized void resetIntCache() {
		if (!freeLargeArrays.isEmpty()) {
			freeLargeArrays.remove(freeLargeArrays.size() - 1);
		}

		if (!freeSmallArrays.isEmpty()) {
			freeSmallArrays.remove(freeSmallArrays.size() - 1);
		}

		freeLargeArrays.addAll(inUseLargeArrays);
		freeSmallArrays.addAll(inUseSmallArrays);
		inUseLargeArrays.clear();
		inUseSmallArrays.clear();
	}

	/**+
	 * Gets a human-readable string that indicates the sizes of all
	 * the cache fields. Basically a synchronized static toString.
	 */
	public static synchronized String getCacheSizes() {
		return "cache: " + freeLargeArrays.size() + ", tcache: " + freeSmallArrays.size() + ", allocated: "
				+ inUseLargeArrays.size() + ", tallocated: " + inUseSmallArrays.size();
	}
}