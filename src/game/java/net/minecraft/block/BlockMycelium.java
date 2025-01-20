package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
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
public class BlockMycelium extends Block {
	public static final PropertyBool SNOWY = PropertyBool.create("snowy");

	protected BlockMycelium() {
		super(Material.grass, MapColor.purpleColor);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, Boolean.valueOf(false)));
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		Block block = iblockaccess.getBlockState(blockpos.up()).getBlock();
		return iblockstate.withProperty(SNOWY, Boolean.valueOf(block == Blocks.snow || block == Blocks.snow_layer));
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom random) {
		if (!world.isRemote) {
			if (world.getLightFromNeighbors(blockpos.up()) < 4
					&& world.getBlockState(blockpos.up()).getBlock().getLightOpacity() > 2) {
				world.setBlockState(blockpos,
						Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
			} else {
				if (world.getLightFromNeighbors(blockpos.up()) >= 9) {
					for (int i = 0; i < 4; ++i) {
						BlockPos blockpos1 = blockpos.add(random.nextInt(3) - 1, random.nextInt(5) - 3,
								random.nextInt(3) - 1);
						IBlockState iblockstate = world.getBlockState(blockpos1);
						Block block = world.getBlockState(blockpos1.up()).getBlock();
						if (iblockstate.getBlock() == Blocks.dirt
								&& iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT
								&& world.getLightFromNeighbors(blockpos1.up()) >= 4 && block.getLightOpacity() <= 2) {
							world.setBlockState(blockpos1, this.getDefaultState());
						}
					}
				}
			}
		}
	}

	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		super.randomDisplayTick(world, blockpos, iblockstate, random);
		if (random.nextInt(10) == 0) {
			world.spawnParticle(EnumParticleTypes.TOWN_AURA, (double) ((float) blockpos.getX() + random.nextFloat()),
					(double) ((float) blockpos.getY() + 1.1F), (double) ((float) blockpos.getZ() + random.nextFloat()),
					0.0D, 0.0D, 0.0D, new int[0]);
		}

	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom random, int i) {
		return Blocks.dirt.getItemDropped(
				Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), random, i);
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState var1) {
		return 0;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { SNOWY });
	}
}