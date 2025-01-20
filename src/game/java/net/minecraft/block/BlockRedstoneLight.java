package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
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
public class BlockRedstoneLight extends Block {
	private final boolean isOn;

	public BlockRedstoneLight(boolean isOn) {
		super(Material.redstoneLight);
		this.isOn = isOn;
		if (isOn) {
			this.setLightLevel(1.0F);
		}

	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState var3) {
		if (!world.isRemote) {
			if (this.isOn && !world.isBlockPowered(blockpos)) {
				world.setBlockState(blockpos, Blocks.redstone_lamp.getDefaultState(), 2);
			} else if (!this.isOn && world.isBlockPowered(blockpos)) {
				world.setBlockState(blockpos, Blocks.lit_redstone_lamp.getDefaultState(), 2);
			}
		}
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState var3, Block var4) {
		if (!world.isRemote) {
			if (this.isOn && !world.isBlockPowered(blockpos)) {
				world.scheduleUpdate(blockpos, this, 4);
			} else if (!this.isOn && world.isBlockPowered(blockpos)) {
				world.setBlockState(blockpos, Blocks.lit_redstone_lamp.getDefaultState(), 2);
			}
		}
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {
		if (!world.isRemote) {
			if (this.isOn && !world.isBlockPowered(blockpos)) {
				world.setBlockState(blockpos, Blocks.redstone_lamp.getDefaultState(), 2);
			}
		}
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Item.getItemFromBlock(Blocks.redstone_lamp);
	}

	public Item getItem(World var1, BlockPos var2) {
		return Item.getItemFromBlock(Blocks.redstone_lamp);
	}

	protected ItemStack createStackedBlock(IBlockState var1) {
		return new ItemStack(Blocks.redstone_lamp);
	}
}