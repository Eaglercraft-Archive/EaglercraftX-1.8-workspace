package net.minecraft.nbt;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.Property;
import net.minecraft.util.StringUtils;

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
public final class NBTUtil {
	/**+
	 * Reads and returns a GameProfile that has been saved to the
	 * passed in NBTTagCompound
	 */
	public static GameProfile readGameProfileFromNBT(NBTTagCompound compound) {
		String s = null;
		String s1 = null;
		if (compound.hasKey("Name", 8)) {
			s = compound.getString("Name");
		}

		if (compound.hasKey("Id", 8)) {
			s1 = compound.getString("Id");
		}

		if (StringUtils.isNullOrEmpty(s) && StringUtils.isNullOrEmpty(s1)) {
			return null;
		} else {
			EaglercraftUUID uuid;
			try {
				uuid = EaglercraftUUID.fromString(s1);
			} catch (Throwable var12) {
				uuid = null;
			}

			Multimap<String, Property> propertiesMap = MultimapBuilder.hashKeys().arrayListValues().build();
			if (compound.hasKey("Properties", 10)) {
				NBTTagCompound nbttagcompound = compound.getCompoundTag("Properties");
				for (String s2 : nbttagcompound.getKeySet()) {
					NBTTagList nbttaglist = nbttagcompound.getTagList(s2, 10);
					for (int i = 0, l = nbttaglist.tagCount(); i < l; ++i) {
						NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
						String value = nbttagcompound1.getString("Value");
						if (!StringUtils.isNullOrEmpty(value)) {
							String sig = nbttagcompound1.getString("Signature");
							if (!StringUtils.isNullOrEmpty(sig)) {
								propertiesMap.put(s2, new Property(s2, value, sig));
							} else {
								propertiesMap.put(s2, new Property(s2, value));
							}
						}
					}
				}
			}

			return new GameProfile(uuid, s, propertiesMap);
		}
	}

	/**+
	 * Writes a GameProfile to an NBTTagCompound.
	 */
	public static NBTTagCompound writeGameProfile(NBTTagCompound tagCompound, GameProfile profile) {
		if (!StringUtils.isNullOrEmpty(profile.getName())) {
			tagCompound.setString("Name", profile.getName());
		}

		if (profile.getId() != null) {
			tagCompound.setString("Id", profile.getId().toString());
		}

		Multimap<String, Property> propertiesMap = profile.getProperties();
		if (!propertiesMap.isEmpty()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			for (String s : profile.getProperties().keySet()) {
				NBTTagList nbttaglist = new NBTTagList();

				for (Property property : profile.getProperties().get(s)) {
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setString("Value", property.getValue());
					if (property.hasSignature()) {
						nbttagcompound1.setString("Signature", property.getSignature());
					}

					nbttaglist.appendTag(nbttagcompound1);
				}

				nbttagcompound.setTag(s, nbttaglist);
			}
			tagCompound.setTag("Properties", nbttagcompound);
		}

		return tagCompound;
	}

	public static boolean func_181123_a(NBTBase parNBTBase, NBTBase parNBTBase2, boolean parFlag) {
		if (parNBTBase == parNBTBase2) {
			return true;
		} else if (parNBTBase == null) {
			return true;
		} else if (parNBTBase2 == null) {
			return false;
		} else if (!parNBTBase.getClass().equals(parNBTBase2.getClass())) {
			return false;
		} else if (parNBTBase instanceof NBTTagCompound) {
			NBTTagCompound nbttagcompound = (NBTTagCompound) parNBTBase;
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) parNBTBase2;

			for (String s : nbttagcompound.getKeySet()) {
				NBTBase nbtbase1 = nbttagcompound.getTag(s);
				if (!func_181123_a(nbtbase1, nbttagcompound1.getTag(s), parFlag)) {
					return false;
				}
			}

			return true;
		} else if (parNBTBase instanceof NBTTagList && parFlag) {
			NBTTagList nbttaglist = (NBTTagList) parNBTBase;
			NBTTagList nbttaglist1 = (NBTTagList) parNBTBase2;
			if (nbttaglist.tagCount() == 0) {
				return nbttaglist1.tagCount() == 0;
			} else {
				for (int i = 0; i < nbttaglist.tagCount(); ++i) {
					NBTBase nbtbase = nbttaglist.get(i);
					boolean flag = false;

					for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
						if (func_181123_a(nbtbase, nbttaglist1.get(j), parFlag)) {
							flag = true;
							break;
						}
					}

					if (!flag) {
						return false;
					}
				}

				return true;
			}
		} else {
			return parNBTBase.equals(parNBTBase2);
		}
	}
}