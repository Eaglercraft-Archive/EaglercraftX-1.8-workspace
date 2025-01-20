package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.EnumSkyBlock;
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
public class BlockIce extends BlockBreakable {
	public BlockIce() {
		super(Material.ice, false);
		this.slipperiness = 0.98F;
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, BlockPos blockpos, IBlockState iblockstate,
			TileEntity var5) {
		entityplayer.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
		entityplayer.addExhaustion(0.025F);
		if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(entityplayer)) {
			ItemStack itemstack = this.createStackedBlock(iblockstate);
			if (itemstack != null) {
				spawnAsEntity(world, blockpos, itemstack);
			}
		} else {
			if (world.provider.doesWaterVaporize()) {
				world.setBlockToAir(blockpos);
				return;
			}

			int i = EnchantmentHelper.getFortuneModifier(entityplayer);
			this.dropBlockAsItem(world, blockpos, iblockstate, i);
			Material material = world.getBlockState(blockpos.down()).getBlock().getMaterial();
			if (material.blocksMovement() || material.isLiquid()) {
				world.setBlockState(blockpos, Blocks.flowing_water.getDefaultState());
			}
		}

	}

	/**+
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {
		if (world.getLightFor(EnumSkyBlock.BLOCK, blockpos) > 11 - this.getLightOpacity()) {
			if (world.provider.doesWaterVaporize()) {
				world.setBlockToAir(blockpos);
			} else {
				this.dropBlockAsItem(world, blockpos, world.getBlockState(blockpos), 0);
				world.setBlockState(blockpos, Blocks.water.getDefaultState());
			}
		}
	}

	public int getMobilityFlag() {
		return 0;
	}
}