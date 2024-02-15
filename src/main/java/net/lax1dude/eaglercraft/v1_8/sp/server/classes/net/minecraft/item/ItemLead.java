package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.Block;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.BlockFence;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.creativetab.CreativeTabs;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.EntityLeashKnot;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.EntityLiving;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.Item;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemStack;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.AxisAlignedBB;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.BlockPos;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.EnumFacing;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.World;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

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
public class ItemLead extends Item {

	static {
		__checkIntegratedContextValid("net/minecraft/item/ItemLead");
	}

	public ItemLead() {
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack var1, EntityPlayer entityplayer, World world, BlockPos blockpos, EnumFacing var5,
			float var6, float var7, float var8) {
		Block block = world.getBlockState(blockpos).getBlock();
		if (block instanceof BlockFence) {
			{
				attachToFence(entityplayer, world, blockpos);
				return true;
			}
		} else {
			return false;
		}
	}

	public static boolean attachToFence(EntityPlayer player, World worldIn, BlockPos fence) {
		EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(worldIn, fence);
		boolean flag = false;
		double d0 = 7.0D;
		int i = fence.getX();
		int j = fence.getY();
		int k = fence.getZ();

		for (EntityLiving entityliving : worldIn.getEntitiesWithinAABB(EntityLiving.class,
				new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0,
						(double) k + d0))) {
			if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == player) {
				if (entityleashknot == null) {
					entityleashknot = EntityLeashKnot.createKnot(worldIn, fence);
				}

				entityliving.setLeashedToEntity(entityleashknot, true);
				flag = true;
			}
		}

		return flag;
	}
}