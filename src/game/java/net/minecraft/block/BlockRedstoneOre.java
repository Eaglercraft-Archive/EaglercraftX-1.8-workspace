package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
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
public class BlockRedstoneOre extends Block {
	private final boolean isOn;

	public BlockRedstoneOre(boolean isOn) {
		super(Material.rock);
		if (isOn) {
			this.setTickRandomly(true);
		}

		this.isOn = isOn;
	}

	/**+
	 * How many world ticks before ticking
	 */
	public int tickRate(World var1) {
		return 30;
	}

	public void onBlockClicked(World world, BlockPos blockpos, EntityPlayer entityplayer) {
		this.activate(world, blockpos);
		super.onBlockClicked(world, blockpos, entityplayer);
	}

	/**+
	 * Triggered whenever an entity collides with this block (enters
	 * into the block)
	 */
	public void onEntityCollidedWithBlock(World world, BlockPos blockpos, Entity entity) {
		this.activate(world, blockpos);
		super.onEntityCollidedWithBlock(world, blockpos, entity);
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer,
			EnumFacing enumfacing, float f, float f1, float f2) {
		this.activate(world, blockpos);
		return super.onBlockActivated(world, blockpos, iblockstate, entityplayer, enumfacing, f, f1, f2);
	}

	private void activate(World worldIn, BlockPos pos) {
		this.spawnParticles(worldIn, pos);
		if (this == Blocks.redstone_ore) {
			worldIn.setBlockState(pos, Blocks.lit_redstone_ore.getDefaultState());
		}

	}

	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {
		if (this == Blocks.lit_redstone_ore) {
			world.setBlockState(blockpos, Blocks.redstone_ore.getDefaultState());
		}

	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Items.redstone;
	}

	/**+
	 * Get the quantity dropped based on the given fortune level
	 */
	public int quantityDroppedWithBonus(int i, EaglercraftRandom random) {
		return this.quantityDropped(random) + random.nextInt(i + 1);
	}

	/**+
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom random) {
		return 4 + random.nextInt(2);
	}

	/**+
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World world, BlockPos blockpos, IBlockState iblockstate, float f, int i) {
		super.dropBlockAsItemWithChance(world, blockpos, iblockstate, f, i);
		if (this.getItemDropped(iblockstate, world.rand, i) != Item.getItemFromBlock(this)) {
			int j = 1 + world.rand.nextInt(5);
			this.dropXpOnBlockBreak(world, blockpos, j);
		}

	}

	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {
		if (this.isOn) {
			this.spawnParticles(world, blockpos);
		}

	}

	private void spawnParticles(World worldIn, BlockPos pos) {
		EaglercraftRandom random = worldIn.rand;
		double d0 = 0.0625D;

		for (int i = 0; i < 6; ++i) {
			double d1 = (double) ((float) pos.getX() + random.nextFloat());
			double d2 = (double) ((float) pos.getY() + random.nextFloat());
			double d3 = (double) ((float) pos.getZ() + random.nextFloat());
			if (i == 0 && !worldIn.getBlockState(pos.up()).getBlock().isOpaqueCube()) {
				d2 = (double) pos.getY() + d0 + 1.0D;
			}

			if (i == 1 && !worldIn.getBlockState(pos.down()).getBlock().isOpaqueCube()) {
				d2 = (double) pos.getY() - d0;
			}

			if (i == 2 && !worldIn.getBlockState(pos.south()).getBlock().isOpaqueCube()) {
				d3 = (double) pos.getZ() + d0 + 1.0D;
			}

			if (i == 3 && !worldIn.getBlockState(pos.north()).getBlock().isOpaqueCube()) {
				d3 = (double) pos.getZ() - d0;
			}

			if (i == 4 && !worldIn.getBlockState(pos.east()).getBlock().isOpaqueCube()) {
				d1 = (double) pos.getX() + d0 + 1.0D;
			}

			if (i == 5 && !worldIn.getBlockState(pos.west()).getBlock().isOpaqueCube()) {
				d1 = (double) pos.getX() - d0;
			}

			if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D
					|| d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
				worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}

	}

	protected ItemStack createStackedBlock(IBlockState var1) {
		return new ItemStack(Blocks.redstone_ore);
	}
}