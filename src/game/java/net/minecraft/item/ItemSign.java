package net.minecraft.item;

import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
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
public class ItemSign extends Item {
	public ItemSign() {
		this.maxStackSize = 16;
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float var6, float var7, float var8) {
		if (enumfacing == EnumFacing.DOWN) {
			return false;
		} else if (!world.getBlockState(blockpos).getBlock().getMaterial().isSolid()) {
			return false;
		} else {
			blockpos = blockpos.offset(enumfacing);
			if (!entityplayer.canPlayerEdit(blockpos, enumfacing, itemstack)) {
				return false;
			} else if (!Blocks.standing_sign.canPlaceBlockAt(world, blockpos)) {
				return false;
			} else if (world.isRemote) {
				return true;
			} else {
				if (enumfacing == EnumFacing.UP) {
					int i = MathHelper
							.floor_double((double) ((entityplayer.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
					world.setBlockState(blockpos, Blocks.standing_sign.getDefaultState()
							.withProperty(BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
				} else {
					world.setBlockState(blockpos,
							Blocks.wall_sign.getDefaultState().withProperty(BlockWallSign.FACING, enumfacing), 3);
				}

				--itemstack.stackSize;
				TileEntity tileentity = world.getTileEntity(blockpos);
				if (tileentity instanceof TileEntitySign
						&& !ItemBlock.setTileEntityNBT(world, entityplayer, blockpos, itemstack)) {
					entityplayer.openEditSign((TileEntitySign) tileentity);
				}

				return true;
			}
		}
	}
}