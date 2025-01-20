package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Set;

import com.google.common.collect.Lists;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IntegerCache;

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
public class VisGraph {
	private static final int field_178616_a = (int) Math.pow(16.0D, 0.0D);
	private static final int field_178614_b = (int) Math.pow(16.0D, 1.0D);
	private static final int field_178615_c = (int) Math.pow(16.0D, 2.0D);
	private final BitSet field_178612_d = new BitSet(4096);
	private static final int[] field_178613_e = new int[1352];
	private int field_178611_f = 4096;

	public void func_178606_a(BlockPos pos) {
		this.field_178612_d.set(getIndex(pos), true);
		--this.field_178611_f;
	}

	private static int getIndex(BlockPos pos) {
		return getIndex(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
	}

	private static int getIndex(int x, int y, int z) {
		return x << 0 | y << 8 | z << 4;
	}

	public SetVisibility computeVisibility() {
		SetVisibility setvisibility = new SetVisibility();
		if (4096 - this.field_178611_f < 256) {
			setvisibility.setAllVisible(true);
		} else if (this.field_178611_f == 0) {
			setvisibility.setAllVisible(false);
		} else {
			for (int i = 0; i < field_178613_e.length; ++i) {
				if (!this.field_178612_d.get(field_178613_e[i])) {
					setvisibility.setManyVisible(this.func_178604_a(field_178613_e[i]));
				}
			}
		}

		return setvisibility;
	}

	public Set<EnumFacing> func_178609_b(BlockPos pos) {
		return this.func_178604_a(getIndex(pos));
	}

	private Set<EnumFacing> func_178604_a(int parInt1) {
		EnumSet enumset = EnumSet.noneOf(EnumFacing.class);
		LinkedList linkedlist = Lists.newLinkedList();
		linkedlist.add(IntegerCache.func_181756_a(parInt1));
		this.field_178612_d.set(parInt1, true);

		while (!linkedlist.isEmpty()) {
			int i = ((Integer) linkedlist.poll()).intValue();
			this.func_178610_a(i, enumset);

			EnumFacing[] facings = EnumFacing._VALUES;
			for (int k = 0; k < facings.length; ++k) {
				EnumFacing enumfacing = facings[k];
				int j = this.func_178603_a(i, enumfacing);
				if (j >= 0 && !this.field_178612_d.get(j)) {
					this.field_178612_d.set(j, true);
					linkedlist.add(IntegerCache.func_181756_a(j));
				}
			}
		}

		return enumset;
	}

	private void func_178610_a(int parInt1, Set<EnumFacing> parSet) {
		int i = parInt1 >> 0 & 15;
		if (i == 0) {
			parSet.add(EnumFacing.WEST);
		} else if (i == 15) {
			parSet.add(EnumFacing.EAST);
		}

		int j = parInt1 >> 8 & 15;
		if (j == 0) {
			parSet.add(EnumFacing.DOWN);
		} else if (j == 15) {
			parSet.add(EnumFacing.UP);
		}

		int k = parInt1 >> 4 & 15;
		if (k == 0) {
			parSet.add(EnumFacing.NORTH);
		} else if (k == 15) {
			parSet.add(EnumFacing.SOUTH);
		}

	}

	private int func_178603_a(int parInt1, EnumFacing parEnumFacing) {
		switch (parEnumFacing) {
		case DOWN:
			if ((parInt1 >> 8 & 15) == 0) {
				return -1;
			}

			return parInt1 - field_178615_c;
		case UP:
			if ((parInt1 >> 8 & 15) == 15) {
				return -1;
			}

			return parInt1 + field_178615_c;
		case NORTH:
			if ((parInt1 >> 4 & 15) == 0) {
				return -1;
			}

			return parInt1 - field_178614_b;
		case SOUTH:
			if ((parInt1 >> 4 & 15) == 15) {
				return -1;
			}

			return parInt1 + field_178614_b;
		case WEST:
			if ((parInt1 >> 0 & 15) == 0) {
				return -1;
			}

			return parInt1 - field_178616_a;
		case EAST:
			if ((parInt1 >> 0 & 15) == 15) {
				return -1;
			}

			return parInt1 + field_178616_a;
		default:
			return -1;
		}
	}

	static {
		boolean flag = false;
		boolean flag1 = true;
		int i = 0;

		for (int j = 0; j < 16; ++j) {
			for (int k = 0; k < 16; ++k) {
				for (int l = 0; l < 16; ++l) {
					if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
						field_178613_e[i++] = getIndex(j, k, l);
					}
				}
			}
		}

	}
}