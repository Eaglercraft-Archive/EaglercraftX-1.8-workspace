package net.minecraft.potion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.carrotsearch.hppc.IntIntHashMap;
import com.carrotsearch.hppc.IntIntMap;
import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.ObjectContainer;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.google.common.collect.Lists;

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
public class PotionHelper {
	public static final String field_77924_a = null;
	public static final String sugarEffect = "-0+1-2-3&4-4+13";
	public static final String ghastTearEffect = "+0-1-2-3&4-4+13";
	public static final String spiderEyeEffect = "-0-1+2-3&4-4+13";
	public static final String fermentedSpiderEyeEffect = "-0+3-4+13";
	public static final String speckledMelonEffect = "+0-1+2-3&4-4+13";
	public static final String blazePowderEffect = "+0-1-2+3&4-4+13";
	public static final String magmaCreamEffect = "+0+1-2-3&4-4+13";
	public static final String redstoneEffect = "-5+6-7";
	public static final String glowstoneEffect = "+5-6-7";
	public static final String gunpowderEffect = "+14&13-13";
	public static final String goldenCarrotEffect = "-0+1+2-3+13&4-4";
	public static final String pufferfishEffect = "+0-1+2+3+13&4-4";
	public static final String rabbitFootEffect = "+0+1-2+3&4-4+13";
	private static final IntObjectMap<String> potionRequirements = new IntObjectHashMap<>();
	private static final IntObjectMap<String> potionAmplifiers = new IntObjectHashMap<>();
	private static final IntIntMap DATAVALUE_COLORS = new IntIntHashMap();
	/**+
	 * An array of possible potion prefix names, as translation IDs.
	 */
	private static final String[] potionPrefixes = new String[] { "potion.prefix.mundane",
			"potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky",
			"potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward",
			"potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered",
			"potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick",
			"potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing",
			"potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent",
			"potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh",
			"potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky" };

	/**+
	 * Checks if the bit at 1 << j is on in i.
	 */
	public static boolean checkFlag(int parInt1, int parInt2) {
		return (parInt1 & 1 << parInt2) != 0;
	}

	/**+
	 * Returns 1 if the flag is set, 0 if it is not set.
	 */
	private static int isFlagSet(int parInt1, int parInt2) {
		/**+
		 * Checks if the bit at 1 << j is on in i.
		 */
		return checkFlag(parInt1, parInt2) ? 1 : 0;
	}

	/**+
	 * Returns 0 if the flag is set, 1 if it is not set.
	 */
	private static int isFlagUnset(int parInt1, int parInt2) {
		/**+
		 * Checks if the bit at 1 << j is on in i.
		 */
		return checkFlag(parInt1, parInt2) ? 0 : 1;
	}

	/**+
	 * Given a potion data value, get its prefix index number.
	 */
	public static int getPotionPrefixIndex(int dataValue) {
		return func_77908_a(dataValue, 5, 4, 3, 2, 1);
	}

	/**+
	 * Given a {@link Collection}<{@link PotionEffect}> will return
	 * an Integer color.
	 */
	public static int calcPotionLiquidColor(Collection<PotionEffect> parCollection) {
		int i = 3694022;
		if (parCollection != null && !parCollection.isEmpty()) {
			float f = 0.0F;
			float f1 = 0.0F;
			float f2 = 0.0F;
			float f3 = 0.0F;

			for (PotionEffect potioneffect : parCollection) {
				if (potioneffect.getIsShowParticles()) {
					int j = Potion.potionTypes[potioneffect.getPotionID()].getLiquidColor();

					for (int k = 0; k <= potioneffect.getAmplifier(); ++k) {
						f += (float) (j >> 16 & 255) / 255.0F;
						f1 += (float) (j >> 8 & 255) / 255.0F;
						f2 += (float) (j >> 0 & 255) / 255.0F;
						++f3;
					}
				}
			}

			if (f3 == 0.0F) {
				return 0;
			} else {
				f = f / f3 * 255.0F;
				f1 = f1 / f3 * 255.0F;
				f2 = f2 / f3 * 255.0F;
				return (int) f << 16 | (int) f1 << 8 | (int) f2;
			}
		} else {
			return i;
		}
	}

	/**+
	 * Given a {@link Collection}<{@link PotionEffect}> will return
	 * an Integer color.
	 */
	public static int calcPotionLiquidColor(ObjectContainer<PotionEffect> parCollection) {
		int i = 3694022;
		if (parCollection != null && !parCollection.isEmpty()) {
			float f = 0.0F;
			float f1 = 0.0F;
			float f2 = 0.0F;
			float f3 = 0.0F;

			for (ObjectCursor<PotionEffect> potioneffect_ : parCollection) {
				PotionEffect potioneffect = potioneffect_.value;
				if (potioneffect.getIsShowParticles()) {
					int j = Potion.potionTypes[potioneffect.getPotionID()].getLiquidColor();

					for (int k = 0; k <= potioneffect.getAmplifier(); ++k) {
						f += (float) (j >> 16 & 255) / 255.0F;
						f1 += (float) (j >> 8 & 255) / 255.0F;
						f2 += (float) (j >> 0 & 255) / 255.0F;
						++f3;
					}
				}
			}

			if (f3 == 0.0F) {
				return 0;
			} else {
				f = f / f3 * 255.0F;
				f1 = f1 / f3 * 255.0F;
				f2 = f2 / f3 * 255.0F;
				return (int) f << 16 | (int) f1 << 8 | (int) f2;
			}
		} else {
			return i;
		}
	}

	/**+
	 * Check whether a {@link Collection}<{@link PotionEffect}> are
	 * all ambient.
	 */
	public static boolean getAreAmbient(Collection<PotionEffect> potionEffects) {
		for (PotionEffect potioneffect : potionEffects) {
			if (!potioneffect.getIsAmbient()) {
				return false;
			}
		}

		return true;
	}

	/**+
	 * Check whether a {@link Collection}<{@link PotionEffect}> are
	 * all ambient.
	 */
	public static boolean getAreAmbient(ObjectContainer<PotionEffect> potionEffects) {
		for (ObjectCursor<PotionEffect> potioneffect : potionEffects) {
			if (!potioneffect.value.getIsAmbient()) {
				return false;
			}
		}

		return true;
	}

	/**+
	 * Given a potion data value, get the associated liquid color
	 * (optionally bypassing the cache)
	 */
	public static int getLiquidColor(int dataValue, boolean bypassCache) {
		if (!bypassCache) {
			int i = DATAVALUE_COLORS.getOrDefault(dataValue, -1);
			if (i != -1) {
				return i;
			} else {
				i = calcPotionLiquidColor(getPotionEffects(dataValue, false));
				DATAVALUE_COLORS.put(dataValue, i);
				return i;
			}
		} else {
			/**+
			 * Given a {@link Collection}<{@link PotionEffect}> will return
			 * an Integer color.
			 */
			return calcPotionLiquidColor(getPotionEffects(dataValue, true));
		}
	}

	/**+
	 * Given a potion data value, get its prefix as a translation
	 * ID.
	 */
	public static String getPotionPrefix(int dataValue) {
		int i = getPotionPrefixIndex(dataValue);
		return potionPrefixes[i];
	}

	private static int func_77904_a(boolean parFlag, boolean parFlag2, boolean parFlag3, int parInt1, int parInt2,
			int parInt3, int parInt4) {
		int i = 0;
		if (parFlag) {
			i = isFlagUnset(parInt4, parInt2);
		} else if (parInt1 != -1) {
			if (parInt1 == 0 && countSetFlags(parInt4) == parInt2) {
				i = 1;
			} else if (parInt1 == 1 && countSetFlags(parInt4) > parInt2) {
				i = 1;
			} else if (parInt1 == 2 && countSetFlags(parInt4) < parInt2) {
				i = 1;
			}
		} else {
			i = isFlagSet(parInt4, parInt2);
		}

		if (parFlag2) {
			i *= parInt3;
		}

		if (parFlag3) {
			i *= -1;
		}

		return i;
	}

	/**+
	 * Returns the number of 1 bits in the given integer.
	 */
	private static int countSetFlags(int parInt1) {
		int i;
		for (i = 0; parInt1 > 0; ++i) {
			parInt1 &= parInt1 - 1;
		}

		return i;
	}

	private static int parsePotionEffects(String parString1, int parInt1, int parInt2, int parInt3) {
		if (parInt1 < parString1.length() && parInt2 >= 0 && parInt1 < parInt2) {
			int i = parString1.indexOf(124, parInt1);
			if (i >= 0 && i < parInt2) {
				int k1 = parsePotionEffects(parString1, parInt1, i - 1, parInt3);
				if (k1 > 0) {
					return k1;
				} else {
					int i2 = parsePotionEffects(parString1, i + 1, parInt2, parInt3);
					return i2 > 0 ? i2 : 0;
				}
			} else {
				int j = parString1.indexOf(38, parInt1);
				if (j >= 0 && j < parInt2) {
					int l1 = parsePotionEffects(parString1, parInt1, j - 1, parInt3);
					if (l1 <= 0) {
						return 0;
					} else {
						int j2 = parsePotionEffects(parString1, j + 1, parInt2, parInt3);
						return j2 <= 0 ? 0 : (l1 > j2 ? l1 : j2);
					}
				} else {
					boolean flag = false;
					boolean flag1 = false;
					boolean flag2 = false;
					boolean flag3 = false;
					boolean flag4 = false;
					byte b0 = -1;
					int k = 0;
					int l = 0;
					int i1 = 0;

					for (int j1 = parInt1; j1 < parInt2; ++j1) {
						char c0 = parString1.charAt(j1);
						if (c0 >= 48 && c0 <= 57) {
							if (flag) {
								l = c0 - 48;
								flag1 = true;
							} else {
								k = k * 10;
								k = k + (c0 - 48);
								flag2 = true;
							}
						} else if (c0 == 42) {
							flag = true;
						} else if (c0 == 33) {
							if (flag2) {
								i1 += func_77904_a(flag3, flag1, flag4, b0, k, l, parInt3);
								flag3 = false;
								flag4 = false;
								flag = false;
								flag1 = false;
								flag2 = false;
								l = 0;
								k = 0;
								b0 = -1;
							}

							flag3 = true;
						} else if (c0 == 45) {
							if (flag2) {
								i1 += func_77904_a(flag3, flag1, flag4, b0, k, l, parInt3);
								flag3 = false;
								flag4 = false;
								flag = false;
								flag1 = false;
								flag2 = false;
								l = 0;
								k = 0;
								b0 = -1;
							}

							flag4 = true;
						} else if (c0 != 61 && c0 != 60 && c0 != 62) {
							if (c0 == 43 && flag2) {
								i1 += func_77904_a(flag3, flag1, flag4, b0, k, l, parInt3);
								flag3 = false;
								flag4 = false;
								flag = false;
								flag1 = false;
								flag2 = false;
								l = 0;
								k = 0;
								b0 = -1;
							}
						} else {
							if (flag2) {
								i1 += func_77904_a(flag3, flag1, flag4, b0, k, l, parInt3);
								flag3 = false;
								flag4 = false;
								flag = false;
								flag1 = false;
								flag2 = false;
								l = 0;
								k = 0;
								b0 = -1;
							}

							if (c0 == 61) {
								b0 = 0;
							} else if (c0 == 60) {
								b0 = 2;
							} else if (c0 == 62) {
								b0 = 1;
							}
						}
					}

					if (flag2) {
						i1 += func_77904_a(flag3, flag1, flag4, b0, k, l, parInt3);
					}

					return i1;
				}
			}
		} else {
			return 0;
		}
	}

	/**+
	 * Returns a list of effects for the specified potion damage
	 * value.
	 */
	public static List<PotionEffect> getPotionEffects(int parInt1, boolean parFlag) {
		ArrayList arraylist = null;

		for (int k = 0; k < Potion.potionTypes.length; ++k) {
			Potion potion = Potion.potionTypes[k];
			if (potion != null && (!potion.isUsable() || parFlag)) {
				String s = potionRequirements.get(potion.getId());
				if (s != null) {
					int i = parsePotionEffects(s, 0, s.length(), parInt1);
					if (i > 0) {
						int j = 0;
						String s1 = potionAmplifiers.get(potion.getId());
						if (s1 != null) {
							j = parsePotionEffects(s1, 0, s1.length(), parInt1);
							if (j < 0) {
								j = 0;
							}
						}

						if (potion.isInstant()) {
							i = 1;
						} else {
							i = 1200 * (i * 3 + (i - 1) * 2);
							i = i >> j;
							i = (int) Math.round((double) i * potion.getEffectiveness());
							if ((parInt1 & 16384) != 0) {
								i = (int) Math.round((double) i * 0.75D + 0.5D);
							}
						}

						if (arraylist == null) {
							arraylist = Lists.newArrayList();
						}

						PotionEffect potioneffect = new PotionEffect(potion.getId(), i, j);
						if ((parInt1 & 16384) != 0) {
							potioneffect.setSplashPotion(true);
						}

						arraylist.add(potioneffect);
					}
				}
			}
		}

		return arraylist;
	}

	/**+
	 * Manipulates the specified bit of the potion damage value
	 * according to the rules passed from applyIngredient.
	 */
	private static int brewBitOperations(int parInt1, int parInt2, boolean parFlag, boolean parFlag2,
			boolean parFlag3) {
		if (parFlag3) {
			if (!checkFlag(parInt1, parInt2)) {
				return 0;
			}
		} else if (parFlag) {
			parInt1 &= ~(1 << parInt2);
		} else if (parFlag2) {
			if ((parInt1 & 1 << parInt2) == 0) {
				parInt1 |= 1 << parInt2;
			} else {
				parInt1 &= ~(1 << parInt2);
			}
		} else {
			parInt1 |= 1 << parInt2;
		}

		return parInt1;
	}

	/**+
	 * Returns the new potion damage value after the specified
	 * ingredient info is applied to the specified potion.
	 */
	public static int applyIngredient(int parInt1, String parString1) {
		byte b0 = 0;
		int i = parString1.length();
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		int j = 0;

		for (int k = b0; k < i; ++k) {
			char c0 = parString1.charAt(k);
			if (c0 >= 48 && c0 <= 57) {
				j = j * 10;
				j = j + (c0 - 48);
				flag = true;
			} else if (c0 == 33) {
				if (flag) {
					parInt1 = brewBitOperations(parInt1, j, flag2, flag1, flag3);
					flag3 = false;
					flag1 = false;
					flag2 = false;
					flag = false;
					j = 0;
				}

				flag1 = true;
			} else if (c0 == 45) {
				if (flag) {
					parInt1 = brewBitOperations(parInt1, j, flag2, flag1, flag3);
					flag3 = false;
					flag1 = false;
					flag2 = false;
					flag = false;
					j = 0;
				}

				flag2 = true;
			} else if (c0 == 43) {
				if (flag) {
					parInt1 = brewBitOperations(parInt1, j, flag2, flag1, flag3);
					flag3 = false;
					flag1 = false;
					flag2 = false;
					flag = false;
					j = 0;
				}
			} else if (c0 == 38) {
				if (flag) {
					parInt1 = brewBitOperations(parInt1, j, flag2, flag1, flag3);
					flag3 = false;
					flag1 = false;
					flag2 = false;
					flag = false;
					j = 0;
				}

				flag3 = true;
			}
		}

		if (flag) {
			parInt1 = brewBitOperations(parInt1, j, flag2, flag1, flag3);
		}

		return parInt1 & 32767;
	}

	public static int func_77908_a(int parInt1, int parInt2, int parInt3, int parInt4, int parInt5, int parInt6) {
		return (checkFlag(parInt1, parInt2) ? 16 : 0) | (checkFlag(parInt1, parInt3) ? 8 : 0)
				| (checkFlag(parInt1, parInt4) ? 4 : 0) | (checkFlag(parInt1, parInt5) ? 2 : 0)
				| (checkFlag(parInt1, parInt6) ? 1 : 0);
	}

	static {
		potionRequirements.put(Integer.valueOf(Potion.regeneration.getId()), "0 & !1 & !2 & !3 & 0+6");
		potionRequirements.put(Integer.valueOf(Potion.moveSpeed.getId()), "!0 & 1 & !2 & !3 & 1+6");
		potionRequirements.put(Integer.valueOf(Potion.fireResistance.getId()), "0 & 1 & !2 & !3 & 0+6");
		potionRequirements.put(Integer.valueOf(Potion.heal.getId()), "0 & !1 & 2 & !3");
		potionRequirements.put(Integer.valueOf(Potion.poison.getId()), "!0 & !1 & 2 & !3 & 2+6");
		potionRequirements.put(Integer.valueOf(Potion.weakness.getId()), "!0 & !1 & !2 & 3 & 3+6");
		potionRequirements.put(Integer.valueOf(Potion.harm.getId()), "!0 & !1 & 2 & 3");
		potionRequirements.put(Integer.valueOf(Potion.moveSlowdown.getId()), "!0 & 1 & !2 & 3 & 3+6");
		potionRequirements.put(Integer.valueOf(Potion.damageBoost.getId()), "0 & !1 & !2 & 3 & 3+6");
		potionRequirements.put(Integer.valueOf(Potion.nightVision.getId()), "!0 & 1 & 2 & !3 & 2+6");
		potionRequirements.put(Integer.valueOf(Potion.invisibility.getId()), "!0 & 1 & 2 & 3 & 2+6");
		potionRequirements.put(Integer.valueOf(Potion.waterBreathing.getId()), "0 & !1 & 2 & 3 & 2+6");
		potionRequirements.put(Integer.valueOf(Potion.jump.getId()), "0 & 1 & !2 & 3 & 3+6");
		potionAmplifiers.put(Integer.valueOf(Potion.moveSpeed.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.digSpeed.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.damageBoost.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.regeneration.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.harm.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.heal.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.resistance.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.poison.getId()), "5");
		potionAmplifiers.put(Integer.valueOf(Potion.jump.getId()), "5");
	}
}