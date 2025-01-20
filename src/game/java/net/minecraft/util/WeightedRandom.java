package net.minecraft.util;

import java.util.Collection;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

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
public class WeightedRandom {
	/**+
	 * Returns the total weight of all items in a collection.
	 */
	public static int getTotalWeight(Collection<? extends WeightedRandom.Item> collection) {
		int i = 0;

		for (WeightedRandom.Item weightedrandom$item : collection) {
			i += weightedrandom$item.itemWeight;
		}

		return i;
	}

	public static <T extends WeightedRandom.Item> T getRandomItem(EaglercraftRandom random, Collection<T> collection,
			int totalWeight) {
		if (totalWeight <= 0) {
			throw new IllegalArgumentException();
		} else {
			int i = random.nextInt(totalWeight);
			/**+
			 * Returns a random choice from the input items.
			 */
			return getRandomItem(collection, i);
		}
	}

	public static <T extends WeightedRandom.Item> T getRandomItem(Collection<T> collection, int weight) {
		for (WeightedRandom.Item weightedrandom$item : collection) {
			weight -= weightedrandom$item.itemWeight;
			if (weight < 0) {
				return (T) weightedrandom$item;
			}
		}

		return (T) null;
	}

	public static <T extends WeightedRandom.Item> T getRandomItem(EaglercraftRandom random, Collection<T> collection) {
		/**+
		 * Returns a random choice from the input items.
		 */
		return getRandomItem(random, collection, getTotalWeight(collection));
	}

	public static class Item {
		protected int itemWeight;

		public Item(int itemWeightIn) {
			this.itemWeight = itemWeightIn;
		}
	}
}