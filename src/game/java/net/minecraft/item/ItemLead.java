package net.minecraft.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

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
public class ItemLead extends Item {
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
			if (world.isRemote) {
				return true;
			} else {
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

		List<EntityLiving> lst = worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double) i - d0,
				(double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0));
		for (int m = 0, l = lst.size(); m < l; ++m) {
			EntityLiving entityliving = lst.get(m);
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