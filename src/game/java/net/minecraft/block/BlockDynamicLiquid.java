package net.minecraft.block;

import java.util.EnumSet;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
public class BlockDynamicLiquid extends BlockLiquid {
	int adjacentSourceBlocks;

	protected BlockDynamicLiquid(Material materialIn) {
		super(materialIn);
	}

	private void placeStaticBlock(World worldIn, BlockPos pos, IBlockState currentState) {
		worldIn.setBlockState(pos,
				getStaticBlock(this.blockMaterial).getDefaultState().withProperty(LEVEL, currentState.getValue(LEVEL)),
				2);
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		int i = ((Integer) iblockstate.getValue(LEVEL)).intValue();
		byte b0 = 1;
		if (this.blockMaterial == Material.lava && !world.provider.doesWaterVaporize()) {
			b0 = 2;
		}

		int j = this.tickRate(world);
		if (i > 0) {
			int k = -100;
			this.adjacentSourceBlocks = 0;

			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
			for (int m = 0; m < facings.length; ++m) {
				EnumFacing enumfacing = facings[m];
				k = this.checkAdjacentBlock(world, blockpos.offset(enumfacing), k);
			}

			int l = k + b0;
			if (l >= 8 || k < 0) {
				l = -1;
			}

			if (this.getLevel(world, blockpos.up()) >= 0) {
				int i1 = this.getLevel(world, blockpos.up());
				if (i1 >= 8) {
					l = i1;
				} else {
					l = i1 + 8;
				}
			}

			if (this.adjacentSourceBlocks >= 2 && this.blockMaterial == Material.water) {
				IBlockState iblockstate2 = world.getBlockState(blockpos.down());
				if (iblockstate2.getBlock().getMaterial().isSolid()) {
					l = 0;
				} else if (iblockstate2.getBlock().getMaterial() == this.blockMaterial
						&& ((Integer) iblockstate2.getValue(LEVEL)).intValue() == 0) {
					l = 0;
				}
			}

			if (this.blockMaterial == Material.lava && i < 8 && l < 8 && l > i && random.nextInt(4) != 0) {
				j *= 4;
			}

			if (l == i) {
				this.placeStaticBlock(world, blockpos, iblockstate);
			} else {
				i = l;
				if (l < 0) {
					world.setBlockToAir(blockpos);
				} else {
					iblockstate = iblockstate.withProperty(LEVEL, Integer.valueOf(l));
					world.setBlockState(blockpos, iblockstate, 2);
					world.scheduleUpdate(blockpos, this, j);
					world.notifyNeighborsOfStateChange(blockpos, this);
				}
			}
		} else {
			this.placeStaticBlock(world, blockpos, iblockstate);
		}

		IBlockState iblockstate1 = world.getBlockState(blockpos.down());
		if (this.canFlowInto(world, blockpos.down(), iblockstate1)) {
			if (this.blockMaterial == Material.lava
					&& world.getBlockState(blockpos.down()).getBlock().getMaterial() == Material.water) {
				world.setBlockState(blockpos.down(), Blocks.stone.getDefaultState());
				this.triggerMixEffects(world, blockpos.down());
				return;
			}

			if (i >= 8) {
				this.tryFlowInto(world, blockpos.down(), iblockstate1, i);
			} else {
				this.tryFlowInto(world, blockpos.down(), iblockstate1, i + 8);
			}
		} else if (i >= 0 && (i == 0 || this.isBlocked(world, blockpos.down(), iblockstate1))) {
			Set<EnumFacing> set = this.getPossibleFlowDirections(world, blockpos);
			int j1 = i + b0;
			if (i >= 8) {
				j1 = 1;
			}

			if (j1 >= 8) {
				return;
			}

			for (EnumFacing enumfacing1 : set) {
				this.tryFlowInto(world, blockpos.offset(enumfacing1), world.getBlockState(blockpos.offset(enumfacing1)),
						j1);
			}
		}

	}

	private void tryFlowInto(World worldIn, BlockPos pos, IBlockState state, int level) {
		if (this.canFlowInto(worldIn, pos, state)) {
			if (state.getBlock() != Blocks.air) {
				if (this.blockMaterial == Material.lava) {
					this.triggerMixEffects(worldIn, pos);
				} else {
					state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
				}
			}

			worldIn.setBlockState(pos, this.getDefaultState().withProperty(LEVEL, Integer.valueOf(level)), 3);
		}

	}

	private int func_176374_a(World worldIn, BlockPos pos, int distance, EnumFacing calculateFlowCost) {
		int i = 1000;

		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
		for (int l = 0; l < facings.length; ++l) {
			EnumFacing enumfacing = facings[l];
			if (enumfacing != calculateFlowCost) {
				BlockPos blockpos = pos.offset(enumfacing);
				IBlockState iblockstate = worldIn.getBlockState(blockpos);
				if (!this.isBlocked(worldIn, blockpos, iblockstate)
						&& (iblockstate.getBlock().getMaterial() != this.blockMaterial
								|| ((Integer) iblockstate.getValue(LEVEL)).intValue() > 0)) {
					if (!this.isBlocked(worldIn, blockpos.down(), iblockstate)) {
						return distance;
					}

					if (distance < 4) {
						int j = this.func_176374_a(worldIn, blockpos, distance + 1, enumfacing.getOpposite());
						if (j < i) {
							i = j;
						}
					}
				}
			}
		}

		return i;
	}

	/**+
	 * This method returns a Set of EnumFacing
	 */
	private Set<EnumFacing> getPossibleFlowDirections(World worldIn, BlockPos pos) {
		int i = 1000;
		EnumSet enumset = EnumSet.noneOf(EnumFacing.class);

		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
		for (int k = 0; k < facings.length; ++k) {
			EnumFacing enumfacing = facings[k];
			BlockPos blockpos = pos.offset(enumfacing);
			IBlockState iblockstate = worldIn.getBlockState(blockpos);
			if (!this.isBlocked(worldIn, blockpos, iblockstate)
					&& (iblockstate.getBlock().getMaterial() != this.blockMaterial
							|| ((Integer) iblockstate.getValue(LEVEL)).intValue() > 0)) {
				int j;
				if (this.isBlocked(worldIn, blockpos.down(), worldIn.getBlockState(blockpos.down()))) {
					j = this.func_176374_a(worldIn, blockpos, 1, enumfacing.getOpposite());
				} else {
					j = 0;
				}

				if (j < i) {
					enumset.clear();
				}

				if (j <= i) {
					enumset.add(enumfacing);
					i = j;
				}
			}
		}

		return enumset;
	}

	private boolean isBlocked(World worldIn, BlockPos pos, IBlockState state) {
		Block block = worldIn.getBlockState(pos).getBlock();
		return !(block instanceof BlockDoor) && block != Blocks.standing_sign && block != Blocks.ladder
				&& block != Blocks.reeds
						? (block.blockMaterial == Material.portal ? true : block.blockMaterial.blocksMovement())
						: true;
	}

	protected int checkAdjacentBlock(World worldIn, BlockPos pos, int currentMinLevel) {
		int i = this.getLevel(worldIn, pos);
		if (i < 0) {
			return currentMinLevel;
		} else {
			if (i == 0) {
				++this.adjacentSourceBlocks;
			}

			if (i >= 8) {
				i = 0;
			}

			return currentMinLevel >= 0 && i >= currentMinLevel ? currentMinLevel : i;
		}
	}

	private boolean canFlowInto(World worldIn, BlockPos pos, IBlockState state) {
		Material material = state.getBlock().getMaterial();
		return material != this.blockMaterial && material != Material.lava && !this.isBlocked(worldIn, pos, state);
	}

	public void onBlockAdded(World world, BlockPos blockpos, IBlockState iblockstate) {
		if (!this.checkForMixing(world, blockpos, iblockstate)) {
			world.scheduleUpdate(blockpos, this, this.tickRate(world));
		}

	}
}