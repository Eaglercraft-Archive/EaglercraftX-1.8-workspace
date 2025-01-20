package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
public class ItemDoor extends Item {
	private Block block;

	public ItemDoor(Block block) {
		this.block = block;
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float var6, float var7, float var8) {
		if (enumfacing != EnumFacing.UP) {
			return false;
		} else {
			IBlockState iblockstate = world.getBlockState(blockpos);
			Block blockx = iblockstate.getBlock();
			if (!blockx.isReplaceable(world, blockpos)) {
				blockpos = blockpos.offset(enumfacing);
			}

			if (!entityplayer.canPlayerEdit(blockpos, enumfacing, itemstack)) {
				return false;
			} else if (!this.block.canPlaceBlockAt(world, blockpos)) {
				return false;
			} else {
				placeDoor(world, blockpos, EnumFacing.fromAngle((double) entityplayer.rotationYaw), this.block);
				--itemstack.stackSize;
				return true;
			}
		}
	}

	public static void placeDoor(World worldIn, BlockPos pos, EnumFacing facing, Block door) {
		BlockPos blockpos = pos.offset(facing.rotateY());
		BlockPos blockpos1 = pos.offset(facing.rotateYCCW());
		int i = (worldIn.getBlockState(blockpos1).getBlock().isNormalCube() ? 1 : 0)
				+ (worldIn.getBlockState(blockpos1.up()).getBlock().isNormalCube() ? 1 : 0);
		int j = (worldIn.getBlockState(blockpos).getBlock().isNormalCube() ? 1 : 0)
				+ (worldIn.getBlockState(blockpos.up()).getBlock().isNormalCube() ? 1 : 0);
		boolean flag = worldIn.getBlockState(blockpos1).getBlock() == door
				|| worldIn.getBlockState(blockpos1.up()).getBlock() == door;
		boolean flag1 = worldIn.getBlockState(blockpos).getBlock() == door
				|| worldIn.getBlockState(blockpos.up()).getBlock() == door;
		boolean flag2 = false;
		if (flag && !flag1 || j > i) {
			flag2 = true;
		}

		BlockPos blockpos2 = pos.up();
		IBlockState iblockstate = door.getDefaultState().withProperty(BlockDoor.FACING, facing).withProperty(
				BlockDoor.HINGE, flag2 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT);
		worldIn.setBlockState(pos, iblockstate.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
		worldIn.setBlockState(blockpos2, iblockstate.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
		worldIn.notifyNeighborsOfStateChange(pos, door);
		worldIn.notifyNeighborsOfStateChange(blockpos2, door);
	}
}