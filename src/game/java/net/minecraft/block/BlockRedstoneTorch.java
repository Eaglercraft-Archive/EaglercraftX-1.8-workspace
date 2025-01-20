package net.minecraft.block;

import java.util.List;
import java.util.Map;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
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
public class BlockRedstoneTorch extends BlockTorch {
	private static Map<World, List<BlockRedstoneTorch.Toggle>> toggles = Maps.newHashMap();
	private final boolean isOn;

	private boolean isBurnedOut(World worldIn, BlockPos pos, boolean turnOff) {
		if (!toggles.containsKey(worldIn)) {
			toggles.put(worldIn, Lists.newArrayList());
		}

		List list = (List) toggles.get(worldIn);
		if (turnOff) {
			list.add(new BlockRedstoneTorch.Toggle(pos, worldIn.getTotalWorldTime()));
		}

		int i = 0;

		for (int j = 0; j < list.size(); ++j) {
			BlockRedstoneTorch.Toggle blockredstonetorch$toggle = (BlockRedstoneTorch.Toggle) list.get(j);
			if (blockredstonetorch$toggle.pos.equals(pos)) {
				++i;
				if (i >= 8) {
					return true;
				}
			}
		}

		return false;
	}

	protected BlockRedstoneTorch(boolean isOn) {
		this.isOn = isOn;
		this.setTickRandomly(true);
		this.setCreativeTab((CreativeTabs) null);
	}

	/**+
	 * How many world ticks before ticking
	 */
	public int tickRate(World var1) {
		return 2;
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState var3) {
		if (this.isOn) {
			EnumFacing[] facings = EnumFacing._VALUES;
			BlockPos tmp = new BlockPos(0, 0, 0);
			for (int i = 0; i < facings.length; ++i) {
				world.notifyNeighborsOfStateChange(blockpos.offsetEvenFaster(facings[i], tmp), this);
			}
		}

	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState var3) {
		if (this.isOn) {
			EnumFacing[] facings = EnumFacing._VALUES;
			BlockPos tmp = new BlockPos(0, 0, 0);
			for (int i = 0; i < facings.length; ++i) {
				world.notifyNeighborsOfStateChange(blockpos.offsetEvenFaster(facings[i], tmp), this);
			}
		}

	}

	public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState iblockstate, EnumFacing enumfacing) {
		return this.isOn && iblockstate.getValue(FACING) != enumfacing ? 15 : 0;
	}

	private boolean shouldBeOff(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing enumfacing = ((EnumFacing) state.getValue(FACING)).getOpposite();
		return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
	}

	/**+
	 * Called randomly when setTickRandomly is set to true (used by
	 * e.g. crops to grow, etc.)
	 */
	public void randomTick(World var1, BlockPos var2, IBlockState var3, EaglercraftRandom var4) {
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		boolean flag = this.shouldBeOff(world, blockpos, iblockstate);
		List<BlockRedstoneTorch.Toggle> list = toggles.get(world);

		if (list != null) {
			int index = 0;
			while (index < list.size() && world.getTotalWorldTime() - list.get(index).time > 60L) {
				index++;
			}
			if (index > 0) {
				list.subList(0, index).clear();
			}
		}

		if (this.isOn) {
			if (flag) {
				world.setBlockState(blockpos, Blocks.unlit_redstone_torch.getDefaultState().withProperty(FACING,
						iblockstate.getValue(FACING)), 3);
				if (this.isBurnedOut(world, blockpos, true)) {
					world.playSoundEffect((double) ((float) blockpos.getX() + 0.5F),
							(double) ((float) blockpos.getY() + 0.5F), (double) ((float) blockpos.getZ() + 0.5F),
							"random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

					for (int i = 0; i < 5; ++i) {
						double d0 = (double) blockpos.getX() + random.nextDouble() * 0.6D + 0.2D;
						double d1 = (double) blockpos.getY() + random.nextDouble() * 0.6D + 0.2D;
						double d2 = (double) blockpos.getZ() + random.nextDouble() * 0.6D + 0.2D;
						world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
					}

					world.scheduleUpdate(blockpos, world.getBlockState(blockpos).getBlock(), 160);
				}
			}
		} else if (!flag && !this.isBurnedOut(world, blockpos, false)) {
			world.setBlockState(blockpos,
					Blocks.redstone_torch.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		}

	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		if (!this.onNeighborChangeInternal(world, blockpos, iblockstate)) {
			if (this.isOn == this.shouldBeOff(world, blockpos, iblockstate)) {
				world.scheduleUpdate(blockpos, this, this.tickRate(world));
			}

		}
	}

	public int getStrongPower(IBlockAccess iblockaccess, BlockPos blockpos, IBlockState iblockstate,
			EnumFacing enumfacing) {
		return enumfacing == EnumFacing.DOWN ? this.getWeakPower(iblockaccess, blockpos, iblockstate, enumfacing) : 0;
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Item.getItemFromBlock(Blocks.redstone_torch);
	}

	/**+
	 * Can this block provide power. Only wire currently seems to
	 * have this change based on its state.
	 */
	public boolean canProvidePower() {
		return true;
	}

	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		if (this.isOn) {
			double d0 = (double) blockpos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
			double d1 = (double) blockpos.getY() + 0.7D + (random.nextDouble() - 0.5D) * 0.2D;
			double d2 = (double) blockpos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
			EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
			if (enumfacing.getAxis().isHorizontal()) {
				EnumFacing enumfacing1 = enumfacing.getOpposite();
				double d3 = 0.27D;
				d0 += 0.27D * (double) enumfacing1.getFrontOffsetX();
				d1 += 0.22D;
				d2 += 0.27D * (double) enumfacing1.getFrontOffsetZ();
			}

			world.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

	public Item getItem(World var1, BlockPos var2) {
		return Item.getItemFromBlock(Blocks.redstone_torch);
	}

	public boolean isAssociatedBlock(Block block) {
		return block == Blocks.unlit_redstone_torch || block == Blocks.redstone_torch;
	}

	static class Toggle {
		BlockPos pos;
		long time;

		public Toggle(BlockPos pos, long time) {
			this.pos = pos;
			this.time = time;
		}
	}
}