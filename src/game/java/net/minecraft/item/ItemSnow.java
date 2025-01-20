package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
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
public class ItemSnow extends ItemBlock {
	public ItemSnow(Block block) {
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float f, float f1, float f2) {
		if (itemstack.stackSize == 0) {
			return false;
		} else if (!entityplayer.canPlayerEdit(blockpos, enumfacing, itemstack)) {
			return false;
		} else {
			IBlockState iblockstate = world.getBlockState(blockpos);
			Block block = iblockstate.getBlock();
			BlockPos blockpos1 = blockpos;
			if ((enumfacing != EnumFacing.UP || block != this.block) && !block.isReplaceable(world, blockpos)) {
				blockpos1 = blockpos.offset(enumfacing);
				iblockstate = world.getBlockState(blockpos1);
				block = iblockstate.getBlock();
			}

			if (block == this.block) {
				int i = ((Integer) iblockstate.getValue(BlockSnow.LAYERS)).intValue();
				if (i <= 7) {
					IBlockState iblockstate1 = iblockstate.withProperty(BlockSnow.LAYERS, Integer.valueOf(i + 1));
					AxisAlignedBB axisalignedbb = this.block.getCollisionBoundingBox(world, blockpos1, iblockstate1);
					if (axisalignedbb != null && world.checkNoEntityCollision(axisalignedbb)
							&& world.setBlockState(blockpos1, iblockstate1, 2)) {
						world.playSoundEffect((double) ((float) blockpos1.getX() + 0.5F),
								(double) ((float) blockpos1.getY() + 0.5F), (double) ((float) blockpos1.getZ() + 0.5F),
								this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F,
								this.block.stepSound.getFrequency() * 0.8F);
						--itemstack.stackSize;
						return true;
					}
				}
			}

			return super.onItemUse(itemstack, entityplayer, world, blockpos1, enumfacing, f, f1, f2);
		}
	}

	/**+
	 * Converts the given ItemStack damage value into a metadata
	 * value to be placed in the world when this Item is placed as a
	 * Block (mostly used with ItemBlocks).
	 */
	public int getMetadata(int i) {
		return i;
	}
}