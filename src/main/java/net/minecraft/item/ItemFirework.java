package net.minecraft.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class ItemFirework extends Item {

	/**+
	 * allows items to add custom lines of information to the
	 * mouseover description
	 */
	public void addInformation(ItemStack itemstack, EntityPlayer var2, List<String> list, boolean var4) {
		if (itemstack.hasTagCompound()) {
			NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("Fireworks");
			if (nbttagcompound != null) {
				if (nbttagcompound.hasKey("Flight", 99)) {
					list.add(StatCollector.translateToLocal("item.fireworks.flight") + " "
							+ nbttagcompound.getByte("Flight"));
				}

				NBTTagList nbttaglist = nbttagcompound.getTagList("Explosions", 10);
				if (nbttaglist != null && nbttaglist.tagCount() > 0) {
					for (int i = 0; i < nbttaglist.tagCount(); ++i) {
						NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
						ArrayList arraylist = Lists.newArrayList();
						ItemFireworkCharge.addExplosionInfo(nbttagcompound1, arraylist);
						if (arraylist.size() > 0) {
							for (int j = 1; j < arraylist.size(); ++j) {
								arraylist.set(j, "  " + (String) arraylist.get(j));
							}

							list.addAll(arraylist);
						}
					}
				}

			}
		}
	}
}