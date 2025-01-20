package net.minecraft.block;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import com.google.common.collect.Lists;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Tuple;
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
public class BlockSponge extends Block {
	public static final PropertyBool WET = PropertyBool.create("wet");

	protected BlockSponge() {
		super(Material.sponge);
		this.setDefaultState(this.blockState.getBaseState().withProperty(WET, Boolean.valueOf(false)));
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**+
	 * Gets the localized name of this block. Used for the
	 * statistics page.
	 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedName() + ".dry.name");
	}

	/**+
	 * Gets the metadata of the item this Block can drop. This
	 * method is called when the block gets destroyed. It returns
	 * the metadata of the dropped item based on the old metadata of
	 * the block.
	 */
	public int damageDropped(IBlockState iblockstate) {
		return ((Boolean) iblockstate.getValue(WET)).booleanValue() ? 1 : 0;
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		this.tryAbsorb(world, blockpos, iblockstate);
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block block) {
		this.tryAbsorb(world, blockpos, iblockstate);
		super.onNeighborBlockChange(world, blockpos, iblockstate, block);
	}

	protected void tryAbsorb(World worldIn, BlockPos pos, IBlockState state) {
		if (!((Boolean) state.getValue(WET)).booleanValue() && this.absorb(worldIn, pos)) {
			worldIn.setBlockState(pos, state.withProperty(WET, Boolean.valueOf(true)), 2);
			worldIn.playAuxSFX(2001, pos, Block.getIdFromBlock(Blocks.water));
		}

	}

	private boolean absorb(World worldIn, BlockPos pos) {
		LinkedList linkedlist = Lists.newLinkedList();
		ArrayList<BlockPos> arraylist = Lists.newArrayList();
		linkedlist.add(new Tuple(pos, Integer.valueOf(0)));
		int i = 0;

		while (!linkedlist.isEmpty()) {
			Tuple tuple = (Tuple) linkedlist.poll();
			BlockPos blockpos = (BlockPos) tuple.getFirst();
			int j = ((Integer) tuple.getSecond()).intValue();

			EnumFacing[] facings = EnumFacing._VALUES;
			for (int k = 0; k < facings.length; ++k) {
				EnumFacing enumfacing = facings[k];
				BlockPos blockpos1 = blockpos.offset(enumfacing);
				if (worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.water) {
					worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 2);
					arraylist.add(blockpos1);
					++i;
					if (j < 6) {
						linkedlist.add(new Tuple(blockpos1, Integer.valueOf(j + 1)));
					}
				}
			}

			if (i > 64) {
				break;
			}
		}

		for (int j = 0, l = arraylist.size(); j < l; ++j) {
			worldIn.notifyNeighborsOfStateChange(arraylist.get(j), Blocks.air);
		}

		return i > 0;
	}

	/**+
	 * returns a list of blocks with the same ID, but different meta
	 * (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(Item item, CreativeTabs var2, List<ItemStack> list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(WET, Boolean.valueOf((i & 1) == 1));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Boolean) iblockstate.getValue(WET)).booleanValue() ? 1 : 0;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { WET });
	}

	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		if (((Boolean) iblockstate.getValue(WET)).booleanValue()) {
			EnumFacing enumfacing = EnumFacing.random(random);
			if (enumfacing != EnumFacing.UP
					&& !World.doesBlockHaveSolidTopSurface(world, blockpos.offset(enumfacing))) {
				double d0 = (double) blockpos.getX();
				double d1 = (double) blockpos.getY();
				double d2 = (double) blockpos.getZ();
				if (enumfacing == EnumFacing.DOWN) {
					d1 = d1 - 0.05D;
					d0 += random.nextDouble();
					d2 += random.nextDouble();
				} else {
					d1 = d1 + random.nextDouble() * 0.8D;
					if (enumfacing.getAxis() == EnumFacing.Axis.X) {
						d2 += random.nextDouble();
						if (enumfacing == EnumFacing.EAST) {
							++d0;
						} else {
							d0 += 0.05D;
						}
					} else {
						d0 += random.nextDouble();
						if (enumfacing == EnumFacing.SOUTH) {
							++d2;
						} else {
							d2 += 0.05D;
						}
					}
				}

				world.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}
}