package net.minecraft.block.state.pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import net.minecraft.block.state.BlockWorldState;

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
public class FactoryBlockPattern {
	private static final Joiner COMMA_JOIN = Joiner.on(",");
	private final List<String[]> depth = Lists.newArrayList();
	private final Map<Character, Predicate<BlockWorldState>> symbolMap = new HashMap<>();
	private int aisleHeight;
	private int rowWidth;

	private FactoryBlockPattern() {
		this.symbolMap.put(' ', Predicates.alwaysTrue());
	}

	public FactoryBlockPattern aisle(String... aisle) {
		if (!(aisle == null || aisle.length <= 0) && !StringUtils.isEmpty(aisle[0])) {
			if (this.depth.isEmpty()) {
				this.aisleHeight = aisle.length;
				this.rowWidth = aisle[0].length();
			}

			if (aisle.length != this.aisleHeight) {
				throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight
						+ ", but was given one with a height of " + aisle.length + ")");
			} else {
				for (int i = 0; i < aisle.length; ++i) {
					String s = aisle[i];
					if (s.length() != this.rowWidth) {
						throw new IllegalArgumentException(
								"Not all rows in the given aisle are the correct width (expected " + this.rowWidth
										+ ", found one with " + s.length() + ")");
					}

					char[] achar = s.toCharArray();
					for (int j = 0; j < achar.length; ++j) {
						char c0 = achar[j];
						if (!this.symbolMap.containsKey(c0)) {
							this.symbolMap.put(c0, null);
						}
					}
				}

				this.depth.add(aisle);
				return this;
			}
		} else {
			throw new IllegalArgumentException("Empty pattern for aisle");
		}
	}

	public static FactoryBlockPattern start() {
		return new FactoryBlockPattern();
	}

	public FactoryBlockPattern where(char symbol, Predicate<BlockWorldState> blockMatcher) {
		this.symbolMap.put(symbol, blockMatcher);
		return this;
	}

	public BlockPattern build() {
		return new BlockPattern(this.makePredicateArray());
	}

	private Predicate<BlockWorldState>[][][] makePredicateArray() {
		this.checkMissingPredicates();
		Predicate[][][] apredicate = new Predicate[this.depth.size()][this.aisleHeight][this.rowWidth];

		for (int i = 0; i < this.depth.size(); ++i) {
			for (int j = 0; j < this.aisleHeight; ++j) {
				for (int k = 0; k < this.rowWidth; ++k) {
					apredicate[i][j][k] = this.symbolMap.get(((String[]) this.depth.get(i))[j].charAt(k));
				}
			}
		}

		return apredicate;
	}

	private void checkMissingPredicates() {
		ArrayList arraylist = Lists.newArrayList();

		for (Entry<Character, Predicate<BlockWorldState>> entry : this.symbolMap.entrySet()) {
			if (entry.getValue() == null) {
				arraylist.add(entry.getKey());
			}
		}

		if (!arraylist.isEmpty()) {
			throw new IllegalStateException(
					"Predicates for character(s) " + COMMA_JOIN.join(arraylist) + " are missing");
		}
	}
}