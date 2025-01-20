package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
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
public class ItemSlab extends ItemBlock {
	private final BlockSlab singleSlab;
	private final BlockSlab doubleSlab;

	public ItemSlab(Block block, BlockSlab singleSlab, BlockSlab doubleSlab) {
		super(block);
		this.singleSlab = singleSlab;
		this.doubleSlab = doubleSlab;
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	/**+
	 * Converts the given ItemStack damage value into a metadata
	 * value to be placed in the world when this Item is placed as a
	 * Block (mostly used with ItemBlocks).
	 */
	public int getMetadata(int i) {
		return i;
	}

	/**+
	 * Returns the unlocalized name of this item. This version
	 * accepts an ItemStack so different stacks can have different
	 * names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack itemstack) {
		return this.singleSlab.getUnlocalizedName(itemstack.getMetadata());
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float f, float f1, float f2) {
		if (itemstack.stackSize == 0) {
			return false;
		} else if (!entityplayer.canPlayerEdit(blockpos.offset(enumfacing), enumfacing, itemstack)) {
			return false;
		} else {
			Object object = this.singleSlab.getVariant(itemstack);
			IBlockState iblockstate = world.getBlockState(blockpos);
			if (iblockstate.getBlock() == this.singleSlab) {
				IProperty iproperty = this.singleSlab.getVariantProperty();
				Comparable comparable = iblockstate.getValue(iproperty);
				BlockSlab.EnumBlockHalf blockslab$enumblockhalf = (BlockSlab.EnumBlockHalf) iblockstate
						.getValue(BlockSlab.HALF);
				if ((enumfacing == EnumFacing.UP && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.BOTTOM
						|| enumfacing == EnumFacing.DOWN && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.TOP)
						&& comparable == object) {
					IBlockState iblockstate1 = this.doubleSlab.getDefaultState().withProperty(iproperty, comparable);
					if (world.checkNoEntityCollision(
							this.doubleSlab.getCollisionBoundingBox(world, blockpos, iblockstate1))
							&& world.setBlockState(blockpos, iblockstate1, 3)) {
						world.playSoundEffect((double) ((float) blockpos.getX() + 0.5F),
								(double) ((float) blockpos.getY() + 0.5F), (double) ((float) blockpos.getZ() + 0.5F),
								this.doubleSlab.stepSound.getPlaceSound(),
								(this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F,
								this.doubleSlab.stepSound.getFrequency() * 0.8F);
						--itemstack.stackSize;
					}

					return true;
				}
			}

			return this.tryPlace(itemstack, world, blockpos.offset(enumfacing), object) ? true
					: super.onItemUse(itemstack, entityplayer, world, blockpos, enumfacing, f, f1, f2);
		}
	}

	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player,
			ItemStack stack) {
		BlockPos blockpos = pos;
		IProperty iproperty = this.singleSlab.getVariantProperty();
		Object object = this.singleSlab.getVariant(stack);
		IBlockState iblockstate = worldIn.getBlockState(pos);
		if (iblockstate.getBlock() == this.singleSlab) {
			boolean flag = iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
			if ((side == EnumFacing.UP && !flag || side == EnumFacing.DOWN && flag)
					&& object == iblockstate.getValue(iproperty)) {
				return true;
			}
		}

		pos = pos.offset(side);
		IBlockState iblockstate1 = worldIn.getBlockState(pos);
		return iblockstate1.getBlock() == this.singleSlab && object == iblockstate1.getValue(iproperty) ? true
				: super.canPlaceBlockOnSide(worldIn, blockpos, side, player, stack);
	}

	private boolean tryPlace(ItemStack stack, World worldIn, BlockPos pos, Object variantInStack) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		if (iblockstate.getBlock() == this.singleSlab) {
			Comparable comparable = iblockstate.getValue(this.singleSlab.getVariantProperty());
			if (comparable == variantInStack) {
				IBlockState iblockstate1 = this.doubleSlab.getDefaultState()
						.withProperty((IProperty) this.singleSlab.getVariantProperty(), comparable);
				if (worldIn.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(worldIn, pos, iblockstate1))
						&& worldIn.setBlockState(pos, iblockstate1, 3)) {
					worldIn.playSoundEffect((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F),
							(double) ((float) pos.getZ() + 0.5F), this.doubleSlab.stepSound.getPlaceSound(),
							(this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F,
							this.doubleSlab.stepSound.getFrequency() * 0.8F);
					--stack.stackSize;
				}

				return true;
			}
		}

		return false;
	}
}