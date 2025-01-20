package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
public class ItemHoe extends Item {
	protected Item.ToolMaterial theToolMaterial;

	public ItemHoe(Item.ToolMaterial material) {
		this.theToolMaterial = material;
		this.maxStackSize = 1;
		this.setMaxDamage(material.getMaxUses());
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float var6, float var7, float var8) {
		if (!entityplayer.canPlayerEdit(blockpos.offset(enumfacing), enumfacing, itemstack)) {
			return false;
		} else {
			IBlockState iblockstate = world.getBlockState(blockpos);
			Block block = iblockstate.getBlock();
			if (enumfacing != EnumFacing.DOWN
					&& world.getBlockState(blockpos.up()).getBlock().getMaterial() == Material.air) {
				if (block == Blocks.grass) {
					return this.useHoe(itemstack, entityplayer, world, blockpos, Blocks.farmland.getDefaultState());
				}

				if (block == Blocks.dirt) {
					switch ((BlockDirt.DirtType) iblockstate.getValue(BlockDirt.VARIANT)) {
					case DIRT:
						return this.useHoe(itemstack, entityplayer, world, blockpos, Blocks.farmland.getDefaultState());
					case COARSE_DIRT:
						return this.useHoe(itemstack, entityplayer, world, blockpos,
								Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
					}
				}
			}

			return false;
		}
	}

	protected boolean useHoe(ItemStack stack, EntityPlayer player, World worldIn, BlockPos target,
			IBlockState newState) {
		worldIn.playSoundEffect((double) ((float) target.getX() + 0.5F), (double) ((float) target.getY() + 0.5F),
				(double) ((float) target.getZ() + 0.5F), newState.getBlock().stepSound.getStepSound(),
				(newState.getBlock().stepSound.getVolume() + 1.0F) / 2.0F,
				newState.getBlock().stepSound.getFrequency() * 0.8F);
		if (worldIn.isRemote) {
			return true;
		} else {
			worldIn.setBlockState(target, newState);
			stack.damageItem(1, player);
			return true;
		}
	}

	/**+
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	public boolean isFull3D() {
		return true;
	}

	/**+
	 * Returns the name of the material this tool is made from as it
	 * is declared in EnumToolMaterial (meaning diamond would return
	 * "EMERALD")
	 */
	public String getMaterialName() {
		return this.theToolMaterial.toString();
	}
}