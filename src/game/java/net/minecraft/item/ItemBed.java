package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
public class ItemBed extends Item {
	public ItemBed() {
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float var6, float var7, float var8) {
		if (world.isRemote) {
			return true;
		} else if (enumfacing != EnumFacing.UP) {
			return false;
		} else {
			IBlockState iblockstate = world.getBlockState(blockpos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(world, blockpos);
			if (!flag) {
				blockpos = blockpos.up();
			}

			int i = MathHelper.floor_double((double) (entityplayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			EnumFacing enumfacing1 = EnumFacing.getHorizontal(i);
			BlockPos blockpos1 = blockpos.offset(enumfacing1);
			if (entityplayer.canPlayerEdit(blockpos, enumfacing, itemstack)
					&& entityplayer.canPlayerEdit(blockpos1, enumfacing, itemstack)) {
				boolean flag1 = world.getBlockState(blockpos1).getBlock().isReplaceable(world, blockpos1);
				boolean flag2 = flag || world.isAirBlock(blockpos);
				boolean flag3 = flag1 || world.isAirBlock(blockpos1);
				if (flag2 && flag3 && World.doesBlockHaveSolidTopSurface(world, blockpos.down())
						&& World.doesBlockHaveSolidTopSurface(world, blockpos1.down())) {
					IBlockState iblockstate1 = Blocks.bed.getDefaultState()
							.withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false))
							.withProperty(BlockBed.FACING, enumfacing1)
							.withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
					if (world.setBlockState(blockpos, iblockstate1, 3)) {
						IBlockState iblockstate2 = iblockstate1.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
						world.setBlockState(blockpos1, iblockstate2, 3);
					}

					--itemstack.stackSize;
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
}